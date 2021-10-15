package com.metoo.foundation.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

/**
 * <p>
 * 	Title: GameTreeTask.java
 * </p>
 * 
 * <p>
 * 	Description: 树-游戏任务控制器；完成任务获取相应水滴数量；
 * </p>
 * 
 * @author 46075
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "game_task")
public class GameTask extends IdEntity{
	
	private String title;// 任务标题
	
	private String content;// 任务内容
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Game game;// 任务对应游戏
	
	@Column(columnDefinition = "int default 0")
	private int display;// 是否显示
	
	private int status;// 任务状态 0：关闭 1：开启
	
	private String url;// 预留前端页面
	
	private String message;// 奖励提示
	
	private int frequency;// 任务完成次数0：无门槛任务 注册奖、注册邮箱 1：每日任务1/1 3：每日三次3/1 -1:可持续完成
	
	private int type;// 任务类型 0：给好友浇水 1：收取好友水滴  2: 浏览商品 3: 用户下单 4: 新用户注册 5:邀请新用户注册 6: 添加邮箱 7: 文章点赞 8：商品评论 9：指定页面商品下单、水滴翻倍
	
	private int genre;// -1：无门槛任务 0: 只需完成一次的任务 1：每日任务 
	
	private int time;// 浏览时长
	
	@Column(columnDefinition = "int default 0")
	private int task_status;
	
	@Column(columnDefinition = "int default 0")
	private int task_frequency;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private GameAward gameAward;
	
	@OneToMany(mappedBy = "gameTask", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<GameUserTask> gameUserTaskList;
	
	private int sequence;// 排序

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getDisplay() {
		return display;
	}

	public void setDisplay(int display) {
		this.display = display;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public GameAward getGameAward() {
		return gameAward;
	}

	public void setGameAward(GameAward gameAward) {
		this.gameAward = gameAward;
	}

	public int getTask_status() {
		return task_status;
	}

	public void setTask_status(int task_status) {
		this.task_status = task_status;
	}

	public List<GameUserTask> getGameUserTaskList() {
		return gameUserTaskList;
	}

	public void setGameUserTaskList(List<GameUserTask> gameUserTaskList) {
		this.gameUserTaskList = gameUserTaskList;
	}

	public int getGenre() {
		return genre;
	}

	public void setGenre(int genre) {
		this.genre = genre;
	}

	public int getTask_frequency() {
		return task_frequency;
	}

	public void setTask_frequency(int task_frequency) {
		this.task_frequency = task_frequency;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public GameTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GameTask(Long id, Date addTime) {
		super(id, addTime);
		// TODO Auto-generated constructor stub
	}

	public GameTask(Long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public GameTask(Long id, String title, String content, String url, String message, int frequency, int type,
			int time, int task_status) {
		super(id);
		this.title = title;
		this.content = content;
		this.url = url;
		this.message = message;
		this.frequency = frequency;
		this.type = type;
		this.time = time;
		this.task_status = task_status;
	}
	

	public GameTask(Long id, String title, String content, String url, String message, int frequency, int type,
			int time, int task_status, int status) {
		super(id);
		this.title = title;
		this.content = content;
		this.url = url;
		this.message = message;
		this.frequency = frequency;
		this.type = type;
		this.time = time;
		this.task_status = task_status;
		this.status = status;
	}
	
}
