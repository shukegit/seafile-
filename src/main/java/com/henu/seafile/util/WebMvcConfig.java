package com.henu.seafile.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 注册拦截器
 * @author Lenovo
 *
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	private static List<String> urlList = new ArrayList<>();
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		urlList.add("/page/login");
		urlList.add("/page/relogin");
		urlList.add("/page/websocket");
		urlList.add("/websocket/wsda");
		urlList.add("/favicon.ico");
		urlList.add("/js/login.js");
		urlList.add("/user/login");
		urlList.add("/page/login2");
		urlList.add("/js/login2.js");
		urlList.add("/img/background.png");
		urlList.add("/js/jquery-3.2.1.js");
		// addPathPatterns("/**") 表示拦截所有的请求，
        // excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问
		registry.addInterceptor(new MyInterceptorUtil()).addPathPatterns("/**").excludePathPatterns(urlList);
		registry.addInterceptor(new IPIntercepterUtil()).addPathPatterns("/user/login");
	}

}
