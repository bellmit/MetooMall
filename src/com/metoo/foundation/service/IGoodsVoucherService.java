package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.GoodsType;
import com.metoo.foundation.domain.GoodsVoucher;

public interface IGoodsVoucherService {
	
	GoodsVoucher getObjById(Long id);
	
	 List<GoodsVoucher> query(String query, Map params, int begin, int max);
	 
	 boolean save(GoodsVoucher instance);

}
