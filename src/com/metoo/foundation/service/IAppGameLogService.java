package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.GameLog;

public interface IAppGameLogService {

	GameLog getObjById(Long id);
	
	boolean save(GameLog instance);
	
	boolean update(GameLog instance);
	
	boolean delete(Long id);
	
	List<GameLog> list(String query, Map params, int begin, int max);
}
