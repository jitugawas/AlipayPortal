package com.payitnz.model;

public class AlipayAPIRequest {
    Integer id = 0;

    String dyMerchantId = "";

    String pgService = "";

    String pgSign = "";

    String pgSignType = "";

    String pgPartnerId = "";

    String pgInputCharset = "";

    String pgAlipaySellerId = "";

    String requestTime = "";

    String ipAddress = "";

    String requestBy = "";

    String softDelete = "N";

    int mcQuantityCommodity = 0;
    
    String mcTransName = "";
    
    String mcReference = "";
    
    String mcComment = "";
    
    String mcLatitude = "";
    
    String mcLongitude = "";
    
    String mcEmail = "";
    
    String mcMobile = "";

    String mcPartnerTransId = "";
    
    String mcPartnerRefundId = "";
    
    String mcRefundReason = "";

    String mcCurrency = "";
    
    String pgSendFormat = "";

    double mcTransAmount = 0;

    String pgBuyerIdentityCode = "";

    String pgIdentityCodeType = "";

    String mcTransCreateTime = "";

    String mcMemo = "";

    String pgBizProduct = "";

    String pgExtendInfo = "";

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

    public String getPgService() {
        return pgService;
    }

    public void setPgService(String pgService) {
        this.pgService = pgService;
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

    public String getPgPartnerId() {
        return pgPartnerId;
    }

    public void setPgPartnerId(String pgPartnerId) {
        this.pgPartnerId = pgPartnerId;
    }

    public String getPgInputCharset() {
        return pgInputCharset;
    }

    public void setPgInputCharset(String pgInputCharset) {
        this.pgInputCharset = pgInputCharset;
    }

    public String getPgAlipaySellerId() {
        return pgAlipaySellerId;
    }

    public void setPgAlipaySellerId(String pgAlipaySellerId) {
        this.pgAlipaySellerId = pgAlipaySellerId;
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

    public int getMcQuantityCommodity() {
        return mcQuantityCommodity;
    }

    public void setMcQuantityCommodity(int mcQuantityCommodity) {
        this.mcQuantityCommodity = mcQuantityCommodity;
    }

    public String getMcPartnerTransId() {
        return mcPartnerTransId;
    }

    public void setMcPartnerTransId(String mcPartnerTransId) {
        this.mcPartnerTransId = mcPartnerTransId;
    }

    public String getMcCurrency() {
        return mcCurrency;
    }

    public void setMcCurrency(String mcCurrency) {
        this.mcCurrency = mcCurrency;
    }

    public double getMcTransAmount() {
        return mcTransAmount;
    }

    public void setMcTransAmount(double mcTransAmount) {
        this.mcTransAmount = mcTransAmount;
    }

    public String getPgBuyerIdentityCode() {
        return pgBuyerIdentityCode;
    }

    public void setPgBuyerIdentityCode(String pgBuyerIdentityCode) {
        this.pgBuyerIdentityCode = pgBuyerIdentityCode;
    }

    public String getPgIdentityCodeType() {
        return pgIdentityCodeType;
    }

    public void setPgIdentityCodeType(String pgIdentityCodeType) {
        this.pgIdentityCodeType = pgIdentityCodeType;
    }

    public String getMcTransCreateTime() {
        return mcTransCreateTime;
    }

    public void setMcTransCreateTime(String mcTransCreateTime) {
        this.mcTransCreateTime = mcTransCreateTime;
    }

    public String getMcMemo() {
        return mcMemo;
    }

    public void setMcMemo(String mcMemo) {
        this.mcMemo = mcMemo;
    }

    public String getPgBizProduct() {
        return pgBizProduct;
    }

    public void setPgBizProduct(String pgBizProduct) {
        this.pgBizProduct = pgBizProduct;
    }

    public String getPgExtendInfo() {
        return pgExtendInfo;
    }

    public void setPgExtendInfo(String pgExtendInfo) {
        this.pgExtendInfo = pgExtendInfo;
    }

    public String getMcTransName() {
        return mcTransName;
    }

    public void setMcTransName(String mcTransName) {
        this.mcTransName = mcTransName;
    }

    public String getPgSendFormat() {
        return pgSendFormat;
    }

    public void setPgSendFormat(String pgSendFormat) {
        this.pgSendFormat = pgSendFormat;
    }

    public String getMcPartnerRefundId() {
        return mcPartnerRefundId;
    }

    public void setMcPartnerRefundId(String mcPartnerRefundId) {
        this.mcPartnerRefundId = mcPartnerRefundId;
    }

    public String getMcRefundReason() {
        return mcRefundReason;
    }

    public void setMcRefundReason(String mcRefundReason) {
        this.mcRefundReason = mcRefundReason;
    }

    public String getMcReference() {
        return mcReference;
    }

    public void setMcReference(String mcReference) {
        this.mcReference = mcReference;
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
    
    

}
