package com.henu.seafile.util;

import java.util.Map;

import com.henu.seafile.common.ServiceResponse;

/**
 * 注册时进行匹配，登录时也要进行匹配
 * 从数据库中提取用户名时也要进行匹配删除
 * @author Lenovo
 *
 */
public class NameAndPasswdUtil {

	public static ServiceResponse<Map<String, String>> checkNameAndPasswd(String name, String password){
		
		char[] names = name.toCharArray();
		char[] passwords = password.toCharArray();
		//不合法返回null
		if(names.length > 10) {
			return ServiceResponse.createByErrorMessage("用户名长度必须小于10");
		}
		if(passwords.length < 6 || passwords.length > 12) {
			return ServiceResponse.createByErrorMessage("密码长度必须在6~12位");	
		}
		for (char c : names) {
			if(c == '@') {
				return ServiceResponse.createByErrorMessage("用户名里不能含有'@'");
			}
		}
		int downLetter = 0;
		int upLetter = 0;
		int number = 0;
		for (char c : passwords) {
			if(c >= 'A' && c <= 'Z') upLetter ++;
			if(c >= 'a' && c <= 'z') downLetter ++;
			if(c >= '0' && c <= '9') number ++;
		}
		if(upLetter == 0 || downLetter == 0 || number == 0) {
			return ServiceResponse.createByErrorMessage("密码里必须含有大写字母，小写字母和数字");
		}
		return ServiceResponse.createBySuccess();
	}
	
	public static String transformName(String username) {
		
		return username + AddToName.END.getDesc();
	}

	private enum AddToName {
		END(0, "@seafile.henu.com");
		
		private final int code;
		private final String desc;
		
		private AddToName(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		@SuppressWarnings("unused")
		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
		
	}
}
