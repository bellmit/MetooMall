package com.metoo.foundation.domain;

import java.math.BigDecimal;
import java.util.Date;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

/**
 * <p>
 * 	Title: GoodsVoucher.class
 * </p>
 * 
 * <p>
 * 	Description: 商品抵用券，直接抵扣金额
 * </p>
 * 
 * @author 46075
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "goods_voucher")
public class GoodsVoucher extends IdEntity{

	private String name;// 抵用券名称
	
	private BigDecimal number;// 抵用券价格 price 字段改为number 统一奖励字段，方便前端使用；（不晓得规范是什么，前端不想根据各种奖励类型区分字段不同）
	
	private BigDecimal enough_price;// 满减金额 默认为0
	
	private Date begin_time;// 抵用券有效时间 默认为null 开始时间可以为null、例如 限8月3日（到期）
	 
	private Date end_time;// 抵用券失效时间 默认为null 
	
	private int type;// 抵用券类型 0：普通抵用券，1: 活动抵用券（种树）没有时间、满减限制；1：有满减限制 2：有时间限制 3：有满减and时间限制 4：注册抵用券  5：邀约抵用券 6:被邀约人抵用券 7:被邀约抵用券
	// 8: 抽奖抵用券
	
	private String msg;// 抵用券描述
	
	private String accessory;// 图片路径
	
	private Integer amount;// 邀约数量
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private GameAward gameAward;
	
	private String probably;// 优惠券概率{"40":"0.4","41":"0.4","42":"0.4","43":"0.4","44":"0.4","45":"0.4","46":"0.4","47":"0.4","48":"0.4","49":"0.4","50":"0.4","51":"0.4","52":"0.4"}
	// {3:10,4:2}
	
	// 注：删除时清空游戏奖励中的记录
	@OneToMany(mappedBy = "goods_Voucher")
	private List<GoodsVoucherLog> goods_voucher_log;
	
	@OneToOne(mappedBy = "goods_voucher", fetch = FetchType.LAZY)
	private LuckyDrawAward lucky_draw_award;// 随机奖励
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public BigDecimal getNumber() {
		return number;
	}

	public void setNumber(BigDecimal number) {
		this.number = number;
	}

	public BigDecimal getEnough_price() {
		return enough_price;
	}

	public void setEnough_price(BigDecimal enough_price) {
		this.enough_price = enough_price;
	}

	public Date getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(Date begin_time) {
		this.begin_time = begin_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAccessory() {
		return accessory;
	}

	public void String(String accessory) {
		this.accessory = accessory;
	}

	public GameAward getGameAward() {
		return gameAward;
	}

	public void setGameAward(GameAward gameAward) {
		this.gameAward = gameAward;
	}

	public String getProbably() {
		return probably;
	}

	public void setProbably(String probably) {
		this.probably = probably;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public void setAccessory(String accessory) {
		this.accessory = accessory;
	}

	public List<GoodsVoucherLog> getGoods_voucher_log() {
		return goods_voucher_log;
	}

	public void setGoods_voucher_log(List<GoodsVoucherLog> goods_voucher_log) {
		this.goods_voucher_log = goods_voucher_log;
	}

	public LuckyDrawAward getLucky_draw_award() {
		return lucky_draw_award;
	}

	public void setLucky_draw_award(LuckyDrawAward lucky_draw_award) {
		this.lucky_draw_award = lucky_draw_award;
	}

	public GoodsVoucher() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GoodsVoucher(Long id, Date addTime) {
		super(id, addTime);
		// TODO Auto-generated constructor stub
	}

	public GoodsVoucher(Long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public GoodsVoucher(Long id, java.lang.String name, BigDecimal number, int type, java.lang.String msg,
			java.lang.String accessory, String probably) {
		super(id);
		this.name = name;
		this.number = number;
		this.type = type;
		this.msg = msg;
		this.accessory = accessory;
		this.probably = probably;
	}
	
	
}
