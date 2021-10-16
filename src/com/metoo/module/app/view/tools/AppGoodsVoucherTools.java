package com.metoo.module.app.view.tools;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metoo.foundation.domain.GoodsVoucher;
import com.metoo.foundation.domain.GoodsVoucherInfo;
import com.metoo.foundation.domain.GoodsVoucherLog;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IGoodsVoucherInfoService;
import com.metoo.foundation.service.IGoodsVoucherLogService;

@Component
public class AppGoodsVoucherTools {
	
	@Autowired
	private IGoodsVoucherInfoService goodsVoucherInfoService;
	@Autowired
	private IGoodsVoucherLogService goodsVoucherLogService;

	public boolean getVoucher(GoodsVoucher goods_voucher, User user) {
		if (goods_voucher != null) {
			GoodsVoucherInfo obj = new GoodsVoucherInfo();
			obj.setAddTime(new Date());
			obj.setGoods_voucher(goods_voucher);
			obj.setUser(user);
			obj.setStore_id(null);
			obj.setStatus(0);
			this.goodsVoucherInfoService.save(obj);
			return true;
		}
		return false;
	}
	
	public void createLog(GoodsVoucher goods_voucher, User user, int type, int status, String message, String message_sa, Double price){
		if (goods_voucher != null || user != null) {
			GoodsVoucherLog log = new GoodsVoucherLog();
			log.setAddTime(new Date());
			log.setLog_read(0);
			log.setType(type);
			log.setStatus(status);
			log.setUser_id(user.getId());
			log.setMessage(message);
			log.setMessage_sa(message_sa);
			if(goods_voucher != null){
				log.setGoods_Voucher(goods_voucher);
				log.setPrice(goods_voucher.getNumber());
			}else{
				log.setPrice(new BigDecimal(price));
			}
			this.goodsVoucherLogService.save(log);
		}
	}
	
}
