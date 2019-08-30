package com.henu.seafile.microservice.group.controller.back;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.henu.seafile.common.ServiceResponse;
import com.henu.seafile.microservice.group.service.GroupService;
import com.henu.seafile.util.HttpServletRequestUtil;

@RestController
@RequestMapping("/group")
public class GroupController {

	@Autowired
	private GroupService groupService;

	@RequestMapping(value = "/addgroup", method = RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> addGroup(HttpServletRequest request) {

		String token = (String) request.getSession().getAttribute("token");
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
		String groupName = HttpServletRequestUtil.getString(request, "groupName");
		groupName = "一个组";
//		if(groupName == null) {
//			return ServiceResponse.createByErrorMessage("未获取到组名称");
//		}
		return groupService.addAGroup(token, groupName);
	}

	/**
	 * 组创建者可以删除组（管理员未知）其他人调用会失败
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deletegroup", method = RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> deleteGroup(HttpServletRequest request) {

		String token = (String) request.getSession().getAttribute("token");
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
		String groupId = HttpServletRequestUtil.getString(request, "groupId");
//		groupId = "7";
		if (groupId == null) {
			return ServiceResponse.createByErrorMessage("未获取到组名称");
		}
		return groupService.deleteGroup(token, groupId);
	}

	@RequestMapping(value = "/renamegroup", method = RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> renameGroup(HttpServletRequest request) {

		String token = (String) request.getSession().getAttribute("token");
		String groupId = HttpServletRequestUtil.getString(request, "groupId");
		String groupName = HttpServletRequestUtil.getString(request, "groupName");
//		token = "dad7ec60adc9738db01ef3fb19a03d9fd8e75ec2";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
//		groupId = "10";
		if (groupId == null) {
			return ServiceResponse.createByErrorMessage("未获取到组名称");
		}
//		groupName = "修改后的组名称";
		if (groupName == null) {
			return ServiceResponse.createByErrorMessage("未获取到修改后的组名称");
		}
		return groupService.renameGroup(token, groupId, groupName);
	}

	/**
	 * 组成员退出组，组创建者调用会返回失败状态和信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/quitgroup", method = RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> quitGroup(HttpServletRequest request) {

		String token = (String) request.getSession().getAttribute("token");
		String groupId = HttpServletRequestUtil.getString(request, "groupId");
		String userName = HttpServletRequestUtil.getString(request, "userName");
//		groupId = "10";
//		userName = "demo02@seafile.henu.com";
//		userName = "sk@123.com";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
		if (groupId == null) {
			return ServiceResponse.createByErrorMessage("未获取到组名称");
		}
		if (userName == null) {
			return ServiceResponse.createByErrorMessage("未获取到用户名");
		}
		return groupService.quitGroup(token, groupId, userName);
	}

	/**
	 * 组的创建者和组管理员有权限，其他人调用会返回错误状态
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addgroupmember", method = RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> addGroupMember(HttpServletRequest request) {

		String token = (String) request.getSession().getAttribute("token");
		String groupId = HttpServletRequestUtil.getString(request, "groupId");
		String userName = HttpServletRequestUtil.getString(request, "userName");
		groupId = "10";
		userName = "demo02@seafile.henu.com";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
		if (groupId == null) {
			return ServiceResponse.createByErrorMessage("未获取到组名称");
		}
		if (userName == null) {
			return ServiceResponse.createByErrorMessage("未获取到用户名");
		}
		return groupService.addGroupMember(token, groupId, userName);
	}

	/**
	 * 组创建者调用
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deletegroupmember", method = RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> deleteGroupMember(HttpServletRequest request) {

		String token = (String) request.getSession().getAttribute("token");
		String groupId = HttpServletRequestUtil.getString(request, "groupId");
		String userName = HttpServletRequestUtil.getString(request, "userName");
		groupId = "10";
		userName = "demo02@seafile.henu.com";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
		if (groupId == null) {
			return ServiceResponse.createByErrorMessage("未获取到组名称");
		}
		if (userName == null) {
			return ServiceResponse.createByErrorMessage("未获取到用户名");
		}
		return groupService.deleteGroupMember(token, groupId, userName);
	}

	@RequestMapping(value = "/setorunsetgroupadmin", method = RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> setOrUnsetGroupAdmin(HttpServletRequest request) {

		String token = (String) request.getSession().getAttribute("token");
		String groupId = HttpServletRequestUtil.getString(request, "groupId");
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String isAdmin = HttpServletRequestUtil.getString(request, "isAdmin");
		groupId = "10";
		userName = "demo02@seafile.henu.com";
		isAdmin = "false";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
		if (groupId == null) {
			return ServiceResponse.createByErrorMessage("未获取到组名称");
		}
		if (userName == null) {
			return ServiceResponse.createByErrorMessage("未获取到用户名");
		}
		if (isAdmin == null) {
			return ServiceResponse.createByErrorMessage("未获取到设置属性");
		}
		return groupService.setOrUnsetGroupAdmin(token, groupId, userName, isAdmin);
	}

	@RequestMapping(value = "/getinfoofagroup", method = RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> getInfoOfAGroup(HttpServletRequest request) {

		String token = (String) request.getSession().getAttribute("token");
		String groupId = HttpServletRequestUtil.getString(request, "groupId");
		groupId = "10";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
		if (groupId == null) {
			return ServiceResponse.createByErrorMessage("未获取到组名称");
		}
		return groupService.getInfoOfAGroup(token, groupId);
	}

	@RequestMapping(value = "/getinfoofagroupmember", method = RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> getInfoOfAGroupMember(HttpServletRequest request) {

		String token = (String) request.getSession().getAttribute("token");
		String groupId = HttpServletRequestUtil.getString(request, "groupId");
		String userName = HttpServletRequestUtil.getString(request, "userName");
		groupId = "10";
		userName = "demo02@seafile.henu.com";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
		if (groupId == null) {
			return ServiceResponse.createByErrorMessage("未获取到组名称");
		}
		if (userName == null) {
			return ServiceResponse.createByErrorMessage("未获取到用户名");
		}
		return groupService.getInfoOfAGroupMember(token, groupId, userName);
	}
	
	@RequestMapping(value = "/listgroups", method = RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> listGroups(HttpServletRequest request) {

		String token = (String) request.getSession().getAttribute("token");

		return groupService.listGroups(token);
	}

	@RequestMapping(value = "/listallgroupmembers", method = RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> listAllGroupMembers(HttpServletRequest request) {

		String token = (String) request.getSession().getAttribute("token");
		String groupId = HttpServletRequestUtil.getString(request, "groupId");
		groupId = "10";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
		if (groupId == null) {
			return ServiceResponse.createByErrorMessage("未获取到组名称");
		}
		return groupService.listAllGroupMembers(token, groupId);
	}

}
