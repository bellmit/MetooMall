package com.metoo.modul.app.game.tree.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iskyshop.core.tools.CommUtil;
import com.metoo.foundation.domain.Game;
import com.metoo.foundation.domain.GameAward;
import com.metoo.foundation.domain.GameAwardInfo;
import com.metoo.foundation.domain.GameGoods;
import com.metoo.foundation.domain.GameLog;
import com.metoo.foundation.domain.GameTask;
import com.metoo.foundation.domain.GameTreeLog;
import com.metoo.foundation.domain.GoodsVoucher;
import com.metoo.foundation.domain.GoodsVoucherInfo;
import com.metoo.foundation.domain.OrderForm;
import com.metoo.foundation.domain.SubTrees;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IAppGameLogService;
import com.metoo.foundation.service.IGameAwardInfoService;
import com.metoo.foundation.service.IGameAwardService;
import com.metoo.foundation.service.IGameGoodsService;
import com.metoo.foundation.service.IGameService;
import com.metoo.foundation.service.IGameTreeLogService;
import com.metoo.foundation.service.IGoodsVoucherInfoService;
import com.metoo.foundation.service.IGoodsVoucherService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.IUserService;

@Component
public class AppGameAwardTools {

	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IGameAwardInfoService gameAwardInfoService;
	@Autowired
	private IGameAwardService gameAwardService;
	@Autowired
	private IGoodsVoucherService goodsVoucherService;
	@Autowired
	private IGoodsVoucherInfoService goodsVoucherInfoService;
	@Autowired
	private IGameGoodsService gameGoodsService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IAppGameLogService appGameLogService;
	@Autowired
	private IGameTreeLogService appGameTreeLogService;
	@Autowired
	private IGameService appGameService;
	
	/**
	 * 添加用户升级奖励，并查询升级奖励 弃用：奖品格式改变
	 * 
	 * @param user
	 *            当前用户
	 * @param gameAward
	 *            阶段奖品
	 * @return
	 */
	public Map award(User user, GameAward gameAward) {
		Map map = new HashMap();
		map.put("id", gameAward.getId());
		map.put("water", gameAward.getWater());
		if (gameAward != null) {
			if (user != null && gameAward.getType() == 0) {
				GameAwardInfo obj = new GameAwardInfo();
				obj.setAddTime(new Date());
				obj.setUser(user);
				obj.setStatus(0);
				obj.setGameAward(gameAward);
				this.gameAwardInfoService.save(obj);
				map.put("id", obj.getId());
				map.put("water", gameAward.getWater());
			}
			if (gameAward.getCoupon() != null) {
				if (gameAward.getCoupon().getCoupon_begin_time().after(gameAward.getCoupon().getCoupon_end_time())) {
					map.put("coupon_amount", gameAward.getCoupon().getCoupon_amount());
					map.put("coupon_order_amount", gameAward.getCoupon().getCoupon_order_amount());
					map.put("coupon_begin_time", gameAward.getCoupon().getCoupon_begin_time());
					map.put("coupon_end_time", gameAward.getCoupon().getCoupon_end_time());
					map.put("coupon_name", gameAward.getCoupon().getCoupon_name());
				}
			}
			return map;
		}
		return null;
	}
	
	/**
	 * 添加用户升级奖励
	 * 
	 * @param user
	 * @param gameAward
	 * @return
	 */
	public List createUpgradeAward(User user, GameAward gameAward) {
		if (user != null && gameAward != null) {
			List<Map> data = new ArrayList<Map>();
			GameAwardInfo gameAwardInfo = new GameAwardInfo();
			gameAwardInfo.setAddTime(new Date());
			gameAwardInfo.setReceiveTime(new Date());
			gameAwardInfo.setUser(user);
			gameAwardInfo.setStatus(1);
			gameAwardInfo.setGameAward(gameAward);
			gameAwardInfo.setDisplay(0);
			if (gameAward.getWater() != null) {
				this.getWater(gameAward.getWater(), user);
				gameAwardInfo.setAward(gameAward.getWater());
				Map waterMap = Json.fromJson(Map.class, gameAward.getWater());
				data.add(waterMap);
			}
			if (gameAward.getGameGoodsList().size() > 0) {
				GameGoods gameGoods = gameAward.getGameGoodsList().get(0);
				gameAwardInfo.setGameGoods(gameGoods);
				Map gameGoodsMap = new HashMap();
				gameGoodsMap.put("id", gameGoods.getId());
				gameGoodsMap.put("accessory", gameGoods.getAccessory());
				gameGoodsMap.put("goods_id", gameGoods.getGoods_id());
				gameGoodsMap.put("number", gameGoods.getNumber());
				data.add(gameGoodsMap);
			}
			if (gameAward.getGoodsVouchers().size() > 0) {
				GoodsVoucher goodsVoucher = gameAward.getGoodsVouchers().get(0);
				gameAwardInfo.setGoods_voucher(goodsVoucher);
				Map goodsVoucherMap = new HashMap();
				goodsVoucherMap.put("id", goodsVoucher.getId());
				goodsVoucherMap.put("name", goodsVoucher.getName());
				goodsVoucherMap.put("number", goodsVoucher.getNumber());
				goodsVoucherMap.put("accessory", goodsVoucher.getAccessory());
				data.add(goodsVoucherMap);
			}
			this.gameAwardInfoService.save(gameAwardInfo);
			return data;
		}
		return null;
	}
	
