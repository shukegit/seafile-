package com.henu.seafile.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class IPIntercepterUtil implements HandlerInterceptor {
	// 在访问接口前执行
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

		System.out.println("\n**************获取IP拦截器调用开始****************");

		String ip = GetIpUtil.getClientIp(request);
		System.out.println("客户端ip：" + ip);
		if (ip == null) {
			System.out.println("ip为空");
			response.sendRedirect("/page/login");
			return false;
		}
//		else {
//			//检查ip是否合理
//			boolean isCorrect = matches(ip);
//			if(!isCorrect) {
//				System.out.println("ip不合理");
//				response.sendRedirect("/page/login");
//				return false;
//			}
//		}	
		System.out.println("ip拦截器通过,已将ip存入session");
		request.getSession().setAttribute("ip", ip);
		
		System.out.println("**************获取IP拦截器调用结束****************\n");
		return true;
	}

	public static boolean matches(String text) {
		
		if(text.equals("0:0:0:0:0:0:0:1")) return true;
		
		if (text != null && !text.isEmpty()) {
			// 定义正则表达式  
            String regex = "^(1\\d{2}|2[0-4 ]\\d|25[0-5]|[1-9]\\d|[1-9])\\." 
                    + "(1\\d{2}|2[0-4]\\ d|25[0-5]|[1-9]\\d|\\d)\\."  
                    + "(1\\d{2}|2[0-4]\\ d|25[0-5]|[1-9]\\d|\\d)\\."  
                    + "(1\\d{2}|2[0-4]\\ d|25[0-5]|[1-9]\\d|\\d)$";  
            // 判断ip地址是否与正则表达式匹配  
            if (text.matches(regex)) {  
            // 返回判断信息  
                return true;  
            } else {  
            // 返回判断信息  
              return false;  
            }
		}
		// 返回判断信息
		return false;
	}

	
}
