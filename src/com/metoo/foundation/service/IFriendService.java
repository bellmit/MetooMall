package com.metoo.foundation.service;

import java.util.List;
import java.util.Map;

import com.metoo.foundation.domain.Friend;

public interface IFriendService {

	/**
	 * 根据id查询一个Friend对象
	 * @param id
	 * @return
	 */
	Friend getObjByID(Long id);
	
	/**
	 * 保存一个Friend对象
	 * @param instance
	 * @return
	 */
	boolean save(Friend instance);
	
	/**
	 * 更新一个Friend对象
	 * @param instance
	 * @return
	 */
	boolean update(Friend instance);
	
	 /**
	  *根据id删除Friend对象 
	  * @param id
	  * @return
	  */
	boolean delete(Long id);
	
	/**
	 * 根据jpql查询一组Friend对象
	 * @param sql
	 * @param params
	 * @param begin
	 * @param max
	 * @return
	 */
	List<Friend> query(String sql, Map params, int begin, int max);
	
}
