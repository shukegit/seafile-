package com.henu.seafile.microservice.directory.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.henu.seafile.common.ServiceResponse;
import com.henu.seafile.microservice.directory.service.DirectoryService;
import com.henu.seafile.util.HttpServletRequestUtil;

@RestController
@RequestMapping("/directory")
public class DirectoryController {

	@Autowired
	private DirectoryService directoryService;
	
	@RequestMapping(value="/createnewdirectory", method=RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> createNewDirectory(HttpServletRequest request) {
		
		String token = (String) request.getSession().getAttribute("token");
		String repositoryId = HttpServletRequestUtil.getString(request, "repositoryId");
		String dir = HttpServletRequestUtil.getString(request, "dir");

		repositoryId = "c85af60c-7cca-4826-a965-c5f5e9cf8a2a";
		dir = "/文件夹3/子文件夹2";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
//		if (repositoryId == null) {
//			return ServiceResponse.createByErrorMessage("未获取到组名称");
//		}
//		if (dir == null) {
//			return ServiceResponse.createByErrorMessage("未获取到用户名");
//		}
		return directoryService.createNewDirectory(token, repositoryId, dir);
	}
	
	@RequestMapping(value="/deletedirectory", method=RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> deleteDirectory(HttpServletRequest request) {
		
		String token = (String) request.getSession().getAttribute("token");
		String repositoryId = HttpServletRequestUtil.getString(request, "repositoryId");
		String dir = HttpServletRequestUtil.getString(request, "dir");

		repositoryId = "c85af60c-7cca-4826-a965-c5f5e9cf8a2a";
		dir = "/文件夹3/文件夹";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
//		if (repositoryId == null) {
//			return ServiceResponse.createByErrorMessage("未获取到组名称");
//		}
//		if (dir == null) {
//			return ServiceResponse.createByErrorMessage("未获取到用户名");
//		}
		return directoryService.deleteDirectory(token, repositoryId, dir);
	}
	
	@RequestMapping(value="/renamedirectory", method=RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> renameDirectory(HttpServletRequest request) {
		
		String token = (String) request.getSession().getAttribute("token");
		String repositoryId = HttpServletRequestUtil.getString(request, "repositoryId");
		String dir = HttpServletRequestUtil.getString(request, "dir");
		String newName = HttpServletRequestUtil.getString(request, "newName");
		repositoryId = "c85af60c-7cca-4826-a965-c5f5e9cf8a2a";
		dir = "/文件夹3/子文件夹1";
		newName = "修改后的子文件夹1";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
//		if (repositoryId == null) {
//			return ServiceResponse.createByErrorMessage("未获取到组名称");
//		}
//		if (dir == null) {
//			return ServiceResponse.createByErrorMessage("未获取到用户名");
//		}
		return directoryService.renameDirectory(token, repositoryId, dir, newName);
	}
	
	@RequestMapping(value="/downloaddirectory", method=RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> downloadDirectory(HttpServletRequest request) {
		
		String token = (String) request.getSession().getAttribute("token");
		String repositoryId = HttpServletRequestUtil.getString(request, "repositoryId");
		String dir = HttpServletRequestUtil.getString(request, "dir");
		String parentDir = HttpServletRequestUtil.getString(request, "parentDir");
		repositoryId = "c85af60c-7cca-4826-a965-c5f5e9cf8a2a";
		dir = "/bbb";
		parentDir = "/wsad";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
//		if (repositoryId == null) {
//			return ServiceResponse.createByErrorMessage("未获取到组名称");
//		}
//		if (dir == null) {
//			return ServiceResponse.createByErrorMessage("未获取到文件夹名称");
//		}
//		if (parentDir == null) {
//		return ServiceResponse.createByErrorMessage("未获取到父文件夹名称");
//	}
		return directoryService.downloadDirectory(token, repositoryId, dir, parentDir);
	}
	
	@RequestMapping(value="/listitemsindirectory", method=RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> listItemsInDirectory(HttpServletRequest request) {
		
		String token = (String) request.getSession().getAttribute("token");
		String repositoryId = HttpServletRequestUtil.getString(request, "repositoryId");
		String dir = HttpServletRequestUtil.getString(request, "dir");
		repositoryId = "c85af60c-7cca-4826-a965-c5f5e9cf8a2a";
		dir = "/wsad";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
//		if (repositoryId == null) {
//			return ServiceResponse.createByErrorMessage("未获取到组名称");
//		}
//		if (dir == null) {
//			return ServiceResponse.createByErrorMessage("未获取到文件夹名称");
//		}
		return directoryService.listItemsInDirectory(token, repositoryId, dir);
	}
	
	@RequestMapping(value="/getdirectorydetail", method=RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> getDirectoryDetail(HttpServletRequest request) {
		
		String token = (String) request.getSession().getAttribute("token");
		String repositoryId = HttpServletRequestUtil.getString(request, "repositoryId");
		String dir = HttpServletRequestUtil.getString(request, "dir");
		repositoryId = "c85af60c-7cca-4826-a965-c5f5e9cf8a2a";
		dir = "/wsad";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
//		if (repositoryId == null) {
//			return ServiceResponse.createByErrorMessage("未获取到组名称");
//		}
//		if (dir == null) {
//			return ServiceResponse.createByErrorMessage("未获取到文件夹名称");
//		}
		return directoryService.getDirectoryDetail(token, repositoryId, dir);
	}
	
	@RequestMapping(value="/sharedirectory", method=RequestMethod.GET)
	private ServiceResponse<Map<String, Object>> shareDirectory(HttpServletRequest request) {
		
		String token = (String) request.getSession().getAttribute("token");
		String repositoryId = HttpServletRequestUtil.getString(request, "repositoryId");
		String dir = HttpServletRequestUtil.getString(request, "dir");
		String shareName = HttpServletRequestUtil.getString(request, "sahreName");
		repositoryId = "c85af60c-7cca-4826-a965-c5f5e9cf8a2a";
		dir = "/wsad";
		shareName = "demo02@seafile.henu.com";
		if (token == null) {
			return ServiceResponse.createByErrorMessage("用户未登录");
		}
//		if (repositoryId == null) {
//			return ServiceResponse.createByErrorMessage("未获取到组名称");
//		}
//		if (dir == null) {
//			return ServiceResponse.createByErrorMessage("未获取到文件夹名称");
//		}
//		if (shareName == null) {
//		return ServiceResponse.createByErrorMessage("未获取到文件夹名称");
//	}
		String perm = "r";
		return directoryService.shareDirectory(token, repositoryId, dir, shareName, perm);
		
	}
}
