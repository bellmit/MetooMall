package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.Resources;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.foundation.dao.GameTreeLogDAO;
import com.metoo.foundation.domain.GameTreeLog;
import com.metoo.foundation.service.IGameTreeLogService;

@Service
@Transactional
public class GameTreeLogServiceImpl implements IGameTreeLogService {

	@Resource(name = "gameTreeLogDAO")
	private GameTreeLogDAO gameTreeLogdao;
	
	@Override
	public GameTreeLog getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.gameTreeLogdao.get(id);
	}

	@Override
	public boolean save(GameTreeLog instance) {
		// TODO Auto-generated method stub
		try {
			this.gameTreeLogdao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public boolean update(GameTreeLog instance) {
		// TODO Auto-generated method stub
		try {
			this.gameTreeLogdao.update(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public List<GameTreeLog> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.gameTreeLogdao.query(query, params, begin, max);
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		try {
			this.gameTreeLogdao.remove(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

}
