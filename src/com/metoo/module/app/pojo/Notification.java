package com.metoo.module.app.pojo;

import java.util.HashMap;

import javax.persistence.OneToOne;

import com.metoo.foundation.domain.Accessory;

/**
 * 
 *<p>
 *	Title: Notification.java
 *</p>
 *
 *<p>
 * Description: Firebase 消息推送
 *</p>
 * @author 46075
 *
 */
public class Notification {

	private String title;// Sets the title of the notification
	
	private String body;// Sets the body of the notification
	
	private String imageUrl;// Sets the URL of the image that is going to be displayed in the notification
	
	private String color;// 通知颜色 #a38fa4 #8fa4a3
	
	private String token;// 用户身份令牌
	
	private String appName;// 应用名称.
	
	private String dataBaseUrl;// 数据库 URL
	
	private int type;
	
	private HashMap<String, Object> map;// 消息推送数据线
	
	@OneToOne
	private Accessory icon;// 通知消息图标

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public HashMap getMap() {
		return map;
	}

	public void setMap(HashMap map) {
		this.map = map;
	}

	public Accessory getIcon() {
		return icon;
	}

	public void setIcon(Accessory icon) {
		this.icon = icon;
	}

	public String getDataBaseUrl() {
		return dataBaseUrl;
	}

	public void setDataBaseUrl(String dataBaseUrl) {
		this.dataBaseUrl = dataBaseUrl;
	}

	
}
