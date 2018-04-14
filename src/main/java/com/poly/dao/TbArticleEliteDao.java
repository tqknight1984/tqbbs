package com.poly.dao;
import java.sql.*;
import java.util.*;

import com.poly.bean.TbArticleElite;

public class  TbArticleEliteDao  extends BaseDao {

	public static void fill(ResultSet rs, TbArticleElite tbarticleelite) throws SQLException {
		tbarticleelite.setElite_id(rs.getInt("elite_id"));//精华帖id
		tbarticleelite.setArticle_id(rs.getInt("article_id"));//文章id
		tbarticleelite.setAdd_time(rs.getTimestamp("add_time"));//添加时间
		tbarticleelite.setAdd_user(rs.getInt("add_user"));//操作人id
		tbarticleelite.setIs_delete(rs.getInt("is_delete"));//是否被删除
	}

	public static List<TbArticleElite> find() {
		DBConnect dbc = null;
		String sql = "select * from tb_article_elite";
		List<TbArticleElite> list = new ArrayList<TbArticleElite>();
		
		try {
			dbc = new DBConnect(sql);
			ResultSet rs = dbc.executeQuery();
			while (rs.next()) {
				TbArticleElite tbarticleelite = new TbArticleElite();
				fill(rs, tbarticleelite);
				list.add(tbarticleelite);
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


	public static List<TbArticleElite> where(String subsql) {
		DBConnect dbc = null;
		String sql = "select * from tb_article_elite where "+subsql+"";
		List<TbArticleElite> list = new ArrayList<TbArticleElite>();
		
		try {
			dbc = new DBConnect(sql);
			ResultSet rs = dbc.executeQuery();
			while (rs.next()) {
				TbArticleElite tbarticleelite = new TbArticleElite();
				fill(rs, tbarticleelite);
				list.add(tbarticleelite);
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
		String sql = "select count(*) from tb_article_elite where "+subsql+"";
		
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
		String sql = "delete from tb_article_elite where "+subsql+"";
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
		String sql = "delete from tb_article_elite where "+subsql+"";
		try {
			dbc.prepareStatement(sql);
			dbc.executeUpdate();
			result = EXECUTE_SUCCESSS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}

	public static int save(TbArticleElite tbarticleelite) throws Exception {
		int result = EXECUTE_FAIL;
		DBConnect dbc = null;
		String sql = "insert into tb_article_elite(`elite_id`,`article_id`,`add_time`,`add_user`,`is_delete`) values(?,?,?,?,?)";
		dbc = new DBConnect();
		dbc.prepareStatement(sql);
		dbc.setInt(1, tbarticleelite.getElite_id());
		dbc.setInt(2, tbarticleelite.getArticle_id());
		dbc.setTimestamp(3, new Timestamp(tbarticleelite.getAdd_time().getTime()));
		dbc.setInt(4, tbarticleelite.getAdd_user());
		dbc.setInt(5, tbarticleelite.getIs_delete());
		dbc.executeUpdate();
		dbc.close();
		result = EXECUTE_SUCCESSS;
		return result;
	}

	public static int save(DBConnect dbc,TbArticleElite tbarticleelite) throws Exception {
		int result = EXECUTE_FAIL;
		String sql = "insert into tb_article_elite(`elite_id`,`article_id`,`add_time`,`add_user`,`is_delete`) values(?,?,?,?,?)";
		dbc.prepareStatement(sql);
		dbc.setInt(1, tbarticleelite.getElite_id());
		dbc.setInt(2, tbarticleelite.getArticle_id());
		dbc.setTimestamp(3, new Timestamp(tbarticleelite.getAdd_time().getTime()));
		dbc.setInt(4, tbarticleelite.getAdd_user());
		dbc.setInt(5, tbarticleelite.getIs_delete());
		dbc.executeUpdate();
		result = EXECUTE_SUCCESSS;
		return result;
	}

	public static int update(DBConnect dbc,TbArticleElite tbarticleelite) throws Exception {
		int result = EXECUTE_FAIL;
		StringBuffer sb = new StringBuffer();
		sb.append("update tb_article_elite set ");
		boolean flag = false;
		if(tbarticleelite.COLUMN_FLAG[0]){
			if(flag){
				sb.append(",elite_id=?");
			}else{
				sb.append("elite_id=?");
				flag=true;
			}
		}
		if(tbarticleelite.COLUMN_FLAG[1]){
			if(flag){
				sb.append(",article_id=?");
			}else{
				sb.append("article_id=?");
				flag=true;
			}
		}
		if(tbarticleelite.COLUMN_FLAG[2]){
			if(flag){
				sb.append(",add_time=?");
			}else{
				sb.append("add_time=?");
				flag=true;
			}
		}
		if(tbarticleelite.COLUMN_FLAG[3]){
			if(flag){
				sb.append(",add_user=?");
			}else{
				sb.append("add_user=?");
				flag=true;
			}
		}
		if(tbarticleelite.COLUMN_FLAG[4]){
			if(flag){
				sb.append(",is_delete=?");
			}else{
				sb.append("is_delete=?");
				flag=true;
			}
		}
		sb.append(" where elite_id=?");
		dbc = new DBConnect();
		dbc.prepareStatement(sb.toString());
		int k=1;
		if(tbarticleelite.COLUMN_FLAG[0]){
			dbc.setInt(k, tbarticleelite.getElite_id());k++;
		}
		if(tbarticleelite.COLUMN_FLAG[1]){
			dbc.setInt(k, tbarticleelite.getArticle_id());k++;
		}
		if(tbarticleelite.COLUMN_FLAG[2]){
			dbc.setTimestamp(k, new Timestamp(tbarticleelite.getAdd_time().getTime()));k++;
		}
		if(tbarticleelite.COLUMN_FLAG[3]){
			dbc.setInt(k, tbarticleelite.getAdd_user());k++;
		}
		if(tbarticleelite.COLUMN_FLAG[4]){
			dbc.setInt(k, tbarticleelite.getIs_delete());k++;
		}
		dbc.setInt(k, tbarticleelite.getElite_id());
		dbc.executeUpdate();
		dbc.close();
		result = EXECUTE_SUCCESSS;
		return result;
	}
	public static int update(TbArticleElite tbarticleelite) {
		int result = EXECUTE_FAIL;
		try {
			DBConnect dbc = new DBConnect();
			result = update(dbc, tbarticleelite);
			dbc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}