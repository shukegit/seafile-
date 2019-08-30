package com.henu.seafile.common;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class ServiceResponse<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -281135549093535703L;
	private int status;
	private String message;
	private T data;
	
	private ServiceResponse(int status) {
		this.status = status;
	}
	private ServiceResponse(String message) {
		this.message = message;
	}
	private ServiceResponse(int status, String message) {
		this.status = status;
		this.message = message;
	}
	private ServiceResponse(int status, T data) {
		this.status = status;
		this.data = data;
	}
	private ServiceResponse(int status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
	@JsonIgnore
	public boolean isSuccess() {
		return this.status == ResponseCode.SUCCESS.getCode();
	}
	
	/**
	 * 只传成功状态标志
	 * @return
	 */
	public static <T> ServiceResponse<T> createBySuccess() {
		return new ServiceResponse<>(ResponseCode.SUCCESS.getCode());
	}
	/**
	 * 传成功状态标志以及自定义信息
	 * @param message
	 * @return
	 */
	public static <T> ServiceResponse<T> createBySuccessMessage(String message) {
		return new ServiceResponse<>(ResponseCode.SUCCESS.getCode(), message);
	}
	/**
	 * 传成功状态标志以及数据
	 * @param data
	 * @return
	 */
	public static <T> ServiceResponse<T> createBySuccessData(T data) {
		return new ServiceResponse<>(ResponseCode.SUCCESS.getCode(), data);
	}
	/**
	 * 传成功状态标志、自定义信息、数据
	 * @param message
	 * @param data
	 * @return
	 */
	public static <T> ServiceResponse<T> createBySuccessMessageAndData(String message, T data) {
		return new ServiceResponse<>(ResponseCode.SUCCESS.getCode(), message, data);
	}	
	public static <T> ServiceResponse<T> createByError() {
		return new ServiceResponse<>(ResponseCode.ERROR.getCode());
	}
	public static <T> ServiceResponse<T> createByErrorMessage(String message) {
		return new ServiceResponse<>(ResponseCode.ERROR.getCode(), message);
	}
	public static <T> ServiceResponse<T> createByErrorCodeAndMessage(Integer code, String message) {
		return new ServiceResponse<>(code, message);
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getStatus() {
		return status;
	}
	public String getMessage() {
		return message;
	}
	public T getData() {
		return data;
	}
	
	
}
