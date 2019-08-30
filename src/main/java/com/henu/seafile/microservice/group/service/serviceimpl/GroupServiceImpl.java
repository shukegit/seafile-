package com.henu.seafile.microservice.group.service.serviceimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.henu.seafile.common.GetMethod;
import com.henu.seafile.common.PostMethod;
import com.henu.seafile.common.PutMethod;
import com.henu.seafile.common.ResponseCode;
import com.henu.seafile.common.ServiceResponse;
import com.henu.seafile.microservice.group.dao.GroupDao;
import com.henu.seafile.microservice.group.pojo.Group;
import com.henu.seafile.microservice.group.service.GroupService;
import com.henu.seafile.microservice.user.dao.UserDao;
import com.henu.seafile.microservice.user.pojo.User;

@Service
public class GroupServiceImpl implements GroupService{
	
	@Autowired
	private GroupDao groupdao;
	@Autowired
	private UserDao userDao;

	@Value("${seafile.api.group}")
	String group;
	@Value("${seafile.api.AddAGroup}")
	String AddAGroup;
	@Value("${seafile.api.DeleteGroup}")
	String DeleteGroup;
	@Value("${seafile.api.RenameGroup}")
	String RenameGroup;
	@Value("${seafile.api.QuitGroup}")
	String QuitGroup;
	@Value("${seafile.api.AddGroupMember}")
	String AddGroupMember;
	@Value("${seafile.api.DeleteGroupMember}")
	String DeleteGroupMember;
	@Value("${seafile.api.SetGroupAdmin}")
	String SetGroupAdmin;
	@Value("${seafile.api.UnsetGroupAdmin}")
	String UnsetGroupAdmin;
	@Value("${seafile.api.ListAllGroupMembers}")
	String ListAllGroupMembers;
	@Override
	public ServiceResponse<Map<String, Object>> addAGroup(String token, String groupName) {
		
		//数据转换
		PostMethod postMethod = new PostMethod();
		Map<String, String> map = new HashMap<>();
		map.put("name", groupName);
		String url = AddAGroup;
		//发送请求
		ServiceResponse<Map<String, Object>> response = postMethod.groupRun(map, url, token);
		
		return response;
	}


	@Override
	public ServiceResponse<Map<String, Object>> deleteGroup(String token, String groupId) {
		
		GetMethod getMethod = new GetMethod();
		
		String url = DeleteGroup + groupId + "/";
		System.out.println(url);
		ServiceResponse<Map<String, Object>> response = getMethod.deleteRun(null, url, token);
		return response;
	}


	@Override
	public ServiceResponse<Map<String, Object>> renameGroup(String token, String groupId, String groupName) {
		
		
		PutMethod putMethod = new PutMethod();
		
		Map<String, String> map = new HashMap<>();
		map.put("name", groupName);
		String url = RenameGroup + groupId + "/";
		return putMethod.groupRun(map, url, token);

	}


	@Override
	public ServiceResponse<Map<String, Object>> quitGroup(String token, String groupId, String userName) {
		
		GetMethod getMethod = new GetMethod();
		String url = group + groupId + QuitGroup + userName + "/";
		System.out.println(url);
		return getMethod.deleteRun(null, url, token);
	}


	@Override
	public ServiceResponse<Map<String, Object>> addGroupMember(String token, String groupId, String groupNumber) {
		
		PostMethod postMethod = new PostMethod();
		Map<String, String> map = new HashMap<>();
		map.put("email", groupNumber);
		String url = group + groupId + AddGroupMember;
		
		return postMethod.groupRun(map, url, token);
	}


	@Override
	public ServiceResponse<Map<String, Object>> deleteGroupMember(String token, String groupId, String groupNumber) {

		GetMethod getMethod = new GetMethod();
		Map<String, String> map = new HashMap<>();
		map.put("user_name", groupNumber);
		String url = DeleteGroupMember + groupId + "/members/";
		System.out.println(url);
		return getMethod.deleteRun(map, url, token);
	}


	/**
	 * 用的中文版的接口
	 */
	@Override
	public ServiceResponse<Map<String, Object>> setOrUnsetGroupAdmin(String token, String groupId, String groupNumber, String bool) {

		PutMethod putMethod = new PutMethod();
		Map<String, String> map = new HashMap<>();
		//bool=true表示set,=false表示unset
		map.put("is_admin", bool);
		String url = group + groupId + SetGroupAdmin + groupNumber + "/";
		System.out.println(url);
		return putMethod.groupRun(map, url, token);
	}


	@Override
	public ServiceResponse<Map<String, Object>> listGroups(String token) {
		
		GetMethod getMethod = new GetMethod();
		String url = group;
		return getMethod.run(url, token);
	}


	@Override
	public ServiceResponse<Map<String, Object>> listAllGroupMembers(String token, String groupId) {
		System.out.println(1);
		GetMethod getMethod = new GetMethod();
		String url = group + groupId + ListAllGroupMembers;
		System.out.println(url);
		return getMethod.run(url, token);
	}


	@Override
	public ServiceResponse<Map<String, Object>> getInfoOfAGroup(String token, String groupId) {
		
		GetMethod getMethod = new GetMethod();
		String url = group + groupId + "/";
		System.out.println(url);
		return getMethod.run(url, token);
	}


	@Override
	public ServiceResponse<Map<String, Object>> getInfoOfAGroupMember(String token, String groupId, String groupNumber) {
		
		GetMethod getMethod = new GetMethod();
		String url = group + groupId + ListAllGroupMembers + groupNumber + "/";
		System.out.println(url);
		return getMethod.run(url, token);
	}


	


	


}
