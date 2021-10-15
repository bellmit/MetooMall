package com.metoo.modul.app.game.manager.tree;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.WebForm;
import com.metoo.foundation.domain.Accessory;
import com.metoo.foundation.domain.GameAward;
import com.metoo.foundation.domain.SubTrees;
import com.metoo.foundation.domain.Tree;
import com.metoo.foundation.service.IAccessoryService;
import com.metoo.foundation.service.IGameAwardService;
import com.metoo.foundation.service.IGameService;
import com.metoo.foundation.service.ISubTreesService;
import com.metoo.foundation.service.ISubjectService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.ITreeService;
import com.metoo.module.app.buyer.domain.Result;

@Controller
@RequestMapping("php/game/subtree")
public class GameSubTreeManager {

	@Autowired
	private ISubTreesService subTreeService;
	@Autowired
	private IGameService gameService;
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IAccessoryService accessoryService;
	@Autowired
	private ITreeService treeService;
	@Autowired
	private IGameAwardService gameAwardService;

	@RequestMapping("query.json")
	@ResponseBody
	public Object query(HttpServletRequest request, HttpServletResponse response, String tree_id) {
		int code = 5200;
		String msg = "Successfully";
		Map map = new HashMap();
		Tree tree = this.treeService.getObjById(CommUtil.null2Long(tree_id));
		if (tree != null) {
			Map params = new HashMap();
			params.put("tree_id", tree.getId());
			List<SubTrees> subTrees = this.subTreeService
					.query("SELECT obj FROM SubTrees obj WHERE obj.tree.id=:tree_id", params, -1, -1);
			if (subTrees.size() > 0) {
				List<Map> list = new ArrayList<Map>();
				for (SubTrees obj : subTrees) {
					Map sub = new HashMap();
					sub.put("accessory",
							obj.getAccessory() == null ? null
									: this.configService.getSysConfig().getImageWebServer() + "/"
											+ obj.getAccessory().getPath() + "/" + obj.getAccessory().getName());
					sub.put("sub_id", obj.getId());
					sub.put("sub_water", obj.getWatering());
					sub.put("sub_status", obj.getStatus());
					sub.put("tree_name", obj.getTree().getName());
					if (obj.getGameAward() != null) {
						sub.put("award_water", obj.getGameAward().getWater());
						if (obj.getGameAward().getCoupon() != null) {
							sub.put("award_coupon_amount", obj.getGameAward().getCoupon().getCoupon_amount());
							sub.put("award_coupon_begin_time", obj.getGameAward().getCoupon().getCoupon_begin_time());
							sub.put("award_coupon_end_time", obj.getGameAward().getCoupon().getCoupon_end_time());
							sub.put("award_coupon_name", obj.getGameAward().getCoupon().getCoupon_name());
						}
					}
					list.add(sub);
				}
				map.put("subTrees", list);
			}
		} else {
			code = 5400;
			msg = "Object is null";
		}
		return new Result(5200, "Successfully", map);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping("/save.json")
	@ResponseBody
	public Object save(HttpServletRequest request, HttpServletResponse response, String id, String game_award_id,
			String tree_id) {
		SubTrees subTrees = null;
		WebForm wf = new WebForm();
		if (id.equals("")) {
			subTrees = wf.toPo(request, SubTrees.class);
			subTrees.setAddTime(new Date());
		} else {
			SubTrees obj = this.subTreeService.getObjById(CommUtil.null2Long(id));
			subTrees = (SubTrees) wf.toPo(request, obj);
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
				subTrees.setAccessory(accessory);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Tree tree = this.treeService.getObjById(CommUtil.null2Long(tree_id));
		if (tree != null) {
			subTrees.setTree(tree);
		}
		GameAward gameAward = this.gameAwardService.getObjById(CommUtil.null2Long(game_award_id));
		if (gameAward != null) {
			subTrees.setGameAward(gameAward);
		}
		if (id.equals("")) {
			this.subTreeService.save(subTrees);
		} else {
			this.subTreeService.update(subTrees);
		}
		return new Result(5200, "Successfully");
	}

	@RequestMapping("delete.json")
	@ResponseBody
	public Object remove(HttpServletRequest request, HttpServletResponse response, String id) {
		SubTrees subTrees = this.subTreeService.getObjById(CommUtil.null2Long(id));
		if (subTrees != null) {
			this.subTreeService.delete(subTrees.getId());
		}
		return new Result(5200, "Successfully");
	}

}
