package com.metoo.modul.app.game.manager.tree;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iskyshop.core.tools.CommUtil;
import com.metoo.core.tools.WebForm;
import com.metoo.foundation.domain.Game;
import com.metoo.foundation.domain.GameAward;
import com.metoo.foundation.domain.GameTask;
import com.metoo.foundation.service.IGameAwardService;
import com.metoo.foundation.service.IGameService;
import com.metoo.foundation.service.IGameTaskService;
import com.metoo.module.app.buyer.domain.Result;

@Controller
@RequestMapping("/php/game/tree/task")
public class GameTaskManageAction {
	
	@Autowired
	private IGameTaskService gameTaskService;
	@Autowired
	private IGameService gameService;
	@Autowired
	private IGameAwardService gameAwardService;
	
	@RequestMapping("/save.json")
	@ResponseBody
	public Object save(HttpServletRequest request, HttpServletResponse response, String id, String game_id, String type, String game_award_id) {
		int code = -1;
		String msg = "";
		GameTask task = null;
		WebForm wf = new WebForm();
		if(id.equals("")){
			task = wf.toPo(request, GameTask.class);
		}else{
			GameTask obj = this.gameTaskService.getObjById(CommUtil.null2Long(id));
			task = (GameTask) wf.toPo(request, obj);
		}    
		if(!game_id.equals("")){
			Game game = this.gameService.getObjById(CommUtil.null2Long(game_id));
			task.setGame(game);
		}
		if(type.equals("water")){
			task.setType(0);
		}else if(type.equals("gather")){
			task.setType(1);
		}else if(type.equals("browse")){
			task.setType(2);
		}else if(type.equals("order")){
			task.setType(3);
		}else if(type.equals("new")){
			task.setType(4);
		}else if(type.equals("email")){
			task.setType(6);
		}else if(type.equals("article")){
			task.setType(7);
		}else if(type.equals("goods")){
			task.setType(8);
		}else if(type.equals("assign")){
			task.setType(9);
		}
		GameAward gameAward = this.gameAwardService.getObjById(CommUtil.null2Long(game_award_id));
		if(gameAward !=null ){
			task.setGameAward(gameAward);
		}
		if(id.equals("")){
			this.gameTaskService.save(task);
		}else{
			this.gameTaskService.update(task);
		}
		return new Result(5200, "Successfully");
	}
	
	// update 关闭任务时，清楚游戏关联的任务记录
	/*@RequestMapping("/task/insert.json")
	@ResponseBody
	public Object insert(HttpServletRequest request, HttpServletResponse response, @RequestBody GameTask gametask, String game_id) {
		int code = -1;
		String msg = "";
		GameTask task = null;
		WebForm wf = new WebForm();
		if(gametask.getId().equals("")){
			task = wf.toPo(request, GameTask.class);
		}else{
			GameTask obj = this.gameTaskService.getObjById(CommUtil.null2Long(gametask.getId()));
			task = (GameTask) wf.toPo(request, obj);
		}
		if(!game_id.equals("")){
			GameTree gameTree = this.gameTreeService.getObjById(CommUtil.null2Long(game_id));
			task.setGameTree(gameTree);
		}
		if(gametask.getId().equals("")){
			this.gameTaskService.save(task);
		}else{
			this.gameTaskService.update(task);
		}
		return new Result(5200, "Successfully");
	}*/
}
