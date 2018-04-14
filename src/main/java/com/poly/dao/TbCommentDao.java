package com.poly.dao;
import java.sql.*;
import java.util.*;

import com.poly.bean.TbComment;

public class  TbCommentDao  extends BaseDao {

	public static void fill(ResultSet rs, TbComment tbcomment) throws SQLException {
		tbcomment.setCom_id(rs.getInt("com_id"));//回复id
		tbcomment.setArticle_userid(rs.getInt("article_userid"));//帖子作者id
		tbcomment.setCom_userid(rs.getInt("com_userid"));//回复的为评论时，评论人id
		tbcomment.setArticle_id(rs.getInt("article_id"));//帖子id
		tbcomment.setComment_id(rs.getInt("comment_id"));//评论id（当id为0时，回复的为帖子）
		tbcomment.setUser_id(rs.getInt("user_id"));//回复人id
		tbcomment.setCom_content(rs.getString("com_content"));//回复内容
		tbcomment.setAdd_time(rs.getTimestamp("add_time"));//添加时间
		tbcomment.setIs_delete(rs.getInt("is_delete"));//是否被删除
	}

	public static List<TbComment> find() {
		DBConnect dbc = null;
		String sql = "select * from tb_comment";
		List<TbComment> list = new ArrayList<TbComment>();
		
		try {
			dbc = new DBConnect(sql);
			ResultSet rs = dbc.executeQuery();
			while (rs.next()) {
				TbComment tbcomment = new TbComment();
				fill(rs, tbcomment);
				list.add(tbcomment);
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


	public static List<TbComment> where(String subsql) {
		DBConnect dbc = null;
		String sql = "select * from tb_comment where "+subsql+"";
		List<TbComment> list = new ArrayList<TbComment>();
		
		try {
			dbc = new DBConnect(sql);
			ResultSet rs = dbc.executeQuery();
			while (rs.next()) {
				TbComment tbcomment = new TbComment();
				fill(rs, tbcomment);
				list.add(tbcomment);
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
		String sql = "select count(*) from tb_comment where "+subsql+"";
		
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
		String sql = "delete from tb_comment where "+subsql+"";
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
		String sql = "delete from tb_comment where "+subsql+"";
		try {
			dbc.prepareStatement(sql);
			dbc.executeUpdate();
			result = EXECUTE_SUCCESSS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}

	public static int save(TbComment tbcomment) throws Exception {
		int result = EXECUTE_FAIL;
		DBConnect dbc = null;
		String sql = "insert into tb_comment(`com_id`,`article_userid`,`com_userid`,`article_id`,`comment_id`,`user_id`,`com_content`,`add_time`,`is_delete`) values(?,?,?,?,?,?,?,?,?)";
		dbc = new DBConnect();
		dbc.prepareStatement(sql);
		dbc.setInt(1, tbcomment.getCom_id());
		dbc.setInt(2, tbcomment.getArticle_userid());
		dbc.setInt(3, tbcomment.getCom_userid());
		dbc.setInt(4, tbcomment.getArticle_id());
		dbc.setInt(5, tbcomment.getComment_id());
		dbc.setInt(6, tbcomment.getUser_id());
		dbc.setString(7, tbcomment.getCom_content());
		dbc.setTimestamp(8, new Timestamp(tbcomment.getAdd_time().getTime()));
		dbc.setInt(9, tbcomment.getIs_delete());
		dbc.executeUpdate();
		dbc.close();
		result = EXECUTE_SUCCESSS;
		return result;
	}

	public static int save(DBConnect dbc,TbComment tbcomment) throws Exception {
		int result = EXECUTE_FAIL;
		String sql = "insert into tb_comment(`com_id`,`article_userid`,`com_userid`,`article_id`,`comment_id`,`user_id`,`com_content`,`add_time`,`is_delete`) values(?,?,?,?,?,?,?,?,?)";
		dbc.prepareStatement(sql);
		dbc.setInt(1, tbcomment.getCom_id());
		dbc.setInt(2, tbcomment.getArticle_userid());
		dbc.setInt(3, tbcomment.getCom_userid());
		dbc.setInt(4, tbcomment.getArticle_id());
		dbc.setInt(5, tbcomment.getComment_id());
		dbc.setInt(6, tbcomment.getUser_id());
		dbc.setString(7, tbcomment.getCom_content());
		dbc.setTimestamp(8, new Timestamp(tbcomment.getAdd_time().getTime()));
		dbc.setInt(9, tbcomment.getIs_delete());
		dbc.executeUpdate();
		result = EXECUTE_SUCCESSS;
		return result;
	}

	public static int update(DBConnect dbc,TbComment tbcomment) throws Exception {
		int result = EXECUTE_FAIL;
		StringBuffer sb = new StringBuffer();
		sb.append("update tb_comment set ");
		boolean flag = false;
		if(tbcomment.COLUMN_FLAG[0]){
			if(flag){
				sb.append(",com_id=?");
			}else{
				sb.append("com_id=?");
				flag=true;
			}
		}
		if(tbcomment.COLUMN_FLAG[1]){
			if(flag){
				sb.append(",article_userid=?");
			}else{
				sb.append("article_userid=?");
				flag=true;
			}
		}
		if(tbcomment.COLUMN_FLAG[2]){
			if(flag){
				sb.append(",com_userid=?");
			}else{
				sb.append("com_userid=?");
				flag=true;
			}
		}
		if(tbcomment.COLUMN_FLAG[3]){
			if(flag){
				sb.append(",article_id=?");
			}else{
				sb.append("article_id=?");
				flag=true;
			}
		}
		if(tbcomment.COLUMN_FLAG[4]){
			if(flag){
				sb.append(",comment_id=?");
			}else{
				sb.append("comment_id=?");
				flag=true;
			}
		}
		if(tbcomment.COLUMN_FLAG[5]){
			if(flag){
				sb.append(",user_id=?");
			}else{
				sb.append("user_id=?");
				flag=true;
			}
		}
		if(tbcomment.COLUMN_FLAG[6]){
			if(flag){
				sb.append(",com_content=?");
			}else{
				sb.append("com_content=?");
				flag=true;
			}
		}
		if(tbcomment.COLUMN_FLAG[7]){
			if(flag){
				sb.append(",add_time=?");
			}else{
				sb.append("add_time=?");
				flag=true;
			}
		}
		if(tbcomment.COLUMN_FLAG[8]){
			if(flag){
				sb.append(",is_delete=?");
			}else{
				sb.append("is_delete=?");
				flag=true;
			}
		}
		sb.append(" where com_id=?");
		dbc = new DBConnect();
		dbc.prepareStatement(sb.toString());
		int k=1;
		if(tbcomment.COLUMN_FLAG[0]){
			dbc.setInt(k, tbcomment.getCom_id());k++;
		}
		if(tbcomment.COLUMN_FLAG[1]){
			dbc.setInt(k, tbcomment.getArticle_userid());k++;
		}
		if(tbcomment.COLUMN_FLAG[2]){
			dbc.setInt(k, tbcomment.getCom_userid());k++;
		}
		if(tbcomment.COLUMN_FLAG[3]){
			dbc.setInt(k, tbcomment.getArticle_id());k++;
		}
		if(tbcomment.COLUMN_FLAG[4]){
			dbc.setInt(k, tbcomment.getComment_id());k++;
		}
		if(tbcomment.COLUMN_FLAG[5]){
			dbc.setInt(k, tbcomment.getUser_id());k++;
		}
		if(tbcomment.COLUMN_FLAG[6]){
			dbc.setString(k, tbcomment.getCom_content());k++;
		}
		if(tbcomment.COLUMN_FLAG[7]){
			dbc.setTimestamp(k, new Timestamp(tbcomment.getAdd_time().getTime()));k++;
		}
		if(tbcomment.COLUMN_FLAG[8]){
			dbc.setInt(k, tbcomment.getIs_delete());k++;
		}
		dbc.setInt(k, tbcomment.getCom_id());
		dbc.executeUpdate();
		dbc.close();
		result = EXECUTE_SUCCESSS;
		return result;
	}
	public static int update(TbComment tbcomment) {
		int result = EXECUTE_FAIL;
		try {
			DBConnect dbc = new DBConnect();
			result = update(dbc, tbcomment);
			dbc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}