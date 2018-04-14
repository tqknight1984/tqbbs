package com.poly.test;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;

import com.poly.bean.User;
import com.poly.redis.RedisManager;
import com.poly.service.user.UserService;
import com.poly.util.MD5Util;
import com.poly.util.ObjectToMapUtil;

public class Tets {

	public static void main(String[] args) {
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	Set<String>keys = jedis.keys("tb_user:user_name*");
	    	for(String key:keys){
	    		String id = jedis.get(key);
	    		Map<String,String> umap = jedis.hgetAll("tb_user:user_id:"+id);
	    		String password = umap.get("user_password");
	    		umap.put("user_password", MD5Util.string2MD5(password));
	    		jedis.hmset("tb_user:user_id:"+id, umap);
	    		jedis.zadd("tb_user:user_id:update", 0,id);
	    	}
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }

	}

}
