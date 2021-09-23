package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.core.query.GenericPageList;
import com.metoo.core.query.PageObject;
import com.metoo.core.query.support.IPageList;
import com.metoo.core.query.support.IQueryObject;
import com.metoo.foundation.domain.Tree;
import com.metoo.foundation.service.ITreeService;

@Service
@Transactional
public class TreeServiceImpl implements ITreeService{

	@Resource(name = "treeDao")
	private IGenericDAO<Tree> treeDao;
	
	@Override
	public Tree getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.treeDao.get(id);
	}

	@Override
	public boolean save(Tree instance) {
		// TODO Auto-generated method stub
		try {
			this.treeDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		
	}

	@Override
	public boolean update(Tree instance) {
		// TODO Auto-generated method stub
		try {
			this.treeDao.update(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		
	}

	@Override
	public IPageList list(IQueryObject properties) {
		// TODO Auto-generated method stub
		if(properties == null){
			return null;
		}
		String query = properties.getQuery();
		String construct = properties.getConstruct();
		Map params = properties.getParameters();
		GenericPageList pList = new GenericPageList(Tree.class, construct, query, params, this.treeDao);
		if(properties != null){
			PageObject pageObj = properties.getPageObj();
			if (pageObj != null)
				pList.doList(pageObj.getCurrentPage() == null ? 0 : pageObj
						.getCurrentPage(), pageObj.getPageSize() == null ? 0
						: pageObj.getPageSize());
		}else
			pList.doList(0, -1);
		return pList;
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		try {
			this.treeDao.remove(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		
	}

	@Override
	public List<Tree> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.treeDao.query(query, params, begin, max);
	}

}
