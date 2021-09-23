package com.metoo.msg.fcm;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;

/**
 * <p>
 * 	Title: NotificationAction.java
 * </p>
 * 
 * <p>
 * 	Description: FCM个推
 * </p>
 * 
 * @author 46075
 *
 */
@Controller
@RequestMapping("/push")
public class NotificationAction {


	@RequestMapping("/notification.json")
	@ResponseBody
	public void pushNotifacation(HttpServletRequest request, HttpServletResponse response){
		String url = request.getSession().getServletContext().getRealPath("firebase-adminsdk.json");
		InputStream in = request.getSession().getServletContext().getResourceAsStream("firebase-adminsdk.json");  
		File file = new File(url);
		
		Map<String, Object> params = new HashMap<String, Object>();
		notification(params,file);
	}

	public void notification(Map<String, Object> params, File file) {
		
		Message.Builder builder = Message.builder();
		
		com.google.firebase.messaging.AndroidConfig.Builder androidBuilder = AndroidConfig.builder();
		
		AndroidNotification.Builder androidNotifiBuilder = AndroidNotification.builder();
		
		Notification notice = new Notification("Test title", "Test Content 1");
		 
		params.put("data", "Test content");
		params.put("title", "Test title");
		params.put("token", "d16Lh5YGQsQ:APA91bEpHST-UVpnomNT6DESnnMyEaPfPSlsIRcNUELRzLoqGHklZdlSj5_Trkxy1VH0UsZQNcr9SiEuYPYpPtPZZLVRkrmoC7nlEC2Q3u4Sovys_zW8U1X7xfLf6gx_SKE47-j67qdq");
		try {
			// String url = request.getSession().getServletContext().getRealPath("firebase-adminsdk.json");
			// InputStream serviceAccount = new ClassPathResource("firebase-adminsdk.json").getInputStream();
			
		
			InputStream serviceAccount = new FileInputStream(file);
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://soarmall.firebaseio.com").build();

					
			FirebaseApp.initializeApp(options);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		androidBuilder.setRestrictedPackageName("plus.H5ED64880");
		
		/*for(Object key : params.keySet()){
			androidNotifiBuilder.setColor("#55BEB7");// 设置消息通知颜色
			androidNotifiBuilder.setBody(params.get("data").toString());// 设置消息内容
			androidNotifiBuilder.setIcon("https://www.server.co/images/favicon.png");// 设置消息图标
			androidNotifiBuilder.setTitle(params.get("title").toString());// 设置消息标题
			builder.setToken(params.get("token").toString());// 客户端申请到的token
			
		}*/
		androidNotifiBuilder.setColor("#55BEB7");// 设置消息通知颜色
		androidNotifiBuilder.setBody("Text Context");// 设置消息内容
		androidNotifiBuilder.setIcon("https://www.server.co/images/favicon.png");// 设置消息图标
		androidNotifiBuilder.setTitle("Text Title");// 设置消息标题
		builder.setToken("d16Lh5YGQsQ:APA91bEpHST-UVpnomNT6DESnnMyEaPfPSlsIRcNUELRzLoqGHklZdlSj5_Trkxy1VH0UsZQNcr9SiEuYPYpPtPZZLVRkrmoC7nlEC2Q3u4Sovys_zW8U1X7xfLf6gx_SKE47-j67qdq");// 客户端申请到的token
		

		AndroidNotification androidNotification = androidNotifiBuilder.build();

		androidBuilder.setNotification(androidNotification);

		AndroidConfig androidConfig = androidBuilder.build();

		builder.setAndroidConfig(androidConfig);

		builder.putData("order", "1");
		
		Message message = builder.build();
		try {
			String fcm = FirebaseMessaging.getInstance().send(message);
			System.out.println("FCM：" + fcm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
