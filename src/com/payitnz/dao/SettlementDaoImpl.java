package com.payitnz.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mkyong.common.MailMail;
import com.payitnz.config.DBTables;
import com.payitnz.config.DynamicPaymentConstant;
import com.payitnz.model.EmailAlertsConfigBean;
import com.payitnz.model.ReconcillationBean;
import com.payitnz.model.ReconcillationFileBean;
import com.payitnz.model.SettlementBean;
import com.payitnz.model.SettlementFileBean;
import com.payitnz.model.TransactionBean;
import com.payitnz.model.User;
import com.payitnz.util.TwilioSMSApi;

@Repository
public class SettlementDaoImpl implements SettlementDao {

	JdbcTemplate jdbcTemplate;
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	DynamicPaymentConstant costant = new DynamicPaymentConstant();
    EmailAlertsConfigBean alertBean = costant.getAlertConfiguration(); 
	TwilioSMSApi smsApi = new TwilioSMSApi();
	
	private static final Logger logger = Logger.getLogger(SettlementDaoImpl.class);
	
	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws DataAccessException {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
		
	@Override
	public void saveOrUpdate(SettlementBean settlementBean) {
		
		  logger.info("Settlement updating record in database");		
		  // TODO Auto-generated method stub		
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  String selectSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT+" WHERE transaction_id='"+settlementBean.getTransactionId()+"'";    
		 		  
		  int id = 0;
		  List<SettlementBean> result = null;
		  boolean exist = false;
	       try {
	           result = jdbcTemplate.query(selectSql,new SettlementMapper());
	           if(result!= null){
	        	   for (SettlementBean bean : result) {
	        		
	        		   if(settlementBean.getPartnerTransactionId().equalsIgnoreCase(bean.getPartnerTransactionId())){
	        			   id = bean.getId();
	        			   exist = true;
	        		   }
					}	        	  
	           }else{
	        	   id = 0;
	        	  
	           } 
	           
	        } catch (EmptyResultDataAccessException e) {
	            // do nothing, return null
	        	e.printStackTrace();
	        }
	       
	       //System.out.println("Exist in db :"+exist);
		    if(!exist){    
		    	
		        String sqlInsert = " INSERT INTO `"+DBTables.ALIPAY_SETTLEMENT+"` (`owner_id`,`settlement_id`,`partner_transaction_id`, `transaction_id`, `amount`, `rmb_amount`, `fee`, `settlement`, `rmb_settlement`, `currency`, `payment_time`, `settlement_time`, `rate`, `settlement_status`, `type`, `status`, `remark`,`uploaded_date`) VALUES "+
		        " (:owner_id,:settlement_id, :partner_transaction_id,:transaction_id, :amount, :rmb_amount, :fee, :settlement, :rmb_settlement, :currency, :payment_time, :settlement_time, :rate, :settlement_status, :type, :status, :remark,:uploaded_date)";
		       
		        namedParameterJdbcTemplate.update(sqlInsert, getSqlParameterByModel(settlementBean,0,""), keyHolder);
		        settlementBean.setId(keyHolder.getKey().intValue());		
		        
		        logger.info("Log settlement record for transaction "+settlementBean.getPartnerTransactionId()+"in database");
			}else{
				
				 logger.info("Settlement record for tranasction id "+settlementBean.getPartnerTransactionId()+" already exist in database");
				//System.out.println("Record Already exist in table should be skipped!");
			
			}	        
	}

	@Override
	public SettlementFileBean populateSettlementFileBean(int fileId){
		List<SettlementFileBean> settlementFileBean = null;
		SettlementFileBean fileBean = new SettlementFileBean();
		String fileSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT_FILES+" WHERE id = '"+fileId+"'";
		settlementFileBean = namedParameterJdbcTemplate.query(fileSql,new SettlementFileMapper());
         if(settlementFileBean!= null && !settlementFileBean.isEmpty()){
       	  for(SettlementFileBean beanM : settlementFileBean){
       		fileBean = beanM;
       	  } 
       	  return fileBean;
         } 
         return fileBean;
	}
	
	@Override
	public SettlementFileBean getSettlementFile(SettlementFileBean bean){
		List<SettlementFileBean> settlementFileBean = null;
		SettlementFileBean fileBean = new SettlementFileBean();
		String fileSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT_FILES+" WHERE id = '"+bean.getId()+"'";
		settlementFileBean = namedParameterJdbcTemplate.query(fileSql,new SettlementFileMapper());
         if(settlementFileBean!= null && !settlementFileBean.isEmpty()){
       	  for(SettlementFileBean beanM : settlementFileBean){
       		fileBean = beanM;
       	  } 
       	  return fileBean;
         } 
         return fileBean;
	}
	
	@Override
	public void validateSettlementTransactions(SettlementFileBean settlementFileBean) {
		// TODO Auto-generated method stub
		List<SettlementBean> resultBeans = null;
		
		String settlementSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT+" WHERE settlement_id = '"+settlementFileBean.getId()+"'";		  
		
		//System.out.println("setlemenfile sql:"+settlementSql);
		SettlementBean returnBean = null;
		ArrayList<SettlementBean> returnBeans = new ArrayList<>();
		
		resultBeans = namedParameterJdbcTemplate.query(settlementSql,new SettlementMapper());
		if(resultBeans!= null && !resultBeans.isEmpty()){
			
			for(SettlementBean beanM : resultBeans){
				returnBean = this.validateTransaction(beanM);	
				if(returnBean!=null){
					returnBeans.add(returnBean);
				}
			} 
			
			if(!returnBeans.isEmpty() && returnBeans!=null){
				String creditFile = this.generateSettlementFile(returnBeans);
				if(creditFile!=null){
					String sqlUpdate = "UPDATE `"+DBTables.ALIPAY_SETTLEMENT_FILES+"` SET credit_file = '"+creditFile+"',settlement_status = 1 WHERE id ="+settlementFileBean.getId();
					//System.out.println(sqlUpdate);			
					jdbcTemplate.update(sqlUpdate);
					
					settlementFileBean.setCreditFile(creditFile);

					ApplicationContext context = new ClassPathXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
   	     			MailMail mm = (MailMail) context.getBean("mailMail");
   	     			if(mm!=null){
   	     				
   	     				String emailIds = alertBean.getSettlement_success_emails();
	     				String[] emailArr = null;
	     				if(emailIds!=null){
	     					  emailArr = emailIds.split(",");
	     					  if(emailArr.length>0){
	     						  for(int i = 0; i<emailArr.length; i++){
	     							  if(emailArr[i]!=null && !emailArr[i].isEmpty()){
	     								 mm.sendSettlementCreditFileMail(emailArr[i], emailArr[i], settlementFileBean);
	     							  }
	     						  }
	     					  }
	     				}
   	     				
   	     			}
   	     			
	   	     		if(alertBean.getIs_settle_reconcile_phone_number()>0){
	   	     			
	   	     			String message = "Successfully settled "+settlementFileBean.getTransactionCount()+" transactions for credit file "+settlementFileBean.getCreditFile();
	   	     			String toNumber = alertBean.getSettle_reconcile_phone_number();
	   	     			if(!toNumber.isEmpty())
	   	     			smsApi.sendSMSAlert(message,toNumber);
	   	     			
	   	     		}
				}
			}else{
				logger.error("Failed to validate settlement file and generate credit file");
			}
			
		}else{
			//System.out.println("Error valdiaitng transactions!");
		}
	}

	public SettlementBean validateTransaction(SettlementBean reconcileBean){
		 
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  
		  List<TransactionBean> resultMer = null;
		  
		  int merchantId = 1;
		  Map<String,Object> merMap = new HashMap<String,Object>();
	  
		  //String merchantSql = "SELECT * FROM "+DBTables.ALIPAY_TRANSACTIONS+" WHERE transaction_id = '"+reconcileBean.getTransactionId()+"'";
		  String merchantSql = "SELECT * FROM "+DBTables.ALIPAY_TRANSACTIONS+" tr INNER JOIN "+DBTables.ALIPAY_TRANSACTION_STATUS+" st ON tr.transaction_type = st.id WHERE tr.pg_partner_trans_id = '"+reconcileBean.getPartnerTransactionId()+"'";
			 
		  resultMer = namedParameterJdbcTemplate.query(merchantSql,new TransactionMapper());
	      if(resultMer!= null && !resultMer.isEmpty()){
	      	  for(TransactionBean beanM : resultMer){
	      		  merchantId = beanM.getMerchantId();
	      	  } 
	      }
        
	       List<User> resultUser = null;
	       String merchant_email = "";
	       String merchant_name = "";
	       String merchant_company = "";
	       String populateMerchant = "SELECT * FROM "+DBTables.ALIPAY_USERS+" WHERE id = '"+merchantId+"'";
	       resultUser = namedParameterJdbcTemplate.query(populateMerchant,new UserMapper());
	       if(resultUser!= null && !resultUser.isEmpty()){
	      	  for(User beanM : resultUser){
	      		  merchant_email = beanM.getEmail();
	      		  merchant_name = beanM.getFirstName()+" "+beanM.getLastName();
	      		  merchant_company=beanM.getCompanyName();
	      	  } 
	       }
	        		
		  //String selectSql = "SELECT * FROM pnz_transactions WHERE partner_transaction_id=:partner_transaction_id AND transaction_id=:transaction_id AND transaction_amount=:transaction_amount AND charge_amount=:charge_amount AND currency=:currency AND payment_time=:payment_time AND transaction_type=:transaction_type AND remark=:remark";
		 // String validateSql = "SELECT * FROM "+DBTables.ALIPAY_TRANSACTIONS+" WHERE pg_alipay_trans_id = '"+reconcileBean.getTransactionId()+"'";
		  String validateSql = "SELECT * FROM "+DBTables.ALIPAY_TRANSACTIONS+" tr INNER JOIN "+DBTables.ALIPAY_TRANSACTION_STATUS+" st ON tr.transaction_type = st.id WHERE tr.pg_partner_trans_id = '"+reconcileBean.getPartnerTransactionId()+"'";
			
		  int id = 0;
		  List<ReconcillationBean> result = null;
		  ArrayList<SettlementBean> validBeans = new ArrayList<SettlementBean>();
		  
		  DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		  Date today = Calendar.getInstance().getTime();   
		  String reportDate = df.format(today);
				  
		  try {
	    	  
	           result = namedParameterJdbcTemplate.query(validateSql, getSqlParameterByModel(reconcileBean,0,""),new ReconcileTransactionMapper());
	           if(result!= null && !result.isEmpty()){
	        	   
	        	 //  System.out.println("Validated!!"+result.toString());
	        	   boolean validated = false;
	        	   for (ReconcillationBean bean : result) {
	        		   
	        		   String amount = bean.getTransactionAmount(); 
	        		  // int settlementAmount = Integer.parseInt(reconcileBean.getSettlement()); 
	        		  // int feeAmount = Integer.parseInt(reconcileBean.getFee());	        		   
	        		  // int totalAmount = settlementAmount+feeAmount;	        		   
	        		   //StringBuffer intBuffer = new StringBuffer();
	        		  // intBuffer.append(totalAmount);
	        		   reconcileBean.setMerchantEmail(merchant_email);
      			   	   reconcileBean.setMerchantCompany(merchant_company);
      			   	      			   	 
      			   	   System.out.println(bean.getPartnerTransactionId()+" == "+reconcileBean.getPartnerTransactionId()+" && "+amount+" == "+reconcileBean.getAmount());
	        		   if(bean.getPartnerTransactionId().equalsIgnoreCase(reconcileBean.getPartnerTransactionId()) && amount.equalsIgnoreCase(reconcileBean.getAmount())){
	        		   	        			   	 
							 id = bean.getId();	
							 String sql = "UPDATE "+DBTables.ALIPAY_SETTLEMENT+" set `settlement_status`="+DynamicPaymentConstant.ALIPAY_SETTLEMENT_VALIDATED+" WHERE id="+reconcileBean.getId()+"";
				 			 jdbcTemplate.execute(sql);
				 			
				 			 String statusSql = "UPDATE "+DBTables.ALIPAY_TRANSACTIONS+" set `is_settled`='"+DynamicPaymentConstant.ALIPAY_SETTLEMENT_VALIDATED+"' WHERE id='"+bean.getId()+"'";
				 			 jdbcTemplate.execute(statusSql);
				 				
				 			
				 			 //validBeans.add(reconcileBean);
				 			
				 			 logger.info("Validated transaction id "+reconcileBean.getPartnerTransactionId());
				 			 // namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(reconcileBean), keyHolder);
				 		     // reconcileBean.setId(keyHolder.getKey().intValue());		
				 			 ApplicationContext context = new ClassPathXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
			   	     		 MailMail mm = (MailMail) context.getBean("mailMail");
			   	     		 if(mm!=null && alertBean!=null){
			   	     			    
			   	     			 if(alertBean.getIs_settlement_success_emails()>0){
			   	     				
			   	     				String emailIds = alertBean.getSettlement_success_emails();
			   	     				String[] emailArr = null;
			   	     				if(emailIds!=null){
			   	     					  emailArr = emailIds.split(",");
			   	     					  if(emailArr.length>0){
			   	     						  for(int i = 0; i<emailArr.length; i++){
			   	     							  if(emailArr[i]!=null && !emailArr[i].isEmpty()){
			   	     								  logger.info("Sending sucess email to email id:"+emailArr[i]);
			   	     								  mm.sendSettlementSuccessMail(emailArr[i], emailArr[i], reconcileBean);	
			   	     							  }
			   	     						  }
			   	     					  }
			   	     				}
			   	     				 
			   	     			 }
			   	     		 }
			   	     		 
			   	     		 validated = true;
				 			 				 			 
	        		   }else{
	        			   
	        			   // System.out.println("In Validated!!"+result.toString());
	        			   String checkSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT_ERRORS+" WHERE transaction_id = '"+reconcileBean.getTransactionId()+"'";
	        			   System.out.println("checkSql!!"+checkSql);
	        			   
	    	        	   List<SettlementBean> resultError = namedParameterJdbcTemplate.query(checkSql, getSqlParameterByModel(reconcileBean,0,""),new SettlementMapper());
	    	  	           if(resultError.isEmpty()){
	    	  	         	 				 		
	    	  	        	    logger.info("Error while validating transaction id "+reconcileBean.getPartnerTransactionId());
					 			
			   		 			String sqlUpdateError = "UPDATE "+DBTables.ALIPAY_SETTLEMENT+" set `settlement_status`="+DynamicPaymentConstant.ALIPAY_SETTLEMENT_ERRORS+" WHERE id="+reconcileBean.getId()+"";
			   		 		    jdbcTemplate.execute(sqlUpdateError);		   		 		      
			   		 			   		 			
			   		 			String sqlError = "INSERT INTO `"+DBTables.ALIPAY_SETTLEMENT_ERRORS+"`( `owner_id`,`settlement_id`, `partner_transaction_id`, `transaction_id`, `amount`,`rmb_amount`,`fee`,`settlement`,`rmb_settlement`, `currency`, `payment_time`,`settlement_time`,`rate`,`status`,`settlement_status`, `type`, `remark`, `uploaded_date`, `email_sent`,`merchant_id`,`merchant_email`)"
			   			 	                + "VALUES ( :owner_id, :settlement_id, :partner_transaction_id, :transaction_id, :amount,:rmb_amount, :fee, :settlement,:rmb_settlement,:currency, :payment_time, :settlement_time, :rate, :status,:settlement_status, :type, :remark, :uploaded_date, :email_sent,:merchant_id,:merchant_email)";
		
			   			 	    namedParameterJdbcTemplate.update(sqlError, getSqlParameterByModel(reconcileBean,merchantId,merchant_email),keyHolder);
			   			 	    int lastId = keyHolder.getKey().intValue();	
			   			 	    
			   			 	    ApplicationContext context = new ClassPathXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
			   	     			MailMail mm = (MailMail) context.getBean("mailMail");
			   	     			if(mm!=null && alertBean!=null){
			   	     				//mm.sendSettlementMail(merchant_name, merchant_email, reconcileBean);
			   	     				 if(alertBean.getIs_settlement_failure_emails()>0){
					   	     				
					   	     				String emailIds = alertBean.getSettlement_failure_emails();
					   	     				String[] emailArr = null;
					   	     				if(emailIds!=null){
					   	     					  emailArr = emailIds.split(",");
					   	     					  if(emailArr.length>0){
					   	     						  for(int i = 0; i<emailArr.length; i++){
					   	     							  if(emailArr[i]!=null && !emailArr[i].isEmpty()){
						   	     								logger.info("Sending failure email to email id:"+emailArr[i]);
						   	     								mm.sendSettlementMail(emailArr[i], emailArr[i], reconcileBean);
					   	     							  }	
					   	     						  }
					   	     					  }
					   	     					String sqlUpdate = "UPDATE `"+DBTables.ALIPAY_SETTLEMENT_ERRORS+"` SET email_sent = 1 WHERE id ="+lastId;
						   	     				jdbcTemplate.update(sqlUpdate);
						   	     				logger.info("Settlement status updated for transaction id "+reconcileBean.getPartnerTransactionId()+" and email sent to the recipient");
							   	     			
					   	     				}
					   	     				 
					   	     			 }
				   	     			}
			   	     			
					   	     		if(alertBean.getIs_settle_reconcile_phone_number()>0){
					   	     			
					   	     			String message = " Failed to settled transaction with tran id: "+reconcileBean.getPartnerTransactionId();
					   	     			String toNumber = alertBean.getSettle_reconcile_phone_number();
					   	     			if(!toNumber.isEmpty())
					   	     			smsApi.sendSMSAlert(message,toNumber);
					   	     			
					   	     		}
			   	     			
		   			 	    }else{
		   			 	    	
		   			 	    	//System.out.println("Record already exist in error table!!");	  		
		   			 	    	
		   			 	    }	 	    
	        		   }			 			
					}
	        	   
	        	   if(validated){
	        		   return reconcileBean;
	        	   }else{
	        		   return null;
	        	   }
	           }else{
	        	   
	        	   String checkSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT_ERRORS+" WHERE transaction_id = '"+reconcileBean.getTransactionId()+"'";
	     		  
	        	   List<SettlementBean> resultError = namedParameterJdbcTemplate.query(checkSql, getSqlParameterByModel(reconcileBean,0,""),new SettlementMapper());
	  	           if(resultError.isEmpty()){
	  	           	  	         	        	   
		        	  // System.out.println("record for tra id:"+reconcileBean.getTransactionId()+" does not exist in transaction table!");
		        	   // System.out.println("In Validated!!"+result.toString());
	  	        	   logger.info("Error while validating transaction id "+reconcileBean.getPartnerTransactionId());
			 							 		
	  		 			String sqlUpdateError = "UPDATE "+DBTables.ALIPAY_SETTLEMENT+" set `settlement_status`="+DynamicPaymentConstant.ALIPAY_SETTLEMENT_ERRORS+" WHERE id='"+reconcileBean.getId()+"'";
	
	  		 			System.out.println("updated query errors !"+sqlUpdateError+" >> for id"+reconcileBean.getId());
	  		 		    jdbcTemplate.execute(sqlUpdateError);		   		 		      
	  		 			   		 			
	  		 			String sqlError = "INSERT INTO `"+DBTables.ALIPAY_SETTLEMENT_ERRORS+"`( `owner_id`,`settlement_id`, `partner_transaction_id`, `transaction_id`, `amount`,`rmb_amount`,`fee`,`settlement`,`rmb_settlement`, `currency`, `payment_time`,`settlement_time`,`rate`,`status`,`settlement_status`, `type`, `remark`, `uploaded_date`, `email_sent`,`merchant_id`,`merchant_email`)"
	  			 	                + "VALUES ( :owner_id, :settlement_id, :partner_transaction_id, :transaction_id, :amount,:rmb_amount, :fee, :settlement,:rmb_settlement,:currency, :payment_time, :settlement_time, :rate, :status,:settlement_status, :type, :remark, :uploaded_date, :email_sent,:merchant_id,:merchant_email)";
	
	  			 	    namedParameterJdbcTemplate.update(sqlError, getSqlParameterByModel(reconcileBean,merchantId,merchant_email),keyHolder);
	  			 	    int lastId = keyHolder.getKey().intValue();	
	  			 	    
	  			 	    ApplicationContext context = new ClassPathXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
	  	     			MailMail mm = (MailMail) context.getBean("mailMail");
	  	     			if(mm!=null && alertBean!=null){
	   	     				//mm.sendSettlementMail(merchant_name, merchant_email, reconcileBean);
	   	     				 if(alertBean.getIs_settlement_failure_emails()>0){
			   	     				
			   	     				String emailIds = alertBean.getSettlement_failure_emails();
			   	     				String[] emailArr = null;
			   	     				if(emailIds!=null){
			   	     					  emailArr = emailIds.split(",");
			   	     					  if(emailArr.length>0){
			   	     						  for(int i = 0; i<emailArr.length; i++){
			   	     							  
			   	     							  if(emailArr[i]!=null && !emailArr[i].isEmpty()){
			   	     								  logger.info("Sending failure email to email id:"+emailArr[i]);			   	     								  
			   	     								  mm.sendSettlementMail(emailArr[i], emailArr[i], reconcileBean);	
			   	     							  }
			   	     						  }
			   	     					  }
			   	     					String sqlUpdate = "UPDATE `"+DBTables.ALIPAY_SETTLEMENT_ERRORS+"` SET email_sent = 1 WHERE id ="+lastId;
				   	     				jdbcTemplate.update(sqlUpdate);
				   	     				logger.info("Settlement status updated for transaction id "+reconcileBean.getPartnerTransactionId()+" and email sent to the recipient");
					   	     			
			   	     				}
			   	     				 
			   	     			 }
		   	     			}
  			 	    
	  	           }else{
	  	        	   
	  	        	// System.out.println("Record already exist in error table!!");	  		 		    
	  	        	   
	  	           }
		 	   }
	           return null;
	      } catch (EmptyResultDataAccessException e) {
	            // do nothing, return null
	        	e.printStackTrace();
	        	return null;
	      }     
	}
	
	@Override
	public List<SettlementBean> getSettledTransactionByDate(String startDate,String endDate) {
		// TODO Auto-generated method stub
		 String selectSql = "";
			if(startDate.equalsIgnoreCase(endDate)){
				 selectSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT+" WHERE '"+startDate+"' >=  SUBDATE(uploaded_date,1) AND '"+endDate+"' <= uploaded_date";
				 System.out.println("Selected queyr:"+selectSql);
			}else{
				selectSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT+" WHERE DATE(uploaded_date) >= '"+startDate+"'  AND DATE(uploaded_date) <= '"+endDate+"'";
				System.out.println("Selected queyr:"+selectSql);
			}
		 // ArrayList<ReconcillationBean> resultArray = new ArrayList<>();
		  List<SettlementBean> result = null;
	       try {
	           //result = namedParameterJdbcTemplate.query(selectSql, params,new ReconcileMapper());
	           result = jdbcTemplate.query(selectSql, new SettlementMapper());	         
	           return result;
	           
	        } catch (EmptyResultDataAccessException e) {
	            // do nothing, return null
	        	e.printStackTrace();
	        	return result;
	        }
	}

	@Override
	public int logFile(SettlementFileBean settlementFileBean) {

		String selectSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT_FILES+" WHERE file_name='"+settlementFileBean.getFileName()+"'";
		  
        int id = 0;
	  	List<SettlementFileBean> result = null;
	  	Map<String,Object> map;
	  	KeyHolder keyHolder = new GeneratedKeyHolder();
	  	int returnId = 0;
	  	try {
           result = jdbcTemplate.query(selectSql,new SettlementFileMapper());
           if(result!= null){
        	   
        	   for (SettlementFileBean bean : result) {
					id = bean.getId();
			   }	
        	   
        	   if(id == 0){        		
        		   	
        		    map = new HashMap<String,Object>();
    	           	map.put("file_name", settlementFileBean.getFileName());
    	        	map.put("transaction_count", settlementFileBean.getTransactionCount());
    	        	map.put("uploaded_date", settlementFileBean.getUploadedDate());
    	        	map.put("settlement_status", settlementFileBean.getSettlementStatus());
    	        	
    	        	String sqlInsert = "INSERT INTO "+DBTables.ALIPAY_SETTLEMENT_FILES+" (`file_name`,`transaction_count`,`uploaded_date`,`settlement_status`) VALUES (:file_name,:transaction_count,:uploaded_date,:settlement_status)";
    	        	//jdbcTemplate.update(sqlInsert,map); 	    	        	
    	            namedParameterJdbcTemplate.update(sqlInsert, getSqlParameterByModel(settlementFileBean),keyHolder);
    	            settlementFileBean.setId(keyHolder.getKey().intValue());
    	            returnId = keyHolder.getKey().intValue();
    	            System.out.println("last inert id in if id = 0:"+returnId);
        	   }
        	   
           }else{
        	 
        	    map = new HashMap<String,Object>();
        	 	map.put("file_name", settlementFileBean.getFileName());
	        	map.put("transaction_count", settlementFileBean.getTransactionCount());
	        	map.put("uploaded_date", settlementFileBean.getUploadedDate());
	        	map.put("settlement_status", settlementFileBean.getSettlementStatus());
	        	
	        	String sqlInsert = "INSERT INTO "+DBTables.ALIPAY_SETTLEMENT_FILES+" (`file_name`,`transaction_count`,`uploaded_date`,`settlement_status`) VALUES (:file_name,:transaction_count,:uploaded_date,:settlement_status)";
	        	namedParameterJdbcTemplate.update(sqlInsert, getSqlParameterByModel(settlementFileBean),keyHolder);
 	            settlementFileBean.setId(keyHolder.getKey().intValue());
 	            
 	           returnId = keyHolder.getKey().intValue();
 	          System.out.println("last inert id if id = null:"+returnId);
           } 
           
           return returnId;
        } catch (EmptyResultDataAccessException e) {
            // do nothing, return null
        	e.printStackTrace();
        	 return returnId;
        }			
	}
	
	@Override
	public boolean checkFile(SettlementFileBean settlementFileBean) {

		String selectSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT_FILES+" WHERE file_name='"+settlementFileBean.getFileName()+"'";
		  
        int id = 0;
	  	List<SettlementFileBean> result = null;
	  	Map<String,Object> map;
	  	KeyHolder keyHolder = new GeneratedKeyHolder();
	  	try {
           result = jdbcTemplate.query(selectSql,new SettlementFileMapper());
           if(result!= null){
        	   
        	   for (SettlementFileBean bean : result) {
					id = bean.getId();
			   }	
        	   
        	   if(id == 0){        		
        		   	
        		   return false;	
        	   }
        	   
           }else{
        	 
        	    return true;
 	            
           } 
           
        } catch (EmptyResultDataAccessException e) {
            // do nothing, return null
        	e.printStackTrace();
        	return false;
        }
	  	return true;
	}

	private static final class SettlementFileMapper implements RowMapper<SettlementFileBean> {
        public SettlementFileBean mapRow(ResultSet rs, int rowNum) throws SQLException {
        	SettlementFileBean user = new SettlementFileBean();
            user.setId(rs.getInt("id"));
            user.setFileName(rs.getString("file_name"));
            user.setTransactionCount(rs.getInt("transaction_count")); 
            user.setUploadedDate(rs.getString("uploaded_date")); 
            user.setSettlementStatus(rs.getInt("settlement_status"));
            user.setCreditFile(DynamicPaymentConstant.SETTLEMENT_CREDIT_FILE_PATH+rs.getString("credit_file"));
            return user;
        }        
    }
		
	private static final class SettlementMapper implements RowMapper<SettlementBean> {
        public SettlementBean mapRow(ResultSet rs, int rowNum) throws SQLException {
        	SettlementBean user = new SettlementBean();
          
        	user.setId(rs.getInt("id"));
            user.setOwnerId(rs.getInt("owner_id"));
            user.setAmount(rs.getString("amount")); 
            user.setRmbAmount(rs.getString("rmb_amount")); 
            user.setUploadedDate(rs.getString("uploaded_date")); 
            user.setSettlementStatus(rs.getInt("settlement_status"));
            user.setTransactionId(rs.getString("transaction_id"));
            user.setPartnerTransactionId(rs.getString("partner_transaction_id")); 
            user.setRemark(rs.getString("remark")); 
            user.setType(rs.getString("type"));
            user.setFee(rs.getString("fee"));
            user.setSettlement(rs.getString("settlement"));
            user.setRmbSettlement(rs.getString("rmb_settlement"));
            user.setCurrency(rs.getString("currency"));
            user.setPaymentTime(rs.getString("payment_time"));
            user.setSettlementTime(rs.getString("settlement_time"));
            user.setRate(rs.getString("rate"));
            user.setStatus(rs.getString("status"));
            user.setCurrency(rs.getString("currency"));
            user.setSettlementId(rs.getInt("settlement_id"));
            
            return user;
        }        
    }
	
	 private static final class ReconcileTransactionMapper implements RowMapper<ReconcillationBean> {

	        public ReconcillationBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	ReconcillationBean user = new ReconcillationBean();
	            user.setId(rs.getInt("id"));
	            /*user.setTransactionAmount(rs.getString("transaction_amount"));
	            user.setTransactionId(rs.getString("transaction_id"));
	            user.setPartnerTransactionId(rs.getString("partner_transaction_id"));
	            user.setTransactionType(rs.getString("transaction_type"));
	            user.setChargeAmount(rs.getString("charge_amount"));
	            user.setCurrency(rs.getString("currency"));
	            user.setPaymentTime(rs.getString("payment_time"));
	            user.setUploadedDate(rs.getString("uploaded_date"));
	            user.setRemark(rs.getString("remark"));	            
	            */
	            
	            user.setTransactionAmount(rs.getString("mc_trans_amount"));
	            user.setTransactionId(rs.getString("pg_alipay_trans_id"));
	            user.setPartnerTransactionId(rs.getString("pg_partner_trans_id"));
	            user.setTransactionType(rs.getString("status"));
	            user.setChargeAmount(rs.getString("pg_trans_amount_cny"));
	            user.setCurrency(rs.getString("mc_currency"));
	            user.setPaymentTime(rs.getString("pg_alipay_pay_time"));
	            user.setUploadedDate(rs.getString("pg_transaction_date"));
	            user.setRemark(rs.getString("remark"));	            
	            user.setMerchantId(rs.getInt("dy_merchant_id"));		            
	       
	           // user.setMerchantId(rs.getInt("merchant_id"));
	 			//user.setMerchantEmail(rs.getString("merchant_email"));
	            return user;
	        }
	        
	    }
	 
