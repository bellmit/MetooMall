package com.metoo.module.app.manage.buyer.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.metoo.core.domain.virtual.SysMap;
import com.metoo.core.mv.JModelAndView;
import com.metoo.core.query.support.IPageList;
import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.OrderForm;
import com.metoo.foundation.domain.Store;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.domain.query.OrderFormQueryObject;
import com.metoo.foundation.service.IOrderFormService;
import com.metoo.foundation.service.IStoreService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.IUserConfigService;
import com.metoo.foundation.service.IUserService;
import com.metoo.manage.admin.tools.OrderFormTools;
import com.metoo.modul.app.desig.pattern.OrderFormFactory;
import com.metoo.module.app.buyer.domain.Result;

import net.sf.json.JSONArray;

/**
 * <p>
 * Description: 用户订单管理类，多店铺合单
 * </p>
 * 
 * @author 46075
 *
 */
@Controller
@RequestMapping("/app/v2/")
public class AppOrderBuyerActionV2 {

	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserConfigService userConfigService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IOrderFormService orderFormService;
	@Autowired
	private OrderFormTools orderFormTools;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private OrderFormFactory orderFormFactory;

	@RequestMapping(value = "order.json", method = RequestMethod.POST)
	public void orderV2(HttpServletRequest request, HttpServletResponse response, String currentPage, String order_id,
			String beginTime, String endTime, String order_status, String token, String name, String language) {
		Map<String, Object> map = new HashMap<String, Object>();
		Result result = null;
		ModelAndView mv = new JModelAndView("", configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		OrderFormQueryObject ofqo = new OrderFormQueryObject(currentPage, mv, "addTime", "desc");
		if (token.equals("")) {
			result = new Result(-100, "token Invalidation");
		} else {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user == null) {
				result = new Result(-100, "token Invalidation");
			} else {
				ofqo.addQuery("obj.user_id", new SysMap("user_id", user.getId().toString()), "=");
				ofqo.addQuery("obj.order_main", new SysMap("order_main", 1), "=");// 只显示主订单,通过主订单完成子订单的加载[是否为主订单，1为主订单，主订单用在买家用户中心显示订单内容]
				// ofqo.addQuery("obj.order_cat", new SysMap("order_cat", 0),
				// "=");//[ 订单分类，0为购物订单，1为手机充值订单 2为生活类团购订单 3为商品类团购订单 4旅游报名订单]
				List<Integer> order_cart = new ArrayList<Integer>();
				order_cart.add(5);
				order_cart.add(0);
				order_cart.add(6);
				ofqo.addQuery("obj.order_cat", new SysMap("order_cat", order_cart), "in");
				ofqo.setPageSize(20);// 设定分页查询，每页24件商品
				if (!CommUtil.null2String(order_id).equals("")) {
					ofqo.addQuery("obj.order_id", new SysMap("order_id", "%" + order_id + "%"), "like");
				}
				if (!CommUtil.null2String(beginTime).equals("")) {
					ofqo.addQuery("obj.addTime", new SysMap("beginTime", CommUtil.formatDate(beginTime)), ">=");
				}
				if (!CommUtil.null2String(endTime).equals("")) {
					String ends = endTime + " 23:59:59";
					ofqo.addQuery("obj.addTime",
							new SysMap("endTime", CommUtil.formatDate(ends, "yyyy-MM-dd hh:mm:ss")), "<=");
				}
				String or_status = null;
				if (!CommUtil.null2String(order_status).equals("")) {
					List<Integer> str = new ArrayList<Integer>();
					if (order_status.equals("order_submit")) {
						ofqo.addQuery("obj.order_status", new SysMap("order_status", 10), "=");
						or_status = "10";
					}
					if (order_status.equals("order_pay")) {
						ofqo.addQuery("obj.order_status", new SysMap("order_status", 20), "=");
						or_status = "20";
					}
					if (order_status.equals("order_shipping")) {
						ofqo.addQuery("obj.order_status", new SysMap("order_status", 30), "=");
						or_status = "30";
					}
					/*
					 * if (order_status.equals("payOnDelivery")) {
					 * ofqo.addQuery("obj.order_status", new
					 * SysMap("order_status", 16), "="); or_status = "16,20"; }
					 */
					if (order_status.equals("pending")) {
						str.add(16);
						str.add(20);
						ofqo.addQuery("obj.order_status", new SysMap("order_status", str), "in");

					}
					if (order_status.equals("unpaid")) {
						ofqo.addQuery("obj.order_status", new SysMap("order_status", 10), "=");
					}
					if (order_status.equals("order_receive")) {
						ofqo.addQuery("obj.order_status", new SysMap("order_status", 40), "=");
						or_status = "40";
					}
					if (order_status.equals("order_finish")) {
						str.clear();
						str.add(50);
						str.add(65);
						ofqo.addQuery("obj.order_status", new SysMap("order_status", str), "in");
						or_status = "50";
					}
					if (order_status.equals("order_cancel")) {
						ofqo.addQuery("obj.order_status", new SysMap("order_status", 0), "=");
						or_status = "0";
					}
				} else {
					ofqo.addQuery("obj.order_status", new SysMap("order_status", 90), "!=");
				}

				map.put("order_status", order_status);
				map.put("imageWebServer", this.configService.getSysConfig().getImageWebServer());
				IPageList pList = this.orderFormService.list(ofqo);
				List<OrderForm> orders = pList.getResult();
				List orderList = new ArrayList();
				for (OrderForm order : orders) {
					boolean flag = true;
					if (!CommUtil.null2String(name).equals("")) {
						flag = this.orderFormTools.verifyGoodsName(order, name);
					}
					if (flag) {
						Map orderMap = new HashMap();
						Store store = this.storeService.getObjById(CommUtil.null2Long(order.getStore_id()));
						orderMap.put("sotre_id", store.getId());
						orderMap.put("sotre_name", store.getStore_name());
						orderMap.put("sotre_logo", store.getStore_logo() != null
								? store.getStore_logo().getPath() + "/" + store.getStore_logo().getName() : "");
						orderMap.put("goods", this.orderFormTools.queryGoodsInfo(order.getGoods_info()));
						orderMap.put("order_id", order.getId());
						orderMap.put("order_number", order.getOrder_id());
						orderMap.put("order_pay_type", order.getPayType());
						orderMap.put("order_status", order.getOrder_status());
						orderMap.put("payment_amount", order.getPayment_amount());
						orderMap.put("usd_payment_amount", order.getUsd_payment_amount());
						if (order.getPayType().equals("online") && order.getOrder_status() == 10) {
							if (order.getPayment_date() != null) {
								Long millisecond1 = order.getPayment_date().getTime() + 31 * 60 * 1000;
								Long millisecond = Long.valueOf(order.getTest()) + 31 * 60 * 1000;
								Calendar current_time = Calendar.getInstance();
								Long time = millisecond - current_time.getTimeInMillis();
								orderMap.put("count_down", time > 0 ? time : 0);
							}
							 if(order.getUrl_payment() != null){
								 orderMap.put("url_payment", CommUtil.null2String(order.getUrl_payment()).equals("") ? "" :  order.getUrl_payment());
							 }
						}
						orderMap.put("childOrder", this.orderFormTools.queryGoodsInfo(order.getChild_order_detail()));
						orderList.add(orderMap);
					}
				}
				map.put("orderlist", orderList);
				int[] status = new int[] { 0, 10, 16, 30, 40 };
				String[] string_status = new String[] { "order_cencel", "order_submit", "order_pending",
						"order_shipping", "order_finish" };
				Map<String, Object> orders_status = new LinkedHashMap<String, Object>();
				List<Map<String, Object>> statusmaps = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < status.length; i++) {
					Map statusmap = new LinkedHashMap();
					int size = this.orderFormService.query("select obj.id from OrderForm obj where obj.user_id="
							+ user.getId().toString() + " and obj.order_status =" + status[i] + "", null, -1, -1)
							.size();
					statusmap.put("order_size_" + status[i], size);
					statusmap.put(string_status[i], size);
					statusmaps.add(statusmap);
				}
				map.put("orderstatus", statusmaps);
				map.put("order_Pages", pList.getPages());
				Map user_map = new HashMap();
				user_map.put("user_id", user.getId());
				user_map.put("user_id", user.getUserName());
				user_map.put("user_email", user.getEmail());
				user_map.put("user_sex", user.getSex());
				map.put("user", user_map);
				result = new Result(4200, "Success", map);
			}
		}
		response.setContentType("application/json; charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().println(Json.toJson(result, JsonFormat.compact()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		/*
		 * Date date = new Date(); System.out.println("time: "+ date.getTime());
		 * Calendar calendar = Calendar.getInstance(); System.out.println(
		 * "time2: "+ calendar.getTime()); System.out.println("time_time:" +
		 * calendar.getTime().getTime());
		 */	
		
		Date time = new Date(1842094);
		SimpleDateFormat formats = new SimpleDateFormat("hh:mm:ss");
		System.out.println("data: " + formats.format(time));
	}

