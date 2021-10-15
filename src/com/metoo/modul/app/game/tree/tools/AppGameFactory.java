package com.metoo.modul.app.game.tree.tools;

import com.metoo.foundation.domain.User;

public class AppGameFactory {

	public AppGameTreeLogInterface creatLog(String type){
		if(type == null){
			return null;
		}
		if(type.equals("ADD")){
			return new AppGameTreeLogImpl(); 
		}
		if(type.equals("del")){
			return new AppGameTreeLogImpl(); 
		}
		return null;
	}
}
