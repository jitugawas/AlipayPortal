package com.payitnz.model;

import java.sql.Timestamp;

public class AlipayWalletVO {

    String particular;

    String amount;

    String infidigiAccountId; // Unique id in Payitnz to identify each merchant.

    String infidigiUserId;

    String infidigiPassword;
    
    String mobileCallId;

    String merchantTransactionId; // Auto generated id to identify each transaction uniquely.
        
    String alipayTransactionId;
    
    String reference;

    String comment;
    
    String refundReason;

    String email;

    String mobile;

    String latitude;
    
    String longitude;

    String barcode;

    String apiResponse;

    String imageAvailable;

    String buyerIdentityCode;

    String buyerIdentityType;
    
    String startDate;
    
    String endDate;

	String userID;
    
    String msg;
    
    String date_and_time;
    
    String payitnz_id;
    
    String sortType;
    
	String type;
	
	String regID;
	
    String pgPartnerTransId = "";
	
    String merchant_name;
    
    String service;

	String partner_key;
	
	String charSet;
	
	String currency;
	
	String return_url;
	
	String user_id;
	
	String channel;
	
	String status;
	
	Timestamp created_date;
	
	String merchant_id;
	
	String mcc;
	
	String commodity_url;
	
	Timestamp DPScreated_date;
	
	Timestamp poli_created_date;
	
	String F2c_service;
	
	Timestamp F2c_created_date;
	
	String gateway;
	
	String transaction_type;
	
	String paymentMethod;
	
	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public Timestamp getF2c_created_date() {
		return F2c_created_date;
	}

	public void setF2c_created_date(Timestamp f2c_created_date) {
		F2c_created_date = f2c_created_date;
	}

	public String getF2c_service() {
		return F2c_service;
	}

	public void setF2c_service(String f2c_service) {
		F2c_service = f2c_service;
	}

	public Timestamp getPoli_created_date() {
		return poli_created_date;
	}

	public void setPoli_created_date(Timestamp poli_created_date) {
		this.poli_created_date = poli_created_date;
	}

	public Timestamp getDPScreated_date() {
		return DPScreated_date;
	}

	public void setDPScreated_date(Timestamp dPScreated_date) {
		DPScreated_date = dPScreated_date;
	}

	public String getBillingID() {
		return billingID;
	}

	public void setBillingID(String billingID) {
		this.billingID = billingID;
	}


	String billingID;
	
	///////////////////////////////
	String f2c_account_id,f2c_secret_key,f2c_merchant_reference,f2c_return_url,f2c_notification_url;
	
	String header_image,header_bottom_border_color,header_background_color,f2c_custom_data,store_card,display_customer_email;
	
	String f2c_payment_method;
	////////////////////////////////////////
	String PxPayUserId,PxPayKey,PxPayUrl,DPStransaction_type,DPScurrency,DPSsuccess_url,DPSfailure_url;
	Boolean dpsvisa,dpsamerican_express,dpsdinner_club;
	


	////////////////////////////////////////////////////////
	String poli_account_id,password,currency_code,poli_merchant_reference,merchant_reference_format,merchant_data,poli_homepage_url,poli_success_url,poli_failure_url;
	String poli_cancellation_url,poli_notification_url,poli_timeout,fi_code,company_code;
	


	public String getPoli_account_id() {
		return poli_account_id;
	}

