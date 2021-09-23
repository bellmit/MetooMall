package com.metoo.modul.integration.baiwan;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IUserService;

@RequestMapping("/baiwan/")
@Controller
public class BaiwanNotificationAction {

	@Autowired
	private IUserService userService;
	
	@RequestMapping("/notification.json")
	@ResponseBody
	public void notification(HttpServletRequest request){
		User user = this.userService.getObjById(Long.parseLong("5418"));
		
		Enumeration<String> en = request.getParameterNames();
     	String str = "baiwan";
        while (en.hasMoreElements()) {
             String paramName = en.nextElement();
             String paramValue = request.getParameter(paramName);
             //此处的编码一定要和自己的网站编码一致，不然会出现乱码，paypal回复的通知为‘INVALID’
             try {
				str = str + "&" + paramName + "=" + URLEncoder.encode(paramValue, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
        user.setFirebase_token(str);
        this.userService.update(user);
	}
}
