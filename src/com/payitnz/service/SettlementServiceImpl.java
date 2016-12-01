package com.payitnz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payitnz.dao.SettlementDao;
import com.payitnz.model.SettlementBean;
import com.payitnz.model.SettlementFileBean;

@Service("SettlementService")
public class SettlementServiceImpl implements SettlementService {

	@Autowired
	SettlementDao settlementDao;
	
	@Autowired
	public void setSettlementService(SettlementDao settlementDao){
		this.settlementDao = settlementDao;
	}
	
	@Override
	public void saveOrUpdate(SettlementBean settlementBean) {
		// TODO Auto-generated method stub
		this.settlementDao.saveOrUpdate(settlementBean);
	}

	@Override
	public void validateSettlementTransactions(SettlementFileBean settlementFileBean) {
		// TODO Auto-generated method stub
		this.settlementDao.validateSettlementTransactions(settlementFileBean);
	}

	@Override
	public List<SettlementBean> getSettledTransactionByDate(String dateStart,String endDate) {
		// TODO Auto-generated method stub
		return this.settlementDao.getSettledTransactionByDate(dateStart,endDate);
	}

	@Override
	public int logFile(SettlementFileBean settlementFileBean) {
		return this.settlementDao.logFile(settlementFileBean);
		
	}

	@Override
	public boolean checkFile(SettlementFileBean settlementFileBean) {
		// TODO Auto-generated method stub
		return this.settlementDao.checkFile(settlementFileBean);
	}

	@Override
	public List<SettlementFileBean> getSettlementFiles(SettlementFileBean fileBean) {
		// TODO Auto-generated method stub
		return this.settlementDao.getSettlementFiles(fileBean);
	}

	@Override
	public void updateSettlement(SettlementFileBean settlementFileBean) {
		// TODO Auto-generated method stub
		this.settlementDao.updateSettlement(settlementFileBean);
	}

	@Override
	public List<SettlementBean> getSettledTransactions(SettlementFileBean fileBean) {
		// TODO Auto-generated method stub
		System.out.println("I am in tractiong et list");
		return this.settlementDao.getSettledTransactions(fileBean);
	}

	@Override
	public void deleteSettlementFileRecord(int id) {
		// TODO Auto-generated method stub
		this.settlementDao.deleteSettlementFileRecord(id);
	}

	@Override
	public SettlementFileBean getSettlementFileById(int id) {
		// TODO Auto-generated method stub
		return this.settlementDao.getSettlementFileById(id);
	}

	@Override
	public SettlementFileBean populateSettlementFileBean(int fileId) {
		// TODO Auto-generated method stub
		return this.settlementDao.populateSettlementFileBean(fileId);
	}

	@Override
	public void generateDirectCreditFile(SettlementBean bean) {
		// TODO Auto-generated method stub
		this.settlementDao.generateDirectCreditFile(bean);
	}

	@Override
	public SettlementFileBean getSettlementFile(SettlementFileBean settlementFileBean) {
		// TODO Auto-generated method stub
		return this.settlementDao.getSettlementFile(settlementFileBean);
	}

}
