package com.metoo.module.app.test.httpclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.jasper.tagplugins.jstl.core.Url;
import org.springframework.stereotype.Component;

import com.metoo.core.language.NameValue;

/**
 * 
 * <p>
 * 	@Title: HttpUrlConnectionUtil.java
 * </p>
 * 
 * <p>
 * 	@description: java.net.HttpURLConnection 包的HttpURLConnection
 * 					对一个可读的InputStream调用close()方法时，就有可能会导致连接池失效
					GET:
					1、创建远程连接
					2、设置连接方式（get、post、put。。。）
					3、设置连接超时时间
					4、设置响应读取时间
					5、发起请求
					6、获取请求数据
					7、关闭连接
					
					POST:
					1、创建远程连接
					2、设置连接方式（get、post、put。。。）
					3、设置连接超时时间
					4、设置响应读取时间
					5、当向远程服务器传送数据/写数据时，需要设置为true（setDoOutput）
					6、当前向远程服务读取数据时，设置为true，该参数可有可无（setDoInput）
					7、设置传入参数的格式:（setRequestProperty）
					8、设置鉴权信息：Authorization:（setRequestProperty）
					9、设置参数
					10、发起请求
					11、获取请求数据
					12、关闭连接

 * </p>
 * 
 * 
 * .net包的HttpURLConnection
 * @author 46075
 *
 */
@Component
public class HttpUrlConnectionUtil {
	
	private CloseableHttpClient ossClient;

    public void OSSHelper() throws Exception {
        // 采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();

        // 设置协议http和https对应的处理socket链接工厂的对象
        SSLConnectionSocketFactory ssl = new SSLConnectionSocketFactory(sslcontext,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", ssl).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);

        // 创建自定义的httpclient对象
        ossClient = HttpClients.custom().setConnectionManager(connManager).build();
    }
    
    /**
     * 绕过验证
     * 
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {

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


	/**W
	 * @httpUrlConnection
	 * @param url 远程接口地址
	 * @param params 传递参数
	 * @param methodType 请求方式
	 * @return String 返回结果
	 * @description 向远程接口发送请求，返回字节流类型结果
	 */
	public String httpUrlConnection(String url, Map<String, Object> params, String methodType, String token) {
		String result = null;
		InputStream is = null;
		HttpsURLConnection conn = null;
		BufferedReader br = null;
		StringBuffer results = new StringBuffer();
		boolean isGet = "get".equalsIgnoreCase(methodType);
		boolean isPost = "post".equalsIgnoreCase(methodType);
		
		//封装参数方式一: 定义参数对象
		/*NameValue nv = new NameValue();
		for (String key : params.keySet()) {
			nv.add(key, params.get(key));
		}
		if (isGet) {
			url += "?" + nv.toString();
		}*/
		//封装参数方式二: 拼接字符转
		StringBuilder parameters = new StringBuilder();
		for (String key : params.keySet()) {
			parameters.append(key).append("=" + params.get(key) + "&");
		}
		if (!parameters.equals("")) {
			parameters.substring(0, parameters.length() - 1);
		}
		if (isGet) {
			url += "?" + parameters;
		}
		
		try {
			URL u = new URL(url);
			conn = (HttpsURLConnection) u.openConnection();
			
			
			conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			if(token != null && !token.equals("")){
				conn.setRequestProperty("Authorization", "Bearer 54Uq2FsZ7JKGAxcdfTdcSgZPnS_jaFZMaIKxfTs_-sk.xwDjnURUwiSOtfb9Nj7msBVq_3FAkyCISgTBbmgeK7U");
			}
			
			if (isPost) {
				conn.setDoOutput(true);
			}
			conn.setDoInput(true);
			if (isPost) {
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");
			} else {
				conn.setRequestMethod("GET");
			}
				conn.connect();//建立TCP连接
			 //post方式需要将传递的参数输出到conn对象中
            if(isPost){
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(parameters.toString());
                dos.flush();
                dos.close();
            }
            //从HttpURLConnection对象中读取响应的消息
            //执行该语句时才正式发起请求
            is = conn.getInputStream();
            byte[] b = new byte[is.available()];
            is.read(b);
            result = new String(b);
            //获取响应数据
     /*       if (conn.getResponseCode() == 200) {
                //获取返回的数据
                is = conn.getInputStream();
                if (null != is) {
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String temp = null;
                    while (null != (temp = br.readLine())) {
                    	results.append(temp);
                    }
                }
            }
            */
            BufferedReader in = null;
    	    if (conn.getResponseCode() == 200) {  
    	        in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));  
    	        String inputLine; 
    	        while ((inputLine = in.readLine()) != null) {  
    	        	results.append(inputLine);
    	        }  
    	        in.close();  
    	    } 
            
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
			//关闭远程连接
			 conn.disconnect();
		}
		return results.toString();
	}
}
