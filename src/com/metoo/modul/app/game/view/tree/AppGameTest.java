package com.metoo.modul.app.game.view.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.Game;
import com.metoo.foundation.domain.PlantingTrees;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IAddressService;
import com.metoo.foundation.service.IAreaService;
import com.metoo.foundation.service.IGameAwardInfoService;
import com.metoo.foundation.service.IAppGameAwardRandomService;
import com.metoo.foundation.service.IGameGoodsService;
import com.metoo.foundation.service.IGameService;
import com.metoo.foundation.service.IGameTreeLogService;
import com.metoo.foundation.service.IPlantingtreesService;
import com.metoo.foundation.service.ISubTreesService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.ITreeService;
import com.metoo.foundation.service.IUserService;
import com.metoo.modul.app.game.tree.tools.AppGameAwardTools;
import com.metoo.modul.app.game.tree.tools.AppGameTreeTools;

@Controller
public class AppGameTest {

	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ITreeService treeService;
	@Autowired
	private IPlantingtreesService plantingtreesService;
	@Autowired
	private ISubTreesService subTreesService;
	@Autowired
	private IGameService gameService;
	@Autowired
	private AppGameAwardTools appGameAwardTools;
	@Autowired
	private IGameAwardInfoService GameAwardInfoService;
	@Autowired
	private AppGameTreeTools appGameTreeTools;
	@Autowired
	private IAppGameAwardRandomService gameAwardRandomService;
	@Autowired
	private IGameGoodsService gameGoodsService;
	@Autowired
	private IAddressService addressService;
	@Autowired
	private IAreaService areaService;
	@Autowired
	private IGameTreeLogService gameTreeLogService;
	
	@RequestMapping("/game/tree/test.json")
	public void test(String param){
		List<Map<String, Object>> list = Json.fromJson(List.class,
				param);
		for(Map<String, Object> map : list){
			System.out.println(map.get("probably").toString());
			Map map1 = Json.fromJson(Map.class, Json.toJson(map.get("probably")));
			System.out.println(map1);
		}
		
	}

	/*
	 * public static void main(String[] args) { // int[] str = {40, 30, 20, 10};
	 * int[] str = {10, 20, 30, 40}; int n = new Random().nextInt(100); int m =
	 * 0; for(int i=0; i < str.length; i++){ if(n <= str[i] + m){
	 * System.out.println("n: " + n + "在第" + i); break; } m = str[i] + m; }
	 * 
	 * 
	 * }
	 */


}
