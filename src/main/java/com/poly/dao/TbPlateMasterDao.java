package com.poly.dao;
import java.sql.*;
import java.util.*;

import com.poly.bean.TbPlateMaster;

public class  TbPlateMasterDao  extends BaseDao {

	public static void fill(ResultSet rs, TbPlateMaster tbplatemaster) throws SQLException {
		tbplatemaster.setMaster_id(rs.getInt("master_id"));//版主id
		tbplatemaster.setUser_id(rs.getInt("user_id"));//用户id
		tbplatemaster.setPlate_id(rs.getInt("plate_id"));//板块id
		tbplatemaster.setAdd_time(rs.getTimestamp("add_time"));//添加时间
		tbplatemaster.setIs_delete(rs.getInt("is_delete"));//是否被删除
	}

	public static List<TbPlateMaster> find() {
		DBConnect dbc = null;
		String sql = "select * from tb_plate_master";
		List<TbPlateMaster> list = new ArrayList<TbPlateMaster>();
		
		try {
			dbc = new DBConnect(sql);
			ResultSet rs = dbc.executeQuery();
			while (rs.next()) {
				TbPlateMaster tbplatemaster = new TbPlateMaster();
				fill(rs, tbplatemaster);
				list.add(tbplatemaster);
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


	public static List<TbPlateMaster> where(String subsql) {
		DBConnect dbc = null;
		String sql = "select * from tb_plate_master where "+subsql+"";
		List<TbPlateMaster> list = new ArrayList<TbPlateMaster>();
		
		try {
			dbc = new DBConnect(sql);
			ResultSet rs = dbc.executeQuery();
			while (rs.next()) {
				TbPlateMaster tbplatemaster = new TbPlateMaster();
				fill(rs, tbplatemaster);
				list.add(tbplatemaster);
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
		String sql = "select count(*) from tb_plate_master where "+subsql+"";
		
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
		String sql = "delete from tb_plate_master where "+subsql+"";
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
		String sql = "delete from tb_plate_master where "+subsql+"";
		try {
			dbc.prepareStatement(sql);
			dbc.executeUpdate();
			result = EXECUTE_SUCCESSS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}

	public static int save(TbPlateMaster tbplatemaster) throws Exception {
		int result = EXECUTE_FAIL;
		DBConnect dbc = null;
		String sql = "insert into tb_plate_master(`master_id`,`user_id`,`plate_id`,`add_time`,`is_delete`) values(?,?,?,?,?)";
		dbc = new DBConnect();
		dbc.prepareStatement(sql);
		dbc.setInt(1, tbplatemaster.getMaster_id());
		dbc.setInt(2, tbplatemaster.getUser_id());
		dbc.setInt(3, tbplatemaster.getPlate_id());
		dbc.setTimestamp(4, new Timestamp(tbplatemaster.getAdd_time().getTime()));
		dbc.setInt(5, tbplatemaster.getIs_delete());
		dbc.executeUpdate();
		dbc.close();
		result = EXECUTE_SUCCESSS;
		return result;
	}

	public static int save(DBConnect dbc,TbPlateMaster tbplatemaster) throws Exception {
		int result = EXECUTE_FAIL;
		String sql = "insert into tb_plate_master(`master_id`,`user_id`,`plate_id`,`add_time`,`is_delete`) values(?,?,?,?,?)";
		dbc.prepareStatement(sql);
		dbc.setInt(1, tbplatemaster.getMaster_id());
		dbc.setInt(2, tbplatemaster.getUser_id());
		dbc.setInt(3, tbplatemaster.getPlate_id());
		dbc.setTimestamp(4, new Timestamp(tbplatemaster.getAdd_time().getTime()));
		dbc.setInt(5, tbplatemaster.getIs_delete());
		dbc.executeUpdate();
		result = EXECUTE_SUCCESSS;
		return result;
	}

	public static int update(DBConnect dbc,TbPlateMaster tbplatemaster) throws Exception {
		int result = EXECUTE_FAIL;
		StringBuffer sb = new StringBuffer();
		sb.append("update tb_plate_master set ");
		boolean flag = false;
		if(tbplatemaster.COLUMN_FLAG[0]){
			if(flag){
				sb.append(",master_id=?");
			}else{
				sb.append("master_id=?");
				flag=true;
			}
		}
		if(tbplatemaster.COLUMN_FLAG[1]){
			if(flag){
				sb.append(",user_id=?");
			}else{
				sb.append("user_id=?");
				flag=true;
			}
		}
		if(tbplatemaster.COLUMN_FLAG[2]){
			if(flag){
				sb.append(",plate_id=?");
			}else{
				sb.append("plate_id=?");
				flag=true;
			}
		}
		if(tbplatemaster.COLUMN_FLAG[3]){
			if(flag){
				sb.append(",add_time=?");
			}else{
				sb.append("add_time=?");
				flag=true;
			}
		}
		if(tbplatemaster.COLUMN_FLAG[4]){
			if(flag){
				sb.append(",is_delete=?");
			}else{
				sb.append("is_delete=?");
				flag=true;
			}
		}
		sb.append(" where master_id=?");
		dbc = new DBConnect();
		dbc.prepareStatement(sb.toString());
		int k=1;
		if(tbplatemaster.COLUMN_FLAG[0]){
			dbc.setInt(k, tbplatemaster.getMaster_id());k++;
		}
		if(tbplatemaster.COLUMN_FLAG[1]){
			dbc.setInt(k, tbplatemaster.getUser_id());k++;
		}
		if(tbplatemaster.COLUMN_FLAG[2]){
			dbc.setInt(k, tbplatemaster.getPlate_id());k++;
		}
		if(tbplatemaster.COLUMN_FLAG[3]){
			dbc.setTimestamp(k, new Timestamp(tbplatemaster.getAdd_time().getTime()));k++;
		}
		if(tbplatemaster.COLUMN_FLAG[4]){
			dbc.setInt(k, tbplatemaster.getIs_delete());k++;
		}
		dbc.setInt(k, tbplatemaster.getMaster_id());
		dbc.executeUpdate();
		dbc.close();
		result = EXECUTE_SUCCESSS;
		return result;
	}
	public static int update(TbPlateMaster tbplatemaster) {
		int result = EXECUTE_FAIL;
		try {
			DBConnect dbc = new DBConnect();
			result = update(dbc, tbplatemaster);
			dbc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}