package com.metoo.modul.app.game.view.tree;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.metoo.core.constant.Globals;
import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.database.DatabaseTools;
import com.metoo.foundation.domain.Accessory;
import com.metoo.foundation.domain.Friend;
import com.metoo.foundation.domain.Game;
import com.metoo.foundation.domain.GameAward;
import com.metoo.foundation.domain.GameGoods;
import com.metoo.foundation.domain.GameTreeLog;
import com.metoo.foundation.domain.PlantingTrees;
import com.metoo.foundation.domain.SubTrees;
import com.metoo.foundation.domain.Tree;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IFriendService;
import com.metoo.foundation.service.IGameService;
import com.metoo.foundation.service.IGameTreeLogService;
import com.metoo.foundation.service.IPlantingtreesService;
import com.metoo.foundation.service.ISubTreesService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.IUserService;
import com.metoo.modul.app.game.tree.tools.AppFriendBuyerTools;
import com.metoo.modul.app.game.tree.tools.AppGameAwardTools;
import com.metoo.modul.app.game.tree.tools.AppGameTreeTools;
import com.metoo.module.app.buyer.domain.Result;
import com.metoo.module.app.manage.buyer.tool.AppUserTools;
import com.metoo.module.app.view.web.tool.AppRegisterViewTools;
import com.metoo.module.app.view.web.tool.AppobileTools;

import net.sf.json.JSONArray;

/**
 * <p>
 * Title: AppFriendBuyerAction.java
 * </p>
 * 
 * <p>
 * Description: 种树小游戏，邀请好友;(邀请好友免费得300水滴); # 抢好友水滴 # 帮好友浇水
 * </p>
 * 
 * 
 * 
 * @author 46075
 *
 */
@Controller
@RequestMapping("app/v1/game/tree/friend/")
public class AppGameTreeFriendBuyerAction {

	@Autowired
	private IUserService userService;
	@Autowired
	private AppobileTools mobileTools;
	@Autowired
	private AppRegisterViewTools appRegisterViewTools;
	@Autowired
	private AppUserTools appUserTools;
	@Autowired
	private IFriendService friendService;
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IGameService gameService;
	@Autowired
	private IGameTreeLogService gameTreeLogService;
	@Autowired
	private ISubTreesService subTreesService;
	@Autowired
	private IPlantingtreesService plantingtreesService;
	@Autowired
	private AppGameAwardTools appGameAwardTools;
	@Autowired
	private AppFriendBuyerTools appFriendBuyerTools;
	@Autowired
	private DatabaseTools databaseTools;

