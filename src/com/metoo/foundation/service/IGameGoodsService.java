package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.GameGoods;

public interface IGameGoodsService {

	
	GameGoods getObjById(Long id);
	
	boolean save(GameGoods instance);
	
	boolean update(GameGoods instance);
	
	List<GameGoods> query(String query, Map params, int begin, int max);
	
	boolean delete(Long id);
}
