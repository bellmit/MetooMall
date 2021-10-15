package com.metoo.foundation.domain;

import java.io.File;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

/**
 * 
 * <p>
 * Title: Article.java
 * </p>
 * 
 * <p>
 * Description:系统文章管理类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * 
 * <p>
 * Company: 沈阳网之商科技有限公司 www.koala.com
 * </p>
 * 
 * @author erikzhang
 * 
 * @date 2014-4-25
 * 
 * @version koala_b2b2c v2.0 2015版 
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "article")
public class Article extends IdEntity {
	private String title;// 文章标题
	private String title_sa;// 文章标题(阿语)
	@Column(columnDefinition = "varchar(255) default 'user' ")
	private String type;// 文章类型，默认为user，商家公告为store，store类型只能商家才能查看
	@ManyToOne(fetch = FetchType.LAZY)
	private ArticleClass articleClass;// 文章分类
	private String url;// 文章链接，如果存在该值则直接跳转到url，不显示文章内容
	private int sequence;// 文章序号，根据序号正序排序
	private boolean display;// 是否显示文章
	private String mark;// 文章标识，存在唯一性，通过标识可以查询对应的文章
	@Column(columnDefinition = "LongText")
	private String content;// 文章内容
	@Column(columnDefinition = "LongText")
	private String content_sa;// 文章内容(阿语)
	private Long article_id; //[用与wap端展示商品,商品关联的分类id，名字有歧义]
	private boolean flag;// 是否为新文章

	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Accessory article_acc;// 广告图片
	
	@ManyToOne(fetch = FetchType.LAZY)
	private GoodsFloor goodsFloor;
	
	@Column(columnDefinition = "int default 0")
	private int click;// 浏览次数

	// 浏览次数
	@Column(columnDefinition = "int default 0")
	private int love;
	
	private String accessory;
	
	
	public String getAccessory() {
		String accessory = null;
		if(article_acc == null){
			accessory = accessory;
		}else{
			accessory = article_acc.getPath() + "/" + article_acc.getName();
		}
		return accessory;
	}

	public void setAccessory(String accessory) {
		this.accessory = accessory;
	}

	public GoodsFloor getGoodsFloor() {
		return goodsFloor;
	}

	public void setGoodsFloor(GoodsFloor goodsFloor) {
		this.goodsFloor = goodsFloor;
	}

	@JsonIgnore
	public Accessory getArticle_acc() {
		return article_acc;
	}

	public void setArticle_acc(Accessory article_acc) {
		this.article_acc = article_acc;
	}

	public Long getArticle_id() {
		return article_id;
	}

	public void setArticle_id(Long article_id) {
		this.article_id = article_id;
	}

	public Article() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Article(Long id, Date addTime) {
		super(id, addTime);
		// TODO Auto-generated constructor stub
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArticleClass getArticleClass() {
		return articleClass;
	}

	public void setArticleClass(ArticleClass articleClass) {
		this.articleClass = articleClass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public int getClick() {
		return click;
	}

	public void setClick(int click) {
		this.click = click;
	}

	public int getLove() {
		return love;
	}

	public void setLove(int love) {
		this.love = love;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public Article(Long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public Article(Long id, String title, String type, String url, String content, Long article_id, Accessory article_acc,
			int click, int like) {
		super(id);
		this.title = title;
		this.type = type;
		this.url = url;
		this.content = content;
		this.article_id = article_id;
		this.article_acc = article_acc;
		this.click = click;
		this.love = love;
	}
	

	public Article(Long id, Date addTime, int deleteStatus, String title, String type, String url, String content, Long article_id, Accessory article_acc,
			int click, int love, String accessory) {
		super(id, addTime);
		this.title = title;
		this.type = type;
		this.url = url;
		this.content = content;
		this.article_id = article_id;
		this.article_acc = article_acc;
		this.click = click;
		this.love = love;
		this.accessory = accessory;
	}
	

	public Article(Long id, Date addTime, String title, String title_sa, String type, String url, String content_sa, Long article_id, Accessory article_acc,
			int click, int love, String accessory) {
		super(id, addTime);
		this.title = title_sa;
		if(title_sa == null || title_sa.equals("")){
			this.title = title;
		}
		this.type = type;
		this.url = url;
		this.content = content_sa;
		this.article_id = article_id;
		this.article_acc = article_acc;
		this.click = click;
		this.love = love;
		this.accessory = accessory;
	}

	public Article(Long id, String title, String type, String url, String content, Long article_id, Accessory article_acc,
			int click, String accessory) {
		super(id);
		this.title = title;
		this.type = type;
		this.url = url;
		this.content = content;
		this.article_id = article_id;
		this.article_acc = article_acc;
		this.click = click;
		this.accessory = accessory;
	}
	
	public Article(Long id, String title, String type, String url, String content, Long article_id,
			int click) {
		super(id);
		this.title = title;
		this.type = type;
		this.url = url;
		this.content = content;
		this.article_id = article_id;
		this.click = click;
	}

	public Article(Accessory article_acc) {
		this.article_acc = article_acc;
	}

	public Article(Long id, String title, int click, int love, Accessory article_acc, String accessory) {
		super(id);
		this.title = title;
		this.click = click;
		this.love = love;
		this.article_acc = article_acc;
		this.accessory = accessory;
	}

	public String getTitle_sa() {
		return title_sa;
	}

	public void setTitle_sa(String title_sa) {
		this.title_sa = title_sa;
	}

	public String getContent_sa() {
		return content_sa;
	}

	public void setContent_sa(String content_sa) {
		this.content_sa = content_sa;
	}


}

