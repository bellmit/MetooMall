package com.metoo.foundation.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;


/**
 * <p>
 * 	Title: GoodsVoucher.class
 * </p>
 * 
 * <p>
 * 	Description: 商品抵用券详情类
 * </p>
 * 
 * @author 46075
 *
 */
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "goods_voucher_info")
public class GoodsVoucherInfo extends IdEntity{

	@Column(columnDefinition = "int default 0")
	private int status;// 0：未使用 1：已使用 -1：已过期 -2：暂时不可用（活动券、集齐碎片可用）
	
	private Long store_id;// 抵用券对应的用户默认为null：平台抵用券
	
	@ManyToOne(fetch = FetchType.LAZY)
	private GoodsVoucher goods_voucher;// 对应的抵用券
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;// 抵用券拥有的用户
	
	@ManyToOne(fetch = FetchType.LAZY)
	private OrderForm of;// 抵用券
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public GoodsVoucher getGoods_voucher() {
		return goods_voucher;
	}

	public void setGoods_voucher(GoodsVoucher goods_voucher) {
		this.goods_voucher = goods_voucher;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public OrderForm getOf() {
		return of;
	}

	public void setOf(OrderForm of) {
		this.of = of;
	}
	

}
