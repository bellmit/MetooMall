package com.metoo.foundation.domain.query;

import org.springframework.web.servlet.ModelAndView;

import com.metoo.core.query.QueryObject;

public class FriendQueryObject extends QueryObject {

	/**
	 * 
	 */
	public FriendQueryObject() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param currentPage
	 * @param mv
	 * @param orderBy
	 * @param orderType
	 */
	public FriendQueryObject(String currentPage, ModelAndView mv, String orderBy, String orderType) {
		super(currentPage, mv, orderBy, orderType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param construct
	 * @param currentPage
	 * @param mv
	 * @param orderBy
	 * @param orderType
	 */
	public FriendQueryObject(String construct, String currentPage, ModelAndView mv, String orderBy, String orderType) {
		super(construct, currentPage, mv, orderBy, orderType);
		// TODO Auto-generated constructor stub
	}

	
}
