package com.metoo.modul.app.desig.pattern;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.metoo.foundation.domain.OrderForm;

/**
 * <p>
 * 	Title: GoodsProperties.java
 * </p>
 * <p>
 * 	Description: 初用工厂模式; 根据不同类型数据封装商品信息;
 * </p>
 * @author 46075
 *
 */
@Repository
public interface BaseOrderForm {
	
	List encapsulation(List<OrderForm> orders);
}
