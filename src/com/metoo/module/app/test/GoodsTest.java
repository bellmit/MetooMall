package com.metoo.module.app.test;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.GoodsSku;
import com.metoo.foundation.domain.Evaluate;
import com.metoo.foundation.domain.Goods;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IEvaluateService;
import com.metoo.foundation.service.IGoodsService;
import com.metoo.foundation.service.IStoreService;


@Controller
public class GoodsTest {


	@Autowired
	private IStoreService storeService;
	@Autowired
	private IEvaluateService evaluateService;
	@Autowired
	private IGoodsService goodsService;
	
	/**
	 * 商品价格and折扣率修改
	 * @param request
	 * @param response
	 * @param name
	 */
	@RequestMapping("price.json")
	public void test(HttpServletRequest request,
			HttpServletResponse response, String name){
		Map params = new HashMap();
		params.put("goods_status", 0);
		params.put("store_name", name);
		params.put("store_status", 15);
		List<Goods> goodsList = this.goodsService.query("select obj from Goods obj where obj.goods_status=:goods_status"
				+ " and obj.goods_store.store_name=:store_name and obj.goods_store.store_status=:store_status", params, -1, -1);
		/*params.put("goods_status", 0);
		List<Goods> goodsList = this.goodsService.query("select obj from Goods obj where obj.goods_status=:goods_status", params, -1, -1);*/
		if(goodsList != null){
			for(Goods obj :  goodsList){
				if(obj.getGoodsSkuList().size() > 0){
					List<GoodsSku> GoodsSkuList = obj.getGoodsSkuList();
					List goods_price_list = new ArrayList();
					List goods_current_price_list = new ArrayList();
					for(GoodsSku goodsSku : GoodsSkuList){
						goods_price_list.add(goodsSku.getGoods_price());
						goods_current_price_list.add(goodsSku.getDiscount_price());
					}
					BigDecimal goods_price = CommUtil.null2BigDecimal(Collections.min(goods_price_list));
					BigDecimal store_price = CommUtil.null2BigDecimal(Collections.min(goods_current_price_list));
					double subtract = CommUtil.subtract(goods_price, store_price);
					double div = CommUtil.div(subtract, goods_price);
					double mul = CommUtil.mul(div, 100);
					BigDecimal e = new BigDecimal(mul);
					if(obj.getGoods_price() == null ){
						obj.setGoods_price(goods_price);
					}
					if(obj.getGoods_current_price() == null || obj.getStore_price() == null){
						obj.setGoods_current_price(store_price);
						obj.setStore_price(store_price);
					}
					obj.setGoods_discount_rate(e);
					this.goodsService.update(obj);
				}else{
					BigDecimal goods_price = obj.getGoods_price();
					BigDecimal store_price = obj.getGoods_current_price();
					double subtract = CommUtil.subtract(goods_price, store_price);
					double div = CommUtil.div(subtract, goods_price);
					double mul = CommUtil.mul(div, 100);
					BigDecimal e = new BigDecimal(mul);
					obj.setGoods_discount_rate(e);
					this.goodsService.update(obj);
				}
			}
		}
	}
	
