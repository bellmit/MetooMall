package com.metoo.modul.app.game.view.tree;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.ResponseUtils;
import com.metoo.foundation.domain.Address;
import com.metoo.foundation.domain.Area;
import com.metoo.foundation.domain.CouponInfo;
import com.metoo.foundation.domain.GameAward;
import com.metoo.foundation.domain.GameAwardInfo;
import com.metoo.foundation.domain.GameGoods;
import com.metoo.foundation.domain.Goods;
import com.metoo.foundation.domain.GoodsVoucher;
import com.metoo.foundation.domain.GoodsVoucherInfo;
import com.metoo.foundation.domain.OrderForm;
import com.metoo.foundation.domain.OrderFormLog;
import com.metoo.foundation.domain.PlantingTrees;
import com.metoo.foundation.domain.Store;
import com.metoo.foundation.domain.SubTrees;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IAddressService;
import com.metoo.foundation.service.IAreaService;
import com.metoo.foundation.service.ICouponInfoService;
import com.metoo.foundation.service.IGameAwardInfoService;
import com.metoo.foundation.service.IGameAwardService;
import com.metoo.foundation.service.IGameGoodsService;
import com.metoo.foundation.service.IGoodsService;
import com.metoo.foundation.service.IGoodsVoucherInfoService;
import com.metoo.foundation.service.IGoodsVoucherService;
import com.metoo.foundation.service.IOrderFormLogService;
import com.metoo.foundation.service.IOrderFormService;
import com.metoo.foundation.service.ISubTreesService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.IUserService;
import com.metoo.manage.seller.tools.TransportTools;
import com.metoo.modul.app.game.tree.tools.AppGameAwardTools;
import com.metoo.module.app.buyer.domain.Result;
import com.metoo.module.app.manage.buyer.tool.AppAddressBuyerTools;
import com.metoo.module.app.view.web.tool.AppCartViewTools;
import com.metoo.module.app.view.web.tool.AppGoodsViewTools;

import net.sf.json.JSONArray;

/**
 * <p>
 * Title: AppGameAwardViewAction.java
 * </p>
 * 
 * <p>
 * Description: 小游戏升级奖励控制器
 * </p>
 * 
 * @author 46075
 *
 */
@Controller
@RequestMapping("app/v1/game/tree/award")
public class AppGameTreeAwardViewAction {

	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IGameAwardInfoService gameAwardInfoService;
	@Autowired
	private ICouponInfoService couponInfoService;
	@Autowired
	private AppGameAwardTools appGameAwardTools;
	@Autowired
	private ISubTreesService subTreesService;
	@Autowired
	private IGameAwardService gameAwardService;
	@Autowired
	private IGoodsVoucherService GoodsvoucherService;
	@Autowired
	private IGoodsVoucherInfoService goodsVoucherInfoService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IGameGoodsService gameGoodsService;
	@Autowired
	private IAddressService addressService;
	@Autowired
	private AppAddressBuyerTools appAddressBuyerTools;
	@Autowired
	private IAreaService areaService;
	@Autowired
	private AppCartViewTools appCartViewTools;
	@Autowired
	private TransportTools transportTools;
	@Autowired
	private IOrderFormLogService orderFormLogService;
	@Autowired
	private IOrderFormService orderService;
	@Autowired
	private AppGoodsViewTools appGoodsViewTools;
	