	public void setPoli_account_id(String poli_account_id) {
		this.poli_account_id = poli_account_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public String getPoli_merchant_reference() {
		return poli_merchant_reference;
	}

	public void setPoli_merchant_reference(String poli_merchant_reference) {
		this.poli_merchant_reference = poli_merchant_reference;
	}

	public String getMerchant_reference_format() {
		return merchant_reference_format;
	}

	public void setMerchant_reference_format(String merchant_reference_format) {
		this.merchant_reference_format = merchant_reference_format;
	}

	public String getMerchant_data() {
		return merchant_data;
	}

	public void setMerchant_data(String merchant_data) {
		this.merchant_data = merchant_data;
	}

	public String getPoli_homepage_url() {
		return poli_homepage_url;
	}

	public void setPoli_homepage_url(String poli_homepage_url) {
		this.poli_homepage_url = poli_homepage_url;
	}

	public String getPoli_success_url() {
		return poli_success_url;
	}

	public void setPoli_success_url(String poli_success_url) {
		this.poli_success_url = poli_success_url;
	}

	public String getPoli_failure_url() {
		return poli_failure_url;
	}

	public void setPoli_failure_url(String poli_failure_url) {
		this.poli_failure_url = poli_failure_url;
	}

	public String getPoli_cancellation_url() {
		return poli_cancellation_url;
	}

	public void setPoli_cancellation_url(String poli_cancellation_url) {
		this.poli_cancellation_url = poli_cancellation_url;
	}

	public String getPoli_notification_url() {
		return poli_notification_url;
	}

	public void setPoli_notification_url(String poli_notification_url) {
		this.poli_notification_url = poli_notification_url;
	}

	public String getPoli_timeout() {
		return poli_timeout;
	}

	public void setPoli_timeout(String poli_timeout) {
		this.poli_timeout = poli_timeout;
	}

	public String getFi_code() {
		return fi_code;
	}

	public void setFi_code(String fi_code) {
		this.fi_code = fi_code;
	}

	public String getCompany_code() {
		return company_code;
	}

	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}

	
	public String getPxPayUserId() {
		return PxPayUserId;
	}

	public void setPxPayUserId(String pxPayUserId) {
		PxPayUserId = pxPayUserId;
	}

	public String getPxPayKey() {
		return PxPayKey;
	}

	public void setPxPayKey(String pxPayKey) {
		PxPayKey = pxPayKey;
	}

	public String getPxPayUrl() {
		return PxPayUrl;
	}

	public void setPxPayUrl(String pxPayUrl) {
		PxPayUrl = pxPayUrl;
	}

	public String getDPStransaction_type() {
		return DPStransaction_type;
	}

	public void setDPStransaction_type(String dPStransaction_type) {
		DPStransaction_type = dPStransaction_type;
	}

	public String getDPScurrency() {
		return DPScurrency;
	}

	public void setDPScurrency(String dPScurrency) {
		DPScurrency = dPScurrency;
	}

	public String getDPSsuccess_url() {
		return DPSsuccess_url;
	}

	public void setDPSsuccess_url(String dPSsuccess_url) {
		DPSsuccess_url = dPSsuccess_url;
	}

	public String getDPSfailure_url() {
		return DPSfailure_url;
	}

	public void setDPSfailure_url(String dPSfailure_url) {
		DPSfailure_url = dPSfailure_url;
	}

	public Boolean getDpsvisa() {
		return dpsvisa;
	}

	public void setDpsvisa(Boolean dpsvisa) {
		this.dpsvisa = dpsvisa;
	}

	public Boolean getDpsamerican_express() {
		return dpsamerican_express;
	}

	public void setDpsamerican_express(Boolean dpsamerican_express) {
		this.dpsamerican_express = dpsamerican_express;
	}

	public Boolean getDpsdinner_club() {
		return dpsdinner_club;
	}

	public void setDpsdinner_club(Boolean dpsdinner_club) {
		this.dpsdinner_club = dpsdinner_club;
	}

	
	boolean visa; 
	public boolean isVisa() {
		return visa;
	}

	public void setVisa(boolean visa) {
		this.visa = visa;
	}

	public boolean isAmerican_express() {
		return american_express;
	}

	public void setAmerican_express(boolean american_express) {
		this.american_express = american_express;
	}

	public boolean isDinner_club() {
		return dinner_club;
	}

	public void setDinner_club(boolean dinner_club) {
		this.dinner_club = dinner_club;
	}

	boolean american_express; 
	boolean dinner_club; 
	/////////////////////////////////
	
	public String getHeader_image() {
		return header_image;
	}

	public void setHeader_image(String header_image) {
		this.header_image = header_image;
	}

	public String getHeader_bottom_border_color() {
		return header_bottom_border_color;
	}

