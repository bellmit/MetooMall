package com.metoo.foundation.domain;

import java.io.File;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ArticleDto {

	private Long id;
	
	private Date addTime;
	
	private int deleteStatus;
	
	private String title;// 文章标题
	
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
	
	private Long article_id; //[用与wap端展示商品,商品关联的分类id，名字有歧义]
	
	private String accessory;
	
	@JsonIgnore
	private Accessory article_acc;// 广告图片
	
	@Column(columnDefinition = "int default 0")
	private int click;// 浏览次数
	// 浏览次数
	@Column(columnDefinition = "int default 0")
	private int like;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getArticle_id() {
		return article_id;
	}

	public void setArticle_id(Long article_id) {
		this.article_id = article_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public String getAccessory() {
		String accessory = null;
		if(article_acc == null){
			accessory = accessory;
		}else{
			accessory = article_acc.getPath() + File.separator + article_acc.getName();
		}
		return accessory;
	}

	public void setAccessory(String accessory) {
		this.accessory = accessory;
	}

	public Accessory getArticle_acc() {
		return article_acc;
	}

	public void setArticle_acc(Accessory article_acc) {
		this.article_acc = article_acc;
	}

	public int getClick() {
		return click;
	}

	public void setClick(int click) {
		this.click = click;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	} 

	public ArticleDto() {
	}

	public ArticleDto(Long id, Date addTime, int deleteStatus, String title, String type, ArticleClass articleClass,
			String url, int sequence, boolean display, String mark, String content, Long article_id, String accessory,
			Accessory article_acc, int click, int like) {
		this.id = id;
		this.addTime = addTime;
		this.deleteStatus = deleteStatus;
		this.title = title;
		this.type = type;
		this.articleClass = articleClass;
		this.url = url;
		this.sequence = sequence;
		this.display = display;
		this.mark = mark;
		this.content = content;
		this.article_id = article_id;
		this.accessory = accessory;
		this.click = click;
		this.like = like;
	}

	
}
