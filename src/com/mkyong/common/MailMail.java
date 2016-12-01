package com.mkyong.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.payitnz.config.DynamicPaymentConstant;
import com.payitnz.dao.SettlementDaoImpl;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.ReconcillationBean;
import com.payitnz.model.SettlementBean;
import com.payitnz.model.SettlementFileBean;
import com.payitnz.model.User;

public class MailMail
{
	private JavaMailSender mailSender;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
	
	private static final Logger logger = Logger.getLogger(MailMail.class);
	
    public void sendMail(String dear, String email, AlipayWalletVO alipayWalletVO) {

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			
			String htmlMsg = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
					+"<html xmlns=\"http://www.w3.org/1999/xhtml\">"

					+"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>Payitnz</title></head>"
					+"<body style=\"font-family:Arial, Helvetica, sans-serif; margin:0; padding:0;\">"
					+"<table align=\"center\" width=\"600\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
					+"<tr>"
					+"<td style=\"padding-left:15px; padding-top:12px;\"><a href=\"#\" style=\"float:left; display:block;\"><img src=\""+DynamicPaymentConstant.SITE_BASE_URL+"/resources/core/logo.png\"/></a></td>"
					
					+"</tr>"
					+"<tr>"
					+"<td>&nbsp;</td>"
					+"</tr>"
					+"<tr style=\"background:#3e6bb5;\">"
					+"<td style=\"font-size:18px; color:#fff; width:100%; background:#3e6bb5; font-family:Arial, Helvetica, sans-serif; font-weight:bold;padding:15px 10px; margin:10px 0;\">Transaction Receipt</td>"
					+"</tr>"
					+"<tr>"
					+"<td style=\"padding-top:15px; padding-left:15px;\">"
					+"<h2 style=\"font-size:20px; color:#464646; font-family:Arial, Helvetica, sans-serif; font-weight:bold;padding:0px; margin:0px;\">Dear Customer,</h2>"
					+"<p style=\"font-size:17px; color:#464646; font-family:Arial, Helvetica, sans-serif; line-height:30px; padding:0px; margin:0px; margin-top:8px;\">"+alipayWalletVO.getMsg()+"<br/> Below are the transaction details:"

					+"</p>"  
					+"<p style=\"font-size:15px; color:#464646; font-family:Arial, Helvetica, sans-serif; line-height:30px; padding:0px; margin:0px; margin-top:8px;\">"
					+"<strong>Merchant</strong> : "+alipayWalletVO.getMerchant_name()+"<br/>"
					+"<strong>Amount</strong>: "+ alipayWalletVO.getAmount()+"<br/>"
					+"<strong>Reference</strong>: "+ alipayWalletVO.getReference()+"<br/>"
					+"<strong>Particulars</strong>: "+ alipayWalletVO.getParticular()+"<br/>"
					+"<strong>Date & Time </strong> : "+alipayWalletVO.getDate_and_time()+"<br/>"
					+"<strong>AliPay Transaction ID</strong>: "+ alipayWalletVO.getAlipayTransactionId()+"<br/>"
					+"<strong>PayiTNZ ID</strong> : "+ alipayWalletVO.getPayitnz_id()		
					+"</p>"  
					+"</td>"
					+"</tr>"


					+"<tr>"
					 +"<td style=\"font-size:18px; color:#1f4c93; font-weight:500; padding-top:45px;padding-left:15px;\">Thanks,</td></tr>"
					+"<tr>"
					+"<td style=\"font-size:16px; color:#464646; padding:15px 0;padding-left:15px;\">"
					+"<h3 style=\"font-size:16px; color:#464646; margin:0; margin-bottom:6px;\">PayiTnz Team</h3>"
					+"</td>"
					+"</tr>"
					+"</table>"
					+"</body>"
					+"</html>";
			
			mimeMessage.setContent(htmlMsg, "text/html");
			helper.setTo(email);
			helper.setSubject("Payitnz Transaction Receipt");
			helper.setFrom(new InternetAddress("admin@payitnz.com","PayitNz Admin"));
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mailSender.send(mimeMessage);

	}

//	
    public void sendVerificationMail(User user,String password, String verifyLink) {

  		MimeMessage mimeMessage = mailSender.createMimeMessage();
  		MimeMessageHelper helper;
  		try {
  			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
  			
  			String htmlMsg = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
  					+"<html xmlns=\"http://www.w3.org/1999/xhtml\">"
  					+"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>Account Verification</title></head><body>"
  					+"<table width=\"700\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">"
  					+"<tr><td style=\"padding:20px; border:1px solid #ccc;\">"
  					+ "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
  					
  					+"<tr><td style=\"border-bottom:solid 1px #ccc; padding-bottom:15px;\"><a href=\"#\"><img src=\""+DynamicPaymentConstant.SITE_BASE_URL+"/resources/core/images/logo.png\" /></a></td></tr>"
  					
  					+"<tr><td style=\"color:#363636; text-decoration:none; padding-top:20px; font-family:Arial, Helvetica, sans-serif;\">"
  					
  					
  					+"<p style=\"font-weight:bold; color:#3e9dd5; font-size:16px; margin:0;\">Account Verification </p>"
  					
  					+"</td></tr>"
  					+"<tr><td><table  border=\"0\" cellpadding=\"2\" cellspacing=\"0\" style=\"font-family:Arial, Helvetica, sans-serif;  font-size:14px;line-height:22px; margin-top:20px;\">"
  					+"<tr style=\"padding-bottom:10px;\"><td style=\"color:#333;\">Dear </td><td style=\"color:#666;\">"+user.getFirstName()+"</td>"
  					+"</tr><tr><td></td></tr><tr>"
  					+"</tr></table></td></tr><tr>"
  					+"<td style=\"font-family:Arial, Helvetica, sans-serif; padding-top:20px; color:#333; font-size:14px;\">Welcome to PayiTNZ Merchant Portal.Please <a style=\"color:#3e9dd5;\" href='"+verifyLink+"'>click here</a> to activate your account. Your login credentials are provided below: </td>"
					+"</tr><tr><td style=\"font-family:Arial, Helvetica, sans-serif; color:#333; font-size:14px; color:#3e9dd5; padding-top:4px;\">"
  					
  					+"<h5>Username : <span style=\"color:#3e9dd5;\">"+user.getUsername()+"</span></h5></td>"
  					
  					+"</tr><tr><td style=\"font-family:Arial, Helvetica, sans-serif; color:#333; font-size:14px;padding-top:4px;\">"
  					
  					+"<h5>Password : <span style=\"color:#3e9dd5;\">"+password+"</span></h5></td>"
  					
  					+"</tr><tr><td style=\"font-family:Arial, Helvetica, sans-serif; color:#333; font-size:14px;padding-top:4px;\">"
  					
  					+"<h5>Account ID : <span style=\"color:#3e9dd5;\">"+user.getInfidigiAccountId()+"</span></h5></td>"
					+"</tr><tr><td></td></tr><tr>"
  					+"<td style=\"font-family:Arial, Helvetica, sans-serif; padding-top:20px; color:#333; font-size:14px;\">Before you can login for the first time, you will need to activate your account either by clicking link provided above (<a style=\"color:#3e9dd5;\" href='"+verifyLink+"'>click here</a>) or by copying and pasting the address link mentioned below in your browser.  </td>"
					+"</tr><tr><td style=\"font-family:Arial, Helvetica, sans-serif; color:#333; font-size:14px; color:#3e9dd5; padding-top:4px;\">"
					+"</tr><tr>" 
  					+"</tr><tr><td>&nbsp;</td></tr>"
  					+ "<tr><td><a style=\"color:#3e9dd5;\" href='"+verifyLink+"'>"+verifyLink+"</a></td></tr>"
  					+ "<tr><td>&nbsp;</td></tr>"
  					+ "<tr><td>&nbsp;</td></tr>"
  					+"<tr><td height=\"22\" style=\"font:normal 14px arial; color:#363636; text-decoration:none;padding-top:20px;\">Thanks & Regards,</td>"
  					+"</tr><tr><td style=\"font:bold 13px arial; color:#363636; text-decoration:none;\">PayiTNZ Team</td>"
  					+"</tr></table></td></tr></table></body></html>";
  			
  			mimeMessage.setContent(htmlMsg, "text/html");
  			helper.setTo(user.getEmail());
  			helper.setSubject("Account Verification Mail");
  			helper.setFrom(new InternetAddress("admin@payitnz.com","PayiTNZ Admin"));
  		} catch (MessagingException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

  		mailSender.send(mimeMessage);
  	}

    public void resetMail(String email,String link){
  		MimeMessage mimeMessage = mailSender.createMimeMessage();
  		MimeMessageHelper helper;
  		try {
  			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
  			String htmlMsg = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
  					+"<html xmlns=\"http://www.w3.org/1999/xhtml\">"
  					+"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>Reset Password</title></head><body>"
  					+"<table width=\"700\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">"
  					+"<tr><td style=\"padding:20px; border:1px solid #ccc;\">"
  					+ "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
  					
  					+"<tr><td style=\"border-bottom:solid 1px #ccc; padding-bottom:15px;\"><a href=\"#\"><img src=\""+DynamicPaymentConstant.SITE_BASE_URL+"/resources/core/images/logo.png\" /></a></td></tr>"
  					
  					+"<tr><td style=\"color:#363636; text-decoration:none; padding-top:20px; font-family:Arial, Helvetica, sans-serif;\">"
  					
  					
  					+"<p style=\"font-weight:bold; color:#3e9dd5; font-size:16px; margin:0;\">Reset Password</p>"
  					
  					+"</td></tr>"
  					+"<tr><td><table  border=\"0\" cellpadding=\"2\" cellspacing=\"0\" style=\"font-family:Arial, Helvetica, sans-serif;  font-size:14px;line-height:22px; margin-top:20px;\">"
  					+"<tr><td></td></tr><tr>"
  					+"</tr></table></td></tr><tr>"
  					
  					+"<td style=\"font-family:Arial, Helvetica, sans-serif; padding-top:20px; color:#333; font-size:14px;\">Copy and paste the following link into your browser to reset  password</td>"
  					+"</tr><tr><td style=\"font-family:Arial, Helvetica, sans-serif; color:#333; font-size:14px; color:#3e9dd5; padding-top:4px;\">"
  					
  					+"<a style=\"color:#3e9dd5;\">"+link+"</a></td>"
  					
  					+"</tr><tr><td>&nbsp;</td></tr><tr>"
  					+"<td height=\"22\" style=\"font:normal 14px arial; color:#363636; text-decoration:none;padding-top:20px;\">Thanks,</td>"
  					+"</tr><tr><td style=\"font:bold 13px arial; color:#363636; text-decoration:none;\">Payitnz Team</td>"
  					+"</tr></table></td></tr></table></body></html>";
  				
  			mimeMessage.setContent(htmlMsg, "text/html");
  			helper.setTo(email);
  			helper.setSubject("Reset Password");
  			helper.setFrom(new InternetAddress("admin@payitnz.com","PayitNz Admin"));
  		} catch (MessagingException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

  		mailSender.send(mimeMessage);

  	}
  	
    public void sendReconcillationMail(String dear, String email,ReconcillationBean bean) {

    	//String message = "";
    	
    	if(mailSender == null){
    		ApplicationContext context = new FileSystemXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
    		mailSender = (JavaMailSender)context.getBean("mailSender");
    	}
    	
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			
			String htmlMsg = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
					+"<html xmlns=\"http://www.w3.org/1999/xhtml\">"
					+"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>Reconcillation Error Email</title></head><body>"
					+"<table width=\"700\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">"
					+"<tr><td style=\"padding:20px; border:1px solid #ccc;\">"
					+ "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
					
					+"<tr><td style=\"border-bottom:solid 1px #ccc; padding-bottom:15px;\"><a href=\"#\"><img src=\""+DynamicPaymentConstant.SITE_BASE_URL+"/resources/core/images/logo.png\" /></a></td></tr>"
					
					+"<tr><td style=\"color:#363636; text-decoration:none; padding-top:20px; font-family:Arial, Helvetica, sans-serif;\">"
					
					
					+"<p style=\"font-weight:bold; color:#3e9dd5; font-size:16px; margin:0;\">Reconcillation Validation Mail</p>"
					
					+"</td></tr>"
					+"<tr><td><table  border=\"0\" cellpadding=\"2\" cellspacing=\"0\" style=\"font-family:Arial, Helvetica, sans-serif;  font-size:14px;line-height:22px; margin-top:20px;\">"
					+"<tr style=\"padding-bottom:10px;\"><td style=\"color:#333;\">Dear </td><td style=\"color:#666;\">"+dear+"</td>"
					+"</tr><tr><td></td></tr><tr>"
					+"</tr></table></td></tr><tr>"
					
					+"<td style=\"font-family:Arial, Helvetica, sans-serif; padding-top:20px; color:#333; font-size:14px;\">We found errors while reconciling your transaction for the order on AlipayWallet. Please check details below for your reference.</td>"
					+"</tr><tr><td></td></tr><tr><tr><td style=\"font-family:Arial, Helvetica, sans-serif; color:#333; font-size:14px; color:#3e9dd5; padding-top:4px;\">"
					
					+"<table><tr><td> Transaction Id : </td> <td>"+bean.getTransactionId()+"</td></tr>"+
					"<tr><td> Partner Transaction Id : </td> <td>"+bean.getPartnerTransactionId()+"</td></tr>"+
					"<tr><td> Transaction Amount : </td> <td>"+bean.getTransactionAmount()+"</td></tr>"+
					"<tr><td> Charge Amount : </td> <td>"+bean.getChargeAmount()+"</td></tr>"+
					"<tr><td> Currency : </td> <td>"+bean.getCurrency()+"</td></tr>"+
					"<tr><td> Transaction Type : </td> <td>"+bean.getTransactionType()+"</td></tr>"+
					"<tr><td> Payment Time : </td> <td>"+bean.getPaymentTime()+"</td></tr>"+
					"<tr><td> Remark : </td> <td>"+bean.getRemark()+"</td></tr>"+
					"</table></td>"					
					+"</tr><tr><td>&nbsp;</td></tr><tr>"
					+"<td height=\"22\" style=\"font:normal 14px arial; color:#363636; text-decoration:none;padding-top:20px;\">Thanks,</td>"
					+"</tr><tr><td style=\"font:bold 13px arial; color:#363636; text-decoration:none;\">"+DynamicPaymentConstant.EMAIL_REGARDS+"</td>"
					+"</tr></table></td></tr></table></body></html>";
			
			mimeMessage.setContent(htmlMsg, "text/html");
			helper.setTo(email);
			helper.setSubject("Reconcillaition Failure mail");
			helper.setFrom(new InternetAddress("admin@payitnz.com","PayitNz Admin"));
			
			if(email!=null && !email.isEmpty() && !email.equals(""))
				mailSender.send(mimeMessage);
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    public void sendReconcillationSuccessMail(String dear, String email,ReconcillationBean bean) {

    	//String message = "";
    	
    	if(mailSender == null){
    		ApplicationContext context = new FileSystemXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
    		mailSender = (JavaMailSender)context.getBean("mailSender");
    	}
    	
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			
			String htmlMsg = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
					+"<html xmlns=\"http://www.w3.org/1999/xhtml\">"
					+"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>Reconciliation Success Email</title></head><body>"
					+"<table width=\"700\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">"
					+"<tr><td style=\"padding:20px; border:1px solid #ccc;\">"
					+ "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
					
					+"<tr><td style=\"border-bottom:solid 1px #ccc; padding-bottom:15px;\"><a href=\"#\"><img src=\""+DynamicPaymentConstant.SITE_BASE_URL+"/resources/core/images/logo.png\" /></a></td></tr>"
					
					+"<tr><td style=\"color:#363636; text-decoration:none; padding-top:20px; font-family:Arial, Helvetica, sans-serif;\">"
					
					
					+"<p style=\"font-weight:bold; color:#3e9dd5; font-size:16px; margin:0;\">Reconciliation Validation Mail</p>"
					
					+"</td></tr>"
					+"<tr><td><table  border=\"0\" cellpadding=\"2\" cellspacing=\"0\" style=\"font-family:Arial, Helvetica, sans-serif;  font-size:14px;line-height:22px; margin-top:20px;\">"
					+"<tr style=\"padding-bottom:10px;\"><td style=\"color:#333;\">Dear </td><td style=\"color:#666;\">"+dear+"</td>"
					+"</tr><tr><td></td></tr><tr>"
					+"</tr></table></td></tr><tr>"
					
					+"<td style=\"font-family:Arial, Helvetica, sans-serif; padding-top:20px; color:#333; font-size:14px;\">Below transaction is succesfully reconciled and updated in the system. Please check details below for your reference.</td>"
					+"</tr><tr><td></td></tr><tr><tr><td style=\"font-family:Arial, Helvetica, sans-serif; color:#333; font-size:14px; color:#3e9dd5; padding-top:4px;\">"
					
					+"<table><tr><td> Transaction Id : </td> <td>"+bean.getTransactionId()+"</td></tr>"+
					"<tr><td> Partner Transaction Id : </td> <td>"+bean.getPartnerTransactionId()+"</td></tr>"+
					"<tr><td> Transaction Amount : </td> <td>"+bean.getTransactionAmount()+"</td></tr>"+
					"<tr><td> Charge Amount : </td> <td>"+bean.getChargeAmount()+"</td></tr>"+
					"<tr><td> Currency : </td> <td>"+bean.getCurrency()+"</td></tr>"+
					"<tr><td> Transaction Type : </td> <td>"+bean.getTransactionType()+"</td></tr>"+
					"<tr><td> Payment Time : </td> <td>"+bean.getPaymentTime()+"</td></tr>"+
					"<tr><td> Remark : </td> <td>"+bean.getRemark()+"</td></tr>"+
					"</table></td>"					
					+"</tr><tr><td>&nbsp;</td></tr><tr>"
					+"<td height=\"22\" style=\"font:normal 14px arial; color:#363636; text-decoration:none;padding-top:20px;\">Thanks,</td>"
					+"</tr><tr><td style=\"font:bold 13px arial; color:#363636; text-decoration:none;\">"+DynamicPaymentConstant.EMAIL_REGARDS+"</td>"
					+"</tr></table></td></tr></table></body></html>";
			
			mimeMessage.setContent(htmlMsg, "text/html");
			helper.setTo(email);
			helper.setSubject("Reconciliation Successfull Email");
			helper.setFrom(new InternetAddress("admin@payitnz.com","PayitNz Admin"));
			
			if(email!=null && !email.isEmpty() && !email.equals(""))
				mailSender.send(mimeMessage);
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    public void sendSettlementMail(String dear, String email,SettlementBean bean) {

    	//String message = "";
    	
    	if(mailSender == null){
    		ApplicationContext context = new FileSystemXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
    		mailSender = (JavaMailSender)context.getBean("mailSender");
    	}
    	
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			
			String htmlMsg = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
					+"<html xmlns=\"http://www.w3.org/1999/xhtml\">"
					+"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>Settlement Error Email</title></head><body>"
					+"<table width=\"700\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">"
					+"<tr><td style=\"padding:20px; border:1px solid #ccc;\">"
					+ "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
					
					+"<tr><td style=\"border-bottom:solid 1px #ccc; padding-bottom:15px;\"><a href=\"#\"><img src=\""+DynamicPaymentConstant.SITE_BASE_URL+"/resources/core/images/logo.png\" /></a></td></tr>"
					
					+"<tr><td style=\"color:#363636; text-decoration:none; padding-top:20px; font-family:Arial, Helvetica, sans-serif;\">"
					
					
					+"<p style=\"font-weight:bold; color:#3e9dd5; font-size:16px; margin:0;\">Settlement Validation Mail</p>"
					
					+"</td></tr>"
					+"<tr><td><table  border=\"0\" cellpadding=\"2\" cellspacing=\"0\" style=\"font-family:Arial, Helvetica, sans-serif;  font-size:14px;line-height:22px; margin-top:20px;\">"
					+"<tr style=\"padding-bottom:10px;\"><td style=\"color:#333;\">Dear </td><td style=\"color:#666;\">"+dear+"</td>"
					+"</tr><tr><td></td></tr><tr>"
					+"</tr></table></td></tr><tr>"
					
					+"<td style=\"font-family:Arial, Helvetica, sans-serif; padding-top:20px; color:#333; font-size:14px;\">We found errors while processing settlement of your transaction for the order on AlipayWallet. Please check details below for your reference.</td>"
					+"</tr><tr><td></td></tr><tr><tr><td style=\"font-family:Arial, Helvetica, sans-serif; color:#333; font-size:14px; color:#3e9dd5; padding-top:4px;\">"
					
					+"<table><tr><td> Transaction Id : </td> <td>"+bean.getTransactionId()+"</td></tr>"+
					"<tr><td> Partner Transaction Id : </td> <td>"+bean.getPartnerTransactionId()+"</td></tr>"+
					"<tr><td> Transaction Amount : </td> <td>"+bean.getAmount()+"</td></tr>"+
					"<tr><td> RMB Amount : </td> <td>"+bean.getRmbAmount()+"</td></tr>"+
					"<tr><td> Charge Amount : </td> <td>"+bean.getFee()+"</td></tr>"+
					"<tr><td> Settlement Amount : </td> <td>"+bean.getSettlement()+"</td></tr>"+
					"<tr><td> RMB Settlement Amount : </td> <td>"+bean.getRmbSettlement()+"</td></tr>"+
					"<tr><td> Settlement Time : </td> <td>"+bean.getSettlementTime()+"</td></tr>"+
					"<tr><td> Charge Amount : </td> <td>"+bean.getFee()+"</td></tr>"+
					"<tr><td> Currency : </td> <td>"+bean.getCurrency()+"</td></tr>"+
					"<tr><td> Transaction Type : </td> <td>"+bean.getType()+"</td></tr>"+
					"<tr><td> Payment Time : </td> <td>"+bean.getPaymentTime()+"</td></tr>"+
					"<tr><td> Remark : </td> <td>"+bean.getRemark()+"</td></tr>"+
					"</table></td>"					
					+"</tr><tr><td>&nbsp;</td></tr><tr>"
					+"<td height=\"22\" style=\"font:normal 14px arial; color:#363636; text-decoration:none;padding-top:20px;\">Thanks,</td>"
					+"</tr><tr><td style=\"font:bold 13px arial; color:#363636; text-decoration:none;\">"+DynamicPaymentConstant.EMAIL_REGARDS+"</td>"
					+"</tr></table></td></tr></table></body></html>";
			
			mimeMessage.setContent(htmlMsg, "text/html");
			helper.setTo(email);
			helper.setSubject("Settlement Failure mail");
			helper.setFrom(new InternetAddress("admin@payitnz.com","PayitNz Admin"));
			
			if(email!=null && !email.isEmpty() && !email.equals(""))
				mailSender.send(mimeMessage);
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public void sendSettlementSuccessMail(String dear, String email,SettlementBean bean) {

    	//String message = "";
    	
    	if(mailSender == null){
    		ApplicationContext context = new FileSystemXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
    		mailSender = (JavaMailSender)context.getBean("mailSender");
    	}
    	
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			
			String htmlMsg = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
					+"<html xmlns=\"http://www.w3.org/1999/xhtml\">"
					+"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>Settlement Success Email</title></head><body>"
					+"<table width=\"700\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">"
					+"<tr><td style=\"padding:20px; border:1px solid #ccc;\">"
					+ "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
					
					+"<tr><td style=\"border-bottom:solid 1px #ccc; padding-bottom:15px;\"><a href=\"#\"><img src=\""+DynamicPaymentConstant.SITE_BASE_URL+"/resources/core/images/logo.png\" /></a></td></tr>"
					
					+"<tr><td style=\"color:#363636; text-decoration:none; padding-top:20px; font-family:Arial, Helvetica, sans-serif;\">"
					
					
					+"<p style=\"font-weight:bold; color:#3e9dd5; font-size:16px; margin:0;\">Settlement Validation Mail</p>"
					
					+"</td></tr>"
					+"<tr><td><table  border=\"0\" cellpadding=\"2\" cellspacing=\"0\" style=\"font-family:Arial, Helvetica, sans-serif;  font-size:14px;line-height:22px; margin-top:20px;\">"
					+"<tr style=\"padding-bottom:10px;\"><td style=\"color:#333;\">Dear </td><td style=\"color:#666;\">"+dear+"</td>"
					+"</tr><tr><td></td></tr><tr>"
					+"</tr></table></td></tr><tr>"
					
					+"<td style=\"font-family:Arial, Helvetica, sans-serif; padding-top:20px; color:#333; font-size:14px;\">Below transaction is successfully settled and updated in the system. Please check details of the transaction below for your reference.</td>"
					+"</tr><tr><td></td></tr><tr><tr><td style=\"font-family:Arial, Helvetica, sans-serif; color:#333; font-size:14px; color:#3e9dd5; padding-top:4px;\">"
					
					+"<table><tr><td> Transaction Id : </td> <td>"+bean.getTransactionId()+"</td></tr>"+
					"<tr><td> Partner Transaction Id : </td> <td>"+bean.getPartnerTransactionId()+"</td></tr>"+
					"<tr><td> Transaction Amount : </td> <td>"+bean.getAmount()+"</td></tr>"+
					"<tr><td> RMB Amount : </td> <td>"+bean.getRmbAmount()+"</td></tr>"+
					"<tr><td> Charge Amount : </td> <td>"+bean.getFee()+"</td></tr>"+
					"<tr><td> Settlement Amount : </td> <td>"+bean.getSettlement()+"</td></tr>"+
					"<tr><td> RMB Settlement Amount : </td> <td>"+bean.getRmbSettlement()+"</td></tr>"+
					"<tr><td> Settlement Time : </td> <td>"+bean.getSettlementTime()+"</td></tr>"+
					"<tr><td> Charge Amount : </td> <td>"+bean.getFee()+"</td></tr>"+
					"<tr><td> Currency : </td> <td>"+bean.getCurrency()+"</td></tr>"+
					"<tr><td> Transaction Type : </td> <td>"+bean.getType()+"</td></tr>"+
					"<tr><td> Payment Time : </td> <td>"+bean.getPaymentTime()+"</td></tr>"+
					"<tr><td> Remark : </td> <td>"+bean.getRemark()+"</td></tr>"+
					"</table></td>"					
					+"</tr><tr><td>&nbsp;</td></tr><tr>"
					+"<td height=\"22\" style=\"font:normal 14px arial; color:#363636; text-decoration:none;padding-top:20px;\">Thanks,</td>"
					+"</tr><tr><td style=\"font:bold 13px arial; color:#363636; text-decoration:none;\">"+DynamicPaymentConstant.EMAIL_REGARDS+"</td>"
					+"</tr></table></td></tr></table></body></html>";
			
			mimeMessage.setContent(htmlMsg, "text/html");
			
			
			
			helper.setTo(email);
			helper.setSubject("Settlement Success mail");
			helper.setFrom(new InternetAddress("admin@payitnz.com","PayitNz Admin"));
			
			if(email!=null && !email.isEmpty() && !email.equals(""))
				mailSender.send(mimeMessage);
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    
    public void sendSettlementCreditFileMail(String dear, String email,SettlementFileBean bean) {

    	//String message = "";
    	
    	if(mailSender == null){
    		ApplicationContext context = new FileSystemXmlApplicationContext("/WEB-INF/Spring-Mail.xml");
    		mailSender = (JavaMailSender)context.getBean("mailSender");
    	}
    	
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
			
			String htmlMsg = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
					+"<html xmlns=\"http://www.w3.org/1999/xhtml\">"
					+"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>Settlement Credit File Generation</title></head><body>"
					+"<table width=\"700\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">"
					+"<tr><td style=\"padding:20px; border:1px solid #ccc;\">"
					+ "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
					
					+"<tr><td style=\"border-bottom:solid 1px #ccc; padding-bottom:15px;\"><a href=\"#\"><img src=\""+DynamicPaymentConstant.SITE_BASE_URL+"/resources/core/images/logo.png\" /></a></td></tr>"
					
					+"<tr><td style=\"color:#363636; text-decoration:none; padding-top:20px; font-family:Arial, Helvetica, sans-serif;\">"
					
					
					+"<p style=\"font-weight:bold; color:#3e9dd5; font-size:16px; margin:0;\">Settlement Validation Mail</p>"
					
					+"</td></tr>"
					+"<tr><td><table  border=\"0\" cellpadding=\"2\" cellspacing=\"0\" style=\"font-family:Arial, Helvetica, sans-serif;  font-size:14px;line-height:22px; margin-top:20px;\">"
					+"<tr style=\"padding-bottom:10px;\"><td style=\"color:#333;\">Dear </td><td style=\"color:#666;\">"+dear+"</td>"
					+"</tr><tr><td></td></tr><tr>"
					+"</tr></table></td></tr><tr>"
					
					+"<td style=\"font-family:Arial, Helvetica, sans-serif; padding-top:20px; color:#333; font-size:14px;\">The settlement file is settled and validated successfully in the system. Please check details of the transaction below for your reference.</td>"
					+"</tr><tr><td></td></tr><tr><tr><td style=\"font-family:Arial, Helvetica, sans-serif; color:#333; font-size:14px; color:#3e9dd5; padding-top:4px;\">"
					
					+"<table><tr><td> Transaction Count : </td> <td>"+bean.getTransactionCount()+"</td></tr>"+
					"<tr><td> File Name: </td> <td>"+bean.getFileName()+"</td></tr>"+		
					"<tr><td> Credit File: </td> <td>"+bean.getCreditFile()+"</td></tr>"+	
					"<tr><td> Uploaded Date: </td> <td>"+bean.getUploadedDate()+"</td></tr>"+	
					"<tr><td> Transaction Count: </td> <td>"+bean.getTransactionCount()+"</td></tr>"+	
					"</table></td>"					
					+"</tr><tr><td>&nbsp;</td></tr><tr>"
					+"</tr><tr><td>&nbsp;</td></tr><tr>"
					+"</tr><tr><td style=\"font-family:Arial, Helvetica, sans-serif; padding-top:20px; color:#333; font-size:14px;\"> Please check attached credit file for bank uploads.</td></tr><tr>"
					+"</tr><tr><td>&nbsp;</td></tr><tr>"
					+"</tr><tr><td>&nbsp;</td></tr><tr>"
					+"<td height=\"22\" style=\"font:normal 14px arial; color:#363636; text-decoration:none;padding-top:20px;\">Thanks,</td>"
					+"</tr><tr><td style=\"font:bold 13px arial; color:#363636; text-decoration:none;\">"+DynamicPaymentConstant.EMAIL_REGARDS+"</td>"
					+"</tr></table></td></tr></table></body></html>";
			
			helper.setText(htmlMsg,true);
			
			//FileSystemResource file = new FileSystemResource(DynamicPaymentConstant.SETTLEMENT_CREDIT_FILE_PATH+bean.getCreditFile());
			//helper.addAttachment(file.getFilename(), file);
			
		//	helper.addAttachment(bean.getCreditFile(),new File(DynamicPaymentConstant.SETTLEMENT_CREDIT_FILE_PATH+bean.getCreditFile()));
			
			FileSystemResource file = new FileSystemResource(new File(DynamicPaymentConstant.SETTLEMENT_CREDIT_FILE_PATH+bean.getCreditFile()));
			helper.addAttachment(bean.getCreditFile(), file);
			
			logger.info("Attaching file "+DynamicPaymentConstant.SETTLEMENT_CREDIT_FILE_PATH+bean.getCreditFile()+" to credit email!");	
			
			helper.setTo(email);
			helper.setSubject("Credit File Generation mail");
			helper.setFrom(new InternetAddress("admin@payitnz.com","PayitNz Admin"));
			
			if(email!=null && !email.isEmpty() && !email.equals(""))
				mailSender.send(mimeMessage);
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			logger.error("Failed to send credit file email. Something went wrong!");	
			logger.error(e.getMessage());
			//e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error("Failed to send credit file email. Something went wrong!");	
			logger.error(e.getMessage());
			//e.printStackTrace();
		}
	}
    
    
}
