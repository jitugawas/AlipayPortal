package com.payitnz.web;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dynamicpayment.paymentexpress.PxPay;

import com.payitnz.config.DynamicPaymentConstant;

import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayWalletVO;

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
	
	
	@RequestMapping(value = "/callPayemntApi", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView saveContact(@ModelAttribute(value="ShoppingCart") AlipayWalletVO alipayWalletVO, HttpServletResponse response,BindingResult result,
			HttpSession session, Model model, HttpServletRequest request) throws Exception {
		Object userid =  request.getSession().getAttribute("user_id");
		
		ModelAndView view = new ModelAndView("Flo2CashForm");
		System.out.println("gateway"+alipayWalletVO.getGateway());
		//get form data 
		 String ipAddress = request.getRemoteAddr();
	     String sender = request.getRemoteHost();
	     
	     System.out.println(alipayWalletVO.getAmount());
		 System.out.println(alipayWalletVO.getReference());
		 System.out.println(alipayWalletVO.getInfidigiAccountId());
		 System.out.println(alipayWalletVO.getParticular());
	     
		//check account details
		 alipayWalletVO.setUser_id(alipayWalletVO.getInfidigiAccountId());
		 if(alipayWalletVO.getGateway().equals("Alipay Online"))
		 {
			AlipayWalletVO info = userService.getAlipayOnlineRecord(alipayWalletVO);
		    if(info != null)
		    {
		    	//save details in session
		    	request.getSession().setAttribute("accountId",alipayWalletVO.getInfidigiAccountId());
		    	
		    	//save transaction details
		    	
		    	//perform transaction
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
				 AlipayWalletVO f2c = dashboardService.getF2CRecord(alipayWalletVO);
				 model = alipayAPIService.processF2C(alipayWalletVO, model, f2c, ipAddress, sender, request);
					return new ModelAndView("redirect:" + DynamicPaymentConstant.FLO2CASHURL);
			 }
			 
			 if(alipayWalletVO.getGateway().equals("DPS"))
			 {
				 AlipayWalletVO dps = dashboardService.getDPSRecord(alipayWalletVO);
				String redirecturl = alipayAPIService.processDPS(alipayWalletVO, model, dps, ipAddress, sender, request);
					return new ModelAndView("redirect:" + redirecturl);
			 }
	   
		 }
     return view;
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
					//Status =" Transaction Declined";
				}   
				else
				{
					transactionBean.setStatus(DynamicPaymentConstant.TRANSACTION_DECLINED);
					transactionBean.setPgResultCode(DynamicPaymentConstant.TRANSACTION_PG_DECLINED);
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
	
}
