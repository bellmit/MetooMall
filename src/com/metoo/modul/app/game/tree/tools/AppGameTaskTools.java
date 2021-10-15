package com.metoo.modul.app.game.tree.tools;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.Article;
import com.metoo.foundation.domain.ArticleInfo;
import com.metoo.foundation.domain.GameTask;
import com.metoo.foundation.domain.GameUserTask;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IAddressService;
import com.metoo.foundation.service.IArticleInfoService;
import com.metoo.foundation.service.IArticleService;
import com.metoo.foundation.service.IGameTaskService;
import com.metoo.foundation.service.IGameUserTaskService;
import com.metoo.foundation.service.IUserService;

@Component
public class AppGameTaskTools {

	@Autowired
	private IGameUserTaskService gameUserTaskService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IGameTaskService gameTaskService;
	@Autowired
	private IArticleInfoService articleInfoService;
	@Autowired
	private IArticleService articleService;
	@Autowired
	private IAddressService addressService;

	/**
	 * 浏览
	 * 
	 * @param token
	 * @param email
	 * @return 返回任务状态
	 */

	public boolean clickTask(User user, GameTask gameTask) {
		Map params = new HashMap();
		params.put("gameTask_id", gameTask.getId());
		params.put("user_id", user.getId());
		params.put("addTime", CommUtil.dayStart());
		List<GameUserTask> gameUserTasks = this.gameUserTaskService.query("SELECT obj " + "FROM GameUserTask obj "
				+ "WHERE obj.user.id=:user_id " + "AND obj.gameTask.id=:gameTask_id " + "AND obj.addTime>=:addTime",
				params, -1, -1);
		
		if (gameUserTasks.size() < 1) {
			return true;
		} else if (gameUserTasks.get(0).getFrequency() < 3) {
			GameUserTask gameUserTask = gameUserTasks.get(0);
			gameUserTask.setFrequency(gameUserTask.getFrequency() + 1);
			gameUserTask.setStatus(1);
			this.gameUserTaskService.update(gameUserTask);
		}
		return false;
	}

	/**
	 * 添加邮箱
	 * 
	 * @param token
	 * @param email
	 * @return 返回任务状态
	 */

	public boolean emailTask(User user, String email, GameTask gameTask) {
		Map params = new HashMap();
		params.put("gameTask_id", gameTask.getId());
		params.put("user_id", user.getId());
		List<GameUserTask> gameUserTasks = this.gameUserTaskService.query("SELECT obj " + "FROM GameUserTask obj "
				+ "WHERE obj.user.id=:user_id " + "AND obj.gameTask.id=:gameTask_id", params, -1, -1);
		// 邮箱任务每个用户只能执行一次
		if (gameUserTasks.size() == 0) {
			// 查询任务状态
			if (user.getEmail() == null || user.getEmail().equals("")) {
				Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
				Matcher matcher = pattern.matcher(CommUtil.null2String(email));
				if (matcher.matches()) {
					user.setEmail(email);
					this.userService.update(user);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 文章点赞
	 * 
	 * @param user
	 * @param id
	 * @return
	 */
	public boolean articleTask(User user, GameTask gameTask, Long id) {
		Map params = new HashMap();
		params.put("gameTask_id", gameTask.getId());
		params.put("user_id", user.getId());
		params.put("addTime", CommUtil.dayStart());
		List<GameUserTask> gameUserTasks = this.gameUserTaskService.query("SELECT obj " + "FROM GameUserTask obj "
				+ "WHERE obj.user.id=:user_id " + "AND obj.gameTask.id=:gameTask_id " + "AND obj.addTime>=:addTime",
				params, -1, -1);
		if (gameUserTasks.size() <= 0) {
			Article article = this.articleService.getObjById(CommUtil.null2Long(id));
			if (article != null) {
				// 查询用户是否已对该文章点赞
				params.clear();
				params.put("user_id", user.getId());
				params.put("article_id", id);
				params.put("type", 0);
				List<ArticleInfo> articleInfos = this.articleInfoService.query("SELECT obj " + "FROM ArticleInfo obj "
						+ "WHERE obj.user.id=:user_id " + "AND obj.article.id=:article_id "
								+ "AND obj.type=:type", params, -1, -1);
				if (articleInfos.size() <= 0) {
					article.setLove(article.getLove() + 1);
					ArticleInfo articleInfo = new ArticleInfo();
					articleInfo.setArticle(article);
					articleInfo.setAddTime(new Date());
					articleInfo.setArticle_title(article.getTitle());
					articleInfo.setType(0);
					articleInfo.setUser(user);
					this.articleInfoService.save(articleInfo);
					this.articleService.update(article);
					return true;
				}
			}
		}
		return false;
	}

	// 检测genre=-1：无门槛任务
	public boolean detectionNewTask(GameTask gameTask, User user) {
		// 查询用户是否多次领取
		Map params = new HashMap();
		params.put("gameTask_id", gameTask.getId());
		params.put("user_id", user.getId());
		List<GameUserTask> gameUserTaskList = this.gameUserTaskService
				.query("SELECT new GameUserTask(obj.id, obj.status, obj.gameTask) " + "FROM GameUserTask obj "
						+ "WHERE obj.gameTask.id=:gameTask_id " + "AND obj.user.id=:user_id ", params, -1, -1);
		if (gameUserTaskList.size() > 0) {
			return false;
		}
		return true;
	}

	public boolean detectionEmailTask(GameTask gameTask, User user) {

		// 查询用户是否多次领取
		Map params = new HashMap();
		params.put("gameTask_id", gameTask.getId());
		params.put("user_id", user.getId());
		List<GameUserTask> gameUserTaskList = this.gameUserTaskService
				.query("SELECT new GameUserTask(obj.id, obj.status, obj.gameTask) " + "FROM GameUserTask obj "
						+ "WHERE obj.gameTask.id=:gameTask_id " + "AND obj.user.id=:user_id ", params, -1, -1);
		if (gameUserTaskList.size() > 1) {
			return false;
		}
		return true;
	}
	
	public String registerTask(User user){
		String code = "";
		if (user.getCode() == null || user.getCode().equals("")) {
			code = CommUtil.randomLowercase(4);
		} else {
			code = user.getCode();
		}
		user.setCode(code);
		this.userService.update(user);
		return code;
	}

	/**
	 * 邀请新人人物与
	 * @param gameTask
	 * @param user
	 * @return
	 */
	public boolean detectionRegisterTask(GameTask gameTask, User user) {
		Map params = new HashMap();
		params.put("gameTask_id", gameTask.getId());
		params.put("user_id", user.getId());
		List<GameUserTask> gameUserTaskList = this.gameUserTaskService
				.query("SELECT new GameUserTask(obj.id, obj.status, obj.gameTask) " + "FROM GameUserTask obj "
						+ "WHERE obj.gameTask.id=:gameTask_id " + "AND obj.user.id=:user_id "
						+ "AND obj.status=:status ", params, -1, -1);
		if (gameUserTaskList.size() > 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * 创建用户任务记录
	 */
	public GameUserTask createTask(GameTask gameTask, User user) {
		if (gameTask != null && user != null) {
			GameUserTask gameUserTask = new GameUserTask();
			gameUserTask.setAddTime(new Date());
			gameUserTask.setStatus(1);
			gameUserTask.setUser(user);
			gameUserTask.setGameTask(gameTask);
			gameUserTask.setStatus(1);
			gameUserTask.setFrequency(gameUserTask.getFrequency() + 1);
			this.gameUserTaskService.save(gameUserTask);
			return gameUserTask;
		}
		return null;
	}


}
