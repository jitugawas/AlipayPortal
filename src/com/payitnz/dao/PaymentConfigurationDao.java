package com.payitnz.dao;

import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayWalletVO;

public interface PaymentConfigurationDao {

	void SaveCUPRecord(AlipayWalletVO alipayWalletVO);

	AlipayWalletVO getCUPRecord(AlipayWalletVO alipayWalletVO);

	void UpdateCUPRecord(AlipayWalletVO alipayWalletVO);

	void SaveDPSRecord(AlipayWalletVO alipayWalletVO);

	AlipayWalletVO getDPSRecord(AlipayWalletVO alipayWalletVO);

	void UpdateDPSRecord(AlipayWalletVO alipayWalletVO);

	AlipayWalletVO getPoliRecord(AlipayWalletVO payConnection);

	void SavePoliRecord(AlipayWalletVO payData);

	void UpdatePoliRecord(AlipayWalletVO payData);

	AlipayWalletVO getF2CRecord(AlipayWalletVO payConnection);

	void UpdateF2CRecord(AlipayWalletVO payData);

	void SaveF2CRecord(AlipayWalletVO payData);

	public AlipayAPIResponse getTransactionByID(AlipayWalletVO paybean);
}
