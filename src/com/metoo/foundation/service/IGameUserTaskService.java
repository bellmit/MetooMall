package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.GameUserTask;

public interface IGameUserTaskService {

	GameUserTask getObjById(Long id);
	
	boolean save(GameUserTask instance);
	
	boolean update(GameUserTask instance);
	
	boolean delete(Long id);
	
	List<GameUserTask> query(String query, Map params, int begin, int max);
}
