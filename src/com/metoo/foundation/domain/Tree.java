package com.metoo.foundation.domain;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "game_trees")
public class Tree extends IdEntity{

	private String name;// 树的名称
	
	private String type;// 树的种类
	
	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Accessory accessory;
	
	@Column(columnDefinition = "int default 0")
	private int status;// 树的状态  胚芽：0 破土而出:1  茁壮成长:2  绿树成荫:3   结出小果:4  果熟蒂落：5
	
	@OneToMany(mappedBy = "tree", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<PlantingTrees> plantingTrees = new ArrayList<PlantingTrees>();
	
	@ManyToMany(mappedBy="trees")
	private List<User> users = new ArrayList<User>();
	
	@OneToMany(mappedBy="tree")
	private List<SubTrees> subTrees = new ArrayList<SubTrees>();// 子状态
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Game game;// 游戏
	
	private String explains;// 每棵树的说明
	
	private int display;// 是否展示
	
	private int waters;// 水滴总数
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<PlantingTrees> getPlantingTrees() {
		return plantingTrees;
	}
	public void setPlantingTrees(List<PlantingTrees> plantingTrees) {
		this.plantingTrees = plantingTrees;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public List<SubTrees> getSubTrees() {
		return subTrees;
	}
	public void setSubTrees(List<SubTrees> subTrees) {
		this.subTrees = subTrees;
	}
	public Accessory getAccessory() {
		return accessory;
	}
	public void setAccessory(Accessory accessory) {
		this.accessory = accessory;
	}
	
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public String getExplains() {
		return explains;
	}
	public void setExplains(String explains) {
		this.explains = explains;
	}
	public int getDisplay() {
		return display;
	}
	public void setDisplay(int display) {
		this.display = display;
	}
	public int getWaters() {
		return waters;
	}
	public void setWaters(int waters) {
		this.waters = waters;
	}
	
	
}
