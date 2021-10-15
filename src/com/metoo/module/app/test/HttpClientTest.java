package com.metoo.module.app.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import org.junit.rules.Timeout.Builder;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.metoo.module.app.buyer.domain.Http;

/**
 * <p>
 * Description : StringHttpMessageConverter :使用http
 * </p>
 * 
 * @author 46075
 *
 */
@Controller
public class HttpClientTest {

	/*
	 * org.apache.http.client.config.RequestConfig.Builder customReqConf =
	 * RequestConfig.custom(); customReqConf.setConnectTimeout(10000);
	 * customReqConf.setSocketTimeout(10000);
	 * customReqConf.setConnectionRequestTimeout(10000);
	 * httpPost.setConfig(customReqConf.build());
	 */

	private static HttpClient client;
	String url = "http://local.soarmall.com/getGoods.json";
	private static String CHARSET = "UTF-8";

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws InterruptedException
	 * @descript 使用HttpClient 请求
	 */
	@RequestMapping(value = "/request.json")
	@ResponseBody
	public String send(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
		client = new HttpClient();
		// HttpPost httpPost = new
		// HttpPost("http://local.soarmall.com/getGoods.json");
		GetMethod get = new GetMethod("http://local.soarmall.com/goodsProperties.json");
		get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
		// 设置连接超时时间(单位毫秒)
		managerParams.setConnectionTimeout(5000);
		// 设置读数据超时时间(单位毫秒)
		managerParams.setSoTimeout(5000);
		String res = "";
		int j = 0;
		int status = 0;
		String ress = "";
		for (int i = 1; i <= 3; i++) {
			j++;
			if (j == 3) {
				System.out.println("发从第" + i + "次请求");
				ress = "返回回来的数据：" + null + "  状态值：" + 200 + "循环次数" + j;
			} else {
				try {
					System.out.println("发从第" + i + "次请求");
					status = client.executeMethod(get);
					res = get.getResponseBodyAsString().trim();
					ress = "返回回来的数据：" + res + "  状态值：" + status + "循环次数" + j;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return Json.toJson(ress, JsonFormat.compact());
	}

	@RequestMapping(value = "/request2.json")
	@ResponseBody
	public String send2(HttpServletRequest request, HttpServletResponse response) {
		String ress = "返回回来的数据";
		JSON.parseObject("");
		return Json.toJson(ress, JsonFormat.compact());
	}

	@RequestMapping("/translate.json")
	public void sendPost(){
		HttpURLConnection httpconn = null;
		String senderId = "";
		//组装请求参数
			JSONObject map = new JSONObject();
			map.put("q", "Robinson Crusoe is a real hero.");
			map.put("target", "ar");
			map.put("format", "");
			map.put("source", "en");
			map.put("model", "nmt");
			map.put("key", "AIzaSyDC4pcWmrtp8flJd4iHULadvOszOVh57Io");
			String params = map.toString();
			String HttpSendSms=Http.post("https://translation.googleapis.com/language/translate/v2", params);
			JSONObject jsonObject =  JSON.parseObject(HttpSendSms);
			System.out.println(jsonObject);
	}
	
	@RequestMapping("/detect.json")
	@ResponseBody
	public void detect(){
		HttpURLConnection httpconn = null;
		String senderId = "";
		//组装请求参数
			JSONObject map = new JSONObject();
			map.put("q", "男鞋");
			map.put("key", "AIzaSyDC4pcWmrtp8flJd4iHULadvOszOVh57Io");
			String params = map.toString();
			String HttpSendSms=Http.post("https://translation.googleapis.com/language/translate/v2/detect", params);
			JSONObject jsonObject =  JSON.parseObject(HttpSendSms);
			System.out.println(jsonObject);
	}
	
	@RequestMapping("/detects.json")
	@ResponseBody
	public String detect1() throws IOException{
		String word = "men shoes";
		    URL obj = new URL("https://translation.googleapis.com/language/translate/v2/detect?" + "q=" + URLEncoder.encode(word, "UTF-8") + "&key=AIzaSyDC4pcWmrtp8flJd4iHULadvOszOVh57Io");
	        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	        con.setRequestProperty("User-Agent", "Mozilla/5.0");

	        BufferedReader in = new BufferedReader(
	                new InputStreamReader(con.getInputStream()));
	        String inputLine;
	        StringBuffer response = new StringBuffer();

	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();

	        return response.toString();
	}
	
	@RequestMapping("/translate1.json")
	@ResponseBody
	public String translate1() throws IOException{
		String word = "shoes";
		/*Translate translate = TranslateOptions.getDefaultInstance().getService();
		Translation translation = translate.translate("¡Hola Mundo!");
		System.out.println(translation.getTranslatedText());
		System.out.printf("Translated Text:\n\t%s\n", translation.getTranslatedText());*/
		    URL obj = new URL("https://translation.googleapis.com/language/translate/v2?" 
		    					+ "q=" + word
		    					+ "&target=ar" 
		    					+ "&format=" 
		    					+ "&source=en" 
		    					+ "&model=nmt" 
		    					+ "&key=AIzaSyDC4pcWmrtp8flJd4iHULadvOszOVh57Io");
	        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

	        BufferedReader in = new BufferedReader(
	                new InputStreamReader(con.getInputStream()));
	        String inputLine;
	        StringBuffer response = new StringBuffer();

	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();

	        return response.toString();
	}
	
	
	@RequestMapping("/translate2.json")
	@ResponseBody
    public String doPost() {
		 String msg = "";
    	HttpURLConnection httpURLConnection = null;
        try {
            // 1. 获取访问地址URL
            URL url = new URL("https://translation.googleapis.com/language/translate/v2");
            // 2. 创建HttpURLConnection对象
            httpURLConnection = (HttpURLConnection) url.openConnection();
            /* 3. 设置请求参数等 */
            // 请求方式  默认 GET
            httpURLConnection.setRequestMethod("POST");
            // 超时时间
            httpURLConnection.setConnectTimeout(3000);
            // 设置是否输出
            httpURLConnection.setDoOutput(true);
            // 设置是否读入
            httpURLConnection.setDoInput(true);
            // 设置是否使用缓存
            httpURLConnection.setUseCaches(false);
            // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
            httpURLConnection.setInstanceFollowRedirects(true);
			// 设置请求头
			httpURLConnection.addRequestProperty("sysId","sysId");
            // 设置使用标准编码格式编码参数的名-值对
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 连接
            httpURLConnection.connect();
            /* 4. 处理输入输出 */
            // 写入参数到请求中
        	/**
    		 * map.put("q", "Robinson Crusoe is a real hero.");
    			map.put("target", "ar");
    			map.put("format", "");
    			map.put("source", "en");
    			map.put("model", "nmt");
    			map.put("key", "AIzaSyDC4pcWmrtp8flJd4iHULadvOszOVh57Io");
    		 */
            String params = "q=你好&target=en&format=&source=zh-CN&model=nmt&key=AIzaSyDC4pcWmrtp8flJd4iHULadvOszOVh57Io";
            OutputStream out = httpURLConnection.getOutputStream();
            out.write(params.getBytes());
            // 简化
            //httpURLConnection.getOutputStream().write(params.getBytes());
            out.flush();
            out.close();
            // 从连接中读取响应信息
           
            int code = httpURLConnection.getResponseCode();
            if (code == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    msg += line + "\n";
                }
                reader.close();
            }
            // 处理结果
        } catch (IOException e) {
        }finally {
            // 5. 断开连接
            if (null != httpURLConnection){
                try {
                    httpURLConnection.disconnect();
                }catch (Exception e){
                }
            }
        }
        return msg;
    }
	
	@RequestMapping("translate3.json")
	@ResponseBody
	public String translate3() throws Exception {
		  HttpEntity entity = null;
        //创建浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //输入访问的地址
        HttpPost httpPost = new HttpPost("https://translation.googleapis.com/language/translate/v2");

        //创建参数
        List<NameValuePair> prams = new ArrayList<>();

        prams.add(new BasicNameValuePair("q","men shoes"));
        prams.add(new BasicNameValuePair("target","ar"));
        prams.add(new BasicNameValuePair("source","en"));
        prams.add(new BasicNameValuePair("model","nmt"));
        prams.add(new BasicNameValuePair("key","AIzaSyDC4pcWmrtp8flJd4iHULadvOszOVh57Io"));

        //将list参数 转换成post请求需要的嗯entity
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(prams);

        //设置post参数

        httpPost.setEntity(formEntity);

        //点击回车，访问地址，拿到返回数据CloseableHttpResponse response
        CloseableHttpResponse response = null;
        String str = "";
        try {
			response = httpClient.execute(httpPost);

			//解析数据
			if (response.getStatusLine().getStatusCode() == 200) {
			    //获得返回的entity
			    entity = response.getEntity();
			    //将entity转换成字符串
			    str =  EntityUtils.toString(entity, "utf-8");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			 try {
	                response.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            try {
	                httpClient.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
        }
		return str;
    }
	
	@RequestMapping("translate4.json")
	@ResponseBody
	public String translate4(HttpServletRequest request, HttpServletResponse responses, String params) throws Exception {
		  HttpEntity entity = null;
        //创建浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //输入访问的地址
        HttpPost httpPost = new HttpPost("https://translation.googleapis.com/language/translate/v2/detect");

        //创建参数
        List<NameValuePair> prams = new ArrayList();

        prams.add(new BasicNameValuePair("q", params));
        prams.add(new BasicNameValuePair("key","AIzaSyDC4pcWmrtp8flJd4iHULadvOszOVh57Io"));

        //将list参数 转换成post请求需要的嗯entity
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(prams);

        //设置post参数

        httpPost.setEntity(formEntity);

        //点击回车，访问地址，拿到返回数据CloseableHttpResponse response
        CloseableHttpResponse response = null;
        String str = "";
        try {
			response = httpClient.execute(httpPost);

			//解析数据
			if (response.getStatusLine().getStatusCode() == 200) {
			    //获得返回的entity
			    entity = response.getEntity();
			    //将entity转换成字符串
			    str =  EntityUtils.toString(entity, "utf-8");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			 try {
	                response.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            try {
	                httpClient.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
        }
		return str;
    }
	
	@RequestMapping("detect5.json")
	@ResponseBody
	public String detect5(){
		URL url = null;  
		HttpURLConnection http = null;  
	  /**
	   *   HttpPost httpPost = new HttpPost("https://translation.googleapis.com/language/translate/v2/detect");

        //创建参数
        List<NameValuePair> prams = new ArrayList();

        prams.add(new BasicNameValuePair("q", params));
        prams.add(new BasicNameValuePair("key","AIzaSyDC4pcWmrtp8flJd4iHULadvOszOVh57Io"));
	   */
	try {  
	    url = new URL("https://translation.googleapis.com/language/translate/v2/detect");  
	    http = (HttpURLConnection) url.openConnection();  
	    http.setDoInput(true);  
	    http.setDoOutput(true);  
	    http.setUseCaches(false);  
	    http.setConnectTimeout(50000);//设置连接超时  
	//如果在建立连接之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。  
	    http.setReadTimeout(50000);//设置读取超时  
	//如果在数据可读取之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。            
	    http.setRequestMethod("POST");  
	    // http.setRequestProperty("Content-Type","text/xml; charset=UTF-8");  
	    http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
	    http.connect();  
	    String param = "q=" + "你好"   
	            + "&key=" + "AIzaSyDC4pcWmrtp8flJd4iHULadvOszOVh57Io";  
	  
	    OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "utf-8");  
	    osw.write(param);  
	    osw.flush();  
	    osw.close();  
	    BufferedReader in = null;
	    if (http.getResponseCode() == 200) {  
	        in = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));  
	        String inputLine; 
	        String result = null;
	        while ((inputLine = in.readLine()) != null) {  
	            result += inputLine;  
	        }  
	        in.close();  
	        //result = "["+result+"]";  
	    }  
	} catch (Exception e) {  
	    System.out.println("err");  
	}
	return null; 
}
	
	@RequestMapping("translation5.json")
	@ResponseBody
	public String translation5(String keyword){
		 String result = "";
		URL url = null;  
		HttpURLConnection http = null;  
	try {  
	    url = new URL("https://translation.googleapis.com/language/translate/v2");  
	    http = (HttpURLConnection) url.openConnection();  
	    http.setDoInput(true);  
	    http.setDoOutput(true);  
	    http.setUseCaches(false);  
	    http.setConnectTimeout(50000);//设置连接超时  
	//如果在建立连接之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。  
	    http.setReadTimeout(50000);//设置读取超时  
	//如果在数据可读取之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。            
	    http.setRequestMethod("POST");  
	    // http.setRequestProperty("Content-Type","text/xml; charset=UTF-8");  
	    http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
	    http.connect();  
	    String param = "q=" + keyword 
				            + "&key=" + "AIzaSyDC4pcWmrtp8flJd4iHULadvOszOVh57Io"
				    		+ "&target=" + "en"
				            + "&source=" + "ar"
				    		+ "&model=" + "nmt";  
	  
	    OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "utf-8");  
	    osw.write(param);  
	    osw.flush();  
	    osw.close();  
	    BufferedReader in = null;
	    if (http.getResponseCode() == 200) {  
	        in = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));  
	        String inputLine; 
	        while ((inputLine = in.readLine()) != null) {  
	            result += inputLine;  
	        }  
	        in.close();  
	        //result = "["+result+"]";  
	    }  
	} catch (Exception e) {  
	    System.out.println("err");  
	}
	return result; 
}
	
}
