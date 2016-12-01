package com.payitnz.config;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.payitnz.dao.SettlementDaoImpl;
import com.payitnz.model.EmailAlertsConfigBean;

public class DynamicPaymentConstant {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	DataSource dataSource;
	
	private static final Logger logger = Logger.getLogger(SettlementDaoImpl.class);
	
	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws DataAccessException {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
    public static final String PG_ALIPAY_WALLET_URL = "https://intlmapi.alipay.com/gateway.do?";
    public static final String PG_UNKNOW_STATUS = "UNKNOW";
    public static final String ALIPAY_PARTNER_ID = "2088021966645500";
    
    public static final String METHOD_OFFLINE = "offline"; 
 	public static final String METHOD_ONLINE ="online";
 	
	public static final String ALIPAY_PORTAL_ACCOUNT = "123011123456700";
	
    //localhost dev-atul
// 	public static final String SERVER_HOST = "http://localhost:8000/";
// 	public static final String SITE_URL = "PayitnzMN6";
// 	public static final String SERVER_SITE_URL = "PayitnzMN6/";

  	//localhost dev-reshma
	 public static final String SERVER_HOST = "http://localhost:8080/";
	   	public static final String SITE_URL = "PayitnzMN";
	    public static final String SERVER_SITE_URL = "PayitnzMN/";
		
  /*
    public static final String SERVER_HOST = "http://localhost:8000/";
    public static final String SITE_URL = "PayitnzNew3";
    public static final String SERVER_SITE_URL = "PayitnzNew3/";  
  */
    
    
  	//test server
// 	public static final String SERVER_HOST = "http://www.ivaultit.com/";
// 	public static final String SITE_URL = "PayitnzMN";
// 	public static final String SERVER_SITE_URL = "PayitnzMN/"; 
    	    
  	public static final String SITE_BASE_URL = SERVER_HOST+SITE_URL;
  	//file upload path
  	
  	//realtive path
  	public static final String USER_FILES_PATH = "resources/userFiles/";
  	public static final String USER_GATEWAY_FILES_PATH = "resources/userGatewayFiles/";
  	public static final String USER_FILES_UPLOAD_URL = SERVER_HOST+SITE_URL+"/"+USER_FILES_PATH;
  	public static final String USER_GATEWAY_FILES_UPLOAD_URL = SERVER_HOST+SITE_URL+"/"+USER_GATEWAY_FILES_PATH;

  	//absolute path localhost
  	//public static final String USER_FILES_ABSOLUTE_PATH = "E:/eclipse ide/myworkspace5/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/"+SITE_URL+"/"+USER_FILES_PATH;
  	//public static final String USER_GATEWAY_FILES_ABSOLUTE_PATH = "E:/eclipse ide/myworkspace5/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/"+SITE_URL+"/"+USER_GATEWAY_FILES_PATH;


  	//absolute path testserver
	public static final String USER_FILES_ABSOLUTE_PATH = "/home/tehmus/jvm/apache-tomcat-7.0.64/domains/ivaultit.com/Payitnz/"+USER_FILES_PATH;
	public static final String USER_GATEWAY_FILES_ABSOLUTE_PATH = "/home/tehmus/jvm/apache-tomcat-7.0.64/domains/ivaultit.com/Payitnz/"+USER_GATEWAY_FILES_PATH;
	  	
	//jeetendra paths
  	//public static final String USER_FILES_ABSOLUTE_PATH = "C:/Users/Jeetu/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/"+SITE_URL+"/"+USER_FILES_PATH;
  	//public static final String USER_GATEWAY_FILES_ABSOLUTE_PATH = "C:/Users/Jeetu/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/"+SITE_URL+"/"+USER_GATEWAY_FILES_PATH;
  	
	
	public static final int VALIDATED = 1; 
	public static final int INVALIDATED =0; 
	public static final int VALIDATION_ERRORS = 2;	
  	public static final String FTP_HOST_TEST = "phpdemo2.quagnitia.com"; 
	public static final int FTP_PORT_TEST = 21; 
	public static final String FTP_USERNAME_TEST = "quagnitia";
	public static final String FTP_PASSWORD_TEST = "K^k4ed89";
	public static final String FTP_FILE_PATH_TEST = "C:/Inetpub/vhosts/phpdemo2.quagnitia.com/httpdocs/cronJob/2088311600334407_transaction_20140423.txt";
	
	public static final String FTP_HOST_STAGING = "110.75.149.86"; 
	public static final int FTP_PORT_STAGING = 22; 
	public static final String FTP_USERNAME_STAGING = "OVERSEATEST";
	public static final String FTP_PASSWORD_STAGING = "ALIPAY";
	public static final String FTP_FILE_PATH_STAGING = "/download";
	
	public static final String FTP_HOST = "phpdemo2.quagnitia.com"; 
	public static final int FTP_PORT = 21; 
	public static final String FTP_USERNAME = "quagnitia";
	public static final String FTP_PASSWORD = "K^k4ed89";
	public static final String FTP_FILE_PATH = "C:/Inetpub/vhosts/phpdemo2.quagnitia.com/httpdocs/cronJob/2088311600334407_transaction_20140423.txt";
	
	/*public static final String FTP_DESTINATION_PATH = "F:\\temp\\userFiles\\";
	public static final String SETTLEMENT_DESTINATION_PATH = "F:\\temp\\settlementFiles\\";
	public static final String SETTLEMENT_CREDIT_FILE_PATH = "F:\\temp\\settlementCreditFiles\\";
	*/
	
	public static final String FTP_DESTINATION_SERVER_PATH = "/download";	
	
	public static final String FTP_DESTINATION_PATH = "/home/tehmus/public_html/temp/userFiles/";
	public static final String SETTLEMENT_DESTINATION_PATH = "/home/tehmus/public_html/temp/userFiles/";
	public static final String SETTLEMENT_CREDIT_FILE_PATH = "/home/tehmus/public_html/temp/settlementCreditFiles/";
	public static final String FLO2CASHURL = "https://demo.flo2cash.co.nz/web2pay/default.aspx";
//	public static final String DB_USER = "tehmus_teamq";
//	public static final String DB_PASSWORD = "teamq@1234";
//	public static final String DB_NAME = "tehmus_payitnz";
//	public static final String DB_URL = "jdbc:mysql://mysql1000.mochahost.com:3306/"+DB_NAME;
	
	
	public static final String DB_USER = "root";
	public static final String DB_PASSWORD = "";
	public static final String DB_NAME = "tehmus_pilotdpa";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/"+DB_NAME;
	
	
/*	public static final String DB_USER = "root";
	public static final String DB_PASSWORD = "";
	public static final String DB_NAME = "tehmus_payitnz";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/"+DB_NAME;
	*/
	
	public static final String EMAIL_REGARDS = "PayitNZ Portal Team";
	
	//Settlement Statuses
	public static final int ALIPAY_TRANSACTION_RECONCILED = 1;
	public static final int ALIPAY_TRANSACTION_NOTRECONCILED = 0;

	public static final int RECONCILLATION_PROCESSED = 1;
	public static final int SETTLEMENT_PROCESSED = 1;
	
	public static final int ALIPAY_SETTLEMENT_VALIDATED = 1;
	public static final int ALIPAY_SETTLEMENT_ERRORS = 2;
	public static final int ALIPAY_SETTLEMENT_NOTVALIDATED = 0;
	
	public static final String TRANSACTION_SUCCESSFULL = "1"; 
	public static final String TRANSACTION_DECLINED ="2"; 	
	public static final String TRANSACTION_REFUND ="3"; 
	public static final String TRANSACTION_SETTLED = "4";	
	public static final String TRANSACTION_NOTSETTLED = "5";	
	public static final String TRANSACTION_FAILED = "6";	
	public static final String TRANSACTION_REVERSAL = "7";
	public static final String TRANSACTION_CANCELLED = "8";
	
	public static final String TRANSACTION_PG_SUCCESSFULL = "SUCCESS"; 
	public static final String TRANSACTION_PG_DECLINED ="DECLINED"; 	
	public static final String TRANSACTION_PG_REFUND ="REFUND"; 
	public static final String TRANSACTION_PG_SETTLED = "SETTLED";	
	public static final String TRANSACTION_PG_NOTSETTLED = "NOTSETTLED";	
	public static final String TRANSACTION_PG_FAILED = "FAILED";	
	public static final String TRANSACTION_PG_REVERSAL = "REVERSAL";
	public static final String TRANSACTION_PG_CANCELLED = "CANCELLED";

	public static final String DPS_METHOD = "DPS";
	
	public static final String CARD_VISA = "VISA";
	
    //-------------------------------- CONSTANT METHODS----------------------------------

  	//round method
  	public static double round(double value, int places) {
  		if (places < 0) throw new IllegalArgumentException();

  		BigDecimal bd = new BigDecimal(value);
  		bd = bd.setScale(places, RoundingMode.HALF_UP);
  		return bd.doubleValue();
  	}

  	public static String shuffle(String input){
  		List<Character> characters = new ArrayList<Character>();
  		for(char c:input.toCharArray()){
  			characters.add(c);
  		}
  		StringBuilder output = new StringBuilder(input.length());
  		while(characters.size()!=0){
  			int randPicker = (int)(Math.random()*characters.size());
  			output.append(characters.remove(randPicker));
  		}
  		return output.toString();
  	}

  	public static String INRFormat(double value) {
  		if(value < 1000) {
  			return format("##0.00", value);
  		} else {
  			double hundreds = value % 1000;
  			int other = (int) (value / 1000);
  			return format(",##", other) + ',' + format("000.00", hundreds);
  		}
  	}

  	public static String INRFormatWithInt(int value) {
  		if(value < 1000) {
  			return format("##0", value);
  		} else {
  			int hundreds = value % 1000;
  			int other = (int) (value / 1000);
  			return format(",##", other) + ',' + format("000", hundreds);
  		}
  	}

  	private static String format(String pattern, Object value) {
  		return new DecimalFormat(pattern).format(value);
  	}

  	public static String getHashPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{

  		MessageDigest md = MessageDigest.getInstance("SHA-256");
  		byte[] hash = md.digest(password.getBytes("UTF-8"));
  		StringBuffer hexString = new StringBuffer();

  		for (int i = 0; i < hash.length; i++) {
  			String hex = Integer.toHexString(0xff & hash[i]);
  			if(hex.length() == 1) hexString.append('0');
  			hexString.append(hex);
  		}

  		password = hexString.toString();
  		return password;
  	}
  	
	public static String Encoding(char[] c) {
		  StringBuffer buffer = new StringBuffer();
		  for (int i = 0; i < c.length; i++) {
		   buffer.append(String.format("\\u%04x", (int) c[i]));
		  }

		  return buffer.toString();
		 }

	public static String decoding(String s) {
		  try {
		   s = s.replace("\\\\", "\\");
		   return StringEscapeUtils.unescapeJava(s);
		  } catch (Exception e) {
		   return "";
		  }
		 } 

	public static String getCurrentAlipayReconcileFile(){
				 
	   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, -3);    
       String yestDate = dateFormat.format(cal.getTime());
       yestDate = yestDate.replace("-", "");                              
       String dynamicFile = DynamicPaymentConstant.ALIPAY_PARTNER_ID+"_transaction_"+yestDate+".txt";
       return dynamicFile;
		
	}
	
