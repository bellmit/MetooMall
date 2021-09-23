package com.metoo.module.app.view.web.action;

public class Snippet {
	/*// 查询用户信息
		public void userInfo(Long user_id, Map map) {
			User user = this.userService.getObjById(user_id);
			Map userMap = new HashMap();
			try {
				userMap = new HashMap();
				if (this.configService.getSysConfig().isIntegral()) {
					userMap.put("userIntegral", user.getIntegral());
					double integral_price = CommUtil.mul(user.getIntegral(),
							this.configService.getSysConfig().getIntegralExchangeRate());
					userMap.put("integral_price", integral_price);
				} else {
					userMap.put("userIntegral", "");
					userMap.put("integral_price", "");
				}
				userMap.put("username", user.getUsername());
				userMap.put("user_email", user.getEmail());
				userMap.put("user_id", user.getId());
				map.put("user", userMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
}

