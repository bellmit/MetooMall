package com.metoo.modul.app.game.tree.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.Address;
import com.metoo.foundation.domain.Game;
import com.metoo.foundation.domain.GameTask;
import com.metoo.foundation.domain.OrderForm;
import com.metoo.foundation.domain.PlantingTrees;
import com.metoo.foundation.domain.SubTrees;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IAddressService;
import com.metoo.foundation.service.IGameTaskService;
import com.metoo.foundation.service.IOrderFormService;
import com.metoo.foundation.service.ISubTreesService;
import com.metoo.foundation.service.IUserService;

@Component
public class AppGameTreeTools {

	@Autowired
	private IGameTaskService gameTaskService;
	@Autowired
	private IOrderFormService orderService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ISubTreesService subTreesService;
	@Autowired
	private IAddressService addressService;

	// 下单赠送水滴-- 不用设置任务状态
	public String grantWater(Map<String, Object> params) {
		if (!params.isEmpty()) {
			// TODO Auto-generated method stub
			GameTask gameTask = this.gameTaskService.getObjById(CommUtil.null2Long(params.get("game_task_id")));
			if (gameTask != null) {
				Game game = gameTask.getGame();
				if (game != null && game.getStatus() == 0) {
					if (gameTask != null) {
						// LinkedHashMap<Object, Object> map = Json.fromJson(LinkedHashMap.class, gameTask.getWater());
						String user_id = params.get("user_id").toString();
						String order_id = params.get("order_id").toString();
						OrderForm order = this.orderService.getObjById(CommUtil.null2Long(params.get("order_id")));
						User user = this.userService.getObjById(CommUtil.null2Long(user_id));
						if (order != null && user != null) {
							if (order.getOrder_main() == 1) {
								int water = order.getTotalPrice().intValue();
								/*for (Map.Entry<Object, Object> entry : map.entrySet()) {
									if (CommUtil.subtract(entry.getKey(), order_total_price) <= 0) {
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
				return null;
			}
		}
		return null;
	}

	/**
	 * 收获
	 * 
	 * @param user
	 */
	/*public void harvest(User user) {

		PlantingTrees plantingTree = user.getPlanting_trees();
		if (plantingTree != null) {
			Trees trees = plantingTree.getTrees();
			SubTrees subTrees = this.subTreesService.getObjById(plantingTree.getSub_trees());
			Map params = new HashMap();
			params.put("trees_id", trees.getId());
			List<SubTrees> sub_trees = this.subTreesService.query(
					"SELECT obj FROM SubTrees obj WHERE obj.trees.id=:trees_id AND obj.status=(SELECT MAX(status) FROM SubTrees)",
					params, -1, -1);
			// 是否满级
			if (subTrees.getStatus() == sub_trees.get(0).getStatus()
					&& subTrees.getWatering() >= subTrees.getWatering()) {

				user.getTrees().add(trees);
				this.userService.update(user);
			}
		}
	}*/


	public boolean fill_level(PlantingTrees plantingTree) {
		boolean fill_level = false;
		SubTrees subTrees = this.subTreesService.getObjById(plantingTree.getSub_trees());
		Map params = new HashMap();
		params.put("tree_id", plantingTree.getTree().getId());
		List<SubTrees> sub_trees = this.subTreesService.query(
				"SELECT obj FROM SubTrees obj WHERE obj.tree.id=:tree_id AND obj.status=(SELECT MAX(status) FROM SubTrees)",
				params, -1, -1);
		// 是否满级
		if (subTrees.getStatus() == sub_trees.get(0).getStatus() && subTrees.getWatering() >= subTrees.getWatering()) {
			fill_level = true;
		}
		return fill_level;
	}
	

	/**
	 * 验证用户是否存在收货地址
	 * 
	 * @param user
	 * @return
	 */
	public boolean verifyAddress(User user) {
		Map params = new HashMap();
		params.put("user_id", user.getId());
		List<Address> addressList = this.addressService.query("SELECT obj FROM Address obj WHERE obj.user.id=:user_id",
				params, -1, -1);
		if(addressList.size() > 0){
			return true;
		}
		return false;

	}

}
