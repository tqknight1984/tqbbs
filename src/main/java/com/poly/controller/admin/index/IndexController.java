package com.poly.controller.admin.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("adminIndexContrller")
@RequestMapping("/admin")
public class IndexController {
	
	@RequestMapping("/index.htm")
	public ModelAndView show() {
		ModelAndView view = new ModelAndView("admin/index/index.ftl");
		return view;
	}
	
	@RequestMapping("/welcome.htm")
	public ModelAndView welcome() {
		ModelAndView view = new ModelAndView("admin/index/welcome.ftl");
		return view;
	}
}
