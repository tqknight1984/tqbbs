package com.poly.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ContentUtil {
	/**
	 * 删除指定标签
	 * @param html
	 * @param tag
	 * @return
	 */
	public static String removeTag(String html,String tag){
		Document doc = Jsoup.parse(html);
		doc.select(tag).remove();
		html = doc.body().toString();
		return html;
	}
}	
