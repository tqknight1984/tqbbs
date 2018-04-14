package com.poly.controller.article;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.poly.util.MyConfig;

@Controller
@RequestMapping("/article")
public class ArtImgUp {
	
	@RequestMapping("/imgup")
	@ResponseBody
	public String upload(HttpSession session,MultipartHttpServletRequest multipartRequest){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		//文件保存目录路径
		String savePath = MyConfig.img_savePath + "temp/" + ymd + "/";
		//文件保存目录URL
		String saveUrl  = MyConfig.bbs_img + "temp/" + ymd + "/";
		
		//最大文件大小
		long maxSize = 8*1024*1024;
		
		//定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
		
		MultipartFile multipartFile = multipartRequest.getFile("imgFile");
	    String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
	    
	    if (extMap.get("image").contains(suffix)) {
	    	return getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get("image") + "格式。");
		}
	    
//	    if (multipartFile.getSize()>maxSize) {
//			return getError("上传文件大小超过限制（应该小于8M）。");
//		}
	    
	    String dirName = "image";
		savePath += dirName + "/";
		saveUrl += dirName + "/";
		
		File saveDirFile = new File(savePath);
		if (!saveDirFile.exists()) {//如果保存上传文件的文件夹不存在，创建文件夹
			saveDirFile.mkdirs();
		}
        
		FileOutputStream fileOutputStream = null;
		
		String name = System.currentTimeMillis() + "";
		String fileName = savePath +name+suffix;
		File writeFile = new File(fileName);
		try {
	    	fileOutputStream = new FileOutputStream(writeFile);
			fileOutputStream.write(multipartFile.getBytes());
		    fileOutputStream.flush();
		    JSONObject obj = new JSONObject();
			obj.put("error", 0);
			obj.put("url", saveUrl + name + suffix);
			return obj.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return getError("出现错误！");
		}
	}
	
	private String getError(String message) {
		JSONObject obj = new JSONObject();
		obj.put("error", 1);
		obj.put("message", message);
		return obj.toJSONString();
	}
}
