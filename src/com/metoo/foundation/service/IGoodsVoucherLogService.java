package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.core.query.support.IPageList;
import com.metoo.core.query.support.IQueryObject;
import com.metoo.foundation.domain.GoodsVoucherLog;

public interface IGoodsVoucherLogService {

	/**
	 * 根据日志ID获取GoodsVoucherLog对象
	 * @param id
	 * @return
	 */
	GoodsVoucherLog getObjById(Long id);
	
	/**
	 * 保存一个GameVoucherLog对象 如果成功返回true、失败返回false
	 * @param instance
	 * @return
	 */
	boolean save(GoodsVoucherLog instance);
	
	/**
	 * 更新一个GameVoucherLog对象 如果成功返回true、失败返回false
	 * @param instance
	 * @return
	 */
	boolean update(GoodsVoucherLog instance);
	
	List<GoodsVoucherLog> query(String query, Map params, int begin, int max);
	
	/**
	 * 通过一个查询对象得到GoodsVoucherLog
	 * 
	 * @param properties
	 * @return
	 */
	IPageList list(IQueryObject properties);
	
}
