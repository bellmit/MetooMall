package com.metoo.msg.fcm;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

@Component
public class FCMPush {
	
	
	static Message.Builder builder= Message.builder();

    static com.google.firebase.messaging.AndroidConfig.Builder androidBuilder= AndroidConfig.builder();

	// static Notification notice=new Notification("Test title", "Test Content 1");

    static AndroidNotification.Builder androidNotifiBuilder=AndroidNotification.builder();

   public static void main( String[] args ){
//   		FileInputStream serviceAccount;
       try {
    	   //InputStream in = getServletContext().getResourceAsStream("firebase-adminsdk.json"); 
    	   
    	   File f = new File("C:\\language\\java\\project\\metoo-copy\\metoo\\WebRoot\\firebase-adminsdk.json");
           InputStream serviceAccount = new FileInputStream(f);
           FirebaseOptions options = new FirebaseOptions.Builder()
                   .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                   .setDatabaseUrl("https://soarmall.firebaseio.com")
                   .build();

           FirebaseApp.initializeApp(options);
       } catch (Exception e1) {
           // TODO Auto-generated catch block
           e1.printStackTrace();
       }



       androidBuilder.setRestrictedPackageName("plus.H5ED64880");

       androidNotifiBuilder.setColor("#55BEB7");// 设置消息通知颜色
       androidNotifiBuilder.setBody("服务消息通知");// 设置消息内容
       androidNotifiBuilder.setIcon("https://www.server.co/images/favicon.png");// 设置消息图标
       androidNotifiBuilder.setTitle("开发者你好");// 设置消息标题

       AndroidNotification androidNotification=androidNotifiBuilder.build();

       androidBuilder.setNotification(androidNotification);

       AndroidConfig androidConfig=androidBuilder.build();

       builder.setAndroidConfig(androidConfig);

       builder.setToken("eeX6jFOUFv2k:APA91bEy9bBHFE3vVSn6IsjfsOpdXgmD4rr-67viivFHGnLZZvfBzsSgIKjCQpLUEz5D1JkhSCg5DMbLI-oamMnYS_DTNBiTwhkXPV_Jh3bKfrtoVK8ROTnztlvcQK8HiWbuQM2TVQXc");// 客户端申请到的token
       // s30
       //builder.setToken("e5CuMoFZQkq_l8hTY00ff1:APA91bHEA9yjCPQmEPu59bkQOKXvRAgU21sbdeV23R4SdZuVy8RUargmue2-ulqYWFAeE1ydYhdaeJUlMCvHom8HvJn_dSvonX-yJPb8_7TtVaI5U1Vn8UkPObndREYW4ikllMEwu2ca");// 客户端申请到的token


       // builder.setNotification(notice);

       Message message=builder.build();
       try {
           String fcm= FirebaseMessaging.getInstance().send(message);
           System.out.println("FCM：" + fcm);
       }catch(Exception e){
           e.printStackTrace();
       }
   }
	
}
