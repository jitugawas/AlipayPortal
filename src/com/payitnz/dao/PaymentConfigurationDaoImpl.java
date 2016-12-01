package com.payitnz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.payitnz.config.DBTables;
import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayWalletVO;

@Repository
public class PaymentConfigurationDaoImpl implements PaymentConfigurationDao{
	 @Autowired
	 NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws DataAccessException {
	        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	    }


	    
	    
		public void UpdateCUPRecord(AlipayWalletVO alipayWalletVO) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", alipayWalletVO.getId());
		        String sql ="UPDATE cup_configuration SET user_id=:user_id, merchant_id=:merchant_id, mcc=:mcc, merchant_name=:merchant_name, commodity_url=:commodity_url, currency=:currency, timeout=:timeout, merchant_reserved_field=:merchant_reserved_field WHERE id=:id";
			    namedParameterJdbcTemplate.update(sql, getSqlParameterByModelCUP(alipayWalletVO));
			
			
		}

		public void SaveCUPRecord(AlipayWalletVO alipayWalletVO) {

			  KeyHolder keyHolder = new GeneratedKeyHolder();

		        String sql = "INSERT INTO cup_configuration(user_id, merchant_id, mcc, merchant_name, commodity_url, currency, timeout, merchant_reserved_field, created_at) " + "VALUES (:user_id, :merchant_id, :mcc, :merchant_name, :commodity_url, :currency, :timeout, :merchant_reserved_field, :created_at)";

		        namedParameterJdbcTemplate.update(sql, getSqlParameterByModelCUP(alipayWalletVO), keyHolder);
		        alipayWalletVO.setId(keyHolder.getKey().intValue());
			
		}
		
	public AlipayWalletVO getCUPRecord(AlipayWalletVO alipayWalletVO) {
		Map<String, Object> params = new HashMap<String, Object>();
	       params.put("id", alipayWalletVO.getUser_id());
	       
	       String sql = "SELECT * FROM cup_configuration WHERE user_id=:id ";
	       List<AlipayWalletVO> result = null;
	       try {
	          
	           result = namedParameterJdbcTemplate.query(sql, params, new CUPMapper());
	           if(result.size() == 0) {
	               return null;
	           } else if(result.size() >=1) {
	           	for (AlipayWalletVO paymentMethodInfo : result) {
	           		
	           	return paymentMethodInfo;
	           	}
	           	 
	           } 
	       } catch (EmptyResultDataAccessException e) {
	           // do nothing, return null
	       }
	       return null;
	}
		
	
	public void UpdateDPSRecord(AlipayWalletVO alipayWalletVO) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", alipayWalletVO.getId());
	        String sql ="UPDATE dps_configuration SET user_id=:user_id, PxPayUserId=:PxPayUserId, PxPayKey=:PxPayKey, PxPayUrl=:PxPayUrl, transaction_type=:transaction_type, email=:email, currency=:currency, success_url=:success_url, failure_url=:failure_url WHERE id=:id";
	        namedParameterJdbcTemplate.update(sql, getSqlParameterByModelDPS(alipayWalletVO));
		
	}

	public void SaveDPSRecord(AlipayWalletVO alipayWalletVO) {

		  KeyHolder keyHolder = new GeneratedKeyHolder();

	        String sql ="INSERT INTO dps_configuration(user_id, PxPayUserId, PxPayKey, PxPayUrl, transaction_type, email, currency, success_url, failure_url,created_date, BillingId) " +
	        		"VALUES (:user_id, :PxPayUserId, :PxPayKey,:PxPayUrl, :transaction_type,:email, :currency, :success_url, :failure_url, :created_date, :BillingId)";


	        namedParameterJdbcTemplate.update(sql, getSqlParameterByModelDPS(alipayWalletVO), keyHolder);
	        alipayWalletVO.setId(keyHolder.getKey().intValue());
		
	}
	
