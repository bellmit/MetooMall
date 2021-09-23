package com.metoo.foundation.domain;

import java.math.BigDecimal;
import java.util.Date;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

/**
 * <p>
 * 	Title: GameGoods.java
 * </p>
 * 
 * <p>
 * 	Description: 游戏奖品
 * </p>
 * 
 * @author 46075
 *
 */
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "game_goods")
public class GameGoods extends IdEntity{

	private String goods_detail;// 商品描述
	
	@Column(columnDefinition = "LongText")
	private String accessory;// 图片路径
	
	private Long goods_id;// 商品ID--新的格式 驼峰 hibernate自动生成的为 "goods_id"
	
//	@Column(precision = 12, scale = 2)
//	private BigDecimal probably;
	private String probably;// 随机奖品概率
	
	@Column(precision = 12, scale = 2)
	private BigDecimal  sign_probably;// 签到概率
	
	private int debris_totals;// 碎片总数
	
	private int number;// 碎片数量
	
	private int sequence;// 排序
	
	@ManyToOne(fetch = FetchType.LAZY)
	private GameAward gameAward;
	
	@ManyToMany(mappedBy = "gameGoodsList")
	private List<GameAward> gameAwards;
	
	private int rate_progress;// 进度
	
	@OneToMany(mappedBy = "gameGoods", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<PlantingTrees> plantingTreesList;

	public String getGoods_detail() {
		return goods_detail;
	}

	public void setGoods_detail(String goods_detail) {
		this.goods_detail = goods_detail;
	}

	public String getProbably() {
		return probably;
	}

	public void setProbably(String probably) {
		this.probably = probably;
	}

	public int getDebris_totals() {
		return debris_totals;
	}

	public void setDebris_totals(int debris_totals) {
		this.debris_totals = debris_totals;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@JsonIgnore
	public GameAward getGameAward() {
		return gameAward;
	}

	public void setGameAward(GameAward gameAward) {
		this.gameAward = gameAward;
	}

	public int getRate_progress() {
		return rate_progress;
	}

	public void setRate_progress(int rate_progress) {
		this.rate_progress = rate_progress;
	}

	public String getAccessory() {
		return accessory;
	}

	public void setAccessory(String accessory) {
		this.accessory = accessory;
	}

	public Long getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Long goods_id) {
		this.goods_id = goods_id;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	@JsonIgnore
	public List<PlantingTrees> getPlantingTreesList() {
		return plantingTreesList;
	}

	public void setPlantingTreesList(List<PlantingTrees> plantingTreesList) {
		this.plantingTreesList = plantingTreesList;
	}

	public GameGoods() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GameGoods(Long id, Date addTime) {
		super(id, addTime);
		// TODO Auto-generated constructor stub
	}

	public GameGoods(Long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	
	
	/**
	 * @param accessory
	 * @param goods_id
	 */
	public GameGoods(Long id, String accessory, Long goods_id) {
		super(id);
		this.accessory = accessory;
		this.goods_id = goods_id;
	}
	
	public GameGoods(Long id, Long goods_id) {
		super(id);
		this.goods_id = goods_id;
	}


	public GameGoods(Long id, String goods_detail, String accessory, Long goods_id, String probably, int debris_totals,
			int number) {
		super(id);
		this.goods_detail = goods_detail;
		this.accessory = accessory;
		this.goods_id = goods_id;
		this.probably = probably;
		this.debris_totals = debris_totals;
		this.number = number;
	}

	@JsonIgnore
	public List<GameAward> getGameAwards() {
		return gameAwards;
	}

	public void setGameAwards(List<GameAward> gameAwards) {
		this.gameAwards = gameAwards;
	}
	
	
}
