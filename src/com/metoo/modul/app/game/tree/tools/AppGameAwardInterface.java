package com.metoo.modul.app.game.tree.tools;

import org.springframework.stereotype.Repository;

import com.metoo.foundation.domain.SubTrees;

@Repository
public interface AppGameAwardInterface {

	/**
	 * 获取Tree最终奖励
	 * 
	 * @param id
	 *            游戏ID
	 * @return GameAward
	 */
	SubTrees award(Long id);
}
