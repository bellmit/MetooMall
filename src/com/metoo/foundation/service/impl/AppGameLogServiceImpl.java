package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.domain.GameLog;
import com.metoo.foundation.service.IAppGameLogService;

@Service
@Transactional
public class AppGameLogServiceImpl implements IAppGameLogService{
	
	@Resource(name = "gameLogDAO")
	private IGenericDAO<GameLog> gameLogDao;

	@Override
	public GameLog getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.gameLogDao.get(id);
	}

	@Override
	public boolean save(GameLog instance) {
		// TODO Auto-generated method stub
		try {
			this.gameLogDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(GameLog instance) {
		// TODO Auto-generated method stub
		try {
			this.gameLogDao.update(instance);
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
			this.gameLogDao.remove(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<GameLog> list(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.gameLogDao.query(query, params, begin, max);
	}

}
