package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.domain.PlantingTrees;
import com.metoo.foundation.service.IPlantingtreesService;

@Service
@Transactional
public class PlantingsTreesServiceImpl implements IPlantingtreesService {

	@Resource(name = "plantingTreesDAO")
	private IGenericDAO<PlantingTrees> plantDao;
	
	@Override
	public boolean save(PlantingTrees instance) {
		// TODO Auto-generated method stub
		try {
			this.plantDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	@Override
	public boolean update(PlantingTrees instance) {
		// TODO Auto-generated method stub
		try {
			this.plantDao.update(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	@Override
	public List<PlantingTrees> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.plantDao.query(query, params, begin, max);
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		try {
			this.plantDao.remove(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}


}
