package com.poly.dao;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

import com.poly.bean.TbArticle;

public class  TbArticleDao  extends BaseDao {

	public static void fill(ResultSet rs, TbArticle tbarticle) throws SQLException {
		tbarticle.setArticle_id(rs.getInt("article_id"));//帖子id
		tbarticle.setUser_id(rs.getInt("user_id"));//用户id
		tbarticle.setArticle_title(rs.getString("article_title"));//帖子标题
		tbarticle.setArticle_markdown(rs.getString("article_markdown"));//文章的markdown
		tbarticle.setArticle_content(rs.getString("article_content"));//帖子内容
		tbarticle.setPlate_id(rs.getInt("plate_id"));//板块id
		tbarticle.setAdd_time(rs.getTimestamp("add_time"));//添加时间
		tbarticle.setUpdate_time(rs.getTimestamp("update_time"));//更新时间
		tbarticle.setArticle_state(rs.getInt("article_state"));//帖子状态：1-正常，2-冻结
		tbarticle.setIs_delete(rs.getInt("is_delete"));//是否被删除
		tbarticle.setArticle_lookcount(rs.getInt("article_lookcount"));//查看数
		tbarticle.setIs_elite(rs.getInt("is_elite"));//是否是精华帖(0-普通帖，1-精华帖)
		tbarticle.setArticle_order(rs.getLong("article_order"));//置顶排序（不置顶为0，置顶按照该值排序）
	}

