package com.metoo.module.app.manage.buyer.action;

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

import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.ResponseUtils;
import com.metoo.foundation.domain.GameAwardInfo;
import com.metoo.foundation.domain.GameGoods;
import com.metoo.foundation.domain.GoodsVoucher;
import com.metoo.foundation.domain.LuckyDraw;
import com.metoo.foundation.domain.LuckyDrawAward;
import com.metoo.foundation.domain.LuckyDrawLog;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IGameAwardInfoService;
import com.metoo.foundation.service.IGameAwardRandomService;
import com.metoo.foundation.service.ILuckyDrawLogService;
import com.metoo.foundation.service.ILuckyDrawService;
import com.metoo.foundation.service.IUserService;
import com.metoo.modul.app.game.tree.tools.AppGameAwardTools;
import com.metoo.module.app.view.tools.AppGoodsVoucherTools;

/**
 * <p>
 * Title: AppLuckyDrawBuyerAction.java
 * </p>
 * 
 * <p>
 * Description: 用戶抽奖管理控制器，可免费获取随机奖品，包含种树所需 水滴、商品抵用券、商品碎片等
 * </p>
 * 
 * @author Administrator
 *
 */

@Controller
@RequestMapping("/app/v1/buyer")
public class AppLuckyDrawBuyerAction {

	@Autowired
	private ILuckyDrawService luckyDrawService;
	@Autowired
	private ILuckyDrawLogService luckyDrawLogService;
	@Autowired
	private IGameAwardRandomService gameAwardRamdomService;
	@Autowired
	private AppGameAwardTools appGameAwardTools;
	@Autowired
	private IUserService userService;
	@Autowired
	private AppGoodsVoucherTools appGoodsVoucherTools;
	@Autowired
	private IGameAwardInfoService gameAwardInfoService;

	@RequestMapping("/lucky_draw.json")
	@ResponseBody
	public Object lucky_draw(HttpServletRequest request, 
			HttpServletResponse response, String token){
		User user = CommUtil.verifyToken(token);
		if(user != null){
			//1, 查询抽奖系统设置信息
			Map params = new HashMap();
			params.put("switchs", 1);
			List<LuckyDraw> lucky_draw_list = this.luckyDrawService
					.query("SELECT obj FROM LuckyDraw obj WHERE obj.switchs=:switchs", params, -1, -1);
			if(lucky_draw_list.size() > 0){
				LuckyDraw luckyDraw = lucky_draw_list.get(0);
				//2, 查询用户可用免费抽奖次数
				params.clear();
				params.put("user_id", user.getId());
				params.put("addTime", CommUtil.dayStart());
				List<LuckyDrawLog> luckyDrawLogs = this.luckyDrawLogService
					.query("SELECT obj FROM LuckyDrawLog obj WHERE obj.user_id=:user_id AND obj.addTime>=:addTime", params, -1, -1);
				int num = new Double(CommUtil.subtract(luckyDraw.getNum(), luckyDrawLogs.size())).intValue() > 0 ?  new Double(CommUtil.subtract(luckyDraw.getNum(), luckyDrawLogs.size())).intValue() : 0;
				// 封装LucyDraw对象为json
				Map data = new HashMap();
				data.put("name", luckyDraw.getName());
				data.put("message", luckyDraw.getMessage());
				data.put("num", num);
				data.put("water", luckyDraw.getWater());
				data.put("drops_unused_water", user.getWater_drops_unused());
				if(luckyDraw.getLucky_draw_award().size() > 0){
					List<Map> maps = new ArrayList<Map>();
					for(LuckyDrawAward luckyDrawAward : luckyDraw.getLucky_draw_award()){
						Map map = new HashMap();
						map.put("id", luckyDrawAward.getId());
						map.put("name", luckyDrawAward.getName());
						map.put("message", luckyDrawAward.getMessage());
						map.put("photo", luckyDrawAward.getPhoto() != null 
								? luckyDrawAward.getPhoto().getPath() + "/" 
										+ luckyDrawAward.getPhoto().getName() : "");
						maps.add(map);
					}
					data.put("award", maps);
				}
				return ResponseUtils.ok(data);
				
			}
			return ResponseUtils.badArgument(4601, "The game is not opened");
		}
		return ResponseUtils.unlogin();
	}

