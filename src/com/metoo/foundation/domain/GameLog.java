package com.metoo.foundation.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

/**
 * <p>
 * 	Title:GameLog.java
 * </p>
 * 
 * <p>
 * 	Description: 游戏日志
 * </p>
 * @author 46075
 *
 */

@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "game_log")
public class GameLog extends IdEntity{

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;// 所属用户
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Game game;// 所属游戏
	
	private int type;// 日志类型 5:邀请新人
	
	@Column(columnDefinition = "int default 0")
	private int log_read;// 已读/未读 0: 未读 1：已读
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User friend;// 记录游戏好友
	
	
	private String message;// 通知内容

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLog_read() {
		return log_read;
	}

	public void setLog_read(int log_read) {
		this.log_read = log_read;
	}

	public User getFriend() {
		return friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
