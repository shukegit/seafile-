package com.henu.seafile.microservice.user.service.serviceImpl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.henu.seafile.common.PostMethod;
import com.henu.seafile.common.PutMethod;
import com.henu.seafile.common.ResponseCode;
import com.henu.seafile.common.ServiceResponse;
import com.henu.seafile.microservice.user.dao.UserDao;
import com.henu.seafile.microservice.user.pojo.User;
import com.henu.seafile.microservice.user.service.UserService;
import com.henu.seafile.util.NameAndPasswdUtil;

@Service
public class UserServiceImpl implements UserService{
	
	@Value("${seafile.api.GetTokenUrl}")
	String GetTokenUrl;
	@Value("${seafile.api.CreateUserUrl}")
	String CreateUserUrl;
	
	@Autowired
	private UserDao userDao;

	@Override
	public ServiceResponse<Map<String, Object>> createUser(String username, String password, String token) {

		PutMethod putMethod = new PutMethod();
		Map<String, String> map = new HashMap<>();
		
		map.put("password", password);
		//拼接用户名
		System.out.println("username:" + NameAndPasswdUtil.transformName(username));
		String url = CreateUserUrl + NameAndPasswdUtil.transformName(username) + "/";//拼接创建用户的接口
		System.out.println(url);
		ServiceResponse<Map<String, Object>> response = putMethod.createUserRun(map, url, token);
		if(response.getStatus() == ResponseCode.ERROR.getCode()) {
			
			return ServiceResponse.createByErrorMessage("创建用户失败");
		}
		//创建成功后拿到用户自己的token,拿不到就将父token存储进去
		ServiceResponse<Map<String, Object>> response2 = getToken(NameAndPasswdUtil.transformName(username), password);
		if(response2.getStatus() == ResponseCode.SUCCESS.getCode()) {
			token = response2.getData().get("data").toString();
			System.out.println("得到了用户的token：" + token);
		} else {
			
		}
		//如果管理员创建用户成功的话就将用户加入到自己的数据表中
		//加密密码
//		String tempPasswd = password;
//		password = MD5Util.getMD5Password(password);
//		if (password == null) {
//			System.out.println("加密失败");
//			password = tempPasswd;
//		}
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setToken(token);
		user.setCreateTime(new Date());
		System.out.println("创建用户成功:" + response.getData().get("data"));
		System.out.println("用户名:" + username);
		System.out.println("密码:" + password);
		int row = userDao.insertUser(user);
		if(row <= 0) {
			return ServiceResponse.createByErrorMessage("添加用户至数据库失败");
		}
		return ServiceResponse.createBySuccessMessage("添加用户至数据库成功");
	}

	@Override
	public ServiceResponse<Map<String, Object>> getToken(String username, String password) {
		PostMethod postMethod = new PostMethod();		
		Map<String, String> map = new HashMap<>();
		
		map.put("password", password);
		map.put("username", username);
		String url = GetTokenUrl;
		ServiceResponse<Map<String, Object>> result = null;
		
		result = postMethod.run(map, url, null);
		
		if(result.getStatus() == ResponseCode.SUCCESS.getCode()) {
			String[] arr = result.getData().get("data").toString().replace('{', ' ').replace('}', ' ').trim().split(":");
			String token = arr[1].replace('"', ' ').trim();
			Map<String, Object> map2 = new HashMap<>();
			System.out.println("token : " + token);
			map2.put("data", token);
			return ServiceResponse.createBySuccessData(map2);
		} else {
			return ServiceResponse.createByErrorMessage("内部错误");
		}
	}
	

	@Override
	public ServiceResponse<Map<String, Object>> queryUser(String username, String password) {
//		String tempPasswd = password;
//		password = MD5Util.getMD5Password(password);
//		if (password == null) {
//			System.out.println("加密失败");
//			password = tempPasswd;
//		}
		User user = userDao.selectUser(username, password);
		System.out.println("queryUser: " + username + "  " + password);

		if(user != null) {
			System.out.println(user.getUsername() + " " + user.getPassword() + " " + user.getToken());
			Map<String, Object> map = new HashMap<>();
			map.put("data", user.getToken());
			return ServiceResponse.createBySuccessData(map);
		}
		return ServiceResponse.createByError();
	}

	@Override
	public ServiceResponse<Map<String, Object>> queryUserByToken(String token) {
		User user = userDao.selectUserByToken(token).get(0);
		System.out.println("queryUserByToken: " + token);
		if(user != null) {
			System.out.println(user.getUsername() + " " + user.getPassword() + " " + user.getToken());
			Map<String, Object> map = new HashMap<>();
			map.put("data", user.getToken());
			return ServiceResponse.createBySuccessData(map);
		}
		return ServiceResponse.createByErrorMessage("没有找到该用户");
	}
	
	@Override
	public ServiceResponse<List<User>> queryUsers() {
		
		List<User> userList = userDao.selectAllUsers();
		System.out.println("userSize : " + userList.size());
		for (User user : userList) {
			System.out.println(user.getUsername());
		}
		return ServiceResponse.createBySuccessData(userList);
	}

	@Override
	public ServiceResponse<String> getLocalIP() {
		
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println(ip);
			return ServiceResponse.createBySuccessData(ip);
		} catch (UnknownHostException e) {
			return ServiceResponse.createByErrorMessage(e.getMessage());
		}
	}

	@Override
	public ServiceResponse<String> updateUserByToken(User user) {
		
		int row = userDao.updateUser(user);
		if(row > 0) {
			return ServiceResponse.createBySuccess();
		}
		return ServiceResponse.createByError();
	}



}