	/**
	 * 添加用户升级奖励
	 * 
	 * @param user
	 * @param gameAward
	 * @return
	 */
	public List createUpgradeAward(User user, GameAward gameAward, int status) {
		if (user != null && gameAward != null) {
			List<Map> data = new ArrayList<Map>();
			GameAwardInfo gameAwardInfo = new GameAwardInfo();
			gameAwardInfo.setAddTime(new Date());
			gameAwardInfo.setReceiveTime(new Date());
			gameAwardInfo.setUser(user);
			gameAwardInfo.setStatus(status);
			gameAwardInfo.setGameAward(gameAward);
			gameAwardInfo.setDisplay(0);
			if (gameAward.getWater() != null) {
				this.getWater(gameAward.getWater(), user);
				gameAwardInfo.setAward(gameAward.getWater());
				Map waterMap = Json.fromJson(Map.class, gameAward.getWater());
				data.add(waterMap);
			}
			if (gameAward.getGameGoodsList().size() > 0) {
				GameGoods gameGoods = gameAward.getGameGoodsList().get(0);
				gameAwardInfo.setGameGoods(gameGoods);
				Map gameGoodsMap = new HashMap();
				gameGoodsMap.put("id", gameGoods.getId());
				gameGoodsMap.put("accessory", gameGoods.getAccessory());
				gameGoodsMap.put("goods_id", gameGoods.getGoods_id());
				gameGoodsMap.put("number", gameGoods.getNumber());
				data.add(gameGoodsMap);
			}
			if (gameAward.getGoodsVouchers().size() > 0) {
				GoodsVoucher goodsVoucher = gameAward.getGoodsVouchers().get(0);
				gameAwardInfo.setGoods_voucher(goodsVoucher);
				Map goodsVoucherMap = new HashMap();
				goodsVoucherMap.put("id", goodsVoucher.getId());
				goodsVoucherMap.put("name", goodsVoucher.getName());
				goodsVoucherMap.put("number", goodsVoucher.getNumber());
				goodsVoucherMap.put("accessory", goodsVoucher.getAccessory());
				data.add(goodsVoucherMap);
			}
			this.gameAwardInfoService.save(gameAwardInfo);
			return data;
		}
		return null;
	}

