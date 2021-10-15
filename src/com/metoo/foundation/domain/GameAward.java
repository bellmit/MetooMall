package com.metoo.foundation.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import com.metoo.core.domain.IdEntity;

import com.metoo.core.constant.Globals;

/**
 * <p>
 * 	Title: GameAward.java
 * </p>
 * 
 * <p>
 * 	Description: 种树奖品类，设置奖品及随机奖品概率
 * </p>
 * 
 * @author 46075
 *
 */
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "game_award")
public class GameAward extends IdEntity{

	private String name;// 奖励名称
	
	@Column(columnDefinition = "LongText")
	private String water;// 水滴数 例如：{"50","10","100":"20"}根据订单金额选择奖励水滴数
		// 随机奖励 [{number:5, probably:0.5}] 签到：[{"number":10},{"number":10}{"number":10}]
	private int number;// 奖品数量
	
//	@Column(columnDefinition = "LongText")
	@ManyToOne(fetch = FetchType.LAZY)
	private Coupon coupon;// 优惠券奖品 {50%: couponId} == {50: 1}
	
	@Column(columnDefinition = "int default 0")
	private int type;
	// 奖品类型  
	// 0：种树升级奖品 
	// 1: 水滴奖励
	// 2：商品[id:{goods_detail:"goods_detail", accessory:accessory_name, probably:"0.0625","debris":"0","rate_progress":"0"} ] 
	// 3：商品抵用券: [{"voucher_id":5,"probably":50, "price":10},{"voucher_id":6,"probably":50, "price":5}] 
	// 4--------------------------:空奖 
	// 6：满级奖励
	// 7: 签到奖励 
	// 8：新人奖
	// 9：添加邮箱奖励
	// 10：文章点赞
	// 11：邀请新用户
	// 12: 浏览
	@ManyToOne(fetch = FetchType.LAZY)
	private SubTrees subTree;// 每一阶段树奖品
	
	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Goods goods;
	
/*	@Column(columnDefinition = "LongText")
	private String goods_info;// 商品信息 [{goods_id:"gods_id", goods_name:"goods_name", goods_details:"goods_details", goods_main_photo:"goods_main_photo", probably:"0.0625","debris":"0","rate_progress":"0","debris_totals":20}]
	
	*/
	@OneToMany(mappedBy = "gameAward", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<GoodsVoucher> goodsVouchers;

/*	@Column(columnDefinition = "LongText")
	private String voucher_info;// 抵用券 [{"voucher_id":5,"probably":50, "price":10, "accessory":"accessory","msg":"msg"},{"voucher_id":6,"probably":50, "price":5}]// 保存时控制比率从小到大
	*/
	@Column(precision = 12, scale = 1)
	private BigDecimal probably;// 不同奖品概率
	
	
	@ManyToMany
	@JoinTable(name = Globals.DEFAULT_TABLE_SUFFIX + "game_award_goods", joinColumns = @JoinColumn(name = "gameAward_id"), inverseJoinColumns = @JoinColumn(name = "gameGoods_id"))
	private List<GameGoods> gameGoodsList;// 商品碎片
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Game game;// 每一阶段树奖品
	
	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Tree tree;
	
	public String getWater() {
		return water;
	}

	public void setWater(String water) {
		this.water = water;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public SubTrees getSubTree() {
		return subTree;
	}

	public void setSubTree(SubTrees subTree) {
		this.subTree = subTree;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public List<GoodsVoucher> getGoodsVouchers() {
		return goodsVouchers;
	}

	public void setGoodsVouchers(List<GoodsVoucher> goodsVouchers) {
		this.goodsVouchers = goodsVouchers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getProbably() {
		return probably;
	}

	public void setProbably(BigDecimal probably) {
		this.probably = probably;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public List<GameGoods> getGameGoodsList() {
		return gameGoodsList;
	}

	public void setGameGoodsList(List<GameGoods> gameGoodsList) {
		this.gameGoodsList = gameGoodsList;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public GameAward() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GameAward(Long id, int type, BigDecimal probably) {
		super(id);
		this.type = type;
		this.probably = probably;
	}
	
	public GameAward(Long id, String water, int type, BigDecimal probably) {
		super(id);
		this.water = water;
		this.type = type;
		this.probably = probably;
	}

	public GameAward(Long id, String name, String water, int type) {
		super(id);
		this.name = name;
		this.water = water;
		this.coupon = coupon;
		this.type = type;
	}

	public GameAward(Long id, String name, String water, int type, BigDecimal probably) {
		super(id);
		this.name = name;
		this.water = water;
		this.coupon = coupon;
		this.type = type;
		this.probably = probably;
	}
	
	
	
	@Override
	public String toString() {
		return "GameAward [water=" + water + ", coupon=" + coupon + ", type=" + type + ", subTree=" + subTree + "]";
	}
	
	

}
