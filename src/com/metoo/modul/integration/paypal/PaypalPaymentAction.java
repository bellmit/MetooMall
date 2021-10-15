package com.metoo.modul.integration.paypal;

import com.alibaba.fastjson.JSON;
import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.ResponseUtils;
import com.metoo.foundation.domain.Goods;
import com.metoo.foundation.domain.OrderForm;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IGoodsService;
import com.metoo.foundation.service.IOrderFormService;
import com.metoo.foundation.service.IPaymentService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.IUserService;
import com.metoo.manage.admin.tools.OrderFormTools;
import com.metoo.manage.admin.tools.PaymentTools;
import com.metoo.module.app.view.web.tool.AppCartViewTools;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/app/v1/paypal")
public class PaypalPaymentAction {

//    public static final String PAYPAL_SUCCESS_URL = "/successPayment";
//    public static final String PAYPAL_CANCEL_URL = "/faildPayment";
//    String cancelUrl = PAYPAL_CANCEL_URL + "?source=web&orderNum=" + order_number;
//    String successUrl = PAYPAL_SUCCESS_URL + "?source=web&orderNum=" + order_number;
    public static final String PAYPAL_SUCCESS_URL = "/app/v1/paypal/success.json";
    public static final String PAYPAL_CANCEL_URL = "/app/v1/paypal/cancel.json";
    
    //https://www.metoomall.com/myorder 取消付款
    
    // https://www.metoomall.com/faildPayment 付款失败
    
    // https://www.metoomall.com/successPayment 付款成功
    
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaypalService paypalService;
    @Autowired
    private IOrderFormService orderService;
    @Autowired
    private IUserService userService;
    @Autowired
    private PaymentTools paymentTools;
    
    @RequestMapping(method = RequestMethod.GET)
    public String index(){
        return "index";
    }
    
    public static void main(String[] args) {
    	Integer n = 3;
    	System.out.println(n.doubleValue());
    	System.out.println(n.floatValue());
    	System.out.println(n.longValue());
    	System.out.println(n.toString());
	}

    /**
     * json版
     * @param request
     * @return
     */
//    @RequestMapping(method = RequestMethod.POST, value = "online_payment.json")
    @ResponseBody
    public Object payment(HttpServletRequest request, double payment_price, String token, String order_number){
    	if(token != null && !token.equals("") && order_number != null && !order_number.equals("")){
    		User user = this.userService.getObjByProperty(null, "app_login_token", token);
    		if(user != null){
    			Map<String, String> params = new HashMap<String, String>();
        		params.put("order_number", order_number);
        		List<OrderForm> orderForms = this.orderService.query("SELECT obj FROM OrderForm obj WHERE obj.order_id=:order_number", params, -1, -1);
        		if(orderForms.size() >0){
        			OrderForm obj = orderForms.get(0);
        			if(obj.getOrder_main() == 1){
        				if(obj != null && obj.getOrder_status() == 10){
            				if(payment_price > 0 && CommUtil.subtract(payment_price, obj.getUsd_payment_amount()) == 0){
            					// 验证商品库存
            					String verifyGoods = this.paymentTools.verifyGoodsInventory(obj);
        						if(verifyGoods == null || verifyGoods.equals("")){
        							// 1, 判断媒体类型
                					String cancelUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_CANCEL_URL;
                				    String successUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_SUCCESS_URL;
                			        String approval_url = sendPayment(cancelUrl, successUrl, payment_price, order_number);
                			        if(approval_url != null){
                			        	obj.setPayment_company(1);
                			        	obj.setUrl_payment(approval_url);
                			        	this.orderService.update(obj);
                			        	return ResponseUtils.ok(approval_url);
                			        }else{
                			        	return ResponseUtils.badArgument("Failed to initiate the payment request and need to pay again");
                			        }
        						}else{
        							return ResponseUtils.badArgument(4206, "Goods in short stock", verifyGoods);
        						}
            				}else{
            					 return ResponseUtils.badArgument("Do not maliciously tamper with the amount");
            				}
            			}else{
            				 return ResponseUtils.badArgument("Abnormal orders");
            			}
        			}else{
        				return ResponseUtils.badArgument("Do not tamper with the order information");
        			}
        			
        		}else{
        			return ResponseUtils.badArgument("Abnormal order number");
        		}
    		}
    		return ResponseUtils.unlogin();
    	}
		return ResponseUtils.badArgumentValue();
        
    }
    
    
    public String sendPayment(String cancelUrl, String successUrl, double payment_price, String order_number){
    	 try {
             Payment payment = paypalService.createPayment(
             		CommUtil.null2Double(payment_price),
                     "USD",
                     PaypalPaymentMethod.paypal,
                     PaypalPaymentIntent.sale,
                     "payment description",
                     cancelUrl,
                     successUrl,
                     order_number);
             for(Links links : payment.getLinks()){
                 if(links.getRel().equals("approval_url")){
                 	return links.getHref();
                 }
             }
         } catch (PayPalRESTException e) {
             log.error(e.getMessage());
             System.out.println(e.getMessage());
         }
         return null;
    }
    /**
     * 重定向版
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "pay")
    public String pay(HttpServletRequest request, String order_number){
        String cancelUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_CANCEL_URL;
        String successUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_SUCCESS_URL;
        try {
            Payment payment = paypalService.createPayment(
                    500.00,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "payment description",
                    cancelUrl,
                    successUrl,
                    order_number);
            for(Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cancel.json")
    public void cancelPay(HttpServletResponse response){
    	response.setStatus(302);
    	response.setHeader("location", "https://www.metoomall.com/myorder");
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value = "/success.json")
    public void successPay(HttpServletResponse response, @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            String custom = payment.getTransactions().get(0).getCustom();
            if(payment.getState().equals("approved")){
            	response.setStatus(302);
            	response.setHeader("location", "https://www.metoomall.com/successPayment");
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
            response.setStatus(302);
        	response.setHeader("location", "https://www.metoomall.com/faildPayment");
        }
    }
    
    /*@RequestMapping(method = RequestMethod.GET, value = "/success.json")
    public void successPay(HttpServletResponse response, @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
            	response.setStatus(302);
            	response.setHeader("location", "http://www.metoomall.com");
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
            response.setStatus(302);
        	response.setHeader("location", "http://www.metoomall.com");
        }
    }*/


}
