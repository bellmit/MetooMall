package com.metoo.module.app.view.web.tool;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.SpringUtil;
import com.metoo.foundation.domain.Goods;
import com.metoo.foundation.domain.GoodsWeight;
import com.metoo.foundation.service.IGoodsService;

public class AppGoodsThreadsUtil {

	private static AppGoodsThreadsUtil goodsThreadsUtil = null;
	
	private List<GoodsWeight> goodsWeightList;
	
	public static AppGoodsThreadsUtil getInstance(){
		if(goodsThreadsUtil == null){
			goodsThreadsUtil = new AppGoodsThreadsUtil();
		}
		return goodsThreadsUtil;
	}
	
	public static AppGoodsThreadsUtil getGoodsThreadsUtil() {
		return goodsThreadsUtil;
	}


	public static void setGoodsThreadsUtil(AppGoodsThreadsUtil goodsThreadsUtil) {
		AppGoodsThreadsUtil.goodsThreadsUtil = goodsThreadsUtil;
	}

	public List<GoodsWeight> getGoodsWeightList() {
		return goodsWeightList;
	}


	public void setGoodsWeightList(List<GoodsWeight> goodsWeightList) {
		this.goodsWeightList = goodsWeightList;
	}


	
	public void update_goods_weight(List<Goods> goodsList){
		double weights = 0.0;
		for(Goods goods : goodsList){
			double value = 0.0;
			for (GoodsWeight goodsWeight : goodsWeightList) {
				try {
					Field field = goods.getClass().getDeclaredField(goodsWeight.getDimensionality());
					field.setAccessible(true);
					Object res;
					try {
						res = field.get(goods);
						value += CommUtil.mul(res, goodsWeight.getValue());
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (NoSuchFieldException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			goods.setWeightiness(new BigDecimal(value));
			IGoodsService goodsService = (IGoodsService) SpringUtil.getObject(IGoodsService.class);
			goodsService.update(goods);
		}
	}
}
