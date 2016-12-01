package com.payitnz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

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
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mkyong.common.MailMail;
import com.payitnz.config.DBTables;
import com.payitnz.config.DynamicPaymentConstant;
import com.payitnz.model.EmailAlertsConfigBean;
import com.payitnz.model.ReconcillationBean;
import com.payitnz.model.ReconcillationFileBean;
import com.payitnz.model.TransactionBean;
import com.payitnz.model.User;


@Repository
public class AlipayReconcillationDaoImpl implements AlipayReconcillationDao {
	
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	DynamicPaymentConstant costant = new DynamicPaymentConstant();
    EmailAlertsConfigBean alertBean = costant.getAlertConfiguration(); 

	private static final Logger logger = Logger.getLogger(AlipayReconcillationDaoImpl.class);
	
	@Autowired
	DataSource dataSource;
	
    JdbcTemplate jdbcTemplate;
    
    MailMail mail = new MailMail();

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws DataAccessException {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
	public void save(ReconcillationBean reconcileBean){
				
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  
		  String selectSql = "SELECT * FROM "+DBTables.ALIPAY_RECONCILLATION+" WHERE transaction_id='"+reconcileBean.getTransactionId()+"'";
		  System.out.println("Check for dulciate query:"+selectSql);
		  
		  if(dataSource == null){
			  dataSource = this.getDataSource();
		  }
		  
		  if(jdbcTemplate == null){
			  jdbcTemplate = new JdbcTemplate(dataSource);
		  }
		  
		  if(namedParameterJdbcTemplate == null){
			  namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);  
		  }
		  
		  int id = 0;
		  List<ReconcillationBean> result = null;
		  boolean exist = false;
	       try {
	           result = jdbcTemplate.query(selectSql,new ReconcileMapper());
	           if(result!= null){
	        	   for (ReconcillationBean bean : result) {
	        		
	        		   if(reconcileBean.getPartnerTransactionId().equalsIgnoreCase(bean.getPartnerTransactionId()) && reconcileBean.getTransactionAmount().equalsIgnoreCase(bean.getTransactionAmount()) && reconcileBean.getChargeAmount().equalsIgnoreCase(bean.getChargeAmount()) && reconcileBean.getCurrency().equalsIgnoreCase(bean.getCurrency()) && reconcileBean.getPaymentTime().equalsIgnoreCase(bean.getPaymentTime()) && reconcileBean.getTransactionType().equalsIgnoreCase(bean.getTransactionType()) && reconcileBean.getRemark().equalsIgnoreCase(bean.getRemark())){
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
	       
	     if(!exist){
		  
	    	 	// System.out.println("Record should be inserted");  
	    	 
		        String sql = "INSERT INTO `"+DBTables.ALIPAY_RECONCILLATION+"`(`admin_id`,`file_id`, `partner_transaction_id`, `transaction_id`, `transaction_amount`, `charge_amount`, `currency`, `payment_time`, `transaction_type`, `remark`, `uploaded_date`, `reconcillation_status`)"
		                + "VALUES (:admin_id,:file_id,:partner_transaction_id, :transaction_id, :transaction_amount, :charge_amount, :currency, :payment_time, :transaction_type, :remark, :uploaded_date, :reconcillation_status)";
	
		        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(reconcileBean), keyHolder);
		        reconcileBean.setId(keyHolder.getKey().intValue());		
		        
		        this.validateTransaction(reconcileBean);
			  
		  }else{
			  System.out.println("Record Already exist in table should be skipped!");
		  }	        
	}
	 
	@Override
	public void validateTransaction(ReconcillationBean reconcileBean) {
		// TODO Auto-generated method stub
		
		  KeyHolder keyHolder = new GeneratedKeyHolder();
		  
		  Map<String,Object> params = new HashMap<String,Object>();
		  
		  params.put("partner_transaction_id",reconcileBean.getPartnerTransactionId());
		  params.put("transaction_id",reconcileBean.getTransactionId());		
		  params.put("transaction_amount",reconcileBean.getTransactionAmount());
		  params.put("charge_amount",reconcileBean.getChargeAmount());
		  params.put("currency",reconcileBean.getCurrency());
		  params.put("payment_time",reconcileBean.getPaymentTime());
		  params.put("transaction_type",reconcileBean.getTransactionType());
		  params.put("remark",reconcileBean.getRemark());
		
		  List<TransactionBean> resultMer = null;
		  
		  int merchantId = 1;
		  Map<String,Object> merMap = new HashMap<String,Object>();
		    
		  String merchantSql = "SELECT * FROM "+DBTables.ALIPAY_TRANSACTIONS+" WHERE pg_partner_trans_id = '"+reconcileBean.getPartnerTransactionId()+"'";
		
		  merMap.put("transaction_id",reconcileBean.getTransactionId());
		  
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
        		  merchant_company = beanM.getCompanyName();
        	  } 
          }
          
  		  String validateSql = "SELECT * FROM "+DBTables.ALIPAY_TRANSACTIONS+" tr INNER JOIN "+DBTables.ALIPAY_TRANSACTION_STATUS+" st ON tr.transaction_type = st.id WHERE tr.pg_partner_trans_id = '"+reconcileBean.getPartnerTransactionId()+"'";
  		  System.out.println("check sql:"+validateSql);
  		  
		  int id = 0;
		  List<ReconcillationBean> result = null;
	      try {
	    	  
	           result = namedParameterJdbcTemplate.query(validateSql, params,new ReconcileTransactionMapper());
	           if(result!= null && !result.isEmpty()){
	        	   
	        	   System.out.println("Validated!!"+result.toString());
	        	   for (ReconcillationBean bean : result) {
	        		   
	        		   System.out.println("coampre:"+bean.getPartnerTransactionId()+" == "+reconcileBean.getPartnerTransactionId()+" and "+bean.getTransactionAmount()+" == "+reconcileBean.getTransactionAmount()+" && "+bean.getTransactionType()+" == "+reconcileBean.getTransactionType());
	        		   
	        		   // perform validation check based on transaction id, partner transaction id, payment amount and transactuion type
	        		   if(bean.getPartnerTransactionId().equalsIgnoreCase(reconcileBean.getPartnerTransactionId()) && bean.getTransactionAmount().equalsIgnoreCase(reconcileBean.getTransactionAmount()) && bean.getTransactionType().equalsIgnoreCase(reconcileBean.getTransactionType())){
	        		   
							 id = bean.getId();							 
							 params.put("id",id);
				 			 params.put("reconcillation_status",1);
				 			 String sql = "UPDATE "+DBTables.ALIPAY_RECONCILLATION+" set `reconcillation_status`='"+DynamicPaymentConstant.VALIDATED+"' WHERE id='"+reconcileBean.getId()+"'";
				 			 jdbcTemplate.execute(sql);
				 			 
				 			 String statusSql = "UPDATE "+DBTables.ALIPAY_TRANSACTIONS+" set `is_reconciled`='"+DynamicPaymentConstant.ALIPAY_TRANSACTION_RECONCILED+"' WHERE id='"+bean.getId()+"'";
				 			 jdbcTemplate.execute(statusSql);
				 			

		   			 	    ApplicationContext context = new ClassPathXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
		   	     			MailMail mm = (MailMail) context.getBean("mailMail");
		   	     					   	     			 
		   	     			if(mm!=null && alertBean!=null){
		   	     			    
			   	     			 if(alertBean.getIs_reconciliation_success_emails()>0){
			   	     				
			   	     				String emailIds = alertBean.getReconciliation_success_emails();
			   	     				String[] emailArr = null;
			   	     				if(emailIds!=null){
			   	     					  emailArr = emailIds.split(",");
			   	     					  if(emailArr.length>0){
			   	     						  for(int i = 0; i<emailArr.length; i++){
			   	     							  if(emailArr[i]!=null && !emailArr[i].isEmpty()){
			   	     								  logger.info("Sending sucess email to email id:"+emailArr[i]);
			   	     								  mm.sendReconcillationSuccessMail(emailArr[i], emailArr[i], reconcileBean);	
			   	     							  }
			   	     						  }
			   	     					  }
			   	     				}
			   	     				 
			   	     			 }
			   	     		 }
		   	     			 
				 			 logger.info("Reconciled tranasction with id:"+reconcileBean.getPartnerTransactionId()+" successfully!"); 
				 			 				 			 
	        		   }else{
	        			   
	        			   	//System.out.println("In Validated!!"+result.toString());
      			 				 				 		
		   		 			String sqlUpdateError = "UPDATE "+DBTables.ALIPAY_RECONCILLATION+" set `reconcillation_status`="+DynamicPaymentConstant.VALIDATION_ERRORS+" WHERE id='"+reconcileBean.getId()+"'";		   		 			
		   		 		    jdbcTemplate.execute(sqlUpdateError);		   		 		    
		   		 			
		   		 			String sqlError = "INSERT INTO `"+DBTables.ALIPAY_RECONCILLATION_ERRORS+"`( `admin_id`, `partner_transaction_id`, `transaction_id`, `transaction_amount`, `charge_amount`, `currency`, `payment_time`, `transaction_type`, `remark`, `uploaded_date`, `email_sent`,`merchant_id`,`merchant_email`,`merchant_company`)"
		   			 	                + "VALUES ( :admin_id, :partner_transaction_id, :transaction_id, :transaction_amount, :charge_amount, :currency, :payment_time, :transaction_type, :remark, :uploaded_date, :email_sent,:merchant_id,:merchant_email,:merchant_company)";
	
		   			 	    namedParameterJdbcTemplate.update(sqlError, getSqlParameterByModel(reconcileBean, merchantId, merchant_email,merchant_company),keyHolder);
		   			 	    //reconcileBean.setId(keyHolder.getKey().intValue());	
		   			 	    
		   			 	    String statusSql = "UPDATE "+DBTables.ALIPAY_TRANSACTIONS+" set `is_reconciled`='"+DynamicPaymentConstant.ALIPAY_TRANSACTION_NOTRECONCILED+"' WHERE id='"+bean.getId()+"'";
		   			 	    jdbcTemplate.execute(statusSql);
			 			 
		   			 	    ApplicationContext context = new ClassPathXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
		   	     			MailMail mm = (MailMail) context.getBean("mailMail");
		   	     			/*if(mm!=null)
		   		 			mm.sendReconcillationMail(merchant_name, merchant_email, reconcileBean);
		   		 	  	 	  */ 	
			   	     		if(mm!=null && alertBean!=null){
		   	     			    
			   	     			 if(alertBean.getIs_reconciliation_failure_emails()>0){
			   	     				
			   	     				String emailIds = alertBean.getReconciliation_failure_emails();
			   	     				String[] emailArr = null;
			   	     				if(emailIds!=null){
			   	     					  emailArr = emailIds.split(",");
			   	     					  if(emailArr.length>0){
			   	     						  for(int i = 0; i<emailArr.length; i++){
			   	     							  if(emailArr[i]!=null && !emailArr[i].isEmpty()){
			   	     								  logger.info("Sending failure email to email id:"+emailArr[i]);
			   	     								  mm.sendReconcillationMail(emailArr[i], emailArr[i], reconcileBean);	
			   	     							  }
			   	     						  }
			   	     					  }
			   	     				}
			   	     				 
			   	     			 }
			   	     		 }
		   	     		
		   	     			logger.error("Error reconciling tranasction with id:"+reconcileBean.getPartnerTransactionId()+"!"); 
		   	     			
	        		   }			 			
					}
	           }else{	        	   
	        	  
	        	    String sqlUpdateError = "UPDATE "+DBTables.ALIPAY_RECONCILLATION+" set `reconcillation_status`="+DynamicPaymentConstant.VALIDATION_ERRORS+" WHERE id='"+reconcileBean.getId()+"'";
  		 		    jdbcTemplate.execute(sqlUpdateError);
  		 		 
  		 			String sqlError = "INSERT INTO `"+DBTables.ALIPAY_RECONCILLATION_ERRORS+"`( `admin_id`, `partner_transaction_id`, `transaction_id`, `transaction_amount`, `charge_amount`, `currency`, `payment_time`, `transaction_type`, `remark`, `uploaded_date`, `email_sent`,`merchant_id`,`merchant_email`,`merchant_company`)"
  			 	                + "VALUES ( :admin_id, :partner_transaction_id, :transaction_id, :transaction_amount, :charge_amount, :currency, :payment_time, :transaction_type, :remark, :uploaded_date, :email_sent,:merchant_id,:merchant_email,:merchant_company)";

  			 	    namedParameterJdbcTemplate.update(sqlError, getSqlParameterByModel(reconcileBean, merchantId, merchant_email,merchant_company),keyHolder);
  			 	     			 	   
  			 	    ApplicationContext context = new ClassPathXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
  	     			MailMail mm = (MailMail) context.getBean("mailMail");
  	     			
  	     			logger.error("Error reconciling tranasction with id:"+reconcileBean.getPartnerTransactionId()+"!"); 
  	     			/*if(mm!=null)
  	     				mm.sendReconcillationMail(merchant_name, merchant_email, reconcileBean);
  		 	  	 	  */ 
  	     	   		if(mm!=null && alertBean!=null){
   	     			    
	   	     			 if(alertBean.getIs_reconciliation_failure_emails()>0){
	   	     				
	   	     				String emailIds = alertBean.getReconciliation_failure_emails();
	   	     				String[] emailArr = null;
	   	     				if(emailIds!=null){
	   	     					  emailArr = emailIds.split(",");
	   	     					  if(emailArr.length>0){
	   	     						  for(int i = 0; i<emailArr.length; i++){
	   	     							  if(emailArr[i]!=null && !emailArr[i].isEmpty()){
		   	     								logger.info("Sending failure email to email id:"+emailArr[i]);
		   	     								mm.sendReconcillationMail(emailArr[i], emailArr[i], reconcileBean);	
		   	     						  }
	   	     						  }
	   	     					  }
	   	     				}
	   	     				 
	   	     			 }
	   	     		 }
		 	   }
	           
	      } catch (EmptyResultDataAccessException e) {
	            // do nothing, return null
	        	e.printStackTrace();
	      }       
	}

	@Override
	public List<ReconcillationBean> getReconciledTransactionByDate(String date) {
		// TODO Auto-generated method stub
		  String selectSql = "SELECT * FROM "+DBTables.ALIPAY_RECONCILLATION+" WHERE DATE(uploaded_date) = '"+date+"'";
		  System.out.println("Selected queyr:"+selectSql);
		  
		  Map<String,Object> params = new HashMap<>();
		  
		  params.put("uploaded_date", date);
		  int id = 0;
		  
		 // ArrayList<ReconcillationBean> resultArray = new ArrayList<>();
		  List<ReconcillationBean> result = null;
	       try {
	           //result = namedParameterJdbcTemplate.query(selectSql, params,new ReconcileMapper());
	           result = jdbcTemplate.query(selectSql, new ReconcileMapper());	         
	           return result;
	           
	        } catch (EmptyResultDataAccessException e) {
	            // do nothing, return null
	        	e.printStackTrace();
	        	return result;
	        }
	}

	@Override
	public void sendReconcillationEmail() {
		// TODO Auto-generated method stub
		
	}
	
	public DataSource getDataSource() {
           
		   DriverManagerDataSource dataSource = new DriverManagerDataSource();
           dataSource.setDriverClassName("com.mysql.jdbc.Driver");
           dataSource.setUrl(DynamicPaymentConstant.DB_URL);
           dataSource.setUsername(DynamicPaymentConstant.DB_USER);
           dataSource.setPassword(DynamicPaymentConstant.DB_PASSWORD);

           return dataSource;
    }

	@Override
	public void importTransaction(TransactionBean transactionBean) {
		// TODO Auto-generated method stub
		  KeyHolder keyHolder = new GeneratedKeyHolder();

		  Map<String,Object> params = new HashMap<String,Object>();
		  
		  params.put("partner_transaction_id",transactionBean.getPartnerTransactionId());
		  params.put("transaction_id",transactionBean.getTransactionId());		
		  params.put("transaction_amount",transactionBean.getTransactionAmount());
		  params.put("charge_amount",transactionBean.getChargeAmount());
		  params.put("currency",transactionBean.getCurrency());
		  params.put("payment_time",transactionBean.getPaymentTime());
		  params.put("transaction_type",transactionBean.getTransactionType());
		  params.put("remark",transactionBean.getRemark());
		  params.put("merchant_id",transactionBean.getMerchantId());
		  params.put("transaction_status",transactionBean.getTransactionStatus());
		  params.put("transaction_date",transactionBean.getTransactionDate());
		  
		  String selectSql = "SELECT * FROM "+DBTables.ALIPAY_TRANSACTIONS+" WHERE transaction_id='"+transactionBean.getTransactionId()+"'";
		 		  
		  int id = 0;
		  List<TransactionBean> result = null;
		  try {
	           result = jdbcTemplate.query(selectSql,new TransactionMapper());
	           if(result!= null){
	        	   for (TransactionBean bean : result) {
						id = bean.getId();
					}	        	  
	           }else{
	        	   id = 0;
	        	  
	           } 
	           
	      } catch (EmptyResultDataAccessException e) {
	            // do nothing, return null
	        	e.printStackTrace();
	      }
	       
	      if(id==0){
		  
	    	 	// System.out.println("Record should be inserted"); 	    	 
	    	 	params.put("uploaded_date",new Date());	    		    			  
			  	System.out.println("Bean id:"+transactionBean.getId()+" in a add mode!");			  
				params.put("transaction_status",1); 
		      /*  String sql = "INSERT INTO `"+DBTables.ALIPAY_TRANSACTIONS+"`(`merchant_id`, `partner_transaction_id`, `transaction_id`, `transaction_amount`, `charge_amount`, `currency`, `payment_time`, `transaction_type`, `remark`, `uploaded_date`, `transaction_status`,`transaction_date`)"
		                + "VALUES (:merchant_id, :partner_transaction_id, :transaction_id, :transaction_amount, :charge_amount, :currency, :payment_time, :transaction_type, :remark, :uploaded_date, :transaction_status,:transaction_date)";
	*/
		        String sql = "INSERT INTO `"+DBTables.ALIPAY_TRANSACTIONS+"`(`merchant_id`, `pg_partner_trans_id`, `pg_alipay_trans_id`, `mc_trans_amount`, `pg_trans_amount_cny`, `mc_currency`, `pg_alipay_pay_time`, `transaction_type`, `remark`, `uploaded_date`, `transaction_status`,`transaction_date`)"
		                + "VALUES (:merchant_id, :partner_transaction_id, :transaction_id, :transaction_amount, :charge_amount, :currency, :payment_time, :transaction_type, :remark, :uploaded_date, :transaction_status,:transaction_date)";
		       		        
		        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(transactionBean), keyHolder);
		        transactionBean.setId(keyHolder.getKey().intValue());	
		        	    	  
		  }else{
			  System.out.println("Record Already exist in table should be skipped!");
		  }	  
	}

