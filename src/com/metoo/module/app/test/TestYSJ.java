package com.metoo.module.app.test;

import org.junit.Test;

public class TestYSJ implements Runnable{
	
	private int i = 10;
	


	@Override
	public void run() {
		
		while(true){
			if(i<=0){
				break;
			}
			System.out.println(Thread.currentThread().getName() + i--);
		}
	}
	
	public static void main(String[] args) {
		TestYSJ ysj = new TestYSJ();
		new Thread(ysj, "ysj-->").start();
		new Thread(ysj, "hkk-->").start();
		new Thread(ysj, "lv-->").start();
		new Thread(ysj, "hu-->").start();
		new Thread(ysj, "kkk-->").start();
		new Thread(ysj, "hh-->").start();
	}
}
