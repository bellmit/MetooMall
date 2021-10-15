package com.metoo.module.app.manage.buyer.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.metoo.core.annotation.ApiVersion;
import com.metoo.core.annotation.SecurityMapping;
import com.metoo.core.domain.virtual.SysMap;
import com.metoo.core.mv.JModelAndView;
import com.metoo.core.query.support.IPageList;
import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.WebForm;
import com.metoo.foundation.domain.Address;
import com.metoo.foundation.domain.Area;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.domain.query.AddressQueryObject;
import com.metoo.foundation.service.IAddressService;
import com.metoo.foundation.service.IAreaService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.IUserConfigService;
import com.metoo.foundation.service.IUserService;
import com.metoo.module.app.buyer.domain.Result;
import com.metoo.module.app.view.web.tool.AppobileTools;

@Controller
@RequestMapping("/app/")
public class AppAddressBuyerAction {
	@Autowired
	private IAddressService addressService;
	@Autowired
	private IAreaService areaService;
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserConfigService userConfigService;
	@Autowired
	private IUserService userService;
	@Autowired
	private AppobileTools mobileTools;

	/**
	 * Address列表页
	 * 
	 * @param currentPage
	 * @param orderBy
	 * @param orderType
	 * @param request
	 * @return
	 */
	@SecurityMapping(title = "收货地址列表", value = "/buyer/address.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping(value = "v1/addressList.json", method = RequestMethod.POST)
	public void address(HttpServletRequest request, HttpServletResponse response, String currentPage, String token) {
		Result result = null;
		Map<String, Object> map = new HashMap<String, Object>();
		ModelAndView mv = new JModelAndView("user/default/usercenter/address.html", configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		if (token.equals("")) {
			result = new Result(-100, "token Invalidation");
		} else {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user == null) {
				result = new Result(-100, "token Invalidation");
			} else {
				String url = this.configService.getSysConfig().getAddress();
				if (url == null || url.equals("")) {
					url = CommUtil.getURL(request);
				}
				AddressQueryObject qo = new AddressQueryObject(currentPage, mv, "default_val desc,obj.addTime", "asc");
				qo.addQuery("obj.user.id", new SysMap("user_id", user.getId()), "=");// SecurityUserHolder.getCurrentUser().getId()
				IPageList pList = this.addressService.list(qo);
				List<Map> addressList = CommUtil.saveIPageList2ModelAndView2(pList);
				map.put("addresList", addressList);
				List<Area> areas = this.areaService.query("select obj from Area obj where obj.parent.id is null", null,
						-1, -1);
				List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>();
				for (Area area : areas) {
					Map<String, Object> areaMap = new HashMap<String, Object>();
					areaMap.put("id", area.getId());
					areaMap.put("areaName", area.getAreaName());
					areaList.add(areaMap);
					map.put("areaMap", areaList);
				}
				result = new Result(4200, "Success", map);
			}
		}

		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	@SecurityMapping(title = "新增收货地址", value = "/buyer/address_add.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping(value = "v1/addressAdd", method = RequestMethod.POST)
	public void addressAdd(HttpServletRequest request, HttpServletResponse response, String currentPage) {

		Result result = null;
		List<Area> areas = this.areaService.query("select obj from Area obj where obj.parent.id is null", null, -1, -1);
		Map<String, Object> areamap = new HashMap<String, Object>();
		List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>();
		for (Area area : areas) {
			Map<String, Object> areaMap = new HashMap<String, Object>();
			areaMap.put("areaId", area.getId());
			areaMap.put("areaName", area.getAreaName());
			areaList.add(areaMap);
		}
		areamap.put("areaMap", areaList);
		areamap.put("currentPage", currentPage);
		result = new Result(4200, "Success", areamap);
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @param currentPage
	 * @param token
	 */
	@SecurityMapping(title = "编辑收货地址", value = "/buyer/address_edit.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping(value = "v1/addressEdit.json", method = RequestMethod.POST)
	public void addressEdit(HttpServletRequest request, HttpServletResponse response, String id, String currentPage,
			String token) {
		int code = -1;
		String msg = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (token.equals("")) {
			code = -100;
			msg = "token Invalidation";
		} else {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user == null) {
				code = -100;
				msg = "token Invalidation";
			} else {
				Address address = this.addressService.getObjById(CommUtil.null2Long(id));
				if (address != null && address.getUser().getId().equals(user.getId())) {
					List<Area> areas = this.areaService.query("select obj from Area obj where obj.parent.id is null",
							null, -1, -1);
					List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>();
					for (Area obj : areas) {
						Map<String, Object> areaMap = new HashMap<String, Object>();
						areaMap.put("areaId", obj.getId());
						areaMap.put("areaName", obj.getAreaName());
						areaList.add(areaMap);
					}
					// 国家：country 城市：city 地区： area
					map.put("areaMap", areaList);
					map.put("currentPage", currentPage);
					String country ="";
					String city = "";
					String area = "";
					Area obj = address.getArea();
					if (obj.getLevel() == 2) {
						country = obj.getParent().getParent().getAreaName();
						city = obj.getParent().getAreaName();
						area = obj.getAreaName();
					} else if (obj.getLevel() == 1) {
						country = obj.getParent().getAreaName();
						city = obj.getAreaName();
					}
					map.put("country", country);
					map.put("city", city);
					map.put("area", area);
					map.put("defaultVal", address.getDefault_val());
					map.put("userName", address.getTrueName());

					map.put("mobile", address.getMobile());
					map.put("email", address.getEmail());
					map.put("telephone", address.getTelephone());
					map.put("AreaInfo", address.getArea_info());
					map.put("AreaZip", address.getZip());
					// 比较当前用户id与地址对应得id是否相同
					code = 4200;
					msg = "Success";
				} else {
					code = 4207;
					msg = "Please select the shipping address";
				}
			}
		}
		this.send_json(JSON.toJSONString(new Result(code, msg, map)), response);
	}

	/**
	 * 根据父id加载下级区域，返回json格式数据，这里只返回id和areaName，根据需要可以修改返回数据 过滤掉有关联的属性关联对象
	 * 
	 * @param request
	 * @param response
	 * @param pid
	 */
	@RequestMapping(value = "v1/loadArea.json", method = RequestMethod.GET)
	public void loadArea(HttpServletRequest request, HttpServletResponse response, String pid) {
		Map<String, Object> params = new HashMap<String, Object>();
		Result result = null;
		List<Area> areas = null;
		if(pid == null || pid.equals("")){
			areas = this.areaService.query("select obj from Area obj where obj.parent.id is null", params, -1, -1);
		}else{
			params.put("pid", CommUtil.null2Long(pid));
			areas = this.areaService.query("select obj from Area obj where obj.parent.id=:pid", params, -1, -1);
		}
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Area area : areas) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", area.getId());
			map.put("areaName", area.getAreaName());
			list.add(map);
		}
		result = new Result(4200, "Success", list);
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param id
	 *            地址
	 * @param area_id
	 *            区域
	 * @param currentPage
	 *            当前页
	 * @return
	 */
	@SecurityMapping(title = "收货地址保存", value = "/buyer/address_save.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping(value = "v1/addressSave.json", method = RequestMethod.POST)
	public void address_save(HttpServletRequest request, HttpServletResponse response, String id, String area_id,
			String flag, String currentPage, String token) {

		boolean saveboolean = false;
		Result result = new Result();
		Map map = new HashMap();
		WebForm wf = new WebForm();// 封装添加表单对象
		Address address = null;
		if (token.equals("")) {
			result = new Result(-100, "token Invalidation");
		} else {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user == null) {
				result = new Result(-100, "token Invalidation");
			} else {
				Map<String, Object> params = new HashMap<String, Object>();
				String telephone = request.getParameter("telephone");
				if (this.mobileTools.verify(telephone)) {
					if (id.equals("")) {
						address = wf.toPo(request, Address.class);
						address.setAddTime(new Date());
					} else {
						Address obj = this.addressService.getObjById(Long.parseLong(id));
						if (obj.getUser().getId().equals(user.getId())) {
							address = (Address) wf.toPo(request, obj);
						}
					}
					address.setDefault_val(0);
					if (flag.equals("1")) {
						params.put("user_id", user.getId());
						params.put("id", CommUtil.null2Long(id));
						params.put("default_val", 1);// [是否为默认收货地址，1为默认地址]
						List<Address> addresList = this.addressService.query(
								"select obj from Address obj where obj.user.id=:user_id and obj.id!=:id and obj.default_val=:default_val",
								params, -1, -1);
						Map<String, String> currentPageMap = new HashMap<String, String>();
						currentPageMap.put("currentPage", currentPage);
						for (Address obj : addresList) {
							obj.setDefault_val(0);
							this.addressService.update(obj);
						}
						address.setDefault_val(1);
					}
					params.clear();
					params.put("user_id", user.getId());
					params.put("id", CommUtil.null2Long(id));
					params.put("default_val", 1);// [是否为默认收货地址，1为默认地址]
					List<Address> defaultAddress = this.addressService.query(
							"select obj from Address obj where obj.user.id=:user_id and obj.id!=:id and obj.default_val=:default_val",
							params, -1, -1);
					if (defaultAddress.size() == 0) {
						address.setDefault_val(1);
					}
					Map<String, String> telephoneMap = this.mobileTools.mobile(telephone);
					address.setTelephone(telephoneMap.get("phoneNumber").toString());
					address.setMobile(telephoneMap.get("phoneNumber").toString());
					address.setUser(user);
					Area area = this.areaService.getObjById(CommUtil.null2Long(area_id));
					address.setArea(area);
					if (id.equals("")) {
						saveboolean = this.addressService.save(address);
						if (saveboolean) {
							List<Address> addresses = user.getAddrs();
							if (addresses.size() == 1) {
								this.address_metoo_default(request, response,
										CommUtil.null2String(addresses.get(0).getId()), currentPage, token);
							}
							map.put("id", address.getId());
							result = new Result(4200, "Success", map);
						} else {
							result = new Result(1, "Error");
						}
					} else {
						if (this.addressService.update(address)) {
							map.put("id", address.getId());
							result = new Result(4200, "Success", map);
						} else {
							result = new Result(1, "Error");
						}
					}
				} else {
					result = new Result(4400, "手机号码格式不对");
				}
			}
		}
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param mulitId
	 * @param currentPage
	 */
	@SecurityMapping(title = "收货地址删除", value = "/buyer/address_del.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping(value = "v1/addressDelete.json", method = RequestMethod.POST)
	public void address_del(HttpServletRequest request, HttpServletResponse response, String mulitId,
			String currentPage, String token) {
		Result result = null;
		Map<String, Object> map = new HashMap<String, Object>();
		boolean del = false;
		if (token.equals("")) {
			result = new Result(-100, "token Invalidation");
		} else {
			Map<String, Object> params = new HashMap<String, Object>();
			boolean flag = false;
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user == null) {
				result = new Result(-100, "token Invalidation");
			} else {
				String[] ids = mulitId.split(",");
				for (String id : ids) {
					if (!id.equals("")) {
						Address address = this.addressService.getObjById(Long.parseLong(id));
						if (address.getUser().getId().equals(user.getId())) {// 只允许删除自己的地址信息
							del = this.addressService.delete(Long.parseLong(id));
							if (address.getDefault_val() == 1) {
								flag = true;
							}
						}
					}
				}
				if (del == true) {
					if (flag) {
						params.clear();
						params.put("uid", user.getId());
						List<Address> addres = this.addressService
								.query("select obj from Address obj where obj.user.id=:uid", params, -1, -1);
						if (user.getAddrs().size() > 0) {
							Address obj = addres.get(0);
							obj.setDefault_val(1);
							this.addressService.update(obj);
						}
					}
					result = new Result(4200, "Success", map);
				} else {
					result = new Result(1, "Error");
				}
			}
			map.put("currentPage", currentPage);
		}
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param mulitId
	 * @param currentPage
	 */
	@SecurityMapping(title = "收货地址默认设置", value = "/buyer/address_default.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping(value = "v1/buyer_address_default.json", method = RequestMethod.POST)
	public void address_default(HttpServletRequest request, HttpServletResponse response, String mulitId,
			String currentPage, String token) {

		Result result = null;
		if (token.equals("")) {
			result = new Result(-100, "token Invalidation");
		} else {
			Map<String, Object> params = new HashMap<String, Object>();
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user == null) {
				result = new Result(-100, "token Invalidation");
			} else {
				String[] ids = mulitId.split(",");
				for (String id : ids) {
					if (!id.equals("")) {
						Address address = this.addressService.getObjById(Long.parseLong(id));
						if (address.getUser().getId().equals(user.getId())) {// 只允许修改自己的地址信息
							params.clear();
							params.put("user_id", user.getId());
							params.put("id", CommUtil.null2Long(id));
							params.put("default_val", 1);// [是否为默认收货地址，1为默认地址]
							List<Address> addresList = this.addressService.query(
									"select obj from Address obj where obj.user.id=:user_id and obj.id!=:id and obj.default_val=:default_val",
									params, -1, -1);
							Map<String, String> map = new HashMap<String, String>();
							map.put("currentPage", currentPage);
							for (Address obj : addresList) {
								obj.setDefault_val(0);
								this.addressService.update(obj);
							}
							address.setDefault_val(1);
							if (this.addressService.update(address)) {
								result = new Result(4200, "Success", map);
							} else {
								result = new Result(1, "Error");
							}
						}
					}
				}
			}
		}
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param mulitId
	 * @param currentPage
	 */
	@SecurityMapping(title = "收货地址默认取消", value = "/buyer/address_default_cancle.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping(value = "v1/address_default_cancle.json", method = RequestMethod.POST)
	public void address__default_cancle(HttpServletRequest request, HttpServletResponse response, String mulitId,
			String currentPage, String token) {

		Result result = new Result();
		boolean flag = false;
		if (token.equals("")) {
			result = new Result(-100, "token Invalidation");
		} else {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user == null) {
				result = new Result(-100, "token Invalidation");
			} else {
				String[] ids = mulitId.split(",");
				for (String id : ids) {
					if (!id.equals("")) {
						Address address = this.addressService.getObjById(Long.parseLong(id));
						if (address.getUser().getId().equals(user.getId())) {// 只允许修改自己的地址信息
							address.setDefault_val(0);
							flag = this.addressService.update(address);
						}
					}
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("currentPage", currentPage);
				if (flag) {
					result = new Result(4200, "Success", map);
				} else {
					result = new Result(1, "Error");
				}
			}
		}
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param mulitId
	 *            地址id
	 * @param type
	 *            操作类型
	 * @param currentPage
	 *            当前页数
	 * @param token
	 *            用戶身份令牌
	 * @description 设置默认收货地址
	 */
	@RequestMapping(value = "v1/address.json", method = RequestMethod.PUT)
	public void addressDefaultCancle(HttpServletRequest request, HttpServletResponse response, String mulitId,
			String type, String currentPage, String token) {

		Result result = null;
		if (token.equals("")) {
			result = new Result(-100, "token Invalidation");
		} else {
			Map<String, Object> params = new HashMap<String, Object>();
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user == null) {
				result = new Result(-100, "token Invalidation");
			} else {
				String[] ids = mulitId.split(",");
				if (type.equals("add")) {
					for (String id : ids) {
						if (!id.equals("")) {
							Address address = this.addressService.getObjById(Long.parseLong(id));
							if (address.getUser().getId().equals(user.getId())) {// 只允许修改自己的地址信息
								params.clear();
								params.put("user_id", user.getId());
								params.put("id", CommUtil.null2Long(id));
								params.put("default_val", 1);// [是否为默认收货地址，1为默认地址]
								List<Address> addresList = this.addressService.query(
										"select obj from Address obj where obj.user.id=:user_id and obj.id!=:id and obj.default_val=:default_val",
										params, -1, -1);
								Map<String, String> map = new HashMap<String, String>();
								map.put("currentPage", currentPage);
								for (Address obj : addresList) {
									obj.setDefault_val(0);
									this.addressService.update(obj);
								}
								address.setDefault_val(1);
								this.addressService.update(address);
							}
						}
					}
				} else {
					for (String id : ids) {
						if (!id.equals("")) {
							Address address = this.addressService.getObjById(Long.parseLong(id));
							if (address.getUser().getId().equals(user.getId())) {// 只允许修改自己的地址信息
								address.setDefault_val(0);
								this.addressService.update(address);
							}
						}
					}
				}
			}
		}
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	public String address_metoo_default(HttpServletRequest request, HttpServletResponse response, String mulitId,
			String currentPage, String token) {
		Result result = null;
		if (token.equals("")) {
			result = new Result(-100, "token Invalidation");
		} else {
			Map<String, Object> params = new HashMap<String, Object>();
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user == null) {
				result = new Result(-100, "token Invalidation");
			} else {
				String[] ids = mulitId.split(",");
				for (String id : ids) {
					if (!id.equals("")) {
						Address address = this.addressService.getObjById(Long.parseLong(id));
						if (address.getUser().getId().equals(user.getId())) {// 只允许修改自己的地址信息
							params.clear();
							params.put("user_id", user.getId());
							params.put("id", CommUtil.null2Long(id));
							params.put("default_val", 1);// [是否为默认收货地址，1为默认地址]
							List<Address> addresList = this.addressService.query(
									"select obj from Address obj where obj.user.id=:user_id and obj.id!=:id and obj.default_val=:default_val",
									params, -1, -1);
							Map<String, String> map = new HashMap<String, String>();
							map.put("currentPage", currentPage);
							for (Address obj : addresList) {
								obj.setDefault_val(0);
								this.addressService.update(obj);
							}
							address.setDefault_val(1);
							if (this.addressService.update(address)) {
								result = new Result(4200, "Success", map);
							} else {
								result = new Result(1, "Error");
							}
						}
					}
				}
			}
		}
		return Json.toJson(result, JsonFormat.compact());
	}

	private void send_json(String json, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.print(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
