package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.domain.ArticleInfo;
import com.metoo.foundation.service.IArticleInfoService;

@Service
@Transactional
public class ArticleInfoServiceImpl implements IArticleInfoService{
	
	@Resource(name = "articleInfoDAO")
	private IGenericDAO<ArticleInfo> articleInfoDao;

	@Override
	public ArticleInfo getObjById(Long id) {
		// TODO Auto-generated method stub
		return this.articleInfoDao.get(id);
	}

	@Override
	public boolean save(ArticleInfo instance) {
		// TODO Auto-generated method stub
		try {
			this.articleInfoDao.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(ArticleInfo instance) {
		// TODO Auto-generated method stub
		try {
			this.articleInfoDao.update(instance);
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
			this.articleInfoDao.remove(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<ArticleInfo> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.articleInfoDao.query(query, params, begin, max);
	}

}
