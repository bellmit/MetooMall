package com.metoo.modul.integration.paypal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.OrderForm;
import com.metoo.foundation.domain.OrderFormLog;
import com.metoo.foundation.domain.StoreLog;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IGoodsService;
import com.metoo.foundation.service.IOrderFormLogService;
import com.metoo.foundation.service.IOrderFormService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.IUserService;
import com.metoo.manage.admin.tools.OrderFormTools;
import com.metoo.manage.seller.tools.StoreLogTools;
import com.metoo.module.app.view.web.tool.AppCartViewTools;

@RequestMapping("/paypal")
@Controller
public class PaypalNotificationAction {
	
	
	private static final String SENDBOX = "https://ipnpb.sandbox.paypal.com/cgi-bin/webscr";
	private static final String LIVE = "https://ipnpb.paypal.com/cgi-bin/webscr";
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IOrderFormService orderService;
	@Autowired
	private AppCartViewTools appCartViewTools;
	@Autowired
	private IOrderFormLogService orderFormLogService;
	@Autowired
	private StoreLogTools storeLogTools;
	
	 @RequestMapping("/notification.json")
     public void receivePaypalNotifacation(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException{
     	//1，获取所有返回参数,并拼接验证参数
     	Enumeration<String> en = request.getParameterNames();
     	String str = "cmd=_notify-validate";
        while (en.hasMoreElements()) {
             String paramName = en.nextElement();
             String paramValue = request.getParameter(paramName);
             //此处的编码一定要和自己的网站编码一致，不然会出现乱码，paypal回复的通知为‘INVALID’
             try {
				str = str + "&" + paramName + "=" + URLEncoder.encode(paramValue, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
		try {
			URL u = new URL(LIVE);
			 HttpURLConnection uc = (HttpURLConnection) u.openConnection();
	         uc.setRequestMethod("POST");
	         uc.setDoOutput(true);
	         uc.setDoInput(true);
	         uc.setUseCaches(false);
	         //设置 HTTP 的头信息
	         uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	         PrintWriter pw = new PrintWriter(uc.getOutputStream());
	         pw.println(str);
	         pw.close();
        
			/**
		     * 接受 PayPal 对 IPN 回发的回复信息
		     */
		    BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		    String res = in.readLine();
		    in.close();
		    // 自定义字段，获取付款时存储的订单号
            String custom = request.getParameter("custom");
            // 交易状态 Completed 代表交易成功 Failed:失败
            String paymentStatus = request.getParameter("payment_status");
            // 交易类型 echeck:电子支票支付 instant PayPal余额、信用卡、即时转账支付的
            String payment_type = request.getParameter("payment_type");
            OrderForm obj = null;
            if ("VERIFIED".equalsIgnoreCase(res)) {
            	if(custom != null && !custom.equals("")){
            		// 查询订单信息
                	Map params = new HashMap();
                	params.put("order_id", custom);
                	List<OrderForm> orderForms = this.orderService.query("SELECT obj FROM OrderForm obj WHERE obj.order_id=:order_id", params, -1, -1);
                	if(orderForms.size() > 0){
                		obj = orderForms.get(0);
                		if(obj != null){
                			// 判断订单是否为代付款
                			if(obj.getOrder_status() == 10 || obj.getOrder_status() == 115){
                				if(paymentStatus.equals("Completed")){
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
                				
                				}else if(paymentStatus.equals("Failed")){
                					obj.setOrder_status(115);
                				}else if("Denied".equals(paymentStatus)){
                					obj.setOrder_status(116);
                				}else if("Pending".equals(paymentStatus)){
                					obj.setOrder_status(110);
                				}
                				if(payment_type.equals("instant")){
                					obj.setPayment_type(1);
                				}else if(payment_type.equals("echeck")){
                					obj.setPayment_type(0);
                				}
                				obj.setIpn(str);
                				obj.setIpn_status(1);
                				this.orderService.update(obj);
                			}
                		}
                	}
            	}
            }else if("INVALID".equalsIgnoreCase(res)){
            	// 非法信息，可以将此记录到您的日志文件中以备调查
            	// 更新用户的数据
            	if(obj != null){
            		obj.setIpn_status(-1);
            		this.orderService.update(obj);
            	}
            }else{
            	// //处理其他错误
            	obj.setIpn_status(0);
        		this.orderService.update(obj);
            }
		}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		try {
			response.getWriter().print("success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
     }
}
