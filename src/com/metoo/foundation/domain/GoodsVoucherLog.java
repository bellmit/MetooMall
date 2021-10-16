package com.metoo.foundation.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

/**
 * <p>
 * 	Title：GoodsVoucherLog.class
 * </p>
 * 
 * <p>
 * 	Description：抵用券获取记录
 * </p>
 * @author 46075
 *
 */

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "goods_voucher_log")
public class GoodsVoucherLog extends IdEntity{

	@ManyToOne(fetch = FetchType.LAZY)
	private GoodsVoucher goods_Voucher;
	
	private int status;// 是否使用 0：获取 1：使用 2：退还
	
	@Column(columnDefinition = "int default 0")
	private int log_read;// 已读/未读 0: 未读 1：已读
	
	@Column(precision = 12, scale = 2)
	private BigDecimal price;// 抵用金/金额
	
	private Long user_id;// 所属用户id
	
	private int type;// 日志类型 0：普通日志 1：注册 2：邀约 3：被邀约 4：游戏 5：抽奖
	
	private String message;// 日志信息
	
	private String message_sa;// 阿语日志

	public GoodsVoucher getGoods_Voucher() {
		return goods_Voucher;
	}

	public void setGoods_Voucher(GoodsVoucher goods_Voucher) {
		this.goods_Voucher = goods_Voucher;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getLog_read() {
		return log_read;
	}

	public void setLog_read(int log_read) {
		this.log_read = log_read;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * 
	 */
	public GoodsVoucherLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param addTime
	 */
	public GoodsVoucherLog(Long id, Date addTime) {
		super(id, addTime);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 */
	public GoodsVoucherLog(Long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage_sa() {
		return message_sa;
	}

	public void setMessage_sa(String message_sa) {
		this.message_sa = message_sa;
	}

	/**
	 * @param status
	 * @param log_read
	 * @param price
	 */
	public GoodsVoucherLog(Long id, Date addTime, int status, int log_read, BigDecimal price, Long user_id, String message) {
		super(id, addTime);
		this.status = status;
		this.log_read = log_read;
		this.price = price;
		this.user_id = user_id;
		this.message = message;
	}
	
	
	public GoodsVoucherLog(Long id, Date addTime, int status, int log_read, 
			int type,  BigDecimal price, String message) {
		super(id, addTime);
		this.status = status;
		this.log_read = log_read;
		this.type = type;
		this.price = price;
		this.user_id = user_id;
		this.message = message;
	}
	
	public GoodsVoucherLog(Long id, Date addTime, int status, int log_read, 
			int type,  BigDecimal price, String message, String message_sa) {
		super(id, addTime);
		this.status = status;
		this.log_read = log_read;
		this.type = type;
		this.price = price;
		this.user_id = user_id;
		this.message = message_sa;
		if(message_sa == null || message_sa.equals("")){
			this.message = message;
		}
		
	}
	
	/**
	 * @param status
	 * @param log_read
	 * @param price
	 */
	public GoodsVoucherLog(int status, int log_read, BigDecimal price) {
		this.status = status;
		this.log_read = log_read;
		this.price = price;
	}

	/**
	 * @param goods_Voucher
	 * @param status
	 * @param log_read
	 * @param price
	 * @param user_id
	 */
	public GoodsVoucherLog(Long id, Date addTime, GoodsVoucher goods_Voucher, 
			int status, int log_read, BigDecimal price, Long user_id, String message) {
		super(id, addTime);
		this.goods_Voucher = goods_Voucher;
		this.status = status;
		this.log_read = log_read;
		this.price = price;
		this.user_id = user_id;
		this.message = message;
	}
	
	
	public GoodsVoucherLog(Long id, Date addTime, int status, int log_read, BigDecimal price, Long user_id) {
		super(id, addTime);
		this.status = status;
		this.log_read = log_read;
		this.price = price;
		this.user_id = user_id;
	}

	public GoodsVoucherLog(Long id, Date addTime, GoodsVoucher goods_Voucher, 
			int status, int log_read, BigDecimal price, Long user_id) {
		super(id, addTime);
		this.goods_Voucher = goods_Voucher;
		this.status = status;
		this.log_read = log_read;
		this.price = price;
		this.user_id = user_id;
	}
	
	
	
	
}