	/**
	 * 用户好友列表
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/list.json", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response, String token) {
		String msg = "Success";
		int code = 4200;
		User user = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (!CommUtil.null2String(token).equals("")) {
			user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user != null) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("deleteStatus", 0);
				params.put("user_id", user.getId());
				params.put("status", 1);
				List<Friend> friends = this.friendService
						.query("SELECT obj FROM " + "Friend obj " + "WHERE obj.deleteStatus=:deleteStatus "
								+ "AND obj.user_id=:user_id " + "AND obj.status=:status", params, -1, -1);
				for (Friend friend : friends) {
					Map<String, Object> map = this.appUserTools.get(friend.getFriend());
					map.put("friend_id", friend.getFriend().getId());
					list.add(map);
				}
			} else {
				code = 4220;
				msg = "The user does not exist";
			}
		} else {
			msg = "token Invalidation";
			code = -100;
		}
		return JSONObject.toJSONString(new Result(code, msg, list));

	}

	/**
	 * 添加好友：通过好友账号，用户名，邮箱，查找用户
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/get.json", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String get(HttpServletRequest request, HttpServletResponse response, String account, String token) {
		int code = 4200;
		String msg = "Success";
		List<User> list = null;
		if (!CommUtil.null2String(account).equals("")) {
			Map params = new HashMap();
			boolean flag = this.mobileTools.verify(account);
			StringBuffer sb = new StringBuffer("select obj from ");
			sb.append(User.class.getName()).append(" obj").append(" where ");
			sb.append("obj.userName=:userName");
			String sql = null;
			if (flag) {
				Map map = this.mobileTools.mobile(account);
				String areaMobile = (String) map.get("userMobile");
				params.put("mobile", areaMobile);
				sb.append(" or obj.mobile=:mobile");
				account = areaMobile;
			}
			boolean verify = this.appRegisterViewTools.verify_email(account);
			if (verify) {
				params.put("email", account);
				sb.append(" or obj.email=:email");
			}
			params.put("userName", account);
			List<User> users = this.userService.query(sb.toString(), params, -1, -1);
			list = this.appUserTools.get(users);
			if (list == null) {
				code = 4220;
				msg = "The user does not exist";
			}
		} else {
			code = 4403;
			msg = "Parameter is null";
		}
		return JSONObject.toJSONString(new Result(code, msg, list));

	}

	/**
	 * 发送添加好友申请
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @param token
	 *            用户身份令牌
	 * @param message
	 *            发送申请信息
	 * @return
	 */
	@RequestMapping("apply.json")
	@ResponseBody
	public String apply(HttpServletRequest request, HttpServletResponse response, String id, String token,
			String message) {
		String msg = "Success";
		int code = 4200;
		if (!CommUtil.null2String(token).equals("")) {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user != null) {
				User friend = this.userService.getObjById(CommUtil.null2Long(id));
				if (friend != null) {
					if (!friend.getId().equals(user.getId())) {
						List<Friend> friendList = null;
						Map params = new HashMap();
						// 查询A=B(A=B:双方互为好友、 A->B: A申请添加B为好友、A<-B: B申请添加A为好友)
						params.clear();
						params.put("deleteStatus", 0);
						params.put("user_id", user.getId());
						params.put("friend_id", friend.getId());
						params.put("status", 1);
						friendList = this.friendService.query(
								"SELECT obj FROM Friend obj WHERE obj.deleteStatus=:deleteStatus AND obj.user_id=:user_id AND obj.friend.id=:friend_id AND obj.status=:status",
								params, -1, -1);
						if (friendList.size() < 1) {// 判断 user <==> friend
													// 是否互为好友
							// 查询A->B
							params.clear();
							params.put("deleteStatus", 0);
							params.put("user_id", friend.getId());
							params.put("friend_id", user.getId());
							params.put("status", 0);
							friendList = this.friendService.query(
									"SELECT obj FROM Friend obj WHERE obj.deleteStatus=:deleteStatus AND obj.user_id=:user_id AND obj.friend.id=:friend_id AND obj.status=:status",
									params, -1, -1);
							if (friendList.size() < 1) {// 判断 A <== B
														// 好友是否已添加我为好友、已添加自动设置双方为好友
								// 查询是否已生成添加好友请求
								params.clear();
								params.put("deleteStatus", 0);
								params.put("user_id", user.getId());
								params.put("friend_id", friend.getId());
								friendList = this.friendService.query(
										"SELECT obj FROM Friend obj WHERE obj.deleteStatus=:deleteStatus AND obj.user_id=:user_id AND obj.friend.id=:friend_id",
										params, -1, -1);
								// 查询今天是否已提交申请 to_days
								params.clear();
								// 查询今日是否已生成添加好友请求
								String sql = "SELECT COUNT(*) FROM metoo_game_treelog obj WHERE obj.user_id="
										+ user.getId() + " AND " + " obj.friend_id=" + friend.getId() + " AND "
										+ "type=5" + " AND " + "status=1" + " AND "
										+ " TO_DAYS(obj.addTime)=TO_DAYS(now())";
								int number = this.databaseTools.queryNum(sql);
								boolean flag = false;
								if (number < 1) {
									flag = true;
								}
								Friend obj = null;
								if (friendList.size() == 0) {
									obj = new Friend();
									obj.setAddTime(new Date());
									obj.setStatus(0);
									obj.setUser_id(user.getId());
									obj.setUserName(user.getUserName());

									obj.setFriend(friend);
									obj.setMobile(friend.getMobile());
									obj.setSex(friend.getSex());
									obj.setFriendName(friend.getUsername());

									obj.setVerification_information(message);

									this.friendService.save(obj);

									if (flag)
										// 发送好友申请、记录好友申日志
										this.appFriendBuyerTools.apply(user, friend);
								} else {
									obj = friendList.get(0);
									int status = obj.getStatus();
									if (status == 0) {
										code = 4200;
										msg = "Success";
										if (flag)
											this.appFriendBuyerTools.apply(user, friend);
										/*
										 * AppGameFactory factory = new
										 * AppGameFactory();
										 * AppGameTreeLogInterface interfaces =
										 * factory.creatLog("ADD"); // 创建申请日志
										 * interfaces.add(user, friend);
										 */

									} else if (status == 1) {
										code = 4223;
										msg = "Have become friends";
									} else if (status == 2) {
										code = 4222;
										msg = "Added, pending reply";
										obj.setStatus(0);
										this.friendService.update(obj);
									}
								}
							} else {
								code = 4200;
								msg = "Success";
								Friend obj = friendList.get(0);
								obj.setStatus(1);
								this.friendService.update(obj);
								Friend newFriend = new Friend();
								newFriend.setAddTime(new Date());
								newFriend.setStatus(1);
								newFriend.setUser_id(user.getId());
								newFriend.setUserName(user.getUserName());

								newFriend.setFriendName(friend.getUserName());
								newFriend.setMobile(friend.getMobile());
								newFriend.setSex(friend.getSex());
								newFriend.setFriend(friend);

								this.friendService.save(newFriend);

								// 调整申请记录 A->B
								params.clear();
								params.put("user_id", friend.getId());
								params.put("friend_id", user.getId());
								params.put("type", 5);
								this.appFriendBuyerTools.verifyFriend(params, 2);

								// A->B （被）
								params.clear();
								params.put("user_id", user.getId());
								params.put("friend_id", friend.getId());
								params.put("type", 5);
								this.appFriendBuyerTools.verifyFriend(params, -2);
							}
						} else {
							code = 4223;
							msg = "Have become friends";
						}
					} else {
						code = 4225;
						msg = "You cannot add yourself as a friend";
					}
				} else {
					code = 4221;
					msg = "Friends don't exist";
				}
			} else {
				code = 4220;
				msg = "The user does not exist";
			}
		} else {
			msg = "token Invalidation";
			code = -100;
		}
		return JSONObject.toJSONString(new Result(code, msg));
	}

	/**
	 * 好友验证
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @param token
	 * @return
	 */
	@RequestMapping("verify.json")
	@ResponseBody
	public String verify(HttpServletRequest request, HttpServletResponse response, String id, String token,
			Integer status) {
		String msg = "Success";
		int code = 4200;
		if (!CommUtil.null2String(token).equals("")) {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user != null) {
				User friend = this.userService.getObjById(CommUtil.null2Long(id));
				if (friend != null) {
					Map params = new HashMap();

					List<Friend> friends = null;
					// 判断双方是否已是好友
					params.put("deleteStatus", 0);
					params.put("user_id", user.getId());
					params.put("friend_id", friend.getId());
					params.put("status", 1);
					friends = this.friendService.query(
							"SELECT obj FROM Friend obj WHERE obj.deleteStatus=:deleteStatus AND obj.friend.id=:friend_id AND obj.user_id=:user_id AND obj.status=:status",
							params, -1, -1);
					if (friends.size() < 1) {
						// 查询是否有此好友申请
						params.clear();
						params.put("deleteStatus", 0);
						params.put("user_id", friend.getId());
						params.put("friend_id", user.getId());
						params.put("status", 0);
						friends = this.friendService.query(
								"SELECT obj FROM Friend obj WHERE obj.deleteStatus=:deleteStatus AND obj.friend.id=:friend_id AND obj.user_id=:user_id AND obj.status=:status",
								params, -1, -1);
						if (friends.size() > 0) {
							for (Friend obj : friends) {
								obj.setStatus(1);
								this.friendService.update(obj);
								Friend newFriend = new Friend();
								newFriend.setAddTime(new Date());
								newFriend.setStatus(status == 1 ? 1 : status == 2 ? 2 : 0);
								newFriend.setUser_id(user.getId());
								newFriend.setUserName(user.getUserName());

								newFriend.setFriendName(friend.getUserName());
								newFriend.setMobile(friend.getMobile());
								newFriend.setSex(friend.getSex());
								newFriend.setFriend(friend);

								this.friendService.save(newFriend);

								// 修改添加好友日志
								params.clear();
								params.put("user_id", user.getId());
								params.put("friend_id", friend.getId());
								params.put("type", 5);
								this.appFriendBuyerTools.verifyFriend(params, status == 1 ? -2 : -3);

								params.clear();
								params.put("user_id", friend.getId());
								params.put("friend_id", user.getId());
								params.put("type", 5);
								this.appFriendBuyerTools.verifyFriend(params, status == 1 ? 2 : 3);
								// break;
							}
						} else {
							code = 5440;
							msg = "Parameter error";
						}
					} else {
						code = 4223;
						msg = "Have become friends";
						// 此处不存在双方互相添加记录；做了互相添加好友验证
						/*
						 * params.clear(); params.put("deleteStatus", 0);
						 * params.put("friend_id", user.getId());
						 * params.put("user_id", friend.getId());
						 * params.put("status", 0); friends =
						 * this.friendService.query(
						 * "SELECT obj FROM Friend obj WHERE obj.deleteStatus=:deleteStatus AND obj.friend.id=:friend_id AND obj.user_id=:user_id AND obj.status=:status"
						 * , params, -1, -1);
						 * this.friendService.delete(friends.get(0).getId());
						 */
					}
				} else {
					code = 4221;
					msg = "The user does not exist";
				}
			} else {
				code = 4220;
				msg = "The user does not exist";
			}
		} else {
			msg = "token Invalidation";
			code = -100;
		}
		return JSONObject.toJSONString(new Result(code, msg));
	}

	/**
	 * 删除好友
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping("remove.json")
	@ResponseBody
	public String refuse(HttpServletRequest request, HttpServletResponse response, String id, String token) {
		String msg = "Success";
		int code = 4200;
		if (!CommUtil.null2String(token).equals("")) {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user != null) {
				User friend = this.userService.getObjById(CommUtil.null2Long(id));
				if (friend != null) {
					Map params = new HashMap();
					params.put("deleteStatus", 0);
					params.put("user_id", user.getId());
					params.put("friend_id", friend.getId());
					params.put("status", 1);
					List<Friend> friendList = this.friendService.query(
							"SELECT obj FROM Friend obj WHERE obj.deleteStatus=:deleteStatus AND obj.user_id=:user_id AND obj.friend.id=:friend_id AND obj.status=:status",
							params, -1, -1);
					if (friendList.size() > 0) {
						for (Friend f : friendList) {
							f.setDeleteStatus(-1);
							f.setStatus(-1);
							this.friendService.update(f);
						}
						params.clear();
						params.put("deleteStatus", 0);
						params.put("user_id", friend.getId());
						params.put("friend_id", user.getId());
						params.put("status", 1);
						List<Friend> friends = this.friendService.query(
								"SELECT obj FROM Friend obj WHERE obj.deleteStatus=:deleteStatus AND obj.user_id=:user_id AND obj.friend.id=:friend_id AND obj.status=:status",
								params, -1, -1);
						if (friends.size() > 0) {
							Friend obj = friends.get(0);
							obj.setDeleteStatus(-1);
							obj.setStatus(-1);
							this.friendService.update(obj);

							// 创建日志
							this.appFriendBuyerTools.remove(user, friend);

							/*
							 * List<GameTreeLog> gameTreeLogs =
							 * this.gameTreeLogService.query(
							 * "SELECT obj FROM GameTreeLog obj WHERE obj.user_id=:user_id AND obj.friend_id=:friend_id AND obj.type=:type"
							 * , params, -1, -1); if(gameTreeLogs.size() > 0){
							 * GameTreeLog gameTreeLog = gameTreeLogs.get(0);
							 * gameTreeLog.setStatus(4);
							 * this.gameTreeLogService.update(gameTreeLog); }
							 */
						}
					} else {
						code = 4224;
						msg = "Non-friend relationship";
					}
				} else {
					code = 4221;
					msg = "The user does not exist";
				}
			} else {
				code = 4220;
				msg = "The user does not exist";
			}
		} else {
			msg = "token Invalidation";
			code = -100;
		}
		return JSONObject.toJSONString(new Result(code, msg));
	}

	@RequestMapping("notification.json")
	@ResponseBody
	public Object notification(HttpServletRequest request, HttpServletResponse response, String token) {
		Map map = new HashMap();
		List<GameTreeLog> gameTreeLogs = null;
		int code = 4200;
		String msg = "Success";
		User user = null;
		if ((user = CommUtil.verifyToken(token)) != null) {
			Map params = new HashMap();
			List<Integer> types = new ArrayList<Integer>();
			types.add(-1);
			types.add(-6);
			types.add(5);
			params.put("type", types);
			params.put("user_id", user.getId());
			map.put("Water", this.query(params));

			/*
			 * params.put("type", 5); params.put("user_id", user.getId());
			 * map.put("Validation", this.query(params));
			 */
		} else {
			code = -100;
			msg = "token Invalidation";
		}
		return new Result(code, msg, map);
	}

	public List<GameTreeLog> query(Map params) {
		List<GameTreeLog> logs = this.gameTreeLogService.query(
				"SELECT obj FROM GameTreeLog obj WHERE obj.type in(:type) AND obj.user_id=:user_id order by obj.addTime DESC",
				params, -1, -1);
		for (GameTreeLog log : logs) {
			log.setLog_read(1);
			this.gameTreeLogService.update(log);
		}
		return logs;
	}

	/**
	 * 树
	 * 
	 * @param request
	 * @param response
	 * @param token
	 *            用户身份令牌 TOKEN
	 * 
	 * @param id
	 *            好友ID
	 * @return
	 */
	@RequestMapping(value = "/planting.json", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object getFrinendTree(HttpServletRequest request, HttpServletResponse response, String token, String id) {
		String msg = "Success";
		int code = 4200;
		Map map = new HashMap();
		if (!CommUtil.null2String(token).equals("")) {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			System.out.println(user.getUsername());
			if (user != null) {
				User friend = this.userService.getObjById(CommUtil.null2Long(id));
				map.put("user_name", friend.getUsername());
				Map params = new HashMap();
				params.put("user", user.getId());
				params.put("friend", friend.getId());
				params.put("status", 1);
				List<User> objs = this.userService.query(
						"SELECT obj FROM Friend obj WHERE obj.user_id=:user AND obj.friend.id=:friend AND obj.status=:status",
						params, -1, -1);
				if (objs.size() > 0) {
					map.put("unused_water", friend.getWater_drops_unused());
					PlantingTrees plantingTree = friend.getPlanting_trees();
					if (plantingTree != null) {
						SubTrees subTree = plantingTree.getSubTree();
						if (subTree != null) {
							map.put("name", plantingTree.getName());
							map.put("watering", plantingTree.getWatering());
							map.put("accessory",
									plantingTree.getAccessory() != null
											? this.configService.getSysConfig().getImageWebServer() + "/"
													+ plantingTree.getAccessory().getPath() + "/"
													+ plantingTree.getAccessory().getName()
											: "");
							Tree trees = plantingTree.getTree();
							int watering = subTree.getWatering();
							map.put("schedule",
									Math.round(CommUtil.div(plantingTree.getGrade_watering(), watering) * 100));
							SubTrees sub = this.subTreesService.getObjById(plantingTree.getSub_trees());
							// 判断是否已完成种植
							if (subTree.getStatus() == 12 && plantingTree.getGrade_watering() >= sub.getWatering()) {
								map.put("fill_level", true);
							}
							// 添加访问日志
							// 设置互为好友 日志
							// this.appFriendBuyerTools.visit(user, friend);
						} else {
							code = 4500;
							msg = "System error, please contact customer service";
						}
					} else {
						code = 4603;
						msg = "The user has not yet selected a tree to grow";
					}
				} else {
					code = 4224;
					msg = "It's not your best friend";
				}
			} else {
				msg = "token Invalidation";
				code = -100;
			}
		} else {
			msg = "token Invalidation";
			code = -100;
		}
		return new Result(code, msg, map);
	}

	/**
	 * 浇水
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @param user_id
	 * @return
	 */
	@RequestMapping("/share_water.json")
	@ResponseBody
	public Object share_water(HttpServletRequest request, HttpServletResponse response, String token, String user_id) {
		// 1，判断游戏状态
		// 2，判断水滴数是否大于可用水滴数
		// 3，判断是否互为好友
		String msg = "";
		int code = -1;
		Map map = new HashMap();
		if (!CommUtil.null2String(token).equals("")) {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			int unused = user.getWater_drops_unused();
			Map params = new HashMap();

			List<Game> games = this.gameService.query("SELECT obj FROM Game obj", params, -1, -1);
			Game gameTree = null;
			if (games.size() > 0) {
				gameTree = games.get(0);
			}
			if (gameTree != null && gameTree.getStatus() == 0) {
				int water = gameTree.getShar_water();
				map.put("water", water);
				if (unused > 0 && unused >= water) {
					params.clear();
					params.put("deleteStatus", 0);
					params.put("user_id", user.getId());
					params.put("friend_id", Long.parseLong(user_id));
					params.put("status", 1);
					List<Friend> friends = this.friendService.query(
							"SELECT obj FROM Friend obj WHERE obj.deleteStatus=:deleteStatus AND obj.user_id=:user_id AND friend.id=:friend_id AND obj.status=:status",
							params, -1, -1);
					if (friends.size() > 0) {
						User friend = this.userService.getObjById(CommUtil.null2Long(user_id));
						if (friend != null) {
							PlantingTrees plantingTree = friend.getPlanting_trees();
							if (plantingTree != null) {
								Tree tree = plantingTree.getTree();
								if (plantingTree.getStatus() <= 12 && plantingTree.getStatus() == 0) {
									SubTrees subTree = plantingTree.getSubTree();
									if (subTree.getStatus() <= 12) {
										map.put("flag", Boolean.FALSE);
										if (plantingTree.getGrade_watering() + water >= subTree.getWatering()) {// 升级
											params.clear();
											params.put("tree_id", plantingTree.getTree().getId());
											params.put("status", subTree.getStatus() + 1);
											List<SubTrees> subTrees = this.subTreesService.query(
													"select obj from SubTrees obj where obj.tree.id=:tree_id and obj.status=:status order by obj.watering desc",
													params, -1, -1);
											if (subTrees.size() > 0) {
												SubTrees next_subtree = subTrees.get(0);
												plantingTree.setAccessory(next_subtree.getAccessory());
												plantingTree.setGrade_watering(new Double(
														CommUtil.subtract(plantingTree.getGrade_watering() + water,
																subTree.getWatering())).intValue());
												plantingTree.setSub_trees(next_subtree.getId());
												plantingTree.setSubTree(next_subtree);
											}
											if (subTree.getGameAward() != null) {
												List gameAward = appGameAwardTools.createUpgradeAward(friend,
														subTree.getGameAward(), 0); // 阶段奖品
												// 记录升级日志
												// 好友记录谁帮他浇了水
												GameTreeLog ltg = new GameTreeLog();
												ltg.setAddTime(new Date());
												ltg.setUser_id(friend.getId());
												ltg.setUser_name(friend.getUsername());
												ltg.setFriend_id(user.getId());
												ltg.setFriend_name(user.getUserName());
												ltg.setWater(water);
												ltg.setTree_id(gameTree.getId());
												ltg.setType(10);
												ltg.setStatus(-10);
												ltg.setGameAward(JSONArray.fromObject(gameAward).toString());
												this.gameTreeLogService.save(ltg);
											}
											if (subTree.getStatus() == 12) {
												plantingTree.setStatus(1);
												map.put("schedule", 100);
												map.put("progress", 100);
												// 满级奖品
												GameGoods gameGoods = plantingTree.getGameGoods();
												if (gameGoods != null) {
													GameAward gameAward = gameGoods.getGameAward();
													if (gameAward != null) {
														List accomplishAward = appGameAwardTools
																.createUpgradeAward(user, gameAward);
														map.put("accomplishAward", accomplishAward);
														// 记录满级日志
														// 好友记录谁帮他浇了水
														GameTreeLog ltg = new GameTreeLog();
														ltg.setAddTime(new Date());
														ltg.setUser_id(friend.getId());
														ltg.setUser_name(friend.getUsername());
														ltg.setFriend_id(user.getId());
														ltg.setFriend_name(user.getUserName());
														ltg.setWater(CommUtil.null2Int(Math.floor(water)));
														ltg.setTree_id(gameTree.getId());
														ltg.setType(12);
														ltg.setStatus(-12);
														ltg.setGameAward(
																JSONArray.fromObject(accomplishAward).toString());
														this.gameTreeLogService.save(ltg);
													}
												}
											}
											map.put("flag", Boolean.TRUE);
										}
										plantingTree.setGrade_watering(plantingTree.getGrade_watering() + water);
										plantingTree.setWatering(plantingTree.getWatering() + water);
										user.setWater_drops_unused(user.getWater_drops_unused() - water);
										user.setWater_drop_used(user.getWater_drop_used() + water);
										this.plantingtreesService.update(plantingTree);
										this.userService.update(user);
									}
									map.put("schedule", Math.floor(CommUtil.div02(plantingTree.getGrade_watering(),
											plantingTree.getSubTree().getWatering()) * 100));
									map.put("progress", Math
											.floor(CommUtil.div02(plantingTree.getWatering(), tree.getWaters()) * 100));
									map.put("remaining_water",
											CommUtil.subtract(subTree.getWatering(), plantingTree.getGrade_watering()));
									// 记录浇水日志
									GameTreeLog gtl = new GameTreeLog();
									gtl.setAddTime(new Date());
									gtl.setUser_id(user.getId());
									gtl.setUser_name(user.getUsername());
									gtl.setFriend_id(friend.getId());
									gtl.setFriend_name(friend.getUserName());
									gtl.setWater(CommUtil.null2Int(Math.floor(water)));
									gtl.setTree_id(gameTree.getId());
									gtl.setType(6);
									gtl.setStatus(6);
									String message = "You watered " + Globals.PREFIXHTML + friend.getUserName() + Globals.SUFFIXHTML + "'s tree with " + water
											+ " water-drops";
									gtl.setMessage(message);
									this.gameTreeLogService.save(gtl);

									// 好友记录谁帮他浇了水
									GameTreeLog ltg = new GameTreeLog();
									ltg.setAddTime(new Date());
									ltg.setUser_id(friend.getId());
									ltg.setUser_name(friend.getUsername());
									ltg.setFriend_id(user.getId());
									ltg.setFriend_name(user.getUserName());
									ltg.setWater(CommUtil.null2Int(Math.floor(water)));
									ltg.setTree_id(gameTree.getId());
									ltg.setType(6);
									ltg.setStatus(-6);
									String message1 = Globals.PREFIXHTML + user.getUserName() + Globals.SUFFIXHTML + " watered your tree with " + water
											+ " water-drops";
									ltg.setMessage(message1);
									this.gameTreeLogService.save(ltg);
									code = 4200;
									msg = "Success";
								} else {
									code = 4610;
									msg = "The current game is over";
								}
							} else {
								code = 4603;
								msg = "The user has not yet selected a tree to grow";
							}
						} else {
							msg = "Is not a friend";
							code = 4611;
						}
					} else {
						msg = "Is not a friend";
						code = 4611;
					}
				} else {
					msg = "Lack of water droplets";
					code = 4602;
				}
			} else {
				msg = "The current game is over";
				code = 4610;
			}
		} else {
			msg = "token Invalidation";
			code = -100;
		}
		return new Result(code, msg, map);
	}

	/**
	 * 浇水日志
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping("/getLog.json")
	@ResponseBody
	public Object water_log(HttpServletRequest request, HttpServletResponse response, String token) {
		User user = null;
		Result result = null;
		if (!"".equals(CommUtil.null2String(token))) {
			user = this.userService.getObjByProperty(null, "app_login_token", token);
		}
		if (user != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Date dayStart = calendar.getTime();

			System.out.println(calendar.getTime());

			// 一天的结束时间 yyyy:MM:dd 23:59:59
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			Date dayEnd = calendar.getTime();

			params.put("dayStart", dayStart);
			params.put("dayEnd", dayEnd);
			params.put("friend_id", user.getId());
			params.put("type", 0);
			List<GameTreeLog> log = this.gameTreeLogService.query(
					"SELECT obj FROM GameTreeLog obj WHERE obj.addTime>=:dayStart AND obj.addTime<=:dayEnd AND obj.friend_id=:friend_id AND obj.type=:type",
					params, -1, -1);
			result = new Result(4200, "Success", log);
		} else {
			result = new Result(-100, "token Invalidation");
		}
		return result;

	}

	/*
	 * public static void main(String[] args) { Calendar calendar =
	 * Calendar.getInstance(); calendar.set(Calendar.HOUR_OF_DAY,0);
	 * calendar.set(Calendar.MINUTE,0); calendar.set(Calendar.SECOND,0);
	 * calendar.set(Calendar.MILLISECOND,0); Date dayStart = calendar.getTime();
	 * 
	 * System.out.println(calendar.getTime());
	 * 
	 * //一天的结束时间 yyyy:MM:dd 23:59:59 calendar.set(Calendar.HOUR_OF_DAY,23);
	 * calendar.set(Calendar.MINUTE,59); calendar.set(Calendar.SECOND,59);
	 * calendar.set(Calendar.MILLISECOND,999);
	 * System.out.println(calendar.getTime()); }
	 */

	/**
	 * 收取好友水滴
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @param user_id
	 * @return
	 */
	@RequestMapping("steal.json")
	@ResponseBody
	public Object steal(HttpServletRequest request, HttpServletResponse response, String token, String user_id) {
		String msg = "";
		int code = -1;
		Result result = null;
		if (!CommUtil.null2String(token).equals("")) {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			int unused = user.getWater_drops_unused();
			Map params = new HashMap();

			List<Game> games = this.gameService.query("SELECT obj FROM Game obj", params, -1, -1);
			Game game = null;
			if (games.size() > 0) {
				game = games.get(0);
			}
			if (game != null && game.getStatus() == 0) {
				// this.userService.getObjById(user_id);
				// 查询是否超过每日水滴上限以及收取次数
				int upper_limit = game.getUpper_limit();
				int frequency_limit = game.getFrequency_limit();
				// 查询好友被收取水滴上限 1,查询日志 2,每次收取水滴在用户当前树种记录
				User friend = this.userService.getObjById(CommUtil.null2Long(user_id));
				params.put("user_id", user.getId());
				params.put("friend_id", friend.getId());
				params.put("type", 9);
				params.put("dayStart", CommUtil.dayStart());
				List<GameTreeLog> logs = this.gameTreeLogService.query(
						"SELECT obj from GameTreeLog obj "
						+ "WHERE "
						+ "obj.addTime>=:dayStart "
						+ "AND obj.friend_id=:friend_id "
						+ "AND obj.type=:type "
						+ "AND obj.user_id=:user_id",
						params, -1, -1);
				int sum = 0;
				for (GameTreeLog log : logs) {
					sum += log.getWater();
				}
				int drops_unused = friend.getWater_drops_unused();
				if (sum <= upper_limit && logs.size() <= frequency_limit) { // 未超过收取次数
					if (drops_unused > 0) {
						int gather = game.getGather();
						double water = Math.floor(CommUtil.mul(drops_unused, gather * 0.01));
						if (drops_unused >= water) {
							user.setWater_drops_unused(
									new Double(CommUtil.add(user.getWater_drops_unused(), water)).intValue());

							friend.setWater_drop_used(
									new Double(CommUtil.add(friend.getWater_drop_used(), water)).intValue());
							friend.setWater_drops_unused(
									new Double(CommUtil.subtract(friend.getWater_drops_unused(), water)).intValue());

							this.userService.update(user);
							this.userService.update(friend);
							// 记录收取日志
							GameTreeLog gtl = new GameTreeLog();
							gtl.setAddTime(new Date());
							gtl.setUser_id(user.getId());
							gtl.setUser_name(user.getUsername());
							gtl.setFriend_id(friend.getId());
							gtl.setFriend_name(friend.getUserName());
							gtl.setWater(new Double(water).intValue());
							gtl.setTree_id(game.getId());
							gtl.setType(9);
							gtl.setStatus(9);
							String message = "You got " + new Double(water).intValue() + " water-drops from " + Globals.PREFIXHTML + friend.getUserName() + Globals.SUFFIXHTML;
							gtl.setMessage(message);
							this.gameTreeLogService.save(gtl);

							// 记录好友被收取多少水滴
							GameTreeLog ltg = new GameTreeLog();
							ltg.setAddTime(new Date());
							ltg.setUser_id(friend.getId());
							ltg.setUser_name(friend.getUsername());
							ltg.setFriend_id(user.getId());
							ltg.setFriend_name(user.getUserName());
							ltg.setWater(new Double(water).intValue());
							ltg.setTree_id(game.getId());
							ltg.setType(9);
							ltg.setStatus(-9);
							ltg.setMessage(Globals.PREFIXHTML + user.getUserName() + Globals.SUFFIXHTML + " got " + new Double(water).intValue() + " water-drops from you");
							this.gameTreeLogService.save(ltg);

							return new Result(4200, "Success", water);
						} else {
							// 用户水滴不足
							return new Result(4602, "Lack of water droplets");
						}
					} else {
						// 用户水滴不足
						return new Result(4602, "Lack of water droplets");
					}
				} else {
					// 今日超过被收取上限
					return new Result(4612, "Has reached its limit");
				}
			} else {
				return new Result(4601, "The game is not opened");
			}
		}
		return result = new Result(-100, "token Invalidation");
	}

}
