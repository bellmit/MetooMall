package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.Resources;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.domain.GameLog;
import com.metoo.foundation.service.IGameLogService;

@Service
@Transactional
public class GameLogServiceImpl implements IGameLogService {
	
	@Resource(name = "gameLogDAO")
	private IGenericDAO<GameLog> gameLogDao;
	

	@Override
	public boolean save(GameLog instance) {
		// TODO Auto-generated method stub
		try {
			this.gameLogDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}


	@Override
	public List<GameLog> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.gameLogDao.query(query, params, begin, max);
	}

}
