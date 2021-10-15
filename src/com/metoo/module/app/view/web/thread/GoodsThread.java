package com.metoo.module.app.view.web.thread;


import java.util.List;

import com.metoo.foundation.domain.Goods;
import com.metoo.foundation.domain.GoodsWeight;
import com.metoo.module.app.view.web.tool.AppGoodsThreadsUtil;

/**
 * <p>
 * 	Title: GoodsThread.java
 * </p>
 * 
 * <p>
 * 	Description: 暂时用来计算商品权重，每个店铺开一线程执行更新商品权重
 * </p>
 * @author 46075
 *
 */
public class GoodsThread implements Runnable {

	private List<Goods> goodsList;

	public GoodsThread(List<Goods> goodsList) {
		super();
		this.goodsList = goodsList;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		AppGoodsThreadsUtil instance = AppGoodsThreadsUtil.getInstance();
		if(goodsList.size() > 0){
			instance.update_goods_weight(goodsList);
		}
	}

}
