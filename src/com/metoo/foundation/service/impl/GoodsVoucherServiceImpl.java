package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.Resources;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.dao.GoodsVoucherDAO;
import com.metoo.foundation.domain.GoodsVoucher;
import com.metoo.foundation.service.IGoodsVoucherService;

@Service
@Transactional
public class GoodsVoucherServiceImpl implements IGoodsVoucherService{

	@Resource(name = "goodsVoucherDAO")
	private IGenericDAO<GoodsVoucher> goodsVoucherDao;
	
	@Override
	public GoodsVoucher getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.goodsVoucherDao.get(id);
	}

	@Override
	public List<GoodsVoucher> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.goodsVoucherDao.query(query, params, begin, max);
	}

	@Override
	public boolean save(GoodsVoucher instance) {
		// TODO Auto-generated method stub
		try {
			this.goodsVoucherDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	

}
