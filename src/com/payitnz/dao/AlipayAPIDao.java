package com.payitnz.dao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dynamicpayment.paymentexpress.DPSRequestBean;
import com.payitnz.model.AlipayAPIRequest;
import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.GenericAPIResponse;
import com.payitnz.model.RequestBean;
import com.payitnz.model.User;

public interface AlipayAPIDao {

    void save(AlipayAPIRequest alipayAPIRequest);

    void save(AlipayAPIResponse alipayAPIResponse);
    
    void update(AlipayAPIResponse alipayAPIResponse);
    

    User getUser(String infidigiAccountId, String infidigiUserId, String infidigiPassword);

    AlipayAPIResponse getTransactionDetailsByPartnerTransactionId(String merchantTransactionId);

    List<GenericAPIResponse>  getTransactionDetailsByCriteria(AlipayWalletVO alipayWalletVO);

    void insertFile(String mcTransactionId, MultipartFile file, String ipAddress, String sender);

    String getMerchantCompanyName(String infidigiAccountId, String infidigiUserID);

    byte[] getFile(String mcTransactionId);

    void UpdateUser(User user);

    List<AlipayAPIResponse> getTransactionList(AlipayWalletVO alipayWalletVO, String ipAddress, String sender);
    
    List<AlipayAPIRequest> getTransactionListOnMap(AlipayWalletVO alipayWalletVO, String ipAddress, String sender);

	AlipayAPIResponse getTransactionDetails(String mcPartnerTransId);
	
	List<User> getUserList(String infidigiAccountId);

	String getMerchantName(String infidigiAccountId, String infidigiUserID);

	List<AlipayAPIResponse> getTransactionDetailsByCriteriaWeb(AlipayWalletVO alipayWalletVO);

	List<AlipayAPIResponse> getRefundTransactionDetailsByCriteriaWeb(AlipayWalletVO alipayWalletVO);

	List<AlipayAPIResponse> getTransactionsOfID(String mcPartnerTransId);

	void savef2C(RequestBean alipayAPIRequest);

	void savePolireq(RequestBean alipayAPIRequest);
	
	void saveCUPreq(RequestBean alipayAPIRequest);

	void save(DPSRequestBean dpsRequest);

//	User getUser(String infidigiUserID);

}