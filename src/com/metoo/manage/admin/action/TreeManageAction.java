package com.metoo.manage.admin.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.WebForm;
import com.metoo.foundation.domain.Accessory;
import com.metoo.foundation.domain.Game;
import com.metoo.foundation.domain.SubTrees;
import com.metoo.foundation.domain.Tree;
import com.metoo.foundation.service.IAccessoryService;
import com.metoo.foundation.service.IGameService;
import com.metoo.foundation.service.ISubTreesService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.ITreeService;
import com.metoo.foundation.service.IUserService;
import com.metoo.foundation.service.impl.SysConfigService;
import com.metoo.module.app.buyer.domain.Result;


@Controller
public class TreeManageAction {

	@Autowired
	private ISysConfigService configService;
	@Autowired
	private ITreeService treeService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IAccessoryService accessoryService;
	@Autowired
	private ISubTreesService subTreeService;
	@Autowired
	private IGameService gameService;
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @param gt_id 游戏id
	 * @return
	 * 添加树
	 */
	@RequestMapping("/tree/save.json")
	@ResponseBody
	public String save(HttpServletRequest request, HttpServletResponse response, String id, String sub_tree, String gt_id) {
		String msg = "";
		int code = -1;
		Tree tree = null;
		WebForm wf = new WebForm();
		if (id.equals("")) {
			tree = wf.toPo(request, Tree.class);
			tree.setAddTime(new Date());
		} else {
			Tree obj = this.treeService.getObjById(CommUtil.null2Long(id));
			if(obj != null){
				tree = (Tree) wf.toPo(request, obj);
			}
		}
		if(tree != null){
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
					tree.setAccessory(accessory);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Game game = this.gameService.getObjById(CommUtil.null2Long(gt_id));
			if(game != null){
				tree.setGame(game);
			}
			if(!id.equals("")){
				this.treeService.update(tree);
			}else{
				this.treeService.save(tree);
			}
			if(!sub_tree.equals("")){
				List<Map> sub_trees = Json.fromJson(List.class, sub_tree);
				for(Map map : sub_trees){
					SubTrees subtrees = null;
					if(!CommUtil.null2String(map.get("id")).equals("")){
						subtrees = this.subTreeService.getObjById(CommUtil.null2Long(map.get("id")));
						subtrees.setWatering(CommUtil.null2Int(map.get("watering")));
						subtrees.setAward(CommUtil.null2Int(map.get("award")));
						Accessory accessory = this.accessoryService.getObjById(CommUtil.null2Long(map.get("accessory")));
						subtrees.setAccessory(accessory);
						subtrees.setTree(tree);
						subtrees.setStatus(CommUtil.null2Int(map.get("status")));
						this.subTreeService.update(subtrees);
					}else{
						subtrees = new SubTrees();
						subtrees.setAddTime(new Date());
						subtrees.setTree(tree);
						subtrees.setWatering(CommUtil.null2Int(map.get("watering")));
						subtrees.setAward(CommUtil.null2Int(map.get("award")));
						Accessory accessory = this.accessoryService.getObjById(CommUtil.null2Long(map.get("accessory")));
						subtrees.setAccessory(accessory);
						subtrees.setStatus(CommUtil.null2Int(map.get("status")));
						this.subTreeService.save(subtrees);
					}
				}
			}
			code = 5200;
			msg = "Successfully";
		}else{
			code = 4605;
			msg = "Object is empty";
		}
		return JSONObject.toJSONString(new Result(code, msg));
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param mulitId
	 * @return
	 * 删除树
	 */
	@RequestMapping("/tree/del.json")
	@ResponseBody
	public String del(HttpServletRequest request, HttpServletResponse response, String mulitId){
		String[] ids = mulitId.split(",");
		for(String id : ids){
			if(!id.equals("")){
				Tree tree = this.treeService.getObjById(CommUtil.null2Long(id));
				if(tree != null){
					this.treeService.delete(CommUtil.null2Long(id));
				}
			}
		}
		return JSONObject.toJSONString(new Result(5200, "Successfully"));
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
/*	@RequestMapping("set_tree_watering.json")
	@ResponseBody
	public String set_tree_watering(HttpServletRequest request, HttpServletResponse response, String id){
		GameTree gameTree = new GameTree();
		WebForm wf = new WebForm();
		if(id.equals("")){
			gameTree = wf.toPo(request, GameTree.class);
			gameTree.setAddTime(new Date());
		}else{
			GameTree obj = this.gameTreeService.getObjById(CommUtil.null2Long(id));
			gameTree = (GameTree) wf.toPo(request, obj);
		}
		return JSONObject.toJSONString(new Result(5200, "Successfully"));
	}*/
	
}
