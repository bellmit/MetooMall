package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.LuckyDrawLog;

public interface ILuckyDrawLogService {

	/**
	 * 根据一个ID得到LuckyDrawLog
	 * @param id
	 * @return
	 */
	LuckyDrawLog getObjById(Long id);
	
	
	/**
	 * 保存一个LuckyDrawLog，如果成功返回true, 否则返回false
	 * @param instance
	 * @return
	 */
	boolean save(LuckyDrawLog instance);
	
	/**
	 * 更新一个LuckyDrawLog
	 * @param instance
	 * @return
	 */
	boolean update(LuckyDrawLog instance);
	
	List<LuckyDrawLog> query(String query, Map params, int begin, int max);
	
}
