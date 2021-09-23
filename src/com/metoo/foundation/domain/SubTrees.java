package com.metoo.foundation.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "sub_tree")
public class SubTrees extends IdEntity{

	@Column(columnDefinition = "int default 0")
	
	private int watering;// 所需水滴数量
	
	private int award;// 奖励水滴数量
	
	@Column(columnDefinition = "int default 0")
	private int status;// 树的状态 胚芽：0  嫩芽吐叶：1  生机勃勃：2 春树暮云：3 枝繁叶茂：4 苍翠欲滴：5 绿树成荫：6 桃红柳绿：7 含苞欲放：8 含苞吐萼：9 纷红骇緑：10 争奇斗艳：11 果熟蒂落：12
	
	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Accessory accessory;// 当前状态树图
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Tree tree;
	
	@OneToMany(mappedBy = "subTree")
	private List<GameAward> game_awards = new ArrayList<GameAward>();// 随机奖品
	
	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private GameAward gameAward;// 升级奖品
	
	public int getWatering() {
		return watering;
	}
	public void setWatering(int watering) {
		this.watering = watering;
	}
	public int getAward() {
		return award;
	}
	public void setAward(int award) {
		this.award = award;
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

	public Tree getTree() {
		return tree;
	}
	public void setTree(Tree tree) {
		this.tree = tree;
	}
	public GameAward getGameAward() {
		return gameAward;
	}
	public void setGameAward(GameAward gameAward) {
		this.gameAward = gameAward;
	}
	public List<GameAward> getGame_awards() {
		return game_awards;
	}
	public void setGame_awards(List<GameAward> game_awards) {
		this.game_awards = game_awards;
	}
	
}
