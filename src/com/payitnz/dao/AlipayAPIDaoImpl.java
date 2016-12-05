package com.payitnz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dynamicpayment.paymentexpress.DPSRequestBean;
import com.payitnz.config.DBTables;
import com.payitnz.model.AlipayAPIRequest;
import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.GenericAPIResponse;
import com.payitnz.model.RequestBean;
import com.payitnz.model.User;

@Repository
public class AlipayAPIDaoImpl implements AlipayAPIDao {

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
    public void save(AlipayAPIRequest alipayAPIRequest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO AlipayAPI_Request(id, dy_merchant_id, pg_service, pg_sign, pg_sign_type, pg_partner_id, pg_input_char_set, pg_alipay_seller_id, mc_quantity_commodity, mc_trans_name, mc_reference, mc_comment, mc_latitude, mc_longitude, mc_email, mc_mobile, mc_partner_trans_id, mc_partner_refund_id, mc_refund_reason, mc_currency, mc_trans_amount,  pg_buyer_identity_code, pg_identity_code_type, mc_trans_create_time, mc_memo, pg_biz_product, pg_extend_info, pg_send_format, request_time, ip_address, request_by, soft_delete) "
                + "VALUES (:id, :dyMerchantId, :pgService, :pgSign, :pgSignType , :pgPartnerId , :pgInputCharset, :pgAlipaySellerId, :mcQuantityCommodity, :mcTransName, :mcReference, :mcComment, :mcLatitude, :mcLongitude, :mcEmail, :mcMobile, :mcPartnerTransId, :mcPartnerRefundId, :mcRefundReason, :mcCurrency, :mcTransAmount, :pgBuyerIdentityCode, :pgIdentityCodeType, :mcTransCreateTime, :mcMemo, :pgBizProduct, :pgExtendInfo, :pgSendFormat, :requestTime, :ipAddress, :requestBy, :softDelete)";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(alipayAPIRequest), keyHolder);
        alipayAPIRequest.setId(keyHolder.getKey().intValue());

    }

    @Override
    public void savef2C(RequestBean alipayAPIRequest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO request_flo2cash(id, userid, cmd, account_id, custom_data, return_url, notification_url, header_image, header_bottom_border, header_background_colour, store_card, display_customer_email, amount, reference, particular, url, mcPartnerTransId)"
                + "VALUES (:id, :userid, :cmd, :account_id, :custom_data , :return_url , :notification_url, :header_image, :header_bottom_border, :header_background_colour, :store_card, :display_customer_email, :amount, :reference, :particular, :url, :mcPartnerTransId)";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModelRequest(alipayAPIRequest), keyHolder);
        alipayAPIRequest.setId(keyHolder.getKey().intValue());

    }
    @Override
    public void savePolireq(RequestBean alipayAPIRequest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO request_poli(id, userid, CurrencyCode, MerchantReference, MerchantHomepageURL, SuccessURL, FailureURL, CancellationURL, NotificationURL, url, mcPartnerTransId, Amount)"
                + "VALUES (:id, :userid, :CurrencyCode, :MerchantReference, :MerchantHomepageURL , :SuccessURL , :FailureURL, :CancellationURL, :NotificationURL, :url, :mcPartnerTransId, :amount)";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModelPoliRequest(alipayAPIRequest), keyHolder);
        alipayAPIRequest.setId(keyHolder.getKey().intValue());

    }
    @Override
	public void save(DPSRequestBean dpsRequest) {
		// TODO Auto-generated method stub
		 KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO "+DBTables.ALIPAY_DPS_REQUEST+" (id, merchant_id, PxPayKey, AmountInput, BillingId, CurrencyInput, EmailAddress, EnableAddBillCard, MerchantReference, Opt, TxnData1, TxnData2, TxnData3, TxnId, TxnType, UrlFail, UrlSuccess, request_time, ip_address, soft_delete) "
                + "VALUES (:id, :merchantId, :pxPayKey, :amountInput, :billingId, :currencyInput, :emailAddress, :enableAddBillCard, :merchantReference, :opt, :txnData1, :txnData2, :txnData3, :txnId, :txnType, :urlFail, :urlSuccess, :requestTime, :ipAddress, :softDelete)";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(dpsRequest), keyHolder);
        dpsRequest.setId(keyHolder.getKey().intValue());
	}
    @Override
    public void saveCUPreq(RequestBean alipayAPIRequest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
//        
//        id, userid, pgPartnerTransId, backEndUrl, charset, commodityDiscount, commodityName, commodityQuantity, commodityUnitPrice, 
//        commodityUrl, customerIp, customerName, defaultBankNumber, defaultPayType, frontEndUrl, merAbbr, merId, merReserved, orderAmount,
//        orderCurrency, orderNumber, orderTime, origQid, signMethod, signature, transTimeout, transType, transferFee, version, url
        
        
        String sql = "INSERT INTO request_cup(id, userid, backEndUrl, charset, commodityDiscount, commodityName, commodityQuantity, commodityUnitPrice, commodityUrl,customerIp, customerName, defaultBankNumber, defaultPayType, frontEndUrl, merAbbr, merId, merReserved, orderAmount, orderCurrency, orderNumber, orderTime, origQid, signMethod, signature, transTimeout, transferFee, version, url, pgPartnerTransId)"
                + "VALUES (:id, :userid, :backEndUrl, :charset, :commodityDiscount , :commodityName , :commodityQuantity, :commodityUnitPrice, :commodityUrl, :customerIp, :customerName, :defaultBankNumber, :defaultPayType, :frontEndUrl, :merAbbr, :merId, :merReserved, :orderAmount, :orderCurrency, :orderNumber, :orderTime, :origQid, :signMethod, :signature, :transTimeout, :transferFee, :version, :url, :pgPartnerTransId)";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModelCUPRequest(alipayAPIRequest), keyHolder);
        alipayAPIRequest.setId(keyHolder.getKey().intValue());

    }
    private SqlParameterSource getSqlParameterByModel(DPSRequestBean alipayAPIRequest) {

	 	System.out.println("Merchant Id:"+alipayAPIRequest.getMerchantId());
	 
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", alipayAPIRequest.getId());
        paramSource.addValue("merchantId", alipayAPIRequest.getMerchantId());
        paramSource.addValue("pxPayKey", alipayAPIRequest.getPxPayKey());
        paramSource.addValue("amountInput", alipayAPIRequest.getAmountInput());
        paramSource.addValue("billingId", alipayAPIRequest.getBillingId());
        paramSource.addValue("currencyInput", alipayAPIRequest.getCurrencyInput());
        paramSource.addValue("emailAddress", alipayAPIRequest.getEmailAddress());
        paramSource.addValue("enableAddBillCard", alipayAPIRequest.getEnableAddBillCard());
        paramSource.addValue("merchantReference", alipayAPIRequest.getMerchantReference());
        paramSource.addValue("opt", alipayAPIRequest.getOpt());
        paramSource.addValue("txnData1", alipayAPIRequest.getTxnData1());
        paramSource.addValue("txnData2", alipayAPIRequest.getTxnData2());
        paramSource.addValue("txnData3", alipayAPIRequest.getTxnData3());
        paramSource.addValue("txnId", alipayAPIRequest.getTxnId());
        paramSource.addValue("txnType", alipayAPIRequest.getTxnType());
        paramSource.addValue("urlFail", alipayAPIRequest.getUrlFail());
        paramSource.addValue("urlSuccess", alipayAPIRequest.getUrlSuccess());	     
        paramSource.addValue("requestTime", alipayAPIRequest.getRequestTime());
        paramSource.addValue("ipAddress", alipayAPIRequest.getIpAddress());
        paramSource.addValue("softDelete", alipayAPIRequest.getSoftDelete());

        return paramSource;
    }

    private SqlParameterSource getSqlParameterByModel(AlipayAPIRequest alipayAPIRequest) {

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", alipayAPIRequest.getId());
        paramSource.addValue("dyMerchantId", alipayAPIRequest.getDyMerchantId());
        paramSource.addValue("pgService", alipayAPIRequest.getPgService());
        paramSource.addValue("pgSign", alipayAPIRequest.getPgSign());
        paramSource.addValue("pgSignType", alipayAPIRequest.getPgSignType());
        paramSource.addValue("pgPartnerId", alipayAPIRequest.getPgPartnerId());
        paramSource.addValue("pgInputCharset", alipayAPIRequest.getPgInputCharset());
        paramSource.addValue("pgAlipaySellerId", alipayAPIRequest.getPgAlipaySellerId());
        paramSource.addValue("mcQuantityCommodity", alipayAPIRequest.getMcQuantityCommodity());
        paramSource.addValue("mcTransName", alipayAPIRequest.getMcTransName());
        paramSource.addValue("mcReference", alipayAPIRequest.getMcReference());
        paramSource.addValue("mcComment", alipayAPIRequest.getMcComment());
        paramSource.addValue("mcLatitude", alipayAPIRequest.getMcLatitude());
        paramSource.addValue("mcLongitude", alipayAPIRequest.getMcLongitude());
        paramSource.addValue("mcEmail", alipayAPIRequest.getMcEmail());
        paramSource.addValue("mcMobile", alipayAPIRequest.getMcMobile());
        paramSource.addValue("mcPartnerTransId", alipayAPIRequest.getMcPartnerTransId());
        paramSource.addValue("mcPartnerRefundId", alipayAPIRequest.getMcPartnerRefundId());
        paramSource.addValue("mcRefundReason", alipayAPIRequest.getMcRefundReason());
        paramSource.addValue("mcCurrency", alipayAPIRequest.getMcCurrency());
        paramSource.addValue("pgSendFormat", alipayAPIRequest.getPgSendFormat());
        paramSource.addValue("mcTransAmount", alipayAPIRequest.getMcTransAmount());
        paramSource.addValue("pgBuyerIdentityCode", alipayAPIRequest.getPgBuyerIdentityCode());
        paramSource.addValue("pgIdentityCodeType", alipayAPIRequest.getPgIdentityCodeType());
        paramSource.addValue("mcTransCreateTime", alipayAPIRequest.getMcTransCreateTime());
        paramSource.addValue("mcMemo", alipayAPIRequest.getMcMemo());
        paramSource.addValue("pgBizProduct", alipayAPIRequest.getPgBizProduct());
        paramSource.addValue("pgExtendInfo", alipayAPIRequest.getPgExtendInfo());
        paramSource.addValue("requestTime", alipayAPIRequest.getRequestTime());
        paramSource.addValue("ipAddress", alipayAPIRequest.getIpAddress());
        paramSource.addValue("requestBy", alipayAPIRequest.getRequestBy());
        paramSource.addValue("softDelete", alipayAPIRequest.getSoftDelete());

        return paramSource;
    }

    private SqlParameterSource getSqlParameterByModelRequest(RequestBean alipayAPIRequest) {

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", alipayAPIRequest.getId());
        paramSource.addValue("userid", alipayAPIRequest.getUserid());
        paramSource.addValue("cmd", alipayAPIRequest.getCmd());
        paramSource.addValue("account_id", alipayAPIRequest.getAccount_id());
        paramSource.addValue("custom_data", alipayAPIRequest.getCustom_data());
        paramSource.addValue("return_url", alipayAPIRequest.getReturn_url());
        paramSource.addValue("notification_url", alipayAPIRequest.getF2c_notification_url());
        paramSource.addValue("header_image", alipayAPIRequest.getHeader_image());
        paramSource.addValue("header_bottom_border", alipayAPIRequest.getHeader_bottom_border());
        paramSource.addValue("header_background_colour", alipayAPIRequest.getHeader_background_colour());
        paramSource.addValue("store_card", alipayAPIRequest.getStore_card());
        paramSource.addValue("display_customer_email", alipayAPIRequest.getDisplay_customer_email());
        paramSource.addValue("amount", alipayAPIRequest.getAmount());
        paramSource.addValue("reference", alipayAPIRequest.getReference());
        paramSource.addValue("particular", alipayAPIRequest.getParticular());
        paramSource.addValue("url", alipayAPIRequest.getUrl());
        paramSource.addValue("mcPartnerTransId", alipayAPIRequest.getMcPartnerTransId());
        return paramSource;
    }
    private SqlParameterSource getSqlParameterByModelPoliRequest(RequestBean alipayAPIRequest) {

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", alipayAPIRequest.getId());
        paramSource.addValue("userid", alipayAPIRequest.getUserid());
        paramSource.addValue("cmd", alipayAPIRequest.getId());
        paramSource.addValue("CurrencyCode", alipayAPIRequest.getCurrencyCode());
        paramSource.addValue("MerchantReference", alipayAPIRequest.getMerchantReference());
        paramSource.addValue("MerchantHomepageURL", alipayAPIRequest.getMerchantHomepageURL());
        paramSource.addValue("SuccessURL", alipayAPIRequest.getSuccessURL());
        paramSource.addValue("FailureURL", alipayAPIRequest.getFailureURL());
        paramSource.addValue("CancellationURL", alipayAPIRequest.getCancellationURL());
        paramSource.addValue("NotificationURL", alipayAPIRequest.getPoli_NotificationURL());
        paramSource.addValue("amount", alipayAPIRequest.getAmount());
        paramSource.addValue("url", alipayAPIRequest.getUrl());
        paramSource.addValue("mcPartnerTransId", alipayAPIRequest.getMcPartnerTransId());
        return paramSource;

    }
    
    private SqlParameterSource getSqlParameterByModelCUPRequest(RequestBean alipayAPIRequest) {

//      id, userid, pgPartnerTransId, backEndUrl, charset, commodityDiscount, commodityName, commodityQuantity, commodityUnitPrice, 
//      commodityUrl, customerIp, customerName, defaultBankNumber, defaultPayType, frontEndUrl, merAbbr, merId, merReserved, orderAmount,
//      orderCurrency, orderNumber, orderTime, origQid, signMethod, signature, transTimeout, transType, transferFee, version, url
    	
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", alipayAPIRequest.getId());
        paramSource.addValue("userid", alipayAPIRequest.getUserid());
        paramSource.addValue("backEndUrl", alipayAPIRequest.getBackEndUrl());
        paramSource.addValue("charset", alipayAPIRequest.getCharset());
        paramSource.addValue("commodityDiscount", alipayAPIRequest.getCommodityDiscount());
        paramSource.addValue("commodityName", alipayAPIRequest.getCommodityName());
        paramSource.addValue("commodityQuantity", alipayAPIRequest.getCommodityQuantity());
        paramSource.addValue("commodityUnitPrice", alipayAPIRequest.getCommodityUnitPrice());
        paramSource.addValue("commodityUrl", alipayAPIRequest.getCommodityUrl());
        paramSource.addValue("customerIp", alipayAPIRequest.getCustomerIp());
        paramSource.addValue("customerName", alipayAPIRequest.getCustomerName());
        paramSource.addValue("defaultBankNumber", alipayAPIRequest.getDefaultBankNumber());
        paramSource.addValue("defaultPayType", alipayAPIRequest.getDefaultPayType());
        paramSource.addValue("frontEndUrl", alipayAPIRequest.getFrontEndUrl());
        paramSource.addValue("merAbbr", alipayAPIRequest.getMerAbbr());
        paramSource.addValue("merId", alipayAPIRequest.getMerId());
        paramSource.addValue("merReserved", alipayAPIRequest.getMerReserved());
        paramSource.addValue("orderAmount", alipayAPIRequest.getTransferFee());
        paramSource.addValue("orderCurrency", alipayAPIRequest.getOrderCurrency());
        paramSource.addValue("orderNumber", alipayAPIRequest.getOrderNumber());
        paramSource.addValue("orderTime", alipayAPIRequest.getOrderTime());
        paramSource.addValue("origQid", alipayAPIRequest.getOrigQid());
        paramSource.addValue("signMethod", alipayAPIRequest.getSignMethod());
        paramSource.addValue("signature", alipayAPIRequest.getSignature());

        paramSource.addValue("transTimeout", alipayAPIRequest.getTransTimeout());
        paramSource.addValue("transType", alipayAPIRequest.getTransType());
        paramSource.addValue("transferFee", alipayAPIRequest.getTransferFee());
        paramSource.addValue("version", alipayAPIRequest.getVersion());
   
        paramSource.addValue("url", alipayAPIRequest.getUrl());
        paramSource.addValue("pgPartnerTransId", alipayAPIRequest.getMcPartnerTransId());
        return paramSource;

    }
    @Override
    public void save(AlipayAPIResponse alipayAPIResponse) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        System.out.println("id in saveorupdate"+alipayAPIResponse.getId());
        String sql = "INSERT INTO AlipayAPI_Response(id, dy_merchant_id, pg_is_success, pg_sign, pg_sign_type, pg_result_code, pg_error, pg_alipay_buyer_login_id, pg_alipay_buyer_user_id, pg_partner_trans_id, pg_partner_refund_id, pg_alipay_trans_id, pg_alipay_pay_time, pg_alipay_reverse_time, pg_alipay_cancel_time, pg_transaction_date, mc_currency, mc_trans_name, mc_trans_amount, pg_exchange_rate, pg_trans_amount_cny, request_time, ip_address, request_by, soft_delete, infidigiUserId, transaction_type, method_type, remark, reference) "
                + "VALUES (:id, :dyMerchantId, :pgIsSuccess, :pgSign, :pgSignType, :pgResultCode, :pgError, :pgAlipayBuyerLoginId, :pgAlipayBuyerUserId, :pgPartnerTransId, :pgPartnerRefundId, :pgAlipayTransId , :pgAlipayPayTime, :pgAlipayReverseTime, :pgAlipayCancelTime, :pgTransactionDate, :mcCurrency, :mcTransName, :mcTransAmount, :pgExchangeRate, :pgTransAmountCny, :requestTime, :ipAddress, :requestBy, :softDelete, :infidigiUserId, :transaction_type, :method_type, :remark, :reference)";

        namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(alipayAPIResponse), keyHolder);
        alipayAPIResponse.setId(keyHolder.getKey().intValue());
    }
    @Override
	public void update(AlipayAPIResponse alipayAPIResponse) {
		// TODO Auto-generated method stub
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", alipayAPIResponse.getId());
    	   String sql = "UPDATE AlipayAPI_Response SET id=:id, pg_result_code=:pgResultCode, pg_alipay_cancel_time=:pgAlipayCancelTime, pg_is_success=:pgIsSuccess,cupReserved=:cupReserved, traceNumber=:traceNumber, mc_currency=:mcCurrency, pg_alipay_buyer_user_id=:pgAlipayBuyerUserId, orderNumber=:orderNumber, pg_alipay_trans_id=:pgAlipayTransId, settleCurrency=:settleCurrency, signMethod=:signMethod, pg_sign=:pgSign, pg_transaction_date=:pgTransactionDate, card_type=:card_type WHERE id=:id";

           namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(alipayAPIResponse));
		
	}
    private SqlParameterSource getSqlParameterByModel(AlipayAPIResponse alipayAPIResponse) {

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", alipayAPIResponse.getId());
        System.out.println(alipayAPIResponse.getId());
        paramSource.addValue("dyMerchantId", alipayAPIResponse.getDyMerchantId());
        paramSource.addValue("pgIsSuccess", alipayAPIResponse.getPgIsSuccess());
        paramSource.addValue("pgSign", alipayAPIResponse.getPgSign());
        paramSource.addValue("pgSignType", alipayAPIResponse.getPgSignType());
        paramSource.addValue("pgResultCode", alipayAPIResponse.getPgResultCode());
        paramSource.addValue("pgError", alipayAPIResponse.getPgError());
        paramSource.addValue("pgAlipayBuyerLoginId", alipayAPIResponse.getPgAlipayBuyerLoginId());
        paramSource.addValue("pgAlipayBuyerUserId", alipayAPIResponse.getPgAlipayBuyerUserId());
        paramSource.addValue("pgPartnerTransId", alipayAPIResponse.getPgPartnerTransId());
        paramSource.addValue("pgPartnerRefundId", alipayAPIResponse.getMerchantRefundId());
        paramSource.addValue("pgAlipayTransId", alipayAPIResponse.getPgAlipayTransId());
        paramSource.addValue("pgAlipayPayTime", alipayAPIResponse.getPgAlipayPayTime());
        paramSource.addValue("pgAlipayReverseTime", alipayAPIResponse.getPgAlipayReverseTime());
        paramSource.addValue("pgAlipayCancelTime", alipayAPIResponse.getPgAlipayCancelTime());
        paramSource.addValue("pgTransactionDate", alipayAPIResponse.getPgTransactionDate());
        paramSource.addValue("mcCurrency", alipayAPIResponse.getMcCurrency());
        paramSource.addValue("mcTransName", alipayAPIResponse.getMcItemName());
        paramSource.addValue("mcTransAmount", alipayAPIResponse.getAmount());
        System.out.println("Transaction amount ===="+alipayAPIResponse.getPgTransactionDate());
        paramSource.addValue("pgExchangeRate", alipayAPIResponse.getPgExchangeRate());
        paramSource.addValue("pgTransAmountCny", alipayAPIResponse.getPgTransAmountCny());
        paramSource.addValue("requestTime", alipayAPIResponse.getRequestTime());
        paramSource.addValue("ipAddress", alipayAPIResponse.getIpAddress());
        paramSource.addValue("requestBy", alipayAPIResponse.getRequestBy());
        paramSource.addValue("softDelete", alipayAPIResponse.getSoftDelete());
        paramSource.addValue("infidigiUserId", alipayAPIResponse.getInfidigiUserId());
        paramSource.addValue("transaction_type", alipayAPIResponse.getTransaction_type());
        paramSource.addValue("method_type", alipayAPIResponse.getMethod_type());
        paramSource.addValue("settleCurrency", alipayAPIResponse.getSettleCurrency());
        paramSource.addValue("cupReserved", alipayAPIResponse.getCupReserved());
        paramSource.addValue("orderNumber", alipayAPIResponse.getOrderNumber());
        paramSource.addValue("signMethod", alipayAPIResponse.getSignMethod()); 
        paramSource.addValue("traceNumber", alipayAPIResponse.getTraceNumber()); 
        paramSource.addValue("remark", alipayAPIResponse.getRemark()); 
        paramSource.addValue("reference", alipayAPIResponse.getReference());
        paramSource.addValue("card_type", alipayAPIResponse.getCardType());
        
        return paramSource;
    }

    @Override
    public User getUser(String infidigiAccountId, String infidigiUserId, String infidigiPassword) {
        User user = null;
        String sql = "select * from users_payitnz where infidigiAccountId = '" + infidigiAccountId + "'  and infidigiUserId = '" + infidigiUserId + "'  and infidigiPassword = '" + infidigiPassword + "'";

        List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> record : resultSet) {
            if (record != null && record.size() != 0) {
                user = new User();
                Integer id = record.get("id") != null ? (Integer) record.get("id") : 0;
                user.setId(id);
                user.setInfidigiAccountId(infidigiAccountId);
                user.setInfidigiUserId(infidigiUserId);
                
            }
        }
        System.out.println("sql==="+sql);
        return user;

    }

    @Override
    public AlipayAPIResponse getTransactionDetailsByPartnerTransactionId(String merchantTransactionId) {
        String sql = "select ars.id, ars.dy_merchant_id, ars.pg_is_success, ars.pg_result_code, ars.method_type, ars.pg_error, ars.pg_alipay_buyer_login_id, ars.pg_alipay_buyer_user_id, ars.mc_trans_amount, ars.pg_partner_trans_id, ars.pg_partner_refund_id, ars.pg_alipay_trans_id, ars.pg_alipay_pay_time, ars.pg_alipay_reverse_time, ars.pg_alipay_cancel_time, ars.pg_transaction_date, ars.mc_currency, ars.mc_trans_name, ars.pg_exchange_rate, ars.pg_trans_amount_cny, ars.request_time, arq.mc_reference, arq.mc_comment, arq.mc_latitude, arq.mc_longitude, arq.mc_email, arq.mc_mobile, arq.mc_trans_amount from AlipayAPI_Response ars , AlipayAPI_Request arq where arq.mc_partner_trans_id = ars.pg_partner_trans_id and ars.pg_partner_trans_id = '"
                + merchantTransactionId + "'";
        AlipayAPIResponse alipayAPIResponse = null;
        List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> record : resultSet) {
            if (record != null && record.size() != 0) {
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
                alipayAPIResponse.setPgAlipayPayTime(record.get("pg_alipay_pay_time") != null ? record.get("pg_alipay_pay_time").toString() : "");
                alipayAPIResponse.setPgAlipayReverseTime(record.get("pg_alipay_reverse_time") != null ? record.get("pg_alipay_reverse_time").toString() : "");
                alipayAPIResponse.setPgAlipayCancelTime(record.get("pg_alipay_cancel_time") != null ? record.get("pg_alipay_cancel_time").toString() : "");
                alipayAPIResponse.setMcCurrency(record.get("mc_currency") != null ? record.get("mc_currency").toString() : "");
//                DecimalFormat df = new DecimalFormat("#.00");
//                String angleFormated = df.format(record.get("mc_trans_amount") != null ? Double.parseDouble(record.get("mc_trans_amount").toString()) : 0);
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
                alipayAPIResponse.setMcItemName(record.get("mc_trans_name") != null ? record.get("mc_trans_name").toString() : "");
                alipayAPIResponse.setMethod_type(record.get("method_type") != null ? record.get("method_type").toString() : "");
                alipayAPIResponse.setPgTransactionDate((Date) record.get("pg_transaction_date"));
                
            }
        }
        return alipayAPIResponse;
    }

    @Override
    public List<GenericAPIResponse> getTransactionDetailsByCriteria(AlipayWalletVO alipayWalletVO) {
        String sql = "select ars.id, ars.dy_merchant_id, ars.pg_is_success, ars.pg_result_code, ars.pg_error, ars.pg_alipay_buyer_login_id, ars.pg_alipay_buyer_user_id, ars.pg_partner_trans_id, ars.pg_partner_refund_id, ars.pg_alipay_trans_id, ars.pg_alipay_pay_time, ars.pg_alipay_reverse_time, ars.pg_alipay_cancel_time, ars.pg_transaction_date, ars.mc_currency, ars.mc_trans_name, ars.mc_trans_amount, ars.pg_exchange_rate, ars.pg_trans_amount_cny, ars.request_time, arq.mc_reference, arq.mc_comment, arq.mc_latitude, arq.mc_longitude, arq.mc_email, arq.mc_mobile, arq.mc_trans_amount from AlipayAPI_Response ars , AlipayAPI_Request arq where (arq.mc_partner_trans_id = ars.pg_partner_trans_id)";

        StringBuffer whereClause = new StringBuffer("");
        boolean flag = true;
        
        if (!StringUtils.isEmpty(alipayWalletVO.getAmount())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            whereClause.append(" ars.mc_trans_amount = ");
            whereClause.append(alipayWalletVO.getAmount());
            flag = true;
        }

        if (!StringUtils.isEmpty(alipayWalletVO.getMerchantTransactionId())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            whereClause.append(" ars.pg_partner_trans_id = ");
            whereClause.append(alipayWalletVO.getMerchantTransactionId());
            flag = true;
        }

        if (!StringUtils.isEmpty(alipayWalletVO.getParticular())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            whereClause.append(" ars.mc_trans_name = '");
            whereClause.append(alipayWalletVO.getParticular());
            whereClause.append("'");
            flag = true;
        }

        if (!StringUtils.isEmpty(alipayWalletVO.getReference())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            whereClause.append(" arq.mc_reference = '");
            whereClause.append(alipayWalletVO.getReference());
            whereClause.append("'");
            flag = true;
        }

        SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd");
        
        if (!StringUtils.isEmpty(alipayWalletVO.getStartDate()) && !StringUtils.isEmpty(alipayWalletVO.getEndDate())) {
        	String date1,date2 ="";
            try {
                date1 = DATE_FORMAT2.format(DATE_FORMAT1.parse(alipayWalletVO.getStartDate()))+" "+"00:00:01";
                date2 = DATE_FORMAT2.format(DATE_FORMAT1.parse(alipayWalletVO.getEndDate()))+" "+"23:59:59";
                if (flag) {
                    whereClause.append(" AND ");
                }
                whereClause.append(" pg_transaction_date BETWEEN '");
                whereClause.append(date1);
                whereClause.append("'");
                whereClause.append(" AND ");
                whereClause.append("'");
                whereClause.append(date2);
                whereClause.append("'");
                flag = true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if (!StringUtils.isEmpty(alipayWalletVO.getStartDate())) {
            String date1="";
            try {
                date1 = DATE_FORMAT2.format(DATE_FORMAT1.parse(alipayWalletVO.getStartDate()))+" "+"00:00:01";
                if (flag) {
                    whereClause.append(" AND ");
                }
                whereClause.append(" pg_transaction_date >= '");
                whereClause.append(date1);
                whereClause.append("'");
                
                flag = true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if (!StringUtils.isEmpty(alipayWalletVO.getEndDate())) {

            String date1 = "";
            try {
                date1 = DATE_FORMAT2.format(DATE_FORMAT1.parse(alipayWalletVO.getEndDate()))+" "+"23:59:59";
                if (flag) {
                    whereClause.append(" pg_transaction_date <= '");
                }
                whereClause.append(date1);
                whereClause.append("'");
                flag = true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        

        if (whereClause.length() > 0) {
            sql = sql + whereClause.toString();
        }
        System.out.println("---sql=" + sql);

        AlipayAPIResponse alipayAPIResponse = null;
        List<GenericAPIResponse> responseList = new ArrayList<GenericAPIResponse>();
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
                        alipayAPIResponse.setChannel(record.get("method_type") != null ? record.get("method_type").toString() : "");
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
                    
                        responseList.add(alipayAPIResponse);
                    
            		}
            	}
            }
        }
        return responseList;

    }
    
    @Override
    public List<AlipayAPIResponse> getTransactionDetailsByCriteriaWeb(AlipayWalletVO alipayWalletVO) {
        String sql = "select * from AlipayAPI_Response ars where";

        StringBuffer whereClause = new StringBuffer("");
        boolean flag = true;
        
        //check role_id     
//        if(alipayWalletVO.getRole_id() == 1){
//			if (flag) {
//                whereClause.append(" AND ");
//            }
//            whereClause.append(" ars.dy_merchant_id = ");
//            whereClause.append(alipayWalletVO.getUser_id());
//            flag = true;	
//		}
//        
        if (!StringUtils.isEmpty(alipayWalletVO.getUserID())) {
            if(alipayWalletVO.getRole_id() == 2 || alipayWalletVO.getRole_id() == 3 || alipayWalletVO.getRole_id() == 1){
          		 {
//          			 if (flag) {
//                        whereClause.append(" AND ");
//                    }
          			 if(alipayWalletVO.getRole_id() == 2 || alipayWalletVO.getRole_id() == 1)
          			 {
          			   whereClause.append(" ars.dy_merchant_id = ");
                      whereClause.append(alipayWalletVO.getUserID());
          			 }
          			 if(alipayWalletVO.getRole_id() == 3 )
          			 {
          			   whereClause.append(" ars.infidigiUserId = ");
                      whereClause.append(alipayWalletVO.getUserID());
          			 }
          			 
          		 }
            }
            flag = true;
        }
        
        if (!StringUtils.isEmpty(alipayWalletVO.getAmount())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            whereClause.append(" ars.mc_trans_amount = ");
            whereClause.append(alipayWalletVO.getAmount());
            flag = true;
        }
     
        if (!StringUtils.isEmpty(alipayWalletVO.getPgPartnerTransId())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            whereClause.append(" ars.pg_partner_trans_id = ");
            whereClause.append(alipayWalletVO.getPgPartnerTransId());
            flag = true;
        }

        if (!StringUtils.isEmpty(alipayWalletVO.getParticular())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            whereClause.append(" ars.mc_trans_name = '");
            whereClause.append(alipayWalletVO.getParticular());
            whereClause.append("'");
            flag = true;
        }

        if (!StringUtils.isEmpty(alipayWalletVO.getReference())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            whereClause.append("  arq.mc_reference = '");
            whereClause.append(alipayWalletVO.getReference());
            whereClause.append("'");
            flag = true;
        }

        if (!StringUtils.isEmpty(alipayWalletVO.getChannel())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            whereClause.append("  ars.method_type = '");
            whereClause.append(alipayWalletVO.getChannel());
            whereClause.append("'");
            flag = true;
        }
        
        if (!StringUtils.isEmpty(alipayWalletVO.getStatus()) && !alipayWalletVO.getStatus().contains("NOT_SETTLED") && !alipayWalletVO.getStatus().contains("IS_SETTLED")) {
            if (flag) {
                whereClause.append(" AND ");
            }
            
            
            whereClause.append("  ars.pg_result_code IN ");
            whereClause.append("("+alipayWalletVO.getStatus()+")");
            whereClause.append("");
            flag = true;
        }
        
        if (!StringUtils.isEmpty(alipayWalletVO.getStatus())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            
            if(alipayWalletVO.getStatus().contains("NOT_SETTLED") || alipayWalletVO.getStatus().contains("IS_SETTLED")){
             
             whereClause.append(" ( ");
             whereClause.append(" ars.is_settled IN ");
             if(alipayWalletVO.getStatus().contains("NOT_SETTLED") && alipayWalletVO.getStatus().contains("IS_SETTLED")){
              whereClause.append("(0,1)");
             }
             else if(alipayWalletVO.getStatus().contains("NOT_SETTLED")){
              whereClause.append("(0)");
             } 
             else if(alipayWalletVO.getStatus().contains("IS_SETTLED")){
              whereClause.append("(1)");
             }
              
             whereClause.append(" OR ");
            }
            
            
            whereClause.append("  ars.pg_result_code IN ");
            whereClause.append("("+alipayWalletVO.getStatus()+")");
            whereClause.append("");
            
            if(alipayWalletVO.getStatus().contains("NOT SETTLED") || alipayWalletVO.getStatus().contains("SETTLED")){
             
             whereClause.append(" ) ");
            }
            
            flag = true;
        }
       
        SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd");

        if (!StringUtils.isEmpty(alipayWalletVO.getStartDate()) && !StringUtils.isEmpty(alipayWalletVO.getEndDate())) {
        	String date1,date2 ="";
            try {
                date1 = DATE_FORMAT2.format(DATE_FORMAT1.parse(alipayWalletVO.getStartDate()))+" "+"00:00:01";
                date2 = DATE_FORMAT2.format(DATE_FORMAT1.parse(alipayWalletVO.getEndDate()))+" "+"23:59:59";
                if (flag) {
                    whereClause.append(" AND ");
                }
                whereClause.append(" pg_transaction_date BETWEEN '");
                whereClause.append(date1);
                whereClause.append("'");
                whereClause.append(" AND ");
                whereClause.append("'");
                whereClause.append(date2);
                whereClause.append("' ");
                flag = true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if (!StringUtils.isEmpty(alipayWalletVO.getStartDate())) {
            String date1="";
            try {
                date1 = DATE_FORMAT2.format(DATE_FORMAT1.parse(alipayWalletVO.getStartDate()))+" "+"00:00:01";
                if (flag) {
                    whereClause.append(" AND ");
                }
                whereClause.append(" pg_transaction_date >= '");
                whereClause.append(date1);
                whereClause.append("' ");
                
                flag = true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if (!StringUtils.isEmpty(alipayWalletVO.getEndDate())) {

            String date1 = "";
            try {
                date1 = DATE_FORMAT2.format(DATE_FORMAT1.parse(alipayWalletVO.getEndDate()))+" "+"23:59:59";
                if (flag) {
                    whereClause.append(" pg_transaction_date <= '");
                }
                whereClause.append(date1);
                whereClause.append("' ");
                flag = true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (whereClause.length() > 0) {
            sql = sql + whereClause.toString();
        }
        
        System.out.println("---sql=" + sql);

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
                        alipayAPIResponse.setReference(record.get("reference") != null ? record.get("reference").toString() : "");
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
    
    
    @Override
    public List<AlipayAPIResponse> getRefundTransactionDetailsByCriteriaWeb(AlipayWalletVO alipayWalletVO) {
        String sql = "select ars.id, ars.dy_merchant_id, ars.pg_is_success, ars.pg_result_code, ars.pg_error, ars.pg_alipay_buyer_login_id, ars.pg_alipay_buyer_user_id, ars.pg_partner_trans_id, ars.pg_partner_refund_id, ars.pg_alipay_trans_id, ars.pg_alipay_pay_time, ars.pg_alipay_reverse_time, ars.pg_alipay_cancel_time, ars.pg_transaction_date, ars.mc_currency, ars.mc_trans_name, ars.mc_trans_amount, ars.pg_exchange_rate, ars.pg_trans_amount_cny, ars.request_time,ars.method_type,ars.transaction_type, arq.mc_reference, arq.mc_comment, arq.mc_latitude, arq.mc_longitude, arq.mc_email, arq.mc_mobile, arq.mc_trans_amount from AlipayAPI_Response ars , AlipayAPI_Request arq where (arq.mc_partner_trans_id = ars.pg_partner_trans_id)";

        StringBuffer whereClause = new StringBuffer("");
        boolean flag = true;
        
        //check role_id     
//        if(alipayWalletVO.getRole_id() == 2 || alipayWalletVO.getRole_id() == 3){
//			if (flag) {
//                whereClause.append(" AND ");
//            }
//            whereClause.append(" ars.dy_merchant_id = ");
//            whereClause.append(alipayWalletVO.getUser_id());
//            flag = true;	
//		}
        
        if (!StringUtils.isEmpty(alipayWalletVO.getUserID())) {
        	
           
            
            if(alipayWalletVO.getRole_id() == 2 || alipayWalletVO.getRole_id() == 3){
       		 {
       			 if (flag) {
                     whereClause.append(" AND ");
                 }
       			 if(alipayWalletVO.getRole_id() == 2 )
       			 {
       			   whereClause.append(" ars.dy_merchant_id = ");
                   whereClause.append(alipayWalletVO.getUserID());
       			 }
       			 if(alipayWalletVO.getRole_id() == 3 )
       			 {
       			   whereClause.append(" ars.infidigiUserId = ");
                   whereClause.append(alipayWalletVO.getUserID());
       			 }
       			 
       		 }
       	 }
         
            flag = true;
        }
        
        if (!StringUtils.isEmpty(alipayWalletVO.getAmount())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            whereClause.append(" ars.mc_trans_amount = ");
            whereClause.append(alipayWalletVO.getAmount());
            flag = true;
        }
     
        if (!StringUtils.isEmpty(alipayWalletVO.getPgPartnerTransId())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            whereClause.append(" ars.pg_partner_trans_id = ");
            whereClause.append(alipayWalletVO.getPgPartnerTransId());
            flag = true;
        }

        if (!StringUtils.isEmpty(alipayWalletVO.getParticular())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            whereClause.append(" ars.mc_trans_name = '");
            whereClause.append(alipayWalletVO.getParticular());
            whereClause.append("'");
            flag = true;
        }

        if (!StringUtils.isEmpty(alipayWalletVO.getReference())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            whereClause.append("  arq.mc_reference = '");
            whereClause.append(alipayWalletVO.getReference());
            whereClause.append("'");
            flag = true;
        }

        if (!StringUtils.isEmpty(alipayWalletVO.getChannel())) {
            if (flag) {
                whereClause.append(" AND ");
            }
            whereClause.append("  ars.method_type = '");
            whereClause.append(alipayWalletVO.getChannel());
            whereClause.append("'");
            flag = true;
        }
              
        
        whereClause.append(" AND (ars.pg_result_code != 'FAILED'");
        whereClause.append(" AND ars.pg_result_code != '')");
       
        SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd");

        if (!StringUtils.isEmpty(alipayWalletVO.getStartDate()) && !StringUtils.isEmpty(alipayWalletVO.getEndDate())) {
        	String date1,date2 ="";
            try {
                date1 = DATE_FORMAT2.format(DATE_FORMAT1.parse(alipayWalletVO.getStartDate()))+" "+"00:00:01";
                date2 = DATE_FORMAT2.format(DATE_FORMAT1.parse(alipayWalletVO.getEndDate()))+" "+"23:59:59";
                if (flag) {
                    whereClause.append(" AND ");
                }
                whereClause.append(" pg_transaction_date BETWEEN '");
                whereClause.append(date1);
                whereClause.append("'");
                whereClause.append(" AND ");
                whereClause.append("'");
                whereClause.append(date2);
                whereClause.append("' ");
                flag = true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if (!StringUtils.isEmpty(alipayWalletVO.getStartDate())) {
            String date1="";
            try {
                date1 = DATE_FORMAT2.format(DATE_FORMAT1.parse(alipayWalletVO.getStartDate()))+" "+"00:00:01";
                if (flag) {
                    whereClause.append(" AND ");
                }
                whereClause.append(" pg_transaction_date >= '");
                whereClause.append(date1);
                whereClause.append("' ");
                
                flag = true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if (!StringUtils.isEmpty(alipayWalletVO.getEndDate())) {

            String date1 = "";
            try {
                date1 = DATE_FORMAT2.format(DATE_FORMAT1.parse(alipayWalletVO.getEndDate()))+" "+"23:59:59";
                if (flag) {
                    whereClause.append(" pg_transaction_date <= '");
                }
                whereClause.append(date1);
                whereClause.append("' ");
                flag = true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (whereClause.length() > 0) {
            sql = sql + whereClause.toString();
        }
        
        System.out.println("---sql=" + sql);

        AlipayAPIResponse alipayAPIResponse = null;
        List<AlipayAPIResponse> responseList = new ArrayList<AlipayAPIResponse>();
        List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(sql);
//        System.out.println("size of refund list"+resultSet.size());
        for (Map<String, Object> record : resultSet) {
            if (record != null && record.size() != 0) {
            	if(record.get("pg_transaction_date")!=null)
            	{
            		
            		if(record.get("mc_trans_amount").equals("")||Double.parseDouble(record.get("mc_trans_amount").toString())== 0||record.get("mc_trans_amount").equals("0.00"))
            		{
//            			System.out.println("amount is wrong" + (record.get("mc_trans_amount").toString()));
            		}
            		else
            		{
//            			System.out.println("amount is right" + (record.get("mc_trans_amount").toString()));
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

    @Override
    public void insertFile(String mcTransactionId, MultipartFile file, String ipAddress, String sender) {
        try {
            LobHandler lobHandler = new DefaultLobHandler(); // reusable object
            String sql = "insert into Merchant_Files values(0,?,?,?,?,?,?,?,'N')";

            jdbcTemplate.update(sql, new Object[] { mcTransactionId, file.getOriginalFilename(), file.getContentType(), new SqlLobValue(file.getInputStream(), (int) file.getSize(), lobHandler), new Date().toString(), ipAddress, sender },
                    new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BLOB, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public byte[] getFile(String mcTransactionId) {
        LobHandler lobHandler = new DefaultLobHandler(); // reusable object
        List<Map<String, Object>> data = jdbcTemplate.query("select id, file_name, file_content from Merchant_Files where mc_partner_trans_id = '" + mcTransactionId + "'", new RowMapper<Map<String, Object>>() {
            public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, Object> results = new HashMap<String, Object>();

                final byte[] blobBytes = lobHandler.getBlobAsBytes(rs, "file_content");
                results.put("BLOB", blobBytes);
                return results;
            }
        });
        return ((byte[]) (data.get(0).get("BLOB")));
    }


    @Override
    public String getMerchantCompanyName(String infidigiAccountId, String infidigiUserID) {
        String sql = "select company_registered_name from users_payitnz where infidigiAccountId = '" + infidigiAccountId + "' AND infidigiUserId = '" + infidigiUserID + "'";
        String compName = "";
        List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> record : resultSet) {
            if (record != null && record.size() != 0) {
                compName = record.get("company_registered_name") != null ? record.get("company_registered_name").toString() : "";

            }
        }
        return compName;
    }
    
    @Override
    public String getMerchantName(String infidigiAccountId, String infidigiUserID) {
        String sql = "select firstName from users_payitnz where infidigiAccountId = '" + infidigiAccountId + "' AND infidigiUserId = '" + infidigiUserID + "'";
        String compName = "";
        List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> record : resultSet) {
            if (record != null && record.size() != 0) {
                compName = record.get("firstName") != null ? record.get("firstName").toString() : "";

            }
        }
        return compName;
    }
    
    @Override
   	public void UpdateUser(User user) {
   		
   		Map<String, Object> params = new HashMap<String, Object>();
   		params.put("id", user.getId());
   	        String sql ="UPDATE users_payitnz SET infidigiPassword=:infidigiPassword WHERE id=:id";
   		    namedParameterJdbcTemplate.update(sql, getSqlParameterByModelUser(user));
   		
   	}
   	
    private SqlParameterSource getSqlParameterByModelUser(User user) {

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", user.getId());
        paramSource.addValue("infidigiAccountId", user.getInfidigiAccountId());
        paramSource.addValue("infidigiUserId", user.getInfidigiUserId());
        paramSource.addValue("infidigiPassword", user.getInfidigiPassword());
      
        return paramSource;
    }
    
    @Override
    public List<AlipayAPIResponse> getTransactionList(AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
   	 List<AlipayAPIResponse> result = null;
   	 Map<String, Object> params = new HashMap<String, Object>();
	        params.put("id", alipayWalletVO.getInfidigiUserId());
	        params.put("resultCode", "SUCCESS");
	        
       if(alipayWalletVO.getType().equals("MyTransactions"))
       {
       	String sql="";
       	if(alipayWalletVO.getSortType().equals("This Month"))
       	{
       		 sql = "SELECT * FROM AlipayAPI_Response ars WHERE (ars.infidigiUserId=:id AND ars.pg_result_code = 'SUCCESS') AND MONTH(ars.pg_transaction_date) = MONTH(CURRENT_DATE()) ORDER BY ars.id DESC";
       		 //SELECT * FROM alipayapi_response WHERE infidigiUserId=:id AND MONTH(pg_transaction_date) = MONTH(CURRENT_DATE());
       	}
      
       	else
       	{
       		if(alipayWalletVO.getSortType().equals("Last Month"))
       		{
       		sql = "SELECT * FROM AlipayAPI_Response ars where (ars.infidigiUserId=:id AND ars.pg_result_code = 'SUCCESS') AND ars.pg_transaction_date BETWEEN DATE_FORMAT(NOW() - INTERVAL 1 MONTH, '%Y-%m-01 00:00:00') AND DATE_FORMAT(LAST_DAY(NOW() - INTERVAL 1 MONTH), '%Y-%m-%d 23:59:59') ORDER BY ars.id DESC";
       		//select * from alipayapi_response where infidigiUserId=:id AND pg_transaction_date BETWEEN DATE_FORMAT(NOW() - INTERVAL 1 MONTH, '%Y-%m-01 00:00:00') AND DATE_FORMAT(LAST_DAY(NOW() - INTERVAL 1 MONTH), '%Y-%m-%d 23:59:59')

       		}
       		else
       		{
       			//select * from dt_table where  `date` >= DATE_SUB(CURDATE(), INTERVAL 10 DAY)
       			sql = "SELECT * FROM AlipayAPI_Response ars WHERE (ars.infidigiUserId=:id AND ars.pg_result_code = 'SUCCESS') AND ars.pg_transaction_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) ORDER BY ars.id DESC";
       		}
       	}
       	
       	try {
//           System.out.println("sql for list"+sql);
           
           result = namedParameterJdbcTemplate.query(sql, params, new TransactionMapper());
  	            if(result.size() == 0) {
               return null;
           } else{
           	
           		return result;
           	              
           }
       } catch (EmptyResultDataAccessException e) {
           // do nothing, return null
       }
       }
       else
       {
       	String sql ="";
       	if(alipayWalletVO.getSortType().equals("This Month"))
       	{
       		 sql = "SELECT * FROM AlipayAPI_Response ars WHERE ars.pg_result_code = 'SUCCESS' AND MONTH(ars.pg_transaction_date) = MONTH(CURRENT_DATE()) ORDER BY ars.id DESC";
       	}
       //	select * from bookings where MONTH(CURDATE())=MONTH(booking_date);
       	else
       	{
       		if(alipayWalletVO.getSortType().equals("Last Month"))
       		{
       		sql = "SELECT * FROM AlipayAPI_Response ars where ars.pg_result_code = 'SUCCESS' AND ars.pg_transaction_date BETWEEN DATE_FORMAT(NOW() - INTERVAL 1 MONTH, '%Y-%m-01 00:00:00') AND DATE_FORMAT(LAST_DAY(NOW() - INTERVAL 1 MONTH), '%Y-%m-%d 23:59:59') ORDER BY ars.id DESC";
       		//select * from alipayapi_response where pg_transaction_date BETWEEN DATE_FORMAT(NOW() - INTERVAL 1 MONTH, '%Y-%m-01 00:00:00') AND DATE_FORMAT(LAST_DAY(NOW() - INTERVAL 1 MONTH), '%Y-%m-%d 23:59:59');

       		}
       		else
       		{
       			//select * from dt_table where  `date` >= DATE_SUB(CURDATE(), INTERVAL 10 DAY)
       			sql = "SELECT * FROM AlipayAPI_Response ars WHERE ars.pg_result_code = 'SUCCESS' AND ars.pg_transaction_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) ORDER BY ars.id DESC";
       		}
       	}
           try {
               
               result = namedParameterJdbcTemplate.query(sql, new TransactionMapper());
      	            if(result.size() == 0) {
                   return null;
               } else{
               	
               		return result;
               	              
               }
           } catch (EmptyResultDataAccessException e) {
               // do nothing, return null
           }
       }
       
       return null;
   }

    @Override
    public List<AlipayAPIRequest> getTransactionListOnMap(AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
    	 List<AlipayAPIRequest> result = null;
        
        	String sql ="";
        		 sql = "Select * from AlipayAPI_Request";
        	
        //	select * from bookings where MONTH(CURDATE())=MONTH(booking_date);
        	
            try {
                
                result = namedParameterJdbcTemplate.query(sql, new RequestMapper());
       	            if(result.size() == 0) {
                    return null;
                } else{
                	
                		return result;
                	              
                }
            } catch (EmptyResultDataAccessException e) {
                // do nothing, return null
            }
        
        
        
  
        return null;
      
    }
    
    @Override
    public AlipayAPIResponse getTransactionDetails(String mcPartnerTransId) {
    	 List<AlipayAPIResponse> result = null;
    	 Map<String, Object> params = new HashMap<String, Object>();
    		params.put("mcPartnerTransId", mcPartnerTransId);
        	String sql ="";
        		 sql = "Select * from AlipayAPI_Response ars , AlipayAPI_Request arq where arq.mc_partner_trans_id = ars.pg_partner_trans_id AND ars.pg_result_code = 'SUCCESS' and ars.pg_partner_trans_id=:mcPartnerTransId";
//        		 StringBuffer whereClause = new StringBuffer("");
//        	        whereClause.append(" AND ars.pg_result_code = 'SUCCESS'");
        //	select * from bookings where MONTH(CURDATE())=MONTH(booking_date);
        	
            try {
                
                result = namedParameterJdbcTemplate.query(sql, params, new TransactionMapper());
       	            if(result.size() == 0) {
       	            	
                    return null;
                } else{
                	for (AlipayAPIResponse alipayAPIResponse : result) {
                		return alipayAPIResponse;
					}
                		
                	              
                }
            } catch (EmptyResultDataAccessException e) {
                // do nothing, return null
            }
        
        
        
  
        return null;
      
    }
    public  List<AlipayAPIResponse>  getTransactionsOfID(String mcPartnerTransId) {
   	 List<AlipayAPIResponse> result = null;
   	 Map<String, Object> params = new HashMap<String, Object>();
   		params.put("mcPartnerTransId", mcPartnerTransId);
       	String sql ="";
       		 sql = "Select * from AlipayAPI_Response where pg_partner_trans_id=:mcPartnerTransId";
       	
       //	select * from bookings where MONTH(CURDATE())=MONTH(booking_date);
       	
           try {
               
               result = namedParameterJdbcTemplate.query(sql, params, new TransactionMapperNew());
      	            if(result.size() == 0) {
      	            	
                   return null;
               } else{
               	//for (AlipayAPIResponse alipayAPIResponse : result) {
               		return result;
					//}
               		
               	              
               }
           } catch (EmptyResultDataAccessException e) {
               // do nothing, return null
           }
       
       
       
 
       return null;
     
   }
    @Override
	public List<User> getUserList(String infidigiAccountId) {
    	 Map<String, Object> params = new HashMap<String, Object>();
 		params.put("merchantID", infidigiAccountId);
     	String sql ="";
     		 sql = "Select * from users_payitnz where infidigiAccountId=:merchantID";
     	
     //	select * from bookings where MONTH(CURDATE())=MONTH(booking_date);
     	
         try {
             
            List<User> result = namedParameterJdbcTemplate.query(sql, params, new UserMapper());
    	            if(result.size() == 0) {
    	            	
                 return null;
             } else{
             	return result;
             }
         } catch (EmptyResultDataAccessException e) {
             // do nothing, return null
         }

     return null;
   
	}

//    @Override
//   	public User getUser(String infidigiUserID) {
//       	 Map<String, Object> params = new HashMap<String, Object>();
//    		params.put("userID", infidigiUserID);
//        	String sql ="";
//        		 sql = "Select * from users_payitnz where infidigiUserId=:userID";
//        	
//        //	select * from bookings where MONTH(CURDATE())=MONTH(booking_date);
//        	
//            try {
//                
//               List<User> result = namedParameterJdbcTemplate.query(sql, params, new UserMapper());
//       	            if(result.size() == 0) {
//       	            	
//                    return null;
//                } else{
//                	for (User user : result) {
//                		return user;
//					}
//                	
//                }
//            } catch (EmptyResultDataAccessException e) {
//                // do nothing, return null
//            }
//
//        return null;
//      
//   	}

    private static final class TransactionMapper implements RowMapper<AlipayAPIResponse> {

        public AlipayAPIResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AlipayAPIResponse transaction = new AlipayAPIResponse();
        	System.out.println("Transacion id ===="+rs.getString("pg_partner_trans_id"));
        	transaction.setId(rs.getInt("id"));
        //	System.out.println("id=="+rs.getInt("id"));
        	transaction.setPgResultCode(rs.getString("pg_result_code"));
        	
        	if(rs.getString("pg_transaction_date") != null)
        	{
        	String s[]=rs.getString("pg_transaction_date").toString().split("\\.");
        	transaction.setPgAlipayPayTime(s[0]);
        	}
        	else
        	{
        		transaction.setPgAlipayPayTime(rs.getString("pg_transaction_date"));	
        	}
        	
        	
        	
//        	transaction.setPgAlipayPayTime(rs.getString("pg_transaction_date"));
        //	transaction.setPgAlipayPayTime(s[0]);
        	transaction.setPgPartnerTransId(rs.getString("pg_partner_trans_id"));
        	transaction.setPgTransAmountCny(rs.getDouble("pg_trans_amount_cny"));
        	transaction.setPgAlipayTransId(rs.getString("pg_alipay_trans_id"));
        	transaction.setInfidigiUserId(rs.getString("infidigiUserId"));
        	transaction.setMcTransAmount(rs.getString("mc_trans_amount"));
        	transaction.setMethod_type(rs.getString("method_type"));
//        	transaction.setMcLatitude(rs.getString("mc_latitude"));
//        	transaction.setMcLongitude(rs.getString("mc_longitude"));
        	//transaction.setPgTransactionDate(rs.getTimestamp("pg_transaction_date"));
        return transaction;
        }
        
    }
    private static final class TransactionMapperNew implements RowMapper<AlipayAPIResponse> {

        public AlipayAPIResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AlipayAPIResponse transaction = new AlipayAPIResponse();
        	transaction.setId(rs.getInt("id"));
        //	System.out.println("id=="+rs.getInt("id"));
        	transaction.setPgResultCode(rs.getString("pg_result_code"));
        	
        	if(rs.getString("pg_transaction_date") != null)
        	{
        	String s[]=rs.getString("pg_transaction_date").toString().split("\\.");
        	transaction.setPgAlipayPayTime(s[0]);
        	}
        	else
        	{
        		transaction.setPgAlipayPayTime(rs.getString("pg_transaction_date"));	
        	}
        	
        	
        	
//        	transaction.setPgAlipayPayTime(rs.getString("pg_transaction_date"));
        //	transaction.setPgAlipayPayTime(s[0]);
        	transaction.setPgPartnerTransId(rs.getString("pg_partner_trans_id"));
        	transaction.setPgTransAmountCny(rs.getDouble("pg_trans_amount_cny"));
        	transaction.setPgAlipayTransId(rs.getString("pg_alipay_trans_id"));
        	transaction.setInfidigiUserId(rs.getString("infidigiUserId"));
        	transaction.setMcTransAmount(rs.getString("mc_trans_amount"));
        	transaction.setMethod_type(rs.getString("method_type"));
//        	transaction.setMcLatitude(rs.getString("mc_latitude"));
//        	transaction.setMcLongitude(rs.getString("mc_longitude"));
        	transaction.setTransactionDate(rs.getString("pg_transaction_date"));
        	transaction.setMcCurrency(rs.getString("mc_currency"));
        	//
        	transaction.setMerchantRefundId(rs.getString("pg_partner_refund_id"));
        	transaction.setRequestTime(rs.getString("request_time"));
        	transaction.setPgIsSuccess(rs.getString("pg_is_success"));
        	transaction.setChannel(rs.getString("method_type"));
        return transaction;
        }
        
    }

    
    private static final class RequestMapper implements RowMapper<AlipayAPIRequest> {

        public AlipayAPIRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AlipayAPIRequest transaction = new AlipayAPIRequest();
        	transaction.setId(rs.getInt("id"));
        	transaction.setMcPartnerTransId(rs.getString("mc_partner_trans_id"));
        	transaction.setMcLatitude(rs.getString("mc_latitude"));
        	transaction.setMcLongitude(rs.getString("mc_longitude"));
//        	transaction.setPgAlipayPayTime(AlipayAPIRequest);
//        	transaction.setPgPartnerTransId(rs.getString("pg_partner_trans_id"));
//        	transaction.setPgTransAmountCny(rs.getDouble("pg_trans_amount_cny"));
        return transaction;
        }
        
    }
    private static final class UserMapper implements RowMapper<User> {

        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        	User user = new User();
        	user.setId(rs.getInt("id"));
        	user.setInfidigiAccountId(rs.getString("infidigiAccountId"));
        	user.setInfidigiUserId(rs.getString("infidigiUserId"));
        	user.setInfidigiPassword(rs.getString("infidigiPassword"));
        	user.setFirstName(rs.getString("firstName"));
        	user.setLastName(rs.getString("lastName"));
        return user;
        }
        
    }
   
}
