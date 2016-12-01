package com.payitnz.model;

public class SettlementBean {
	
	private int id;
	private int ownerId;
	private String partnerTransactionId;
	private String transactionId;
	private String amount;
	private String rmbAmount;
	private String fee;
	private String settlement;
	private String rmbSettlement;
	private String currency;
	private String paymentTime;
	private String settlementTime;
	private String type;	
	private int settlementStatus;
	private String remark;
	private String uploadedDate;
	private String rate;
	private String status;
	private int settlementId;
	private String destinationAccount;
	private String merchantCompany;
	private String merchantEmail;
	
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public String getPartnerTransactionId() {
		return partnerTransactionId;
	}
	public void setPartnerTransactionId(String partnerTransactionId) {
		this.partnerTransactionId = partnerTransactionId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRmbAmount() {
		return rmbAmount;
	}
	public void setRmbAmount(String rmbAmount) {
		this.rmbAmount = rmbAmount;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getSettlement() {
		return settlement;
	}
	public void setSettlement(String settlement) {
		this.settlement = settlement;
	}
	public String getRmbSettlement() {
		return rmbSettlement;
	}
	public void setRmbSettlement(String rmbSettlement) {
		this.rmbSettlement = rmbSettlement;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}
	public String getSettlementTime() {
		return settlementTime;
	}
	public void setSettlementTime(String settlementTime) {
		this.settlementTime = settlementTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSettlementStatus() {
		return settlementStatus;
	}
	public void setSettlementStatus(int settlementStatus) {
		this.settlementStatus = settlementStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUploadedDate() {
		return uploadedDate;
	}
	public void setUploadedDate(String uploadedDate) {
		this.uploadedDate = uploadedDate;
	}
	public int getSettlementId() {
		return settlementId;
	}
	public void setSettlementId(int settlementId) {
		this.settlementId = settlementId;
	}
	public String getDestinationAccount() {
		return destinationAccount;
	}
	public void setDestinationAccount(String destinationAccount) {
		this.destinationAccount = destinationAccount;
	}
	public String getMerchantCompany() {
		return merchantCompany;
	}
	public void setMerchantCompany(String merchantCompany) {
		this.merchantCompany = merchantCompany;
	}
	public String getMerchantEmail() {
		return merchantEmail;
	}
	public void setMerchantEmail(String merchantEmail) {
		this.merchantEmail = merchantEmail;
	}
	
}
