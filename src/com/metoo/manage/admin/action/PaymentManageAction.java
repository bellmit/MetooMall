package com.metoo.manage.admin.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpRequest;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.metoo.core.annotation.SecurityMapping;
import com.metoo.core.mv.JModelAndView;
import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.WebForm;
import com.metoo.core.tools.database.DatabaseTools;
import com.metoo.foundation.domain.OrderForm;
import com.metoo.foundation.domain.OrderFormLog;
import com.metoo.foundation.domain.Payment;
import com.metoo.foundation.domain.StoreLog;
import com.metoo.foundation.domain.SysConfig;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IOrderFormLogService;
import com.metoo.foundation.service.IOrderFormService;
import com.metoo.foundation.service.IPaymentService;
import com.metoo.foundation.service.IStoreLogService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.IUserConfigService;
import com.metoo.foundation.service.IUserService;
import com.metoo.manage.admin.tools.OrderFormTools;
import com.metoo.manage.admin.tools.PaymentTools;
import com.metoo.manage.seller.tools.StoreLogTools;
import com.metoo.module.app.buyer.domain.Result;
import com.metoo.module.app.test.httpclient.HttpClientUtil;
import com.metoo.module.app.test.httpclient.HttpUrlConnectionUtil;
import com.metoo.module.app.view.web.tool.AppCartViewTools;
import com.metoo.module.app.view.web.tool.AppLuckyDrawTools;
import com.metoo.module.app.view.web.tool.AppRegisterViewTools;

/**
 * 
 * <p>
 * Title: PaymentManageAction.java
 * </p>
 * 
 * <p>
 * Description:支付方式控制器,配置系统接受支付的所有支付方式，B2B2C由平台统一收款，只需要运营商配置收款方式，商家无需关心收款方式
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * 
 * <p>
 * Company: 沈阳网之商科技有限公司 www.koala.com
 * </p>
 * 
 * @author erikzhang
 * 
 * @date 2014-5-25
 * 
 * @version koala_b2b2c v2.0 2015版
 */
@Controller
public class PaymentManageAction {
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserConfigService userConfigService;
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private PaymentTools paymentTools;
	@Autowired
	private IUserService userService;
	@Autowired
	private IOrderFormService orderService;
	@Autowired
	private HttpClientUtil httpClient;
	@Autowired
	private HttpUrlConnectionUtil util;
	@Autowired
	private DatabaseTools databaseTools;
	@Autowired
	private AppCartViewTools appCartViewTools;
	@Autowired
	private StoreLogTools storeLogTools;
	@Autowired
	private OrderFormTools orderFormTools;
	@Autowired
	private IStoreLogService storeLogService;
	@Autowired
	private IOrderFormLogService orderFormLogService;
	@Autowired
	private AppLuckyDrawTools appLuckyDrawTools;
	@Autowired
	private AppRegisterViewTools appRegisterViewTools;

	static Logger logger = Logger.getLogger(PaymentManageAction.class);
	
	public static void main(String[] args) {
		long current_time_millis = System.currentTimeMillis();
		long timeStampSec = System.currentTimeMillis() / 1000;// 13位时间戳（单位毫秒）转换为10位字符串（单位秒）
		String timestamp = String.format("%010d", timeStampSec);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		long d = CommUtil.null2Long("1630055641") * 1000;
		
	    Date date = new Date(d);
	
		System.out.println(date);;
		
		System.out.println(sdf.format(date));
	}

