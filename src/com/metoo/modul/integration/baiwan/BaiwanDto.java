package com.metoo.modul.integration.baiwan;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


import lombok.Data;

public class BaiwanDto {

	private String orderNo;// 订单编号
	
	private double amount;// 支付金额
	
	private String currency;// 订单币种
	
	private String language;// 支付页面语言 en_US
	
	private String cardNo;// 信用卡号
	
	private Integer cvv;// 信用卡背后cvv2号码
	
	private Integer expirationYear;// 信用卡有效期年份【4位】
	
	private String expirationMonth;// 信用卡有效期月份【2位】
	
	private String billFirstName;// 信用卡账单人名字
	
	private String billLastName;// 信用卡账单人姓氏
	
	private String billAddress;// 账单城市
	
	private String billCity;// 账单地址
	
	private String billState;// 账单州
	
	private String billCountry;// 账单国家
	
	private Integer billZip;// 账单邮编
	
	private String shipFirstName;// 收货人名称
	
	private String shipLastName;// 收货人姓氏
	
	private String shipAddress;// 收货详细地址
	
	private String shipCity;// 收货城市
	
	private String shipState;// 收货省/州
	
	private String shipCountry;// 收货国家
	
	private Integer shipZip;// 收货邮编
	
	private Integer phone;// 持卡人电话
	
	private String email;// 持卡人邮箱
	
	private String token;// 用户身份令牌：后期移除

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getCvv() {
		return cvv;
	}

	public void setCvv(Integer cvv) {
		this.cvv = cvv;
	}

	public Integer getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}

	public String getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public String getBillFirstName() {
		return billFirstName;
	}

	public void setBillFirstName(String billFirstName) {
		this.billFirstName = billFirstName;
	}

	public String getBillLastName() {
		return billLastName;
	}

	public void setBillLastName(String billLastName) {
		this.billLastName = billLastName;
	}

	public String getBillAddress() {
		return billAddress;
	}

	public void setBillAddress(String billAddress) {
		this.billAddress = billAddress;
	}

	public String getBillCity() {
		return billCity;
	}

	public void setBillCity(String billCity) {
		this.billCity = billCity;
	}

	public String getBillState() {
		return billState;
	}

	public void setBillState(String billState) {
		this.billState = billState;
	}

	public String getBillCountry() {
		return billCountry;
	}

	public void setBillCountry(String billCountry) {
		this.billCountry = billCountry;
	}

	public Integer getBillZip() {
		return billZip;
	}

	public void setBillZip(Integer billZip) {
		this.billZip = billZip;
	}

	public String getShipFirstName() {
		return shipFirstName;
	}

	public void setShipFirstName(String shipFirstName) {
		this.shipFirstName = shipFirstName;
	}

	public String getShipLastName() {
		return shipLastName;
	}

	public void setShipLastName(String shipLastName) {
		this.shipLastName = shipLastName;
	}

	public String getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}

	public String getShipCity() {
		return shipCity;
	}

	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}

	public String getShipState() {
		return shipState;
	}

	public void setShipState(String shipState) {
		this.shipState = shipState;
	}

	public String getShipCountry() {
		return shipCountry;
	}

	public void setShipCountry(String shipCountry) {
		this.shipCountry = shipCountry;
	}

	public Integer getShipZip() {
		return shipZip;
	}

	public void setShipZip(Integer shipZip) {
		this.shipZip = shipZip;
	}

	public Integer getPhone() {
		return phone;
	}

	public void setPhone(Integer phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
