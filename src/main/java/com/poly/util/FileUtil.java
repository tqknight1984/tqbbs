package com.poly.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	
	/** 
     * 复制单个文件 
     * @param oldPath String 原文件路径 如：c:/fqf.txt 
     * @param newPath String 复制后路径 如：f:/fqf.txt 
     * @return boolean 
     */ 
   public static boolean copyFile(String oldPath, String newPath) { 
	   boolean result = false;
       try { 
           int bytesum = 0; 
           int byteread = 0; 
           File oldfile = new File(oldPath); 
           if (oldfile.exists()) { //文件存在时 
               InputStream inStream = new FileInputStream(oldPath); //读入原文件 
               String fp = newPath.replaceAll("/[0-9]+.(gif|jpg|jpeg|png|bmp)", "");
               File ff = new File(fp);
               if (!ff.exists()) {
            	   ff.mkdirs();
               }
               FileOutputStream fs = new FileOutputStream(newPath); 
               byte[] buffer = new byte[1444]; 
               int length; 
               while ( (byteread = inStream.read(buffer)) != -1) { 
                   bytesum += byteread; //字节数 文件大小 
                   System.out.println(bytesum); 
                   fs.write(buffer, 0, byteread); 
               } 
               inStream.close(); 
           }
           result = true;
       } 
       catch (Exception e) { 
           System.out.println("复制单个文件操作出错"); 
           e.printStackTrace();
           result = false;
       }
       
       return result;
   }
   
   /** 
    * 删除文件 
    * @param filePathAndName String 文件路径及名称 如c:/fqf.txt 
    * @param fileContent String 
    * @return boolean 
    */ 
  public static void delFile(String filePathAndName) { 
      try { 
          String filePath = filePathAndName; 
          filePath = filePath.toString(); 
          File myDelFile = new File(filePath); 
          myDelFile.delete();

      } 
      catch (Exception e) { 
          System.out.println("删除文件操作出错"); 
          e.printStackTrace();
      }

  }
  	/**
  	 * 读取本地文件（txt）
  	 * @param filePath
  	 * @return
  	 * @throws IOException 
  	 */
    public static List<String> readFile(String filePath) throws IOException {
        List<String> lines=new ArrayList<String>();  
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"GB2312"));  
        String line = null;  
        while ((line = br.readLine()) != null) {
        	if (line.length()>0) {
        		lines.add(line);
			}
        }  
        br.close();
        
        return lines;
    }
    public static void cleanFolder(String folderPath) {
    	try {
    		delAllFile(folderPath); //删除完里面所有内容
    	} catch (Exception e) {
    		e.printStackTrace(); 
    	}
    }
    public static void delFolder(String folderPath) {
        try {
           delAllFile(folderPath); //删除完里面所有内容
           String filePath = folderPath;
           filePath = filePath.toString();
           File myFilePath = new File(filePath);
           myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
          e.printStackTrace(); 
        }
   }
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
          return flag;
        }
        if (!file.isDirectory()) {
          return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
           if (path.endsWith(File.separator)) {
              temp = new File(path + tempList[i]);
           } else {
               temp = new File(path + File.separator + tempList[i]);
           }
           if (temp.isFile()) {
              temp.delete();
           }
           if (temp.isDirectory()) {
              delAllFile(path + File.separator + tempList[i]);//先删除文件夹里面的文件
              delFolder(path + File.separator + tempList[i]);
              flag = true;
           }
        }
        return flag;
      }
}
