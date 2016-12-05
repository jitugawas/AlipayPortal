package com.payitnz.web;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.fasterxml.jackson.annotation.JsonView;
import com.mkyong.common.MailMail;
import com.mkyong.common.SFTPpullsshkeys;
import com.payitnz.config.DynamicPaymentConstant;
import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayDashboardBean;
import com.payitnz.model.AlipayPaymentGatewayBean;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.EmailAlertsConfigBean;
import com.payitnz.model.GenericAPIResponse;
import com.payitnz.model.InfiUserFilesBean;
import com.payitnz.model.InfiUserSearchBean;
import com.payitnz.model.ReconcillationBean;
import com.payitnz.model.ReconcillationFileBean;
import com.payitnz.model.SettlementBean;
import com.payitnz.model.SettlementFileBean;
import com.payitnz.model.User;
import com.payitnz.service.AlipayAPIService;
import com.payitnz.service.AlipayReconcillationService;
import com.payitnz.service.DashboardService;
import com.payitnz.service.SettlementService;
import com.payitnz.service.UserService;
import com.payitnz.util.CreatePDF;

@Controller
public class DashboardController {

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
	
	
	@Autowired
	private AlipayAPIService alipayAPIService;
	
	@Autowired
	public void setUserService(AlipayAPIService alipayAPIService) {
		this.alipayAPIService = alipayAPIService;
	}
	
	private AlipayReconcillationService alipayReconcillationService;

	@Autowired
	private SettlementService settlementService;
	
	@Autowired
	public void setSettlemnetService(SettlementService settlementService) {
		this.settlementService = settlementService;
	}
	
/*	@Autowired
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

    private static final Logger logger = Logger.getLogger(DashboardController.class);
	
//	private static final Logger logger = LoggerFactory
//			.getLogger(DashboardController.class);
	
	Calendar calendar = Calendar.getInstance();
	java.util.Date now = calendar.getTime();
	java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = new Date();
	
	//Dashboard link urls	

	@RequestMapping(value={"/home"}, method={RequestMethod.GET})
	public ModelAndView Home(@ModelAttribute(value="user") User userForm, BindingResult result, Map<String, Object> model,AlipayDashboardBean search,HttpServletRequest request) {
		ModelAndView view = new ModelAndView("home");
		DecimalFormat df = new DecimalFormat("#.00"); 
		Object user_id = request.getSession().getAttribute("user_id");
		if(user_id ==null){
			view.setViewName("redirect:/webLogin");
			return view;
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
	
		return view;
	}
	
	@RequestMapping(value={"/getDashboardTableAjax"}, method={RequestMethod.POST})
	public ModelAndView getDashboardTableAjax(@ModelAttribute(value="user") User userForm, BindingResult result,@RequestBody AlipayDashboardBean search,HttpServletRequest request) {
		ModelAndView view = new ModelAndView("dashboardTable");
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		DecimalFormat df = new DecimalFormat("#.00"); 
		Map<Object, Object> transaction = new HashMap<Object, Object>();
		//Map<Object, Object> payMethods = new HashMap<Object, Object>();
		
		//fetch param config details
		int role_id = (int) request.getSession().getAttribute("role_id");
		int user_id = (int) request.getSession().getAttribute("user_id");
			
		search.setDisplay_transactions_per_day(7);
				
		//set user_id and role_id
		search.setUser_id(user_id);
		search.setRole_id(role_id);
		
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
		
		int days = 0;
		try {
			Date date1 = format.parse(format.format(search.getFromDate()));
			Date date2 = format.parse(format.format(search.getToDate()));
			 
			 long diff = date2.getTime() - date1.getTime();
			 days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		view.addObject("DisplayDays", days);	
		return view;
	}

	@RequestMapping(value = "/webLogout", method = RequestMethod.GET)
	public String viewLogout(HttpSession session,Map<String, Object> model) throws ServletException {
		//session.getAttribute("user_id");
		Object user_id = session.getAttribute("user_id");
		if(user_id !=null){
		 logger.info(session.getAttribute("email").toString()+" have logged out at " + new Date());
		}
		session.invalidate();
		return "redirect:/webLogin";
	}

	@RequestMapping(value = "/setupMerchant", method = RequestMethod.GET)
	public ModelAndView SetupMerchant(@ModelAttribute("userForm") User userForm,InfiUserSearchBean search,
			BindingResult result, HttpServletRequest request) {
		ModelAndView view = new ModelAndView("setupMerchant");
		Object user_id = request.getSession().getAttribute("user_id");
		if(user_id ==null){
			view.setViewName("redirect:/webLogin");
			return view;
		}
		
		int roleId = Integer.decode(request.getSession().getAttribute("role_id").toString());
		int permissionSetupMerchant = (int) request.getSession().getAttribute("permission_setup_merchant");
		int allPermission = (int) request.getSession().getAttribute("all_permission");
		if((allPermission == 0 && permissionSetupMerchant == 0) || roleId !=1){
			view.setViewName("redirect:/webLogin");
			return view;
		}

		
		Map<String, Object> model = new HashMap<String, Object>();

		int userId = (int) request.getSession().getAttribute("user_id");


		//set session role_id and user_id
		search.setUser_id(userId);
		search.setRole_id(roleId);
		search.setDisplay_user(1);

		//set userForm value if role id 2
		if(roleId == 2){
			userForm.setCompanyName((String) request.getSession().getAttribute("company_name"));
		}

		//get all details by search details
		List<User> allUserList = userService.findUsersDetailsBySearchParams(search);		
		//List<DpUsersBean> allUserList = dashboardService.findAll();
		for (User UsersBean : allUserList) {
			if(UsersBean.getStatus().equals("0"))
			{
				UsersBean.setStatus("Inactive");
			}

			if(UsersBean.getStatus().equals("1"))
			{
				UsersBean.setStatus("Active");
			}
			if(UsersBean.getStatus().equals("2"))
			{
				UsersBean.setStatus("LockedOut");
			}

		}

		//get all merchant company details
		/*List<User> companyList = dashboardService.getAllMerchantCompanies();

			ArrayList<String> companies = new ArrayList<String>();

			for(User each:companyList){
				companies.add(each.getCompanyName());
			}

			JSONArray json1 = new JSONArray(companies);
		 */
		//set  session
		request.getSession().setAttribute("userObj",search);

		//model.put("Names", allUserList);
		//model.put("Companies",json1);

