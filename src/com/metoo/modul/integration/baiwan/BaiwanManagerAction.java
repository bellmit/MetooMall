package com.metoo.modul.integration.baiwan;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.ResponseUtils;
import com.metoo.foundation.domain.OrderForm;
import com.metoo.foundation.domain.Payment;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IOrderFormService;
import com.metoo.foundation.service.IPaymentService;
import com.metoo.foundation.service.IUserService;
import com.metoo.manage.admin.tools.OrderFormTools;
import com.metoo.modul.integration.paypal.URLUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/app/v1/payment/baiwan")
public class BaiwanManagerAction {
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IOrderFormService orderService;
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private OrderFormTools orderFormTools;

	
	/*@RequestMapping(value = "/online_payment.json", method = RequestMethod.POST)
	@ResponseBody
	public Object payment(HttpServletRequest request, HttpServletResponse response, @RequestBody BaiwanDto dto){
		if(dto != null){
			if(dto.getOrderNo() != null && dto.getAmount() > 0){
				Map<String, String> params = new HashMap<String, String>();
        		params.put("order_number", dto.getOrderNo());
        		List<OrderForm> orderForms = this.orderService.query("SELECT obj FROM OrderForm obj WHERE obj.order_id=:order_number", params, -1, -1);
        		if(orderForms.size() > 0){
        			OrderForm obj = orderForms.get(0);
        			if(obj.getOrder_main() == 1){
        				if(obj != null && obj.getOrder_status() == 10){
        					if(dto.getAmount() > 0 
        							&& CommUtil.subtract(dto.getAmount(), obj.getPayment_amount()) == 0){
        						// 1：查询百万支付
        						params.clear();
        						params.put("mark", "baiwan");
        						List<Payment> payments = this.paymentService.query("SELECT obj FROM Payment obj WHERE obj.mark=:mark", params, -1, -1);
        						if(payments.size() > 0){
        							Payment payment = payments.get(0);
        							// 汇率
            						double exchange_rate = payment.getExchange_rate();
        							BaiwanVo baiwanVo = new BaiwanVo();
        							// 1：商户号
        							System.out.println(payment.getMerchantNo());
        							System.out.println(payment.getTerminalNo());
        							baiwanVo.setMerchantNo(payment.getMerchantNo());
            						// 2：终端号
        							baiwanVo.setTerminalNo(payment.getTerminalNo());
            						// 3：订单号
        							baiwanVo.setOrderNo(dto.getOrderNo());
            						// 4：订单金额 
        							baiwanVo.setAmount(dto.getAmount());
        							// 5：订单币种
        							baiwanVo.setCurrency("USD");
            						// 6：支付数字签名
            						// 6-1：订单编号
        							String order_number = dto.getOrderNo();
            						// 6-2：持卡人IP
            						String current_ip = CommUtil.getIpAddr(request);// 获得本机IP
            						// 6-3：订单支付金额
            						double price = dto.getAmount();
            						// 6-4：订单币种
            						String currency = "USD";
            						// 6-5：商户号
            						String merchantNo = payment.getMerchantNo();
            						// 6-6：终端号
            						String terminalNo = payment.getTerminalNo();
            						// 6-7：支付类型
            						Integer payType = 1;
            						// 6-8：业务类型
            						Integer serviceType = 0;
            						// 6-9：支付方式
            						Integer payMethod = 0;
            						// 6-10：shakey：安全码
            						String shakey = payment.getSafeKey();
            						String sign =  order_number + current_ip 
            								+ price + currency + merchantNo + terminalNo + payType
            								+ serviceType + payMethod + shakey;
            						baiwanVo.setSecurityValue(getSHA256StrJava(sign));
            						// 7：支付结果返回地址
            						String returnURL = payment.getReturnURL();
            						// 87：支付结果异步返回地址
            						String asynReturnURL = payment.getAsynReturnURL();
            						// 9：订单商品信息
            						String goodsJson = this.queryGoods(obj);
            						// 10：网站网址
            						baiwanVo.setWebsite("https://www.metoomall.com");
            						// 11：持卡人IP
            						baiwanVo.setPayIp(current_ip);
            						// 12：支付页面语言
            						baiwanVo.setLanguage(payment.getLanguage());
            						// 13: 订单备注
            						// 14：支付类型
            						baiwanVo.setPayType(1);
            						// 15：业务类型
            						baiwanVo.setServiceType(0);
            						// 16：支付方式
            						baiwanVo.setPayMethod(0);
            						// 17：卡号
            						baiwanVo.setCardNo(dto.getCardNo());
            						// 18：信用卡背后的cvv2号码
            						baiwanVo.setCvv(dto.getCvv());
            						// 19：信用卡有效期年份【4位】
            						baiwanVo.setExpirationYear(dto.getExpirationYear());
            						// 20：信用卡有效期月份【2位】
            						baiwanVo.setExpirationMonth(dto.getExpirationMonth());
            						// 21：信用卡账单人名字
            						baiwanVo.setBillFirstName(dto.getBillFirstName());
            						// 22：信用卡账单人姓氏 
            						baiwanVo.setBillLastName(dto.getBillLastName());
            						// 23：账单人地址（详细收货地址）
            						baiwanVo.setBillAddress(dto.getBillAddress());
            						// 24：账单人城市 
            						baiwanVo.setBillCity(dto.getBillCity());
            						// 25：账单人州/省 AK
            						baiwanVo.setBillState(dto.getBillState());
            						// 26：账单人国家 US
            						baiwanVo.setBillCountry(dto.getBillCountry());
            						// 27：账单人邮编
            						baiwanVo.setBillZip(dto.getBillZip());
            						// 28：收货人地址
            						baiwanVo.setShipAddress(dto.getShipAddress());
            						// 29：收货城市
            						baiwanVo.setShipCity(dto.getShipCity());
            						// 30：收货人州/省
            						baiwanVo.setShipState(dto.getShipState());
            						// 31：收货人国家
            						baiwanVo.setShipCountry(dto.getShipCountry());
            						// 32：收货人邮编
            						baiwanVo.setShipZip(dto.getShipZip());
            						// 33：持卡人电话
            						baiwanVo.setPhone(dto.getPhone());
            						// 34：持卡人邮箱
            						baiwanVo.setEmail(dto.getEmail());
            						// 35：收货人名字
            						baiwanVo.setShipFirstName(dto.getShipFirstName());
            						// 36：收货人姓氏
            						baiwanVo.setShipLastName(dto.getShipLastName());
            						// 37：浏览器语言
            						baiwanVo.setAcceptLanguage(request.getLocale().getLanguage());
            						// 38：持卡人浏览器信息 userAgent
            						baiwanVo.setUserAgent(request.getHeader("user-agent"));
            						String result = this.sendPost(payment.getApi(), JSONObject.fromObject(baiwanVo).toString());
            						if(result != null && !result.equals("")){
            							return ResponseUtils.ok(result);
            						}
            						return ResponseUtils.fail(4500, "支付失败");
        						}
        						return ResponseUtils.badArgument("The current payment method is not open yet");
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
        			return ResponseUtils.badArgumentValue();
        		}
			}else{
				return ResponseUtils.badArgumentValue();
			}
		}else{
			return ResponseUtils.badArgumentValue();
		}
	}
	*/
	@RequestMapping(value = "/online_payment.json", method = RequestMethod.POST)
	@ResponseBody
	public Object paymentMap(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody BaiwanDto dto){
		if(dto != null){
			String token = dto.getToken();
			User user = null;
			if(token != null && !token.equals("")){
				user = this.userService.getObjByProperty(null, "app_login_token", token);
			}
			if(user != null){
				if(dto.getOrderNo() != null && dto.getAmount() > 0){
					Map<String, String> params = new HashMap<String, String>();
	        		params.put("order_number", dto.getOrderNo());
	        		List<OrderForm> orderForms = this.orderService.query("SELECT obj FROM OrderForm obj WHERE obj.order_id=:order_number", params, -1, -1);
	        		if(orderForms.size() > 0){
	        			OrderForm obj = orderForms.get(0);
	        			if(obj.getUser_id().equals(user.getId().toString())){
	        				if(obj.getOrder_main() == 1){
		        				if(obj != null && obj.getOrder_status() == 10){
		        					if(dto.getAmount() > 0 
		        							&& CommUtil.subtract(dto.getAmount(), obj.getPayment_amount()) == 0){
		        						// 1：查询百万支付
		        						params.clear();
		        						params.put("mark", "baiwan");
		        						List<Payment> payments = this.paymentService.query("SELECT obj FROM Payment obj WHERE obj.mark=:mark", params, -1, -1);
		        						if(payments.size() > 0){
		        							Payment payment = payments.get(0);
		        							// 汇率
		            						double exchange_rate = payment.getExchange_rate();
		        							BaiwanVo baiwanVo = new BaiwanVo();
		        							Map<String, Object> map = new HashMap<String, Object>();
		        							
		        							map.put("remark", "Test remark");
		        							// 1：商户号
		        							map.put("merchantNo", payment.getMerchantNo());
		        							baiwanVo.setMerchantNo(payment.getMerchantNo());
		            						// 2：终端号
		        							map.put("terminalNo", payment.getTerminalNo());
		        							baiwanVo.setTerminalNo(payment.getTerminalNo());
		            						// 3：订单号
		        							map.put("orderNo", dto.getOrderNo());
		        							baiwanVo.setOrderNo(dto.getOrderNo());
		            						// 4：订单金额  
		        							map.put("amount", dto.getAmount());
		        							baiwanVo.setAmount(dto.getAmount());
		        							// 5：订单币种
		        							map.put("currency", "USD");
		        							baiwanVo.setCurrency("USD");
		            						// 6：支付数字签名
			            						// 6-1：订单编号
			        							String order_number = dto.getOrderNo();
			            						// 6-2：持卡人IP
			            						String current_ip = CommUtil.getIpAddr(request);// 获得本机IP
			            						// 6-3：订单支付金额
			            						double price = dto.getAmount();
			            						// 6-4：订单币种
			            						String currency = "USD";
			            						// 6-5：商户号
			            						String merchantNo = payment.getMerchantNo();
			            						// 6-6：终端号
			            						String terminalNo = payment.getTerminalNo();
			            						// 6-7：支付类型
			            						Integer payType = 1;
			            						// 6-8：业务类型
			            						Integer serviceType = 0;
			            						// 6-9：支付方式
			            						Integer payMethod = 0;
		            						// 6-10：shakey：安全码
		            						String shakey = payment.getShakey();
		            						String sign =  order_number + current_ip 
		            								+ price + currency + merchantNo + terminalNo + payType
		            								+ serviceType + payMethod + shakey;
		            						map.put("securityValue", getSHA256StrJava(sign));
		            						baiwanVo.setSecurityValue(getSHA256StrJava(sign));
		            						// 7：支付结果返回地址
		            						map.put("returnURL", payment.getReturnURL());
		            						String returnURL = payment.getReturnURL();
		            						// 8：支付结果异步返回地址
		            						map.put("asynReturnURL", payment.getAsynReturnURL());
		            						String asynReturnURL = payment.getAsynReturnURL();
		            						// 9：订单商品信息
		            						Map goodsJsonMap = new HashMap();
		            						goodsJsonMap.put("data", this.queryGoods(obj));
		            						map.put("goodsJson", goodsJsonMap.toString());
		            						String goodsJson = this.queryGoods(obj);
		            						// 10：网站网址
		            						map.put("website", payment.getWebsite());
		            						baiwanVo.setWebsite(payment.getWebsite());
		            						// 11：持卡人IP
		            						map.put("payIp", current_ip);
		            						baiwanVo.setPayIp(current_ip);
		            						// 12：支付页面语言
		            						map.put("language", payment.getLanguage());
		            						baiwanVo.setLanguage(payment.getLanguage());
		            						// 13: 订单备注
		            						// 14：支付类型
		            						map.put("payType", "1");
		            						baiwanVo.setPayType(1);
		            						// 15：业务类型
		            						map.put("serviceType", "0");
		            						baiwanVo.setServiceType(0);
		            						// 16：支付方式
		            						map.put("payMethod", 0);
		            						baiwanVo.setPayMethod(0);
		            						// 17：卡号
		            						map.put("cardNo", dto.getCardNo());
		            						baiwanVo.setCardNo(dto.getCardNo());
		            						// 18：信用卡背后的cvv2号码
		            						map.put("cvv", dto.getCvv());
		            						baiwanVo.setCvv(dto.getCvv());
		            						// 19：信用卡有效期年份【4位】
		            						map.put("expirationYear", dto.getExpirationYear());
		            						baiwanVo.setExpirationYear(dto.getExpirationYear());
		            						// 20：信用卡有效期月份【2位】
		            						map.put("expirationMonth", dto.getExpirationMonth());
		            						baiwanVo.setExpirationMonth(dto.getExpirationMonth());
		            						// 21：信用卡账单人名字
		            						map.put("billFirstName", dto.getBillFirstName());
		            						baiwanVo.setBillFirstName(dto.getBillFirstName());
		            						// 22：信用卡账单人姓氏 
		            						map.put("billLastName", dto.getBillLastName());
		            						baiwanVo.setBillLastName(dto.getBillLastName());
		            						// 23：账单人地址（详细收货地址）
		            						map.put("billAddress", dto.getBillAddress());
		            						baiwanVo.setBillAddress(dto.getBillAddress());
		            						// 24：账单人城市 
		            						map.put("billCity", dto.getBillCity());
		            						baiwanVo.setBillCity(dto.getBillCity());
		            						// 25：账单人州/省 AK
		            						map.put("billState", dto.getBillState());
		            						baiwanVo.setBillState(dto.getBillState());
		            						// 26：账单人国家 US
		            						map.put("billCountry", dto.getBillCountry());
		            						baiwanVo.setBillCountry(dto.getBillCountry());
		            						// 27：账单人邮编
		            						map.put("billZip", dto.getBillZip());
		            						baiwanVo.setBillZip(dto.getBillZip());
		            						// 28：收货人地址
		            						map.put("shipAddress", dto.getShipAddress());
		            						baiwanVo.setShipAddress(dto.getShipAddress());
		            						// 29：收货城市
		            						map.put("shipCity", dto.getShipCity());
		            						baiwanVo.setShipCity(dto.getShipCity());
		            						// 30：收货人州/省
		            						map.put("shipState", dto.getShipState());
		            						baiwanVo.setShipState(dto.getShipState());
		            						// 31：收货人国家
		            						map.put("shipCountry", dto.getShipCountry());
		            						baiwanVo.setShipCountry(dto.getShipCountry());
		            						// 32：收货人邮编
		            						map.put("shipZip", dto.getShipZip());
		            						baiwanVo.setShipZip(dto.getShipZip());
		            						// 33：持卡人电话
		            						map.put("phone", dto.getPhone());
		            						baiwanVo.setPhone(dto.getPhone());
		            						// 34：持卡人邮箱
		            						map.put("email", dto.getEmail());
		            						baiwanVo.setEmail(dto.getEmail());
		            						// 35：收货人名字
		            						map.put("shipFirstName", dto.getShipFirstName());
		            						baiwanVo.setShipFirstName(dto.getShipFirstName());
		            						// 36：收货人姓氏
		            						map.put("shipLastName", dto.getShipLastName());
		            						baiwanVo.setShipLastName(dto.getShipLastName());
		            						// 37：浏览器语言
		            						map.put("acceptLanguage", request.getLocale().getLanguage());
		            						baiwanVo.setAcceptLanguage(request.getLocale().getLanguage());
		            						// 38：持卡人浏览器信息 userAgent
		            						map.put("userAgent", request.getHeader("user-agent"));
		            						baiwanVo.setUserAgent(request.getHeader("user-agent"));
		            						
		            						// 封装数据
		            						String param = "";
		            						for (Entry<String, Object> entry : map.entrySet()) {
		            							System.out.println(entry.getKey() + " : " + entry.getValue());
		            							param += entry.getKey() + "=" + entry.getValue() + "&";
		            						}
		            						param = param.substring(0, param.length() - 1);
		            						
		            						String result = this.sendPost(payment.getApi(), param);
		            						if(result != null && !result.equals("")){
		            							Map result_map = Json.fromJson(Map.class,
		            									result);
		            							if(result_map.get("status").equals("2")){
		            								obj.setPayment_company(3);
		            								this.orderService.update(obj);
		            								return ResponseUtils.ok(result_map.get("msg"));
		            							}
		            							return ResponseUtils.badArgument(Integer.parseInt(result_map.get("status").toString()), result_map.get("msg").toString());
		            						}
		            						return ResponseUtils.fail(4500, "Payment failure");
		        						}
		        						return ResponseUtils.badArgument("The current payment method is not open yet");
		            				}else{
		            					 return ResponseUtils.badArgument("Do not maliciously tamper with the param");
		            				}
		        				}else{
		        					 return ResponseUtils.badArgument("Abnormal orders");
		        				}
		        			}else{
		        				return ResponseUtils.badArgument("Do not tamper with the order information");
		        			}
	        			}else{
	        				return ResponseUtils.badArgument("Do not maliciously tamper with the param");
	        			}
	        		}else{
	        			return ResponseUtils.badArgumentValue();
	        		}
				}else{
					return ResponseUtils.badArgumentValue();
				}
			}else{
				return ResponseUtils.unlogin();
			}
			
		}else{
			return ResponseUtils.badArgumentValue();
		}
	}
	
