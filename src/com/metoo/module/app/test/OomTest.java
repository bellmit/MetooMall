package com.metoo.module.app.test;

/**
 * 测试强引用、软引用、弱引用、虚引用
 * @author 46075
 *
 */
public class OomTest {

	public static void main(String[] args) {
		Object object = new Object();
		Object[] objArray = new Object[Integer.MAX_VALUE];
	}
}
