package com.metoo.modul.app.game.tree.tools;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metoo.core.constant.Globals;
import com.metoo.foundation.domain.Friend;
import com.metoo.foundation.domain.GameTreeLog;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IFriendService;
import com.metoo.foundation.service.IGameTreeLogService;

@Component
public class AppFriendBuyerTools {

	@Autowired
	private IGameTreeLogService gameTreeLogService;
	@Autowired
	private IFriendService friendService;

	/**
	 * 
	 * @param params
	 * @param status
	 * @return
	 */
	public boolean verifyFriend(Map params, int status) {
		List<GameTreeLog> gameTreeLogs = this.gameTreeLogService.query(
				"SELECT obj FROM GameTreeLog obj WHERE obj.user_id=:user_id AND obj.friend_id=:friend_id AND obj.type=:type",
				params, -1, -1);
		if (gameTreeLogs.size() > 0) {
			GameTreeLog gameTreeLog = gameTreeLogs.get(0);
			gameTreeLog.setStatus(status);
			return this.gameTreeLogService.update(gameTreeLog);
		}
		return false;
	}

	public boolean verifyFriend1(Map params, int status) {
		List<GameTreeLog> gameTreeLogs = this.gameTreeLogService.query(
				"SELECT obj FROM GameTreeLog obj WHERE obj.user_id=:user_id AND obj.friend_id=:friend_id AND obj.type=:type",
				params, -1, -1);
		if (gameTreeLogs.size() > 0) {
			GameTreeLog gameTreeLog = gameTreeLogs.get(0);
			gameTreeLog.setStatus(status);
			return this.gameTreeLogService.update(gameTreeLog);
		}
		return false;
	}

	/**
	 * 申请添加好友日志
	 */
	public void apply(User user, User friend) {

		this.create(user, friend, -1);
	}

	public void remove(User user, User friend) {

		this.create(user, friend, -4);
	}
	
	public void registerGameLog(User user, User friend, String gameAward) {

		this.create(user, friend, 7, -7, gameAward);
	}
	
	public void visit(User user, User friend) {

		this.create(user, friend, 8, -8);
	}

	public void create(User user, User friend, Integer status) {

		// User 以添加 friend 为好友
		GameTreeLog gtl = new GameTreeLog();
		gtl.setAddTime(new Date());
		gtl.setUser_id(user.getId());
		gtl.setUser_name(user.getUsername());
		gtl.setStatus(Math.abs(status));
		gtl.setType(5);
		gtl.setFriend_id(friend.getId());
		gtl.setFriend_name(friend.getUserName());
		String message = "You have sent a verification message to " + Globals.PREFIXHTML + friend.getUserName() + Globals.SUFFIXHTML;
		gtl.setMessage(message);
		this.gameTreeLogService.save(gtl);

		// friend 
		GameTreeLog gtlf = new GameTreeLog();
		gtlf.setAddTime(new Date());
		gtlf.setUser_id(friend.getId());
		gtlf.setUser_name(friend.getUsername());
		gtlf.setStatus(status);
		gtlf.setType(5);
		gtlf.setFriend_id(user.getId());
		gtlf.setFriend_name(user.getUserName());
		
		String message1 = Globals.PREFIXHTML + user.getUserName() + Globals.SUFFIXHTML + " request to add you as friend";
		gtlf.setMessage(message1);
		 
		this.gameTreeLogService.save(gtlf);
	}
	
	public void create(User user, User friend, Integer type, Integer status) {

		// User 以添加 friend 为好友
		GameTreeLog gtl = new GameTreeLog();
		gtl.setAddTime(new Date());
		gtl.setUser_id(user.getId());
		gtl.setUser_name(user.getUsername());
		gtl.setStatus(Math.abs(status));
		gtl.setType(type);
		gtl.setFriend_id(friend.getId());
		gtl.setFriend_name(friend.getUserName());
		gtl.setMessage(Globals.PREFIXHTML + friend.getUserName() + Globals.SUFFIXHTML + " become a new user through your invitation");
		this.gameTreeLogService.save(gtl);

		// friend 
		GameTreeLog gtlf = new GameTreeLog();
		gtlf.setAddTime(new Date());
		gtlf.setUser_id(friend.getId());
		gtlf.setUser_name(friend.getUsername());
		gtlf.setStatus(status);
		gtlf.setType(type);
		gtlf.setFriend_id(user.getId());
		gtlf.setFriend_name(user.getUserName());
		gtlf.setMessage(Globals.PREFIXHTML + user.getUserName() + Globals.SUFFIXHTML + " has become your friend. Go visit his orchard!");
		this.gameTreeLogService.save(gtlf);
	}
	
	public void create(User user, User friend, Integer type, Integer status, String gameAward) {

		// User 以添加 friend 为好友
		GameTreeLog gtl = new GameTreeLog();
		gtl.setAddTime(new Date());
		gtl.setUser_id(user.getId());
		gtl.setUser_name(user.getUsername());
		gtl.setStatus(Math.abs(status));
		gtl.setType(type);
		gtl.setFriend_id(friend.getId());
		gtl.setFriend_name(friend.getUserName());
		gtl.setMessage(Globals.PREFIXHTML + friend.getUserName() + Globals.SUFFIXHTML + " has become your friend");
		this.gameTreeLogService.save(gtl);

		// friend 
		GameTreeLog gtlf = new GameTreeLog();
		gtlf.setAddTime(new Date());
		gtlf.setUser_id(friend.getId());
		gtlf.setUser_name(friend.getUsername());
		gtlf.setStatus(status);
		gtlf.setType(type);
		gtlf.setFriend_id(user.getId());
		gtlf.setFriend_name(user.getUserName());
		gtlf.setMessage(Globals.PREFIXHTML + user.getUserName() + Globals.SUFFIXHTML + "  has become your friend");
		gtlf.setGameAward(gameAward);
		this.gameTreeLogService.save(gtlf);
	}

	/**
	 * 设置互为好友
	 * @param user
	 * @param friend
	 * @return
	 */
	public boolean creatFriend(User user, User friend){
		// 
		Friend obj = new Friend();
		obj.setAddTime(new Date());
		obj.setStatus(1);
		obj.setUser(user);
		obj.setUserName(user.getUserName());
		obj.setFriend(friend);
		obj.setMobile(friend.getMobile());
		obj.setSex(friend.getSex());
		obj.setFriendName(friend.getUsername());
		if(this.friendService.save(obj)){
			Friend obj1 = new Friend();
			obj1.setAddTime(new Date());
			obj1.setStatus(1);
			obj1.setUser(friend);
			obj1.setUserName(friend.getUserName());
			obj1.setFriend(user);
			obj1.setFriendName(user.getUsername());
			obj1.setMobile(user.getMobile());
			obj1.setSex(user.getSex());
			return this.friendService.save(obj1);
		}
		return false;
	}
}