	/**
	 * 礼品盒子
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping("/gift_box.json")
	@ResponseBody
	public Object giftBox(HttpServletRequest request, HttpServletResponse response, String token) {
		Map data = new HashMap();
		Map params = new HashMap();
		if (!CommUtil.null2String(token).equals("")) {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if(user != null){
				params.put("type", 2);
				List<GameAward> gameAwards = this.gameAwardService.query(
						"SELECT obj FROM GameAward obj where obj.type=:type",
						params, -1, -1);
				// 查询抵用券总金额、可以有多種查詢方式
				params.clear();
				/*params.put("user_id", user.getId());
				params.put("voucher_type", 1);// 抵用券类型
				params.put("voucher_status", 0);// 未过期/未使用用户抵用券
				params.put("status", 1);// 已领取奖品
				List<GameAwardInfo> goodsVouchers = this.gameAwardInfoService
															.query("SELECT obj FROM GameAwardInfo obj "
																	+ "WHERE obj.user.id=:user_id "
																	+ "AND obj.status=:status "
																	+ "AND obj.goods_voucher.type=:voucher_type "
																    + "AND obj.goods_voucher_info.status=:voucher_status", params, -1, -1);*/
				params.put("user_id", user.getId());
				params.put("status", 0);
//				params.put("type", 1);
				List<GoodsVoucherInfo> goodsVouchers = this.goodsVoucherInfoService
						.query("select obj from GoodsVoucherInfo obj "
								+ "WHERE obj.user.id=:user_id "
								+ "AND obj.status=:status ", params, -1, -1);
				// " + "AND obj.goods_voucher.type=:type
				
				double price = 0;
				for(GoodsVoucherInfo obj : goodsVouchers){
					price = CommUtil.add(price, obj.getGoods_voucher().getNumber());
				}
				// 商品碎片
				//1. 查询所有商品
				List<Map> goods_infos = new ArrayList<Map>();
				if(gameAwards.size() > 0){
					GameAward gameAward = gameAwards.get(0);
					/*params.clear();
					params.put("gameAward_id", gameAward.getId());
					List<GameGoods> gameGoodsList = this.gameGoodsService.query("SELECT obj "
													+ "FROM GameGoods obj "
													+ "WHERE obj.gameAward.id=:gameAward_id ORDER BY obj.sequence DESC", params, -1, -1);*/
					List<GameGoods> gameGoodsList = gameAward.getGameGoodsList();
					
					for(GameGoods gameGoods : gameGoodsList){
						Map map = new HashMap();
						params.clear();
						params.put("user_id", user.getId());
						params.put("status", 1);
						params.put("gameGoods_id", gameGoods.getId());
						List<GameAwardInfo> gameAwardInfos = this.gameAwardInfoService.query("SELECT obj "
															+ "FROM GameAwardInfo obj "
															+ "WHERE obj.user.id=:user_id "
															+ "AND obj.status=:status "
															+ "AND obj.gameGoods.id=:gameGoods_id", params, -1, -1);
						int number = 0;
						for(GameAwardInfo gameAwardInfo : gameAwardInfos){
							/*Map goods_info = Json.fromJson(Map.class, gameAwardInfo.getAward());
							debris = new Double(CommUtil.add(debris, goods_info.get("debris"))).intValue();*/
							System.out.println(gameAwardInfo.getGameGoods().getId());
							number = new Double(CommUtil.add(number, gameAwardInfo.getGameGoods().getNumber())).intValue();
						}
						
						map.put("id", gameGoods.getId());
						map.put("goods_detail", gameGoods.getGoods_detail());
						map.put("accessory", this.configService.getSysConfig().getImageWebServer() + "/" + gameGoods.getAccessory());
						map.put("number", number);
						map.put("debris_totals", gameGoods.getDebris_totals());
						map.put("probably", gameGoods.getProbably());
						map.put("rate_progress", CommUtil.div(number, gameGoods.getDebris_totals()));
						map.put("goods_id", gameGoods.getGoods_id());
						map.put("game_id", gameAward.getId());
						goods_infos.add(map);
					}
				}
				
				data.put("price", price);
				data.put("giftBox", goods_infos);
				data.put("domain", this.configService.getSysConfig().getImageWebServer());
			}
		}
		return new Result(4200, "Success", data);
	}
	
	/**
	 * 商品碎片属性
	 * @param id 碎片ID
	 * @return
	 */
	@RequestMapping("/debris.json")
	@ResponseBody
	public Object debris(String id){
		GameGoods gameGoods = this.gameGoodsService.getObjById(CommUtil.null2Long(id));
		if(gameGoods != null){
			Goods goods = this.goodsService.getObjById(gameGoods.getGoods_id());
			if(goods != null && goods.getGoods_status() == 0){
				Map map = this.appGoodsViewTools.goods_detail(goods.getId());
				return ResponseUtils.ok(map);
			}
		}
		return ResponseUtils.ok();
	}
	
	/**
	 * 领取奖励
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @param game_user_award_id
	 * @return
	 */
