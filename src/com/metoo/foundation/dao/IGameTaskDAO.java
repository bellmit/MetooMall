package com.metoo.foundation.dao;

import org.springframework.stereotype.Repository;

import com.metoo.core.base.GenericDAO;
import com.metoo.foundation.domain.GameTask;

@Repository("gameTaskDAO")
public class IGameTaskDAO extends GenericDAO<GameTask> {

}
