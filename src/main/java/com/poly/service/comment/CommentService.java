package com.poly.service.comment;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

import com.poly.redis.RedisManager;
import com.poly.service.BaseService;
import com.poly.util.ObjectToMapUtil;

@Service("commentService")
public class CommentService extends BaseService {
	
	public void persistComment(){
		Jedis jedis = RedisManager.getJedisObject();
		try {
			Set<String> set_in = jedis.zrangeByScore("tb_comment_insert", "-inf", "+inf");
			
			BASE64Encoder encoder = new BASE64Encoder();
			int[] types = {java.sql.Types.INTEGER, java.sql.Types.VARCHAR, java.sql.Types.TIME};
			for (String id : set_in) {
				
				Map<String, String> map = jedis.hgetAll("tb_comment:com_id:" + id);
				if (map.get("com_content")==null) {
					System.out.println("com_content  >>>>> "+ id +"   >>>>>>>>>>>  " + map.get("com_content"));
					jedis.zrem("tb_comment_insert", id);
					jedis.zrem("tb_comment:list:user_id:" + map.get("user_id"), id);
					if (jedis.exists("tb_comment:list:article_userid:" + map.get("article_userid"))) {
						jedis.zrem("tb_comment:list:article_userid:" + map.get("article_userid"), id);
					}
					if (jedis.exists("tb_comment:list:com_userid:" + map.get("com_userid"))) {
						jedis.zrem("tb_comment:list:com_userid:" + map.get("com_userid"), id);
					}
					jedis.zrem("tb_comment:list:article_id:" + map.get("article_id"), id);
					if (jedis.exists("tb_comment:list:comment_id:" + map.get("comment_id"))) {
						jedis.zrem("tb_comment:list:comment_id:" + map.get("comment_id"), id);
					}
					if (jedis.exists("tb_comment:list:article_id_0:" + map.get("article_id"))) {
						jedis.zrem("tb_comment:list:article_id_0:" + map.get("article_id"), id);
					}
					
					jedis.zrem("tb_comment_insert", id);
					
					jedis.del("tb_comment:com_id:" + id);
					
					continue;
					
				}
				
				String sql = "insert into tb_comment(`com_id`,`article_userid`,`com_userid`,"
													+ "`article_id`,`comment_id`, `user_id`,"
													+ "`com_content`,`add_time`,`is_delete`) "
											+ "values("+map.get("com_id")+", "+map.get("article_userid")+", "+map.get("com_userid")+","
													+ map.get("article_id") +", "+ map.get("comment_id") +", "+ map.get("user_id") +","
													+ "?,'"+ map.get("add_time") +"',"+ map.get("is_delete") +")";
				try {
					String content = encoder.encode(map.get("com_content").getBytes());
					jdbc.update(sql, new Object[]{content}, new int[]{types[1]});
					jedis.zrem("tb_comment_insert", id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (jedis.exists("tb_comment_update")) {
				Set<String> set_up = jedis.zrangeByScore("tb_comment_update", "-inf", "+inf");
				for (String id : set_up) {
					
					Map<String, String> map = jedis.hgetAll("tb_comment:com_id:" + id);
					
					String sql = "update tb_comment set `com_content`=?, is_delete="+ map.get("is_delete") +" where `com_id`=" + map.get("com_id");
					try {
						String content = encoder.encode(map.get("com_content").getBytes());
						jdbc.update(sql, new Object[]{content}, new int[]{types[1]});
						jedis.zrem("tb_comment_update", id);
						
						if ("1".equals(map.get("is_delete"))) {
							
					    	if (!map.get("article_userid").equals(map.get("user_id"))) {
								jedis.zrem("tb_comment:list:article_userid:" + map.get("article_userid"), id);
							}
					    	
					    	if (!map.get("com_userid").equals(map.get("user_id"))) {
								jedis.zrem("tb_comment:list:com_userid:" + map.get("com_userid"), id);
							}
					    	jedis.zrem("tb_comment:list:article_id:" + map.get("article_id"), id);
					    	if ("0".equals(map.get("comment_id"))) {
					    		jedis.zrem("tb_comment:list:article_id_0:" + map.get("article_id"), id);
							}else {
								jedis.zrem("tb_comment:list:comment_id:" + map.get("comment_id"), id);
							}
					    	jedis.zrem("tb_comment:list:user_id:" + map.get("user_id"), id);
					    	
					    	jedis.del("tb_comment:com_id:" + id);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		} finally {
			RedisManager.recycleJedisOjbect(jedis);
		}
	}
	
	public void toRedis() {
		String sql = "SELECT * FROM tb_comment ORDER BY com_id";
		List<Map<String, Object>> list = jdbc.queryForList(sql);
		if (list!=null) {
			System.out.println("刷新 comment 数据，总共 " + list.size() + " 条");

			BASE64Decoder decoder = new BASE64Decoder();
			Jedis jedis = RedisManager.getJedisObject();
			int incr = 0;
			try {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					int com_id = (int) map.get("com_id");
					incr = com_id;
					int is_delete = (int) map.get("is_delete");
					if (is_delete == 0) {
						try {
							Date date = (Date) map.get("add_time");
							long timer = date.getTime();
							Map<String, String> map2 = ObjectToMapUtil.mapToStringMap(map);
							String content = new String(decoder.decodeBuffer(map2.get("com_content")));
							map2.put("com_content", content);
							jedis.hmset("tb_comment:com_id:" + com_id, map2);
							if (!map2.get("article_userid").equals(map2.get("user_id"))) {
								jedis.zadd("tb_comment:list:article_userid:" + map2.get("article_userid"), timer, com_id + "");
							}
							if (!"0".equals(map2.get("com_userid")) && !map2.get("com_userid").equals(map2.get("user_id"))) {
								jedis.zadd("tb_comment:list:com_userid:" + map2.get("com_userid"), timer, com_id + "");
							}
							jedis.zadd("tb_comment:list:article_id:" + map2.get("article_id"), timer, com_id + "");
							
							if (!"0".equals(map2.get("com_userid"))) {
					    		jedis.zadd("tb_comment:list:article_id_0:" + map2.get("article_id"), timer, com_id + "");
							}else {
								jedis.zadd("tb_comment:list:comment_id:" + map2.get("comment_id"), timer, com_id + "");
							}
					    	jedis.zadd("tb_comment:list:user_id:" + map2.get("user_id"), timer, com_id + "");
					    	
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if (jedis.exists("tb_comment_comid_incr")) {
					String inc = jedis.get("tb_comment_comid_incr");
					try {
						int ii = Integer.parseInt(inc);
						if (incr<ii) {
							incr = ii;
						}
					} catch (Exception e) {
					}
				}
				
				jedis.set("tb_comment_comid_incr", incr + "");

				System.out.println("comment 数据加载完毕\n\n");
			} finally {
				RedisManager.recycleJedisOjbect(jedis);
			}
			
		}
	}
}
