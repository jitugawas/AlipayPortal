package com.payitnz.model;


import java.util.Date;
import java.util.List;

public class GenericAPIResponse {
    Integer id = 0;

    String merchantTransactionId = "";
    
    String merchantRefundId = "";
    
    String merchantCompany = "";
    
    String infidigiAccountId = "";

    String infidigiUserId = "";

    String mobileCallId = "";

    String statusCode = "";
    
    String message = "";

    int userID = 0;

    String status;

	String amount;

	String TransactionDate;
	
	String trDate;
	
	public String getTrDate() {
		return trDate;
	}

	public void setTrDate(String trDate) {
		this.trDate = trDate;
	}

	String latitude;

	String longitude;
	
	String firstName;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
	public String getTransactionDate() {
		return TransactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		TransactionDate = transactionDate;
	}

    public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

    List<GenericAPIResponse> transactionList;
    
    public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
    public List<GenericAPIResponse> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<GenericAPIResponse> transactionList) {
		this.transactionList = transactionList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantTransactionId() {
        return merchantTransactionId;
    }

    public void setMerchantTransactionId(String merchantTransactionId) {
        this.merchantTransactionId = merchantTransactionId;
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

    public String getMobileCallId() {
        return mobileCallId;
    }

    public void setMobileCallId(String mobileCallId) {
        this.mobileCallId = mobileCallId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMerchantRefundId() {
        return merchantRefundId;
    }

    public void setMerchantRefundId(String merchantRefundId) {
        this.merchantRefundId = merchantRefundId;
    }

    public String getMerchantCompany() {
        return merchantCompany;
    }

    public void setMerchantCompany(String merchantCompany) {
        this.merchantCompany = merchantCompany;
    }

    
   
}
