package com.poly.service.article;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

import com.mysql.jdbc.Blob;
import com.poly.dao.DBConnect;
import com.poly.dao.DBConnectionManager;
import com.poly.redis.RedisManager;
import com.poly.service.BaseService;
import com.poly.util.LuceneUtil;
import com.poly.util.ObjectToMapUtil;

@Service("articleService")
public class ArticleService extends BaseService {
	
	public void persistentArticle() throws ParseException {
		Jedis jedis = RedisManager.getJedisObject();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
			Set<String> set_up = jedis.zrangeByScore("tb_article_update", "-inf", "+inf");
			Set<String> set_in = jedis.zrangeByScore("tb_article_insert", "-inf", "+inf");
//			Set<String> set_del = jedis.zrangeByScore("tb_article_delete", "-inf", "+inf");
			BASE64Encoder encoder = new BASE64Encoder();
			int[] types = {java.sql.Types.INTEGER, java.sql.Types.VARCHAR, java.sql.Types.TIME, java.sql.Types.BLOB};
			
			//插入文章
			for (String id : set_in) {
				try {
					Map<String, String> map = jedis.hgetAll("tb_article:article_id:" + id);
					double score = 0;
					Double ss = jedis.zscore("tb_article:list:look", id + "");
					if (ss!=null) score = ss;
					String sql = "insert into tb_article(`article_id`,`user_id`,`article_title`,`article_content`,`article_markdown`,`plate_id`,"
							+ "`add_time`,`update_time`,`article_state`,"
							+ "`is_delete`,`article_lookcount`,`is_elite`,`article_order`) "
							+ "values("+ id +","+ map.get("user_id") +",?,?,?,"+ map.get("plate_id") +","
							+ "'"+ map.get("add_time") +"','"+ map.get("update_time") +"',"+ map.get("article_state") +","
							+ ""+ map.get("is_delete") +","+ score +","+ map.get("is_elite") +","+ map.get("article_order") +")";
					String content = encoder.encode(map.get("article_content").getBytes());
					String markdown = "";
					if (map.containsKey("article_markdown") && map.get("article_markdown")!=null && map.get("article_markdown").length()>0) {
						markdown = encoder.encode(map.get("article_markdown").getBytes());
					}
					jdbc.update(sql, new Object[]{map.get("article_title"),content,markdown}, new int [] {types[1],types[1],types[1]});

					jedis.zrem("tb_article_insert", id);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			//更新文章
			for (String id : set_up) {
				try {
					Map<String, String> map = jedis.hgetAll("tb_article:article_id:" + id);
					double score = 0;
					Double ss = jedis.zscore("tb_article:list:look", id + "");
					if (ss!=null) score = ss;
					String sql = "update tb_article set article_title=?, article_content=?, article_markdown=?, "
							+ "plate_id="+ map.get("plate_id") +", "
							+ "update_time='"+ map.get("update_time") +"', "
							+ "article_state="+ map.get("article_state") +", "
							+ "is_delete="+ map.get("is_delete") +", "
							+ "article_lookcount="+ score +", "
							+ "is_elite="+ map.get("is_elite") +", "
							+ "article_order="+ map.get("article_order") +" "
							+ "where article_id=" + id;
					String content = encoder.encode(map.get("article_content").getBytes());
					String markdown = "";
					if (map.containsKey("article_markdown") && map.get("article_markdown")!=null && map.get("article_markdown").length()>0) {
						markdown = encoder.encode(map.get("article_markdown").getBytes());
					}
					jdbc.update(sql, new Object[]{map.get("article_title"),content,markdown}, new int [] {types[1],types[1],types[1]});
					jedis.zrem("tb_article_update", id);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} finally {
			RedisManager.recycleJedisOjbect(jedis);
		}

	}
	
	public void toRedis() {
		String sql = "SELECT t1.*,t2.is_in FROM tb_article t1 LEFT JOIN tb_plate t2 ON t1.plate_id=t2.plate_id ORDER BY article_id DESC limit 1000";
		List<Map<String, Object>> list = jdbc.queryForList(sql);
		if (list!=null) {
			System.out.println("刷新 article 数据，总共 " + list.size() + " 条");

			BASE64Decoder decoder = new BASE64Decoder();
			Jedis jedis = RedisManager.getJedisObject();
//			int incr = 0;
			try {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					int article_id = (int) map.get("article_id");
//					incr = article_id;
					int is_delete = (int) map.get("is_delete");
					if (is_delete == 0) {
						int is_in = (int) map.get("is_in");
						
						map.remove("is_in");
						Map<String, String> map2 = ObjectToMapUtil.mapToStringMap(map);
						try {
							String content = new String(decoder.decodeBuffer(map2.get("article_content")));
							map2.put("article_content", content);
							if (map2.get("article_markdown")!=null && map2.get("article_markdown").length()>0) {
								String markdown = new String(decoder.decodeBuffer(map2.get("article_markdown")));
								map2.put("article_markdown", markdown);
							} else {
								map2.put("article_markdown", "");
							}
							
							Date date = (Date) map.get("add_time");
							long timer = date.getTime();
							jedis.hmset("tb_article:article_id:" + article_id, map2);
							
							if (map.get("article_state")!=null) {
								int state = (int) map.get("article_state");
								jedis.zadd("tb_article:list:article_state:" + state, timer, article_id + "");
							}
							if (!map2.get("article_state").equals("2")) {
								jedis.zadd("tb_article:list:plate_id:" + map2.get("plate_id"), timer, article_id + "");
								jedis.zadd("tb_article:list:user_id:" + map2.get("user_id"), timer, article_id + "");
								
								
								if (is_in == 0) {
									jedis.zadd("tb_article:list:all", timer, article_id + "");
								} else {
									jedis.zadd("tb_article:list:allin", timer, article_id + "");
								}
								
								//浏览量
								int lookCount = (int) map.get("article_lookcount");
								jedis.zadd("tb_article:list:look", lookCount, article_id + "");
								
								//置顶
								long article_order = (long) map.get("article_order");
								jedis.zadd("tb_article:list:article_order", article_order, article_id + "");
								
								//精华帖子
								int is_elite = (int) map.get("is_elite");
								if (is_elite == 1) {
									jedis.zadd("tb_article:list:is_elite:1", timer, article_id + "");
								}
								
						    	//创建搜索
						    	Map<String, String> searchMap = new HashMap<String, String>();
						    	searchMap.put("title", map2.get("article_title"));
						    	searchMap.put("userid", map2.get("user_id"));
						    	searchMap.put("plateid", map2.get("plate_id"));
						    	searchMap.put("id", map2.get("article_id"));
						    	searchMap.put("addtime", timer + "");
						    	try {
						    		LuceneUtil.insertorUpdateIndex(searchMap, 2,1);//全部帖子
						    		if(is_in == 0){
						    			LuceneUtil.insertorUpdateIndex(searchMap, 2,2);//外部帖子
						    		}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}

				String maxArticleIdSql = "SELECT MAX(article_id) FROM tb_article;";
				Integer maxArticleId = jdbc.queryForObject(maxArticleIdSql, Integer.class);
				String inc = jedis.get("tb_article_articleid_incr");
				try {
					int incI = Integer.parseInt(inc);
					if (incI > maxArticleId) {
						maxArticleId = incI;
					}
				}
				catch (Exception e) {
				}
				jedis.set("tb_article_articleid_incr", maxArticleId + "");  //创建自增主键

				System.out.println("article 数据加载完毕\n\n");
			}  finally {
				RedisManager.recycleJedisOjbect(jedis);
			}
		}
	}

	public void updateArticle() {
		Jedis jedis = RedisManager.getJedisObject();
		try {
			if(!jedis.exists("tb_article_new_in")){
				int[] types = {java.sql.Types.INTEGER, java.sql.Types.VARCHAR, java.sql.Types.TIME, java.sql.Types.BLOB};
				BASE64Encoder encoder = new BASE64Encoder();
				Set<String> set1 = jedis.zrangeByScore("tb_article:list:all", "-inf", "+inf");
				for (String id : set1) {
					String content1 = jedis.hget("tb_article:article_id:" + id, "article_content");
					String content = encoder.encode(content1.getBytes());
					
					String sql = "update tb_article set article_content=? where article_id=" + id;
					jdbc.update(sql, new Object[]{content}, new int[]{types[1]});
					jedis.zadd("tb_article_new_in", 0, id);
				}
				
				
				Set<String> set2 = jedis.zrangeByScore("tb_article:list:allin", "-inf", "+inf");
				
				for (String id : set2) {
					String content1 = jedis.hget("tb_article:article_id:" + id, "article_content");
					String content = encoder.encode(content1.getBytes());
					
					String sql = "update tb_article set article_content=? where article_id=" + id;
					jdbc.update(sql, new Object[]{content}, new int[]{types[1]});
					jedis.zadd("tb_article_new_in", 0, id);
				}
			}else {
				jedis.expire("tb_article_new_in", 3600);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.recycleJedisOjbect(jedis);
		}
	}
	
//	public void getTest() throws SQLException{
//		String sql = "SELECT t1.*,t2.is_in FROM tb_article t1 LEFT JOIN tb_plate t2 ON t1.plate_id=t2.plate_id ORDER BY article_id";
////		List<Map<String, Object>> list = jdbc.queryForList(sql);
////		if (list!=null) {
////			BASE64Decoder decoder = new BASE64Decoder();
////			Jedis jedis = RedisManager.getJedisObject();
////			try {
////				for (int i = 0; i < list.size(); i++) {
////					Map<String, Object> map = list.get(i);
////					Map<String, String> map2 = ObjectToMapUtil.changeToMap(map);
////					System.out.println(">>>>>>>  " + (i+1));
//////					try {
////////						String content = new String(decoder.decodeBuffer((String) map2.get("article_content")));
//////						
//////					} catch (IOException e) {
//////						e.printStackTrace();
//////					}
////					System.out.println(map.get("article_content"));
////					System.out.println(BLOB2String(map.get("article_content")));
////					
////				}
////				
////			}  finally {
////				RedisManager.recycleJedisOjbect(jedis);
////			}
////		}
//		
//		DBConnect dbc = null;
//		try {
//			dbc = new DBConnect(sql);
//			ResultSet rs = dbc.executeQuery();
//			byte[] data = null;
//			while (rs.next()) {
//				java.sql.Blob blob = rs.getBlob("article_content");
//				InputStream inStream = blob.getBinaryStream();
//                try {
//                    long nLen = blob.length();
//                    int nSize = (int) nLen;
//                    //System.out.println("img data size is :" + nSize);
//                    data = new byte[nSize];
//                    inStream.read(data);
//                    inStream.close();
//                    
//                    String str = new String(data);
//                    System.out.println(str);
//                } catch (IOException e) {
//                    System.out.println("失败");
//                }
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (dbc != null)
//					dbc.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	public void blobToMysql() {
//		try {
//			String str = "haha";
//			
////			ByteArrayInputStream     baisss     =     new     ByteArrayInputStream(bytes_zyjs);  
////			InputStreamReader     bais     =     new     InputStreamReader(baisss);
//			
//			ByteArrayInputStream bitestream = new ByteArrayInputStream(str.getBytes());
//			InputStreamReader reader = new InputStreamReader(bitestream);
//			
//			int[] types = {java.sql.Types.INTEGER, java.sql.Types.VARCHAR, java.sql.Types.TIME, java.sql.Types.BLOB};
//			String sql = "insert into tb_article(`article_id`,`user_id`,`article_title`,`article_content`,`plate_id`,"
//								+ "`add_time`,`update_time`,`article_state`,"
//								+ "`is_delete`,`article_lookcount`,`is_elite`,`article_order`) "
//						+ "values(4,2,?,?,2,"
//								+ "'2015-11-27 16:36:20','2015-11-27 16:36:20',1,"
//								+ "0,11,0,1)";
//			
//			jdbc.update(sql, new Object[]{"HAHA",bitestream}, new int [] {types[1],types[3]});
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
//	
//	public String BLOB2String(Object o) throws SQLException{  
//        String str = "---- 没有 ---";  
////        byte[] inbyte=null;  
////        java.sql.Blob blob = (Blob) o;
////        if (blob != null) {  
////            inbyte = blob.getBytes(1, (int) blob.length());  
////        }  
////        str =new String (inbyte);  
////        if(o instanceof java.sql.Blob){  
////            java.sql.Blob blob = (Blob) o;
//////        	Blob blob = (Blob) o;  
////            if (blob != null) {  
////                inbyte = blob.getBytes(1, (int) blob.length());  
////            }  
////            str =new String (inbyte);  
////        }  
//        return str;  
//    }  
}
