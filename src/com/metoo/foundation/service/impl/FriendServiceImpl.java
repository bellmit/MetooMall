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
import com.metoo.foundation.dao.FriendDAO;
import com.metoo.foundation.domain.Friend;
import com.metoo.foundation.domain.GoodsCart;
import com.metoo.foundation.service.IFriendService;

@Service
@Transactional
public class FriendServiceImpl implements IFriendService{
	
	@Resource(name = "friendDAO")
	private IGenericDAO<Friend> friendDao;

	@Override
	public Friend getObjByID(Long id) {
		// TODO Auto-generated method stub
		return this.friendDao.get(id);
	}

	@Override
	public boolean save(Friend instance) {
		// TODO Auto-generated method stub
		try {
			this.friendDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(Friend instance) {
		// TODO Auto-generated method stub
		try {
			this.friendDao.update(instance);
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
			this.friendDao.remove(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Friend> query(String sql, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.friendDao.query(sql, params, begin, max);
	}

	@Override
	public IPageList list(IQueryObject properties) {
		// TODO Auto-generated method stub
		if (properties == null) {
			return null;
		}
		String query = properties.getQuery();
		String construct = properties.getConstruct();
		Map params = properties.getParameters();
		GenericPageList pList = new GenericPageList(Friend.class,construct, query,
				params, this.friendDao);
		if (properties != null) {
			PageObject pageObj = properties.getPageObj();
			if (pageObj != null)
				pList.doList(pageObj.getCurrentPage() == null ? 0 : pageObj
						.getCurrentPage(), pageObj.getPageSize() == null ? 0
						: pageObj.getPageSize());
		} else
			pList.doList(0, -1);
		return pList;
	}

}
