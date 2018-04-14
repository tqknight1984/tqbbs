package com.poly.dao;
import java.sql.*;
import java.util.*;

import com.poly.bean.TbPlate;

public class  TbPlateDao  extends BaseDao {

	public static void fill(ResultSet rs, TbPlate tbplate) throws SQLException {
		tbplate.setPlate_id(rs.getInt("plate_id"));//板块id
		tbplate.setPlate_name(rs.getString("plate_name"));//板块名称
		tbplate.setAdd_time(rs.getTimestamp("add_time"));//添加时间
		tbplate.setIs_in(rs.getInt("is_in"));//是否为内部板块
		tbplate.setIs_delete(rs.getInt("is_delete"));//是否被删除
	}

	public static List<TbPlate> find() {
		DBConnect dbc = null;
		String sql = "select * from tb_plate";
		List<TbPlate> list = new ArrayList<TbPlate>();
		
		try {
			dbc = new DBConnect(sql);
			ResultSet rs = dbc.executeQuery();
			while (rs.next()) {
				TbPlate tbplate = new TbPlate();
				fill(rs, tbplate);
				list.add(tbplate);
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


	public static List<TbPlate> where(String subsql) {
		DBConnect dbc = null;
		String sql = "select * from tb_plate where "+subsql+"";
		List<TbPlate> list = new ArrayList<TbPlate>();
		
		try {
			dbc = new DBConnect(sql);
			ResultSet rs = dbc.executeQuery();
			while (rs.next()) {
				TbPlate tbplate = new TbPlate();
				fill(rs, tbplate);
				list.add(tbplate);
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
		String sql = "select count(*) from tb_plate where "+subsql+"";
		
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
		String sql = "delete from tb_plate where "+subsql+"";
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
		String sql = "delete from tb_plate where "+subsql+"";
		try {
			dbc.prepareStatement(sql);
			dbc.executeUpdate();
			result = EXECUTE_SUCCESSS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}

	public static int save(TbPlate tbplate) throws Exception {
		int result = EXECUTE_FAIL;
		DBConnect dbc = null;
		String sql = "insert into tb_plate(`plate_id`,`plate_name`,`add_time`,`is_in`,`is_delete`) values(?,?,?,?,?)";
		dbc = new DBConnect();
		dbc.prepareStatement(sql);
		dbc.setInt(1, tbplate.getPlate_id());
		dbc.setString(2, tbplate.getPlate_name());
		dbc.setTimestamp(3, new Timestamp(tbplate.getAdd_time().getTime()));
		dbc.setInt(4, tbplate.getIs_in());
		dbc.setInt(5, tbplate.getIs_delete());
		dbc.executeUpdate();
		dbc.close();
		result = EXECUTE_SUCCESSS;
		return result;
	}

	public static int save(DBConnect dbc,TbPlate tbplate) throws Exception {
		int result = EXECUTE_FAIL;
		String sql = "insert into tb_plate(`plate_id`,`plate_name`,`add_time`,`is_in`,`is_delete`) values(?,?,?,?,?)";
		dbc.prepareStatement(sql);
		dbc.setInt(1, tbplate.getPlate_id());
		dbc.setString(2, tbplate.getPlate_name());
		dbc.setTimestamp(3, new Timestamp(tbplate.getAdd_time().getTime()));
		dbc.setInt(4, tbplate.getIs_in());
		dbc.setInt(5, tbplate.getIs_delete());
		dbc.executeUpdate();
		result = EXECUTE_SUCCESSS;
		return result;
	}

	public static int update(DBConnect dbc,TbPlate tbplate) throws Exception {
		int result = EXECUTE_FAIL;
		StringBuffer sb = new StringBuffer();
		sb.append("update tb_plate set ");
		boolean flag = false;
		if(tbplate.COLUMN_FLAG[0]){
			if(flag){
				sb.append(",plate_id=?");
			}else{
				sb.append("plate_id=?");
				flag=true;
			}
		}
		if(tbplate.COLUMN_FLAG[1]){
			if(flag){
				sb.append(",plate_name=?");
			}else{
				sb.append("plate_name=?");
				flag=true;
			}
		}
		if(tbplate.COLUMN_FLAG[2]){
			if(flag){
				sb.append(",add_time=?");
			}else{
				sb.append("add_time=?");
				flag=true;
			}
		}
		if(tbplate.COLUMN_FLAG[3]){
			if(flag){
				sb.append(",is_in=?");
			}else{
				sb.append("is_in=?");
				flag=true;
			}
		}
		if(tbplate.COLUMN_FLAG[4]){
			if(flag){
				sb.append(",is_delete=?");
			}else{
				sb.append("is_delete=?");
				flag=true;
			}
		}
		sb.append(" where plate_id=?");
		dbc = new DBConnect();
		dbc.prepareStatement(sb.toString());
		int k=1;
		if(tbplate.COLUMN_FLAG[0]){
			dbc.setInt(k, tbplate.getPlate_id());k++;
		}
		if(tbplate.COLUMN_FLAG[1]){
			dbc.setString(k, tbplate.getPlate_name());k++;
		}
		if(tbplate.COLUMN_FLAG[2]){
			dbc.setTimestamp(k, new Timestamp(tbplate.getAdd_time().getTime()));k++;
		}
		if(tbplate.COLUMN_FLAG[3]){
			dbc.setInt(k, tbplate.getIs_in());k++;
		}
		if(tbplate.COLUMN_FLAG[4]){
			dbc.setInt(k, tbplate.getIs_delete());k++;
		}
		dbc.setInt(k, tbplate.getPlate_id());
		dbc.executeUpdate();
		dbc.close();
		result = EXECUTE_SUCCESSS;
		return result;
	}
	public static int update(TbPlate tbplate) {
		int result = EXECUTE_FAIL;
		try {
			DBConnect dbc = new DBConnect();
			result = update(dbc, tbplate);
			dbc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}