public AlipayWalletVO getDPSRecord(AlipayWalletVO alipayWalletVO) {
	Map<String, Object> params = new HashMap<String, Object>();
       params.put("id", alipayWalletVO.getUser_id());
       
       String sql = "SELECT * FROM dps_configuration WHERE user_id=:id ";
       List<AlipayWalletVO> result = null;
       try {
          
           result = namedParameterJdbcTemplate.query(sql, params, new DPSMapper());
           if(result.size() == 0) {
               return null;
           } else if(result.size() >=1) {
           	for (AlipayWalletVO paymentMethodInfo : result) {
           		
           	return paymentMethodInfo;
           	}
           	 
           } 
       } catch (EmptyResultDataAccessException e) {
           // do nothing, return null
       }
       return null;
}
	

@Override
public AlipayWalletVO getPoliRecord(AlipayWalletVO payConnection) {
	Map<String, Object> params = new HashMap<String, Object>();
       params.put("id", payConnection.getUser_id());
       
       String sql = "SELECT * FROM poli_configuration WHERE user_id=:id ";
       List<AlipayWalletVO> result = null;
       try {
          
           result = namedParameterJdbcTemplate.query(sql, params, new PoliMapper());
           if(result.size() == 0) {
               return null;
           } else if(result.size() >=1) {
           	for (AlipayWalletVO paymentMethodInfo : result) {
           		
           	return paymentMethodInfo;
           	}
           	 
           } 
       } catch (EmptyResultDataAccessException e) {
           // do nothing, return null
       }
       return null;
}

@Override
public void SavePoliRecord(AlipayWalletVO payData) {
	
		  KeyHolder keyHolder = new GeneratedKeyHolder();

	        String sql = "INSERT INTO poli_configuration(user_id, account_id, merchant_reference, merchant_reference_format, notification_url, cancellation_url, currency_code, merchant_data, homepage_url, success_url,failure_url, timeout, fi_code, company_code, created_date) " +
	        "VALUES (:user_id, :account_id, :merchant_reference,:merchant_reference_format, :notification_url,:cancellation_url, :currency_code, :merchant_data, :homepage_url, :success_url,:failure_url, :timeout, :fi_code, :company_code, :created_date )";

	        namedParameterJdbcTemplate.update(sql, getSqlParameterByModelPoli(payData), keyHolder);
	        payData.setId(keyHolder.getKey().intValue());
	
}

@Override
public void UpdatePoliRecord(AlipayWalletVO payData) {
	
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("id", payData.getId());
        String sql ="UPDATE poli_configuration SET password=:password, account_id=:account_id, merchant_reference=:merchant_reference, merchant_reference_format=:merchant_reference_format, notification_url=:notification_url, cancellation_url=:cancellation_url, currency_code=:currency_code, merchant_data=:merchant_data, homepage_url=:homepage_url, success_url=:success_url,  failure_url=:failure_url, timeout=:timeout, fi_code=:fi_code, company_code=:company_code WHERE id=:id";
	        namedParameterJdbcTemplate.update(sql, getSqlParameterByModelPoli(payData));
	
}

@Override
public void SaveF2CRecord(AlipayWalletVO payData) {
	
		  KeyHolder keyHolder = new GeneratedKeyHolder();

	        String sql = "INSERT INTO f2c_configuration(service, user_id, return_url, account_id, secret_key, merchant_reference, notification_url,header_image, header_bottom_border_color, header_background_color, custom_data, store_card, display_customer_email, created_date) " +
	        "VALUES (:service, :user_id, :return_url, :account_id, :secret_key, :merchant_reference, :notification_url,:header_image, :header_bottom_border_color, :header_background_color, :custom_data, :store_card, :display_customer_email, :created_date)";

	        namedParameterJdbcTemplate.update(sql, getSqlParameterByModelF2C(payData), keyHolder);
	        payData.setId(keyHolder.getKey().intValue());
	
}
@Override
public void UpdateF2CRecord(AlipayWalletVO payData) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("id", payData.getId());
        String sql ="UPDATE f2c_configuration SET id=:id, service=:service, return_url=:return_url, account_id=:account_id, secret_key=:secret_key, merchant_reference=:merchant_reference, notification_url=:notification_url, header_image=:header_image, header_bottom_border_color=:header_bottom_border_color, header_background_color=:header_background_color, custom_data=:custom_data, store_card=:store_card,  display_customer_email=:display_customer_email WHERE id=:id";
	        namedParameterJdbcTemplate.update(sql, getSqlParameterByModelF2C(payData));
	
}

