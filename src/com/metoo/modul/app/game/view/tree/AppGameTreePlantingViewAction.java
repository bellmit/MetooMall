package com.metoo.modul.app.game.view.tree;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.ResponseUtils;
import com.metoo.foundation.domain.Address;
import com.metoo.foundation.domain.Area;
import com.metoo.foundation.domain.Game;
import com.metoo.foundation.domain.GameAward;
import com.metoo.foundation.domain.GameGoods;
import com.metoo.foundation.domain.GameTreeLog;
import com.metoo.foundation.domain.PlantingTrees;
import com.metoo.foundation.domain.SubTrees;
import com.metoo.foundation.domain.Tree;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IAddressService;
import com.metoo.foundation.service.IAreaService;
import com.metoo.foundation.service.IGameAwardInfoService;
import com.metoo.foundation.service.IAppGameAwardRandomService;
import com.metoo.foundation.service.IGameGoodsService;
import com.metoo.foundation.service.IGameService;
import com.metoo.foundation.service.IGameTreeLogService;
import com.metoo.foundation.service.IPlantingtreesService;
import com.metoo.foundation.service.ISubTreesService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.ITreeService;
import com.metoo.foundation.service.IUserService;
import com.metoo.modul.app.game.tree.tools.AppGameAwardTools;
import com.metoo.modul.app.game.tree.tools.AppGameTreeTools;
import com.metoo.module.app.buyer.domain.Result;

/**
 * <p>
 * Title: PlantingTrees.java
 * </p>
 * 
 * <p>
 * Description: 前端用户植树小游戏控制器，用户选择要种的树并浇水;
 * </p>
 * 
 * @author 46075
 *
 */
@Controller
@RequestMapping("/app/v1/game/tree")
public class AppGameTreePlantingViewAction {

	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ITreeService treeService;
	@Autowired
	private IPlantingtreesService plantingtreesService;
	@Autowired
	private ISubTreesService subTreesService;
	@Autowired
	private IGameService gameService;
	@Autowired
	private AppGameAwardTools appGameAwardTools;
	@Autowired
	private IGameAwardInfoService GameAwardInfoService;
	@Autowired
	private AppGameTreeTools appGameTreeTools;
	@Autowired
	private IAppGameAwardRandomService gameAwardRandomService;
	@Autowired
	private IGameGoodsService gameGoodsService;
	@Autowired
	private IAddressService addressService;
	@Autowired
	private IAreaService areaService;
	@Autowired
	private IGameTreeLogService gameTreeLogService;

