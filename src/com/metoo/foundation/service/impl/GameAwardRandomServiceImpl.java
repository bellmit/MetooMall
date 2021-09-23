package com.metoo.foundation.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.nutz.json.Json;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.GameAward;
import com.metoo.foundation.domain.GameAwardInfo;
import com.metoo.foundation.domain.GameGoods;
import com.metoo.foundation.domain.Goods;
import com.metoo.foundation.domain.GoodsVoucher;
import com.metoo.foundation.domain.GoodsVoucherInfo;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IGameAwardInfoService;
import com.metoo.foundation.service.IAppGameAwardRandomService;
import com.metoo.foundation.service.IGameAwardService;
import com.metoo.foundation.service.IGameGoodsService;
import com.metoo.foundation.service.IGoodsService;
import com.metoo.foundation.service.IGoodsVoucherInfoService;
import com.metoo.foundation.service.IGoodsVoucherService;
import com.metoo.foundation.service.IUserService;
import com.taobao.top.link.embedded.websocket.util.StringUtil;

@Service
@Transactional
public class GameAwardRandomServiceImpl implements IAppGameAwardRandomService {


	@Autowired
	private IGameAwardService gameAwardService;
	@Autowired
	private IGameAwardInfoService gameAwardInfoService;
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IGameGoodsService gameGoodsService;
	@Autowired
	private IGoodsVoucherService goodsVoucherService;
	@Autowired
	private IUserService userService;
	
	@Override
	public Object randomAward(Long id, User user) {
		Map map = new HashMap();
		// 查询随机奖品
		List<Integer> types = new ArrayList<Integer>();
		types.add(1);
		types.add(2);
		types.add(3);
		types.add(4);
		Map params = new HashMap();
		params.put("type", types);
		List<GameAward> gameAwards = this.gameAwardService
											.query("SELECT new GameAward(id, water, type, probably) "
													+ "FROM GameAward obj "
													+ "WHERE obj.type in (:type) "
													+ "ORDER BY obj.probably DESC", params, -1, -1);
		if(gameAwards.size() > 0){
			// 计算奖品概率
			List<Integer> probadlys = new ArrayList<Integer>();
			int random = new Random().nextInt(100);
			int median = 0;
			GameAward gameAward = null;
			for(GameAward ga : gameAwards){
				probadlys.add(new Double(CommUtil.mul(ga.getProbably(), 100)).intValue());
			}
			gameAward = gameAwards.get(getProbadlys(probadlys));
			Object award = null;
			map.put("award", award);
			GameGoods gameGoods = null;
			GoodsVoucher goodsVoucher = null;
			switch (gameAward.getType()) {
			case 1:
				award = resolver(gameAward.getWater(), id);
				map.put("award", award);
				break;
			case 2:
				gameGoods = gameGoods(gameAward.getId(), id);
				map.put("award", JSONObject.toJSON(gameGoods));
				break;
			case 3:
				goodsVoucher = goodsVoucher(gameAward.getId(), id);
				map.put("award", JSONObject.toJSON(goodsVoucher));
				break;
			default:
				break;
			}
			// 设置用户奖品1：设置默认未领取 2：检测用户对随机奖励的意向
			GameAwardInfo gameAwardInfo = new GameAwardInfo();
			gameAwardInfo.setAddTime(new Date());
			gameAwardInfo.setUser(user);
			gameAwardInfo.setStatus(0);
			gameAwardInfo.setGameAward(gameAward);
			gameAwardInfo.setGoods_voucher(goodsVoucher);
			gameAwardInfo.setGameGoods(gameGoods);
			gameAwardInfo.setAward(Json.toJson(award));
			this.gameAwardInfoService.save(gameAwardInfo);
		
			if(gameAward.getType() == 1 || gameAward.getType() == 2 || gameAward.getType() == 3){
				map.put("type", gameAward.getType());
				map.put("id", gameAwardInfo.getId());
			}
			return map;
		}
		return null;
	}

