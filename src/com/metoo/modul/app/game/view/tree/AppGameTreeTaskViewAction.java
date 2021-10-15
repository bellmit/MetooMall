package com.metoo.modul.app.game.view.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.ResponseUtils;
import com.metoo.foundation.domain.Game;
import com.metoo.foundation.domain.GameAward;
import com.metoo.foundation.domain.GameTask;
import com.metoo.foundation.domain.GameUserTask;
import com.metoo.foundation.domain.PlantingTrees;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IGameService;
import com.metoo.foundation.service.IGameTaskService;
import com.metoo.foundation.service.IGameUserTaskService;
import com.metoo.foundation.service.IPlantingtreesService;
import com.metoo.foundation.service.IUserService;
import com.metoo.modul.app.game.tree.tools.AppGameAwardTools;
import com.metoo.modul.app.game.tree.tools.AppGameTaskTools;

/**
 * <p>
 *	 Title: AppGameTaskViewActionjava
 * </p>
 * 
 * <p>
 *	 Description: 小游戏-种树-任务管理器
 * </p>
 * 
 * @author 46075
 *
 */
@Controller
@RequestMapping("app/v1/game/tree/task")
public class AppGameTreeTaskViewAction {

	@Autowired
	private IGameTaskService gameTaskService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IGameUserTaskService gameUserTaskService;
	@Autowired
	private IGameService gameService;
	@Autowired
	private AppGameTaskTools appGameTaskTools;
	@Autowired
	private AppGameAwardTools appGameAwardTools;
	@Autowired
	private IPlantingtreesService plantingtreesService;

