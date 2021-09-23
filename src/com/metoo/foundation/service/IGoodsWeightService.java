package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.core.query.support.IPageList;
import com.metoo.core.query.support.IQueryObject;
import com.metoo.foundation.domain.GoodsType;
import com.metoo.foundation.domain.GoodsWeight;

public interface IGoodsWeightService {

	GoodsWeight getObjById(Long id);
	
	boolean save(GoodsWeight instance);
	
	boolean update(GoodsWeight instance);
	
	boolean delete(Long id);
	
	IPageList list(IQueryObject properties);
	
	List<GoodsWeight> query(String query, Map params, int begin, int max);
}
