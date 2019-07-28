package com.henu.seafile.microservice.user.container;

import java.util.Hashtable;
import java.util.Map;


import com.henu.seafile.microservice.user.pojo.PoolInfo;

/**
 * 用hashTable存储token和电脑ip
 * 每次登录的之后通过token拿到ip
 * 判断ip和以前的是否相等
 * 相等通过，不相等将前面那个删除出去
 * 每次调用接口的时候都要在这里找token，找到说明允许操作，找不到不让操作
 * @author Lenovo
 *
 */
public class UserPool {
	private static Map<String, Object> tablePool = new Hashtable<>();;


	public static void push(PoolInfo poolInfo) {
		tablePool.put(poolInfo.getToken(), poolInfo.getIp());
	}

	public static void pop(PoolInfo poolInfo) {
		tablePool.remove(poolInfo.getToken());
	}

	public static boolean isDrop(PoolInfo poolInfo) {
		String ip = (String)tablePool.get(poolInfo.getToken());
		if (ip != null) {//找到了key
			if(poolInfo.getIp() != ip) {
				//同一台电脑不同的设备
				return true;
			}
			return false;
		}
		return false;
	}
	public static boolean isExit(String token) {
		String ip = (String)tablePool.get(token);
		if (ip != null) {//找到了
			return true;
		}
		return false;
	}
	
	public static int getLength() {
		return tablePool.size();
	}
	
	public static Map<String, Object> getUserPool() {
		return tablePool;
	}

}


