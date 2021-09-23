package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.GoodsVoucher;
import com.metoo.foundation.domain.GoodsVoucherInfo;

public interface IGoodsVoucherInfoService {
	
	 GoodsVoucherInfo getObjById(Long id);
	
	 List<GoodsVoucherInfo> query(String query, Map params, int begin, int max);
	 
	 boolean save(GoodsVoucherInfo instance);
	 
	 boolean update(GoodsVoucherInfo instance);
}
