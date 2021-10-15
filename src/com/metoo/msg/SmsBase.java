package com.metoo.msg;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.metoo.foundation.domain.SysConfig;
import com.metoo.module.app.buyer.domain.Http;
/**
 * 
 * <p>
 * Title: SmsBase.java
 * </p>
 * 
 * <p>
 * Description: 系统手机短信发送类，结合第三方短信平台进行管理使用
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * 
 * <p>
 * Company: 沈阳网之商科技有限公司 www.koala.com
 * </p>
 * 
 * @author erikzhang
 * 
 * @date 2014-4-24
 * 
 * @version koala_b2b2c v2.0 2015版
 */
public class SmsBase {
	private String url;
	private String id;
	private String pwd;
	private String appkey;// 短信应用appkey
	private String secretkey;// 短信应用secretkey

	
	public SmsBase(String url, String id, String pwd) {
		this.url = url;
		this.id = id;
		this.pwd = pwd;
	}
	
	public SmsBase(String url, String id, String pwd, String appkey, String secretkey) {
		this.url = url;
		this.id = id;
		this.pwd = pwd;
		this.appkey = appkey;
		this.secretkey = secretkey;
	}



	public static String KEY_STATUS_CODE = "statusCode";
	public static String KEY_CONTENT = "content";
	private static CloseableHttpClient ossClient;
	
	public static CloseableHttpClient OSSHelper() throws Exception {
	        // 采用绕过验证的方式处理https请求
	        SSLContext sslcontext = createIgnoreVerifySSL();

	        // 设置协议http和https对应的处理socket链接工厂的对象
	        SSLConnectionSocketFactory ssl = new SSLConnectionSocketFactory(sslcontext,
	                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
	                .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", ssl).build();
	        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
	        HttpClients.custom().setConnectionManager(connManager);

	        return ossClient = HttpClients.custom().setConnectionManager(connManager).build();
	    }
	   
	   /**
	     * 绕过验证
	     * 
	     * @return
	     * @throws NoSuchAlgorithmException
	     * @throws KeyManagementException
	     */
	    private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {

	        SSLContext sc = SSLContext.getInstance("TLS");

	        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
	        X509TrustManager trustManager = new X509TrustManager() {
	            @Override
	            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
	                    String paramString) throws CertificateException {}

	            @Override
	            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
	                    String paramString) throws CertificateException {}

	            @Override
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	        };

	        sc.init(null, new TrustManager[] { trustManager }, null);
	        return sc;
	    }
	    
	public String SendSms(String mobile, String content)
			throws UnsupportedEncodingException {
		String code = "1"; //1:代表失败 0：代表成功
		Integer x_ac = 10;// 发送信息
		HttpURLConnection httpconn = null;
		content = Jsoup.clean(content, Whitelist.none()).replace("&nbsp;", "")
				.trim();
		String senderId = "";
		//组装请求参数
			JSONObject map=new JSONObject();
			map.put("account", id);
			map.put("password", pwd);
			map.put("msg", content);
			map.put("mobile", mobile);
			map.put("senderId", senderId);
			String params=map.toString();
		try {
			String HttpSendSms=Http.post("http://intapi.253.com/send/json", params);
			JSONObject jsonObject =  JSON.parseObject(HttpSendSms);
			code = jsonObject.get("code").toString();
			String msgid = jsonObject.get("msgid").toString();
			String error = jsonObject.get("error").toString();
			System.out.println("状态码:" + code + ",状态码说明:" + error + ",消息id:" + msgid);
			//{"code": "108", "error":"手机号码格式错误", "msgid":""}
			//logger.info("状态码:" + code + ",状态码说明:" + error + ",消息id:" + msgid);
		} catch (Exception e) {
			// TODO: handle exception
			//logger.error("请求异常：" + e);
		}
		return code;
	}
	
	public String SendSmsh(String mobile, String content)
			throws UnsupportedEncodingException {
		Integer x_ac = 10;// 发送信息
		HttpURLConnection httpconn = null;
		String result = "-20";
		content = Jsoup.clean(content, Whitelist.none()).replace("&nbsp;", "");// 过滤所有html代码
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		sb.append("?id=").append(id);
		sb.append("&pwd=").append(pwd);
		sb.append("&to=").append(mobile);
		sb.append("&content=").append(URLEncoder.encode(content, "gb2312")); // 注意乱码的话换成gb2312编码
		try {
			URL url = new URL(sb.toString());
			httpconn = (HttpURLConnection) url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					httpconn.getInputStream()));
			result = rd.readLine();
			rd.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (httpconn != null) {
				httpconn.disconnect();
				httpconn = null;
			}

		}
		return result;
	}
	   private static Map<String, Object> buildResultMap(CloseableHttpResponse response, HttpEntity entity) throws ParseException, IOException {
		   Map<String, Object> result;
		   result = new HashMap<>(2);
		   result.put(KEY_STATUS_CODE, response.getStatusLine().getStatusCode());  //status code
		   if (entity != null) {
			   result.put(KEY_CONTENT, EntityUtils.toString(entity, "UTF-8")); //message content
		   }
		   return result;
	   }
	
	 public String postJson(String mobile, String content) throws Exception {
	 	String code = "1"; //1:代表失败 0：代表成功
	 	String content1 = java.net.URLEncoder.encode(content,   "utf-8");     
	    String content2 = java.net.URLDecoder.decode(content1,   "utf-8");
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", mobile);
		params.put("content", content2);
		params.put("appkey", appkey);
		params.put("secretkey", secretkey);
		
		CloseableHttpClient asd = OSSHelper();
        Map<String, Object> result = null;
        HttpPost httpPost = new HttpPost("http://api.wftqm.com/api/sms/mtsend");
        CloseableHttpResponse response = null;
        try {
            httpPost.setHeader("Accept", "application/json;charset=UTF-8");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
//	            httpPost.setHeader("Authorization",  "Basic" + token);
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            for(Map.Entry<String,String> entry : params.entrySet()){
            	pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairs,"UTF-8"));
            response = ossClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // response.getStatusLine().getStatusCode();
            result = buildResultMap(response, entity);
            JSONObject jsonObject =  JSON.parseObject(result.get("content").toString());
	        code = jsonObject.get("code").toString();
			String messageid = jsonObject.get("messageid").toString();
			String message = jsonObject.get("result").toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    
        return code;
    }
}