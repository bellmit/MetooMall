package  com.metoo.module.app.manage.buyer.v1.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.metoo.core.annotation.SecurityMapping;
import com.metoo.core.mv.JModelAndView;
import com.metoo.core.tools.CommUtil;
import com.metoo.core.tools.Md5Encrypt;
import com.metoo.core.tools.WebForm;
import com.metoo.foundation.domain.Area;
import com.metoo.foundation.domain.Favorite;
import com.metoo.foundation.domain.FootPoint;
import com.metoo.foundation.domain.GoodsCart;
import com.metoo.foundation.domain.IntegralLog;
import com.metoo.foundation.domain.OrderForm;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.domain.VerifyCode;
import com.metoo.foundation.service.IAreaService;
import com.metoo.foundation.service.IFavoriteService;
import com.metoo.foundation.service.IFootPointService;
import com.metoo.foundation.service.IGoodsCartService;
import com.metoo.foundation.service.IIntegralLogService;
import com.metoo.foundation.service.IOrderFormService;
import com.metoo.foundation.service.ISysConfigService;
import com.metoo.foundation.service.IUserConfigService;
import com.metoo.foundation.service.IUserService;
import com.metoo.foundation.service.IVerifyCodeService;
import com.metoo.module.app.buyer.domain.Result;
import com.metoo.module.app.view.web.tool.AppCartViewTools;
import com.metoo.module.app.view.web.tool.AppobileTools;
import com.metoo.msg.MsgTools;

@Controller
@RequestMapping("/app/v1/")
public class AppAccountBuyerAction {

	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IVerifyCodeService mobileverifycodeService;
	@Autowired
	private IUserConfigService userConfigService;
	@Autowired
	private IUserService userService;
	@Autowired
	private MsgTools msgTools;
	@Autowired
	private IFavoriteService favoriteService;
	@Autowired
	private IFootPointService footPointService;
	@Autowired
	private IOrderFormService orderFormService;
	@Autowired
	private IGoodsCartService goodsCartService;
	@Autowired
	private AppCartViewTools mCartViewTools;
	@Autowired
	private AppobileTools mobileTools;
	@Autowired
	private IAreaService areaService;
	@Autowired
	private IIntegralLogService intergralLogService;

