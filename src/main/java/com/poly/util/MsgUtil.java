package com.poly.util;

import java.util.Map;
import com.poly.bean.Msg;

public class MsgUtil {
	public static Msg mapToMsg(Map<String,String> map) throws Exception{
		Msg msg = null;
		if(map!=null && map.size()>=0){
			msg  = new Msg();
			Integer id = Integer.parseInt(map.get("id"));
			String from = map.get("from");
			String to = map.get("to");
			String content = map.get("content");
			long time = Long.parseLong(map.get("time"));
			msg.setContent(content);
			msg.setFrom(from);
			msg.setId(id);
			msg.setTime(time);
			msg.setTo(to);
		}
		return msg;
	}
}
