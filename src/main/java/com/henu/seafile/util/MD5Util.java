package com.henu.seafile.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

public class MD5Util {

	public static String getMD5Password(String password) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md5Bytes = md5.digest(password.getBytes());
			return Hex.encodeHexString(md5Bytes);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("加密失败：" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
