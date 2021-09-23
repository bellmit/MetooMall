package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.PlantingTrees;

public interface IPlantingtreesService {

	/**
	 * 保存一个PlantingTrees 对象
	 * @param instance
	 * @return
	 */
	boolean save(PlantingTrees instance);
	
	boolean update(PlantingTrees instance);
	
	List<PlantingTrees> query(String query, Map params, int begin, int max);
	
	boolean delete(Long id);
}