	public List upgradeAwards(User user) {
		// 查询用户升级奖励
		Map params = new HashMap();
		params.put("user_id", user.getId());
		params.put("type", 0);
		params.put("status", 0);
		List<GameAwardInfo> gameAwardInfos = this.gameAwardInfoService.query(
				"SELECT obj FROM GameAwardInfo obj WHERE obj.user.id=:user_id AND obj.gameAward.type=:type AND obj.status=:status ORDER BY obj.addTime ASC",
				params, -1, -1);
		if(gameAwardInfos.size() > 0){
			List<Map> data = new ArrayList<Map>();
			GameAwardInfo gameAwardInfo = gameAwardInfos.get(0);
			gameAwardInfo.setDisplay(1);
			this.gameAwardInfoService.update(gameAwardInfo);
			GameAward gameAward = gameAwardInfo.getGameAward();
			if (gameAward.getWater() != null) {
				this.getWater(gameAward.getWater(), user);
				gameAwardInfo.setAward(gameAward.getWater());
				Map waterMap = Json.fromJson(Map.class, gameAward.getWater());
				data.add(waterMap);
			}
			if (gameAward.getGameGoodsList().size() > 0) {
				GameGoods gameGoods = gameAward.getGameGoodsList().get(0);
				gameAwardInfo.setGameGoods(gameGoods);
				Map gameGoodsMap = new HashMap();
				gameGoodsMap.put("id", gameGoods.getId());
				gameGoodsMap.put("accessory", gameGoods.getAccessory());
				gameGoodsMap.put("goods_id", gameGoods.getGoods_id());
				gameGoodsMap.put("number", gameGoods.getNumber());
				data.add(gameGoodsMap);
			}
			if (gameAward.getGoodsVouchers().size() > 0) {
				GoodsVoucher goodsVoucher = gameAward.getGoodsVouchers().get(0);
				gameAwardInfo.setGoods_voucher(goodsVoucher);
				Map goodsVoucherMap = new HashMap();
				goodsVoucherMap.put("id", goodsVoucher.getId());
				goodsVoucherMap.put("name", goodsVoucher.getName());
				goodsVoucherMap.put("number", goodsVoucher.getNumber());
				goodsVoucherMap.put("accessory", goodsVoucher.getAccessory());
				data.add(goodsVoucherMap);
			}
			gameAwardInfo.setStatus(1);
			this.gameAwardInfoService.save(gameAwardInfo);
			return data;
		}
		return null;
	}

	public List upgradeAwardsV2(User user) {
		// 查询用户升级奖励
		Map params = new HashMap();
		params.put("user_id", user.getId());
		params.put("type", 0);
		params.put("status", 0);
		List<GameAwardInfo> gameAwardInfos = this.gameAwardInfoService.query(
				"SELECT obj FROM GameAwardInfo obj WHERE obj.user.id=:user_id AND obj.gameAward.type=:type AND obj.status=:status ORDER BY obj.addTime DESC",
				params, -1, -1);
		if(gameAwardInfos.size() > 0){
			List data = new ArrayList();
			//for(GameAwardInfo gameAwardInfo : gameAwardInfos){
				List<Map> maps = new ArrayList<Map>();
				GameAwardInfo gameAwardInfo = gameAwardInfos.get(0);
				gameAwardInfo.setDisplay(1);
				this.gameAwardInfoService.update(gameAwardInfo);
				GameAward gameAward = gameAwardInfo.getGameAward();
				if (gameAward.getWater() != null) {
					this.getWater(gameAward.getWater(), user);
					gameAwardInfo.setAward(gameAward.getWater());
					Map waterMap = Json.fromJson(Map.class, gameAward.getWater());
					maps.add(waterMap);
				}
				if (gameAward.getGameGoodsList().size() > 0) {
					GameGoods gameGoods = gameAward.getGameGoodsList().get(0);
					gameAwardInfo.setGameGoods(gameGoods);
					Map gameGoodsMap = new HashMap();
					gameGoodsMap.put("id", gameGoods.getId());
					gameGoodsMap.put("accessory", gameGoods.getAccessory());
					gameGoodsMap.put("goods_id", gameGoods.getGoods_id());
					gameGoodsMap.put("number", gameGoods.getNumber());
					maps.add(gameGoodsMap);
				}
				if (gameAward.getGoodsVouchers().size() > 0) {
					GoodsVoucher goodsVoucher = gameAward.getGoodsVouchers().get(0);
					gameAwardInfo.setGoods_voucher(goodsVoucher);
					Map goodsVoucherMap = new HashMap();
					goodsVoucherMap.put("id", goodsVoucher.getId());
					goodsVoucherMap.put("name", goodsVoucher.getName());
					goodsVoucherMap.put("number", goodsVoucher.getNumber());
					goodsVoucherMap.put("accessory", goodsVoucher.getAccessory());
					maps.add(goodsVoucherMap);
				}
				gameAwardInfo.setStatus(1);
				this.gameAwardInfoService.save(gameAwardInfo);
				// data.add(maps);
			//}
			return maps;
		}
		return null;
	}
	/**
	 * 领取奖励
	 * @param gameAward
	 */
	public boolean recevieAward(GameAward gameAward, GameAwardInfo gameAwardInfo, User user){
		boolean flag = true;
		switch (gameAward.getType()) {	
		case 0:// 升级奖励
			break;
		case 1: // 水滴
			flag = this.getWater(gameAwardInfo.getAward(), user);
			break;
		case 2: // 商品
		/*case 3:// 
			goods(gameAwardInfo);
			break;*/
			break;
		case 3:// 抵用券
			flag = this.getVoucher(gameAwardInfo.getGoods_voucher(), user);
			break;
		case 5:// 空奖
			break;
		default:
			break;
		}
		return flag;
	}
	
