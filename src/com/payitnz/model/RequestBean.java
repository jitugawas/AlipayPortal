package com.payitnz.model;

public class RequestBean 
{
	// Common // 
	int id;
	String userid ;
	String url;
	String Amount;
	String mcPartnerTransId;
	// flo2cash // 
	
	

	String cmd;
	String account_id;
	String custom_data;
	String return_url;
	String f2c_notification_url;
	String header_image;
	String header_bottom_border;
	String header_background_colour;
	String store_card;
	String display_customer_email;
	String reference ,particular;

	public String getMcPartnerTransId() {
		return mcPartnerTransId;
	}

	public void setMcPartnerTransId(String mcPartnerTransId) {
		this.mcPartnerTransId = mcPartnerTransId;
	}


	public String getF2c_notification_url() {
		return f2c_notification_url;
	}

	public void setF2c_notification_url(String f2c_notification_url) {
		this.f2c_notification_url = f2c_notification_url;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}



	// poli // 
	
	String CurrencyCode;
	String MerchantReference;
	String MerchantHomepageURL;
	String SuccessURL;
	String FailureURL;
	String CancellationURL;
	String poli_NotificationURL;

	public String getPoli_NotificationURL() {
		return poli_NotificationURL;
	}

	public void setPoli_NotificationURL(String poli_NotificationURL) {
		this.poli_NotificationURL = poli_NotificationURL;
	}

	public String getCurrencyCode() {
		return CurrencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}

	public String getMerchantReference() {
		return MerchantReference;
	}

	public void setMerchantReference(String merchantReference) {
		MerchantReference = merchantReference;
	}

	public String getMerchantHomepageURL() {
		return MerchantHomepageURL;
	}

	public void setMerchantHomepageURL(String merchantHomepageURL) {
		MerchantHomepageURL = merchantHomepageURL;
	}

	public String getSuccessURL() {
		return SuccessURL;
	}

	public void setSuccessURL(String successURL) {
		SuccessURL = successURL;
	}

	public String getFailureURL() {
		return FailureURL;
	}

	public void setFailureURL(String failureURL) {
		FailureURL = failureURL;
	}

	public String getCancellationURL() {
		return CancellationURL;
	}

	public void setCancellationURL(String cancellationURL) {
		CancellationURL = cancellationURL;
	}



	
	// CUP // 
	
	String backEndUrl;
	String charset;
	String commodityDiscount;
	String commodityName;
	String commodityQuantity;
	String commodityUnitPrice;
	String commodityUrl;
	String customerIp;
	String customerName;
	String defaultBankNumber;
	String defaultPayType ;
	String frontEndUrl;
	String merAbbr ;
	String merId ;
	String merReserved;
	String orderAmount;
	String orderCurrency; 
	String orderNumber;
	String orderTime;
	String origQid;
	String signMethod;
	String signature;
	String transTimeout;
	String transType;
	String transferFee;
	String version ;

	public String getBackEndUrl() {
		return backEndUrl;
	}

	public void setBackEndUrl(String backEndUrl) {
		this.backEndUrl = backEndUrl;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getCommodityDiscount() {
		return commodityDiscount;
	}

	public void setCommodityDiscount(String commodityDiscount) {
		this.commodityDiscount = commodityDiscount;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getCommodityQuantity() {
		return commodityQuantity;
	}

	public void setCommodityQuantity(String commodityQuantity) {
		this.commodityQuantity = commodityQuantity;
	}

	public String getCommodityUnitPrice() {
		return commodityUnitPrice;
	}

	public void setCommodityUnitPrice(String commodityUnitPrice) {
		this.commodityUnitPrice = commodityUnitPrice;
	}

	public String getCommodityUrl() {
		return commodityUrl;
	}

	public void setCommodityUrl(String commodityUrl) {
		this.commodityUrl = commodityUrl;
	}

	public String getCustomerIp() {
		return customerIp;
	}

	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDefaultBankNumber() {
		return defaultBankNumber;
	}

	public void setDefaultBankNumber(String defaultBankNumber) {
		this.defaultBankNumber = defaultBankNumber;
	}

	public String getDefaultPayType() {
		return defaultPayType;
	}

	public void setDefaultPayType(String defaultPayType) {
		this.defaultPayType = defaultPayType;
	}

	public String getFrontEndUrl() {
		return frontEndUrl;
	}

	public void setFrontEndUrl(String frontEndUrl) {
		this.frontEndUrl = frontEndUrl;
	}

	public String getMerAbbr() {
		return merAbbr;
	}

	public void setMerAbbr(String merAbbr) {
		this.merAbbr = merAbbr;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getMerReserved() {
		return merReserved;
	}

	public void setMerReserved(String merReserved) {
		this.merReserved = merReserved;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderCurrency() {
		return orderCurrency;
	}

	public void setOrderCurrency(String orderCurrency) {
		this.orderCurrency = orderCurrency;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrigQid() {
		return origQid;
	}

	public void setOrigQid(String origQid) {
		this.origQid = origQid;
	}

	public String getSignMethod() {
		return signMethod;
	}

	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTransTimeout() {
		return transTimeout;
	}

	public void setTransTimeout(String transTimeout) {
		this.transTimeout = transTimeout;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getTransferFee() {
		return transferFee;
	}

	public void setTransferFee(String transferFee) {
		this.transferFee = transferFee;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getCustom_data() {
		return custom_data;
	}

	public void setCustom_data(String custom_data) {
		this.custom_data = custom_data;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}


	public String getHeader_image() {
		return header_image;
	}

	public void setHeader_image(String header_image) {
		this.header_image = header_image;
	}

	public String getHeader_bottom_border() {
		return header_bottom_border;
	}

	public void setHeader_bottom_border(String header_bottom_border) {
		this.header_bottom_border = header_bottom_border;
	}

	public String getHeader_background_colour() {
		return header_background_colour;
	}

	public void setHeader_background_colour(String header_background_colour) {
		this.header_background_colour = header_background_colour;
	}

	public String getStore_card() {
		return store_card;
	}

	public void setStore_card(String store_card) {
		this.store_card = store_card;
	}

	public String getDisplay_customer_email() {
		return display_customer_email;
	}

	public void setDisplay_customer_email(String display_customer_email) {
		this.display_customer_email = display_customer_email;
	}

	

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getParticular() {
		return particular;
	}

	public void setParticular(String particular) {
		this.particular = particular;
	}
	
}