	@RequestMapping("/weightiness.json")
	public void timeGoods(){
		double weight = 0.0;
		Goods goods = this.goodsService.getObjById(Long.parseLong("4204"));
		System.out.println(goods.getId());
		//商品权重计算
		double well_weight = CommUtil.mul(goods.getWell_evaluate(), "0.08"); // 星级率
		weight += well_weight;
		double discount = CommUtil.mul(goods.getGoods_discount_rate(), 0.005); // 折扣率
		weight += discount;
		/*double inventory = CommUtil.mul(goods.getGoods_inventory(), 0.01);//库存
		weight += inventory;
		System.out.println(inventory);*/
		
		double transport = 0.0;//运费类型
		switch (goods.getGoods_store().getGrade().getGradeName()) {
		
		case "Local": transport = CommUtil.mul(1, 0.05);
					weight += transport;
			break;
		case "FBS": transport = CommUtil.mul(2, 0.05);
					weight += transport;
			break;
		
		default:
				transport = CommUtil.mul(0, 0.05);
				weight += transport;
			break;
		}
		double transportfee = 0.0;//承担运费方式
		if(goods.getGoods_transfee() == 0){
			transportfee = CommUtil.mul(1, 0.05);
		}else{
			transportfee = CommUtil.mul(2, 0.05);
		}
		weight += transportfee;
		
		double div = CommUtil.div(goods.getGoods_collect(), goods.getGoods_click());
		double collect_click = CommUtil.mul(div, 0.05);//收藏/浏览
		weight += collect_click;
		Map params = new HashMap();
		params.clear();
		params.put("evaluate_goods_id", goods.getId());
		List<Evaluate> eva_list = this.evaluateService
				.query("select obj from Evaluate obj where obj.evaluate_goods.id=:evaluate_goods_id",
						params, -1, -1);
		double div1 = CommUtil.div(eva_list.size(), goods.getGoods_salenum());//评论率
		double evaluate_salenum = CommUtil.mul(div1, 0.05);
		weight += evaluate_salenum;
		
		double goods_type = 0.0;//商品类型
		if(goods.getGoods_type() == 0){
			goods_type = CommUtil.mul(2, 0.08);
		}else{
			goods_type = CommUtil.mul(1, 0.08);
		}
		weight += goods_type;
		
		double goods_enough = 0.0;//是否参加满减
		if(goods.getEnough_reduce() == 0){
			goods_enough = CommUtil.mul(0, 0.03);
		}else{
			goods_enough = CommUtil.mul(1, 0.03);
		}
		weight += goods_enough;
		
		double enough_free = 0.0;// 是否参满包邮
		if(goods.getEnough_free() == 0){
			enough_free = CommUtil.mul(0, 0.03);
		}else{
			enough_free = CommUtil.mul(1, 0.03);
		}
		weight += enough_free;
		
		double order_enough_give_status = 0.0;// 是否参满就送
		if(goods.getOrder_enough_give_status() == 0){
			order_enough_give_status = CommUtil.mul(0, 0.03);
		}else{
			order_enough_give_status = CommUtil.mul(1, 0.03);
		}
		weight += order_enough_give_status;
		
		if(goods.getCarts().size() > 0){// 加购数/浏览数 
			double  div2 = CommUtil.div(goods.getCarts().size(), goods.getGoods_click());
			double cart_click = CommUtil.mul(div2, 0.03);
			weight += cart_click;
		}
		if(goods.getCarts().size() > 0){// 售出数量/浏览数 
			double div3 = CommUtil.div(goods.getGoods_salenum(), goods.getGoods_click());
			double salenum_click = CommUtil.mul(div3, 0.03);
			weight += salenum_click;
		}
		
		/*if(goods.getGoods_brand() == null ){ // brand
			 CommUtil.mul(, 0.05);
		}else{
			
		}*/
		
		//CommUtil.mul(goods.getGoods_brand() != null ? , 0.01);
		goods.setWeightiness(CommUtil.null2BigDecimal(weight));
		this.goodsService.update(goods);
	}
	
	@RequestMapping("/getGoods.json")
	@ResponseBody
	public String getGoods(HttpServletRequest request, HttpServletResponse respon) throws InterruptedException{
		Goods obj = this.goodsService.getObjById(CommUtil.null2Long(6630));
		Thread.sleep(7000);
		return Json.toJson(obj.getId());
	}
	
	@RequestMapping("/goodsProperties.json")
	@ResponseBody
	public String goodsProperties(HttpServletRequest request, HttpServletResponse respon, String goods_id) throws InterruptedException{
		Goods obj = this.goodsService.getObjById(CommUtil.null2Long(goods_id));
		Map map = new HashMap();
		map.put("goods_salenum", obj.getGoods_salenum());
		map.put("goods_price", obj.getGoods_price());
		map.put("goods_current_price", obj.getGoods_current_price());
		map.put("goods_weight", obj.getGoods_weight());
		map.put("goods_transfee", obj.getGoods_transfee());
		map.put("goods_click", obj.getGoods_click());
		map.put("goods_collect", obj.getGoods_collect());
		map.put("well_evaluate", obj.getWell_evaluate());
		map.put("middle_evaluate", obj.getMiddle_evaluate());
		map.put("bad_evaluate", obj.getBad_evaluate());
		map.put("enough_reduce", obj.getEnough_reduce());
		map.put("goods_discount_rate", obj.getGoods_discount_rate());
		map.put("weightiness", obj.getWeightiness());
		return JSONObject.toJSONString(map);
	}
	
	public static void main(String[] args) {
        Integer a = new Integer(3);
        Integer b = 3;                  // 将3自动装箱成Integer类型
        int c = 3;
        Integer d = new Integer(4);
        Integer e = 100, f=100;
        Integer g = 200, h=100;
        Integer i = new Integer(3);
        System.out.println(a == b);     // false 两个引用没有引用同一对象
        System.out.println(a == c);     
        System.out.println(b == c);     
        System.out.println(a == d);     
        System.out.println(e == f);
        System.out.println(g == h);
        System.out.println(a == i);
        System.out.println(a.equals(i));
    }
}

