package com.metoo.manage.admin.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.Goods;
import com.metoo.foundation.domain.OrderForm;
import com.metoo.foundation.domain.Payment;
import com.metoo.foundation.service.IGoodsService;
import com.metoo.foundation.service.IOrderFormService;
import com.metoo.foundation.service.IPaymentService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.IUserService;
import com.metoo.module.app.view.web.tool.AppCartViewTools;

/**
 * 
 * <p>
 * Title: PaymentTools.java
 * </p>
 * 
 * <p>
 * Description: 支付方式处理工具类，用来管理支付方式信息，主要包括查询支付方式等
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * 
 * <p>
 * Company: 沈阳网之商科技有限公司 www.koala.com
 * </p>
 * 
 * @author erikzhang
 * 
 * @date 2014-5-25
 * 
 * @version koala_b2b2c v2.0 2015版
 */
@Component
public class PaymentTools {
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private IUserService userService;
	private IOrderFormService orderService;
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private AppCartViewTools appCartViewTools;

	public boolean queryPayment(String mark) {
		Map params = new HashMap();
		params.put("mark", mark);
		List<Payment> objs = this.paymentService.query(
				"select obj from Payment obj where obj.mark=:mark", params, -1,
				-1);
		if (objs.size() > 0) {
			// System.out.println(objs.get(0).isInstall());
			return objs.get(0).isInstall();
		} else
			return false;
	}

	public Map queryShopPayment(String mark) {
		Map ret = new HashMap();
		Map params = new HashMap();
		params.put("mark", mark);
		List<Payment> objs = this.paymentService.query(
				"select obj from Payment obj where obj.mark=:mark", params, -1,
				-1);
		if (objs.size() == 1) {
			ret.put("install", objs.get(0).isInstall());
			ret.put("content", objs.get(0).getContent());
		} else {
			ret.put("install", false);
			ret.put("content", "");
		}
		return ret;
	}
	
	 public List<Map> queryJsonInfo(String json) {

			List<Map> map_list = new ArrayList<Map>();
			if (json != null && !json.equals("")) {
				map_list = Json.fromJson(ArrayList.class, json);
			}
			return map_list;
		}
	    
		public String verifyGoodsInventory(OrderForm orderForm){
			String goodsInfo = orderForm.getGoods_info();
			if(goodsInfo != null && !goodsInfo.equals("")){
				List<Map> map_list = Json.fromJson(ArrayList.class, goodsInfo);
				if(map_list.size() > 0){
					if(orderForm.getOrder_main() == 1){
						List<Map> maps = this.queryJsonInfo(orderForm.getChild_order_detail());
						if(maps.size() > 0){
							for (Map map : maps) {
								OrderForm child_order = this.orderService.getObjById(CommUtil.null2Long(map.get("order_id")));
								map_list.addAll(Json.fromJson(ArrayList.class, child_order.getGoods_info()));
							}
						}
					}
					Map itemMap = null;
					for(Map map : map_list){
						String goods_id = map.get("goods_id").toString();
						Goods obj = this.goodsService.getObjById(CommUtil.null2Long(goods_id));
						if(obj != null){
							int inventory = 0;
							if (obj.getInventory_type().equals("all")) {
								inventory = obj.getGoods_inventory();
							} else {
								String goods_color = map.get("goods_color").toString();
								String gsp = map.get("goods_gsp_ids").toString();
								Map<String, Object> goods = this.appCartViewTools.generic_default_info_color(obj, gsp,
										goods_color);
								inventory = CommUtil.null2Int(goods.get("count"));
							}
							
							int goods_count = CommUtil.null2Int(map.get("goods_count"));
							if(inventory < goods_count){
								itemMap = new HashMap();
								itemMap.put("goods_id", obj.getId());
								itemMap.put("goods_name", obj.getGoods_name());
								itemMap.put("goods_main_photo", obj.getGoods_main_photo() != null ? this.configService.getSysConfig().getImageWebServer()
										+ obj.getGoods_main_photo().getPath() + "/" + obj.getGoods_main_photo().getName() : "");
								break;
							}
						}
					}
					if(itemMap != null){
						return JSON.toJSONString(itemMap);
					}
					return null;
				}
			}
			return null;
		}
}
