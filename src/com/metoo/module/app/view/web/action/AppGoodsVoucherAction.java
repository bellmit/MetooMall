package com.metoo.module.app.view.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.metoo.core.domain.virtual.SysMap;
import com.metoo.core.mv.JModelAndView;
import com.metoo.core.query.support.IPageList;
import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.ResponseUtils;
import com.metoo.foundation.domain.Address;
import com.metoo.foundation.domain.GoodsVoucherInfo;
import com.metoo.foundation.domain.GoodsVoucherLog;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.domain.query.GoodsQueryObject;
import com.metoo.foundation.domain.query.GoodsVoucherLogQueryObject;
import com.metoo.foundation.service.IGoodsVoucherInfoService;
import com.metoo.foundation.service.IGoodsVoucherLogService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.IUserConfigService;
import com.metoo.foundation.service.IUserService;

@Controller
@RequestMapping("/app/v1/goods_voucher")
public class AppGoodsVoucherAction {
	
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserConfigService userConfigService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IGoodsVoucherInfoService goodsVoucherInfoService;
	@Autowired
	private IGoodsVoucherLogService goodsVoucherLogService;

	/**
	 * 查询用户低佣金总额和抵用券获取使用记录数量
	 * @param token
	 * @return
	 */
	@RequestMapping("/list.json")
	@ResponseBody
	public Object goodsVoucher(String token){
		if(token != null && !token.equals("")){
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if(user != null){
				Map params = new HashMap();
				params.put("user_id", user.getId());
				params.put("status", 0);
				List<GoodsVoucherInfo> goodsVouchers = this.goodsVoucherInfoService
						.query("select obj from GoodsVoucherInfo obj where obj.user.id=:user_id "
								+ "and obj.status=:status", params, -1, -1);
				//1,查询抵用券总金额
				double price = 0;
				for(GoodsVoucherInfo obj : goodsVouchers){
					price = CommUtil.add(price, obj.getGoods_voucher().getNumber());
				}
				Map data = new HashMap();
				data.put("price", price);
				//2, 查询抵用券获取总数量（未读）
				params.clear();
				params.put("user_id", user.getId());
				params.put("log_read", 0);
				List<GoodsVoucherLog> goodsVoucherLogs = this.goodsVoucherLogService
						.query("SELECT new GoodsVoucherLog(id, addTime, status, log_read, price, user_id) "
								+ "FROM GoodsVoucherLog obj WHERE obj.user_id=:user_id AND obj.log_read=:log_read ORDER BY obj.addTime DESC", params, -1, -1);
				data.put("read", goodsVoucherLogs.size());
				return ResponseUtils.ok(data);
			}
		}
		return ResponseUtils.unlogin();
	}
	
	/**
	 * 查询抵用券获取/使用记录
	 * @param request
	 * @param response
	 * @param token
	 * @return
	 */
	@RequestMapping("/log.json")
	@ResponseBody
	public Object log(HttpServletRequest request, HttpServletResponse response,String currentPage, String orderBy, String orderType, String token){
		if(token != null && !token.equals("")){
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if(user != null){
				Map data = new HashMap();
				// 1，查询低佣金获取/使用记录
				ModelAndView mv = new JModelAndView("",
						configService.getSysConfig(),this.userConfigService.getUserConfig(), 0, request, response);
				GoodsVoucherLogQueryObject qo = new GoodsVoucherLogQueryObject(currentPage, mv, orderBy,
						orderType);
				qo.addQuery("obj.user_id", new SysMap("user_id",
						user.getId()), "=");
				IPageList pList = this.goodsVoucherLogService.list(qo);
				
				data.put("data", pList.getResult());
				// 2,查询未读记录更新为已读
				Map params = new HashMap();
				params.put("user_id", user.getId());
				params.put("log_read", 0);
				List<GoodsVoucherLog> goodsVoucherLogs = this.goodsVoucherLogService
						.query("SELECT obj FROM GoodsVoucherLog obj WHERE obj.user_id=:user_id AND obj.log_read=:log_read", params, -1, -1);
				if(goodsVoucherLogs.size() > 0){
					for(GoodsVoucherLog log : goodsVoucherLogs){
						log.setLog_read(1);
						this.goodsVoucherLogService.update(log);
					}
				}
				List<Map> maps = new ArrayList<Map>(); 
				if(pList.getResult().size() > 0 ){
					List<GoodsVoucherLog> GoodsVoucherLogs = pList.getResult();
					for(GoodsVoucherLog log : GoodsVoucherLogs){
						Map map = new HashMap();
						map.put("addTime", log.getAddTime());
						map.put("status", log.getStatus());
						map.put("log_read", log.getLog_read());
						map.put("type", log.getType());
						map.put("price", log.getPrice());
						map.put("message", log.getMessage());
						maps.add(map);
					}
				}
				return ResponseUtils.ok(maps);
			}
		}
		return ResponseUtils.unlogin();
	}
}
