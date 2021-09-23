package com.metoo.foundation.domain;

import javax.persistence.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;


/**
 * <p>
 * 	Title: PlantingTrees.java
 * </p>
 * 
 * <p>
 * 	Description: 用户植树表，记录用户当前浇水树的状态；后其根据已完成的树做兑换时，删除正在进行的这棵树
 * </p>
 * @author 46075
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "planting_trees")
public class PlantingTrees extends IdEntity{
	
	private String name;// 植树名称
	
	private int status;// 0: 未完成 1：已完成
	
	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Accessory accessory;//当前树的图片
	
	@OneToOne(fetch = FetchType.LAZY)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Tree tree;// 树
	
	private int grade_watering;// 记录每一阶段的水滴数量：计算当前状态的进度条；浇水的同时同步更新当前字段和status字段
	
	@ManyToOne(fetch = FetchType.LAZY)
	private GameGoods gameGoods;// 用户选择的奖品
	
	@Column(columnDefinition = "int default 0")
	private int watering;// 当前树/花 的水滴数量
	
	private Long sub_trees;// 记录当前状态id 弃用
	
	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private SubTrees subTree;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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
	public int getWatering() {
		return watering;
	}
	public void setWatering(int watering) {
		this.watering = watering;
	}
	public int getGrade_watering() {
		return grade_watering;
	}
	public void setGrade_watering(int grade_watering) {
		this.grade_watering = grade_watering;
	}
	public Long getSub_trees() {
		return sub_trees;
	}
	public void setSub_trees(Long sub_trees) {
		this.sub_trees = sub_trees;
	}
	public GameGoods getGameGoods() {
		return gameGoods;
	}
	public void setGameGoods(GameGoods gameGoods) {
		this.gameGoods = gameGoods;
	}
	public SubTrees getSubTree() {
		return subTree;
	}
	public void setSubTree(SubTrees subTree) {
		this.subTree = subTree;
	}
}
