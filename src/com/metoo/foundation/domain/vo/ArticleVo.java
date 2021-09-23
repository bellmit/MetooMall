package com.metoo.foundation.domain.vo;

import java.io.File;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.metoo.foundation.domain.Accessory;
import com.metoo.foundation.domain.ArticleClass;

/**
 * <p>
 * 	Title: Article.java
 * </p>
 * 
 * <p>
 * 	Description: 文章视图对象；封装文章及关联对象；原因:Hibernate 双向关联导致java对象转json字符串时死循环
 * </p>
 * 
 * @author 46075
 *
 */
public class ArticleVo {

	private Long id;
	
	private Date addTime;
	
	private String title;// 文章标题
	
	private String type;// 文章类型，默认为user，商家公告为store，store类型只能商家才能查看
	
	private String url;// 文章链接，如果存在该值则直接跳转到url，不显示文章内容
	
	private String content;// 文章内容
	
	private Long article_id; //[用与wap端展示商品,商品关联的分类id，名字有歧义]
	
	private String accessory;
	
	private int click;// 浏览次数

	private int like;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getAccessory() {
		return accessory;
	}

	public void setAccessory(String accessory) {
		this.accessory = accessory;
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

}
