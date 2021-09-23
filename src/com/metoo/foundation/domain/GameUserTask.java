package com.metoo.foundation.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;
/**
 * <p>
 * 	Title: UserGameTreeTask.java
 * </p>
 * 
 * <p>
 * 	Description: 用户任务列表；记录用户任务完成状态
 * </p>
 * @author 46075
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "game_user_task")
public class GameUserTask extends IdEntity{

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	private int status;// 任务状态 0：未完成  1：已完成未领取 2：已领取
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	private GameTask gameTask;;// 游戏任务id
	
	private String task_id;// 任务id 
	
	private Long game_id;// 用户任务对应的游戏id

	private int frequency;// 记录任务完成次数
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	@JsonIgnore
	public GameTask getGameTask() {
		return gameTask;
	}

	public void setGameTask(GameTask gameTask) {
		this.gameTask = gameTask;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	
	public GameUserTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getGame_id() {
		return game_id;
	}

	public void setGame_id(Long game_id) {
		this.game_id = game_id;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public GameUserTask(Long id, Date addTime) {
		super(id, addTime);
		// TODO Auto-generated constructor stub
	}

	public GameUserTask(Long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public GameUserTask(Long id, int status) {
		super(id);
		this.status = status;
	}

	public GameUserTask(Long id, int status, String task_id) {
		super(id);
		this.status = status;
		this.task_id = task_id;
	}

	public GameUserTask(Long id, int status, GameTask gameTask) {
		super(id);
		this.status = status;
		this.gameTask = gameTask;
	}
	public GameUserTask(Long id, int status, int frequency, GameTask gameTask) {
		super(id);
		this.status = status;
		this.frequency = frequency;
		this.gameTask = gameTask;
	}
	
	public GameUserTask(Long id, Date addTime, int status, GameTask gameTask) {
		super(id, addTime);
		this.status = status;
		this.gameTask = gameTask;
	}

}
