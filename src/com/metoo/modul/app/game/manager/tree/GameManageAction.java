package com.metoo.modul.app.game.manager.tree;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.WebForm;
import com.metoo.foundation.domain.Accessory;
import com.metoo.foundation.domain.Game;
import com.metoo.foundation.service.IAccessoryService;
import com.metoo.foundation.service.IGameService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.module.app.buyer.domain.Result;

/**
 * <p>
 * 	Tite: GameTreeManageAction.java
 * </p>
 * 
 * <p>
 * 	Description: 小游戏控制器；用于创建种树小游戏
 * </p>
 * @author 46075
 *
 */
@Controller
@RequestMapping("app/v1/tree/")
public class GameManageAction {

	@Autowired
	private IGameService gameService;
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IAccessoryService accessoryService;

	/**
	 * 创建Trees游戏
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "game/save.json")
	@ResponseBody
	public String save(HttpServletRequest request, HttpServletResponse response, String id) {
		Game game = null;
		WebForm wf = new WebForm();
		if (id.equals("")) {
			game = wf.toPo(request, Game.class);
			game.setAddTime(new Date());
		} else {
			Game obj = this.gameService.getObjById(CommUtil.null2Long(id));
			game = (Game) wf.toPo(request, obj);
		}
		String uploadFilePath = this.configService.getSysConfig().getUploadFilePath();
		String filepath = uploadFilePath + "/" + "tree";
		try {
			Map map = CommUtil.httpsaveFileToServer(request, "accessory", filepath, null, null);
			if (map.get("fileName") != "") {
				Accessory accessory = new Accessory();
				accessory.setName(CommUtil.null2String(map.get("fileName")));
				accessory.setExt(CommUtil.null2String(map.get("mime")));
				accessory.setSize(BigDecimal.valueOf((CommUtil.null2Double(map.get("fileSize")))));
				accessory.setPath(uploadFilePath + "/tree");
				accessory.setWidth(CommUtil.null2Int(map.get("width")));
				accessory.setHeight(CommUtil.null2Int(map.get("height")));
				this.accessoryService.save(accessory);
				game.setAccessory(accessory);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (id.equals("")) {
			List<Game> gameTrees = this.gameService.query("select obj from GameTree obj", null, -1, -1);
			if (gameTrees.size() == 0) {
				this.gameService.save(game);
			}
		} else {
			this.gameService.update(game);
		}
		return JSONObject.toJSONString(new Result(5200, "Successfully"));
	}

	@RequestMapping("update/accessory.json")
	@ResponseBody
	public String img(HttpServletRequest request, HttpServletResponse response){
		String uploadFilePath = this.configService.getSysConfig().getUploadFilePath();
		String filepath = uploadFilePath + "/" + "tree";
		try {
			Map map = CommUtil.httpsaveFileToServer(request, "accessory", filepath, null, null);
			if (map.get("fileName") != "") {
				Accessory accessory = new Accessory();
				accessory.setName(CommUtil.null2String(map.get("fileName")));
				accessory.setExt(CommUtil.null2String(map.get("mime")));
				accessory.setSize(BigDecimal.valueOf((CommUtil.null2Double(map.get("fileSize")))));
				accessory.setPath(uploadFilePath + "/tree");
				accessory.setWidth(CommUtil.null2Int(map.get("width")));
				accessory.setHeight(CommUtil.null2Int(map.get("height")));
				this.accessoryService.save(accessory);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
	}


	
}
