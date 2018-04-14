package com.poly.util;

import com.jianzhou.sdk.BusinessService;

/**
 * 上海建周发送短信通道
 * 短信模板设置的时候，里面除了签名用【】  ，其它地方不能出现【】
 * @author niuhaidong
 *
 * 2015年9月8日
 */
public class SMSjianzhou {
	/** 短信签名 */
	public final static String SIGN = "【小胖看车团】";
	/** 账号 */
	public final static String MSG_ACCOUNT = "sdk_chetuan";
	/** 密码 */
	public final static String MSG_PWD = "20150908";
	
	/**
	 * 发送短信
	 * @param phone
	 * @param msg
	 * @return
	 * @throws Exception 
	 */
	public static int sendPhoneMsg(String phone, String msg) throws Exception {
		BusinessService bs = new BusinessService();
		int result = 0;
		bs.setWebService("http://www.jianzhou.sh.cn/JianzhouSMSWSServer/services/BusinessService");
		result = bs.sendBatchMessage(MSG_ACCOUNT, MSG_PWD, phone, msg + SIGN);
		if(result < 0) {
			throw new Exception("发送短信异常");
		}
		return result;
	}
	
	
	public static void main(final String[] args) throws Exception {
		BusinessService bs = new BusinessService();
		System.out.println("333:" + sendPhoneMsg("13800000000","测试"));
	}
}
