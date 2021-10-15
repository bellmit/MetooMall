package com.metoo.foundation.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//1					3	1			2021-10-14 11:14:26	0		0		5
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

/**
 * <p>
 * 	Title:	LuckyDraw.java
 * </p>
 * 
 * <p>
 * 	Description: 抽奖管理类；设置抽奖信息及规则等
 * </p>
 * 
 * @author Administrator
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "lucky_draw")
public class LuckyDraw extends IdEntity{

	private String name;// 游戏名称 例如：天天抽奖
	private String message;// 游戏描述 例如：抽取水滴、商品碎片、抵用券等奖品;用于种树娱乐过多时，在列表中添加相应描述
	private String en_rule;// 英文规则描述
	private String sa_rule;// 阿语描述
	@Column(columnDefinition = "int default 0")
	private int num;// 抽奖次数 每日免费赠送三次 使用"LuckyDrawLog"对象记录用户抽奖记录
	@Column(columnDefinition = "int default 0")
	private int water;// 抽奖一次扣除水滴数量
	@Column(columnDefinition = "int default 0")
	private int switchs;// 活动开启关闭 0：关闭 1：开启
	@Column(columnDefinition = "int default 0")
	private int register;// 注册奖励：抽奖次数
	@Column(columnDefinition = "int default 0")
	private int order;// 	下单奖励：抽奖次数
	@OneToMany(mappedBy = "lucky_draw", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<LuckyDrawAward> lucky_draw_award = new ArrayList<LuckyDrawAward>();// 奖品管理类
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEn_rule() {
		return en_rule;
	}
	public void setEn_rule(String en_rule) {
		this.en_rule = en_rule;
	}
	public String getSa_rule() {
		return sa_rule;
	}
	public void setSa_rule(String sa_rule) {
		this.sa_rule = sa_rule;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getSwitchs() {
		return switchs;
	}
	public void setSwitchs(int switchs) {
		this.switchs = switchs;
	}
	public int getRegister() {
		return register;
	}
	public void setRegister(int register) {
		this.register = register;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getWater() {
		return water;
	}
	public void setWater(int water) {
		this.water = water;
	}
	public List<LuckyDrawAward> getLucky_draw_award() {
		return lucky_draw_award;
	}
	public void setLucky_draw_award(List<LuckyDrawAward> lucky_draw_award) {
		this.lucky_draw_award = lucky_draw_award;
	}
	
	public LuckyDraw() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LuckyDraw(Long id, Date addTime) {
		super(id, addTime);
		// TODO Auto-generated constructor stub
	}
	public LuckyDraw(Long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public LuckyDraw(Long id, String name, String message, int num, int water, List<LuckyDrawAward> lucky_draw_award) {
		super(id);
		this.name = name;
		this.message = message;
		this.num = num;
		this.water = water;
		this.lucky_draw_award = lucky_draw_award;
	}
	
}
