package com.henu.seafile.microservice.user.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.henu.seafile.microservice.user.pojo.User;

@Mapper
public interface UserDao {

	List<User> selectAllUsers();
	
	User selectUser(@Param("username")String username, @Param("password")String password);
	
	List<User> selectUserByToken(String token);
	
	int insertUser(User user);
	
	/**
	 * 通过token更新user
	 * @param user 传过来的user的token必须非空，不然找不到对应值user，其余属性，谁非空就更新谁
	 * @return
	 */
	int updateUser(User user);
	
	/**
	 * 通过token删除user
	 * @param user
	 * @return
	 */
	int deleteUser(String token);
	
	/**
	 * 删除整个表
	 * @return
	 */
	int deleteUsers();
	
}