	@Override
	public Object randomAward(User user) {
		Map map = new HashMap();
		// 查询随机奖品
		Map params = new HashMap();
		params.put("type", 7);
		List<GameAward> gameAwards = this.gameAwardService
											.query("SELECT obj FROM GameAward obj WHERE obj.type=:type", params, -1, -1);
		if(gameAwards.size() > 0){
			// 计算奖品概率
			List<Integer> probadlys = new ArrayList<Integer>();
			int random = new Random().nextInt(100);
			int median = 0;
			GameAward gameAward = gameAwards.get(0);
			/*for(GameAward ga : gameAwards){
				probadlys.add(ga.getProbably().intValue());
			}
			gameAward = gameAwards.get(getProbadlys(probadlys));*/
			// 判断随机奖励类型（提供不同解析方式）
			GameGoods award = null;
			switch (gameAward.getType()) {
			case 7:
				award = gameGoods(gameAward.getId());
			default: 
				break;
			}
			// 设置用户奖品1：设置默认未领取 2：检测用户对随机奖励的意向
			GameAwardInfo gameAwardInfo = new GameAwardInfo();
			gameAwardInfo.setAddTime(new Date());
			gameAwardInfo.setUser(user);
			gameAwardInfo.setStatus(1);
			gameAwardInfo.setGameAward(gameAward);
			gameAwardInfo.setGoods(gameAward.getGoods());
			this.gameAwardInfoService.save(gameAwardInfo);
		
			map.put("award", award);
			map.put("type", gameAward.getType());
			map.put("id", gameAwardInfo.getId());
			return map;
		}
		return null;
	}
	
	
	public GameGoods gameGoods(Long id){
		Map params = new HashMap();
		params.put("gameAward_id", id);
		List<GameGoods> list = this.gameGoodsService.query("SELECT new GameGoods(id,goods_detail,accessory,goods_id,probably,debris_totals,number) "
										+ "FROM GameGoods obj "
										+ "WHERE obj.gameAward.id=:gameAward_id", params, -1, -1);
		if(list != null && list.size() > 0){
			// 排序List集合 比率从大到小
			CommUtil.compareTo2(list);
			// 计算奖品概率
			List<Integer> probadlys = new ArrayList<Integer>();
			int random = new Random().nextInt(100);
			int median = 0;
			
			for(GameGoods obj : list){
				probadlys.add(new Double(CommUtil.mul(obj.getProbably(), 100)).intValue());
			};
			GameGoods gameGoods = list.get(getProbadlys(probadlys));
			return gameGoods;
		}
		return null;
	}
	
	public GoodsVoucher goodsVoucher(Long id){
		Map params = new HashMap();
		params.put("gameAward_id", id);
		List<GoodsVoucher> list = this.goodsVoucherService.query("SELECT new GoodsVoucher(id,name,price,type,msg,accessory,probably) "
										+ "FROM GoodsVoucher obj "
										+ "WHERE obj.gameAward.id=:gameAward_id", params, -1, -1);
	
		if(list != null && list.size() > 0){
			// 排序List集合 比率从大到小
			CommUtil.compareTo3(list);// 优化、反射
			// 计算奖品概率
			List<Integer> probadlys = new ArrayList<Integer>();
			int random = new Random().nextInt(100);
			int median = 0;
			
			for(GoodsVoucher obj : list){
				probadlys.add(new Double(CommUtil.mul(obj.getProbably(), 100)).intValue());
			};
			GoodsVoucher goodsVoucher = list.get(getProbadlys(probadlys));
			return goodsVoucher;
		}
		return null;
	}
	
	// 解析奖品JSON数据
		public GameGoods gameGoods(Long id, Long sub_id){
			Map params = new HashMap();
			GameAward gameAward = this.gameAwardService.getObjById(id);
			if(gameAward != null){
				List<GameGoods> list = gameAward.getGameGoodsList();
				if(list != null && list.size() > 0){
					// 排序List集合 比率从大到小
					CommUtil.compareToGoods(list, sub_id);
					// 计算奖品概率
					List<Integer> probadlys = new ArrayList<Integer>();
					int random = new Random().nextInt(100);
					int median = 0;
					
					for(GameGoods obj : list){
						Map probablyMap = Json.fromJson(Map.class, obj.getProbably());
						String probably = (String) probablyMap.get(sub_id.toString());
						probadlys.add(new Double(CommUtil.mul(probably, 100)).intValue());
					};
					
					GameGoods gameGoods = list.get(getProbadlys(probadlys));
					GameGoods obj = new GameGoods();
					obj.setId(gameGoods.getId());
					obj.setGoods_detail(gameGoods.getGoods_detail());
					obj.setGoods_id(gameGoods.getGoods_id());
					obj.setProbably(gameGoods.getProbably());
					obj.setDebris_totals(gameGoods.getDebris_totals());
					obj.setNumber(gameGoods.getNumber());
					obj.setAccessory(gameGoods.getAccessory());
					return obj;
				}
			}
			
			return null;
		}
	
