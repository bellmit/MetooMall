package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.GameAwardInfo;

public interface IGameAwardInfoService {

	GameAwardInfo getObjById(Long id);
	
	boolean save(GameAwardInfo instance);
	
	boolean update(GameAwardInfo instance);
	
	boolean delete(Long id);
	
	List<GameAwardInfo> query(String query, Map params, int begin, int max);
}
