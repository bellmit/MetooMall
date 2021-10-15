package com.metoo.modul.app.desig.pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.OrderForm;
import com.metoo.foundation.domain.Store;
import com.metoo.foundation.service.IStoreService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.manage.admin.tools.OrderFormTools;

/**
 * <p>
 * Title: ConsolidatedOrder.java
 * </p>
 * <p>
 * Description: 多店铺订单商品属性封装
 * </p>
 * 
 * @author 46075
 *
 */
@Component
public class ConsolidatedOrderForm implements BaseOrderForm {

	@Autowired
	private IStoreService storeService;
	@Autowired
	private OrderFormTools orderFormTools;

	@Override
	public List<Map<String, Object>> encapsulation(List<OrderForm> instances) {
		List<Map<String, Object>> list = null;
		if (instances != null && instances.size() > 0) {
			list = new ArrayList<Map<String, Object>>();
			for (OrderForm obj : instances) {
				Map<String, Object> map = new HashMap<String, Object>();
				Store store = this.storeService.getObjById(CommUtil.null2Long(obj.getStore_id()));
				map.put("sotre_id", store.getId());
				map.put("sotre_name", store.getStore_name());
				map.put("sotre_logo", store.getStore_logo() != null
						? store.getStore_logo().getPath() + "/" + store.getStore_logo().getName() : "");
				map.put("goods", this.orderFormTools.queryGoodsInfo(obj.getGoods_info()));
				map.put("order_id", obj.getId());
				map.put("order_number", obj.getOrder_id());
				map.put("order_status", obj.getOrder_status());
				map.put("childOrder", this.orderFormTools.queryGoodsInfo(obj.getChild_order_detail()));
				map.put("payment_amount", obj.getPayment_amount());
				list.add(map);
			}
		}
		return list;
	}

}