	public void setHeader_bottom_border_color(String header_bottom_border_color) {
		this.header_bottom_border_color = header_bottom_border_color;
	}

	public String getHeader_background_color() {
		return header_background_color;
	}

	public void setHeader_background_color(String header_background_color) {
		this.header_background_color = header_background_color;
	}

	public String getF2c_custom_data() {
		return f2c_custom_data;
	}

	public void setF2c_custom_data(String f2c_custom_data) {
		this.f2c_custom_data = f2c_custom_data;
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

	public String getF2c_payment_method() {
		return f2c_payment_method;
	}

	public void setF2c_payment_method(String f2c_payment_method) {
		this.f2c_payment_method = f2c_payment_method;
	}

	
	
	
	
	
	

	public String getF2c_account_id() {
		return f2c_account_id;
	}

	public void setF2c_account_id(String f2c_account_id) {
		this.f2c_account_id = f2c_account_id;
	}

	public String getF2c_secret_key() {
		return f2c_secret_key;
	}

	public void setF2c_secret_key(String f2c_secret_key) {
		this.f2c_secret_key = f2c_secret_key;
	}

	public String getF2c_merchant_reference() {
		return f2c_merchant_reference;
	}

	public void setF2c_merchant_reference(String f2c_merchant_reference) {
		this.f2c_merchant_reference = f2c_merchant_reference;
	}

	public String getF2c_return_url() {
		return f2c_return_url;
	}

	public void setF2c_return_url(String f2c_return_url) {
		this.f2c_return_url = f2c_return_url;
	}

	public String getF2c_notification_url() {
		return f2c_notification_url;
	}

	public void setF2c_notification_url(String f2c_notification_url) {
		this.f2c_notification_url = f2c_notification_url;
	}

	public String getCommodity_url() {
		return commodity_url;
	}

	public void setCommodity_url(String commodity_url) {
		this.commodity_url = commodity_url;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getMerchant_reserved_field() {
		return merchant_reserved_field;
	}

	public void setMerchant_reserved_field(String merchant_reserved_field) {
		this.merchant_reserved_field = merchant_reserved_field;
	}

	String timeout;
	
	String merchant_reserved_field;
	
	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	int id;
	
	int role_id;
	
	boolean success;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

		
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Timestamp currentTimestamp) {
		this.created_date = currentTimestamp;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCharSet() {
		return charSet;
	}

	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

	public String getPartner_key() {
		return partner_key;
	}

	public void setPartner_key(String partner_key) {
		this.partner_key = partner_key;
	}

    public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMerchant_name() {
		return merchant_name;
	}

	public void setMerchant_name(String merchant_name) {
		this.merchant_name = merchant_name;
	}

	public String getPgPartnerTransId() {
		return pgPartnerTransId;
	}

	public void setPgPartnerTransId(String pgPartnerTransId) {
		this.pgPartnerTransId = pgPartnerTransId;
	}

	public String getRegID() {
		return regID;
	}

	public void setRegID(String regID) {
		this.regID = regID;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDate_and_time() {
		return date_and_time;
	}

	public void setDate_and_time(String date_and_time) {
		this.date_and_time = date_and_time;
	}

	public String getPayitnz_id() {
		return payitnz_id;
	}

	public void setPayitnz_id(String payitnz_id) {
		this.payitnz_id = payitnz_id;
	}
    
    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInfidigiAccountId() {
        return infidigiAccountId;
    }

    public void setInfidigiAccountId(String infidigiAccountId) {
        this.infidigiAccountId = infidigiAccountId;
    }

    public String getInfidigiUserId() {
        return infidigiUserId;
    }

    public void setInfidigiUserId(String infidigiUserId) {
        this.infidigiUserId = infidigiUserId;
    }

    public String getInfidigiPassword() {
        return infidigiPassword;
    }

    public void setInfidigiPassword(String infidigiPassword) {
        this.infidigiPassword = infidigiPassword;
    }

    public String getMerchantTransactionId() {
        return merchantTransactionId;
    }

    public void setMerchantTransactionId(String merchantTransactionId) {
        this.merchantTransactionId = merchantTransactionId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(String apiResponse) {
        this.apiResponse = apiResponse;
    }

    public String getImageAvailable() {
        return imageAvailable;
    }

    public void setImageAvailable(String imageAvailable) {
        this.imageAvailable = imageAvailable;
    }

    public String getBuyerIdentityCode() {
        return buyerIdentityCode;
    }

    public void setBuyerIdentityCode(String buyerIdentityCode) {
        this.buyerIdentityCode = buyerIdentityCode;
    }

    public String getBuyerIdentityType() {
        return buyerIdentityType;
    }

    public void setBuyerIdentityType(String buyerIdentityType) {
        this.buyerIdentityType = buyerIdentityType;
    }

    public String getMobileCallId() {
        return mobileCallId;
    }

    public void setMobileCallId(String mobileCallId) {
        this.mobileCallId = mobileCallId;
    }

    public String getAlipayTransactionId() {
        return alipayTransactionId;
    }

    public void setAlipayTransactionId(String alipayTransactionId) {
        this.alipayTransactionId = alipayTransactionId;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
    
	//online payment
	
		String online_service;
		String alipay_online_partner_id = "";
		String alipay_online_partner_key = "";
		String online_character_set;
		String online_currency;
		String alipay_online_return_url;
		String alipay_online_notification_url;
		int order_valid_time;
		String subject;
		String sign_type;
		String url;
		int alipay_supported_method;
		Timestamp alipay_online_created_date;
		String alipay_online_cancellation_url;
		boolean online_status;

		public String getOnline_service() {
			return online_service;
		}

		public void setOnline_service(String online_service) {
			this.online_service = online_service;
		}

		public String getAlipay_online_partner_id() {
			return alipay_online_partner_id;
		}

		public void setAlipay_online_partner_id(String alipay_online_partner_id) {
			this.alipay_online_partner_id = alipay_online_partner_id;
		}

		public String getAlipay_online_partner_key() {
			return alipay_online_partner_key;
		}

		public void setAlipay_online_partner_key(String alipay_online_partner_key) {
			this.alipay_online_partner_key = alipay_online_partner_key;
		}

		public String getOnline_character_set() {
			return online_character_set;
		}

		public void setOnline_character_set(String online_character_set) {
			this.online_character_set = online_character_set;
		}

		public String getOnline_currency() {
			return online_currency;
		}

		public void setOnline_currency(String online_currency) {
			this.online_currency = online_currency;
		}

		public String getAlipay_online_return_url() {
			return alipay_online_return_url;
		}

		public void setAlipay_online_return_url(String alipay_online_return_url) {
			this.alipay_online_return_url = alipay_online_return_url;
		}

		public String getAlipay_online_notification_url() {
			return alipay_online_notification_url;
		}

		public void setAlipay_online_notification_url(String alipay_online_notification_url) {
			this.alipay_online_notification_url = alipay_online_notification_url;
		}

		public int getOrder_valid_time() {
			return order_valid_time;
		}

		public void setOrder_valid_time(int order_valid_time) {
			this.order_valid_time = order_valid_time;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getSign_type() {
			return sign_type;
		}

		public void setSign_type(String sign_type) {
			this.sign_type = sign_type;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public Timestamp getAlipay_online_created_date() {
			return alipay_online_created_date;
		}

		public void setAlipay_online_created_date(Timestamp alipay_online_created_date) {
			this.alipay_online_created_date = alipay_online_created_date;
		}

		public String getAlipay_online_cancellation_url() {
			return alipay_online_cancellation_url;
		}

		public void setAlipay_online_cancellation_url(String alipay_online_cancellation_url) {
			this.alipay_online_cancellation_url = alipay_online_cancellation_url;
		}

		public int getAlipay_supported_method() {
			return alipay_supported_method;
		}

		public void setAlipay_supported_method(int alipay_supported_method) {
			this.alipay_supported_method = alipay_supported_method;
		}

		public boolean isOnline_status() {
			return online_status;
		}

		public void setOnline_status(boolean online_status) {
			this.online_status = online_status;
		}

}
