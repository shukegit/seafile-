package com.henu.seafile.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.henu.seafile.microservice.user.container.UserPool;
import com.henu.seafile.microservice.user.pojo.PoolInfo;
import com.henu.seafile.util.websocket.WebSocket;



/**
 * 检查每次请求的token和ip是否在hashMap中
 * 注意：这儿的拦截是被动的，也就是客户端有请求，服务器才会去检查
 * @author Lenovo
 *
 */
@Component
public class MyInterceptorUtil implements HandlerInterceptor {
	// 在访问接口前执行
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

		System.out.println("\n**************拦截器(检查用户是否在pool中)调用开始****************");


		//对所有操作都在pool中检查其token和ip，如果找到，说明有权限操作，如果没有，则没有权限操作
		String token = (String) request.getSession().getAttribute("token");
		String ip = (String) request.getSession().getAttribute("ip");
		
		PoolInfo poolInfo = new PoolInfo();
		poolInfo.setToken(token);
		poolInfo.setIp(ip);
		boolean b = UserPool.isExit(token, ip);
		if(!b) {
			System.out.println("拦截器拦截：userPool中没有该用户");
			response.sendRedirect("/page/relogin");
//			WebSocket socket = new WebSocket();
//			socket.sendMessage("/page/relogin", token);
			return false;
		}
		System.out.println("拦截器通过");

		System.out.println("**************拦截器(检查用户是否在pool中)调用结束****************\n");
		return true;
	}

	// 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o,
			ModelAndView modelAndView) throws Exception {
//        System.out.println("postHandle被调用 : Controller方法调用之后触发");
	}

	// 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e)
			throws Exception {
//        System.out.println("afterCompletion被调用：请求结束之后被调用");
	}

}
