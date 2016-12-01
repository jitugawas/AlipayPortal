package com.payitnz.web;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mkyong.common.FTPFunctions;
import com.mkyong.common.MailMail;
import com.mkyong.common.SFTPpullsshkeys;
import com.payitnz.model.AlipayDashboardBean;
import com.payitnz.model.AlipayPaymentGatewayBean;
import com.payitnz.model.InfiSecurityQuestionsBean;
import com.payitnz.model.ReconcillationBean;
import com.payitnz.model.TransactionBean;
import com.payitnz.model.User;
import com.payitnz.service.AlipayReconcillationService;
import com.payitnz.service.DashboardService;
import com.payitnz.service.UserService;
import com.payitnz.config.DynamicPaymentConstant;

@Controller
public class LoginController {
	
	private SFTPpullsshkeys sftpObj = new SFTPpullsshkeys();
	
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
	
	private AlipayReconcillationService alipayReconcillationService;

	/*@Autowired
	private FileValidator fileValidator;

	@InitBinder
	protected void initBinderFileBucket(WebDataBinder binder) {
	      binder.setValidator(fileValidator);
	}
	*/
	
    @Autowired
    public void setAlipayReconcillationService(AlipayReconcillationService alipayReconcillationService) {
        this.alipayReconcillationService = alipayReconcillationService;
    }

//	private static final Logger logger = LoggerFactory
//			.getLogger(DashboardController.class);
	final static Logger logger = Logger.getLogger(LoginController.class);
	Calendar calendar = Calendar.getInstance();
	java.util.Date now = calendar.getTime();
	java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = new Date();
	
	@RequestMapping(value = "/webLogin", method = RequestMethod.GET)
	public String viewLogin(HttpServletRequest request,@ModelAttribute(value="user") User userForm,Map<String, Object> model,HttpSession session) {			
		
		if(request.getSession().getAttribute("user_id") != null && request.getSession().getAttribute("role_id") != null){
			return "redirect:/home";
		}else{
			session.invalidate();
			String loginAction = DynamicPaymentConstant.SITE_BASE_URL+"/webLogin";
			model.put("loginAction", loginAction);
			return "login";
		}

	}
		
