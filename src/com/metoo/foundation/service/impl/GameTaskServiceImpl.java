package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.domain.GameTask;
import com.metoo.foundation.service.IGameTaskService;

@Service
@Transactional
public class GameTaskServiceImpl implements IGameTaskService{

	@Resource(name = "gameTaskDAO")
	private IGenericDAO<GameTask> gameTaskDao;
	
	@Override
	public GameTask getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.gameTaskDao.get(id);
	}

	@Override
	public boolean save(GameTask instance) {
		// TODO Auto-generated method stub
		try {
			this.gameTaskDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	@Override
	public boolean update(GameTask instance) {
		// TODO Auto-generated method stub
		try {
			this.gameTaskDao.update(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		try {
			this.gameTaskDao.remove(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	@Override
	public List<GameTask> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.gameTaskDao.query(query, params, begin, max);
	}

}
