package com.payitnz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payitnz.dao.AlipayTransactionDao;
import com.payitnz.dao.InfiUserDao;
import com.payitnz.dao.PaymentConfigurationDao;
import com.payitnz.dao.PaymentConfigurationDaoImpl;
import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayDashboardBean;
import com.payitnz.model.AlipayPaymentGatewayBean;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.EmailAlertsConfigBean;

@Service("DashboardService")
public class DashboardServiceImpl implements DashboardService{
	
	AlipayTransactionDao transactionDao;
	
	InfiUserDao userDao;
	
	@Autowired
	public void setAlipayTransactionDao(AlipayTransactionDao transactionDao){
		this.transactionDao = transactionDao;
	}
	
	PaymentConfigurationDao configDao;
	
	@Autowired
	public void setPaymentConfigurationDao(PaymentConfigurationDao configDao){
		this.configDao = configDao;
	}
	
	@Autowired
	public void setInfiUserDao(InfiUserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public List<AlipayDashboardBean> findAllTransDetails(AlipayDashboardBean search) {
		return transactionDao.findAllTransDetails(search);
	}

	@Override
	public List<AlipayPaymentGatewayBean> findAllPaymentMethods() {
		return transactionDao.findAllPaymentMethods();
	}

	@Override
	public boolean checkUserPassword(String password) {
		return userDao.CheckUserPassword(password);
	}

	@Override
	public List<AlipayAPIResponse> searchTransactionDataById(String transactionId) {
		return transactionDao.searchTransactionDataById(transactionId);
	}
	
	@Override
	public List<EmailAlertsConfigBean> getAdminAlertConfigDetails(int userId) {
		return userDao.getAdminAlertConfigDetails(userId);
	}

	@Override
	public void saveAdminAlertConfigDetails(EmailAlertsConfigBean alertForm) {
		userDao.saveAdminAlertConfigDetails(alertForm);
	}

	@Override
	public void updateAdminAlertConfigDetails(EmailAlertsConfigBean alertForm) {
		userDao.updateAdminAlertConfigDetails(alertForm);
		
	}
	
	@Override
	public void SaveCUPRecord(AlipayWalletVO alipayWalletVO) {
		configDao.SaveCUPRecord(alipayWalletVO);
	}
	
	@Override
	public AlipayWalletVO getCUPRecord(AlipayWalletVO alipayWalletVO) {
		return configDao. getCUPRecord(alipayWalletVO);
	}
	
	@Override
	public void UpdateCUPRecord(AlipayWalletVO alipayWalletVO) {
		configDao.UpdateCUPRecord(alipayWalletVO);
	}
	@Override
	public void SaveDPSRecord(AlipayWalletVO alipayWalletVO) {
		configDao.SaveDPSRecord(alipayWalletVO);
	}
	
	@Override
	public AlipayWalletVO getDPSRecord(AlipayWalletVO alipayWalletVO) {
		return configDao. getDPSRecord(alipayWalletVO);
	}
	
	@Override
	public void UpdateDPSRecord(AlipayWalletVO alipayWalletVO) {
		configDao.UpdateDPSRecord(alipayWalletVO);
	}
	
	@Override
	public void SavePoliRecord(AlipayWalletVO alipayWalletVO) {
		configDao.SavePoliRecord(alipayWalletVO);
	}
	@Override
	public void UpdatePoliRecord(AlipayWalletVO alipayWalletVO) {
		configDao.UpdatePoliRecord(alipayWalletVO);
	}
	@Override
	public AlipayWalletVO getPoliRecord(AlipayWalletVO alipayWalletVO) {
		return configDao. getPoliRecord(alipayWalletVO);
	}
	
	@Override
	public AlipayWalletVO getF2CRecord(AlipayWalletVO alipayWalletVO) {
		return configDao. getF2CRecord(alipayWalletVO);
	}
	@Override
	public void SaveF2CRecord(AlipayWalletVO alipayWalletVO) {
		configDao.SaveF2CRecord(alipayWalletVO);
	}
	@Override
	public void UpdateF2CRecord(AlipayWalletVO alipayWalletVO) {
		configDao.UpdateF2CRecord(alipayWalletVO);
	}
}
