package com.metoo.foundation.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "game_treelog")
public class GameTreeLog extends IdEntity {

	private Long user_id;// 所属用户id

	private String user_name;// 所属用户名称

	private Long tree_id;// 树id

	@ManyToOne(fetch = FetchType.LAZY)
	private Game game;// 所属游戏
 
	private int type;// 日志的种类  2: 浏览商品  4, 新用户任务 5：添加好友 6：浇水 7：邀请好友  8：访问好友 9：偷取水滴 10：给好友浇水升级奖励日志（只在好友通知中展示） 11：订单
	// 12: 满级日志

	@Column(columnDefinition = "int default 0")
	private int status;
	// 添加好友日志记录操作状态 
	// 1：申请添加好友 -1：被申請添加好友
	// 2：申请成功 -2：通过申请
	// 3：被拒绝添加 -3：拒绝添加
	// 4：移除好友 -4：移除好友
	// 6：给好友浇水 -6: 好友给我浇水
	// 7：邀请** 注册成为好友 -7：被**邀请注册成功并成为好友
	// 8：我访问了我的好友 -8：我的好友访问了我
	// 9：偷取好友水滴 -9：好友偷取我的水滴
	// 10: 升级奖励 
	// 11： 下单奖励

	private String name;// 树的名称

	private Long friend_id;// 好友ID

	private String friend_name;// 好友用户名

	@ManyToOne(fetch = FetchType.LAZY)
	private GameTask gameTask;

	@Column(columnDefinition = "int default 0")
	private int log_read;// 已读/未读 0: 未读 1：已读

	private int water;// 水滴数 偷/送
	
	private String gameAward;// 阶段奖品信息
	
	private String message;// 通知内容

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public Long getTree_id() {
		return tree_id;
	}

	public void setTree_id(Long tree_id) {
		this.tree_id = tree_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getFriend_id() {
		return friend_id;
	}

	public void setFriend_id(Long friend_id) {
		this.friend_id = friend_id;
	}

	public GameTask getGameTask() {
		return gameTask;
	}

	public void setGameTask(GameTask gameTask) {
		this.gameTask = gameTask;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFriend_name() {
		return friend_name;
	}

	public void setFriend_name(String friend_name) {
		this.friend_name = friend_name;
	}

	public int getLog_read() {
		return log_read;
	}

	public void setLog_read(int log_read) {
		this.log_read = log_read;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getWater() {
		return water;
	}

	public void setWater(int water) {
		this.water = water;
	}

	public String getGameAward() {
		return gameAward;
	}

	public void setGameAward(String gameAward) {
		this.gameAward = gameAward;
	}

	/**
	 * 
	 */
	public GameTreeLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param addTime
	 */
	public GameTreeLog(Long id, Date addTime) {
		super(id, addTime);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 */
	public GameTreeLog(Long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param user_id
	 * @param user_name
	 * @param tree_id
	 * @param type
	 * @param status
	 * @param name
	 * @param friend_id
	 * @param friend_name
	 * @param log_read
	 * @param water
	 */
	public GameTreeLog(Long id, Date addTime, Long user_id, String user_name, Long tree_id, int type, int status,
			String name, Long friend_id, String friend_name, int log_read, int water) {
		super(id, addTime);
		this.user_id = user_id;
		this.user_name = user_name;
		this.tree_id = tree_id;
		this.type = type;
		this.status = status;
		this.name = name;
		this.friend_id = friend_id;
		this.friend_name = friend_name;
		this.log_read = log_read;
		this.water = water;
	}

	/**
	 * @param user_id
	 * @param user_name
	 * @param type
	 * @param status
	 * @param friend_id
	 * @param friend_name
	 * @param log_read
	 * @param water
	 */
	public GameTreeLog(Long id, Date addTime, Long user_id, String user_name, int type, int status, Long friend_id,
			String friend_name, int log_read, int water) {
		super(id, addTime);
		this.user_id = user_id;
		this.user_name = user_name;
		this.type = type;
		this.status = status;
		this.friend_id = friend_id;
		this.friend_name = friend_name;
		this.log_read = log_read;
		this.water = water;
	}
	
	public GameTreeLog(Long id, Date addTime, Long user_id, String user_name, int type, int status, Long friend_id,
			String friend_name, int log_read, int water, String message) {
		super(id, addTime);
		this.user_id = user_id;
		this.user_name = user_name;
		this.type = type;
		this.status = status;
		this.friend_id = friend_id;
		this.friend_name = friend_name;
		this.log_read = log_read;
		this.water = water;
		this.message = message;
	}


}
