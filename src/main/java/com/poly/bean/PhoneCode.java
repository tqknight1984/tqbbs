package com.poly.bean;

import java.util.Date;
/**
 * 手机短信信息
 * @author mfw
 *
 */
public class PhoneCode {
	/*
	 * 手机号
	 */
	private String phone;
	/*
	 * 验证码
	 */
	private String code;
	/*
	 *发送时间 
	 */
	private Date sendTime;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	
}
