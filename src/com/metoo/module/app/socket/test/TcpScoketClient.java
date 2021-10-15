package com.metoo.module.app.socket.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * <p>
 * 	Title: TcpSocketClient.java
 * </p>
 * 
 * <p>
 * 	Desctiption: Socket 实现文件上传
 * </p>
 * 
 * @author 46075
 *
 */
public class TcpScoketClient {
	
	public static void main(String[] args) {
		Socket socket = null;
		OutputStream os = null;
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		
		try {
			// 1，创建一个Socket
			socket = new Socket(InetAddress.getByName("127.0.0.1"), 223);
			// 2, 创建一个输出流
			os = socket.getOutputStream();
			// 3, 读取文件
			fis = new FileInputStream(new File("C:\\Users\\46075\\Pictures\\姗姗\\1610332897(1).jpg"));
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = fis.read(buffer)) != -1){
				os.write(buffer,0 , len);
			}
			// 4，通知服务器我传输完了
			socket.shutdownOutput();
			
			// 5, 确认服务器接收完毕，才断开连接
			InputStream is = socket.getInputStream();
			baos = new ByteArrayOutputStream();
			byte[] buffer2 = new byte[1024];
			int len2 = 0;
			while((len2 = is.read()) != -1){
				baos.write(buffer2);
			}
			System.out.println(baos.toString());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(baos != null){
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
