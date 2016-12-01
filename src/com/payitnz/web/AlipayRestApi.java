package com.payitnz.web;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.common.gis.Coordinate;
import com.mkyong.common.MailMail;
import com.payitnz.config.DynamicPaymentConstant;
import com.payitnz.model.AlipayAPIRequest;
import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.GenericAPIResponse;
import com.payitnz.model.User;
import com.payitnz.service.AlipayAPIService;
import com.payitnz.service.UserService;

@RestController
public class AlipayRestApi {

    private AlipayAPIService alipayAPIService;
    private UserService userService;
    protected  Coordinate center = new Coordinate();
    final static Logger logger = Logger.getLogger(LoginController.class);
    GenericAPIResponse genericAPIResponse ;
    static {
        TimeZone.setDefault(TimeZone.getTimeZone("NZ")); // setting New Zealand time zone
    }

    @Autowired
    public void setAlipayAPIService(AlipayAPIService alipayAPIService) {
        this.alipayAPIService = alipayAPIService;
    }
    @Autowired
    public void setUserAPIService(UserService userService) {
        this.userService = userService;
    }
   
    @RequestMapping("/login")
    public GenericAPIResponse login(@RequestBody AlipayWalletVO alipayWalletVO, HttpServletRequest request) {
        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
        boolean isValidUser = alipayAPIService.authenticateUser(alipayWalletVO, ipAddress, sender, genericAPIResponse);
        if (isValidUser) {
        	       	
        	 logger.info(alipayWalletVO.getUser_id()+"have logged in at" + new Date());
            genericAPIResponse.setStatusCode("202");
            genericAPIResponse.setMessage("Login successful");
            User user= new User();
            user.setId(genericAPIResponse.getUserID());
            user.setInfidigiPassword(alipayWalletVO.getInfidigiPassword());
        	alipayAPIService.UpdateUser(user);

        } else {

            genericAPIResponse.setStatusCode("101");
            genericAPIResponse.setMessage("Unauthorised user");
        }
        return genericAPIResponse;

    }

    @RequestMapping("/logout")
    public GenericAPIResponse logout(@RequestBody AlipayWalletVO alipayWalletVO, HttpServletRequest request) {
        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
        boolean isValidUser = alipayAPIService.authenticateUser(alipayWalletVO, ipAddress, sender, genericAPIResponse);
        if (isValidUser) {
        	 logger.info(alipayWalletVO.getUser_id()+"have logged in at" + new Date());
            genericAPIResponse.setStatusCode("203");
            genericAPIResponse.setMessage("Logout successful");

        } else {

            genericAPIResponse.setStatusCode("101");
            genericAPIResponse.setMessage("Unauthorised user");
        }
        return genericAPIResponse;

    }

