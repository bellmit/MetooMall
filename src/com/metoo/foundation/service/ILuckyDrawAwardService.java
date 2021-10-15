package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.LuckyDrawAward;

public interface ILuckyDrawAwardService {

	/**
	 * 根据一个ID获取LuckyDrawAward
	 * @param id
	 * @return
	 */
	LuckyDrawAward getObjById(Long id);
	
	/**
	 * 保存一个LuckyDrawAward对象，成功返回true，失败返回false
	 * @param instance
	 * @return
	 */
	boolean save(LuckyDrawAward instance);
	
	/**
	 * 保存一个LuckyDrawAward对象，成功返回true，失败返回false
	 * @param instance
	 * @return
	 */
	boolean update(LuckyDrawAward instance);
	
	
	List<LuckyDrawAward> query(String query, Map params, int begin, int max);
}