//	@RequestMapping("getAward.json")
	@ResponseBody
	public Object getAward(HttpServletRequest request, HttpServletResponse response, String token,
			String game_user_award_id) {
		int code = 4200;
		String msg = "Successfully";
		if (!CommUtil.null2String(token).equals("")) {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user != null) {
				GameAwardInfo GameAwardInfo = null;
				GameAward gameAward = this.gameAwardService.getObjById(Long.parseLong(game_user_award_id));
				if (gameAward != null && gameAward.getType() == 1) {
					GameAwardInfo = new GameAwardInfo();
					GameAwardInfo.setAddTime(new Date());
					GameAwardInfo.setUser(user);
					GameAwardInfo.setStatus(0);
					GameAwardInfo.setGameAward(gameAward);
					this.gameAwardInfoService.save(GameAwardInfo);
				} else {
					GameAwardInfo = this.gameAwardInfoService.getObjById(CommUtil.null2Long(game_user_award_id));
					if (GameAwardInfo != null && GameAwardInfo.getStatus() == 0
							&& GameAwardInfo.getUser().getId().equals(user.getId())) {
						gameAward = GameAwardInfo.getGameAward();
					}
				}

				if (gameAward != null) {
					int water = Integer.parseInt(gameAward.getWater());
					user.setWater_drops_unused(user.getWater_drops_unused() + water);
					if (gameAward.getCoupon() != null) {
						if (gameAward.getCoupon().getCoupon_begin_time()
								.after(gameAward.getCoupon().getCoupon_end_time())
								&& gameAward.getCoupon().getCoupon_end_time().after(new Date())) {
							CouponInfo info = new CouponInfo();
							info.setAddTime(new Date());
							info.setCoupon(gameAward.getCoupon());
							info.setUser(user);
							info.setCoupon_sn(UUID.randomUUID().toString());
							this.couponInfoService.save(info);
						}
					}
					GameAwardInfo.setStatus(1);
					this.gameAwardInfoService.update(GameAwardInfo);
				}
			} else {
				code = -100;
				msg = "token Invalidation";
			}
		} else {
			code = -100;
			msg = "token Invalidation";
		}
		return new Result(code, msg);
	}

	/**
	 * 领取奖励-随机奖品
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @param game_user_award_id
	 * @return
	 */
	@RequestMapping("getAward.json")
	@ResponseBody
	public Object getAwardV2(HttpServletRequest request, HttpServletResponse response, String token,
			String id) {
		int code = 4200;
		String msg = "Successfully";
		if (!CommUtil.null2String(token).equals("")) {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user != null) {
				//GameAward gameAward = this.gameAwardService.getObjById(Long.parseLong(game_award_id));
				GameAwardInfo gameAwardInfo = this.gameAwardInfoService.getObjById(CommUtil.null2Long(id));
				if (gameAwardInfo != null && gameAwardInfo.getStatus() == 0) {
					GameAward gameAward = gameAwardInfo.getGameAward();
					if(gameAward != null){
						boolean flag = this.appGameAwardTools.recevieAward(gameAward, gameAwardInfo, user);
						gameAwardInfo.setStatus(1);
						gameAwardInfo.setReceiveTime(new Date());
						this.gameAwardInfoService.update(gameAwardInfo);
						return ResponseUtils.ok(flag);
					}else{
						return ResponseUtils.badArgumentValue();
					}
				}else{
					return ResponseUtils.badArgumentValue();
				}
			} else {
				return ResponseUtils.unlogin();
			}
		} else {
			return ResponseUtils.unlogin();
		}
	}

	/**
	 * 查看当前阶段奖励
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping("checkReward.json")
	@ResponseBody
	public Result getAward(HttpServletRequest request, HttpServletResponse response, String token) {
		Map map = new HashMap();
		User user = null;
		String msg = "Successfully";
		int code = 4200;
		if (!CommUtil.null2String(token).equals("")) {
			user = this.userService.getObjByProperty(null, "app_login_token", token);
		}
		if (user != null) {
			// 查询用户已完成奖励
		/*	Map userAward = this.appGameAwardTools.getAward(user);
			if (userAward != null && !userAward.isEmpty()) {
				map.put("userAward", userAward);
			} else {}*/

			// 查询当前阶段奖励
			PlantingTrees plantingTree = user.getPlanting_trees();
			if (plantingTree != null) {
				SubTrees subTrees = plantingTree.getSubTree();
				if (subTrees != null) {
					Map gameAward = this.appGameAwardTools.getAward(subTrees.getGameAward());
					map.put("userAward", gameAward);
				}
			} else {
				code = 4603;
				msg = "This user has not yet selected a tree to grow";
			}
		} else {
			code = -100;
			msg = "token Invalidation";
		}
		return new Result(code, msg, map);
	}
	
	/*@RequestMapping("/verify")
	public Object verify(String id, String token){
		User user = CommUtil.verifyToken(token);
		if(user != null){
			//1, 查询碎片商品是否已上架
			GameGoods gameGoods = this.gameGoodsService.getObjById(Long.parseLong(id));
			if(gameGoods != null && gameGoods.getGoods_id()){
				
			}
			//2, 查询用户是否已及其碎片
			Map params = new HashMap();
			
			params.put("id", id);
		}
		return ResponseUtils.badArgument(-100, "token Invalidation");
	}*/

	/**
	 * 获取实物奖励
	 * 
	 * @param id
	 * @return
	 */
	/*
	 * @RequestMapping("/materialReward.json")
	 * 
	 * @ResponseBody public Object getGoods(@RequestBody GameMeterialRewardDto
	 * dto) { String msg = "Successfully"; int code = 4200; Map map = new
	 * HashMap(); Map params = new HashMap(); params.put("id",
	 * dto.getGameAward_id()); params.put("type", 2); List<GameAward>
	 * gameAwardList = this.gameAwardService .query(
	 * "SELECT obj FROM GameAward obj WHERE obj.id=:id AND obj.type=:type",
	 * params, -1, -1); // GameAward gameAward =
	 * this.gameAwardService.getObjById(dto.getGameAward_id()); User user =
	 * this.userService.getObjByProperty(null, "app_login_token",
	 * dto.getToken()); if (user != null) { if (gameAwardList.size() > 0) {
	 * GameAward gameAward = gameAwardList.get(0); if (gameAward.getGoods() !=
	 * null) { Goods goods = gameAward.getGoods(); if (goods != null) { if
	 * (gameAward.getGoods().getGoods_inventory() > 0) {
	 * 
	 * params.clear(); params.put("user_id", user.getId());
	 * params.put("gameAward_id", gameAward.getId()); params.put("status", 0);
	 * List<GameAwardInfo> GameAwardInfoList = this.gameAwardInfoService.query(
	 * "SELECT obj FROM GameAwardInfo obj WHERE obj.user.id=:user_id " +
	 * "AND obj.gameAward.id=:gameAward_id AND obj.status=:status", params, -1,
	 * -1); if(GameAwardInfoList.size() > 0){ Address address = null; if
	 * (dto.getAddress_id() != null) { address =
	 * this.addressService.getObjById(dto.getAddress_id()); } else { //
	 * 验证用户是否已有当前地址 address = (Address)
	 * this.appAddressBuyerTools.verifyAdr(user, dto.getArea_id(),
	 * dto.getArea_info(), dto.getMobile()); } if (address != null) { float
	 * ship_price = 0; if (goods.getGoods_transfee() == 0) { // 计算运费 String
	 * city_name = this.mCartViewTools.getCity(address.getArea()); ship_price =
	 * transportTools.goods_trans_fee(
	 * CommUtil.null2String(goods.getGoods_store().getTransport().getId()),
	 * "express", goods.getId(), city_name); } map.put("ship_price",
	 * ship_price); map.put("goods_id", goods.getId()); map.put("goods_name",
	 * goods.getGoods_name()); map.put("goods_accessory",
	 * this.configService.getSysConfig().getImageWebServer() + "/" +
	 * goods.getAccessory().getPath() + "/" + goods.getAccessory().getName());
	 * map.put("goods_price", goods.getGoods_current_price());
	 * map.put("store_id", goods.getGoods_store().getId());
	 * map.put("store_name", goods.getGoods_store().getStore_name());
	 * map.put("order_total_price", CommUtil.add(goods.getGoods_current_price(),
	 * ship_price)); return new Result(code, msg, map); } else { code = 4207;
	 * msg = "Please select the shipping address"; } }else{ code = 4260; msg=
	 * "Get the failure"; } } else { code = 4206; msg = "Goods in short stock";
	 * } } else { // 奖品已下架；等待奖品更新 code = 4208; msg =
	 * "The merchandise is off the shelves"; } } else { code = 4206; msg =
	 * "Goods in short stock"; } } else { code = 4260; msg= "Get the failure"; }
	 * } else { code = -100; msg = "token Invalidation"; } return new
	 * Result(code, msg); }
	 */

	@RequestMapping("/materialReward.json")
	@ResponseBody
	public Object getGoods(String token, Long gameAward_id, String userName, String mobile, String email,
			Long address_id, Long area_id, String area_info) {
		String msg = "Success";
		int code = 4200;
		Map map = new HashMap();
		// GameAward gameAward =
		// this.gameAwardService.getObjById(dto.getGameAward_id());
		if (CommUtil.null2String(token).equals("")) {
			return new Result(-100, "token Invalidation");
		}
		User user = this.userService.getObjByProperty(null, "app_login_token", token);
		if (user != null) {
			Map params = new HashMap();
			params.put("user_id", user.getId());
			params.put("id", gameAward_id);
			params.put("status", 0);
			List<GameAwardInfo> GameAwardInfoList = this.gameAwardInfoService
					.query("SELECT obj FROM GameAwardInfo obj WHERE obj.user.id=:user_id "
							+ "AND obj.id=:id AND obj.status=:status", params, -1, -1);
			if (GameAwardInfoList.size() > 0) {
				GameAwardInfo GameAwardInfo = GameAwardInfoList.get(0);
				GameAward gameAward = GameAwardInfo.getGameAward();
				if (gameAward.getGoods() != null) {
					Goods goods = gameAward.getGoods();
					if (goods != null) {
						if (gameAward.getGoods().getGoods_inventory() > 0) {
							Address address = null;
							if (address_id != null) {
								address = this.addressService.getObjById(address_id);
							} else {
								// 验证用户是否已有当前地址
								address = (Address) this.appAddressBuyerTools.verifyAdr(user, area_id, area_info,
										mobile);
							}
							Map adrMap = new HashMap();
							adrMap.put("id", address.getId());
							adrMap.put("userName", address.getTrueName());
							adrMap.put("telephone", address.getTelephone());
							adrMap.put("zip", address.getZip());
							adrMap.put("mobile", address.getMobile());
							adrMap.put("defaultVal", address.getDefault_val());
							if (address.getArea() != null) {
								Area area = this.areaService.getObjById(address.getArea().getId());
								adrMap.put("area_id", CommUtil.null2String(area.getId()));
								adrMap.put("areaInfo", address.getArea_info());
								adrMap.put("area_abbr", area.getParent().getAbbr());
								if (area.getLevel() == 2) {
									adrMap.put("country", address.getArea().getParent().getParent().getAreaName());
									adrMap.put("city", address.getArea().getParent().getAreaName());
									adrMap.put("area", address.getArea().getAreaName());
								} else if (area.getLevel() == 1) {
									adrMap.put("country", address.getArea().getParent().getAreaName());
									adrMap.put("city", address.getArea().getAreaName());
									adrMap.put("area", "");
								}
							}

							map.put("address", adrMap);
							map.put("address_id", address.getId());
							if (address != null) {
								float ship_price = 0;
								if (goods.getGoods_transfee() == 0) {
									// 计算运费
									String city_name = this.appCartViewTools.getCity(address.getArea());
									ship_price = transportTools.goods_trans_fee(
											CommUtil.null2String(goods.getGoods_store().getTransport().getId()),
											"express", goods.getId(), city_name);
								}

								map.put("ship_price", ship_price);
								map.put("goods_id", goods.getId());
								map.put("goods_name", goods.getGoods_name());
								map.put("goods_accessory",
										this.configService.getSysConfig().getImageWebServer() + "/"
												+ goods.getGoods_main_photo().getPath() + "/"
												+ goods.getGoods_main_photo().getName());
								map.put("goods_price", goods.getGoods_current_price());
								map.put("store_id", goods.getGoods_store().getId());
								map.put("store_name", goods.getGoods_store().getStore_name());
								map.put("store_logo",
										goods.getGoods_store().getStore_logo() != null
												? this.configService.getSysConfig().getImageWebServer() + "/"
														+ goods.getGoods_store().getStore_logo().getPath() + "/"
														+ goods.getGoods_store().getStore_logo().getName()
												: null);
								map.put("order_total_price", CommUtil.add(goods.getGoods_current_price(), ship_price));
								return new Result(code, msg, map);
							} else {
								code = 4207;
								msg = "Please select the shipping address";
							}
						} else {
							code = 4206;
							msg = "Goods in short stock";
						}
					} else {
						// 奖品已下架；等待奖品更新
						code = 4208;
						msg = "The merchandise is off the shelves";
					}
				} else {
					code = 4206;
					msg = "Goods in short stock";
				}
			} else {
				code = 4260;
				msg = "Get the failure";
			}
		} else {
			code = -100;
			msg = "token Invalidation";
		}
		return new Result(code, msg);
	}

	@RequestMapping("/gameAwardOrder.json")
	@ResponseBody
	public Object createOrder(String token, Long address_id, String order_type, Long gameAward_id) {
		String msg = "Success";
		int code = 4200;
		User user = CommUtil.verifyToken(token);
		if (user != null) {
			Map params = new HashMap();
			params.put("user_id", user.getId());
			params.put("id", gameAward_id);
			params.put("status", 0);
			List<GameAwardInfo> GameAwardInfoList = this.gameAwardInfoService
					.query("SELECT obj FROM GameAwardInfo obj WHERE obj.user.id=:user_id "
							+ "AND obj.id=:id AND obj.status=:status", params, -1, -1);
			// GameAward gameAward =
			// this.gameAwardService.getObjById(dto.getGameAward_id());
			if (GameAwardInfoList.size() > 0) {
				GameAwardInfo GameAwardInfo = GameAwardInfoList.get(0);
				GameAward gameAward = GameAwardInfo.getGameAward();
				Goods obj = gameAward.getGoods();
				if (obj != null) {
					Address address = this.addressService.getObjById(address_id);
					if (address != null) {
						float ship_price = 0;
						float user_ship_price = 0;
						float store_ship_price = 0;
						float order_total_price = 0;
						// 计算运费
						String city_name = this.appCartViewTools.getCity(address.getArea());
						ship_price = transportTools.goods_trans_fee(
								CommUtil.null2String(obj.getGoods_store().getTransport().getId()), "express",
								obj.getId(), city_name);
						if (obj.getGoods_transfee() == 0) {
							user_ship_price = ship_price;
							order_total_price = ship_price;
						} else {
							store_ship_price = ship_price;
						}
						CommUtil.add(obj.getGoods_current_price(), user_ship_price);

						Store store = obj.getGoods_store();

						// 封装商品信息
						List<Map> orderGoodsList = new ArrayList<Map>();
						Map goodsMap = this.appCartViewTools.orderGoods(obj, "1", "", "");
						orderGoodsList.add(goodsMap);
						double commission_amount = this.appCartViewTools.getGoodsOrderCommission(obj, 1);// 订单佣金
						double goods_vat = CommUtil.mul(obj.getGoods_current_price(), 0.05);// 服务VAT
						double commission_vat = CommUtil.mul(commission_amount, 0.05);// 佣金VAT

						// 保存订单
						OrderForm of = new OrderForm();
						of.setOrder_main(1);
						of.setAddTime(new Date());
						of.setStore_id(store.getId().toString());
						String SM = "SM" + CommUtil.randomString(5) + user.getId();
						of.setOrder_id(SM);
						// 设置收货地址信息
						of.setReceiver_Name(address.getTrueName());
						if (address.getArea().getLevel() == 2) {
							of.setReceiver_area(address.getArea().getParent().getParent().getAreaName() + " "
									+ address.getArea().getParent().getAreaName() + address.getArea().getAreaName());
							of.setReceiver_state(address.getArea().getParent().getParent().getAreaName());
							of.setReceiver_city(address.getArea().getParent().getAreaName());
							of.setReceiver_street(address.getArea().getAreaName());
						} else if (address.getArea().getLevel() == 1) {
							of.setReceiver_area(
									address.getArea().getParent().getAreaName() + address.getArea().getAreaName());
							of.setReceiver_state(address.getArea().getParent().getAreaName());
							of.setReceiver_city(address.getArea().getAreaName());
							of.setReceiver_street("");
						}
						of.setReceiver_area_info(address.getArea_info());
						of.setReceiver_mobile(address.getMobile());
						of.setReceiver_telephone(address.getTelephone());
						of.setReceiver_zip(address.getZip());
						of.setReceiver_email(address.getEmail());
						of.setTransport_type(CommUtil.null2String(obj.getGoods_transfee()));
						of.setTransport("Express");
						of.setUser_id(user.getId().toString());
						of.setUser_name(user.getUserName());
						of.setGoods_info(Json.toJson(orderGoodsList, JsonFormat.compact()));// 设置商品信息json数据
						of.setShip_price(BigDecimal.valueOf(user_ship_price));
						of.setStore_ship_price(BigDecimal.valueOf(store_ship_price));
						of.setGoods_amount(obj.getGoods_current_price());
						of.setTotalPrice(BigDecimal.valueOf(order_total_price));
						of.setPayment_amount(new BigDecimal(order_total_price));

						of.setOrder_form(0);//
						of.setCommission_amount(BigDecimal.valueOf(commission_amount));// 该订单总体佣金费用
						of.setGoods_vat(BigDecimal.valueOf(goods_vat));
						of.setCommission_vat(BigDecimal.valueOf(commission_vat));
						of.setOrder_form(0);// 商家商品订单
						of.setOrder_cat(6);
						of.setStore_id(store.getId().toString());
						of.setStore_name(store.getStore_name());
						of.setOrder_type(order_type);
						of.setDelivery_type(0);
						this.orderService.save(of);

						// 封装订单日志
						OrderFormLog ofl = new OrderFormLog();
						ofl.setAddTime(new Date());
						of.setPay_msg("AWARD");
						of.setPayTime(new Date());
						of.setPayType("AWARD");
						of.setOrder_status(20);
						// 记录支付日志
						ofl.setAddTime(new Date());
						ofl.setLog_info("提交货到付款申请");
						ofl.setLog_user(user);
						ofl.setOf(of);
						this.orderFormLogService.save(ofl);
						// 更新商品库存
						this.appCartViewTools.updateGoodsInventory(of);

						GameAwardInfo.setStatus(1);
						this.gameAwardInfoService.update(GameAwardInfo);
					}
				} else {
					code = 4208;
					msg = "The merchandise is off the shelves";
				}
			} else {
				code = 4206;
				msg = "Goods in short stock";
			}

		} else {
			code = -100;
			msg = "token Invalidation";
		}
		return new Result(code, msg);
	}

}
