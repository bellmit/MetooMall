package com.metoo.foundation.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

@Table(name=Globals.DEFAULT_TABLE_SUFFIX + "search_keyword")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SearchKeyword extends IdEntity{
	
	private String word; //搜索单词
	private String arabic_word;// 阿语--翻译
	@Column(columnDefinition = "int default 0")
	private int sort;// 排序
	private Long goodsclass_id;// 类目id
	private Date updated_at;// 更新时间
	private String language;// 语言类型： ar阿语
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getArabic_word() {
		return arabic_word;
	}
	public void setArabic_word(String arabic_word) {
		this.arabic_word = arabic_word;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public Long getGoodsclass_id() {
		return goodsclass_id;
	}
	public void setGoodsclass_id(Long goodsclass_id) {
		this.goodsclass_id = goodsclass_id;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
}