@Override
public AlipayWalletVO getF2CRecord(AlipayWalletVO payConnection) {
	Map<String, Object> params = new HashMap<String, Object>();
       params.put("id", payConnection.getUser_id());
       
       String sql = "SELECT * FROM f2c_configuration WHERE user_id=:id ";
       List<AlipayWalletVO> result = null;
       try {
          
           result = namedParameterJdbcTemplate.query(sql, params, new F2CMapper());
           if(result.size() == 0) {
               return null;
           } else if(result.size() >=1) {
           	for (AlipayWalletVO paymentMethodInfo : result) {
           	return paymentMethodInfo;
           	}
           	 
           } 
       } catch (EmptyResultDataAccessException e) {
           // do nothing, return null
       }
       return null;
}


private SqlParameterSource getSqlParameterByModelF2C(AlipayWalletVO payInfo) {
    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    paramSource.addValue("return_url", payInfo.getF2c_return_url());
    paramSource.addValue("account_id", payInfo.getF2c_account_id());
    paramSource.addValue("id", payInfo.getId());

    paramSource.addValue("user_id", payInfo.getUser_id());
    paramSource.addValue("secret_key", payInfo.getF2c_secret_key());
    paramSource.addValue("merchant_reference", payInfo.getF2c_merchant_reference());
    paramSource.addValue("header_bottom_border_color", payInfo.getHeader_bottom_border_color());
    paramSource.addValue("notification_url", payInfo.getF2c_notification_url());
    paramSource.addValue("header_image", payInfo.getHeader_image());
    paramSource.addValue("header_background_color", payInfo.getHeader_background_color());
    paramSource.addValue("custom_data", payInfo.getF2c_custom_data());
    paramSource.addValue("store_card", payInfo.getStore_card());
    paramSource.addValue("display_customer_email", payInfo.getDisplay_customer_email());
//    paramSource.addValue("payment_method", payInfo.getF2c_payment_method());
//    paramSource.addValue("visa", payInfo.isVisa());
//    paramSource.addValue("american_express", payInfo.isAmerican_express());
//    paramSource.addValue("dinner_club", payInfo.isDinner_club());
    paramSource.addValue("created_date", payInfo.getF2c_created_date());
//    paramSource.addValue("status", payInfo.getstatus());
    paramSource.addValue("service", payInfo.getF2c_service());

 
    return paramSource;
}

private static final class F2CMapper implements RowMapper<AlipayWalletVO> {

    public AlipayWalletVO mapRow(ResultSet rs, int rowNum) throws SQLException {
    	AlipayWalletVO f2cgateway = new AlipayWalletVO();
    	f2cgateway.setId(rs.getInt("id"));
    	f2cgateway.setUser_id(rs.getString("user_id"));
    	f2cgateway.setF2c_secret_key(rs.getString("secret_key"));
    	f2cgateway.setF2c_account_id(rs.getString("account_id"));
    	f2cgateway.setF2c_merchant_reference(rs.getString("merchant_reference"));
    	f2cgateway.setF2c_service(rs.getString("service"));

    	f2cgateway.setF2c_notification_url(rs.getString("notification_url"));
    	f2cgateway.setHeader_image(rs.getString("header_image"));
    	f2cgateway.setHeader_bottom_border_color(rs.getString("header_bottom_border_color"));
    	f2cgateway.setHeader_background_color(rs.getString("header_background_color"));
    	f2cgateway.setF2c_custom_data(rs.getString("custom_data"));
    	f2cgateway.setStore_card(rs.getString("store_card"));
    	f2cgateway.setF2c_return_url(rs.getString("return_url"));
    	f2cgateway.setDisplay_customer_email(rs.getString("display_customer_email"));
//    	f2cgateway.setF2c_payment_method(rs.getInt("payment_method"));
//    	f2cgateway.setVisa(rs.getBoolean("visa"));
//    	f2cgateway.setAmerican_express(rs.getBoolean("american_express"));
//    	f2cgateway.setDinner_club(rs.getBoolean("dinner_club"));
    	f2cgateway.setF2c_created_date(rs.getTimestamp("created_date"));
//    	f2cgateway.setstatus(rs.getBoolean("status"));
			return f2cgateway;
    
}
}

