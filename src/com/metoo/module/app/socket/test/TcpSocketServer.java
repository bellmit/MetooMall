package com.metoo.module.app.socket.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpSocketServer {
	
	public static void main(String[] args) {

		// TODO Auto-generated method stub
		ServerSocket serverSocket = null;
		Socket socket = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			// 1，创建服务
			serverSocket = new ServerSocket(223);
			// 2, 监听客户端连接
			socket = serverSocket.accept();// 阻塞监听，会一直等待客户端连接
			
			// 3，获取输入流
			is = socket.getInputStream();
			
			// 4, 输出文件
			fos = new FileOutputStream(new File("C:\\Users\\46075\\Pictures\\socket\\1610332897.jpg"));
			
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = is.read(buffer)) != -1){
				fos.write(buffer, 0 , len);
			}
			// 5, 通知客户端我接收完了
			OutputStream os = socket.getOutputStream();
			os.write("Test socket file upload".getBytes());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			// 关闭资源
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(socket != null ){
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(serverSocket != null){
				try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	
	}
}
