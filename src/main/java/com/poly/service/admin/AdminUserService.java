package com.poly.service.admin;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.poly.bean.AdminUser;
import com.poly.dao.AdminUserDao;

@Service("adminUserService")
public class AdminUserService {
	@Resource(name="adminUserDao")
	private AdminUserDao adminUserDao;
	
	public AdminUser getAdminUserDaoByUsername(String username) throws SQLException{
		return adminUserDao.getAdminUserByUsername(username);
	}
}
