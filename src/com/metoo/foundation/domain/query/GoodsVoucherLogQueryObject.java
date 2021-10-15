package com.metoo.foundation.domain.query;

import org.springframework.web.servlet.ModelAndView;

import com.metoo.core.query.QueryObject;

public class GoodsVoucherLogQueryObject extends QueryObject  {

	/**
	 * 
	 */
	public GoodsVoucherLogQueryObject() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param currentPage
	 * @param mv
	 * @param orderBy
	 * @param orderType
	 */
	public GoodsVoucherLogQueryObject(String currentPage, ModelAndView mv, String orderBy, String orderType) {
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
	public GoodsVoucherLogQueryObject(String construct, String currentPage, ModelAndView mv, String orderBy,
			String orderType) {
		super(construct, currentPage, mv, orderBy, orderType);
		// TODO Auto-generated constructor stub
	}

}
