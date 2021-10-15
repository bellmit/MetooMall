package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.domain.LuckyDrawLog;
import com.metoo.foundation.service.ILuckyDrawLogService;

@Service
@Transactional
public class LuckyDrawLogServiceImpl implements ILuckyDrawLogService{
			
	@Resource(name = "luckyDrawLogDAO")
	private IGenericDAO<LuckyDrawLog> luckyDrawLogDao;
	
	@Override
	public LuckyDrawLog getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.luckyDrawLogDao.get(id);
	}

	@Override
	public boolean save(LuckyDrawLog instance) {
		// TODO Auto-generated method stub
		try {
			this.luckyDrawLogDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(LuckyDrawLog instance) {
		// TODO Auto-generated method stub
		try {
			this.luckyDrawLogDao.update(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<LuckyDrawLog> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.luckyDrawLogDao.query(query, params, begin, max);
	}

}
