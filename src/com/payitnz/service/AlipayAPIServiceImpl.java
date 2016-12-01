package com.payitnz.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.dsig.keyinfo.PGPData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.dynamicpayment.paymentexpress.DPSRequestBean;
import com.dynamicpayment.paymentexpress.PxPay;
import com.payitnz.config.DynamicPaymentConstant;
import com.payitnz.dao.AlipayAPIDao;
import com.payitnz.model.AlipayAPIRequest;
import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.GenericAPIResponse;
import com.payitnz.model.User;
import com.payitnz.util.Payment;
import com.payitnz.util.XmlParser;
import com.payitnz.web.LoginController;

@Service("alipayAPIRequestService")
public class AlipayAPIServiceImpl implements AlipayAPIService {

    AlipayAPIDao alipayAPIDao;
    final static Logger logger = Logger.getLogger(LoginController.class);
    @Autowired
    public void setAlipayAPIDao(AlipayAPIDao alipayAPIDao) {
        this.alipayAPIDao = alipayAPIDao;
    }

    @Override
    public void saveOrUpdate(AlipayAPIRequest alipayAPIRequest) {
        alipayAPIDao.save(alipayAPIRequest);

    }

    @Override
    public void saveOrUpdate(AlipayAPIResponse alipayAPIResponse) {
    	if(alipayAPIResponse.getId() == 0)
    	{
    		alipayAPIDao.save(alipayAPIResponse);
    	}
    	else
    	{
    		 alipayAPIDao.update(alipayAPIResponse);
    	}

    }

    @Override
    public void saveOrUpdate(DPSRequestBean dpsRequest) {
    	
    		alipayAPIDao.save(dpsRequest);
    	
    }
    
    @Override
	public void UpdateUser(User user) {
		alipayAPIDao.UpdateUser(user);
		
	}
    @Override
    public  List<AlipayAPIResponse> getTransactionList(AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        return alipayAPIDao.getTransactionList(alipayWalletVO, ipAddress, sender);
    }
    @Override
    public  List<AlipayAPIRequest> getTransactionListOnMap(AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        return alipayAPIDao.getTransactionListOnMap(alipayWalletVO, ipAddress, sender);
    }
    @Override
    public AlipayAPIResponse getTransactionDetails(String mcPartnerTransId) {
        return alipayAPIDao.getTransactionDetails(mcPartnerTransId);
    }
    
    @Override
    public List<AlipayAPIResponse> getTransactionsOfID(String mcPartnerTransId) {
        return alipayAPIDao.getTransactionsOfID(mcPartnerTransId);
    }
    
  
    @Override
    public boolean callAlipayPaymentAPI(Object finalURL, AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        StringBuffer responseAlipay = new StringBuffer();
        AlipayAPIResponse alipayAPIResponse = null;
        try {

            URL obj = new URL(finalURL.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
            urlConnection.setRequestMethod("GET");

            // Send post request
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "TEXT/HTML");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.flush();
            wr.close();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + finalURL);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            int flag = 0;
           
            while ((inputLine = in.readLine()) != null) {
            	if(inputLine.contains("<error>")&&inputLine.contains("</error>"))
            	{
            		if(inputLine.contains("ILLEGAL_SIGN"))
            		{
            			System.out.println("---------------->Error Error"+inputLine);
            			flag = 1;
            			
            		}
            		else
            		{
            			System.out.println("---------------->something else"+inputLine);
            		}
            	}
                responseAlipay.append(inputLine);
            }
            in.close();
            
            // print result
            System.out.println("Response from Alipay=" + responseAlipay.toString());
           
            alipayAPIResponse = XmlParser.parseAlipayPaymentAPIResponse(responseAlipay.toString());
            alipayAPIResponse.setRequestBy(sender);
            alipayAPIResponse.setIpAddress(ipAddress);
            alipayAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
            logger.info("Payment completed for : " + alipayAPIResponse.getPgPartnerTransId());
            if(flag == 1)
            {
            	alipayAPIResponse.setPgResultCode("UNKNOW");
            }
            Calendar cal=Calendar.getInstance();
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            
            //int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            //int minute = Calendar.getInstance().get(Calendar.MINUTE);
            //int second = Calendar.getInstance().get(Calendar.SECOND);
            String curDate = "" + day + "/" + month + "/" + year;
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:MM:ss");
//            Date inspectionDate2 = null;
//            try {
//                inspectionDate2 = formatter.parse(curDate);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
           System.out.println("status==="+alipayAPIResponse.getStatusCode());
            alipayAPIResponse.setAmount(alipayWalletVO.getAmount());
            alipayAPIResponse.setPgTransactionDate(cal.getTime());
            alipayAPIResponse.setRequestTime(new Date().toString());
            System.out.println("transaction id in 1st api"+alipayAPIResponse.getPgPartnerTransId()+cal.getTime());
            alipayWalletVO.setPgPartnerTransId(alipayAPIResponse.getPgPartnerTransId());
            alipayAPIResponse.setMethod_type("Alipay Offline");
            alipayAPIResponse.setTransaction_type("1");
            alipayAPIResponse.setDyMerchantId(alipayWalletVO.getInfidigiAccountId());
            alipayAPIResponse.setRemark("");
            saveOrUpdate(alipayAPIResponse);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(alipayAPIResponse.getPgResultCode().equalsIgnoreCase(DynamicPaymentConstant.PG_UNKNOW_STATUS)) {
            return false; //call query api to know the actual status of payment transaction
        } else {
            return true;
        }
        
    }
    
    /*@Override
    public AlipayAPIResponse callAlipayPaymentAPI(Object finalURL, AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        StringBuffer responseAlipay = new StringBuffer();
        AlipayAPIResponse alipayAPIResponse = null;
        try {

            URL obj = new URL(finalURL.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
            urlConnection.setRequestMethod("GET");

            // Send post request
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "TEXT/HTML");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.flush();
            wr.close();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + finalURL);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                responseAlipay.append(inputLine);
            }
            in.close();

            // print result
            System.out.println("Response from Alipay=" + responseAlipay.toString());

            alipayAPIResponse = XmlParser.parseAlipayPaymentAPIResponse(responseAlipay.toString());
            alipayAPIResponse.setRequestBy(sender);
            alipayAPIResponse.setIpAddress(ipAddress);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            //int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            //int minute = Calendar.getInstance().get(Calendar.MINUTE);
            //int second = Calendar.getInstance().get(Calendar.SECOND);
            String curDate = "" + day + "/" + month + "/" + year;
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date inspectionDate2 = null;
            try {
                inspectionDate2 = formatter.parse(curDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            alipayAPIResponse.setPgTransactionDate(inspectionDate2);
            alipayAPIResponse.setRequestTime(new Date().toString());
            
            saveOrUpdate(alipayAPIResponse);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alipayAPIResponse;
    }*/