	public Map extract(GameAwardInfo gameAwardInfo){
		Map map = new HashMap();
		String award = gameAwardInfo.getAward();
		map.put("award", award);
		GameGoods gameGoods = gameAwardInfo.getGameGoods();
		map.put("gameGoods", gameGoods);
		GoodsVoucher goodsVoucher = gameAwardInfo.getGoods_voucher();
		map.put("goodsVoucher", goodsVoucher);
		return map;
	}
	
	public Map extract(GameAward gameAward){
		Map map = new HashMap();
		String award = gameAward.getWater();
		map.put("award", award);
		List<GameGoods> gameGoods = gameAward.getGameGoodsList();
		map.put("gameGoods", gameGoods);
		List<GoodsVoucher> goodsVoucher = gameAward.getGoodsVouchers();
		map.put("goodsVoucher", goodsVoucher);
		return map;
	}
	
	
	/**
	 * 领取奖品：水滴
	 * 
	 * @param award
	 *            水滴json数据
	 * @param user
	 */
	public boolean getWater(String award, User user) {
		Map map = Json.fromJson(Map.class, award);
		if (!map.isEmpty()) {
			user.setWater_drops_unused(user.getWater_drops_unused() + CommUtil.null2Int(map.get("number")));
			this.userService.update(user);
			return true;
		}
		return false;
	}

	/**
	 * 领取奖品：商品
	 * 
	 * @param gameAwardInfo
	 */
	/*
	 * public void getGameGoods(GameAwardInfo gameAwardInfo){ GameGoods
	 * gameGoods = gameAwardInfo.getGameGoods(); if(!map.isEmpty()){ Goods goods
	 * = this.goodsService.getObjById(CommUtil.null2Long(map.get("goods_id")));
	 * if(goods != null){ gameAwardInfo.setGoods(goods); } } }
	 */

	/**
	 * 领取奖品：抵用券
	 * 
	 * @param gameAwardInfo
	 * @param user
	 */
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

	// 查询用升级-待领取奖品
	public Map getAward(User user) {
		if (user != null) {
			Map map = new HashMap();
			Map params = new HashMap();
			params.clear();
			params.put("user_id", user.getId());
			params.put("status", 0);
			params.put("type", 0);
			List<GameAwardInfo> GameAwardInfos = this.gameAwardInfoService.query(
					"SELECT obj FROM GameAwardInfo obj WHERE obj.user.id=:user_id AND obj.status=:status AND obj.gameAward.type=:type order by obj.addTime asc",
					params, -1, -1);
			if (GameAwardInfos.size() > 0) {
				GameAwardInfo obj = GameAwardInfos.get(0);
				map.put("id", obj.getId());
				if (obj.getGameAward() != null && obj.getGameAward().getType() == 0) {
					GameAward gameAward = obj.getGameAward();
					map.put("game_award_id", gameAward.getId());
					map.put("water", gameAward.getWater());
					if (gameAward.getCoupon() != null) {
						if (gameAward.getCoupon().getCoupon_begin_time()
								.after(gameAward.getCoupon().getCoupon_end_time())) {
							map.put("coupon_amount", gameAward.getCoupon().getCoupon_amount());
							map.put("coupon_order_amount", gameAward.getCoupon().getCoupon_order_amount());
							map.put("coupon_begin_time", gameAward.getCoupon().getCoupon_begin_time());
							map.put("coupon_end_time", gameAward.getCoupon().getCoupon_end_time());
							map.put("coupon_name", gameAward.getCoupon().getCoupon_name());
						}
					}
				}
				return map;
			}
		}
		return null;
	}

