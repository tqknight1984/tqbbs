package com.poly.controller.user;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.poly.bean.Message;
import com.poly.bean.TbArticle;
import com.poly.bean.User;
import com.poly.controller.BaseController;
import com.poly.redis.RedisDao;
import com.poly.util.ArticleUtil;
import com.poly.util.Base64ImageUtil;
import com.poly.util.ContentUtil;
import com.poly.util.ImageHelper;
import com.poly.util.MD5Util;
import com.poly.util.MyConfig;
import com.poly.util.UserUtil;
@Controller("userCenterController")
@RequestMapping("/userCenter")
public class UserCenterController extends BaseController{
	/**
	 * 个人中心
	 * @param session
	 * @param page
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/mine/{page}.htm")
	public ModelAndView mine(HttpSession session,@PathVariable("page")Integer page,HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("user/mine.ftl");
		if(page==null){
			page=1;
		}
//		User user = (User) session.getAttribute("user");
		User user = UserUtil.getUserFromCookie(request);
		Integer user_id = user.getUser_id();
		Set<String> commentIds = RedisDao.getCommeentsByArticleUserId(user_id);
		List<String> cids = new ArrayList<String>();
		for(String id:commentIds){
			cids.add(id);
		}
		int p = cids.size();
		List<Map<String,String>> comments = new ArrayList<Map<String,String>>();
		int pages = 0;
		if(p>0){
			if(p%articlePageSize==0){
				pages = p/articlePageSize;
			}else{
				pages = p/articlePageSize+1;
			}
			if(page>=pages){
				page=pages;
			}
			int start = (page-1)*articlePageSize;
			int end = start+articlePageSize-1;
			if(end>=p){
				end = p-1;
			}
			for(int i=start;i<=end;i++){
				String id = cids.get(i);
				Map<String,String> commentInfo = RedisDao.getCommentInfoByCommengId(id);
				if(commentInfo==null||commentInfo.size()<=0){
					continue;
				}
				String article_id = commentInfo.get("article_id");
				Map<String,String> articleInfo = RedisDao.getArticleWithoutContent(article_id);
				String article_title = articleInfo.get("article_title");
				commentInfo.put("article_title",article_title);
				commentInfo.put("article_id",article_id);
				
				String comment_user = commentInfo.get("user_id");
				Map<String,String> commentUserInfo = RedisDao.getUserByUserId(comment_user);
				String username = commentUserInfo.get("user_name");
				commentInfo.put("comment_user_name",username);
				comments.add(commentInfo);
			}
		}
		int articleSize = RedisDao.getArticleSizeByUserId(user_id);
		int eliteSize = RedisDao.getEliteArticleIdListByUserId(user_id)==null?0:RedisDao.getEliteArticleIdListByUserId(user_id).size();
		mv.addObject("articleSize", articleSize);
		mv.addObject("eliteSize", eliteSize);
		mv.addObject("comments", comments);
		mv.addObject("commentSize", p);
		mv.addObject("page", page.intValue());
		mv.addObject("pages", pages);
		return mv;
	}
	/**
	 * 我的帖子
	 * @param session
	 * @param page
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/myArticles/{page}.htm")
	public ModelAndView myArticles(HttpSession session,@PathVariable("page")Integer page,HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("user/myArticles.ftl");
		if(page==null){
			page=1;
		}
//		User user = (User) session.getAttribute("user");
		User user = UserUtil.getUserFromCookie(request);
		Integer user_id = user.getUser_id();
		Set<String> articleIds = RedisDao.getArticleIdListByUserId(user_id);
		if(articleIds==null){
			articleIds = new HashSet<String>();
		}
		List<String> aids = new ArrayList<String>();
		for(String id:articleIds){
			aids.add(id);
		}
		int p = aids.size();
		int pages = 0;
		List<Map<String,String>> articles = new ArrayList<Map<String,String>>();
		if(p>0){
			if(p%articlePageSize==0){
				pages = p/articlePageSize;
			}else{
				pages = p/articlePageSize+1;
			}
			if(page>=pages){
				page=pages;
			}
			int start = (page-1)*articlePageSize;
			int end = start+articlePageSize-1;
			if(end>=p){
				end = p-1;
			}
			for(int i=start;i<=end;i++){
				String id = aids.get(i);
				Map<String,String> articleInfo = RedisDao.article_getDetail(Integer.parseInt(id));
				String plate_id = articleInfo.get("plate_id");
				String content = articleInfo.get("article_content");
				content = ContentUtil.removeTag(content, "img");
				Map<String,String> plateInfo = RedisDao.getPlateInfoById(plate_id);
				articleInfo.put("plate_name", plateInfo.get("plate_name"));
				articleInfo.put("plate_id", plateInfo.get("plate_id"));
				articleInfo.put("article_content", content);
				String add_time_str = articleInfo.get("add_time");
				if(!StringUtils.isEmpty(add_time_str)){
					Date add_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(add_time_str);
					long between = System.currentTimeMillis()-add_date.getTime();
					String time_str = "";
					if(between<60*60*1000){
						time_str = ((Double)Math.floor(between/60/1000)).intValue()+"分钟前";
					}else if(between<24*60*60*1000){
						time_str = Math.floor(between/60/60/1000)+"小时前";
					}else{
						time_str = new SimpleDateFormat("yyyy-MM-dd").format(add_date);
					}
					articleInfo.put("add_time", time_str);
				}
				articles.add(articleInfo);
			}
		}
		mv.addObject("articles", articles);
		mv.addObject("page", page.intValue());
		mv.addObject("pages", pages);
		return mv;
	}
	@RequestMapping("/myElite/{page}.htm")
	public ModelAndView myElite(HttpSession session,@PathVariable("page")Integer page,HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("user/myElite.ftl");
		if(page==null){
			page=1;
		}
//		User user = (User) session.getAttribute("user");
		User user = UserUtil.getUserFromCookie(request);
		Integer user_id = user.getUser_id();
		Set<String> eliteIds = RedisDao.getEliteArticleIdListByUserId(user_id);
		if(eliteIds==null){
			eliteIds = new HashSet<String>();
		}
		List<String> aids = new ArrayList<String>();
		for(String id:eliteIds){
			aids.add(id);
		}
		int p = aids.size();
		int pages = 0;
		List<Map<String,String>> articles = new ArrayList<Map<String,String>>();
		if(p>0){
			if(p%articlePageSize==0){
				pages = p/articlePageSize;
			}else{
				pages = p/articlePageSize+1;
			}
			if(page>=pages){
				page=pages;
			}
			int start = (page-1)*articlePageSize;
			int end = start+articlePageSize-1;
			if(end>=p){
				end = p-1;
			}
			for(int i=start;i<=end;i++){
				String id = aids.get(i);
				Map<String,String> articleInfo = RedisDao.article_getDetail(Integer.parseInt(id));
				String plate_id = articleInfo.get("plate_id");
				String content = articleInfo.get("article_content");
				content = ContentUtil.removeTag(content, "img");
				Map<String,String> plateInfo = RedisDao.getPlateInfoById(plate_id);
				articleInfo.put("plate_name", plateInfo.get("plate_name"));
				articleInfo.put("plate_id", plateInfo.get("plate_id"));
				articleInfo.put("article_content", content);
				String add_time_str = articleInfo.get("add_time");
				if(!StringUtils.isEmpty(add_time_str)){
					Date add_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(add_time_str);
					long between = System.currentTimeMillis()-add_date.getTime();
					String time_str = "";
					if(between<60*60*1000){
						time_str = ((Double)Math.floor(between/60/1000)).intValue()+"分钟前";
					}else if(between<24*60*60*1000){
						time_str = Math.floor(between/60/60/1000)+"小时前";
					}else{
						time_str = new SimpleDateFormat("yyyy-MM-dd").format(add_date);
					}
					articleInfo.put("add_time", time_str);
				}
				articles.add(articleInfo);
			}
		}
		mv.addObject("articles", articles);
		mv.addObject("page", page.intValue());
		mv.addObject("pages", pages);
		return mv;
	}
	/**
	 * 我的回复
	 * @param session
	 * @param page
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/myComments/{page}.htm")
	public ModelAndView myComments(HttpSession session,@PathVariable("page")Integer page,HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("user/myComments.ftl");
		if(page==null){
			page=1;
		}
//		User user = (User) session.getAttribute("user");
		User user = UserUtil.getUserFromCookie(request);
		Integer user_id = user.getUser_id();
		Set<String> commentsIds = RedisDao.getCommentsListByUserId(user_id);
		List<String> cids = new ArrayList<String>();
		for(String id:commentsIds){
			cids.add(id);
		}
		int p = cids.size();
		int pages = 0;
		List<Map<String,String>> comments = new ArrayList<Map<String,String>>();
		if(p>0){
			if(p%articlePageSize==0){
				pages = p/articlePageSize;
			}else{
				pages = p/articlePageSize+1;
			}
			if(page>=pages){
				page=pages;
			}
			int start = (page-1)*articlePageSize;
			int end = start+articlePageSize-1;
			if(end>=p){
				end = p-1;
			}
			for(int i=start;i<=end;i++){
				String id = cids.get(i);
				Map<String,String> commentInfo = RedisDao.getCommentInfoByCommengId(id);
				if(commentInfo==null||commentInfo.size()<=0){
					continue;
				}
				String article_id = commentInfo.get("article_id");
				Map<String,String> articleInfo = RedisDao.getArticleWithoutContent(article_id);
				String article_title = articleInfo.get("article_title");
				commentInfo.put("article_title",article_title);
				String article_user_id = articleInfo.get("user_id");
				Map<String,String> articleUserInfo = RedisDao.getUserByUserId(article_user_id);
				String article_user_name = articleUserInfo.get("user_name");
				commentInfo.put("article_user_name",article_user_name);
				String plate_id = articleInfo.get("plate_id");
				Map<String,String> plateInfo = RedisDao.getPlateInfoById(plate_id);
				commentInfo.put("plate_name", plateInfo.get("plate_name"));
				commentInfo.put("plate_id", plateInfo.get("plate_id"));
				Map<String,String> last = RedisDao.getLastUserByArticleId(article_id);
				commentInfo.put("last_user_name", last.get("last_user_name"));
				commentInfo.put("last_user_id", last.get("last_user_id"));
				commentInfo.put("last_time", last.get("last_time"));
				comments.add(commentInfo);
			}
		}
		mv.addObject("commentSize", p);
		mv.addObject("comments", comments);
		mv.addObject("page", page.intValue());
		mv.addObject("pages", pages);
		return mv;
	}
	/**
	 * 进入修改密码界面
	 * @return
	 */
	@RequestMapping("/toChangePassword.htm")
	public ModelAndView toChangePassword(){
		ModelAndView mv = new ModelAndView("user/toChangePassword.ftl");
		return mv;
	}
	/**
	 * 修改密码
	 * @param password
	 * @param newPassword
	 * @param password2
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/changePassword")
	public @ResponseBody Message changePassword(String password,String newPassword,String password2,HttpSession session,HttpServletRequest request) throws Exception{
		Message msg = new Message();
//		User user = (User) session.getAttribute("user");
		User user = UserUtil.getUserFromCookie(request);
		if(user==null){
			msg.setCode(errorCode);
			msg.setMsg("当前没有登录的用户，请登录后再修改!");
			return msg;
		}
		if(StringUtils.isEmpty(password)){
			msg.setCode(errorCode);
			msg.setMsg("原密码不能为空!");
			return msg;
		}
		if(!MD5Util.string2MD5(password).equals(user.getUser_password())){
			msg.setCode(errorCode);
			msg.setMsg("密码错误!");
			return msg;
		}
		if(StringUtils.isEmpty(newPassword)){
			msg.setCode(errorCode);
			msg.setMsg("新密码不能为空!");
			return msg;
		}
		if(newPassword.length()<6){
			msg.setCode(errorCode);
			msg.setMsg("新密码长度不能少于6位!");
			return msg;
		}
		if(StringUtils.isEmpty(password2)||!newPassword.equals(password2)){
			msg.setCode(errorCode);
			msg.setMsg("两次密码不一致!");
			return msg;
		}
		user.setUser_password(MD5Util.string2MD5(newPassword));
		session.setAttribute("user", user);
		int id = RedisDao.saveUser(user);
		RedisDao.addUserUpdateList(id);
		msg.setCode(successCode);
		msg.setMsg("密码修改成功!");
		return msg;
		
	}
	/**
	 * 进入修改头像界面
	 * @return
	 */
	@RequestMapping("/toChangePhoto.htm")
	public ModelAndView toChangePhoto(){
		ModelAndView mv = new ModelAndView("user/toChangePhoto.ftl");
		return mv;
	}
	/**
	 * 修改头像
	 * @param password
	 * @param newPassword
	 * @param password2
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/changePhoto")
	public @ResponseBody String changePhoto(HttpSession session,MultipartHttpServletRequest multipartRequest) throws Exception{
		FileOutputStream fileOutputStream = null;
//		User user = (User) session.getAttribute("user");
		User user = UserUtil.getUserFromCookie(multipartRequest);
		if(user==null){
			return "noLogin";
		}
	    MultipartFile multipartFile = multipartRequest.getFile("upfile");
	    String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        String filePath=MyConfig.img_savePath+"user_photo/"+user.getUser_id()+"/";
        File newFile = new File(filePath);
		if(!newFile.exists()){
			newFile.mkdirs();
		}
		String name = "photo"+System.currentTimeMillis();
		String fileName = filePath +name+suffix;
		String photo_url = "user_photo/"+user.getUser_id()+"/"+name+suffix;
		File writeFile = new File(fileName);
	    try {
	    	fileOutputStream = new FileOutputStream(writeFile);
	    	fileOutputStream.write(multipartFile.getBytes());
			System.out.println("image name:"+multipartFile.getOriginalFilename());
		    System.out.println("image type:"+multipartFile.getContentType());
		    fileOutputStream.flush();
	    	if(multipartFile.getSize()>100*1024){
	    		MarvinImage image = MarvinImageIO.loadImage(fileName);
	    		MarvinImage backupImage = image.clone();  //克隆图像，确保对原图不做修改  
	    	    backupImage.resize(200, 200);   //设置新宽度和高度  
	    	    String thumbnailFilename = filePath +name+"_thumbnail"+suffix;
	    	    MarvinImageIO.saveImage(backupImage, thumbnailFilename); 
	    	    photo_url = "user_photo/"+user.getUser_id()+"/"+name+"_thumbnail"+suffix;
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			fileOutputStream.close();
		}
	    user.setUser_photo(photo_url);
		session.setAttribute("user", user);
		int id = RedisDao.saveUser(user);
		RedisDao.addUserUpdateList(id);
		return "success";
		
	}
	/**
	 * 进入修改头像界面
	 * @return
	 */
	@RequestMapping("/toChangeNickname.htm")
	public ModelAndView toChangeNickname(){
		ModelAndView mv = new ModelAndView("user/toChangeNicknameo.ftl");
		return mv;
	}
	/**
	 * 修改头像
	 * @param password
	 * @param newPassword
	 * @param password2
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/changeNickname")
	public @ResponseBody Message changeNickname(HttpSession session,HttpServletRequest request,String nickname) throws Exception{
		Message msg = new Message();
//		User user = (User) session.getAttribute("user");
		User user = UserUtil.getUserFromCookie(request);
		if(user==null){
			msg.setCode(errorCode);
			msg.setMsg("当前没有登录的用户，请登录后再修改!");
			return msg;
		}
		user.setUser_nickname(nickname);
		int id = RedisDao.saveUser(user);
		RedisDao.addUserUpdateList(id);
		msg.setCode(successCode);
		msg.setMsg("修改成功");
		return msg;
	}
	/**
	 *删除帖子
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/deleteArticle")
	public @ResponseBody Message deleteArticle(int article_id,HttpServletRequest request) throws Exception{
		Message msg = new Message();
		User user = UserUtil.getUserFromCookie(request);
		Map<String,String> amap = RedisDao.getArticleInfoById(article_id);
		boolean flag = false;
		if(user!=null){
			if(amap.get("user_id").equals(user.getUser_id().toString())){
				flag = true;
			}else{
				List<Map<String,String>> userPlates = RedisDao.getPlateListByUserIdForMaster(user.getUser_id());
				if(userPlates==null||userPlates.size()==0){
					msg.setCode(errorCode);
					msg.setMsg("无效请求");
					return msg;
				}
				String plate_id = amap.get("plate_id");
				for(Map<String,String> plate : userPlates){
					String pid = plate.get("plate_id");
					if(plate_id.equals(pid)){
						flag = true;
						break;
					}
				}
			}
		}else{
			msg.setCode(errorCode);
			msg.setMsg("无效请求");
			return msg;
		}
		if(!flag){
			msg.setCode(errorCode);
			msg.setMsg("此文章不在你的管理范围之内!");
			return msg;
		}
		try {
			RedisDao.article_delete(article_id);
		} catch (IOException e) {
			msg.setCode(errorCode);
			msg.setMsg("删除失败");
			e.printStackTrace();
			return msg;
		}
		msg.setCode(successCode);
		msg.setMsg("删除成功");
		return msg;
	}
	/**
	 * 进入添加精华页面
	 * @return
	 */
	@RequestMapping("/toAddElite.htm")
	public ModelAndView toAddElite(){
		ModelAndView mv = new ModelAndView("user/addElite.ftl");
		return mv;
	}
	/**
	 * 解析文章url
	 * @return
	 */
	@RequestMapping("/parseUrlForAddElite")
	public @ResponseBody Map<String,String> parseUrlForAddElite(String url,HttpSession session){
		Map<String,String> map = new HashMap<String,String>();
		int id = ArticleUtil.getArticleIdFromUrl(url);
		if(id==-1){
			map.put("code", errorCode);
			map.put("msg", "链接无效!");
			return map;
		}
		Map<String,String> amap = RedisDao.getArticleInfoById(id);
		if(amap==null||amap.size()<=0){
			map.put("code", errorCode);
			map.put("msg", "链接无效!");
			return map;
		}
		String plate_id = amap.get("plate_id");
		if(StringUtils.isEmpty(plate_id)){
			map.put("code", errorCode);
			map.put("msg", "链接无效!");
			return map;
		}
		List<Map<String,String>> userPlates = (List<Map<String, String>>) session.getAttribute("userPlates");
		boolean flag = false;
		for(Map<String,String> plate : userPlates){
			String pid = plate.get("plate_id");
			if(plate_id.equals(pid)){
				flag = true;
				break;
			}
		}
		if(!flag){
			map.put("code", errorCode);
			map.put("msg", "此文章不在你的管理范围之内!");
			return map;
		}
//		if("1".equals(amap.get("is_elite"))){
//			map.put("code", errorCode);
//			map.put("msg", "此文章已是精华文章!");
//			return map;
//		}
		map.put("code", successCode);
		map.put("msg", "解析成功");
		map.put("title", amap.get("article_title"));
		map.put("article_id", amap.get("article_id"));
		String is_elite = amap.get("is_elite");
		if(StringUtils.isEmpty(is_elite)){
			is_elite = "0";
		}
		Map<String,String> umap = RedisDao.getUserByUserId(amap.get("user_id"));
		map.put("author", umap.get("user_name"));
		map.put("add_time", amap.get("add_time"));
		map.put("is_elite",is_elite);
		return map;
	}
	/**
	 * 解析文章精华操作
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/makeElite")
	public @ResponseBody Map<String,String> makeElite(int id,int type,HttpSession session,HttpServletRequest request) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		if(id==0||type==0){
			map.put("code", errorCode);
			map.put("msg", "无效请求!");
			return map;
		}
		Map<String,String> amap = RedisDao.getArticleInfoById(id);
		if(amap==null||amap.size()<=0){
			map.put("code", errorCode);
			map.put("msg", "无效请求!");
			return map;
		}
		String plate_id = amap.get("plate_id");
		if(StringUtils.isEmpty(plate_id)){
			map.put("code", errorCode);
			map.put("msg", "无效请求!");
			return map;
		}
//		User user = (User) session.getAttribute("user");
		User user = UserUtil.getUserFromCookie(request);
		boolean flag = false;
		if(user!=null){
			List<Map<String,String>> userPlates = RedisDao.getPlateListByUserIdForMaster(user.getUser_id());
			if(userPlates==null||userPlates.size()==0){
				map.put("code", errorCode);
				map.put("msg", "无效请求!");
				return map;
			}
			for(Map<String,String> plate : userPlates){
				String pid = plate.get("plate_id");
				if(plate_id.equals(pid)){
					flag = true;
					break;
				}
			}
		}else{
			map.put("code", errorCode);
			map.put("msg", "无效请求!");
			return map;
		}
		if(!flag){
			map.put("code", errorCode);
			map.put("msg", "此文章不在你的管理范围之内!");
			return map;
		}
		if(type==1&&"1".equals(amap.get("is_elite"))){
			map.put("code", errorCode);
			map.put("msg", "此文章已是精华文章!");
			return map;
		}
		if(type==2&&"0".equals(amap.get("is_elite"))){
			map.put("code", errorCode);
			map.put("msg", "此文章不是精华文章!");
			return map;
		}
		int back = RedisDao.makeElite(id,type);
		if(back==-1){
			map.put("code", errorCode);
			map.put("msg", "操作失败!");
			return map;
		}
		map.put("code", successCode);
		map.put("msg", "操作成功");
		return map;
	}
	/**
	 * 文章精华操作
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/makeTop")
	public @ResponseBody Map<String,String> makeTop(int id,int type,HttpSession session,HttpServletRequest request) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		if(id==0||type==0){
			map.put("code", errorCode);
			map.put("msg", "无效请求!");
			return map;
		}
		Map<String,String> amap = RedisDao.getArticleInfoById(id);
		if(amap==null||amap.size()<=0){
			map.put("code", errorCode);
			map.put("msg", "无效请求!");
			return map;
		}
		String plate_id = amap.get("plate_id");
		if(StringUtils.isEmpty(plate_id)){
			map.put("code", errorCode);
			map.put("msg", "无效请求!");
			return map;
		}
//		User user = (User) session.getAttribute("user");
		User user = UserUtil.getUserFromCookie(request);
		boolean flag = false;
		if(user!=null){
			List<Map<String,String>> userPlates = RedisDao.getPlateListByUserIdForMaster(user.getUser_id());
			if(userPlates==null||userPlates.size()==0){
				map.put("code", errorCode);
				map.put("msg", "无效请求!");
				return map;
			}
			for(Map<String,String> plate : userPlates){
				String pid = plate.get("plate_id");
				if(plate_id.equals(pid)){
					flag = true;
					break;
				}
			}
		}else{
			map.put("code", errorCode);
			map.put("msg", "无效请求!");
			return map;
		}
		if(!flag){
			map.put("code", errorCode);
			map.put("msg", "此文章不在你的管理范围之内!");
			return map;
		}
		if(type==2&&"0".equals(amap.get("article_order"))){
			map.put("code", errorCode);
			map.put("msg", "此文章没有置顶!");
			return map;
		}
		int back = RedisDao.makeTop(id,type);
		if(back==-1){
			map.put("code", errorCode);
			map.put("msg", "操作失败!");
			return map;
		}
		map.put("code", successCode);
		map.put("msg", "操作成功");
		return map;
	}
	/**
	 * 模糊查询用户列表
	 * @return
	 */
	@RequestMapping("/getUserListByIndex")
	public @ResponseBody List<User> getUserListByIndex(String index){
		List<User> list = RedisDao.getUserByUsernameIndex(index);
		return list;
	}
}
