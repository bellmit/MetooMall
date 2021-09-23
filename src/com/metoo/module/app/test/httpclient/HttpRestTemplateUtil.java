package com.metoo.module.app.test.httpclient;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

/**
 * <p>
 * 	@Title: HttpRestTemplate.java
 * </p>
 * 
 * <p>
 * 	@description: 0,使用 application/x-www-form-urlencoded 这个请求头 会对数据镜像 url 编码;可以传递 非 字符串类型的数据！
 * 				  1,exchange()可以指定HttpMethod,请求体requestEntity，建议在开发中使用exchange方法
 * 				  2,https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#rest-resttemplate
 * 
 * </P>
 * @author 46075
 *
 */
@Component
public class HttpRestTemplateUtil {

	@Autowired
	private HttpUtil httpUtil;

	// 建议以注入方式使用 @Configuration
	public String restTemplate(String url, String content, String mobile) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		// headers.add("Content-Type", "application/x-www-form-urlencoded");
		// timestamp:long类型时间戳--转String
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<Object, Object> params = new LinkedMultiValueMap<Object, Object>();
		params.add("smsContent", content);
		long timestamp = this.httpUtil.getTimestamp();
		params.add("to", mobile);
		params.add("timestamp", String.valueOf(timestamp));
		String sig = this.httpUtil.getMD5("d5318078b04d4a7b94502a38d8ca34d8", timestamp);
		params.add("sig", sig);
		params.add("accountSid", "d5318078b04d4a7b94502a38d8ca34d8");

		HttpEntity<MultiValueMap<Object, Object>> httpEntity = new HttpEntity<MultiValueMap<Object, Object>>(params, headers);

		ResponseEntity<String> exchange = restTemplate.postForEntity(url, httpEntity, String.class);
		return exchange.getBody().toString();
	}
}