	// 查看用戶升级奖励：奖励使用关联表
	public Map getAward(GameAward gameAward) {
		Map map = new HashMap();
		if (gameAward != null) {
			map.put("gameAward_id", gameAward.getId());
			Map params = new HashMap();
			params.put("gameAward_id", gameAward.getId());
			/*List<GameGoods> list = this.gameGoodsService.query(
					"SELECT obj " + "FROM GameGoods obj " + "WHERE obj.gameAward.id=:gameAward_id", params, -1, -1);*/
			List<GameGoods> list = gameAward.getGameGoodsList();
			// 商品碎片
			List<Map> data = new ArrayList<Map>();
			for (GameGoods gameGoods : list) {
				Map goodsMap = new HashMap();
				goodsMap.put("id", gameGoods.getGoods_id());
				goodsMap.put("accessory",
						this.configService.getSysConfig().getImageWebServer() + "/" + gameGoods.getAccessory());
				goodsMap.put("number", gameGoods.getNumber());
				data.add(goodsMap);
			}

			// 水滴(为图片添加域名，让懒得要死的前端什么都不用做)
			if (gameAward.getWater() != null) {
				Map water = Json.fromJson(Map.class, gameAward.getWater());
				water.put("accessory",
						this.configService.getSysConfig().getImageWebServer() + "/" + water.get("accessory"));
				data.add(water);
			}

			// 优惠券
			if (gameAward.getCoupon() != null) {
				if (gameAward.getCoupon().getCoupon_begin_time().after(gameAward.getCoupon().getCoupon_end_time())) {
					map.put("coupon_amount", gameAward.getCoupon().getCoupon_amount());
					map.put("coupon_order_amount", gameAward.getCoupon().getCoupon_order_amount());
					map.put("coupon_begin_time", gameAward.getCoupon().getCoupon_begin_time());
					map.put("coupon_end_time", gameAward.getCoupon().getCoupon_end_time());
					map.put("coupon_name", gameAward.getCoupon().getCoupon_name());
				}
			}
			map.put("data", data);
		}
		return map;
	}

	/**
	 * 查询用户抵用券金额
	 * @param user
	 * @return
	 */
	public double queryGoodsVoucher(User user,  Boolean voucher){
		if(user != null){
			Map params = new HashMap();
			params.put("type", 0);
			params.put("status", 0);
			List<Game> gameList = this.appGameService.query("select obj from Game obj where obj.type=:type and obj.status=:status", params, -1, -1);
			if(gameList.size() > 0){
				params.clear();
				params.put("user_id", user.getId());
				params.put("status", 0);
				List<GoodsVoucherInfo> goodsVoucherInfos = this.goodsVoucherInfoService
						.query("select obj from GoodsVoucherInfo obj where obj.user.id=:user_id and obj.status=:status", params, -1, -1);
				if(goodsVoucherInfos.size() > 0){
					double price = 0;
					for(GoodsVoucherInfo obj : goodsVoucherInfos){
						price = CommUtil.add(price, obj.getGoods_voucher().getNumber());
					}
					return price;
				}
			}
		}
		return 0;
		
	}
	
	/**
	 * 查询用户抵用券金额
	 * @param user
	 * @return
	 */
	public List<GoodsVoucherInfo> editGoodsVoucher(User user, OrderForm of){
		if(user != null){
			// 查询游戏是否开启
			Map params = new HashMap();
			params.put("type", 0);
			params.put("status", 0);
			List<Game> gameList = this.appGameService.query("select obj from Game obj where obj.type=:type and obj.status=:status", params, -1, -1);
			if(gameList.size() > 0){
				params.clear();
				params.put("user_id", user.getId());
				params.put("status", 0);
				List<GoodsVoucherInfo> goodsVoucherInfos = this.goodsVoucherInfoService
						.query("select obj from GoodsVoucherInfo obj where obj.user.id=:user_id and obj.status=:status", params, -1, -1);
				if(goodsVoucherInfos.size() > 0){
					for(GoodsVoucherInfo goodsVoucherInfo : goodsVoucherInfos){
						goodsVoucherInfo.setStatus(1);
						goodsVoucherInfo.setOf(of);
						this.goodsVoucherInfoService.update(goodsVoucherInfo);
					}
					return goodsVoucherInfos;
				}
			}
		}
		return null;
	}
	
	/**
	 * 返回抵用券
	 * @param goodsVoucherInfoList
	 * @return
	 */
	public boolean editGoodsVoucherInfo(List<GoodsVoucherInfo> goodsVoucherInfoList){
		if(goodsVoucherInfoList.size() > 0){
			for(GoodsVoucherInfo goodsVoucherInfo : goodsVoucherInfoList){
				goodsVoucherInfo.setStatus(0);
				goodsVoucherInfo.setOf(null);
				this.goodsVoucherInfoService.update(goodsVoucherInfo);
			}
			return true;
		}
		return false;
		
	}
	
	
}
