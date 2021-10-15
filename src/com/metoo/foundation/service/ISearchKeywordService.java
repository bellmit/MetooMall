package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.SearchKeyword;

/**
 * Php负责查询，这里不在提供查询方法
 * @author 46075
 *
 */
public interface ISearchKeywordService {
	
	/**
	 * 保存一个SearchKeyword对象
	 * @param instance
	 * @return
	 */
	boolean save(SearchKeyword instance);
	
	/**
	 * 更新一个SearchKeyword对象
	 * @param intance
	 * @return
	 */
	boolean update(SearchKeyword intance);
	
	List<SearchKeyword> query(String query, Map params, int begin, int max);
	
}
