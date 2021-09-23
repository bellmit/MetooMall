package com.metoo.modul.app.game.tree.tools;

import java.math.BigDecimal;
import java.util.Map;

import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;

import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.Game;
import com.metoo.foundation.domain.GameTask;
import com.metoo.foundation.domain.GameUserTask;
import com.metoo.foundation.domain.OrderForm;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IGameService;
import com.metoo.foundation.service.IGameTaskService;
import com.metoo.foundation.service.IGameUserTaskService;
import com.metoo.foundation.service.IOrderFormService;
import com.metoo.foundation.service.IUserService;

public class GameTaskUtil implements GameTaskBase{

	@Autowired
	private IGameTaskService gameTaskService;
	@Autowired
	private IGameUserTaskService gameUserTaskService;
	@Autowired
	private IGameService gameService;
	@Autowired
	private IOrderFormService orderService;
	@Autowired
	private IUserService userService;
	
	@Override
	public boolean grant(Long id) {
		/*// TODO Auto-generated method stub
		GameUserTask gameUserTask = this.gameUserTaskService.getObjById(id);
		if(gameUserTask != null){
			Game game = this.gameService.getObjById(gameUserTask.getGameTask().getGame().getId());
			if(game != null && game.getStatus() == 0){
				GameTask gameTask = gameUserTask.getGameTask();
				if(gameTask != null){
					Map express_map = Json.fromJson(Map.class,
							gameTask.getWater());
					
				}
			}
		}*/
		return false;
	}
	
	/*@Override
	public String grant1(Map<String, Object> params) {
		// TODO Auto-generated method stub
		System.out.println(CommUtil.null2Long(params.get("game_task_id")));
		GameTask gameUserTask = this.gameUserTaskService.getObjById(CommUtil.null2Long(params.get("game_task_id")));
		if(gameUserTask != null){
			GameTree game = this.gameTreeService.getObjById(gameUserTask.getGame_tree_id());
			if(game != null && game.getStatus() == 0){
				GameTask gameTask = gameUserTask.getGame_task();
				if(gameTask != null){
					Map<Object, Object> map = Json.fromJson(Map.class,
							gameTask.getWater());
					String user_id = params.get("user_id").toString();
					String order_id = params.get("order_id").toString();
					OrderForm order = this.orderService.getObjById(CommUtil.null2Long("order_id"));
					User user = this.userService.getObjById(CommUtil.null2Long(user_id));
					if(order != null && user != null){
						if(order.getOrder_main() == 1){
							BigDecimal order_total_price = order.getTotalPrice();
							int water = 0;
							for(Map.Entry<Object, Object> entry : map.entrySet()){
								if(CommUtil.subtract(entry.getKey(), order_total_price) >= 0){
									water = Integer.parseInt(entry.getValue().toString());
								}
							}
							user.setWater_drops_unused(user.getWater_drops_unused() + water);
							this.userService.update(user);
							return gameTask.getMessage();
						}
					}
				}
			}
		}
		return null;
	}*/

	@Override
	public String grant1(Map<String, Object> params) {
		// TODO Auto-generated method stub
		System.out.println(CommUtil.null2Long(params.get("game_task_id")));
		GameTask gameTask = this.gameTaskService.getObjById(CommUtil.null2Long(params.get("game_task_id")));
		if(gameTask != null){
			Game game = gameTask.getGame();
			if(game != null && game.getStatus() == 0){
				if(gameTask != null){
					/*Map<Object, Object> map = Json.fromJson(Map.class,
							gameTask.getWater());*/
					String user_id = params.get("user_id").toString();
					String order_id = params.get("order_id").toString();
					OrderForm order = this.orderService.getObjById(CommUtil.null2Long("order_id"));
					User user = this.userService.getObjById(CommUtil.null2Long(user_id));
					if(order != null && user != null){
						if(order.getOrder_main() == 1){
							BigDecimal order_total_price = order.getTotalPrice();
							int water = 0;
							/*for(Map.Entry<Object, Object> entry : map.entrySet()){
								if(CommUtil.subtract(entry.getKey(), order_total_price) >= 0){
									water = Integer.parseInt(entry.getValue().toString());
								}
							}*/
							user.setWater_drops_unused(user.getWater_drops_unused() + water);
							this.userService.update(user);
							return gameTask.getMessage();
						}
					}
				}
			}
		}
		return null;
	}
}
