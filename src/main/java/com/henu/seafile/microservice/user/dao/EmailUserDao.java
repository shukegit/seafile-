package com.henu.seafile.microservice.user.dao;

import com.henu.seafile.microservice.user.pojo.EmailUser;

public interface EmailUserDao {
	
	int insertUserOrAdmin(EmailUser emailUser);
	
	/**
	 * 通过email删除用户
	 * @param emailUser
	 * @return
	 */
	int deleteUser(String email);
	
	/**
	 * 删除所有的user
	 * @return
	 */
	int deleteUsers();

}
