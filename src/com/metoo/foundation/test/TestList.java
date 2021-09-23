package com.metoo.foundation.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.nutz.lang.util.LinkedArray;

public class TestList {

	public static void main(String[] args) {
	/*	List list = new ArrayList();
		list.add(1);
		list.add(2);
		list.add(3);
		if(list.indexOf(9) > 0 ){
			System.out.println(list.indexOf(3));;
		}
		System.out.println("没有获取当前对象");*/
		
		LinkedList linked = new LinkedList();
		linked.add(1);
		linked.add(2);
		linked.add(3);
		linked.poll();
		//linked.push(4);
		linked.pop();
		System.out.println(linked);
	}
}
