package com.henu.seafile.microservice.group.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.henu.seafile.microservice.group.pojo.Group;

@Mapper
public interface GroupDao {
	
	List<Group> selectGroups();
	
	Group selectGroupByGroupId(int groupId);
	
	Group selectGroupByGroupName(String groupName);
	
	List<Group> selectGroupByCreatorName(String creatorName);

}
