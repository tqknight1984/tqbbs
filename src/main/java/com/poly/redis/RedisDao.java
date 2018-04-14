package com.poly.redis;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.crypto.Data;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ZParams;

import com.poly.bean.Message;
import com.poly.bean.Msg;
import com.poly.bean.TbArticle;
import com.poly.bean.TbArticleElite;
import com.poly.bean.TbComment;
import com.poly.bean.TbPlate;
import com.poly.bean.TbPlateMaster;
import com.poly.bean.User;
import com.poly.util.ArticleHtmlUtil;
import com.poly.util.LuceneUtil;
import com.poly.util.MsgUtil;
import com.poly.util.MyConfig;
import com.poly.util.ObjectToMapUtil;
import com.poly.util.UserUtil;

public class RedisDao {
	public static User getUserByUsername(String username) throws Exception{
		User user = null;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	String id = jedis.get("tb_user:user_name:"+username);
	    	if(StringUtils.isNotEmpty(id)){
	    		Map<String,String> map = jedis.hgetAll("tb_user:user_id:"+id);
	    		user = UserUtil.mapToUser(map);
	    	}
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return user;
	}

	public static User getUserByPhone(String phone) throws Exception {
		User user = null;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	String id = jedis.get("tb_user:user_phone:"+phone);
	    	if(StringUtils.isNotEmpty(id)){
	    		Map<String,String> map = jedis.hgetAll("tb_user:user_id:"+id);
	    		user = UserUtil.mapToUser(map);
	    	}
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return user;
	}

