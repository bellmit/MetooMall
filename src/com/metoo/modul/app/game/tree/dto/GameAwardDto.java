package com.metoo.modul.app.game.tree.dto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.metoo.foundation.domain.Coupon;
import com.metoo.foundation.domain.Goods;
import com.metoo.foundation.domain.SubTrees;

public class GameAwardDto {
	
	private Long id;
	
	private String water;// 水滴数 例如：{"50","10","100":"20"}根据订单金额选择奖励水滴数
	
	private Long coupon_id;// 奖品对应的优惠券对象
	
	private int type;// 奖品类型 0：种树升级奖品 1: 随机奖励 2：满级奖品 
	
	private Long subTree_id;// 每一阶段树奖励
	
	private Long goods_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWater() {
		return water;
	}

	public void setWater(String water) {
		this.water = water;
	}

	public Long getCoupon_id() {
		return coupon_id;
	}

	public void setCoupon_id(Long coupon_id) {
		this.coupon_id = coupon_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getSubTree_id() {
		return subTree_id;
	}

	public void setSubTree_id(Long subTree_id) {
		this.subTree_id = subTree_id;
	}

	public Long getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Long goods_id) {
		this.goods_id = goods_id;
	}
	
	

}
