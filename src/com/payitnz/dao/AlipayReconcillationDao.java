package com.payitnz.dao;

import java.util.Date;
import java.util.List;

import com.payitnz.model.ReconcillationBean;
import com.payitnz.model.ReconcillationFileBean;
import com.payitnz.model.TransactionBean;;

public interface AlipayReconcillationDao {
	 void save(ReconcillationBean reconcileBean);	
	 void validateTransaction(ReconcillationBean reconcileBean);
	 List<ReconcillationBean> getReconciledTransactionByDate(String date);
	 void sendReconcillationEmail();
	 void importTransaction(TransactionBean transactionBean);
	 int saveReconcillationFile(ReconcillationFileBean bean);
	 public void updateReconcillation(ReconcillationFileBean settlementFileBean);
}
