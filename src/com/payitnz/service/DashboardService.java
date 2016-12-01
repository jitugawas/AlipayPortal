package com.payitnz.service;

import java.util.List;

import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayDashboardBean;
import com.payitnz.model.AlipayPaymentGatewayBean;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.EmailAlertsConfigBean;

public interface DashboardService {

	List<AlipayDashboardBean> findAllTransDetails(AlipayDashboardBean search);
	List<AlipayPaymentGatewayBean> findAllPaymentMethods();
	boolean checkUserPassword(String password);
	List<AlipayAPIResponse> searchTransactionDataById(String transactionId);
	List<EmailAlertsConfigBean> getAdminAlertConfigDetails(int userId);
	void saveAdminAlertConfigDetails(EmailAlertsConfigBean alertForm);
	void updateAdminAlertConfigDetails(EmailAlertsConfigBean alertForm);
	void SaveCUPRecord(AlipayWalletVO alipayWalletVO);
	AlipayWalletVO getCUPRecord(AlipayWalletVO alipayWalletVO);
	void UpdateCUPRecord(AlipayWalletVO alipayWalletVO);
	void SaveDPSRecord(AlipayWalletVO alipayWalletVO);
	AlipayWalletVO getDPSRecord(AlipayWalletVO alipayWalletVO);
	void UpdateDPSRecord(AlipayWalletVO alipayWalletVO);
	void SavePoliRecord(AlipayWalletVO alipayWalletVO);
	void UpdatePoliRecord(AlipayWalletVO alipayWalletVO);
	AlipayWalletVO getPoliRecord(AlipayWalletVO alipayWalletVO);
	AlipayWalletVO getF2CRecord(AlipayWalletVO alipayWalletVO);
	void SaveF2CRecord(AlipayWalletVO alipayWalletVO);
	void UpdateF2CRecord(AlipayWalletVO alipayWalletVO);

}
