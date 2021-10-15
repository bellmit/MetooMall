package com.metoo.modul.integration.baiwan;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.OrderForm;
import com.metoo.foundation.domain.OrderFormLog;
import com.metoo.foundation.domain.Payment;
import com.metoo.foundation.domain.StoreLog;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IOrderFormLogService;
import com.metoo.foundation.service.IOrderFormService;
import com.metoo.foundation.service.IPaymentService;
import com.metoo.foundation.service.IUserService;
import com.metoo.manage.seller.tools.StoreLogTools;
import com.metoo.module.app.view.web.tool.AppCartViewTools;

@RequestMapping("/baiwan/")
@Controller
public class BaiwanNotificationAction {

	@Autowired
	private IUserService userService;
	@Autowired
	private IOrderFormService orderFormService;
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private AppCartViewTools appCartViewTools;
	@Autowired
	private IOrderFormLogService orderFormLogService;
	@Autowired
	private StoreLogTools storeLogTools;
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param msg 返回信息
	 * @param terminalNo 终端号
	 * @param amount 支付金额
	 * @param orderNo 订单号
	 * @param redirectUrl 重定向地址
	 * @param tradeNo 支付平台流水号
	 * @param tradeDateTime 交易日期
	 * @param currency 支付订单币种
	 * @param is3d 是否是3D交易
	 * @param securityValue SHA256安全数字签名
	 * @param merchantNo 商户编号
	 * @param status 调用接口返回编码
	 */
	@RequestMapping("/notification.json")
	@ResponseBody
	public void notification(HttpServletRequest request, HttpServletResponse response, String msg, String terminalNo, 
			String amount, String orderNo, String redirectUrl, String tradeNo, String tradeDateTime, String currency, 
			String is3d, String securityValue, String merchantNo, String status){
		if(orderNo != null && !orderNo.equals("")){
			Map params = new HashMap();
			params.put("order_id", orderNo);
			List<OrderForm> orderForms = this.orderFormService.query("SELECT obj FROM OrderForm obj WHERE obj.order_id=:order_id", params, -1, -1);
			if(orderForms.size() > 0){
				// 跳转地址
				params.clear();
				params.put("mark", "baiwan");
				List<Payment> payments = this.paymentService
						.query("SELECT obj FROM Payment obj WHERE obj.mark=:mark", params, -1, -1);
				Payment payment = payments.get(0);
				OrderForm obj = orderForms.get(0);
				if(status.equals("2")){
					obj.setOrder_status(20);
					// 更新商品库存
					// 1. 更新商品库存
					this.appCartViewTools.updateGoodsInventory(obj);// 更新主订单商品库存、在线支付完成，更新库存
					// 2. 记录订单日志
					User user = this.userService.getObjById(CommUtil.null2Long(obj.getUser_id()));
					OrderFormLog main_ofl = new OrderFormLog();
					main_ofl.setAddTime(new Date());
					main_ofl.setLog_info("Baiwan-在线支付");
					main_ofl.setLog_user(user);
					main_ofl.setOf(obj);
					this.orderFormLogService.save(main_ofl);
					// 3. 增加店铺日下单数
					StoreLog storeLog = this.storeLogTools.getTodayStoreLog(CommUtil.null2Long(obj.getStore_id()));
					
					response.setHeader("location", payment.getSuccessURL());
				}else if(status.equals("1")){
					obj.setOrder_status(115);
					response.setHeader("location", payment.getFailURL());
				}else if(status.equals("3") 
						|| status.equals("4")){
					obj.setOrder_status(110);
					response.setHeader("location", payment.getFailURL());
				}else{
					obj.setOrder_status(105);
					response.setHeader("location", payment.getFailURL());
				}
				obj.setPayment_msg(msg);
				obj.setTrade_no(tradeNo);
				this.orderFormService.update(obj);
				response.setStatus(302);
			}
		}
	}
}
