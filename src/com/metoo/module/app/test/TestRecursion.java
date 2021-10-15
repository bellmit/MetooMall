package com.metoo.module.app.test;


/**
 * <p>
 * 	Title: TestRecursion.java
 * </p>
 * 
 * <p>
 * 	Description: 测试递归 1-n之间的和
 * </p>
 * 
 * @author 46075
 *
 */
public class TestRecursion {
	
	public static void main(String[] args) {
		int s = sum(3);
		System.out.println(s);
	}
 
	/**
	 * 定义一个方法，使用递归计算1-n之间的和
	 *  1+2+3+..n
	 *  n+(n-1) +(n-1)+..1;
	 *  已知 最大值n  最小值1
	 *  使用递归必需明确
	 *  	1.递归的结束条件  获取到1的时候结束
	 *  	2.递归的目标  获取下一个被加的数字(n-1)
	 *  
	 *  学习视频分享:javacto.taobao.com
	 */
	private static int sum(int n) {
		//获取到1的时候
		if(n==1){
			return 1;
		}
		//获取下一个被加的数字 (n-1)
		return n+sum(n-1);
	}

}
