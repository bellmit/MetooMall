package com.metoo.core.language;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

@Controller
public class Test {
	
	@RequestMapping("getGoogle.json")
	public void main() {
        
        
        exec("How old are you", "英语","中文简体");
         
       /* exec("How old are you", "英语","中文繁体");
         
        exec("你多大年纪了","中文简体", "英语");
         
        exec("你多大年紀了","中文繁体", "日语");*/
    }
    public static void exec(String a,String b,String c){
        String html = Google.translate(a,b,c);
        if(html == null){
            System.out.println("翻译失败");
        }
        System.out.println("【返回数据】");
        System.out.println(html);
         
        //解析翻译的结果.看个人需要了
        {//字符串函数解析 .也可以用json-lib 
            String s = WebRequest.mid(html, "[[[", "]]");
            String[] ss = s.split(",");
            System.out.println("【翻译结果】");
            for(int i = 0 ; i < ss.length ; i++){
                s = ss[i].substring(1,ss[i].length()-1);
                System.out.println(s);
            }
        }
    }
}
