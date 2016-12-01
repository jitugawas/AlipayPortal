package com.payitnz.dao;

import java.util.List;

import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayDashboardBean;
import com.payitnz.model.AlipayPaymentGatewayBean;

public interface AlipayTransactionDao {

	List<AlipayDashboardBean> findAllTransDetails(AlipayDashboardBean search);
	List<AlipayPaymentGatewayBean> findAllPaymentMethods();
	List<AlipayAPIResponse> searchTransactionDataById(String transactionId);

}
