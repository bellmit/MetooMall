package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.core.query.support.IPageList;
import com.metoo.core.query.support.IQueryObject;
import com.metoo.foundation.domain.Tree;

public interface ITreeService {

	/**
	 * 
	 * @param id
	 * @return
	 */
	Tree getObjById(Long id);
	
	/**
	 * 保存一个Tree
	 * @param instance
	 * @return
	 */
	boolean save(Tree instance);
	
	/**
	 * 更新一个Tree
	 * @param instance
	 * @return
	 */
	boolean update(Tree instance);
	
	/**
	 * 通过一个查询对象得到Tree
	 * @param properties
	 * @return
	 */
	IPageList list(IQueryObject properties);
	
	
	/**
	 * 删除一个Tree
	 * @param id
	 * @return
	 */
	boolean delete(Long id);
	
	List<Tree> query(String query, Map params, int begin, int max);
}
