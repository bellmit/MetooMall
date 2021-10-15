package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.SubTrees;

public interface ISubTreesService {

	/**
	 * 根据id获取一个SubTrees对象
	 * 
	 * @param id
	 * @return
	 */
	SubTrees getObjById(Long id);

	/**
	 * 保存一个SubTrees对象
	 * 
	 * @param instance
	 * @return
	 */
	boolean save(SubTrees instance);

	/**
	 * 更新一个SubTrees对象
	 * 
	 * @param instance
	 * @return
	 */
	boolean update(SubTrees instance);

	/**
	 * 删除一个SubTrees对象
	 * 
	 * @param id
	 * @return
	 */
	boolean delete(Long id);

	/**
	 * 
	 * @param query
	 *            查询sql
	 * @param params
	 *            查询参数
	 * @param begin
	 *            起始页
	 * @param max
	 *            每页最大数
	 * @return
	 */
	List<SubTrees> query(String query, Map params, int begin, int max);

}
