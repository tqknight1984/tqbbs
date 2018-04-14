package com.poly.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class PostMsg {
	
	public static int _HttpPost(String phone,String msg)
	{
		OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
        	String e_msg = java.net.URLEncoder.encode(msg+"			【小胖看车团】","utf-8");
        	String s_url="http://sms.chanzor.com:8001/sms.aspx?action=send&account=xiaopangkct&password=152900&mobile="+phone+"&content="+e_msg+"&sendTime=";
        	//String s_url = "http://www.10690071.com:9007/url_sms/struts/urlCurrentAction!toCurrentSms?userName=admin&compCode=blcm&pwd=5EFA1616F10DBF3F54E4998C571D3235&message=" + e_msg + "&phone=" + phone;
            URL realUrl = new URL(s_url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
           // conn.setRequestProperty("accept", "*/*");
           // conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1;");
            //conn.setRequestProperty("Content-type", "text/xml;charset=GBK"); 
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream());//,"GBK");
      
            //String param = "userName=admin&compCode=blcm&pwd=5EFA1616F10DBF3F54E4998C571D3235&message=" + e_msg + "&phone=" + phone;
            //String encodeStr = java.net.URLEncoder.encode(param,"GBK");
            // 发送请求参数
            //out.write(param);
            // flush输出流的缓冲
            out.flush();
            out.close();   
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        if(result.indexOf("<returnstatus>Success</returnstatus>")>0){
        	return 1;
        }else{
        	return 0;
        }
        
	}

	
}
