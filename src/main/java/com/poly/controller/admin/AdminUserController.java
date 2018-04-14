
package com.poly.controller.admin;

import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.poly.bean.AdminUser;
import com.poly.bean.Message;
import com.poly.controller.BaseController;
import com.poly.service.admin.AdminUserService;
import com.poly.util.MD5Util;

@Controller("adminUserController2")
@RequestMapping("/adminUser")
public class AdminUserController extends BaseController{
	@Resource(name="adminUserService")
	private AdminUserService adminUserService;
	@RequestMapping("/toLogin.htm")
	public ModelAndView toLogin(){
		ModelAndView mv = new ModelAndView("admin/login/login.ftl");
		return mv;
	
	}
	@RequestMapping("/login")
	public @ResponseBody Message login(String username,String password,HttpSession session) throws SQLException{
		Message msg = new Message();
		if(StringUtils.isEmpty(username)){
			msg.setCode(errorCode);
			msg.setMsg("用户名不能为空");
			return msg;
		}
		if(StringUtils.isEmpty(password)){
			msg.setCode(errorCode);
			msg.setMsg("密码不能为空");
			return msg;
		}
		AdminUser user = adminUserService.getAdminUserDaoByUsername(username);
		if(!MD5Util.string2MD5(password).equals(user.getPassword())){
			msg.setCode(errorCode);
			msg.setMsg("密码错误");
			return msg;
		}
		session.setAttribute("adminUser", user);
		msg.setCode(successCode);
		msg.setMsg("登录成功");
		return msg;
	}
	@RequestMapping("/logout")
	public @ResponseBody Message logout(HttpSession session){
		Message msg = new Message();
		session.setAttribute("adminUser", null);
		msg.setCode(successCode);
		msg.setMsg("注销成功");
		return msg;
	}
}
