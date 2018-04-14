package com.poly.inteceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.poly.bean.AdminUser;
import com.poly.bean.User;
import com.poly.util.MyConfig;

public class AdminInteceptor extends HandlerInterceptorAdapter{
	
	 public boolean preHandle(HttpServletRequest request,  
	            HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		AdminUser user = (AdminUser) session.getAttribute("adminUser");
		if(user!=null){
			if(user.getRole()==0){
				return super.preHandle(request, response, handler);
			}else{
				response.sendRedirect(MyConfig.bbs_url+"no_power.html");
				return false;
			}
		}else{
			response.sendRedirect(request.getContextPath()+"/adminUser/toLogin.htm");
			return false;
		}
	}
}
