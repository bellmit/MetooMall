package com.metoo.modul.app.game.view.tree;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.ResponseUtils;
import com.metoo.foundation.domain.Game;
import com.metoo.foundation.domain.GameAward;
import com.metoo.foundation.domain.GameGoods;
import com.metoo.foundation.domain.GameTask;
import com.metoo.foundation.domain.GameTreeLog;
import com.metoo.foundation.domain.GameUserTask;
import com.metoo.foundation.domain.PlantingTrees;
import com.metoo.foundation.domain.SubTrees;
import com.metoo.foundation.domain.Tree;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.domain.query.TreeQueryObject;
import com.metoo.foundation.service.IGameAwardService;
import com.metoo.foundation.service.IGameService;
import com.metoo.foundation.service.IGameTaskService;
import com.metoo.foundation.service.IGameTreeLogService;
import com.metoo.foundation.service.IGameUserTaskService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.ITreeService;
import com.metoo.foundation.service.IUserService;
import com.metoo.modul.app.game.tree.tools.AppGameAwardImpl;
import com.metoo.modul.app.game.tree.tools.AppGameAwardInterface;
import com.metoo.modul.app.game.tree.tools.AppGameAwardTools;
import com.metoo.module.app.buyer.domain.Result;

/**
 * <p>
 * Title: AppGameViewAction.java
 * </p>
 * 
 * <p>
 * Description： 游戏前端控制器，用来显示用户树、任务列表、水滴等
 * </p>
 * 
 * @author 46075
 *
 */
@Controller
@RequestMapping("app/v1/game/tree")
public class AppGameTreeViewAction {

	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ITreeService treeService;
	@Autowired
	private IGameTaskService gameTaskService;
	@Autowired
	private IGameService gameService;
	@Autowired
	private IGameUserTaskService gameUserTaskService;
	@Autowired
	private IGameTreeLogService gameTreeLogService;
	@Autowired
	private AppGameAwardTools appGameAwardTools;
	@Autowired
	private IGameAwardService gameAwardService;

	
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		Date date1  = cal.getTime();
		cal.add(Calendar.MINUTE, 10);
		cal.add(Calendar.SECOND, 10);
		
		
		long diff = cal.getTime().getTime() - date1.getTime();
		
		long days = diff / (1000 * 60 * 60 * 24 * 30);  
		System.out.println(days);
		long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);  
		System.out.println(hours);
	    long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);  
		System.out.println(minutes);
	    long second = (diff-days*(1000 * 60 * 60 * 24) - hours * (1000* 60 * 60)) / (1000* 60) - minutes/60;  
	   
	}
	/**
	 * 种树-列表
	 * @param request
	 * @param response
	 * @param token
	 * @param currentPage
	 * @return
	 */
	@RequestMapping(value = "/list.json", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object list2(HttpServletRequest request, HttpServletResponse response, String token, String currentPage) {
		User user = null;
		Tree tree = null;// 用户正在种植的树
		List<Tree> user_trees = new ArrayList<>();// 用户已完成的树
		if (token != null && !token.equals("")) {
			user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user != null) {
				PlantingTrees plantings = user.getPlanting_trees();
				if (plantings != null) {
					tree = plantings.getTree();
				}
				user_trees = user.getTrees();
			}
		}
		Map params = new HashMap();
		params.put("deleteStatus", 0);
		params.put("display", 0);
		List<Tree> trees = this.treeService.query(
				"SELECT obj " + "FROM Tree obj " + "WHERE obj.deleteStatus=:deleteStatus " + "AND obj.display=:display",
				params, -1, -1);
		if (trees.size() > 0) {
			List<Map> maps = new ArrayList<Map>();
			for (Tree obj : trees) {
				params.clear();
				params.put("type", 6);
				params.put("tree_id", obj.getId());
				List<GameAward> gameAwards = this.gameAwardService.query(
						"SELECT obj " + "FROM GameAward obj " + "WHERE obj.type=:type " + "AND obj.tree.id=:tree_id",
						params, -1, -1);
				if (gameAwards.size() > 0) {
					for (GameAward gameAward : gameAwards) {
						List<GameGoods> gameGoodsList = gameAward.getGameGoodsList();
						if (gameGoodsList.size() > 0) {
							for (GameGoods gameGoods : gameGoodsList) {
								Map map = new HashMap();
								map.put("goods_id", gameGoods.getGoods_id());
								map.put("goods_detail", gameGoods.getGoods_detail());
								map.put("accessory",
										gameGoods.getAccessory() == null ? ""
												: this.configService.getSysConfig().getImageWebServer() + "/"
														+ gameGoods.getAccessory());
								map.put("id", gameGoods.getId());
								maps.add(map);
							}
						}
					}
				}
			}
			return ResponseUtils.ok(maps);
		}
		return ResponseUtils.unlogin();
	}
	
	public List<GameTreeLog> query(Map params) {
		return this.gameTreeLogService.query(
				"SELECT obj FROM GameTreeLog obj WHERE obj.type in(:type) AND obj.user_id=:user_id", params, -1, -1);
	}
}
