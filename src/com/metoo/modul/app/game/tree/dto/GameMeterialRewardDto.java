package com.metoo.modul.app.game.tree.dto;

/**
 * <p>
 * 	Title: GameMeterialReward.java
 * </p>
 * 
 * <p>
 * 	Description: 免费领取参数
 * </p>
 * 
 * <author>
 * 	HKK
 * </author>
 *
 */
public class GameMeterialRewardDto {

	private String token;// 用户登录Token(后期优化)
	
	private Long gameAward_id;// 奖品ID
	
	private String userName;// 用户名
	
	private String mobile;// 电话
	
	private String email;// 邮箱
	
	private Long address_id;// 用户地址ID
	
	private Long area_id;// 区域
	
	private String area_info;// 详细地址

	public Long getGameAward_id() {
		return gameAward_id;
	}

	public void setGameAward_id(Long gameAward_id) {
		this.gameAward_id = gameAward_id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getArea_id() {
		return area_id;
	}

	public void setArea_id(Long area_id) {
		this.area_id = area_id;
	}

	public String getArea_info() {
		return area_info;
	}

	public void setArea_info(String area_info) {
		this.area_info = area_info;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getAddress_id() {
		return address_id;
	}

	public void setAddress_id(Long address_id) {
		this.address_id = address_id;
	}
	
	

	public GameMeterialRewardDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GameMeterialRewardDto(String token, Long gameAward_id, String userName, String mobile, String email,
			Long address_id, Long area_id, String area_info) {
		super();
		this.token = token;
		this.gameAward_id = gameAward_id;
		this.userName = userName;
		this.mobile = mobile;
		this.email = email;
		this.address_id = address_id;
		this.area_id = area_id;
		this.area_info = area_info;
	}

	@Override
	public String toString() {
		return "GameMeterialRewardDto [token=" + token + ", gameAward_id=" + gameAward_id + ", userName=" + userName
				+ ", mobile=" + mobile + ", email=" + email + ", address_id=" + address_id + ", area_id=" + area_id
				+ ", area_info=" + area_info + "]";
	}
	
}
