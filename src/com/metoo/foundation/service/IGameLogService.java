package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.GameLog;

public interface IGameLogService {

	boolean save(GameLog instance);
	
	List<GameLog> query(String query, Map params, int begin, int max);
}
