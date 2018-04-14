package com.poly.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.poly.bean.User;
import com.poly.redis.RedisDao;

public class UserUtil {
	public static User mapToUser(Map<String,String> map) throws Exception{
		User user = null;
		if(map!=null && map.size()>=0){
			user  = new User();
			Integer id = Integer.parseInt(map.get("user_id"));
			String username = map.get("user_name");
			String password = map.get("user_password");
			String phone = map.get("user_phone");
			String gradeStr = map.get("user_grade");
			Integer grade = 0;
			if(StringUtils.isNotEmpty(gradeStr)){
				grade = Integer.parseInt(gradeStr);
			}
			String photo = map.get("user_photo");
			String nickname = map.get("user_nickname");
			
			String stateStr = map.get("user_state");
			Integer state = 0;
			if(StringUtils.isNotEmpty(stateStr)){
				state = Integer.parseInt(stateStr);
			}
			String joinTimeStr = map.get("reg_time");
			Date joinTime = null;
			if(StringUtils.isNotEmpty(joinTimeStr)){
				joinTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(joinTimeStr);
			}
			String lastLoginTimeStr = map.get("log_time");
			Date lastLoginTime = null;
			if(StringUtils.isNotEmpty(lastLoginTimeStr)){
				lastLoginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastLoginTimeStr);
			}
			Integer role = 0;
			String roleStr = map.get("role");
			if(StringUtils.isNotEmpty(roleStr)){
				role = Integer.parseInt(roleStr);
			}
			user.setUser_grade(grade);
			user.setUser_id(id);
			user.setReg_time(joinTime);
			user.setLog_time(lastLoginTime);
			user.setUser_nickname(nickname);
			user.setUser_password(password);
			user.setUser_phone(phone);
			user.setUser_photo(photo);
			user.setUser_name(username);
			user.setUser_state(state);
			user.setRole(role);
		}
		return user;
	}
	public static User getUserFromCookie(HttpServletRequest request) throws Exception{
		User user = null;
		Cookie cookie = CookieUtils.getCookie(Constant.TOKEN, request);
		if(cookie==null||StringUtils.isEmpty(cookie.getValue())){
			return user;
		}
		String token = cookie.getValue();
		Map<String,String> umap = RedisDao.getUserByToken(token);
		if(umap==null||umap.size()<=0){
			return user;
		}
		user = mapToUser(umap);
		return user;
	}
	public static String getToken(User user){
		String token=MD5Util.string2MD5(user.getUser_name()+user.getUser_id());
		return token;
	}
	public static void setUserCookie(User user,HttpServletRequest request,HttpServletResponse response){
		String token = getToken(user);
		RedisDao.setUserCookie(token, user.getUser_id().toString());
		CookieUtils.setCookie(Constant.TOKEN, token,request, response);
		CookieUtils.setCookie(Constant.UID, user.getUser_id().toString(),request, response);
	}
	public static void deleteUserCookie(User user){
		String token = getToken(user);
		RedisDao.deleteUserCookie(token);
	}
}
