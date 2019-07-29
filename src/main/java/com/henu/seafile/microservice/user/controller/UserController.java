package com.henu.seafile.microservice.user.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.henu.seafile.common.ResponseCode;
import com.henu.seafile.common.ServiceResponse;
import com.henu.seafile.microservice.user.container.UserPool;
import com.henu.seafile.microservice.user.pojo.PoolInfo;
import com.henu.seafile.microservice.user.pojo.User;
import com.henu.seafile.microservice.user.service.UserService;
import com.henu.seafile.util.GetIpUtil;
import com.henu.seafile.util.HttpServletRequestUtil;
import com.henu.seafile.util.NameAndPasswdUtil;
import com.mysql.fabric.xmlrpc.base.Data;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 登录接口 (1)用户登录成功后将token存放进session, (2)管理员登录成功后将session和admin存入session
	 * (3)接口返回给前端token，以后前端的请求都将token加入到header中访问后台接口，不然会被拦截
	 * 
	 * 关于一个账号只能登录一次 (1)解决方案一：不让后面的登录 (2)解决方案二：让前面已经登录的下线
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	private ServiceResponse<Map<String, Object>> login(HttpServletRequest request) {

		int interval = request.getSession().getMaxInactiveInterval();
		System.out.println("\n" + "session有效时长：" + interval + "\n");

		// 判断是管理员登录还是用户登录，如果是管理员登录，则拿token存储到session中，如果是用户登录则去数据库中找到对应的表进行验证，然后拿出表里面的token
		// 由管理员创建用户的时候一并将管理员的token跟在用户后面，部署一个seafile只对应一个管理员，所以也只有一个token，因为token是通行证，所以必须它必不可少
		String username = HttpServletRequestUtil.getString(request, "username");
		String password = HttpServletRequestUtil.getString(request, "password");
		if (username == null || password == null) {
			return ServiceResponse.createByErrorMessage("用户名或密码为空");
		}

		// (1)首先验证是否为用户，判断密码用户名错误之类的
		ServiceResponse<Map<String, Object>> userResponse = userService.queryUser(username, password);
		if (userResponse.getStatus() == ResponseCode.SUCCESS.getCode()) {

			request.getSession().setAttribute("token", userResponse.getData().get("data").toString());
			pushUser(request);
			return userResponse;
		}
		// (1)首先验证是否为管理员:获取token
		ServiceResponse<Map<String, Object>> adminResponse = userService.getToken(username, password);
		if (adminResponse.getStatus() == ResponseCode.SUCCESS.getCode()) {
			String token = adminResponse.getData().get("data").toString();
			System.out.println("管理员 :" + token);
			request.getSession().setAttribute("token", token);
			request.getSession().setAttribute("admin", "admin");// 用于区别是用户登录还是管理员登录

			pushUser(request);
			return adminResponse;
		}

		return ServiceResponse.createByErrorMessage("用户名或密码不正确");
	}

	

	private void pushUser(HttpServletRequest request) {

		String token = (String) request.getSession().getAttribute("token");
		String ip = (String) request.getSession().getAttribute("ip");

		System.out.println("用户token：" + token + "  客户端ip：" + ip);
		PoolInfo poolInfo = new PoolInfo();
		poolInfo.setToken(token);
		poolInfo.setIp(ip);
		if (UserPool.isDrop(poolInfo)) {
			UserPool.pop(poolInfo);
		}
		UserPool.push(poolInfo);
		System.out.println("当前pool里的人数: " + UserPool.getLength());
		System.out.println(UserPool.getUserPool());
	}

	@RequestMapping(value = "/createuser", method = RequestMethod.POST)
	private ServiceResponse<Map<String, String>> createUser(HttpServletRequest request) {
		String username = HttpServletRequestUtil.getString(request, "username");
		String password = HttpServletRequestUtil.getString(request, "password");
		if (username == null || password == null) {
			return ServiceResponse.createByErrorMessage("用户名或密码为空");
		}
		// 验证密码格式
		ServiceResponse<Map<String, String>> response = NameAndPasswdUtil.checkNameAndPasswd(username, password);
		if (response.getStatus() == ResponseCode.ERROR.getCode()) {
			return response;
		}
		String token = (String) request.getSession().getAttribute("token");

		System.out.println(username + " " + password);
		ServiceResponse<Map<String, Object>> responce = userService.createUser(username, password, token);
		if (responce.getStatus() == ResponseCode.SUCCESS.getCode()) {
			return ServiceResponse.createByErrorMessage("success!");
		} else {
			return ServiceResponse.createByErrorMessage("非管理员，没有权限");
		}

	}

	/**
	 * 如果登录，才能访问这个url 所以能访问到url说明要么是用户要么是管理员 通过admin排除用户 如果操作继续执行，说明操作者就是管理员
	 * 
	 * @param request
	 * @return 返回的data类型是List ！！！
	 */
	@RequestMapping(value = "/getusers", method = RequestMethod.GET)
	private ServiceResponse<List<User>> getUsers(HttpServletRequest request) {

		String admin = (String) request.getSession().getAttribute("admin");

		System.out.println(admin);
		if (admin == null || !admin.equals("admin")) {
			return ServiceResponse.createByErrorMessage("非管理员，没有权限");
		}
		return userService.queryUsers();

	}

	@RequestMapping(value = "/getuser", method = RequestMethod.POST)
	private ServiceResponse<Map<String, Object>> getUser(HttpServletRequest request) {

		String admin = (String) request.getSession().getAttribute("admin");
		String userToken = HttpServletRequestUtil.getString(request, "userToken");
		System.out.println("admin:" + admin + " " + "userToken: " + userToken);
		if (admin == null || !admin.equals("admin")) {
			return ServiceResponse.createByErrorMessage("非管理员，没有权限");
		}
		if (userToken == null) {
			return ServiceResponse.createByErrorMessage("操作失败，没有用户信息");
		}
		return userService.queryUserByToken(userToken);

	}

	@RequestMapping(value = "/changeuserpasswd", method = RequestMethod.POST)
	private ServiceResponse<String> changeUserPasswd(HttpServletRequest request) {

		String admin = (String) request.getSession().getAttribute("admin");
		String userToken = HttpServletRequestUtil.getString(request, "userToken");
		String userPasswd = HttpServletRequestUtil.getString(request, "userPasswd");
		System.out.println("admin:" + admin + " " + "userToken: " + userToken);
		if (admin == null || !admin.equals("admin")) {
			return ServiceResponse.createByErrorMessage("非管理员，没有权限");
		}
		if (userToken == null || userPasswd == null) {
			return ServiceResponse.createByErrorMessage("操作失败，没有用户信息");
		}
		User user = new User();
		user.setToken(userToken);
		user.setPassword(userPasswd);
		return userService.updateUserByToken(user);
	}

	@RequestMapping(value = "/forbiduser", method = RequestMethod.POST)
	private ServiceResponse<String> forbidUser(HttpServletRequest request) {

		String admin = (String) request.getSession().getAttribute("admin");
		String userToken = HttpServletRequestUtil.getString(request, "userToken");
		String isUseful = HttpServletRequestUtil.getString(request, "isUseful");
		System.out.println("admin:" + admin + " " + "userToken: " + userToken);
		if (admin == null || !admin.equals("admin")) {
			return ServiceResponse.createByErrorMessage("非管理员，没有权限");
		}
		if (userToken == null || isUseful == null) {
			return ServiceResponse.createByErrorMessage("操作失败，没有用户信息");
		}
		User user = new User();
		user.setToken(userToken);
		if (isUseful.equals("true")) {
			user.setUseful(true);
		} else if (isUseful.equals("false")) {
			user.setUseful(false);
		} else {
			return ServiceResponse.createByErrorMessage("操作失败，无效的用户信息");
		}
		return userService.updateUserByToken(user);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> logout(HttpServletRequest request) {

		String token = (String)request.getSession().getAttribute("token");
		String ip = (String)request.getSession().getAttribute("ip");
		
		
		//获取session  
		HttpSession   session   =   request.getSession();    
		// 获取session中所有的键值  
		Enumeration<String> attrs = session.getAttributeNames();  
		// 遍历attrs中的
		while(attrs.hasMoreElements()) {
		// 获取session键值  
		    String name = attrs.nextElement().toString();
		    // 根据键值取session中的值  
		    Object vakue = session.getAttribute(name);
		    // 打印结果 
		    System.out.println("------" + name + ":" + vakue +"--------\n");
		}
	
		PoolInfo poolInfo = new PoolInfo();
		poolInfo.setToken(token);
		poolInfo.setIp(ip);
		UserPool.pop(poolInfo);
		request.getSession().invalidate();
		System.out.println("退出成功！");
		System.out.println("当前pool里的人数: " + UserPool.getLength());
		return ServiceResponse.createBySuccess();
	}

	@RequestMapping(value = "/getlocalip", method = RequestMethod.GET)
	private ServiceResponse<String> getLocalIP(HttpServletRequest request) {

		return userService.getLocalIP();
	}

	@RequestMapping(value = "/getonlineusernumber", method = RequestMethod.GET)
	private ServiceResponse<Integer> getOnlineUserNumber(HttpServletRequest request) {

		return ServiceResponse.createBySuccessData(UserPool.getLength());
	}
}
