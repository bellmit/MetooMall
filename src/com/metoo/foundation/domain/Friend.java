package com.metoo.foundation.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.metoo.core.constant.Globals;
import com.metoo.core.domain.IdEntity;

/**
 * <p>
 * 	Title: Friend.java
 * </p>
 * 
 * <p>
 * 	Description: 好友实体类
 * </p>
 * 
 * <p>
 * 	Company: 觅通科技有限公司
 * </p>
 * 
 * @author 46075
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = Globals.DEFAULT_TABLE_SUFFIX + "friend")
public class Friend extends IdEntity{

	private Long user_id;// 当前用户id
	private int status;// 好友状态  0：申请添加 1：同意 2：拒绝  -1:已删除 -2：被拒绝
	private String userName;// 当前用户名称
	@ManyToOne(fetch = FetchType.LAZY)
	private User friend;// 好友
	private String friendName;// 好友名称
	private String nickName;// 好友备注
	private int sex;// 好友性别
	private String mobile;// 好友电话
	private String code;// 好友邀请码
	private String verification_information;// 验证信息
	
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public User getFriend() {
		return friend;
	}
	public void setFriend(User friend) {
		this.friend = friend;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getVerification_information() {
		return verification_information;
	}
	public void setVerification_information(String verification_information) {
		this.verification_information = verification_information;
	}
	
	/**
	 * 
	 */
	public Friend() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param id
	 * @param addTime
	 */
	public Friend(Long id, Date addTime) {
		super(id, addTime);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param id
	 */
	public Friend(Long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param user_id
	 * @param status
	 * @param userName
	 * @param friend
	 * @param friendName
	 * @param nickName
	 * @param sex
	 * @param mobile
	 * @param code
	 * @param verification_information
	 */
	public Friend(Long user_id, String userName, String nickName, int sex) {
		this.user_id = user_id;
		this.userName = userName;
		this.nickName = nickName;
		this.sex = sex;
	}
	
}