	private SqlParameterSource getSqlParameterByModel(SettlementFileBean settlementFileBean) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", settlementFileBean.getId());
        paramSource.addValue("file_name", settlementFileBean.getFileName());
        paramSource.addValue("uploaded_date", settlementFileBean.getUploadedDate());
        paramSource.addValue("transaction_count", settlementFileBean.getTransactionCount());
        paramSource.addValue("settlement_status", settlementFileBean.getSettlementStatus());        
        return paramSource;
    }
	
	
	private SqlParameterSource getSqlParameterByModel(SettlementBean settlementBean,int merchantId,String merchant_email) {

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", settlementBean.getId());
        paramSource.addValue("owner_id", settlementBean.getOwnerId());
        paramSource.addValue("partner_transaction_id", settlementBean.getPartnerTransactionId());
        paramSource.addValue("transaction_id", settlementBean.getTransactionId());
        paramSource.addValue("amount", settlementBean.getAmount());
        paramSource.addValue("rmb_amount", settlementBean.getRmbAmount());
        paramSource.addValue("fee", settlementBean.getFee());
        paramSource.addValue("settlement", settlementBean.getSettlement());
        paramSource.addValue("rmb_settlement", settlementBean.getRmbSettlement());
        paramSource.addValue("currency", settlementBean.getCurrency());
        paramSource.addValue("payment_time", settlementBean.getPaymentTime());
        paramSource.addValue("settlement_time", settlementBean.getSettlementTime());
        paramSource.addValue("type", settlementBean.getType());
        paramSource.addValue("remark", settlementBean.getRemark());
        paramSource.addValue("rate", settlementBean.getRate());
        paramSource.addValue("uploaded_date", settlementBean.getUploadedDate());
        paramSource.addValue("status", settlementBean.getStatus());	   
        paramSource.addValue("settlement_status", settlementBean.getSettlementStatus());	  
        paramSource.addValue("settlement_id", settlementBean.getSettlementId());
        paramSource.addValue("email_sent", 0);
        paramSource.addValue("merchant_id", merchantId);
        paramSource.addValue("merchant_email", merchant_email);
        
        return paramSource;
    }

	 private SqlParameterSource getSqlParameterByModel(ReconcillationBean reconcileBean) {

	        MapSqlParameterSource paramSource = new MapSqlParameterSource();
	        paramSource.addValue("id", reconcileBean.getId());
	        paramSource.addValue("owner_id", reconcileBean.getAdminId());
	        paramSource.addValue("partner_transaction_id", reconcileBean.getPartnerTransactionId());
	        paramSource.addValue("transaction_id", reconcileBean.getTransactionId());
	        paramSource.addValue("transaction_amount", reconcileBean.getTransactionAmount());
	        paramSource.addValue("charge_amount", reconcileBean.getChargeAmount());
	        paramSource.addValue("currency", reconcileBean.getCurrency());
	        paramSource.addValue("payment_time", reconcileBean.getPaymentTime());
	        paramSource.addValue("transaction_type", reconcileBean.getTransactionType());
	        paramSource.addValue("remark", reconcileBean.getRemark());
	        paramSource.addValue("uploaded_date", reconcileBean.getUploadedDate());
	        paramSource.addValue("reconcillation_status", reconcileBean.getReconcillationStatus());	       
	        paramSource.addValue("merchant_id", reconcileBean.getMerchantId());
	        paramSource.addValue("merchant_email", reconcileBean.getMerchantEmail());
	        paramSource.addValue("file_id", reconcileBean.getFileId());
	        paramSource.addValue("email_sent", 0);
	     
	        return paramSource;
	 }
	 
	@Override
	public List<SettlementFileBean> getSettlementFiles(SettlementFileBean settlementFileBean) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		 String selectSql = "";
		 if(settlementFileBean.getStartDate().equals(settlementFileBean.getEndDate())){
			  selectSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT_FILES+" WHERE '"+settlementFileBean.getStartDate()+"' >= DATE(uploaded_date)  AND '"+settlementFileBean.getEndDate()+"' <= DATE(uploaded_date)";			 
		 }else{
			  selectSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT_FILES+" WHERE DATE(uploaded_date) >= '"+settlementFileBean.getStartDate()+"' AND DATE(uploaded_date) <= '"+settlementFileBean.getEndDate()+"'";
		 }
		 
		 System.out.println("Selected queyr:"+selectSql); 
		 Map<String,Object> params = new HashMap<>();		  
		 // params.put("uploaded_date", dateStart);
		 int id = 0;		  
		 // ArrayList<ReconcillationBean> resultArray = new ArrayList<>();
		  List<SettlementFileBean> result = null;
		  try {
			  //result = namedParameterJdbcTemplate.query(selectSql, params,new ReconcileMapper());
			  result = jdbcTemplate.query(selectSql, new SettlementFileMapper());	         
			  return result;
           
		  } catch (EmptyResultDataAccessException e) {
			  // do nothing, return null
			  e.printStackTrace();
			  return result;
		  }
	}

	@Override
	public void updateSettlement(SettlementFileBean settlementFileBean) {
		// TODO Auto-generated method stub

    	String sqlUpdate = "UPDATE "+DBTables.ALIPAY_SETTLEMENT_FILES+" SET `transaction_count`=:transaction_count WHERE id=:id";
    	//jdbcTemplate.update(sqlInsert,map); 	    	        	
        namedParameterJdbcTemplate.update(sqlUpdate, getSqlParameterByModel(settlementFileBean));
       
	}

	@Override
	public List<SettlementBean> getSettledTransactions(SettlementFileBean settlementFileBean) {
		// TODO Auto-generated method stub
		 Map<String,Object> params = new HashMap<>();
		
		 String selectSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT+" WHERE ";
		 
		 if(settlementFileBean.getId()>0){
			 selectSql+= " `settlement_id` =:id";
			 params.put("id",settlementFileBean.getId());
		 }
		 
		 if(settlementFileBean.getStartDate()!=null && settlementFileBean.getEndDate()!=null){
			 selectSql+= "  DATE(uploaded_date) >=:start_date  AND DATE(uploaded_date) <=:end_date";
			 params.put("start_date",settlementFileBean.getStartDate());
			 params.put("end_date",settlementFileBean.getEndDate());
		 }
		 
		 /*if(settlementFileBean.getId()>0){
			 selectSql = " `settlement_id` =:id";
			 params.put("id",settlementFileBean.getId());
		 }*/
		 
		 System.out.println("Selected queyr:"+selectSql);

		
		 // ArrayList<ReconcillationBean> resultArray = new ArrayList<>();
		  List<SettlementBean> result = null;
	       try {
	           //result = namedParameterJdbcTemplate.query(selectSql, params,new ReconcileMapper());
	           result = namedParameterJdbcTemplate.query(selectSql,params, new SettlementMapper());	         
	           return result;
	           
	        } catch (EmptyResultDataAccessException e) {
	            // do nothing, return null
	        	e.printStackTrace();
	        	return result;
	        }
	}

	@Override
	public void deleteSettlementFileRecord(int id) {
		
		 // TODO Auto-generated method stub
		 String deleteFileSql = "DELETE FROM "+DBTables.ALIPAY_SETTLEMENT_FILES+" WHERE id ="+id+"";
		 System.out.println("Selected queyr:"+deleteFileSql);
		 		  
		 try {
	          //result = namedParameterJdbcTemplate.query(selectSql, params,new ReconcileMapper());
	           jdbcTemplate.execute(deleteFileSql);	         
                
	           String deleteRecordsSql = "DELETE FROM "+DBTables.ALIPAY_SETTLEMENT+" WHERE settlement_id ="+id+"";
	           jdbcTemplate.execute(deleteRecordsSql);	 
	           
		 } catch (EmptyResultDataAccessException e) {
           // do nothing, return null
			  e.printStackTrace();
       
		 }
	}

	@Override
	public SettlementFileBean getSettlementFileById(int id) {
		// TODO Auto-generated method stub
		String selectSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT_FILES+" WHERE id ="+id+"";
		  System.out.println("Selected queyr:"+selectSql);
		 		  
		 // ArrayList<ReconcillationBean> resultArray = new ArrayList<>();
		  SettlementFileBean result = null;
		  try {
	         //result = namedParameterJdbcTemplate.query(selectSql, params,new ReconcileMapper());
	         result = jdbcTemplate.queryForObject(selectSql, new SettlementFileMapper());	         
	         return result;
	         
		  } catch (EmptyResultDataAccessException e) {
	          // do nothing, return null
	      	e.printStackTrace();
	      	return result;
		  }
	}
	
	 private static final class UserMapper implements RowMapper<User>{

			@Override
			public User mapRow(ResultSet rs, int ag) throws SQLException {
				User infiBean = new User();
				infiBean.setCompanyName(rs.getString("companyName"));
				infiBean.setEmail(rs.getString("email"));
				infiBean.setFirstName(rs.getString("firstName"));
				infiBean.setLastName(rs.getString("lastName"));
				infiBean.setInfidigiAccountId(rs.getString("infidigiAccountId"));
				infiBean.setId(rs.getInt("id"));
				infiBean.setInfidigiUserId(rs.getString("infidigiUserId"));
				infiBean.setPhoneNo(rs.getString("phoneNo"));
				infiBean.setRoleId(rs.getInt("roleId"));
				infiBean.setStatus(rs.getString("status"));
				infiBean.setUsername(rs.getString("username"));
				return infiBean;
			}
			 
		 }
	 
	 private static final class TransactionMapper implements RowMapper<TransactionBean> {

	        public TransactionBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	TransactionBean user = new TransactionBean();
	            user.setMerchantId(rs.getInt("id"));	           
	           /* user.setTransactionId(rs.getString("transaction_id"));
	            user.setPartnerTransactionId(rs.getString("partner_transaction_id"));
	            user.setTransactionAmount(rs.getString("transaction_amount"));
	            user.setTransactionStatus(rs.getInt("transaction_status"));
	            user.setPartnerTransactionId(rs.getString("partner_transaction_id"));
	            user.setTransactionType(rs.getString("transaction_type"));
	            user.setChargeAmount(rs.getString("charge_amount"));
	            user.setCurrency(rs.getString("currency"));
	            user.setPaymentTime(rs.getString("payment_time"));
	            user.setUploadedDate(rs.getString("uploaded_date"));	                   
	            user.setMerchantId(rs.getInt("merchant_id"));	
	            user.setRemark(rs.getString("remark"));	
	         */
	            user.setTransactionAmount(rs.getString("mc_trans_amount"));
	            user.setTransactionId(rs.getString("pg_alipay_trans_id"));
	            user.setPartnerTransactionId(rs.getString("pg_partner_trans_id"));
	            user.setTransactionType(rs.getString("status"));
	            user.setChargeAmount(rs.getString("pg_trans_amount_cny"));
	            user.setCurrency(rs.getString("mc_currency"));
	            user.setPaymentTime(rs.getString("pg_alipay_pay_time"));
	            user.setUploadedDate(rs.getString("pg_transaction_date"));
	            user.setRemark(rs.getString("remark"));	            
	            user.setMerchantId(rs.getInt("dy_merchant_id"));		            
	            return user;
	         
	        }
	        
	    }

	@Override
	public void generateDirectCreditFile(SettlementBean bean) {
		// TODO Auto-generated method stub
		
	}
	
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = "|";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	//CSV file header
	private static final String FILE_HEADER = "alipay_settlement|generated_date|deduction_account|amount|payee_particulars|payee_code|payee_reference|destination_account|payer_particulars|payer_code|payer_reference|payee_name";

	public String generateSettlementFile(ArrayList<SettlementBean> beanArray) {
		
		//Create a new list of student objects
		ArrayList<SettlementBean> settlementBeans = new ArrayList();
		
		for(SettlementBean bean:beanArray){
			settlementBeans.add(bean);
		}
				
		Date today1 = Calendar.getInstance().getTime(); 
		String strDate = String.valueOf(today1.getTime());
		
		String fileName = "settlement_transaction_"+strDate+".csv";;
		String fullPath = DynamicPaymentConstant.SETTLEMENT_CREDIT_FILE_PATH+fileName;
		FileWriter fileWriter = null;
				
		try {
			
			File file = new File(fullPath);
			
			if(!file.exists()){
				file.createNewFile();
			}else{
				logger.info("Error while generating credit file for settlement. The file path is not valid!");
				return null;
			}
			
			fileWriter = new FileWriter(file);
			//Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());
			
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date today = Calendar.getInstance().getTime(); 
			String reportDate = df.format(today);
			String[] dateArr = reportDate.split("/");
			
			String dateYear = dateArr[2];
			String settlementField = "set_"+dateYear.replace("/", "");
			
			//Write a new student object list to the CSV file
			for (SettlementBean student : settlementBeans) {
				fileWriter.append("Alipay settlement");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(reportDate);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(DynamicPaymentConstant.ALIPAY_PORTAL_ACCOUNT);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(student.getAmount());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append("Alipay settlement");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append("");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getSettlementTime()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getDestinationAccount()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(settlementField);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append("");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getOwnerId()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(student.getMerchantCompany()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
			
			System.out.println("Settlement CSV file was created successfully !!!");
			logger.info("Credit file "+fileName+" generated successfully for settlement file ");
 			return fileName;
		} catch (Exception e) {
			
			System.out.println("Error in CsvFileWriter !!!");
			logger.info("Error while generating credit file for settlement");
 			
			e.printStackTrace();
			return null;
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
		
	}
	
	public String getRecipientEmails(){
		return "EmailIds";
	}
}
