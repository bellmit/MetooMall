package com.metoo.modul.app.game.view.tree;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.hql.ast.util.NodeTraverser.VisitationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.ResponseUtils;
import com.metoo.foundation.domain.Game;
import com.metoo.foundation.domain.GameLog;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IGameLogService;
import com.metoo.foundation.service.IGameService;
import com.metoo.foundation.service.IUserService;

@Controller
@RequestMapping("/app/v1/buyer/game/vision")
public class AppGameVisionBuyerViewAction {

	@Autowired
	private IUserService userService;
	@Autowired
	private IGameService gameService;
	@Autowired
	private IGameLogService gameLogService;

	@RequestMapping("/property.json")
	@ResponseBody
	public Object info(String token) {
		User user = CommUtil.verifyToken(token);
		if (user != null) {
			Map params = new HashMap();
			params.put("type", 1);
			List<Game> games = this.gameService.query("SELECT obj FROM Game obj WHERE obj.type=:type", params, -1, -1);
			if (games.size() > 0) {
				Game game = games.get(0);
				if (game.getSwitchs() == 1) {
					Map data = new HashMap();
					int num = game.getNum();
					// 1, 查询用户剩余可用次数
					params.clear();
					params.put("user_id", user.getId());
					params.put("addTime", CommUtil.dayStart());
					List<GameLog> gameLog = this.gameLogService.query(
							"SELECT obj FROM GameLog obj WHERE obj.user.id=:user_id AND obj.addTime>=:addTime", params,
							-1, -1);
					num = CommUtil.subtract(game.getNum(), gameLog.size()) >= 0
							? new Double(CommUtil.subtract(game.getNum(), gameLog.size())).intValue() : 0;
					data.put("num", num);
					return ResponseUtils.ok(data);
				}
			}
			return ResponseUtils.badArgument(4601, "The game is not opened");
		}
		return ResponseUtils.unlogin();
	}

	@RequestMapping("/award.json")
	@ResponseBody
	public Object vision_game(String token, Integer water) {
		User user = CommUtil.verifyToken(token);
		if (user != null) {
			Map params = new HashMap();
			params.put("type", 1);
			List<Game> games = this.gameService.query("SELECT obj FROM Game obj WHERE obj.type=:type", params, -1, -1);
			if (games.size() > 0) {
				Game game = games.get(0);
				if (game != null || game.getSwitchs() == 1) {
					Map data = new HashMap();
					// 1, 查询用户剩余可用次数
					params.clear();
					params.put("user_id", user.getId());
					params.put("addTime", CommUtil.dayStart());
					List<GameLog> gameLog = this.gameLogService.query(
							"SELECT obj FROM GameLog obj WHERE obj.user.id=:user_id AND obj.addTime>=:addTime", params,
							-1, -1);
					int num = CommUtil.subtract(game.getNum(), gameLog.size()) >= 0
							? new Double(CommUtil.subtract(game.getNum(), gameLog.size())).intValue() : 0;
					if(num > 0){
						if (water > 0 && water < 50) {
							user.setWater_drops_unused(user.getWater_drops_unused() + water);
							boolean update = this.userService.update(user);
							if (update) {
								GameLog log = new GameLog();
								log.setAddTime(new Date());
								log.setUser(user);
								log.setGame(games.get(0));
								log.setMessage("通过眼力测试游戏获得 " + water + "水滴");
								this.gameLogService.save(log);
								return ResponseUtils.ok();
							}
							return ResponseUtils.badArgument(4616, "Not enough water droplets");
						}
						return ResponseUtils.badArgument(4616, "Not enough water droplets");
					}
					return ResponseUtils.badArgument(4616, "Not enough water droplets");
				}
			}
			return ResponseUtils.badArgument(4601, "The game is not opened");
		}
		return ResponseUtils.unlogin();

	}
}
