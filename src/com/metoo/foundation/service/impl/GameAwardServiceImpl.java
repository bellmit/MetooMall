package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.foundation.dao.GameAwardDAO;
import com.metoo.foundation.domain.GameAward;
import com.metoo.foundation.service.IGameAwardService;

@Service
@Transactional
public class GameAwardServiceImpl implements IGameAwardService{

	@Resource(name = "gameAwardDAO")
	private GameAwardDAO gameAwardDao;
	
	@Override
	public GameAward getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.gameAwardDao.get(id);
	}

	@Override
	public boolean save(GameAward instance) {
		// TODO Auto-generated method stub
		try {
			this.gameAwardDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public boolean update(GameAward instance) {
		// TODO Auto-generated method stub
		try {
			this.gameAwardDao.update(instance);
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
			this.gameAwardDao.remove(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<GameAward> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.gameAwardDao.query(query, params, begin, max);
	}

}
