package com.poly.inteceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.poly.util.MyConfig;
import com.poly.util.UserUtil;

public class HostURLInteceptor extends HandlerInterceptorAdapter {
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(modelAndView != null) {
			modelAndView.addObject("bbs_url", MyConfig.bbs_url);
			modelAndView.addObject("bbs_img", MyConfig.bbs_img);
			modelAndView.addObject("bbs_admin", MyConfig.bbs_admin);
			modelAndView.addObject("user", UserUtil.getUserFromCookie(request));
			modelAndView.addObject("che_url",MyConfig.che_url);
			modelAndView.addObject("ws_url",MyConfig.ws_url);
		}
	}
}
