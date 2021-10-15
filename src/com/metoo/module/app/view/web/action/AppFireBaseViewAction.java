package com.metoo.module.app.view.web.action;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metoo.core.tools.CommUtil;
import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.IUserService;
import com.metoo.module.app.buyer.domain.Result;
import com.metoo.module.app.pojo.Notification;
import com.metoo.msg.fcm.FireBaseUtils;

/**
 * 
 * <p>
 * Title: AppFireBaseViewAction.java
 * </p>
 * 
 * <P>
 * Description: web聊天控制器：发送聊天消息通知
 * </p>
 * 
 * @author 46075
 *
 */
@Controller
@RequestMapping("/php/v1/firebase")
public class AppFireBaseViewAction {

	@Autowired
	private IUserService userService;
	@Autowired
	private FireBaseUtils fireBaseUtils;

	@RequestMapping("/message.json")
	@ResponseBody
	public Object pushSigln(HttpServletRequest request, HttpServletResponse response, String user_id, String message) {
		Result result = null;
		User user = null;
		if (!CommUtil.null2String(user_id).equals("")) {
			user = this.userService.getObjById(CommUtil.null2Long(user_id));
		}
		if (user != null) {
			if (message != null && !"".equals(message)) {
				if (user.getFirebase_token() != null && !user.getFirebase_token().equals("")) {
					Notification notification = new Notification();
					notification.setTitle("new message");
					notification.setBody(message);
					notification.setColor("#8fa4a3");
					notification.setAppName("Soarmall");
					notification.setDataBaseUrl("https://soarmall.firebaseio.com");
					notification.setToken(user.getFirebase_token());
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("url", "/chat");
					map.put("message_id", user == null ? "" : user.getId());
					notification.setMap(map);
					if (!this.fireBaseUtils.isInit("Soarmall")) {
						// InputStream in =
						// request.getSession().getServletContext().getResourceAsStream("firebase-adminsdk.json");
						String url = request.getSession().getServletContext().getRealPath("firebase-adminsdk.json");
						File file = new File(url);
						this.fireBaseUtils.initSDK(file, notification);
					}
					String push = this.fireBaseUtils.pushSigle(notification);
					result = new Result(5200, push);
				} else {
					result = new Result(500, "Push failed. The specified device could not be found");
				}
			} else {
				result = new Result(400, "Error");
			}
		} else {
			result = new Result(-100, "The user does not exist.");
		}
		return result;
	}
}
