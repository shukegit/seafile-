package com.henu.seafile.microservice.group.service;

import java.util.List;
import java.util.Map;

import com.henu.seafile.common.ServiceResponse;
import com.henu.seafile.microservice.group.pojo.Group;

public interface GroupService {
	
	ServiceResponse<Map<String, Object>> addAGroup(String token, String groupName);
	
	ServiceResponse<Map<String, Object>> deleteGroup(String token, String groupId);
	
	ServiceResponse<Map<String, Object>> renameGroup(String token, String groupId, String groupName);
	
	ServiceResponse<Map<String, Object>> quitGroup(String token, String groupId, String userName);
	
	ServiceResponse<Map<String, Object>> addGroupMember(String token, String groupId, String userName);
	
	ServiceResponse<Map<String, Object>> deleteGroupMember(String token, String groupId, String userName);
	
	//bool=true表示set,=false表示unset
	ServiceResponse<Map<String, Object>> setOrUnsetGroupAdmin(String token, String groupId, String userName, String bool);
	
	ServiceResponse<Map<String, Object>> listGroups(String token);
	
	ServiceResponse<Map<String, Object>> listAllGroupMembers(String token, String groupId);
	
	ServiceResponse<Map<String, Object>> getInfoOfAGroup(String token, String groupId);
	
	ServiceResponse<Map<String, Object>> getInfoOfAGroupMember(String token, String groupId, String groupNumber);
	
}
