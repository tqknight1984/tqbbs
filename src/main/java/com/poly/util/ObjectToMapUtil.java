package com.poly.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
public class ObjectToMapUtil {
	public static String stringToAscii(String value)  
	{  
	    StringBuffer sbu = new StringBuffer();  
	    char[] chars = value.toCharArray();   
	    for (int i = 0; i < chars.length; i++) {  
	        if(i != chars.length - 1)  
	        {  
	            sbu.append((int)chars[i]).append(",");  
	        }  
	        else {  
	            sbu.append((int)chars[i]);  
	        }  
	    }  
	    return sbu.toString();  
	}

    public static  Map<String, String> mapToStringMap(Map<String, Object> from){
        Map<String, String> to = new HashMap<>();
        for (Map.Entry<String, Object> keyVal: from.entrySet()) {
            String key = keyVal.getKey();
            Object value = keyVal.getValue();
            if (value != null) {
                to.put(key, value.toString());
            }
        }
        return to;
    }

	public static  Map<String, String> changeToMap(Object model){
		Map<String, String> map = new HashMap<String, String>();
		if(model == null) {
			return map;
		}
		Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
		String newname = "";
		//StringUtils.parseRedisStr();
        try {
            for (int j = 0; j < field.length; j++) {
            	 // 遍历所有属性
                String name = field[j].getName(); // 获取属性的名字
                if("KEY".equals(name)||"COLUMN_FLAG".equals(name))
                	continue;
                newname = name.toLowerCase();
                name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
                String type = field[j].getGenericType().toString(); // 获取属性的类型
                if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = model.getClass().getMethod("get" + name);
                    String value = (String) m.invoke(model); // 调用getter方法获取属性值
                    if (value == null) 
                    	map.put(newname, "NULL");
                    else
                    	map.put(newname, value);
                    
                }
                if (type.equals("class java.lang.Integer")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Integer value = (Integer) m.invoke(model);
                    if (value == null) 
                    	map.put(newname, "0");
                    else
                    	map.put(newname, value+"");
                    
                }
                if (type.equals("int")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Integer value = (Integer) m.invoke(model);
                    if (value == null) 
                    	map.put(newname, "0");
                    else
                    	map.put(newname, value+"");
                    
                }
                if (type.equals("class java.lang.Boolean")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Boolean value = (Boolean) m.invoke(model);
                    if (value == null) 
                    	map.put(newname, "false");
                    else
                    	map.put(newname, ""+value);
                }
                if (type.equals("class java.lang.Long")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Long value = (Long) m.invoke(model);
                    if (value == null) 
                    	map.put(newname, "0.0");
                    else
                    	map.put(newname, ""+value);
                }
                if (type.equals("long")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Long value = (Long) m.invoke(model); 
                    if (value==null) 
                    	map.put(newname, "0.0");
                    else
                    	map.put(newname, ""+value);
                }
                if(type.equals("double")){
                	Method m = model.getClass().getMethod("get" + name);
                }
                if (type.equals("class java.util.Date")) {
                    Method m = model.getClass().getMethod("get" + name);
                    Date value = (Date) m.invoke(model);
                    if (value == null) 
                    	map.put(newname, "NULL");
                    else{
                    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    	String time = format.format(value);
                    	map.put(newname, time);
                    	
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
	}
}
