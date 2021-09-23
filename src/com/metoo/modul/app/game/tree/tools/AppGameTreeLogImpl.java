package com.metoo.modul.app.game.tree.tools;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.metoo.foundation.domain.GameTreeLog;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IUserService;

@Service
@Component
public class AppGameTreeLogImpl implements AppGameTreeLogInterface{

	@Autowired
	private IUserService userService;
	
	@Override
	public void add(User user, User friend) {
		// TODO Auto-generated method stub
		user = this.userService.getObjById(Long.parseLong("5340"));
		System.out.println(user.getApp_login_token());
	}

	@Override
	public void remove(User user, User friend) {
		// TODO Auto-generated method stub
		
	}



}
