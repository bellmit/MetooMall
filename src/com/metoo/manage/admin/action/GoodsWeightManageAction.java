package com.metoo.manage.admin.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
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
import org.springframework.web.servlet.ModelAndView;
import com.metoo.core.beans.BeanUtils;
import com.metoo.core.beans.BeanWrapper;
import com.metoo.core.domain.virtual.SysMap;
import com.metoo.core.mv.JModelAndView;
import com.metoo.core.query.support.IPageList;
import com.metoo.core.security.support.SecurityUserHolder;
import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.WebForm;
import com.metoo.foundation.domain.Address;
import com.metoo.foundation.domain.Goods;
import com.metoo.foundation.domain.GoodsWeight;
import com.metoo.foundation.domain.Store;
import com.metoo.foundation.domain.query.GoodsWeightQueryObject;
import com.metoo.foundation.service.IGoodsService;
import com.metoo.foundation.service.IGoodsWeightService;
import com.metoo.foundation.service.IStoreService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.IUserConfigService;
import com.metoo.module.app.view.web.thread.GoodsThread;
import com.metoo.module.app.view.web.tool.AppGoodsThreadsUtil;

@Controller
public class GoodsWeightManageAction {

	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserConfigService userConfigService;
	@Autowired
	private IGoodsWeightService goodsWeightService;
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IStoreService storeService;

	@RequestMapping("/admin/goods_weight.htm")
	public ModelAndView getDimensionality(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("admin/blue/goods_weight.html", configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		GoodsWeightQueryObject gwq = new GoodsWeightQueryObject();
		// gwq.addQuery("display", new SysMap("display", true), "=");
		IPageList pList = this.goodsWeightService.list(gwq);
		CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
		return mv;
	}

	@RequestMapping("/admin/goods_weight_add.htm")
	public ModelAndView goods_weight_add(HttpServletRequest request, HttpServletResponse response, String id,
			String currentPage) {
		ModelAndView mv = new JModelAndView("admin/blue/goods_weight_add.html", configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		return mv;
	}

	@RequestMapping("/admin/goods_weight_save.htm")
	public String goods_weight_save(HttpServletRequest request, HttpServletResponse response, String id,
			String dimensionality, String value, String currentPage) {
		WebForm wf = new WebForm();
		GoodsWeight goodsWeight = null;
		if (id == null || id.equals("")) {
			goodsWeight = wf.toPo(request, GoodsWeight.class);
			goodsWeight.setAddTime(new Date());
		} else {
			goodsWeight = this.goodsWeightService.getObjById(CommUtil.null2Long(id));
		}
		goodsWeight.setDimensionality(dimensionality);
		goodsWeight.setValue(CommUtil.null2BigDecimal(value));
		goodsWeight.setDisplay(true);
		if (id != null && id.equals("")) {
			this.goodsWeightService.save(goodsWeight);
		} else
			this.goodsWeightService.update(goodsWeight);
		return "redirect:/admin/goods_weight.htm?currentPage=" + currentPage;
	}

	@RequestMapping("/admin/goods_weight_ajax.htm")
	public void goods_weight_ajax(HttpServletRequest request, HttpServletResponse response, String id, String fieldName,
			String value) throws ClassNotFoundException {
		GoodsWeight obj = this.goodsWeightService.getObjById(Long.parseLong(id));
		Field[] fields = GoodsWeight.class.getDeclaredFields();
		BeanWrapper wrapper = new BeanWrapper(obj);
		Object val = null;
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				Class clz = Class.forName("java.lang.String");
				if (field.getType().getName().equals("java.math.BigDecimal")) {
					clz = Class.forName("java.math.BigDecimal");
				}
				if (field.getType().getName().equals("boolean")) {
					clz = Class.forName("java.lang.Boolean");
				}
				if (!value.equals("")) {
					val = BeanUtils.convertType(value, clz);
				} else {
					val = !CommUtil.null2Boolean(wrapper.getPropertyValue(fieldName));
				}
				wrapper.setPropertyValue(fieldName, val);
			}
		}
		this.goodsWeightService.update(obj);
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.print(val.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/admin/goods_weight_update.htm")
	@ResponseBody
	public String goods_weight_update(HttpServletRequest request, HttpServletResponse response)
			throws IllegalArgumentException, IllegalAccessException {
		Map params = new HashMap();
		params.put("store_status", 15);
		List<Store> stores = this.storeService.query("select obj from Store obj where obj.store_status=:store_status", params, -1, -1);
		params.clear();
		params.put("display", true);
		List<GoodsWeight> goodsWeights = this.goodsWeightService
				.query("select obj from GoodsWeight obj where obj.display=:display", params, -1, -1);
		AppGoodsThreadsUtil instance = AppGoodsThreadsUtil.getInstance();
		instance.setGoodsWeightList(goodsWeights);
		for(Store store : stores){
			params.clear();
			params.put("goods_status", 0);
			params.put("store_id", store.getId());
			List<Goods> goodsList = this.goodsService.query(
					"select obj from Goods obj where obj.goods_status=:goods_status and obj.goods_store.id=:store_id",
					params, -1, -1);
			GoodsThread goodsThread = new GoodsThread(goodsList);
			goodsThread.run();
		}
		return "seccuess";
	}

	
}
