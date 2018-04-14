package com.poly.service.plate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.poly.redis.RedisManager;
import com.poly.service.BaseService;
import com.poly.util.ObjectToMapUtil;

@Service("plateMasterService")
public class PlateMasterService extends BaseService {

	public void persistPlateMaster() throws DataAccessException, NumberFormatException, ParseException {
		Jedis jedis = RedisManager.getJedisObject();
		try {
			Set<String> setdel = jedis.zrangeByScore("tb_plate_master_delete", "-inf", "+inf");
			Set<String> setin = jedis.zrangeByScore("tb_plate_master_insert", "-inf", "+inf");
			for (String id : setin) {
				Map<String, String> map = jedis.hgetAll("tb_plate_master:master_id:" + id);
				String sql = "insert into tb_plate_master(`master_id`,`user_id`,`plate_id`,`add_time`,`is_delete`) "
						+ "values(" + id + "," + map.get("user_id") + "," + map.get("plate_id") + ",'"
						+ map.get("add_time") + "'," + map.get("is_delete") + ")";
				jdbc.update(sql);
				jedis.zrem("tb_plate_master_insert", id);

				jedis.zrem("tb_plate_master_delete", id);
			}

			for (String id : setdel) {
				if (jedis.zscore("tb_plate_master_delete", id) != null) {
					String sql = "update tb_plate_master set is_delete=1 where master_id=" + id;
					jdbc.update(sql);
					jedis.del("tb_plate_master:master_id:" + id);
					jedis.zrem("tb_plate_master_delete", id);
				}
			}
		} finally {
			RedisManager.recycleJedisOjbect(jedis);
		}
	}

	public void toRedis() {
		String sql = "SELECT * FROM tb_plate_master ORDER BY master_id";
		List<Map<String, Object>> list = jdbc.queryForList(sql);
		if (list != null) {
			System.out.println("刷新 plateMaster 数据，总共 " + list.size() + " 条");

			Jedis jedis = RedisManager.getJedisObject();
			int incr = 0;
			try {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					if (map != null) {
						int master_id = (int) map.get("master_id");
						incr = master_id;
						int is_delete = (int) map.get("is_delete");
						if (is_delete == 0) {
							try {
								Date date = (Date) map.get("add_time");
								long timer = date.getTime();
								Map<String, String> map2 = ObjectToMapUtil.mapToStringMap(map);
								jedis.hmset("tb_plate_master:master_id:" + master_id, map2);
								jedis.zadd("tb_plate_master:list:plate_id:" + map2.get("plate_id"), timer,
										master_id + "");
								jedis.zadd("tb_plate_master:list:userid:plateid:" + map2.get("plate_id"), 0,
										map2.get("user_id"));
								jedis.zadd("tb_plate_master:list:user_id:" + map2.get("user_id"), timer,
										master_id + "");
								jedis.zadd("tb_plate_master:list:plateid:userid:" + map2.get("user_id"), timer,
										map2.get("plate_id"));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}

				if (jedis.exists("tb_plate_master_incr")) {
					String inc = jedis.get("tb_plate_master_incr");
					try {
						int ii = Integer.parseInt(inc);
						if (incr < ii) {
							incr = ii;
						}
					} catch (Exception e) {
					}
				}

				jedis.set("tb_plate_master_incr", incr + "");

				System.out.println("plateMaster 数据加载完毕\n\n");
			} finally {
				RedisManager.recycleJedisOjbect(jedis);
			}

		}
	}

}
