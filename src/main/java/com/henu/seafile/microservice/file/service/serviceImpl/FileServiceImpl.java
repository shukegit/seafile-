package com.henu.seafile.microservice.file.service.serviceImpl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.henu.seafile.common.GetMethod;
import com.henu.seafile.common.PostMethod;
import com.henu.seafile.common.ResponseCode;
import com.henu.seafile.common.ServiceResponse;
import com.henu.seafile.microservice.file.service.FileService;



@Service
public class FileServiceImpl implements FileService{


	@Value("${seafile.api.DownloadFileUrl}")
	String DownloadFileUrl;
	
	
	@Override
	public ServiceResponse<Map<String, Object>> simpleUpload(String Token, File file, String getUploadLinkUrl, String filePath) {

		System.out.println(4);
		GetMethod getMethod = new GetMethod();
		PostMethod postMethod = new PostMethod();
		ServiceResponse<Map<String, Object>> response = null;
		//用Token通过get请求获取存储文件路径url
		System.out.println(5); 
		response = getMethod.runUploadFile(Token, getUploadLinkUrl);//获取上传链接
		if(response.getStatus() == ResponseCode.ERROR.getCode()) {
			System.out.println(6);
			return ServiceResponse.createByErrorMessage("请求上传链接失败");
		}
		String pathUrl = response.getData().get("data").toString().replace('"',' ').trim();//处理传递过来的链接格式
		System.out.println(pathUrl);
		//用url和file存储通过post请求上传文件
		System.out.println(7);
		response = postMethod.fileUploadRun(file, pathUrl, filePath);//上传文件
		if(response.getStatus() == ResponseCode.SUCCESS.getCode()) {
			//上传成功后将返回的文件标识保存到数据库中
			//数据表包括返回序列号，提交时间，
			return response;
		} else {
			System.out.println("上传文件失败！");
		}
		System.out.println(14);
		return ServiceResponse.createByErrorMessage("上传文件失败");
	}


	@Override
	public ServiceResponse<Map<String, Object>> downloadFile(String Token, String downloadPath) {
		System.out.println("234");
		ServiceResponse<Map<String, Object>> response = new GetMethod().downloadFile(downloadPath,Token);
		if(response.getStatus() == ResponseCode.ERROR.getCode()) {//如果失败直接返回状态码和状态信息
			return response;
		}
		//如果成功处理掉返回链接的两个双引号
		Map<String, Object> map = new HashMap<>(); 
		String backStr = response.getData().get("data").toString();
		backStr = backStr.replace('"',' ').trim();
		map.put("data", backStr);
		return ServiceResponse.createBySuccessData(map);	
	}


	@Override
	public ServiceResponse<Map<String, Object>> copyOrMoveFile(Map<String, String> map, String Token, String fileCopyPath) {
		
		PostMethod postMethod = new PostMethod();
		ServiceResponse<Map<String, Object>> response = postMethod.fileCopyRun(map, fileCopyPath, Token);
		if(response.getStatus() == ResponseCode.ERROR.getCode()) {
			return ServiceResponse.createByErrorMessage(response.getMessage());
		}
		return response;
	}


	@Override
	public ServiceResponse<Map<String, Object>> deleteFile(String Token, String deletePath) {
		
		return new GetMethod().deleteFile(deletePath,Token);
		
	}

}
