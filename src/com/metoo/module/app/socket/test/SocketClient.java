package com.metoo.module.app.socket.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {

	public static void main(String[] args) {

		Socket socket= null;
		OutputStream os = null;
		try {
			// 1，获取服务器IP 端口
			InetAddress serverIP = InetAddress.getByName("127.0.0.1");
			int port = 2222;
			// 2，创建一个socket连接
			try {
				socket = new Socket(serverIP, port);
				// 发送消息 IO流
				os = socket.getOutputStream();
				os.write("Test socket send msg".getBytes());
				os.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
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

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
