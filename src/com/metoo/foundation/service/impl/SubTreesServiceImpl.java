package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.domain.SubTrees;
import com.metoo.foundation.service.ISubTreesService;

@Service
@Transactional
public class SubTreesServiceImpl implements ISubTreesService{

	@Resource(name = "subTreesDAO")
	private IGenericDAO<SubTrees> subTreesDao;
	
	@Override
	public SubTrees getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.subTreesDao.get(id);
	}

	@Override
	public boolean save(SubTrees instance) {
		// TODO Auto-generated method stub
		try {
			this.subTreesDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public boolean update(SubTrees instance) {
		// TODO Auto-generated method stub
		try {
			this.subTreesDao.update(instance);
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
			this.subTreesDao.remove(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<SubTrees> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.subTreesDao.query(query, params, begin, max);
	}

}
