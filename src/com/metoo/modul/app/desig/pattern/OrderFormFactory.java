package com.metoo.modul.app.desig.pattern;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metoo.foundation.domain.OrderForm;
import com.metoo.foundation.service.IStoreService;
import com.metoo.manage.admin.tools.OrderFormTools;

@Component
public class OrderFormFactory {
	
	@Autowired
	private IStoreService storeService;
	@Autowired
	private OrderFormTools orderFormTools;
	@Autowired
	private ConsolidatedOrderForm consolidatedOrderForm;

	public List<Map<String, Object>> getGoods(List<OrderForm> instances, String type){
		if(type == null){
			return null;
		}
		if(type.equalsIgnoreCase("consolidated")){
			return this.consolidatedOrderForm().encapsulation(instances);
		}else if(type.equalsIgnoreCase("separate")){
			return new SeparateOrderForm().encapsulation(instances);
		}
		return null;
		
	}

	private ConsolidatedOrderForm consolidatedOrderForm() {
		// TODO Auto-generated method stub
		return null;
	}
}