	@Override
	public int saveReconcillationFile(ReconcillationFileBean bean) {
		// TODO Auto-generated method stub
		
		if(this.validateReconcillatonFile(bean.getFileName())){
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String sqlInsert = "INSERT INTO "+DBTables.ALIPAY_RECONCILLATION_FILES+" (`file_name`,`transaction_count`,`uploaded_date`,`status`,`owner_id`) VALUES (:file_name,:transaction_count,:uploaded_date,:status,:owner_id)";
	    	namedParameterJdbcTemplate.update(sqlInsert,getSqlParameterByModel(bean),keyHolder);
	    	bean.setId(keyHolder.getKey().intValue());
	    	return keyHolder.getKey().intValue();
    	}else{
    		return 0;
    	}
	}
	
	 public boolean validateReconcillatonFile(String reconcillationFile){
			
		 	if(dataSource == null){
		 		dataSource = this.getDataSource();
		 	}
		  
		 	if(jdbcTemplate == null){
		 		jdbcTemplate = new JdbcTemplate(dataSource);
		 	}
		  
		 	if(namedParameterJdbcTemplate == null){
		 		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);  
		 	}
		 	
			String selectSql = "SELECT * FROM "+DBTables.ALIPAY_RECONCILLATION_FILES+" WHERE file_name='"+reconcillationFile+"'";
	 		  
