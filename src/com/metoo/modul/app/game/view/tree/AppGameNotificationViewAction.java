package com.metoo.modul.app.game.view.tree;

import java.util.ArrayList;
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

import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.ResponseUtils;
import com.metoo.foundation.domain.Article;
import com.metoo.foundation.domain.ArticleInfo;
import com.metoo.foundation.domain.GameTreeLog;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IAppGameLogService;
import com.metoo.foundation.service.IArticleInfoService;
import com.metoo.foundation.service.IArticleService;
import com.metoo.foundation.service.IGameTreeLogService;
import com.metoo.module.app.buyer.domain.Result;

/**
 * <p>
 * Title: AppGameNotificationViewAction.java
 * </p>
 * 
 * <p>
 * Description: 游戏通知管理类
 * </p>
 * 
 * @author 46075
 *
 */
@RequestMapping("/app/v1/game/notification")
@Controller
public class AppGameNotificationViewAction {

	@Autowired
	private IGameTreeLogService gameTreeLogService;
	@Autowired
	private IArticleInfoService articleInfoService;
	@Autowired
	private IArticleService articleService;

	/**
	 * 小游戏-消息通知
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping("/list.json")
	@ResponseBody
	public Object list(String token) {
		User user = CommUtil.verifyToken(token);
		if (user != null) {
			Map data = new HashMap();
			// 查询所有系统信息、并查询未读信息条数、并设置为已读
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("user_id", user.getId());
			List<GameTreeLog> gameTreeLogs = this.gameTreeLogService
					.query("SELECT new GameTreeLog(id, addTime, user_id, user_name, type, status, friend_id, friend_name, log_read, water, message) FROM GameTreeLog obj "
							+ "WHERE obj.user_id=:user_id ORDER BY obj.addTime DESC", params, -1, -1);
			if (gameTreeLogs.size() > 0) {
				for (GameTreeLog gameTreeLog : gameTreeLogs) {
					int i = 0;
					if (gameTreeLog.getLog_read() == 0) {
						gameTreeLog.setLog_read(1);
						this.gameTreeLogService.update(gameTreeLog);
					}
					data.put("notification", gameTreeLogs);
				}
			}
			return ResponseUtils.ok(data);
		}
		return ResponseUtils.unlogin();
	}

	/**
	 * 未读消息
	 * 
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping("/unread.json")
	@ResponseBody
	public Object unread(HttpServletRequest request, HttpServletResponse response, String token) {
		Map map = new HashMap();
		User user = null;
		if ((user = CommUtil.verifyToken(token)) != null) {
			Map params = new HashMap();
			// 查询用户系统信息、查询未读条数
			params.clear();
			params.put("user_id", user.getId());
			params.put("log_read", 0);
			List<GameTreeLog> gameTreeLogs = this.gameTreeLogService
					.query("SELECT new GameTreeLog(id, addTime, user_id, user_name, type, status, friend_id, friend_name, log_read, water) FROM GameTreeLog obj "
							+ "WHERE obj.user_id=:user_id " + "AND obj.log_read=:log_read", params, -1, -1);
			map.put("notification", gameTreeLogs.size());
			
			params.clear();
			params.put("mark", "Discovery");
			List<Article> articles = this.articleService
					.query("SELECT new Article(obj.id, obj.addTime, obj.deleteStatus, obj.title, obj.type, obj.url, obj.content, obj.article_id, obj.article_acc, obj.click, obj.love, obj.accessory) "
							+ "FROM Article obj " + "WHERE obj.mark=:mark "
							+ "ORDER BY obj.sequence DESC, obj.addTime DESC", params, -1, -1);
			Set<Long> article_id = new HashSet<Long>();
			for(Article article : articles){
				article_id.add(article.getId());
			}
			List<ArticleInfo> articleInfos = new ArrayList<ArticleInfo>();
			if(user != null){
				params.clear();
				params.put("user_id", user.getId());
				params.put("article_id", article_id);
				params.put("type", 0);
				articleInfos = this.articleInfoService
						.query("SELECT obj FROM ArticleInfo obj WHERE obj.article.id in(:article_id) "
								+ "AND obj.user.id=:user_id "
								+ "AND obj.type=:type", params, -1, -1);
			}
			map.put("number", articles.size() - articleInfos.size());
			return ResponseUtils.ok(map);
		}
		return ResponseUtils.unlogin();
	}

}
