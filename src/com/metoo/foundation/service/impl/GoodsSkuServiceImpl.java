package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.domain.GoodsSku;
import com.metoo.foundation.service.IGoodsSkuService;

@Service
@Transactional
public class GoodsSkuServiceImpl implements IGoodsSkuService{
	
	@Resource(name = "GoodsSku")
	private IGenericDAO<GoodsSku> goodsSkuDao;
	
	
	@Override
	public GoodsSku getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.goodsSkuDao.get(id);
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		try {
			this.goodsSkuDao.remove(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean save(GoodsSku cgds) {
		// TODO Auto-generated method stub
		try {
			this.goodsSkuDao.save(cgds);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(GoodsSku goodsSku) {
		// TODO Auto-generated method stub
		try {
			this.goodsSkuDao.update(goodsSku);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public List<GoodsSku> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.goodsSkuDao.query(query, params, begin, max);
	}


}
