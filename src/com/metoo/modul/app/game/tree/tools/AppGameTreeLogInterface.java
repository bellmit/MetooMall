package com.metoo.modul.app.game.tree.tools;

import org.springframework.stereotype.Component;

import com.metoo.foundation.domain.User;

@Component
public interface AppGameTreeLogInterface {

	void add(User user, User friend);
	
	void remove(User user, User friend);
}
