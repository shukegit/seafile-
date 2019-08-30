package com.henu.seafile.microservice.file.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.henu.seafile.common.PostMethod;
import com.henu.seafile.common.PutMethod;
import com.henu.seafile.common.ResponseCode;
import com.henu.seafile.common.ServiceResponse;
import com.henu.seafile.util.HttpServletRequestUtil;


@RestController
//@RequestMapping(value = "/demo", method = RequestMethod.GET)
@RequestMapping(value = "/demo")
public class ResponseForSomethingController {


	/*
	 * 测试springboot创建是否成功
	 */
	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public String ping(HttpServletRequest request) {
		String string = request.getParameter("info");
		System.out.println(string);
		if("ping".equals(string)) {
			return "pong";
		}
		return new MyException().getMessage();
		
	}
	/*
	 * 测试连接seafile
	 */
	@SuppressWarnings("null")
	@RequestMapping(value = "/seafileping", method = RequestMethod.GET)
	public String connectSeafile() {
//		GetMethod getMethod = new GetMethod();
		

//		String url = "http://192.168.1.58:8000/api2/ping/";
		ServiceResponse<Map<String, Object>> result = null;
//		result = getMethod.run(url, null);
		return result == null ? ("error " + result.getMessage()) : ("success! " + result.getData());
	}
	
	/*
	 * 获取tocken
	 */
	@RequestMapping(value = "/getTocken", method = RequestMethod.POST)
	public ServiceResponse<Map<String, Object>> getTocken(HttpServletRequest request) {
			
		PostMethod postMethod = new PostMethod();		
		Map<String, String> map = new HashMap<>();
		
		String username = HttpServletRequestUtil.getString(request, "username");
		String password = HttpServletRequestUtil.getString(request, "password");
		System.out.println(username + "    " + password + "wwadadawdaw");
		if(username == null || password == null) {
			return ServiceResponse.createByErrorMessage("用户名或密码为空");
		}
		map.put("password", password);
		map.put("username", username);
		String url = "http://10.12.37.209:8000/api2/auth-token/";
		ServiceResponse<Map<String, Object>> result = null;
		
		result = postMethod.run(map, url, null);
	
		if(result.getStatus() == ResponseCode.SUCCESS.getCode()) {
			return result;
		} else {
			return ServiceResponse.createByErrorMessage("内部错误");
		}
	}
	/*
	 * 创建用户
	 */
	@RequestMapping(value = "/createuser", method = RequestMethod.GET)
	public String createUser() {
			
		PutMethod putMethod = new PutMethod();		
		Map<String, String> map = new HashMap<>();
//		map.put("password", "123123");
		map.put("password", "123123");
		String url = "http://192.168.1.58:8000/api2/accounts/sk@123.com/";
		ServiceResponse<Map<String, Object>> result = null;
		try {
			result = putMethod.run(map, url, null);
		} catch (IOException | InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return result.getData().get("data").toString();
	}
	
	
}
class MyException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getMessage() {
		return "no currect format";
	}
	
}
