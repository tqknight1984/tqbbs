package com.poly.inteceptor;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.poly.bean.User;
import com.poly.redis.RedisDao;
import com.poly.util.MyConfig;
import com.poly.util.UserUtil;

public class LoginInteceptor extends HandlerInterceptorAdapter{
	private final Logger logger = Logger.getLogger(LoginInteceptor.class);
	
	@Override
	 public boolean preHandle(HttpServletRequest request,  
	            HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
//		User user = (User) session.getAttribute("user");
		User user = UserUtil.getUserFromCookie(request);
		if(user!=null&&user.getUser_state()==0){
			return super.preHandle(request, response, handler);
		}else{
			StringBuffer url = request.getRequestURL();  
			String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();
			//String requestUrl = request.getRequestURL().toString();
			//String requestUrl = request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();
			StringBuffer parameter = new StringBuffer();
			Map<String, String[]> parameterMap = request.getParameterMap();
			if (parameterMap != null) {
				for (Entry<String, String[]> entry : parameterMap.entrySet()) {
					String parameterName = entry.getKey();
					String[] parameterValues = entry.getValue();
					if (parameterValues != null) {
						for (String parameterValue : parameterValues) {
							parameter.append(parameterName + "=" + parameterValue + "&");
						}
					}
				}
				
			}
			String parameterStr = parameter.toString();
			if(StringUtils.isNotEmpty(parameterStr)&&parameterStr.length()>0){
				parameterStr = parameterStr.substring(0,parameterStr.length()-1);
			}
			String requestUrl = request.getRequestURI() ;
			if(StringUtils.isNotEmpty(parameterStr)){
				requestUrl = requestUrl+"?" + parameterStr;
			}
			//String requestUrl = parameterStr != null ? request.getRequestURI() + "?" + parameterStr : request.getRequestURI();
			requestUrl = URLEncoder.encode(requestUrl, "UTF-8");
			
			response.sendRedirect(request.getContextPath()+"/user/toLogin?fromUrl="+requestUrl);
            logger.info("unLogin!");
			return false;
		}
	}
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		if(modelAndView != null) {
			User user = UserUtil.getUserFromCookie(request);
			Integer user_id = user.getUser_id();
			int articleSize = RedisDao.getArticleSizeByUserId(user_id);
			int eliteSize = RedisDao.getEliteArticleIdListByUserId(user_id)==null?0:RedisDao.getEliteArticleIdListByUserId(user_id).size();
			modelAndView.addObject("articleSize", articleSize);
			modelAndView.addObject("eliteSize", eliteSize);
		}
	}
	
}