    @RequestMapping("/payment")
    public GenericAPIResponse payment(@RequestBody AlipayWalletVO alipayWalletVO, HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    	
    	GenericAPIResponse genericAPIResponse=new GenericAPIResponse();
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
       
        boolean isValidUser = alipayAPIService.authenticateUser(alipayWalletVO, ipAddress, sender, genericAPIResponse);
        if (isValidUser) {
        	
        	alipayWalletVO.setUser_id(alipayWalletVO.getInfidigiAccountId());
        	AlipayWalletVO info = userService.getAlipayRecord(alipayWalletVO);
        	if(info != null)
        	{
        	alipayWalletVO.setCharSet(info.getCharSet());
        	alipayWalletVO.setPayitnz_id(info.getPayitnz_id());
        	alipayWalletVO.setPartner_key(info.getPartner_key());
        	alipayWalletVO.setCurrency(info.getCurrency());
        	alipayWalletVO.setService(info.getService());
            Object returnParams[] = alipayAPIService.createPaymentTransaction(alipayWalletVO, ipAddress, sender);
            genericAPIResponse = (GenericAPIResponse) returnParams[1];
            String merchantCompany = alipayAPIService.getMerchantCompanyName(alipayWalletVO.getInfidigiAccountId(), alipayWalletVO.getUser_id());
            genericAPIResponse.setMerchantCompany(merchantCompany);

            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                 
                @Override
                public void run() {
                    System.out.println("---TimerTask started - payment()");
                    int counter = 0;
                    boolean isTransactionCompleted = alipayAPIService.callAlipayPaymentAPI(returnParams[0], alipayWalletVO, ipAddress, sender);
                    
                    if(!isTransactionCompleted) {
                        
                        while(counter < 8) {
                            counter++;
                            try {
                                Thread.sleep(4000);//wait for 4 seconds
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Object returnParams[] = alipayAPIService.createQueryTransaction(alipayWalletVO, ipAddress, sender);
                            AlipayAPIResponse alipayAPIResponse = alipayAPIService.callAlipayQueryAPI(returnParams[0], alipayWalletVO, ipAddress, sender);
                            if(!alipayAPIResponse.getPgResultCode().equalsIgnoreCase(DynamicPaymentConstant.PG_UNKNOW_STATUS)) {
                                break;//break the while loop because now the status of transaction is known
                            }
                        }
                        if(counter == 8) { //tried the QUERY api 8 times so now reverse the transaction
                            //call Alipay reverse API
                            Object returnParams[] = alipayAPIService.createReverseTransaction(alipayWalletVO, ipAddress, sender);
                            AlipayAPIResponse alipayAPIResponse = alipayAPIService.callAlipayReverseAPI(returnParams[0], alipayWalletVO, ipAddress, sender);
                            if(alipayAPIResponse.getPgResultCode().equalsIgnoreCase("")) {
                                alipayAPIResponse = alipayAPIService.callAlipayReverseAPI(returnParams[0], alipayWalletVO, ipAddress, sender);
                                if(alipayAPIResponse.getPgResultCode().equalsIgnoreCase(""))
                                		{
                                    //alipayAPIResponse = XmlParser.parseAlipayReversalAPIResponse(responseAlipay.toString());
                       
                                	alipayAPIResponse.setId(0);
                                    alipayAPIResponse.setRequestBy(sender);
                                    alipayAPIResponse.setIpAddress(ipAddress);
                                    alipayAPIResponse.setRequestTime(new Date().toString());
                                    alipayAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
                                    alipayAPIResponse.setPgResultCode("UNKNOWN");
                                    alipayAPIResponse.setPgPartnerTransId(alipayWalletVO.getPgPartnerTransId());
                                    alipayAPIResponse.setTransaction_type("7");
                                    alipayAPIResponse.setMethod_type("Alipay Offline");
                                    alipayAPIResponse.setRemark("");
                                    alipayAPIResponse.setDyMerchantId(alipayWalletVO.getInfidigiAccountId());
                                    
                                    alipayAPIService.saveOrUpdate(alipayAPIResponse);
                                			
                                		}
                              
                            }
                        }
                    }
                }
                

            }, new Date());
        }
        	else
        	{
        		genericAPIResponse = new GenericAPIResponse();
                genericAPIResponse.setStatusCode("101");
                genericAPIResponse.setMessage("Not registered merchant");
        	}
        } else {
            genericAPIResponse = new GenericAPIResponse();
            genericAPIResponse.setStatusCode("101");
            genericAPIResponse.setMessage("Unauthorised user");
        }
    
