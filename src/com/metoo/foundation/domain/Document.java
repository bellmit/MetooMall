package com.metoo.foundation.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

/**
 * 
 * <p>
 * Title: Document.java
 * </p>
 * 
 * <p>
 * Description:
 * 系统文章类，管理系统文章，包括注册协议、商铺协议等等,文章通过mark进行访问，使用urlrewrite静态化处理，将mark作为参数传递
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
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
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "document")
public class Document extends IdEntity {
	private String mark;// 文章标识
	private String title;// 文章标题
	private String title_sa;// 文章标题
	@Lob
	@Column(columnDefinition = "LongText")
	private String content;// 文章内容
	@Lob
	@Column(columnDefinition = "LongText")
	private String content_sa;// 文章内容

	public Document() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Document(Long id, Date addTime) {
		super(id, addTime);
		// TODO Auto-generated constructor stub
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Document(Long id, String title, String content) {
		super(id);
		this.title = title;
		this.content = content;
	}

	public Document(Long id, Date addTime, String title, String title_sa, String content_sa) {
		super(id, addTime);
		this.title = title_sa;
		if(title_sa == null || title_sa.equals("")){
			this.title = title;
		}
		this.content = content_sa;
	}

	public Document(String title, String mark) {
		super();
		this.mark = mark;
		this.title = title;
	}
	
	public Document(String title, String title_sa, String mark) {
		super();
		this.mark = mark;
		this.title = title_sa;
		if(title_sa == null || title_sa.equals("")){
			this.title = title;
		}
		this.mark = mark;
	}
	
}
