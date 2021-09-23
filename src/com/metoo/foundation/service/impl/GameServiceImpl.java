package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.domain.Game;
import com.metoo.foundation.service.IGameService;

@Service
@Transactional
public class GameServiceImpl implements IGameService{

	@Resource(name = "gameDAO")
	private IGenericDAO<Game> gameDao;
	
	@Override
	public Game getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.gameDao.get(id);
	}

	@Override
	public boolean save(Game instance) {
		// TODO Auto-generated method stub
		try {
			this.gameDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		
	}

	@Override
	public boolean update(Game instance) {
		// TODO Auto-generated method stub
		try {
			this.gameDao.update(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	@Override
	public List<Game> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.gameDao.query(query, params, begin, max);
	}

}
