package com.metoo.foundation.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metoo.core.dao.IGenericDAO;
import com.metoo.foundation.domain.SearchKeyword;
import com.metoo.foundation.service.ISearchKeywordService;

@Service
@Transactional
public class SearchKeywordServiceImpl implements ISearchKeywordService{

	@Resource(name = "searchKeywordDAO")
	private IGenericDAO<SearchKeyword> searchKeyword;
	
	@Override
	public boolean save(SearchKeyword instance) {
		// TODO Auto-generated method stub
		try {
			this.searchKeyword.save(instance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	@Override
	public boolean update(SearchKeyword intance) {
		// TODO Auto-generated method stub
		try {
			this.update(intance);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	@Override
	public List<SearchKeyword> query(String query, Map params, int begin, int max) {
		// TODO Auto-generated method stub
		return this.searchKeyword.query(query, params, begin, max);
	}

}
