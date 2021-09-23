package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.core.query.GenericPageList;
import com.metoo.core.query.PageObject;
import com.metoo.core.query.support.IPageList;
import com.metoo.core.query.support.IQueryObject;
import com.metoo.foundation.domain.GoodsType;
import com.metoo.foundation.domain.GoodsWeight;
import com.metoo.foundation.service.IGoodsWeightService;

@Service
@Transactional
public class GoodsWeightServiceImpl implements IGoodsWeightService{
	
	@Resource(name = "goodsWeightDAO")
	private IGenericDAO<GoodsWeight> goodsWeightDao;

	@Override
	public GoodsWeight getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.goodsWeightDao.get(id);
	}

	@Override
	public boolean save(GoodsWeight instance) {
		// TODO Auto-generated method stub
		try {
			this.goodsWeightDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(GoodsWeight instance) {
		// TODO Auto-generated method stub
		try {
			this.goodsWeightDao.update(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		try {
			this.goodsWeightDao.remove(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
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
		GenericPageList pList = new GenericPageList(GoodsWeight.class,construct, query,
				params, this.goodsWeightDao);
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

	@Override
	public List<GoodsWeight> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.goodsWeightDao.query(query, params, begin, max);
	}

}
