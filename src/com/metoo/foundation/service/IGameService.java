package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.Game;

public interface IGameService {

	/**
	 * 根据id获取一个GameTree
	 * @param id
	 * @return
	 */
	Game getObjById(Long id);
	
	/**
	 * 保存一个GameTree
	 * @param instance
	 * @return
	 */
	boolean save(Game instance);
	
	/**
	 * 更新一个GameTree
	 * @param instance
	 * @return
	 */
	boolean update(Game instance);
	
	List<Game> query(String query, Map params, int begin, int max);
}