private SqlParameterSource getSqlParameterByModelPoli(AlipayWalletVO payInfo) {
    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    paramSource.addValue("id", payInfo.getId());

    paramSource.addValue("account_id", payInfo.getPoli_account_id());
    paramSource.addValue("password", payInfo.getPassword());
    paramSource.addValue("user_id", payInfo.getUser_id());
    paramSource.addValue("merchant_reference", payInfo.getPoli_merchant_reference());
    paramSource.addValue("merchant_reference_format", payInfo.getMerchant_reference_format());
    paramSource.addValue("notification_url", payInfo.getPoli_notification_url());
    paramSource.addValue("cancellation_url", payInfo.getPoli_cancellation_url());
    paramSource.addValue("homepage_url", payInfo.getPoli_homepage_url());
    paramSource.addValue("success_url", payInfo.getPoli_success_url());
    paramSource.addValue("failure_url", payInfo.getPoli_failure_url());
    paramSource.addValue("timeout", payInfo.getPoli_timeout());
    paramSource.addValue("fi_code", payInfo.getFi_code());
    paramSource.addValue("company_code", payInfo.getCompany_code());
    paramSource.addValue("currency_code", payInfo.getCurrency_code());
    paramSource.addValue("merchant_data", payInfo.getMerchant_data());
  //  paramSource.addValue("payment_method", payInfo.getPoli_payment_method());
    paramSource.addValue("created_date", payInfo.getPoli_created_date());
//    paramSource.addValue("status", payInfo.getstatus());
 
    return paramSource;
}
private static final class PoliMapper implements RowMapper<AlipayWalletVO> {

    public AlipayWalletVO mapRow(ResultSet rs, int rowNum) throws SQLException {
    	AlipayWalletVO poligateway = new AlipayWalletVO();
 	   poligateway.setId(rs.getInt("id"));
 	   poligateway.setUser_id(rs.getString("user_id"));
 	   poligateway.setPoli_account_id(rs.getString("account_id"));
 	   poligateway.setPassword(rs.getString("password"));
 	   poligateway.setPoli_merchant_reference(rs.getString("merchant_reference"));
 	   poligateway.setMerchant_reference_format(rs.getString("merchant_reference_format"));
 	   poligateway.setCurrency_code(rs.getString("currency_code"));
 	   poligateway.setMerchant_data(rs.getString("merchant_data"));
 	   poligateway.setPoli_notification_url(rs.getString("notification_url"));
 	   poligateway.setPoli_cancellation_url(rs.getString("cancellation_url"));
 	   
 	   poligateway.setPoli_homepage_url(rs.getString("homepage_url"));
 	   poligateway.setPoli_success_url(rs.getString("success_url"));
 	   poligateway.setPoli_failure_url(rs.getString("failure_url"));
 	   poligateway.setPoli_timeout(rs.getString("timeout"));
 	   poligateway.setFi_code(rs.getString("fi_code"));
 //	   poligateway.setPoli_payment_method(rs.getInt("payment_method"));
 	//   poligateway.setStatus(rs.getBoolean("status"));
 	   poligateway.setPoli_created_date(rs.getTimestamp("created_date"));
 	   poligateway.setCompany_code(rs.getString("company_code"));
			return poligateway;
    
}

}



	private static final class CUPMapper implements RowMapper<AlipayWalletVO> {

	       public AlipayWalletVO mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	   AlipayWalletVO CUPgateway = new AlipayWalletVO();
	    	   CUPgateway.setId(rs.getInt("id"));
	    	   //CUPgateway.setUser_id(rs.getInt("user_id"));
	    	   CUPgateway.setMerchant_id(rs.getString("merchant_id"));
	    	   CUPgateway.setMcc(rs.getString("mcc"));
	    	  // CUPgateway.setTransaction_type(rs.getString("transaction_type"));
	    	   CUPgateway.setMerchant_name(rs.getString("merchant_name"));
	    	   CUPgateway.setCommodity_url(rs.getString("commodity_url"));
	    	   CUPgateway.setCurrency(rs.getString("currency"));
	    	   CUPgateway.setTimeout(rs.getString("timeout"));
	    	   CUPgateway.setMerchant_reserved_field(rs.getString("merchant_reserved_field"));
	    	 //  CUPgateway.setSupported_method(rs.getInt("supported_method"));
	    	   CUPgateway.setCreated_date(rs.getTimestamp("created_at"));
//	    	   poligateway.set(rs.getString("failure_url"));
//	    	   poligateway.setFi_code(rs.getString("created_date"));

				return CUPgateway;
	       
	   }

}

	private SqlParameterSource getSqlParameterByModelCUP(AlipayWalletVO payInfo) {
       MapSqlParameterSource paramSource = new MapSqlParameterSource();
       paramSource.addValue("id", payInfo.getId());
       paramSource.addValue("user_id", payInfo.getUser_id());
       paramSource.addValue("merchant_id", payInfo.getMerchant_id());
       paramSource.addValue("mcc", payInfo.getMcc());
     //  paramSource.addValue("transaction_type", payInfo.getTransaction_type());
       paramSource.addValue("merchant_name", payInfo.getMerchant_name());
       paramSource.addValue("commodity_url", payInfo.getCommodity_url());
       paramSource.addValue("currency", payInfo.getCurrency());
       paramSource.addValue("timeout", payInfo.getTimeout());
       paramSource.addValue("merchant_reserved_field", payInfo.getMerchant_reserved_field());
//       paramSource.addValue("supported_method", payInfo.getSupported_method());
       paramSource.addValue("created_at", payInfo.getDate_and_time());
    //   paramSource.addValue("status", 1);
      
    
       return paramSource;
   }
	
	private SqlParameterSource getSqlParameterByModelDPS(AlipayWalletVO payInfo) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        
        paramSource.addValue("id", payInfo.getId());

        
        paramSource.addValue("user_id", payInfo.getUser_id());
        paramSource.addValue("PxPayUserId", payInfo.getPxPayUserId());
        paramSource.addValue("PxPayKey", payInfo.getPxPayKey());
        paramSource.addValue("PxPayUrl", payInfo.getPxPayUrl());
        paramSource.addValue("transaction_type", payInfo.getDPStransaction_type());
        paramSource.addValue("currency", payInfo.getDPScurrency());
        paramSource.addValue("BillingId", payInfo.getBillingID());
