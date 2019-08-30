package com.henu.seafile.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


public class FileUploadUtil {

	/**
	 * 将前端传递过来的CommonsMultipartFile转换为后台可以提交的File类型
	 * 
	 * @param request
	 * @return 转换成功返回file，失败返回null
	 */
	public static File transformConMultipartFileToFile(HttpServletRequest request) {
		
		MultipartFile multipartFile = null;
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if(resolver.isMultipart(request)) {
//			StandardMultipartHttpServletRequest multipartHttpServletRequest = (StandardMultipartHttpServletRequest)request;
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
			multipartFile = (MultipartFile)multipartHttpServletRequest.getFile("seafile");
		} else {
			return null;//处理失败返回null
		}
		//将CommonsMultipartFile转化为File类型
		String fileName = multipartFile.getOriginalFilename();
		
		File file = new File(fileName);
		System.out.println(file.getAbsolutePath());
		InputStream seaFileInputStream = null;
		try {
			seaFileInputStream = multipartFile.getInputStream();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
		try {
			FileUtils.copyInputStreamToFile(seaFileInputStream, file);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
		
		return file;
	}
	
	

}
