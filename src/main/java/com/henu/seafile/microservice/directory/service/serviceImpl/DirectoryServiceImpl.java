package com.henu.seafile.microservice.directory.service.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.henu.seafile.common.GetMethod;
import com.henu.seafile.common.PostMethod;
import com.henu.seafile.common.ResponseCode;
import com.henu.seafile.common.ServiceResponse;
import com.henu.seafile.microservice.directory.service.DirectoryService;

@Service
public class DirectoryServiceImpl implements DirectoryService{
	
	@Value("${seafile.api.Directory}")
	private String directory;
	@Value("${seafile.api.CreateNewDirectory}")
	private String createNewDirectory;
	@Value("${seafile.api.DownloadDirectory}")
	private String downloadDirectory;
	@Value("${seafile.api.DownloadDirectory.GetTaskToken}")
	private String getTaskToken;
	@Value("${seafile.api.DownloadDirectory.QueryTaskProgress}")
	private String queryTaskProgress;
	@Value("${seafile.api.DownloadPath}")
	private String downloadPath;
	@Value("${seafile.api.GetDirectoryDetail}")
	private String getDirectoryDetail;
	@Value("${seafile.api.ShareDirectory}")
	private String shareDirectory;
	@Override
	public ServiceResponse<Map<String, Object>> createNewDirectory(String token, String repositoryId, String dir) {
		
		PostMethod postMethod = new PostMethod();
		Map<String, String> map = new HashMap<String, String>();
		map.put("operation", "mkdir");
		String url = directory + repositoryId + createNewDirectory + "?p=" + dir;
		System.out.println(url);
		return postMethod.directoryRun(map, url, token);
	}

	@Override
	public ServiceResponse<Map<String, Object>> deleteDirectory(String token, String repositoryId, String dir) {

		GetMethod getMethod = new GetMethod();

		String url = directory + repositoryId + createNewDirectory + "?p=" + dir;
		System.out.println(url);
		return getMethod.deleteRun(null, url, token);
		
	}

	@Override
	public ServiceResponse<Map<String, Object>> renameDirectory(String token, String repositoryId, String dir, String newName) {
		
		PostMethod postMethod = new PostMethod();
		Map<String, String> map = new HashMap<String, String>();
		map.put("operation", "rename");
		map.put("newname", newName);
		String url = directory + repositoryId + createNewDirectory + "?p=" + dir;
		System.out.println(url);
		return postMethod.directoryRun(map, url, token);
	}

	@Override
	public ServiceResponse<Map<String, Object>> shareDirectory(String token, String repositoryId, String dir, String shareName, String perm) {
		PostMethod postMethod = new PostMethod();
		Map<String, String> map = new HashMap<String, String>();
		map.put("emails", shareName);
		map.put("s_type", "d");
		map.put("path", dir);
		map.put("perm", "r");
		String url = directory + repositoryId + shareDirectory;
		System.out.println(url);
		return postMethod.directoryRun(map, url, token);
	}

	@Override
	public ServiceResponse<Map<String, Object>> downloadDirectory(String token, String repositoryId, String dir, String parentDir) {
		
		GetMethod getMethod = new GetMethod();
		String url = downloadDirectory + repositoryId + getTaskToken + "?parent_dir=" + parentDir + "&" + "dirents=" + dir;
		System.out.println(url);
		ServiceResponse<Map<String, Object>> result = getMethod.run(url, token);
		if(result.getStatus() == ResponseCode.ERROR.getCode()) {
			return result;
		}
		String[] arr = result.getData().get("data").toString().replace('{', ' ').replace('}', ' ').trim().split(":");
		String zipToken = arr[1].replace('"', ' ').trim();
		String url2 = queryTaskProgress + "?token=" + zipToken;
		System.out.println("\n" + url2);
		
		ServiceResponse<Map<String, Object>> result2;
		String[] strings;
		String[] strings1;
		String[] strings2;
		
		//判断解压状态，解压成功后strings1[1].equals(strings2[1]=true,这时就可以通知前台进行文件目录下载了
		while(true) {
			result2 = getMethod.run(url2, token);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
				return ServiceResponse.createByErrorMessage("内部错误");
			}
			if(result2.getStatus() == ResponseCode.ERROR.getCode()) {
				return result2;
			}
			strings = ((String)result2.getData().get("data")).replace('{', ' ').replace('}', ' ').trim().split(",");
			strings1 = strings[0].split(":");
			strings2 = strings[1].split(":");
			if(strings1[1].equals(strings2[1])) break;
		}
		String downloadUrl = downloadPath + zipToken;
		System.out.println("downloadUrl:" + downloadUrl);
		Map<String, Object> map = new HashMap<>();
		map.put("data", downloadUrl);
		return ServiceResponse.createBySuccessData(map);
	}

	@Override
	public ServiceResponse<Map<String, Object>> listItemsInDirectory(String token, String repositoryId, String dir) {
		
		GetMethod getMethod = new GetMethod();
		String url = directory + repositoryId + createNewDirectory + "?p=" + dir;
		System.out.println(url);
		return getMethod.run(url, token);
	}

	@Override
	public ServiceResponse<Map<String, Object>> getDirectoryDetail(String token, String repositoryId, String dir) {
		GetMethod getMethod = new GetMethod();
		String url = downloadDirectory + repositoryId + getDirectoryDetail + "?path=" + dir;
		System.out.println(url);
		return getMethod.run(url, token);
	}

}