//        paramSource.addValue("visa", payInfo.isDpsvisa());
//        paramSource.addValue("american_express", payInfo.isDpsamerican_express());
//        paramSource.addValue("dinner_club", payInfo.isDpsdinner_club());
        paramSource.addValue("created_date", payInfo.getDPScreated_date());
//        paramSource.addValue("status", payInfo.getDPSStatus());
        paramSource.addValue("failure_url", payInfo.getDPSfailure_url());
        paramSource.addValue("success_url", payInfo.getDPSsuccess_url());
        paramSource.addValue("email", payInfo.getEmail());

        return paramSource;
    }
	
	private static final class DPSMapper implements RowMapper<AlipayWalletVO> {

	       public AlipayWalletVO mapRow(ResultSet rs, int rowNum) throws SQLException {
	 
		       
	    	   AlipayWalletVO DPSpaygateway = new AlipayWalletVO();
	    	   DPSpaygateway.setId(rs.getInt("id"));
	    	 //  DPSpaygateway.setUser_id(rs.getInt("user_id"));
	    	   DPSpaygateway.setDPSsuccess_url(rs.getString("success_url"));
	    	   DPSpaygateway.setDPSfailure_url(rs.getString("failure_url"));
	    	   DPSpaygateway.setDPScurrency(rs.getString("currency"));
	    	 //  DPSpaygateway.setBillingId(rs.getString("BillingId"));
	    	   DPSpaygateway.setPxPayUserId(rs.getString("PxPayUserId"));
	    	   DPSpaygateway.setPxPayKey(rs.getString("PxPayKey"));
	    	   DPSpaygateway.setPxPayUrl(rs.getString("PxPayUrl"));
	    	   DPSpaygateway.setEmail(rs.getString("email"));
	    	   DPSpaygateway.setDPStransaction_type(rs.getString("transaction_type"));

	    	 //  DPSpaygateway.setDPScreated_date(rs.getTimestamp("created_date"));
//	    	   DPSpaygateway.setDpsvisa(rs.getBoolean("visa"));
//	    	   DPSpaygateway.setDpsamerican_express(rs.getBoolean("american_express"));
//	    	   DPSpaygateway.setDpsdinner_club(rs.getBoolean("dinner_club"));
	    	//   DPSpaygateway.setDPSStatus(rs.getBoolean("status"));

				return DPSpaygateway;
	       
	   }
	       

	   
	}
	
	
	@Override
	public AlipayAPIResponse getTransactionByID(AlipayWalletVO paybean) {
		Map<String, Object> params = new HashMap<String, Object>();
	       params.put("id", paybean.getAlipayTransactionId());
	       
	       String sql = "SELECT * FROM "+DBTables.ALIPAY_TRANSACTIONS+" WHERE pg_partner_trans_id=:id ";
	       List<AlipayAPIResponse> result = null;
	       try {
	          
	           result = namedParameterJdbcTemplate.query(sql, params, new DPSTransactionMapper());
	           if(result.size() == 0) {
	               return null;
	           } else if(result.size() >=1) {
	           	for (AlipayAPIResponse paymentMethodInfo : result) {
	           		
	           		return paymentMethodInfo;
	           	}
	           	 
	           } 
	       } catch (EmptyResultDataAccessException e) {
	           // do nothing, return null
	       }
	       return null;
	}
	
	private static final class DPSTransactionMapper implements RowMapper<AlipayAPIResponse> {

	       public AlipayAPIResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
	 
		       
	    	   AlipayAPIResponse alipayAPIResponse  = new AlipayAPIResponse();
	    	   alipayAPIResponse.setId(rs.getInt("id"));
	    	   alipayAPIResponse.setDyMerchantId(rs.getString("dy_merchant_id"));
               alipayAPIResponse.setPgIsSuccess(rs.getString("pg_is_success") );
               alipayAPIResponse.setPgResultCode(rs.getString("pg_result_code") );
               alipayAPIResponse.setPgError(rs.getString("pg_error"));             
               alipayAPIResponse.setPgPartnerTransId(rs.getString("pg_partner_trans_id") );
               alipayAPIResponse.setMerchantRefundId(rs.getString("pg_partner_refund_id") );
               alipayAPIResponse.setPgAlipayTransId(rs.getString("pg_alipay_trans_id") );
               alipayAPIResponse.setPgAlipayReverseTime(rs.getString("pg_alipay_reverse_time") );
               alipayAPIResponse.setPgAlipayCancelTime(rs.getString("pg_alipay_cancel_time"));
               alipayAPIResponse.setMcCurrency(rs.getString("mc_currency"));
               alipayAPIResponse.setMcItemName(rs.getString("mc_trans_name"));
               alipayAPIResponse.setMcTransAmount(rs.getString("mc_trans_amount"));
               alipayAPIResponse.setPgExchangeRate(rs.getDouble("pg_exchange_rate"));
               alipayAPIResponse.setPgTransAmountCny(rs.getDouble("pg_trans_amount_cny"));
               alipayAPIResponse.setRequestTime(rs.getString("request_time"));
               alipayAPIResponse.setPgAlipayBuyerLoginId(rs.getString("pg_alipay_buyer_login_id"));
               alipayAPIResponse.setPgAlipayBuyerUserId(rs.getString("pg_alipay_buyer_user_id") );
               alipayAPIResponse.setRequestTime(rs.getString("request_time"));
               alipayAPIResponse.setIpAddress(rs.getString("ip_address") );
               alipayAPIResponse.setInfidigiUserId(rs.getString("infidigiUserId"));
               alipayAPIResponse.setChannel(rs.getString("method_type") );
               alipayAPIResponse.setTransaction_type(rs.getString("transaction_type") );
               if(rs.getString("pg_transaction_date")!=null)
               {
	               String s[]=rs.getString("pg_transaction_date").toString().split("\\.");
	               alipayAPIResponse.setTransactionDate(rs.getString("pg_transaction_date") != null ? s[0] : "");
	               alipayAPIResponse.setPgAlipayPayTime(s[0]);
               }
               else
               {
               	alipayAPIResponse.setTransactionDate("");
               	alipayAPIResponse.setPgAlipayPayTime("");
               }
               
               alipayAPIResponse.setPgMerchantTransactionId(rs.getString("pg_merchant_transaction_id"));
               alipayAPIResponse.setRemark(rs.getString("remark") );
               
				return alipayAPIResponse;
	       
	   }
	       

	   
	}
}
