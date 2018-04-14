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
import Decoder.BASE64Encoder;

import com.poly.redis.RedisManager;
import com.poly.service.BaseService;
import com.poly.util.ObjectToMapUtil;

@Service("plateService")
public class PlateService extends BaseService {
	
	public void persistPlate() throws DataAccessException, NumberFormatException, ParseException {
		Jedis jedis = RedisManager.getJedisObject();
		try {
			Set<String> set_up = jedis.zrangeByScore("tb_plate_update", "-inf", "+inf");
			Set<String> set_in = jedis.zrangeByScore("tb_plate_insert", "-inf", "+inf");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			int[] types = {java.sql.Types.INTEGER, java.sql.Types.VARCHAR, java.sql.Types.DATE};
			for (String id : set_in) {
				Map<String, String> map = jedis.hgetAll("tb_plate:plate_id:" + id);
				String sql = "insert into tb_plate(`plate_id`,`plate_name`,`add_time`,`is_in`,`is_delete`) "
							+ "values("+ id +",?,'"+ map.get("add_time") +"',"+ map.get("is_in") +","+ map.get("is_delete") +")";
				jdbc.update(sql, new Object[]{map.get("plate_name")},new int[]{types[1]});
				jedis.zrem("tb_plate_insert", id);
				if (jedis.zscore("tb_plate_update", id)!=null) {
					jedis.zrem("tb_plate_update", id);
				}
				
//				jedis.zadd("tb_palte_db_has", 0, id);
			}
			
			for (String id : set_up) {
//				if (jedis.exists("tb_palte_db_has") && jedis.zscore("tb_palte_db_has", id)==null) {
//					for (int i = 1; i <= Integer.parseInt(id); i++) {
//						if (jedis.zscore("tb_palte_db_has", i + "")==null) {
//							Map<String, String> map = jedis.hgetAll("tb_plate:plate_id:" + i);
//							String sql = "insert into tb_plate(`plate_id`,`plate_name`,`add_time`,`is_delete`) "
//										+ "values("+ i +",?,'"+ map.get("add_time") +"',"+ map.get("is_delete") +")";
//							jdbc.update(sql, new Object[]{map.get("plate_name")},new int[]{types[1]});
//							jedis.zrem("tb_plate_insert", i + "");
//							if (jedis.zscore("tb_plate_update", i + "")!=null) {
//								jedis.zrem("tb_plate_update", i + "");
//							}
//							
//							jedis.zadd("tb_palte_db_has", 0, i + "");
//						}
//					}
//				}
				if (jedis.zscore("tb_plate_update", id)==null) {
					continue;
				}
				Map<String, String> map = jedis.hgetAll("tb_plate:plate_id:" + id);
				String sql = "update tb_plate set plate_name=? where plate_id=" + id;
				try {
					jdbc.update(sql, new Object[]{map.get("plate_name")},new int[]{types[1]});
					jedis.zrem("tb_plate_update", id);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		} finally {
			RedisManager.recycleJedisOjbect(jedis);
		}
	}
	
	
	public void toRedis() {
		String sql = "SELECT * FROM tb_plate ORDER BY plate_id";
		List<Map<String, Object>> list = jdbc.queryForList(sql);
		if (list!=null) {
			System.out.println("刷新 plate 数据，总共 " + list.size() + " 条");

			Jedis jedis = RedisManager.getJedisObject();
			int incr = 0;
			try {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					int plate_id = (int) map.get("plate_id");
					incr = plate_id;
					int is_delete = (int) map.get("is_delete");
					if (is_delete == 0) {
						try {
							Date date = (Date) map.get("add_time");
							long timer = date.getTime();
							Map<String, String> map2 = ObjectToMapUtil.mapToStringMap(map);
							jedis.hmset("tb_plate:plate_id:" + plate_id, map2);
							int isin = (int) map.get("is_in");
							if (isin == 1) {
								jedis.zadd("tb_plate:list:in", timer, plate_id + "");
							} else {
								jedis.zadd("tb_plate:list:all", timer, plate_id + "");
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				if (jedis.exists("tb_palte_plateid_incr")) {
					String inc = jedis.get("tb_palte_plateid_incr");
					try {
						int ii = Integer.parseInt(inc);
						if (incr<ii) {
							incr = ii;
						}
					} catch (Exception e) {
					}
				}
				
				jedis.set("tb_palte_plateid_incr", incr + "");

				System.out.println("plate 数据加载完毕\n\n");

			} finally {
				RedisManager.recycleJedisOjbect(jedis);
			}
			
			
		}
	}
}
