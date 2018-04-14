package com.poly.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

public class ImgTest {
	
	public static void main(String[] args) {
		String str = "<img title=\"\" src=\"/bbs_img/temp/20151021/image/1445400269898.jpg\" alt=\"\" align=\"\" height=\"192\" width=\"289\" />";
		str = "<img src=\"/bbs_img/temp/20151021/image/1445400800984.jpg\" alt=\"\" />";
		System.out.println(str);
		String[] arr = new String[]{"src","width","height"};
		Map<String, String> infoMap = getImgInfo(str, arr);
		System.out.println(infoMap.size());
	}

	public static int dealContent(String articleId, String content) {
		String url_head = MyConfig.bbs_img;
		if (url_head.contains(MyConfig.bbs_url)) {
			url_head = url_head.replace(MyConfig.bbs_url, "/");
		}
		
		String imgsrc = url_head + "temp/" + "[0-9]{8}/image/" + "[0-9]+.(gif|jpg|jpeg|png|bmp)";
		Pattern pp_img = Pattern.compile("<img.*src=\""+ imgsrc +"\"[^>]*? />");
		Matcher m = pp_img.matcher(content);
		List<String> imgList_1 = new ArrayList<String>();
		List<String> imgList_2 = new ArrayList<String>();
		List<String> imgList_3 = new ArrayList<String>();
		while(m.find()){
			String img_html = m.group(0);
			String[] arr = new String[]{"src","width","height"};
			Map<String, String> infoMap = getImgInfo(img_html, arr);
			
			String imgscr_real_1 = infoMap.get("src").replace(url_head, MyConfig.img_savePath);
			String imgscr_real_2 = imgscr_real_1.replaceAll("temp/[0-9]{8}", articleId);  //将临时路径改为真实路径
			String imgscr_real_url = imgscr_real_2.replace(MyConfig.img_savePath, MyConfig.bbs_img);
			String imgscr_replace = imgscr_real_url.replace(MyConfig.bbs_img, MyConfig.bbs_img_replace);
			System.out.println(imgscr_real_1);
			System.out.println(imgscr_real_2);
			System.out.println(imgscr_real_url);
			System.out.println(imgscr_replace);
			
			MarvinImage img = MarvinImageIO.loadImage(imgscr_real_1);
			MarvinImage image = img.clone();
			int w = image.getWidth();
			if (infoMap.get("width").length()>0) {
				w = Integer.parseInt(infoMap.get("width"));
			}
			int h = image.getHeight();
			if (infoMap.get("height").length()>0) {
				h = Integer.parseInt(infoMap.get("height"));
			}
			
			image.resize(w, h);
			
			String fp = imgscr_real_2.replaceAll("/[0-9]+.(gif|jpg|jpeg|png|bmp)", "");
            File ff = new File(fp);
            if (!ff.exists()) {
         	   ff.mkdirs();
            }
            
			MarvinImageIO.saveImage(image, imgscr_real_2);
			img = null;
			
			imgList_1.add(img_html);
			imgList_2.add(img_html.replace(infoMap.get("src"), imgscr_replace));
		}
		
		for (int i = 0; i < imgList_1.size(); i++) {
			content = content.replace(imgList_1.get(i), imgList_2.get(i));
		}
		
		return 0;
	}
	
	private static Map<String, String> getImgInfo(String html_str, String[] arr) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < arr.length; i++) {
			String tag = arr[i];
			if (tag.replaceAll(" ", "").length()>0) {
				String str = "";
				int a = html_str.indexOf(tag + "=\"");
				if (a>=0) {
					a = a+ tag.length() + 2;
//					System.out.println("a---" + a);
					int b = html_str.indexOf("\"", a+1);
//					System.out.println("b---" + b);
					if (b>a) {
						str = html_str.substring(a, b);
//						System.out.println(tag + ":  " + str);
					}
				}
				map.put(tag, str);
			}
		}
		
		return map;
	}
	
	
}
