package com.metoo.module.app.test.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metoo.core.language.NameValue;

/**
 * 
 * <p>
 * 	@Title: HttpClientApiTest.java
 * </p>
 * 
 * <p>
 * 	@Description: 测试使用多种方式调用http接口 
 * 					1，通过JDK网络类Java.net.HttpURLConnection
 * 					2，通过common封装好的HttpClient
 * 					3，通过Apache封装好的CloseableHttpClient
 * 					4，通过SpringBoot-RestTemplate
 * 					5，测试地址（秒嘀短信）https://openapi.miaodiyun.com/distributor/sendSMS
 * </p>
 * @author 46075
 *
 */
@Controller
@RequestMapping("sms")
public class HttpClientApiTest {
	
	@Autowired
	private HttpUrlConnectionUtil util;
	@Autowired
	private HttpUtil httpUtil;
	@Autowired
	private HttpClientUtil httpClient;
	@Autowired
	private HttpRestTemplateUtil httpRestTemplate;
	@Autowired
	private HttpClientConnectionManager connectManage;

	 public static void main(String[] args) {
	        String s1 = "runoob";
	        String s2 = "runoob";
	        System.out.println("s1 == s2 is:" + s1 == s2);
	    }
	 
	/**
	 * 
	 * @param url
	 * @param requestMethod
	 * @return
	 * @description httpUrlConnection
	 */
	@RequestMapping(value = "httpUrlConnection.json", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String httpUrlConnection(String url, String requestMethod){
		String result = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("smsContent", "This is test sms");
		long timestamp = this.httpUtil.getTimestamp();
		params.put("to", "15838152042");
		params.put("timestamp", timestamp);
		String sig = this.httpUtil.getMD5("d5318078b04d4a7b94502a38d8ca34d8", timestamp);
		params.put("sig",sig);
		params.put("accountSid", "d5318078b04d4a7b94502a38d8ca34d8");
		result = this.util.httpUrlConnection(url, params, requestMethod, "");
		return  result;
    }
	
	@RequestMapping("/anyShare.json")
	@ResponseBody
	public Map<String, Object> anyShare(String token){
		Map<String, String> params = new HashMap<String, String>();
		String url = "https://116.63.192.186:443/api/efast/v1/entry-doc-lib";
		params.put("type", "direction");
		params.put("sort", "doc_lib_name");
		params.put("direction", "asc");
		try {
			return this.connectManage.postJson(url, token, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "httpClient.json", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String httpClient(String url, String requestMethod){
		String result = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("smsContent", "This is test sms");
		long timestamp = this.httpUtil.getTimestamp();
		params.put("to", "15838152042");
		params.put("timestamp", timestamp);
		String sig = this.httpUtil.getMD5("d5318078b04d4a7b94502a38d8ca34d8", timestamp);
		params.put("sig",sig);
		params.put("accountSid", "d5318078b04d4a7b94502a38d8ca34d8");
		result = null;
		try {
			result = this.httpClient.httpClient(url, params, requestMethod);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "httpClient2.json", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String httpClient2(String url, String requestMethod, String Content, String mobile){
		String result = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("smsContent", Content);
		params.put("to", mobile);
		long timestamp = this.httpUtil.getTimestamp();
		String sig = this.httpUtil.getMD5("d5318078b04d4a7b94502a38d8ca34d8", timestamp);
		params.put("timestamp", timestamp);
		params.put("sig",sig);
		params.put("accountSid", "d5318078b04d4a7b94502a38d8ca34d8");
		result = null;
		try {
			HttpClientUtil httpClient2 = new HttpClientUtil();
			result = httpClient2.httpClient(url, params, requestMethod);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	//Spring 提供的用于访问 Rest 服务的客端, RestTemplate提供了多种便捷访问远程Http服务的方法
	@RequestMapping("httpTemplate.json")
	@ResponseBody
	public String httpRestTemplate(String url, String mobile, String content){
		return (String) this.httpRestTemplate.restTemplate(url, content, mobile);
	}
	
	@RequestMapping("connectManage.json")
	@ResponseBody
	public String httpClientManage(String url){
		long timestamp = this.httpUtil.getTimestamp();
		NameValue nv = new NameValue();
		nv.add("smsContent", "This is test sms");
		nv.add("to", "15838152042");
		nv.add("timestamp", timestamp);
		String sig = this.httpUtil.getMD5("d5318078b04d4a7b94502a38d8ca34d8", timestamp);
		nv.add("sig",sig);
		nv.add("accountSid","d5318078b04d4a7b94502a38d8ca34d8");
		
		// Map<String, Object> result = connectManage.postJson(url,nv.toString());
		//return Json.toJson(result, JsonFormat.compact());
		return null;
	}
	
	
}
