package com.metoo.modul.integration.baiwan;

public class BaiwanVo {
	
	private String merchantNo;// 商户号
	
	private String terminalNo;// 终端号
	
	private String orderNo;// 订单编号

	private double amount;// 支付金额
	
	private String currency;// 订单币种
	
	private String securityValue;// 支付数字签名
	
	private String returnURL;// 支付结果返回地址
	
	private String asynReturnURL;// 支付结果异步返回地址
	
	private String goodsJson;// 订单商品信息
	
	private String website;// 网店网址(交易网店域名)
	
	private String payIp;// 持卡人IP地址
	
	private String language;// 支付页面语言 en_US
	
	private String remark;// 订单备注

	private Integer payType;// 0跳转，1直连【本文档中固定1】固定1
	
	private Integer serviceType;// 业务类型 0实体行业，1虚拟行业 0
	
	private Integer payMethod;// 支付方式  0信用卡/1电子支票/2电子钱包/3借记卡 固定0
	
	private String cardNo;// 信用卡号
	
	private Integer cvv;// 信用卡背后的cvv2号码
	
	private Integer expirationYear;// 信用卡有效期年份【4位】
	
	private String expirationMonth;// 信用卡有效期月份【2位】
	
	private String billFirstName;// 信用卡账单人名字
	
	private String billLastName;// 信用卡账单人姓氏
	
	private String billAddress;// 账单人地址 广州市天河区幸福大街2006号
	
	private String billCity;// 账单地址 账单人城市 广州市
	
	private String billState;// 账单人 州/省 当国家是AU、CA、JP、US四个国家时，州需要传递州代码，其他国家州传递“--”
	
	private String billCountry;// 账单人国家 US
	
	private Integer billZip;// 账单人邮编 89878
	
	private String shipFirstName;// 收货人名字 当serviceType为0时该参数必传
	
	private String shipLastName;// 收货人姓氏 当serviceType为0时该参数必传
	
	private String shipAddress;// 收货地址 当serviceType为0时该参数必传
	
	private String shipCity;// 收货城市 当serviceType为0时该参数必传
	
	private String shipState;//收货 州/省 当国家是AU、CA、JP、US四个国家时，州需要传递州代码，其他国家州请传递--【当serviceType为0时该参数必传】
	
	private String shipCountry;// 收货国家 当serviceType为0时该参数必传
	
	private Integer shipZip;// 收货邮编 当serviceType为0时该参数必传
	
	private String phone;// 持卡人电话
	
	private String email;// 持卡人邮箱
	
	private String acceptLanguage;// 持卡人浏览器语言
	
	private String userAgent;// 持卡人浏览器用户代理信息

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

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

	public String getSecurityValue() {
		return securityValue;
	}

	public void setSecurityValue(String securityValue) {
		this.securityValue = securityValue;
	}

	public String getReturnURL() {
		return returnURL;
	}

	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}

	public String getAsynReturnURL() {
		return asynReturnURL;
	}

	public void setAsynReturnURL(String asynReturnURL) {
		this.asynReturnURL = asynReturnURL;
	}

	public String getGoodsJson() {
		return goodsJson;
	}

	public void setGoodsJson(String goodsJson) {
		this.goodsJson = goodsJson;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPayIp() {
		return payIp;
	}

	public void setPayIp(String payIp) {
		this.payIp = payIp;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public Integer getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	
}
