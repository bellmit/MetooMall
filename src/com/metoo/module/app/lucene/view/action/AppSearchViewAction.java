package com.metoo.module.app.lucene.view.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.metoo.core.language.Google;
import com.metoo.core.mv.JModelAndView;
import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.Goods;
import com.metoo.foundation.domain.GoodsClass;
import com.metoo.foundation.domain.Store;
import com.metoo.foundation.domain.SysConfig;
import com.metoo.foundation.service.IGoodsClassService;
import com.metoo.foundation.service.IGoodsService;
import com.metoo.foundation.service.IStoreService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.IUserConfigService;
import com.metoo.lucene.LuceneResult;
import com.metoo.lucene.LuceneUtil;
import com.metoo.lucene.LuceneVo;
import com.metoo.module.app.buyer.domain.Result;
import com.metoo.module.app.manage.buyer.action.AppSearchKeywordAction;
import com.metoo.module.app.manage.buyer.tool.AppSearchKeywordTools;
import com.metoo.view.web.tools.GoodsClassViewTools;
import com.metoo.view.web.tools.GoodsViewTools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/app/")
public class AppSearchViewAction {

	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private IGoodsClassService goodsClassService;
	@Autowired
	private IUserConfigService userConfigService;
	@Autowired
	private GoodsClassViewTools gcTools;
	@Autowired
	private GoodsViewTools goodsTools;
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private AppSearchKeywordTools appSearchKeywordTools;

