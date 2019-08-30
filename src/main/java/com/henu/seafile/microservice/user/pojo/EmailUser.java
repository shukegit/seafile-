package com.henu.seafile.microservice.user.pojo;

public class EmailUser {
	
	private long id;
	private String email;
	private String passwd;
	/**
	 * isStaff = 0，isActive = 1表示用户；isStaff = 1，isActive = 1表示管理员
	 */
	private boolean isStaff;
	/**
	 * isStaff = 0，isActive = 1表示用户；isStaff = 1，isActive = 1表示管理员
	 */
	private boolean isActive;
	private long ctime;//创建时间
	private String referenceId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public boolean isStaff() {
		return isStaff;
	}
	public void setStaff(boolean isStaff) {
		this.isStaff = isStaff;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	
	

}
