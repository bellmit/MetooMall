package com.metoo.module.app.view.web.tool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.metoo.core.tools.CommUtil;

@Component
public class AppRegisterViewTools {
	
	// 验证用户邮箱
	public boolean verify_email(String email){
		Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = pattern.matcher(CommUtil.null2String(email));
		if(matcher.matches()){
			return true;
		}else{
			return false;
		}
	}

}
