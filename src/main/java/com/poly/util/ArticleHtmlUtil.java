package com.poly.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.coobird.thumbnailator.Thumbnails;

import com.aviyehuda.easyimage.Image;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

public class ArticleHtmlUtil {
	/**
	 * 处理帖子中的图片（没有删除原有图片，对于更改帖子时，修改图片的情况未做处理）
	 * @param articleId
	 * @param content
	 * @return 经过处理的帖子
	 * @author 丛凤杰
	 */
	public static String dealContent(String articleId, String content) {
		String url_head = MyConfig.bbs_img;
		if (url_head.contains(MyConfig.bbs_url)) {
			url_head = url_head.replace(MyConfig.bbs_url, "/");
		}
		
		String imgsrc = url_head + "temp/" + "[0-9]{8}/image/" + "[0-9]+.(gif|jpg|jpeg|png|bmp|JPG|JPEG)";
		Pattern pp_img = Pattern.compile("<img.[^>]*src=\""+ imgsrc +"\"[^>]*?/>");
		Matcher m = pp_img.matcher(content);
		List<String> imgList_1 = new ArrayList<String>();
		List<String> imgList_2 = new ArrayList<String>();
		while(m.find()){
			String img_html = m.group(0);
			String[] arr = new String[]{"src","width","height"};
			Map<String, String> infoMap = getImgInfo(img_html, arr);
			
			String imgscr_real_1 = infoMap.get("src").replace(url_head, MyConfig.img_savePath);
			String imgscr_real_2 = imgscr_real_1.replaceAll("temp/[0-9]{8}", articleId);  //将临时路径改为真实路径
			String imgscr_real_url = imgscr_real_2.replace(MyConfig.img_savePath, MyConfig.bbs_img);
			String imgscr_replace = imgscr_real_url.replace(MyConfig.bbs_img, MyConfig.bbs_img_replace);
			System.out.println("imgscr_real_1 >>>>>>>>>> " + imgscr_real_1);
			System.out.println("imgscr_real_2 >>>>>>>>>> " + imgscr_real_2);
			System.out.println("imgscr_real_url >>>>>>>>>> " + imgscr_real_url);
			System.out.println("imgscr_replace >>>>>>>>>> " + imgscr_replace);
			
			MarvinImage img = MarvinImageIO.loadImage(imgscr_real_1);
			MarvinImage image = img.clone();
			
			int isold1 = 0;
			int isold2 = 0;
			int w = image.getWidth();
			try {
				if (infoMap.get("width").length()>0) {
					w = Integer.parseInt(infoMap.get("width"));
					isold1 = 1;
				}
			} catch (Exception e) {
				
			}
			
			int h = image.getHeight();
			try {
				if (infoMap.get("height").length()>0) {
					h = Integer.parseInt(infoMap.get("height"));
					isold2 = 1;
				}
			} catch (Exception e) {
				
			}
			if (isold1==1 && isold2==0) {
				h = w*h/image.getWidth();
			}
			if (isold1==0 && isold2==1) {
				w = w*h/image.getHeight();
			}
			
			int w_max = Integer.parseInt(MyConfig.img_width_max);
			if (w>w_max) {
				h = h*w_max/w;
				w = w_max;
			}
			
			image.resize(w, h);
			
			String fp = imgscr_real_2.replaceAll("/[0-9]+.(gif|jpg|jpeg|png|bmp|JPG|JPEG)", "");
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
		
		return content;
	}
	
	/**
	 * 处理帖子中的图片（没有删除原有图片，对于更改帖子时，修改图片的情况未做处理）
	 * @param articleId
	 * @param content
	 * @return 经过处理的帖子
	 * @author 丛凤杰
	 */
	public static String dealContent2(String articleId, String content) {
		String url_head = MyConfig.bbs_img;
//		if (url_head.contains(MyConfig.bbs_url)) {
//			url_head = url_head.replace(MyConfig.bbs_url, "/");
//		}
		
		String imgsrc = url_head + "temp/" + "[0-9]{8}/image/" + "[0-9]+.(gif|jpg|jpeg|png|bmp|JPG|JPEG)";
		Pattern pp_img = Pattern.compile("<img.[^>]*src=\""+ imgsrc +"\"[^>]*?>");
		Matcher m = pp_img.matcher(content);
		List<String> imgList_1 = new ArrayList<String>();
		List<String> imgList_2 = new ArrayList<String>();
		while(m.find()){
			String img_html = m.group(0);
			String[] arr = new String[]{"src","width","height"};
			Map<String, String> infoMap = getImgInfo(img_html, arr);
			
			String imgscr_real_1 = infoMap.get("src").replace(url_head, MyConfig.img_savePath);
			String imgscr_real_2 = imgscr_real_1.replaceAll("temp/[0-9]{8}", articleId);  //将临时路径改为真实路径
			String imgscr_real_url = imgscr_real_2.replace(MyConfig.img_savePath, MyConfig.bbs_img);
			String imgscr_replace = imgscr_real_url.replace(MyConfig.bbs_img, MyConfig.bbs_img_replace);
			System.out.println("imgscr_real_1 >>>>>>>>>> " + imgscr_real_1);
			System.out.println("imgscr_real_2 >>>>>>>>>> " + imgscr_real_2);
			System.out.println("imgscr_real_url >>>>>>>>>> " + imgscr_real_url);
			System.out.println("imgscr_replace >>>>>>>>>> " + imgscr_replace);
			
			MarvinImage img = MarvinImageIO.loadImage(imgscr_real_1);
			MarvinImage image = img.clone();
			
			int isold1 = 0;
			int isold2 = 0;
			int w = image.getWidth();
			try {
				if (infoMap.get("width").length()>0) {
					w = Integer.parseInt(infoMap.get("width"));
					isold1 = 1;
				}
			} catch (Exception e) {
				
			}
			
			int h = image.getHeight();
			try {
				if (infoMap.get("height").length()>0) {
					h = Integer.parseInt(infoMap.get("height"));
					isold2 = 1;
				}
			} catch (Exception e) {
				
			}
			if (isold1==1 && isold2==0) {
				h = w*h/image.getWidth();
			}
			if (isold1==0 && isold2==1) {
				w = w*h/image.getHeight();
			}
			
			int w_max = Integer.parseInt(MyConfig.img_width_max);
			if (w>w_max) {
				h = h*w_max/w;
				w = w_max;
			}
			
			image.resize(w, h);
			
			String fp = imgscr_real_2.replaceAll("/[0-9]+.(gif|jpg|jpeg|png|bmp|JPG|JPEG)", "");
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
		
		return content;
	}
	
	/**
	 * 处理帖子中的图片（没有删除原有图片，对于更改帖子时，修改图片的情况未做处理）
	 * @param articleId
	 * @param content
	 * @return 经过处理的帖子
	 * @author 丛凤杰
	 */
	public static String dealContent3(String articleId, String content) {
		String url_head = MyConfig.bbs_img;
//		if (url_head.contains(MyConfig.bbs_url)) {
//			url_head = url_head.replace(MyConfig.bbs_url, "/");
//		}
		
		String imgsrc = url_head + "temp/" + "[0-9]{8}/image/" + "[0-9]+.(gif|jpg|jpeg|png|bmp|JPG|JPEG)";
		Pattern pp_img = Pattern.compile("<img.[^>]*src=\""+ imgsrc +"\"[^>]*?>");
		Matcher m = pp_img.matcher(content);
		List<String> imgList_1 = new ArrayList<String>();
		List<String> imgList_2 = new ArrayList<String>();
		while(m.find()){
			String img_html = m.group(0);
			String[] arr = new String[]{"src","width","height"};
			Map<String, String> infoMap = getImgInfo(img_html, arr);
			
			String imgscr_real_1 = infoMap.get("src").replace(url_head, MyConfig.img_savePath);
			String imgscr_real_2 = imgscr_real_1.replaceAll("temp/[0-9]{8}", articleId);  //将临时路径改为真实路径
			String imgscr_real_url = imgscr_real_2.replace(MyConfig.img_savePath, MyConfig.bbs_img);
			String imgscr_replace = imgscr_real_url.replace(MyConfig.bbs_img, MyConfig.bbs_img_replace);
			System.out.println("imgscr_real_1 >>>>>>>>>> " + imgscr_real_1);
			System.out.println("imgscr_real_2 >>>>>>>>>> " + imgscr_real_2);
			System.out.println("imgscr_real_url >>>>>>>>>> " + imgscr_real_url);
			System.out.println("imgscr_replace >>>>>>>>>> " + imgscr_replace);
			
			
			
//			MarvinImage img = MarvinImageIO.loadImage(imgscr_real_1);
//			MarvinImage image = img.clone();
			
			
			Image image  = new Image(imgscr_real_1);
			
			
			int isold1 = 0;
			int isold2 = 0;
			int w = image.getWidth();
			try {
				if (infoMap.get("width").length()>0) {
					w = Integer.parseInt(infoMap.get("width"));
					isold1 = 1;
				}
			} catch (Exception e) {
				
			}
			
			int h = image.getHeight();
			try {
				if (infoMap.get("height").length()>0) {
					h = Integer.parseInt(infoMap.get("height"));
					isold2 = 1;
				}
			} catch (Exception e) {
				
			}
			if (isold1==1 && isold2==0) {
				h = w*h/image.getWidth();
			}
			if (isold1==0 && isold2==1) {
				w = w*h/image.getHeight();
			}
			
			int w_max = Integer.parseInt(MyConfig.img_width_max);
			if (w>w_max) {
				h = h*w_max/w;
				w = w_max;
			}
			
//			image.resize(w, h);
			
			image.resize(w, h);
			
			String fp = imgscr_real_2.replaceAll("/[0-9]+.(gif|jpg|jpeg|png|bmp|JPG|JPEG)", "");
            File ff = new File(fp);
            if (!ff.exists()) {
         	   ff.mkdirs();
            }
            
//			MarvinImageIO.saveImage(image, imgscr_real_2);
//			img = null;
			
			image.saveAs(imgscr_real_2);
			
			imgList_1.add(img_html);
			imgList_2.add(img_html.replace(infoMap.get("src"), imgscr_replace));
		}
		
		for (int i = 0; i < imgList_1.size(); i++) {
			content = content.replace(imgList_1.get(i), imgList_2.get(i));
		}
		
		return content;
	}
	
	/**
	 * 处理帖子中的图片（没有删除原有图片，对于更改帖子时，修改图片的情况未做处理）
	 * @param articleId
	 * @param content
	 * @return 经过处理的帖子
	 * @author 丛凤杰
	 * @throws IOException 
	 */
	public static String dealContent4(String articleId, String content) throws IOException {
		String url_head = MyConfig.bbs_img;
//		if (url_head.contains(MyConfig.bbs_url)) {
//			url_head = url_head.replace(MyConfig.bbs_url, "/");
//		}
		
		String imgsrc = url_head + "temp/" + "[0-9]{8}/image/" + "[0-9]+.(gif|jpg|jpeg|png|bmp|JPG|JPEG)";
		Pattern pp_img = Pattern.compile("<img.[^>]*src=\""+ imgsrc +"\"[^>]*?>");
		Matcher m = pp_img.matcher(content);
		List<String> imgList_1 = new ArrayList<String>();
		List<String> imgList_2 = new ArrayList<String>();
		while(m.find()){
			String img_html = m.group(0);
			String[] arr = new String[]{"src","width","height"};
			Map<String, String> infoMap = getImgInfo(img_html, arr);
			
			String imgscr_real_1 = infoMap.get("src").replace(url_head, MyConfig.img_savePath);
			String imgscr_real_2 = imgscr_real_1.replaceAll("temp/[0-9]{8}", articleId);  //将临时路径改为真实路径
			String imgscr_real_url = imgscr_real_2.replace(MyConfig.img_savePath, MyConfig.bbs_img);
			String imgscr_replace = imgscr_real_url.replace(MyConfig.bbs_img, MyConfig.bbs_img_replace);
			System.out.println("imgscr_real_1 >>>>>>>>>> " + imgscr_real_1);
			System.out.println("imgscr_real_2 >>>>>>>>>> " + imgscr_real_2);
			System.out.println("imgscr_real_url >>>>>>>>>> " + imgscr_real_url);
			System.out.println("imgscr_replace >>>>>>>>>> " + imgscr_replace);
			
			
			
//			MarvinImage img = MarvinImageIO.loadImage(imgscr_real_1);
//			MarvinImage image = img.clone();
			
			
			Image image  = new Image(imgscr_real_1);
			
			
			int isold1 = 0;
			int isold2 = 0;
			int w = image.getWidth();
			try {
				if (infoMap.get("width").length()>0) {
					w = Integer.parseInt(infoMap.get("width"));
					isold1 = 1;
				}
			} catch (Exception e) {
				
			}
			
			int h = image.getHeight();
			try {
				if (infoMap.get("height").length()>0) {
					h = Integer.parseInt(infoMap.get("height"));
					isold2 = 1;
				}
			} catch (Exception e) {
				
			}
			if (isold1==1 && isold2==0) {
				h = w*h/image.getWidth();
			}
			if (isold1==0 && isold2==1) {
				w = w*h/image.getHeight();
			}
			
			int w_max = Integer.parseInt(MyConfig.img_width_max);
			if (w>w_max) {
				h = h*w_max/w;
				w = w_max;
			}
			
			String fp = imgscr_real_2.replaceAll("/[0-9]+.(gif|jpg|jpeg|png|bmp|JPG|JPEG)", "");
            File ff = new File(fp);
            if (!ff.exists()) {
         	   ff.mkdirs();
            }
			
			Thumbnails.of(imgscr_real_1).size(w, h).keepAspectRatio(false).toFile(imgscr_real_2);
			
			imgList_1.add(img_html);
			imgList_2.add(img_html.replace(infoMap.get("src"), imgscr_replace));
		}
		
		for (int i = 0; i < imgList_1.size(); i++) {
			content = content.replace(imgList_1.get(i), imgList_2.get(i));
		}
		
		return content;
	}
	
	/**
	 * 经内容转化为网页形式
	 * @param content
	 * @return
	 */
	public static String convertContentToHtml(String content) {
		content = content.replaceAll(MyConfig.bbs_img_replace, MyConfig.bbs_img);
		return content;
	}
	
	/**
	 * 获取html代码中的标签值，如获取img中的src、width、height
	 * @param html_str
	 * @param arr
	 * @return
	 */
	public static Map<String, String> getImgInfo(String html_str, String[] arr) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < arr.length; i++) {
			String tag = arr[i];
			if (tag.replaceAll(" ", "").length()>0) {
				String str = "";
				int a = html_str.indexOf(tag + "=\"");
				if (a>=0) {
					a = a+ tag.length() + 2;
					int b = html_str.indexOf("\"", a+1);
					if (b>a) {
						str = html_str.substring(a, b);
					}
				}
				map.put(tag, str);
			}
		}
		
		return map;
	}
}
