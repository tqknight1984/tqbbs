package com.poly.controller.admin.plate;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.poly.bean.TbPlate;
import com.poly.redis.RedisDao;

@Controller("admin_PlateController")
@RequestMapping("/admin/plate")
public class PlateController {
	
	@RequestMapping("/show.htm")
	@ResponseBody
	public ModelAndView show(){
		ModelAndView view = new ModelAndView("admin/plate/plate_show.ftl");
		List<Map<String, String>> pList = RedisDao.plate_list();
		view.addObject("plist", pList);
		return view;
	}
	
	@RequestMapping("/showin.htm")
	@ResponseBody
	public ModelAndView showin(){
		ModelAndView view = new ModelAndView("admin/plate/plate_showin.ftl");
		List<Map<String, String>> pList = RedisDao.plate_listin();
		view.addObject("plist", pList);
		return view;
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public String add(String plate_name, int isin){
		if (plate_name==null || plate_name.equals("") || plate_name.equals("NULL")) {
			return "0";
		}else {
			TbPlate tbPlate = new TbPlate();
			tbPlate.setAdd_time(new Date());
			tbPlate.setPlate_name(plate_name);
			tbPlate.setIs_in(isin);
			
			boolean result = RedisDao.plate_add(tbPlate);
			if (result) {
				return "1";
			}else {
				return "0";
			}
		}
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public String update(int plate_id,String plate_name, int isin){
		if (plate_name==null || plate_name.equals("") || plate_name.equals("NULL")) {
			return "0";
		}else {
			TbPlate tbPlate = new TbPlate();
			tbPlate.setPlate_id(plate_id);
			tbPlate.setPlate_name(plate_name);
			tbPlate.setIs_in(isin);
			
			boolean result = RedisDao.plate_update(tbPlate);
			if (result) {
				return "1";
			}else {
				return "0";
			}
		}
	}
	
	@RequestMapping("/order")
	@ResponseBody
	public String order(int id_1,int id_2, int isin){
		int result = RedisDao.plate_order(id_1,id_2,isin);
		return result + "";
	}
}