	private String getSHA256StrJava(String str) {
//		str = "123456789" + "127.0.0.1" + "1" + "USD" + "10072" + "100001" + "1" + "0" + "0" + "c4F7f2E2rl1JRkA";
		java.security.MessageDigest messageDigest;
		String encodeStr = "";
		try {
			messageDigest = java.security.MessageDigest.getInstance("SHA-256");
			messageDigest.update(str.getBytes("UTF-8"));
			encodeStr = byte2Hex(messageDigest.digest());
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (java.io.UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodeStr;
	}
	
	private String byte2Hex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				// 1得到一位的进行补0操作
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}
	
	public List<Map> queryJsonInfo(String json) {

		List<Map> map_list = new ArrayList<Map>();
		if (json != null && !json.equals("")) {
			map_list = Json.fromJson(ArrayList.class, json);
		}
		return map_list;
	}
	
	// 解析订单商品信息（包含子订单）
	public String queryGoods(OrderForm orderForm){
		String goodsInfo = orderForm.getGoods_info();
		if(goodsInfo != null && !goodsInfo.equals("")){
			List<Map> map_list = Json.fromJson(ArrayList.class, goodsInfo);
			if(map_list.size() > 0){
				if(orderForm.getOrder_main() == 1){
					List<Map> maps = this.queryJsonInfo(orderForm.getChild_order_detail());
					if(maps.size() > 0){
						for (Map map : maps) {
							OrderForm child_order = this.orderService.getObjById(CommUtil.null2Long(map.get("order_id")));
							map_list.addAll(Json.fromJson(ArrayList.class, child_order.getGoods_info()));
						}
					}
				}
				List<Map> itemMaps = new ArrayList();
				for(Map map : map_list){
					Map item = new HashMap();
					item.put("itemName", map.get("goods_name"));
					item.put("price", map.get("goods_all_price"));
					item.put("quantity", map.get("goods_count"));
					itemMaps.add(item);
				}
				return JSON.toJSONString(itemMaps);
			}
		}
		return null;
	}
	
	public String sendPost(String url, String params) {
		// 根据实际请求需求进行参数封装
		java.io.PrintWriter out = null;
		java.io.BufferedReader in = null;
		String result = "";
		try {
			java.net.URL realUrl = new java.net.URL(url);
			// 打开和URL之间的连接
			java.net.URLConnection conn = realUrl.openConnection();
//			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new java.io.PrintWriter(conn.getOutputStream());
			// 发送请求参数
			/*out.print("serviceType=0"
					+ "&goodsJson=[{'itemName':'LV1','price':'1','quantity':'1'},{'itemName':'LV1','price':'1','quantity':'1'}]"
					+ "&billState=sa"
					+ "&language=en_US"
					+ "&remark="
					+ "&billFirstName=CHANG"
					+ "&cardNo=4514610806812985"
					+ "&shipCountry=sa"
					+ "&payType=1"
					+ "&billCountry=sa"
					+ "&payMethod=0"
					+ "&asynReturnURL=https://www.metoomall.com"
					+ "&currency=USD"
					+ "&returnURL=https://www.metoomall.com"
					+ "&email=512345678@163.com"
					+ "&expirationYear=2025"
					+ "&shipCity=sa"
					+ "&terminalNo=100001"
					+ "&amount=1"
					+ "&cvv=755"
					+ "&website=metoomall.com"
					+ "&orderNo=123456789"
					+ "&shipZip=12345"
					+ "6&billZip=123456"
					+ "&userAgent=PostmanRuntime/7.28.4"
					+ "&billLastName=WANG"
					+ "&expirationMonth=04"
					+ "&billCity=sa"
					+ "&shipFirstName=kk"
					+ "&securityValue=5c3e10257b4e66593636200b6354a2b32a1930ca8c5109cca01d8c22762766f7"
					+ "&shipAddress=sa"
					+ "&acceptLanguage=zh&"
					+ "billAddress=sa"
					+ "&phone=512345678"
					+ "&payIp=127.0.0.1"
					+ "&shipState=sa"
					+ "&shipLastName=h"
					+ "&merchantNo=10072"
					);*/
			out.print(params);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (java.io.IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	public String sendPostForm(String url, BaiwanVo vo) {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
	       // 填入各个表单域的值
	       NameValuePair[] data = {
	               new NameValuePair("merchantNo", vo.getMerchantNo()),
	               new NameValuePair("terminalNo", vo.getTerminalNo()),
	               new NameValuePair("orderNo", vo.getOrderNo()),
	               new NameValuePair("amount", String.valueOf(vo.getAmount())),
	               new NameValuePair("currency", vo.getCurrency()),
	               new NameValuePair("securityValue", vo.getSecurityValue()),
	               new NameValuePair("returnURL", vo.getReturnURL()),
	               new NameValuePair("asynReturnURL", vo.getAsynReturnURL()),
	               new NameValuePair("goodsJson", vo.getGoodsJson()),
	               new NameValuePair("website", vo.getWebsite()),
	               new NameValuePair("payIp", vo.getPayIp()),
	               new NameValuePair("language", vo.getLanguage()),
	               new NameValuePair("remark", vo.getRemark()),
	               new NameValuePair("payType", vo.getPayType().toString()),
	               new NameValuePair("serviceType", vo.getServiceType().toString()),
	               new NameValuePair("payMethod", vo.getPayMethod().toString()),
	               new NameValuePair("cardNo", vo.getCardNo().toString()),
	               new NameValuePair("cvv", vo.getCvv().toString()),
	               new NameValuePair("expirationYear", vo.getExpirationYear().toString()),
	               new NameValuePair("expirationMonth", vo.getExpirationMonth().toString()),
	               new NameValuePair("billFirstName", vo.getBillFirstName()),
	               new NameValuePair("billLastName", vo.getBillLastName()),
	               new NameValuePair("billAddress", vo.getBillAddress()),
	               new NameValuePair("billCity", vo.getBillCity()),
	               new NameValuePair("billState", vo.getBillState()),
	               new NameValuePair("billCountry", vo.getBillCountry()),
	               new NameValuePair("billZip", vo.getBillZip().toString()),
	               new NameValuePair("shipFirstName", vo.getShipFirstName()),
	               new NameValuePair("shipLastName", vo.getShipLastName()),
	               new NameValuePair("shipAddress", vo.getShipAddress()),
	               new NameValuePair("shipCity", vo.getShipCity()),
	               new NameValuePair("shipState", vo.getShipState()),
	               new NameValuePair("shipCountry", vo.getShipCountry()),
	               new NameValuePair("shipZip", vo.getShipZip().toString()),
	               new NameValuePair("phone", vo.getPhone().toString()),
	               new NameValuePair("email", vo.getEmail()),
	               new NameValuePair("acceptLanguage", vo.getAcceptLanguage()),
	               new NameValuePair("userAgent", vo.getUserAgent())};
	      // 将表单的值放入postMethod中
	      postMethod.setRequestBody(data);
	      // 执行postMethod
	       int statusCode = 0;
	       try {
	           statusCode = httpClient.executeMethod(postMethod);
	       } catch (HttpException e) {
	           e.printStackTrace();
	       } catch (IOException e) {
	           e.printStackTrace();
	       }	
	       
		return null;
	}
	
	@RequestMapping("/notification.json")
	public void notification(HttpServletRequest request, HttpServletResponse response){
		//1，获取所有返回参数,并拼接验证参数
     	Enumeration<String> en = request.getParameterNames();
     	String str = "";
        while (en.hasMoreElements()) {
             String paramName = en.nextElement();
             String paramValue = request.getParameter(paramName);
             //此处的编码一定要和自己的网站编码一致，不然会出现乱码，paypal回复的通知为‘INVALID’
             try {
				str = str + "&" + paramName + "=" + URLEncoder.encode(paramValue, "utf-8");
				System.out.println("=====================================================================");
				System.out.println("result: " + str);
				System.out.println("=====================================================================");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
	}
	
}
