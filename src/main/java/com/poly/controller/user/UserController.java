package com.poly.controller.user;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.chetuan.mq.MetaQSend;
import com.poly.bean.Message;
import com.poly.bean.PhoneCode;
import com.poly.bean.User;
import com.poly.controller.BaseController;
import com.poly.redis.RedisDao;
import com.poly.service.user.UserService;
import com.poly.util.Base64ImageUtil;
import com.poly.util.Constant;
import com.poly.util.CookieUtils;
import com.poly.util.MD5Util;
import com.poly.util.MyConfig;
import com.poly.util.ObjectToMapUtil;
import com.poly.util.PostMsg;
import com.poly.util.UserUtil;

@Controller("userController")
@RequestMapping("/user")
public class UserController extends BaseController{
	@Resource(name="userService")
	private UserService userService;
	/**
	 * 进入登录界面
	 * @return
	 */
	@RequestMapping("/toLogin")
	public ModelAndView toLogin(String fromUrl){
		ModelAndView mv = new ModelAndView("user/toLogin.ftl");
		if(StringUtils.isEmpty(fromUrl)){
			fromUrl = "";
		}
		mv.addObject("fromUrl", fromUrl);
		return mv;
	}
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping("/login")
	public @ResponseBody Message login(String username,String password,HttpSession session,
			HttpServletRequest request,HttpServletResponse response){
		Message msg = new Message();
		try{
			if(StringUtils.isEmpty(username)){
				msg.setCode(errorCode);
				msg.setMsg("用户名不能为空!");
				return msg;
			}
			if(StringUtils.isEmpty(password)){
				msg.setCode(errorCode);
				msg.setMsg("密码不能为空!");
				return msg;
			}
			User user  = RedisDao.getUserByUsername(username);
			if(user!=null&&user.getUser_state()==1){
				msg.setCode(errorCode);
				msg.setMsg("该账号已被锁定!");
				return msg;
			}
			if(user==null||user.getUser_state()!=0||!MD5Util.string2MD5(password).equals(user.getUser_password())){
				user = RedisDao.getUserByPhone(username);
				if(user!=null&&user.getUser_state()==1){
					msg.setCode(errorCode);
					msg.setMsg("该账号已被锁定!");
					return msg;
				}
				if(user==null||!MD5Util.string2MD5(password).equals(user.getUser_password())){
					msg.setCode(errorCode);
					msg.setMsg("用户名或密码错误!");
					return msg;
				}
//				msg.setCode(errorCode);
//				msg.setMsg("用户名或密码错误!");
//				return msg;
			}
			user.setLog_time(new Date());
			int id = RedisDao.saveUser(user);
			RedisDao.addUserUpdateList(id);
//			session.setAttribute("user", user);
			UserUtil.setUserCookie(user, request, response);
			msg.setCode(successCode);
			msg.setMsg("登录成功！");
			return msg;
		}catch(Exception e){
			msg.setCode(errorCode);
			msg.setMsg("系统异常!");
			return msg;
		}
	}
	/**
	 * 进入注册页面
	 * @return
	 */
	@RequestMapping("/toRegister.htm")
	public ModelAndView toRegister(){
		ModelAndView mv = new ModelAndView("user/toRegister.ftl");
		return mv;
		
	}
	/**
	 *用户注册
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/register")
	public@ResponseBody Message register(HttpSession session,String username,String password,
			String password2,String phone,String code,HttpServletResponse response,HttpServletRequest request) throws Exception{
		Message msg = new Message();
		if(StringUtils.isEmpty(username)){
			msg.setCode(errorCode);
			msg.setMsg("用户名不能为空！");
			return msg;
		}
		if(!username.matches(MyConfig.username_reg)){
			msg.setCode(errorCode);
			msg.setMsg("用户名格式不正确，6-20位数字、字母或1-4位汉字");
			return msg;
		}
		
		if(StringUtils.isEmpty(password)){
			msg.setCode(errorCode);
			msg.setMsg("密码不能为空！");
			return msg;
		}
		if(StringUtils.isEmpty(phone)){
			msg.setCode(errorCode);
			msg.setMsg("手机号不能为空！");
			return msg;
		}
		if(!phone.matches(MyConfig.phone_reg)){
			msg.setCode(errorCode);
			msg.setMsg("手机号格式不正确！");
			return msg;
		}
		if(StringUtils.isEmpty(code)){
			msg.setCode(errorCode);
			msg.setMsg("验证码不能为空！");
			return msg;
		}
		if(!password.equals(password2)){
			msg.setCode(errorCode);
			msg.setMsg("两次输入密码不一致！");
			return msg;
		}
		Map<String,String> phoneCode = RedisDao.getUserCodeMapByPhone(phone);
		if(phoneCode==null||phoneCode.size()<=0){
			msg.setCode(errorCode);
			msg.setMsg("请先获取验证码！");
			return msg;
		}
		String sendTime = phoneCode.get("sendtime");
		if(sendTime!=null){
			long time_betwwen = new Date().getTime() - new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sendTime).getTime();
			if(time_betwwen/1000>120){
				msg.setCode(errorCode);
				msg.setMsg("验证码已失效！");
				return msg;
			}
		}else{
			msg.setCode(errorCode);
			msg.setMsg("验证码已失效！");
			return msg;
		}
		if(!code.equals(phoneCode.get("code"))){
			msg.setCode(errorCode);
			msg.setMsg("验证码错误！");
			return msg;
		}
		User user = RedisDao.getUserByPhone(phone);
		if(user!=null&&user.getUser_state()!=9){
			msg.setCode(errorCode);
			msg.setMsg("该手机号已注册！");
			return msg;
		}
		user = RedisDao.getUserByUsername(username);
		if(user!=null&&user.getUser_state()!=9){
			msg.setCode(errorCode);
			msg.setMsg("该用户名已被注册！");
			return msg;
		}
		user = new User();
		user.setUser_grade(0);
		user.setReg_time(new Date());
		user.setLog_time(new Date());
		user.setUser_nickname("");
		user.setUser_password(MD5Util.string2MD5(password));
		user.setUser_phone(phone);
		user.setUser_photo("");
		user.setUser_name(username);
		user.setUser_state(0);
		user.setRole(0);;
		int id = RedisDao.saveUser(user);
		RedisDao.addUserInsertList(id);
//		session.setAttribute("user", user);
		UserUtil.setUserCookie(user, request, response);
		msg.setCode(successCode);
		msg.setMsg("注册成功!");
		return msg;
		
	}
	/**
	 * 获取手机验证码
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/getPhoneCode")
	public @ResponseBody Message getPhoneCode(String phone) throws Exception{
		Message msg = new Message();
		Map<String,String> pc = RedisDao.getUserCodeMapByPhone(phone);
		String sendTime = pc.get("sendtime");
		if(sendTime!=null){
			long time_betwwen = new Date().getTime() - new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sendTime).getTime();
			if(time_betwwen/1000<60){
				msg.setCode(errorCode);
				msg.setMsg("验证码发送频繁，请"+(60-(time_betwwen/1000))+"秒之后发送！");
				return msg;
			}
		}
		int a=(int)(Math.random()*100)+1;
		int b=(int)(Math.random()*100)+1;
		String aStr="";
		String bStr="";
		if(String.valueOf(a).length()==1){
			aStr="00"+a;
		}else if(String.valueOf(a).length()==2){
			aStr="0"+a;
		}else{
			aStr=String.valueOf(a);
		}
		if(String.valueOf(b).length()==1){
			bStr="00"+b;
		}else if(String.valueOf(b).length()==2){
			bStr="0"+b;
		}else{
			bStr=String.valueOf(b);
		}
		String code=aStr+bStr;
		String content = "验证码:"+code;
//		int back = PostMsg._HttpPost(phone, content);
		//int flag = SMSjianzhou.sendPhoneMsg(phone, content);
		boolean flag=MetaQSend.getInstance().sendMsg("yzm", phone+"|"+content); 
		if(flag){
			msg.setCode("0000");
			msg.setMsg("");
            PhoneCode phoneCode = new PhoneCode();
			phoneCode.setCode(code);
			phoneCode.setPhone(phone);
			phoneCode.setSendTime(new Date());
			Map<String,String> mp=ObjectToMapUtil.changeToMap(phoneCode);
			RedisDao.updateUserCodeByPhone(phone, mp);	
		}else{
			msg.setCode("1111");
			msg.setMsg("发送短信失败");
		}
		return msg;
	}
	/**
	 * 检查用户名是否已被注册
	 * @param username
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/checkUsername")
	public @ResponseBody Message checkUsername (String username) throws Exception{
		Message msg = new Message();
		User user = RedisDao.getUserByUsername(username);
		if(user!=null&&user.getUser_state()!=9){
			msg.setCode(errorCode);
			msg.setMsg("用户名已存在");
			return msg;
		}
		msg.setCode(successCode);
		msg.setMsg("用户名可以注册");
		return msg;
		
	}
	/**
	 * 注销
	 * @param username
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/logout")
	public @ResponseBody Message logout (HttpSession session,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Message msg = new Message();
//		session.removeAttribute("user");
		CookieUtils.setCookie(Constant.TOKEN, null, 0, request, response);
		CookieUtils.setCookie(Constant.UID, null, 0, request, response);
		msg.setCode(successCode);
		msg.setMsg("注销成功!");
		return msg;
		
	}
	/**
	 * 检查手机号是否已被注册
	 * @param username
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/checkPhone")
	public @ResponseBody Message checkPhone (String phone) throws Exception{
		Message msg = new Message();
		User user = RedisDao.getUserByPhone(phone);
		if(user!=null&&user.getUser_state()!=9){
			msg.setCode(errorCode);
			msg.setMsg("手机号已注册");
			return msg;
		}
		msg.setCode(successCode);
		msg.setMsg("手机号可以注册");
		return msg;
		
	}
	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getToken")
	public @ResponseBody Map<String,String> getToken (HttpServletRequest request) throws Exception{
		Map<String,String> map = new HashMap<String, String>();
		Cookie cookie = CookieUtils.getCookie(Constant.TOKEN, request);
		if(cookie==null||StringUtils.isEmpty(cookie.getValue())){
			map.put("code", "1111");
			return map;
		}
		String token = cookie.getValue();
		Map<String,String> umap = RedisDao.getUserByToken(token);
		if(umap==null||umap.size()<=0){
			map.put("code", "1111");
			return map;
		}
		map.put("code", "0000");
		map.put("uid", umap.get("user_id"));
		map.put("token", token);
		return map;
	}
	@RequestMapping("/userList.htm")
	public ModelAndView showUserList(HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("user/user_list.ftl");
		Map<String,String> urlMap = new LinkedHashMap<String,String>();
		urlMap.put("首页", "tqbbs/index/index.htm");
		urlMap.put("编程达人", "tqbbs/user/userList.htm");
		User user = UserUtil.getUserFromCookie(request);
		boolean is_inner = false;
		if(user!=null&&user.getRole()==1){
			is_inner = true;
		}
		List<Map<String, String>> list = userService.getUserListOrderByArticleSize();
		List<Map<String,String>> list1 = new ArrayList<Map<String,String>>();
		List<Map<String,String>> list2 = new ArrayList<Map<String,String>>();
		List<Map<String,String>> list3 = new ArrayList<Map<String,String>>();
		for(int i=1;i<=list.size();i++){
			if(i%3==1){
				list1.add(list.get(i-1));
			}else if(i%3==2){
				list2.add(list.get(i-1));
			}else{
				list3.add(list.get(i-1));
			}
		}
		mv.addObject("list1",list1);
		mv.addObject("list2",list2);
		mv.addObject("list3",list3);
		mv.addObject("urlMap",urlMap);
		mv.addObject("is_inner",is_inner);
		return mv;
	}
	/**
	 * 保存聊天图片
	 * @return
	 */
	@RequestMapping("/saveImage")
	public @ResponseBody Message saveImage(String data){
		Message msg = new Message();
		try{
			JSONArray jsonArr = JSONArray.fromObject(data);
			for(int i=0;i<jsonArr.size();i++){
				JSONObject photo = (JSONObject) jsonArr.get(i);
				String name = photo.getString("md5");
				String base64 = photo.getString("base64");
				String filePath =MyConfig.img_savePath+"msg/"+name+".jpg";
				if(!new File(filePath).exists()){
					Base64ImageUtil.GenerateImage(base64, filePath);
				}
			}
			msg.setCode(successCode);
			msg.setMsg("成功");
		}catch(Exception e){
			e.printStackTrace();
			msg.setCode(errorCode);
			msg.setMsg("异常");
		}
		
		return msg;
	}
	/**
	 * 进入找回密码页面
	 * @return
	 */
	@RequestMapping("/toGetBackPassword.htm")
	public ModelAndView toGetBackPassword(){
		ModelAndView mv = new ModelAndView("user/toGetBackPassword.ftl");
		Map<String,String> urlMap = new LinkedHashMap<String,String>();
		urlMap.put("首页", "tqbbs/index/index.htm");
		urlMap.put("验证手机号", "tqbbs/user/toGetBackPassword.htm");
		mv.addObject("urlMap",urlMap);
		return mv;
	}
	/**
	 * 验证手机号验证码（找回密码）
	 * @param phone
	 * @param yzm
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/checkPhoneCode")
	public @ResponseBody Message checkPhoneCode(String phone ,String yzm,HttpServletRequest request ,
			HttpServletResponse response) throws Exception{
		Message msg = new Message();
		User user = RedisDao.getUserByPhone(phone);
		if(StringUtils.isEmpty(phone)){
			msg.setCode(errorCode);
			msg.setMsg("手机号不能为空");
			return msg;
		}
		if(StringUtils.isEmpty(yzm)){
			msg.setCode(errorCode);
			msg.setMsg("验证码不能为空");
			return msg;
		}
		if(user==null||user.getUser_state()!=0){
			msg.setCode(errorCode);
			msg.setMsg("用户不存在或用户状态异常");
			return msg;
		}
		Map<String, String> phoneCode = RedisDao.getUserCodeMapByPhone(phone);
		if(phoneCode==null||phoneCode.size()<=0){
			msg.setCode(errorCode);
			msg.setMsg("请先获取验证码！");
			return msg;
		}
		String sendTime = phoneCode.get("sendtime");
		if(sendTime!=null){
			long time_betwwen = new Date().getTime() - new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sendTime).getTime();
			if(time_betwwen/1000>120){
				msg.setCode(errorCode);
				msg.setMsg("验证码已失效！");
				return msg;
			}
		}else{
			msg.setCode(errorCode);
			msg.setMsg("验证码已失效！");
			return msg;
		}
		if(!yzm.equals(phoneCode.get("code"))){
			msg.setCode(errorCode);
			msg.setMsg("验证码错误！");
			return msg;
		}
		String mark = MD5Util.string2MD5(user.getUser_phone());
		RedisDao.saveBackPasswordMark(mark,user.getUser_name());
		CookieUtils.setCookie("backMark", mark, request, response);
		msg.setCode(successCode);
		msg.setMsg("验证成功");
		return msg;
	}
	/**
	 * 进入重置密码界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toUpdatePassword.htm")
	public ModelAndView toUpdatePassword(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView("user/toUpdatePassword.ftl");
		String mark = CookieUtils.getCookieValue("backMark", request);
		if(StringUtils.isEmpty(mark)){
			return this.toGetBackPassword();
		}
		User user = RedisDao.getUserByMark(mark);
		if(user==null){
			return this.toGetBackPassword();
		}
		Map<String,String> urlMap = new LinkedHashMap<String,String>();
		urlMap.put("首页", "tqbbs/index/index.htm");
		urlMap.put("重置密码", "tqbbs/user/toUpdatePassword.htm");
		mv.addObject("urlMap",urlMap);
		mv.addObject("backUser",user);
		return mv;
	}
	/**
	 * 修改密码（找回密码）
	 * @param username
	 * @param password
	 * @param rePassword
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updatePassword")
	public @ResponseBody Message updatePassword(String password,String rePassword,
			HttpServletRequest request,HttpServletResponse response){
		Message msg = new Message();
		if(StringUtils.isEmpty(password)){
			msg.setCode(errorCode);
			msg.setMsg("密码不能为空");
			return msg;
		}
		if(!password.equals(rePassword)){
			msg.setCode(errorCode);
			msg.setMsg("两次密码不一致");
			return msg;
		}
		String mark = CookieUtils.getCookieValue("backMark", request);
		if(StringUtils.isEmpty(mark)){
			msg.setCode(errorCode);
			msg.setMsg("系统异常");
			return msg;
		}
		User user = RedisDao.getUserByMark(mark);
		if(user==null){
			msg.setCode(errorCode);
			msg.setMsg("系统异常");
			return msg;
		}
		user.setUser_password(MD5Util.string2MD5(password));
		RedisDao.saveUser(user);
		CookieUtils.setCookie("backMark", null, request, response);
		RedisDao.removeMark(mark);
		msg.setCode(successCode);
		msg.setMsg("密码设置完成");
		return msg;
	}
	/**
	 * 修改成功
	 * @param sign
	 * @return
	 */
	@RequestMapping("/updateSuccess.htm")
	public ModelAndView updateSuccess(String sign){
		ModelAndView mv = new ModelAndView("user/updateSuccess.ftl");
		Map<String,String> urlMap = new LinkedHashMap<String,String>();
		if(StringUtils.isEmpty(sign)||!"success".equals(sign)){
			return this.toGetBackPassword();
		}
		urlMap.put("首页", "tqbbs/index/index.htm");
		urlMap.put("重置成功", "tqbbs/user/updateSuccess.htm");
		mv.addObject("urlMap",urlMap);
		return mv;
	}
}