	@SecurityMapping(title = "支付方式列表", value = "/admin/payment_list.htm*", rtype = "admin", rname = "支付方式", rcode = "payment_set", rgroup = "设置")
	@RequestMapping("/admin/payment_list.htm")
	public ModelAndView payment_list(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("admin/blue/payment_list.html", configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		SysConfig config = this.configService.getSysConfig();
		String store_payment = CommUtil.null2String(config.getStore_payment());
		Map map = Json.fromJson(HashMap.class, store_payment);
		if (map != null) {
			for (Iterator it = map.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				Object val = map.get(key);
				mv.addObject(key, val);
			}
		}
		mv.addObject("paymentTools", paymentTools);
		return mv;
	}

	@SecurityMapping(title = "支付方式设置", value = "/admin/payment_set.htm*", rtype = "admin", rname = "支付方式", rcode = "payment_set", rgroup = "设置")
	@RequestMapping("/admin/payment_set.htm")
	public ModelAndView payment_set(HttpServletRequest request, HttpServletResponse response, String mark, String type,
			String pay, String config_id) {
		ModelAndView mv = new JModelAndView("admin/blue/success.html", configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		if (CommUtil.null2String(type).equals("admin")) {
			Map params = new HashMap();
			params.put("mark", mark);
			List<Payment> objs = this.paymentService.query("select obj from Payment obj where obj.mark=:mark", params,
					-1, -1);
			Payment obj = null;
			if (objs.size() > 0) {
				obj = objs.get(0);
			} else
				obj = new Payment();
			obj.setAddTime(new Date());
			obj.setMark(mark);
			obj.setInstall(!CommUtil.null2Boolean(pay));
			if (CommUtil.null2String(obj.getName()).equals("")) {
				if (mark.trim().equals("alipay")) {
					obj.setName("支付宝");
				}
				if (mark.trim().equals("balance")) {
					obj.setName("预存款支付");
				}
				if (mark.trim().equals("outline")) {
					obj.setName("线下支付");
				}
				if (mark.trim().equals("tenpay")) {
					obj.setName("财付通");
				}
				if (mark.trim().equals("bill")) {
					obj.setName("快钱支付");
				}
				if (mark.trim().equals("chinabank")) {
					obj.setName("网银在线");
				}
				if (mark.trim().equals("alipay_wap")) {
					obj.setName("支付宝手机网页支付");
				}
			}
			if (objs.size() > 0) {
				this.paymentService.update(obj);
			} else
				this.paymentService.save(obj);
		}
		if (CommUtil.null2String(type).equals("user")) {
			SysConfig config = this.configService.getSysConfig();
			String store_payment = CommUtil.null2String(config.getStore_payment());
			Map map = Json.fromJson(HashMap.class, store_payment);
			if (map == null)
				map = new HashMap();
			map.put(mark, !CommUtil.null2Boolean(pay));
			store_payment = Json.toJson(map, JsonFormat.compact());
			config.setStore_payment(store_payment);
			if (!CommUtil.null2String(config_id).equals("")) {
				this.configService.update(config);
			} else
				this.configService.save(config);
		}
		mv.addObject("list_url", CommUtil.getURL(request) + "/admin/payment_list.htm?type=" + type);
		mv.addObject("op_title", "设置支付方式成功");
		mv.addObject("paymentTools", paymentTools);
		return mv;
	}

	@SecurityMapping(title = "支付方式编辑", value = "/admin/payment_edit.htm*", rtype = "admin", rname = "支付方式", rcode = "payment_set", rgroup = "设置")
	@RequestMapping("/admin/payment_edit.htm")
	public ModelAndView payment_edit(HttpServletRequest request, HttpServletResponse response, String mark) {
		ModelAndView mv = new JModelAndView("admin/blue/payment_info.html", configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		Map params = new HashMap();
		params.put("mark", mark);
		List<Payment> objs = this.paymentService.query("select obj from Payment obj where obj.mark=:mark", params, -1,
				-1);
		Payment obj = null;
		if (objs.size() > 0) {
			obj = objs.get(0);
		} else {
			obj = new Payment();
			obj.setMark(mark);
		}
		mv.addObject("obj", obj);
		mv.addObject("edit", true);
		return mv;
	}

	@SecurityMapping(title = "支付方式保存", value = "/admin/payment_save.htm*", rtype = "admin", rname = "支付方式", rcode = "payment_set", rgroup = "设置")
	@RequestMapping("/admin/payment_save.htm")
	public ModelAndView payment_save(HttpServletRequest request, HttpServletResponse response, String mark,
			String list_url) {
		ModelAndView mv = new JModelAndView("admin/blue/success.html", configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		Map params = new HashMap();
		params.put("mark", mark);
		List<Payment> objs = this.paymentService.query("select obj from Payment obj where obj.mark=:mark", params, -1,
				-1);
		Payment obj = null;
		if (objs.size() > 0) {
			Payment temp = objs.get(0);
			WebForm wf = new WebForm();
			obj = (Payment) wf.toPo(request, temp);
		} else {
			WebForm wf = new WebForm();
			obj = wf.toPo(request, Payment.class);
			obj.setAddTime(new Date());
		}
		if (objs.size() > 0) {
			this.paymentService.update(obj);
		} else {
			this.paymentService.save(obj);
		}
		mv.addObject("op_title", "保存支付方式成功");
		mv.addObject("list_url", list_url);
		return mv;
	}

	/**
	 * 第三方支付接口
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	/*
	 * @RequestMapping("test/payment.json") public void pay(HttpServletRequest
	 * request, HttpServletResponse response) throws IOException { //
	 * https://sandbox.glocashpayment.com/gateway/payment/index //
	 * https://sandbox.glocash.com/gateway/payment/index String sandbox_url =
	 * "https://sandbox.glocashpayment.com/gateway/payment/index"; // 测试地址
	 * String live_url = "https://pay.glocash.com/gateway/payment/index"; //
	 * 正式地址
	 * 
	 * // 秘钥 测试地址请用测试秘钥 正式地址用正式秘钥 请登录商户后台查看 String sandbox_key =
	 * "60865e6c6bca34ac500d652a84106ce50490d0d745e059a747ad0359a279ad39"; //
	 * TODO // 测试秘钥 // 商户后台查看// String live_key =
	 * "e47780532843ab590ca371f58d0a74f523a8588dac76a571a490a2fc6c3568d7"; //
	 * TODO 正式秘钥 商户后台查看(必须材料通过以后才能使用) long timeStampSec =
	 * System.currentTimeMillis() / 1000; String timestamp =
	 * String.format("%010d", timeStampSec);
	 * 
	 * String timetemp = new java.text.SimpleDateFormat("yyyyMMddHHmmss")
	 * .format(new java.util.Date(timeStampSec * 1000)); String timetemp2 = new
	 * java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss") .format(new
	 * java.util.Date(timeStampSec * 1000));
	 * 
	 * java.util.Random rand = new java.util.Random();
	 * 
	 * // 支付参数 java.util.Map<String, String> data = new
	 * java.util.HashMap<String, String>(); data.put("REQ_SANDBOX", "1"); //
	 * TODO 是否开启测试模式 注意秘钥是否对应 data.put("REQ_EMAIL", "gm@soarmall.com"); // TODO
	 * 需要换成自己的 // 商户邮箱 // 商户后台申请的邮箱 data.put("REQ_TIMES", timestamp); // 请求时间
	 * data.put("REQ_INVOICE", "TEST" + timetemp + rand.nextInt(1000) + 9000);
	 * // 订单号 data.put("BIL_METHOD", "L01"); // 请求方式 C01:信用卡支付
	 * data.put("CUS_EMAIL", "460751446@qq.com"); // 客户邮箱 data.put("BIL_PRICE",
	 * "0.1"); // 价格 data.put("BIL_CURRENCY", "USD"); // 币种
	 * data.put("BIL_CC3DS", "0"); // 是否开启3ds 1 开启 0 不开启 data.put("URL_SUCCESS",
	 * "http://hs.crjblog.cn/success.php"); // 支付成功跳转页面 data.put("URL_FAILED",
	 * "http://hs.crjblog.cn/failed.php"); // 支付失败跳转页面 data.put("URL_NOTIFY",
	 * "http://hs.crjblog.cn/notify.php"); // 异步回调跳转页面
	 * 
	 * // $data['BIL_PRCCODE'] = 0; //电话支付相关参数 信用卡不需要填写 // 更多支付参数请参考文档
	 * 经典模式->附录2：付款请求参数表 // 签名 String url = data.get("REQ_SANDBOX") == "0" ?
	 * sandbox_url : live_url;// 根据REQ_SANDBOX调整地址 String key =
	 * data.get("REQ_SANDBOX") == "0" ? sandbox_key : live_key;//
	 * 根据REQ_SANDBOX调整秘钥
	 * 
	 * String reg_sign = key + data.get("REQ_TIMES") + data.get("REQ_EMAIL") +
	 * data.get("REQ_INVOICE") + data.get("CUS_EMAIL") + data.get("BIL_METHOD")
	 * + data.get("BIL_PRICE") + data.get("BIL_CURRENCY"); data.put("REQ_SIGN",
	 * getSHA256StrJava(reg_sign));
	 * 
	 * java.io.File file = new java.io.File(""); String filePath =
	 * file.getCanonicalPath();
	 * 
	 * try { java.io.File files = new java.io.File(filePath + "\\ccDirect.log");
	 * java.io.Writer outfile = new java.io.FileWriter(files, true);
	 * outfile.write(timetemp2 + "\r\n"); outfile.write(url + "\r\n"); for
	 * (java.util.Map.Entry<String, String> entry : data.entrySet()) {
	 * outfile.write(entry.getKey() + ": " + entry.getValue() + "\r\n"); }
	 * outfile.close();
	 * 
	 * String param = ""; for (java.util.Map.Entry<String, String> entry :
	 * data.entrySet()) { param += entry.getKey() + "=" + entry.getValue() +
	 * "&"; } param = param.substring(0, param.length() - 1); // post请求 String
	 * result = sendPost(url, param); com.alibaba.fastjson.JSONObject parseData
	 * = com.alibaba.fastjson.JSONObject.parseObject(result);
	 * 
	 * if (parseData.getString("REQ_ERROR") != null) {
	 * System.out.println("<pre>"); for (String keystr : parseData.keySet()) {
	 * System.out.println(keystr + ": " + parseData.get(keystr) + "<br/>"); }
	 * System.out.println("</pre>"); return; }
	 * 
	 * outfile = new java.io.FileWriter(files, true); outfile.write("\r\n");
	 * outfile.write(result + "\r\n"); for (String keystr : parseData.keySet())
	 * { outfile.write(keystr + ": " + parseData.get(keystr) + "\r\n"); }
	 * outfile.close();
	 * 
	 * if (result.length() > 0 && !parseData.isEmpty()) {
	 * response.sendRedirect(parseData.getString("URL_PAYMENT")); } else {
	 * System.out.println(result); for (String keystr : parseData.keySet()) {
	 * System.out.println(keystr + ": " + parseData.get(keystr) + "<br/>"); } }
	 * 
	 * } catch (Exception e) { System.out.println("<pre>");
	 * System.out.println(e.getMessage()); System.out.println("</pre>"); } }
	 */

	/*
	 * public static void main(String[] args) { Logger logger =
	 * Logger.getLogger(PaymentManageAction.class);
	 * logger.info("======================================");
	 * System.out.println("================"); }
	 */

	@RequestMapping("/payment/test.json")
	@ResponseBody
	public String test(HttpServletRequest request){
		String system_domain = CommUtil.generic_domain(request);
		system_domain = request.getScheme() + "://" + system_domain + "/gateway/transaction/index.json";
		return system_domain;
	}

	/**
	 * 发起付款请求
	 * 
	 * @param request
	 * @param response
	 * @param order_num
	 *            订单号
	 * @param paymen_mode
	 *            请求方式 C01:信用卡支付 L01
	 * @param email
	 *            客户邮箱
	 * @param total_price
	 *            价格
	 * @param currency
	 *            币种 USD
	 * @param URL_SUCCESS
	 *            支付成功跳转页面
	 * @param URL_FAILED
	 *            支付失败跳转页面
	 * @param URL_NOTIFY
	 *            异步回调跳转页面
	 * @return TNS_GCID: 交易流水号、URL_PAYMENT：当前交易付款地址
	 * @throws IOException
	 */
	// @RequestMapping(value = "app/glocash/v1/online_payment.json", produces = { "application/json;charset=UTF-8" })
	@RequestMapping(value = "app/v1/online_payment.json", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String payment(HttpServletRequest request, HttpServletResponse response, String order_num,
			String paymen_mode, String email, String total_price, String currency, String URL_SUCCESS,
			String URL_FAILED, String URL_NOTIFY, String IFS_MODE, String IFS_URL) throws IOException {
		int code = -1;
		String msg = "";
		Map map = new HashMap();
		// https://sandbox.glocashpayment.com/gateway/payment/index
		// https://sandbox.glocash.com/gateway/payment/index
		String sandbox_url = "https://sandbox.glocashpayment.com/gateway/payment/index"; // 测试地址
		String live_url = "https://pay.glocashpayment.com/gateway/payment/index"; // 正式地址

		// 秘钥 测试地址请用测试秘钥 正式地址用正式秘钥 请登录商户后台查看
		String sandbox_key = "60865e6c6bca34ac500d652a84106ce50490d0d745e059a747ad0359a279ad39"; // TODO
																									// 测试秘钥
		if (!CommUtil.null2String(email).equals("")) {
			boolean verify = appRegisterViewTools.verify_email(email);
			if (verify) {
				String live_key = "e47780532843ab590ca371f58d0a74f523a8588dac76a571a490a2fc6c3568d7"; // TODO
																										// 正式秘钥
																										// 商户后台查看(必须材料通过以后才能使用)
				long current_time_millis = System.currentTimeMillis();
				long timeStampSec = System.currentTimeMillis() / 1000;// 13位时间戳（单位毫秒）转换为10位字符串（单位秒）
				String timestamp = String.format("%010d", timeStampSec);

				String timetemp = new java.text.SimpleDateFormat("yyyyMMddHHmmss")
						.format(new java.util.Date(timeStampSec * 1000));
				String timetemp2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new java.util.Date(timeStampSec * 1000));

				java.util.Random rand = new java.util.Random();

				// 支付参数
				java.util.Map<String, Object> data = new java.util.HashMap<String, Object>();
				data.put("REQ_SANDBOX", "0"); // TODO 是否开启测试模式 注意秘钥是否对应
				data.put("REQ_EMAIL", "gm@soarmall.com"); // TODO 需要换成自己的
															// 商户邮箱
															// 商户后台申请的邮箱
				data.put("REQ_TIMES", timestamp); // 请求时间
				data.put("REQ_INVOICE", order_num); // 订单号
				data.put("BIL_METHOD", paymen_mode); // 请求方式 C01:信用卡支付
				data.put("CUS_EMAIL", email); // 客户邮箱
				data.put("BIL_PRICE", total_price); // 价格
				data.put("BIL_CURRENCY", "SAR"); // 币种
				data.put("BIL_CC3DS", "1"); // 是否开启3ds 1 开启 0 不开启
				data.put("URL_SUCCESS", URL_SUCCESS); // 支付成功跳转页面
				data.put("URL_FAILED", URL_FAILED); // 支付失败跳转页面
				// 获取服务器域名request.getScheme() + "://" + request.getServerName()
				String system_domain = CommUtil.generic_domain(request);
				system_domain = request.getScheme() + "://" + system_domain + "gateway/transaction/index.json?order_number=" + order_num;
				data.put("URL_NOTIFY", system_domain); // 异步回调跳转页面

				data.put("IFS_MODE", IFS_MODE);
				data.put("IFS_URL", IFS_URL);

				// $data['BIL_PRCCODE'] = 0; //电话支付相关参数 信用卡不需要填写
				// 更多支付参数请参考文档 经典模式->附录2：付款请求参数表
				// 签名
				String url = data.get("REQ_SANDBOX") == "1" ? sandbox_url : live_url;// 根据REQ_SANDBOX调整地址
				String key = data.get("REQ_SANDBOX") == "1" ? sandbox_key : live_key;// 根据REQ_SANDBOX调整秘钥

				String reg_sign = key + data.get("REQ_TIMES") + data.get("REQ_EMAIL") + data.get("REQ_INVOICE")
						+ data.get("CUS_EMAIL") + data.get("BIL_METHOD") + data.get("BIL_PRICE")
						+ data.get("BIL_CURRENCY");
				data.put("REQ_SIGN", getSHA256StrJava(reg_sign));
				// 新增参数
				Map params = new HashMap();
				params.put("order_id", order_num);
				List<OrderForm> orders = this.orderService
						.query("SELECT obj FROM OrderForm obj WHERE obj.order_id=:order_id", params, -1, -1);
				if (orders.size() > 0) {
					OrderForm obj = orders.get(0);
					if (obj != null && obj.getOrder_main() == 1) {
						 data.put("REQ_MERCHANT", "Soarmall");// obj.getStore_name() 
						 //展示在付款页面的商户名称（默认为商户域名）
						// data.put("CUS_COUNTRY", obj.getReceiver_state());
						 //付款人所在国家代码（ISO3166两字母）
						// data.put("CUS_STATE", obj.getReceiver_area());
						 // 付款人所在州/县/区域
						// data.put("CUS_CITY", obj.getReceiver_city());
						 // 付款人所在城市
						// data.put("CUS_ADDRESS", obj.getReceiver_city());//
						 // 付款人所在地址
						 //data.put("CUS_POSTAL", null);// 付款人所属邮编
						 //data.put("CUS_PHONE", obj.getReceiver_mobile());
						 //付款人的座机号码（国际区号|号码）
						 data.put("CUS_MOBILE", obj.getReceiver_mobile());
						 //付款人的手机号码（国际区号|号码）
						 //data.put("CUS_IMUSR", obj.getReceiver_telephone());
						 // 付款人的即时通信用户名（类型:用户名）
						data.put("CUS_FNAME", "CHANG");// 付款人名
						data.put("CUS_LNAME", "WANG");// 付款人姓
						// data.put("CUS_REGISTER",
						// String.valueOf(obj.getAddTime().getTime() / 1000));//
						// 付款人在商户系统中的注册时间（UNIX时间戳）
						List<Map> maps = orderFormTools.queryGoodsInfo(obj.getGoods_info());
						StringBuilder str = new StringBuilder();
						int number = 1;
						for (Map map1 : maps) {
							if (maps.size() - 1 == maps.indexOf(map1)) {
								number = number - 1;
							}
							str.append(String.valueOf(map1.get("goods_name")) + ";");
							number += Integer.parseInt(map1.get("goods_count").toString());
						}
						data.put("BIL_GOODSNAME", str);// 展示在付款页面的商品描述
						// data.put("BIL_QUANTITY", number);// 商品数量
					}
				}
				logger.info("This is Payment: ==================== " + data);
				java.io.File file = new java.io.File("");
				String filePath = file.getCanonicalPath();

				try {
					java.io.File files = new java.io.File(filePath + "\\ccDirect.log");
					java.io.Writer outfile = new java.io.FileWriter(files, true);
					outfile.write(timetemp2 + "\r\n");
					outfile.write(url + "\r\n");
					for (Entry<String, Object> entry : data.entrySet()) {
						outfile.write(entry.getKey() + ": " + entry.getValue() + "\r\n");
					}
					outfile.close();

					String param = "";
					for (Entry<String, Object> entry : data.entrySet()) {
						System.out.println(entry.getKey() + " : " + entry.getValue());
						param += entry.getKey() + "=" + entry.getValue() + "&";
					}
					param = param.substring(0, param.length() - 1);
					// post请求
					String result = sendPost(url, param);
					
					com.alibaba.fastjson.JSONObject parseData = com.alibaba.fastjson.JSONObject.parseObject(result);
					if (parseData.getString("REQ_ERROR") != null) {
						System.out.println("<pre>");
						for (String keystr : parseData.keySet()) {
							System.out.println(keystr + ": " + parseData.get(keystr) + "<br/>");
						}
						System.out.println("</pre>");
						code = 4100;
						msg = "Error";
						return JSONObject.toJSONString(new Result(code, URL_NOTIFY, parseData));
					}
					outfile = new java.io.FileWriter(files, true);
					outfile.write("\r\n");
					outfile.write(result + "\r\n");
					for (String keystr : parseData.keySet()) {
						outfile.write(keystr + ": " + parseData.get(keystr) + "\r\n");
						map.put(keystr, parseData.get(keystr));
					}
					outfile.close();

					if (result.length() > 0 && !parseData.isEmpty()) {

						String URL_PAYMENT = "";
						if (!map.isEmpty()) {
							URL_PAYMENT = map.get("URL_PAYMENT").toString();
							String gc_id = map.get("TNS_GCID").toString();
							if (!CollectionUtils.isEmpty(orders)) {
								OrderForm order = orders.get(0);
								order.setUrl_payment(URL_PAYMENT);
								order.setPayment_date(new Date());
								order.setTest(String.valueOf(timeStampSec * 1000));
								order.setGc_id(gc_id);
								this.orderService.update(order);
							}
						}
						return JSONObject.toJSONString(new Result(4200, "Successfully", JSONObject.toJSON(map)));
						// response.sendRedirect(parseData.getString("URL_PAYMENT"));
					} else {
						for (String keystr : parseData.keySet()) {
							System.out.println(keystr + ": " + parseData.get(keystr) + "<br/>");
						}
						System.out.println("</pre>");
						code = 4100;
						msg = "Error";
						return JSONObject.toJSONString(new Result(code, URL_NOTIFY, parseData));
					}
				} catch (IndexOutOfBoundsException e) {
					return JSONObject.toJSONString(new Result(4705, "Error", e.getMessage()));
				} catch (Exception e) {
					return JSONObject.toJSONString(new Result(4705, "Error", e.getMessage()));
				}
			} else {
				return JSONObject.toJSONString(new Result(4400, "Email format error"));
			}
		} else {
			return JSONObject.toJSONString(new Result(4403, "Email is empty"));
		} // 商户后台查看//
	}

	private String getSHA256StrJava(String str) {
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

	public String sendPost(String url, String param) {
		// 根据实际请求需求进行参数封装
		java.io.PrintWriter out = null;
		java.io.BufferedReader in = null;
		String result = "";
		try {
			java.net.URL realUrl = new java.net.URL(url);
			// 打开和URL之间的连接
			java.net.URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new java.io.PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
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
	

	// 付款状态通知（PSN） 线支付回调控制
	@RequestMapping("gateway/transaction/index.json")
	@ResponseBody
	public void pns(HttpServletRequest request, HttpServletResponse response, String order_number) {
		logger.info("order_number: " + order_number);
		String message = "";
		Map params = new HashMap();
		params.put("order_number", order_number);
		OrderForm obj = this.orderService
				.query("SELECT obj FROM OrderForm obj WHERE obj.order_id=:order_number", params, -1, -1).get(0);
		if (obj != null) {
			List<Long> cart_order_ids = new ArrayList<Long>();// 记录订单id
																// 发送邮件到用户邮箱
			String BIL_STATUS = request.getParameter("BIL_STATUS");
			int order_status = 10;
			try {
				switch (BIL_STATUS) {
				case "unpaid":// 交易未付款
					order_status = 10;
					break;
				case "paid":// 交易已付款
					order_status = 20;
					break;
				case "pending":// 付款处理中（非实时到帐或处理延迟）
					order_status = 110;
					break;
				case "cancelled":// 付款已取消（由付款人操作）
					order_status = 95;
					break;
				case "failed": // 付款失败
					order_status = 115;
					break;
				case "refunding":// 退款处理中
					order_status = 100;
					break;
				case "refunded":// 已全额退款
					order_status = 80;
					break;
				case "complaint":// 付款存在争议（由付款人发起）
					order_status = 105;
					break;
				case "chargeback":// 交易已拒付/撤单
					order_status = 21;
					break;
				default:
					order_status = 10;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				obj.setTest("params is null");
			}
			obj.setOrder_status(order_status);// 更新订单状态，并更新商品库存
			// obj.setTest(request.getParameter("REQ_TIMES").toString());
			User user = this.userService.getObjById(CommUtil.null2Long(obj.getUser_id()));
			if (order_status == 20) {
				obj.setPayTime(new Date());
				// 1. 更新商品库存
				this.appCartViewTools.updateGoodsInventory(obj);// 更新主订单商品库存、在线支付完成，更新库存

				// 2. 记录订单日志
				OrderFormLog main_ofl = new OrderFormLog();
				main_ofl.setAddTime(new Date());
				main_ofl.setLog_info("Glocash在线支付");
				main_ofl.setLog_user(user);
				main_ofl.setOf(obj);
				this.orderFormLogService.save(main_ofl);
				// 3. 增加店铺日下单数
				StoreLog storeLog = this.storeLogTools.getTodayStoreLog(CommUtil.null2Long(obj.getStore_id()));
				/*
				 * storeLog.setPlaceorder(storeLog.getPlaceorder() + 1); if
				 * (this.orderFormTools.queryOrder(obj.getStore_name())) {
				 * storeLog.setRepetition(storeLog.getRepetition() + 1); }
				 * this.storeLogService.update(storeLog);
				 */

				cart_order_ids.add(obj.getId());

				if (obj.getOrder_main() == 1 && !CommUtil.null2String(obj.getChild_order_detail()).equals("")) {
					List<Map> maps = this.orderFormTools.queryGoodsInfo(obj.getChild_order_detail());
					for (Map map : maps) {
						OrderForm child_order = this.orderService.getObjById(CommUtil.null2Long(map.get("order_id")));
						child_order.setPayTime(new Date());
						child_order.setOrder_status(order_status);
						this.orderService.update(child_order);

						OrderFormLog child_ofl = new OrderFormLog();
						child_ofl.setAddTime(new Date());
						child_ofl.setLog_info("Glocash在线支付");
						child_ofl.setLog_user(user);
						child_ofl.setOf(child_order);
						this.orderFormLogService.update(child_ofl);

						StoreLog log = this.storeLogTools.getTodayStoreLog(CommUtil.null2Long(obj.getStore_id()));

						cart_order_ids.add(child_order.getId());
					}
				}

				// 4. 赠送抽奖次数
				int lucky = appLuckyDrawTools.getLuckyDraw();
				user.setRaffle(user.getRaffle() + lucky);
				this.userService.update(user);

				// 5. 记录订单Id、支付完成发送邮件提示
			}

			// 封装支付回调数据
			Map<String, String[]> map = request.getParameterMap();
			for (Object key : map.keySet()) {
			}
			message= "Success";
			response.setStatus(200);
		} else {
			response.setStatus(400);
			message = "Error";
		}
		this.orderService.update(obj);
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().print(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
