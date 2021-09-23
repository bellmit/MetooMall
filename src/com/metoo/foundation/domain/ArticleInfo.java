package com.metoo.foundation.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;


@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "article_info")
public class ArticleInfo extends IdEntity{

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;// 记录用户点赞信息
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Article article;
	
	private String article_title;// 文章标题
	
	private int type;// 1：浏览 0：点赞

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public String getArticle_title() {
		return article_title;
	}

	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	
}
