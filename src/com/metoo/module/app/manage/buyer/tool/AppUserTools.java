package com.metoo.module.app.manage.buyer.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.metoo.foundation.domain.User;
import com.metoo.foundation.service.ISysConfigService;

@Component
public class AppUserTools {

	@Autowired
	private ISysConfigService configService;

	public List get(List<User> users) {
		if (users.size() > 0) {
			List<Map> list = new ArrayList<Map>();
			for (User user : users) {
				Map map = new HashMap();
				map.put("user_id", user.getId());
				map.put("user_name", user.getUserName());
				map.put("user_email", user.getEmail());
				map.put("user_sex", user.getSex());
				if (user.getSex() == -1) {
					map.put("user_photo", this.configService.getSysConfig().getImageWebServer() + "/" + "resources"
							+ "/" + "style" + "/" + "common" + "/" + "images" + "/" + "member.png");
				}
				if (user.getSex() == 0) {
					map.put("user_photo", this.configService.getSysConfig().getImageWebServer() + "/" + "resources"
							+ "/" + "style" + "/" + "common" + "/" + "images" + "/" + "member0.png");
				}
				if (user.getSex() == 1) {
					map.put("user_photo", this.configService.getSysConfig().getImageWebServer() + "/" + "resources"
							+ "/" + "style" + "/" + "common" + "/" + "images" + "/" + "member1.png");
				}
				list.add(map);
			}
			return list;
		}
		return null;

	}

	public Map<String, Object> get(User user) {
		if (user != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("friend_id", user.getId());
			map.put("user_name", user.getUserName());
			map.put("user_email", user.getEmail());
			map.put("user_sex", user.getSex());
			if (user.getSex() == -1) {
				map.put("user_photo", this.configService.getSysConfig().getImageWebServer() + "/" + "resources" + "/"
						+ "style" + "/" + "common" + "/" + "images" + "/" + "member.png");
			}
			if (user.getSex() == 0) {
				map.put("user_photo", this.configService.getSysConfig().getImageWebServer() + "/" + "resources" + "/"
						+ "style" + "/" + "common" + "/" + "images" + "/" + "member0.png");
			}
			if (user.getSex() == 1) {
				map.put("user_photo", this.configService.getSysConfig().getImageWebServer() + "/" + "resources" + "/"
						+ "style" + "/" + "common" + "/" + "images" + "/" + "member1.png");
			}
			return map;
		}
		return null;

	}
}
