package com.metoo.modul.app.desig.pattern;

import java.util.List;

import com.metoo.foundation.domain.OrderForm;

/**
 * <p>
 * 	Title: SeparateOrder.java
 * </p>
 * 
 * <p>
 * 	Description: 拆单商品属性封装
 * </p>
 * @author 46075
 *
 */
public class SeparateOrderForm implements BaseOrderForm{

	@Override
	public List encapsulation(List<OrderForm> instances) {
		return instances;
		
	}

}
