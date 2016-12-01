package com.payitnz.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payitnz.dao.AlipayAPIDao;
import com.payitnz.dao.PaymentConfigurationDao;
import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.web.LoginController;

@Service("alipayPaymentService")
public class AlipayPaymentServiceImpl implements AlipayPaymentService {

	PaymentConfigurationDao paymentDao;
    final static Logger logger = Logger.getLogger(LoginController.class);
    @Autowired
    public void setPaymentDao(PaymentConfigurationDao paymentDao) {
        this.paymentDao = paymentDao;
    }
    
	@Override
	public AlipayAPIResponse getTransactionDEtialsById(AlipayWalletVO payBean) {
		// TODO Auto-generated method stub
		return paymentDao.getTransactionByID(payBean);
	}

}
