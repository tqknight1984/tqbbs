package com.poly.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie工具类
 * @author niuhaidong
 *
 * 2015年6月3日
 */
public class CookieUtils {
	
	/**
	 * 设置cookie
	 * @param cookieName
	 * @param cookieValue
	 * @param maxAge 过期时间 秒  
	 * @param request
	 * @param response
	 */
	public static void setCookie(String cookieName, String cookieValue, int maxAge, HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = getCookie(cookieName, request);
		if(cookie == null) {
			cookie = new Cookie(cookieName, cookieValue);
		} else {
			cookie.setValue(cookieValue);
		}
		
		if(maxAge >= 0) {
			cookie.setMaxAge(maxAge);
		}
		
		cookie.setPath("/");
		cookie.setDomain(".tqbbs.com");
		response.addCookie(cookie);
	}
	
	/**
	 * 设置cookie
	 * @param cookieName
	 * @param cookieValue
	 * @param request
	 * @param response
	 */
	public static void setCookie(String cookieName, String cookieValue, HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = getCookie(cookieName, request);
		if(cookie == null) {
			cookie = new Cookie(cookieName, cookieValue);
		} else {
			cookie.setValue(cookieValue);
		}
		cookie.setMaxAge(-1);
		cookie.setPath("/");
		cookie.setDomain(".tqbbs.com");
		response.addCookie(cookie);
	}

	/**
	 * 根据cookieName 获取cookie对象
	 * @param cookieName
	 * @return
	 */
	public static Cookie getCookie(String cookieName, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null) {
			return null;
		}
		for(Cookie cookie : cookies) {
			if(cookieName.equals(cookie.getName())) {
				return cookie;
			}
		}
		return null;
	}
	
	/**
	 * 根据cookieName 获取cookie值
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(String cookieName, HttpServletRequest request) {
		Cookie cookie = getCookie(cookieName, request);
		if(cookie == null) {
			return null;
		}
		return cookie.getValue();
	}
	
	
}
