package com.payitnz.service;

import java.util.Date;
import java.util.List;

import com.payitnz.model.ReconcillationBean;
import com.payitnz.model.ReconcillationFileBean;
import com.payitnz.model.TransactionBean;

public interface AlipayReconcillationService {
	void saveOrUpdate(ReconcillationBean reconcileBean);
	void validateTransaction(ReconcillationBean reconcileBean);
	List<ReconcillationBean> getReconciledTransactionByDate(String date);
	void importTransactions(TransactionBean transactionBean);
	int saveReconcillationFile(ReconcillationFileBean bean);
	public void updateReconcillation(ReconcillationFileBean settlementFileBean);
}
