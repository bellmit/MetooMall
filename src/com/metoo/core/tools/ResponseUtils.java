package com.metoo.core.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metoo.module.app.buyer.domain.Result;

/**
 * HttpServletResponse帮助类
 * 
 * @author lzb
 * 
 */
public final class ResponseUtils {
	public static final Logger log = LoggerFactory
			.getLogger(ResponseUtils.class);

	/**
	 * 发送文本。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderText(HttpServletResponse response, String text) {
		render(response, "text/plain;charset=UTF-8", text);
	}



	/**
	 * 发送json。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderJson(HttpServletResponse response, String text) {
		render(response, "application/json;charset=UTF-8", text);
	}

	/**
	 * 发送xml。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderXml(HttpServletResponse response, String text) {
		render(response, "text/xml;charset=UTF-8", text);
	}

	/**
	 * 发送内容。使用UTF-8编码。
	 * 
	 * @param response
	 * @param contentType
	 * @param text
	 */
	public static void render(HttpServletResponse response, String contentType,
			String text) {
		response.setContentType(contentType);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public static Object ok(){
		return new Result(4200, "Success");
	}
	
	public static Object ok(Object data){
		return new Result(4200, "Success", data);
	}
	
	 public static Object badArgumentValue() {
	        return new Result(4402, "Parameter error");
	}
	 
	 public static Object badArgument(String message) { return fail(4400, message);} //未找到指定资源
	 
	 public static Object badArgument(Integer code, String message) { return fail(code, message);} //未找到指定资源
	 
	 public static Object badArgument(Integer code, String message, Object data) { return new Result(code, message, data);} //未找到指定资源


	 public static Object fail(Integer errno, String errmsg) {
	       
	        return new Result(errno, errmsg);
	}
	 
	 public static Object unlogin() {
	        return new Result(-100, "Log in");
	    }
	 
}
