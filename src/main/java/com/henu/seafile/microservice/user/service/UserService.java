package com.henu.seafile.microservice.user.service;

import java.util.List;
import java.util.Map;

import com.henu.seafile.common.ServiceResponse;
import com.henu.seafile.microservice.user.pojo.User;

public interface UserService {

	ServiceResponse<Map<String, Object>> createUser(String username, String password, String token);
	
	ServiceResponse<Map<String, Object>> getToken(String username, String password);
	
	ServiceResponse<Map<String, Object>> queryUser(String username, String password);
	
	ServiceResponse<Map<String, Object>> queryUserByToken(String token);
	
	ServiceResponse<List<User>> queryUsers();
	
	ServiceResponse<String> updateUserByToken(User user);
	
	ServiceResponse<String> getLocalIP();
}