		model.put("Names", allUserList);
		return new ModelAndView("setupMerchant", model);
	}

	@RequestMapping(value = "/setupUser", method = RequestMethod.GET)
	public ModelAndView SetupUser(@ModelAttribute("userForm") User userForm,InfiUserSearchBean search,
			BindingResult result, HttpServletRequest request) {
		ModelAndView view = new ModelAndView("setupUser");
		Object user_id = request.getSession().getAttribute("user_id");
		if(user_id ==null){
			view.setViewName("redirect:/webLogin");
			return view;
		}
		
		int roleId = Integer.decode(request.getSession().getAttribute("role_id").toString());
		int permissionSetupUsers = (int) request.getSession().getAttribute("permission_setup_users");
		int allPermission = (int) request.getSession().getAttribute("all_permission");
		if((allPermission == 0 && permissionSetupUsers == 0) || roleId == 3 ){
			view.setViewName("redirect:/webLogin");
			return view;
		}
		

		Map<String, Object> model = new HashMap<String, Object>();

		int userId = (int) request.getSession().getAttribute("user_id");


		//set session role_id and user_id
		search.setUser_id(userId);
		search.setRole_id(roleId);
		search.setDisplay_user(2);

		//set userForm value if role id 2
		if(roleId == 2){
			userForm.setCompanyName((String) request.getSession().getAttribute("company_name"));
		}

		//get all details by search details
		List<User> allUserList = userService.findUsersDetailsBySearchParams(search);		
		//List<DpUsersBean> allUserList = dashboardService.findAll();
		for (User UsersBean : allUserList) {
			if(UsersBean.getStatus().equals("0"))
			{
				UsersBean.setStatus("Inactive");
			}

			if(UsersBean.getStatus().equals("1"))
			{
				UsersBean.setStatus("Active");
			}
			if(UsersBean.getStatus().equals("2"))
			{
				UsersBean.setStatus("LockedOut");
			}

		}

		//get all merchant company details
		/*List<User> companyList = dashboardService.getAllMerchantCompanies();

			ArrayList<String> companies = new ArrayList<String>();

			for(User each:companyList){
				companies.add(each.getCompanyName());
			}

			JSONArray json1 = new JSONArray(companies);
		 */
		//set  session
		request.getSession().setAttribute("userObj",search);

		//model.put("Names", allUserList);
		//model.put("Companies",json1);

		model.put("Names", allUserList);
		return new ModelAndView("setupUser", model);
	}

	@RequestMapping(value = "/checkMerchantUserId", method = RequestMethod.POST)
	public @ResponseBody boolean checkMerchantUserId(@RequestBody User search,HttpServletRequest request) {
		int userId = Integer.decode(search.getInfidigiUserId());

		//check if userid exists
		boolean result = userService.checkIfMerchantUserIdExists(userId);
		return result;
	}

	// save or update Merchant
	@RequestMapping(value = "/registerMerchant", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateMerchant(@ModelAttribute("userForm") User user,@ModelAttribute("id") String id,HttpServletRequest request, BindingResult result, Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest req){
		String directoryPath = DynamicPaymentConstant.USER_FILES_ABSOLUTE_PATH;
		String baseUrlPath = DynamicPaymentConstant.USER_FILES_UPLOAD_URL;

		File uploadPath = new File(directoryPath);
		if (!uploadPath.exists()) {
			if (uploadPath.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}

		ModelAndView view = new ModelAndView("setupMerchant");
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("css", "error");
			redirectAttributes.addFlashAttribute("message", "Invalid form data");
			return view;
		} else {
			List<MultipartFile> files = user.getFiles();
			List<String> fileNames = user.getFile_name();
			List<String> fileDesc = user.getFile_description();

			boolean emailStatus = userService.CheckEmailId(user.getEmail());	
			boolean username = userService.CheckUsername(user.getUsername());

			if((emailStatus || username ) && user.getId() == 0){
				//result.addError(new ObjectError("error", "Email Already Exists"));
				if(emailStatus && username){
					redirectAttributes.addFlashAttribute("css", "error");
					redirectAttributes.addFlashAttribute("message", "Email and username already exists");
				}else if (emailStatus){
					redirectAttributes.addFlashAttribute("css", "error");
					redirectAttributes.addFlashAttribute("message", "Email already exists");
				}else{
					redirectAttributes.addFlashAttribute("css", "error");
					redirectAttributes.addFlashAttribute("message", "Username already exists");
				}

			}
			else
			{		
				if(user.getId() == 0){

					redirectAttributes.addFlashAttribute("css", "success");
					redirectAttributes.addFlashAttribute("message", "Merchant added successfully");

					//add user details 
					int creator_role_id = Integer.decode(request.getSession().getAttribute("role_id").toString());
					//set role 
					user.setRoleId(2);

					//generate account id
					Random r = new Random(System.currentTimeMillis());

					//check if it exists
					//user.setInfidigiAccountId(""+((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)));
					user.setInfidigiAccountId(request.getSession().getAttribute("user_id").toString());
							
					user.setStatus("0");
					//user.setAddress("pune");
					//user.setPassword("");
					//user.setCountry("NZ");
					user.setCreated_date(dateFormat.format(date));
					user.setCreator_id((int) request.getSession().getAttribute("user_id"));
					String password = user.getInfidigiPassword();
					
					try {
						user.setInfidigiPassword(DynamicPaymentConstant.getHashPassword(password));
					} catch (NoSuchAlgorithmException
							| UnsupportedEncodingException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				
					ApplicationContext context = new ClassPathXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
					MailMail mm = (MailMail) context.getBean("mailMail");

					int userId = userService.save(user);
					String encodedID = DynamicPaymentConstant.Encoding(Integer.toString(user.getId()).toCharArray());
					String verifyLink = DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SITE_URL+"/UserVerification?id="+encodedID;
					System.out.println(verifyLink);
					mm.sendVerificationMail(user,password,verifyLink);

					try {
						user.setInfidigiPassword(DynamicPaymentConstant.getHashPassword(user.getInfidigiPassword()));
					} catch (NoSuchAlgorithmException | UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					//add files to folder
					Map<Integer,Map<Object,Object>> fileNameValues = new HashMap<Integer,Map<Object,Object>>();
					Map<Integer,Map<Object,Object>> fileDescValues = new HashMap<Integer,Map<Object,Object>>();

					if(null != fileNames && fileNames.size() > 0){
						int i=0;
						for (String file_name : fileNames) {
							Map<Object,Object> map = new HashMap<Object,Object>();	
							map.put("file_name",file_name);
							fileNameValues.put(i,map);
							i++;
						}

					}

					if(null != fileDesc && fileDesc.size() > 0){
						int j=0;
						for (String file_desc : fileDesc) {
							Map<Object,Object> map = new HashMap<Object,Object>();
							map.put("file_description",file_desc);
							fileDescValues.put(j,map);
							j++;
						}

					}

					if(null != files && files.size() > 0) {
						int l=0;
						for (MultipartFile multipartFile : files) {
							Map<Object,Object> map = new HashMap<Object,Object>();
							String fileName = multipartFile.getOriginalFilename();
							int m=0;
							for(Map.Entry<Integer, Map<Object,Object>> entry : fileNameValues.entrySet())
							{
								Map<Object,Object> map1= entry.getValue();
								//if filename
								if(entry.getKey() == l && !map1.get("file_name").equals("")){
									m++;
									try {
										String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
										BufferedOutputStream stream = new BufferedOutputStream(
												new FileOutputStream(new File(directoryPath + map1.get("file_name")+"."+extension)));
										FileCopyUtils.copy(multipartFile.getInputStream(), stream);
										stream.close();
										map.put("file_name",map1.get("file_name")+"."+extension);
										fileNameValues.put(l,map);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}


								}

							}

							if(m == 0 && !fileName.equals("")){
								//else use original name
								try {
									BufferedOutputStream stream = new BufferedOutputStream(
											new FileOutputStream(new File(directoryPath + multipartFile.getOriginalFilename())));
									FileCopyUtils.copy(multipartFile.getInputStream(), stream);
									stream.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								map.put("file_name",fileName);
								fileNameValues.put(l,map);
							}

							l++;
						}
					}

					//generate files data	
					InfiUserFilesBean UserFilesBean = new InfiUserFilesBean();
					UserFilesBean.setCreated_date(dateFormat.format(date));
					UserFilesBean.setUser_id(userId);

					if(!fileNameValues.isEmpty()){
						for(Map.Entry<Integer, Map<Object,Object>> fileEntry : fileNameValues.entrySet())
						{
							Map<Object,Object> map1 = fileEntry.getValue();

							for(Map.Entry<Integer, Map<Object,Object>> descEntry : fileDescValues.entrySet())
							{
								Map<Object,Object> map2= descEntry.getValue();

								//if filename
								if(descEntry.getKey() == fileEntry.getKey() && !map1.get("file_name").toString().equals("")){
									UserFilesBean.setFile_name((String) map1.get("file_name"));

									if(map2.get("file_description")!= null){
										UserFilesBean.setFile_description((String) map2.get("file_description"));
									}else{
										UserFilesBean.setFile_description("");
									}
									//add to database
									userService.saveFiles(UserFilesBean);
								}

							}
						}
					}

				}else{

					redirectAttributes.addFlashAttribute("css", "success");
					redirectAttributes.addFlashAttribute("message", "Merchant details updated successfully");

					//update
					User userDetails = userService.findById(user.getId());

					//set original password 
					user.setInfidigiPassword(userDetails.getInfidigiPassword());
					user.setVerified(userDetails.getVerified());
					user.setLast_logged_in_at(userDetails.getLast_logged_in_at());
					user.setEmail(userDetails.getEmail());
					
					int userId = userService.update(user);

					//add files to folder
					Map<Integer,Map<Object,Object>> fileNameValues = new HashMap<Integer,Map<Object,Object>>();
					Map<Integer,Map<Object,Object>> fileDescValues = new HashMap<Integer,Map<Object,Object>>();

					if(null != fileNames && fileNames.size() > 0){
						int i=0;
						for (String file_name : fileNames) {
							Map<Object,Object> map = new HashMap<Object,Object>();	
							map.put("file_name",file_name);
							fileNameValues.put(i,map);
							i++;
						}

					}

					if(null != fileDesc && fileDesc.size() > 0){
						int j=0;
						for (String file_desc : fileDesc) {
							Map<Object,Object> map = new HashMap<Object,Object>();
							map.put("file_description",file_desc);
							fileDescValues.put(j,map);
							j++;
						}

					}

					if(null != files && files.size() > 0) {
						int l=0;
						for (MultipartFile multipartFile : files) {
							Map<Object,Object> map = new HashMap<Object,Object>();
							String fileName = multipartFile.getOriginalFilename();
							int m=0;
							for(Map.Entry<Integer, Map<Object,Object>> entry : fileNameValues.entrySet())
							{
								Map<Object,Object> map1= entry.getValue();
								//if filename
								if(entry.getKey() == l && !map1.get("file_name").equals("")){
									m++;
									try {
										String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
										BufferedOutputStream stream = new BufferedOutputStream(
												new FileOutputStream(new File(directoryPath + map1.get("file_name")+"."+extension)));
										FileCopyUtils.copy(multipartFile.getInputStream(), stream);
										stream.close();
										map.put("file_name",map1.get("file_name")+"."+extension);
										fileNameValues.put(l,map);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}


								}

							}

							if(m == 0 && !fileName.equals("")){
								//else use original name
								try {
									BufferedOutputStream stream = new BufferedOutputStream(
											new FileOutputStream(new File(directoryPath + multipartFile.getOriginalFilename())));
									FileCopyUtils.copy(multipartFile.getInputStream(), stream);
									stream.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								map.put("file_name",fileName);
								fileNameValues.put(l,map);
							}

							l++;
						}
					}

					//generate files data	
					InfiUserFilesBean UserFilesBean = new InfiUserFilesBean();
					UserFilesBean.setCreated_date(dateFormat.format(date));
					UserFilesBean.setUser_id(userId);

					if(!fileNameValues.isEmpty()){

						for(Map.Entry<Integer, Map<Object,Object>> fileEntry : fileNameValues.entrySet())
						{
							Map<Object,Object> map1 = fileEntry.getValue();

							for(Map.Entry<Integer, Map<Object,Object>> descEntry : fileDescValues.entrySet())
							{
								Map<Object,Object> map2= descEntry.getValue();

								//if filename
								if(descEntry.getKey() == fileEntry.getKey() && !map1.get("file_name").toString().equals("")){
									UserFilesBean.setFile_name((String) map1.get("file_name"));

									if(map2.get("file_description")!= null){
										UserFilesBean.setFile_description((String) map2.get("file_description"));
									}else{
										UserFilesBean.setFile_description("");
									}
									//add to database
									userService.saveFiles(UserFilesBean);
								}

							}
						}

					}
				}
			}
			view.setViewName("redirect:/setupMerchant");
			return view;
		}

	}

	// save or update Merchant
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateUser(@ModelAttribute("userForm") User user,@ModelAttribute("id") String id,HttpServletRequest request, BindingResult result, Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest req){
		String directoryPath = DynamicPaymentConstant.USER_FILES_ABSOLUTE_PATH;
		String baseUrlPath = DynamicPaymentConstant.USER_FILES_UPLOAD_URL;

		File uploadPath = new File(directoryPath);
		if (!uploadPath.exists()) {
			if (uploadPath.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}

		ModelAndView view = new ModelAndView("setupUser");
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("css", "error");
			redirectAttributes.addFlashAttribute("message", "Invalid form data");
			return view;
		} else {
			List<MultipartFile> files = user.getFiles();
			List<String> fileNames = user.getFile_name();
			List<String> fileDesc = user.getFile_description();

			boolean emailStatus = userService.CheckEmailId(user.getEmail());	
			boolean username = userService.CheckUsername(user.getUsername());

			if((emailStatus || username ) && user.getId() == 0){
				//result.addError(new ObjectError("error", "Email Already Exists"));
				if(emailStatus && username){
					redirectAttributes.addFlashAttribute("css", "error");
					redirectAttributes.addFlashAttribute("message", "Email and username already exists");
				}else if (emailStatus){
					redirectAttributes.addFlashAttribute("css", "error");
					redirectAttributes.addFlashAttribute("message", "Email already exists");
				}else{
					redirectAttributes.addFlashAttribute("css", "error");
					redirectAttributes.addFlashAttribute("message", "Username already exists");
				}

			}
			else
			{		
				if(user.getId() == 0){

					redirectAttributes.addFlashAttribute("css", "success");
					redirectAttributes.addFlashAttribute("message", "User added successfully");

					//add user details 
					int creator_role_id = Integer.decode(request.getSession().getAttribute("role_id").toString());
					//set role 
					user.setRoleId(3);

					//generate account id
					Random r = new Random( System.currentTimeMillis() );

					//check if it exists
					//user.setInfidigiAccountId(""+((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)));
					user.setInfidigiAccountId(request.getSession().getAttribute("user_id").toString());
					
					user.setStatus("0");
					
					//user.setAddress("pune");
					//user.setPassword("");
					//user.setCountry("NZ");
					user.setCreated_date(dateFormat.format(date));
					user.setCreator_id((int) request.getSession().getAttribute("user_id"));
					
					String password = user.getInfidigiPassword();
					
					try {
						user.setInfidigiPassword(DynamicPaymentConstant.getHashPassword(password));
					} catch (NoSuchAlgorithmException
							| UnsupportedEncodingException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				
					
					ApplicationContext context = new ClassPathXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
					MailMail mm = (MailMail) context.getBean("mailMail");

					int userId = userService.save(user);
					String encodedID = DynamicPaymentConstant.Encoding(Integer.toString(user.getId()).toCharArray());
					String verifyLink = DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SITE_URL+"/UserVerification?id="+encodedID;
					System.out.println(verifyLink);
					mm.sendVerificationMail(user,password,verifyLink);

					try {
						user.setInfidigiPassword(DynamicPaymentConstant.getHashPassword(user.getInfidigiPassword()));
					} catch (NoSuchAlgorithmException | UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					//add files to folder
					Map<Integer,Map<Object,Object>> fileNameValues = new HashMap<Integer,Map<Object,Object>>();
					Map<Integer,Map<Object,Object>> fileDescValues = new HashMap<Integer,Map<Object,Object>>();

					if(null != fileNames && fileNames.size() > 0){
						int i=0;
						for (String file_name : fileNames) {
							Map<Object,Object> map = new HashMap<Object,Object>();	
							map.put("file_name",file_name);
							fileNameValues.put(i,map);
							i++;
						}

					}

					if(null != fileDesc && fileDesc.size() > 0){
						int j=0;
						for (String file_desc : fileDesc) {
							Map<Object,Object> map = new HashMap<Object,Object>();
							map.put("file_description",file_desc);
							fileDescValues.put(j,map);
							j++;
						}

					}

					if(null != files && files.size() > 0) {
						int l=0;
						for (MultipartFile multipartFile : files) {
							Map<Object,Object> map = new HashMap<Object,Object>();
							String fileName = multipartFile.getOriginalFilename();
							int m=0;
							for(Map.Entry<Integer, Map<Object,Object>> entry : fileNameValues.entrySet())
							{
								Map<Object,Object> map1= entry.getValue();
								//if filename
								if(entry.getKey() == l && !map1.get("file_name").equals("")){
									m++;
									try {
										String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
										BufferedOutputStream stream = new BufferedOutputStream(
												new FileOutputStream(new File(directoryPath + map1.get("file_name")+"."+extension)));
										FileCopyUtils.copy(multipartFile.getInputStream(), stream);
										stream.close();
										map.put("file_name",map1.get("file_name")+"."+extension);
										fileNameValues.put(l,map);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}


								}

							}

							if(m == 0 && !fileName.equals("")){
								//else use original name
								try {
									BufferedOutputStream stream = new BufferedOutputStream(
											new FileOutputStream(new File(directoryPath + multipartFile.getOriginalFilename())));
									FileCopyUtils.copy(multipartFile.getInputStream(), stream);
									stream.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								map.put("file_name",fileName);
								fileNameValues.put(l,map);
							}

							l++;
						}
					}

					//generate files data	
					InfiUserFilesBean UserFilesBean = new InfiUserFilesBean();
					UserFilesBean.setCreated_date(dateFormat.format(date));
					UserFilesBean.setUser_id(userId);

					if(!fileNameValues.isEmpty()){
						for(Map.Entry<Integer, Map<Object,Object>> fileEntry : fileNameValues.entrySet())
						{
							Map<Object,Object> map1 = fileEntry.getValue();

							for(Map.Entry<Integer, Map<Object,Object>> descEntry : fileDescValues.entrySet())
							{
								Map<Object,Object> map2= descEntry.getValue();

								//if filename
								if(descEntry.getKey() == fileEntry.getKey() && !map1.get("file_name").toString().equals("")){
									UserFilesBean.setFile_name((String) map1.get("file_name"));

									if(map2.get("file_description")!= null){
										UserFilesBean.setFile_description((String) map2.get("file_description"));
									}else{
										UserFilesBean.setFile_description("");
									}
									//add to database
									userService.saveFiles(UserFilesBean);
								}

							}
						}
					}

				}else{

					redirectAttributes.addFlashAttribute("css", "success");
					redirectAttributes.addFlashAttribute("message", "User details updated successfully");

					//update
					User userDetails = userService.findById(user.getId());

					//set original password 
					user.setInfidigiPassword(userDetails.getInfidigiPassword());
					user.setVerified(userDetails.getVerified());
					user.setLast_logged_in_at(userDetails.getLast_logged_in_at());		
					user.setEmail(userDetails.getEmail());		
					
					int userId = userService.update(user);

					//add files to folder
					Map<Integer,Map<Object,Object>> fileNameValues = new HashMap<Integer,Map<Object,Object>>();
					Map<Integer,Map<Object,Object>> fileDescValues = new HashMap<Integer,Map<Object,Object>>();

					if(null != fileNames && fileNames.size() > 0){
						int i=0;
						for (String file_name : fileNames) {
							Map<Object,Object> map = new HashMap<Object,Object>();	
							map.put("file_name",file_name);
							fileNameValues.put(i,map);
							i++;
						}

					}

					if(null != fileDesc && fileDesc.size() > 0){
						int j=0;
						for (String file_desc : fileDesc) {
							Map<Object,Object> map = new HashMap<Object,Object>();
							map.put("file_description",file_desc);
							fileDescValues.put(j,map);
							j++;
						}

					}

					if(null != files && files.size() > 0) {
						int l=0;
						for (MultipartFile multipartFile : files) {
							Map<Object,Object> map = new HashMap<Object,Object>();
							String fileName = multipartFile.getOriginalFilename();
							int m=0;
							for(Map.Entry<Integer, Map<Object,Object>> entry : fileNameValues.entrySet())
							{
								Map<Object,Object> map1= entry.getValue();
								//if filename
								if(entry.getKey() == l && !map1.get("file_name").equals("")){
									m++;
									try {
										String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
										BufferedOutputStream stream = new BufferedOutputStream(
												new FileOutputStream(new File(directoryPath + map1.get("file_name")+"."+extension)));
										FileCopyUtils.copy(multipartFile.getInputStream(), stream);
										stream.close();
										map.put("file_name",map1.get("file_name")+"."+extension);
										fileNameValues.put(l,map);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}

							if(m == 0 && !fileName.equals("")){
								//else use original name
								try {
									BufferedOutputStream stream = new BufferedOutputStream(
											new FileOutputStream(new File(directoryPath + multipartFile.getOriginalFilename())));
									FileCopyUtils.copy(multipartFile.getInputStream(), stream);
									stream.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								map.put("file_name",fileName);
								fileNameValues.put(l,map);
							}

							l++;
						}
					}

					//generate files data	
					InfiUserFilesBean UserFilesBean = new InfiUserFilesBean();
					UserFilesBean.setCreated_date(dateFormat.format(date));
					UserFilesBean.setUser_id(userId);

					if(!fileNameValues.isEmpty()){

						for(Map.Entry<Integer, Map<Object,Object>> fileEntry : fileNameValues.entrySet())
						{
							Map<Object,Object> map1 = fileEntry.getValue();

							for(Map.Entry<Integer, Map<Object,Object>> descEntry : fileDescValues.entrySet())
							{
								Map<Object,Object> map2= descEntry.getValue();

								//if filename
								if(descEntry.getKey() == fileEntry.getKey() && !map1.get("file_name").toString().equals("")){
									UserFilesBean.setFile_name((String) map1.get("file_name"));

									if(map2.get("file_description")!= null){
										UserFilesBean.setFile_description((String) map2.get("file_description"));
									}else{
										UserFilesBean.setFile_description("");
									}
									//add to database
									userService.saveFiles(UserFilesBean);
								}

							}
						}

					}
				}
			}
			view.setViewName("redirect:/setupUser");
			return view;
		}
	}

	@RequestMapping(value = "/deleteMerchant", method = RequestMethod.GET)
	public String DeleteMerchant(@ModelAttribute("user") User userForm,
			BindingResult result, Map<String, Object> model, HttpServletRequest request) {
		String email = request.getParameter("email");
		String method = request.getParameter("method");
		ModelAndView view = new ModelAndView("setupMerchant");
		System.out.println("email"+email);
		System.out.println("method"+method);
		userService.delete(Integer.parseInt(request.getParameter("id")));
		return "redirect:/setupMerchant";
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
	public String DeleteUser(@ModelAttribute("user") User userForm,
			BindingResult result, Map<String, Object> model, HttpServletRequest request) {
		String email = request.getParameter("email");
		String method = request.getParameter("method");
		ModelAndView view = new ModelAndView("setupUser");
		System.out.println("email"+email);
		System.out.println("method"+method);
		userService.delete(Integer.parseInt(request.getParameter("id")));
		return "redirect:/setupUser";
	}

	@RequestMapping(value = "/viewMerchant", method = RequestMethod.GET)
	public ModelAndView ViewMerchant(@ModelAttribute("user") User userForm,
			BindingResult result, Map<String, Object> model, HttpServletRequest request) throws ParseException {
		String UserID = request.getParameter("id");
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		ModelAndView view = new ModelAndView("ViewMerchant");

		Object user_id = request.getSession().getAttribute("user_id");
		if(user_id ==null){
			view.setViewName("redirect:/webLogin");
			return view;
		}

		int roleId = Integer.decode(request.getSession().getAttribute("role_id").toString());
		int permissionSetupMerchant = (int) request.getSession().getAttribute("permission_setup_merchant");
		int allPermission = (int) request.getSession().getAttribute("all_permission");
		if((allPermission == 0 && permissionSetupMerchant == 0) || roleId !=1){
			view.setViewName("redirect:/webLogin");
			return view;
		}

		User user = userService.findById(Integer.parseInt(UserID));

		//get User files
		List<InfiUserFilesBean> files = userService.findAllUserFiles(Integer.parseInt(UserID));
		view.addObject("FirstName", user.getFirstName());
		view.addObject("MobileNumber", user.getPhoneNo());
		view.addObject("Email", user.getEmail());
		view.addObject("companyName", user.getCompanyName());
		view.addObject("Lastname", user.getLastName());
		view.addObject("CreatedDate",format.format(dateFormat.parse(user.getCreated_date())));
		view.addObject("UserName", user.getUsername());
		view.addObject("UserID", user.getInfidigiUserId());
		view.addObject("AccountID",user.getInfidigiAccountId());
		view.addObject("UserFiles",files);
		return view;
	}

	@RequestMapping(value = "/viewUser", method = RequestMethod.GET)
	public ModelAndView ViewUser(@ModelAttribute("user") User userForm,
			BindingResult result, Map<String, Object> model, HttpServletRequest request) throws ParseException {
		String UserID = request.getParameter("id");
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.S");	
		ModelAndView view = new ModelAndView("ViewUser");

		Object user_id = request.getSession().getAttribute("user_id");
		if(user_id ==null){
			view.setViewName("redirect:/webLogin");
			return view;
		}

		int roleId = Integer.decode(request.getSession().getAttribute("role_id").toString());
		int permissionSetupUsers = (int) request.getSession().getAttribute("permission_setup_users");
		int allPermission = (int) request.getSession().getAttribute("all_permission");
		if((allPermission == 0 && permissionSetupUsers == 0) || roleId == 3 ){
			view.setViewName("redirect:/webLogin");
			return view;
		}

		User user = userService.findById(Integer.parseInt(UserID));

		//get User files
		List<InfiUserFilesBean> files = userService.findAllUserFiles(Integer.parseInt(UserID));

		view.addObject("FirstName", user.getFirstName());
		view.addObject("MobileNumber", user.getPhoneNo());
		view.addObject("Email", user.getEmail());
		view.addObject("companyName", user.getCompanyName());
		view.addObject("Lastname", user.getLastName());
		view.addObject("CreatedDate",format.format(dateFormat.parse(user.getCreated_date())));
		view.addObject("UserName", user.getUsername());
		view.addObject("UserID", user.getInfidigiUserId());
		view.addObject("AccountID",user.getInfidigiAccountId());
		view.addObject("UserFiles",files);
		return view;
	}

	@RequestMapping(value = "/editMerchant", method = RequestMethod.GET)
	public ModelAndView EditMerchant(@ModelAttribute("user") User userForm,
			BindingResult result, Map<String, Object> model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

		ModelAndView view = new ModelAndView("editMerchant");
		Object user_id = request.getSession().getAttribute("user_id");
		if(user_id ==null){

			view.setViewName("redirect:/webLogin");
			return view;
		}

		int roleId = Integer.decode(request.getSession().getAttribute("role_id").toString());
		int permissionSetupMerchant = (int) request.getSession().getAttribute("permission_setup_merchant");
		int allPermission = (int) request.getSession().getAttribute("all_permission");
		if((allPermission == 0 && permissionSetupMerchant == 0) || roleId !=1){
			view.setViewName("redirect:/webLogin");
			return view;
		}

		User user = userService.findById(Integer.parseInt(request.getParameter("id")));

		user.setId(Integer.parseInt(request.getParameter("id")));
		//user.setPassword("");
		request.setAttribute("id", request.getParameter("id"));
		redirectAttributes.addFlashAttribute("id", request.getParameter("id"));


		//get User files
		List<InfiUserFilesBean> files = userService.findAllUserFiles(user.getId());
		view.addObject("UserFiles",files);
		view.addObject("filesCount",files.size());
		view.addObject("userForm",user);

		return view;
	}

	@RequestMapping(value = "/editUser", method = RequestMethod.GET)
	public ModelAndView EditUser(@ModelAttribute("user") User userForm,
			BindingResult result, Map<String, Object> model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

		ModelAndView view = new ModelAndView("editUser");
		Object user_id = request.getSession().getAttribute("user_id");
		if(user_id ==null){

			view.setViewName("redirect:/webLogin");
			return view;
		}

		int roleId = Integer.decode(request.getSession().getAttribute("role_id").toString());
		int permissionSetupUsers = (int) request.getSession().getAttribute("permission_setup_users");
		int allPermission = (int) request.getSession().getAttribute("all_permission");
		if((allPermission == 0 && permissionSetupUsers == 0) || roleId == 3 ){
			view.setViewName("redirect:/webLogin");
			return view;
		}

		User user = userService.findById(Integer.parseInt(request.getParameter("id")));

		user.setId(Integer.parseInt(request.getParameter("id")));
		//user.setPassword("");
		request.setAttribute("id", request.getParameter("id"));
		redirectAttributes.addFlashAttribute("id", request.getParameter("id"));


		//get User files
		List<InfiUserFilesBean> files = userService.findAllUserFiles(user.getId());
		view.addObject("UserFiles",files);
		view.addObject("filesCount",files.size());
		view.addObject("userForm",user);

		return view;
	}


	@RequestMapping(value = "/deleteMerchantFile", method = RequestMethod.GET)
	public String DeleteMerchantFile(@ModelAttribute("user") User userForm,
			BindingResult result, Map<String, Object> model, HttpServletRequest request) {
		int userId = Integer.parseInt(request.getParameter("userId"));
		//get file details
		List<InfiUserFilesBean> fileDetails =  userService.getFileDetails(Integer.parseInt(request.getParameter("id")));

		userService.deleteFile(Integer.parseInt(request.getParameter("id")));
		//unlink file
		String filePath = DynamicPaymentConstant.USER_FILES_ABSOLUTE_PATH;
		try{
			String fileName = "";
			for(InfiUserFilesBean dpFile:fileDetails){
				fileName = dpFile.getFile_name();
			}

			File file = new File(filePath+fileName);

			if(file.delete()){
				System.out.println(file.getName() + " is deleted!");
			}else{
				System.out.println("Delete operation is failed.");
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return "redirect:/editMerchant?id="+userId;
	}

	@RequestMapping(value = "/deleteUserFile", method = RequestMethod.GET)
	public String DeleteUserFile(@ModelAttribute("user") User userForm,
			BindingResult result, Map<String, Object> model, HttpServletRequest request) {
		int userId = Integer.parseInt(request.getParameter("userId"));
		//get file details
		List<InfiUserFilesBean> fileDetails =  userService.getFileDetails(Integer.parseInt(request.getParameter("id")));

		userService.deleteFile(Integer.parseInt(request.getParameter("id")));
		//unlink file
		String filePath = DynamicPaymentConstant.USER_FILES_ABSOLUTE_PATH;
		try{
			String fileName = "";
			for(InfiUserFilesBean dpFile:fileDetails){
				fileName = dpFile.getFile_name();
			}

			File file = new File(filePath+fileName);

			if(file.delete()){
				System.out.println(file.getName() + " is deleted!");
			}else{
				System.out.println("Delete operation is failed.");
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return "redirect:/editUser?id="+userId;
	}


	@RequestMapping(value={"/downloadFile"},method = RequestMethod.GET)
	public void downloadFile(HttpServletRequest request,HttpServletResponse response) throws IOException {
		List<InfiUserFilesBean> fileDetails =  userService.getFileDetails(Integer.parseInt(request.getParameter("id")));
		String filePath = DynamicPaymentConstant.USER_FILES_ABSOLUTE_PATH;
		String fileName = "";
		for(InfiUserFilesBean dpFile:fileDetails){
			fileName = dpFile.getFile_name();
		}

		PrintWriter out = response.getWriter();  
		response.setContentType("text/html");
		response.setContentType("APPLICATION/OCTET-STREAM");   
		response.setHeader("Content-disposition", "attachment;filename=" + fileName);

		FileInputStream fileInputStream = new FileInputStream(filePath + fileName);  

		int i;   
		while ((i=fileInputStream.read()) != -1) {  
			out.write(i);   
		}   
		fileInputStream.close();   
		out.close();   
	}  


	@RequestMapping(value = "/getMerchantsTableAjax", method = RequestMethod.POST)
	public ModelAndView getMerchantsTableAjax(@ModelAttribute("userForm") User userForm,@RequestBody InfiUserSearchBean search,
			BindingResult result, HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		ModelAndView view=new ModelAndView("merchantTable");

		Object user_id = request.getSession().getAttribute("user_id");
		if(user_id ==null){
			view.setViewName("redirect:/webLogin");
			return view;
		}

		int userId = (int) request.getSession().getAttribute("user_id");
		int roleId =  (int) request.getSession().getAttribute("role_id");

		//set session role_id and user_id
		search.setUser_id(userId);
		search.setRole_id(roleId);
		search.setDisplay_user(1);

		//get all details by search details
		List<User> allUserList = userService.findUsersDetailsBySearchParams(search);		
		//List<DpUsersBean> allUserList = dashboardService.findAll();
		for (User UsersBean : allUserList) {
			if(UsersBean.getStatus().equals("0"))
			{
				UsersBean.setStatus("Inactive");
			}

			if(UsersBean.getStatus().equals("1"))
			{
				UsersBean.setStatus("Active");
			}
			if(UsersBean.getStatus().equals("2"))
			{
				UsersBean.setStatus("LockedOut");
			}

		}

		//set  session
		request.getSession().setAttribute("userObj",search);

		model.put("Names", allUserList);
		return new ModelAndView("merchantTable", model);
	}


	@RequestMapping(value = "/getUsersTableAjax", method = RequestMethod.POST)
	public ModelAndView getUsersTableAjax(@ModelAttribute("userForm") User userForm,@RequestBody InfiUserSearchBean search,
			BindingResult result, HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		ModelAndView view=new ModelAndView("userTable");

		Object user_id = request.getSession().getAttribute("user_id");
		if(user_id ==null){
			view.setViewName("redirect:/webLogin");
			return view;
		}

		int userId = (int) request.getSession().getAttribute("user_id");
		int roleId =  (int) request.getSession().getAttribute("role_id");

		//set session role_id and user_id
		search.setUser_id(userId);
		search.setRole_id(roleId);
		search.setDisplay_user(2);

		//get all details by search details
		List<User> allUserList = userService.findUsersDetailsBySearchParams(search);		
		//List<DpUsersBean> allUserList = dashboardService.findAll();
		for (User UsersBean : allUserList) {
			if(UsersBean.getStatus().equals("0"))
			{
				UsersBean.setStatus("Inactive");
			}

			if(UsersBean.getStatus().equals("1"))
			{
				UsersBean.setStatus("Active");
			}
			if(UsersBean.getStatus().equals("2"))
			{
				UsersBean.setStatus("LockedOut");
			}

		}

		//set  session
		request.getSession().setAttribute("userObj",search);

		model.put("Names", allUserList);
		return new ModelAndView("userTable", model);
	}


	//Download csv ----------------------------------------------------------------------

	@RequestMapping(value={"/downloadUserCSV"})
	public void downloadUserCSV(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException {
		response.setContentType("text/csv");
		String reportName = "Payitnz_Users.csv";
		response.setHeader("Content-disposition", "attachment;filename=" + reportName);
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		ArrayList<String> rows = new ArrayList<String>();
		rows.add("First Name,Last Name,Username,Company Name,Phone Number,Email,Status,Date Of Creation");
		rows.add("\n");

		//get all details by search details
		List<User> allUserList = userService.findUsersDetailsBySearchParams((InfiUserSearchBean) request.getSession().getAttribute("userObj"));		
		for (User UsersBean : allUserList) {
			String status=null;
			if(UsersBean.getStatus().equals("0"))
			{
				status = "Inactive";
			}

			if(UsersBean.getStatus().equals("1"))
			{
				status = "Active";
			}
			if(UsersBean.getStatus().equals("2"))
			{
				status = "LockedOut";
			}

			rows.add(UsersBean.getFirstName()+","+UsersBean.getLastName()+","+UsersBean.getUsername()+","+UsersBean.getCompanyName()+","+UsersBean.getPhoneNo()+","+UsersBean.getEmail()+","+status+","+format.format(dateFormat.parse(UsersBean.getCreated_date())));
			rows.add("\n");

		}

		for (String outputString : rows) {
			response.getOutputStream().print(outputString);
		}
		response.getOutputStream().flush();
	}

	@RequestMapping(value={"/downloadMerchantCSV"})
	public void downloadMerchantCSV(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException {
		response.setContentType("text/csv");
		String reportName = "Payitnz_Merchants.csv";
		response.setHeader("Content-disposition", "attachment;filename=" + reportName);
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		ArrayList<String> rows = new ArrayList<String>();
		rows.add("First Name,Last Name,Username,Company Name,Phone Number,Email,Status,Date Of Creation");
		rows.add("\n");

		//get all details by search details
		List<User> allUserList = userService.findUsersDetailsBySearchParams((InfiUserSearchBean) request.getSession().getAttribute("userObj"));		
		for (User UsersBean : allUserList) {
			String status=null;
			if(UsersBean.getStatus().equals("0"))
			{
				status = "Inactive";
			}

			if(UsersBean.getStatus().equals("1"))
			{
				status = "Active";
			}
			if(UsersBean.getStatus().equals("2"))
			{
				status = "LockedOut";
			}

			rows.add(UsersBean.getFirstName()+","+UsersBean.getLastName()+","+UsersBean.getUsername()+","+UsersBean.getCompanyName()+","+UsersBean.getPhoneNo()+","+UsersBean.getEmail()+","+status+","+format.format(dateFormat.parse(UsersBean.getCreated_date())));
			rows.add("\n");

		}

		for (String outputString : rows) {
			response.getOutputStream().print(outputString);
		}
		response.getOutputStream().flush();
	}
	
	
	@RequestMapping(value={"/downloadDashboardCSV"})
	public void downloadCSV(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		String reportName = "Payitnz_Transaction_Report.csv";
		response.setHeader("Content-disposition", "attachment;filename=" + reportName);
		int totalTransVolume = 0;
		double totalTransValue = 0.0;
		List<AlipayDashboardBean> transDetails = dashboardService.findAllTransDetails((AlipayDashboardBean) request.getSession().getAttribute("dashboardDataObj"));

		//fetch payment types
		//List<DpPaymentTypesBean> paymentTypes = dashboardService.getAllPaymentTypes();
		
		List<AlipayPaymentGatewayBean> paymentMethods = dashboardService.findAllPaymentMethods();
		
	
		ArrayList<String> rows = new ArrayList<String>();
	
		rows.add("Channel,Transaction Number,Dollar Value,Average,Largest,Smallest");
		rows.add("\n");
		for (AlipayPaymentGatewayBean dpgPaymentTypeBean : paymentMethods) {
			int i = 0;
			for (AlipayDashboardBean dpTransactionBean : transDetails) {
				if (dpgPaymentTypeBean.getPayment_method().equals(dpTransactionBean.getPayment_method())) {
					rows.add(String.valueOf(dpgPaymentTypeBean.getPayment_method()) + "," + dpTransactionBean.getTrans_num() + ",$" + DynamicPaymentConstant.round(dpTransactionBean.getSum_amount(),2) + ",$" + DynamicPaymentConstant.round(dpTransactionBean.getAvg_amount(),2) + ",$" + DynamicPaymentConstant.round(dpTransactionBean.getMax_amount(),2) + ",$" + DynamicPaymentConstant.round(dpTransactionBean.getMin_amount(),2));
					rows.add("\n");
					totalTransVolume += dpTransactionBean.getTrans_num();
					totalTransValue += dpTransactionBean.getSum_amount();
					break;
				}
				++i;
			}
			if (i != transDetails.size()) continue;
			rows.add(String.valueOf(dpgPaymentTypeBean.getPayment_method()) + ",0,$0.0,$0.0,$0.0,$0.0");
			rows.add("\n");
		}
		rows.add("Total," + totalTransVolume + ",$" + totalTransValue);
		rows.add("\n");
		for (String outputString : rows) {
			response.getOutputStream().print(outputString);
		}
		response.getOutputStream().flush();
	}
	
	@RequestMapping(value={"/downloadReportCSV"})
	public void downloadReportCSV(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException {
		response.setContentType("text/csv");
		String reportName = "Payitnz_Transaction_Report.csv";		
		response.setHeader("Content-disposition", "attachment;filename=" + reportName);
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		ArrayList<String> rows = new ArrayList<String>();

		//int role_id = Integer.decode((String)request.getSession().getAttribute("role_id"));

		rows.add("Channel,Transaction ID,Amount,Reference,Date/Time");
		rows.add("\n");

		//fetch data from transaction
		String ipAddress = request.getRemoteAddr();
		String sender = request.getRemoteHost();
		List<AlipayAPIResponse> responseList = alipayAPIService.getTransactionDetailsByCriteriaWeb((AlipayWalletVO) request.getSession().getAttribute("reportDataObj"), ipAddress, sender);

		if (responseList == null || responseList.isEmpty()) {

		}
		else
		{
			for (AlipayAPIResponse trans : responseList) {
				rows.add(trans.getChannel()+","+trans.getPgPartnerTransId()+","+trans.getMcTransAmount()+","+trans.getMcReference()+","+format.format(dateFormat.parse(trans.getTransactionDate())));
				rows.add("\n");
			}
		}

		for (String outputString : rows) {
			response.getOutputStream().print(outputString);
		}
		response.getOutputStream().flush();
	}
	
	@RequestMapping(value={"/downloadTransactionDetailsCSV"})
	public void downloadTransactionDetailsCSV(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException {
		response.setContentType("text/csv");
		String reportName = "Payitnz_Transaction_Details.csv";
		response.setHeader("Content-disposition", "attachment;filename=" + reportName);
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		ArrayList<String> rows = new ArrayList<String>();
		List<AlipayAPIResponse> transData = new ArrayList<AlipayAPIResponse>();
		
		//fetch transaction details by id
		String transactionId = request.getParameter("id");
		//int role_id = (int) request.getSession().getAttribute("role_id");
		
		//fetch data from transaction
		transData = dashboardService.searchTransactionDataById(transactionId);
		String header = "Channel,Merchant Transaction ID,Alipay Transaction ID,Amount,Reference,Particulars,Gateway Status,Sign,Sign Type,Result Code,Error,Alipay Buyer Login ID,Alipay Buyer User ID,Alipay Transaction Refund ID,"
						+"Alipay Pay Time,Alipay Reverse Time,Alipay Cancel Time,Merchant Currency,Merchant User ID,Exchange Rate,Request Time,Ip Address,Remark,Transaction Date";
		String data = "";
		if(!transData.isEmpty()){		
			for (AlipayAPIResponse trans : transData) {		
				data = trans.getChannel()+","+trans.getPgMerchantTransactionId()+","+trans.getPgPartnerTransId()+","+trans.getMcTransAmount()+","+trans.getMcReference()+","+trans.getMcItemName()+","+trans.getPgIsSuccess()+","
				+trans.getPgSign()+","+trans.getPgSignType()+","+trans.getPgResultCode()+","+trans.getPgError()+","+trans.getPgAlipayBuyerLoginId()+","+trans.getPgAlipayBuyerUserId()+","+trans.getMerchantRefundId()+","+trans.getPgAlipayPayTime()+","
				+trans.getPgAlipayReverseTime()+","+trans.getPgAlipayCancelTime()+","+trans.getMcCurrency()+","+trans.getInfidigiUserId()+","+trans.getPgExchangeRate()+","+trans.getRequestTime()+","+trans.getIpAddress()+","+trans.getRemark()+","+format.format(dateFormat.parse(trans.getTransactionDate()));	
			}
		}
		
		rows.add(header);	
		rows.add("\n");
		rows.add(data);
		
		for (String outputString : rows) {
			response.getOutputStream().print(outputString);
		}
		response.getOutputStream().flush();
	}


	//dowload pdf -------------------------------------------------------------------

	@RequestMapping(value={"/downloadUserPDF"})
	public void downloadUserPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletContext servletContext = request.getSession().getServletContext();
		File tempDirectory = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
		String temperotyFilePath = tempDirectory.getAbsolutePath();
		String fileName = "Payitnz_Users.pdf";
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		try {
			CreatePDF.createUserPDF(String.valueOf(temperotyFilePath) + "\\" + fileName,request);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos = this.convertPDFToByteArrayOutputStream(String.valueOf(temperotyFilePath) + "\\" + fileName);
			ServletOutputStream os = response.getOutputStream();
			baos.writeTo((OutputStream)os);
			os.flush();
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@RequestMapping(value={"/downloadMerchantPDF"})
	public void downloadMerchantPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletContext servletContext = request.getSession().getServletContext();
		File tempDirectory = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
		String temperotyFilePath = tempDirectory.getAbsolutePath();
		String fileName = "Payitnz_Merchants.pdf";
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		try {
			CreatePDF.createMerchantPDF(String.valueOf(temperotyFilePath) + "\\" + fileName,request);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos = this.convertPDFToByteArrayOutputStream(String.valueOf(temperotyFilePath) + "\\" + fileName);
			ServletOutputStream os = response.getOutputStream();
			baos.writeTo((OutputStream)os);
			os.flush();
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@RequestMapping(value={"/downloadDashboardPDF"})
	public void downloadPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletContext servletContext = request.getSession().getServletContext();
		File tempDirectory = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
		String temperotyFilePath = tempDirectory.getAbsolutePath();
		String fileName = "Payitnz_Transaction_Report.pdf";
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		try {
			CreatePDF.createPDF(String.valueOf(temperotyFilePath) + "\\" + fileName,request);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos = this.convertPDFToByteArrayOutputStream(String.valueOf(temperotyFilePath) + "\\" + fileName);
			ServletOutputStream os = response.getOutputStream();
			baos.writeTo((OutputStream)os);
			os.flush();
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@RequestMapping(value={"/downloadReportPDF"})
	public void downloadReportPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletContext servletContext = request.getSession().getServletContext();
		File tempDirectory = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
		String temperotyFilePath = tempDirectory.getAbsolutePath();
		String reportName = "Payitnz_Transaction_Report.pdf";	
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=" +reportName);
		try {
			CreatePDF.createReportPDF(String.valueOf(temperotyFilePath) + "\\" + reportName,request);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos = this.convertPDFToByteArrayOutputStream(String.valueOf(temperotyFilePath) + "\\" + reportName);
			ServletOutputStream os = response.getOutputStream();
			baos.writeTo((OutputStream)os);
			os.flush();
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	@RequestMapping(value={"/downloadTransactionDetailsReportPDF"})
	public void downloadTransactionDetailsReportPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletContext servletContext = request.getSession().getServletContext();
		File tempDirectory = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
		String temperotyFilePath = tempDirectory.getAbsolutePath();
		String fileName = "Payitnz_Transaction_Details.pdf";
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		try {
			CreatePDF.createTransactionDetailsReportPDF(String.valueOf(temperotyFilePath) + "\\" + fileName,request);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos = this.convertPDFToByteArrayOutputStream(String.valueOf(temperotyFilePath) + "\\" + fileName);
			ServletOutputStream os = response.getOutputStream();
			baos.writeTo((OutputStream)os);
			os.flush();
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName) {

		InputStream inputStream = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {

			inputStream = new FileInputStream(fileName);
			byte[] buffer = new byte[1024];
			baos = new ByteArrayOutputStream();

			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return baos;
	}
	
	//-------------------------------------------------------------------------------

	@RequestMapping(value = "/setupConnection", method = RequestMethod.GET)
	 public ModelAndView setupConnection(@ModelAttribute("registrationForm") AlipayWalletVO registrationForm,
	   BindingResult result, HttpServletRequest request) {
	  ModelAndView view = new ModelAndView("setupConnection");
	  
	  Object user_id = request.getSession().getAttribute("user_id");
		if(user_id ==null){
			view.setViewName("redirect:/webLogin");
			return view;
		}
	
		int permissionSetupConnections = (int) request.getSession().getAttribute("permission_setup_connections");
		int allPermission = (int) request.getSession().getAttribute("all_permission");
		if(allPermission == 0 && permissionSetupConnections == 0){
			view.setViewName("redirect:/webLogin");
			return view;
		}
	  
	  HttpSession session = request.getSession();
	  Object infidigiAccountId = session.getAttribute("user_id");
	  registrationForm.setUser_id(infidigiAccountId.toString());
	  AlipayWalletVO form1 = userService.getAlipayRecord(registrationForm);
	  if(form1 != null)
	  {
		  registrationForm.setService(form1.getService());
		  registrationForm.setPayitnz_id(form1.getPayitnz_id());
		  registrationForm.setPartner_key(form1.getPartner_key());
		  registrationForm.setCharSet(form1.getCharSet());
		  registrationForm.setCurrency(form1.getCurrency());
		  registrationForm.setReturn_url(form1.getReturn_url());
	 }
	  
	  AlipayWalletVO form2 = userService.getAlipayOnlineRecord(registrationForm);
	  if(form2 != null)
	  {
		  registrationForm.setOnline_service(form2.getOnline_service());
		  registrationForm.setAlipay_online_partner_id(form2.getAlipay_online_partner_id());
		  registrationForm.setAlipay_online_partner_key(form2.getAlipay_online_partner_key());
		  registrationForm.setOnline_character_set(form2.getOnline_character_set());
		  registrationForm.setOnline_currency(form2.getOnline_currency());
		  registrationForm.setAlipay_online_return_url(form2.getAlipay_online_return_url());
		  registrationForm.setAlipay_online_notification_url(form2.getAlipay_online_notification_url());
		  registrationForm.setOrder_valid_time(form2.getOrder_valid_time());
		  registrationForm.setAlipay_supported_method(form2.getAlipay_supported_method());
	  }
	  
	  AlipayWalletVO form3 = dashboardService.getCUPRecord(registrationForm);
	  if(form3 != null)
	  {
		  registrationForm.setMerchant_id(form3.getMerchant_id());
		  registrationForm.setMcc(form3.getMcc());
		  registrationForm.setMerchant_name(form3.getMerchant_name());
		  registrationForm.setCommodity_url(form3.getCommodity_url());
		  registrationForm.setCurrency(form3.getCurrency());
		  registrationForm.setTimeout(form3.getTimeout());
		  registrationForm.setMerchant_reserved_field(form3.getMerchant_reserved_field());
	  }
	  AlipayWalletVO form4 = dashboardService.getDPSRecord(registrationForm);
	  if(form4 != null)
	  {
		  registrationForm.setPxPayKey(form4.getPxPayKey());
		  registrationForm.setPxPayUrl(form4.getPxPayUrl());
		  registrationForm.setPxPayUserId(form4.getPxPayUserId());
		  registrationForm.setDPStransaction_type(form4.getDPStransaction_type());
		  registrationForm.setCurrency(form4.getCurrency());
		  registrationForm.setEmail(form4.getEmail());
		  registrationForm.setDPSsuccess_url(form4.getDPSsuccess_url());
		  registrationForm.setDPSfailure_url(form4.getDPSfailure_url());
	  }
	  AlipayWalletVO form5 = dashboardService.getPoliRecord(registrationForm);
	  if(form5 != null)
	  {
		  registrationForm.setPoli_account_id(form5.getPoli_account_id());
		  registrationForm.setPassword(form5.getPassword());
		  registrationForm.setCurrency_code(form5.getCurrency_code());
		  registrationForm.setPoli_merchant_reference(form5.getPoli_merchant_reference());
		  registrationForm.setMerchant_reference_format(form5.getMerchant_reference_format());
		  registrationForm.setMerchant_data(form5.getMerchant_data());
		  registrationForm.setPoli_homepage_url(form5.getPoli_homepage_url());
		  registrationForm.setPoli_success_url(form5.getPoli_success_url());
		  registrationForm.setPoli_cancellation_url(form5.getPoli_cancellation_url());
		  registrationForm.setPoli_failure_url(form5.getPoli_failure_url());
		  registrationForm.setPoli_notification_url(form5.getPoli_notification_url());
		  registrationForm.setPoli_timeout(form5.getPoli_timeout());
		  registrationForm.setFi_code(form5.getFi_code());
		  registrationForm.setCompany_code(form5.getCompany_code());
	  }
	  AlipayWalletVO form6 = dashboardService.getF2CRecord(registrationForm);
	  if(form6 != null)
	  {


		    registrationForm.setF2c_service(form6.getF2c_service());
		    registrationForm.setF2c_account_id(form6.getF2c_account_id());
		    registrationForm.setF2c_secret_key(form6.getF2c_secret_key());
		    registrationForm.setF2c_merchant_reference(form6.getF2c_merchant_reference());
		    registrationForm.setF2c_return_url(form6.getF2c_return_url());
		    registrationForm.setF2c_notification_url(form6.getF2c_notification_url());
		    registrationForm.setHeader_image(form6.getHeader_image());
		    registrationForm.setHeader_background_color(form6.getHeader_background_color());
		    registrationForm.setHeader_bottom_border_color(form6.getHeader_bottom_border_color());
		    if(form6.getStore_card().equals("1"))
		    {
		    registrationForm.setStore_card("Yes");
		    view.addObject("isStoreCard","Yes");
		    }
		    else
		    {
			    registrationForm.setStore_card("No");
			    view.addObject("isStoreCard","No");

		    }
		    
		    if(form6.getDisplay_customer_email().equals("1"))
		    {
		    	registrationForm.setDisplay_customer_email("Yes");
		    	view.addObject("isEmail","Yes");
		    }
		    else
		    {
		    	registrationForm.setDisplay_customer_email("no");
		    	view.addObject("isEmail","No");

		    }
		
		   
		    registrationForm.setDisplay_customer_email(form6.getDisplay_customer_email());
		   
		    registrationForm.setF2c_custom_data(form6.getF2c_custom_data());
		    

		    
	  }
	  else
	  {
		  registrationForm.setStore_card("Yes");
		    view.addObject("isStoreCard","Yes");
		  registrationForm.setDisplay_customer_email("Yes");
	    	view.addObject("isEmail","Yes");
	  }
	  
	  view.addObject("registrationForm", registrationForm);
	  return view; 
	}
	
	@RequestMapping(value={"/saveAlipayPayConnection"}, method={RequestMethod.POST})
	public ModelAndView saveOrUpdateAlipayPayConnection(@ModelAttribute("registrationForm") AlipayWalletVO payConnection, BindingResult result,HttpServletRequest request) {
		ModelAndView view = new ModelAndView("setupConnection");
		Object user_id =  request.getSession().getAttribute("user_id");
		//check if data exists in table mapped with user_id
		if(user_id != null)
		{
			payConnection.setUser_id(user_id.toString());
			System.out.println(user_id);
			AlipayWalletVO info = userService.getAlipayRecord(payConnection);
			//if exits update

			if (info == null) {
				
				payConnection.setCreated_date(currentTimestamp);
				userService.SaveAlipayRecord(payConnection);
			} else {
				payConnection.setId(info.getId());
				payConnection.setCreated_date(currentTimestamp);
				userService.UpdateAlipayRecord(payConnection);
			}
			view.setViewName("redirect:/setupConnection#alipay_f2f");

			return view;
		}
		else
		{
			view.setViewName("redirect:/webLogin");
			return view;
		}
	}
	
	
	@RequestMapping(value={"/saveAlipayPayOnlineConnection"}, method={RequestMethod.POST})
	public ModelAndView saveOrUpdateAlipayOnlinePayConnection(@ModelAttribute("registrationForm") AlipayWalletVO payConnection, BindingResult result,HttpServletRequest request) {
		ModelAndView view = new ModelAndView("setupPayConnection");
		Object user_id =  request.getSession().getAttribute("user_id");
		//check if data exists in table mapped with user_id
		if(user_id != null)
		{
			payConnection.setUser_id(user_id.toString());
			System.out.println(user_id);
			AlipayWalletVO info = userService.getAlipayOnlineRecord(payConnection);
			//			//if exits update

			if (info == null) {
				payConnection.setSubject("Alipay");
				payConnection.setSign_type("MD5");
				payConnection.setUrl("https://openapi.alipaydev.com/gateway.do?");
				payConnection.setAlipay_online_created_date(currentTimestamp);
				
				int methodId = payConnection.getAlipay_supported_method();
				if(methodId == 6){
					payConnection.setOnline_status(true);
				}else{
					payConnection.setOnline_status(false);
				}
				
				//save
				userService.SaveAlipayOnlineRecord(payConnection);
				
			} else {
				payConnection.setId(info.getId());
				payConnection.setSubject("Alipay");
				payConnection.setSign_type("MD5");
				payConnection.setUrl("https://openapi.alipaydev.com/gateway.do?");
				payConnection.setAlipay_online_created_date(currentTimestamp);
			
				int methodId = payConnection.getAlipay_supported_method();
				if(methodId == 6){
					payConnection.setOnline_status(true);
				}else{
					payConnection.setOnline_status(false);
				}
				
				//update
				userService.UpdateAlipayOnlineRecord(payConnection);
			}
			view.setViewName("redirect:/setupConnection#alipay_online");

			return view;
		}
		else
		{
			view.setViewName("redirect:/webLogin");
			return view;
		}
	}
	
	@RequestMapping(value = "/reconcil", method = RequestMethod.GET)
	public ModelAndView reconcile(@ModelAttribute("reconcileForm") ReconcillationBean reconcileForm,
		BindingResult result, HttpServletRequest request) {
		//ModelAndView view = new ModelAndView("reconcillation");
		
		HttpSession session = request.getSession();
		if(session.getAttribute("user_id") == null){
			session.invalidate();	
			return new ModelAndView("redirect:webLogin");
		}
		
		
		int permissionReconciliation = (int) request.getSession().getAttribute("permission_reconciliation");
		int allPermission = (int) request.getSession().getAttribute("all_permission");
		if(allPermission == 0 && permissionReconciliation == 0){
			return new ModelAndView("redirect:/webLogin");
		}
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		String dateString = dateFormat.format(date);//request.getParameter("selectDate");
		
		//DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		System.out.println("Selected date:"+dateString);
		String[] dateArr = dateString.split("-");
		//Date date = (Date)formatter.parse(dateString);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(dateArr[2]);
		stringBuilder.append("-");
		stringBuilder.append(dateArr[1]);
		stringBuilder.append("-");
		stringBuilder.append(dateArr[0]);
		String dateS = stringBuilder.toString();
		List<ReconcillationBean> resultList =  alipayReconcillationService.getReconciledTransactionByDate(dateS);
		
		System.out.println("Rsult count:"+resultList.size());
		Map<String,Object> map = new HashMap<>();				
		map.put("reconcileList", resultList);
		
		map.put("selectedDate", "Transaction Details for <strong>"+dateString+"</strong>");
		map.put("totalResults", "Total results found <strong>"+resultList.size()+"</strong>");
		
		ModelAndView view = new ModelAndView("reconcillation",map);
		
		return view;
	}
	
	@RequestMapping(value = "/reconcile", method = RequestMethod.POST)
	public ModelAndView reconcilePost(@ModelAttribute("reconcileForm") ReconcillationBean reconcileForm,
		HttpServletRequest request) {
		//BindingResult result, 		
		
		HttpSession session = request.getSession();
		if(session.getAttribute("user_id") == null){
			session.invalidate();
			session.invalidate();	
			return new ModelAndView("redirect:webLogin");
		}
		
		if( request.getParameter("selectDate")!=null){
			System.out.println("Date selected:"+ request.getParameter("selectDate"));
			String dateString = request.getParameter("selectDate");
							
			//DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			System.out.println("Selected date:"+dateString);
			String[] dateArr = dateString.split("-");
			//Date date = (Date)formatter.parse(dateString);
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(dateArr[2]);
			stringBuilder.append("-");
			stringBuilder.append(dateArr[1]);
			stringBuilder.append("-");
			stringBuilder.append(dateArr[0]);
			String dateS = stringBuilder.toString();
			List<ReconcillationBean> resultList =  alipayReconcillationService.getReconciledTransactionByDate(dateS);
			
			System.out.println("Rsult count:"+resultList.size());
			Map<String,Object> map = new HashMap<>();				
			map.put("reconcileList", resultList);
			
			map.put("selectedDate", "Transaction Details for <strong>"+dateString+"</strong>");
			map.put("totalResults", "Total results found <strong>"+resultList.size()+"</strong>");
			
			ModelAndView view = new ModelAndView("reconcillation",map);
			
			
			return view;
			
		}else{
			
			List<ReconcillationBean> resultList = new ArrayList<>();
			Map<String,Object> map = new HashMap<>();				
			map.put("reconcileList", (ReconcillationBean) resultList);			
			map.put("selectedDate", "");
			map.put("totalResults", "Total results found 0");
			ModelAndView view = new ModelAndView("reconcillation",map);
			return view;
			
		}						
		
	}
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
	public ModelAndView uploadReconcillationFile(@ModelAttribute("uploadFile") ReconcillationBean reconcileForm,
			BindingResult result, HttpServletRequest request) {
		ModelAndView view = new ModelAndView("uploadFile");
		HttpSession session = request.getSession();
		if(session.getAttribute("user_id") == null){
			session.invalidate();		
			view.setViewName("webLogin");
			return view;
		}
		return view;
	}


	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/saveFile", method = RequestMethod.POST)
	public @ResponseBody
	ModelAndView saveReconcillationFile(@RequestParam("reconcillationFile") MultipartFile file, HttpServletRequest request,RedirectAttributes redirectMap) {

		//BindingResult result,
		 System.out.println("In save file code!!");
		 
		 HttpSession session = request.getSession();
		 if(session.getAttribute("user_id") == null){			
			session.invalidate();	
			return new ModelAndView("redirect:webLogin");
		}
			
		 String uploadType = request.getParameter("upload_type");
		 
		 BufferedReader br;
	     String line = "";
	     String cvsSplitBy = "|";
	     String name= ""; 
	     String filePath = DynamicPaymentConstant.FTP_DESTINATION_PATH;
	    
	     System.out.println("In save file code!!");
	    
	    // AlipayReconcillationServiceImpl service = new AlipayReconcillationServiceImpl();
			
	 	if(request.getSession().getAttribute("user_id") == null){
			return new ModelAndView("redirect:webLogout");
		}
		    
		 if(uploadType.equals("ftp")){
			
         	boolean result = false;		 
			// boolean result = false;
			 result = sftpObj.downloadAlipayFtpFile();
         				 
			 if(result){
 
	             String generatedFile = DynamicPaymentConstant.getCurrentAlipayReconcileFile();   
				 File newFile = new File(DynamicPaymentConstant.FTP_DESTINATION_PATH+generatedFile); 
				 
				 newFile.setExecutable(true, false);
			     newFile.setReadable(true, false);
			     newFile.setWritable(true, false);
				 int transactionCount =0;
				 if (newFile.isFile() && newFile.canRead() ) {
						try {
							
							logger.info("Server File Location="
									+ newFile.getAbsolutePath());
							name = newFile.getName();
							br = new BufferedReader(new FileReader(newFile));
				            
							ReconcillationFileBean fileBean= new ReconcillationFileBean(); 
														
							ReconcillationBean reconcileBean = new ReconcillationBean();
				            
				            // HttpSession session = request.getSession(true);
				            int adminId = (int)session.getAttribute("user_id");
				            
				            Date myDate = new Date();
			                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
			                String myDateString = sdf.format(myDate);
			                
			                fileBean.setFileName(generatedFile);
							fileBean.setOwnerId(adminId);
							fileBean.setStatus(0);
							fileBean.setTransactionCount(transactionCount);
							fileBean.setUploadedDate(new Date());
							
							int lastId = alipayReconcillationService.saveReconcillationFile(fileBean);
							
							int i = 0; 
				            while ((line = br.readLine()) != null) {
				            	i++;
				            	if(i>2){
					            											
					                String[] reconcillationArray = line.split("\\|");
				
					                //System.out.println("line :"+line.toString());
					                
					               
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
					                reconcileBean.setFileId(lastId);
					                
					                transactionCount++;
					                
					                alipayReconcillationService.saveOrUpdate(reconcileBean); 	                
					              
				            	}
				            }
				            
				            fileBean.setTransactionCount(transactionCount);
				            fileBean.setStatus(DynamicPaymentConstant.RECONCILLATION_PROCESSED);
				            alipayReconcillationService.updateReconcillation(fileBean);
				            
				           String message = "<span style='color:green;'>You successfully uploaded and processed file=" + name+"</span>";
				           redirectMap.addFlashAttribute("message", message);
				            return new ModelAndView("redirect:reconcil");
				           //return "redirect:reconcil";
						} catch (Exception e) {
							e.printStackTrace();
							String message = "<span style='color:red;'>You failed to upload " + name + " => " + e.getMessage()+"</span>";
							 redirectMap.addFlashAttribute("message", message);
							return new ModelAndView("redirect:uploadFile");
							// return "redirect:uploadFile";
						}
					} else {
						String message = "<span style='color:red;'>You failed to upload " + name
								+ " because the file was empty.</span>";
						  redirectMap.addFlashAttribute("message", message);
						return new ModelAndView("redirect:uploadFile");
						//  return "redirect:uploadFile";
					}
				 
			 }else{
				 
				 String message = "<span style='color:red;'>The file is not downloaded since it is already processed.</span>";
					  redirectMap.addFlashAttribute("message", message);
					return new ModelAndView("redirect:uploadFile");
				 
			 }
		 }else{
			 				 				 
			if (!file.isEmpty()) {
				try {
					
					byte[] bytes = file.getBytes();
					name = file.getOriginalFilename();					
					// Creating the directory to store file
					//String rootPath = System.getProperty("catalina.home");
					File dir = new File(filePath);
					if (!dir.exists()){
						dir.mkdirs();
					}else{
						System.out.println("Diretcory exist:"+dir.getPath());
					}

					// Create the file on server
					File serverFile = new File(filePath + file.getOriginalFilename());
					
					serverFile.setExecutable(true, false);
					serverFile.setReadable(true, false);
					serverFile.setWritable(true, false);
				     
					BufferedOutputStream stream = new BufferedOutputStream(
							new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
					
					logger.info("Server File Location="
							+ serverFile.getAbsolutePath());
					
					br = new BufferedReader(new FileReader(serverFile));
		            
					ReconcillationBean reconcileBean = new ReconcillationBean();
		            
					ReconcillationFileBean fileBean= new ReconcillationFileBean(); 
										
		            //HttpSession session = request.getSession(true);
		            int adminId = (int)session.getAttribute("user_id");
		            int transactionCount = 0;
		            
		            fileBean.setFileName(file.getOriginalFilename());
					fileBean.setOwnerId(adminId);
					fileBean.setStatus(0);
					fileBean.setTransactionCount(transactionCount);
					fileBean.setUploadedDate(new Date());
					
					int lastId = alipayReconcillationService.saveReconcillationFile(fileBean);
							            
					int i = 0; 
		            while ((line = br.readLine()) != null) {
		            	
		            	i++;
		            	if(i>2){
			            	
		            		//line = "141135001090|2014042311001004900062558353|105.00|0.00|USD|2014-04-23 10:01:49|REVERSAL|";
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
			                reconcileBean.setFileId(lastId);
			                
			                alipayReconcillationService.saveOrUpdate(reconcileBean);
			                	                
			                transactionCount++;
			               // alipayReconcillationService.validateTransaction(reconcileBean);
		            	}
		            }
		            
		            fileBean.setTransactionCount(transactionCount);
		            fileBean.setStatus(DynamicPaymentConstant.RECONCILLATION_PROCESSED);
		            alipayReconcillationService.updateReconcillation(fileBean);
		            
		           String message = "<span style='color:green;'>You successfully uploaded and processed file=" + name+"</span>";
		           redirectMap.addFlashAttribute("message", message);
		            return new ModelAndView("redirect:reconcil");
		           //return "redirect:reconcil";
				} catch (Exception e) {
					e.printStackTrace();
					String message = "<span style='color:red;'>You failed to upload " + name + " => " + e.getMessage()+"</span>";
					 redirectMap.addFlashAttribute("message", message);
					return new ModelAndView("redirect:uploadFile");
					// return "redirect:uploadFile";
				}
			} else {
				String message = "<span style='color:red;'>You failed to upload " + name
						+ " because the file was empty.</span>";
				  redirectMap.addFlashAttribute("message", message);
				return new ModelAndView("redirect:uploadFile");
				//  return "redirect:uploadFile";
			}
		 }		 		
	}
	
	
	@RequestMapping(value = "/reconcileTransaction", method = RequestMethod.GET)
	public ModelAndView reconcileTransactions(@ModelAttribute("uploadFile") ReconcillationBean reconcileForm,
			BindingResult result, HttpServletRequest request) {
		ModelAndView view = new ModelAndView("reconcileTransaction");
		HttpSession session = request.getSession();
		if(session.getAttribute("user_id") == null){
			session.invalidate();		
			view.setViewName("webLogin");
			return view;
		}
		return view;
	}
	
	@RequestMapping(value = "/testTransaction", method = RequestMethod.POST)
	public String testTransactions(@ModelAttribute("adminId") ReconcillationBean reconcileForm,
			BindingResult result, HttpServletRequest request) {
		//ModelAndView view = new ModelAndView("reconcileTransaction");
		return "Test Transaction View";
	}
	
	//----------------------------report --------------------------------------------
	@RequestMapping(value = "/alipayTransactions", method = RequestMethod.GET)
	public ModelAndView alipayTransactions(@ModelAttribute("registrationForm") AlipayWalletVO registrationForm,
		AlipayWalletVO alipayWalletVO,BindingResult result, HttpServletRequest request) throws ParseException {
		ModelAndView view = new ModelAndView("alipayTransactions");
		String ipAddress = request.getRemoteAddr();
    	String sender = request.getRemoteHost();
    	DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    	List<AlipayAPIResponse> responseList = new ArrayList<AlipayAPIResponse>();
		HttpSession session = request.getSession();
				
		if(session.getAttribute("user_id") != null)
		{
			
			int permissionAlipayTransactions = (int) request.getSession().getAttribute("permission_alipay_transactions");
			int allPermission = (int) request.getSession().getAttribute("all_permission");
			if(allPermission == 0 && permissionAlipayTransactions == 0){
				view.setViewName("redirect:/webLogin");
				return view;
			}
			
			//get role_id and user_id from session 
    		int role_id = (int) request.getSession().getAttribute("role_id");
    	
    		//set user_id and role_id
    		alipayWalletVO.setUser_id(request.getSession().getAttribute("user_id").toString());
    		alipayWalletVO.setRole_id(role_id);
			
    		if(alipayWalletVO.getRole_id() == 3)
    		{
    			alipayWalletVO.setUserID(request.getSession().getAttribute("infidigiUserId").toString());	
    		}
    		else
    		{
    			if(alipayWalletVO.getRole_id() == 2)
    			{
    			alipayWalletVO.setUserID(request.getSession().getAttribute("user_id").toString());
    			}
    		}
				Map<Object, Object> transList = new HashMap<Object, Object>();
	    		responseList = alipayAPIService.getTransactionDetailsByCriteriaWeb(alipayWalletVO, ipAddress, sender);
	    		if (responseList == null || responseList.isEmpty()) {
	
	    			view.addObject("transList",transList);
	    		}
	    		else
	    		{
	    			int j=0;
					for (AlipayAPIResponse trans : responseList) {				   
							Map<Object, Object> map = new HashMap<Object, Object>();
							map.put("id", trans.getId());
							map.put("channel", trans.getChannel());
							map.put("pgPartnerTransId",trans.getPgPartnerTransId());
							map.put("TransactionDate", format.format(dateFormat.parse(trans.getTransactionDate())));
							map.put("mcTransAmount",trans.getMcTransAmount());
							map.put("pgMerchantTransactionId", trans.getPgMerchantTransactionId());
							map.put("mcParticular",trans.getMcItemName());
							map.put("mcReference",trans.getReference());
							map.put("status",trans.getPgResultCode());
							transList.put(j,map);
							j++;
						}
							
	    			view.addObject("transList",transList);
	    			
	    			Map<Object, Object> Users = new HashMap<Object, Object>();
	    			Object infidigiAccountId = session.getAttribute("user_id");
	    			List<User> users = alipayAPIService.getUserList(infidigiAccountId.toString());
	    			int i=0;
	    			if(users != null)
	    			{
	    				for (User user : users) {
	    					Map<Object, Object> map = new HashMap<Object, Object>();
	    					map.put("id", user.getId());
	    					map.put("user_id",user.getId());
	    					map.put("Name", user.getFirstName());
	    					Users.put(i,map);
	    					i++;
	    				}
	    			}
	    			view.addObject("Users",Users);
	    		
	    		//storing object in session
	    		request.getSession().setAttribute("reportDataObj",alipayWalletVO);
	
			}
		}
		else
		{
			session.invalidate();  
			view.setViewName("/webLogin");			
			return new ModelAndView("redirect:webLogin");
				
		}
		return view;
	}
	
	@RequestMapping("/webSearch ")
    public ModelAndView Websearch(@ModelAttribute("search") AlipayWalletVO alipayWalletVO, HttpServletRequest request, Model model) throws ParseException {    	
    	String ipAddress = request.getRemoteAddr();
    	String sender = request.getRemoteHost();
    	ModelAndView view = new ModelAndView();
    	DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    	List<AlipayAPIResponse> responseList = new ArrayList<AlipayAPIResponse>();
    	HttpSession session = request.getSession();
    	if(session.getAttribute("user_id") != null)
    	{
    		
    		if(alipayWalletVO.getUserID().equals("undefined"))
    		{
    			alipayWalletVO.setUserID("");
    		}
    		//get role_id and user_id from session 
    		int role_id = (int) request.getSession().getAttribute("role_id");
    	
    		//set user_id and role_id
    		alipayWalletVO.setUser_id(request.getSession().getAttribute("user_id").toString());
    		alipayWalletVO.setRole_id(role_id);
    		
  
    		Map<Object, Object> transList = new HashMap<Object, Object>();
    		responseList = alipayAPIService.getTransactionDetailsByCriteriaWeb(alipayWalletVO, ipAddress, sender);
    		if (responseList == null || responseList.isEmpty()) {

    			view.addObject("transList",transList);
    		}
    		else
    		{
    			
				int j=0;
				for (AlipayAPIResponse trans : responseList) {				   
						Map<Object, Object> map = new HashMap<Object, Object>();
						map.put("id", trans.getId());
						map.put("channel", trans.getChannel());
						map.put("pgPartnerTransId",trans.getPgPartnerTransId());
						map.put("TransactionDate", format.format(dateFormat.parse(trans.getTransactionDate())));
						map.put("mcTransAmount",trans.getMcTransAmount());
						map.put("pgMerchantTransactionId", trans.getPgMerchantTransactionId());
						map.put("mcParticular",trans.getMcItemName());
						map.put("mcReference",trans.getMcReference());
						map.put("status",trans.getPgResultCode());
						transList.put(j,map);
						j++;
					}
						
    			view.addObject("transList",transList);
    			
    			Map<Object, Object> Users = new HashMap<Object, Object>();
    			Object infidigiAccountId = session.getAttribute("user_id");
    			List<User> users = alipayAPIService.getUserList(infidigiAccountId.toString());
    			int i=0;
    			if(users != null)
    			{
    				for (User user : users) {
    					Map<Object, Object> map = new HashMap<Object, Object>();
    					//map.put("id", user.getId());
    					map.put("user_id",user.getId());
    					Users.put(i,map);
    					i++;
    				}
    			}
    			view.addObject("Users",Users);

    		}
    		view.setViewName("reportTable");
    	}
    	
    	//storing object in session
		request.getSession().setAttribute("reportDataObj",alipayWalletVO);
    	return view;
    }
	
//	@JsonView(Views.Public.class)
//	 @RequestMapping("/Transaction ")
//	    public Map<String,Object> GetTransaction(@RequestBody AlipayWalletVO alipayWalletVO, HttpServletRequest request) {
//	      //  AlipayAPIResponse genericAPIResponse = new AlipayAPIResponse();
//	        String ipAddress = request.getRemoteAddr();
//	        String sender = request.getRemoteHost();
//	        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//	        Map<String,Object> map = new LinkedHashMap<String,Object>();
//	        AlipayAPIResponse genericAPIResponse = alipayAPIService.getTransactionDetails(alipayWalletVO.getPgPartnerTransId());
//	       // Map<Object, Object> map = new HashMap<Object, Object>();
//	        List<Object> validationStatus = new ArrayList<Object>();
//            if (genericAPIResponse != null) {
//        	
//			map.put("id", genericAPIResponse.getId());
//			map.put("channel", genericAPIResponse.getChannel());
////			map.put("pgPartnerTransId",trans.getPgPartnerTransId());
//			if(genericAPIResponse.getTransactionDate() != null)
//
//				try {
//				map.put("TransactionDate", format.format(dateFormat.parse(genericAPIResponse.getTransactionDate())));
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			map.put("mcTransAmount",genericAPIResponse.getMcTransAmount());
////			map.put("pgMerchantTransactionId", trans.getPgMerchantTransactionId());
////			map.put("mcParticular",trans.getMcItemName());
//			map.put("mcReference",genericAPIResponse.getMcReference());
//			validationStatus.add(map);
//        } 
//   
//	    		return map;
//
//
//	    }
	@RequestMapping(value = "/getTransactionDetailsById", method = RequestMethod.GET)
	public ModelAndView getTransactionDetailsById(HttpServletRequest request, RedirectAttributes redirectAttributes) throws ParseException {

		ModelAndView view = new ModelAndView("transactionDetails");
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		Object user_id = request.getSession().getAttribute("user_id");
		if(user_id ==null){
			view.setViewName("redirect:/login");
			return view;
		}
		
		int permissionAlipayTransactions = (int) request.getSession().getAttribute("permission_alipay_transactions");
		int allPermission = (int) request.getSession().getAttribute("all_permission");
		if(allPermission == 0 && permissionAlipayTransactions == 0){
			view.setViewName("redirect:/webLogin");
			return view;
		}
		
		List<AlipayAPIResponse> transData = new ArrayList<AlipayAPIResponse>();
		
		//fetch transaction details by id
		String transactionId = request.getParameter("id");
		//int role_id = (int) request.getSession().getAttribute("role_id");
		
		//fetch data from transaction
		transData = dashboardService.searchTransactionDataById(transactionId);
		Map<String,Object> mapNew = new LinkedHashMap<String,Object>();
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		if(!transData.isEmpty()){		
			for (AlipayAPIResponse trans : transData) {				   
				map.put("Channel", trans.getChannel());
				map.put("Alipay Transaction ID",trans.getPgPartnerTransId());
				map.put("Amount",trans.getMcTransAmount());
				map.put("Reference",trans.getMcReference());	
				map.put("Particulars",trans.getMcItemName());
				map.put("Gateway Status",trans.getPgIsSuccess());
				map.put("Sign", trans.getPgSign());
				map.put("Sign Type", trans.getPgSignType());
				map.put("Result Code",trans.getPgResultCode());
				map.put("Error",trans.getPgError());
				map.put("Alipay Buyer Login ID",trans.getPgAlipayBuyerLoginId());
				map.put("Alipay Buyer User ID",trans.getPgAlipayBuyerUserId());
				map.put("Alipay Transaction Refund ID",trans.getMerchantRefundId());
				map.put("Alipay Pay Time",trans.getPgAlipayPayTime());
				map.put("Alipay Reverse Time",trans.getPgAlipayReverseTime());
				map.put("Alipay Cancel Time",trans.getPgAlipayCancelTime());
				map.put("Merchant Currency",trans.getMcCurrency());
				map.put("Merchant User ID",trans.getInfidigiUserId());
				map.put("Exchange Rate",trans.getPgExchangeRate());
				map.put("Request Time",trans.getRequestTime());
				map.put("Ip Address",trans.getIpAddress());
				map.put("Remark",trans.getRemark());
				map.put("Transaction Date", format.format(dateFormat.parse(trans.getTransactionDate())));	
				
				List <AlipayAPIResponse> allTrans = alipayAPIService.getTransactionsOfID(trans.getPgPartnerTransId());
				for (AlipayAPIResponse alipayAPIResponse : allTrans) {
					if(alipayAPIResponse.getPgResultCode().equals(trans.getPgResultCode()))
					{
						continue;
						
					}
					else
					{
						
						mapNew.put("Channel", alipayAPIResponse.getChannel());
						mapNew.put("Alipay Transaction ID",alipayAPIResponse.getPgPartnerTransId());
						mapNew.put("Amount",alipayAPIResponse.getMcTransAmount());
						//mapNew.put("Reference",alipayAPIResponse.getMcReference());	
						//mapNew.put("Particulars",alipayAPIResponse.getMcItemName());
						mapNew.put("Gateway Status",alipayAPIResponse.getPgIsSuccess());
						//mapNew.put("Sign", alipayAPIResponse.getPgSign());
						//mapNew.put("Sign Type", alipayAPIResponse.getPgSignType());
						mapNew.put("Result Code",alipayAPIResponse.getPgResultCode());
						//mapNew.put("Error",alipayAPIResponse.getPgError());
						//mapNew.put("Alipay Buyer Login ID",alipayAPIResponse.getPgAlipayBuyerLoginId());
						//mapNew.put("Alipay Buyer User ID",alipayAPIResponse.getPgAlipayBuyerUserId());
						mapNew.put("Alipay Transaction Refund ID",alipayAPIResponse.getMerchantRefundId());
						mapNew.put("Alipay Pay Time",alipayAPIResponse.getPgAlipayPayTime());
					//	mapNew.put("Alipay Reverse Time",alipayAPIResponse.getPgAlipayReverseTime());
					//	mapNew.put("Alipay Cancel Time",alipayAPIResponse.getPgAlipayCancelTime());
					//	mapNew.put("Merchant Currency",alipayAPIResponse.getMcCurrency());
						mapNew.put("Merchant User ID",alipayAPIResponse.getInfidigiUserId());
					//	mapNew.put("Exchange Rate",alipayAPIResponse.getPgExchangeRate());
						mapNew.put("Request Time",alipayAPIResponse.getRequestTime());
						//mapNew.put("Ip Address",alipayAPIResponse.getIpAddress());
					//	mapNew.put("Remark",alipayAPIResponse.getRemark());
						mapNew.put("Transaction Date", format.format(dateFormat.parse(alipayAPIResponse.getTransactionDate())));
					}
				}
			}
		}
		view.addObject("otherTransData", mapNew);
		view.addObject("transData",map);	
		return view;
	}

	
	//----------------------------report --------------------------------------------
	
	//-----------------------------profile ------------------------------------------
	
	@RequestMapping(value = "/viewProfile", method = RequestMethod.POST)
	public ModelAndView viewProfile(@ModelAttribute("user") User userForm,
			BindingResult result, HttpServletRequest request) {
		ModelAndView view = new ModelAndView("profile");
		//Object user_id = request.getSession().getAttribute("user_id");
		int userId = (int) request.getSession().getAttribute("user_id");
		User user = userService.findById(userId);			
		view.addObject("FirstName", user.getFirstName());
		view.addObject("LastName", user.getLastName());
		view.addObject("UserName", user.getUsername());
		view.addObject("MobileNumber", user.getPhoneNo());
		view.addObject("Email", user.getEmail());
		view.addObject("CompanyName", user.getCompanyName());		
		return view;
	}


	@RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
	public ModelAndView UpdateProfile(@ModelAttribute("user") User user,HttpServletRequest request, BindingResult result, Model model, final RedirectAttributes redirectAttributes){
		ModelAndView view = new ModelAndView("profile");	
		Object user_id = request.getSession().getAttribute("user_id");						
		if(user_id ==null){
			view.setViewName("redirect:/webLogin");
			return view;
		} 
		
		int userId = (int) request.getSession().getAttribute("user_id");
		User userForm = userService.findById(userId);						
		user.setId((int) user_id);						
		user.setFirstName(request.getParameter("FirstName"));	
		user.setLastName(request.getParameter("LastName"));
		user.setUsername(userForm.getCompanyName());
		user.setCompanyName(request.getParameter("CompanyName"));
		user.setPhoneNo(request.getParameter("MobileNumber"));
		user.setEmail(userForm.getEmail());
		
		//update password
		if(request.getParameter("current_psswd") != null && !request.getParameter("current_psswd").isEmpty()){
			//check password 
			try {
				if(userForm.getInfidigiPassword().equals(DynamicPaymentConstant.getHashPassword(request.getParameter("current_psswd")))){
					//check password and confirm password
					if(!request.getParameter("new_psswd").isEmpty() && !request.getParameter("cfm_new_psswd").isEmpty()){
						//password match
						if(request.getParameter("new_psswd").equals(request.getParameter("cfm_new_psswd"))){
							user.setInfidigiPassword(DynamicPaymentConstant.getHashPassword(request.getParameter("new_psswd")));
						}else{
							
						}
					}else{
						
					}
					
				}else{
					//invalid password
					
				}
				
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{		
			user.setInfidigiPassword(userForm.getInfidigiPassword());
		}
		
		user.setStatus(userForm.getStatus());
		user.setVerified(userForm.getVerified());
		int userId1 = userService.update(user);

		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("message", "User details updated successfully");

		view.setViewName("redirect:/home");
		return view;
	}
	
	//-----------------------------profile ------------------------------------------
	
	//check user password
	@RequestMapping(value = "/checkUserPasswordold", method = RequestMethod.POST)
	public JSONArray checkUserPassword(@RequestBody User search,RedirectAttributes redirectAttributes,HttpServletRequest request){
		System.out.println(search.getInfidigiPassword());
		List<String> validationStatus = new ArrayList<String>();
		boolean status = false;
		try {
			status = dashboardService.checkUserPassword(DynamicPaymentConstant.getHashPassword(search.getInfidigiPassword()));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		if(status){
			validationStatus.add("true");
		}else{
			validationStatus.add("false");
		}
		
		JSONArray json1 = new JSONArray(validationStatus);
		return json1;
	}
	
	@RequestMapping(value = "/settlement", method = RequestMethod.GET)
	 public ModelAndView settlement(@ModelAttribute("settlementForm") SettlementBean settlementForm,
	  BindingResult result, HttpServletRequest request) {
	  //ModelAndView view = new ModelAndView("reconcillation");
	  
	  HttpSession session = request.getSession();
	  if(session.getAttribute("user_id") == null){
	   session.invalidate(); 
	   return new ModelAndView("redirect:webLogin");
	  }
	  
	  int permissionSettlement = (int) request.getSession().getAttribute("permission_settlement");
	  int allPermission = (int) request.getSession().getAttribute("all_permission");
	  if(allPermission == 0 && permissionSettlement == 0){
		return new ModelAndView("redirect:webLogin");
	  }
	  
	  DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	  Date date = new Date();
	  System.out.println(dateFormat.format(date));
	  String endDateString = dateFormat.format(date);//request.getParameter("selectDate");
	  
	  
	  Calendar cal = Calendar.getInstance();
	  cal.add(Calendar.DATE, -1);    
	  String startDateString=  dateFormat.format(cal.getTime());  
	           
	  //DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	  System.out.println("Selected date:"+endDateString);
	  String[] dateArr = endDateString.split("-");
	  //Date date = (Date)formatter.parse(dateString);
	  StringBuilder stringBuilder = new StringBuilder();
	  stringBuilder.append(dateArr[2]);
	  stringBuilder.append("-");
	  stringBuilder.append(dateArr[1]);
	  stringBuilder.append("-");
	  stringBuilder.append(dateArr[0]);
	  String dateEnd = stringBuilder.toString();
	  
	  
	  String[] dateArr1 = startDateString.split("-");
	  //Date date = (Date)formatter.parse(dateString);
	  StringBuilder stringBuilder1 = new StringBuilder();
	  stringBuilder1.append(dateArr1[2]);
	  stringBuilder1.append("-");
	  stringBuilder1.append(dateArr1[1]);
	  stringBuilder1.append("-");
	  stringBuilder1.append(dateArr1[0]);
	  String dateStart = stringBuilder1.toString();
	  
	  SettlementFileBean fileBean = new SettlementFileBean();
	  fileBean.setStartDate(dateStart);
	  fileBean.setEndDate(dateEnd);
	  
	  List<SettlementFileBean> resultList =  settlementService.getSettlementFiles(fileBean);
	  
	  //System.out.println("Result count:"+resultList.size());
	  Map<String,Object> map = new HashMap<>();    
	  map.put("settlementList", resultList);
	  
	  map.put("selectedDate", "Transaction Details for <strong>"+endDateString+"</strong>");
	  map.put("totalResults", "Total results found <strong> "+resultList.size()+" </strong>");
	  
	  ModelAndView view = new ModelAndView("settlement",map);
	  
	  return view;
	 }
	
	@RequestMapping(value = "/settlementPost", method = RequestMethod.POST)
	public ModelAndView settlementList(@ModelAttribute("settlementForm") SettlementBean settlementForm,
		BindingResult result, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		if(session.getAttribute("user_id") == null){
			session.invalidate();	
			return new ModelAndView("redirect:webLogin");
		}				
		
		if( request.getParameter("start_date")!=null &&  request.getParameter("end_date")!=null){
			
			System.out.println("Date selected:"+ request.getParameter("selectDate"));
			String start_date = request.getParameter("start_date");
			String end_date = request.getParameter("end_date");
			
			if(request.getParameter("start_date").isEmpty()){
				
	             DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	             Calendar cal = Calendar.getInstance();
	             cal.add(Calendar.DATE, -1);    
	             start_date=  dateFormat.format(cal.getTime());	            
			}
			
			if(request.getParameter("end_date").isEmpty()){
				 Date settlementDate = new Date();
	             SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	             end_date= sdf.format(settlementDate);
	             
			}
			
			//DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			System.out.println("Selected date:"+start_date);
			String[] startDateArr = start_date.split("-");
			//Date date = (Date)formatter.parse(dateString);
			StringBuilder stringBuilder1 = new StringBuilder();
			stringBuilder1.append(startDateArr[2]);
			stringBuilder1.append("-");
			stringBuilder1.append(startDateArr[1]);
			stringBuilder1.append("-");
			stringBuilder1.append(startDateArr[0]);
			String dateStart = stringBuilder1.toString();
			
			String[] endDateArr = end_date.split("-");
			//Date date = (Date)formatter.parse(dateString);
			StringBuilder stringBuilder2 = new StringBuilder();
			stringBuilder2.append(endDateArr[2]);
			stringBuilder2.append("-");
			stringBuilder2.append(endDateArr[1]);
			stringBuilder2.append("-");
			stringBuilder2.append(endDateArr[0]);
			String dateEnd = stringBuilder2.toString();
			
			SettlementFileBean fileBean = new SettlementFileBean();
			fileBean.setStartDate(dateStart);
			fileBean.setEndDate(dateEnd);
			
			List<SettlementFileBean> resultList =  settlementService.getSettlementFiles(fileBean);
			
			//System.out.println("Result count:"+resultList.size());
			Map<String,Object> map = new HashMap<>();				
			map.put("settlementList", resultList);
			
			map.put("selectedDate", "Transaction Details for date from <strong>"+dateStart+" to "+dateEnd+"</strong>");
			map.put("totalResults", "Total results found <strong> "+resultList.size()+" </strong>");
			
			ModelAndView view = new ModelAndView("settlement",map);
			
			return view;
			
		}else{
			
			List<SettlementFileBean> resultList = new ArrayList<>();
			Map<String,Object> map = new HashMap<>();				
			map.put("settlementList", (SettlementFileBean) resultList);			
			map.put("selectedDate", "");
			map.put("totalResults", "Total results found 0");
			ModelAndView view = new ModelAndView("settlement",map);
			return view;
			
		}	
	}
	
	@RequestMapping(value = "/settledTransactions", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView settledTransactions(@ModelAttribute("settlementForm") SettlementBean settlementForm,
		BindingResult result, HttpServletRequest request) throws ParseException {
		
		HttpSession session = request.getSession();
		if(session.getAttribute("user_id") == null){
			session.invalidate();	
			return new ModelAndView("redirect:webLogin");
		}				
		
		if( request.getParameter("id")!=null){
						
			
			int id = Integer.parseInt(request.getParameter("id"));
			
			SettlementFileBean fileBean = new SettlementFileBean();
			fileBean.setId(id);
			
			SettlementFileBean settlementFileBean =  settlementService.getSettlementFile(fileBean);
			
			System.out.println("settlement id:"+id);
			
			String fileName = settlementFileBean.getFileName();		
			
			List<SettlementBean> resultList =  settlementService.getSettledTransactions(settlementFileBean);
			//List<SettlementBean> resultList =  settlementService.getSettledTransactions(id);
									
			//System.out.println("Result count:"+resultList.size());
			Map<String,Object> map = new HashMap<>();				
			map.put("settlementList", resultList);
			
			map.put("selectedDate", "Transaction Details for the settlement file <strong>"+fileName+" </strong>");
			map.put("totalResults", "Total results found <strong> "+resultList.size()+" </strong>");
			
			ModelAndView view = new ModelAndView("settlementList",map);
			
			return view;
			
		}else if( request.getParameter("start_date")!=null && request.getParameter("end_date")!=null){
			
			String start_date = request.getParameter("start_date");
			String end_date = request.getParameter("end_date");
						
			SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

			String formatedStartDate = myFormat.format(fromUser.parse(start_date));
			String formatedEndDate = myFormat.format(fromUser.parse(end_date));
						
			SettlementFileBean fileBean = new SettlementFileBean();			
			fileBean.setStartDate(formatedStartDate);
			fileBean.setEndDate(formatedEndDate);
			
			SettlementFileBean settlementFileBean =  settlementService.getSettlementFile(fileBean);
			
			System.out.println("Start date:"+ start_date+" end date:"+end_date);
						
			String fileName = settlementFileBean.getFileName();
			settlementFileBean.setStartDate(formatedStartDate);
			settlementFileBean.setEndDate(formatedEndDate);
			
			List<SettlementBean> resultList =  settlementService.getSettledTransactions(settlementFileBean);
			//List<SettlementBean> resultList =  settlementService.getSettledTransactions(id);
									
			//System.out.println("Result count:"+resultList.size());
			Map<String,Object> map = new HashMap<>();				
			map.put("settlementList", resultList);
			
			map.put("selectedDate", "Transaction Details for the settlement file <strong>"+fileName+" </strong>");
			map.put("totalResults", "Total results found <strong> "+resultList.size()+" </strong>");
			
			ModelAndView view = new ModelAndView("settlementList",map);
			
			return view;
			
		}else{
			
			List<SettlementBean> resultList = new ArrayList<>();
			Map<String,Object> map = new HashMap<>();				
			map.put("settlementList", (SettlementBean) resultList);			
			map.put("selectedDate", "");
			map.put("totalResults", "Total results found 0");
			ModelAndView view = new ModelAndView("settlementList",map);
			return view;
			
		}	
	}
	
	@RequestMapping(value = "/uploadSettlement", method = RequestMethod.GET)
	public ModelAndView uploadSettlementFile(@ModelAttribute("uploadSettlment") SettlementBean settlementForm,
			BindingResult result, HttpServletRequest request) {
		ModelAndView view = new ModelAndView("uploadSettlement");
		HttpSession session = request.getSession();
		if(session.getAttribute("user_id") == null){
			session.invalidate();		
			//view.setViewName("webLogin");
			return new ModelAndView("redirect:webLogin");
		}
		return view;
	}
	
	@RequestMapping(value = "/deleteSettlement", method = RequestMethod.GET)
	public ModelAndView deleteSettlementFile(HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectMap) {
		ModelAndView view = new ModelAndView("settlement");
		HttpSession session = request.getSession();
		if(session.getAttribute("user_id") == null){
			session.invalidate();		
			//view.setViewName("webLogin");
			return new ModelAndView("redirect:webLogin");
		}
		
		if( request.getParameter("id")!=null){
						
			int id = Integer.parseInt(request.getParameter("id"));
			settlementService.deleteSettlementFileRecord(id);
			String message = "<span style='color:green;'> The settlement record is deleted succsfully!</span>";
			redirectMap.addFlashAttribute("message", message);
			return new ModelAndView("redirect:settlement");
			
		}else{
			String message = "<span style='color:red;'> Failed to delete settlement record from system!</span>";
			redirectMap.addFlashAttribute("message", message);
			return new ModelAndView("redirect:settlement");
		}
		
	}

	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/saveSettlementFile", method = RequestMethod.POST)
	public @ResponseBody
	ModelAndView saveSettlementFile(@RequestParam("settlementFile") MultipartFile file, HttpServletRequest request,RedirectAttributes redirectMap) {

		//BindingResult result,
		 System.out.println("In save file code!!");
		 
		 HttpSession session = request.getSession();
		 if(session.getAttribute("user_id") == null){			
			session.invalidate();	
			return new ModelAndView("redirect:webLogin");
		}
			
		 String uploadType = request.getParameter("upload_type");
		 
		 BufferedReader br;
	     String line = "";
	     String cvsSplitBy = "|";
	     String name= ""; 
	     String filePath = DynamicPaymentConstant.SETTLEMENT_DESTINATION_PATH;
	    
	     System.out.println("In save file code!!");
	    
	    // AlipayReconcillationServiceImpl service = new AlipayReconcillationServiceImpl();
			
	 	if(request.getSession().getAttribute("user_id") == null){
			return new ModelAndView("redirect:webLogout");
		}
	 	
	 	SettlementFileBean settlementFileBean = new SettlementFileBean();
		    
		 if(uploadType.equals("ftp")){
			   
			 boolean result = false;		 
				// boolean result = false;
				 result = sftpObj.downloadAlipaySettlementFile();
	         				 
				 if(result){
	 
		             String generatedFile = DynamicPaymentConstant.getCurrentAlipaySettlementFile();   
					 File newFile = new File(DynamicPaymentConstant.FTP_DESTINATION_PATH+generatedFile); 
					 
					 newFile.setExecutable(true, false);
				     newFile.setReadable(true, false);
				     newFile.setWritable(true, false);
					 int transactionCount =0;
					 if (newFile.isFile() && newFile.canRead() ) {
							try {
								
								logger.info("Server File Location="
										+ newFile.getAbsolutePath());
								name = newFile.getName();
								br = new BufferedReader(new FileReader(newFile));
					            
								SettlementBean settlementBean = new SettlementBean();
						            
						            //HttpSession session = request.getSession(true);
						            int adminId = (int)session.getAttribute("user_id");
						            
									int i = 0; 
									int transCount = 0;
									
									Date settlementDate = new Date();
					                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
					                String sDateString = sdf.format(settlementDate);
						            
						            settlementFileBean.setFileName(file.getOriginalFilename());
						            settlementFileBean.setTransactionCount(transCount);
						            settlementFileBean.setUploadedDate(sDateString);
						            settlementFileBean.setSettlementStatus(DynamicPaymentConstant.ALIPAY_SETTLEMENT_NOTVALIDATED);
						            int lastId = settlementService.logFile(settlementFileBean);
						            System.out.println("last inert id:"+lastId);
						            
						            while ((line = br.readLine()) != null) {
						            	i++;
						            	if(i>2){
							            									 
							                // use comma as separator
						            		//line = "141135001090|2014042311001004900062558353|105.00|0.00|USD|2014-04-23 10:01:49|REVERSAL|";
							                String[] settlementArray = line.split("\\|");
						
							                System.out.println("line string :"+line.toString());
							                System.out.println("line array:"+settlementArray.toString());
							                
							                Date myDate = new Date();
							                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
							                String myDateString = sdf1.format(myDate);
							                
							                settlementBean.setOwnerId(adminId);
							                settlementBean.setPartnerTransactionId(settlementArray[0]);
							                settlementBean.setTransactionId(settlementArray[1]);
							                settlementBean.setAmount(settlementArray[2]);
							                settlementBean.setRmbAmount(settlementArray[3]);
							                settlementBean.setFee(settlementArray[4]);
							                settlementBean.setSettlement(settlementArray[5]);
							                settlementBean.setRmbSettlement(settlementArray[6]);
							                settlementBean.setCurrency(settlementArray[7]);
							                settlementBean.setRate(settlementArray[8]);
							                settlementBean.setPaymentTime(settlementArray[9]);
							                settlementBean.setSettlementTime(settlementArray[10]);
							                settlementBean.setType(settlementArray[11]); 
							                settlementBean.setStatus(settlementArray[12]);				               
							                settlementBean.setRemark(settlementArray[13]);
							                settlementBean.setUploadedDate(myDateString);
							                settlementBean.setSettlementId(lastId);
							                
							                settlementService.saveOrUpdate(settlementBean);
							             
							                	                
							               //alipayReconcillationService.validateTransaction(reconcileBean);
							                transCount++;
						            	}
						            }	
						           			           
						           settlementFileBean.setTransactionCount(transCount);
						           settlementService.updateSettlement(settlementFileBean);
						            
					           String message = "<span style='color:green;'>You successfully uploaded and processed file=" + name+"</span>";
					           redirectMap.addFlashAttribute("message", message);
					            return new ModelAndView("redirect:settlement");
					           //return "redirect:reconcil";
							} catch (Exception e) {
								e.printStackTrace();
								String message = "<span style='color:red;'>You failed to upload " + name + " => " + e.getMessage()+"</span>";
								 redirectMap.addFlashAttribute("message", message);
								return new ModelAndView("redirect:uploadSettlement");
								// return "redirect:uploadFile";
							}
						} else {
							String message = "<span style='color:red;'>You failed to upload " + name
									+ " because the file was empty.</span>";
							  redirectMap.addFlashAttribute("message", message);
							return new ModelAndView("redirect:uploadSettlement");
							//  return "redirect:uploadFile";
						}
					 
				 }else{
					 
					 String message = "<span style='color:red;'>The file is not downloaded since it is already processed.</span>";
						  redirectMap.addFlashAttribute("message", message);
						return new ModelAndView("redirect:uploadSettlement");
					 
				 }
			 
		 }else{
			 				 				 
				if (!file.isEmpty()) {
					try {
						byte[] bytes = file.getBytes();
						
						name = file.getOriginalFilename();
						
						boolean checkFile = settlementService.checkFile(settlementFileBean);
						if(checkFile){
							String message = "<span style='color:red;'> Sorry the file is already beign processed!</span>";
							 redirectMap.addFlashAttribute("message", message);
							return new ModelAndView("redirect:uploadSettlement");
						}
						
						// Creating the directory to store file
						//String rootPath = System.getProperty("catalina.home");
						File dir = new File(filePath);
						if (!dir.exists()){
							dir.mkdirs();
						}else{
							System.out.println("Diretcory exist:"+dir.getPath());
						}

						// Create the file on server
						File serverFile = new File(filePath + file.getOriginalFilename());
						
						serverFile.setExecutable(true, false);
						serverFile.setReadable(true, false);
						serverFile.setWritable(true, false);
					     
						BufferedOutputStream stream = new BufferedOutputStream(
								new FileOutputStream(serverFile));
						stream.write(bytes);
						stream.close();

						
						logger.info("Server File Location="
								+ serverFile.getAbsolutePath());
						
						 br = new BufferedReader(new FileReader(serverFile));
			            SettlementBean settlementBean = new SettlementBean();
			            
			            //HttpSession session = request.getSession(true);
			            int adminId = (int)session.getAttribute("user_id");
			            
						int i = 0; 
						int transCount = 0;
						
						Date settlementDate = new Date();
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
		                String sDateString = sdf.format(settlementDate);
			            
			            settlementFileBean.setFileName(file.getOriginalFilename());
			            settlementFileBean.setTransactionCount(transCount);
			            settlementFileBean.setUploadedDate(sDateString);
			            settlementFileBean.setSettlementStatus(DynamicPaymentConstant.ALIPAY_SETTLEMENT_NOTVALIDATED);
			            int lastId = settlementService.logFile(settlementFileBean);
			            System.out.println("last inert id:"+lastId);
			            
			            while ((line = br.readLine()) != null) {
			            	i++;
			            	if(i>2){
				            									 
				                // use comma as separator
			            		//line = "141135001090|2014042311001004900062558353|105.00|0.00|USD|2014-04-23 10:01:49|REVERSAL|";
				                String[] settlementArray = line.split("\\|");
			
				                System.out.println("line string :"+line.toString());
				                System.out.println("line array:"+settlementArray.toString());
				                
				                Date myDate = new Date();
				                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
				                String myDateString = sdf1.format(myDate);
				                
				                settlementBean.setOwnerId(adminId);
				                settlementBean.setPartnerTransactionId(settlementArray[0]);
				                settlementBean.setTransactionId(settlementArray[1]);
				                settlementBean.setAmount(settlementArray[2]);
				                settlementBean.setRmbAmount(settlementArray[3]);
				                settlementBean.setFee(settlementArray[4]);
				                settlementBean.setSettlement(settlementArray[5]);
				                settlementBean.setRmbSettlement(settlementArray[6]);
				                settlementBean.setCurrency(settlementArray[7]);
				                settlementBean.setRate(settlementArray[8]);
				                settlementBean.setPaymentTime(settlementArray[9]);
				                settlementBean.setSettlementTime(settlementArray[10]);
				                settlementBean.setType(settlementArray[11]); 
				                settlementBean.setStatus(settlementArray[12]);				               
				                settlementBean.setRemark(settlementArray[13]);
				                settlementBean.setUploadedDate(myDateString);
				                settlementBean.setSettlementId(lastId);
				                
				                settlementService.saveOrUpdate(settlementBean);
				             
				                	                
				               //alipayReconcillationService.validateTransaction(reconcileBean);
				                transCount++;
			            	}
			            }	
			           			           
			           settlementFileBean.setTransactionCount(transCount);
			           settlementService.updateSettlement(settlementFileBean);
			            
			           String message = "<span style='color:green;'>You successfully uploaded and processed file=" + name+"</span>";
			           redirectMap.addFlashAttribute("message", message);
			            return new ModelAndView("redirect:settlement");
			           //return "redirect:reconcil";
					} catch (Exception e) {
						e.printStackTrace();
						String message = "<span style='color:red;'>You failed to upload " + name + " => " + e.getMessage()+"</span>";
						 redirectMap.addFlashAttribute("message", message);
						return new ModelAndView("redirect:uploadSettlement");
						// return "redirect:uploadFile";
					}
				} else {
					String message = "<span style='color:red;'>You failed to upload " + name
							+ " because the file was empty.</span>";
					  redirectMap.addFlashAttribute("message", message);
					return new ModelAndView("redirect:uploadSettlement");
					//  return "redirect:uploadFile";
				}
				
				
		 }
		/* if(result.hasErrors()){
			 //return new ModelAndView("uploadFile");
			 return "redirect:uploadFile";
		 }*/
		
	}
	
	@RequestMapping(value = "/validateSettlement", method = RequestMethod.GET)
	public ModelAndView validateSettlementFile(HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectMap) {
		ModelAndView view = new ModelAndView("settlement");
		HttpSession session = request.getSession();
		if(session.getAttribute("user_id") == null){
			session.invalidate();		
			//view.setViewName("webLogin");
			return new ModelAndView("redirect:webLogin");
		}
		
		if( request.getParameter("id")!=null){
						
			int id = Integer.parseInt(request.getParameter("id"));
			SettlementFileBean bean = settlementService.populateSettlementFileBean(id);
			String message = "";
			if(bean!=null){
				settlementService.validateSettlementTransactions(bean);
				message = "<span style='color:green;'> The settlement record is validated succsfully!</span>";
			}else{
				message = "<span style='color:red;'> Failed to validated settlement record!</span>";
			}
			redirectMap.addFlashAttribute("message", message);
			return new ModelAndView("redirect:settlement");
			
		}else{
			String message = "<span style='color:red;'> Failed to validated settlement record!</span>";
			redirectMap.addFlashAttribute("message", message);
			return new ModelAndView("redirect:settlement");
		}	
	}
	
	
	//----------------------------refund --------------------------------------------
	@RequestMapping(value = "/refundListing", method = RequestMethod.GET)
	public ModelAndView alipayRefund(@ModelAttribute("registrationForm") AlipayWalletVO registrationForm,
		AlipayWalletVO alipayWalletVO,BindingResult result, HttpServletRequest request) throws ParseException {
		ModelAndView view = new ModelAndView("alipayRefund");
		String ipAddress = request.getRemoteAddr();
    	String sender = request.getRemoteHost();
    	DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    	List<AlipayAPIResponse> responseList = new ArrayList<AlipayAPIResponse>();
		HttpSession session = request.getSession();
		if(session.getAttribute("user_id") != null)
		{
			
			int permissionRefund = (int) request.getSession().getAttribute("permission_refund");
			int allPermission = (int) request.getSession().getAttribute("all_permission");
			if(allPermission == 0 && permissionRefund == 0){
				view.setViewName("redirect:/webLogin");
				return view;
			}
			
			//get role_id and user_id from session 
    		int role_id = (int) request.getSession().getAttribute("role_id");
    		
    		//set user_id and role_id
    		alipayWalletVO.setUser_id(request.getSession().getAttribute("user_id").toString());
    		alipayWalletVO.setRole_id(role_id);
    		if(alipayWalletVO.getRole_id() == 3)
    		{
    			alipayWalletVO.setUserID(request.getSession().getAttribute("infidigiUserId").toString());	
    		}
    		else
    		{
    			alipayWalletVO.setUserID(request.getSession().getAttribute("user_id").toString());	
    		}
			
			
				Map<Object, Object> transList = new HashMap<Object, Object>();
	    		responseList = alipayAPIService.getRefundTransactionDetailsByCriteriaWeb(alipayWalletVO, ipAddress, sender);
//	    		System.out.println("list size in trans class" + responseList.size());
	    		if (responseList == null || responseList.isEmpty()) {
	
	    			view.addObject("transList",transList);
	    		}
	    		else
	    		{
	    			int j=0;
					for (AlipayAPIResponse trans : responseList) {
						System.out.println( trans.getPgResultCode());
							Map<Object, Object> map = new HashMap<Object, Object>();
//							System.out.println("Status == "+trans.getPgResultCode());
							if(trans.getPgResultCode().equals("REFUND") || trans.getPgResultCode().equals("CANCELLED"))
							{
								j++;
								
							}
							else
							{
//							System.out.println("refund not done for"+trans.getPgPartnerTransId());
							map.put("id", trans.getId());
							map.put("channel", trans.getChannel());
							map.put("pgPartnerTransId",trans.getPgPartnerTransId());
							map.put("TransactionDate", format.format(dateFormat.parse(trans.getTransactionDate())));
							map.put("mcTransAmount",trans.getMcTransAmount());
							map.put("pgMerchantTransactionId", trans.getPgMerchantTransactionId());
							map.put("mcParticular",trans.getMcItemName());
							map.put("mcReference",trans.getMcReference());
							map.put("status",trans.getPgResultCode());
							transList.put(j,map);
							j++;
							}
						}
					}	
	    			view.addObject("transList",transList);
	    			
	    			Map<Object, Object> Users = new HashMap<Object, Object>();
	    			Object infidigiAccountId = session.getAttribute("user_id");
	    			List<User> users = alipayAPIService.getUserList(infidigiAccountId.toString());
	    			int i=0;
	    			if(users != null)
	    			{
	    				for (User user : users) {
	    					Map<Object, Object> map = new HashMap<Object, Object>();
	    					map.put("id", user.getId());
	    					map.put("user_id",user.getId());
	    					Users.put(i,map);
	    					i++;
	    				}
	    			}
	    			view.addObject("Users",Users);
	    		
	    		//storing object in session
	    		request.getSession().setAttribute("reportDataObj",alipayWalletVO);
	
			
		}
		else
		{
			session.invalidate();  
			view.setViewName("/webLogin");
			return view;
		}
		return view;
	}
	
	@RequestMapping("/refundSearch ")
    public ModelAndView RefundSearch(@ModelAttribute("search") AlipayWalletVO alipayWalletVO, HttpServletRequest request, Model model) throws ParseException {    	
    	String ipAddress = request.getRemoteAddr();
    	String sender = request.getRemoteHost();
    	ModelAndView view = new ModelAndView();
    	DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    	List<AlipayAPIResponse> responseList = new ArrayList<AlipayAPIResponse>();
    	HttpSession session = request.getSession();
    	if(session.getAttribute("user_id") != null)
    	{
    		
    		if(alipayWalletVO.getUserID().equals("undefined"))
    		{
    			alipayWalletVO.setUserID("");
    		}
    		//get role_id and user_id from session 
    		int role_id = (int) request.getSession().getAttribute("role_id");
    	
    		//set user_id and role_id
    		alipayWalletVO.setUser_id(request.getSession().getAttribute("user_id").toString());
    		alipayWalletVO.setRole_id(role_id);
    		
  
    		Map<Object, Object> transList = new HashMap<Object, Object>();
    		responseList = alipayAPIService.getTransactionDetailsByCriteriaWeb(alipayWalletVO, ipAddress, sender);
    		if (responseList == null || responseList.isEmpty()) {

    			view.addObject("transList",transList);
    		}
    		else
    		{
    			
				int j=0;
				for (AlipayAPIResponse trans : responseList) {				   
						Map<Object, Object> map = new HashMap<Object, Object>();
						map.put("id", trans.getId());
						map.put("channel", trans.getChannel());
						map.put("pgPartnerTransId",trans.getPgPartnerTransId());
						map.put("TransactionDate", format.format(dateFormat.parse(trans.getTransactionDate())));
						map.put("mcTransAmount",trans.getMcTransAmount());
						map.put("pgMerchantTransactionId", trans.getPgMerchantTransactionId());
						map.put("mcParticular",trans.getMcItemName());
						map.put("mcReference",trans.getMcReference());
						map.put("status",trans.getPgResultCode());
						transList.put(j,map);
						j++;
					}
						
    			view.addObject("transList",transList);
    			
    			Map<Object, Object> Users = new HashMap<Object, Object>();
    			Object infidigiAccountId = session.getAttribute("user_id");
    			List<User> users = alipayAPIService.getUserList(infidigiAccountId.toString());
    			int i=0;
    			if(users != null)
    			{
    				for (User user : users) {
    					Map<Object, Object> map = new HashMap<Object, Object>();
    					map.put("id", user.getId());
    					map.put("user_id",user.getInfidigiUserId());
    					Users.put(i,map);
    					i++;
    				}
    			}
    			view.addObject("Users",Users);

    		}
    		view.setViewName("refundTable");
    	}
    	
    	//storing object in session
		request.getSession().setAttribute("reportDataObj",alipayWalletVO);
    	return view;
    }
	
	@RequestMapping(value = "/setupAlerts", method = RequestMethod.GET)
	public ModelAndView setupAlerts(@ModelAttribute("user") EmailAlertsConfigBean alertForm,
			BindingResult result, HttpServletRequest request) {
		ModelAndView view = new ModelAndView("setupAlerts");
		Object user_id = request.getSession().getAttribute("user_id");
		if(user_id ==null){
			view.setViewName("redirect:/webLogin");
			return view;
		}
		
		int roleId = Integer.decode(request.getSession().getAttribute("role_id").toString());
		if(roleId !=1){
			view.setViewName("redirect:/webLogin");
			return view;
		}
		
		//Object user_id = request.getSession().getAttribute("user_id");
		int userId = (int) request.getSession().getAttribute("user_id");
		
		//get configuration details
		List<EmailAlertsConfigBean> alertData = dashboardService.getAdminAlertConfigDetails(userId);
		
		if(!alertData.isEmpty() && alertData!=null){
			view.addObject("alertForm", alertForm);	
			for(EmailAlertsConfigBean configBean :alertData){
				alertForm.setId(configBean.getId());
				alertForm.setIs_reconciliation_failure_emails(configBean.getIs_reconciliation_failure_emails());
				alertForm.setIs_reconciliation_success_emails(configBean.getIs_reconciliation_success_emails());
				alertForm.setIs_settle_reconcile_phone_number(configBean.getIs_settle_reconcile_phone_number());
				alertForm.setIs_settlement_failure_emails(configBean.getIs_settlement_failure_emails());
				alertForm.setIs_settlement_success_emails(configBean.getIs_settlement_success_emails());
				alertForm.setReconciliation_failure_emails(configBean.getReconciliation_failure_emails());
				alertForm.setReconciliation_success_emails(configBean.getReconciliation_success_emails());
				alertForm.setSettle_reconcile_phone_number(configBean.getSettle_reconcile_phone_number());
				alertForm.setSettlement_failure_emails(configBean.getSettlement_failure_emails());
				alertForm.setSettlement_success_emails(configBean.getSettlement_success_emails());
			}
		}
	
		view.addObject("alertForm", alertForm);	
		
		return view;
	}
	
	
	@RequestMapping(value = "/saveAdminEmailAlertsConfig", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateAdminEmailAlertsConfig(@ModelAttribute("user") EmailAlertsConfigBean alertForm,
			BindingResult result, HttpServletRequest request,final RedirectAttributes redirectAttributes) {
		ModelAndView view = new ModelAndView("setupAlerts");
		Object user_id = request.getSession().getAttribute("user_id");
		if(user_id ==null){
			view.setViewName("redirect:/webLogin");
			return view;
		}
		
		int roleId = Integer.decode(request.getSession().getAttribute("role_id").toString());
		if(roleId !=1){
			view.setViewName("redirect:/webLogin");
			return view;
		}
		
		//Object user_id = request.getSession().getAttribute("user_id");
		int userId = (int) request.getSession().getAttribute("user_id");
		
		//save or update
		List<EmailAlertsConfigBean> alertData = dashboardService.getAdminAlertConfigDetails(userId);
		
		if(alertData.isEmpty() || alertData == null){
			//save		
			alertForm.setUser_id(userId);
			dashboardService.saveAdminAlertConfigDetails(alertForm);
			
			//alert message
			//redirectAttributes.addFlashAttribute("css", "success");
			//redirectAttributes.addFlashAttribute("message", "Email alert Config details updated successfully.");
		}else{
			
			//update
			for(EmailAlertsConfigBean configBean :alertData){
				alertForm.setId(configBean.getId());
			}		
			dashboardService.updateAdminAlertConfigDetails(alertForm);
			//alert message
			//redirectAttributes.addFlashAttribute("css", "success");
			//redirectAttributes.addFlashAttribute("message", "Email alert Config details updated successfully.");
		}
		
		view.setViewName("redirect:/setupAlerts");
		return view;
	}
	
	@RequestMapping(value={"/saveDynamicPayConnection"}, method={RequestMethod.POST})
	public ModelAndView saveOrUpdateDynamicPayConnection(@ModelAttribute("search") AlipayWalletVO alipayWalletVO,BindingResult result,HttpServletRequest request) {
		ModelAndView view = new ModelAndView("setupPaymentConnection");
		System.out.println(alipayWalletVO.getMerchant_id());
		Object user_id =  request.getSession().getAttribute("user_id");
		if(user_id != null)
		{
			//check if data exists in table mapped with user_id
			alipayWalletVO.setUser_id(user_id.toString());
			System.out.println(user_id);
			AlipayWalletVO info = dashboardService.getCUPRecord(alipayWalletVO);


			//if exits update

			if (info == null) {
				alipayWalletVO.setCreated_date(currentTimestamp);
				dashboardService.SaveCUPRecord(alipayWalletVO);
			
			} else {
				alipayWalletVO.setId(info.getId());
				alipayWalletVO.setCreated_date(currentTimestamp);
				//alipayWalletVO.setstatus(true);
				dashboardService.UpdateCUPRecord(alipayWalletVO);
			}
			view.setViewName("redirect:/setupConnection#Dynamic");
			return view;
		}
		else
		{
			view.setViewName("redirect:/login");
			return view;
		}
	}

	@RequestMapping(value={"/saveDPSPayConnection"}, method={RequestMethod.POST})
	public ModelAndView saveOrUpdateDPSPayConnection(@ModelAttribute(value="payConnection") AlipayWalletVO payConnection, BindingResult result,HttpServletRequest request) {
		ModelAndView view = new ModelAndView("setupPayConnection");
		Object user_id =  request.getSession().getAttribute("user_id");
		//check if data exists in table mapped with user_id
		if(user_id != null)
		{
			payConnection.setUser_id(user_id.toString());
			payConnection.setBillingID("");
			System.out.println(user_id);
			AlipayWalletVO info = dashboardService.getDPSRecord(payConnection);
			//			//if exits update

			if (info == null) {

				payConnection.setDPScreated_date(currentTimestamp);
			
				dashboardService.SaveDPSRecord(payConnection);

				
			} else {
				payConnection.setId(info.getId());
			
				payConnection.setDPScreated_date(currentTimestamp);
				dashboardService.UpdateDPSRecord(payConnection);
			}
			view.setViewName("redirect:/setupConnection#DPS");

			return view;
		}
		else
		{
			view.setViewName("redirect:/login");
			return view;
		}
	}

	@RequestMapping(value={"/savePoliPayConnection"}, method={RequestMethod.POST})
	public ModelAndView saveOrUpdatePoliPayConnection(@ModelAttribute(value="payConnection") AlipayWalletVO payConnection, BindingResult result,HttpServletRequest request) {
		ModelAndView view = new ModelAndView("setupPayConnection");
		Object user_id =  request.getSession().getAttribute("user_id");
		//check if data exists in table mapped with user_id
		if(user_id != null)
		{
			payConnection.setUser_id(user_id.toString());
			AlipayWalletVO info = dashboardService.getPoliRecord(payConnection);
			//			//if exits update
			
			if (info == null) {
				payConnection.setUser_id(user_id.toString());
				payConnection.setPoli_created_date(currentTimestamp);
				dashboardService.SavePoliRecord(payConnection);
			} else {
						
				payConnection.setId(info.getId());
				
				payConnection.setPoli_created_date(currentTimestamp);
				dashboardService.UpdatePoliRecord(payConnection);
			}
			view.setViewName("redirect:/setupConnection#POLi");

			return view;
		}

		else
		{
			view.setViewName("redirect:/login");
			return view;
		}

	}
	
	
	@RequestMapping(value={"/saveF2CPayConnection"}, method={RequestMethod.POST})
	public ModelAndView saveOrUpdateF2CPayConnection(@ModelAttribute(value="payConnection") AlipayWalletVO payConnection, BindingResult result,HttpServletRequest request) {
		ModelAndView view = new ModelAndView("setupPayConnection");
		Object user_id =  request.getSession().getAttribute("user_id");
		//check if data exists in table mapped with user_id
		if(user_id != null)
		{
			payConnection.setUser_id(user_id.toString());
			System.out.println(user_id);
			AlipayWalletVO info = dashboardService.getF2CRecord(payConnection);
			//			//if exits update
			if(payConnection.getDisplay_customer_email().equals("Yes"))
			{
				payConnection.setDisplay_customer_email("1");
			}
			else
			{
				payConnection.setDisplay_customer_email("0");
			}
			if(payConnection.getStore_card().equals("Yes"))
			{
				payConnection.setStore_card("1");
			}
			else
			{
				payConnection.setStore_card("0");
			}
			if (info == null) {
				payConnection.setUser_id(user_id.toString());
				payConnection.setF2c_created_date(currentTimestamp);
//				if(!payConnection.isVisa() && !payConnection.isAmerican_express() && !payConnection.isDinner_club())
//				{
//					payConnection.setVisa(true);
//				}
				dashboardService.SaveF2CRecord(payConnection);
				
			} else {
				payConnection.setId(info.getId());
				payConnection.setF2c_created_date(currentTimestamp);
				dashboardService.UpdateF2CRecord(payConnection);
				
			}
			
			view.setViewName("redirect:/setupConnection#Flo2Cash");
			
			return view;

		}
		else
		{
			view.setViewName("redirect:/login");
			return view;
		}
	}

}
