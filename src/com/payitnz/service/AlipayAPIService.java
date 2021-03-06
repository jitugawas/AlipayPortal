package com.payitnz.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.payitnz.model.AlipayAPIRequest;
import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.GenericAPIResponse;
import com.payitnz.model.RequestBean;
import com.payitnz.model.User;

public interface AlipayAPIService {

    void saveOrUpdate(AlipayAPIRequest alipayAPIRequest);

    void saveOrUpdate(AlipayAPIResponse alipayAPIResponse);

    boolean authenticateUser(AlipayWalletVO alipayWalletVO, String ipAddress, String sender, GenericAPIResponse genericAPIResponse);

    Object[] createPaymentTransaction(AlipayWalletVO alipayWalletVO, String ipAddress, String sender);

    boolean callAlipayPaymentAPI(Object returnParams, AlipayWalletVO alipayWalletVO, String ipAddress, String sender);
    
    AlipayAPIResponse callAlipayReverseAPI(Object returnParams, AlipayWalletVO alipayWalletVO, String ipAddress, String sender);
    
    AlipayAPIResponse callAlipayCancelAPI(Object returnParams, AlipayWalletVO alipayWalletVO, String ipAddress, String sender);
    AlipayAPIResponse callAlipayCancelAPIWeb(Object returnParams, AlipayWalletVO alipayWalletVO, String ipAddress, String sender);
    
    AlipayAPIResponse callAlipayRefundAPI(Object returnParams, AlipayWalletVO alipayWalletVO, String ipAddress, String sender);
    
    AlipayAPIResponse callAlipayQueryAPI(Object returnParams, AlipayWalletVO alipayWalletVO, String ipAddress, String sender);

    Object[] createReverseTransaction(AlipayWalletVO alipayWalletVO, String ipAddress, String sender);

    Object[] createQueryTransaction(AlipayWalletVO alipayWalletVO, String ipAddress, String sender);

    Object[] createRefundTransaction(AlipayWalletVO alipayWalletVO, String ipAddress, String sender);

    Object[] createCancelTransaction(AlipayWalletVO alipayWalletVO, String ipAddress, String sender);

    AlipayAPIResponse getPaymentTransactionDetails(AlipayWalletVO alipayWalletVO, String ipAddress, String sender);
    
    List<GenericAPIResponse>  getPaymentTransactionByCriteria(AlipayWalletVO alipayWalletVO, String ipAddress, String sender);

    void saveFile(String mcTransactionId, MultipartFile file, String ipAddress, String sender);

    String getMerchantCompanyName(String infidigiAccountId, String infidigiUserID);

    byte[] getFile(String mcTransactionId);
	
	void UpdateUser(User user);

    List<AlipayAPIResponse> getTransactionList(AlipayWalletVO alipayWalletVO, String ipAddress, String sender);
    
    List<AlipayAPIRequest> getTransactionListOnMap(AlipayWalletVO alipayWalletVO, String ipAddress, String sender);

	AlipayAPIResponse getTransactionDetails(String mcPartnerTransId);

	List<User> getUserList(String infidigiAccountId);
	String getMerchantName(String infidigiAccountId, String infidigiUserID);
	
	List<AlipayAPIResponse> getTransactionDetailsByCriteriaWeb(AlipayWalletVO alipayWalletVO, String ipAddress, String sender);

	AlipayAPIResponse callAlipayRefundAPIWeb(Object finalURL, AlipayWalletVO alipayWalletVO, String ipAddress,
			String sender);

	String processAlipay(AlipayWalletVO shoppingCart, Model model, AlipayWalletVO alipay, String ipAddress,
			String sender, HttpServletRequest request);

	List<AlipayAPIResponse> getRefundTransactionDetailsByCriteriaWeb(AlipayWalletVO alipayWalletVO,String ipaddress,String sender);

	List<AlipayAPIResponse> getTransactionsOfID(String mcPartnerTransId);

	Model processF2C(AlipayWalletVO f2c, Model model, AlipayWalletVO alipay, String ipAddress, String sender,
			HttpServletRequest request);

	String processPoli(AlipayWalletVO alipayWalletVO, Model model, AlipayWalletVO poli, String ipAddress, String sender,
			HttpServletRequest request);
	
	void savef2cReq(RequestBean alipayAPIRequest);

	void savePoliReq(RequestBean alipayAPIRequest);

	void saveCUPReq(RequestBean alipayAPIRequest);
//	User getUser(String infidigiUserID);

	String processDPS(AlipayWalletVO alipay, Model model, AlipayWalletVO dps, String ipAddress, String sender,
			HttpServletRequest request);

//	SOAPMessage createSOAPRequest();

	void printSOAPResponse(SOAPMessage soapResponse) throws Exception;
}