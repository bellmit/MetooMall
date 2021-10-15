package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.GameTask;


public interface IGameTaskService {

	public GameTask getObjById(Long id);
	
	public boolean save(GameTask instance);
	
	public boolean update(GameTask instance);
	
	public boolean delete(Long id);
	
	public List<GameTask> query(String query, Map params, int begin, int max);
}
