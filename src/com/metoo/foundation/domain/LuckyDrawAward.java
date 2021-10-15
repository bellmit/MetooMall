package com.metoo.foundation.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

/**
 * <p>
 * Title: LuckyDrawAward.java
 * </p>
 * 
 * <p>
 * Descrption:	抽奖奖品管理类；可优化（此类由php设计，可优化至Game、GameAward）
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * 
 * <p>
 * Company: 湖南觅通科技有限公司
 * </p>
 * 
 * @author Administrator
 *
 * @date 2021-10-14
 * 
 * @version metoomall 1.0
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "lucky_draw_award")
public class LuckyDrawAward extends IdEntity{

	private String name;// 奖品名称
	
	private String message;// 奖品描述
	
	@Column(columnDefinition = "int default 0")
	private int type;// 奖品类型 0：空奖 1：sar 2：水滴 3：随机碎片
	
	@OneToOne(cascade = CascadeType.REMOVE)
	private GoodsVoucher goods_voucher;// 商品抵用券
	
	private int water;// 水滴 {"50water":"0", "10water":"10", "5water":"30"}
	
	private BigDecimal probably;// 中奖概率
	
	// 随机碎片
	@OneToMany(mappedBy = "lucky_draw_award", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<GameGoods> game_goods_list = new ArrayList<GameGoods>();

	@ManyToOne(fetch = FetchType.LAZY)
	private LuckyDraw lucky_draw;// 抽奖管理类
	
	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Accessory photo;// 奖品图片
	
	private int level;// 奖品等级
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public GoodsVoucher getGoods_voucher() {
		return goods_voucher;
	}

	public void setGoods_voucher(GoodsVoucher goods_voucher) {
		this.goods_voucher = goods_voucher;
	}

	public int getWater() {
		return water;
	}

	public void setWater(int water) {
		this.water = water;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public BigDecimal getProbably() {
		return probably;
	}

	public void setProbably(BigDecimal probably) {
		this.probably = probably;
	}

	public List<GameGoods> getGame_goods_list() {
		return game_goods_list;
	}

	public void setGame_goods_list(List<GameGoods> game_goods_list) {
		this.game_goods_list = game_goods_list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LuckyDraw getLucky_draw() {
		return lucky_draw;
	}

	public void setLucky_draw(LuckyDraw lucky_draw) {
		this.lucky_draw = lucky_draw;
	}

	public Accessory getPhoto() {
		return photo;
	}

	public void setPhoto(Accessory photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "LuckyDrawAward [type=" + type + ", goods_voucher=" + goods_voucher + ", water=" + water + ", probably="
				+ probably + ", game_goods_list=" + game_goods_list + ", getType()=" + getType()
				+ ", getGoods_voucher()=" + getGoods_voucher() + ", getWater()=" + getWater() + ", getProbably()="
				+ getProbably() + ", getGame_goods_list()=" + getGame_goods_list() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
}
