package com.payitnz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.payitnz.dao.AlipayReconcillationDao;
import com.payitnz.dao.AlipayReconcillationDaoImpl;
import com.payitnz.model.ReconcillationBean;
import com.payitnz.model.ReconcillationFileBean;
import com.payitnz.model.TransactionBean;

@Service("AlipayReconcillationService")
public class AlipayReconcillationServiceImpl implements AlipayReconcillationService{
	
	AlipayReconcillationDao alipayReconcillationDao;
	
	@Override
	public void saveOrUpdate(ReconcillationBean reconcileBean) {
		// TODO Auto-generated method stub
	
		System.out.println("Bean:"+reconcileBean.getPartnerTransactionId());
		alipayReconcillationDao.save(reconcileBean);
	}	
	
	@Autowired
    public void setAlipayReconcillationDao(AlipayReconcillationDao alipayReconcillationDao) {
        this.alipayReconcillationDao = alipayReconcillationDao;
    }

	@Override
	public void validateTransaction(ReconcillationBean reconcileBean) {
		// TODO Auto-generated method stub
		alipayReconcillationDao.validateTransaction(reconcileBean);
	}

	@Override
	public List<ReconcillationBean> getReconciledTransactionByDate(String date) {
		// TODO Auto-generated method stub
		return alipayReconcillationDao.getReconciledTransactionByDate(date);
	}

	@Override
	public void importTransactions(TransactionBean transactionBean) {
		// TODO Auto-generated method stub
		alipayReconcillationDao.importTransaction(transactionBean);
	}

	@Override
	public int saveReconcillationFile(ReconcillationFileBean bean) {
		// TODO Auto-generated method stub
		return this.alipayReconcillationDao.saveReconcillationFile(bean);
	}

	@Override
	public void updateReconcillation(ReconcillationFileBean settlementFileBean) {
		// TODO Auto-generated method stub
		this.alipayReconcillationDao.updateReconcillation(settlementFileBean);
	}
	
}
