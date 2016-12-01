package com.payitnz.model;

import java.util.Date;

public class AlipayAPIResponse extends GenericAPIResponse {
    Integer id = 0;

    String dyMerchantId = "";

    String pgIsSuccess = "";

    String pgSign = "";

    String pgSignType = "";

    String pgResultCode = "";

    String pgError = "";
    
    String pgErrorDescription = "";

    String pgAlipayBuyerLoginId = "";

    String pgAlipayBuyerUserId = "";

    String pgPartnerTransId = "";
    
    Date pgTransactionDate = null;

    String pgAlipayTransId = "";
    
    String pgAlipayTransStatus = "";
    
    String pgAlipayPayTime = "";
    
    String pgAlipayReverseTime = "";
    
    String pgAlipayCancelTime = "";
    
    String pgOutTradeNo = "";
    
    String mcCurrency = "";
    
    String mcItemName = "";
    
    String mcReference = "";
    
    String mcComment = "";
    
    String mcLatitude = "";
    
    String mcLongitude = "";
    
    String mcEmail = "";
    
    String mcMobile = "";

    String mcTransAmount = "";

    double pgExchangeRate = 0;

    double pgTransAmountCny = 0;

    String requestTime = "";

    String ipAddress = "";

    String requestBy = "";

    String softDelete = "N";

    String transaction_type;
    
    String method_type;
    
    String remark;
    
    String channel;
    
    String pgMerchantTransactionId;
    
    String cardType;
    
    public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public AlipayAPIResponse[] getResList() {
		return resList;
	}

	public void setResList(AlipayAPIResponse[] resList) {
		this.resList = resList;
	}

	AlipayAPIResponse [] resList;
    public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public String getMethod_type() {
		return method_type;
	}

	public void setMethod_type(String method_type) {
		this.method_type = method_type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDyMerchantId() {
        return dyMerchantId;
    }

    public void setDyMerchantId(String dyMerchantId) {
        this.dyMerchantId = dyMerchantId;
    }

    public String getPgIsSuccess() {
        return pgIsSuccess;
    }

    public void setPgIsSuccess(String pgIsSuccess) {
        this.pgIsSuccess = pgIsSuccess;
    }

    public String getPgSign() {
        return pgSign;
    }

    public void setPgSign(String pgSign) {
        this.pgSign = pgSign;
    }

    public String getPgSignType() {
        return pgSignType;
    }

    public void setPgSignType(String pgSignType) {
        this.pgSignType = pgSignType;
    }

    public String getPgResultCode() {
        return pgResultCode;
    }

    public void setPgResultCode(String pgResultCode) {
        this.pgResultCode = pgResultCode;
    }

    public String getPgError() {
        return pgError;
    }

    public void setPgError(String pgError) {
        this.pgError = pgError;
    }

    public String getPgAlipayBuyerLoginId() {
        return pgAlipayBuyerLoginId;
    }

    public void setPgAlipayBuyerLoginId(String pgAlipayBuyerLoginId) {
        this.pgAlipayBuyerLoginId = pgAlipayBuyerLoginId;
    }

    public String getPgAlipayBuyerUserId() {
        return pgAlipayBuyerUserId;
    }

    public void setPgAlipayBuyerUserId(String pgAlipayBuyerUserId) {
        this.pgAlipayBuyerUserId = pgAlipayBuyerUserId;
    }

    public String getPgPartnerTransId() {
        return pgPartnerTransId;
    }

    public void setPgPartnerTransId(String pgPartnerTransId) {
        this.pgPartnerTransId = pgPartnerTransId;
    }

    public String getPgAlipayTransId() {
        return pgAlipayTransId;
    }

    public void setPgAlipayTransId(String pgAlipayTransId) {
        this.pgAlipayTransId = pgAlipayTransId;
    }

    public String getPgAlipayPayTime() {
        return pgAlipayPayTime;
    }

    public void setPgAlipayPayTime(String pgAlipayPayTime) {
        this.pgAlipayPayTime = pgAlipayPayTime;
    }

    public String getMcCurrency() {
        return mcCurrency;
    }

    public void setMcCurrency(String mcCurrency) {
        this.mcCurrency = mcCurrency;
    }

    public String getMcTransAmount() {
        return mcTransAmount;
    }

    public void setMcTransAmount(String mcTransAmount) {
        this.mcTransAmount = mcTransAmount;
    }

    public double getPgExchangeRate() {
        return pgExchangeRate;
    }

    public void setPgExchangeRate(double pgExchangeRate) {
        this.pgExchangeRate = pgExchangeRate;
    }

    public double getPgTransAmountCny() {
        return pgTransAmountCny;
    }

    public void setPgTransAmountCny(double pgTransAmountCny) {
        this.pgTransAmountCny = pgTransAmountCny;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public String getSoftDelete() {
        return softDelete;
    }

    public void setSoftDelete(String softDelete) {
        this.softDelete = softDelete;
    }

    public String getPgAlipayReverseTime() {
        return pgAlipayReverseTime;
    }

    public void setPgAlipayReverseTime(String pgAlipayReverseTime) {
        this.pgAlipayReverseTime = pgAlipayReverseTime;
    }

    public String getPgAlipayTransStatus() {
        return pgAlipayTransStatus;
    }

    public void setPgAlipayTransStatus(String pgAlipayTransStatus) {
        this.pgAlipayTransStatus = pgAlipayTransStatus;
    }

    public String getPgErrorDescription() {
        return pgErrorDescription;
    }

    public void setPgErrorDescription(String pgErrorDescription) {
        this.pgErrorDescription = pgErrorDescription;
    }

    public String getPgOutTradeNo() {
        return pgOutTradeNo;
    }

    public void setPgOutTradeNo(String pgOutTradeNo) {
        this.pgOutTradeNo = pgOutTradeNo;
    }

    public String getPgAlipayCancelTime() {
        return pgAlipayCancelTime;
    }

    public void setPgAlipayCancelTime(String pgAlipayCancelTime) {
        this.pgAlipayCancelTime = pgAlipayCancelTime;
    }

    public String getMcItemName() {
        return mcItemName;
    }

    public void setMcItemName(String mcItemName) {
        this.mcItemName = mcItemName;
    }

    public String getMcReference() {
        return mcReference;
    }

    public void setMcReference(String mcReference) {
        this.mcReference = mcReference;
    }

    public Date getPgTransactionDate() {
        return pgTransactionDate;
    }

    public void setPgTransactionDate(Date pgTransactionDate) {
        this.pgTransactionDate = pgTransactionDate;
    }

    public String getMcComment() {
        return mcComment;
    }

    public void setMcComment(String mcComment) {
        this.mcComment = mcComment;
    }

    public String getMcLatitude() {
        return mcLatitude;
    }

    public void setMcLatitude(String mcLatitude) {
        this.mcLatitude = mcLatitude;
    }

    public String getMcLongitude() {
        return mcLongitude;
    }

    public void setMcLongitude(String mcLongitude) {
        this.mcLongitude = mcLongitude;
    }

    public String getMcEmail() {
        return mcEmail;
    }

    public void setMcEmail(String mcEmail) {
        this.mcEmail = mcEmail;
    }

    public String getMcMobile() {
        return mcMobile;
    }

    public void setMcMobile(String mcMobile) {
        this.mcMobile = mcMobile;
    }

	public String getPgMerchantTransactionId() {
		return pgMerchantTransactionId;
	}

	public void setPgMerchantTransactionId(String pgMerchantTransactionId) {
		this.pgMerchantTransactionId = pgMerchantTransactionId;
	}
    
    

}
