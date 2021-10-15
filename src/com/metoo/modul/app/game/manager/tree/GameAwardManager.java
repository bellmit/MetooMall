package com.metoo.modul.app.game.manager.tree;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.Json;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.WebForm;
import com.metoo.foundation.domain.GameAward;
import com.metoo.foundation.domain.GameTask;
import com.metoo.foundation.domain.Goods;
import com.metoo.foundation.domain.GoodsVoucher;
import com.metoo.foundation.domain.SubTrees;
import com.metoo.foundation.service.IGameAwardService;
import com.metoo.foundation.service.IGameTaskService;
import com.metoo.foundation.service.IGoodsService;
import com.metoo.foundation.service.IGoodsVoucherService;
import com.metoo.foundation.service.ISubTreesService;
import com.metoo.modul.app.game.tree.dto.GameAwardDto;
import com.metoo.module.app.buyer.domain.Result;

/**
 * 定义每个环节的奖品
 * <p>
 * Title: AppGameAwardViewAction.java
 * </p>
 * 
 * <p>
 * Description: 设置活动奖品
 * </p>
 * 
 * @author 46075
 *
 */
@Controller
@RequestMapping("/php/game/award")
public class GameAwardManager {

	@Autowired
	private IGameAwardService gameAwardService;
	@Autowired
	private IGameTaskService gameTaskService;
	@Autowired
	private ISubTreesService subTreeService;
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IGoodsVoucherService goodsVoucherService;

	@RequestMapping("query.json")
	@ResponseBody
	public Object query(HttpServletRequest request, HttpServletResponse response) {
		Map params = new HashMap();
		params.put("type", 0);
		List<GameAward> gameAwards = this.gameAwardService.query("SELECT obj FROM GameAward obj WHERE obj.type=:type",
				params, -1, -1);
		return new Result(5200, "Successfully", gameAwards);

	}

	/**
	 * 种树游戏 奖品 保存/更新
	 * @param request
	 * @param response
	 * @param id
	 * @param tree_id
	 * @return
	 */
	@RequestMapping("/save.json")
	@ResponseBody
	public Object save(HttpServletRequest request, HttpServletResponse response, String id,
			String tree_id, String voucher_id, String goods_info) {
		GameAward gameAward = null;
		WebForm wf = new WebForm();
		if (id.equals("")) {
			gameAward = wf.toPo(request, GameAward.class);
			gameAward.setAddTime(new Date());
		} else {
			GameAward obj = this.gameAwardService.getObjById(CommUtil.null2Long(id));
			gameAward = (GameAward) wf.toPo(request, obj);
		}

		SubTrees subTree = this.subTreeService.getObjById(CommUtil.null2Long(tree_id));
		if(subTree != null && subTree.getTree() != null){
			gameAward.setSubTree(subTree);
		}
		if(voucher_id != null && !voucher_id.equals("")){
			List<GoodsVoucher> goodsVouchers = new ArrayList<GoodsVoucher>();
			String[] voucher_ids = voucher_id.split(",");
			for(String v_id : voucher_ids){
				GoodsVoucher goodsVoucher = this.goodsVoucherService.getObjById(Long.parseLong(v_id));
				if(goodsVoucher != null){
					goodsVouchers.add(goodsVoucher);
				}
			}
			gameAward.setGoodsVouchers(goodsVouchers);
		}
		
/*		if(goods_info != null && !goods_info.equals("")){
			Map goods_map = Json.fromJson(Map.class,
					goods_info);
			List<Map> list = resolver(goods_info);
			for(Map map : list){
				if(map.get("goods_id") != null){
					Goods goods = this.goodsService.getObjById(CommUtil.null2Long(map.get("goods_id")));
					if(goods != null){
						
					}
				}
			}
			
		}*/
		if (id.equals("")) {
			this.gameAwardService.save(gameAward);
		} else {
			this.gameAwardService.update(gameAward);
		}
		return new Result(5200, "Successfully");
	}
	
	
	public List<Map> resolver(String json) {

		List<Map> map_list = new ArrayList<Map>();
		if (json != null && !json.equals("")) {
			map_list = Json.fromJson(ArrayList.class, json);
		}
		return map_list;
	}
	
	@RequestMapping("/save1.json")
	@ResponseBody
	public Object save1(GameAwardDto dto) {
		GameAward gameAward = null;
		WebForm wf = new WebForm();
		if (dto.getId() == null || dto.getId().equals("")) {
			/*gameAward = wf.toPo(request, GameAward.class);*/
			gameAward = new GameAward();
			BeanUtils.copyProperties(dto, gameAward);
			gameAward.setAddTime(new Date());
		} else {
			gameAward = this.gameAwardService.getObjById(CommUtil.null2Long(dto.getId()));
			/*gameAward = (GameAward) wf.toPo(request, obj);*/
			BeanUtils.copyProperties(dto, gameAward);
		}

		SubTrees subTree = this.subTreeService.getObjById(dto.getSubTree_id());
		if(subTree != null && subTree.getTree() != null){
			gameAward.setSubTree(subTree);
		}
		Goods goods = this.goodsService.getObjById(dto.getGoods_id());
		if(goods != null){
			gameAward.setGoods(goods);
		}
		if (dto.getId() == null || dto.getId().equals("")) {
			this.gameAwardService.save(gameAward);
		} else {
			this.gameAwardService.update(gameAward);
		}
		return new Result(5200, "Successfully");
	}
	
	/**
	 * 删除奖品
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping("delete.json")
	@ResponseBody
	public Object remove(HttpServletRequest request, HttpServletResponse response, String id) {
		GameAward gameAward = this.gameAwardService.getObjById(CommUtil.null2Long(id));
		if (gameAward != null) {
			this.gameAwardService.delete(gameAward.getId());
		}
		return new Result(5200, "Successfully");
	}

}
