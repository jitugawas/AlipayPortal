package com.payitnz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayDashboardBean;
import com.payitnz.model.AlipayPaymentGatewayBean;

@Repository
public class AlipayTransactionDaoImpl implements AlipayTransactionDao {
	
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	JdbcTemplate jdbcTemplate;

	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws DataAccessException {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<AlipayDashboardBean> findAllTransDetails(AlipayDashboardBean search) {
		Map<String, Object> params = new HashMap<String, Object>();
		int i=0;
		
		String sql = "SELECT DISTINCT ares.method_type as payment_method ,count(ares.method_type) as trans_num,SUM(ares.mc_trans_amount) as sum_amount,AVG(ares.mc_trans_amount) as avg_amount,MAX(ares.mc_trans_amount) as max_amount,MIN(ares.mc_trans_amount) as min_amount FROM `AlipayAPI_Response`as ares "
				+ " WHERE ares.method_type !='' AND ares.pg_result_code='SUCCESS'";
			
		if(search.getToDate() !=null && search.getFromDate() !=null){
			params.put("to_date",search.getToDate());
			params.put("from_date",search.getFromDate());
			i++;
			sql+= " AND ares.pg_transaction_date BETWEEN '"+search.getFromDate()+" 00:00:00' AND '"+search.getToDate()+" 00:00:00' ";
		}
		
		if(search.getRole_id() == 2 || search.getRole_id() == 3){
			params.put("user_id",search.getUser_id());
			i++;
			if(search.getToDate() ==null && search.getFromDate() ==null){
				sql+= " AND ares.dy_merchant_id = :user_id AND ares.pg_transaction_date >= DATE(NOW()) - INTERVAL "+search.getDisplay_transactions_per_day()+" DAY";
			}else{
				sql+= " AND ares.dy_merchant_id = :user_id ";
			}
		}
		
		if(i==0){
			sql	+= " AND ares.pg_transaction_date >= DATE(NOW()) - INTERVAL 7 DAY";
		}
		
		sql += " GROUP BY ares.method_type ORDER BY ares.method_type ASC";
		//System.out.println(sql);
		List<AlipayDashboardBean> result = namedParameterJdbcTemplate.query(sql,params,new TransactionMapper());
		return result;
	}

	@Override
	public List<AlipayPaymentGatewayBean> findAllPaymentMethods() {
		String sql = "SELECT * FROM pnz_payment_methods ORDER BY payment_method ASC";
		List<AlipayPaymentGatewayBean> result = namedParameterJdbcTemplate.query(sql,new PayMethodMapper());
		return result;
	}
	
	private static final class TransactionMapper implements RowMapper<AlipayDashboardBean> {
		public AlipayDashboardBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			AlipayDashboardBean trans = new AlipayDashboardBean();
			trans.setAvg_amount(rs.getDouble("avg_amount"));
			trans.setMax_amount(rs.getDouble("max_amount"));
			trans.setMin_amount(rs.getDouble("min_amount"));
			trans.setTrans_num(rs.getInt("trans_num"));
			trans.setPayment_method(rs.getString("payment_method"));
			//trans.setPayment_method_id(rs.getInt("payment_method_id"));
			trans.setSum_amount(rs.getDouble("sum_amount"));
			return trans;
		}
	}

