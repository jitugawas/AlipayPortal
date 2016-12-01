package com.payitnz.scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.mkyong.common.SFTPpullsshkeys;
import com.payitnz.config.DynamicPaymentConstant;
import com.payitnz.dao.SettlementDaoImpl;
import com.payitnz.model.SettlementBean;
import com.payitnz.model.SettlementFileBean;
import com.payitnz.web.DashboardController;

public class ScheduledSettlementTask extends TimerTask{

	SettlementDaoImpl alipaySettlementService = new SettlementDaoImpl();
	
	SettlementFileBean settlementFileBean= new SettlementFileBean();
	
	private static final Logger logger = Logger.getLogger(DashboardController.class);
		
	private SFTPpullsshkeys sftpObj = new SFTPpullsshkeys();
	
	// Add your task here
	public void run() {
		 
		 Date now = new Date(); // initialize date
		 System.out.println("Time is :" + now); // Display current time
		
		 this.processSettlementFile();
	}
	
	public void processSettlementFile(){
		
		 BufferedReader br;
	     String line = "";	   
	  
		 boolean dowloaded = sftpObj.downloadAlipaySettlementFile();
		 
		 if(dowloaded){
			 
			 String generatedFile = DynamicPaymentConstant.getCurrentAlipaySettlementFile();   
			 File newFile = new File(DynamicPaymentConstant.FTP_DESTINATION_PATH+generatedFile); 
			 
			 if (newFile.isFile() && newFile.canRead() ) {
				try {						
						   			
					logger.info("Server File Location=" + newFile.getAbsolutePath());						
					
					 br = new BufferedReader(new FileReader(newFile));
					 SettlementBean settlementBean = new SettlementBean();
		            
		            //HttpSession session = request.getSession(true);
		            int adminId =1; //(int)session.getAttribute("user_id");
		            int transCount = 0;
		            
		       	 	Date settlementDate = new Date();
	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
	                String sDateString = sdf.format(settlementDate);
		            
		            settlementFileBean.setFileName(generatedFile);
		            settlementFileBean.setTransactionCount(transCount);
		            settlementFileBean.setUploadedDate(sDateString);
		            settlementFileBean.setSettlementStatus(DynamicPaymentConstant.ALIPAY_SETTLEMENT_NOTVALIDATED);
		            int lastId = alipaySettlementService.logFile(settlementFileBean);
		            
					int i = 0; 
		            while ((line = br.readLine()) != null) {
		            	i++;
		            	if(i>2){
			            											
			                String[] settlementArray = line.split("\\|");
		
			                //System.out.println("line :"+line.toString());
			                
			                Date myDate = new Date();
			                sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
			                String myDateString = sdf.format(myDate);			                		             
			                
			                settlementBean.setOwnerId(adminId);
			                settlementBean.setPartnerTransactionId(settlementArray[0]);
			                settlementBean.setTransactionId(settlementArray[1]);
			                settlementBean.setAmount(settlementArray[2]);
			                settlementBean.setRmbAmount(settlementArray[3]);
			                settlementBean.setFee(settlementArray[4]);
			                settlementBean.setSettlement(settlementArray[5]);
			                settlementBean.setRmbSettlement(settlementArray[6]);
			                settlementBean.setCurrency(settlementArray[7]);
			                settlementBean.setRate(settlementArray[8]);
			                settlementBean.setPaymentTime(settlementArray[9]);
			                settlementBean.setSettlementTime(settlementArray[10]);
			                settlementBean.setType(settlementArray[11]); 
			                settlementBean.setStatus(settlementArray[12]);				               
			                settlementBean.setRemark(settlementArray[13]);
			                settlementBean.setUploadedDate(myDateString);
			                settlementBean.setSettlementId(lastId);
			                
			                alipaySettlementService.saveOrUpdate(settlementBean);			                	                
			                transCount++;
		            	}
		            }	
		           			           
		           settlementFileBean.setTransactionCount(transCount);
		           settlementFileBean.setSettlementStatus(DynamicPaymentConstant.SETTLEMENT_PROCESSED);
		           alipaySettlementService.updateSettlement(settlementFileBean);
		           
		           logger.info("Processed "+transCount+"records from file :"	+ newFile.getAbsolutePath());
		           
				}catch(Exception exp){
					exp.printStackTrace();
					logger.error(exp.getMessage());
				}
		 	}	
		}else{
			logger.error("Failed to process new settlement file from alipay server!");
		}
	}
	
}
