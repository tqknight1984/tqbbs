package com.poly.dao;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.springframework.stereotype.Component;

import com.poly.bean.AdminUser;
@Component("adminUserDao")
public class AdminUserDao {
	@Resource(name="queryRunner")
	QueryRunner queryRunner;
	public AdminUser getAdminUserByUsername(String username) throws SQLException{
		AdminUser user = null;
		String sql = "select * from tb_admin_user where username=?";
		user = queryRunner.query(sql,new BeanHandler<AdminUser>(AdminUser.class),new Object[]{username});
		return user;
	}
		
}
