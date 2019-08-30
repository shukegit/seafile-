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
		
		/**
		 * 同一个会话只能登录一个账户，因为下一个用户登录时存放在session中的token会将前一个token替换掉，因此对于
		 * 一个浏览器登录好几个用户这种情况，session中只会存放最后一个用户的token，而pool中却存放了好几个人的信息。
		 * 解决方案是：每次都遍历map，得到values，在values中找是否存在和后一个登录相同的ip，如果有，删除当前value所在
		 * 的map
		 */
		for(Map.Entry<String, Object> map : tablePool.entrySet()) {
//			System.out.println(map.getValue() + "<---->" + poolInfo.getIp());
			if(map.getValue().equals(poolInfo.getIp())) tablePool.remove(map.getKey());
//			System.out.println("删除后的人数" + tablePool.size());
		}
		
		/**
		 * 比较不同ip的登录情况
		 */
		String ip = (String)tablePool.get(poolInfo.getToken());
		if (ip != null) {//该用户又登录了
			if(!poolInfo.getIp().equals(ip)) {
				//不同的设备的登录(用户名一样，ip不一样)
				return true;
			}
			return false;
		}
		return false;
	}
	public static boolean isExit(String token, String ip) {
		if(token == null || ip == null) {
			return false;
		}
		String poolIp = (String)tablePool.get(token);
		if (poolIp != null && poolIp.equals(ip)) {//找到了
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


