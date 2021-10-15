package com.metoo.foundation.dao;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.ModelAndView;

import com.metoo.core.base.GenericDAO;
import com.metoo.core.query.QueryObject;
import com.metoo.foundation.domain.Game;

@Repository("gameDAO")
public class GameDAO extends GenericDAO<Game>{

}