	@RequestMapping("/v1/hot_search.json")
	public void search_hot(HttpServletRequest request, HttpServletResponse response) {
		Map searchMap = new HashMap();
		SysConfig sysc = this.configService.getSysConfig();
		int code = 1;
		List strList = null;
		if (sysc.getHotSearch() != null && !sysc.getHotSearch().equals("")) {
			String[] str = CommUtil.splitByChar(sysc.getHotSearch(), ",");
			strList = new ArrayList();
			for (String string : str) {
				strList.add(string);
			}
			code = 4200;
		}
		Result result = new Result(code, strList);
		String search_temp = Json.toJson(result, JsonFormat.compact());
		try {
			response.setContentType("application/json;charset=UTF-8");	
			response.getWriter().print(search_temp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param gc_id
	 * @param currentPage
	 * @param orderBy
	 * @param orderType
	 * @param goods_type
	 * @param goods_inventory
	 * @param keyword
	 * @param goods_transfee
	 * @param goods_cod
	 * @param searchType
	 * @param store_id
	 * @param language
	 * @throws UnsupportedEncodingException
	 * @description App 商品搜索；仅用于搜索商品搜索
	 */
	@RequestMapping("v1/search.json")
	public void wapsearchV2(HttpServletRequest request, HttpServletResponse response, String gc_id, String currentPage,
			String orderBy, String orderType, String goods_type, String goods_inventory, String keyword,
			String goods_transfee, String goods_cod, String searchType, String store_id, String language)
			throws UnsupportedEncodingException {
		if (goods_type.equals("")) {
			goods_type = "1";
		}
		Result result = null;
		Map map = new HashMap();
		if (keyword != null && !keyword.equals("")) {
			Google google = new Google("https://translation.googleapis.com/language/translate/v2/detect",
					"AIzaSyDC4pcWmrtp8flJd4iHULadvOszOVh57Io");
			String json = google.detect(keyword);
			JSONObject jSONObject = JSONObject.fromObject(json);
			JSONObject data = (JSONObject) jSONObject.get("data");
			JSONArray detections = (JSONArray) data.get("detections");
			JSONArray index = (JSONArray) detections.get(0);
			JSONObject index1 = (JSONObject) index.get(0);
			String lau = (String) index1.get("language");
			if (lau.equals("ar")) {
				// 查询是否有记录
				keyword = appSearchKeywordTools.getEnWord(keyword, lau);
			}
			String path = System.getProperty("metoob2b2c.root") + File.separator + "luence" + File.separator + "goods";
			LuceneUtil lucene = LuceneUtil.instance();
			lucene.setIndex_path(path);
			List temp_list = this.goodsClassService.query("select obj.id from GoodsClass obj", null, -1, -1);
			lucene.setGc_size(temp_list.size());
			boolean order_type = true;
			String order_by = "";
			Sort sort = null;
			String query_gc = "";
			if (orderBy == null || orderBy.equals("")) {
				order_by = "goods_weightiness";
				sort = new Sort(new SortField(order_by, SortField.Type.DOUBLE, order_type));
			}
			if (CommUtil.null2String(orderBy).equals("goods_salenum")) {
				order_by = "goods_salenum";
				sort = new Sort(new SortField(order_by, SortField.Type.INT, order_type));
			}
			if (CommUtil.null2String(orderBy).equals("evaluate_count")) {
				order_by = "evaluate_count";
				sort = new Sort(new SortField(order_by, SortField.Type.INT, order_type));
			}
			if (CommUtil.null2String(orderBy).equals("goods_collect")) {
				order_by = "goods_collect";
				sort = new Sort(new SortField(order_by, SortField.Type.INT, order_type));
			}
			if (CommUtil.null2String(orderBy).equals("well_evaluate")) {
				order_by = "well_evaluate";
				sort = new Sort(new SortField(order_by, SortField.Type.DOUBLE, order_type));
			}
			if (CommUtil.null2String(orderType).equals("")) {
				orderType = "desc";
			}
			if (CommUtil.null2String(orderType).equals("asc")) {
				order_type = false;
			}
			if (CommUtil.null2String(orderBy).equals("goods_current_price")) {
				order_by = "curr_price";
				sort = new Sort(new SortField(order_by, SortField.Type.DOUBLE, order_type));
			}

			if (CommUtil.null2String(orderBy).equals("addTime")) {
				order_by = "add_time";
				sort = new Sort(new SortField(order_by, SortField.Type.DOUBLE, order_type));
			}
			if (CommUtil.null2String(orderBy).equals("goods_discount_rate")) {
				order_by = "goods_rate";
				sort = new Sort(new SortField(order_by, SortField.Type.STRING, order_type));
			}
			List goods_class_ids = null;
			Set set = new HashSet();
			if (gc_id != null && !gc_id.equals("")) {
				List<Long> long_ids = new ArrayList<Long>();
				String[] class_ids = gc_id.split(",");
				for (String id : class_ids) {
					set.addAll(this.genericIds(CommUtil.null2Long(id)));
				}
				Map params = new HashMap();
				params.put("ids", set);
				goods_class_ids = this.goodsClassService
						.query("select obj.id from GoodsClass obj where obj.id not in(:ids)", params, -1, -1);
			}
			LuceneResult pList = null;
			if (sort != null) {
				pList = lucene.appserch(keyword, CommUtil.null2Int(currentPage), goods_inventory, goods_type,
						goods_class_ids, goods_transfee, goods_cod, sort, null, null, null, store_id);
			} else {
				pList = lucene.appserch(keyword, CommUtil.null2Int(currentPage), goods_inventory, goods_type,
						goods_class_ids, goods_transfee, goods_cod, sort, null, null, null, store_id);
			}
			List<LuceneVo> luceneVos = pList.getVo_list();
			List<Map<String, Object>> luceneList = new ArrayList<Map<String, Object>>();
			for (LuceneVo lucenevo : luceneVos) {
				Goods obj = this.goodsService.getObjById(CommUtil.null2Long(lucenevo.getVo_id()));
				if (obj.getGoods_status() == 0 && obj.getGoods_store().getStore_status() == 15) {
					Map<String, Object> goodsMap = new HashMap<String, Object>();
					goodsMap.put("goods_id", lucenevo.getVo_id());
					goodsMap.put("goods_name", lucenevo.getVo_title());
					if ("1".equals(language)) {
						goodsMap.put("goods_name",
								obj.getKsa_goods_name() != null && !"".equals(obj.getKsa_goods_name())
										? obj.getKsa_goods_name() : obj.getGoods_name());
					}
					goodsMap.put("goods_inventory", lucenevo.getVo_goods_inventory());
					goodsMap.put("vo_well_evaluate",
							lucenevo.getVo_well_evaluate() == 0.0 ? 0 : lucenevo.getVo_well_evaluate());
					goodsMap.put("goods_price", lucenevo.getVo_cost_price());
					goodsMap.put("goods_current_price", lucenevo.getVo_curr_price());
					goodsMap.put("store_price", lucenevo.getVo_store_price());
					goodsMap.put("goods_img", this.configService.getSysConfig().getImageWebServer() + "/"
							+ lucenevo.getVo_main_photo_url());
					goodsMap.put("store_name", lucenevo.getVo_store_username());
					goodsMap.put("goods_serial", lucenevo.getVo_goods_serial());
					goodsMap.put("goods_collect", lucenevo.getVo_goods_collect());
					goodsMap.put("store_id", lucenevo.getVo_store_id());
					goodsMap.put("goods_discount_rate", lucenevo.getVo_rate());
					goodsMap.put("goods_class", lucenevo.getVo_goods_class());
					goodsMap.put("goods_weightiness", lucenevo.getVo_goods_weightiness());
					luceneList.add(goodsMap);
				}
			}
			map.put("lucen", luceneList);
			map.put("goods_currentPage", pList.getCurrentPage());
			map.put("goods_Pages", pList.getPages());
			// 对关键字命中的商品进行分类提取
			/*
			 * Set<String> list_gcs = lucene.LoadData_goods_class(keyword); //
			 * 对商品分类数据进行分析加载,只查询id和className List<GoodsClass> gcs =
			 * this.query_GC_second(list_gcs);
			 * 
			 * mv.addObject("list_gc", list_gcs); mv.addObject("gcs", gcs);
			 * mv.addObject("allCount", pList.getRows());
			 */
		}
		// 加载页面上其它的商品信息，最近浏览，猜你喜欢，推广热卖，直通车。
		/*
		 * this.search_other_goods(mv); // 处理系统商品对比信息 List<Goods>
		 * goods_compare_list = (List<Goods>) request
		 * .getSession(false).getAttribute("goods_compare_cart"); //
		 * 计算商品对比中第一间商品的分类，只允许对比同一个分类的商品 if (goods_compare_list == null) {
		 * goods_compare_list = new ArrayList<Goods>(); } int compare_goods_flag
		 * = 0;// 默认允许对比商品，如果商品分类不一致曾不允许对比 for (Goods compare_goods :
		 * goods_compare_list) { if (compare_goods != null) { compare_goods =
		 * this.goodsService.getObjById(compare_goods .getId()); if
		 * (!compare_goods.getGc().getParent().getParent().getId()
		 * .equals(CommUtil.null2Long(gc_id))) { compare_goods_flag = 1; } } }
		 */
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().print(Json.toJson(new Result(4200, "Suucess", map), JsonFormat.compact()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 根据店铺SEO关键字，查出关键字命中的店铺
	 * 
	 * @param keyword
	 * @return
	 */
	public List<Store> search_stores_seo(String keyword) {
		Map params = new HashMap();
		params.put("keyword1", keyword);
		params.put("keyword2", keyword + ",%");
		params.put("keyword3", "%," + keyword + ",%");
		params.put("keyword4", "%," + keyword);
		List<Store> stores = this.storeService.query(
				"select obj from Store obj where obj.store_seo_keywords =:keyword1 or obj.store_seo_keywords like:keyword2 or obj.store_seo_keywords like:keyword3 or obj.store_seo_keywords like:keyword4",
				params, 0, 3);
		Collections.sort(stores, new Comparator() {
			public int compare(Object o1, Object o2) {
				Store store1 = (Store) o1;
				Store store2 = (Store) o2;
				int l1 = store1.getStore_seo_keywords().split(",").length;
				int l2 = store2.getStore_seo_keywords().split(",").length;
				if (l1 > l2) {
					return 1;
				}
				;
				if (l1 == l2) {
					if (store1.getPoint().getStore_evaluate().compareTo(store2.getPoint().getStore_evaluate()) == 1) {
						return -1;
					}
					;
					if (store1.getPoint().getStore_evaluate().compareTo(store2.getPoint().getStore_evaluate()) == -1) {
						return 1;
					}
					;
					return 0;
				}
				return -1;
			}
		});
		return stores;
	}

	/**
	 * 得到一个存有搜索数据的Cookie
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Cookie search_history_cookie(HttpServletRequest request, String keyword) {
		String str = "";
		Cookie[] cookies = request.getCookies();
		Cookie search_cookie = null;
		try {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("search_history")) {
					String str_temp = URLDecoder.decode(cookie.getValue(), "UTF-8");
					for (String s : str_temp.split(",")) {
						if (!s.equals(keyword) && !str.equals("")) {
							str = str + "," + s;
						} else if (!s.equals(keyword)) {
							str = s;
						}
					}
					break;
				}
				;
			}
			if (str.equals("")) {
				str = keyword;
				str = URLEncoder.encode(str, "UTF-8");
				search_cookie = new Cookie("search_history", str);
			} else {
				str = keyword + "," + str;
				str = URLEncoder.encode(str, "UTF-8");
				search_cookie = new Cookie("search_history", str);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return search_cookie;
	}

	private Set<Long> genericIds(Long id) {
		Set<Long> ids = new HashSet<Long>();
		if (id != null) {
			ids.add(id);
			Map params = new HashMap();
			params.put("pid", id);
			List id_list = this.goodsClassService.query("select obj.id from GoodsClass obj where obj.parent.id=:pid",
					params, -1, -1);
			ids.addAll(id_list);
			for (int i = 0; i < id_list.size(); i++) {
				Long cid = CommUtil.null2Long(id_list.get(i));
				Set<Long> cids = genericIds(cid);
				ids.add(cid);
				ids.addAll(cids);
			}
		}
		return ids;
	}
}