	@RequestMapping("/start.json")
	@ResponseBody
	public Object start(String token){
		User user = CommUtil.verifyToken(token);
		if(user != null){
			Map params = new HashMap();
			params.put("switchs", 1);
			List<LuckyDraw> lucky_draw_list = this.luckyDrawService
					.query("SELECT obj FROM LuckyDraw obj WHERE obj.switchs=:switchs", params, -1, -1);
			if(lucky_draw_list.size() > 0){
				LuckyDraw luckyDraw = lucky_draw_list.get(0);
				if(luckyDraw != null && luckyDraw.getSwitchs() == 1){
					// 1, 查询剩余免费抽奖次数
					//2, 查询用户可用免费抽奖次数
					params.clear();
					params.put("user_id", user.getId());
					params.put("addTime", CommUtil.dayStart());
					List<LuckyDrawLog> luckyDrawLogs = this.luckyDrawLogService
						.query("SELECT obj FROM LuckyDrawLog obj WHERE obj.user_id=:user_id AND obj.addTime>=:addTime", params, -1, -1);
					int num = new Double(CommUtil.subtract(luckyDraw.getNum(), luckyDrawLogs.size())).intValue() - 1;
					int water = 0;
					if(num < 0){
						water = user.getWater_drops_unused() - luckyDraw.getWater() >= 0 ?  luckyDraw.getWater() : 0;
						user.setWater_drops_unused(user.getWater_drops_unused() + luckyDraw.getWater());
						this.userService.update(user);
					}
					if(num >= 0 || water > 0){
						LuckyDrawAward lucky_draw_award = this.gameAwardRamdomService.lucky_draw_award(luckyDraw.getId());
						if(lucky_draw_award != null){
							Map data = new HashMap();
							data.put("id", lucky_draw_award.getId());
							// 2, 根据奖品类型给用户添加奖品
							boolean flag = false;
							String photo = lucky_draw_award.getPhoto() != null ? lucky_draw_award.getPhoto().getPath()
													+ "/" 
													+ lucky_draw_award.getPhoto().getName() : "";
							if(lucky_draw_award.getType() == 1){
								GoodsVoucher goods_voucher = lucky_draw_award.getGoods_voucher();
								if(goods_voucher != null){
									flag = this.appGameAwardTools.getVoucher(goods_voucher, user);
									// 2-1: 记录用户抵用券获取记录
									this.appGoodsVoucherTools.getVoucher(goods_voucher, user);
									String message = "Lottery rewards";
									// 2-2: 记录用户抵用券获取记录
									this.appGoodsVoucherTools.createLog(goods_voucher, user, 5, 0, message, null);
								}
							}else if(lucky_draw_award.getType() == 2){
								user.setWater_drops_unused(user.getWater_drops_unused() + lucky_draw_award.getWater());
								this.userService.update(user);
								flag = true;
							}else if(lucky_draw_award.getType() == 3){
								// 随机产生一个碎片
								GameGoods game_goods = this.gameAwardRamdomService.lucky_draw_goods(lucky_draw_award.getId());
								if(game_goods != null){
									GameAwardInfo gameAwardInfo = new GameAwardInfo();
									gameAwardInfo.setAddTime(new Date());
									gameAwardInfo.setUser(user);
									gameAwardInfo.setStatus(1);
									gameAwardInfo.setGameGoods(game_goods);
//									gameAwardInfo.setAward(Json.toJson(award));
									this.gameAwardInfoService.save(gameAwardInfo);
									flag = true;
									photo = game_goods.getAccessory() != null ? game_goods.getAccessory() : "";
								}
							}else if(lucky_draw_award.getType() == 0){
								flag = true;
							}
							if(flag){
								
								data.put("photo", photo);
								data.put("num", num < 0 ? 0 : num);
								data.put("water", user.getWater_drops_unused());
								// 记录抽奖日志
								LuckyDrawLog lucky_draw_log = new LuckyDrawLog();
								lucky_draw_log.setAddTime(new Date());
								lucky_draw_log.setUser_id(user.getId());
								lucky_draw_log.setUser_name(user.getUserName());
								lucky_draw_log.setName(lucky_draw_award.getName());
								lucky_draw_log.setMessage(lucky_draw_award.getMessage());
								this.luckyDrawLogService.save(lucky_draw_log);
								
								return ResponseUtils.ok(data);
							}
							return ResponseUtils.badArgument(4615, "Prize claim failure");
						}
					}
					return ResponseUtils.badArgument(4616, "Not enough water droplets");
				}
			}
			return ResponseUtils.badArgument(4601, "The game is not opened");
		}
		return ResponseUtils.unlogin();
	}
}
