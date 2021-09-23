package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.GameTreeLog;

public interface IGameTreeLogService {

	public GameTreeLog getObjById(Long id);
	
	public boolean save(GameTreeLog instance);
	
	public boolean update(GameTreeLog instance);
	
	public boolean delete(Long id);
	
	public List<GameTreeLog> query(String query, Map params, int begin, int max);
	
}
