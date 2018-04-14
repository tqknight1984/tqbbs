package com.poly.controller.admin.plate;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.poly.redis.RedisDao;

@Controller("admin_PlateMasterController")
@RequestMapping("/admin/plate")
public class MasterController {
	
	@RequestMapping("/master.htm")
	@ResponseBody
	public ModelAndView list(){
		ModelAndView view = new ModelAndView("admin/plate/master.ftl");
		List<Map<String, String>> pList = RedisDao.plate_list();
		List<Map<String, String>> pListin = RedisDao.plate_listin();
		view.addObject("plist", pList);
		view.addObject("plistin", pListin);
		
		List<Map<String, String>> masterList = RedisDao.plate_getMasterList(0);
		List<Map<String, String>> masterListin = RedisDao.plate_getMasterList(1);
		view.addObject("masterList", masterList);
		view.addObject("masterListin", masterListin);
		return view;
	}
	
	@RequestMapping("/master/add")
	@ResponseBody
	public int add(int userid, int isin, int plateid) {
		int result = RedisDao.plate_masterAdd(isin, userid, plateid);
		return result;
	}
	
	@RequestMapping("/master/delete")
	@ResponseBody
	public int delete(int masterid) {
		int result = RedisDao.plate_masterDelete(masterid);
		return result;
	}
	
	@RequestMapping("/master/userSearch")
	@ResponseBody
	public Map<String, String> userSearch(String key) {
		Map<String, String> map = RedisDao.plate_searchUser(key);
		return map;
	}
}
