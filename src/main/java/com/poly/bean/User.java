package com.poly.bean;

import java.util.Date;
/**
 * 用户
 * @author mfw
 *
 */
public class User {
	/*
	 * 用户id，主键
	 */
	private Integer user_id;
	/*
	 * 用户名，唯一
	 */
	private String user_name;
	/*
	 * 密码
	 */
	private String user_password;
	/*
	 * 昵称
	 */
	private String user_nickname;
	/*
	 * 手机号，唯一
	 */
	private String user_phone;
	/*
	 * 等级
	 */
	private Integer user_grade;
	/*
	 * 头像
	 */
	private String user_photo;
	/*
	 * 注册时间
	 */
	private Date reg_time;
	/*
	 * 最后登录时间
	 */
	private Date log_time;
	/*
	 * 用户状态(0-启用)
	 */
	private Integer user_state;
	/*
	 *用户角色 0-普通用户,1-内部用户
	 */
	private Integer role;
	
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_nickname() {
		return user_nickname;
	}
	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	public String getUser_photo() {
		return user_photo;
	}
	public void setUser_photo(String user_photo) {
		this.user_photo = user_photo;
	}
	public Date getReg_time() {
		return reg_time;
	}
	public void setReg_time(Date reg_time) {
		this.reg_time = reg_time;
	}
	public Date getLog_time() {
		return log_time;
	}
	public void setLog_time(Date log_time) {
		this.log_time = log_time;
	}
	public Integer getUser_state() {
		return user_state;
	}
	public void setUser_state(Integer user_state) {
		this.user_state = user_state;
	}
	public Integer getUser_grade() {
		return user_grade;
	}
	public void setUser_grade(Integer user_grade) {
		this.user_grade = user_grade;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	
	
	
	
	
}
