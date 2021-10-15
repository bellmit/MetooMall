package com.metoo.module.app.test.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@Component
public class HttpClientConnectionManager {

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
	 
	    //创建HttpClient连接池管理对象
	    private final static PoolingHttpClientConnectionManager poolConnManager 
	    								= new PoolingHttpClientConnectionManager();  //连接池管理器
	    
	    private final static HttpRequestRetryHandler httpRequestRetryHandler 
	    								= new HttpRequestRetryHandler() {  //retry handler
	        public boolean retryRequest(IOException exception,
	                                    int executionCount, HttpContext context) {
	            if (executionCount >= 5) {
	                return false;
	            }
	            if (exception instanceof NoHttpResponseException) {
	                return true;
	            }
	            if (exception instanceof InterruptedIOException) {
	                return false;
	            }
	            if (exception instanceof UnknownHostException) {
	                return false;
	            }
	            if (exception instanceof ConnectTimeoutException) {
	                return false;
	            }
	            HttpClientContext clientContext = HttpClientContext
	                    .adapt(context);
	            HttpRequest request = clientContext.getRequest();
	 
	            if (!(request instanceof HttpEntityEnclosingRequest)) {
	                return true;
	            }
	            return false;
	        }
	    };
	    
	    static RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(10000)//获取数据的超时时间
				.setConnectTimeout(10000)// 建立连接的超时
				.setConnectionRequestTimeout(10000)// 连接池获取到连接的超时时间
				.build();
	 
	    static {   //类加载的时候 设置最大连接数 和 每个路由的最大连接数
	        poolConnManager.setMaxTotal(2000); //最大连接数
	        poolConnManager.setDefaultMaxPerRoute(1000);// 路由是由host管理，数量不易把握
	    }
	 
	    /**
	     * ########################### core code#######################
	     * @return
	     */
	    private static CloseableHttpClient getCloseableHttpClient() {
	        CloseableHttpClient httpClient = HttpClients.custom()
	                .setConnectionManager(poolConnManager)
	                .setRetryHandler(httpRequestRetryHandler)
	                .setDefaultRequestConfig(requestConfig)
	                .build();
	 
	        return httpClient;
	    }
	 
	    /**
	     * buildResultMap
	     *
	     * @param response
	     * @param entity
	     * @return
	     * @throws IOException
	     */
	    private static Map<String, Object> buildResultMap(CloseableHttpResponse response, HttpEntity entity) throws
	            IOException {
	        Map<String, Object> result;
	        result = new HashMap<>(2);
	        result.put(KEY_STATUS_CODE, response.getStatusLine().getStatusCode());  //status code
	        if (entity != null) {
	            result.put(KEY_CONTENT, EntityUtils.toString(entity, "UTF-8")); //message content
	        }
	        return result;
	    }
	 
	    /**
	     * send json by post method
	     *
	     * @param url
	     * @param message
	     * @return
	     * @throws Exception
	     */
	    public static Map<String, Object> postJson(String url, String token, Map<String,String> params) throws Exception {
	    	CloseableHttpClient asd = OSSHelper();
	        Map<String, Object> result = null;
	        HttpPost httpPost = new HttpPost(url);
	        
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
	        return result;
	    }
	    
	    public void getstrign(){
	    	
	    }
	    
	}