	/**
	 * 种树-任务列表
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @param id
	 *            游戏id
	 * @return
	 */
	@RequestMapping(value = "/list.json", produces = "application/json;charset=utf-8")
	@ResponseBody
	public Object task(HttpServletRequest request, HttpServletResponse response, String token, String id) {
		int code = -1;
		String msg = "";
		Map<String, Object> map = new HashMap<String, Object>();
		User user = null;
		if (token != null && !token.equals("")) {
			user = this.userService.getObjByProperty(null, "app_login_token", token);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", 0);
		List<Game> games = this.gameService.query("SELECT obj FROM Game obj WHERE obj.status=:status", params, -1, -1);
		Game game = null;
		if (games.size() > 0) {
			game = games.get(0);
		}
		if (game != null && game.getStatus() == 0) {
			// 判断后台添加任务时，用户查看任务列表，添加相应任务
			// List<GameTask> gameTasks = game.getGameTasks();
			if (user != null) {
				// 根据任务完成次数查询任务列表
				// 0：无门槛任务 注册奖、注册邮箱
				// 查询未被领取、或今天完成的任务
				// 1, 单日任务
				params.clear();
				params.put("game_id", game.getId());
//				params.put("genre", 1);
				List<GameTask> gameTasks = this.gameTaskService.query(
						"SELECT new GameTask(obj.id, obj.title, obj.content, obj.url, obj.message, obj.frequency, obj.type, obj.time, obj.task_status, status) "
								+ "FROM GameTask obj 	" + "WHERE obj.game.id=:game_id " /*+ "AND obj.genre=:genre "*/
										+ "ORDER BY obj.sequence DESC",
						params, -1, -1);
				
				// 每日任务
				params.clear();
				params.put("game_id", game.getId());
				params.put("user_id", user.getId());
				params.put("addTime", CommUtil.dayStart());
				List<GameUserTask> gameUserTasks = this.gameUserTaskService
						.query("SELECT new GameUserTask(obj.id, obj.status, obj.frequency, obj.gameTask) " + "FROM GameUserTask obj "
								+ "WHERE obj.gameTask.game.id=:game_id " + "AND obj.user.id=:user_id "
								+ "AND obj.addTime>=:addTime ", params, -1, -1);
				// 设置用户任务完成状态
				for (int i = 0; i < gameTasks.size(); i++) {
					System.out.println(gameTasks.get(i	).getTitle());
					for (GameUserTask gameUserTask : gameUserTasks) {
						if (gameTasks.get(i).getId().equals(gameUserTask.getGameTask().getId())) {
							gameTasks.get(i).setTask_status(gameUserTask.getStatus());
							System.out.println(gameUserTask.getFrequency());
							gameTasks.get(i).setTask_frequency(gameUserTask.getFrequency());
						}
					}
				}
				
				// 只需完成一次任务（邮箱）
				params.clear();
				params.put("game_id", game.getId());
				params.put("user_id", user.getId());
				params.put("genre", 0);
				List<GameUserTask> emailTasks = this.gameUserTaskService
						.query("SELECT new GameUserTask(obj.id, obj.addTime, obj.status, obj.gameTask) " + "FROM GameUserTask obj "
								+ "WHERE obj.gameTask.game.id=:game_id " + "AND obj.user.id=:user_id "
								+ "AND obj.gameTask.genre=:genre ", params, -1, -1);
				// 设置用户任务完成状态
				for (int i = 0; i < gameTasks.size(); i++) {
					if(gameTasks.get(i).getType() == 6){
						if(emailTasks.size() > 0){
							for (GameUserTask userEmailTask : emailTasks) {
								if(userEmailTask != null){// 已经做过任务 判断执行日期
									if(CommUtil.isNow(userEmailTask.getAddTime())){
										gameTasks.get(i).setTask_status(userEmailTask.getStatus());
									}else if(userEmailTask.getStatus() < 2){
										gameTasks.get(i).setTask_status(userEmailTask.getStatus());
									}else{
										gameTasks.remove(i);
									}
								}else{
									if(user.getEmail() != null && !user.getEmail().equals("")){
										gameTasks.remove(i);
									}	
								}
							}
						}else if(user.getEmail() != null && !user.getEmail().equals("")){
							gameTasks.remove(i);
						}
						
					}
				}
				
				params.clear();
				params.put("game_id", game.getId());
				params.put("user_id", user.getId());
				params.put("status", 2);// 不展示此类任务; 已领取的，并且非当天的无门槛奖励；
				List<Integer> genres = new ArrayList<>();
				genres.add(0);
				genres.add(-1);
				params.put("genre", genres);
				params.put("addTime", CommUtil.dayStart());
				List<GameUserTask> gameUserTasks1 = this.gameUserTaskService
						.query("SELECT new GameUserTask(obj.id, obj.status, obj.gameTask) " + "FROM GameUserTask obj "
								+ "WHERE obj.gameTask.game.id=:game_id " + "AND obj.user.id=:user_id "
								+ "AND obj.status=:status " + "AND obj.addTime<=:addTime "
								+ "AND obj.gameTask.genre in(:genre) ", params, -1, -1);
				
				for (GameUserTask gameUserTask1 : gameUserTasks1) {
					boolean flag = true;
					int j = 0;
					for (int i = 0; i < gameTasks.size(); i++) {
						System.out.println("gameTaskName: " + gameTasks.get(i).getTitle());
						System.out.println("gameTaskName: " + gameTasks.get(j).getTitle());
						System.out.println("gameTaskName: " + gameUserTask1.getGameTask().getTitle());
						if (gameTasks.get(i).getId().equals(gameUserTask1.getGameTask().getId())) {
							gameTasks.remove(j);
							flag = false;
						}
						if (flag) {
							j++;
						}
					}
					
				}
				
				map.put("gameTasks", gameTasks);
			} else {
				params.clear();
				params.put("game_id", game.getId());
				List<GameTask> gameTasks = this.gameTaskService
						.query("SELECT new GameTask(obj.id, obj.title, obj.content, obj.url, obj.message, obj.frequency, obj.type, obj.time, obj.task_status, obj.status) "
								+ "FROM GameTask obj " 
								+ "WHERE obj.game.id=:game_id "
								+ "ORDER BY obj.sequence DESC", params, -1, -1);
				map.put("gameTasks", gameTasks);
			}
			return ResponseUtils.ok(map);
		} else {
			return ResponseUtils.badArgument("The game is not opened");
		}
	}
	
	/**
	 * 2:浏览 4：注册 5：邀请好友 6：添加邮箱 7：文章点赞 8：商品评论
	 * 
	 * @param task_id
	 * @param token
	 * @param email
	 * @return
	 */
	@RequestMapping("/execute.json")
	@ResponseBody
	public Object task(String task_id, String token, String email, String article_id) {
		User user = null;
		Map data = new HashMap();
		if (!token.equals("")) {
			user = this.userService.getObjByProperty(null, "app_login_token", token);
		}
		if (user != null) {
			GameTask gameTask = this.gameTaskService.getObjById(Long.parseLong(task_id));
			if (gameTask != null && gameTask.getStatus() == 1) {
				Game game = gameTask.getGame();
				if (game != null && game.getStatus() == 0) {
					//1，查询用户是否已经执行任务
					/*Map<String, Object> params = new HashMap<String, Object>(); 
					params.put("gameTask_id", gameTask.getId());
					params.put("addTime", CommUtil.dayStart());
					List<GameUserTask> gameUserTask = this.gameUserTaskService.query("SELECT new GameUserTask(id) "
							+ "FROM GameUserTask obj " + "WHERE obj.gameTask.id=:gameTask_id", params, -1, -1);
					if(gameUserTask.size() == 0){// 今日还未执行任务
						
					}*/
					// 优化（展示用户列表时，不默认创建用户任务）
					// 第一种方式：查询用户任务
					// 使用分层开发优化 switch
					boolean flag = false;// 记录任务完成状态
					boolean lose = false;
					switch (gameTask.getType()) {
					case 2:// 浏览
						flag = this.appGameTaskTools.clickTask(user, gameTask);
					case 4: // 新用户 可直接领取
						break;
					case 6: // 邮箱
						flag = this.appGameTaskTools.emailTask(user, email, gameTask);
						break;
					case 5:// 邀请新用户-返回邀请码
						String code = this.appGameTaskTools.registerTask(user);
						data.put("code", code);
						lose = true;
						// flag = this.appGameTaskTools.detectionRegisterTask(gameTask, user);
 						break;
					case 7:// 文章点赞
						flag = this.appGameTaskTools.articleTask(user, gameTask, CommUtil.null2Long(article_id));
					default:
						break;
					}
					if (flag) {
						this.appGameTaskTools.createTask(gameTask, user);
						return ResponseUtils.ok();
					}
					if(lose){
						return ResponseUtils.ok(data);
					}
					return ResponseUtils.badArgument("Params error");
				}
				return ResponseUtils.badArgument(4601, "The game is not opened");
			}
			return ResponseUtils.badArgument("Resource does not exist");
		}
		return ResponseUtils.unlogin();
	}

	/**
	 * V2：领取奖励
	 * 
	 * @param token
	 * @param task_id
	 * @return
	 */
	@RequestMapping("/getAward.json")
	@ResponseBody
	public Object getWater(String token, String id) {
		User user = null;
		Map data = new HashMap();
		if (!token.equals("")) {
			user = this.userService.getObjByProperty(null, "app_login_token", token);
		}
		if (user != null) {
			GameTask gameTask = this.gameTaskService.getObjById(Long.parseLong(id));
			if (gameTask != null && gameTask.getStatus() == 1) {
				Game game = gameTask.getGame();
				if (game != null && game.getStatus() == 0) {
					// 1，查询用户是否已种植
					Map params = new HashMap();
					params.put("user_id", user.getId());
					List<PlantingTrees> plants = this.plantingtreesService.query(
							"select obj from PlantingTrees obj where obj.user.id=:user_id",
							params, -1, -1);
					if(plants.size() > 0){
						// 2，查询用户任务
						params.clear();
						params.put("gameTask_id", gameTask.getId());
						params.put("user_id", user.getId());
						params.put("status", 1);
						List<GameUserTask> gameUserTasks = this.gameUserTaskService
								.query("SELECT obj " + "FROM GameUserTask obj " + "WHERE obj.user.id=:user_id "
										+ "AND obj.gameTask.id=:gameTask_id " + "AND obj.status=:status", params, -1, -1);
						// 新人奖和各种任务奖励
						if (gameUserTasks.size() > 0  || gameTask.getGenre() == -1 || gameTask.getGenre() == 0) {
							GameAward gameAward = gameTask.getGameAward();
							boolean flag = true;
							switch (gameTask.getGenre()) {
							case -1:
								flag = this.appGameTaskTools.detectionNewTask(gameTask, user);
								break;
							case 0: 
								flag = this.appGameTaskTools.detectionEmailTask(gameTask, user);
							default:
								break;
							}
							if(flag){
								GameUserTask gameUserTask = null;
								if(gameTask.getGenre() == -1){
									// 记录用户执行任务日志
									gameUserTask = this.appGameTaskTools.createTask(gameTask, user);
								}else{
									gameUserTask = gameUserTasks.get(0);
								}
								
								// 发放奖品
								List list = this.appGameAwardTools.createUpgradeAward(user, gameAward);
								data.put("gameAward", list);
								// 修改用户任务状态
								if(gameTask.getFrequency() == 3){
									if(gameUserTask.getFrequency() < 3){
										gameUserTask.setStatus(0);
									}else if(gameUserTask.getFrequency() == 3){
										gameUserTask.setStatus(2);
									}
								}else{
									gameUserTask.setStatus(2);
								}
								this.gameUserTaskService.update(gameUserTask);
							}
							 return ResponseUtils.ok(data);
						}
						return ResponseUtils.badArgument("Task unfinished");
					}else{
						return ResponseUtils.badArgument(4603, "Please start planting first");	
					}
				}
				return ResponseUtils.badArgument(4601, "The game is not opened");
			}
			return ResponseUtils.badArgument("Resource does not exist");
		}
		return ResponseUtils.unlogin();
	}
	
	
}