        return genericAPIResponse;

    }
    @SuppressWarnings("unused")
	@RequestMapping("/map_transactions")
    public GenericAPIResponse ListTransactionsOnMap(@RequestBody AlipayWalletVO alipayWalletVO, HttpServletRequest request) {
    	 GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
    	List <GenericAPIResponse> genericList = new ArrayList<>();
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
        genericAPIResponse.setInfidigiUserId(alipayWalletVO.getInfidigiUserId());
        boolean isValidUser = alipayAPIService.authenticateUser(alipayWalletVO, ipAddress, sender, genericAPIResponse);
        if (isValidUser) {
        	
        	double lat = Double.parseDouble(alipayWalletVO.getLatitude());
        	double lon = Double.parseDouble(alipayWalletVO.getLongitude());
        	 List<AlipayAPIRequest> apr = alipayAPIService.getTransactionListOnMap(alipayWalletVO, ipAddress, sender);
        	 if(apr != null)
        	 {
        		 
        	 for (AlipayAPIRequest alipayAPIReq : apr) {
        		 GenericAPIResponse obj = new GenericAPIResponse();
        		 
					if (alipayWalletVO.getType().equals("AllTransactions")) {

						List<User> users = alipayAPIService.getUserList(alipayWalletVO.getInfidigiAccountId());

						for (User user : users) {
							double lat1;
							double lon1;
							if(alipayAPIReq.getMcLatitude() != null && alipayAPIReq.getMcLongitude() != null && !(alipayAPIReq.getMcLatitude().equals("")) && !(alipayAPIReq.getMcLongitude().equals("")) && !(alipayAPIReq.getMcLatitude().equals(" ")) && !(alipayAPIReq.getMcLongitude().equals(" ")))
							{
							 lat1 = Double.parseDouble(alipayAPIReq.getMcLatitude());
			        		  lon1 =  Double.parseDouble(alipayAPIReq.getMcLongitude());
							}
							else
							{
								 lat1 = 0;
								 lon1 =  0;
								
							}
						
							double cn = Coordinate.distFrom(lat1, lon1, lat, lon);
							double distance = 10000;
							if (cn <= distance) {
								AlipayAPIResponse details = alipayAPIService
										.getTransactionDetails(alipayAPIReq.getMcPartnerTransId());
								if (details != null) {
									if (user.getInfidigiUserId().equals(details.getInfidigiUserId())) {

										obj.setStatus(details.getPgResultCode());
										obj.setInfidigiUserId(details.getInfidigiUserId());
										obj.setMerchantTransactionId(alipayAPIReq.getMcPartnerTransId());
										obj.setLatitude(alipayAPIReq.getMcLatitude());
										obj.setLongitude(alipayAPIReq.getMcLongitude());
										genericList.add(obj);
										break;
									}

								}
							}
						}

					}
					else {

						double lat1;
						double lon1;
						if(alipayAPIReq.getMcLatitude() != null && alipayAPIReq.getMcLongitude() != null && !(alipayAPIReq.getMcLatitude().equals("")) && !(alipayAPIReq.getMcLongitude().equals("")) && !(alipayAPIReq.getMcLatitude().equals(" ")) && !(alipayAPIReq.getMcLongitude().equals(" ")))
						{
						 lat1 = Double.parseDouble(alipayAPIReq.getMcLatitude());
		        		  lon1 =  Double.parseDouble(alipayAPIReq.getMcLongitude());
						}
						else
						{
							 lat1 = 0;
							 lon1 =  0;
							
						}
						
						double cn = Coordinate.distFrom(lat1, lon1, lat, lon);
						double distance = 10000;
						if (cn <= distance) {
						AlipayAPIResponse details = alipayAPIService
								.getTransactionDetails(alipayAPIReq.getMcPartnerTransId());
						if (alipayWalletVO.getType().equals("MyTransactions")) {
							if(details != null)
							{
							if (details.getInfidigiUserId().equals(alipayWalletVO.getInfidigiUserId())) {
								obj.setStatus(details.getPgResultCode());
								obj.setInfidigiUserId(details.getInfidigiUserId());
								obj.setMerchantTransactionId(alipayAPIReq.getMcPartnerTransId());
								obj.setLatitude(alipayAPIReq.getMcLatitude());
								obj.setLongitude(alipayAPIReq.getMcLongitude());
								genericList.add(obj);
							}
							}
						}
						}
					}
					
           	 }
        	 }
        	 else
        	 {
        		   genericAPIResponse.setStatusCode("301");
                   genericAPIResponse.setMessage("No record found."); 
        	 }
		} else {

			genericAPIResponse.setStatusCode("101");
			genericAPIResponse.setMessage("Unauthorised user");
		}
		if (genericList != null) {
			genericAPIResponse.setStatusCode("201");
		} else {
			genericAPIResponse.setStatusCode("302");
		}
		genericAPIResponse.setTransactionList(genericList);
		return genericAPIResponse;

    }

    @RequestMapping("/reversal")
    public GenericAPIResponse reversal(@RequestBody AlipayWalletVO alipayWalletVO, HttpServletRequest request) {
        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
        boolean isValidUser = alipayAPIService.authenticateUser(alipayWalletVO, ipAddress, sender, genericAPIResponse);
        if (isValidUser) {
            Object returnParams[] = alipayAPIService.createReverseTransaction(alipayWalletVO, ipAddress, sender);
            genericAPIResponse = (GenericAPIResponse) returnParams[1];
            String merchantCompany = alipayAPIService.getMerchantCompanyName(alipayWalletVO.getInfidigiAccountId(), alipayWalletVO.getInfidigiUserId());
            String m_name = alipayAPIService.getMerchantName(alipayWalletVO.getInfidigiAccountId(), alipayWalletVO.getInfidigiUserId());
            genericAPIResponse.setFirstName(m_name);
            genericAPIResponse.setMerchantCompany(merchantCompany);
            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("---TimerTask started - reversal()");
                    alipayAPIService.callAlipayReverseAPI(returnParams[0], alipayWalletVO, ipAddress, sender);
                }

            }, new Date());
        } else {
            genericAPIResponse = new GenericAPIResponse();
            genericAPIResponse.setStatusCode("101");
            genericAPIResponse.setMessage("Unauthorised user");
        }
        return genericAPIResponse;

    }

    @RequestMapping("/cancelation ")
    public GenericAPIResponse cancelation(@RequestBody AlipayWalletVO alipayWalletVO, HttpServletRequest request) {
        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
        boolean isValidUser = alipayAPIService.authenticateUser(alipayWalletVO, ipAddress, sender, genericAPIResponse);
        if (isValidUser) {
            Object returnParams[] = alipayAPIService.createCancelTransaction(alipayWalletVO, ipAddress, sender);
            genericAPIResponse = (GenericAPIResponse) returnParams[1];
            String merchantCompany = alipayAPIService.getMerchantName(alipayWalletVO.getInfidigiAccountId(), alipayWalletVO.getInfidigiUserId());
            genericAPIResponse.setMerchantCompany(merchantCompany);
            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("---TimerTask started - cancelation()");
                    alipayAPIService.callAlipayCancelAPI(returnParams[0], alipayWalletVO, ipAddress, sender);
                }

            }, new Date());
        } else {
            genericAPIResponse = new GenericAPIResponse();
            genericAPIResponse.setStatusCode("101");
            genericAPIResponse.setMessage("Unauthorised user");
        }
        return genericAPIResponse;

    }
  
  
    @RequestMapping("/refund ")
    public GenericAPIResponse refund(@RequestBody AlipayWalletVO alipayWalletVO, HttpServletRequest request) {
        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
        
        boolean isValidUser = alipayAPIService.authenticateUser(alipayWalletVO, ipAddress, sender, genericAPIResponse);
        if (isValidUser) {
        	alipayWalletVO.setUser_id(alipayWalletVO.getInfidigiAccountId());
         	 AlipayWalletVO alipayWalletVO1 = userService.getAlipayRecord(alipayWalletVO);
    	  	 logger.info("log for transactions");
    	  	if(alipayWalletVO1 != null)
    	  	{
       
    	  		alipayWalletVO.setCharSet(alipayWalletVO1.getCharSet());
    	  		alipayWalletVO.setService(alipayWalletVO1.getService());
    	  		alipayWalletVO.setPayitnz_id(alipayWalletVO1.getPayitnz_id());
    	  		alipayWalletVO.setCurrency(alipayWalletVO1.getCurrency());
            Object returnParams[] = alipayAPIService.createRefundTransaction(alipayWalletVO, ipAddress, sender);
            genericAPIResponse = (GenericAPIResponse) returnParams[1];
            String merchantCompany = alipayAPIService.getMerchantName(alipayWalletVO.getInfidigiAccountId(), alipayWalletVO.getInfidigiUserId());
            if(genericAPIResponse != null)
            {
            genericAPIResponse.setMerchantCompany(merchantCompany);
            }
            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("---TimerTask started");
                    alipayAPIService.callAlipayRefundAPI(returnParams[0], alipayWalletVO, ipAddress, sender);
                }

            }, new Date());
    	  	
        } else {
            genericAPIResponse = new GenericAPIResponse();
            genericAPIResponse.setStatusCode("101");
            genericAPIResponse.setMessage("Unauthorised user");
        }
        }
        return genericAPIResponse;

    }

    
   
    /**
     * This method retrieves the data from Infidigi database and returns
     * @param alipayWalletVO
     * @param request
     * @return
     */
    @RequestMapping("/query ")
    public GenericAPIResponse query(@RequestBody AlipayWalletVO alipayWalletVO, HttpServletRequest request) {
        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
      
        boolean isValidUser = alipayAPIService.authenticateUser(alipayWalletVO, ipAddress, sender, genericAPIResponse);
        if (isValidUser) {
            genericAPIResponse = alipayAPIService.getPaymentTransactionDetails(alipayWalletVO, ipAddress, sender);
            if (genericAPIResponse == null) {
                genericAPIResponse = new GenericAPIResponse();
                genericAPIResponse.setStatusCode("301");
                genericAPIResponse.setMessage("No record available for given transaction id.");
            } else {
                String merchantCompany = alipayAPIService.getMerchantName(alipayWalletVO.getInfidigiAccountId(), alipayWalletVO.getInfidigiUserId());
                String m_name = alipayAPIService.getMerchantName(alipayWalletVO.getInfidigiAccountId(), alipayWalletVO.getInfidigiUserId());
                genericAPIResponse.setFirstName(m_name);
                
                genericAPIResponse.setMerchantCompany(merchantCompany);
            }
        } else {
            genericAPIResponse.setStatusCode("101");
            genericAPIResponse.setMessage("Unauthorised user");
        }
        return genericAPIResponse;

    }

   
    /**
     * This method search the data from Infidigi database and returns
     * @param alipayWalletVO
     * @param request
     * @return
     */
    @RequestMapping("/search ")
    public List<GenericAPIResponse> search(@RequestBody AlipayWalletVO alipayWalletVO, HttpServletRequest request) {
        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
        List<GenericAPIResponse> responseList = new ArrayList<GenericAPIResponse>();
        boolean isValidUser = alipayAPIService.authenticateUser(alipayWalletVO, ipAddress, sender, genericAPIResponse);
        if (isValidUser) {
        	String startDate = alipayWalletVO.getStartDate();
        	String endDate = alipayWalletVO.getEndDate();
        	
        	alipayWalletVO.setStartDate(startDate);
        	alipayWalletVO.setEndDate(endDate);
        	
            responseList = alipayAPIService.getPaymentTransactionByCriteria(alipayWalletVO, ipAddress, sender);
            if (responseList == null || responseList.isEmpty()) {
                genericAPIResponse.setStatusCode("302");
                genericAPIResponse.setMessage("No matching records found.");
                responseList.add(genericAPIResponse);
            }
        } else {
            genericAPIResponse.setStatusCode("101");
            genericAPIResponse.setMessage("Unauthorised user");
            responseList.add(genericAPIResponse);
        }
        return responseList;

    }
   

    /**
     * This method retrieves the data from Alipay gateway via its API and returns
     * @param alipayWalletVO
     * @param request
     * @return
     */
    @RequestMapping("/queryAlipay ")
    public GenericAPIResponse queryAlipay(@RequestBody AlipayWalletVO alipayWalletVO, HttpServletRequest request) {
        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
        boolean isValidUser = alipayAPIService.authenticateUser(alipayWalletVO, ipAddress, sender, genericAPIResponse);
        if (isValidUser) {
            Object returnParams[] = alipayAPIService.createQueryTransaction(alipayWalletVO, ipAddress, sender);
            genericAPIResponse = alipayAPIService.callAlipayQueryAPI(returnParams[0], alipayWalletVO, ipAddress, sender);

        } else {
            genericAPIResponse.setStatusCode("101");
            genericAPIResponse.setMessage("Unauthorised user");
        }
        return genericAPIResponse;

    }
    @RequestMapping("/list_transactions")
    public GenericAPIResponse ListTransactions(@RequestBody AlipayWalletVO alipayWalletVO, HttpServletRequest request) {
        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
        List< GenericAPIResponse> list = new ArrayList();
        
        
        boolean isValidUser = alipayAPIService.authenticateUser(alipayWalletVO, ipAddress, sender, genericAPIResponse);
        if (isValidUser) {
        	
        	List<AlipayAPIResponse> apr = alipayAPIService.getTransactionList(alipayWalletVO, ipAddress, sender);
        	if(apr != null)
        	{
        		
        	System.out.println("siize == "+apr.size());
        	for (AlipayAPIResponse alipayAPIResponse : apr) {
        		
        		if(!(alipayAPIResponse.getMcTransAmount().equals("0.0")))
        		{
        		GenericAPIResponse genericAPIResponse1 = new GenericAPIResponse();
        		genericAPIResponse1.setStatus(alipayAPIResponse.getPgResultCode());
        		genericAPIResponse1.setAmount(Double.toString(alipayAPIResponse.getPgTransAmountCny()));
        		
//        		if(!(alipayAPIResponse.getPgAlipayPayTime().equals("")))
//        		{
//        		String x = alipayAPIResponse.getPgAlipayPayTime();
//        		String upToNCharacters = x.substring(0, Math.min(x.length(), 8));
//        		String year = upToNCharacters.substring(0, Math.min(upToNCharacters.length(), 4));
//        		String remainder = upToNCharacters.substring(4);
//        		String month = remainder.substring(0, Math.min(remainder.length(), 2));
//        		String day = remainder.substring(2);
//        		String Date = day +"-"+month +"-" +year;
//        		genericAPIResponse1.setTransactionDate(Date);
//        		}
//        		else
//        		{
//        			genericAPIResponse1.setTransactionDate(alipayAPIResponse.getPgAlipayPayTime());
//        		}
        		
        		genericAPIResponse1.setTrDate(alipayAPIResponse.getPgAlipayPayTime());
        		
        		genericAPIResponse1.setMerchantTransactionId(alipayAPIResponse.getPgPartnerTransId());
        		
        		
        		list.add(genericAPIResponse1);
        	}
			}
        	genericAPIResponse.setStatusCode("201");
        	genericAPIResponse.setTransactionList(list);
        	
        	}
        	else
        	{
        		 genericAPIResponse.setStatusCode("302");
                 genericAPIResponse.setMessage("No results found");
        	}
        	
        } else {

        	 genericAPIResponse.setStatusCode("101");
             genericAPIResponse.setMessage("Unauthorised user");
        }
        return genericAPIResponse;

    }
    @RequestMapping("/editUser")
    public GenericAPIResponse EditUser(@RequestBody AlipayWalletVO alipayWalletVO, HttpServletRequest request) {
        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
       User user= new User();
       if(alipayWalletVO.getUserID() != "" && alipayWalletVO.getUserID() != null && !(alipayWalletVO.getUserID().equals("0")))
       {
       	user.setId(Integer.parseInt(alipayWalletVO.getUserID()));
       	
       	try {
			user.setInfidigiPassword(DynamicPaymentConstant.getHashPassword(alipayWalletVO.getInfidigiPassword()));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        	alipayAPIService.UpdateUser(user);
        	
            genericAPIResponse.setStatusCode("204");
            genericAPIResponse.setMessage("Account Updated successfully");
       }
       else
       {
    	   genericAPIResponse.setStatusCode("101");
           genericAPIResponse.setMessage("Unauthorised user");
       }
        return genericAPIResponse;
       
    }
   
    
    @RequestMapping("/emailBankReceipt")
    public GenericAPIResponse emailBankReceipt(@RequestBody AlipayWalletVO alipayWalletVO, HttpServletRequest request) {
        GenericAPIResponse genericAPIResponse = new GenericAPIResponse();
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
        String EmailIDs = alipayWalletVO.getEmail();
        AlipayAPIResponse alipayAPIResponse = alipayAPIService.getTransactionDetails(alipayWalletVO.getPayitnz_id());
        alipayWalletVO.setDate_and_time(alipayAPIResponse.getPgAlipayPayTime());
        if(EmailIDs.contains(","))
        {
        List<String> emailList = Arrays.asList(EmailIDs.split(","));
        if (!(alipayWalletVO.getEmail().equals(""))) {
        	for (String email : emailList) {
        		
                 ApplicationContext context = new ClassPathXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
     			MailMail mm = (MailMail) context.getBean("mailMail");
     			mm.sendMail("",email,alipayWalletVO);
			}

        	 genericAPIResponse.setStatusCode("true");
        	 genericAPIResponse.setMessage("Mail Sent Successfully.");
        	 
        } else {

           // genericAPIResponse.setStatusCode("101");
            genericAPIResponse.setMessage("Unauthorised user");
        }
        }
        else
        {
        	 if (!(alipayWalletVO.getEmail().equals(""))) {
             	
                      ApplicationContext context = new ClassPathXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
          			MailMail mm = (MailMail) context.getBean("mailMail");
          			
          			mm.sendMail("",EmailIDs,alipayWalletVO);
          			 genericAPIResponse.setStatusCode("true");
                 	 genericAPIResponse.setMessage("Mail Sent Successfully.");
     			}

             	            	 
            else {

                // genericAPIResponse.setStatusCode("101");
                 genericAPIResponse.setMessage("Unauthorised user");
             
        }
        
       
        }
        return genericAPIResponse;

    }
    

//	@RequestMapping(value={"/list"}, method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
//	public Test LearningTest() {
//		
//		JSONArray arr =  new JSONArray();
//		
//		String titles[] ={"The Pump Room","The Eye","Chalice Well","Tate Modern","Eiffel Tower","Parc Guell"};
//		String place[]={"Bath","London","Glastonbury","London","Paris","Barcelona"};
//		double lat[] = {51.143669,51.507774,48.858271,41.414483,51.38131,51.143669};
//		double lon[] = { -2.35959,-0.119483,-2.706782,-0.099446,2.294114,2.152579};
//		String info[] = {"The Pump Room Restaurant in Bath is one of the city’s most elegant places to enjoy stylish, Modern-British cuisine.","At 135m, the London Eye is the world’s largest cantilevered observation wheel. It was designed by Marks Barfield Architects and launched in 2000.","Chalice Well is one of Britain's most ancient wells, nestling in the Vale of Avalon between the famous Glastonbury Tor and Chalice Hill.","Tate Modern is a modern art gallery located in London. It is Britain's national gallery of international modern art and forms part of the Tate group.","The Eiffel Tower (French: La Tour Eiffel, is an iron lattice tower located on the Champ de Mars in Paris.","Parc Guell is a garden complex with architectural elements situated on the hill of El Carmel in the Gràcia district of Barcelona."};
//		String telephone[] = {"+44 (0)1225 444477","+44 (0)8717 813000","+44 (0)1458 831154","+44 (0)20 7887 8888","+33 892 70 12 39","+34 902 20 03 02"};
//		String url[] = {"http://www.romanbaths.co.uk","http://www.londoneye.com","http://www.chalicewell.org.uk","http://www.tate.org.uk","http://www.tour-eiffel.fr","http://www.parkguell.es"};
//		boolean status[] = {true,false,true,true,false,false};
//		Test t = new Test();
//		
//		for(int i=0; i<=5; i++)
//		{
//			JSONObject obj = new JSONObject();
//			obj.put("title", titles[i]);
//			obj.put("place", place[i]);
//			obj.put("latitude", lat[i]);
//			obj.put("longitude", lon[i]);
//			obj.put("information", info[i]);
//			obj.put("telephone", telephone[i]);
//			obj.put("url", url[i]);
//			obj.put("visited", status[i]);
//			arr.put(obj);
//			
//		}
//		String jsonFormattedString = arr.toString().replaceAll("\\\\", "");
//		t.setJsonString(jsonFormattedString);
//		
//	return t;
//}
  
}
