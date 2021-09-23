package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.domain.GameGoods;
import com.metoo.foundation.service.IGameGoodsService;

@Service
@Transactional
public class GameGoodsServiceImpl implements IGameGoodsService{
	
	@Resource(name = "gameGoodsDAO")
	private IGenericDAO<GameGoods> gameGoodsDao;

	@Override
	public boolean save(GameGoods instance) {
		// TODO Auto-generated method stub
		try {
			this.gameGoodsDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(GameGoods instance) {
		// TODO Auto-generated method stub
		try {
			this.gameGoodsDao.update(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<GameGoods> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.gameGoodsDao.query(query, params, begin, max);
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		try {
			this.gameGoodsDao.remove(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public GameGoods getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.gameGoodsDao.get(id);
	}

}
