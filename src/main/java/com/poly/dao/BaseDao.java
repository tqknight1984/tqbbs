package com.poly.dao;

public class BaseDao {

	public static final int EXECUTE_FAIL = -1;
	public static final int EXECUTE_SUCCESSS = 1;

	public static int execute(String sql) {
		int result = EXECUTE_FAIL;
		DBConnect dbc = null;
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

	public static int execute(DBConnect dbc, String sql) {
		int result = EXECUTE_FAIL;
		try {
			dbc.prepareStatement(sql);
			dbc.executeUpdate();
			result = EXECUTE_SUCCESSS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
}
