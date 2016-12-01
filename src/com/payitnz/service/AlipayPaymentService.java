package com.payitnz.service;

import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayWalletVO;

public interface AlipayPaymentService  {
	AlipayAPIResponse getTransactionDEtialsById(AlipayWalletVO payBean);
}
