package com.henu.seafile.microservice.user.service.serviceImpl;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henu.seafile.microservice.user.dao.UserDao;
import com.henu.seafile.microservice.user.pojo.User;
import com.henu.seafile.microservice.user.service.UrlService;

@Service
public class UrlServiceImpl implements UrlService{

	@Autowired
	UserDao userDao;
	
	public List<User> QueryUsers() {
		List<User> uList = userDao.selectAllUsers();
		return uList;
	}

}
