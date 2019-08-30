package com.henu.seafile.microservice.file.service;

import java.io.File;
import java.util.Map;

import com.henu.seafile.common.ServiceResponse;

public interface FileService {
	
	ServiceResponse<Map<String, Object>> simpleUpload(String Token, File file, String getUploadLinkUrl, String filePath);
	
	ServiceResponse<Map<String, Object>> downloadFile(String Token, String downloadPath);
	
	ServiceResponse<Map<String, Object>> deleteFile(String Token, String deletePath);
	
	ServiceResponse<Map<String, Object>> copyOrMoveFile(Map<String, String> map, String Token, String fileCopyPath);
	
}
