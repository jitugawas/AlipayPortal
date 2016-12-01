package com.payitnz.util;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.payitnz.config.DynamicPaymentConstant;
import com.payitnz.model.AlipayAPIResponse;
import com.payitnz.model.AlipayDashboardBean;
import com.payitnz.model.AlipayPaymentGatewayBean;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.InfiUserSearchBean;
import com.payitnz.model.User;
import com.payitnz.service.AlipayAPIService;
import com.payitnz.service.DashboardService;
import com.payitnz.service.UserService;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CreatePDF {
	private static Font TIME_ROMAN = new Font(Font.FontFamily.TIMES_ROMAN, 18.0f, 1);
	private static Font TIME_ROMAN_SMALL = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1);
	@Autowired
	static UserService userService;

	@Autowired
	void setUserService(UserService userService) {
		CreatePDF.userService = userService;
	}
	
	
	@Autowired
	static DashboardService dashboardService;

	@Autowired
	void setDashboardService(DashboardService dashboardService)
	{
		CreatePDF.dashboardService = dashboardService;
	}
	
	@Autowired
	static AlipayAPIService alipayAPIService;
	
	@Autowired
	void setUserService(AlipayAPIService alipayAPIService) {
		CreatePDF.alipayAPIService = alipayAPIService;
	}
	
	Calendar calendar = Calendar.getInstance();
	java.util.Date now = calendar.getTime();
	java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Document createUserPDF(String file,HttpServletRequest request) throws ParseException {
		Document document = null;
		try {
			document = new Document(PageSize.A4.rotate());
			PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(file));
			document.open();
			CreatePDF.addUserMetaData(document);
			CreatePDF.addUserTitlePage(document);
			CreatePDF.createUserTable(document,request);
			document.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	public static Document createMerchantPDF(String file,HttpServletRequest request) throws ParseException {
		Document document = null;
		try {
			document = new Document(PageSize.A4.rotate());
			PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(file));
			document.open();
			CreatePDF.addMerchantMetaData(document);
			CreatePDF.addMerchantTitlePage(document);
			CreatePDF.createUserTable(document,request);
			document.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	
	public static Document createPDF(String file,HttpServletRequest request) {
		Document document = null;
		try {
			document = new Document(PageSize.A4.rotate());
			PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(file));
			document.open();
			CreatePDF.addMetaData(document);
			CreatePDF.addTitlePage(document,request);
			CreatePDF.createTable(document,request);
			document.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	
	public static Document createReportPDF(String file,HttpServletRequest request) {
		Document document = null;
		try {
			document = new Document(PageSize.A4.rotate());
			PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(file));
			document.open();
			CreatePDF.addMetaData(document);
			CreatePDF.addTitlePage(document,request);
			CreatePDF.createReportTable(document,request);
			document.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (DocumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}
	
	public static Document createTransactionDetailsReportPDF(String file,HttpServletRequest request) {
		Document document = null;
		try {
			document = new Document();
			PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(file));
			document.open();
			CreatePDF.addMerchantMetaData(document);
			CreatePDF.addMerchantTitlePage(document,request);
			CreatePDF.createTransactionDetailsReportTable(document,request);
			document.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (DocumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	private static void addUserMetaData(Document document) {
		document.addTitle("Payitnz Users");
		document.addSubject("Payitnz Users");
		document.addAuthor("Payitnz Admin");
		document.addCreator("Payitnz Admin");
	}
	
	private static void addMerchantMetaData(Document document) {
		document.addTitle("Payitnz Merchants");
		document.addSubject("Payitnz Merchants");
		document.addAuthor("Payitnz Admin");
		document.addCreator("Payitnz Admin");
	}
	
	private static void addMetaData(Document document) {
		document.addTitle("Payitnz Transaction report");
		document.addSubject("Payitnz Transaction report");
		document.addAuthor("Payitnz Admin");
		document.addCreator("Payitnz Admin");
	}
		
	private static void addUserTitlePage(Document document) throws DocumentException {
		Paragraph preface = new Paragraph();
		CreatePDF.creteEmptyLine(preface, 1);
		preface.add((Element)new Paragraph("Payitnz Users", TIME_ROMAN));
		CreatePDF.creteEmptyLine(preface, 1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		preface.add((Element)new Paragraph("Created on : " + simpleDateFormat.format(new Date()), TIME_ROMAN_SMALL));
		document.add((Element)preface);
	}
	
	private static void addMerchantTitlePage(Document document) throws DocumentException {
		Paragraph preface = new Paragraph();
		CreatePDF.creteEmptyLine(preface, 1);
		preface.add((Element)new Paragraph("Payitnz Merchants", TIME_ROMAN));
		CreatePDF.creteEmptyLine(preface, 1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		preface.add((Element)new Paragraph("Created on : " + simpleDateFormat.format(new Date()), TIME_ROMAN_SMALL));
		document.add((Element)preface);
	}
	
	private static void addTitlePage(Document document,HttpServletRequest request) throws DocumentException {
		AlipayDashboardBean dpbean = (AlipayDashboardBean) request.getSession().getAttribute("dashboardDataObj");
		Paragraph preface = new Paragraph();
		CreatePDF.creteEmptyLine(preface, 1);
		preface.add((Element)new Paragraph("Payitnz Transaction Report", TIME_ROMAN));
		CreatePDF.creteEmptyLine(preface, 1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		preface.add((Element)new Paragraph("Created on : " + simpleDateFormat.format(new Date()), TIME_ROMAN_SMALL));
		document.add((Element)preface);
	}
	
	private static void addMerchantTitlePage(Document document,HttpServletRequest request) throws DocumentException {
		Paragraph preface = new Paragraph();
		CreatePDF.creteEmptyLine(preface, 1);
		preface.add((Element)new Paragraph("Payitnz Transaction Details", TIME_ROMAN));
		CreatePDF.creteEmptyLine(preface, 1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		preface.add((Element)new Paragraph("Created on : " + simpleDateFormat.format(new Date()), TIME_ROMAN_SMALL));
		document.add((Element)preface);
	}

	private static void creteEmptyLine(Paragraph paragraph, int number) {
		int i = 0;
		while (i < number) {
			paragraph.add((Element)new Paragraph(" "));
			++i;
		}
	}
	
	private static void createUserTable(Document document, HttpServletRequest request) throws DocumentException, ParseException {
		Font font = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font .BOLD);
		Font normalFont =  new Font(Font.FontFamily.TIMES_ROMAN, 10);
		Paragraph paragraph = new Paragraph();
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		CreatePDF.creteEmptyLine(paragraph, 2);
		document.add((Element)paragraph);
		PdfPTable table = new PdfPTable(7);
		PdfPCell c1 = new PdfPCell(new Phrase("First Name",font));
		c1.setHorizontalAlignment(1);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Last Name",font));
		c1.setHorizontalAlignment(1);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Username",font));
		c1.setHorizontalAlignment(1);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Company Name",font));
		c1.setHorizontalAlignment(1);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Phone Number",font));
		c1.setHorizontalAlignment(1);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Email",font));
		c1.setHorizontalAlignment(1);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Date Of Creation",font));
		c1.setHorizontalAlignment(1);
		table.addCell(c1);
		
		//get all details by search details
		List<User> allUserList = userService.findUsersDetailsBySearchParams((InfiUserSearchBean) request.getSession().getAttribute("userObj"));		
		for (User dpUsersBean : allUserList) {
			table.setWidthPercentage(100.0f);
			table.getDefaultCell().setHorizontalAlignment(1);
			table.getDefaultCell().setVerticalAlignment(5);
			c1 = new PdfPCell(new Phrase(dpUsersBean.getFirstName(),normalFont));
			c1.setHorizontalAlignment(1);
			table.addCell(c1);
			c1 = new PdfPCell(new Phrase(dpUsersBean.getLastName(),normalFont));
			c1.setHorizontalAlignment(1);
			table.addCell(c1);
			c1 = new PdfPCell(new Phrase(dpUsersBean.getUsername(),normalFont));
			c1.setHorizontalAlignment(1);
			table.addCell(c1);
			c1 = new PdfPCell(new Phrase(dpUsersBean.getCompanyName(),normalFont));
			c1.setHorizontalAlignment(1);
			table.addCell(c1);
			c1 = new PdfPCell(new Phrase(dpUsersBean.getPhoneNo(),normalFont));
			c1.setHorizontalAlignment(1);
			table.addCell(c1);
			c1 = new PdfPCell(new Phrase(dpUsersBean.getEmail(),normalFont));
			c1.setHorizontalAlignment(1);
			table.addCell(c1);
			c1 = new PdfPCell(new Phrase(format.format(dateFormat.parse(dpUsersBean.getCreated_date())),normalFont));
			c1.setHorizontalAlignment(1);
			table.addCell(c1);
		}

		document.add((Element)table);
		
	}
	
	private static void createTable(Document document,HttpServletRequest request) throws DocumentException {
		Font font = new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD);
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Paragraph paragraph = new Paragraph();
		CreatePDF.creteEmptyLine(paragraph, 2);
		document.add((Element)paragraph);
		PdfPTable table = new PdfPTable(6);
		table.getDefaultCell().setPaddingBottom(4);
		table.getDefaultCell().setPaddingLeft(4);
		table.getDefaultCell().setPaddingRight(4);
		table.getDefaultCell().setPaddingTop(4);
		PdfPCell c1 = new PdfPCell(new Phrase("Channel",font));
		c1.setHorizontalAlignment(1);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Transaction Number",font));
		c1.setHorizontalAlignment(1);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Dollar Value",font));
		c1.setHorizontalAlignment(1);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Average",font));
		c1.setHorizontalAlignment(1);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Largest",font));
		c1.setHorizontalAlignment(1);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Smallest",font));
		c1.setHorizontalAlignment(1);
		table.addCell(c1);
		int totalTransVolume = 0;
		double totalTransValue = 0.0;
		List<AlipayDashboardBean> transDetails = dashboardService.findAllTransDetails((AlipayDashboardBean) request.getSession().getAttribute("dashboardDataObj"));

		//fetch payment types
		//List<DpPaymentTypesBean> paymentTypes = dashboardService.getAllPaymentTypes();
		
		List<AlipayPaymentGatewayBean> paymentMethods = dashboardService.findAllPaymentMethods();
		for (AlipayPaymentGatewayBean dpgPaymentTypeBean : paymentMethods) {
			int i = 0;
			for (AlipayDashboardBean dpTransactionBean : transDetails) {
				if (dpgPaymentTypeBean.getPayment_method().equals(dpTransactionBean.getPayment_method())) {
					table.setWidthPercentage(100.0f);
					table.getDefaultCell().setHorizontalAlignment(1);
					table.getDefaultCell().setVerticalAlignment(5);
					table.addCell(dpgPaymentTypeBean.getPayment_method());
					table.addCell("" + DynamicPaymentConstant.INRFormatWithInt(dpTransactionBean.getTrans_num()));
					table.addCell("$" + DynamicPaymentConstant.INRFormat(dpTransactionBean.getSum_amount()));
					table.addCell("$" + DynamicPaymentConstant.INRFormat(dpTransactionBean.getAvg_amount()));
					table.addCell("$" + DynamicPaymentConstant.INRFormat(dpTransactionBean.getMax_amount()));
					table.addCell("$" + DynamicPaymentConstant.INRFormat(dpTransactionBean.getMin_amount()));
					totalTransVolume += dpTransactionBean.getTrans_num();
					totalTransValue += dpTransactionBean.getSum_amount();
					break;
				}
				++i;
			}
			if (i != transDetails.size()) continue;
			table.setWidthPercentage(100.0f);
			table.getDefaultCell().setHorizontalAlignment(1);
			table.getDefaultCell().setVerticalAlignment(5);
			table.addCell(dpgPaymentTypeBean.getPayment_method());
			table.addCell("0");
			table.addCell("$0.00");
			table.addCell("$0.00");
			table.addCell("$0.00");
			table.addCell("$0.00");
		}
		document.add((Element)table);
		
		//add days
		int days = 0;
		AlipayDashboardBean search = (AlipayDashboardBean) request.getSession().getAttribute("dashboardDataObj");
		
		if(search.getFromDate() != null && search.getToDate() !=null){
			try {
				Date date1 = format.parse(format.format(search.getFromDate()));
				Date date2 = format.parse(format.format(search.getToDate()));
				 
				 long diff = date2.getTime() - date1.getTime();
				 days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}else{
			days = search.getDisplay_transactions_per_day();
		}
		

		Paragraph preface = new Paragraph();
		CreatePDF.creteEmptyLine(preface, 1);
		if(days == 1){
			preface.add((Element)new Paragraph("Total Transaction Volume for "+days+" day : " + DynamicPaymentConstant.INRFormatWithInt(totalTransVolume), TIME_ROMAN_SMALL));
		}else{
			preface.add((Element)new Paragraph("Total Transaction Volume for "+days+" days : " + DynamicPaymentConstant.INRFormatWithInt(totalTransVolume), TIME_ROMAN_SMALL));
		}
		CreatePDF.creteEmptyLine(preface, 1);
		if(days == 1){
			preface.add((Element)new Paragraph("Total Transaction $ Value for "+days+" days : "+ DynamicPaymentConstant.INRFormat(totalTransValue), TIME_ROMAN_SMALL));
		}else{
			preface.add((Element)new Paragraph("Total Transaction $ Value for "+days+" days : "+ DynamicPaymentConstant.INRFormat(totalTransValue), TIME_ROMAN_SMALL));
		}
		
		document.add((Element)preface);
	}
	
	private static void createReportTable(Document document,HttpServletRequest request) throws DocumentException, ParseException {	
		   Font font = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font .BOLD);
		    Font normalFont =  new Font(Font.FontFamily.TIMES_ROMAN, 10);
			Paragraph paragraph = new Paragraph();
			//paragraph.setFont(font);
			DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			CreatePDF.creteEmptyLine(paragraph, 2);
			document.add((Element)paragraph);
			//int role_id = Integer.decode((String)request.getSession().getAttribute("role_id"));
			PdfPTable table;
			table = new PdfPTable(5);
			
			PdfPCell c1 = new PdfPCell(new Phrase("Channel",font));
			c1.setHorizontalAlignment(1);
			table.addCell(c1);
							
			c1 = new PdfPCell(new Phrase("Transaction ID",font));
			c1.setHorizontalAlignment(1);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Phrase("Amount",font));
			c1.setHorizontalAlignment(1);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Phrase("Reference",font));
			c1.setHorizontalAlignment(1);
			table.addCell(c1);
			
			c1 = new PdfPCell(new Phrase("Date/Time",font));
			c1.setHorizontalAlignment(1);
			table.addCell(c1);
			
			
			//fetch data from transaction
			String ipAddress = request.getRemoteAddr();
			String sender = request.getRemoteHost();
			List<AlipayAPIResponse> responseList = alipayAPIService.getTransactionDetailsByCriteriaWeb((AlipayWalletVO) request.getSession().getAttribute("reportDataObj"), ipAddress, sender);
			table.setWidthPercentage(100.0f);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setHorizontalAlignment(1);
			table.getDefaultCell().setVerticalAlignment(5);
			
			if (responseList == null || responseList.isEmpty()) {

			}
			else
			{
		
				for (AlipayAPIResponse trans : responseList) {
					c1 = new PdfPCell(new Phrase(trans.getChannel(),normalFont));
					c1.setHorizontalAlignment(1);
					table.addCell(c1);
					c1 = new PdfPCell(new Phrase(trans.getPgPartnerTransId(),normalFont));
					c1.setHorizontalAlignment(1);
					table.addCell(c1);
					c1 = new PdfPCell(new Phrase(trans.getMcTransAmount(),normalFont));
					c1.setHorizontalAlignment(1);
					table.addCell(c1);
					c1 = new PdfPCell(new Phrase(trans.getMcReference(),normalFont));
					c1.setHorizontalAlignment(1);
					table.addCell(c1);
					c1 = new PdfPCell(new Phrase(format.format(dateFormat.parse(trans.getTransactionDate())),normalFont));
					c1.setHorizontalAlignment(1);
					table.addCell(c1);
				}
				
			}
						
			document.add((Element)table);
		}
		
	private static void createTransactionDetailsReportTable(Document document,HttpServletRequest request) throws DocumentException, ParseException {
		Paragraph paragraph = new Paragraph();
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		CreatePDF.creteEmptyLine(paragraph, 2);
		document.add((Element)paragraph);
		PdfPTable table;
		table = new PdfPTable(2);
		
		//fetch data from transaction
		List<AlipayAPIResponse> transData = new ArrayList<AlipayAPIResponse>();
		
		//fetch transaction details by id
		String transactionId = request.getParameter("id");
		//int role_id = (int) request.getSession().getAttribute("role_id");
		
		//fetch data from transaction
		transData = dashboardService.searchTransactionDataById(transactionId);
		
		if(!transData.isEmpty()){
			for (AlipayAPIResponse trans : transData) {
				
				table.setWidthPercentage(100.0f);
				table.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.getDefaultCell().setVerticalAlignment(10);
				table.getDefaultCell().setPaddingBottom(10);
				//table.getDefaultCell().setPaddingLeft(10);
				table.getDefaultCell().setPaddingRight(10);
				//table.getDefaultCell().setPaddingTop(10);
				
				PdfPCell c1 = new PdfPCell(new Phrase("Channel"));
				table.addCell(c1);
				table.addCell(trans.getChannel());
				
				c1 = new PdfPCell(new Phrase("Alipay Transaction ID"));
				table.addCell(c1);
				table.addCell(trans.getPgPartnerTransId());
				
				c1 = new PdfPCell(new Phrase("Amount"));
				table.addCell(c1);
				table.addCell("$" + trans.getMcTransAmount());
				
				c1 = new PdfPCell(new Phrase("Reference"));
				table.addCell(c1);
				table.addCell(trans.getMcReference());
				
				
				c1 = new PdfPCell(new Phrase("Particulars"));
				table.addCell(c1);
				table.addCell(trans.getMcItemName());
				
				c1 = new PdfPCell(new Phrase("Gateway Status"));
				table.addCell(c1);
				table.addCell(trans.getPgIsSuccess());
				
				c1 = new PdfPCell(new Phrase("Sign"));
				table.addCell(c1);
				table.addCell(trans.getPgSign());
				
				c1 = new PdfPCell(new Phrase("Sign Type"));
				table.addCell(c1);
				table.addCell(trans.getPgSignType());
				
				c1 = new PdfPCell(new Phrase("Result Code"));
				table.addCell(c1);
				table.addCell(trans.getPgResultCode());
				
				c1 = new PdfPCell(new Phrase("Error"));
				table.addCell(c1);
				table.addCell(trans.getPgError());
				
				c1 = new PdfPCell(new Phrase("Alipay Buyer Login ID"));
				table.addCell(c1);
				table.addCell(trans.getPgAlipayBuyerLoginId());
				
				c1 = new PdfPCell(new Phrase("Alipay Buyer User ID"));
				table.addCell(c1);
				table.addCell(trans.getPgAlipayBuyerUserId());
				
				c1 = new PdfPCell(new Phrase("Alipay Transaction Refund ID"));
				table.addCell(c1);
				table.addCell(trans.getMerchantRefundId());
				
				c1 = new PdfPCell(new Phrase("Alipay Pay Time"));
				table.addCell(c1);
				table.addCell(trans.getPgAlipayPayTime());
				
				c1 = new PdfPCell(new Phrase("Alipay Reverse Time"));
				table.addCell(c1);
				table.addCell(trans.getPgAlipayReverseTime());
				
				c1 = new PdfPCell(new Phrase("Alipay Cancel Time"));
				table.addCell(c1);
				table.addCell(trans.getPgAlipayCancelTime());
				
				c1 = new PdfPCell(new Phrase("Merchant Currency"));
				table.addCell(c1);
				table.addCell(trans.getMcCurrency());
				
				c1 = new PdfPCell(new Phrase("Merchant User ID"));
				table.addCell(c1);
				table.addCell(trans.getInfidigiUserId());
				
				c1 = new PdfPCell(new Phrase("Exchange Rate"));
				table.addCell(c1);
				table.addCell(""+trans.getPgExchangeRate());
				
				c1 = new PdfPCell(new Phrase("Request Time"));
				table.addCell(c1);
				table.addCell(trans.getRequestTime());
				
				c1 = new PdfPCell(new Phrase("Ip Address"));
				table.addCell(c1);
				table.addCell(trans.getIpAddress());
				
				c1 = new PdfPCell(new Phrase("Remark"));
				table.addCell(c1);
				table.addCell(trans.getRemark());
				
				c1 = new PdfPCell(new Phrase("Transaction Date"));
				table.addCell(c1);
				table.addCell(format.format(dateFormat.parse(trans.getTransactionDate())));
				
			}
		}
		
		
		document.add((Element)table);
	}
	
	
}