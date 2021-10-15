package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.Resources;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.domain.LuckyDrawAward;
import com.metoo.foundation.service.ILuckyDrawAwardService;

@Service
@Transactional
public class LuckyDrawAwardServiceImpl implements ILuckyDrawAwardService{

	@Resource(name = "luckyDrawAwardDAO")
	private IGenericDAO<LuckyDrawAward> luckyDrawAwardDao;
	
	@Override
	public LuckyDrawAward getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.luckyDrawAwardDao.get(id);
	}

	@Override
	public boolean save(LuckyDrawAward instance) {
		// TODO Auto-generated method stub
		try {
			this.luckyDrawAwardDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(LuckyDrawAward instance) {
		// TODO Auto-generated method stub
		try {
			this.luckyDrawAwardDao.update(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<LuckyDrawAward> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.luckyDrawAwardDao.query(query, params, begin, max);
	}

}
