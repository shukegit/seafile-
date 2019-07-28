package com.henu.seafile.microservice.group.pojo;

import java.math.BigInteger;

public class Group {
	
	private BigInteger groupId;
	private String groupName;
	private String CreatorName;
	private BigInteger timeStamp;
	private String type;
	private int parentGroupId;
	public BigInteger getGroupId() {
		return groupId;
	}
	public void setGroupId(BigInteger groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getCreatorName() {
		return CreatorName;
	}
	public void setCreatorName(String creatorName) {
		CreatorName = creatorName;
	}
	public BigInteger getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(BigInteger timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getParentGroupId() {
		return parentGroupId;
	}
	public void setParentGroupId(int parentGroupId) {
		this.parentGroupId = parentGroupId;
	}
	
	

}
