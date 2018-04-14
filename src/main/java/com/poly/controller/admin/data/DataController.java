package com.poly.controller.admin.data;

import com.poly.service.hotImg.HotImgService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.poly.controller.BaseController;
import com.poly.service.article.ArticleService;
import com.poly.service.comment.CommentService;
import com.poly.service.plate.PlateMasterService;
import com.poly.service.plate.PlateService;
import com.poly.service.user.UserService;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Controller("baseController")
@RequestMapping("/admin/data")
public class DataController extends BaseController{
	
	@Autowired
	public ArticleService articleService;
	@Autowired
	public UserService userService;
	
	@Autowired
	public CommentService commentService;
	@Autowired
	public PlateMasterService plateMasterService;

	@Autowired
	public PlateService plateService;

	@Autowired
	private HotImgService hotImgService;
	
	@RequestMapping("/toRefresh.htm")
	public ModelAndView toRefreashPage(){
		ModelAndView mv = new ModelAndView("admin/data/refresh.ftl");
		return mv;
	}

	@RequestMapping("/refresh")
	@ResponseBody
	public Map<String, Object> doRefresh(){
		Map<String, Object> result = new HashMap<>();

		userService.toRedis();
		articleService.toRedis();
		commentService.toRedis();
		plateService.toRedis();
		plateMasterService.toRedis();
		hotImgService.mysqlToRedis();

		result.put("success", true);
		result.put("message", "数据刷新完成");
		return result;
	}

	@RequestMapping("/save")
	@ResponseBody
	public Map<String, Object> doSave(){
		Map<String, Object> result = new HashMap<>();

		try {
			userService.persistentUser();
		} catch (Exception e) {
			result.put("user", false);
			e.printStackTrace();
		}
		try {
			articleService.persistentArticle();
		} catch (ParseException e) {
			result.put("article", false);
			e.printStackTrace();
		}
		commentService.persistComment();

		try {
			plateService.persistPlate();
		} catch (ParseException e) {
			result.put("plate", false);
			e.printStackTrace();
		}
		try {
			plateMasterService.persistPlateMaster();
		} catch (ParseException e) {
			result.put("plateMaster", false);
			e.printStackTrace();
		}
		hotImgService.redisToMysql();
		result.put("success", true);
		return result;
	}

}
