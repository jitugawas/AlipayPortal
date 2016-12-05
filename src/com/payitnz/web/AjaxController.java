package com.payitnz.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.annotation.JsonView;
import com.payitnz.config.DynamicPaymentConstant;
import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.GenericAPIResponse;
import com.payitnz.service.AlipayAPIService;
import com.payitnz.service.DashboardService;
import com.payitnz.service.UserService;

@RestController
public class AjaxController {
	
	@Autowired
	private DashboardService dashboardService;
	 private UserService userService;
	 @Autowired
	    public void setUserAPIService(UserService userService) {
	        this.userService = userService;
	    }
	@Autowired
	public void setDashboardService(DashboardService dashboardService){
		this.dashboardService = dashboardService;
	}
	
	@Autowired
	private AlipayAPIService alipayAPIService;
	 GenericAPIResponse genericAPIResponse ;
	@Autowired
	public void setUserService(AlipayAPIService alipayAPIService) {
		this.alipayAPIService = alipayAPIService;
	}
	final static Logger logger = Logger.getLogger(AjaxController.class);
	Calendar calendar = Calendar.getInstance();
	java.util.Date now = calendar.getTime();
	java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//check user password
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/checkUserPassword")
	public JSONArray checkUserPassword(RedirectAttributes redirectAttributes,HttpServletRequest request){
		List<Object> validationStatus = new ArrayList<Object>();
		boolean status = false;
		if(request.getParameter("fieldValue") == null ||request.getParameter("fieldValue").equals("")){
			status = true;
		}else{
			try {
				status = dashboardService.checkUserPassword(DynamicPaymentConstant.getHashPassword(request.getParameter("fieldValue")));
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		validationStatus.add("current_psswd");
		validationStatus.add(status);	
		JSONArray json1 = new JSONArray(validationStatus);
		return json1;
	}
	
	@JsonView(Views.Public.class)
	  @RequestMapping("/Transaction ")
	  public  Map<Integer,Map<String,Object>> GetTransaction( HttpServletRequest request) {
	   //AlipayAPIResponse genericAPIResponse = new AlipayAPIResponse();
	   String ipAddress = request.getRemoteAddr();
	   String sender = request.getRemoteHost();
	   System.out.println(request.getParameter("pgPartnerTransId"));
	   DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	   Map<Integer,Map<String,Object>> bigMap = new HashMap<Integer,Map<String,Object>>();
	   Map<String,Object> map = new LinkedHashMap<String,Object>();
	   // Map<Object, Object> map = new HashMap<Object, Object>();
	   List<Object> validationStatus = new ArrayList<Object>();
	   
	   AlipayAPIResponse genericAPIRespons = alipayAPIService.getTransactionDetails(request.getParameter("pgPartnerTransId"));
	   
	   if (genericAPIRespons != null) {
		   
		   map.put("id", genericAPIRespons.getId());
		    map.put("channel", genericAPIRespons.getMethod_type());
		    //   map.put("pgPartnerTransId",trans.getPgPartnerTransId());
		    if(genericAPIRespons.getPgAlipayPayTime() != null)
		     try {
		      map.put("TransactionDate", format.format(dateFormat.parse(genericAPIRespons.getPgAlipayPayTime())));
		     } catch (ParseException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		     }
		    map.put("status", genericAPIRespons.getPgResultCode());
		   
		    //   map.put("pgMerchantTransactionId", trans.getPgMerchantTransactionId());
		    //   map.put("mcParticular",trans.getMcItemName());
		    map.put("mcReference",genericAPIRespons.getMcReference());
		    map.put("latlong",genericAPIRespons.getMcLatitude()+","+genericAPIRespons.getMcLongitude());
		    map.put("mcTransAmount",genericAPIRespons.getMcTransAmount());
		    validationStatus.add(map);
		 
		   List<AlipayAPIResponse> list = alipayAPIService.getTransactionsOfID(request.getParameter("pgPartnerTransId"));
		   
		   
		   int count=1;
		   for (AlipayAPIResponse genericAPIResponse : list) {
			   if(genericAPIResponse.getPgResultCode().equals("REFUND"))
			   {
				  // String first = 
				   String amt1 = genericAPIRespons.getMcTransAmount();
				   String amt2 = genericAPIResponse.getMcTransAmount();
				   double a=Double.parseDouble(""+amt1);
				   double b=Double.parseDouble(""+amt2);
				   double amt = a-b;
				   
				   //genericAPIRespons.setMcTransAmount(Double.toString(amt));
				//   map.put("mcTransAmount",genericAPIRespons.getMcTransAmount());
				   map.put("remainingAmt", amt);
				  
			   }
			   else
			   {
				   map.put("remainingAmt", genericAPIRespons.getMcTransAmount());
				   
			   }
			   bigMap.put(0,map);
			   map=new LinkedHashMap<String,Object>();
			   map.put("id", genericAPIResponse.getId());
			    map.put("channel", genericAPIResponse.getMethod_type());
			    //   map.put("pgPartnerTransId",trans.getPgPartnerTransId());
			    if(genericAPIResponse.getPgAlipayPayTime() != null)
			     try {
			      map.put("TransactionDate", format.format(dateFormat.parse(genericAPIResponse.getPgAlipayPayTime())));
			     } catch (ParseException e) {
			      // TODO Auto-generated catch block
			      e.printStackTrace();
			     }
			    map.put("status", genericAPIResponse.getPgResultCode());
			    map.put("mcTransAmount",genericAPIResponse.getMcTransAmount());
			    //   map.put("pgMerchantTransactionId", trans.getPgMerchantTransactionId());
			       map.put("mcParticular",genericAPIResponse.getMcItemName());
			    map.put("mcReference",genericAPIResponse.getMcReference());
			    map.put("latlong",genericAPIResponse.getMcLatitude()+","+genericAPIResponse.getMcLongitude());
			     
			    validationStatus.add(map);
			    bigMap.put(count,map);
			    count++;
			    
		}
	    
		   System.out.println("000000000"+bigMap.get(0));
	   } 

	   return bigMap;
	  }

	@JsonView(Views.Public.class)
	  @RequestMapping("/webCancellation")
	    public Map<String,Object> webCancellation(HttpServletRequest request) {
	        //GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
	    	System.out.println("in webCancellation");
	    	AlipayWalletVO alipayWallet = new AlipayWalletVO();
	    	System.out.println(request.getParameter("pgPartnerTransId"));
	    	System.out.println(request.getParameter("amount"));
	    	
	    	//ModelAndView view = new ModelAndView("alipayTransactions");
	    	 Map<String,Object> map = new LinkedHashMap<String,Object>();
	        String ipAddress = request.getRemoteAddr();
	        String sender = request.getRemoteHost();
	        alipayWallet.setPgPartnerTransId(request.getParameter("pgPartnerTransId"));
	        alipayWallet.setAmount(request.getParameter("amount"));
			        HttpSession session = request.getSession();
			  	  Object infidigiuserId = session.getAttribute("user_id");
			  	 Object infidigiAccountId = session.getAttribute("merchantId");
			  	 
			  	alipayWallet.setUser_id(infidigiuserId.toString());
			  	AlipayWalletVO alipayWalletVO = userService.getAlipayRecord(alipayWallet);
			  	if(alipayWalletVO != null)
			  	{
			  	 AlipayAPIResponse transDetails = alipayAPIService.getTransactionDetails(alipayWallet.getPgPartnerTransId());        
			        if(transDetails != null)
			        {
			        	System.out.println("id"+transDetails.getId());
			        	alipayWalletVO.setId(transDetails.getId());
			        }
			  	alipayWalletVO.setAmount(alipayWallet.getAmount());
			  	alipayWalletVO.setMerchantTransactionId(alipayWallet.getPgPartnerTransId());
	            Object returnParams[] = alipayAPIService.createCancelTransaction(alipayWalletVO, ipAddress, sender);
	            genericAPIResponse = (GenericAPIResponse) returnParams[1];
	            String merchantCompany = alipayAPIService.getMerchantName(infidigiAccountId.toString(), infidigiuserId.toString());
	            
	            
	            genericAPIResponse.setMerchantCompany(merchantCompany);
	                    System.out.println("---TimerTask started - cancelation()");
	                    
	                    AlipayAPIResponse res = alipayAPIService.callAlipayCancelAPIWeb(returnParams[0], alipayWalletVO, ipAddress, sender);
	                    System.out.println("success value"+res.getPgIsSuccess());
	                    if(res.getPgIsSuccess().equals("F"))
	                    {
	                    	 map.put("msg","Cancellation failed.");
//	                    	result.addError(new ObjectError("error", "Something is wrong"));
//	                    	return view;
	                    }
	                    
	                    transDetails = alipayAPIService.getTransactionDetails(alipayWallet.getPgPartnerTransId());  
	                    if(transDetails != null)
	                    {
	                    	if(transDetails.getPgResultCode().equals("CANCELLED"))
								{
	                    		map.put("msg","Transaction Cancelled Successfully.");
								}
	                    }
			  	}
			  	else
			  	{
			  		map.put("msg","Merchant not Configured.");
			  	}
	        return map;

	    }
	
	@RequestMapping("/webRefund ")
    public Map<String,Object> WebRefund(AlipayWalletVO alipayWallet, HttpServletRequest request) {
        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        AlipayAPIResponse alipayAPIResponse = new AlipayAPIResponse();
        
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
        HttpSession session = request.getSession();
        Map<String,Object> map = new LinkedHashMap<String,Object>();
	    Object infidigiAccountId = session.getAttribute("user_id");
	  	 alipayWallet.setUser_id(infidigiAccountId.toString());
	  	 AlipayWalletVO alipayWalletVO = userService.getAlipayRecord(alipayWallet);
	  	// logger.info("log for transactions");
	  	if(alipayWalletVO != null)
	  	{
	  		String pgTrnasctionId = request.getParameter("pgPartnerTransId");
	  		AlipayAPIResponse transDetails = alipayAPIService.getTransactionDetails(pgTrnasctionId);        
	        
	  		if(transDetails != null)
	        {
	        	System.out.println("id"+transDetails.getId());
	        	alipayWalletVO.setId(transDetails.getId());
	        }
	        
	  		
	  		String reference = request.getParameter("reference");
            String currency = transDetails.getMcCurrency();//request.getParameter("currency");
            String total_fee= request.getParameter("total_fee");
            
            
	  		alipayWalletVO.setPgPartnerTransId(request.getParameter("pgPartnerTransId"));
	        alipayWalletVO.setAmount(request.getParameter("amount"));
	        alipayWalletVO.setReference(request.getParameter("reference"));
	        alipayWalletVO.setStatus(request.getParameter("status"));
	        alipayWalletVO.setRefundReason(request.getParameter("refundReason"));

        
	    	alipayWalletVO.setUser_id(session.getAttribute("user_id").toString());
	    	alipayWalletVO.setInfidigiAccountId(session.getAttribute("user_id").toString());
            Object returnParams[] = alipayAPIService.createRefundTransaction(alipayWalletVO, ipAddress, sender);
            genericAPIResponse = (GenericAPIResponse) returnParams[1];
            String merchantCompany = alipayAPIService.getMerchantName(alipayWalletVO.getInfidigiAccountId(), alipayWalletVO.getInfidigiUserId());
           
            System.out.println("---TimerTask started");
            AlipayAPIResponse ali = alipayAPIService.callAlipayRefundAPIWeb(returnParams[0], alipayWalletVO, ipAddress, sender);
            map.put("msg",ali.getMessage());
            genericAPIResponse = new GenericAPIResponse();
            genericAPIResponse.setStatusCode("101");
            genericAPIResponse.setMessage("Unauthorised user");
            
            alipayAPIResponse.setRequestBy(sender);
            alipayAPIResponse.setIpAddress(ipAddress);
            alipayAPIResponse.setInfidigiUserId(session.getAttribute("user_id").toString());

            Calendar cal=Calendar.getInstance();
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            
            String curDate = "" + day + "/" + month + "/" + year;
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:MM:ss");

           System.out.println("status==="+alipayAPIResponse.getStatusCode());
            alipayAPIResponse.setAmount(request.getParameter("amount"));
            alipayAPIResponse.setPgTransactionDate(cal.getTime());
            alipayAPIResponse.setRequestTime(new Date().toString());
            System.out.println("transaction id in 1st api"+alipayAPIResponse.getPgPartnerTransId()+cal.getTime());
          //  alipayWalletVO.setPgPartnerTransId(alipayAPIResponse.getPgPartnerTransId());
            alipayAPIResponse.setPgPartnerTransId(pgTrnasctionId);
            
            System.out.println("statsu:"+genericAPIResponse.getStatus()+" sttus code:"+genericAPIResponse.getStatusCode());
            if(alipayAPIResponse.getStatusCode().equals("SUCCESS"))
            {
	            alipayAPIResponse.setPgResultCode("REFUND");
	           // alipayAPIResponse.setPgAlipayTransStatus(DynamicPaymentConstant.TRANSACTION_REFUND);
	            alipayAPIResponse.setPgIsSuccess("T");
            }
            else
            {
	         	   alipayAPIResponse.setPgResultCode("FAILED");
	         	 // alipayAPIResponse.setPgAlipayTransStatus(DynamicPaymentConstant.TRANSACTION_FAILED);
	         	   alipayAPIResponse.setPgIsSuccess("F");
            }
                                    
//            alipayAPIResponse.setMcReference(reference);
//           
//            alipayAPIResponse.setPgAlipayTransId( genericAPIResponse.getMerchantTransactionId());
//            alipayAPIResponse.setMerchantRefundId(genericAPIResponse.getMerchantRefundId());
//            alipayAPIResponse.setTransaction_type(DynamicPaymentConstant.TRANSACTION_REFUND);
//            alipayAPIResponse.setDyMerchantId(session.getAttribute("user_id").toString());
//            alipayAPIResponse.setRemark("");
//            alipayAPIResponse.setPgAlipayBuyerUserId("");
//            alipayAPIResponse.setPgAlipayPayTime("");
//            alipayAPIResponse.setMcCurrency(currency);
//            alipayAPIResponse.setMcTransAmount(total_fee);
//            alipayAPIResponse.setRequestTime(new Date().toString());
//            alipayAPIResponse.setMethod_type("Online");
//            
//            alipayAPIService.saveOrUpdate(alipayAPIResponse);
	  	}
	  	else
	  	{
	  		map.put("msg","Merchant not Configured.");
	  	}
        return map;

    }

	@RequestMapping(value = "/listGateways")
	ModelAndView listGateways(@ModelAttribute(value="shoppingCart") AlipayWalletVO alipayWalletVO, HttpServletRequest request) throws IOException {
		 Map<Integer,Map<String,Object>> bigMap = new HashMap<Integer,Map<String,Object>>();
		 ModelAndView model = new ModelAndView("DPGPortal");
		  //AlipayWalletVO alipayWalletVO = new AlipayWalletVO();
		  List<String> paymentMethods = new ArrayList<String>();
		  alipayWalletVO.setInfidigiAccountId( request.getParameter("infidigiAccountId"));
		  alipayWalletVO.setUser_id(alipayWalletVO.getInfidigiAccountId());
		  AlipayWalletVO dps = dashboardService.getDPSRecord(alipayWalletVO);
		  int count=1;
		  if(dps != null)
		  {
			  Map<String,Object> map = new LinkedHashMap<String,Object>();
			  map.put("id", count);
			  paymentMethods.add("Payment Express(DPS)");
			    bigMap.put(count, map);
			    count++;
		  }
		  
		  AlipayWalletVO f2c = dashboardService.getF2CRecord(alipayWalletVO);
		  if(f2c != null)
		  {
			  Map<String,Object> map = new LinkedHashMap<String,Object>();
			  map.put("id", count);
			  paymentMethods.add("Flo2Cash");
			    bigMap.put(count, map);
			    count++;
		  }
		  
		  AlipayWalletVO poli = dashboardService.getPoliRecord(alipayWalletVO);
		  if(poli != null)
		  {
			  Map<String,Object> map = new LinkedHashMap<String,Object>();
			  map.put("id", count);
			  paymentMethods.add("POLi");
			    bigMap.put(count, map);
			    count++;
		  }
		  AlipayWalletVO cup = dashboardService.getCUPRecord(alipayWalletVO);
		  if(cup != null)
		  {
			  Map<String,Object> map = new LinkedHashMap<String,Object>();
			  map.put("id", count);
			  paymentMethods.add("CUP");
			    bigMap.put(count, map);
			    count++;
		  }
		  AlipayWalletVO alipayOnline = userService.getAlipayOnlineRecord(alipayWalletVO);
		  
		  if(alipayOnline != null)
		  {
			  Map<String,Object> map = new LinkedHashMap<String,Object>();
			  map.put("id", count);
			  paymentMethods.add( "Alipay Online");
			    bigMap.put(count, map);
			    count++;
		  }
		  model.addObject("paymentMethods", paymentMethods);
		 
		 model.addObject("amount",alipayWalletVO.getAmount());
		  System.out.println("000000000"+bigMap.get(1));
		return  model;
	}
	
}
