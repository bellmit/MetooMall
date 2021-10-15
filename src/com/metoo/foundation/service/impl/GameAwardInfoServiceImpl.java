package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.dao.GameAwardInfoDAO;
import com.metoo.foundation.domain.GameAwardInfo;
import com.metoo.foundation.service.IGameAwardInfoService;

@Service
@Transactional
public class GameAwardInfoServiceImpl implements IGameAwardInfoService{
	
	@Resource(name = "gameAwardInfoDAO")
	private IGenericDAO<GameAwardInfo> GameAwardInfoDao;

	@Override
	public GameAwardInfo getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.GameAwardInfoDao.get(id);
	}

	@Override
	public boolean save(GameAwardInfo instance) {
		// TODO Auto-generated method stub
		try {
			this.GameAwardInfoDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public boolean update(GameAwardInfo instance) {
		// TODO Auto-generated method stub
		try {
			this.GameAwardInfoDao.update(instance);
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
			this.GameAwardInfoDao.remove(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<GameAwardInfo> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.GameAwardInfoDao.query(query, params, begin, max);
	}

}
