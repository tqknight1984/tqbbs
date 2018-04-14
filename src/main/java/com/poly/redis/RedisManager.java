package com.poly.redis;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.poly.util.MD5Util;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.poly.servlet.InitServlet;

public class RedisManager {

	private static JedisPool pool;
	
	private static int preTomIdx = 0;
	
	private static int timeout=2000;


	static {

		try {

			Properties props = new Properties();
			props.load(RedisManager.class.getClassLoader().getResourceAsStream("redis.properties"));

			JedisPoolConfig config = new JedisPoolConfig();
			config.setMinIdle(Integer.valueOf(props.getProperty("jedis.pool.minIdle")));
			config.setMaxIdle(Integer.valueOf(props.getProperty("jedis.pool.maxIdle")));
			config.setMaxWaitMillis(Long.valueOf(props.getProperty("jedis.pool.maxWaitMillis")));
			config.setTestOnBorrow(Boolean.valueOf(props.getProperty("jedis.pool.testOnBorrow")));
			config.setTestOnReturn(Boolean.valueOf(props.getProperty("jedis.pool.testOnReturn")));
			config.setMaxTotal(Integer.valueOf(props.getProperty("jedis.pool.maxActive")));

			preTomIdx = Integer.valueOf(props.getProperty("prepare.tomcat.index"));
			
			timeout = Integer.valueOf(props.getProperty("jedis.pool.timeout"));

			String password = props.getProperty("redis.password");
			if ("".equals(password)) {
				password = null;
			}
			pool = new JedisPool(config, props.getProperty("redis.ip"),
					Integer.valueOf(props.getProperty("redis.port")),
					timeout, password,
					Integer.valueOf(props.getProperty("redis.db.index")));
			
			System.out.println(">>> >>> redis is ready");

		} catch (IOException e) {

			e.printStackTrace();

		}

	}


	public static Jedis getJedisObject() {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
		} catch (Exception e) {
			if (jedis != null)
				pool.returnBrokenResource(jedis);
			jedis = pool.getResource();
			e.printStackTrace();
		}
		return jedis;
	}

	public static void recycleJedisOjbect(Jedis jedis) {
		pool.returnResource(jedis);
	}
	
	public static long getIdx(String key) {
		Long idx = -1l;
		Jedis jedis = getJedisObject();// ���jedisʵ��
		idx = jedis.incr(key);
		System.out.println("idx:" + idx);
		recycleJedisOjbect(jedis);
		return idx;
	}

	public static long getIdx() {
		String idxStr = "";
		int tom = preTomIdx;
		if (InitServlet.rootPath != null) {
			int begin = InitServlet.rootPath.indexOf("/Tomcat");
			if (begin > 0 && InitServlet.rootPath.length() >= begin + 8) {
				String tomFlag = InitServlet.rootPath.substring(begin + 7, begin + 8);
				tom = Integer.valueOf(tomFlag);
			}
		}
		idxStr = String.valueOf(tom);
		System.out.println("idxStr-----1--->" + idxStr);
		idxStr = idxStr + String.valueOf(System.currentTimeMillis());
		System.out.println("idxStr-----2--->" + idxStr);
		idxStr = idxStr + String.valueOf((int) (10 * Math.random()));
		System.out.println("idxStr-----3--->" + idxStr);
		return Long.valueOf(idxStr);
	}

	public static void main(String[] args) {
		Jedis jedis = getJedisObject();

		//清空tb_hot_img 相关的redis数据
//		jedis.set("tb_hot_img:id:next", "1");
//		jedis.zremrangeByRank("tb_hot_img:insert:ids", 0, -1);
//		jedis.zremrangeByRank("tb_hot_img:update:ids", 0, -1);
//		Set<String> ids = jedis.zrevrange("tb_hot_img:ids", 0, -1);
//		for (String id : ids) {
//			System.out.println(id);
//			jedis.del(id);
//		}
//		jedis.zremrangeByRank("tb_hot_img:ids", 0, -1);


//		Set<String> ids = jedis.zrevrange("tb_article_insert", 0, -1);
//		for (String id : ids) {
//			System.out.println(id);
//			System.out.println(jedis.hgetAll("tb_article:article_id:" + id));
//		}
//		jedis.del("tb_article:article_id:365");
//		jedis.zrem("tb_article_insert", "365");

//		jedis.set("tb_article_articleid_incr", "364");
//		System.out.println(jedis.get("tb_article_articleid_incr"));

//		System.out.println(jedis.get("tb_user:user_id:incr"));

//		System.out.println(jedis.get("tb_comment_comid_incr"));

//		System.out.println(jedis.get("tb_palte_plateid_incr"));

//		System.out.println(jedis.get("tb_plate_master_incr"));

		//修改严佳文手机号
		String key = "tb_user:user_id:14";
		jedis.hset(key, "user_phone", "13918608242");
		String newPassword = MD5Util.string2MD5("123456");
		System.out.println(newPassword);
		jedis.hset(key, "user_password", newPassword);
		Map<String, String> user = jedis.hgetAll(key);
		System.out.println(user);

		RedisManager.recycleJedisOjbect(jedis);
	}

}