    @Override
    public boolean authenticateUser(AlipayWalletVO alipayWalletVO, String ipAddress, String sender, GenericAPIResponse genericAPIResponse) {
    	 String password;
		try {
			password = DynamicPaymentConstant.getHashPassword(alipayWalletVO.getInfidigiPassword());
			alipayWalletVO.setInfidigiPassword(password);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        User user = alipayAPIDao.getUser(alipayWalletVO.getInfidigiAccountId(), alipayWalletVO.getInfidigiUserId(), alipayWalletVO.getInfidigiPassword());
        if(user != null)
        {
        if (user.getId() != 0) {
        	genericAPIResponse.setUserID(user.getId());
        	
            return true;
        }
        }
        return false;

    }

    @Override
    public Object[] createPaymentTransaction(AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        Object returnParams[] = new Object[2];

        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        try {
            String sign_type = "MD5";
            String input_charset = alipayWalletVO.getCharSet();
            String alipayAPIURL = DynamicPaymentConstant.PG_ALIPAY_WALLET_URL;
            String merchantPrivateKey = alipayWalletVO.getPartner_key();//"9lsktl2d2tqmunkebgilrkkwk37kcww2";// OFFLINE to ONLINE mode
            						
            // String merchantPrivateKey = "yq9ud70vvpmnsxk7u75g27jfwg02nenh";//OFFLINE mode

            Map<String, String> params = new HashMap<String, String>();
            params.put("_input_charset", alipayWalletVO.getCharSet());//"UTF-8"
            // params.put("alipay_seller_id", alipayWalletVO.getMerchantId() );
            params.put("alipay_seller_id", alipayWalletVO.getPayitnz_id());//"2088021966645500"// OFFLINE to ONLINE mode
            // params.put("alipay_seller_id", "2088021966388155");//OFFLINE mode

            params.put("biz_product", "OVERSEAS_MBARCODE_PAY");
            params.put("buyer_identity_code", alipayWalletVO.getBuyerIdentityCode());
            params.put("currency", alipayWalletVO.getCurrency());
            params.put("identity_code_type", alipayWalletVO.getBuyerIdentityType());
            // params.put("partner", alipayWalletVO.getMerchantId());
            params.put("partner", alipayWalletVO.getPayitnz_id()/*"2088021966645500"*/);// OFFLINE to ONLINE mode
            // params.put("partner", "2088021966388155");//OFFLINE mode

            Random rand = new Random();
            long partnerTranId = Math.abs(rand.nextLong());
            alipayWalletVO.setMerchantTransactionId("" + partnerTranId);
            params.put("partner_trans_id", "" + partnerTranId);
            params.put("sendFormat", "normal");
            params.put("service", alipayWalletVO.getService());
            params.put("trans_amount", alipayWalletVO.getAmount());
            params.put("trans_name", alipayWalletVO.getParticular());
            
            String finalURL = Payment.CreateUrl(params, merchantPrivateKey, alipayAPIURL, input_charset, sign_type);
            logger.info("Sent Payment Request for : " + partnerTranId);
            returnParams[0] = finalURL;
            // save request parameters into database
            AlipayAPIRequest alipayAPIRequest = new AlipayAPIRequest();
            alipayAPIRequest.setPgService(params.get("service"));
            alipayAPIRequest.setPgSign(" ");
            alipayAPIRequest.setPgSignType(" ");
            alipayAPIRequest.setPgPartnerId(params.get("partner"));
            alipayAPIRequest.setPgInputCharset(params.get("_input_charset"));
            alipayAPIRequest.setPgAlipaySellerId(params.get("alipay_seller_id"));
            alipayAPIRequest.setMcQuantityCommodity(0);
            alipayAPIRequest.setMcTransName(params.get("trans_name"));
            alipayAPIRequest.setMcReference(alipayWalletVO.getReference());
            alipayAPIRequest.setMcComment(alipayWalletVO.getComment());
            alipayAPIRequest.setMcLatitude(alipayWalletVO.getLatitude());
            alipayAPIRequest.setMcLongitude(alipayWalletVO.getLongitude());
            alipayAPIRequest.setMcEmail(alipayWalletVO.getEmail());
            alipayAPIRequest.setMcMobile(alipayWalletVO.getMobile());
            alipayAPIRequest.setMcPartnerTransId(params.get("partner_trans_id"));
            alipayAPIRequest.setMcCurrency(params.get("currency"));
            alipayAPIRequest.setMcTransAmount(Double.parseDouble(params.get("trans_amount")));
            alipayAPIRequest.setPgBuyerIdentityCode(params.get("buyer_identity_code"));
            alipayAPIRequest.setPgIdentityCodeType(params.get("identity_code_type"));
            alipayAPIRequest.setPgSendFormat(params.get("sendFormat"));
            alipayAPIRequest.setMcTransCreateTime(" ");
            alipayAPIRequest.setMcMemo(" ");
            alipayAPIRequest.setPgBizProduct(params.get("biz_product"));
            alipayAPIRequest.setPgExtendInfo(" ");
            alipayAPIRequest.setIpAddress(ipAddress);
            alipayAPIRequest.setRequestBy(sender);
            
            /*SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat("MM/dd/yyyy");
            Date inspDate;

            inspDate = DATE_FORMAT1.parse(equipmentDetails.getInspDate());

            SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd");
            String inspectionDate = DATE_FORMAT2.format(inspDate);
            equipmentDetails.setInspDate(inspectionDate);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);
            int second = Calendar.getInstance().get(Calendar.SECOND);
            String requestTime = "" + year + month + day + hour + minute + second;
            
            System.out.println("---requestTime=" + requestTime);*/
           
            alipayAPIRequest.setRequestTime(new Date().toString());
            saveOrUpdate(alipayAPIRequest);

            genericAPIResponse.setId(alipayAPIRequest.getId());
            genericAPIResponse.setInfidigiAccountId(alipayWalletVO.getInfidigiAccountId());
            genericAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
            genericAPIResponse.setMerchantTransactionId(alipayAPIRequest.getMcPartnerTransId());
            genericAPIResponse.setMessage("Payment request received");
            genericAPIResponse.setMobileCallId(alipayWalletVO.getMobileCallId());
            
            genericAPIResponse.setStatusCode("201");
            returnParams[1] = genericAPIResponse;
            System.out.println("---alipayAPIRequest id=" + alipayAPIRequest.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnParams;

    }

    @Override
    public Object[] createReverseTransaction(AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        Object returnParams[] = new Object[2];

        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        try {
            String sign_type = "MD5";
            String input_charset = alipayWalletVO.getCharSet();//"UTF-8";
            String alipayAPIURL = DynamicPaymentConstant.PG_ALIPAY_WALLET_URL;
            String merchantPrivateKey = alipayWalletVO.getPartner_key();//"9lsktl2d2tqmunkebgilrkkwk37kcww2";// OFFLINE to ONLINE mode
            // String merchantPrivateKey = "yq9ud70vvpmnsxk7u75g27jfwg02nenh";//OFFLINE mode
            logger.info("Sent Request for Reverse the transaction : " + alipayWalletVO.getMerchantTransactionId());
            Map<String, String> params = new HashMap<String, String>();
            params.put("_input_charset", input_charset);
            params.put("service", alipayWalletVO.getService());
            params.put("partner_trans_id", "" + alipayWalletVO.getMerchantTransactionId());
            params.put("partner", alipayWalletVO.getPayitnz_id());// OFFLINE to ONLINE mode
            // params.put("partner", "2088021966388155");//OFFLINE mode
            String finalURL = Payment.CreateUrl(params, merchantPrivateKey, alipayAPIURL, input_charset, sign_type);
            returnParams[0] = finalURL;
            // save request parameters into database
            AlipayAPIRequest alipayAPIRequest = new AlipayAPIRequest();
            alipayAPIRequest.setPgService(params.get("service"));
            alipayAPIRequest.setPgSign(" ");
            alipayAPIRequest.setPgSignType(" ");
            alipayAPIRequest.setPgPartnerId(params.get("partner"));
            alipayAPIRequest.setPgInputCharset(params.get("_input_charset"));
            alipayAPIRequest.setMcQuantityCommodity(0);
            alipayAPIRequest.setMcPartnerTransId(params.get("partner_trans_id"));
            alipayAPIRequest.setMcTransCreateTime(" ");
            alipayAPIRequest.setMcMemo(" ");
            alipayAPIRequest.setPgExtendInfo(" ");
            alipayAPIRequest.setIpAddress(ipAddress);
            alipayAPIRequest.setRequestBy(sender);
            alipayAPIRequest.setRequestTime(new Date().toString());
            saveOrUpdate(alipayAPIRequest);

            genericAPIResponse.setId(alipayAPIRequest.getId());
            genericAPIResponse.setInfidigiAccountId(alipayWalletVO.getInfidigiAccountId());
            genericAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
            genericAPIResponse.setMerchantTransactionId(alipayAPIRequest.getMcPartnerTransId());
            genericAPIResponse.setMessage("Payment reversal request received");
            genericAPIResponse.setMobileCallId(alipayWalletVO.getMobileCallId());
            genericAPIResponse.setStatusCode("201");
            returnParams[1] = genericAPIResponse;
            System.out.println("---alipayAPIRequest id=" + alipayAPIRequest.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnParams;

    }

    @Override
    public Object[] createRefundTransaction(AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        Object returnParams[] = new Object[2];

        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        try {
            String sign_type = "MD5";
            String input_charset = alipayWalletVO.getCharSet();
            String alipayAPIURL = DynamicPaymentConstant.PG_ALIPAY_WALLET_URL;
            alipayWalletVO.setService("alipay.acquire.overseas.spot.refund");
            String merchantPrivateKey = alipayWalletVO.getPartner_key();//"9lsktl2d2tqmunkebgilrkkwk37kcww2";// OFFLINE to ONLINE mode
            // String merchantPrivateKey = "yq9ud70vvpmnsxk7u75g27jfwg02nenh";//OFFLINE mode

            Map<String, String> params = new HashMap<String, String>();
            params.put("_input_charset", input_charset);
            params.put("service", alipayWalletVO.getService());
            params.put("partner_trans_id", "" + alipayWalletVO.getPgPartnerTransId());
            params.put("partner", alipayWalletVO.getPayitnz_id());// OFFLINE to ONLINE mode
            // params.put("partner", "2088021966388155");//OFFLINE mode
            // TODO: test it later for asynch mode
            // params.put("notify_url", "");
            Random rand = new Random();
            long partnerRefundId = Math.abs(rand.nextLong());
            params.put("partner_refund_id", "" + partnerRefundId);
            params.put("refund_amount", alipayWalletVO.getAmount());
            params.put("currency", alipayWalletVO.getCurrency());
            params.put("refund_reson", alipayWalletVO.getRefundReason());
           // logger.info("Sent Refund request for : " + alipayWalletVO.getPgPartnerTransId());
            String finalURL = Payment.CreateUrl(params, merchantPrivateKey, alipayAPIURL, input_charset, sign_type);
            returnParams[0] = finalURL;
            // save request parameters into database
            AlipayAPIRequest alipayAPIRequest = new AlipayAPIRequest();
            alipayAPIRequest.setPgService(params.get("service"));
            alipayAPIRequest.setPgSign(" ");
            alipayAPIRequest.setPgSignType(" ");
            alipayAPIRequest.setPgPartnerId(params.get("partner"));
            alipayAPIRequest.setPgInputCharset(params.get("_input_charset"));
            alipayAPIRequest.setMcQuantityCommodity(0);
            alipayAPIRequest.setMcPartnerTransId(params.get("partner_trans_id"));
            alipayAPIRequest.setMcPartnerRefundId("" + partnerRefundId);
            alipayAPIRequest.setMcRefundReason(params.get("refund_reson"));
            alipayAPIRequest.setMcTransCreateTime(" ");
            alipayAPIRequest.setMcTransAmount(Double.parseDouble(params.get("refund_amount")));
            alipayAPIRequest.setMcMemo(" ");
            alipayAPIRequest.setPgExtendInfo(" ");
            alipayAPIRequest.setIpAddress(ipAddress);
            alipayAPIRequest.setRequestBy(sender);
            alipayAPIRequest.setRequestTime(new Date().toString());
            saveOrUpdate(alipayAPIRequest);

            genericAPIResponse.setId(alipayAPIRequest.getId());
            genericAPIResponse.setInfidigiAccountId(alipayWalletVO.getInfidigiAccountId());
            genericAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
            genericAPIResponse.setMerchantTransactionId(alipayAPIRequest.getMcPartnerTransId());
            genericAPIResponse.setMerchantRefundId(alipayAPIRequest.getMcPartnerRefundId());
            genericAPIResponse.setMessage("Payment refund request received");
            genericAPIResponse.setMobileCallId(alipayWalletVO.getMobileCallId());
            genericAPIResponse.setStatusCode("201");
            returnParams[1] = genericAPIResponse;
            System.out.println("---alipayAPIRequest id=" + alipayAPIRequest.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnParams;

    }
    @Override
	public String processAlipay(AlipayWalletVO shoppingCart, Model model, AlipayWalletVO alipay, String ipAddress, String sender, HttpServletRequest request) {
		Date Now_Date=new Date();
		/*
		String paygateway	=	"https://openapi.alipaydev.com/gateway.do?";	//working and tested
	
	
		String service      = "create_forex_trade";//fix for payment transaction
		String sign_type       =   "MD5";
		String out_trade_no		= Now_Date.toString();	//external deal No
		String input_charset   =  "UTF-8";       
		String partner			=	"2088101122136241"; //partners’ User ID at alipay for testing 2088101122136241
		String key             =    "760bdzec6y9goq7ctyx96ezkz78287de"; //partners’ User key at alipay
		String body			= "Washing machine"; //trade description
		String total_fee			= "0.01";	//transaction amount - range is 0.01～1000000.00
		String currency     =  "USD";//Please refer to the short name in
		String subject			= "Alipay";			 //
		String notify_url		= "http://www.ivaultit.com/AliPayWeb/alipay_notify.jsp";	//Notification acceptance URL after the successful payment of the deal
		String return_url		= "http://www.ivaultit.com/AliPayWeb/alipay_return.jsp";	
		*/
		Random rand = new Random();
		 long partnerTranId = Math.abs(rand.nextLong());
		 
//         alipayWalletVO.setMerchantTransactionId("" + partnerTranId);
//         params.put("partner_trans_id", "" + partnerTranId);
		String paygateway	= alipay.getUrl();	//working and tested		
		String service      = alipay.getOnline_service();//fix for payment transaction
		String sign_type       =  alipay.getSign_type();
		String out_trade_no		= Now_Date.toString();	//external deal No
		String input_charset   = alipay.getOnline_character_set();  // alipay.getCharacter_set();       
		String partner			=alipay.getAlipay_online_partner_id(); //partners’ User ID at alipay for testing 2088101122136241
		String key             =   alipay.getAlipay_online_partner_key(); //partners’ User key at alipay
		String body			= shoppingCart.getParticular(); //trade description
		String total_fee			= shoppingCart.getAmount();	//transaction amount - range is 0.01～1000000.00
		String currency     =  alipay.getOnline_currency();//Please refer to the short name in
		String subject			= alipay.getSubject();			 //
		String notify_url		= "http://www.ivaultit.com/AliPayWeb/alipay_notify.jsp";	//Notification acceptance URL after the successful payment of the deal
		String return_url		= DynamicPaymentConstant.SERVER_HOST + DynamicPaymentConstant.SERVER_SITE_URL+"alipayResponse";	
	    AlipayAPIRequest alipayAPIRequest = new AlipayAPIRequest();
        alipayAPIRequest.setPgService(alipay.getOnline_service());
        alipayAPIRequest.setPgSign(" ");
        alipayAPIRequest.setPgSignType(" ");
        alipayAPIRequest.setPgPartnerId(alipay.getAlipay_online_partner_id());
        alipayAPIRequest.setPgInputCharset(alipay.getOnline_character_set());
        alipayAPIRequest.setPgAlipaySellerId(alipay.getAlipay_online_partner_id());
        alipayAPIRequest.setMcQuantityCommodity(0);
        alipayAPIRequest.setMcTransName(body);
        alipayAPIRequest.setMcReference(" ");
        alipayAPIRequest.setMcComment(" ");
        alipayAPIRequest.setMcLatitude(" ");
        alipayAPIRequest.setMcLongitude(" ");
        alipayAPIRequest.setMcEmail("");
        alipayAPIRequest.setMcMobile("");
        alipayAPIRequest.setMcPartnerTransId(""+partnerTranId);
        alipayAPIRequest.setMcCurrency(currency);
        alipayAPIRequest.setMcTransAmount(Double.parseDouble(total_fee));
        alipayAPIRequest.setPgBuyerIdentityCode("");
        alipayAPIRequest.setPgIdentityCodeType("");
        alipayAPIRequest.setPgSendFormat(service);
        alipayAPIRequest.setMcTransCreateTime(" ");
        alipayAPIRequest.setMcMemo(" ");
        alipayAPIRequest.setPgBizProduct("");
        alipayAPIRequest.setPgExtendInfo(" ");
        alipayAPIRequest.setIpAddress(ipAddress);
        alipayAPIRequest.setRequestBy(sender);
        
        saveOrUpdate(alipayAPIRequest);
        request.getSession().setAttribute("partner-trans-id",partnerTranId);
		String ItemUrl=Payment.CreateOnlineUrl(partnerTranId,paygateway,service,sign_type,out_trade_no,input_charset,partner,key,body,total_fee,currency,subject,notify_url,return_url);
		
		return ItemUrl;

	}
    @Override
    public Model processF2C(AlipayWalletVO VO, Model model, AlipayWalletVO f2c, String ipAddress, String sender, HttpServletRequest request) {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		Random rand = new Random();
		 long partnerTranId = Math.abs(rand.nextLong());
		jsonBuilder.add("cmd", f2c.getF2c_service());
		jsonBuilder.add("account_id", f2c.getF2c_account_id());
		jsonBuilder.add("custom_data", f2c.getF2c_custom_data());
		jsonBuilder.add("return_url",DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SERVER_SITE_URL+"resonseF2C");
		jsonBuilder.add("notification_url", f2c.getF2c_notification_url());
		jsonBuilder.add("header_image", f2c.getHeader_image());
		jsonBuilder.add("header_bottom_border", f2c.getHeader_bottom_border_color());
		jsonBuilder.add("header_background_colour", f2c.getHeader_background_color());
		
		model.addAttribute("cmd", f2c.getF2c_service());
		model.addAttribute("account_id", f2c.getF2c_account_id());
		//	        model.addAttribute("payment_method", f2c.getF2c_payment_method());
		model.addAttribute("custom_data", f2c.getF2c_custom_data());
		model.addAttribute("return_url",DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SERVER_SITE_URL+"resonseF2C");

		model.addAttribute("notification_url", f2c.getF2c_notification_url());	     
		model.addAttribute("header_image", f2c.getHeader_image());
		model.addAttribute("header_bottom_border", f2c.getHeader_bottom_border_color());
		model.addAttribute("header_background_colour", f2c.getHeader_background_color());
		
		if(f2c.getStore_card() == "1")
		{
			model.addAttribute("store_card", "1");
			jsonBuilder.add("store_card", "1");

		}
		else
		{
			model.addAttribute("store_card", "0");
			jsonBuilder.add("store_card", "0");

		}
		if(f2c.getDisplay_customer_email() == "1")
		{
			model.addAttribute("display_customer_email", "1");
			jsonBuilder.add("display_customer_email", "1");
		}
		else
		{
			model.addAttribute("display_customer_email", "0");
			jsonBuilder.add("display_customer_email", "0");
		}
		jsonBuilder.add("amount", VO.getAmount());
		jsonBuilder.add("reference", partnerTranId);
		jsonBuilder.add("particular", "${particular}");
		model.addAttribute("amount", VO.getAmount());
		model.addAttribute("reference", partnerTranId);
		model.addAttribute("particular", "${particular}");
		JsonObject jsonObject = jsonBuilder.build();
		//transactionBean.setRequest(jsonObject.toString());
//		 AlipayAPIRequest alipayAPIRequest = new AlipayAPIRequest();
//	        alipayAPIRequest.setPgService(f2c.getF2c_service());
//	        alipayAPIRequest.setPgSign(" ");
//	        alipayAPIRequest.setPgSignType(" ");
//	        alipayAPIRequest.setPgPartnerId(f2c.getAlipay_online_partner_id());
//	        alipayAPIRequest.setPgInputCharset("");
//	        alipayAPIRequest.setPgAlipaySellerId("");
//	        alipayAPIRequest.setMcQuantityCommodity(0);
//	        alipayAPIRequest.setMcTransName(VO.getParticular());
//	        alipayAPIRequest.setMcReference(" ");
//	        alipayAPIRequest.setMcComment(" ");
//	        alipayAPIRequest.setMcLatitude(" ");
//	        alipayAPIRequest.setMcLongitude(" ");
//	        alipayAPIRequest.setMcEmail("");
//	        alipayAPIRequest.setMcMobile("");
//	        alipayAPIRequest.setMcPartnerTransId(""+partnerTranId);
//	        alipayAPIRequest.setMcCurrency("");
//	        alipayAPIRequest.setMcTransAmount(Double.parseDouble(VO.getAmount()));
//	        alipayAPIRequest.setPgBuyerIdentityCode("");
//	        alipayAPIRequest.setPgIdentityCodeType("");
//	        alipayAPIRequest.setPgSendFormat(service);
//	        alipayAPIRequest.setMcTransCreateTime(" ");
//	        alipayAPIRequest.setMcMemo(" ");
//	        alipayAPIRequest.setPgBizProduct("");
//	        alipayAPIRequest.setPgExtendInfo(" ");
//	        alipayAPIRequest.setIpAddress(ipAddress);
//	        alipayAPIRequest.setRequestBy(sender);
//	        
//	        saveOrUpdate(alipayAPIRequest);
		return model;

	}
    
    @Override
	public String processDPS(AlipayWalletVO alipay, Model model, AlipayWalletVO dps, String ipAddress, String sender,
			HttpServletRequest request) {
		//	        String PxPayUrl = "https://uat.paymentexpress.com/pxaccess/pxpay.aspx";
		//	        String PxPayUserId = "AhuraConsulting_Dev";
		//	        String PxPayKey = "78efe7aad82db9354675fea0c9fa9484d5cdeaeee1be6ad90939ce74f8c14c98";

    	HttpSession session = request.getSession();    	
    	session.setAttribute("userId", alipay.getUser_id());
    	
		System.out.println(dps.getPxPayUrl());
		System.out.println(dps.getPxPayUserId());
		System.out.println(dps.getPxPayKey());
		String PxPayUrl = dps.getPxPayUrl();
		String PxPayUserId = dps.getPxPayUserId();
		String PxPayKey = dps.getPxPayKey();

		//	        Double amt = Double.valueOf(shoppingCart.getAmount());
		//	        int amount = amt.intValue();
		// number = temp.doubleValue();

		 Random rand = new Random();
		 long partnerTranId = Math.abs(rand.nextLong());
		 
		DPSRequestBean gr = new DPSRequestBean();

		gr.setCurrencyInput("USD");
		gr.setEmailAddress("");
		gr.setMerchantId(alipay.getUser_id());
		gr.setPxPayKey(PxPayKey);
		gr.setPxPayUserId(PxPayUserId);
		
		System.out.println("Amount:"+alipay.getAmount().toString());
		
		DecimalFormat df = new DecimalFormat("#.00"); 
		String amount = df.format(Double.valueOf(alipay.getAmount()));
		System.out.println("amount"+ amount);
		gr.setAmountInput(amount);
		gr.setIpAddress(ipAddress);
		gr.setBillingId("");
		gr.setEnableAddBillCard(0);
		gr.setMerchantReference(String.valueOf(partnerTranId));
		gr.setOpt("");
		gr.setTxnData1("");
		gr.setTxnData2("");
		gr.setTxnData3("");
		gr.setTxnId("");
		gr.setTxnType("Purchase");
		gr.setUrlFail(DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SERVER_SITE_URL+"dpsResponse");
		gr.setUrlSuccess(DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SERVER_SITE_URL+"dpsResponse");
		String redirectUrl = PxPay.GenerateRequest(PxPayUserId, PxPayKey, gr, PxPayUrl);
		String inputXml = gr.getXml();
		//transactionBean.setRequest(inputXml);
		System.out.println(inputXml);
		System.out.println("redirect url"+ redirectUrl);
		//int status = hc.storeTransaction(bean);

		alipayAPIDao.save(gr);
		
		AlipayAPIResponse alipayAPIResponse = new AlipayAPIResponse();
		
		
		 
		 alipayAPIResponse.setDyMerchantId(alipay.getUser_id());
         alipayAPIResponse.setPgIsSuccess("");
         alipayAPIResponse.setPgResultCode("");
         alipayAPIResponse.setPgError("");             
         alipayAPIResponse.setPgPartnerTransId(new StringBuffer().append(partnerTranId).toString());
         alipayAPIResponse.setMerchantRefundId("");
         alipayAPIResponse.setPgAlipayTransId("" );
         alipayAPIResponse.setPgAlipayReverseTime("");
         alipayAPIResponse.setPgAlipayCancelTime("");
         alipayAPIResponse.setMcCurrency("USD");
         alipayAPIResponse.setMcItemName("");
         alipayAPIResponse.setMcTransAmount(alipay.getAmount());
         alipayAPIResponse.setPgExchangeRate(0.00);
         alipayAPIResponse.setPgTransAmountCny(0.00);
         alipayAPIResponse.setAmount(amount);
         alipayAPIResponse.setIpAddress(ipAddress);
         alipayAPIResponse.setRemark(sender);
         
         DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");         
         //to convert Date to String, use format method of SimpleDateFormat class.
         String strDate = dateFormat.format(new Date());
         alipayAPIResponse.setRequestTime(strDate);
         alipayAPIResponse.setMcReference(alipay.getReference());
         alipayAPIResponse.setMcComment("" );
         alipayAPIResponse.setMcLatitude("");
         alipayAPIResponse.setMcLongitude("" );
         alipayAPIResponse.setMcEmail(alipay.getEmail());
         alipayAPIResponse.setMcMobile("");
         alipayAPIResponse.setInfidigiUserId(alipay.getInfidigiAccountId());
         alipayAPIResponse.setChannel("DPS" );
         alipayAPIResponse.setTransaction_type("1");    
         alipayAPIResponse.setTransactionDate("");
     	 alipayAPIResponse.setPgAlipayPayTime("");         
         alipayAPIResponse.setPgMerchantTransactionId("");
         
         alipayAPIDao.save(alipayAPIResponse);
         
 		 model.addAttribute("redirect_url", redirectUrl);
		 return redirectUrl;

	}

    @Override
    public Object[] createCancelTransaction(AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        Object returnParams[] = new Object[2];

        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        try {
            String sign_type = "MD5";
            String input_charset = alipayWalletVO.getCharSet();//"UTF-8";
            String alipayAPIURL = DynamicPaymentConstant.PG_ALIPAY_WALLET_URL;
            String merchantPrivateKey = alipayWalletVO.getPartner_key();//"9lsktl2d2tqmunkebgilrkkwk37kcww2";// OFFLINE to ONLINE mode
            // String merchantPrivateKey = "yq9ud70vvpmnsxk7u75g27jfwg02nenh";//OFFLINE mode

            Map<String, String> params = new HashMap<String, String>();
            params.put("_input_charset", input_charset);
            params.put("service", "alipay.acquire.overseas.spot.cancel");
            params.put("partner_trans_id", "" + alipayWalletVO.getMerchantTransactionId());
            params.put("partner", alipayWalletVO.getPayitnz_id());// OFFLINE to ONLINE mode
            // params.put("partner", "2088021966388155");//OFFLINE mode
            String finalURL = Payment.CreateUrl(params, merchantPrivateKey, alipayAPIURL, input_charset, sign_type);
            logger.info("Sent request for payment cancellation : " + alipayWalletVO.getMerchantTransactionId());
            returnParams[0] = finalURL;
            // save request parameters into database
            AlipayAPIRequest alipayAPIRequest = new AlipayAPIRequest();
            alipayAPIRequest.setPgService(params.get("service"));
            alipayAPIRequest.setPgSign(" ");
            alipayAPIRequest.setPgSignType(" ");
            alipayAPIRequest.setPgPartnerId(params.get("partner"));
            alipayAPIRequest.setPgInputCharset(params.get("_input_charset"));
            alipayAPIRequest.setMcQuantityCommodity(0);
            alipayAPIRequest.setMcPartnerTransId(params.get("partner_trans_id"));
            alipayAPIRequest.setMcTransCreateTime(" ");
            alipayAPIRequest.setMcMemo(" ");
            alipayAPIRequest.setPgExtendInfo(" ");
            alipayAPIRequest.setIpAddress(ipAddress);
            alipayAPIRequest.setRequestBy(sender);
            alipayAPIRequest.setRequestTime(new Date().toString());
            
            saveOrUpdate(alipayAPIRequest);

            genericAPIResponse.setId(alipayAPIRequest.getId());
            genericAPIResponse.setInfidigiAccountId(alipayWalletVO.getInfidigiAccountId());
            genericAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
            genericAPIResponse.setMerchantTransactionId(alipayAPIRequest.getMcPartnerTransId());
            genericAPIResponse.setMessage("Payment cancel request received");
            genericAPIResponse.setMobileCallId(alipayWalletVO.getMobileCallId());
            genericAPIResponse.setStatusCode("201");
            returnParams[1] = genericAPIResponse;
            System.out.println("---alipayAPIRequest id=" + alipayAPIRequest.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnParams;

    }

    @Override
    public Object[] createQueryTransaction(AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        StringBuffer responseAlipay = new StringBuffer();
        Object returnParams[] = new Object[2];

        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        try {
            String sign_type = "MD5";
            String input_charset = "UTF-8";
            String alipayAPIURL = DynamicPaymentConstant.PG_ALIPAY_WALLET_URL;
            // TODO: load merchant's private key from the table based on Infidigi account id
            String merchantPrivateKey = alipayWalletVO.getPartner_key();//"9lsktl2d2tqmunkebgilrkkwk37kcww2";// OFFLINE to ONLINE mode
            // String merchantPrivateKey = "yq9ud70vvpmnsxk7u75g27jfwg02nenh";//OFFLINE mode

            Map<String, String> params = new HashMap<String, String>();
            params.put("_input_charset", alipayWalletVO.getCharSet());//"UTF-8"
            params.put("service", alipayWalletVO.getService());//"alipay.acquire.overseas.query"
            params.put("partner_trans_id", "" + alipayWalletVO.getMerchantTransactionId());
            // params.put("alipay_trans_id", "" + alipayWalletVO.getAlipayTransactionId());
            params.put("partner", alipayWalletVO.getPayitnz_id());//"2088021966645500" OFFLINE to ONLINE mode
            // params.put("partner", "2088021966388155");//OFFLINE mode
            String finalURL = Payment.CreateUrl(params, merchantPrivateKey, alipayAPIURL, input_charset, sign_type);
            returnParams[0] = finalURL;
            // save request parameters into database
            AlipayAPIRequest alipayAPIRequest = new AlipayAPIRequest();
            alipayAPIRequest.setPgService(params.get("service"));
            alipayAPIRequest.setPgSign(" ");
            alipayAPIRequest.setPgSignType(" ");
            alipayAPIRequest.setPgPartnerId(params.get("partner"));
            alipayAPIRequest.setPgInputCharset(params.get("_input_charset"));
            alipayAPIRequest.setMcQuantityCommodity(0);
            alipayAPIRequest.setMcPartnerTransId(params.get("partner_trans_id"));
            alipayAPIRequest.setMcTransCreateTime(" ");
            alipayAPIRequest.setMcMemo(" ");
            alipayAPIRequest.setPgExtendInfo(" ");
            alipayAPIRequest.setIpAddress(ipAddress);
            alipayAPIRequest.setRequestBy(sender);
            alipayAPIRequest.setRequestTime(new Date().toString());
            saveOrUpdate(alipayAPIRequest);

            System.out.println("---alipayAPIRequest id=" + alipayAPIRequest.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnParams;

    }

    @Override
    public AlipayAPIResponse callAlipayReverseAPI(Object finalURL, AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        StringBuffer responseAlipay = new StringBuffer();
        AlipayAPIResponse alipayAPIResponse = null;
        try {
        	 
            URL obj = new URL(finalURL.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
            urlConnection.setRequestMethod("GET");

            // Send post request
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "TEXT/HTML");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.flush();
            wr.close();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + finalURL);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                responseAlipay.append(inputLine);
            }
            in.close();

            // print result
            System.out.println("Response from Alipay=" + responseAlipay.toString());
            
            alipayAPIResponse = XmlParser.parseAlipayReversalAPIResponse(responseAlipay.toString());
            alipayAPIResponse.setRequestBy(sender);
            alipayAPIResponse.setIpAddress(ipAddress);
            alipayAPIResponse.setRequestTime(new Date().toString());
            alipayAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
            alipayAPIResponse.setMethod_type("Alipay Offline");
            alipayAPIResponse.setTransaction_type("7");
            alipayAPIResponse.setRemark("");
            alipayAPIResponse.setDyMerchantId(alipayWalletVO.getInfidigiAccountId());
            logger.info("Reverse transaction is done for : " + alipayAPIResponse.getPgPartnerTransId());
            saveOrUpdate(alipayAPIResponse);
  
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alipayAPIResponse;

    }

    @Override
    public AlipayAPIResponse callAlipayCancelAPI(Object finalURL, AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        StringBuffer responseAlipay = new StringBuffer();
        AlipayAPIResponse alipayAPIResponse = null;
        try {

            URL obj = new URL(finalURL.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
            urlConnection.setRequestMethod("GET");

            // Send post request
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "TEXT/HTML");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.flush();
            wr.close();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + finalURL);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                responseAlipay.append(inputLine);
            }
            in.close();

            // print result
            System.out.println("---callAlipayCancelAPI() - Response from Alipay=" + responseAlipay.toString());

            alipayAPIResponse = XmlParser.parseAlipayCancelAPIResponse(responseAlipay.toString());
            alipayAPIResponse.setRequestBy(sender);
            alipayAPIResponse.setIpAddress(ipAddress);
            alipayAPIResponse.setRequestTime(new Date().toString());
            alipayAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
            alipayAPIResponse.setMethod_type("Alipay Offline");
            alipayAPIResponse.setTransaction_type("8");
            alipayAPIResponse.setRemark("");
            alipayAPIResponse.setAmount(alipayWalletVO.getAmount());
            alipayAPIResponse.setDyMerchantId(alipayWalletVO.getUser_id());
            logger.info("Payment cancellation completed for : " + alipayAPIResponse.getPgPartnerTransId());
            saveOrUpdate(alipayAPIResponse);
          
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alipayAPIResponse;

    }
    @Override
    public AlipayAPIResponse callAlipayCancelAPIWeb(Object finalURL, AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        StringBuffer responseAlipay = new StringBuffer();
        AlipayAPIResponse alipayAPIResponse = null;
        try {

            URL obj = new URL(finalURL.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
            urlConnection.setRequestMethod("GET");

            // Send post request
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "TEXT/HTML");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.flush();
            wr.close();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + finalURL);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;

//            while ((inputLine = in.readLine()) != null) {
//                responseAlipay.append(inputLine);
//            }
//           
           
            int flag = 0;
           
            while ((inputLine = in.readLine()) != null) {
            	if(inputLine.contains("<error>")&&inputLine.contains("</error>"))
            	{
            		flag = 1;
            		
            	}
            	responseAlipay.append(inputLine);
            }
            in.close();
            alipayAPIResponse = XmlParser.parseAlipayCancelAPIResponse(responseAlipay.toString());
            System.out.println("success value"+alipayAPIResponse.getPgIsSuccess());
            // print result
            if(flag == 0)
            {
                System.out.println("---callAlipayCancelAPI() - Response from Alipay=" + responseAlipay.toString());

             
                alipayAPIResponse.setRequestBy(sender);
                alipayAPIResponse.setIpAddress(ipAddress);
                alipayAPIResponse.setRequestTime(new Date().toString());
                alipayAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
                alipayAPIResponse.setMethod_type("Alipay Offline");
                alipayAPIResponse.setTransaction_type("8");
                alipayAPIResponse.setRemark("");
                alipayAPIResponse.setAmount(alipayWalletVO.getAmount());
                alipayAPIResponse.setDyMerchantId(alipayWalletVO.getUser_id());
                alipayAPIResponse.setId(alipayWalletVO.getId());
                
          
                alipayAPIResponse.setPgResultCode("CANCELLED");
                saveOrUpdate(alipayAPIResponse);
            }
            else
            {
            	if(alipayAPIResponse.getPgIsSuccess().equals("T"))
            	{
                System.out.println("---callAlipayCancelAPI() - Response from Alipay=" + responseAlipay.toString());
                
             //   alipayAPIResponse = XmlParser.parseAlipayCancelAPIResponse(responseAlipay.toString());
                alipayAPIResponse.setRequestBy(sender);
                alipayAPIResponse.setIpAddress(ipAddress);
                alipayAPIResponse.setRequestTime(new Date().toString());
                alipayAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
                alipayAPIResponse.setMethod_type("Alipay Offline");
                alipayAPIResponse.setTransaction_type("8");
                alipayAPIResponse.setRemark("");
                alipayAPIResponse.setAmount(alipayWalletVO.getAmount());
                alipayAPIResponse.setDyMerchantId(alipayWalletVO.getUser_id());
                saveOrUpdate(alipayAPIResponse);
                alipayAPIResponse.setPgIsSuccess("F");
            	}
            }
           
          
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alipayAPIResponse;

    }
    @Override
    public AlipayAPIResponse callAlipayRefundAPI(Object finalURL, AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        StringBuffer responseAlipay = new StringBuffer();
        AlipayAPIResponse alipayAPIResponse = null;
        try {

            URL obj = new URL(finalURL.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
            urlConnection.setRequestMethod("GET");

            // Send post request
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "TEXT/HTML");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.flush();
            wr.close();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + finalURL);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                responseAlipay.append(inputLine);
            }
            in.close();

            // print result
            System.out.println("Response from Alipay=" + responseAlipay.toString());

            alipayAPIResponse = XmlParser.parseAlipayRefundAPIResponse(responseAlipay.toString());
            alipayAPIResponse.setRequestBy(sender);
            alipayAPIResponse.setIpAddress(ipAddress);
            alipayAPIResponse.setRequestTime(new Date().toString());
            alipayAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
            alipayAPIResponse.setMethod_type("Alipay Offline");
            alipayAPIResponse.setTransaction_type("3");
            alipayAPIResponse.setRemark("");
            alipayAPIResponse.setDyMerchantId(alipayWalletVO.getInfidigiAccountId());
            logger.info("Payment Refund completed for : " + alipayAPIResponse.getPgPartnerTransId());
            saveOrUpdate(alipayAPIResponse);
          
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alipayAPIResponse;

    }
    @Override
    public AlipayAPIResponse callAlipayRefundAPIWeb(Object finalURL, AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        StringBuffer responseAlipay = new StringBuffer();
        AlipayAPIResponse alipayAPIResponse = new AlipayAPIResponse();
        try {

        	if(finalURL != null)
        	{
            URL obj = new URL(finalURL.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
            urlConnection.setRequestMethod("GET");

            // Send post request
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "TEXT/HTML");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.flush();
            wr.close();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + finalURL);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;

            
            int flag = 0;
           
            while ((inputLine = in.readLine()) != null) {
            	if(inputLine.contains("<error>")&&inputLine.contains("</error>"))
            	{
            		flag = 1;
            		
            	}
            	responseAlipay.append(inputLine);
            }
            in.close();

            // print result
            System.out.println("Response from Alipay=" + responseAlipay.toString());

        
            if(flag == 1)
            {
            	
                alipayAPIResponse = XmlParser.parseAlipayRefundAPIResponse(responseAlipay.toString());
                alipayAPIResponse.setRequestBy(sender);
                alipayAPIResponse.setIpAddress(ipAddress);
                alipayAPIResponse.setRequestTime(new Date().toString());
                alipayAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
                alipayAPIResponse.setMethod_type("Alipay Offline");
                alipayAPIResponse.setTransaction_type(DynamicPaymentConstant.TRANSACTION_REFUND);
                alipayAPIResponse.setRemark("");
                alipayAPIResponse.setAmount(alipayWalletVO.getAmount());
                alipayAPIResponse.setDyMerchantId(alipayWalletVO.getInfidigiAccountId());
                alipayAPIResponse.setPgTransactionDate(new Date());
                logger.info("Payment Refund completed for : " + alipayAPIResponse.getPgPartnerTransId());
                saveOrUpdate(alipayAPIResponse);
                alipayAPIResponse.setMessage("Refund failed!");
            }
            else
            {
            
            System.out.println("id==="+alipayWalletVO.getId());
            System.out.println("alipayAPIResponse id==="+alipayAPIResponse.getId());
                alipayAPIResponse = XmlParser.parseAlipayRefundAPIResponse(responseAlipay.toString());
                alipayAPIResponse.setRequestBy(sender);
                alipayAPIResponse.setIpAddress(ipAddress);
                alipayAPIResponse.setRequestTime(new Date().toString());
                alipayAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
                alipayAPIResponse.setMethod_type("Alipay Offline");
                alipayAPIResponse.setTransaction_type(DynamicPaymentConstant.TRANSACTION_REFUND);
                alipayAPIResponse.setPgResultCode("REFUND");
                alipayAPIResponse.setRemark("");
                alipayAPIResponse.setAmount(alipayWalletVO.getAmount());
                alipayAPIResponse.setDyMerchantId(alipayWalletVO.getInfidigiAccountId());
                alipayAPIResponse.setPgTransactionDate(new Date());
                logger.info("Payment Refund completed for : " + alipayAPIResponse.getPgPartnerTransId());
                saveOrUpdate(alipayAPIResponse);
            	alipayAPIResponse.setMessage("Refund successfull!");
            }
        	}
        	else
        	{
        		alipayAPIResponse.setMessage("Something wrong in merchant configuration!");
        	}
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alipayAPIResponse;

    }

    @Override
    public AlipayAPIResponse callAlipayQueryAPI(Object finalURL, AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        StringBuffer responseAlipay = new StringBuffer();
        AlipayAPIResponse alipayAPIResponse = null;
        try {

            URL obj = new URL(finalURL.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
            urlConnection.setRequestMethod("GET");

            // Send post request
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "TEXT/HTML");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.flush();
            wr.close();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + finalURL);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                responseAlipay.append(inputLine);
            }
            in.close();

            // print result
            System.out.println("Response from Alipay=" + responseAlipay.toString());

            alipayAPIResponse = XmlParser.parseAlipayQueryAPIResponse(responseAlipay.toString());
            alipayAPIResponse.setRequestBy(sender);
            alipayAPIResponse.setAmount(alipayWalletVO.getAmount());
            alipayAPIResponse.setIpAddress(ipAddress);
            alipayAPIResponse.setRequestTime(new Date().toString());
            alipayAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
            alipayAPIResponse.setMethod_type("Alipay Offline");
            alipayAPIResponse.setTransaction_type("1");
            alipayAPIResponse.setRemark("");
            alipayAPIResponse.setDyMerchantId(alipayWalletVO.getInfidigiAccountId());
            saveOrUpdate(alipayAPIResponse);
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alipayAPIResponse;

    }
    
    /*@Override
    public AlipayAPIResponse callAlipayQueryAPI(Object finalURL, AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        StringBuffer responseAlipay = new StringBuffer();
        AlipayAPIResponse alipayAPIResponse = null;
        try {

            URL obj = new URL(finalURL.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
            urlConnection.setRequestMethod("GET");

            // Send post request
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "TEXT/HTML");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.flush();
            wr.close();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + finalURL);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                responseAlipay.append(inputLine);
            }
            in.close();

            // print result
            System.out.println("Response from Alipay=" + responseAlipay.toString());

            alipayAPIResponse = XmlParser.parseAlipayQueryAPIResponse(responseAlipay.toString());
            alipayAPIResponse.setRequestBy(sender);
            alipayAPIResponse.setIpAddress(ipAddress);
            alipayAPIResponse.setRequestTime(new Date().toString());
            saveOrUpdate(alipayAPIResponse);
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alipayAPIResponse;

    }*/


    @Override
    public AlipayAPIResponse getPaymentTransactionDetails(AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        return alipayAPIDao.getTransactionDetailsByPartnerTransactionId(alipayWalletVO.getMerchantTransactionId());
    }

    @Override
    public  List<GenericAPIResponse>  getPaymentTransactionByCriteria(AlipayWalletVO alipayWalletVO, String ipAddress, String sender) {
        return alipayAPIDao.getTransactionDetailsByCriteria(alipayWalletVO);
    }

    @Override
    public void saveFile(String mcTransactionId, MultipartFile file, String ipAddress, String sender) {
        alipayAPIDao.insertFile(mcTransactionId, file, ipAddress, sender);
        
    }

    @Override
    public String getMerchantCompanyName(String infidigiAccountId, String infidigiUserID) {
        return alipayAPIDao.getMerchantCompanyName(infidigiAccountId, infidigiUserID);
    }

    @Override
    public byte[] getFile(String mcTransactionId) {
        return alipayAPIDao.getFile(mcTransactionId);
        
    }

	@Override
	public List<User> getUserList(String infidigiAccountId) {
		return alipayAPIDao.getUserList(infidigiAccountId);
	}
	@Override
	public String getMerchantName(String infidigiAccountId, String infidigiUserID) {
		return alipayAPIDao.getMerchantName(infidigiAccountId, infidigiUserID);
	}
	 @Override
	    public List<AlipayAPIResponse> getTransactionDetailsByCriteriaWeb(AlipayWalletVO alipayWalletVO, String ipAddress, String sender)  {
	         List<AlipayAPIResponse> list = alipayAPIDao.getTransactionDetailsByCriteriaWeb(alipayWalletVO);
	         return list;
	    }
//	@Override
//	public User getUser(String infidigiUserID) {
//		// TODO Auto-generated method stub
//		return alipayAPIDao.getUser(infidigiUserID);
//	}

	@Override
	public List<AlipayAPIResponse> getRefundTransactionDetailsByCriteriaWeb(AlipayWalletVO alipayWalletVO,String ipaddress, String sender) {
		// TODO Auto-generated method stub
		return this.alipayAPIDao.getRefundTransactionDetailsByCriteriaWeb(alipayWalletVO);
	}

}
