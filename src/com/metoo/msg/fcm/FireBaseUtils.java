package com.metoo.msg.fcm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.metoo.module.app.pojo.Notification;

@Component
public class FireBaseUtils {

	private static Map<String, FirebaseApp> fireBaseAppMap = new ConcurrentHashMap<>();
	

	
	com.google.firebase.messaging.AndroidConfig.Builder androidBuilder= AndroidConfig.builder();
	
	static AndroidNotification.Builder androidNotifiBuilder = AndroidNotification.builder();

	// 判断SDK否初始化
	public boolean isInit(String appName) {
	
		return fireBaseAppMap.get(appName) != null;
	}

	// 初始化SDK
	public void initSDK(File file, Notification notification) {

		InputStream serviceAccount;
		try {
			serviceAccount = new FileInputStream(file);
			FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl(notification.getDataBaseUrl()).build();
			 FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
			
			fireBaseAppMap.put(notification.getAppName(), firebaseApp);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 单台设备推送
	 * @param notification
	 */
	public String pushSigle(Notification notification){
		if(notification != null){
			
			 //获取实例
	        FirebaseApp firebaseApp = fireBaseAppMap.get(notification.getAppName());
	        
	        //实例为空的情况
	        if (firebaseApp == null) {
	            return "";
	        }
	        
	        //构建消息体
	        Message.Builder builder= Message.builder();
	        
	        androidNotifiBuilder.setIcon("");
	        androidNotifiBuilder.setColor(notification.getColor());
	        androidNotifiBuilder.setTitle(notification.getTitle());
	        androidNotifiBuilder.setBody(notification.getBody());
	      
	        AndroidNotification androidNotification = androidNotifiBuilder.build();
	        androidBuilder.setNotification(androidNotification);
	        
	        AndroidConfig androidConfig=androidBuilder.build();
	        
	        builder.setAndroidConfig(androidConfig);
	        
	        builder.setToken(notification.getToken());
	        
	        System.out.println(notification.getMap());
	        
	        // builder.putAllData(notification.getMap());
	        
	        // builder.putData("params", notification.getMap().toString());
	        
	        Message message=builder.build();
	        
	        try {
	        	String response = FirebaseMessaging.getInstance().send(message);
				return "Successfully";
			} catch (FirebaseMessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return e.getMessage();
			}
		}
		return null;
	}
}