	public static int saveUser(User user) {
		Jedis jedis = RedisManager.getJedisObject();
		Integer id = user.getUser_id();
	    try {
			if(id==null){
				id = jedis.incr("tb_user:user_id:incr").intValue();
				user.setUser_id(id);
				Map<String,String> map = ObjectToMapUtil.changeToMap(user);
				jedis.hmset("tb_user:user_id:"+user.getUser_id(), map);
				jedis.set("tb_user:user_name:"+user.getUser_name(),user.getUser_id()+"");
				jedis.zadd("tb_user:list:all",new Date().getTime(), id+"");
				String phone = user.getUser_phone();
				if(StringUtils.isNotEmpty(phone)){
					jedis.zadd("tb_user:list:user_state:0", 0, id+"");
					jedis.zadd("tb_user:list:role:0", 0,id+"");
					jedis.set("tb_user:user_phone:"+phone, id+"");
				}
				//建立用户名索引
				String username = user.getUser_name();
	    		String uid = user.getUser_id().toString();
	    		long time = user.getLog_time().getTime();
	    		for(int i=0;i<username.length();i++){
	    			for(int j=i;j<=username.length();j++){
	    				String key = username.substring(i,j);
	    				jedis.zadd("username:index:"+key, time,uid);
	    			}
	    		}
			}else{
				Map<String,String> map = ObjectToMapUtil.changeToMap(user);
				jedis.hmset("tb_user:user_id:"+user.getUser_id(), map);
				jedis.set("tb_user:user_name:"+user.getUser_name(),user.getUser_id()+"");
			}
			
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return id;
	}

	public static Map<String, String> getUserCodeMapByPhone(String phone) {
		Jedis jedis = RedisManager.getJedisObject();
		Map<String, String> result = new HashMap<String, String>();
	    try {
	    	result=jedis.hgetAll("tb_phone_code:phone:"+phone);
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	    return result;
	}

	public static void updateUserCodeByPhone(String phone,
			Map<String, String> mp) {
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	jedis.hmset("tb_phone_code:phone:"+phone,mp);
	    	
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		
	}
	/**
	 * 首页查询帖子
	 * @return 
	 */
	public static List<Map<String,Object>> getallplateAndarticles() {
		Jedis jedis = RedisManager.getJedisObject();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	    try {
	    	Set<String> set=jedis.zrange("tb_plate:list:all", 0, -1);
	    	if(set!=null&&set.size()>0){
	    		int i=0;
	    		for(String plateid:set){
	    			Map<String,Object> mapNew=new HashMap<String, Object>();
		    		Map<String,String> map=jedis.hgetAll("tb_plate:plate_id:"+plateid);
		    		String plate_name=map.get("plate_name").toString();
		    		Set<String> articleset=jedis.zrevrange("tb_article:list:plate_id:"+plateid, 0, 4);
		    		List<Map<String,String>> articleList=new ArrayList<Map<String,String>>();
		    		for(String articleid:articleset){
//		    			Map<String,String> articleMap=jedis.hgetAll("tb_article:article_id:"+articleid);
		    			String article_title=jedis.hget("tb_article:article_id:"+articleid, "article_title");
		    			Map<String,String> articleMapNew=new HashMap<String, String>();
//		    			articleMapNew.put("userid", articleMap.get("user_id"));
		    			articleMapNew.put("articleid", articleid);
		    			articleMapNew.put("article_title", article_title==null? "":article_title);
//		    			articleMapNew.put("username", jedis.hgetAll("tb_user:user_id:"+articleMap.get("user_id")).get("user_name"));
		    			articleList.add(articleMapNew);
		    		}
		    		mapNew.put("plateid", plateid);
		    		mapNew.put("platename", plate_name);
		    		mapNew.put("content", articleList);
		    		mapNew.put("id", i%6+1);
		    		list.add(mapNew);
		    		i++;
		    	}
	    	}
	    	return list;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	/**
	 * 首页热度贴
	 * @return 
	 */
	public static List<Map<String,Object>> gethotArticles() {
		Jedis jedis = RedisManager.getJedisObject();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	    try {
    		String keyp1="tb_article:list:all";
	    	String keyp2="tb_article:list:allin";
	    	String key=UUID.randomUUID().toString();
	    	Integer weights[] = {1,1};
			ZParams Op_params2 = new ZParams();
			Op_params2.weights(ArrayUtils.toPrimitive(weights));
	    	
	    	jedis.zunionstore(key, Op_params2,keyp1,keyp2);
	    	jedis.expire(key,60);
	    	Set<String> set=jedis.zrevrange(key, 0, 5);
	    	if(set!=null&&set.size()>0){
	    		for(String articleid:set){
//	    			   Map<String,String> map=jedis.hgetAll("tb_article:article_id:"+articleid);
	    			   String article_title=jedis.hget("tb_article:article_id:"+articleid, "article_title");
	    			   if(article_title!=null&&!article_title.equals("")){
	    				   Map<String,Object> mapNew=new HashMap<String, Object>();
	    				   mapNew.put("articleid", articleid);
	    				   mapNew.put("article_title", article_title);
//	    				   mapNew.put("article_content", ArticleHtmlUtil.convertContentToHtml(map.get("article_content")));
	    				   list.add(mapNew);
	    			   }
		    		 }
		    	}
	    	return list;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	/**
	 * 学习交流页面
	 * @return 
	 */
	public static List<Map<String,Object>> getallplateAndarticlesForlearn() {
		Jedis jedis = RedisManager.getJedisObject();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	    try {
	    	Set<String> set=jedis.zrange("tb_plate:list:all", 0, -1);
	    	System.out.println("-------111-------->>set.size:"+set.size());
	    	if(set!=null&&set.size()>0){
	    		int i=0;
	    		for(String plateid:set){
	    			Map<String,Object> mapNew=new HashMap<String, Object>();
	    			System.out.println("-------222-------->>key:tb_plate:plate_id:"+plateid);
		    		Map<String,String> map=jedis.hgetAll("tb_plate:plate_id:"+plateid);
		    		System.out.println("-------222-------->>map.size:"+map.size());
		    		String plate_name=map.get("plate_name").toString();
		    		Set<String> articleset=jedis.zrevrange("tb_article:list:plate_id:"+plateid, 0, 0);
		    		long articlecounts=jedis.zcard("tb_article:list:plate_id:"+plateid);
		    		String articleid="";
		    		 if(articleset!=null&&articleset.size()>0){
			    	    	Iterator<String> it = articleset.iterator();
			    	    	while(it.hasNext()){
			    	    		articleid=it.next();
			    	    	}
		    		 }
//		    		 Map<String,String> articleMap=jedis.hgetAll("tb_article:article_id:"+articleid);
		    		 String user_id=jedis.hget("tb_article:article_id:"+articleid, "user_id");
		    		 String articlename="";
//		    		 String lasttime="";
		    		 String userid="";
		    		 if(user_id!=null&&!user_id.equals("")){
		    			 String user_nickname=jedis.hgetAll("tb_user:user_id:"+user_id).get("user_nickname");
		    			 if(user_nickname==null||user_nickname.equals("")){
		    				 articlename=jedis.hgetAll("tb_user:user_id:"+user_id).get("user_name");
		    			 }
		    			 else{
		    				 articlename=user_nickname;
		    			 }
//		    			 lasttime=articleMap.get("add_time");
		    			 userid=user_id;
		    		 }
		    		mapNew.put("articlename", articlename);
//		    		mapNew.put("lasttime", lasttime);
		    		mapNew.put("userid", userid);
		    		mapNew.put("plateid", plateid);
		    		mapNew.put("platename", plate_name);
		    		mapNew.put("articlecounts", articlecounts);
		    		mapNew.put("id", i%9+1);
		    		//每个模块的前三条帖子
		    		List<Map<String,String>> articleList=new ArrayList<Map<String,String>>();
		    		Set<String> plateSet=jedis.zrevrange("tb_article:list:plate_id:"+plateid, 0, 2);
		    		if(plateSet!=null&&plateSet.size()>0){
		    	    	Iterator<String> it = plateSet.iterator();
		    	    	while(it.hasNext()){
		    	    		String articleid2=it.next();
		    	    		String article_title=jedis.hget("tb_article:article_id:"+articleid2, "article_title");
//		    	    		Map<String,String> map2=jedis.hgetAll("tb_article:article_id:"+articleid2);
		    	    		Map<String,String> plateMap=new HashMap<String, String>();
		    	    		plateMap.put("articleid",articleid2);
		    	    		plateMap.put("article_title",article_title);
		    	    		articleList.add(plateMap);
		    	    	}
	    		 }
		    		mapNew.put("articleList", articleList);
		    		list.add(mapNew);
		    		i++;
		    	}
	    	}
	    	return list;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	/**
	 * 项目日志页面
	 * @return 
	 */
	public static List<Map<String,Object>> getallplateAndarticlesForInlearn() {
		Jedis jedis = RedisManager.getJedisObject();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	    try {
	    	Set<String> set=jedis.zrange("tb_plate:list:in", 0, -1);
	    	if(set!=null&&set.size()>0){
	    		int i=0;
	    		for(String plateid:set){
	    			Map<String,Object> mapNew=new HashMap<String, Object>();
		    		Map<String,String> map=jedis.hgetAll("tb_plate:plate_id:"+plateid);
		    		String plate_name=map.get("plate_name").toString();
		    		Set<String> articleset=jedis.zrevrange("tb_article:list:plate_id:"+plateid, 0, 0);
		    		long articlecounts=jedis.zcard("tb_article:list:plate_id:"+plateid);
		    		String articleid="";
		    		 if(articleset!=null&&articleset.size()>0){
			    	    	Iterator<String> it = articleset.iterator();
			    	    	while(it.hasNext()){
			    	    		articleid=it.next();
			    	    	}
		    		 }
//		    		 Map<String,String> articleMap=jedis.hgetAll("tb_article:article_id:"+articleid);
		    		 String user_id=jedis.hget("tb_article:article_id:"+articleid, "user_id");
		    		 String articlename="";
//		    		 String lasttime="";
		    		 String userid="";
		    		 if(user_id!=null&&!user_id.equals("")){
		    			 articlename=jedis.hgetAll("tb_user:user_id:"+user_id).get("user_name");
//		    			 lasttime=articleMap.get("add_time");
		    			 userid=user_id;
		    		 }
		    		mapNew.put("articlename", articlename);
//		    		mapNew.put("lasttime", lasttime);
		    		mapNew.put("userid", userid);
		    		mapNew.put("plateid", plateid);
		    		mapNew.put("platename", plate_name);
		    		mapNew.put("articlecounts", articlecounts);
		    		mapNew.put("id", i%9+1);
		    		//每个模块的前三条帖子
		    		List<Map<String,String>> articleList=new ArrayList<Map<String,String>>();
		    		Set<String> plateSet=jedis.zrevrange("tb_article:list:plate_id:"+plateid, 0, 2);
		    		if(plateSet!=null&&plateSet.size()>0){
		    	    	Iterator<String> it = plateSet.iterator();
		    	    	while(it.hasNext()){
		    	    		String articleid2=it.next();
//		    	    		Map<String,String> map2=jedis.hgetAll("tb_article:article_id:"+articleid2);
		    	    		String article_title=jedis.hget("tb_article:article_id:"+articleid2, "article_title");
		    	    		Map<String,String> plateMap=new HashMap<String, String>();
		    	    		plateMap.put("articleid",articleid2);
		    	    		plateMap.put("article_title",article_title);
		    	    		articleList.add(plateMap);
		    	    	}
	    		 }
		    		mapNew.put("articleList", articleList);
		    		list.add(mapNew);
		    		i++;
		    	}
	    	}
	    	return list;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	/**
	 *查询全部外部帖子的id
	 * @return 
	 */
	public static List<String> getallarticlesidByplateid(String plateid) {
		Jedis jedis = RedisManager.getJedisObject();
		List<String> cids = new ArrayList<String>();
	    try {
	    	Set<String> set=new HashSet<String>();
	    	String key2=UUID.randomUUID().toString();
	    	String key=UUID.randomUUID().toString();
	    	String key1="";
	    	if(plateid.equals("0")){
	    		 String key4="tb_article:list:article_order";
	    		 key1="tb_article:list:all";
	    		jedis.zinterstore(key2, key1,key4);
	    		jedis.expire(key2, 60);
	    		
	    	}
	    	else{
	    		key1="tb_article:list:plate_id:"+plateid;
	    		String kp="tb_article:list:article_order";
	    		
	    		jedis.zinterstore(key2, key1,kp);
	    		jedis.expire(key2, 60);
	    		
	    	}
	    	Integer weights[] = {1,2};
			ZParams Op_params2 = new ZParams();
			Op_params2.weights(ArrayUtils.toPrimitive(weights));
			jedis.zunionstore(key,Op_params2, key1,key2);
			jedis.expire(key, 60);
    	     set=jedis.zrevrange(key, 0, -1);
			for(String id:set){
				cids.add(id);
			}
	    	return cids;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	/**
	 *查询全部外部帖子的id
	 * @return 
	 */
	public static List<String> getallAndinarticlesidByplateid(String plateid) {
		Jedis jedis = RedisManager.getJedisObject();
		List<String> cids = new ArrayList<String>();
	    try {
	    	Set<String> set=new HashSet<String>();
	    	String key2="tb_article:list:article_order";
	    	String key=UUID.randomUUID().toString();
	    	String key1="";
	    	if(plateid.equals("0")){
	    		String keyp1="tb_article:list:all";
		    	String keyp2="tb_article:list:allin";
		    	key1=UUID.randomUUID().toString();
		    	Integer weights[] = {1,1};
				ZParams Op_params2 = new ZParams();
				Op_params2.weights(ArrayUtils.toPrimitive(weights));
		    	
		    	jedis.zunionstore(key1, Op_params2,keyp1,keyp2);
		    	jedis.expire(key1,60);
		    	
	    	}
	    	else{
	    		key2=UUID.randomUUID().toString();
	    		key1="tb_article:list:plate_id:"+plateid;
	    		String kp="tb_article:list:article_order";
	    		
	    		jedis.zinterstore(key2, key1,kp);
	    		jedis.expire(key2, 60);
	    	}
	    	Integer weights[] = {1,2};
			ZParams Op_params2 = new ZParams();
			Op_params2.weights(ArrayUtils.toPrimitive(weights));
			jedis.zunionstore(key,Op_params2, key1,key2);
			jedis.expire(key, 60);
    	     set=jedis.zrevrange(key, 0, -1);
			for(String id:set){
				cids.add(id);
			}
	    	return cids;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	
	/**
	 *根据模块和用户查询外部帖子的id
	 * @return 
	 */
	public static List<String> 	getallarticlesidByplateidAnduserid(String plateid,String userid) {
		Jedis jedis = RedisManager.getJedisObject();
		List<String> cids = new ArrayList<String>();
	    try {
	    	String key1="tb_article:list:user_id:"+userid;
	    	String key2="";
	    	Set<String> set=new HashSet<String>();
	    	if(plateid.equals("0")){
	    		key2="tb_article:list:all";
	    	}
	    	else{
	    		key2="tb_article:list:plate_id:"+plateid;
	    	}
			Integer weights[] = {0,1};
			ZParams Op_params2 = new ZParams();
			Op_params2.weights(ArrayUtils.toPrimitive(weights));
			String key=UUID.randomUUID().toString();
			jedis.zinterstore(key,Op_params2, key1,key2);
			jedis.expire(key, 60);
			 set=jedis.zrevrange(key, 0, -1);	
			for(String id:set){
				cids.add(id);
			}
	    	return cids;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	/**
	 *根据模块和用户查询外部和内部帖子的id
	 * @return 
	 */
	public static List<String> 	getallAndinarticlesidByplateidAnduserid(String plateid,String userid) {
		Jedis jedis = RedisManager.getJedisObject();
		List<String> cids = new ArrayList<String>();
	    try {
	    	String key1="tb_article:list:user_id:"+userid;
	    	String key2="";
	    	Set<String> set=new HashSet<String>();
	    	if(plateid.equals("0")){
	    		String key3="tb_article:list:all";
	    		String key4="tb_article:list:allin";
	    		key2=UUID.randomUUID().toString();
	    		jedis.zunionstore(key2, key3,key4);
	    		jedis.expire(key2, 60);
	    	}
	    	else{
	    		key2="tb_article:list:plate_id:"+plateid;
	    	}
			Integer weights[] = {0,1};
			ZParams Op_params2 = new ZParams();
			Op_params2.weights(ArrayUtils.toPrimitive(weights));
			String key=UUID.randomUUID().toString();
			jedis.zinterstore(key,Op_params2, key1,key2);
			jedis.expire(key, 60);
			 set=jedis.zrevrange(key, 0, -1);	
			for(String id:set){
				cids.add(id);
			}
	    	return cids;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	
	/**
	 *根据模块和用户查询所有的帖子
	 * @return 
	 */
	public static List<Map<String,String>> getarticlesbyplateidAnduserid(List<String> cids,String userid,int start,int end) {
		Jedis jedis = RedisManager.getJedisObject();
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
	    try {
		    	for(int i=start;i<=end;i++){
		    		String articleid=cids.get(i);
		    		Map<String,String> mapNew=new HashMap<String, String>();
		    		Map<String,String> map=getArticleWithoutContent(articleid);
//		    		Map<String,String> map=jedis.hgetAll("tb_article:article_id:"+articleid);
		    		long count=jedis.zcard("tb_comment:list:article_id_0:"+articleid);
		    	    Set<String> set=jedis.zrevrange("tb_comment:list:article_id_0:"+articleid, 0, 0);
		    	    String backname="";
		    	    String backtime="";
		    	    String com_userid=""; 
		    	    if(set!=null&&set.size()>0){
		    	    	Iterator<String> it = set.iterator();
		    	    	String commentid="";
		    	    	while(it.hasNext()){
		    	    		commentid=it.next();
		    	    	}
		    	    	Map<String,String> comm_map=jedis.hgetAll("tb_comment:com_id:"+commentid);
		    	    	if(comm_map!=null&&comm_map.size()>0){
		    	    		backtime=comm_map.get("add_time");
		    	    		com_userid=comm_map.get("user_id");
		    	    		backname=jedis.hgetAll("tb_user:user_id:"+com_userid).get("user_name");
		    	    	}
		    	    }
		    	    mapNew.put("article_title",map.get("article_title"));
		    	    mapNew.put("article_id",articleid);
		    	    mapNew.put("platename",jedis.hgetAll("tb_plate:plate_id:"+map.get("plate_id")).get("plate_name"));
		    	    mapNew.put("username", jedis.hgetAll("tb_user:user_id:"+map.get("user_id")).get("user_name"));
		    	    mapNew.put("backcount", String.valueOf(count));
//		    	    mapNew.put("add_time", map.get("add_time"));
		    	    mapNew.put("add_time", map.get("update_time"));
		    	    mapNew.put("is_elite", map.get("is_elite"));
		    	    mapNew.put("article_order", map.get("article_order"));
//		    	    double lookcount=jedis.zscore("tb_article:list:look", articleid + "");
		    	    String lookcountSt=String.valueOf(jedis.zscore("tb_article:list:look", articleid + ""));
		    	    if(lookcountSt==null||lookcountSt.equals("null")){
	    	    	mapNew.put("article_lookcount","0");
		    	    }
		    	    else{
		    	    	double lookcount=jedis.zscore("tb_article:list:look", articleid + "");
		    	    	mapNew.put("article_lookcount",(int)lookcount+"");
		    	    }
		    		if(!backname.equals("")&&!backtime.equals("")){
		    			mapNew.put("backname", backname);
		    			mapNew.put("backtime", backtime);
		    			mapNew.put("backuserid", com_userid);
		    		}
		    		else{
		    			mapNew.put("backname", jedis.hgetAll("tb_user:user_id:"+map.get("user_id")).get("user_name"));
		    			mapNew.put("backtime", map.get("add_time"));
		    			mapNew.put("backuserid",map.get("user_id"));
		    		}
		    		list.add(mapNew);
		    	}
	    	return list;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}

	public static Map<String,String> getLastUserByArticleId(String article_id) throws Exception{
		Jedis jedis = RedisManager.getJedisObject();
		Map<String,String> last = new HashMap<String,String>();
	    try {
//	    	Map<String,String> map=jedis.hgetAll("tb_article:article_id:"+article_id);
	    	Map<String,String> map=getArticleWithoutContent(article_id);
			long count=jedis.zcard("tb_comment:list:article_id_0:"+article_id);
			if(count <= 0){
				Map<String,String> umap = jedis.hgetAll("tb_user:user_id:"+map.get("user_id"));
				last.put("last_user_name", umap.get("user_name"));
				last.put("last_user_id", umap.get("user_id"));
				last.put("last_time", map.get("update_time"));
			}else{
				String comment_id=jedis.zrevrange("tb_comment:list:article_id_0:"+article_id, 0, 0).iterator().next();
				Map<String,String> comm_map=jedis.hgetAll("tb_comment:com_id:"+comment_id);
				String user_id = comm_map.get("user_id");
				Map<String,String> umap = jedis.hgetAll("tb_user:user_id:"+user_id);
				last.put("last_user_name", umap.get("user_name"));
				last.put("last_user_id", umap.get("user_id"));
				last.put("last_time", comm_map.get("add_time"));
			}
			return last;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		
	}
	public static Map<String,String> getUserMessage(String userid){
		Jedis jedis = RedisManager.getJedisObject();
		Map<String,String> map=new HashMap<String,String>();
	    try {
	    	String username="";
	    	String user_photo="";
	    	Map<String,String> usermap=jedis.hgetAll("tb_user:user_id:"+userid);
	    	if(usermap!=null&&usermap.size()>0){
	    		username=usermap.get("user_name");
	    		user_photo= usermap.get("user_photo");
	    	}
	    	long count=jedis.zcard("tb_article:list:user_id:"+userid);
	    	map.put("user_name",username );
	    	map.put("user_photo",user_photo);
	    	map.put("count", String.valueOf(count));
	    	return map;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		
	}
	/**
	 *根据模块查询所有的帖子
	 * @return 
	 */
	public static List<Map<String,String>> getarticlesbyplateid(List<String> cids,int start,int end) {
		Jedis jedis = RedisManager.getJedisObject();
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
	    try {
		    	for(int i=start;i<=end;i++){
		    		String articleid=cids.get(i);
		    		Map<String,String> mapNew=new HashMap<String, String>();
		    		Map<String,String> map=getArticleWithoutContent(articleid);
//		    		Map<String,String> map=jedis.hgetAll("tb_article:article_id:"+articleid);
		    		long count=jedis.zcard("tb_comment:list:article_id_0:"+articleid);
		    	    Set<String> set=jedis.zrevrange("tb_comment:list:article_id_0:"+articleid, 0, 0);
		    	    String backname="";
		    	    String backtime="";
		    	    String com_userid=""; 
		    	    if(set!=null&&set.size()>0){
		    	    	Iterator<String> it = set.iterator();
		    	    	String commentid="";
		    	    	while(it.hasNext()){
		    	    		commentid=it.next();
		    	    	}
		    	    	Map<String,String> comm_map=jedis.hgetAll("tb_comment:com_id:"+commentid);
		    	    	if(comm_map!=null&&comm_map.size()>0){
		    	    		backtime=comm_map.get("add_time");
		    	    	    com_userid=comm_map.get("user_id");
		    	    		backname=jedis.hgetAll("tb_user:user_id:"+com_userid).get("user_name");
		    	    	}
		    	    }
		    	    mapNew.put("article_title",map.get("article_title"));
		    	    mapNew.put("article_id",map.get("article_id"));
		    	    mapNew.put("platename",jedis.hgetAll("tb_plate:plate_id:"+map.get("plate_id")).get("plate_name"));
		    	    mapNew.put("username", jedis.hgetAll("tb_user:user_id:"+map.get("user_id")).get("user_name"));
		    	    mapNew.put("userid", map.get("user_id"));
		    	    mapNew.put("backcount", String.valueOf(count));
//		    	    mapNew.put("add_time", map.get("add_time"));
		    	    mapNew.put("add_time", map.get("update_time"));
		    	    
		    	    mapNew.put("is_elite", map.get("is_elite"));
		    	    mapNew.put("article_order", map.get("article_order"));
//		    	    double lookcount=jedis.zscore("tb_article:list:look", articleid + "");
		    	    String lookcountSt=String.valueOf(jedis.zscore("tb_article:list:look", articleid + ""));
		    	    if(lookcountSt==null||lookcountSt.equals("null")){
	    	    	mapNew.put("article_lookcount","0");
		    	    }
		    	    else{
		    	    	double lookcount=jedis.zscore("tb_article:list:look", articleid + "");
		    	    	mapNew.put("article_lookcount",(int)lookcount+"");
		    	    }
		    		if(!backname.equals("")&&!backtime.equals("")){
		    			mapNew.put("backname", backname);
		    			mapNew.put("backtime", backtime);
		    			mapNew.put("backuserid", com_userid);
		    		}
		    		else{
		    			mapNew.put("backname", jedis.hgetAll("tb_user:user_id:"+map.get("user_id")).get("user_name"));
		    			mapNew.put("backtime", map.get("add_time"));
		    			mapNew.put("backuserid", map.get("user_id"));
		    		}
		    		list.add(mapNew);
		    	}
	    	return list;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	/**
	 *查询外部所有的模块
	 * @return 
	 */
	public static List<Map<String,String>> getallplate() {
		Jedis jedis = RedisManager.getJedisObject();
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
	    try {
	    	Set<String> set=jedis.zrange("tb_plate:list:all", 0, -1);
	    	if(set!=null&&set.size()>0){
	    		for(String plateid:set){
	    			Map<String,String> map=jedis.hgetAll("tb_plate:plate_id:"+plateid);
	    			list.add(map);
	    		}
	    	}
	    	return list;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	/**
	 *查询外部和内部所有的模块
	 * @return 
	 */
	public static List<Map<String,String>> getallAndinplate() {
		Jedis jedis = RedisManager.getJedisObject();
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
	    try {
	    	String key1="tb_plate:list:all";
	    	String key2="tb_plate:list:in";
	    	String key=UUID.randomUUID().toString();
	    	jedis.zunionstore(key, key1,key2);
	    	jedis.expire(key,60);
	    	Set<String> set=jedis.zrange(key, 0, -1);
	    	if(set!=null&&set.size()>0){
	    		for(String plateid:set){
	    			Map<String,String> map=jedis.hgetAll("tb_plate:plate_id:"+plateid);
	    			list.add(map);
	    		}
	    	}
	    	return list;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	/**
	 *根据id查询模块的名称
	 * @return 
	 */
	public static String getplatenamebyid(String plateid) {
		Jedis jedis = RedisManager.getJedisObject();
		String platename="";
	    try {
	    	Map<String,String> map=jedis.hgetAll("tb_plate:plate_id:"+plateid);
	    	if(map!=null&&map.size()>0){
	    		platename=map.get("plate_name");
	    	}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	    return platename;
	}
	/**
	 *根据id查询用戶的名称
	 * @return 
	 */
	public static String getusernamebyid(String id) {
		Jedis jedis = RedisManager.getJedisObject();
		String username="";
	    try {
	    	Map<String,String> map=jedis.hgetAll("tb_user:user_id:"+id);
	    	if(map!=null&&map.size()>0){
	    		username=map.get("user_name");
	    	}
	    	return username;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	public static void main(String []arg){
		cleanMsg();
	}	
	/**
	 * 保存帖子
	 * @param tbArticle
	 * @return 帖子的id
	 * @throws IOException 
	 */
	public static int article_save(TbArticle tbArticle, int isin) throws IOException {
		int result = 0;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	long nowtimer = System.currentTimeMillis();
	    	long article_id = jedis.incr("tb_article_articleid_incr");
	    	
	    	String newContent = tbArticle.getArticle_content();
	    	if (tbArticle.getArticle_markdown()!=null) {
	    		newContent = ArticleHtmlUtil.dealContent4(article_id + "", tbArticle.getArticle_content());
			} else {
				newContent = ArticleHtmlUtil.dealContent(article_id + "", tbArticle.getArticle_content());
			}
	    	tbArticle.setArticle_content(newContent);
	    	
	    	tbArticle.setArticle_id((int) article_id);
	    	Map<String, String> map = ObjectToMapUtil.changeToMap(tbArticle);
	    	jedis.hmset("tb_article:article_id:" + article_id, map);
	    	jedis.zadd("tb_article:list:plate_id:" + tbArticle.getPlate_id(), nowtimer, article_id + "");
	    	jedis.zadd("tb_article:list:user_id:" + tbArticle.getUser_id(), nowtimer, article_id + "");
	    	jedis.zadd("tb_article:list:article_state:", nowtimer, article_id + "");
	    	if (jedis.zscore("tb_plate:list:in", tbArticle.getPlate_id() + "")==null) {
	    		jedis.zadd("tb_article:list:all", nowtimer, article_id + "");
			}else {
				jedis.zadd("tb_article:list:allin", nowtimer, article_id + "");
			}
	    	
	    	jedis.zadd("tb_article_insert", 0, article_id + "");
	    	result = (int) article_id;
	    	
	    	//创建搜索
	    	Map<String, String> searchMap = new HashMap<String, String>();
	    	searchMap.put("title", map.get("article_title"));
	    	searchMap.put("userid", map.get("user_id"));
	    	searchMap.put("plateid", map.get("plate_id"));
	    	searchMap.put("id", map.get("article_id"));
	    	searchMap.put("addtime", nowtimer + "");
	    	try {
	    		LuceneUtil.insertorUpdateIndex(searchMap, 2,1);//全部帖子
	    		if(isin==0){
	    			LuceneUtil.insertorUpdateIndex(searchMap, 2,2);//外部帖子
	    		}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	    return result;
	}
	
//	/**
//	 * 将帖子中的图片进行处理
//	 * @param content
//	 * @param articleId
//	 * @return
//	 */
//	private static String dealContent(String content, String articleId) {
//		String str = MyConfig.bbs_img + "temp/";
//		Pattern pp = Pattern.compile(str + "[0-9]{8}/image/" + "[0-9]+.(gif|jpg|jpeg|png|bmp)");
//		Matcher m = pp.matcher(content);
//		while(m.find()){
//			String s1 = m.group(0);
//			String t1 = s1.replace(MyConfig.bbs_img, MyConfig.img_savePath);
//			String t2 = t1.replaceAll("temp/[0-9]{8}", articleId);
//			FileUtil.copyFile(t1, t2);
//			FileUtil.delFile(t1);
//		}
//		
//		return content.replaceAll(MyConfig.bbs_img + "temp/[0-9]{8}", MyConfig.bbs_img + articleId);
//	}
	
	public static Map<String, String> article_getDetail(int articleid) {
		Map<String, String> resultMap = null;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	if (jedis.exists("tb_article:article_id:" + articleid)) {
	    		resultMap = new HashMap<String, String>();
	    		resultMap = jedis.hgetAll("tb_article:article_id:" + articleid);
		    	resultMap.put("article_content", ArticleHtmlUtil.convertContentToHtml(resultMap.get("article_content")));
		    	Map<String, String> userMap = jedis.hgetAll("tb_user:user_id:" + resultMap.get("user_id"));
		    	Map<String, String> plateMap = jedis.hgetAll("tb_plate:plate_id:" + resultMap.get("plate_id"));
		    	resultMap.put("article_username", userMap.get("user_name"));
		    	resultMap.put("article_userphoto", getUserPoto(userMap.get("user_photo")));
		    	resultMap.put("article_userRegtime", userMap.get("reg_time"));
		    	resultMap.put("article_plateName", plateMap.get("plate_name"));
		    	long acount = jedis.zcount("tb_article:list:user_id:" + resultMap.get("user_id"), "-inf", "+inf");
		    	resultMap.put("article_tcount", (int)acount + "");
		    	double score = 0;
		    	try {
		    		score = jedis.zscore("tb_article:list:look", articleid + "");
				} catch (Exception e) {
				}
		    	
		    	resultMap.put("lookCount", (int) score + "");  //查看数
		    	
		    	double replyCount = jedis.zcount("tb_comment:list:article_id_0:" + articleid, "-inf", "+inf");
		    	resultMap.put("replyCount", (int)replyCount + "");  //回复数
			}
	    	
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		
		return resultMap;
		
	}

	public static Map<String, String> article_getDetail(int articleid, int user_id) {
		Map<String, String> resultMap = null;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	if (jedis.exists("tb_article:article_id:" + articleid) 
	    			&& "0".equals(jedis.hget("tb_article:article_id:" + articleid, "is_delete"))) {
	    		//判断是否为内部帖子
	    		int isin = 0;
	    		int canLook = 0;
	    		
	    		resultMap = new HashMap<String, String>();
	    		resultMap = jedis.hgetAll("tb_article:article_id:" + articleid);
	    		
	    		if (jedis.zscore("tb_article:list:allin", articleid + "")!=null) {
					isin = 1;
				}
	    		
	    		if (isin==0) {
					canLook =1;
				} else if (user_id>0 && jedis.zscore("tb_user:list:role:1", user_id + "")!=null) {
					canLook =1;
				}
	    		
	    		resultMap.put("article_isin", isin + "");
	    		resultMap.put("article_canlook", canLook + "");
	    		
	    		if (canLook==1) {
	    			resultMap.put("article_content", ArticleHtmlUtil.convertContentToHtml(resultMap.get("article_content")));
			    	Map<String, String> userMap = jedis.hgetAll("tb_user:user_id:" + resultMap.get("user_id"));
			    	Map<String, String> plateMap = jedis.hgetAll("tb_plate:plate_id:" + resultMap.get("plate_id"));
			    	
			    	if (isin==0) {
			    		if (userMap.get("user_nickname")!=null || "".equals(userMap.get("user_nickname")) || "null".equals(userMap.get("user_nickname"))) {
			    			resultMap.put("article_username", userMap.get("user_name"));
						} else {
							resultMap.put("article_username", userMap.get("user_nickname"));
						}
			    	} else {
			    		resultMap.put("article_username", userMap.get("user_name"));
					}
			    	
			    	resultMap.put("article_userphoto", getUserPoto(userMap.get("user_photo")));
			    	resultMap.put("article_userRegtime", userMap.get("reg_time"));
			    	resultMap.put("article_plateName", plateMap.get("plate_name"));
			    	long acount = jedis.zcount("tb_article:list:user_id:" + resultMap.get("user_id"), "-inf", "+inf");
			    	resultMap.put("article_tcount", (int)acount + "");
			    	
			    	double score = 0;
			    	if (jedis.exists("tb_article:list:look") && jedis.zscore("tb_article:list:look", articleid + "")!=null) {
			    		score = jedis.zscore("tb_article:list:look", articleid + "");
					}
			    	
			    	resultMap.put("lookCount", (int) score + "");  //查看数
			    	
			    	double replyCount = jedis.zcount("tb_comment:list:article_id_0:" + articleid, "-inf", "+inf");
			    	resultMap.put("replyCount", (int)replyCount + "");  //回复数
				}
	    		
			}
	    	
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		
		return resultMap;
		
	}
	
	private static String getUserPoto(String photo) {
		if (photo!=null && photo.length()>3) {
			return MyConfig.bbs_img + photo;
		}else {
			return MyConfig.bbs_url + MyConfig.userPhoto_defaut;
		}
	}

	public static boolean plate_add(TbPlate tbPlate) {
		boolean result = false;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	String key = "tb_plate:list:all";
	    	if (tbPlate.getIs_in()==1) {
	    		key = "tb_plate:list:in";
			}
	    	long id = jedis.incr("tb_palte_plateid_incr");
	    	tbPlate.setPlate_id((int) id);
	    	Map<String, String> map = ObjectToMapUtil.changeToMap(tbPlate);
	    	jedis.hmset("tb_plate:plate_id:" + id, map);
	    	jedis.zadd(key, System.currentTimeMillis(), id + "");
	    	jedis.zadd("tb_plate_insert", System.currentTimeMillis(), id + "");
	    	result = true;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		
		return result;
	}


	public static List<Map<String, String>> plate_list() {
		List<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	Set<String> set = jedis.zrange("tb_plate:list:all", 0, -1);
	    	for (String id : set) {
				Map<String, String> map = jedis.hgetAll("tb_plate:plate_id:" + id);
				if (jedis.exists("tb_plate_master:list:plate_id:" + map.get("plate_id"))) {
					map.put("master_count", jedis.zcard("tb_plate_master:list:plate_id:" + map.get("plate_id")) + "");
				} else {
					map.put("master_count", "0");
				}
				resultList.add(map);
			}
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return resultList;
	}
	
	public static int plate_isin(int plateid) {
		int result = 0;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			if (jedis.zscore("tb_plate:list:all", plateid + "")==null) {
				result = 1;
			}
		} finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return result;
	}

	public static Set<String>  getArticleIdListByUserId(int user_id){
		Set<String> set = null;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	set = jedis.zrevrangeByScore("tb_article:list:user_id:"+user_id,"+inf","-inf");
	    	
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	    return set;
	}
	public static Map<String,String> getPlateInfoById(String id){
		Map<String,String> plate = null;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	plate = jedis.hgetAll("tb_plate:plate_id:" + id);
	    	
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return plate;
	}

	public static boolean comment_add(TbComment tbComment) {
		boolean result = false;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	String newContent = ArticleHtmlUtil.dealContent(tbComment.getArticle_id() + "", tbComment.getCom_content());
	    	tbComment.setCom_content(newContent);
	    	
	    	Map<String, String> artMap = jedis.hgetAll("tb_article:article_id:" + tbComment.getArticle_id());
	    	tbComment.setArticle_userid(Integer.parseInt(artMap.get("user_id")));
	    	if (tbComment.getComment_id()==0) {
				tbComment.setCom_userid(0);
			}else {
				Map<String, String> comMap = jedis.hgetAll("tb_comment:com_id:" + tbComment.getComment_id());
				tbComment.setCom_userid(Integer.parseInt(comMap.get("user_id")));
			}
	    	
	    	long comid = jedis.incr("tb_comment_comid_incr");
	    	tbComment.setCom_id((int) comid);
	    	
	    	Map<String, String> comMap = ObjectToMapUtil.changeToMap(tbComment);
	    	
	    	long timer = System.currentTimeMillis();
	    	String id = comid + "";
	    	
	    	jedis.hmset("tb_comment:com_id:" + id, comMap);
	    	if (tbComment.getArticle_userid()!=tbComment.getUser_id()) {
	    		jedis.zadd("tb_comment:list:article_userid:" + comMap.get("article_userid"), timer, id);
			}
	    	if (tbComment.getCom_userid()!=tbComment.getUser_id()) {
	    		jedis.zadd("tb_comment:list:com_userid:" + comMap.get("com_userid"), timer, id);
	    	}
	    	jedis.zadd("tb_comment:list:article_id:" + comMap.get("article_id"), timer, id);
	    	if (tbComment.getComment_id()==0) {
	    		jedis.zadd("tb_comment:list:article_id_0:" + comMap.get("article_id"), timer, id);
			}else {
				jedis.zadd("tb_comment:list:comment_id:" + comMap.get("comment_id"), timer, id);
			}
	    	jedis.zadd("tb_comment:list:user_id:" + comMap.get("user_id"), timer, id);
	    	jedis.zadd("tb_comment_insert", 0, id);
	    	
	    	result = true;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		
		return result;
	}

	public static List<Map<String, Object>> comment_getDetailByArticleId(int articleid) {
		List<Map<String, Object>> resuList = null;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	if (jedis.exists("tb_comment:list:article_id_0:" + articleid)) {
	    		resuList = new ArrayList<Map<String, Object>>();
				Set<String> comSet = jedis.zrange("tb_comment:list:article_id_0:" + articleid, 0, -1);
				for (String comid : comSet) {
					Map<String, String> cMap = jedis.hgetAll("tb_comment:com_id:" + comid);
					cMap.put("com_content", ArticleHtmlUtil.convertContentToHtml(cMap.get("com_content")));
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("com1", cMap);
					Map<String, String> userMap = jedis.hgetAll("tb_user:user_id:" + cMap.get("user_id"));
					userMap.put("user_photo", getUserPoto(userMap.get("user_photo")));
					long acount = jedis.zcount("tb_article:list:user_id:" + userMap.get("user_id"), "-inf", "+inf");
					userMap.put("article_tcount", (int)acount + "");
					map.put("userInfo", userMap);
					if (jedis.exists("tb_comment:list:comment_id:" + cMap.get("com_id"))) {
						List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
						Set<String> cSet = jedis.zrange("tb_comment:list:comment_id:" + cMap.get("com_id"), 0, -1);
						for (String mm : cSet) {
							Map<String, String> mMap = jedis.hgetAll("tb_comment:com_id:" + mm);
							mMap.put("com_content", ArticleHtmlUtil.convertContentToHtml(mMap.get("com_content")));
							Map<String, String> uMap = jedis.hgetAll("tb_user:user_id:" + mMap.get("user_id"));
							mMap.put("user_name", uMap.get("user_name"));
							mMap.put("user_photo", getUserPoto(uMap.get("user_photo")));
							mList.add(mMap);
						}
						map.put("com2", mList);
					}else {
						map.put("com2", null);
					}
					resuList.add(map);
				}
			}
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		
		return resuList;
	}
	public static Set<String> getCommeentsByArticleUserId(Integer user_id) {
		String key = "tb_comment:list:article_userid:"+user_id;
		Set<String> set = null;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	set = jedis.zrevrangeByScore(key,"+inf","-inf");
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	    return set;
	}
	public static Map<String,String> getCommentInfoByCommengId(String comment_id){
		Map<String,String> comment = null;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	comment = jedis.hgetAll("tb_comment:com_id:" + comment_id);
	    	
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return comment;
	}

	public static int getArticleSizeByUserId(int user_id) {
		int size = 0;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	Set<String> set = jedis.zrange("tb_article:list:user_id:"+user_id,0,-1);
	    	size = set.size();
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return size;
	}

	public static Map<String, String> getUserByUserId(String user_id) {
		Map<String, String> map = null;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	map = jedis.hgetAll("tb_user:user_id:"+user_id);
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return map;
	}

	public static Set<String> getCommentsListByUserId(Integer user_id) {
		String key = "tb_comment:list:user_id:"+user_id;
		Set<String> set = null;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	set = jedis.zrevrangeByScore(key,"+inf","-inf");
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	    return set;
	}

	public static Map<String, String> article_getContent(int user_id, int articleid) {
		Map<String, String> resultMap = null;
		int ii = 0;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	if (jedis.exists("tb_article:article_id:" + articleid) ) {
	    		resultMap = jedis.hgetAll("tb_article:article_id:" + articleid);
		    	
		    	resultMap.put("article_content", ArticleHtmlUtil.convertContentToHtml(resultMap.get("article_content")));
		    	if (resultMap!=null) {
					String uid = resultMap.get("user_id");
					if (uid.equals(user_id + "")) {
						if (jedis.exists("tb_article:list:user_id:" + user_id) 
		    			&& jedis.zscore("tb_article:list:user_id:" + user_id, articleid + "")!=null) {
							ii = 1;
							resultMap.put("is_master", "0");
						}
					} else {
						if (jedis.exists("tb_plate_master:list:userid:plateid:" + resultMap.get("plate_id")) && jedis.zscore("tb_plate_master:list:userid:plateid:" + resultMap.get("plate_id"),user_id + "")!=null) {
							ii = 1;
							resultMap.put("is_master", "1");
						}
					}
				}
			}
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		
	    if (ii==1) {
			return resultMap;
		}else {
			resultMap = null;
			return null;
		}
	}

	public static int article_update(int uid, TbArticle tbArticle) throws IOException {
		int result = 0;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	if (tbArticle.getArticle_id()>0) {
	    		
	    		Map<String, String> artMap = jedis.hgetAll("tb_article:article_id:" + tbArticle.getArticle_id());
	    		boolean power = false;
	    		if (artMap.get("user_id").equals(uid + "")) {
					power = true;
				}else {
					if (jedis.exists("tb_plate_master:list:userid:plateid:" + artMap.get("plate_id")) 
							&& jedis.zscore("tb_plate_master:list:userid:plateid:" + artMap.get("plate_id"), uid + "")!=null) {
						power = true;
					}
				}
	    		
	    		if (power) {
	    			String newContent = tbArticle.getArticle_content();
	    			if (tbArticle.getArticle_markdown()!=null) {
	    	    		newContent = ArticleHtmlUtil.dealContent4(tbArticle.getArticle_id() + "", tbArticle.getArticle_content());
	    			} else {
	    				newContent = ArticleHtmlUtil.dealContent(tbArticle.getArticle_id() + "", tbArticle.getArticle_content());
	    			}
	    			
	    			tbArticle.setArticle_content(newContent);
			    	
			    	String id = tbArticle.getArticle_id() + "";
//			    	Map<String, String> map = ObjectToMapUtil.changeToMap(tbArticle);
			    	jedis.hset("tb_article:article_id:" + id, "article_content", tbArticle.getArticle_content());
			    	jedis.hset("tb_article:article_id:" + id, "article_markdown", tbArticle.getArticle_markdown());
			    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					jedis.hset("tb_article:article_id:" + id, "update_time", format.format(new Date()));
			    	jedis.zadd("tb_article_update", System.currentTimeMillis(), id);
			    	result = tbArticle.getArticle_id();
			    	
			    	String title1 = jedis.hget("tb_article:article_id:" + id, "article_title");
			    	String plate1 = jedis.hget("tb_article:article_id:" + id, "plate_id");
			    	long timer = System.currentTimeMillis();
			    	try {
			    		double ddd = jedis.zscore("tb_article:list:plate_id:" + plate1, id);
			    		timer = (long) ddd;
					} catch (Exception e) {
					}
			    	boolean ischange = false;
			    	if (!title1.equals(tbArticle.getArticle_title())) {  //修改标题
			    		jedis.hset("tb_article:article_id:" + id, "article_title", tbArticle.getArticle_title());
			    		ischange = true;
					}
			    	if (!plate1.equals(tbArticle.getPlate_id() + "")) {  //修改所属板块
			    		jedis.hset("tb_article:article_id:" + id, "plate_id", tbArticle.getPlate_id()+"");
			    		
			    		jedis.zrem("tb_article:list:plate_id:" + plate1, id);
			    		jedis.zadd("tb_article:list:plate_id:" + tbArticle.getPlate_id(), timer, tbArticle.getArticle_id() + "");
				    	
				    	if (jedis.zscore("tb_plate:list:in", tbArticle.getPlate_id() + "")==null) { //板块--不属于内部板块
				    		jedis.zrem("tb_article:list:allin", id);
				    		jedis.zadd("tb_article:list:all", timer, id);
						}else {
							jedis.zrem("tb_article:list:all", id);
							jedis.zadd("tb_article:list:allin", timer, id);
						}
				    	
				    	ischange = true;
					}
			    	
			    	if (ischange) {
			    		try {
							LuceneUtil.deleteIndex(tbArticle.getArticle_id() + "");
							
							//创建搜索
					    	Map<String, String> searchMap = new HashMap<String, String>();
					    	searchMap.put("title", tbArticle.getArticle_title());
					    	searchMap.put("userid", tbArticle.getUser_id() + "");
					    	searchMap.put("plateid", tbArticle.getPlate_id() + "");
					    	searchMap.put("id", id);
					    	searchMap.put("addtime", timer + "");
					    	
					    	LuceneUtil.insertorUpdateIndex(searchMap, 2,1);//全部帖子
					    	if(jedis.zscore("tb_plate:list:in", tbArticle.getPlate_id() + "")!=null){
				    			LuceneUtil.insertorUpdateIndex(searchMap, 2,2);//外部帖子
				    		}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
			    	
				}
			}
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		
		return result;
	}

	public static boolean plate_update(TbPlate tbPlate) {
		boolean result = false;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	jedis.hset("tb_plate:plate_id:" + tbPlate.getPlate_id(), "plate_name", tbPlate.getPlate_name());
	    	jedis.zadd("tb_plate_update", 0, tbPlate.getPlate_id() + "");
	    	result = true;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		
		return result;
	}

	public static void addUserUpdateList(int id) {
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	String key_insert = "tb_user:user_id:insert";
	    	String key_update = "tb_user:user_id:update";
	    	Set<String>insert_Ids = jedis.zrange(key_insert, 0, -1);
	    	boolean isInsert = true;
	    	for(String iid:insert_Ids){
	    		if(iid.equals(id+"")){
	    			isInsert = false;
	    			break;
	    		}
	    	}
	    	if(isInsert){
	    		jedis.zadd(key_update,new Date().getTime(),id+"");
	    	}
	    	
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}

	public static void addUserInsertList(int id) {
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	String key_insert = "tb_user:user_id:insert";
	    	jedis.zadd(key_insert,new Date().getTime(),id+"");
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	public static Set<String> getInsertIds(){
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	String key_insert = "tb_user:user_id:insert";
	    	Set<String>insert_Ids = jedis.zrevrange(key_insert,0,-1);
	    	return insert_Ids;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	
	/**
	 * 增加查看数量
	 * @param articleid
	 */
	public static void article_addLookCount(int articleid) {
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	if (jedis.exists("tb_article:article_id:" + articleid)) {
	    		jedis.zincrby("tb_article:list:look", 1, articleid + "");
	    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	    		jedis.hset("tb_article:article_id:" + articleid, "update_time", format.format(new Date()));
		    	jedis.zadd("tb_article_update", System.currentTimeMillis(), articleid + "");
			}
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	
	/**
	 * 删除帖子
	 * @param article_id
	 * @return -1-帖子不存在，0-删除失败，1-删除成功
	 * @throws IOException 
	 */
	public static int article_delete(int article_id) throws IOException {
		int result = 0;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	if (jedis.exists("tb_article:article_id:" + article_id)) {
	    		Map<String, String> map = jedis.hgetAll("tb_article:article_id:" + article_id);
				jedis.zrem("tb_article:list:plate_id:" + map.get("plate_id"), article_id + "");
				jedis.zrem("tb_article:list:user_id:" + map.get("user_id"), article_id + "");
				jedis.zrem("tb_article:list:article_state:" + map.get("article_state"), article_id + "");
				jedis.zrem("tb_article:list:all", article_id + "");
				jedis.zrem("tb_article:list:allin", article_id + "");
				jedis.zrem("tb_article:list:look", article_id + "");
				jedis.zrem("tb_article:list:article_order", article_id + "");
				
//				jedis.zadd("tb_article_delete", 0, article_id + "");
				jedis.hset("tb_article:article_id:" + article_id, "is_delete", "1");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				jedis.hset("tb_article:article_id:" + article_id, "update_time", format.format(new Date()));
				jedis.zadd("tb_article_update", System.currentTimeMillis(), article_id + "");
				LuceneUtil.deleteIndex(article_id + "");
				
				//删除评论
				comment_deleteByArticleid(article_id);
				
				result = 1;
			}else {
				result = -1;
			}
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return result;
	}
	
	/**
	 * 添加精华帖
	 * @param article_id
	 * @param user_id
	 * @return -1：不合法添加，0：添加失败，1：添加成功
	 */
	public static int elite_add(int article_id, int user_id) {
		int result = 0;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			if (jedis.exists("tb_article:article_id:" + article_id)) { //判断添加的文章是否合法
				Map<String, String> artMap = jedis.hgetAll("tb_article:article_id:" + article_id);
				if (jedis.exists("tb_plate_master:list:user_id:" + user_id)  //判断添加精华帖的操作者是否合法
						&& jedis.zscore("tb_plate_master:list:user_id:" + user_id, artMap.get("plate_id"))!=null) {
					long id = jedis.incr("tb_article_elite_incr");
					long timer = System.currentTimeMillis();
					TbArticleElite tbArticleElite = new TbArticleElite();
					tbArticleElite.setElite_id((int) id);
					tbArticleElite.setAdd_time(new Date());
					tbArticleElite.setAdd_user(user_id);
					tbArticleElite.setArticle_id(article_id);
					tbArticleElite.setIs_delete(0);
					
					Map<String, String> map = ObjectToMapUtil.changeToMap(tbArticleElite);
					jedis.hmset("tb_article_elite:elite_id:" + id, map);
					jedis.zadd("tb_plate_master:list:plate_id:" + artMap.get("plate_id"), timer, id + "");
					jedis.zadd("tb_article_elite:list:add_user:" + user_id, timer, id + "");
					jedis.zadd("tb_article_elite:list:all", timer, id + "");
					jedis.zadd("tb_article_elite_insert", timer, id + "");
					result = 1;
				} else {
					result = -1;
				}
			} else {
				result = -1;
			}
		} finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return result;
	}
	
	/**
	 * 添加版主
	 * @param plate_id
	 * @param user_id
	 * @return 0-添加失败，1-添加成功
	 */
	public static int master_add(int plate_id, int user_id) {
		int result = 0;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			if (jedis.exists("tb_user:user_id:" + user_id) && jedis.exists("tb_plate:plate_id:" + plate_id)) {
				long id = jedis.incr("tb_plate_master_incr");
				long timer = System.currentTimeMillis();
				TbPlateMaster tbPlateMaster = new TbPlateMaster();
				tbPlateMaster.setAdd_time(new Date());
				tbPlateMaster.setIs_delete(0);
				tbPlateMaster.setMaster_id((int) id);
				tbPlateMaster.setPlate_id(plate_id);
				tbPlateMaster.setUser_id(user_id);
				
				Map<String, String> map = ObjectToMapUtil.changeToMap(tbPlateMaster);
				jedis.hmset("tb_plate_master:master_id:" + id, map);
				jedis.zadd("tb_plate_master:list:plate_id:" + plate_id, timer, id + "");
				jedis.zadd("tb_plate_master:list:user_id:" + user_id, timer, id + "");
				jedis.zadd("tb_plate_master_insert", timer, id + "");
				result = 1;
			}
		} finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return result;
	}
	
	/**
	 * 板块排序（板块交换位置）
	 * @param id_1 靠前的板块
	 * @param id_2 靠后的板块
	 * @param isin 
	 * @return
	 */
	public static int plate_order(int id_1, int id_2, int isin) {
		int result = 0;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			String key = "tb_plate:list:all";
			if (isin==1) {
				key = "tb_plate:list:in";
			}
			double score1 = jedis.zscore(key, id_1 + "");
			double score2 = jedis.zscore(key, id_2 + "");
			double scorex = score1-score2;
			if (scorex==0) {
				score1 = (int) (Math.random() * 100);
				score2 = (int) (Math.random() * 100);
				if (score1>score2) {
					jedis.zincrby(key, score1, id_2 + "");
					jedis.zincrby(key, score2, id_1 + "");
				}else {
					jedis.zincrby(key, score2, id_2 + "");
					jedis.zincrby(key, score1, id_1 + "");
				}
				
			}else {
				jedis.zincrby(key, scorex, id_2 + "");
				jedis.zincrby(key, -1*scorex, id_1 + "");
			}
			result = 1;
		} finally {
			RedisManager.recycleJedisOjbect(jedis);
		}
		return result;
	}
	
	/**
	 * 获取内部板块列表
	 * @return
	 */
	public static List<Map<String, String>> plate_listin() {
		List<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	Set<String> set = jedis.zrangeByScore("tb_plate:list:in", "-inf", "+inf");
	    	for (String id : set) {
				Map<String, String> map = jedis.hgetAll("tb_plate:plate_id:" + id);
				if (jedis.exists("tb_plate_master:list:plate_id:" + map.get("plate_id"))) {
					map.put("master_count", jedis.zcard("tb_plate_master:list:plate_id:" + map.get("plate_id")) + "");
				} else {
					map.put("master_count", "0");
				}
				resultList.add(map);
			}
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return resultList;
	}

	public static List<User> getUserList(Integer role, Integer state, String username) throws Exception {
		List<User> users = new ArrayList<User>();
		Jedis jedis = RedisManager.getJedisObject();
		String temp_key = "tb_user:list:role-state:";
	    try {
	    	if(StringUtils.isNotEmpty(username)){
	    		if(jedis.exists("tb_user:user_name:"+username)){
	    			String id = jedis.get("tb_user:user_name:"+username);
	    			Map<String,String>umap = jedis.hgetAll("tb_user:user_id:"+id);
		    		User user = UserUtil.mapToUser(umap);
		    		if(role!=null&&role!=-1){
		    			if(state!=null&&state!=-1){
		    				if(user.getRole()==role&&user.getUser_state()==state){
				    			users.add(user);
				    		}
		    			}else{
		    				if(user.getRole()==role){
				    			users.add(user);
				    		}
		    			}
		    		}else{
		    			users.add(user);
		    		}
	    		}
	    		return users;
	    	}
	    	List<String> keyList = new ArrayList<String>();
	    	List<Integer> wightList = new ArrayList<Integer>();
	    	keyList.add("tb_user:list:all");
	    	wightList.add(1);
	    	if(role!=null&&role!=-1){
	    		keyList.add("tb_user:list:role:"+role);
	    		wightList.add(0);
	    		temp_key += role;
	    	}
	    	if(state!=null&&state!=-1){
	    		keyList.add("tb_user:list:user_state:"+state);
	    		wightList.add(0);
	    		temp_key += "-"+role;
	    	}
	    	String[] keyArr = new String[keyList.size()];
			Integer[] weightArr = new Integer[wightList.size()];
			keyList.toArray(keyArr);
			wightList.toArray(weightArr);
			ZParams Op_params = new ZParams();
			Op_params.weights(ArrayUtils.toPrimitive(weightArr));
	    	jedis.zinterstore(temp_key, Op_params, keyArr);
	    	Set<String>ids = jedis.zrange(temp_key, 0, -1);
	    	for(String id:ids){
	    		Map<String,String>umap = jedis.hgetAll("tb_user:user_id:"+id);
	    		User user = UserUtil.mapToUser(umap);
	    		users.add(user);
	    	}
	    } finally {
	    	jedis.expire(temp_key, 20);
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return users;
	}

	public static void editUser(User user, User uold) {
		int role_old = uold.getRole();
		int role = user.getRole();
		int user_id = user.getUser_id();
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	uold.setRole(role);
	    	saveUser(uold);
	    	addUserUpdateList(user_id);
	    	jedis.zrem("tb_user:list:role:"+role_old, user_id+"");
	    	jedis.zadd("tb_user:list:role:"+role, 0,user_id+"");
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}

	public static void deleteUser(Integer user_id) {
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	Map<String,String> umap = getUserByUserId(user_id.toString());
	    	umap.put("user_state", "1");
	    	jedis.hmset("tb_user:user_id:"+user_id, umap);
	    	jedis.zadd("tb_user:list:user_state:1", 0,user_id+"");
	    	jedis.zrem("tb_user:list:user_state:0", user_id+"");
	    	//删除用户名索引
	    	String username = umap.get("user_name");
    		String uid = umap.get("user_id");
    		for(int i=0;i<username.length();i++){
    			for(int j=i;j<=username.length();j++){
    				String key = username.substring(i,j);
    				jedis.zrem("username:index:"+key, uid);
    			}
    		}
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		
	}
	

	public static Map<String, String> plate_searchUser(String key) {
		Map<String, String> resultMap = null;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			String userid = "";
			if (jedis.exists("tb_user:user_name:" + key)) {
				userid = jedis.get("tb_user:user_name:" + key);
			} else if (jedis.exists("tb_user:user_phone:" + key)) {
				userid = jedis.get("tb_user:user_phone:" + key);
			}
			if (!userid.equals("")&&!userid.equals("null")&&userid!=null) {
				resultMap = jedis.hgetAll("tb_user:user_id:" + userid);
			}
		} finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return resultMap;
	}

	public static int plate_masterAdd(int isin, int userid, int plateid) {
		int result = 0;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			String key = "tb_plate_master:list:userid:plateid:" + plateid;
			if (jedis.exists(key) && jedis.zscore(key, userid + "")!=null) {
				result = -1;
			} else {
				long id = jedis.incr("tb_plate_master_incr");
				TbPlateMaster master = new TbPlateMaster();
				master.setMaster_id((int) id);
				master.setPlate_id(plateid);
				master.setUser_id(userid);
				master.setIs_delete(0);
				master.setAdd_time(new Date());
				
				long timer = System.currentTimeMillis();
				Map<String, String> map = ObjectToMapUtil.changeToMap(master);
				jedis.hmset("tb_plate_master:master_id:" + id, map);
				jedis.zadd("tb_plate_master:list:plate_id:" + plateid, timer, id+"");
				jedis.zadd("tb_plate_master:list:userid:plateid:" + plateid, 0, userid+"");
				jedis.zadd("tb_plate_master:list:user_id:" + userid, timer, id+"");
				jedis.zadd("tb_plate_master:list:plateid:userid:" + userid, timer, plateid+"");
				jedis.zadd("tb_plate_master_insert", timer, id + "");
				result = 1;
			}
		} finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		
		return result;
	}

	public static List<Map<String, String>> plate_getMasterList(int isin) {
		List<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
		Jedis jedis = RedisManager.getJedisObject();
		try {
			String key = "tb_plate:list:all";
			if (isin==1) key = "tb_plate:list:in";
			Set<String> set = jedis.zrangeByScore(key, "-inf", "+inf");
			for (String id : set) {
				if (jedis.exists("tb_plate_master:list:plate_id:" + id)) {
					Set<String> pSet = jedis.zrangeByScore("tb_plate_master:list:plate_id:" + id, "-inf", "+inf");
					for (String mid : pSet) {
						Map<String, String> masterMap = jedis.hgetAll("tb_plate_master:master_id:" + mid);
						Map<String, String> uMap = jedis.hgetAll("tb_user:user_id:" + masterMap.get("user_id"));
						Map<String, String> pMap = jedis.hgetAll("tb_plate:plate_id:" + masterMap.get("plate_id"));
						masterMap.put("user_name", uMap.get("user_name"));
						masterMap.put("plate_name", pMap.get("plate_name"));
						resultList.add(masterMap);
					}
				}
			}
		} finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return resultList;
	}

	public static Set<String> getEliteArticleIdListByUserId(Integer user_id) {
		Jedis jedis = RedisManager.getJedisObject();
		String article_key="tb_article:list:user_id:"+user_id;
		String elite_key = "tb_article:list:is_elite:1";
		String temp_key="temp:elite:useid:"+user_id;
		Set<String> set = null;
		try {
			String[] setTmp = new String[2];
			setTmp[0] = article_key;
			setTmp[1] = elite_key;
			ZParams Op_params = new ZParams();
			Op_params.aggregate(ZParams.Aggregate.SUM);
			Op_params.weights(new int[] { 0, 1 });
			jedis.zinterstore(temp_key, Op_params, setTmp);
			set = jedis.zrevrangeByScore(temp_key, "+inf", "-inf");
		} finally {
			jedis.expire(temp_key, 20);
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return set;
	}
	
	public static int plate_masterDelete(int masterid) {
		int result = 0;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			if (jedis.exists("tb_plate_master:master_id:" + masterid)) {
				Map<String, String> map = jedis.hgetAll("tb_plate_master:master_id:" + masterid);
				jedis.hset("tb_plate_master:master_id:" + masterid, "is_delete", "1");
				jedis.zrem("tb_plate_master:list:plate_id:" + map.get("plate_id"), masterid+"");
				jedis.zrem("tb_plate_master:list:userid:plateid:" + map.get("plate_id"), map.get("user_id"));
				jedis.zrem("tb_plate_master:list:user_id:" + map.get("user_id"), masterid+"");
				jedis.zrem("tb_plate_master:list:plateid:userid:" + map.get("user_id"), map.get("plate_id"));
				
				jedis.zadd("tb_plate_master_delete", 0, masterid+"");
				result = 1;
			}else {
				result = -1;
			}
		} finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return result;
	}

	public static List<Map<String,String>> getPlateListByUserIdForMaster(int user_id) {
		Jedis jedis = RedisManager.getJedisObject();
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try {
			Set<String> set = jedis.zrange("tb_plate_master:list:plateid:userid:"+user_id, 0, -1);
			for(String id:set){
				Map<String,String> plate = jedis.hgetAll("tb_plate:plate_id:"+id);
				String plate_name = plate.get("plate_name");
				Map<String,String> map = new HashMap<String, String>();
				map.put("plate_id", id);
				map.put("plate_name", plate_name);
				list.add(map);
			}
		} finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return list;
		
	}

	public static Map<String, String> getArticleInfoById(int id) {
		Jedis jedis = RedisManager.getJedisObject();
		Map<String,String> map =null;
		try {
			map = jedis.hgetAll("tb_article:article_id:" + id);
		} finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return map;
	}

	public static int makeElite(int id, int type) {
		int back = -1;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			if(type==1){
				Map<String,String> amap = jedis.hgetAll("tb_article:article_id:"+id);
				amap.put("is_elite", "1");
				jedis.hmset("tb_article:article_id:"+id,amap);
				jedis.zadd("tb_article:list:is_elite:1", System.currentTimeMillis(),id+"");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				jedis.hset("tb_article:article_id:" + id, "update_time", format.format(new Date()));
				jedis.zadd("tb_article_update", System.currentTimeMillis(),id+"");
				back=1;
			}else if(type==2){
				Map<String,String> amap = jedis.hgetAll("tb_article:article_id:"+id);
				amap.put("is_elite", "0");
				jedis.hmset("tb_article:article_id:"+id,amap);
				jedis.zrem("tb_article:list:is_elite:1",id+"");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				jedis.hset("tb_article:article_id:" + id, "update_time", format.format(new Date()));
				jedis.zadd("tb_article_update", System.currentTimeMillis(),id+"");
				back=1;
			}
		} finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return back;
		
	}

	public static int makeTop(int id, int type) {
		int back = -1;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			if(type==1){
				long timer = System.currentTimeMillis();
				Map<String,String> amap = jedis.hgetAll("tb_article:article_id:"+id);
				amap.put("article_order", timer+"");
				jedis.hmset("tb_article:article_id:"+id,amap);
				jedis.zadd("tb_article:list:article_order", timer,id+"");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				jedis.hset("tb_article:article_id:" + id, "update_time", format.format(new Date()));
				jedis.zadd("tb_article_update", System.currentTimeMillis(),id+"");
				back=1;
			}else if(type==2){
				Map<String,String> amap = jedis.hgetAll("tb_article:article_id:"+id);
				amap.put("article_order", "0");
				jedis.hmset("tb_article:article_id:"+id,amap);
				jedis.zrem("tb_article:list:article_order",id+"");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				jedis.hset("tb_article:article_id:" + id, "update_time", format.format(new Date()));
				jedis.zadd("tb_article_update", System.currentTimeMillis(),id+"");
				back=1;
			}
		} finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return back;
	}
	/**
	 * 首页精华贴 内部+外部所有
	 * @return 
	 */
	public static List<Map<String,Object>> getjinghuaArticles() {
		Jedis jedis = RedisManager.getJedisObject();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	    try {
	    	Set<String> set=jedis.zrevrange("tb_article:list:is_elite:1", 0, 9);
	    	if(set!=null&&set.size()>0){
	    		for(String articleid:set){
//	    			   Map<String,String> map=jedis.hgetAll("tb_article:article_id:"+articleid);
	    			Map<String,String> map=getArticleWithoutContent(articleid);
	    			   if(map!=null&&map.size()>0){
	    				   Map<String,Object> mapNew=new HashMap<String, Object>();
	    				   mapNew.put("articleid", articleid);
	    				   mapNew.put("article_title", map.get("article_title"));
//	    				   mapNew.put("article_content", ArticleHtmlUtil.convertContentToHtml(map.get("article_content")));
	    				   list.add(mapNew);
	    			   }
		    		 }
		    	}
	    	return list;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}
	/**
	 * 首页精华贴 外部
	 * @return 
	 */
	public static List<Map<String,Object>> getOutjinghuaArticles() {
		Jedis jedis = RedisManager.getJedisObject();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	    try {
	    	String key1="tb_article:list:all";
	    	String key2="tb_article:list:is_elite:1";
	    	String key=UUID.randomUUID().toString();
	    	jedis.zinterstore(key, key1,key2);
	    	jedis.expire(key, 60);
	    	Set<String> set=jedis.zrevrange(key, 0, 9);
	    	if(set!=null&&set.size()>0){
	    		for(String articleid:set){
//	    			   Map<String,String> map=jedis.hgetAll("tb_article:article_id:"+articleid);
	    			Map<String,String> map=getArticleWithoutContent(articleid);
	    			   if(map!=null&&map.size()>0){
	    				   Map<String,Object> mapNew=new HashMap<String, Object>();
	    				   mapNew.put("articleid", articleid);
	    				   mapNew.put("article_title", map.get("article_title"));
//	    				   mapNew.put("article_content", ArticleHtmlUtil.convertContentToHtml(map.get("article_content")));
	    				   list.add(mapNew);
	    			   }
		    		 }
		    	}
	    	return list;
	    } finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}

	public static boolean plate_masterCheckIsMaster(int user_id, int plate_id) {
		boolean result = false;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			if (jedis.exists("tb_plate_master:list:userid:plateid:" + plate_id) && jedis.zscore("tb_plate_master:list:userid:plateid:" + plate_id,user_id + "")!=null) {
				result = true;
			}
		} finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return result;
	}
	public static int recoverUser(Integer user_id) {
		int back = 1;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	Map<String,String> umap = getUserByUserId(user_id.toString());
	    	umap.put("user_state", "0");
	    	jedis.hmset("tb_user:user_id:"+user_id, umap);
	    	jedis.zadd("tb_user:list:user_state:0", 0,user_id+"");
	    	jedis.zrem("tb_user:list:user_state:1", user_id+"");
	    	//添加用户名索引
	    	String username = umap.get("user_name");
    		String uid = umap.get("user_id");
    		String log_time = umap.get("log_time");
    		long time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(log_time).getTime();
    		for(int i=0;i<username.length();i++){
    			for(int j=i;j<=username.length();j++){
    				String key = username.substring(i,j);
    				jedis.zadd("username:index:"+key, time,uid);
    			}
    		}
	    } catch(Exception e){
	    	back = -1;
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return back;
	}

	public static int removeUser(Integer user_id) {
		int back = 1;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	Map<String,String> umap = getUserByUserId(user_id.toString());
	    	jedis.hmset("tb_user:user_id:"+user_id, umap);
	    	jedis.del("tb_user:user_name:"+umap.get("user_name"));
	    	jedis.del("tb_user:user_phone:"+umap.get("user_phone"));
	    	jedis.del("tb_user:user_phone:"+umap.get("user_phone"));
	    	jedis.zrem("tb_user:list:user_state:"+umap.get("user_state"), user_id+"");
	    	jedis.zrem("tb_user:list:role:"+umap.get("role"), user_id+"");
	    	jedis.zrem("tb_user:list:all", user_id.toString());
	    	UserUtil.deleteUserCookie(UserUtil.mapToUser(umap));
	    	
	    	umap.put("user_state", "9");
	    	//删除用户名索引
	    	String username = umap.get("user_name");
    		String uid = umap.get("user_id");
    		for(int i=0;i<username.length();i++){
    			for(int j=i;j<=username.length();j++){
    				String key = username.substring(i,j);
    				jedis.zrem("username:index:"+key, uid);
    			}
    		}
	    } catch(Exception e){
	    	back = -1;
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return back;
	}

	public static Map<String, String> getUserByToken(String token) {
		Map<String, String> umap = null;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	String id = jedis.get("tb_user:token:"+token);
	    	if(StringUtils.isNotEmpty(id)){
	    		umap = jedis.hgetAll("tb_user:user_id:"+id);
	    	}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return umap;
	}
	public static int setUserCookie(String token,String user_id){
		int back = 1;
		Jedis jedis = RedisManager.getJedisObject();
		String key = "tb_user:token:"+token;
	    try {
	    	jedis.set(key,user_id);
	    } catch(Exception e){
	    	back = -1;
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return back;
	}
	public static Map<String,String> getArticleWithoutContent(Jedis jedis,String article_id){
		Map<String,String> map = new HashMap<String,String>();
		List<String> list = jedis.hmget("tb_article:article_id:"+article_id, "article_id","user_id","article_title",
				"plate_id","add_time","update_time","article_state","is_delete","article_lookcount","is_elite","article_order");
		if(list==null||list.size()<11){
			return map;
		}
		map.put("article_id", list.get(0));
		map.put("user_id", list.get(1));
		map.put("article_title", list.get(2));
		map.put("plate_id", list.get(3));
		map.put("add_time", list.get(4));
		map.put("update_time", list.get(5));
		map.put("article_state", list.get(6));
		map.put("is_delete", list.get(7));
		map.put("article_lookcount", list.get(8));
		map.put("is_elite", list.get(9));
		map.put("article_order", list.get(10));
		return map;
	}
	public static Map<String,String> getArticleWithoutContent(String article_id){
		Jedis jedis = RedisManager.getJedisObject();
		Map<String,String> map = new HashMap<String,String>();
	    try {
	    	List<String> list = jedis.hmget("tb_article:article_id:"+article_id, "article_id","user_id","article_title",
					"plate_id","add_time","update_time","article_state","is_delete","article_lookcount","is_elite","article_order");
			if(list==null||list.size()<11){
				return map;
			}
			map.put("article_id", list.get(0));
			map.put("user_id", list.get(1));
			map.put("article_title", list.get(2));
			map.put("plate_id", list.get(3));
			map.put("add_time", list.get(4));
			map.put("update_time", list.get(5));
			map.put("article_state", list.get(6));
			map.put("is_delete", list.get(7));
			map.put("article_lookcount", list.get(8));
			map.put("is_elite", list.get(9));
			map.put("article_order", list.get(10));
	    } catch(Exception e){
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return map;
	}
	public static int ifInPalate(String plateid){
		Jedis jedis = RedisManager.getJedisObject();
		int state=0;
		 try {
			String is_in=jedis.hget("tb_plate:plate_id:"+plateid, "is_in");
			if(!is_in.equals("NULL")&&is_in!=null){
				state=Integer.parseInt(is_in);
		} 
		} catch(Exception e){
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return state;
	}
	
	public static boolean comment_deleteByArticleid(int articleid){
		Jedis jedis = RedisManager.getJedisObject();
		boolean result = false;
		try {
			if (jedis.exists("tb_comment:list:article_id:" + articleid)) {
				Set<String> set = jedis.zrange("tb_comment:list:article_id:" + articleid, 0, -1);
				String uid = jedis.hget("tb_article:article_id:" + articleid, "user_id");
				for (String com_id : set) {
					jedis.hset("tb_comment:com_id:" + com_id, "is_delete", "1");  //更新字段
					jedis.zadd("tb_comment_update", System.currentTimeMillis(), com_id);  //添加更新
					
					if (jedis.exists("tb_comment:list:article_userid:" + uid)) {
						jedis.zrem("tb_comment:list:article_userid:" + uid, com_id);  //删除帖子原作者索引
					}
					
					String com_userid = jedis.hget("tb_comment:com_id:" + com_id, "com_userid");
					jedis.zrem("tb_comment:list:com_userid:" + com_userid, com_id);
					
					String user_id = jedis.hget("tb_comment:com_id:" + com_id, "user_id");
					jedis.zrem("tb_comment:list:user_id:" + user_id, com_id);
					
					if (jedis.exists("tb_comment:list:comment_id:" + com_id)) {
						jedis.del("tb_comment:list:comment_id:" + com_id);
					}
				}
				
				jedis.del("tb_comment:list:article_id:" + articleid);
				if (jedis.exists("tb_comment:list:article_id_0:" + articleid)) {
					jedis.del("tb_comment:list:article_id_0:" + articleid);
				}
			}
			
			result = true;
		} catch(Exception e){
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return result;
	}
	
	/**
	 * 冻结文章
	 * @param articleid
	 * @return 0-失败，1-成功，2-已被冻结
	 */
	public static int article_frostByArticleid(int articleid) {
		Jedis jedis = RedisManager.getJedisObject();
		int result = 0;
		try {
			if (jedis.exists("tb_comment:list:article_id:" + articleid)) {
				if (jedis.exists("tb_article:list:state:2") && jedis.zscore("tb_article:list:state:2", articleid + "")!=null) {
					result = 2;
				} else {
					Map<String, String> map = jedis.hgetAll("tb_article:article_id:" + articleid);
					jedis.zrem("tb_article:list:plate_id:" + map.get("plate_id"), articleid + "");
					jedis.zrem("tb_article:list:user_id:" + map.get("user_id"), articleid + "");
					jedis.zrem("tb_article:list:article_state:" + map.get("article_state"), articleid + "");
					jedis.zrem("tb_article:list:all", articleid + "");
					jedis.zrem("tb_article:list:allin", articleid + "");
					
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					jedis.hset("tb_article:article_id:" + articleid, "update_time", format.format(new Date()));
					jedis.hset("tb_article:article_id:" + articleid, "article_state", "2");
					
					jedis.zadd("tb_article:list:article_state:2", System.currentTimeMillis(), articleid + "");
					jedis.zadd("tb_article_update", System.currentTimeMillis(), articleid + "");
					LuceneUtil.deleteIndex(articleid + "");
					
					result = 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return result;
	}
	
	/**
	 * 取消冻结文章
	 * @param articleid
	 * @return 0-失败，1-成功
	 */
	public static int article_unfrostByArticleid(int articleid) {
		Jedis jedis = RedisManager.getJedisObject();
		int result = 0;
		try {
			if (jedis.exists("tb_comment:list:article_id:" + articleid)) {
				if (jedis.exists("tb_article:list:state:2") && jedis.zscore("tb_article:list:state:2", articleid + "")!=null) {
					Map<String, String> map = jedis.hgetAll("tb_article:article_id:" + articleid);
					jedis.zadd("tb_article:list:plate_id:" + map.get("plate_id"), System.currentTimeMillis(), articleid + "");
					jedis.zadd("tb_article:list:user_id:" + map.get("user_id"), System.currentTimeMillis(), articleid + "");
					jedis.zadd("tb_article:list:article_state:" + map.get("article_state"), System.currentTimeMillis(), articleid + "");
					
					int isin = 0;
					if (jedis.exists("tb_plate:list:in") && jedis.zscore("tb_plate:list:in", map.get("plate_id"))!=null) {
						jedis.zadd("tb_article:list:allin", System.currentTimeMillis(), articleid + "");
						isin = 1;
					} else {
						jedis.zadd("tb_article:list:all", System.currentTimeMillis(), articleid + "");
					}
					
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					jedis.hset("tb_article:article_id:" + articleid, "update_time", format.format(new Date()));
					jedis.hset("tb_article:article_id:" + articleid, "article_state", "1");
					
					jedis.zadd("tb_article:list:article_state:1", System.currentTimeMillis(), articleid + "");
					jedis.zadd("tb_article_update", System.currentTimeMillis(), articleid + "");
					
					Map<String, String> searchMap = new HashMap<String, String>();
			    	searchMap.put("title", map.get("article_title"));
			    	searchMap.put("userid", map.get("user_id"));
			    	searchMap.put("plateid", map.get("plate_id"));
			    	searchMap.put("id", map.get("article_id"));
			    	searchMap.put("addtime", System.currentTimeMillis() + "");
			    	try {
			    		LuceneUtil.insertorUpdateIndex(searchMap, 2,1);//全部帖子
			    		if(isin==0){
			    			LuceneUtil.insertorUpdateIndex(searchMap, 2,2);//外部帖子
			    		}
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					result = 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return result;
	}

	public static Message changeUsername(Integer user_id, String username) throws Exception {
		Message msg = new Message();
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	if(jedis.exists("tb_user:user_name:"+user_id)){
	    		msg.setCode("1111");
				msg.setMsg("用户名已存在！");
				return msg;
	    	}
	    	Map<String,String> umap = getUserByUserId(user_id.toString());
	    	String old_username = umap.get("user_name");
	    	umap.put("user_name", username);
	    	jedis.hmset("tb_user:user_id:"+user_id.toString(), umap);
	    	jedis.del("tb_user:user_name:"+old_username);
	    	jedis.set("tb_user:user_name:"+username, user_id.toString());
	    	jedis.zadd("tb_user:user_id:update", System.currentTimeMillis(),user_id.toString());
	    	msg.setCode("0000");
	    	msg.setMsg("修改成功");
	    } catch(Exception e){
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return msg;
	}

	public static int deleteUserCookie(String token) {
		int back = 1;
		Jedis jedis = RedisManager.getJedisObject();
		String key = "tb_user:token:"+token;
	    try {
	    	jedis.del(key);
	    } catch(Exception e){
	    	back = -1;
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return back;
	}

	public static void removeUserMap(String user_id) {
		Jedis jedis = RedisManager.getJedisObject();
		String key = "tb_user:user_id:"+user_id;
	    try {
	    	jedis.del(key);
	    } catch(Exception e){
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	}

	public static List<Map<String, String>> getUserListWithArticleSize() {
		Jedis jedis = RedisManager.getJedisObject();
		 List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	    try {
	    	Set<String> set = jedis.zrange("tb_user:list:user_state:0", 0, -1);
	    	for(String id:set){
	    		Map<String,String> umap = jedis.hgetAll("tb_user:user_id:"+id);
	    		Set<String> aset = jedis.zrange("tb_article:list:user_id:"+id,0,-1);
	    		int size = aset.size();
	    		umap.put("articleSize", size+"");
	    		list.add(umap);
	    	}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return list;
	}
	public static boolean isUserExist(String token,String id) {
		boolean falg = false;
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	String id2 = jedis.get("tb_user:token:"+token);
	    	if(StringUtils.isNotEmpty(id2)){
	    		falg = id2.equals(id);
	    	}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return falg;
	}
	public static int makeUsernameIndex(){
		Jedis jedis = RedisManager.getJedisObject();
		int back = 1;
	    try {
	    	Set<String> ids = jedis.zrange("tb_user:list:user_state:0", 0, -1);
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	for(String id:ids){
	    		Map<String,String> umap = jedis.hgetAll("tb_user:user_id:"+id);
	    		if(umap==null||umap.size()<=0){
	    			continue;
	    		}
	    		String username = umap.get("user_name");
	    		String uid = umap.get("user_id");
	    		String log_time = umap.get("log_time");
	    		long time = sdf.parse(log_time).getTime();
	    		for(int i=0;i<username.length();i++){
	    			for(int j=i;j<=username.length();j++){
	    				String key = username.substring(i,j);
	    				jedis.zadd("username:index:"+URLEncoder.encode(key, "utf-8"), time,uid);
	    			}
	    		}
	    	}
	    } catch(Exception e){
	    	e.printStackTrace();
	    	back = -1;
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
	    return back;
	}
	public static List<User> getUserByUsernameIndex(String index){
		List<User> list = new ArrayList<User>();
		Jedis jedis = RedisManager.getJedisObject();
	    try {
	    	Set<String> ids = jedis.zrevrangeByScore("username:index:"+index, "+inf", "-inf");
	    	for(String id:ids){
	    		User user = UserUtil.mapToUser(getUserByUserId(id));
	    		list.add(user);
	    	}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return list;
	}
	/**
	 * 
	 * @param msg
	 * @param expire 过期时间 秒
	 * @return
	 */
	public static int saveMsg(Msg msg,int expire){
		int back = 1;
		Jedis jedis = RedisManager.getJedisObject();
		Integer id = null;
	    try {
	    	if(msg==null){
	    		return -1;
	    	}
	    	String from = msg.getFrom();
	    	String to = msg.getTo();
	    	if(from==null||to == null){
	    		return -1;
	    	}
	    	String key = "msg:list:from-to:"+from+"-"+to;
	    	if("".equals(to)){
	    		key = "msg:list:public";
	    	}
	    	id = msg.getId();
	    	if(id==null){
	    		id = jedis.incr("incr:msg:id").intValue();
	    		msg.setId(id);
	    	}
	    	
	    	if(jedis.zcard(key)>=1000){//最好写到配置文件里
	    		String first = jedis.zrange(key,0,0).iterator().next();
	    		jedis.zrem(key, first);
	    		jedis.del("msg:id:"+first);
	    	}
	    	Map<String,String> map = ObjectToMapUtil.changeToMap(msg);
	    	jedis.hmset("msg:id:"+id, map);
	    	jedis.zadd(key, msg.getTime(),id.toString());
	    } catch(Exception e){
	    	e.printStackTrace();
	    	back = -1;
	    }finally {
	    	jedis.expire("msg:id:"+id, expire);
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return back;
	}
	/**
	 * 
	 * @param from
	 * @param to
	 * @param lastTime	当前最后一条数据的时间，毫秒
	 * @param effective 距当前时间的有效间隔，毫秒
	 * @param size    获取数量
	 * @return
	 */
	public static List<Msg> getMsg(String from,String to,long lastTime,long effective,int size){
		List<Msg> list = new ArrayList<Msg>();
		Jedis jedis = RedisManager.getJedisObject();
		if(from==null||to==null){
			return list;
		}
		String key = "msg:list:from-to:"+from+"-"+to;
		if("".equals(to)){
			key = "msg:list:public";
		}
	    try {
	    	long beginTime = System.currentTimeMillis()-effective;
	    	Set<String> ids = jedis.zrevrangeByScore(key, lastTime, beginTime);
	    	int i = 0;
	    	for(String id :ids){
	    		Map<String,String> map = jedis.hgetAll("msg:id:"+id);
	    		if(map==null){
	    			continue;
	    		}
	    		Msg msg = MsgUtil.mapToMsg(map);
	    		list.add(msg);
	    		i++;
	    		if(i>=size){
	    			break;
	    		}
	    	}
	    } catch(Exception e){
	    	e.printStackTrace();
	    }finally {
	    	RedisManager.recycleJedisOjbect(jedis);
	    }
		return list;
	}
	public static int cleanMsg(){
		int back = 1;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			Set<String> keys = jedis.keys("msg:id:*");
			for (String key : keys) {
				jedis.del(key);
			}
			keys = jedis.keys("msg:list:*");
			for (String key : keys) {
				jedis.del(key);
			}
			keys = jedis.keys("leavemsg:list:*");
			for (String key : keys) {
				jedis.del(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
			back = -1;
		} finally {
			RedisManager.recycleJedisOjbect(jedis);
		}
		return back;
	}
	/**
	 * 留言
	 * @param msg 消息
	 * @param expire 过期时间
	 * @return
	 */
	public static boolean leaveMessage(Msg msg,int expire){
		boolean flag = false;
		Jedis jedis = RedisManager.getJedisObject();
		Integer id = null;
		try {
			String from = msg.getFrom();
			String to  = msg.getTo();
			if(StringUtils.isEmpty(from)||StringUtils.isEmpty(to)){
				return flag;
			}
			String key = "leavemsg:list:to:"+to;
			if(jedis.zcard(key)>=1000){
				return flag;
			}
			id = msg.getId();
	    	if(id==null){
	    		id = jedis.incr("incr:msg:id").intValue();
	    		msg.setId(id);
	    	}
			Map<String,String> map = ObjectToMapUtil.changeToMap(msg);
	    	jedis.hmset("msg:id:"+id, map);
	    	jedis.zadd(key, msg.getTime(),id.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.expire("msg:id:"+id, expire);
			RedisManager.recycleJedisOjbect(jedis);
		}
		flag = true;
		return flag;
	}
	/**
	 * 提取留言
	 * @param from 
	 * @param to
	 * @param effective 距当前时间的有效间隔，毫秒
	 * @return
	 */
	public static List<Map<String,String>> getLeaveMsg(String from ,String to,long effective){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Jedis jedis = RedisManager.getJedisObject();
		try {
			if(StringUtils.isEmpty(from)||StringUtils.isEmpty(to)){
				return list;
			}
			String key = "leavemsg:list:from-to:"+from+"-"+to;
			Set<String> ids = jedis.zrevrangeByScore(key, System.currentTimeMillis(), System.currentTimeMillis()-effective);
	    	for(String id:ids){
	    		String msgKey = "msg:id:"+id;
	    		Map<String,String> map = jedis.hgetAll(msgKey);
	    		if(map==null){
	    			continue;
	    		}
	    		list.add(map);
	    		jedis.zadd("msg:list:from-to:"+from+"-"+to, Long.parseLong(map.get("time")),id);
	    	}
	    	jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.recycleJedisOjbect(jedis);
		}
		return list;
	}

	public static void saveBackPasswordMark(String mark,String username) {
		String key = "mark:getBackPassword:phone:"+mark;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			jedis.set(key, username);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.expire(key, 600);
			RedisManager.recycleJedisOjbect(jedis);
		}
		
	}
	public static User getUserByMark(String mark){
		String key = "mark:getBackPassword:phone:"+mark;
		User user = null;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			String username = jedis.get(key);
			user = getUserByUsername(username);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.expire(key, 600);
			RedisManager.recycleJedisOjbect(jedis);
		}
		return  user;
	}

	public static void removeMark(String mark) {
		String key = "mark:getBackPassword:phone:"+mark;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.recycleJedisOjbect(jedis);
		}
	}
	
	/**
	 * 修改tb_article:list:allin，tb_article:list:all这两个列表，将权值修改为更新时间
	 */
	public static void article_deallist() {
		Jedis jedis = RedisManager.getJedisObject();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Set<String> id1 = jedis.zrange("tb_article:list:allin", 0, -1);
			Set<String> id2 = jedis.zrange("tb_article:list:all", 0, -1);
			Set<String> inset = jedis.zrange("tb_plate:list:in", 0, -1);
			
			Set<String> orderSet = jedis.zrange("tb_article:list:article_order", 0, -1);
			for (String id : orderSet) {
				String user_id = jedis.hget("tb_article:article_id:" + id, "user_id");
				if (jedis.zscore("tb_article:list:allin", id)==null) {
					jedis.zrem("tb_article:list:article_order", id);
					System.out.println("deleate >>>>>>>>>>>>> " + id);
				}
			}
			
			
			List<String> list1 = new ArrayList<String>();
			List<String> list2 = new ArrayList<String>();
			for (String id : id1) {
				String user_id = jedis.hget("tb_article:article_id:" + id, "user_id");
				if (jedis.zscore("tb_article:list:user_id:" + user_id, id)==null) {
					System.out.println("deleate >>>>>>>>>>>>> " + id);
					article_delete(Integer.parseInt(id));
				}
//				try {
//					String pid = jedis.hget("tb_article:article_id:" + id, "plate_id");
//					if (inset.contains(pid)) {
//						String dd = jedis.hget("tb_article:article_id:" + id, "add_time");
//						jedis.hset("tb_article:article_id:" + id, "update_time", dd);
//						jedis.zadd("tb_article_update", System.currentTimeMillis(), id);
//						Date date = sdf.parse(dd);
//						jedis.zadd("tb_article:list:allin", date.getTime(), id);
//						
//						System.out.println("tb_article:list:allin >>>>>>>>>>>>>>>>> " + id);
//					} else {
//						list1.add(id);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			}
			for (String id : id2) {
				String user_id = jedis.hget("tb_article:article_id:" + id, "user_id");
				if (jedis.zscore("tb_article:list:user_id:" + user_id, id)==null) {
					System.out.println("deleate >>>>>>>>>>>>> " + id);
					article_delete(Integer.parseInt(id));
				}
				
//				try {
//					String pid = jedis.hget("tb_article:article_id:" + id, "plate_id");
//					if (inset.contains(pid)) {
//						list2.add(id);
//					} else {
//						String dd = jedis.hget("tb_article:article_id:" + id, "add_time");
//						Date date = sdf.parse(dd);
//						jedis.zadd("tb_article:list:all", date.getTime(), id);
//						
//						jedis.hset("tb_article:article_id:" + id, "update_time", dd);
//						jedis.zadd("tb_article_update", System.currentTimeMillis(), id);
//						System.out.println("tb_article:list:all >>>>>>>>>>>>>>>>> " + id);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			}
			
			for (int i = 0; i < list1.size(); i++) {
				String id = list1.get(i);
				jedis.zrem("tb_article:list:allin", id);
				
				System.out.println("tb_article:list:allin  >>>>>>>>> del >>>>>>>>>>>>>>>>> " + id);
				
				try {
					LuceneUtil.deleteIndex(id + "");
					
					//创建搜索
			    	Map<String, String> searchMap = new HashMap<String, String>();
			    	searchMap.put("title", jedis.hget("tb_article:article_id:" + id, "article_title"));
			    	searchMap.put("userid", jedis.hget("tb_article:article_id:" + id, "user_id"));
			    	searchMap.put("plateid", jedis.hget("tb_article:article_id:" + id, "plate_id"));
			    	searchMap.put("id", id);
			    	
			    	String pid = jedis.hget("tb_article:article_id:" + id, "plate_id");
			    	searchMap.put("addtime",jedis.zscore("tb_article:list:plate_id:" + pid, id) + "");
			    	
			    	LuceneUtil.insertorUpdateIndex(searchMap, 2,1);//全部帖子
			    	if(jedis.zscore("tb_plate:list:in", pid + "")!=null){
		    			LuceneUtil.insertorUpdateIndex(searchMap, 2,2);//外部帖子
		    		}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
			for (int i = 0; i < list2.size(); i++) {
				String id = list2.get(i);
				jedis.zrem("tb_article:list:all", id);
				
				System.out.println("tb_article:list:all  >>>>>>>>> del >>>>>>>>>>>>>>>>> " + id);
				
				try {
					LuceneUtil.deleteIndex(id + "");
					
					//创建搜索
			    	Map<String, String> searchMap = new HashMap<String, String>();
			    	searchMap.put("title", jedis.hget("tb_article:article_id:" + id, "article_title"));
			    	searchMap.put("userid", jedis.hget("tb_article:article_id:" + id, "user_id"));
			    	searchMap.put("plateid", jedis.hget("tb_article:article_id:" + id, "plate_id"));
			    	searchMap.put("id", id);
			    	
			    	String pid = jedis.hget("tb_article:article_id:" + id, "plate_id");
			    	searchMap.put("addtime",jedis.zscore("tb_article:list:plate_id:" + pid, id) + "");
			    	
			    	LuceneUtil.insertorUpdateIndex(searchMap, 2,1);//全部帖子
			    	if(jedis.zscore("tb_plate:list:in", pid + "")!=null){
		    			LuceneUtil.insertorUpdateIndex(searchMap, 2,2);//外部帖子
		    		}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.recycleJedisOjbect(jedis);
		}
	}
	
	public static int comment_update(int uid, TbComment tbComment) {
		int result = 0;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			String key = "tb_comment:list:user_id:" + tbComment.getUser_id();
			if (jedis.exists(key) && jedis.zscore(key, tbComment.getCom_id() + "")!=null) {
				jedis.hset(key, "com_content", tbComment.getCom_content());
				jedis.zadd("tb_comment_update", System.currentTimeMillis(), tbComment.getCom_id() + "");
				result = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.recycleJedisOjbect(jedis);
		}
		
		return result;
	}
	
	public static int comment_delete(int uid, TbComment tbComment) {
		int result = 0;
		Jedis jedis = RedisManager.getJedisObject();
		try {
			String key = "tb_comment:list:user_id:" + tbComment.getUser_id();
			if (jedis.exists(key) && jedis.zscore(key, tbComment.getCom_id() + "")!=null) {
				jedis.hset(key, "com_content", tbComment.getCom_content());
				jedis.hset(key, "is_delete", "1");
				jedis.zadd("tb_comment_update", System.currentTimeMillis(), tbComment.getCom_id() + "");
				result = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisManager.recycleJedisOjbect(jedis);
		}
		
		return result;
	}
}
