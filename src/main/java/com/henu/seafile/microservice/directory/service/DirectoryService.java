package com.henu.seafile.microservice.directory.service;

import java.util.Map;

import com.henu.seafile.common.ServiceResponse;

public interface DirectoryService {
	
	ServiceResponse<Map<String, Object>> createNewDirectory(String token, String repositoryId, String dir);
	
	ServiceResponse<Map<String, Object>> deleteDirectory(String token, String repositoryId, String dir);
	
	ServiceResponse<Map<String, Object>> renameDirectory(String token, String repositoryId, String dir, String newName);
	
	ServiceResponse<Map<String, Object>> shareDirectory(String token, String repositoryId, String dir, String shareName, String perm);;
	
//	ServiceResponse<Map<String, Object>> uploadDirectory();
	
	ServiceResponse<Map<String, Object>> downloadDirectory(String token, String repositoryId, String dir, String parentDir);
	
//	ServiceResponse<Map<String, Object>> searchDirectory();
	
	ServiceResponse<Map<String, Object>> listItemsInDirectory(String token, String repositoryId, String dir);
	
	ServiceResponse<Map<String, Object>> getDirectoryDetail(String token, String repositoryId, String dir);

}
