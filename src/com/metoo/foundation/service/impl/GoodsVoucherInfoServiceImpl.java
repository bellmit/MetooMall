package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.domain.GoodsVoucherInfo;
import com.metoo.foundation.service.IGoodsVoucherInfoService;

@Service
@Transactional
public class GoodsVoucherInfoServiceImpl implements IGoodsVoucherInfoService{

	@Resource(name = "goodsVoucherInfoDAO")
	private IGenericDAO<GoodsVoucherInfo> goodsVoucherInfoDao;
	
	@Override
	public GoodsVoucherInfo getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.goodsVoucherInfoDao.get(id);
	}

	@Override
	public List<GoodsVoucherInfo> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.goodsVoucherInfoDao.query(query, params, begin, max);
	}

	@Override
	public boolean save(GoodsVoucherInfo instance) {
		// TODO Auto-generated method stub
		try {
			this.goodsVoucherInfoDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(GoodsVoucherInfo instance) {
		// TODO Auto-generated method stub
		try {
			this.goodsVoucherInfoDao.update(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
