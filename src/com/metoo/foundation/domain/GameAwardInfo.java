package com.metoo.foundation.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

/**
 * <p>
 * Title：GameUserAward.java
 * </p>
 * 
 * <p>
 * Description：记录用户已完成奖励、status: 领取OR未领取
 * </p>
 * 
 * @author 46075
 *
 */
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "game_award_info")
public class GameAwardInfo extends IdEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;// 奖励拥有用户

	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private GameAward gameAward;// 所属奖励

	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Goods goods;// 记录碎片商品信息 便于查询碎片记录

	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private GameGoods gameGoods;// 记录碎片商品信息 便于查询碎片记录

	@Column(columnDefinition = "LongText")
	private String award;// 奖品信息

	@Column(columnDefinition = "int default 0")
	private int status;// 0：未领取 1：已领取 
	
	private Date receiveTime;// 领取奖励时间 记录发放和领取时间 检测是否存在违规操作

	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private GoodsVoucher goods_voucher;// 商品抵用券

	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private GoodsVoucherInfo goods_voucher_info;// 用户商品抵用券

	@Column(columnDefinition = "int default 0")
	private int display;// 是否展示 0：展示 1：不展示

	private int water;// 水滴数

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public GameAward getGameAward() {
		return gameAward;
	}

	public void setGameAward(GameAward gameAward) {
		this.gameAward = gameAward;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
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

	public GoodsVoucherInfo getGoods_voucher_info() {
		return goods_voucher_info;
	}

	public void setGoods_voucher_info(GoodsVoucherInfo goods_voucher_info) {
		this.goods_voucher_info = goods_voucher_info;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	public GameGoods getGameGoods() {
		return gameGoods;
	}

	public void setGameGoods(GameGoods gameGoods) {
		this.gameGoods = gameGoods;
	}

	public int getDisplay() {
		return display;
	}

	public void setDisplay(int display) {
		this.display = display;
	}

}
