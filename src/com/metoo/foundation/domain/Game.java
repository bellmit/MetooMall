package com.metoo.foundation.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "game")
public class Game extends IdEntity{

	@Column(columnDefinition = "int default 0")
	private int status;// 游戏状态 0：开启 1：关闭
	
	private int watering_nmber;// 每次浇水 水滴数量
	
	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Accessory accessory;// 游戏界面主图 未种树时的图片
	
	@Column(columnDefinition = "int default 0")
	private int type;// 游戏类型0：种树
	
	@OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<GameTask> gameTasks = new ArrayList<GameTask>();

	private int shar_water;// 替好友浇水，水滴数
	
	private int upper_limit;// 
	
	private int frequency_limit;// 最大次数限制
	
	private int gather;// 收取好友水滴数量 百分比
	
	@OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<GameAward> gameAward;// 游戏奖励
	
	@OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Tree> trees;// 树集合
	
	private String explains;// 游戏说明
	
	@Column(columnDefinition = "int default 0")
	private int switchs;// 活动开启关闭 0：关闭 1：开启
	
	@Column(columnDefinition = "int default 0")
	private int num;// 视力游戏可免费娱乐次数
	
	public int getWatering_nmber() {
		return watering_nmber;
	}

	public void setWatering_nmber(int watering_nmber) {
		this.watering_nmber = watering_nmber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Accessory getAccessory() {
		return accessory;
	}

	public void setAccessory(Accessory accessory) {
		this.accessory = accessory;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<GameTask> getGameTasks() {
		return gameTasks;
	}

	public void setGameTasks(List<GameTask> gameTasks) {
		this.gameTasks = gameTasks;
	}

	public int getShar_water() {
		return shar_water;
	}

	public void setShar_water(int shar_water) {
		this.shar_water = shar_water;
	}

	public int getUpper_limit() {
		return upper_limit;
	}

	public void setUpper_limit(int upper_limit) {
		this.upper_limit = upper_limit;
	}

	public int getGather() {
		return gather;
	}

	public void setGather(int gather) {
		this.gather = gather;
	}

	public int getFrequency_limit() {
		return frequency_limit;
	}

	public void setFrequency_limit(int frequency_limit) {
		this.frequency_limit = frequency_limit;
	}

	public String getExplains() {
		return explains;
	}

	public void setExplains(String explains) {
		this.explains = explains;
	}

	public List<GameAward> getGameAward() {
		return gameAward;
	}

	public void setGameAward(List<GameAward> gameAward) {
		this.gameAward = gameAward;
	}

	public List<Tree> getTrees() {
		return trees;
	}

	public void setTrees(List<Tree> trees) {
		this.trees = trees;
	}

	public int getSwitchs() {
		return switchs;
	}

	public void setSwitchs(int switchs) {
		this.switchs = switchs;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
