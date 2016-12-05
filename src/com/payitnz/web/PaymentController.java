package com.payitnz.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.payitnz.util.Payment;
import com.dpg.dynamicgateway.DynamicPaymentOperations;
import com.dynamicpayment.paymentexpress.PxPay;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payitnz.config.DynamicPaymentConstant;
import com.payitnz.model.AlipayAPIRequest;
import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.GenericAPIResponse;
import com.payitnz.model.RequestBean;
import com.payitnz.service.AlipayAPIService;
import com.payitnz.service.AlipayPaymentService;
import com.payitnz.service.DashboardService;
import com.payitnz.service.UserService;

@Controller
public class PaymentController {

	@Autowired
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	private DashboardService dashboardService;
	
	@Autowired
	public void setDashboardService(DashboardService dashboardService){
		this.dashboardService = dashboardService;
	}
	@Autowired
	private AlipayPaymentService paymentService;
	
	@Autowired
	public void setPaymentService(AlipayPaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	
	@Autowired
	private AlipayAPIService alipayAPIService;
	
	@Autowired
	public void setUserService(AlipayAPIService alipayAPIService) {
		this.alipayAPIService = alipayAPIService;
	}
	
	@RequestMapping(value = "/onlinePayment")
	public ModelAndView listContact(ModelAndView model, HttpServletRequest request) throws IOException {
		model.setViewName("Flo2CashForm");
		return model;
	}
	public DynamicPaymentOperations dp = new DynamicPaymentOperations();
	
	@RequestMapping(value = "/callPayemntApi", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView saveContact(@ModelAttribute(value="ShoppingCart") AlipayWalletVO alipayWalletVO, HttpServletResponse response,BindingResult result,
			HttpSession session, Model model, HttpServletRequest request) throws Exception {
		Object userid =  request.getSession().getAttribute("user_id");
		
		ModelAndView view = new ModelAndView("Flo2CashForm");
		System.out.println("gateway"+alipayWalletVO.getGateway());   
		//get form data 
		 String ipAddress = request.getRemoteAddr();
	     String sender = request.getRemoteHost();
	     
	     String str = alipayWalletVO.getGateway();

	     String m = str.replaceFirst(",","");
	     alipayWalletVO.setGateway(m);
	     System.out.println(alipayWalletVO.getAmount());
		 System.out.println(alipayWalletVO.getReference());
		 System.out.println(alipayWalletVO.getInfidigiAccountId());
		 System.out.println(alipayWalletVO.getParticular());
		 request.getSession().setAttribute("accountId",alipayWalletVO.getInfidigiAccountId());
		//check account details
		 alipayWalletVO.setUser_id(alipayWalletVO.getInfidigiAccountId());
		 if(alipayWalletVO.getGateway().equals("Alipay Online"))
		 {
		 AlipayWalletVO info = userService.getAlipayOnlineRecord(alipayWalletVO);
		 //request.getSession().setAttribute("accountId",alipayWalletVO.getInfidigiAccountId());
	    if(info != null)
	    {
	    	//save details in session
	    	
	    	
	    	//save transaction details
	    	
	         String amount = alipayWalletVO.getAmount();
//				transactionBean.setAmount(amount);
//	      //  transactionBean.setTax(confee);
//	         transactionBean.setMcReference(f2c.getF2c_merchant_reference());
//	         transactionBean.setMcItemName("");
//	         transactionBean.setPgResultCode("FAILED");
//	         transactionBean.setPgIsSuccess("F");
//	        transactionBean.setDyMerchantId(alipayWalletVO.getUser_id());
//	         transactionBean.setBank_name("");
//	         transactionBean.setPoli_payerAcc_number("");
//	         transactionBean.setPoli_payerAcc_sortCode("");
//	         transactionBean.setMethod_type("Flo2Cash");
//	         transactionBean.setPgPartnerTransId(""+partnerTranId);
//	        alipayAPIService.saveOrUpdate(transactionBean);
//	    	//perform transaction
	    	String url = alipayAPIService.processAlipay(alipayWalletVO, model, info, ipAddress, sender, request);
	    	
	    	
	    	if(url.equals("") || url == null)
			{

				result.addError(new ObjectError("error", "Invalid Method Data."));
				return view;	
			}
			else
			{

				return new ModelAndView("redirect:" + url);
			}
		 }
	    	else{
	    	
	    	result.addError(new ObjectError("error", "Invalid user Data."));
			return view;
	    }
	    	
	    }
		 else
		 {
		 if(alipayWalletVO.getGateway().equals("Flo2Cash"))
		 {
			 AlipayAPIResponse transactionBean = new AlipayAPIResponse();
			 AlipayWalletVO f2c = dashboardService.getF2CRecord(alipayWalletVO);
			 Random rand = new Random();
			 long partnerTranId = Math.abs(rand.nextLong());
	         transactionBean.setPgPartnerTransId(""+partnerTranId);					        
 
	         String amount = alipayWalletVO.getAmount();
				transactionBean.setAmount(amount);
	      //  transactionBean.setTax(confee);
	         transactionBean.setMcReference(f2c.getF2c_merchant_reference());
	         transactionBean.setMcItemName("");
	         transactionBean.setPgResultCode("FAILED");
	         transactionBean.setPgIsSuccess("F");
	        transactionBean.setDyMerchantId(alipayWalletVO.getUser_id());
	         transactionBean.setBank_name("");
	         transactionBean.setPoli_payerAcc_number("");
	         transactionBean.setPoli_payerAcc_sortCode("");
	         transactionBean.setMethod_type("Flo2Cash");
	         transactionBean.setPgPartnerTransId(""+partnerTranId);
	         transactionBean.setRequestTime(new Date().toString());
	         transactionBean.setMcCurrency("NZD");
	         transactionBean.setMcItemName(alipayWalletVO.getParticular());
	         transactionBean.setReference(alipayWalletVO.getReference());
	        alipayAPIService.saveOrUpdate(transactionBean);
	        alipayWalletVO.setPgPartnerTransId(""+partnerTranId);
			 model = alipayAPIService.processF2C(alipayWalletVO, model, f2c, ipAddress, sender, request);
				return new ModelAndView("redirect:" + DynamicPaymentConstant.FLO2CASHURL);
		 }
		 else
		 {
			 if(alipayWalletVO.getGateway().equals("POLi"))
			 {
				 AlipayWalletVO poli = dashboardService.getPoliRecord(alipayWalletVO);
				 Random rand = new Random();
				 long partnerTranId = Math.abs(rand.nextLong());
				 alipayWalletVO.setPgPartnerTransId(""+partnerTranId);	
				 AlipayAPIResponse transactionBean = new AlipayAPIResponse();
				// AlipayWalletVO cup = dashboardService.getCUPRecord(alipayWalletVO);
//				 Random rand = new Random();
//				 long partnerTranId = Math.abs(rand.nextLong());
		         transactionBean.setPgPartnerTransId(""+partnerTranId);					        
	 
		         String amount = alipayWalletVO.getAmount();
					transactionBean.setAmount(amount);
		      //  transactionBean.setTax(confee);
		         transactionBean.setMcReference(poli.getF2c_merchant_reference());
		         transactionBean.setMcItemName("");
		         transactionBean.setPgResultCode("FAILED");
		         transactionBean.setPgIsSuccess("F");
		        transactionBean.setDyMerchantId(alipayWalletVO.getUser_id());
		         transactionBean.setBank_name("");
		         transactionBean.setPoli_payerAcc_number("");
		         transactionBean.setPoli_payerAcc_sortCode("");
		         transactionBean.setMethod_type("POLi");
		         transactionBean.setReference(alipayWalletVO.getReference());
		         transactionBean.setPgTransactionDate(new Date());
		         transactionBean.setRequestTime(new Date().toString());
		         transactionBean.setMcCurrency(poli.getCurrency_code());
		         transactionBean.setMcItemName(alipayWalletVO.getParticular());
		        alipayAPIService.saveOrUpdate(transactionBean);
		        alipayWalletVO.setPgPartnerTransId(""+partnerTranId);
		        poli.setMerchant_name(""+partnerTranId);
			 
				 
				 String redirectURL = alipayAPIService.processPoli(alipayWalletVO, model, poli, ipAddress, sender, request);
				 				
				 view.setViewName("error");
					if (result.hasErrors()) {
						
					}
				if(redirectURL == null || redirectURL.equals(""))
				{
					result.addError(new ObjectError("error", "Invalid Method Data."));
					return view;	
				}
				else
				{
					return new ModelAndView("redirect:" + redirectURL);
				}
				
			 }
			 else
			 {
				 
				 if(alipayWalletVO.getGateway().equals("CUP"))
				 {
					 AlipayAPIResponse transactionBean = new AlipayAPIResponse();
					 AlipayWalletVO cup = dashboardService.getCUPRecord(alipayWalletVO);
					 Random rand = new Random();
					 long partnerTranId = Math.abs(rand.nextLong());
			         transactionBean.setPgPartnerTransId(""+partnerTranId);					        
		 
			         String amount = alipayWalletVO.getAmount();
						transactionBean.setAmount(amount);
			      //  transactionBean.setTax(confee);
			         transactionBean.setMcReference(cup.getF2c_merchant_reference());
			         transactionBean.setMcItemName("");
			         transactionBean.setPgResultCode("FAILED");
			         transactionBean.setPgIsSuccess("F");
			        transactionBean.setDyMerchantId(alipayWalletVO.getUser_id());
			         transactionBean.setBank_name("");
			         transactionBean.setPoli_payerAcc_number("");
			         transactionBean.setPoli_payerAcc_sortCode("");
			         transactionBean.setMethod_type("CUP");
			        alipayAPIService.saveOrUpdate(transactionBean);
			        alipayWalletVO.setPgPartnerTransId(""+partnerTranId);
			        cup.setMerchant_name(""+partnerTranId);
					// AlipayWalletVO cup = dashboardService.getCUPRecord(alipayWalletVO);
					  RequestBean cupAPIRequest = new RequestBean();
					 dp.startService(request, response,  cup, alipayWalletVO, cupAPIRequest);
					 alipayAPIService.saveCUPReq(cupAPIRequest);
			 		// paymentService.SaveTransaction(transactionBean);
					
				 }
				 else
				 {
					 if(alipayWalletVO.getGateway().equals("Payment Express(DPS)"))
					 {
						 AlipayWalletVO dps = dashboardService.getDPSRecord(alipayWalletVO);
						String redirecturl = alipayAPIService.processDPS(alipayWalletVO, model, dps, ipAddress, sender, request);
							return new ModelAndView("redirect:" + redirecturl);
					 }
				 }
			 }
			 
			 
		 }
	   
		 }
     return null;
	}
	
	
	@RequestMapping(value = "/resonseF2C", method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView DynamicResponseF2C(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("DynamicResponseF2C");
		Enumeration<String> parameterNames = request.getParameterNames();
	//	TransactionBean transactionBean = new TransactionBean();
		JSONObject json = new JSONObject();
//		while (parameterNames.hasMoreElements()) {
//
//			String paramName = parameterNames.nextElement();
//			
//			System.out.println(paramName);
//			System.out.println("\n");
//
//			String[] paramValues = request.getParameterValues(paramName);
//			for (int i = 0; i < paramValues.length; i++) {
//				String paramValue = paramValues[i];
//				System.out.println("\t" + paramValue);
//				System.out.println("\n");
//				
//				json.put(paramName, paramValue);
//			}
//		}
		if(parameterNames.hasMoreElements())
		{
		if(parameterNames.hasMoreElements() || !request.getParameter("reference").equals(""))
		{
		//System.out.println(request.getParameter("txn_status"));
		String transactionID = request.getParameter("reference");		
		
		//TransactionBean transactionBean = new TransactionBean();
		List<AlipayAPIResponse> transactionList= alipayAPIService.getTransactionsOfID(transactionID);
		AlipayAPIResponse transactionBean = new AlipayAPIResponse();
		for (AlipayAPIResponse alipayAPIResponse : transactionList) {
			transactionBean = alipayAPIResponse;
		}
		
		
       // transactionBean.setPgPartnerTransId(transactionID);
        transactionBean.setMcTransAmount(request.getParameter("amount"));
       
        String Status = request.getParameter("txn_status");
        String card_type = request.getParameter("card_type");
        System.out.println("Status"+Status);
		if(Status.equals("2"))
		{
			transactionBean.setPgResultCode("SUCCESS");
			transactionBean.setPgIsSuccess("T");
		}
		if(Status.equals("1"))
		{
			transactionBean.setPgResultCode("PROCESSING");
			 transactionBean.setPgIsSuccess("F");		
		}
		if(Status.equals("3"))
		{
			transactionBean.setPgResultCode("FAILED");
			 transactionBean.setPgIsSuccess("F");
		}
		if(Status.equals("4"))
		{
			transactionBean.setPgResultCode("BLOCKED");
			 transactionBean.setPgIsSuccess("F");
		}
		if(Status.equals("11"))
		{
			transactionBean.setPgResultCode("DECLINED");
			 transactionBean.setPgIsSuccess("F");
		}  
		
		
		if(card_type.equals("1"))
		{
			transactionBean.setCardType(DynamicPaymentConstant.METHOD_AMERICAN_EXP);;
	
		}
		if(card_type.equals("3"))
		{
			transactionBean.setCardType(DynamicPaymentConstant.METHOD_DINERS_CLUB);
				
		}
		if(card_type.equals("4"))
		{
			transactionBean.setCardType(DynamicPaymentConstant.METHOD_MASTER_CARD);
			
		}
		if(card_type.equals("5"))
		{
			transactionBean.setCardType(DynamicPaymentConstant.METHOD_VISA);
			 
		}
		if(card_type.equals("6"))
		{
			transactionBean.setCardType(DynamicPaymentConstant.METHOD_CUP);
			
		}        
		transactionBean.setPgTransactionDate(new Date());
		alipayAPIService.saveOrUpdate(transactionBean);
       
		}
		}
		else
		{
			request.setAttribute("txn_status", 11);
		}
		
		
//       //@dev-atul
//        //if status success update payment done status
//        if(transactionBean.getStatus() == 1){
//        	 updatePaymentLinkTransactionStatus(request);
//        }
//        transactionBean.setF2c_receipt_no(request.getParameter("receipt_no"));
//        transactionBean.setResponse(json.toString());
//        transactionBean.setGateway_transaction_id(request.getParameter("txn_id"));
//        request.setAttribute("merchant_unique_reference", transactionBean.getMerchant_unique_reference());
//        paymentService.UpdateTransaction(transactionBean);
//        Object id= request.getSession().getAttribute("user_id");
//        DynamicPayBean bean = new DynamicPayBean();
//		bean.setUser_id(Integer.parseInt(id.toString()));
//		DynamicPayBean f2c = paymentService.getF2CRecord(bean);
        
        model.addObject("return_url",DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SERVER_SITE_URL+"onlinePayment");
		
		return model;
	}
	@RequestMapping(value = "/unionPayResponse", method = RequestMethod.POST)
	@ResponseBody
	public void UnionPayResponse(@ModelAttribute("shoppingCart") @Validated AlipayWalletVO shoppingCart,
			BindingResult result, Model model, final RedirectAttributes redirectAttributes, HttpSession session,
			HttpServletRequest req, HttpServletResponse response) {
		Enumeration<String> parameterNames = req.getParameterNames();
		//TransactionBean transactionBean = new TransactionBean();
		JSONObject res = new JSONObject();
		while (parameterNames.hasMoreElements()) {

			String paramName = parameterNames.nextElement();
			System.out.println(paramName);
			System.out.println("\n");
			
			String[] paramValues = req.getParameterValues(paramName);
	
			for (int i = 0; i < paramValues.length; i++) {
				String paramValue = paramValues[i];
				System.out.println("\t" + paramValue);
				System.out.println("\n");
				res.put(paramName,paramValue);
				model.addAttribute(paramName,paramValue);
			}
			
		}
		String transactionID = req.getParameter("merAbbr");
//		model.addAttribute("merchant_unique_reference",transactionBean.getMerchant_unique_reference());
//		req.setAttribute("merchant_unique_reference", transactionBean.getMerchant_unique_reference());
		AlipayAPIResponse transactionBean = new AlipayAPIResponse();
		List <AlipayAPIResponse> transactions = alipayAPIService.getTransactionsOfID(transactionID);
		for (AlipayAPIResponse alipayAPIResponse : transactions) {
			transactionBean = alipayAPIResponse;
		}
	
//		transactionBean = paymentService.getTransactionByID(transactionBean);
//		transactionBean.setResponse(res.toString());
		String Status = req.getParameter("respCode");
//		
		if(Status.equals("00"))
		{
			transactionBean.setPgResultCode("SUCCESS");
			transactionBean.setPgIsSuccess("T");
		}   
		else
		{
		transactionBean.setPgResultCode("DECLINED");
		 transactionBean.setPgIsSuccess("F");
		}

	
		transactionBean.setCupReserved(req.getParameter("cupReserved"));
		transactionBean.setTraceNumber(req.getParameter("traceNumber"));
		transactionBean.setPgAlipayBuyerUserId(req.getParameter("merId"));
		transactionBean.setExchangeDate(req.getParameter("exchangeDate"));
		transactionBean.setPgExchangeRate(Double.parseDouble(req.getParameter("exchangeRate")));
		if(req.getParameter("orderCurrency").equals("554"))
		{
		transactionBean.setMcCurrency("NDZ");
		}
		transactionBean.setOrderNumber(req.getParameter("orderNumber"));
		transactionBean.setPgAlipayTransId(req.getParameter("qid"));
		
		transactionBean.setPgAlipayPayTime(req.getParameter("respTime"));
		transactionBean.setSettleCurrency(req.getParameter("settleCurrency"));
		transactionBean.setSignMethod(req.getParameter("signMethod"));
		transactionBean.setPgSign(req.getParameter("signature"));
		transactionBean.setPgTransactionDate(new Date());
		alipayAPIService.saveOrUpdate(transactionBean);
//		paymentService.UpdateTransaction(transactionBean);
//		
//		//@dev-atul
//        //if status success update payment done status
//        if(transactionBean.getStatus() == 1){
//        	 updatePaymentLinkTransactionStatus(req);
//        }
//        
//		System.out.println("cardType"+req.getParameter("cupReserved "));
//		Object id= req.getSession().getAttribute("user_id");
//        DynamicPayBean bean = new DynamicPayBean();
//		bean.setUser_id(Integer.parseInt(id.toString()));
//		DynamicPayBean dynamic = paymentService.getCUPRecord(bean);
		model.addAttribute("return_url",DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SERVER_SITE_URL+"onlinePayment");
		dp.responseService(req, response, DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SERVER_SITE_URL+"onlinePayment");
	}

	@RequestMapping(value = "/alipayResponse", method = RequestMethod.GET)
	 public ModelAndView AlipayResponse(@ModelAttribute("shoppingCart") @Validated AlipayWalletVO shoppingCart,
	   BindingResult result, Model model, final RedirectAttributes redirectAttributes, HttpSession session,
	   HttpServletRequest req, HttpServletResponse response) {
	   ModelAndView view = new ModelAndView("DynamicResponseAlipay");
	   Enumeration<String> parameterNames = req.getParameterNames();
	
	  // Object id= req.getSession().getAttribute("user_id");
	   Object accountID= req.getSession().getAttribute("accountId");
	      //   DynamicPayBean bean = new DynamicPayBean();
	  // bean.setUser_id(Integer.parseInt(id.toString()));
	   shoppingCart.setUser_id(accountID.toString());
	   AlipayWalletVO info = userService.getAlipayOnlineRecord(shoppingCart);
	   model.addAttribute("return_url",info.getAlipay_online_return_url());
	   JSONObject json = new JSONObject();
	   while (parameterNames.hasMoreElements()) {

	    String paramName = parameterNames.nextElement();
	    System.out.println(paramName);
	    System.out.println("\n");
	    
	    String[] paramValues = req.getParameterValues(paramName);
	    for (int i = 0; i < paramValues.length; i++) {
		     String paramValue = paramValues[i];
		     System.out.println("\t" + paramValue);
		     System.out.println("\n");
		     json.put(paramName, paramValue);
		     model.addAttribute(paramName,paramValue);
		    }	    
	    }
	   
	   String ipAddress = req.getRemoteAddr();
	     String sender = req.getRemoteHost();
	  //   int role_id = (int) request.getSession().getAttribute("role_id");
			//Object user_id = req.getSession().getAttribute("user_id");
	   AlipayAPIResponse alipayAPIResponse = new AlipayAPIResponse();
	   alipayAPIResponse.setRequestBy(sender);
       alipayAPIResponse.setIpAddress(ipAddress);
       alipayAPIResponse.setInfidigiUserId(accountID.toString());

       Calendar cal=Calendar.getInstance();
       int year = Calendar.getInstance().get(Calendar.YEAR);
       int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
       int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
       
       String curDate = "" + day + "/" + month + "/" + year;
       SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:MM:ss");

      System.out.println("status==="+alipayAPIResponse.getStatusCode());
       alipayAPIResponse.setAmount(req.getParameter("total_fee"));
       alipayAPIResponse.setPgTransactionDate(cal.getTime());
       alipayAPIResponse.setRequestTime(new Date().toString());
       System.out.println("transaction id in 1st api"+alipayAPIResponse.getPgPartnerTransId()+cal.getTime());
     //  alipayWalletVO.setPgPartnerTransId(alipayAPIResponse.getPgPartnerTransId());
       alipayAPIResponse.setPgPartnerTransId(req.getSession().getAttribute("partner-trans-id").toString());
       if(req.getParameter("trade_status").equals("TRADE_FINISHED"))
       {
       alipayAPIResponse.setPgResultCode("SUCCESS");
       alipayAPIResponse.setPgIsSuccess("T");
       }
       else
       {
    	   alipayAPIResponse.setPgResultCode("DECLINED");
    	   alipayAPIResponse.setPgIsSuccess("F");
       }
       alipayAPIResponse.setMcReference(info.getReference());
       
       alipayAPIResponse.setPgAlipayTransId(req.getParameter("trade_no"));
       alipayAPIResponse.setTransaction_type("1");
       alipayAPIResponse.setDyMerchantId(accountID.toString());
       alipayAPIResponse.setRemark("");
       alipayAPIResponse.setPgAlipayBuyerUserId("");
       alipayAPIResponse.setPgAlipayPayTime("");
       alipayAPIResponse.setMcCurrency(req.getParameter("currency"));
       alipayAPIResponse.setMcTransAmount(req.getParameter("total_fee"));
       alipayAPIResponse.setRequestTime(req.getParameter("out_trade_no"));
       alipayAPIResponse.setMethod_type("Alipay Online");
       alipayAPIService.saveOrUpdate(alipayAPIResponse);
	
	     System.out.println("json"+json.toString());
	    return view;
	 }
		
	@RequestMapping(value = "/poliResponse")
	public String poliResponse(HttpServletRequest request, ModelMap model, HttpServletResponse response,
			HttpSession session) {
		 String ipAddress = request.getRemoteAddr();
	     String sender = request.getRemoteHost();
		Object id= request.getSession().getAttribute("accountId");
        AlipayWalletVO bean = new AlipayWalletVO();
		bean.setUser_id(id.toString());
		AlipayWalletVO poli = dashboardService.getPoliRecord(bean);
		try {
			String token = request.getParameter("token");
		//	MerchantConfigBean sessionData = null;

			try {
				String webPage = DynamicPaymentConstant.PG_POLI_GET_TRANSACTION_URL;
				webPage = webPage + "?token=" + URLEncoder.encode(token, "UTF-8");
		        
				StringBuffer queryString = new StringBuffer("?");
				String accountId = poli.getPoli_account_id();
				String password = poli.getPassword();
				 Object accountID= request.getSession().getAttribute("accountId");
				String authString = accountId + ":" + password;
				byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
				String authStringEnc = new String(authEncBytes);

				URL obj;
				try {
					obj = new URL(webPage);
					HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
					urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
					urlConnection.setRequestMethod("GET");

					// Send post request
					urlConnection.setDoOutput(true);
					urlConnection.setDoInput(true);
					urlConnection.setRequestProperty("Content-Type", "application/json");

					int responseCode = urlConnection.getResponseCode();
					
					System.out.println("\nSending 'POST' request to URL : " + webPage);

					System.out.println("Response Code : " + responseCode);
				
					if(responseCode != 401)
					{
					BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					String inputLine;
					StringBuffer responsePoli = new StringBuffer();
					
					while ((inputLine = in.readLine()) != null) {
						responsePoli.append(inputLine);
					}
					in.close();

					// print result
					System.out.println(responsePoli.toString());

					ObjectMapper mapper = new ObjectMapper();
					Map<String, Object> map = mapper.readValue(responsePoli.toString(), new TypeReference<Map<String, Object>>() {
					});
					System.out.println("---map=" + map);
					Iterator itr = map.keySet().iterator();

					while (itr.hasNext()) {
						String key = (String) itr.next();
						Object value = map.get(key);
						queryString.append(key);
						queryString.append("=");
						queryString.append(value);
						queryString.append("&");
					}
					
					//insert response parameters into database
					//PayerAcctSuffix=&PayerAcctNumber=98742364&PayerAcctSortCode=123456&MerchantAcctNumber=0383496&MerchantAcctSuffix=000&MerchantAcctSortCode=123077&MerchantAcctName=Ahura%20Consulting&MerchantReferenceData=null&TransactionRefNo=996425228454&CurrencyCode=NZD&CountryCode=NZL&PaymentAmount=33.0&AmountPaid=33.0&EstablishedDateTime=2016-04-12T14:31:08.58&StartDateTime=2016-04-12T14:31:08.58&EndDateTime=2016-04-12T14:31:48.437&BankReceipt=44021973-120738&BankReceiptDateTime=12%20April%202016%2014:31:48&TransactionStatusCode=Completed&ErrorCode=&ErrorMessage=&FinancialInstitutionCode=iBankNZ01&FinancialInstitutionName=iBank%20NZ%2001&MerchantReference=merchant%20reference&MerchantAccountSuffix=000&MerchantAccountNumber=0383496&PayerFirstName=&PayerFamilyName=&PayerAccountSuffix=&
					JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
					String PayerAcctSuffix = (String)map.get("PayerAcctSuffix");
					String PayerAcctNumber = (String)map.get("PayerAcctNumber");
					String PayerAcctSortCode = (String)map.get("PayerAcctSortCode");
					String MerchantAcctNumber = (String)map.get("MerchantAcctNumber");
					String MerchantAcctSuffix = (String)map.get("MerchantAcctSuffix");
					String MerchantAcctSortCode = (String)map.get("MerchantAcctSortCode");
					String MerchantAcctName = (String)map.get("MerchantAcctName");
					String MerchantReferenceData = (String)map.get("MerchantReferenceData");
					String TransactionRefNo = (String)map.get("TransactionRefNo");
					String CurrencyCode = (String)map.get("CurrencyCode");
					String CountryCode = (String)map.get("CountryCode");
					String PaymentAmount = map.get("PaymentAmount").toString();
					String AmountPaid = map.get("AmountPaid").toString();
					String EstablishedDateTime = (String)map.get("EstablishedDateTime");
					String StartDateTime = (String)map.get("StartDateTime");
					String EndDateTime = (String)map.get("EndDateTime");
					String BankReceipt = (String)map.get("BankReceipt");
					String BankReceiptDateTime = (String)map.get("BankReceiptDateTime");
					String TransactionStatusCode = (String)map.get("TransactionStatusCode");
					String ErrorCode = (String)map.get("ErrorCode");
					String ErrorMessage = (String)map.get("ErrorMessage");
					String FinancialInstitutionCode = (String)map.get("FinancialInstitutionCode");
					String FinancialInstitutionName = (String)map.get("FinancialInstitutionName");
					String MerchantReference = (String)map.get("MerchantReference");
					String MerchantAccountSuffix = (String)map.get("MerchantAccountSuffix");
					String MerchantAccountNumber = (String) map.get("MerchantAccountNumber");
					String PayerFirstName = (String)map.get("PayerFirstName");
					String PayerFamilyName = (String)map.get("PayerFamilyName");
					String PayerAccountSuffix = (String)map.get("PayerAccountSuffix");
					
					
					List <AlipayAPIResponse> alipayAPIResponseList = alipayAPIService.getTransactionsOfID(MerchantReference);
					AlipayAPIResponse alipayAPIResponse = new AlipayAPIResponse();
					for (AlipayAPIResponse alipayAPI : alipayAPIResponseList) {
						alipayAPIResponse = alipayAPI;
					}
					   alipayAPIResponse.setRequestBy(sender);
				       alipayAPIResponse.setIpAddress(ipAddress);
				       alipayAPIResponse.setInfidigiUserId(accountID.toString());
				       Calendar cal=Calendar.getInstance();
				       int year = Calendar.getInstance().get(Calendar.YEAR);
				       int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
				       int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				       
				       String curDate = "" + day + "/" + month + "/" + year;
				       SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:MM:ss");
				       alipayAPIResponse.setAmount(PaymentAmount);
				       alipayAPIResponse.setPgTransactionDate(cal.getTime());
				       alipayAPIResponse.setRequestTime(new Date().toString());
				       alipayAPIResponse.setPgPartnerTransId(MerchantReference);
				       if(TransactionStatusCode.equals("Completed"))
						{
				    	   alipayAPIResponse.setPgResultCode("SUCCESS");
				           alipayAPIResponse.setPgIsSuccess("T");
						}
						else
						{
							alipayAPIResponse.setPgResultCode("DECLINED");
					    	   alipayAPIResponse.setPgIsSuccess("F");
						}
				       alipayAPIResponse.setMcReference(poli.getReference());
				       alipayAPIResponse.setPoli_payerAcc_number(PayerAcctNumber);
				       alipayAPIResponse.setPoli_payerAcc_sortCode(PayerAcctSortCode);
				       alipayAPIResponse.setBank_name(FinancialInstitutionName);
				       alipayAPIResponse.setPoli_trans_ref_no(TransactionRefNo);
				       alipayAPIResponse.setPgAlipayTransId("");
				       alipayAPIResponse.setMethod_type("POLi");
				       
				      alipayAPIService.saveOrUpdate(alipayAPIResponse);
				       
					System.out.println("Response Code : " + PayerAccountSuffix);
					model.addAttribute("PayerAcctNumber", PayerAcctNumber);
					model.addAttribute("MerchantAcctNumber", MerchantAcctNumber);
					model.addAttribute("MerchantAcctName", MerchantAcctName);
					model.addAttribute("TransactionRefNo", TransactionRefNo);
					model.addAttribute("CurrencyCode", CurrencyCode);
					model.addAttribute("PaymentAmount", PaymentAmount);
					model.addAttribute("AmountPaid", AmountPaid);
					model.addAttribute("BankReceipt", BankReceipt);
					model.addAttribute("BankReceiptDateTime", BankReceiptDateTime);
					model.addAttribute("TransactionStatusCode", TransactionStatusCode);
					model.addAttribute("ErrorCode", ErrorCode);
					model.addAttribute("ErrorMessage", ErrorMessage);
					model.addAttribute("FinancialInstitutionCode", FinancialInstitutionCode);
					model.addAttribute("FinancialInstitutionName", FinancialInstitutionName);
					model.addAttribute("MerchantReference", MerchantReference);
					//merchantResponse.set
					model.addAttribute("PayerFirstName", PayerFirstName);
					model.addAttribute("PayerFamilyName", PayerFamilyName);

					model.addAttribute("merchant_unique_reference",poli.getPoli_merchant_reference());
					}
					else
					{
						String TransactionStatusCode = "Cancelled";
						request.setAttribute("TransactionStatusCode", TransactionStatusCode);
						model.addAttribute("TransactionStatusCode", TransactionStatusCode);
					}
					// Dc.storeTransaction(bean);
				} catch (MalformedURLException e) {

					e.printStackTrace();
				} catch (JsonParseException e) {

					e.printStackTrace();
				} catch (JsonMappingException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}

			
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {

		}
		
	     
	        
	        model.addAttribute("return_url",poli.getPoli_success_url());
		return "/DynamicResponsePoli";

	}
	@RequestMapping(value = "/dpsResponse")
	public ModelAndView DPSResponse(@ModelAttribute("shoppingCart") @Validated AlipayWalletVO shoppingCart,BindingResult result, Model model, final RedirectAttributes redirectAttributes, HttpSession session,
			HttpServletRequest req, HttpServletResponse response) {
		AlipayWalletVO DPBean = new AlipayWalletVO();
		Object userid =  req.getSession().getAttribute("userId");;
		DPBean.setUser_id(userid.toString());
		
		AlipayWalletVO dps = dashboardService.getDPSRecord(DPBean);
		String PxPayUrl = dps.getPxPayUrl();//"https://uat.paymentexpress.com/pxaccess/pxpay.aspx";
		String PxPayUserId = dps.getPxPayUserId();//"AhuraConsulting_Dev";
		String PxPayKey =dps.getPxPayKey();// "78efe7aad82db9354675fea0c9fa9484d5cdeaeee1be6ad90939ce74f8c14c98";
		com.dynamicpayment.paymentexpress.Response responseBean = new com.dynamicpayment.paymentexpress.Response();
		try {
			
		    responseBean = PxPay.ProcessResponse(PxPayUserId, PxPayKey, req.getParameter("result"), PxPayUrl);
		   
		    String transactionID = responseBean.getMerchantReference();
		    dps.setAlipayTransactionId(transactionID);		    
		    AlipayAPIResponse transactionBean = paymentService.getTransactionDEtialsById(dps);
		    
			transactionBean.setMerchantTransactionId(transactionID);
		    transactionBean.setPgAlipayTransId(responseBean.getDpsTxnRef());
		    
		    transactionBean.setMethod_type(DynamicPaymentConstant.DPS_METHOD);
			
		    transactionBean.setCardType(responseBean.getCardName());
		      transactionBean.setPgTransactionDate(new Date());
		      /*  if(responseBean.getCardName().equals("Amex"))
		        {
		        	transactionBean.setCard_type(1);
		        	transactionBean.setPayment_type(2);
		        }
		        else
		        {
			        if(responseBean.getCardName().equals("Visa"))
			        {
			        	 transactionBean.setCard_type(4);
			        	 transactionBean.setPayment_type(1);
			        }
			        else
			        {
				        if(responseBean.getCardName().equals("MasterCard"))
				        {
				        	transactionBean.setCard_type(3);
				        	transactionBean.setPayment_type(7);
				        }
				        else
				        {
					        if(responseBean.getCardName().equals("Diners"))
					        {
					        	transactionBean.setCard_type(2);
					        	transactionBean.setPayment_type(3);
					        }
					        else
					        {
					        	transactionBean.setCard_type(0);
					        	transactionBean.setPayment_type(0);
					        }
				        }
			        }
		        }*/
		        
		        String Status = responseBean.getSuccess();

				if(Status.equals("0"))
				{
					transactionBean.setStatus(DynamicPaymentConstant.TRANSACTION_SUCCESSFULL);
					transactionBean.setPgResultCode(DynamicPaymentConstant.TRANSACTION_PG_SUCCESSFULL);
					transactionBean.setPgIsSuccess("T");
					//Status =" Transaction Declined";
				}   
				else
				{
					transactionBean.setStatus(DynamicPaymentConstant.TRANSACTION_DECLINED);
					transactionBean.setPgResultCode(DynamicPaymentConstant.TRANSACTION_PG_DECLINED);
					transactionBean.setPgIsSuccess("F");
				}
				
				System.out.println("DPS resposne:"+responseBean.toString());
				
				JSONObject res = new JSONObject();
				res.put("Amount", responseBean.getAmountSettlement());
				res.put("auth_code", responseBean.getAuthCode());
				res.put("billingID", responseBean.getDpsBillingId());			
				res.put("cardHolderName", responseBean.getCardHolderName());
				res.put("card_name", responseBean.getCardName());
				res.put("card_number", responseBean.getCardNumber());
				res.put("Cardnumber2", responseBean.getCardNumber2());
				res.put("client_info", responseBean.getClientInfo());
				res.put("currency", responseBean.getCurrencyInput());
				res.put("amountset", responseBean.getAmountSettlement());
				res.put("taxRef", responseBean.getDpsTxnRef());
				res.put("email", responseBean.getEmailAddress());
				res.put("merRef", responseBean.getMerchantReference());
				res.put("resp_text", responseBean.getResponseText());
				res.put("success", responseBean.getSuccess());
				res.put("txn1", responseBean.getTxnData1());
				res.put("txn2", responseBean.getTxnData2());
				res.put("txn3", responseBean.getTxnData3());
				res.put("txnID", responseBean.getTxnId());
				res.put("txnMac", responseBean.getTxnMac());
				res.put("expiry_date", responseBean.getDateExpiry());
				res.put("trans_type", responseBean.getTxnType());
				res.put("merchant_unique_reference",transactionBean.getMerchantTransactionId());
						
			alipayAPIService.saveOrUpdate(transactionBean);
			
			
			model.addAttribute("Amount", responseBean.getAmountSettlement());
			model.addAttribute("auth_code", responseBean.getAuthCode());
			model.addAttribute("billingID", responseBean.getDpsBillingId());
			
			
			model.addAttribute("cardHolderName", responseBean.getCardHolderName());
			model.addAttribute("card_name", responseBean.getCardName());
			model.addAttribute("card_number", responseBean.getCardNumber());
			model.addAttribute("Cardnumber2", responseBean.getCardNumber2());
			model.addAttribute("client_info", responseBean.getClientInfo());
			model.addAttribute("currency", responseBean.getCurrencyInput());
			model.addAttribute("amountset", responseBean.getAmountSettlement());
			model.addAttribute("taxRef", responseBean.getDpsTxnRef());
			model.addAttribute("email", responseBean.getEmailAddress());
			model.addAttribute("merRef", responseBean.getMerchantReference());
			model.addAttribute("resp_text", responseBean.getResponseText());
			model.addAttribute("success", responseBean.getSuccess());
			model.addAttribute("txn1", responseBean.getTxnData1());
			model.addAttribute("txn2", responseBean.getTxnData2());
			model.addAttribute("txn3", responseBean.getTxnData3());
			model.addAttribute("txnID", responseBean.getTxnId());
			model.addAttribute("txnMac", responseBean.getTxnMac());
			model.addAttribute("expiry_date", responseBean.getDateExpiry());
			model.addAttribute("trans_type", responseBean.getTxnType());
			model.addAttribute("merchant_unique_reference",transactionBean.getMerchantTransactionId());
			model.addAttribute("transaction_status",transactionBean.getStatus());
			 
			//model.addAttribute("expDate", "");
		
			model.addAttribute("return_url",dps.getDPSsuccess_url());		
			
		} catch (Exception e) {
		    e.printStackTrace();
		}
			ModelAndView view = new ModelAndView("DPSResponse");
			return view;
	}
	@RequestMapping(value = "/gateway")
	public ModelAndView Gateways(@ModelAttribute("shoppingCart") @Validated AlipayWalletVO shoppingCart,BindingResult result, ModelAndView model, final RedirectAttributes redirectAttributes, HttpSession session,
			HttpServletRequest req, HttpServletResponse response) {
		model.setViewName("DPGPortal");
		return model;
		
	}

	
}
