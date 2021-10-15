package com.metoo.module.app.test.httpclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;


/**
 * <p>
 *  @Title: HttpClientUtil.java
 * </p>
 * 
 * <p>
 *  @description: 使用HttpClient调用远程接口
 *  HttpClient支持 HTTP 协议最新的版本和建议
	实现了所有 HTTP 的方法（GET,POST,PUT,HEAD 等）
	支持自动转向
	支持 HTTPS 协议
	支持代理服务器等
 *  </p>
 * @author 46075
 *
 */
@Component
public class HttpClientUtil {

	public String httpClient(String url, Map<String, Object> params, String requestMethod) throws UnsupportedEncodingException {
		String result = null;
		StringBuilder parameters = new StringBuilder();
		long timestamp = -1l;
		String sig = null;
		for (String key : params.keySet()) {
			parameters.append(key).append("=" + params.get(key) + "&");
			timestamp = (long) params.get("timestamp");
			sig =  params.get("sig").toString();
		}
		JSONObject json = new JSONObject();
		json.put("smsContent", "This is test sms");
		
		json.put("to", "15838152042");
		json.put("timestamp", timestamp);
		json.put("sig",sig);
		json.put("accountSid", "d5318078b04d4a7b94502a38d8ca34d8");
		
		boolean isGet = "get".equalsIgnoreCase(requestMethod);
		boolean isPost = "post".equalsIgnoreCase(requestMethod);
		boolean isPut = "put".equalsIgnoreCase(requestMethod);
		boolean isDelete = "delete".equalsIgnoreCase(requestMethod);
		if (!parameters.equals("")) {
			parameters.substring(0, parameters.length() - 1);
		}
		DefaultHttpClient client = new DefaultHttpClient();
		CloseableHttpClient hc = HttpClients.createDefault();
		//HttpClient httpClient = HttpClients.createDefault(); 
		HttpRequestBase method = null;
		if (isGet) {
			url += "?" + parameters;
			method = new HttpGet(url);
		} else if (isPost) {
			method = new HttpPost(url);
			HttpPost postMethod = (HttpPost) method;
			StringEntity entity = new StringEntity(parameters.toString());
			postMethod.setEntity(entity);
		} else if (isPut) {
			method = new HttpPut(url);
			HttpPut putMethod = (HttpPut) method;
			StringEntity entity = new StringEntity(parameters.toString());
			putMethod.setEntity(entity);
		} else if (isDelete) {
			url += "?" + parameters;
			method = new HttpDelete(url);
		}
		method.addHeader("Content-type", "application/x-www-form-urlencoded");
		HttpClientContext context = null;
		try {
			//HttpResponse response = client.execute(method, context);
			/*CloseableHttpResponse close_response = hc.execute(method, context);
			close_response.getStatusLine().getStatusCode();*/
			HttpResponse response = hc.execute(method, context);
			// 返回状态码200，则访问接口成功
			if (response.getStatusLine().getStatusCode() == 200) {
				//直接解析entity
				result = EntityUtils.toString(response.getEntity());
				/*HttpEntity entity = response.getEntity();
				System.out.println("entity: " + entity);
				System.out.println("entity_context: " + entity.getContent());*/
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}

	public static String httpRequestToString(String url, String requestMethod, Map<String, String> params,
			String... auth) {
		// 接口返回结果
		String methodResult = null;
		try {
			String parameters = "";
			boolean hasParams = false;
			// 将参数集合拼接成特定格式，如name=zhangsan&age=24
			for (String key : params.keySet()) {
				String value = URLEncoder.encode(params.get(key), "UTF-8");
				parameters += key + "=" + value + "&";
				hasParams = true;
			}
			if (hasParams) {
				parameters = parameters.substring(0, parameters.length() - 1);
			}
			// 是否为GET方式请求
			boolean isGet = "get".equalsIgnoreCase(requestMethod);
			boolean isPost = "post".equalsIgnoreCase(requestMethod);
			boolean isPut = "put".equalsIgnoreCase(requestMethod);
			boolean isDelete = "delete".equalsIgnoreCase(requestMethod);

			// 创建HttpClient连接对象
			DefaultHttpClient client = new DefaultHttpClient();
			HttpRequestBase method = null;
			if (isGet) {
				url += "?" + parameters;
				method = new HttpGet(url);
			} else if (isPost) {
				method = new HttpPost(url);
				HttpPost postMethod = (HttpPost) method;
				StringEntity entity = new StringEntity(parameters);
				postMethod.setEntity(entity);
			} else if (isPut) {
				method = new HttpPut(url);
				HttpPut putMethod = (HttpPut) method;
				StringEntity entity = new StringEntity(parameters);
				putMethod.setEntity(entity);
			} else if (isDelete) {
				url += "?" + parameters;
				method = new HttpDelete(url);
			}
			method.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			// 设置参数内容类型
			method.addHeader("Content-Type", "application/x-www-form-urlencoded");
			// httpClient本地上下文
			HttpClientContext context = null;
			if (!(auth == null || auth.length == 0)) {
				String username = auth[0];
				String password = auth[1];
				UsernamePasswordCredentials credt = new UsernamePasswordCredentials(username, password);
				// 凭据提供器
				CredentialsProvider provider = new BasicCredentialsProvider();
				// 凭据的匹配范围
				provider.setCredentials(AuthScope.ANY, credt);
				context = HttpClientContext.create();
				context.setCredentialsProvider(provider);
			}
			// 访问接口，返回状态码
			HttpResponse response = client.execute(method, context);
			// 返回状态码200，则访问接口成功
			if (response.getStatusLine().getStatusCode() == 200) {
				methodResult = EntityUtils.toString(response.getEntity());
			}
			client.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return methodResult;
	}
	
	
}
