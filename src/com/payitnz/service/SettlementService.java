package com.payitnz.service;

import java.util.List;

import com.payitnz.model.SettlementBean;
import com.payitnz.model.SettlementFileBean;

public interface SettlementService {

	void saveOrUpdate(SettlementBean settlementBean);
	void validateSettlementTransactions(SettlementFileBean settlementFileBean);
	List<SettlementBean> getSettledTransactionByDate(String startDate,String endDate);
	int logFile(SettlementFileBean settlementFileBean);
	public boolean checkFile(SettlementFileBean settlementFileBean);
	List<SettlementFileBean> getSettlementFiles(SettlementFileBean fileBean);
	void updateSettlement(SettlementFileBean settlementFileBean);
	List<SettlementBean> getSettledTransactions(SettlementFileBean settlementFileBean);
	void deleteSettlementFileRecord(int id);
	SettlementFileBean getSettlementFileById(int id);
	SettlementFileBean getSettlementFile(SettlementFileBean settlementFileBean);
	public SettlementFileBean populateSettlementFileBean(int fileId);
	public void generateDirectCreditFile(SettlementBean bean);
}
