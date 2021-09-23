package com.metoo.module.app.test;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

/**
 * <p>
 * 	Title: JavaThread.java
 * </p>
 * 
 * <p>
 * 	Description：java高级教程：多线程开发
 *    实现：
 *     通过实现 Runnable 接口；
	      通过继承 Thread 类本身；
	      通过 Callable 和 Future 创建线程。
	   概念：
	        线程同步
		线程间通信
		线程死锁
		线程控制：挂起、停止和恢复
		sleep(); 在指定的毫秒数内让当前正在执行的线程休眠; yield();暂停当前正在执行的线程对象; join();等待该线程终止的时间最长为 millis 毫秒;daemon();守护线程;
 * </p>
 * @author 46075
 *
 */
public class JavaJUC {


	/*// synchronized 实现同步
	public static void main(String[] args) {
		ArrayList list = new ArrayList();
		for (int i = 0; i < 10000; i++) {
//			new Thread(()->{
//				list.add(Thread.currentThread().getName());
//			}).start();
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					synchronized(list){
						list.add(Thread.currentThread().getName());
					}
				}
			});
			thread.start();
		}
		try {
			// 使当前主线程，等待for循环内的所有子线程执行完毕；否则可能会导致子线程没有执行完，主线程就结束了
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(list.size());
	}
	
	@Test
	public void copyOnWriteArrayList() throws InterruptedException{
		CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>();
		for (int i = 0; i < 10000; i++) {
			new Thread(()->{
				list.add(Thread.currentThread().getName());
			}).start();
		}
		
		Thread.sleep(3000);
		System.out.println(list.size());
	}
	

	
class TestLock implements Runnable{

	private int number = 10;
	
	private final ReentrantLock reentranLock = new ReentrantLock();
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				reentranLock.lock();
				if(number > 0){
					
					System.out.println(number--);
				}else{
					break;
					
				}
			} finally {
				// TODO Auto-generated catch block
				reentranLock.unlock();
			}
		}
	}
}

	@Test
	public void lock(){
		TestLock testLock = new TestLock();
		new Thread(testLock).start();
		new Thread(testLock).start();
	}*/
}
