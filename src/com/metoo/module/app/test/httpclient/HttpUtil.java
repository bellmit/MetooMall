package com.metoo.module.app.test.httpclient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import org.springframework.stereotype.Component;

/**
 * <p>
 * 	@Title: HttpUtil.java
 * </p>
 * <p>
 * 	@Description: 获取调用远程api参数
 * </p>
 * @author 46075
 *
 */
@Component
public class HttpUtil {

	 private static String SMS_TOKEN = "c691aaec67a31b31fb78ab92d50b69eb";
	
	 public String getMD5(String sid, long timestamp) {
	        StringBuilder result = new StringBuilder();
	        String source = sid + SMS_TOKEN + timestamp;
	        // 获取某个类的实例
	        try {
	            MessageDigest digest = MessageDigest.getInstance("MD5");
	            // 要进行加密的东西
	            byte[] bytes = digest.digest(source.getBytes());
	            for (byte b : bytes) {
	                String hex = Integer.toHexString(b & 0xff);
	                if (hex.length() == 1) {
	                    result.append("0" + hex);
	                } else {
	                    result.append(hex);
	                }
	            }
	        } catch (NoSuchAlgorithmException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return result.toString();
	    }
	 
	// 获取时间戳
    public long getTimestamp() {
    	 Calendar calendar = Calendar.getInstance();
         long millis = Calendar.getInstance().getTime().getTime();
         return millis;
    }
}