	@RequestMapping("/verify/address.json")
	@ResponseBody
	public Object verifyAddress(String token) {
		// User user = CommUtil.verifyToken(token);
		Map data = new HashMap();
		User user = null;
		if (!CommUtil.null2String(token).equals("")) {
			user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user != null) {
				Map params = new HashMap();
				params.put("user_id", user.getId());
				List<Address> addressList = this.addressService
						.query("SELECT new Address(obj.id, obj.trueName, obj.area, obj.area_info, obj.zip, obj.telephone, obj.mobile, obj.email, obj.default_val) FROM Address obj "
								+ "WHERE obj.user.id=:user_id ORDER BY obj.default_val DESC", params, -1, -1);
				if (addressList.size() > 0) {
					List<Map> maps = new ArrayList<Map>();
					for (Address address : addressList) {
						Map map = new HashMap();
						map.put("id", address.getId());
						map.put("userName", address.getTrueName());
						map.put("area_info", address.getArea_info());
						map.put("telephone", address.getTelephone());
						map.put("mobile", address.getMobile());
						map.put("email", address.getEmail());
						map.put("default_val", address.getDefault_val());
						// 国家：country 城市：city 地区： area
						String country = "";
						String city = "";
						String area = "";
						Area obj = address.getArea();
						if (obj.getLevel() == 2) {
							country = obj.getParent().getParent().getAreaName();
							city = obj.getParent().getAreaName();
							area = obj.getAreaName();
						} else if (obj.getLevel() == 1) {
							country = obj.getParent().getAreaName();
							city = obj.getAreaName();
						}
						map.put("country", country);
						map.put("city", city);
						map.put("area", area);
						maps.add(map);
					}
					data.put("address", maps);
					List<Area> areas = this.areaService.query("select obj from Area obj where obj.parent.id is null",
							null, -1, -1);
					List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>();
					for (Area obj : areas) {
						Map<String, Object> areaMap = new HashMap<String, Object>();
						areaMap.put("areaId", obj.getId());
						areaMap.put("areaName", obj.getAreaName());
						areaList.add(areaMap);
					}
					data.put("area", areaList);
				}
				return ResponseUtils.ok(data);
			}
		}
		return ResponseUtils.unlogin();
	}

	/**
	 * 
	 * 种树
	 * 
	 * @param request
	 * @param response
	 * @param token
	 *            用户身份令牌
	 * @param id
	 *            植物id
	 * @return
	 */
	@RequestMapping(value = "planting.json", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object planting(HttpServletRequest request, HttpServletResponse response, String token, String id,
			String address_id) {
		int code = -1;
		String msg = "";
		Map map = new HashMap();
		User user = null;
		if (token != null && !token.equals("")) {
			user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user != null) {
				// 验证用户是否拥有收货地址
				// boolean flag = this.appGameTreeTools.verifyAddress(user);
				Address address = this.addressService.getObjById(CommUtil.null2Long(address_id));
				if (address != null && address.getUser().getId().equals(user.getId())) {
					if (id != null && !id.equals("")) {
						GameGoods gameGoods = this.gameGoodsService.getObjById(CommUtil.null2Long(id));
						if (gameGoods != null) {
							List<GameAward> gameAwards = gameGoods.getGameAwards();
							if (gameAwards.size() > 0) {
								GameAward gameAward = null;
								for(GameAward obj : gameAwards){
									if(obj.getType() == 6){
										gameAward = obj;
										break;
									}
								}
								Tree tree = gameAward.getTree();
								if (tree != null) {
									Map params = new HashMap();
									params.put("user_id", user.getId());
									params.put("tree_id", tree.getId());
									List<PlantingTrees> plants = this.plantingtreesService.query(
											"select obj from PlantingTrees obj where obj.user.id=:user_id and obj.tree.id=:tree_id",
											params, -1, -1);
									// 查询用户是否有种植正在进行
									if (plants.size() == 0) {
										params.clear();
										params.put("user_id", user.getId());
										List<PlantingTrees> user_plants = this.plantingtreesService.query(
												"select obj from PlantingTrees obj where obj.user.id=:user_id", params,
												-1, -1);
										if (user_plants.size() == 0) {
											PlantingTrees plantTree = new PlantingTrees();
											plantTree.setAddTime(new Date());
											plantTree.setName(tree.getName());
											plantTree.setStatus(0);
											plantTree.setTree(tree);
											plantTree.setUser(user);
											plantTree.setWatering(0);
											plantTree.setGameGoods(gameGoods);
											params.clear();
											params.clear();
											params.put("tree_id", tree.getId());
											List<SubTrees> subTrees = this.subTreesService.query(
													"SELECT obj " + "FROM SubTrees obj " + "WHERE obj.tree.id=:tree_id "
															+ "ORDER BY obj.status " + "ASC",
													params, -1, -1);
											if (subTrees.size() > 0) {
												SubTrees subTree = subTrees.get(0);
												plantTree.setAccessory(subTree.getAccessory());
												plantTree.setSub_trees(subTree.getId());
												plantTree.setSubTree(subTrees.get(0));
											}
											this.plantingtreesService.save(plantTree);
											code = 4200;
											msg = "Success";
										} else {
											for (PlantingTrees Plant : user_plants) {
												map.put("tree_name", Plant.getName());
												break;
											}
											code = 4604;
											msg = "It is currently in progress";
										}
									} else {
										// 重复种植
										code = 4604;
										msg = "It is currently in progress";
									}
								} else {
									code = 4403;
									msg = "Parameter is null";
								}
							} else {
								code = 4403;
								msg = "Parameter is null";
							}
						} else {
							code = 4403;
							msg = "Parameter is null";
						}
					} else {
						code = 4403;
						msg = "Parameter is null";
					}
				} else {
					code = 4407;
					msg = "Please select the shipping address";
				}
			} else {
				code = -100;
				msg = "token Invalidation";
			}
		} else {
			code = -100;
			msg = "token Invalidation";
		}
		return new Result(code, msg, map);
	}

	/**
	 * 用户当前种植的树
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @param gt_id
	 * @return
	 */
	@RequestMapping(value = "user/palnting.json", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String plantingTree(HttpServletRequest request, HttpServletResponse response, String token, String gt_id) {
		int code = 4200;
		String msg = "Success";
		User user = null;
		Map<String, Object> map = new HashMap<String, Object>();
		if (token != null && !token.equals("")) {
			user = this.userService.getObjByProperty(null, "app_login_token", token);
		}
		if (user != null) {
			map.put("domain", this.configService.getSysConfig().getImageWebServer());
			map.put("user_name", user.getUserName());
			map.put("water_drops_unused", user.getWater_drops_unused());
			PlantingTrees plantingTree = user.getPlanting_trees();
			if (plantingTree != null) {
				SubTrees subTree = plantingTree.getSubTree();
				if (subTree != null) {
					map.put("name", plantingTree.getName());
					map.put("watering", plantingTree.getWatering());
					map.put("flag", plantingTree.getStatus() > 12 ? 1 : 0);
					Tree tree = plantingTree.getTree();
					Game game = tree.getGame();
					if (game != null) {
						map.put("game_id", game.getId());
						if (tree != null) {
							Map params = new HashMap();
							int watering = 0;
							map.put("accessory",
									subTree.getAccessory() != null
											? this.configService.getSysConfig().getImageWebServer() + "/"
													+ subTree.getAccessory().getPath() + "/"
													+ subTree.getAccessory().getName()
											: "");
							watering = subTree.getWatering(); // 当前阶段所需水滴数

							map.put("schedule",
									Math.floor(CommUtil.div02(plantingTree.getGrade_watering(), watering) * 100));
							map.put("progress",
									Math.floor(CommUtil.div02(plantingTree.getWatering(), tree.getWaters()) * 100));

							map.put("remaining_water", CommUtil.subtract(watering, plantingTree.getGrade_watering()));
							// 查询用户-- 阶段--奖品 ：不存在待领取奖励：每次浇水都直接赠送给用户
							// 查询好友浇水的阶段奖励；是否记录哪位好友
							List upgradeAward = this.appGameAwardTools.upgradeAwardsV2(user);
							map.put("upgradeAward", upgradeAward);
							// 满级奖励
							GameGoods gameGoods = plantingTree.getGameGoods();
							if (gameGoods != null) {
								params.clear();
								params.put("id", gameGoods.getId());
								List<GameGoods> gamgeGoodsList = this.gameGoodsService
										.query("SELECT new GameGoods(obj.id, obj.accessory, obj.goods_id) "
												+ "FROM GameGoods obj " + "WHERE obj.id=:id", params, -1, -1);
								if (gamgeGoodsList.size() > 0) {
									GameGoods obj = gamgeGoodsList.get(0);
									Map gameGoodsMap = new HashMap();
									gameGoodsMap.put("id", obj.getId());
									gameGoodsMap.put("accessory", obj.getAccessory());
									gameGoodsMap.put("goods_id", obj.getGoods_id());
									map.put("gameGoods", gameGoodsMap);
								}
							}

							
							// 判断是否已完成种植
							if (plantingTree.getSubTree().getStatus() == 12
									&& plantingTree.getStatus() == 1) {
								map.put("fill_level", true);
								// 查询用户-满级-奖品
								GameGoods obj = plantingTree.getGameGoods();
								if (obj != null) {
									GameAward gameAward = obj.getGameAward();
									if (gameAward != null) {
										Map accomplishAward = new HashMap();
										accomplishAward.put("id", obj.getId());
										accomplishAward.put("accessory", obj.getAccessory());
										accomplishAward.put("goods_id", obj.getGoods_id());
										accomplishAward.put("number", obj.getNumber());
										map.put("accomplishAward", accomplishAward);
									}
								}
							}

							// 查询用户系统信息、查询未读条数
							params.clear();
							params.put("user_id", user.getId());
							params.put("log_read", 0);
							List<GameTreeLog> gameTreeLogs = this.gameTreeLogService.query(
									"SELECT new GameTreeLog(id, addTime, user_id, user_name, type, status, friend_id, friend_name, log_read, water) FROM GameTreeLog obj "
											+ "WHERE obj.user_id=:user_id " + "AND obj.log_read=:log_read",
									params, -1, -1);
							map.put("notification", gameTreeLogs.size());
						} else {
							code = 4605;
							msg = "The tree has been removed";
						}
					} else {
						code = 4601;
						msg = "The game is not opened";
					}
				} else {
					code = 4500;
					msg = "System error, please contact customer service";
				}
			} else {
				code = 4603;
				msg = "The user has not yet selected a tree to grow";
			}
		} else {
			code = -100;
		}
		return JSONObject.toJSONString(new Result(code, msg, map));
	}

	/**
	 * 浇水
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "watering.json", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object watering(HttpServletRequest request, HttpServletResponse response, String token) {
		int code = 4200;
		String msg = "Success";
		Map<String, Object> map = new HashMap<String, Object>();
		User user = null;
		if (!CommUtil.null2String(token).equals("")) {
			user = this.userService.getObjByProperty(null, "app_login_token", token);
		}
		if (user != null) { 
			List<Game> games = this.gameService.query("SELECT obj FROM Game obj", null, -1, -1);
			if (games.size() > 0) {
				Game game = games.get(0);
				if (game.getStatus() == 0) {
					int watering = game.getWatering_nmber();// 每次浇水水滴数
					PlantingTrees plantingTree = user.getPlanting_trees();
					if (plantingTree != null) {
						Tree tree = plantingTree.getTree();
						if (user.getWater_drops_unused() >= watering) {
							if (plantingTree.getStatus() <= 12 
									&& plantingTree.getStatus() == 0) {
								List upgradeAward = null;
								SubTrees next_subtree = null;
								SubTrees subTree = plantingTree.getSubTree();
								boolean flag = false;
								if (plantingTree.getStatus() <= 12) {
									Map params = new HashMap();
									map.put("flag", Boolean.FALSE);
									if (plantingTree.getGrade_watering() + watering >= subTree.getWatering()) {// 升级
										params.clear();
										params.put("tree_id", plantingTree.getTree().getId());
										params.put("status", subTree.getStatus() + 1);
										List<SubTrees> subTrees = this.subTreesService.query(
												"select obj from SubTrees obj where obj.tree.id=:tree_id and obj.status=:status order by obj.watering desc",
												params, -1, -1);
										if (subTrees.size() > 0) {
											next_subtree = subTrees.get(0);
											plantingTree.setAccessory(next_subtree.getAccessory());// 更新当前阶段图片
											plantingTree.setGrade_watering(new Double(CommUtil.subtract(
													plantingTree.getGrade_watering() + watering, subTree.getWatering()))
															.intValue());
											plantingTree.setSub_trees(next_subtree.getId());
											plantingTree.setSubTree(next_subtree);
											map.put("accessory", CommUtil.autoComplete(next_subtree.getAccessory()));
										}
										if (subTree.getGameAward() != null) {
											upgradeAward = appGameAwardTools.createUpgradeAward(user,
													subTree.getGameAward()); // 阶段奖品
										}
										if (subTree.getStatus() == 12) {
											plantingTree.setStatus(1);
											flag = true;
											map.put("schedule", 100);
											map.put("progress", 100);
											// 满级奖品
											GameGoods gameGoods = plantingTree.getGameGoods();
											if (gameGoods != null) {
												GameAward gameAward = gameGoods.getGameAward();
												if (gameAward != null) {
													List accomplishAward = appGameAwardTools.createUpgradeAward(user,
															gameAward);
													map.put("accomplishAward", accomplishAward);
												}
											}
										}
										map.put("flag", Boolean.TRUE);
									}
									
									map.put("upgradeAward", upgradeAward);
									plantingTree.setGrade_watering(plantingTree.getGrade_watering() + watering);
									plantingTree.setWatering(plantingTree.getWatering() + watering);
									user.setWater_drops_unused(user.getWater_drops_unused() - watering);
									user.setWater_drop_used(user.getWater_drop_used() + watering);
									this.userService.update(user);
									boolean update = this.plantingtreesService.update(plantingTree);
								}
								if (!flag) {
									try {
										map.put("watering", watering);
										// 随机奖品
										Object randomAward = this.gameAwardRandomService
												.randomAward(subTree.getId(), user);
										map.put("randomAward", randomAward);
										map.put("schedule",
												Math.floor(CommUtil.div02(plantingTree.getGrade_watering(),
														plantingTree.getSubTree().getWatering()) * 100));
										map.put("progress",
												Math.floor(
														CommUtil.div02(plantingTree.getWatering(), tree.getWaters())
																* 100));
										map.put("water_drops_unused", user.getWater_drops_unused());
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							} else {
								code = 4610;
								msg = "The current game is over";
							}
						} else {
							code = 4602;
							msg = "Lack of water droplets";
						}
					} else {
						code = 4603;
						msg = "This user has not yet selected a tree to grow";
					}
				} else {
					code = 4601;
					msg = "The game is not opened";
				}
			} else {
				code = 4601;
				msg = "The game is not opened";
			}
		} else {
			code = -100;
			msg = "token Invalidation";
		}
		return JSONObject.toJSONString(new Result(code, msg, map));

		/*
		 * int code = 4200; String msg = "Success"; Map map = new HashMap();
		 * User user = null; if (!CommUtil.null2String(token).equals("")) { user
		 * = this.userService.getObjByProperty(null, "app_login_token", token);
		 * } if (user != null) { List<Game> games = this.gameService.query(
		 * "SELECT obj FROM Game obj", null, -1, -1); if (games.size() > 0) {
		 * Game game = games.get(0); if (game.getStatus() == 0) { int watering =
		 * game.getWatering_nmber();// 每次浇水水滴数 int watering_number = 0;
		 * PlantingTrees plantingTree = user.getPlanting_trees(); if
		 * (plantingTree != null) { Tree tree = plantingTree.getTree(); if
		 * (user.getWater_drops_unused() >= watering) { if
		 * (plantingTree.getStatus() <= 12) { Map userAward = null; // int award
		 * = 0; boolean flag = false;// 默认为false、满级之后为true SubTrees next_subtree
		 * = null; SubTrees subTree = null; if (plantingTree.getStatus() < 12) {
		 * Map params = new HashMap(); params.put("watering",
		 * plantingTree.getGrade_watering() + watering); params.put("tree_id",
		 * plantingTree.getTree().getId()); params.put("status",
		 * plantingTree.getStatus()); List<SubTrees> subTrees =
		 * this.subTreesService.query(
		 * "select obj from SubTrees obj where obj.tree.id=:tree_id and obj.watering<=:watering and obj.status=:status order by obj.watering desc"
		 * , params, -1, -1); int status = -1; String accessory = ""; // 升级 if
		 * (subTrees.size() > 0) { subTree = subTrees.get(0);
		 * System.out.println("sub_id: " + subTree.getId()); status =
		 * subTree.getStatus(); if (plantingTree.getGrade_watering() + watering
		 * >= subTree.getWatering()) { params.clear(); params.put("tree_id",
		 * plantingTree.getTree().getId()); params.put("status",
		 * plantingTree.getStatus() + 1); next_subtree = this.subTreesService
		 * .query(
		 * "select obj from SubTrees obj where obj.tree.id=:tree_id and obj.status=:status order by obj.watering desc"
		 * , params, -1, -1) .get(0); accessory = next_subtree.getAccessory() ==
		 * null ? "" : this.configService.getSysConfig().getImageWebServer() +
		 * "/" + next_subtree.getAccessory().getPath() + "/" +
		 * next_subtree.getAccessory().getName();
		 * plantingTree.setAccessory(next_subtree.getAccessory());// 设置每一级
		 * plantingTree.setStatus(status + 1);//
		 * 如何用户当前状态的水滴大于改等级的水滴，则等级+1；当前等级的树水滴清零，并传递给下一个等级
		 * plantingTree.setGrade_watering(new Double(CommUtil.subtract(
		 * plantingTree.getGrade_watering() + watering, subTree.getWatering()))
		 * .intValue()); plantingTree.setSub_trees(next_subtree.getId());
		 * plantingTree.setSubTree(next_subtree); if (subTree.getGameAward() !=
		 * null) { userAward = appGameAwardTools.upgradeAward(user,
		 * subTree.getGameAward()); // 阶段奖品 } flag = true; } else {
		 * watering_number = watering; subTree =
		 * this.subTreesService.getObjById(plantingTree.getSub_trees()); } }
		 * else { watering_number = watering; subTree =
		 * this.subTreesService.getObjById(plantingTree.getSub_trees()); }
		 * map.put("flag", flag); map.put("accessory", accessory); } else if
		 * (plantingTree.getStatus() == 12) { watering_number = watering;
		 * subTree =
		 * this.subTreesService.getObjById(plantingTree.getSub_trees());
		 * 
		 * } boolean fill_level = false; if (plantingTree.getStatus() == 12 &&
		 * plantingTree.getGrade_watering() >= subTree.getWatering()) {
		 * fill_level = true; } if (fill_level) { code = 4614; msg =
		 * "Completed with planting"; } else {
		 * 
		 * // final ReentrantLock lock = new // ReentrantLock(); // lock.lock();
		 * try { plantingTree
		 * .setGrade_watering(plantingTree.getGrade_watering() +
		 * watering_number);
		 * 
		 * plantingTree.setWatering(plantingTree.getWatering() + watering);
		 * user.setWater_drops_unused(user.getWater_drops_unused() - watering);
		 * user.setWater_drop_used(user.getWater_drop_used() + watering);
		 * boolean update = this.plantingtreesService.update(plantingTree); if
		 * (update) { map.put("watering", watering); map.put("userAward",
		 * userAward); // 随机奖品 Object randomAward = this.gameAwardRandomService
		 * .randomAward(subTree.getId(), user); map.put("randomAward",
		 * randomAward); map.put("schedule", Math.round(
		 * CommUtil.div(plantingTree.getGrade_watering(), watering) * 100));
		 * map.put("progress", Math.round(
		 * CommUtil.div(plantingTree.getGrade_watering(), tree.getWaters())
		 * 100)); // 满级奖品 if (plantingTree.getStatus() == 12 &&
		 * plantingTree.getGrade_watering() >= subTree.getWatering()) { if
		 * (subTree.getGameAward() != null) { userAward =
		 * appGameAwardTools.award(user, subTree.getGameAward()); } } } else {
		 * code = 4500; msg = "optimistic locking failed"; } } catch (Exception
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * 
		 * finally { lock.unlock(); }
		 * 
		 * } } else { code = 4610; msg = "The current game is over"; } } else {
		 * code = 4602; msg = "Lack of water droplets"; } } else { code = 4603;
		 * msg = "This user has not yet selected a tree to grow"; } } else {
		 * code = 4601; msg = "The game is not opened"; } } else { code = 4601;
		 * msg = "The game is not opened"; } } else { code = -100; msg =
		 * "token Invalidation"; } return JSONObject.toJSONString(new
		 * Result(code, msg, map));
		 */}

	// 方式一：最后一级奖励未领取不允许收取树

	// 方式二：在用户的树林中也可以领取奖励，增加用户对游戏的体验流程

	/**
	 * 收获
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	/*@RequestMapping("harvest.json")
	@ResponseBody
	public Object harvest(HttpServletRequest request, HttpServletResponse response, String token) {
		Map map = new HashMap();
		User user = CommUtil.verifyToken(token);
		if (user != null) {
			PlantingTrees plantingTree = user.getPlanting_trees();
			if (plantingTree != null) {
				Tree tree = plantingTree.getTree();
				SubTrees subTrees = this.subTreesService.getObjById(plantingTree.getSub_trees());
				Map params = new HashMap();
				params.put("tree_id", tree.getId());
				List<SubTrees> sub_trees = this.subTreesService.query(
						"SELECT obj FROM SubTrees obj WHERE obj.tree.id=:tree_id AND obj.status=(SELECT MAX(status) FROM SubTrees)",
						params, -1, -1);
				// 是否满级
				if (subTrees.getStatus() == sub_trees.get(0).getStatus()
						&& subTrees.getWatering() == subTrees.getWatering()) {

					if (plantingTree != null) {
						boolean flag = appGameTreeTools.fill_level(plantingTree);
						if (flag) {
							// 自动收取用户已满级的树；
							user.getTrees().add(tree);
							this.userService.update(user);
							plantingTree.setAccessory(null);
							plantingTree.setTree(null);
							// 删除当前正在种植得树
							this.plantingtreesService.update(plantingTree);
						}
					}

					// 查询用户奖品
					Map userAward = this.appGameAwardTools.getAward(user);

					if (!userAward.isEmpty()) {
						user.getTrees().add(tree);
						map.put("userAward", userAward);
						this.userService.update(user);
						return new Result(4200, "Success");
					} else {
						return new Result(4300, "Reward not claimed");
					}
				} else {
					return new Result(4614, "Unfinished planting");
				}
			} else {
				return new Result(4605, "Not a tree");
			}
		} else {
			return new Result(-100, "token Invalidation");
		}
	}*/

	/**
	 * 树林
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping("userTrees.json") // userTrees
	@ResponseBody
	public Object matureTrees(HttpServletRequest request, HttpServletResponse response, String token) {
		User user = null;
		if (!CommUtil.null2String(token).equals("")) {
			user = this.userService.getObjByProperty(null, "app_login_token", token);
		}
		if (user != null) {
			List<Tree> trees = user.getTrees();

			List<Map> list = new ArrayList<>();
			for (Tree tree : trees) {
				Map map = new HashMap();
				map.put("id", tree.getId());
				map.put("name", tree.getName());
				map.put("type", tree.getType());
				map.put("accessory", tree.getAccessory() != null ? this.configService.getSysConfig().getImageWebServer()
						+ "/" + tree.getAccessory().getPath() + "/" + tree.getAccessory().getName() : null);
				list.add(map);
			}
			return new Result(4200, "Success", list);
		} else {
			return new Result(-100, "token Invalidation.");
		}
	}

}