	@SecurityMapping(title = "个人信息", value = "/buyer_account.json*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping(value = "account.json", method = RequestMethod.POST)
	public void account(HttpServletRequest request, HttpServletResponse response, String token) {

		Result result = new Result();
		Map<String, Object> map = new HashMap<String, Object>();
		if(token.equals("")){
			result = new Result(-100,"token Invalidation");
		}else{
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if(user == null){
				result = new Result(-100,"token Invalidation");
			} else{
				Map<String, Object> userMap = new HashMap<String, Object>();
				userMap.put("TrueName", user.getTrueName());
				userMap.put("Sex", user.getSex());
				userMap.put("Email", user.getEmail());
				userMap.put("Telephone", user.getTelephone());
				userMap.put("Mobile", user.getMobile());
				userMap.put("Birthday", user.getBirthday());
				List<Area> areas = this.areaService.query(
						"select obj from Area obj where obj.parent.id is null", null, -1, -1);
				List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>();
				for (Area area : areas) {
					Map<String, Object> areaMap = new HashMap<String, Object>();
					areaMap.put("id", area.getId());
					areaMap.put("areaName", area.getAreaName());
					areaList.add(areaMap);
					map.put("areaMap", areaList);
				}
				map.put("userMap", userMap);
			    result = new Result(4200, "Success" ,map);
		}
	}
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	@SecurityMapping(title = "个人信息保存", value = "/buyer_account_save.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping(value = "accountSave.json", method = RequestMethod.POST)
	public void account_save(HttpServletRequest request, HttpServletResponse response, String area_id, String birthday,
			String token) {
		Result result = null;
		WebForm wf = new WebForm();
		if(token.equals("")){
			result = new Result(-100, "token Invalidation");
		}else{
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if(user == null){
				result = new Result(-100, "token Invalidation");
			}else{
				   user = (User) wf.toPo(request, this.userService
						.getObjById(user.getId()));
				if (area_id != null && !area_id.equals("")) {
					Area area = this.areaService
							.getObjById(CommUtil.null2Long(area_id));
				}
				if (birthday != null && !birthday.equals("")) {
					String y[] = birthday.split("-");
					Calendar calendar = new GregorianCalendar();
					int years = calendar.get(Calendar.YEAR) - CommUtil.null2Int(y[0]);
					user.setYears(years);
				}
				if(this.userService.update(user)){
					result = new Result(4200, "Success");
				}else{
					result = new Result(1, "Error");
				}
			}
		}
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	@SecurityMapping(title = "密码修改保存", value = "/buyer_account_password_save.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping(value = "accountPassword.json", method = RequestMethod.POST)
	public void account_password_save(HttpServletRequest request, HttpServletResponse response, String old_password,
			String new_password, String token) {

		Result result = new Result();
		if(token.equals("")){
			result = new Result(-100,"token Invalidation");
		}else{
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if(user == null){
				result = new Result(-100,"token Invalidation");	
			}else{
				//获取当前登录用户数据库密码，比较用户输入密码加密 (md5)if-true 对新的密码加密
				if(user.getPassword().equals(
						Md5Encrypt.md5(old_password).toLowerCase())){
					user.setPassword(Md5Encrypt.md5(new_password).toLowerCase());
					boolean pwd = this.userService.update(user);
					if(pwd){
/*							String content1 = "尊敬的"
								+ user.getUserName()
								+ "您好，您于" + CommUtil.formatLongDate(new Date())
								+ "修改密码成功，新密码为：" + new_password + ",请妥善保管。["
								+ this.configService.getSysConfig().getTitle() + "]";
						
*/							String content = "Hi,dear customer "
								+ user.getUserName()
								+ ". you have success changed your password on : "
								+ CommUtil.formatLongDate(new Date())
								+ ". Your new password is: " 
								+ new_password
								+ ",Please keep it safe. [Soarmall.com]";
						try {
							this.msgTools.sendSMS(user.getMobile(), content);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						result = new Result(0, content, pwd);
					}
				}else{
					result = new Result(1, "PassWord Error");
				}
			}
		}
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	@SecurityMapping(title = "邮箱修改保存", value = "/buyer_account_email_save.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping(value = "accountEmail.json", method = RequestMethod.POST)
	public void account_email_save(HttpServletRequest request, HttpServletResponse response, String password,
			String email, String token) {

		Result result = null;
		if(CommUtil.null2String(token).equals("")){
			result = new Result(-100,"token Invalidation");
		}else{
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if(user == null){
				result = new Result(-100, "token Invalidation");	
			}else{
		      if(user.getPassword().equals(Md5Encrypt.md5(password).toLowerCase())){
		    	  user.setEmail(email);
		    	  if(this.configService.getSysConfig().isIntegral()){
		    		  user.setIntegral(this.configService.getSysConfig().getMemberEmail());
		    		  IntegralLog integralLog = new IntegralLog();
		    		  integralLog.setAddTime(new Date());
		    		  integralLog.setContent("用户"+new Date()+"完善邮箱增加" + this.configService.getSysConfig().getMemberEmail() + "分");
		    		  integralLog.setIntegral(this.configService.getSysConfig().getMemberEmail());
		    		  integralLog.setIntegral_user(user);
		    		  integralLog.setType("Add");
		    		  integralLog.setIntegral_from("登录");
		    		  this.intergralLogService.save(integralLog);
		    	  }
		    	 if(this.userService.update(user)){
		    		 result = new Result(4200, "Success");
		    	 }else{
		    		 result = new Result(1, "Error");
		    	 }
		      }else{
		    	  result = new Result(1, "PassWord Error");
		      }
			}
		}
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	/**
	 *
	 * @description 手机短信发送
	 * @param request
	 * @param response
	 * @param type
	 * @throws UnsupportedEncodingException
	 */
	@SecurityMapping(title = "手机短信发送", value = "/buyer/account_mobile_sms.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping(value = "accountSendSms.json", method = RequestMethod.POST)
	public void account_mobile_sms(HttpServletRequest request, HttpServletResponse response, String type, String mobile,
			String token) throws UnsupportedEncodingException {
		Map params = new HashMap();
		Result result = null;
		if (token.equals("")) {
			result = new Result(-100, "token Invalidation");
		} else {
			params.put("app_login_token", token);
			List<User> users = this.userService.query(
					"select obj from User obj where obj.app_login_token=:app_login_token order by obj.addTime desc",
					params, -1, -1);
			if (users.isEmpty()) {
				result = new Result(-100, "token Invalidation");
			} else {
				User user = users.get(0);
				int ret = 0;
				if (type.equals("mobile_vetify_code")) {
					String code = CommUtil.randomString(4).toUpperCase();
					String content = "尊敬的" + user.getUserName() + "您好，您在试图修改"
							+ this.configService.getSysConfig().getWebsiteName() + "用户绑定手机，手机验证码为：" + code + "。["
							+ this.configService.getSysConfig().getTitle() + "]";
					if (this.configService.getSysConfig().isSmsEnbale()) {
						boolean ret1 = this.msgTools.sendSMS(mobile, content);
						if (ret1) {
							VerifyCode mvc = this.mobileverifycodeService.getObjByProperty(null, "mobile", mobile);
							if (mvc == null) {
								mvc = new VerifyCode();
							}
							mvc.setAddTime(new Date());
							mvc.setCode(code);
							mvc.setMobile(mobile);
							this.mobileverifycodeService.update(mvc);
						} else {
							ret = 1;
						}
					} else {
						ret = 2;
					}
					result = new Result(ret);
				}
			}
		}
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	/**
	 * @description 手机号修改保存
	 * @param request
	 * @param response
	 * @param mobile_verify_code
	 * @param mobile
	 * @param token
	 * @throws Exception
	 */
	@SecurityMapping(title = "手机号码保存", value = "/buyer/account_mobile_save.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
	@RequestMapping(value = "accountMobile.json", method = RequestMethod.POST)
	public void account_mobile_save(HttpServletRequest request, HttpServletResponse response, String mobile_verify_code,
			String mobile, String token) throws Exception {
		Result result = null;
		if (token.equals("")) {
			result = new Result(-100, "token Invalidation");
		} else {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user == null) {
				result = new Result(-100, "token Invalidation");
			} else {
				VerifyCode mvc = this.mobileverifycodeService.getObjByProperty(null, "mobile",
						this.mobileTools.mobile(mobile).get("areaMobile").toString());
				if (mvc != null && mvc.getCode().equalsIgnoreCase(mobile_verify_code)) {
					user.setMobile(mobile);
					this.userService.update(user);
					this.mobileverifycodeService.delete(mvc.getId());
					// 绑定成功后发送手机短信提醒
					String content = "尊敬的" + user.getUserName() + "您好，您于" + CommUtil.formatLongDate(new Date())
							+ "绑定手机号成功。[" + this.configService.getSysConfig().getTitle() + "]";
					this.msgTools.sendSMS(user.getMobile(), content);
					result = new Result(4200, "Success");
				} else {
					String content = "Verification code error, phone binding failed";
					result = new Result(1, "error", content);
				}
			}
		}
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	/**
	 * @description app获取邀请码
	 * @param request
	 * @param response
	 * @param token
	 */
	@RequestMapping(value = "invitation.json", method = RequestMethod.GET)
	public void invitation(HttpServletRequest request, HttpServletResponse response, String token) {
		Result result = null;
		if (CommUtil.null2String(token).equals("")) {
			result = new Result(-100, "token Invalidation");
		} else {
			User user = this.userService.getObjByProperty(null, "app_login_token", token);
			if (user == null) {
				result = new Result(-100, "token Invalidation");
			} else {
				String code = "";
				if (user.getCode() == null || user.getCode().equals("")) {
					code = CommUtil.randomLowercase(4);
				} else {
					code = user.getCode();
				}
				user.setCode(code);
				this.userService.update(user);
				Map<String, String> map = new HashMap<String, String>();
				map.put("code", code);
				result = new Result(4200, "Success", map);
			}
		}
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	/**
	 * @description 用户中心
	 * @param request
	 * @param response
	 * @param token
	 */
	@RequestMapping(value = "userCenter.json")
	public void system(HttpServletRequest request, HttpServletResponse response, String token, String visitor_id) {
		int code = -1;
		String msg = "";
		Result result = null;
		Map<String, Object> data = new HashMap<String, Object>();
		User user = null;
		if (!token.equals("")) {
			user = this.userService.getObjByProperty(null, "app_login_token", token);
		}
		String cart_session_id = "";
		if (null != visitor_id && !"".equals(visitor_id)) {
			cart_session_id = visitor_id;
		} else {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("cart_session_id")) {
						cart_session_id = CommUtil.null2String(cookie.getValue());
					}
				}
			}
		}
		if (null != cart_session_id && cart_session_id.equals("")) {
			cart_session_id = UUID.randomUUID().toString();
			Cookie cookie = new Cookie("cart_session_id", cart_session_id);
			cookie.setDomain(CommUtil.generic_domain(request));
			response.addCookie(cookie);
		}
		if (null == user && !"".equals(visitor_id)) {
			cart_session_id = visitor_id;
		}

		List<GoodsCart> goodsCarts = this.mCartViewTools.cartListCalc(request, user, cart_session_id);
		if (null != user) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("type", 0);
			params.put("user_id", user.getId());
			List<Favorite> favorites = this.favoriteService.query(
					"select obj from Favorite obj where obj.type=:type and obj.user_id=:user_id", params, -1, -1);
			params.clear();
			params.put("fp_user_id", user.getId());
			List<FootPoint> footPoints = this.footPointService.query(
					"select new FootPoint(id) from FootPoint obj where obj.fp_user_id=:fp_user_id", params, -1, -1);
			/*
			 * params.clear(); params.put("user_id", user.getId());
			 * params.put("cart_status", 0); List<GoodsCart> goodsCarts =
			 * this.goodsCartService.query(
			 * "select obj from GoodsCart obj where obj.user.id=:user_id and obj.cart_status=:cart_status "
			 * , params, -1, -1);
			 */
			params.clear();
			params.put("order_status", 30);
			params.put("user_id", user.getId().toString());
			List<OrderForm> shippeds = this.orderFormService.query(
					"select obj from OrderForm obj where obj.order_status=:order_status and obj.user_id=:user_id",
					params, -1, -1);
			
			params.put("order_status", 40);
			List<OrderForm> reviews = this.orderFormService.query(
					"select obj from OrderForm obj where obj.order_status=:order_status and obj.user_id=:user_id",
					params, -1, -1);
			List<Integer> status = new ArrayList<Integer>();
			status.add(16);
			status.add(20);
			params.put("order_status", status);
			List<OrderForm> pendings = this.orderFormService.query(
					"select obj from OrderForm obj where obj.order_status in(:order_status) and obj.user_id=:user_id",
					params, -1, -1);
			params.put("order_status", 10);
			List<OrderForm> unpaid = this.orderFormService.query(
					"select obj from OrderForm obj where obj.order_status in(:order_status) and obj.user_id=:user_id",
					params, -1, -1);
			data.put("favorite", favorites.size() <= 0 ? 0 : favorites.size());
			data.put("footPoint", footPoints.size() <= 0 ? 0 : footPoints.size());
			data.put("shippeds", shippeds.size() <= 0 ? 0 : shippeds.size());
			data.put("reviews", reviews.size() <= 0 ? 0 : reviews.size());
			data.put("pendings", pendings.size() <= 0 ? 0 : pendings.size());
			data.put("unpaid", unpaid.size() <= 0 ? 0 : unpaid.size());
			code = 4200;
			msg = "Success";
		} else {
			code = -100;
		}
		data.put("goodsCarts", goodsCarts.size() <= 0 ? 0 : goodsCarts.size());
		data.put("visitor_id", cart_session_id);
		result = new Result(code, msg, data);
		this.send_json(Json.toJson(result, JsonFormat.compact()), response);
	}

	@RequestMapping("editUserMbile.json")
	public void editUserMobile(HttpServletRequest request, HttpServletResponse response) {
		Map params = new HashMap();
		List<User> users = this.userService.query("select obj from User obj where obj.mobile is not null ", params, -1,
				-1);
		for (User user : users) {
			String mobile = user.getMobile();
			if (!CommUtil.null2String(mobile).equals("") && mobile.substring(0, 1).equals("0")) {
				user.setTelephone(mobile.substring(1, mobile.length() - 1));
				this.userService.update(user);
			}
		}
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
