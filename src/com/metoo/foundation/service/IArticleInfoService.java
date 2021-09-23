package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.ArticleInfo;

public interface IArticleInfoService {
	
	ArticleInfo getObjById(Long id);
	
	boolean save(ArticleInfo instance);
	
	boolean update(ArticleInfo instance);
	
	boolean delete(Long id);

	List<ArticleInfo> query(String query, Map params, int begin, int max);
}
