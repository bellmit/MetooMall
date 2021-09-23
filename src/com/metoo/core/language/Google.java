package com.metoo.core.language;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class Google {
	
	  private static final Map<String,String> LANGUAGE = new HashMap<String,String>();
	     
	    static {
	        LANGUAGE.put("sq", "sq");// 阿尔巴尼亚语
	        LANGUAGE.put("ar", "ar");// 阿拉伯语
	        LANGUAGE.put("az", "az");// 阿塞拜疆语
	        LANGUAGE.put("ga", "ga");// 爱 尔兰语
	        LANGUAGE.put("et", "et");// 爱沙尼亚语
	        LANGUAGE.put("eu", "eu");// 巴斯克语
	        LANGUAGE.put("be", "be");// 白俄罗斯语
	        LANGUAGE.put("bg", "bg");// 保加利亚语
	        LANGUAGE.put("is", "is");// 冰岛语
	        LANGUAGE.put("pl", "pl");// 波兰语
	        LANGUAGE.put("fa", "fa");// 波斯语
	        LANGUAGE.put("af", "af");// 布尔语
	        LANGUAGE.put("af", "af");// 南非荷兰语
	        LANGUAGE.put("da", "da");// 丹麦语
	        LANGUAGE.put("de", "de");// 德语
	        LANGUAGE.put("ru", "ru");// 俄语
	        LANGUAGE.put("fr", "fr");// 法语
	        LANGUAGE.put("tl", "tl");// 菲律宾语
	        LANGUAGE.put("fi", "fi");// 芬兰语
	        LANGUAGE.put("ka", "ka");// 格鲁吉亚语
	        LANGUAGE.put("gu", "gu");// 古吉拉特语
	        LANGUAGE.put("ht", "ht");// 海地克里奥尔语
	        LANGUAGE.put("ko", "ko");// 韩语
	        LANGUAGE.put("nl", "nl");// 荷兰语
	        LANGUAGE.put("gl", "gl");// 加利西亚语
	        LANGUAGE.put("ca", "ca");// 加泰罗尼亚语
	        LANGUAGE.put("cs", "cs");// 捷克语
	        LANGUAGE.put("kn", "kn");// 卡纳达语
	        LANGUAGE.put("hr", "hr");// 克罗地亚语
	        LANGUAGE.put("la", "la");// 拉丁文
	        LANGUAGE.put("lv", "lv");// 拉脱维亚语
	        LANGUAGE.put("lo", "lo");// 老挝语
	        LANGUAGE.put("lt", "lt");// 立陶宛语
	        LANGUAGE.put("ro", "ro");// 罗马尼亚语
	        LANGUAGE.put("mt", "mt");// 马耳他语
	        LANGUAGE.put("ms", "ms");// 马来语
	        LANGUAGE.put("mk", "mk");// 马其顿语
	        LANGUAGE.put("bn", "bn");// 孟加拉语 
	        LANGUAGE.put("no", "no");// 挪威语
	        LANGUAGE.put("pt", "pt");// 葡萄牙语
	        LANGUAGE.put("ja", "ja");// 日语
	        LANGUAGE.put("sv", "sv");// 瑞典语
	        LANGUAGE.put("sr", "sr");// 塞尔维亚语
	        LANGUAGE.put("eo", "eo");// 世界语
	        LANGUAGE.put("sk", "sk");// 斯洛伐克语
	        LANGUAGE.put("sl", "sl");// 斯洛文尼亚语
	        LANGUAGE.put("sw", "sw");// 斯瓦希里语
	        LANGUAGE.put("te", "te");// 泰卢固语
	        LANGUAGE.put("ta", "ta");// 泰米尔语
	        LANGUAGE.put("th", "th");// 泰语
	        LANGUAGE.put("tr", "tr");// 土耳其语
	        LANGUAGE.put("cy", "cy");// 威尔士语
	        LANGUAGE.put("ur", "ur");// 乌尔都语
	        LANGUAGE.put("uk", "uk");// 乌克兰语
	        LANGUAGE.put("iw", "iw");// 希伯来语
	        LANGUAGE.put("el", "el");// 希腊语
	        LANGUAGE.put("es", "es");// 西班牙语
	        LANGUAGE.put("hu", "hu");// 匈牙利语
	        LANGUAGE.put("hy", "hy");// 亚美尼亚语
	        LANGUAGE.put("it", "it");// 意大利语
	        LANGUAGE.put("yi", "yi");// 意第绪语
	        LANGUAGE.put("hi", "hi");// 印地语
	        LANGUAGE.put("id", "id");// 印尼语
	        LANGUAGE.put("en", "en");// 英语
	        LANGUAGE.put("vi", "vi");// 越南语
	        LANGUAGE.put("zh-TW", "zh-TW");// 中文繁体
	        LANGUAGE.put("zh-CN", "zh-CN");// 中文简体
	 
	    }
	
	private String uri;// http url
	private String key;// google API密钥
	private String target;// 目标语言
	private String source;// 源语言
	private String model;// 翻译模型
	
	
	public Google(String uri, String key, String target, String source,String model) {
		super();
		this.uri = uri;
		this.key = key;
		this.target = target;
		this.source = source;
		this.model = model;
	}

	public Google(String uri, String key) {
		super();
		this.uri = uri;
		this.key = key;
	}

	
	/**
	 * 
	 * @param params
	 * @return
	 * @description 检测语言类型
	 */
	public String detect(String params){
		URL url = null;  
		HttpURLConnection http = null;  
	try {  
	    url = new URL(uri);;  
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
	    NameValue form = new NameValue();
		form.add("q", params);
		form.add("key", key);
	    OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "utf-8");  
	    osw.write(form.toString());  
	    osw.flush();  
	    osw.close();  
	    BufferedReader in = null;
	    if (http.getResponseCode() == 200) {  
	        in = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));  
	        String inputLine; 
	        String result = "";
	        while ((inputLine = in.readLine()) != null) {  
	            result += inputLine;  
	        }  
	        in.close();  
	        return result; 
	    }  
	} catch (Exception e) {  
	    System.out.println("语言检测失败");  
	}
	return null; 
}
	
	
	public String translation(String params){
		String result = "";
		URL url = null;  
		HttpURLConnection http = null;  
	try {  
	    url = new URL(uri);  
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
	    NameValue form = new NameValue();
		form.add("q", params);
		form.add("key", key);
		form.add("target", target);
		form.add("source", LANGUAGE.get(source));
		form.add("model", model);
		
	    OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "utf-8");  
	    osw.write(form.toString());  
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


	/**
     * Key -> Value
     * 语言     -> 单词表示
     */
  /*  private static final Map<String,String> LANGUAGE = new HashMap<String,String>();
     
    static {
        LANGUAGE.put("阿尔巴尼亚语", "sq");
        LANGUAGE.put("阿拉伯语", "ar");
        LANGUAGE.put("阿塞拜疆语", "az");
        LANGUAGE.put("爱尔兰语", "ga");
        LANGUAGE.put("爱沙尼亚语", "et");
        LANGUAGE.put("巴斯克语", "eu");
        LANGUAGE.put("白俄罗斯语", "be");
        LANGUAGE.put("保加利亚语", "bg");
        LANGUAGE.put("冰岛语", "is");
        LANGUAGE.put("波兰语", "pl");
        LANGUAGE.put("波斯语", "fa");
        LANGUAGE.put("布尔语", "af");
        LANGUAGE.put("南非荷兰语", "af");
        LANGUAGE.put("丹麦语", "da");
        LANGUAGE.put("德语", "de");
        LANGUAGE.put("俄语", "ru");
        LANGUAGE.put("法语", "fr");
        LANGUAGE.put("菲律宾语", "tl");
        LANGUAGE.put("芬兰语", "fi");
        LANGUAGE.put("格鲁吉亚语", "ka");
        LANGUAGE.put("古吉拉特语", "gu");
        LANGUAGE.put("海地克里奥尔语", "ht");
        LANGUAGE.put("韩语", "ko");
        LANGUAGE.put("荷兰语", "nl");
        LANGUAGE.put("加利西亚语", "gl");
        LANGUAGE.put("加泰罗尼亚语", "ca");
        LANGUAGE.put("捷克语", "cs");
        LANGUAGE.put("卡纳达语", "kn");
        LANGUAGE.put("克罗地亚语", "hr");
        LANGUAGE.put("拉丁语", "la");
        LANGUAGE.put("拉脱维亚语", "lv");
        LANGUAGE.put("老挝语", "lo");
        LANGUAGE.put("立陶宛语", "lt");
        LANGUAGE.put("罗马尼亚语", "ro");
        LANGUAGE.put("马耳他语", "mt");
        LANGUAGE.put("马来语", "ms");
        LANGUAGE.put("马其顿语", "mk");
        LANGUAGE.put("孟加拉语", "bn");
        LANGUAGE.put("挪威语", "no");
        LANGUAGE.put("葡萄牙语", "pt");
        LANGUAGE.put("日语", "ja");
        LANGUAGE.put("瑞典语", "sv");
        LANGUAGE.put("塞尔维亚语", "sr");
        LANGUAGE.put("世界语", "eo");
        LANGUAGE.put("斯洛伐克语", "sk");
        LANGUAGE.put("斯洛文尼亚语", "sl");
        LANGUAGE.put("斯瓦希里语", "sw");
        LANGUAGE.put("泰卢固语", "te");
        LANGUAGE.put("泰米尔语", "ta");
        LANGUAGE.put("泰语", "th");
        LANGUAGE.put("土耳其语", "tr");
        LANGUAGE.put("威尔士语", "cy");
        LANGUAGE.put("乌尔都语", "ur");
        LANGUAGE.put("乌克兰语", "uk");
        LANGUAGE.put("希伯来语", "iw");
        LANGUAGE.put("希腊语", "el");
        LANGUAGE.put("西班牙语", "es");
        LANGUAGE.put("匈牙利语", "hu");
        LANGUAGE.put("亚美尼亚语", "hy");
        LANGUAGE.put("意大利语", "it");
        LANGUAGE.put("意第绪语", "yi");
        LANGUAGE.put("印地语", "hi");
        LANGUAGE.put("印尼语", "id");
        LANGUAGE.put("英语", "en");
        LANGUAGE.put("越南语", "vi");
        LANGUAGE.put("中文繁体", "zh-TW");
        LANGUAGE.put("中文简体", "zh-CN");
 
    }*/
    /**
     * GET 谷歌翻译
     * @param value 待翻译的字符串
     * @param src 源语言
     * @param target 目标语言
     * @return 翻译结果JSON字符串
     */
    public static String translate(String value, String src, String target) {
        src = LANGUAGE.get(src);
        target = LANGUAGE.get(target);
        if(src == null || target == null){
            return null;
        }
        NameValue form = new NameValue();
       /* form.add("client", "t");
        form.add("hl", src);
        form.add("ie", "UTF-8");
        form.add("multires", 1);
        form.add("oe", "UTF-8");
        form.add("otf", 1);
        form.add("prev", "conf");
        form.add("psl", src);
        form.add("ptl", src);
        form.add("sc", 1);
        form.add("sl", src);
        form.add("ssel", 3);
        form.add("text", WebRequest.encode(value, "UTF-8"));
        form.add("tl", target);
        form.add("tsel", 6);*/
        form.add("q", value);
        form.add("target", target);
        form.add("format", "");
        form.add("source", src);
        form.add("model", "nmt");
        form.add("key", "AIzaSyDC4pcWmrtp8flJd4iHULadvOszOVh57Io");
 
        return WebRequest.get("https://translation.googleapis.com/language/translate/v2？"
                + form.toString(), "UTF-8");
    }
	
}
