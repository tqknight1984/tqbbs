package com.poly.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poly.bean.Message;
import com.poly.bean.PhoneCode;
import com.poly.redis.RedisDao;

public class PhoneMessageUtil {
	public static Logger logger = LoggerFactory.getLogger(PhoneMessageUtil.class);
	
	public static Message sendMessage(String phone,String code){
		Message message = new Message();
		String reg = "/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/";
		OutputStreamWriter out = null;
        BufferedReader in = null;
        String msg = "验证码："+code+"   【车团论坛】";
		try {        	
        	String e_msg = java.net.URLEncoder.encode(msg,"utf-8");
        	
        	String s_url = "http://www.10690071.com:9007/url_sms/struts/urlCurrentAction!toCurrentSms?userName=admin&compCode=blcm&pwd=5EFA1616F10DBF3F54E4998C571D3235&message=" + e_msg + "&phone=" + phone;
            URL realUrl = new URL(s_url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1;");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream());//,"GBK");
      
            // flush输出流的缓冲
            out.flush();
            out.close();   
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            if(out!=null){
                out.close();
            }
            if(in!=null){
                in.close();
            }   
            message.setCode("0000");
            message.setMsg("");
            PhoneCode phoneCode = new PhoneCode();
			phoneCode.setCode(code);
			phoneCode.setPhone(phone);
			phoneCode.setSendTime(new Date());
			Map<String,String> mp=ObjectToMapUtil.changeToMap(phoneCode);
			RedisDao.updateUserCodeByPhone(phone, mp);	
        } catch (Exception e) {
        	logger.info("发送 POST 请求出现异常！"); 
        	 message.setCode("1111");
             message.setMsg("发送短信失败");
            e.printStackTrace();
        }
		return message;
		
	}
}
