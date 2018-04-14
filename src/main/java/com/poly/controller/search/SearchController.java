package com.poly.controller.search;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.poly.bean.User;
import com.poly.controller.BaseController;
import com.poly.redis.RedisDao;
import com.poly.util.LuceneUtil;
import com.poly.util.UserUtil;

@Controller("searchController")
@RequestMapping("/search")
public class SearchController extends BaseController{
	/**
	 * 查詢
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/toSearch")
	public ModelAndView toSearch(String ksWord,Integer page,HttpSession session,HttpServletRequest request) throws UnsupportedEncodingException{

//		User user = (User) session.getAttribute("user");
		ModelAndView mv = new ModelAndView("bbs/search.ftl");
		ksWord = new String(ksWord.getBytes("iso8859-1"),"UTF-8");
		
		User user=new User();
		try {
			user = UserUtil.getUserFromCookie(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Map<String, String>> list = null;
		if(page==null){
			page=1;
		}
		int pages=1;
		try {
			if(ksWord!=null&&!ksWord.equals("")){
				if(user==null||user.getRole()==0){
					list = LuceneUtil.searchIndex(ksWord,page,2);//外部帖子
				}
				else{
					list = LuceneUtil.searchIndex(ksWord,page,1);//全部帖子
				}
			if(list.size()>0){
			pages=Integer.parseInt(list.get(list.size()-1).get("pages"));
			list.remove(list.size()-1);
			}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mv.addObject("page", page.intValue());
		mv.addObject("pages",pages);
		mv.addObject("ksWord",ksWord);
		mv.addObject("list",list);
		return mv;
	}
}
