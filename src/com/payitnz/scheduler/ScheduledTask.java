package com.payitnz.scheduler;

import java.util.TimerTask;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mkyong.common.FTPFunctions;
import com.mkyong.common.SFTPpullsshkeys;
import com.payitnz.config.DynamicPaymentConstant;
import com.payitnz.dao.AlipayReconcillationDaoImpl;
import com.payitnz.model.ReconcillationBean;
import com.payitnz.service.AlipayReconcillationService;
import com.payitnz.web.DashboardController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author 
 */
// Create a class extends with TimerTask
public class ScheduledTask extends TimerTask {

	Date now; // to display current time

	AlipayReconcillationDaoImpl alipayReconcillationService = new AlipayReconcillationDaoImpl();
	
	private static final Logger logger = LoggerFactory
			.getLogger(DashboardController.class);
	
	private SFTPpullsshkeys sftpObj = new SFTPpullsshkeys();
	
	// Add your task here
	public void run() {
		 
		 now = new Date(); // initialize date
		 System.out.println("Time is :" + now); // Display current time
		
		 BufferedReader br;
	     String line = "";	   
	  
		 boolean dowloaded = sftpObj.downloadAlipayFtpFile();
		 
		 if(dowloaded){
			 
			 String generatedFile = DynamicPaymentConstant.getCurrentAlipayReconcileFile();   
			 File newFile = new File(DynamicPaymentConstant.FTP_DESTINATION_PATH+generatedFile); 
			 
			 if (newFile.isFile() && newFile.canRead() ) {
					try {						
							   			
						logger.info("Server File Location="
								+ newFile.getAbsolutePath());						
						
						 br = new BufferedReader(new FileReader(newFile));
			            ReconcillationBean reconcileBean = new ReconcillationBean();
			            
			            //HttpSession session = request.getSession(true);
			            int adminId =1; //(int)session.getAttribute("user_id");
			            
						int i = 0; 
			            while ((line = br.readLine()) != null) {
			            	i++;
			            	if(i>2){
				            											
				                String[] reconcillationArray = line.split("\\|");
			
				                //System.out.println("line :"+line.toString());
				                
				                Date myDate = new Date();
				                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
				                String myDateString = sdf.format(myDate);
				                
				                reconcileBean.setAdminId(adminId);
				                reconcileBean.setTransactionAmount(reconcillationArray[2]);
				                reconcileBean.setChargeAmount(reconcillationArray[3]);
				                reconcileBean.setTransactionType(reconcillationArray[6]);
				                reconcileBean.setUploadedDate(myDateString);
				                reconcileBean.setRemark("");
				                reconcileBean.setPartnerTransactionId(reconcillationArray[0]);
				                reconcileBean.setPaymentTime(reconcillationArray[5]);
				                reconcileBean.setReconcillationStatus(0);
				                reconcileBean.setCurrency(reconcillationArray[4]);
				                reconcileBean.setTransactionId(reconcillationArray[1]);
				                
				                alipayReconcillationService.save(reconcileBean);
				                	                
				               // alipayReconcillationService.validateTransaction(reconcileBean);
			            	}
			            }
					}catch(Exception exp){
						exp.printStackTrace();
					}
			 	}	
		 	}
		 }
}