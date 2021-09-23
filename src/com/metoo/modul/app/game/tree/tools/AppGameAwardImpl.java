package com.metoo.modul.app.game.tree.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.SpringUtil;
import com.metoo.foundation.domain.SubTrees;
import com.metoo.foundation.service.IGameService;
import com.metoo.foundation.service.ISubTreesService;

public class AppGameAwardImpl implements AppGameAwardInterface {

	@Autowired
	private ISubTreesService subTreesService;

	/*
	 * private static AppGameAwardImpl appGameAwardImpl;
	 * 
	 * @PostConstruct public void init() { appGameAwardImpl = this;
	 * appGameAwardImpl.gameTreeService = (IGameTreeService)
	 * SpringUtil.getObject(IGameTreeService.class);
	 * appGameAwardImpl.subTreesService = (ISubTreesService)
	 * SpringUtil.getObject(ISubTreesService.class); }
	 */
	@Override
	public SubTrees award(Long id) {
		// TODO Auto-generated method stub
		Map params = new HashMap();
		params.put("trees_id", id);
		this.subTreesService = (ISubTreesService) SpringUtil.getObject(ISubTreesService.class);
	/*	List<SubTrees> sub_trees = this.subTreesService
				.query("SELECT max(status) status FROM SubTrees obj WHERE obj.trees.id=:trees_id", params, -1, -1);*/
		List<SubTrees> sub_trees = this.subTreesService
				.query("SELECT obj FROM SubTrees obj WHERE obj.trees.id=:trees_id AND obj.status=(SELECT MAX(status) FROM SubTrees)", params, -1, -1);
		if (sub_trees.size() > 0) {
			SubTrees sub = sub_trees.get(0);
			return sub;
		}
		return null;
	}

}