	@RequestMapping(value={"/webLogin"}, method={RequestMethod.POST})
	public ModelAndView doLogin(HttpServletRequest request,@ModelAttribute(value="user") User userForm, BindingResult result, Map<String, Object> model,AlipayDashboardBean search) throws ParseException {
		List<User> userData = null;
		ModelAndView view = new ModelAndView("login");
		if (result.hasErrors()) {
			return view;
		}	
		try {
			userData = this.userService.findByIDAndPassword(userForm.getUsername(), DynamicPaymentConstant.getHashPassword(userForm.getInfidigiPassword()));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | NullPointerException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (userData.isEmpty()) {
			result.addError(new ObjectError("error", "Invalid Username or Password. Please try again."));
		} else {
			DecimalFormat df = new DecimalFormat("#.00"); 
			//storing data in session
			for (User infiUserBean:userData){
				
				if(infiUserBean.getStatus().equals("0")){
					
					result.addError(new ObjectError("error", "User is inactive. Please verify your account before you login!"));
					return view;
				}
				
				if(infiUserBean.getStatus().equals("2")){
					//System.out.println("Error ehre!");
					result.addError(new ObjectError("error", "User is locked out. Please contact System owner/Merchant!"));
					return view;
				}
				 logger.info(infiUserBean.getEmail()+" "+"have logged in at " + new Date());
				request.getSession().setAttribute("user_id",infiUserBean.getId());
				request.getSession().setAttribute("email",infiUserBean.getEmail());
				request.getSession().setAttribute("first_name",infiUserBean.getFirstName());
				request.getSession().setAttribute("last_name",infiUserBean.getLastName());
				request.getSession().setAttribute("company_name",infiUserBean.getCompanyName());
				request.getSession().setAttribute("phone_number",infiUserBean.getPhoneNo());
				request.getSession().setAttribute("role_id",infiUserBean.getRoleId());
				request.getSession().setAttribute("merchantId",infiUserBean.getInfidigiAccountId());
				request.getSession().setAttribute("username", infiUserBean.getUsername());
				request.getSession().setAttribute("infidigiUserId", infiUserBean.getInfidigiUserId());
				//set status
				request.getSession().setAttribute("all_permission", infiUserBean.getAll_permission());
				request.getSession().setAttribute("permission_alipay_transactions", infiUserBean.getPermission_alipay_transactions());
				request.getSession().setAttribute("permission_refund", infiUserBean.getPermission_refund());
				request.getSession().setAttribute("permission_setup_connections", infiUserBean.getPermission_setup_connections());
				request.getSession().setAttribute("permission_setup_merchant", infiUserBean.getPermission_setup_merchant());	
				request.getSession().setAttribute("permission_setup_users", infiUserBean.getPermission_setup_users());
				request.getSession().setAttribute("permission_settlement", infiUserBean.getPermission_settlement());
				request.getSession().setAttribute("permission_reconciliation", infiUserBean.getPermission_reconciliation());
				request.getSession().setAttribute("permisson_access_app", infiUserBean.getPermission_access_app());
				
				//set current logged in
				DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss , EEEEEEEEE d MMMMMMMM yyyy");
				Date date1 = new Date();
				String current_time = dateFormat1.format(date1);
				request.getSession().setAttribute("current_logged_in_at",current_time);
				
				//set last logged in
				if(infiUserBean.getLast_logged_in_at() == null){
					request.getSession().setAttribute("last_logged_in_at",current_time);
				}else{		
					String last_logged_in_time = dateFormat1.format(dateFormat.parse(infiUserBean.getLast_logged_in_at()));
					request.getSession().setAttribute("last_logged_in_at",last_logged_in_time);
				}
				
				//update last logged in database
				infiUserBean.setLast_logged_in_at(dateFormat.format(dateFormat1.parse(current_time)));
				userService.update(infiUserBean);
			
			}
			
			//fetch param config details
			int role_id = (int) request.getSession().getAttribute("role_id");
			
			search.setDisplay_transactions_per_day(7);
			
			//set user_id and role_id
			search.setUser_id((int) request.getSession().getAttribute("user_id"));
			search.setRole_id(role_id);

			Map<Object, Object> transaction = new HashMap<Object, Object>();
			//Map<Object, Object> payMethods = new HashMap<Object, Object>();
			//Map<Object, Object> totTransDetails = new HashMap<Object, Object>();
			
			int totalTransVolume = 0;
			double totalTransValue = 0.0;
			List<AlipayDashboardBean> transDetails = dashboardService.findAllTransDetails(search);

			//fetch payment types
			//List<DpPaymentTypesBean> paymentTypes = dashboardService.getAllPaymentTypes();
			
			List<AlipayPaymentGatewayBean> paymentMethods = dashboardService.findAllPaymentMethods();
			
			//List<AlipayDashboardBean> totalTransDetails = dashboardService.getAllTotalTransactionDetails();

			ArrayList<Object> transNumber = new ArrayList<Object>();
			ArrayList<Object> transValue = new ArrayList<Object>();
			ArrayList<Object> transValueToolTip = new ArrayList<Object>();
			ArrayList<Object> transNumToolTip = new ArrayList<Object>();
			
			/*for (AlipayDashboardBean TransactionBean : totalTransDetails) {
				totTransDetails.put("total_amount",TransactionBean.getTotal_amount());
				totTransDetails.put("total_transaction",Math.round(TransactionBean.getTotal_transaction()));
			}
			*/

			int loop =0;
			for (AlipayPaymentGatewayBean paymentTypeBean : paymentMethods) {
				Map<Object, Object> map = new HashMap<Object, Object>();
				int k = 0;
				for (AlipayDashboardBean TransactionBean : transDetails) {
					if (paymentTypeBean.getPayment_method().equals(TransactionBean.getPayment_method())) {
						transNumber.add(TransactionBean.getTrans_num());
						transValue.add(DynamicPaymentConstant.round(TransactionBean.getSum_amount(),2));
						transValueToolTip.add("$"+df.format(TransactionBean.getSum_amount()));
						transNumToolTip.add(DynamicPaymentConstant.INRFormatWithInt(TransactionBean.getTrans_num()));
						map.put("payment_method", paymentTypeBean.getPayment_method());
						map.put("avg_amount", DynamicPaymentConstant.INRFormat(TransactionBean.getAvg_amount()));
						map.put("sum_amount", DynamicPaymentConstant.INRFormat(TransactionBean.getSum_amount()));
						map.put("min_amount", DynamicPaymentConstant.INRFormat(TransactionBean.getMin_amount()));
						map.put("max_amount", DynamicPaymentConstant.INRFormat(TransactionBean.getMax_amount()));
						map.put("trans_num", DynamicPaymentConstant.INRFormatWithInt(TransactionBean.getTrans_num()));
						totalTransVolume += TransactionBean.getTrans_num();
						totalTransValue += TransactionBean.getSum_amount();
						transaction.put(loop, map);
						loop++;
						break;
					}
					++k;
				}
				if (k != transDetails.size()) continue;
				map.put("payment_method", paymentTypeBean.getPayment_method());
				map.put("avg_amount","0.00");
				map.put("sum_amount","0.00");
				map.put("min_amount","0.00");
				map.put("max_amount","0.00");
				map.put("trans_num",0);
				transNumber.add(0);
				transValue.add(0);
				transValueToolTip.add("$"+0);
				transNumToolTip.add(0);
				transaction.put(loop, map);
				loop++;
			}

			JSONArray json1 = new JSONArray(transNumber);
			JSONArray json2 = new JSONArray(transValue);
			JSONArray json3 = new JSONArray(transValueToolTip);
			JSONArray json4 = new JSONArray(transNumToolTip);

			//storing user dashboard search object in  session
			request.getSession().setAttribute("dashboardDataObj",search);
			
			view.addObject("GraphTransNumData",json1);
			view.addObject("GraphTransValueData", json2);
			view.addObject("GraphTransValueTooltipData",json3);
			view.addObject("GraphTransNumTooltipData",json4);
			view.addObject("TransactionDetails", transaction);
			
			//graph data 
			view.addObject("GraphTransValue1",transValue.get(0));
			view.addObject("GraphTransValue2",transValue.get(1));
			view.addObject("GraphTransTooltipValue1", transValueToolTip.get(0));
			view.addObject("GraphTransTooltipValue2", transValueToolTip.get(1));
			
			//view.addObject("TotalTransDetails",totalTransDetails);
			//view.addObject("PaymentMethods", payMethods);
			view.addObject("TotalTransVolume",DynamicPaymentConstant.INRFormatWithInt(totalTransVolume));
			if(totalTransValue == 0.0){
				view.addObject("TotalTransValue","0.00");
			}else{
				view.addObject("TotalTransValue", DynamicPaymentConstant.INRFormat(totalTransValue));
			}
			view.addObject("DisplayDays",search.getDisplay_transactions_per_day());
			
			view.setViewName("home");
		}
		
		return view;
	}
	
	//forgot password
	
	@RequestMapping(value={"/forgotPassword"}, method={RequestMethod.GET})
	public ModelAndView forgotPassword(@ModelAttribute("userForm")User userForm, BindingResult result, Map<String, Object> model) {
		ModelAndView view = new ModelAndView("forgotPassword");
		return view;
	}
	
	@RequestMapping(value={"/verifyEmail"}, method={RequestMethod.GET})
	public String verifyEmail(@ModelAttribute(value="userForm") User userForm, BindingResult result, Map<String, Object> model,final RedirectAttributes redirectAttributes) {
		ModelAndView view = new ModelAndView("forgotPassword");
		if(result.hasErrors()){
			redirectAttributes.addFlashAttribute("css", "error");
			redirectAttributes.addFlashAttribute("message","Invalid parameters!");
		}else{
			
			boolean validEmail = userService.CheckEmailId(userForm.getEmail());
			//check email exists
			if(validEmail){	
				//get userId 
				int userId =userService.findByEmail(userForm.getEmail());
				User userInfo = userService.findById(userId);
				int verified = userInfo.getVerified();
				
				if(verified == 1){
					redirectAttributes.addFlashAttribute("css","success");
					redirectAttributes.addFlashAttribute("message","Reset password link has been successfully sent to your email Id");
								
					//send mail
					ApplicationContext context = new ClassPathXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
					MailMail mm = (MailMail) context.getBean("mailMail");
					String encodedID = DynamicPaymentConstant.Encoding(Integer.toString(userId).toCharArray());
					String resetLink = DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SITE_URL+"/resetPassword?id="+encodedID;
					mm.resetMail(userForm.getEmail(), resetLink);
				}else{
					
					redirectAttributes.addFlashAttribute("css","error");
					redirectAttributes.addFlashAttribute("message","User is inactive, Please verify your account before login,check your inbox for verification mail.");
				}
					
			}else{
				redirectAttributes.addFlashAttribute("css", "error");
				redirectAttributes.addFlashAttribute("message","Invalid Email Id");
			}
		}
		
		return "redirect:/forgotPassword";
	}
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public ModelAndView resetPassword(@ModelAttribute("userForm")User userForm,
			BindingResult result, Model model,  HttpServletRequest request, RedirectAttributes redirectAttributes) {
		ModelAndView view = new ModelAndView("resetPassword");
		String decodedID = DynamicPaymentConstant.decoding(request.getParameter("id"));
		view.addObject("id",decodedID);
		return view;
	}
	
	@RequestMapping(value = "/savePassword", method = RequestMethod.POST)
	public String savePassword(@ModelAttribute("userForm") User userForm,
			BindingResult result, Model model,  HttpServletRequest request, RedirectAttributes redirectAttributes,HttpServletResponse response) throws IOException {
		User userInfo = userService.findById(userForm.getId());
		//userInfo.setPassword(userForm.getPassword());
		try {
			userInfo.setInfidigiPassword(DynamicPaymentConstant.getHashPassword(userForm.getInfidigiPassword()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		userService.update(userInfo);
		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("message","Password updated successfully");
		return "redirect:/webLogin";
	}
	
	//user verification
	
	@RequestMapping(value = "/UserVerification", method = RequestMethod.GET)
	public ModelAndView UserVerification(@ModelAttribute("userForm") InfiSecurityQuestionsBean userForm,
			BindingResult result, Model model,  HttpServletRequest request, RedirectAttributes redirectAttributes) {

		System.out.println(request.getParameter("id"));
		String decodedID = DynamicPaymentConstant.decoding(request.getParameter("id"));

		System.out.println("decodedID"+decodedID);
		User userInfo = userService.findById(Integer.decode(decodedID));

		ModelAndView view = new ModelAndView("UserVerification");
		if(userInfo!=null){
			view.addObject("id",decodedID);
			view.addObject("companyName",userInfo.getCompanyName());
			view.addObject("email",userInfo.getEmail());
			view.addObject("role_id",userInfo.getRoleId());
			view.addObject("verified",userInfo.getVerified());
		}else{
			view.addObject("expired",1);
		}
		
		List<InfiSecurityQuestionsBean> allQuestions = userService.findAllQuestions();
		populateUsers(allQuestions, view);

		return view;
	}
	
	private void populateUsers(List<InfiSecurityQuestionsBean> allQuestions, ModelAndView model) {
		List<String> questions = new ArrayList<String>();
		List<Integer> id = new ArrayList<Integer>();
		for(InfiSecurityQuestionsBean sec : allQuestions)
		{
			System.out.println("methodInfo" + sec.getSecurity_question());
			questions.add(sec.getSecurity_question());
			id.add(sec.getId());
		}
		model.addObject("questions", questions);
		model.addObject("ids", id);
	}
	
	@RequestMapping(value = "/SaveVerificationInfo", method = RequestMethod.POST)
	public void SaveUserVerification(@ModelAttribute("userForm") InfiSecurityQuestionsBean userForm,HttpSession session,
			BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) throws IOException {
		String que1 = userForm.getQuestion1();
		String que2 = userForm.getQuestion2();


		User userInfo = userService.findById(userForm.getId());
		userForm.setCompany_name(userInfo.getCompanyName());
		userForm.setUser_id(Integer.toString(userForm.getId()));
		//userInfo.setInfidigiPassword(infidigiPassword);
		/*try {
			//userInfo.setPassword(DynamicPaymentConstant.getHashPassword(userForm.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//userInfo.setInfidigiPassword(userInfo.getInfidigiPassword());
		userInfo.setStatus("1");
		userInfo.setVerified(1);
		List<InfiSecurityQuestionsBean> allQuestions = userService.findAllQuestions();
		for(int i=0;i<=1;i++)
		{
			for(InfiSecurityQuestionsBean sec : allQuestions)
			{

				if(sec.getSecurity_question().equals(que1) || sec.getSecurity_question().equals(que2))
				{
					String ans=userForm.getAnswer();
					String ans1=userForm.getAnswer1();

					int qid=sec.getId();
					userForm.setQuestion_id(qid);

					userForm.setCreated_date(currentTimestamp);
					if(sec.getSecurity_question() == que1)
					{
						userForm.setAnswer1("");
						userForm.setAnswer(ans);
						userService.saveSecurityQuestion(userForm);
					}
					else
					{
						userForm.setAnswer1(ans1);
						userForm.setAnswer("");
						userService.saveSecurityQuestion(userForm);

					}

				}
			}
		}

		//update password
		userService.update(userInfo);
		session.invalidate();
		//ModelAndView view = new ModelAndView("login");
		//view.addObject("user", new User());
		//view.addObject("email",userInfo.getEmail());
		//view.setViewName("login");
		//response.sendRedirect("localhost:8000/DynamicPaymentPortal/login");
		
		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("message","Account verified suucessfully");
		
		response.sendRedirect(DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SITE_URL+"/webLogin");;
		//response.sendRedirect(Constant.SERVER_HOST+"/"+Constant.SITE_URL+"/login");
	}
	
	//This should be called from cronjob
		@RequestMapping(value="/scanFtpfile",method = RequestMethod.GET)
		public void scanFtpfile(HttpServletRequest request, HttpServletResponse response){
			
			 BufferedReader br;
		     String line = "";	    
		     String name= ""; 
		     //String filePath = DynamicPaymentConstant.USER_FILES_PATH;
		     	     
			String userName = "infidigidev@infidigi.com";//DynamicPaymentConstant.FTP_USERNAME;
	     	String password = "Infidigi@2016";//DynamicPaymentConstant.FTP_PASSWORD;
	     	int port = 21;//DynamicPaymentConstant.FTP_PORT;
	     	String host ="ftp.infidigi.com";// DynamicPaymentConstant.FTP_HOST; 
	     	boolean result = false;
	     	
	        FTPFunctions ftpobj;
			try {
				ftpobj = new FTPFunctions(host, port, userName, password);
			  ftpobj.downloadFTPFile( "//cronjob//transaction.txt", DynamicPaymentConstant.FTP_DESTINATION_PATH+"csv.txt");
	             System.out.println("FTP File downloaded successfully");
	              result = ftpobj.listFTPFiles(DynamicPaymentConstant.FTP_DESTINATION_PATH, "csv.txt");
	             System.out.println(result);
	             result = true;
	             ftpobj.disconnect();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}					      	            
	         

			 if(result){
				 File newFile = new File(DynamicPaymentConstant.FTP_DESTINATION_PATH+"csv.txt"); 
		
				 if (newFile.isFile() && newFile.canRead() ) {
						try {
							
							logger.info("Server File Location="
									+ newFile.getAbsolutePath());
							name = newFile.getName();
							
							 br = new BufferedReader(new FileReader(newFile));
				            ReconcillationBean reconcileBean = new ReconcillationBean();
				            			           
				            int adminId = 1;//(int)session.getAttribute("user_id");
				            
							int i = 0; 
				            while ((line = br.readLine()) != null) {
				            	i++;
				            	if(i>2){
					            											
					                String[] reconcillationArray = line.split("\\|");
				
					                //System.out.println("line :"+line.toString());
					                
					                Date myDate = new Date();
					                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
					                String myDateString = sdf.format(myDate);
					                
					                reconcileBean.setAdminId(adminId);
					                reconcileBean.setTransactionAmount(reconcillationArray[2]);
					                reconcileBean.setChargeAmount(reconcillationArray[3]);
					                reconcileBean.setTransactionType(reconcillationArray[6]);
					                reconcileBean.setUploadedDate(myDateString);
					                reconcileBean.setRemark("");
					                reconcileBean.setPartnerTransactionId(reconcillationArray[0]);
					                reconcileBean.setPaymentTime(reconcillationArray[5]);
					                reconcileBean.setReconcillationStatus(0);
					                reconcileBean.setCurrency(reconcillationArray[4]);
					                reconcileBean.setTransactionId(reconcillationArray[1]);
					                
					                alipayReconcillationService.saveOrUpdate(reconcileBean);
					                	                
					               // alipayReconcillationService.validateTransaction(reconcileBean);
				            	}
				            }
				           String message = "<span style='color:green;'>You successfully uploaded and processed file=" + name+"</span>";
				          logger.debug(message);
				          //  boolean downloaded = FTPFunctions.downloadFile();
					  		PrintWriter writer;
					  		try {
					  			writer = response.getWriter();
					  			 writer.print(message);
					  		} catch (IOException e1) {
					  			// TODO Auto-generated catch block
					  			e1.printStackTrace();
					  		} 
				        
						} catch (Exception e) {
							e.printStackTrace();
							String message = "<span style='color:red;'>You failed to upload " + name + " => " + e.getMessage()+"</span>";
							logger.debug(message);
							PrintWriter writer;
					  		try {
					  			writer = response.getWriter();
					  			 writer.print(message);
					  		} catch (IOException e1) {
					  			// TODO Auto-generated catch block
					  			e1.printStackTrace();
					  		} 
						}
					} else {
						String message = "<span style='color:red;'>You failed to upload " + name
								+ " because the file was empty.</span>";
						logger.debug(message);
						PrintWriter writer;
				  		try {
				  			writer = response.getWriter();
				  			 writer.print(message);
				  		} catch (IOException e1) {
				  			// TODO Auto-generated catch block
				  			e1.printStackTrace();
				  		} 
					}
				 
			 }
		}
	
		@RequestMapping(value = "/importTransaction", method = RequestMethod.GET)
		public void importTransactions(HttpServletRequest request, HttpServletResponse response) {
			//ModelAndView view = new ModelAndView("reconcileTransaction");
			
			 BufferedReader br;
		     String line = "";	    
		     String name= ""; 
		     //String filePath = DynamicPaymentConstant.USER_FILES_PATH;
		     
		     boolean dowloaded = sftpObj.downloadAlipayFtpFile();
			 if(dowloaded){
				 
				 String generatedFile = DynamicPaymentConstant.getCurrentAlipayReconcileFile();   
				 File newFile = new File(DynamicPaymentConstant.FTP_DESTINATION_PATH+generatedFile); 
				 
				 if (newFile.isFile() && newFile.canRead() ) {
						try {
							
							logger.info("Server File Location="
									+ newFile.getAbsolutePath());
							name = newFile.getName();
							
							 br = new BufferedReader(new FileReader(newFile));
				            TransactionBean transactionBean = new TransactionBean();
				            			           
				            int adminId = 1;//(int)session.getAttribute("user_id");
				            int merchant_id = 1;
							int i = 0; 
				            while ((line = br.readLine()) != null) {
				            	i++;
				            	if(i>2){
					            											
					                String[] reconcillationArray = line.split("\\|");
					                
					                Date myDate = new Date();
					                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
					                String myDateString = sdf.format(myDate);
					                String transactionDate =  sdf.format(myDate);
					                
					                transactionBean.setMerchantId(merchant_id);
					                transactionBean.setTransactionAmount(reconcillationArray[2]);
					                transactionBean.setChargeAmount(reconcillationArray[3]);
					                transactionBean.setTransactionType(reconcillationArray[6]);
					                transactionBean.setUploadedDate(myDateString);
					                transactionBean.setRemark("");
					                transactionBean.setPartnerTransactionId(reconcillationArray[0]);
					                transactionBean.setPaymentTime(reconcillationArray[5]);
					                transactionBean.setTransactionStatus(0);
					                transactionBean.setCurrency(reconcillationArray[4]);
					                transactionBean.setTransactionId(reconcillationArray[1]);
					                transactionBean.setTransactionDate(transactionDate);
					                alipayReconcillationService.importTransactions(transactionBean);
					                	                
					               // alipayReconcillationService.validateTransaction(reconcileBean);
				            	}
				            }
				           String message = "<span style='color:green;'>You successfully uploaded and processed file=" + name+"</span>";
				          logger.debug(message);			      
				        
						} catch (Exception e) {
							e.printStackTrace();
							String message = "<span style='color:red;'>You failed to upload " + name + " => " + e.getMessage()+"</span>";
							logger.debug(message);
							PrintWriter writer;
					  		try {
					  			writer = response.getWriter();
					  			 writer.print(message);
					  		} catch (IOException e1) {
					  			// TODO Auto-generated catch block
					  			e1.printStackTrace();
					  		} 
						}
					} else {
						String message = "<span style='color:red;'>You failed to upload " + name
								+ " because the file was empty.</span>";
						logger.debug(message);
						PrintWriter writer;
				  		try {
				  			writer = response.getWriter();
				  			 writer.print(message);
				  		} catch (IOException e1) {
				  			// TODO Auto-generated catch block
				  			e1.printStackTrace();
				  		} 
					}
				 
			 }		 
					
		}
		
}
