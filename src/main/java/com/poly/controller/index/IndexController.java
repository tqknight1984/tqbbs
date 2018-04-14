package com.poly.controller.index;

import com.poly.bean.User;
import com.poly.controller.BaseController;
import com.poly.controller.user.UserCenterController;
import com.poly.redis.RedisDao;
import com.poly.service.hotImg.HotImgService;
import com.poly.service.user.UserService;
import com.poly.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("indexController")
@RequestMapping("/index")
public class IndexController extends BaseController{
	@Resource(name="userCenterController")
	UserCenterController userCenterController;
	@Resource(name="userService")
	private UserService userService;

	@Autowired
	private HotImgService hotImgService;

	/**
	 * 进入主页
	 * @return
	 */
	@RequestMapping("/index.htm")
	public ModelAndView toindex(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("bbs/index_bbs.ftl");
		User user=new User();
		List<Map<String,Object>> jinghuaList=new ArrayList<Map<String,Object>>();
		try {
			user = UserUtil.getUserFromCookie(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(user==null||user.getRole()==0){
//			   plateList=RedisDao.getallplate();
			   jinghuaList=RedisDao.getOutjinghuaArticles();
			}
		else{
			jinghuaList=RedisDao.getjinghuaArticles();
		}
		List<Map<String,String>> urlList=new ArrayList<Map<String,String>>();
		List<Map<String,Object>> list=RedisDao.getallplateAndarticles();
		List<Map<String,Object>> hotList=RedisDao.gethotArticles();
		List<Map<String,String>> userList = userService.getUserListOrderByArticleSize();
		mv.addObject("hotImgs", hotImgService.visibles());
		mv.addObject("articleLists",list);
		mv.addObject("hotarticleLists",hotList);
		mv.addObject("jinghuaLists",jinghuaList);
		Map<String,String> map=new HashMap<String, String>();
		map.put("url", "tqbbs/index/index.htm");
		map.put("urlname", "首页");
		urlList.add(map);
		mv.addObject("urlList",urlList);
		mv.addObject("userList",userList);
		return mv;
	}
	/**
	 * 学习交流
	 * @return
	 */
	@RequestMapping("/learnindex.htm")
	public ModelAndView tolearnindex(HttpServletRequest request){
		System.out.println("--------------->>learnindex.htm");
		ModelAndView mv = new ModelAndView("bbs/bbs_learn.ftl");
		User user=new User();
		List<Map<String,Object>> jinghuaList=new ArrayList<Map<String,Object>>();
		try {
			user = UserUtil.getUserFromCookie(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("学习交流：");
		System.out.println(System.currentTimeMillis());
		if(user==null||user.getRole()==0){
//			   plateList=RedisDao.getallplate();
			   jinghuaList=RedisDao.getOutjinghuaArticles();
			}
		else{
			jinghuaList=RedisDao.getjinghuaArticles();
		}
		List<Map<String,String>> urlList=new ArrayList<Map<String,String>>();
		List<Map<String,Object>> list=RedisDao.getallplateAndarticlesForlearn();
		System.out.println("--------------->>list.size:"+list.size());
		List<Map<String,String>> userList = userService.getUserListOrderByArticleSize();
		System.out.println(System.currentTimeMillis());
		mv.addObject("articleLists",list);
		mv.addObject("jinghuaLists",jinghuaList);
		Map<String,String> map=new HashMap<String, String>();
		map.put("url", "tqbbs/index/learnindex.htm");
		map.put("urlname", "学习交流");
		urlList.add(map);
		mv.addObject("urlList",urlList);
		mv.addObject("userList",userList);
		return mv;
	}
	/**
	 * 内部交流
	 * @return
	 */
	@RequestMapping("/inlearnindex.htm")
	public ModelAndView toinlearnindex(){
		ModelAndView mv = new ModelAndView("bbs/bbs_inlearn.ftl");
		List<Map<String,String>> urlList=new ArrayList<Map<String,String>>();
		System.out.println("项目日志：");
		System.out.println(System.currentTimeMillis());
		List<Map<String,Object>> jinghuaList=RedisDao.getjinghuaArticles();
		System.out.println(System.currentTimeMillis());
		List<Map<String,Object>> list=RedisDao.getallplateAndarticlesForInlearn();
		List<Map<String,String>> userList = userService.getUserListOrderByArticleSize();
		System.out.println(System.currentTimeMillis());
		mv.addObject("articleLists",list);
		mv.addObject("jinghuaLists",jinghuaList);
		Map<String,String> map=new HashMap<String, String>();
		map.put("url", "tqbbs/index/inlearnindex.htm");
		map.put("urlname", "项目日志");
		urlList.add(map);
		mv.addObject("urlList",urlList);
		mv.addObject("userList",userList);
		return mv;
	}
	@RequestMapping("/tobbsList/{plateid}/{page}.htm")
	public ModelAndView tobbsList(@PathVariable("plateid")String plateid,@PathVariable("page")Integer page,HttpServletRequest request){
//		User user = (User) session.getAttribute("user");
		User user=new User();
		try {
			user = UserUtil.getUserFromCookie(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(plateid);
		List<Map<String,String>> urlList=new ArrayList<Map<String,String>>();
		if(page==null){
			page=1;
		}
		ModelAndView mv = new ModelAndView("bbs/bbs_list.ftl");
		List<String> cidsList=new ArrayList<String>();
		List<Map<String,String>> plateList=new ArrayList<Map<String,String>>();
		plateList=RedisDao.getallplate();
	/*	if(user==null||user.getRole()==0){
//		   plateList=RedisDao.getallplate();
		   cidsList=RedisDao.getallarticlesidByplateid(plateid);
		}
		else{*/
//			plateList=RedisDao.getallAndinplate();
			cidsList=RedisDao.getallAndinarticlesidByplateid(plateid);
//		}
		int pages = 1;
		int p = cidsList.size();
		int if_in=RedisDao.ifInPalate(plateid);
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
			List<Map<String,String>> list=RedisDao.getarticlesbyplateid(cidsList,start,end);
			if(plateid.equals("0")){
				mv.addObject("platename","全部帖子");	
				mv.addObject("plateid","0");	
				Map<String,String> map1=getMap(if_in);
				urlList.add(map1);
				Map<String,String> map=new HashMap<String, String>();
				map.put("url", "tqbbs/index/tobbsList/0/1.htm");
				map.put("urlname", "全部帖子");
				urlList.add(map);
				mv.addObject("urlList",urlList);
			}
			else{
				String platename=RedisDao.getplatenamebyid(plateid);
				mv.addObject("platename",platename);	
				mv.addObject("plateid",plateid);
				Map<String,String> map1=getMap(if_in);
				urlList.add(map1);
				Map<String,String> map=new HashMap<String, String>();
				map.put("url", "tqbbs/index/tobbsList/"+plateid+"/1.htm");
				map.put("urlname",platename);
				urlList.add(map);
				mv.addObject("urlList",urlList);
			}
			mv.addObject("page", page.intValue());
			mv.addObject("pages",pages);
			mv.addObject("articleLists",list);
			mv.addObject("plateList",plateList);
		}
		else{
			if(plateid.equals("0")){
				mv.addObject("platename","全部帖子");	
				mv.addObject("plateid","0");	
				Map<String,String> map1=getMap(if_in);
				urlList.add(map1);
				Map<String,String> map=new HashMap<String, String>();
				map.put("url", "tqbbs/index/tobbsList/0/1.htm");
				map.put("urlname", "全部帖子");
				urlList.add(map);
				mv.addObject("urlList",urlList);
			}
			else{
				String platename=RedisDao.getplatenamebyid(plateid);
				mv.addObject("platename",platename);		
				mv.addObject("plateid",plateid);	
				Map<String,String> map1=getMap(if_in);
				urlList.add(map1);
				Map<String,String> map=new HashMap<String, String>();
				map.put("url", "tqbbs/index/tobbsList/"+plateid+"/1.htm");
				map.put("urlname",platename);
				urlList.add(map);
				mv.addObject("urlList",urlList);
			}
			mv.addObject("page", page.intValue());
			mv.addObject("pages",pages);
			mv.addObject("articleLists",new ArrayList<Map<String,String>>());
			mv.addObject("plateList",plateList);
		}
		return mv;
	}
	@RequestMapping("/tobbsUserList/{plateid}/{userid}/{page}.htm")
	public ModelAndView tobbsUserList(HttpSession session,HttpServletRequest request,@PathVariable("plateid")String plateid,@PathVariable("userid")String userid,@PathVariable("page")Integer page){
		List<Map<String,String>> urlList=new ArrayList<Map<String,String>>();
//		User user = (User) session.getAttribute("user");
		User user=new User();
		try {
			user = UserUtil.getUserFromCookie(request);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(user!=null&&user.getUser_id()==Integer.parseInt(userid)){
			try {
				return userCenterController.mine(session, 1,request);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(plateid);
		if(page==null){
			page=1;
		}
		ModelAndView mv = new ModelAndView("bbs/bbs_userlist.ftl");
		List<String> cidsList=new ArrayList<String>();
		List<Map<String,String>> plateList=new ArrayList<Map<String,String>>();
		 plateList=RedisDao.getallplate();
		if(user==null||user.getRole()==0){
//			   plateList=RedisDao.getallplate();
			   cidsList=RedisDao.getallarticlesidByplateidAnduserid(plateid,userid);
			}
		else{
//				plateList=RedisDao.getallAndinplate();
				cidsList=RedisDao.getallAndinarticlesidByplateidAnduserid(plateid,userid);
			}
		Map<String,String> map=RedisDao.getUserMessage(userid);
		int pages = 1;
		int p = cidsList.size();
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
			List<Map<String,String>> list=RedisDao.getarticlesbyplateidAnduserid(cidsList,userid,start,end);
			if(plateid.equals("0")){
				mv.addObject("platename","全部帖子");	
				mv.addObject("plateid","0");
				Map<String,String> map1=new HashMap<String, String>();
				map1.put("url", "tqbbs/index/learnindex.htm");
				map1.put("urlname", "学习交流");
				urlList.add(map1);
				Map<String,String> map2=new HashMap<String, String>();
				map2.put("url", "tqbbs/index/tobbsUserList/0/"+userid+"/1.htm");
				map2.put("urlname", map.get("user_name"));
				urlList.add(map2);
				Map<String,String> map3=new HashMap<String, String>();
				map3.put("url", "tqbbs/index/tobbsUserList/0/"+userid+"/1.htm");
				map3.put("urlname", "全部帖子");
				urlList.add(map3);
				mv.addObject("urlList",urlList);
				mv.addObject("urlListsize",urlList.size());
			}
			else{
				mv.addObject("platename",RedisDao.getplatenamebyid(plateid));	
				mv.addObject("plateid",plateid);	
				Map<String,String> map1=new HashMap<String, String>();
				map1.put("url", "tqbbs/index/learnindex.htm");
				map1.put("urlname", "学习交流");
				urlList.add(map1);
				Map<String,String> map2=new HashMap<String, String>();
				map2.put("url", "tqbbs/index/tobbsUserList/0/"+userid+"/1.htm");
				map2.put("urlname", map.get("user_name"));
				urlList.add(map2);
				Map<String,String> map3=new HashMap<String, String>();
				map3.put("url", "tqbbs/index/tobbsUserList/"+plateid+"/"+userid+"/1.htm");
				map3.put("urlname", RedisDao.getplatenamebyid(plateid));
				urlList.add(map3);
				mv.addObject("urlList",urlList);
				mv.addObject("urlListsize",urlList.size());
			}
			mv.addObject("page", page.intValue());
			mv.addObject("pages",pages);
			mv.addObject("articleLists",list);
			mv.addObject("plateList",plateList);
		}
		else{
			if(plateid.equals("0")){
				mv.addObject("platename","全部帖子");	
				mv.addObject("plateid","0");	
				Map<String,String> map1=new HashMap<String, String>();
				map1.put("url", "tqbbs/index/learnindex.htm");
				map1.put("urlname", "学习交流");
				urlList.add(map1);
				Map<String,String> map2=new HashMap<String, String>();
				map2.put("url", "tqbbs/index/tobbsUserList/0/"+userid+"/1.htm");
				map2.put("urlname", map.get("user_name"));
				urlList.add(map2);
				Map<String,String> map3=new HashMap<String, String>();
				map3.put("url", "tqbbs/index/tobbsUserList/0/"+userid+"/1.htm");
				map3.put("urlname", "全部帖子");
				urlList.add(map3);
				mv.addObject("urlList",urlList);
			}
			else{
				mv.addObject("platename",RedisDao.getplatenamebyid(plateid));	
				mv.addObject("plateid",plateid);	
				Map<String,String> map1=new HashMap<String, String>();
				map1.put("url", "tqbbs/index/learnindex.htm");
				map1.put("urlname", "学习交流");
				urlList.add(map1);
				Map<String,String> map2=new HashMap<String, String>();
				map2.put("url", "tqbbs/index/tobbsUserList/0/"+userid+"/1.htm");
				map2.put("urlname", map.get("user_name"));
				urlList.add(map2);
				Map<String,String> map3=new HashMap<String, String>();
				map3.put("url", "tqbbs/index/tobbsUserList/"+plateid+"/"+userid+"/1.htm");
				map3.put("urlname", RedisDao.getplatenamebyid(plateid));
				urlList.add(map3);
				mv.addObject("urlList",urlList);
			}
			
			mv.addObject("page", page.intValue());
			mv.addObject("pages",pages);
			mv.addObject("articleLists",new ArrayList<Map<String,String>>());
			mv.addObject("plateList",plateList);
			mv.addObject("userid",userid);
		}
		mv.addObject("username",map.get("user_name"));
		mv.addObject("userphoto",map.get("user_photo"));
		mv.addObject("count",map.get("count"));
		mv.addObject("urlList",urlList);
		return mv;
	}
	public Map<String,String> getMap(int if_in){
		Map<String,String> map=new HashMap<String, String>();
		if(if_in==0){
			map.put("url", "tqbbs/index/learnindex.htm");
			map.put("urlname", "学习交流");
		}
		else{
			map.put("url", "tqbbs/index/inlearnindex.htm");
			map.put("urlname", "项目日志");
		}
		return map;
	}
}
