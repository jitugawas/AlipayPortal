package com.mkyong.common;

import java.io.BufferedInputStream;  
import java.io.BufferedOutputStream;  
import java.io.File;  
import java.io.FileOutputStream;  
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.jcraft.jsch.Channel;  
import com.jcraft.jsch.ChannelSftp;  
import com.jcraft.jsch.JSch;  
import com.jcraft.jsch.Session;
import com.payitnz.config.DBTables;
import com.payitnz.config.DynamicPaymentConstant;
import com.payitnz.model.ReconcillationBean;
import com.payitnz.model.ReconcillationFileBean;
import com.payitnz.model.SettlementFileBean;
import com.payitnz.model.TransactionBean;  

public class SFTPpullsshkeys {  
	
		NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
		private static Logger logger = Logger.getLogger(SFTPpullsshkeys.class);
		
		@Autowired
		DataSource dataSource;
		
	    JdbcTemplate jdbcTemplate;
	    	
	    @Autowired
	    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws DataAccessException {
	        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	    }
	
	    @Autowired
	    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
	        this.jdbcTemplate = jdbcTemplate;
	    }
	    
		public SFTPpullsshkeys() {
	
        }
 
        public static void main(String[] args) {
        	
        	String SFTPPASS = "ALIPAY";        	
        	String SFTPHOST = "110.75.149.86";  
            int    SFTPPORT = 22;  
            String SFTPUSER = "OVERSEATEST";         
            String SFTPWORKINGDIR = "/download/";   

        	Session 	session 	= null;
        	Channel 	channel 	= null;
        	ChannelSftp channelSftp = null;

        	try{
        		
        		String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	        	dateToday = dateToday.replace("-", "");
	        	System.out.println("todays date :"+dateToday);
	        	
	        	JSch jsch = new JSch();
	        	session = jsch.getSession(SFTPUSER,SFTPHOST,SFTPPORT);
	        	session.setPassword(SFTPPASS);
	        	java.util.Properties config = new java.util.Properties();
	        	config.put("StrictHostKeyChecking", "no");
	        	session.setConfig(config);
	        	session.connect();
	        	channel = session.openChannel("sftp");
	        	channel.connect();
	        	channelSftp = (ChannelSftp)channel;
	        	channelSftp.cd(SFTPWORKINGDIR);
	        	/*byte[] buffer = new byte[1024];
	        	BufferedInputStream bis = new BufferedInputStream(channelSftp.get("2088021966645500_transaction_20151109.txt"));
	        	File newFile = new File(DynamicPaymentConstant.FTP_DESTINATION_PATH+"/transaction.txt");
	        	OutputStream os = new FileOutputStream(newFile);
	        	BufferedOutputStream bos = new BufferedOutputStream(os);
	        	int readCount;
	        	//System.out.println("Getting: " + theLine);
	        	while( (readCount = bis.read(buffer)) > 0) {
		        	System.out.println("Writing: " );
		        	bos.write(buffer, 0, readCount);
	        	}
	        	bis.close();
	        	bos.close();
	        	*/
        	}catch(Exception ex){
        		ex.printStackTrace();
        	}

      }

        
    public boolean downloadAlipayFtpFile(){
    	
    	String SFTPPASS = DynamicPaymentConstant.FTP_PASSWORD_STAGING;        	
    	String SFTPHOST = DynamicPaymentConstant.FTP_HOST_STAGING;  
        int    SFTPPORT = DynamicPaymentConstant.FTP_PORT_STAGING;  
        String SFTPUSER = DynamicPaymentConstant.FTP_USERNAME_STAGING;         
        String SFTPWORKINGDIR = DynamicPaymentConstant.FTP_FILE_PATH_STAGING;   

    	Session 	session 	= null;
    	Channel 	channel 	= null;
    	ChannelSftp channelSftp = null;

    	try{
    		
        	JSch jsch = new JSch();
        	session = jsch.getSession(SFTPUSER,SFTPHOST,SFTPPORT);
        	session.setPassword(SFTPPASS);
        	java.util.Properties config = new java.util.Properties();
        	config.put("StrictHostKeyChecking", "no");
        	session.setConfig(config);
        	session.connect();
        	channel = session.openChannel("sftp");
        	channel.connect();
        	if(channel.isConnected()){
	        	channelSftp = (ChannelSftp)channel;
	        	channelSftp.cd(SFTPWORKINGDIR);
	        	byte[] buffer = new byte[1024];   
	                           
	            String dynamicFile = DynamicPaymentConstant.getCurrentAlipayReconcileFile();  								 
	            if(this.validateReconcillatonFile(dynamicFile)){
	            
	                //String dynamicFile = DynamicPaymentConstant.ALIPAY_PARTNER_ID+"_transaction_"+yestDate+".txt";                
	                System.out.println("Dynamic File:"+dynamicFile);
	                logger.info("generated dynamic reconcillation file:"+dynamicFile);
	                
		        	BufferedInputStream bis = new BufferedInputStream(channelSftp.get(dynamicFile));
		        	File newFile = new File(DynamicPaymentConstant.FTP_DESTINATION_PATH+dynamicFile);
		        	OutputStream os = new FileOutputStream(newFile);
		        	BufferedOutputStream bos = new BufferedOutputStream(os);
		        	int readCount;
		        	int lineCount = 0;
		        	int status = 0;
		        	//System.out.println("Getting: " + theLine);
		        	while( (readCount = bis.read(buffer)) > 0) {
			        	System.out.println("Writing: " + lineCount);
			        	bos.write(buffer, 0, readCount);
			        	lineCount++;
		        	}
		        	
		        	if(lineCount > 0){
		        		status = DynamicPaymentConstant.RECONCILLATION_PROCESSED;
		        	}
		        	
		        	bis.close();
		        	bos.close();
		        	Map<String,Object> map = new HashMap<>();
		        	
		        	ReconcillationFileBean fileBean = new ReconcillationFileBean();
		           	map.put("file_name", dynamicFile);
		        	map.put("transaction_count", lineCount);
		        	map.put("uploaded_date", new Date());
		        	map.put("status", status);
		        	
		        	Date myDate = new Date();
		            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
		            String myDateString = sdf.format(myDate);
		             
		            KeyHolder keyHolder = new GeneratedKeyHolder();
		            
		        	fileBean.setFileName(dynamicFile);
		        	fileBean.setTransactionCount(lineCount);
		        	fileBean.setOwnerId(1);
		        	fileBean.setUploadedDate(new Date());
		        	fileBean.setStatus(status);
		        	
		        	  if(dataSource == null){
		    			  dataSource = this.getDataSource();
		    		  }
		    		  
		    		  if(jdbcTemplate == null){
		    			  jdbcTemplate = new JdbcTemplate(dataSource);
		    		  }
		    		  
		    		  if(namedParameterJdbcTemplate == null){
		    			  namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);  
		    		  }
		    		  
		        	String sqlInsert = "INSERT INTO "+DBTables.ALIPAY_RECONCILLATION_FILES+" (`file_name`,`transaction_count`,`uploaded_date`,`status`,`owner_id`) VALUES (:file_name,:transaction_count,:uploaded_date,:status,:owner_id)";
		        	namedParameterJdbcTemplate.update(sqlInsert,getSqlParameterByModel(fileBean),keyHolder);
		        	fileBean.setId(keyHolder.getKey().intValue());
		        	logger.info("The reconcillation file for today is successfully processed:"+fileBean.getFileName());
		        	return true;
	        	}else{
	        		logger.info("The reconcillation file for today is already being processed!");
	        		return false;
	        	}
        	}else{
        		logger.error("Cannot connect to the server. Please check if the permission or access is granted on alipay server!");
        		return false;
        	}
    	}catch(Exception ex){
    		ex.printStackTrace();
    		return false;
    	}        	
    }
    
    
 public boolean downloadAlipaySettlementFile(){
    	
    	String SFTPPASS = DynamicPaymentConstant.FTP_PASSWORD_STAGING;        	
    	String SFTPHOST = DynamicPaymentConstant.FTP_HOST_STAGING;  
        int    SFTPPORT = DynamicPaymentConstant.FTP_PORT_STAGING;  
        String SFTPUSER = DynamicPaymentConstant.FTP_USERNAME_STAGING;         
        String SFTPWORKINGDIR = DynamicPaymentConstant.FTP_FILE_PATH_STAGING;   

    	Session 	session 	= null;
    	Channel 	channel 	= null;
    	ChannelSftp channelSftp = null;

    	try{
    		
        	JSch jsch = new JSch();
        	session = jsch.getSession(SFTPUSER,SFTPHOST,SFTPPORT);
        	session.setPassword(SFTPPASS);
        	java.util.Properties config = new java.util.Properties();
        	config.put("StrictHostKeyChecking", "no");
        	session.setConfig(config);
        	session.connect();
        	channel = session.openChannel("sftp");
        	channel.connect();
        	
        	if(channel.isConnected()){
        	
	        	channelSftp = (ChannelSftp)channel;
	        	channelSftp.cd(SFTPWORKINGDIR);
	        	
	        	byte[] buffer = new byte[1024];   
	                           
	            String dynamicFile = DynamicPaymentConstant.getCurrentAlipaySettlementFile();  //"2088021966645500_settlement_20160912.txt"; 								 
	            if(this.validateSettlementFile(dynamicFile)){
	            
	                //String dynamicFile = DynamicPaymentConstant.ALIPAY_PARTNER_ID+"_transaction_"+yestDate+".txt";
	                
	                System.out.println("Dynamic File:"+dynamicFile);
	                logger.info("generated dynamic settlement file:"+dynamicFile);
	                
	                if(channelSftp.get(dynamicFile)!=null){
			        	BufferedInputStream bis = new BufferedInputStream(channelSftp.get(dynamicFile));
			        	File newFile = new File(DynamicPaymentConstant.FTP_DESTINATION_PATH+dynamicFile);
			        	OutputStream os = new FileOutputStream(newFile);
			        	BufferedOutputStream bos = new BufferedOutputStream(os);
			        	int readCount;
			        	int lineCount = 0;
			        	int status = 0;
			        	//System.out.println("Getting: " + theLine);
			        	while( (readCount = bis.read(buffer)) > 0) {
				        	System.out.println("Writing: " + lineCount);
				        	bos.write(buffer, 0, readCount);
				        	lineCount++;
			        	}
			        	
			        	if(lineCount > 0){
			        		status = 1;
			        	}
			        	bis.close();
			        	bos.close();
			        	Map<String,Object> map = new HashMap<>();
			        	
			        	SettlementFileBean fileBean = new SettlementFileBean();
			           	map.put("file_name", dynamicFile);
			        	map.put("transaction_count", lineCount);
			        	map.put("uploaded_date", new Date());
			        	map.put("status", status);
			        	
			        	Date myDate = new Date();
			            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
			            String myDateString = sdf.format(myDate);
			             
			            KeyHolder keyHolder = new GeneratedKeyHolder();
			            
			        	fileBean.setFileName(dynamicFile);
			        	fileBean.setTransactionCount(lineCount);
			        	fileBean.setUploadedDate(myDateString);
			        	fileBean.setSettlementStatus(status);
			        	
			        	  if(dataSource == null){
			    			  dataSource = this.getDataSource();
			    		  }
			    		  
			    		  if(jdbcTemplate == null){
			    			  jdbcTemplate = new JdbcTemplate(dataSource);
			    		  }
			    		  
			    		  if(namedParameterJdbcTemplate == null){
			    			  namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);  
			    		  }
			    		  
			        	String sqlInsert = "INSERT INTO "+DBTables.ALIPAY_SETTLEMENT_FILES+" (`file_name`,`transaction_count`,`uploaded_date`,`settlement_status`) VALUES (:file_name,:transaction_count,:uploaded_date,:status)";
			        	namedParameterJdbcTemplate.update(sqlInsert,getSqlParameterByModel(fileBean),keyHolder);
			        	fileBean.setId(keyHolder.getKey().intValue());
			        	logger.info("The settlement file for today is successfully processed:"+fileBean.getFileName());
			        	return true;
		        	}else{
		        		logger.error("File is already beign process for today");
		        		return false;
		        	}
	            }
	            return false;
            }else{
            	logger.error("Cannot connect to the server. Please check if the permission or access is granted on alipay server!");
        		return false;
            }
    	}catch(Exception ex){
    		ex.printStackTrace();
    		return false;
    	}        	
    }
    
	
	public DataSource getDataSource() {
           
		   DriverManagerDataSource dataSource = new DriverManagerDataSource();
           dataSource.setDriverClassName("com.mysql.jdbc.Driver");
           dataSource.setUrl(DynamicPaymentConstant.DB_URL);
           dataSource.setUsername(DynamicPaymentConstant.DB_USER);
           dataSource.setPassword(DynamicPaymentConstant.DB_PASSWORD);

           return dataSource;
    }
	
	public boolean validateReconcillatonFile(String reconcillationFile){
	
		String selectSql = "SELECT * FROM "+DBTables.ALIPAY_RECONCILLATION_FILES+" WHERE file_name='"+reconcillationFile+"'";
 		  
        int id = 0;
	  	List<ReconcillationFileBean> result = null;
	  	try {
	  		
	  	  if(dataSource == null){
			  dataSource = this.getDataSource();
		  }
		  
		  if(jdbcTemplate == null){
			  jdbcTemplate = new JdbcTemplate(dataSource);
		  }
		  
		  if(namedParameterJdbcTemplate == null){
			  namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);  
		  }
		  
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
	
	public boolean validateSettlementFile(String reconcillationFile){
		
		String selectSql = "SELECT * FROM "+DBTables.ALIPAY_SETTLEMENT_FILES+" WHERE file_name='"+reconcillationFile+"'";
 		  
        int id = 0;
	  	List<SettlementFileBean> result = null;
	  	try {
	  		
	  	  if(dataSource == null){
			  dataSource = this.getDataSource();
		  }
		  
		  if(jdbcTemplate == null){
			  jdbcTemplate = new JdbcTemplate(dataSource);
		  }
		  
		  if(namedParameterJdbcTemplate == null){
			  namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);  
		  }
		  
           result = jdbcTemplate.query(selectSql,new SettlementFileMapper());
           if(result!= null){
        	   for (SettlementFileBean bean : result) {
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
	
	 private static final class ReconcileFileMapper implements RowMapper<ReconcillationFileBean> {
        public ReconcillationFileBean mapRow(ResultSet rs, int rowNum) throws SQLException {
        	ReconcillationFileBean user = new ReconcillationFileBean();
            user.setId(rs.getInt("id"));
            user.setFileName(rs.getString("file_name"));
            user.setTransactionCount(rs.getInt("transaction_count")); 
            user.setUploadedDate(rs.getDate("uploaded_date")); 
            user.setStatus(rs.getInt("status"));
            user.setFileName(rs.getString("file_name"));
            return user;
        }        
    }
	 
	 private static final class SettlementFileMapper implements RowMapper<SettlementFileBean> {
	        public SettlementFileBean mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	SettlementFileBean user = new SettlementFileBean();
	            user.setId(rs.getInt("id"));
	            user.setFileName(rs.getString("file_name"));
	            user.setTransactionCount(rs.getInt("transaction_count")); 
	            user.setUploadedDate(rs.getString("uploaded_date")); 
	            user.setSettlementStatus(rs.getInt("settlement_status"));
	            user.setFileName(rs.getString("file_name"));
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
	
	private SqlParameterSource getSqlParameterByModel(SettlementFileBean settlementFileBean) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", settlementFileBean.getId());
        paramSource.addValue("file_name", settlementFileBean.getFileName());      
        paramSource.addValue("uploaded_date", settlementFileBean.getUploadedDate());
        paramSource.addValue("transaction_count", settlementFileBean.getTransactionCount());
        paramSource.addValue("settlement_status", settlementFileBean.getSettlementStatus());        
        return paramSource;
    }
		
} 