	public static List<TbArticle> find() {
		DBConnect dbc = null;
		String sql = "select * from tb_article";
		List<TbArticle> list = new ArrayList<TbArticle>();
		
		try {
			dbc = new DBConnect(sql);
			ResultSet rs = dbc.executeQuery();
			while (rs.next()) {
				TbArticle tbarticle = new TbArticle();
				fill(rs, tbarticle);
				list.add(tbarticle);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (dbc != null)
					dbc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
		
	}


	public static List<TbArticle> where(String subsql) {
		DBConnect dbc = null;
		String sql = "select * from tb_article where "+subsql+"";
		List<TbArticle> list = new ArrayList<TbArticle>();
		
		try {
			dbc = new DBConnect(sql);
			ResultSet rs = dbc.executeQuery();
			while (rs.next()) {
				TbArticle tbarticle = new TbArticle();
				fill(rs, tbarticle);
				list.add(tbarticle);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (dbc != null)
					dbc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
		
	}

	public static int whereCount(String subsql) {
		DBConnect dbc = null;
		int result = EXECUTE_FAIL;
		String sql = "select count(*) from tb_article where "+subsql+"";
		
		try {
			dbc = new DBConnect(sql);
			ResultSet rs = dbc.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
			return EXECUTE_FAIL;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (dbc != null)
					dbc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
		
	}


	public static int delete(String subsql) {
		int result = EXECUTE_FAIL;
		DBConnect dbc = null;
		String sql = "delete from tb_article where "+subsql+"";
		try {
			dbc = new DBConnect();
			dbc.prepareStatement(sql);
			dbc.executeUpdate();
			dbc.close();
			result = EXECUTE_SUCCESSS;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (dbc != null)
					dbc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
		
	}

	public static int delete(DBConnect dbc,String subsql) {
		int result = EXECUTE_FAIL;
		String sql = "delete from tb_article where "+subsql+"";
		try {
			dbc.prepareStatement(sql);
			dbc.executeUpdate();
			result = EXECUTE_SUCCESSS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}

	public static int save(TbArticle tbarticle) throws Exception {
		int result = EXECUTE_FAIL;
		DBConnect dbc = null;
		String sql = "insert into tb_article(`article_id`,`user_id`,`article_title`,`article_markdown`,`article_content`,`plate_id`,`add_time`,`update_time`,`article_state`,`is_delete`,`article_lookcount`,`is_elite`,`article_order`) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		dbc = new DBConnect();
		dbc.prepareStatement(sql);
		dbc.setInt(1, tbarticle.getArticle_id());
		dbc.setInt(2, tbarticle.getUser_id());
		dbc.setString(3, tbarticle.getArticle_title());
		dbc.setString(4, tbarticle.getArticle_markdown());
		dbc.setString(5, tbarticle.getArticle_content());
		dbc.setInt(6, tbarticle.getPlate_id());
		dbc.setTimestamp(7, new Timestamp(tbarticle.getAdd_time().getTime()));
		dbc.setTimestamp(8, new Timestamp(tbarticle.getUpdate_time().getTime()));
		dbc.setInt(9, tbarticle.getArticle_state());
		dbc.setInt(10, tbarticle.getIs_delete());
		dbc.setInt(11, tbarticle.getArticle_lookcount());
		dbc.setInt(12, tbarticle.getIs_elite());
		dbc.setLong(13, tbarticle.getArticle_order());
		dbc.executeUpdate();
		dbc.close();
		result = EXECUTE_SUCCESSS;
		return result;
	}

	public static int save(DBConnect dbc,TbArticle tbarticle) throws Exception {
		int result = EXECUTE_FAIL;
		String sql = "insert into tb_article(`article_id`,`user_id`,`article_title`,`article_markdown`,`article_content`,`plate_id`,`add_time`,`update_time`,`article_state`,`is_delete`,`article_lookcount`,`is_elite`,`article_order`) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		dbc.prepareStatement(sql);
		dbc.setInt(1, tbarticle.getArticle_id());
		dbc.setInt(2, tbarticle.getUser_id());
		dbc.setString(3, tbarticle.getArticle_title());
		dbc.setString(4, tbarticle.getArticle_markdown());
		dbc.setString(5, tbarticle.getArticle_content());
		dbc.setInt(6, tbarticle.getPlate_id());
		dbc.setTimestamp(7, new Timestamp(tbarticle.getAdd_time().getTime()));
		dbc.setTimestamp(8, new Timestamp(tbarticle.getUpdate_time().getTime()));
		dbc.setInt(9, tbarticle.getArticle_state());
		dbc.setInt(10, tbarticle.getIs_delete());
		dbc.setInt(11, tbarticle.getArticle_lookcount());
		dbc.setInt(12, tbarticle.getIs_elite());
		dbc.setLong(13, tbarticle.getArticle_order());
		dbc.executeUpdate();
		result = EXECUTE_SUCCESSS;
		return result;
	}

	public static int update(DBConnect dbc,TbArticle tbarticle) throws Exception {
		int result = EXECUTE_FAIL;
		StringBuffer sb = new StringBuffer();
		sb.append("update tb_article set ");
		boolean flag = false;
		if(tbarticle.COLUMN_FLAG[0]){
			if(flag){
				sb.append(",article_id=?");
			}else{
				sb.append("article_id=?");
				flag=true;
			}
		}
		if(tbarticle.COLUMN_FLAG[1]){
			if(flag){
				sb.append(",user_id=?");
			}else{
				sb.append("user_id=?");
				flag=true;
			}
		}
		if(tbarticle.COLUMN_FLAG[2]){
			if(flag){
				sb.append(",article_title=?");
			}else{
				sb.append("article_title=?");
				flag=true;
			}
		}
		if(tbarticle.COLUMN_FLAG[3]){
			if(flag){
				sb.append(",article_markdown=?");
			}else{
				sb.append("article_markdown=?");
				flag=true;
			}
		}
		if(tbarticle.COLUMN_FLAG[4]){
			if(flag){
				sb.append(",article_content=?");
			}else{
				sb.append("article_content=?");
				flag=true;
			}
		}
		if(tbarticle.COLUMN_FLAG[5]){
			if(flag){
				sb.append(",plate_id=?");
			}else{
				sb.append("plate_id=?");
				flag=true;
			}
		}
		if(tbarticle.COLUMN_FLAG[6]){
			if(flag){
				sb.append(",add_time=?");
			}else{
				sb.append("add_time=?");
				flag=true;
			}
		}
		if(tbarticle.COLUMN_FLAG[7]){
			if(flag){
				sb.append(",update_time=?");
			}else{
				sb.append("update_time=?");
				flag=true;
			}
		}
		if(tbarticle.COLUMN_FLAG[8]){
			if(flag){
				sb.append(",article_state=?");
			}else{
				sb.append("article_state=?");
				flag=true;
			}
		}
		if(tbarticle.COLUMN_FLAG[9]){
			if(flag){
				sb.append(",is_delete=?");
			}else{
				sb.append("is_delete=?");
				flag=true;
			}
		}
		if(tbarticle.COLUMN_FLAG[10]){
			if(flag){
				sb.append(",article_lookcount=?");
			}else{
				sb.append("article_lookcount=?");
				flag=true;
			}
		}
		if(tbarticle.COLUMN_FLAG[11]){
			if(flag){
				sb.append(",is_elite=?");
			}else{
				sb.append("is_elite=?");
				flag=true;
			}
		}
		if(tbarticle.COLUMN_FLAG[12]){
			if(flag){
				sb.append(",article_order=?");
			}else{
				sb.append("article_order=?");
				flag=true;
			}
		}
		sb.append(" where article_id=?");
		dbc = new DBConnect();
		dbc.prepareStatement(sb.toString());
		int k=1;
		if(tbarticle.COLUMN_FLAG[0]){
			dbc.setInt(k, tbarticle.getArticle_id());k++;
		}
		if(tbarticle.COLUMN_FLAG[1]){
			dbc.setInt(k, tbarticle.getUser_id());k++;
		}
		if(tbarticle.COLUMN_FLAG[2]){
			dbc.setString(k, tbarticle.getArticle_title());k++;
		}
		if(tbarticle.COLUMN_FLAG[3]){
			dbc.setString(k, tbarticle.getArticle_markdown());k++;
		}
		if(tbarticle.COLUMN_FLAG[4]){
			dbc.setString(k, tbarticle.getArticle_content());k++;
		}
		if(tbarticle.COLUMN_FLAG[5]){
			dbc.setInt(k, tbarticle.getPlate_id());k++;
		}
		if(tbarticle.COLUMN_FLAG[6]){
			dbc.setTimestamp(k, new Timestamp(tbarticle.getAdd_time().getTime()));k++;
		}
		if(tbarticle.COLUMN_FLAG[7]){
			dbc.setTimestamp(k, new Timestamp(tbarticle.getUpdate_time().getTime()));k++;
		}
		if(tbarticle.COLUMN_FLAG[8]){
			dbc.setInt(k, tbarticle.getArticle_state());k++;
		}
		if(tbarticle.COLUMN_FLAG[9]){
			dbc.setInt(k, tbarticle.getIs_delete());k++;
		}
		if(tbarticle.COLUMN_FLAG[10]){
			dbc.setInt(k, tbarticle.getArticle_lookcount());k++;
		}
		if(tbarticle.COLUMN_FLAG[11]){
			dbc.setInt(k, tbarticle.getIs_elite());k++;
		}
		if(tbarticle.COLUMN_FLAG[12]){
			dbc.setLong(k, tbarticle.getArticle_order());k++;
		}
		dbc.setInt(k, tbarticle.getArticle_id());
		dbc.executeUpdate();
		dbc.close();
		result = EXECUTE_SUCCESSS;
		return result;
	}
	public static int update(TbArticle tbarticle) {
		int result = EXECUTE_FAIL;
		try {
			DBConnect dbc = new DBConnect();
			result = update(dbc, tbarticle);
			dbc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}