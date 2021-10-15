package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.query.GenericPageList;
import com.metoo.core.query.PageObject;
import com.metoo.core.query.support.IPageList;
import com.metoo.core.query.support.IQueryObject;
import com.metoo.foundation.dao.GoodsVoucherLogDAO;
import com.metoo.foundation.domain.GoodsVoucher;
import com.metoo.foundation.domain.GoodsVoucherLog;
import com.metoo.foundation.domain.GroupClass;
import com.metoo.foundation.service.IGoodsVoucherLogService;

@Service
@Transactional
public class GoodsVoucherLogServiceImpl implements IGoodsVoucherLogService{
	
	@Resource(name = "goodsVoucherLogDAO")
	private GoodsVoucherLogDAO goodsVoucherLogDao;

	@Override
	public GoodsVoucherLog getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.goodsVoucherLogDao.get(id);
	}

	@Override
	public boolean save(GoodsVoucherLog instance) {
		// TODO Auto-generated method stub
		try {
			this.goodsVoucherLogDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public boolean update(GoodsVoucherLog instance) {
		// TODO Auto-generated method stub
		try {
			this.goodsVoucherLogDao.update(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<GoodsVoucherLog> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.goodsVoucherLogDao.query(query, params, begin, max);
	}

	@Override
	public IPageList list(IQueryObject properties) {
		// TODO Auto-generated method stub
		if (properties == null) {
			return null;
		}
		String query = properties.getQuery();
		String construct = properties.getConstruct();
		Map params = properties.getParameters();
		GenericPageList pList = new GenericPageList(GoodsVoucherLog.class,construct, query,
				params, this.goodsVoucherLogDao);
		if (properties != null) {
			PageObject pageObj = properties.getPageObj();
			if (pageObj != null)
				pList.doList(pageObj.getCurrentPage() == null ? 0 : pageObj
						.getCurrentPage(), pageObj.getPageSize() == null ? 0
						: pageObj.getPageSize());
		} else
			pList.doList(0, -1);
		return pList;
	}

}
