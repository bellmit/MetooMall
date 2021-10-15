package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.GoodsSku;

public interface IGoodsSkuService {
	
	GoodsSku getObjById(Long id);
	
	boolean delete(Long id);
	
	public boolean save(GoodsSku cgds);
	
	boolean update(GoodsSku instance);
	
	List<GoodsSku> query(String query, Map params, int begin, int max);
}
