package com.payitnz.model;

public class SettlementFileBean {

	private int id;
	private String fileName;
	private int transactionCount;
	private String uploadedDate;
	private int settlementStatus;
	private String creditFile;
	private String startDate;
	private String endDate;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public int getTransactionCount() {
		return transactionCount;
	}
	
	public void setTransactionCount(int transactionCount) {
		this.transactionCount = transactionCount;
	}
	
	public String getUploadedDate() {
		return uploadedDate;
	}
	
	public void setUploadedDate(String uploadedDate) {
		this.uploadedDate = uploadedDate;
	}
	
	public int getSettlementStatus() {
		return settlementStatus;
	}
	
	public void setSettlementStatus(int settlementStatus) {
		this.settlementStatus = settlementStatus;
	}

	public String getCreditFile() {
		return creditFile;
	}

	public void setCreditFile(String creditFile) {
		this.creditFile = creditFile;
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
	
}
