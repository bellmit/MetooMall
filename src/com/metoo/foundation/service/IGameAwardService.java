package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.GameAward;

public interface IGameAwardService {

	/**
	 * 根据ID 获取一个奖品对象
	 * @param id
	 * @return
	 */
	GameAward getObjById(Long id);
	
	boolean save(GameAward instance);
	
	boolean update(GameAward instance);
	
	boolean  delete(Long id);
	
	List<GameAward> query(String query, Map params, int begin, int max);
	
}
