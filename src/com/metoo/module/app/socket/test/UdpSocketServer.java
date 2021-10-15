package com.metoo.module.app.socket.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpSocketServer {

	
	public static void main(String[] args) {

		DatagramSocket ds = null;
		DatagramPacket dp = null;
		
		try {
			// 1, 开放端口
			ds = new DatagramSocket(1025);
			// 2, 接收数据包
			byte[] buf = new byte[1024];
			 
			dp = new DatagramPacket(buf, 0,buf.length);
			
			try {
				ds.receive(dp);
				
				System.out.println(new String(dp.getData(), 0, dp.getLength()));
				System.out.println(dp.getData().toString());
				ds.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