	public static String getCurrentAlipaySettlementFile(){
		 
	   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, -3);    
       String yestDate = dateFormat.format(cal.getTime());
       yestDate = yestDate.replace("-", "");                              
       String dynamicFile = DynamicPaymentConstant.ALIPAY_PARTNER_ID+"_settlement_"+yestDate+".txt";
       return dynamicFile;
		
	}
	
	public EmailAlertsConfigBean getAlertConfiguration(){
		
		  if(dataSource == null){
			  dataSource = this.getDataSource();
		  }
		  
		  if(jdbcTemplate == null){
			  jdbcTemplate = new JdbcTemplate(dataSource);
		  }
		  
		  if(namedParameterJdbcTemplate == null){
			  namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);  
		  }
		EmailAlertsConfigBean validBean = new EmailAlertsConfigBean();
		List<EmailAlertsConfigBean> alertBean = new ArrayList<EmailAlertsConfigBean>();
		String fileSql = "SELECT * FROM "+DBTables.ALIPAY_EMAIL_ALERTS+"";
		alertBean = namedParameterJdbcTemplate.query(fileSql,new AlertMapper());
         if(alertBean!= null){
       	  for(EmailAlertsConfigBean beanM : alertBean){
       		validBean = beanM;
       	  } 
       	  return validBean;
         } 
         return validBean;
		
	}
	
	private static final class AlertMapper implements RowMapper<EmailAlertsConfigBean> {

		public EmailAlertsConfigBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmailAlertsConfigBean alertBean = new EmailAlertsConfigBean();
			System.out.println("I am here now!");
			alertBean.setId(rs.getInt("id"));
			alertBean.setUser_id(rs.getInt("user_id"));
			alertBean.setIs_reconciliation_failure_emails(rs.getInt("is_reconciliation_failure_emails"));
			alertBean.setIs_reconciliation_success_emails(rs.getInt("is_reconciliation_success_emails"));
			alertBean.setIs_settle_reconcile_phone_number(rs.getInt("is_settle_reconcile_phone_number"));
			alertBean.setIs_settlement_failure_emails(rs.getInt("is_settlement_failure_emails"));
			alertBean.setIs_settlement_success_emails(rs.getInt("is_settlement_success_emails"));
			alertBean.setReconciliation_failure_emails(rs.getString("reconciliation_failure_emails"));
			alertBean.setReconciliation_success_emails(rs.getString("reconciliation_success_emails"));
			alertBean.setSettle_reconcile_phone_number(rs.getString("settle_reconcile_phone_number"));
			alertBean.setSettlement_failure_emails(rs.getString("settlement_failure_emails"));
			alertBean.setSettlement_success_emails(rs.getString("settlement_success_emails"));
			return alertBean;
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
  	//-------------------------------------------------------------------------------------
}
