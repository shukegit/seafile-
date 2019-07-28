package com.henu.seafile.microservice.file.controller.back;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.henu.seafile.common.ServiceResponse;
import com.henu.seafile.microservice.file.service.FileService;
import com.henu.seafile.util.HttpServletRequestUtil;
import com.henu.seafile.util.FileUploadUtil;

@RestController
@RequestMapping(value = "/file")
public class FileController {

	@Value("${seafile.api.FileHead}")
	private String FileHead;
	@Value("${seafile.api.GetUploadLinkUrl}")
	String GetUploadLinkUrl;
	@Value("${seafile.api.CopyFileUrl}")
	private String CopyFileUrl;
	@Value("${seafile.api.MoveFileUrl}")
	private String MoveFileUrl;
	@Value("${seafile.api.DownloadFileUrl}")
	private String DownloadFileUrl;
	@Value("${seafile.api.DeleteFileUrl}")
	private String DeleteFileUrl;

	@Autowired
	private FileService fileService;


	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	private ServiceResponse<Map<String, Object>> uploadFile(HttpServletRequest request) {

		String Token = (String)request.getSession().getAttribute("token");
		if(Token == null || Token.equals("")) {
			return ServiceResponse.createByErrorMessage("登录过期，请重新登录");
		}
		String repositoryId = HttpServletRequestUtil.getString(request, "repositoryId");
		String filePath = HttpServletRequestUtil.getString(request, "filePath");
		System.out.println("filePath:" + filePath + "\n" + "repositoryId:" + repositoryId);
		if(repositoryId == null || filePath == null) {
			return ServiceResponse.createByErrorMessage("未获取到上传路径");
		}

		File file = null;
		try {
			file = FileUploadUtil.transformConMultipartFileToFile(request);	
		} catch(RuntimeException e) {
			return ServiceResponse.createByErrorMessage("未选择文件");
		}
		if (file == null) {
			return ServiceResponse.createByErrorMessage("解析文件失败");
		}
		String getUploadLinkUrl = FileHead + repositoryId + GetUploadLinkUrl;
		return fileService.simpleUpload(Token, file, getUploadLinkUrl, filePath);
	}

	@RequestMapping(value = "/downloadfile", method = RequestMethod.POST)
	private ServiceResponse<Map<String, Object>> DownloadOrDeleteFile(HttpServletRequest request) {

		String Token = (String)request.getSession().getAttribute("token");
		if(Token == null || Token.equals("")) {
			return ServiceResponse.createByErrorMessage("登录过期，请重新登录");
		}
		String repositoryId = HttpServletRequestUtil.getString(request, "repositoryId");
		String filePath = HttpServletRequestUtil.getString(request, "filePath");
		if(repositoryId == null || filePath == null) {
			return ServiceResponse.createByErrorMessage("未获取到上传路径");
		}
		String downloadUrl = FileHead + repositoryId + DownloadFileUrl + "?p=" + filePath + "&reuse=1";//默认一个小时内可以下载多次
//		String Token = "f0ce71a12a2fb4f3334d59c7ff5504470897eee9";
		return fileService.downloadFile(Token, downloadUrl);
	}

	@RequestMapping(value = "/copyfile", method = RequestMethod.POST)
	private ServiceResponse<Map<String, Object>> copyFile(HttpServletRequest request) {

		String Token = (String)request.getSession().getAttribute("token");
		if(Token == null || Token.equals("")) {
		return ServiceResponse.createByErrorMessage("登录过期，请重新登录");
		}
		
	
		// 从session中获得
//		String Token = "f0ce71a12a2fb4f3334d59c7ff5504470897eee9";
		Map<String, String> map = checkData(request);
		String flag = map.get("flag").toString();
		String filePath = map.get("filePath").toString();
		if(flag.equals("0")) {
			return ServiceResponse.createByErrorMessage("格式错误");
		}
		map.remove("flag");
		map.remove("filePath");		
		return fileService.copyOrMoveFile(map, Token, filePath);	
	}
	
	private Map<String, String> checkData(HttpServletRequest request) {

		Map<String, String> map = new HashMap<>();

		// 前台返回给后台的数据有：原存储库id及文件地址；目标存储库id及文件地址
		String oldRepositoryId = HttpServletRequestUtil.getString(request, "oldRepositoryId");
		String oldFileCopyPath = HttpServletRequestUtil.getString(request, "oldFileCopyPath");
		String newRepositoryId = HttpServletRequestUtil.getString(request, "newRepositoryId");
		String newFileCopyPath = HttpServletRequestUtil.getString(request, "newFileCopyPath");
		String operation = HttpServletRequestUtil.getString(request, "operation");
		// 上面这几个数据都是前台传递过来的

		if (oldRepositoryId == null || oldFileCopyPath == null || newRepositoryId == null || newFileCopyPath == null
				|| operation == null) {
			map.put("flag", "0");
		} else {
			map.put("flag", "1");
		}
		String filePath = null;
		if(operation.equals("copy")) {
			filePath = FileHead + oldRepositoryId + CopyFileUrl + "?p=" + oldFileCopyPath;
		} else if(operation.equals("move")) {
			filePath = FileHead + oldRepositoryId + MoveFileUrl + "?p=" + oldFileCopyPath;
		}
		map.put("filePath", filePath);
		map.put("operation", operation);
		map.put("dst_repo", newRepositoryId);
		map.put("dst_dir", newFileCopyPath);
		
		System.out.println(filePath);		
		return map;
	}
	
	
	@RequestMapping(value = "/movefile", method = RequestMethod.POST)
	private ServiceResponse<Map<String, Object>> moveFile(HttpServletRequest request) {
		// 从session中获得

		String Token = (String)request.getSession().getAttribute("token");
		if(Token == null || Token.equals("")) {
		return ServiceResponse.createByErrorMessage("登录过期，请重新登录");
		}
//		String Token = "f0ce71a12a2fb4f3334d59c7ff5504470897eee9";
		Map<String, String> map = checkData(request);
		String flag = map.get("flag").toString();
		String filePath = map.get("filePath").toString();
		if(flag.equals("0")) {
			return ServiceResponse.createByErrorMessage("格式错误");
		}
		map.remove("flag");
		map.remove("filePath");	
		System.out.println("mapSize:" + map.size());
		return fileService.copyOrMoveFile(map, Token, filePath);
		
	}
	

	@RequestMapping(value = "/deletefile", method = RequestMethod.POST)
	private ServiceResponse<Map<String, Object>> deleteFile(HttpServletRequest request) {
	
		String Token = (String)request.getSession().getAttribute("token");
		if(Token == null || Token.equals("")) {
		return ServiceResponse.createByErrorMessage("登录过期，请重新登录");
		}		
		String repositoryId = HttpServletRequestUtil.getString(request, "repositoryId");
		String filePath = HttpServletRequestUtil.getString(request, "filePath");
		if(repositoryId == null || filePath == null) {
			return ServiceResponse.createByErrorMessage("参数错误");
		}
				
		//判断的方法是同时接收两个参数，fileCopyPath为空表示下载文件 fileDownloadPath为空表示删除文件
		String deletePath = FileHead + repositoryId + DeleteFileUrl + "?p=" + filePath;//默认一个小时内可以下载多次

//		String Token = "f0ce71a12a2fb4f3334d59c7ff5504470897eee9";
		return fileService.deleteFile(Token, deletePath);
		
		
		
		
		
	}
	
	
}