	private static final class PayMethodMapper implements RowMapper<AlipayPaymentGatewayBean> {
		public AlipayPaymentGatewayBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			AlipayPaymentGatewayBean payMethod = new AlipayPaymentGatewayBean();
			payMethod.setId(rs.getInt("id"));
			payMethod.setPayment_method(rs.getString("payment_method"));
		//	payMethod.setType(rs.getInt("type"));
			//payMethod.setPayment_method_type_id(rs.getInt("payment_method_type_id"));
			return payMethod;
		}
	}

	@Override
	public List<AlipayAPIResponse> searchTransactionDataById(String transactionId) {
		String sql = "select ars.id, ars.method_type,ars.pg_merchant_transaction_id,ars.dy_merchant_id, ars.pg_is_success, ars.pg_result_code, ars.pg_error, ars.pg_alipay_buyer_login_id, ars.pg_alipay_buyer_user_id, ars.pg_partner_trans_id, ars.pg_partner_refund_id, ars.pg_alipay_trans_id, ars.pg_alipay_pay_time, ars.pg_alipay_reverse_time, ars.pg_alipay_cancel_time, ars.pg_transaction_date, ars.mc_currency, ars.mc_trans_name, ars.mc_trans_amount, ars.pg_exchange_rate, ars.pg_trans_amount_cny, ars.request_time, arq.mc_reference, arq.mc_comment, arq.mc_latitude, arq.mc_longitude, arq.mc_email, arq.mc_mobile, arq.mc_trans_amount from AlipayAPI_Response ars , AlipayAPI_Request arq where arq.mc_partner_trans_id = ars.pg_partner_trans_id and ars.id = '"
                + transactionId + "'";
		AlipayAPIResponse alipayAPIResponse = null;
        List<AlipayAPIResponse> responseList = new ArrayList<AlipayAPIResponse>();
        List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> record : resultSet) {
            if (record != null && record.size() != 0) {
            	if(record.get("pg_transaction_date")!=null)
            	{
            		
            		if(record.get("mc_trans_amount").equals("")||Double.parseDouble(record.get("mc_trans_amount").toString())== 0||record.get("mc_trans_amount").equals("0.00"))
            		{
            			
            		}
            		else
            		{

                        alipayAPIResponse = new AlipayAPIResponse();
                        Integer id = record.get("id") != null ? (Integer) record.get("id") : 0;
                        alipayAPIResponse.setId(id);
                        alipayAPIResponse.setDyMerchantId(record.get("dy_merchant_id") != null ? record.get("dy_merchant_id").toString() : "");
                        alipayAPIResponse.setPgIsSuccess(record.get("pg_is_success") != null ? record.get("pg_is_success").toString() : "");
                        alipayAPIResponse.setPgResultCode(record.get("pg_result_code") != null ? record.get("pg_result_code").toString() : "");
                        alipayAPIResponse.setPgError(record.get("pg_error") != null ? record.get("pg_error").toString() : "");
                        alipayAPIResponse.setPgAlipayBuyerLoginId(record.get("pg_alipay_buyer_login_id") != null ? record.get("pg_alipay_buyer_login_id").toString() : "");
                        alipayAPIResponse.setPgAlipayBuyerUserId(record.get("pg_alipay_buyer_user_id") != null ? record.get("pg_alipay_buyer_user_id").toString() : "");
                        alipayAPIResponse.setPgPartnerTransId(record.get("pg_partner_trans_id") != null ? record.get("pg_partner_trans_id").toString() : "");
                        alipayAPIResponse.setMerchantRefundId(record.get("pg_partner_refund_id") != null ? record.get("pg_partner_refund_id").toString() : "");
                        alipayAPIResponse.setPgAlipayTransId(record.get("pg_alipay_trans_id") != null ? record.get("pg_alipay_trans_id").toString() : "");
                        alipayAPIResponse.setPgAlipayReverseTime(record.get("pg_alipay_reverse_time") != null ? record.get("pg_alipay_reverse_time").toString() : "");
                        alipayAPIResponse.setPgAlipayCancelTime(record.get("pg_alipay_cancel_time") != null ? record.get("pg_alipay_cancel_time").toString() : "");
                        alipayAPIResponse.setMcCurrency(record.get("mc_currency") != null ? record.get("mc_currency").toString() : "");
                        alipayAPIResponse.setMcItemName(record.get("mc_trans_name") != null ? record.get("mc_trans_name").toString() : "");
                        alipayAPIResponse.setMcTransAmount(record.get("mc_trans_amount") != null ? record.get("mc_trans_amount").toString() : "");
                        alipayAPIResponse.setPgExchangeRate(record.get("pg_exchange_rate") != null ? Double.parseDouble(record.get("pg_exchange_rate").toString()) : 0);
                        alipayAPIResponse.setPgTransAmountCny(record.get("pg_trans_amount_cny") != null ? Double.parseDouble(record.get("pg_trans_amount_cny").toString()) : 0);
                        alipayAPIResponse.setRequestTime(record.get("request_time") != null ? record.get("request_time").toString() : "");
                        alipayAPIResponse.setMcReference(record.get("mc_reference") != null ? record.get("mc_reference").toString() : "");
                        alipayAPIResponse.setMcComment(record.get("mc_comment") != null ? record.get("mc_comment").toString() : "");
                        alipayAPIResponse.setMcLatitude(record.get("mc_latitude") != null ? record.get("mc_latitude").toString() : "");
                        alipayAPIResponse.setMcLongitude(record.get("mc_longitude") != null ? record.get("mc_longitude").toString() : "");
                        alipayAPIResponse.setMcEmail(record.get("mc_email") != null ? record.get("mc_email").toString() : "");
                        alipayAPIResponse.setMcMobile(record.get("mc_mobile") != null ? record.get("mc_mobile").toString() : "");
                        alipayAPIResponse.setInfidigiUserId(record.get("infidigiUserId") != null ? record.get("infidigiUserId").toString() : "");
                        alipayAPIResponse.setChannel(record.get("method_type") != null ? record.get("method_type").toString() : "");
                        alipayAPIResponse.setTransaction_type(record.get("transaction_type") != null ? record.get("transaction_type").toString() : "");
                        if(record.get("pg_transaction_date")!=null)
                        {
                        String s[]=record.get("pg_transaction_date").toString().split("\\.");
                        alipayAPIResponse.setTransactionDate(record.get("pg_transaction_date") != null ? s[0] : "");
                        alipayAPIResponse.setPgAlipayPayTime(s[0]);
                        }
                        else
                        {
                        	alipayAPIResponse.setTransactionDate("");
                        	alipayAPIResponse.setPgAlipayPayTime("");
                        }
                        alipayAPIResponse.setPgMerchantTransactionId(record.get("pg_merchant_transaction_id")!= null ? record.get("pg_merchant_transaction_id").toString() : "");
                        
                        responseList.add(alipayAPIResponse);
                    
            		}
            	}
            }
        }
        return responseList;	
        
	}

}
