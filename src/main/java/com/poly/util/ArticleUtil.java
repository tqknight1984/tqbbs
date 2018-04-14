package com.poly.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticleUtil {
	
	public static String getFilterStr(String filePaths) throws IOException {
		String zz = "";
		String[] path = filePaths.split(",");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < path.length; i++) {
			list.addAll(FileUtil.readFile(path[i]));
		}
		
		for (int i = 0; i < list.size(); i++) {
			if (i<list.size()-1) {
				zz = zz + "("+ list.get(i) +")|";
			}else {
				zz = zz + "("+ list.get(i) +")";
			}
		}
		return zz;
	}
	public static int getArticleIdFromUrl(String url){
		int begin = url.lastIndexOf("/")+1;
		int end  = url.lastIndexOf(".");
		if(begin>=end){
			return -1;
		}
		int id = 0;
		try {
			id = Integer.parseInt(url.substring(begin,end));
		} catch (Exception e) {
			id=-1;
		}
		return id;
	}
}
