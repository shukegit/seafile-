package com.henu.seafile.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.henu.seafile.microservice.user.container.UserPool;
import com.henu.seafile.microservice.user.pojo.PoolInfo;



/**
 * 这个类不会检查登录
 * @author Lenovo
 *
 */
@Component
public class MyInterceptorUtil implements HandlerInterceptor {
	// 在访问接口前执行
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

		System.out.println("\n**************拦截器调用开始****************");

		// 验证token
//		try {
//			String token = (String) request.getSession().getAttribute("token");
//
//			if (token == null) {
//				System.out.println("拦截器拦截：获取token失败");
//				response.sendRedirect("/page/login");
//				return false;
//			}
//		} catch (Exception e) {
//			System.out.println("拦截器拦截：获取token失败");
//			response.sendRedirect("/page/login");
//			return false;
//		}
		//从容器中检查是否已经登录过了，如果登录过了，则让以前登录的下线
		String token = (String) request.getSession().getAttribute("token");
		String ip = (String) request.getSession().getAttribute("ip");
		
		PoolInfo poolInfo = new PoolInfo();
		poolInfo.setToken(token);
		poolInfo.setIp(ip);
		boolean b = UserPool.isExit(token);
		if(!b) {
			System.out.println("拦截器拦截：userPool中没有该用户");
			response.sendRedirect("/page/login");
			return false;
		}
		System.out.println("拦截器通过");

		System.out.println("**************拦截器调用结束****************\n");
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
