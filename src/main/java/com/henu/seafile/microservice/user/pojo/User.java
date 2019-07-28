package com.henu.seafile.microservice.user.pojo;

import java.util.Date;

public class User {
	private int id;
	private String username;
	private String password;
	private String token;
	private boolean isUseful;
	private Date createTime;
	private Date lastEditTime;
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public boolean isUseful() {
		return isUseful;
	}
	public void setUseful(boolean isUseful) {
		this.isUseful = isUseful;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	
	
}
