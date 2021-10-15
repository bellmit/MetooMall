package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.domain.GameUserTask;
import com.metoo.foundation.service.IGameUserTaskService;

@Service
@Transactional
public class GameUserTaskServiceImpl implements IGameUserTaskService{
	
	@Resource(name = "gameUserTaskDAO")
	private IGenericDAO<GameUserTask> gameUserTaskDao;

	@Override
	public GameUserTask getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.gameUserTaskDao.get(id);
	}

	@Override
	public boolean save(GameUserTask instance) {
		// TODO Auto-generated method stub
		try {
			this.gameUserTaskDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	@Override
	public boolean update(GameUserTask instance) {
		// TODO Auto-generated method stub
		try {
			this.gameUserTaskDao.update(instance);
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
			this.gameUserTaskDao.remove(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	@Override
	public List<GameUserTask> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.gameUserTaskDao.query(query, params, begin, max);
	}

}
