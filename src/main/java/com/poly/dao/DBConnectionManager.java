package com.poly.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;

//import com.deity.adminCT.servlets.InitServlet;


/**
 * ������DBConnectionManager֧�ֶ�һ�������������ļ��������ݿ�����
 * �صķ���.�ͻ�������Ե���getInstance()�������ʱ����Ψһʵ��.
 */

public class DBConnectionManager {

	private static final DBConnectionManager instance = new DBConnectionManager();

	private static String driverClass, jdbcUrl, user, password;

	static {
		System.out.println("DBConnectionManager static{...}");
//		servletContext.getRealPath("/");
//		init();
	}

	public static void destroyDS() {

	}

	public static DBConnectionManager getInstance() {
		return instance;
	}

	private DBConnectionManager() {
		System.out.println("create a new DBConnectionManager");
	}

	public void freeConnection(Connection conn) {
		if (conn == null) {
			return;
		}
		try {
			conn.close();
			conn = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			//Class.forName(driverClass);// Ҫ��JVM���Ҳ�����ָ�����࣬Ҳ����˵JVM��ִ�и���ľ�̬�����
			DriverManager.setLoginTimeout(10);// ����10s�ò������ӱ���
//			conn = DriverManager.getConnection(jdbcUrl, user, password);
//			conn.setAutoCommit(true);
			conn = DriverManager.getConnection("proxool.che-113");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	

	public static void init(String rootPath) {
		InputStream is = null;
		try {
			Properties props = new Properties();
			//String rootPath = InitServlet.rootPath;
//			is = new FileInputStream(rootPath + "/WEB-INF/classes/db.properties.produce");
//			props.load(is);
//
//			driverClass = props.getProperty("driverClass");
//			jdbcUrl = props.getProperty("jdbcUrl");
//			user = props.getProperty("user");
//			password = props.getProperty("password");
			PropertyConfigurator.configure(rootPath + "/WEB-INF/classes/proxool.properties");

			// dataSource = new ProxoolDataSource();
			// dataSource.setAlias("proxool.dbpool");
			// dataSource.setDriverUrl(props.getProperty(JDBC_URL));
			// dataSource.setDriver(props.getProperty(DRIVER_CLASS));
			// dataSource.setUser(props.getProperty(USER));
			// dataSource.setPassword(props.getProperty(PASSWORD));
			// dataSource.setMinimumConnectionCount(Integer.valueOf(props.getProperty(MIN)));
			// dataSource.setMaximumConnectionCount(Integer.valueOf(props.getProperty(MAX)));
			// dataSource.setMaximumActiveTime(Integer.valueOf(props.getProperty(MAX_ACTIVE_TIME)));
			// //
			// dataSource.setMaximumConnectionLifetime(Integer.valueOf(props.getProperty(MAXIMUM_CONNECTION_LIFE_TIME)));
			// //
			// dataSource.setHouseKeepingSleepTime(Integer.valueOf(props.getProperty(HOUSE_KEEPING_SLEEP_TIME)));
			// //
			// dataSource.setHouseKeepingTestSql(props.getProperty(HOUSE_KEEPING_TEST_SQL));
			// // dataSource.setTrace(true);
			// // dataSource.setVerbose(true);
			// dataSource.setPrototypeCount(Integer.valueOf(props.getProperty(PROTOTYPECOUNT)));
			
			//Class.forName(driverClass);
			
			System.out.println("read db.properties.produce");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					// just ignore it
				}
			}
		}
	}
}