package com.metoo.module.app.socket.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpSocketClient {

	public static void main(String[] args) {
		DatagramSocket dgSocket = null;
		DatagramPacket dataGramPacket = null;
		
		try {
			// 1, 建立一个Scoket
			DatagramSocket ds = new DatagramSocket();
			
			// 2, 建立包
			// 2-1, 创建消息数据
			String msg = "测试UDP发送消息";
			try {
				InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
				dataGramPacket = new DatagramPacket(msg.getBytes(), 0, msg.getBytes().length, inetAddress, 1025);
				try {
					ds.send(dataGramPacket);
					
					ds.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			// 3，发送包
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