	        int id = 0;
		  	List<ReconcillationFileBean> result = null;
		  	try {		  		
		  	  
	           result = jdbcTemplate.query(selectSql,new ReconcileFileMapper());
	           if(result!= null){
	        	   for (ReconcillationFileBean bean : result) {
						id = bean.getId();
					}	
	        	   if(id > 0){
	        		   return false;
	        	   }else{
	        		   return true;
	        	   }
	           }else{
	        	   return true;
	        	  
	           } 
	           
	        } catch (EmptyResultDataAccessException e) {
	            // do nothing, return null
	        	e.printStackTrace();
	        	return false;
	        }	  	
		}
	 
		@Override
		public void updateReconcillation(ReconcillationFileBean settlementFileBean) {
			// TODO Auto-generated method stub

	    	String sqlUpdate = "UPDATE "+DBTables.ALIPAY_RECONCILLATION_FILES+" SET `transaction_count`=:transaction_count WHERE id=:id";
	    	//jdbcTemplate.update(sqlInsert,map); 	    	        	
	        namedParameterJdbcTemplate.update(sqlUpdate, getSqlParameterByModel(settlementFileBean));
	       
		}
		 private SqlParameterSource getSqlParameterByModel(ReconcillationBean reconcileBean) {

		        MapSqlParameterSource paramSource = new MapSqlParameterSource();
		        paramSource.addValue("id", reconcileBean.getId());
		        paramSource.addValue("admin_id", reconcileBean.getAdminId());
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
		 
		 
		 private SqlParameterSource getSqlParameterByModel(TransactionBean reconcileBean) {

		        MapSqlParameterSource paramSource = new MapSqlParameterSource();
		        paramSource.addValue("id", reconcileBean.getId());	      
		        paramSource.addValue("partner_transaction_id", reconcileBean.getPartnerTransactionId());
		        paramSource.addValue("transaction_id", reconcileBean.getTransactionId());
		        paramSource.addValue("transaction_amount", reconcileBean.getTransactionAmount());
		        paramSource.addValue("charge_amount", reconcileBean.getChargeAmount());
		        paramSource.addValue("currency", reconcileBean.getCurrency());
		        paramSource.addValue("payment_time", reconcileBean.getPaymentTime());
		        paramSource.addValue("transaction_type", reconcileBean.getTransactionType());
		        paramSource.addValue("remark", reconcileBean.getRemark());
		        paramSource.addValue("uploaded_date", reconcileBean.getUploadedDate());
		        paramSource.addValue("transaction_status", reconcileBean.getTransactionStatus());	 
		        paramSource.addValue("transaction_date", reconcileBean.getTransactionDate());	 
		        paramSource.addValue("merchant_id", reconcileBean.getMerchantId());
		        paramSource.addValue("merchant_email", reconcileBean.getMerchantEmail());
		        paramSource.addValue("email_sent", 0);
		     
		        return paramSource;
		    }
		 
		 
		 private SqlParameterSource getSqlParameterByModel(ReconcillationBean reconcileBean,int id,String email, String merchantCompany ) {

		        MapSqlParameterSource paramSource = new MapSqlParameterSource();
		        paramSource.addValue("id", reconcileBean.getId());
		        paramSource.addValue("admin_id", reconcileBean.getAdminId());
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
		        paramSource.addValue("merchant_id", id);
		        paramSource.addValue("merchant_email", email);	
		        paramSource.addValue("merchant_company", merchantCompany);
		        paramSource.addValue("email_sent", 0);	
		     
		        return paramSource;
		    }
		 
		 private static final class ReconcileMapper implements RowMapper<ReconcillationBean> {

		        public ReconcillationBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		        	ReconcillationBean user = new ReconcillationBean();
		            user.setId(rs.getInt("id"));
		            user.setTransactionAmount(rs.getString("transaction_amount"));
		            user.setTransactionId(rs.getString("transaction_id"));
		            user.setPartnerTransactionId(rs.getString("partner_transaction_id"));
		            user.setTransactionType(rs.getString("transaction_type"));
		            user.setChargeAmount(rs.getString("charge_amount"));
		            user.setCurrency(rs.getString("currency"));
		            user.setPaymentTime(rs.getString("payment_time"));
		            user.setUploadedDate(rs.getString("uploaded_date"));
		            user.setRemark(rs.getString("remark"));
		            user.setReconcillationStatus(rs.getInt("reconcillation_status"));	 
		            user.setMerchantId(0);
		 			user.setMerchantEmail("");
		            return user;
		        }
		        
		    }

		 
		 private static final class ReconcileTransactionMapper implements RowMapper<ReconcillationBean> {

		        public ReconcillationBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		        	ReconcillationBean user = new ReconcillationBean();
		            user.setId(rs.getInt("id"));
		           /* user.setTransactionAmount(rs.getString("transaction_amount"));
		            user.setTransactionId(rs.getString("pg_alipay_trans_id"));
		            user.setPartnerTransactionId(rs.getString("pg_partner_trans_id"));
		            user.setTransactionType(rs.getString("transaction_type"));
		            user.setChargeAmount(rs.getString("charge_amount"));
		            user.setCurrency(rs.getString("currency"));
		            user.setPaymentTime(rs.getString("payment_time"));
		            user.setUploadedDate(rs.getString("uploaded_date"));
		            user.setRemark(rs.getString("remark"));	            
		            user.setMerchantId(rs.getInt("merchant_id"));
		 			//user.setMerchantEmail(rs.getString("merchant_email"));
		           	      */
		            
		            //pg_partner_trans_id pg_alipay_trans_id mc_trans_amount pg_trans_amount_cny mc_currency pg_alipay_pay_time transaction_type remark
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
		            user.setTransactionType(rs.getString("transaction_type"));
		            user.setChargeAmount(rs.getString("pg_trans_amount_cny"));
		            user.setCurrency(rs.getString("mc_currency"));
		            user.setPaymentTime(rs.getString("pg_alipay_pay_time"));
		            user.setUploadedDate(rs.getString("pg_transaction_date"));
		            user.setRemark(rs.getString("remark"));	            
		            user.setMerchantId(rs.getInt("dy_merchant_id"));		         
		            
		            return user;
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
		
		 private static final class ReconcileFileMapper implements RowMapper<ReconcillationFileBean> {
	         public ReconcillationFileBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	         	ReconcillationFileBean user = new ReconcillationFileBean();
	            user.setId(rs.getInt("id"));
	            user.setFileName(rs.getString("file_name"));
	            user.setTransactionCount(rs.getInt("transaction_count")); 
	            user.setUploadedDate(rs.getDate("uploaded_date")); 
	            user.setStatus(rs.getInt("status"));
	            return user;
	         }        
	     }
		 
		 private SqlParameterSource getSqlParameterByModel(ReconcillationFileBean reconcillationFileBean) {
	        MapSqlParameterSource paramSource = new MapSqlParameterSource();
	        paramSource.addValue("id", reconcillationFileBean.getId());
	        paramSource.addValue("file_name", reconcillationFileBean.getFileName());
	        paramSource.addValue("owner_id", reconcillationFileBean.getOwnerId());
	        paramSource.addValue("uploaded_date", reconcillationFileBean.getUploadedDate());
	        paramSource.addValue("transaction_count", reconcillationFileBean.getTransactionCount());
	        paramSource.addValue("status", reconcillationFileBean.getStatus());        
	        return paramSource;
	     }
}
