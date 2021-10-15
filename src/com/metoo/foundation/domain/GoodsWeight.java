package com.metoo.foundation.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

/**
 * <p>
 * 	Title: GoodsWeight.java
 * </p>
 * 
 * <p>
 * 	Description: 商品权重管理类，添加商品权重指标
 * </p>
 * @author 46075
 *
 */

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "goods_weight")
public class GoodsWeight extends IdEntity{
	
	//通用权重值
	private BigDecimal value;// 权重值
	private String dimensionality;// 权重指标：指标与商品属性相同，方便权重计算；例如goods_price:value 键值对
	private boolean display;//是否显示
	//预留认为设置权重值 主要目的搜索
	private String class_id;// 增加某一类目下权重值
	
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public String getDimensionality() {
		return dimensionality;
	}
	public void setDimensionality(String dimensionality) {
		this.dimensionality = dimensionality;
	}

	public boolean isDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	public String getClass_id() {
		return class_id;
	}
	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}
	
	

}