	// @RequestMapping(value = "order.json", method = RequestMethod.POST)
	public void orderV2_1(HttpServletRequest request, HttpServletResponse response, String currentPage, String order_id,
			String beginTime, String endTime, String order_status, String token, String name, String language) {
		Map<String, Object> map = new HashMap<String, Object>();
		Result result = null;
		ModelAndView mv = new JModelAndView("user/default/usercenter/buyer_order.html", configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		OrderFormQueryObject ofqo = new OrderFormQueryObject(currentPage, mv, "addTime", "desc");
		if (token.equals("")) {
			result = new Result(-100, "token Invalidation");
		} else {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user == null) {
				result = new Result(-100, "token Invalidation");
			} else {
				ofqo.addQuery("obj.user_id", new SysMap("user_id", user.getId().toString()), "=");
				ofqo.addQuery("obj.order_main", new SysMap("order_main", 1), "=");// 只显示主订单,通过主订单完成子订单的加载[是否为主订单，1为主订单，主订单用在买家用户中心显示订单内容]
				// ofqo.addQuery("obj.order_cat", new SysMap("order_cat", 0),
				// "=");//[ 订单分类，0为购物订单，1为手机充值订单 2为生活类团购订单 3为商品类团购订单 4旅游报名订单]
				List<Integer> order_cart = new ArrayList<Integer>();
				order_cart.add(5);
				order_cart.add(0);
				ofqo.addQuery("obj.order_cat", new SysMap("order_cat", order_cart), "in");
				ofqo.setPageSize(20);// 设定分页查询，每页24件商品
				if (!CommUtil.null2String(order_id).equals("")) {
					ofqo.addQuery("obj.order_id", new SysMap("order_id", "%" + order_id + "%"), "like");
				}
				if (!CommUtil.null2String(beginTime).equals("")) {
					ofqo.addQuery("obj.addTime", new SysMap("beginTime", CommUtil.formatDate(beginTime)), ">=");
				}
				if (!CommUtil.null2String(endTime).equals("")) {
					String ends = endTime + " 23:59:59";
					ofqo.addQuery("obj.addTime",
							new SysMap("endTime", CommUtil.formatDate(ends, "yyyy-MM-dd hh:mm:ss")), "<=");
				}
				String or_status = null;
				if (!CommUtil.null2String(order_status).equals("")) {
					if (order_status.equals("order_submit")) {
						ofqo.addQuery("obj.order_status", new SysMap("order_status", 10), "=");
						or_status = "10";
					}
					if (order_status.equals("order_pay")) {
						ofqo.addQuery("obj.order_status", new SysMap("order_status", 20), "=");
						or_status = "20";
					}
					if (order_status.equals("order_shipping")) {
						ofqo.addQuery("obj.order_status", new SysMap("order_status", 30), "=");
						or_status = "30";
					}
					if (order_status.equals("payOnDelivery")) {
						ofqo.addQuery("obj.order_status", new SysMap("order_status", 16), "=");
						or_status = "16";
					}
					if (order_status.equals("order_receive")) {
						ofqo.addQuery("obj.order_status", new SysMap("order_status", 40), "=");
						or_status = "40";
					}
					if (order_status.equals("order_finish")) {
						ofqo.addQuery("obj.order_status", new SysMap("order_status", 50), "=");
						ofqo.addQuery("obj.order_status", new SysMap("order_status", 65), "=");
						or_status = "50";
					}
					if (order_status.equals("order_cancel")) {
						ofqo.addQuery("obj.order_status", new SysMap("order_status", 0), "=");
						or_status = "0";
					}
				} else {
					ofqo.addQuery("obj.order_status", new SysMap("order_status", 90), "!=");
				}

				map.put("order_status", order_status);
				map.put("imageWebServer", this.configService.getSysConfig().getImageWebServer());
				IPageList pList = this.orderFormService.list(ofqo);
				List<OrderForm> orders = null;
				if (CommUtil.null2String(name).equals("")) {
					orders = pList.getResult();
				} else {
					for (OrderForm obj : orders) {
						if (this.orderFormTools.verifyGoodsName(obj, name)) {
							orders.add(obj);
						}
					}
				}
				map.put("orderlist", orderFormFactory.getGoods(orders, "consolidated"));
				int[] status = new int[] { 0, 10, 16, 30, 40 };
				String[] string_status = new String[] { "order_cencel", "order_submit", "order_pending",
						"order_shipping", "order_finish" };
				Map<String, Object> orders_status = new LinkedHashMap<String, Object>();
				List<Map<String, Object>> statusmaps = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < status.length; i++) {
					Map statusmap = new LinkedHashMap();
					int size = this.orderFormService.query("select obj.id from OrderForm obj where obj.user_id="
							+ user.getId().toString() + " and obj.order_status =" + status[i] + "", null, -1, -1)
							.size();
					statusmap.put("order_size_" + status[i], size);
					statusmap.put(string_status[i], size);
					statusmaps.add(statusmap);
				}
				map.put("orderstatus", statusmaps);
				map.put("order_Pages", pList.getPages());
				result = new Result(4200, "Success", map);
			}
		}
		response.setContentType("application/json; charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().println(Json.toJson(result, JsonFormat.compact()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @param token
	 * @param language
	 * @return
	 */
	@RequestMapping(value = "order_view.json")
	@ResponseBody
	public String getOrderView(HttpServletRequest request, HttpServletResponse response, String id, String token,
			String language) {
		int code = -1;
		String msg = "";
		Map map = new HashMap();
		Map main = new HashMap();
		List<Map> order = new ArrayList<Map>();
		if (!CommUtil.null2String(token).equals("")) {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user != null) {
				OrderForm obj = this.orderFormService.getObjById(CommUtil.null2Long(id));
				// List<Map> goodsList =
				// this.orderFormTools.queryGoodsInfo(obj.getGoods_info());
				if (obj != null && obj.getUser_id().equals(user.getId().toString())) {
					String imgWebServer = this.configService.getSysConfig().getImageWebServer();
					if (obj.getChild_order_detail() != null && !obj.getChild_order_detail().equals("")) {
						order = this.orderFormTools.queryGoodsInfo(obj.getChild_order_detail());
						// main.put("childs", JSONArray.fromObject(childs));
						/*
						 * JSONArray array =
						 * this.orderFormTools.queryChildOrder(obj.
						 * getChild_order_detail()); order.add(array);
						 */
					}
					main.put("order_goods_info", obj.getGoods_info());
					main.put("enough_free", obj.getEnough_free());
					main.put("order_id", obj.getId());
					main.put("order_status", obj.getOrder_status());
					main.put("enough_reduce_amount", obj.getEnough_reduce_amount());
					Map coupon_map = orderFormTools.queryCouponInfo(obj.getCoupon_info());
					main.put("coupon_amount", coupon_map.get("coupon_amount"));
					main.put("goods_amount", obj.getGoods_amount());
					main.put("ship_price", obj.getEnough_free() == 1 ? 0 : obj.getShip_price());
					main.put("totalPrice", obj.getEnough_free() == 1 ? obj.getGoods_amount() : obj.getTotalPrice());
					order.add(main);
					map.put("order_number", obj.getOrder_id());
					map.put("order_status", obj.getOrder_status());
					map.put("platform_ship_price", obj.getPlatform_ship_price());
					map.put("discounts_amount", obj.getDiscounts_amount());
					map.put("integral", obj.getIntegral());
					map.put("goodsVoucherPrice", obj.getGoods_voucher_price());
					/*
					 * map.put("integral_price", CommUtil.mul(obj.getIntegral(),
					 * this.configService.getSysConfig().getIntegralExchangeRate
					 * ()));
					 */
					map.put("order_coupon_amount", obj.getCoupon_amount());
					map.put("order_goods_amount",
							CommUtil.add(CommUtil.subtract(obj.getPayment_amount(), obj.getPlatform_ship_price()), obj.getGoods_voucher_price()));
					map.put("payment_amount", obj.getPayment_amount());
					map.put("usd_payment_amount", obj.getUsd_payment_amount());
					map.put("imgWebServer", imgWebServer);
					// 店铺
					Store store = this.storeService.getObjById(CommUtil.null2Long(obj.getStore_id()));
					Map storeMap = new HashMap();
					main.put("store_name", store.getStore_name());
					main.put("store_id", store.getId());
					main.put("store_enough_free", store.getEnough_free());
					main.put("store_enough_free_price", store.getEnough_free_price());
					main.put("store_logo",
							store.getStore_logo() == null ? ""
									: this.configService.getSysConfig().getImageWebServer() + "/"
											+ store.getStore_logo().getPath() + "/" + store.getStore_logo().getName());
					// main.put("store", storeMap);

					// 收货地址
					map.put("Transport", obj.getTransport());
					map.put("Receiver_Name", obj.getReceiver_Name());
					map.put("Receiver_area", obj.getReceiver_area());
					map.put("Receiver_area_info", obj.getReceiver_area_info());
					map.put("Receiver_zip", obj.getReceiver_zip());
					map.put("Receiver_telephone", obj.getReceiver_telephone());
					map.put("Receiver_mobile", obj.getReceiver_mobile());

					map.put("shipCode", obj.getShipCode());
					map.put("AddTime", obj.getAddTime());
					map.put("ShipTime", obj.getShipTime());
					map.put("ConfirmTime", obj.getConfirmTime());
					map.put("FinishTime", obj.getFinishTime());
					map.put("order", order);
					code = 4200;
					msg = "Success";
				} else {
					code = 4205;
					msg = "No information was found";
				}
			} else {
				code = -100;
				msg = "token Invalidation";
			}
		} else {
			code = -100;
			msg = "token Invalidation";
		}
		return Json.toJson(new Result(code, msg, map), JsonFormat.compact());
	}

}
