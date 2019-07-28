package com.henu.seafile.microservice.file.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henu.seafile.microservice.user.pojo.User;
import com.henu.seafile.microservice.user.service.UrlService;

@RestController
@RequestMapping("/login")
public class GetUserInfoController {
	@Autowired
	UrlService UrlService;
	
	@RequestMapping("/getUserInfo")
	public String getUserInfo(HttpServletRequest request) {
		List<User> userList = UrlService.QueryUsers();
		
		for (User user : userList) {
			System.out.println(user.getId() + " " + user.getUsername() + " " + user.getPassword());
		}
		return "yes";
	}

}