	public GoodsVoucher goodsVoucher(Long id, Long sub_id){
		Map params = new HashMap();
		params.put("gameAward_id", id);
		List<GoodsVoucher> list = this.goodsVoucherService.query("SELECT new GoodsVoucher(id,name,number,type,msg,accessory,probably) "
										+ "FROM GoodsVoucher obj "
										+ "WHERE obj.gameAward.id=:gameAward_id", params, -1, -1);
	
		if(list != null && list.size() > 0){
			// 排序List集合 比率从大到小
			CommUtil.compareToVoucher(list, sub_id);// 优化、反射
			// 计算奖品概率
			List<Integer> probadlys = new ArrayList<Integer>();
			int random = new Random().nextInt(100);
			int median = 0;
			
			for(GoodsVoucher obj : list){
				Map probablyMap = Json.fromJson(Map.class, obj.getProbably());
				String probably = (String) probablyMap.get(id.toString());
				probadlys.add(new Double(CommUtil.mul(probably, 100)).intValue());
			};
			GoodsVoucher goodsVoucher = list.get(getProbadlys(probadlys));
			return goodsVoucher;
		}
		return null;
	}
	
	
	// 解析奖品JSON数据
	public Object resolver(String param){
		List<Map<String, Object>> list = Json.fromJson(List.class,
				param);
		if(list.size() > 0){
			// 排序List集合 比率从大到小
			CommUtil.compareTo(list);
			// 计算奖品概率
			List<Integer> probadlys = new ArrayList<Integer>();
			int random = new Random().nextInt(100);
			int median = 0;
			Map data = new HashMap();
			for(Map map : list){
				probadlys.add(new Double(CommUtil.mul(map.get("probably"), 100)).intValue());
			};
			data = list.get(getProbadlys(probadlys));
			return data;
		}
		return null;
	}
	
	public Object resolver(String param, Long id){
		List<Map<String, Object>> list = Json.fromJson(List.class,
				param);
		if(list.size() > 0){
			// 排序List集合 比率从大到小
			CommUtil.compareTo(list, id);
			// 计算奖品概率
			List<Integer> probadlys = new ArrayList<Integer>();
			int random = new Random().nextInt(100);
			int median = 0;
			Map data = new HashMap();
			for(Map map : list){
				Map probablyMap = Json.fromJson(Map.class, Json.toJson(map.get("probably")));
				String probably = (String) probablyMap.get(id.toString());
				probadlys.add(new Double(CommUtil.mul(probably, 100)).intValue());
			};
			data = list.get(getProbadlys(probadlys));
			return data;
		}
		return null;
	}

	
	// 获取概率值
	public int getProbadlys(List<Integer> probadlys){
		int random = new Random().nextInt(100);
		int median = 0;
		int index = 0;
		for(int i=0; i < probadlys.size(); i ++){
			if(random <= probadlys.get(i) + median){
				index = i;
				break;
			}
			median = probadlys.get(i) + median;
			
		}
		return index;
	}

	@Override
	public Object randomAward(User user, int index) {
		Map map = new HashMap();
		// 查询随机奖品
		Map params = new HashMap();
		params.put("type", 7);
		List<GameAward> gameAwards = this.gameAwardService
											.query("SELECT obj FROM GameAward obj WHERE obj.type=:type", params, -1, -1);
		if(gameAwards.size() > 0){
			// 计算奖品概率
			List<Integer> probadlys = new ArrayList<Integer>();
			int random = new Random().nextInt(100);
			int median = 0;
			GameAward gameAward = gameAwards.get(0);
			/*for(GameAward ga : gameAwards){
				probadlys.add(ga.getProbably().intValue());
			}
			gameAward = gameAwards.get(getProbadlys(probadlys));*/
			// 判断随机奖励类型（提供不同解析方式）
			List<Object> list = new ArrayList<Object>();
			GameGoods award = null;
			switch (gameAward.getType()) {
			case 7:
				award = gameGoods(gameAward.getId(), Long.parseLong("-1"));
				list.add(award);
			default: 
				break;
			}
			// 1, 碎片
			// 设置用户奖品1：设置默认未领取 2：检测用户对随机奖励的意向
			GameAwardInfo gameAwardInfo = new GameAwardInfo();
			gameAwardInfo.setAddTime(new Date());
			gameAwardInfo.setUser(user);
			gameAwardInfo.setStatus(1);
			gameAwardInfo.setGameAward(gameAward);
			gameAwardInfo.setGoods(gameAward.getGoods());
			gameAwardInfo.setGameGoods(award);
			this.gameAwardInfoService.save(gameAwardInfo);
			// 2, 水滴
			List<Map> waterMaps = Json.fromJson(List.class, gameAward.getWater());
			Map waterMap = waterMaps.get(index);
			int water = (int) waterMap.get("number");
			user.setWater_drops_unused(user.getWater_drops_unused() + water);
			this.userService.update(user);
			list.add(waterMap);
			map.put("award", list);
			map.put("type", gameAward.getType());
			map.put("id", gameAwardInfo.getId());
			return map;
		}
		return null;
	}
	
}
