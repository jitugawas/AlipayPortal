package com.dpg.dynamicgateway;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.dpg.dynamicgateway.QuickPayConf;
import com.dpg.dynamicgateway.QuickPaySampleServLet;
import com.dpg.dynamicgateway.QuickPayUtils;

import com.payitnz.config.DynamicPaymentConstant;
import com.payitnz.model.AlipayWalletVO;
import com.payitnz.model.RequestBean;
import com.payitnz.service.AlipayAPIService;



public class DynamicPaymentOperations {

	@Autowired
	private AlipayAPIService alipayAPIService;

	@Autowired
	public void setUserService(AlipayAPIService alipayAPIService) {
		this.alipayAPIService = alipayAPIService;
	}
	 public void startService(HttpServletRequest request, HttpServletResponse response, AlipayWalletVO cup, AlipayWalletVO cart, RequestBean cupAPIRequest) {
         //ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ©Ã…â€œÃ¢â€šÂ¬ÃƒÂ¨Ã‚Â¦Ã¯Â¿Â½ÃƒÂ§Ã‚Â»Ã¢â‚¬Å¾ÃƒÂ¨Ã‚Â£Ã¢â‚¬Â¦ÃƒÂ¥Ã‚Â¦Ã¢â‚¬Å¡ÃƒÂ¤Ã‚Â¸Ã¢â‚¬Â¹ÃƒÂ¥Ã‚Â¯Ã‚Â¹ÃƒÂ¨Ã‚Â±Ã‚Â¡ÃƒÂ§Ã…Â¡Ã¢â‚¬Å¾ÃƒÂ¦Ã¢â‚¬Â¢Ã‚Â°ÃƒÂ¦Ã¯Â¿Â½Ã‚Â®
		 final long serialVersionUID = -6247574514957274823L;
		    int i = 0;

     String ipAddr = new QuickPayUtils().getAddr(request);
     String transType = "01";//cup.getTransaction_type();
     String origQid = "";
     String merId = cup.getMerchant_id();
     String merCode = cup.getMcc();
     String merName = "";
     String commodityUrl = "";
     String commodityName = "";
     String merReserved = "";
     String transTimeout = "";
     String acqCode = "";
    
     //vrajesh
     TimeZone.setDefault(TimeZone.getTimeZone("Hongkong"));
     /*SimpleDateFormat isoFormat = new SimpleDateFormat("yyyyMMddHHmmss");
     TimeZone tz = TimeZone.getTimeZone("Hongkong");
     isoFormat.setTimeZone(tz);*/
     
     //isoFormat.format(new Date()),//the time system using by UnionPay is GMT +8
 try {
     merName = new String(cup.getMerchant_name().getBytes("ISO-8859-1"), "UTF-8");
     commodityUrl = new String("");
     commodityName = new String("");
 } catch (UnsupportedEncodingException ex) {
     Logger.getLogger(QuickPaySampleServLet.class.getName()).log(Level.SEVERE, null, ex);
     return;
 }
     String commodityUnitPrice = "";
     String commodityQuantity = "";
     String commodityDiscount = "";
     String transferFee ="";
     String orderCurrency = cup.getCurrency();
     String orderAmount = cart.getAmount();
 
     if(!commodityUnitPrice.equals("")){
         commodityUnitPrice = String.format("%12.0f", Double.parseDouble(commodityUnitPrice) * 100).trim();
     }
     if(!commodityDiscount.equals("")){
         commodityDiscount = String.format("%12.0f", Double.parseDouble(commodityDiscount) * 100).trim();
     }
     if(!transferFee.equals("")){
         transferFee = String.format("%12.0f", Double.parseDouble(transferFee) * 100).trim();
     }
     orderAmount = String.format("%12.0f", Double.parseDouble(orderAmount) * 100).trim();
     merReserved = "";
     String time= cup.getTimeout();
     System.out.println("time"+time);
     transTimeout = time;
 
     String signType = request.getParameter("sign");
     if (!QuickPayConf.signType_SHA1withRSA.equalsIgnoreCase(signType)) {
             signType = QuickPayConf.signType;
     }
 
     String html;
     String[] valueVo;
 
     if(QuickPayConf.isMerInterface){
         valueVo = new String[]{
                             QuickPayConf.version,//ÃƒÂ¥Ã¯Â¿Â½Ã¯Â¿Â½ÃƒÂ¨Ã‚Â®Ã‚Â®ÃƒÂ§Ã¢â‚¬Â°Ã‹â€ ÃƒÂ¦Ã…â€œÃ‚Â¬
                             QuickPayConf.charset,//ÃƒÂ¥Ã‚Â­Ã¢â‚¬â€�ÃƒÂ§Ã‚Â¬Ã‚Â¦ÃƒÂ§Ã‚Â¼Ã¢â‚¬â€œÃƒÂ§Ã‚Â Ã¯Â¿Â½
                 transType,//ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ§Ã‚Â±Ã‚Â»ÃƒÂ¥Ã…Â¾Ã¢â‚¬Â¹
                 origQid,//ÃƒÂ¥Ã…Â½Ã…Â¸ÃƒÂ¥Ã‚Â§Ã¢â‚¬Â¹ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ¦Ã‚ÂµÃ¯Â¿Â½ÃƒÂ¦Ã‚Â°Ã‚Â´ÃƒÂ¥Ã¯Â¿Â½Ã‚Â·
                 merId,//ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ¤Ã‚Â»Ã‚Â£ÃƒÂ§Ã‚Â Ã¯Â¿Â½
                 merName,//ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ§Ã‚Â®Ã¢â€šÂ¬ÃƒÂ§Ã‚Â§Ã‚Â°
                 commodityUrl,//ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¥Ã¢â‚¬Å“Ã¯Â¿Â½URL
                 commodityName,//ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¥Ã¢â‚¬Å“Ã¯Â¿Â½ÃƒÂ¥Ã¯Â¿Â½Ã¯Â¿Â½ÃƒÂ§Ã‚Â§Ã‚Â°
                 commodityUnitPrice,//ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¥Ã¢â‚¬Å“Ã¯Â¿Â½ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¤Ã‚Â»Ã‚Â· ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¤Ã‚Â½Ã¯Â¿Â½ÃƒÂ¯Ã‚Â¼Ã…Â¡ÃƒÂ¥Ã‹â€ Ã¢â‚¬Â 
                 commodityQuantity,//ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¥Ã¢â‚¬Å“Ã¯Â¿Â½ÃƒÂ¦Ã¢â‚¬Â¢Ã‚Â°ÃƒÂ©Ã¢â‚¬Â¡Ã¯Â¿Â½
                 commodityDiscount,//ÃƒÂ¦Ã…Â Ã‹Å“ÃƒÂ¦Ã¢â‚¬Â°Ã‚Â£ ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¤Ã‚Â½Ã¯Â¿Â½ÃƒÂ¯Ã‚Â¼Ã…Â¡ÃƒÂ¥Ã‹â€ Ã¢â‚¬Â 
                 transferFee,//ÃƒÂ¨Ã‚Â¿Ã¯Â¿Â½ÃƒÂ¨Ã‚Â´Ã‚Â¹ ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¤Ã‚Â½Ã¯Â¿Â½ÃƒÂ¯Ã‚Â¼Ã…Â¡ÃƒÂ¥Ã‹â€ Ã¢â‚¬Â 
                 new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(++i),//ÃƒÂ¨Ã‚Â®Ã‚Â¢ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¥Ã¯Â¿Â½Ã‚Â·ÃƒÂ¯Ã‚Â¼Ã‹â€ ÃƒÂ©Ã…â€œÃ¢â€šÂ¬ÃƒÂ¨Ã‚Â¦Ã¯Â¿Â½ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ¨Ã¢â‚¬Â¡Ã‚ÂªÃƒÂ¥Ã‚Â·Ã‚Â±ÃƒÂ§Ã¢â‚¬ï¿½Ã…Â¸ÃƒÂ¦Ã‹â€ Ã¯Â¿Â½ÃƒÂ¯Ã‚Â¼Ã¢â‚¬Â°
                 orderAmount,//ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ©Ã¢â‚¬Â¡Ã¢â‚¬ËœÃƒÂ©Ã‚Â¢Ã¯Â¿Â½ ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¤Ã‚Â½Ã¯Â¿Â½ÃƒÂ¯Ã‚Â¼Ã…Â¡ÃƒÂ¥Ã‹â€ Ã¢â‚¬Â 
                 orderCurrency,//ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ¥Ã‚Â¸Ã¯Â¿Â½ÃƒÂ§Ã‚Â§Ã¯Â¿Â½
                 new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),//ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ¦Ã¢â‚¬â€�Ã‚Â¶ÃƒÂ©Ã¢â‚¬â€�Ã‚Â´
                 ipAddr, //ÃƒÂ§Ã¢â‚¬ï¿½Ã‚Â¨ÃƒÂ¦Ã‹â€ Ã‚Â·IP
                 "",//ÃƒÂ§Ã¢â‚¬ï¿½Ã‚Â¨ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ§Ã…â€œÃ…Â¸ÃƒÂ¥Ã‚Â®Ã…Â¾ÃƒÂ¥Ã‚Â§Ã¢â‚¬Å“ÃƒÂ¥Ã¯Â¿Â½Ã¯Â¿Â½
                 "",//ÃƒÂ©Ã‚Â»Ã‹Å“ÃƒÂ¨Ã‚Â®Ã‚Â¤ÃƒÂ¦Ã¢â‚¬ï¿½Ã‚Â¯ÃƒÂ¤Ã‚Â»Ã‹Å“ÃƒÂ¦Ã¢â‚¬â€œÃ‚Â¹ÃƒÂ¥Ã‚Â¼Ã¯Â¿Â½
                 "",//ÃƒÂ©Ã‚Â»Ã‹Å“ÃƒÂ¨Ã‚Â®Ã‚Â¤ÃƒÂ©Ã¢â‚¬Å“Ã‚Â¶ÃƒÂ¨Ã‚Â¡Ã…â€™ÃƒÂ§Ã‚Â¼Ã¢â‚¬â€œÃƒÂ¥Ã¯Â¿Â½Ã‚Â·
                 transTimeout,//ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ¨Ã‚Â¶Ã¢â‚¬Â¦ÃƒÂ¦Ã¢â‚¬â€�Ã‚Â¶ÃƒÂ¦Ã¢â‚¬â€�Ã‚Â¶ÃƒÂ©Ã¢â‚¬â€�Ã‚Â´
                 QuickPayConf.merFrontEndUrl,// ÃƒÂ¥Ã¢â‚¬Â°Ã¯Â¿Â½ÃƒÂ¥Ã¯Â¿Â½Ã‚Â°ÃƒÂ¥Ã¢â‚¬ÂºÃ…Â¾ÃƒÂ¨Ã‚Â°Ã†â€™ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·URL
                             QuickPayConf.merBackEndUrl,// ÃƒÂ¥Ã¯Â¿Â½Ã…Â½ÃƒÂ¥Ã¯Â¿Â½Ã‚Â°ÃƒÂ¥Ã¢â‚¬ÂºÃ…Â¾ÃƒÂ¨Ã‚Â°Ã†â€™ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·URL
                 merReserved //ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ¤Ã‚Â¿Ã¯Â¿Â½ÃƒÂ§Ã¢â‚¬Â¢Ã¢â€žÂ¢ÃƒÂ¥Ã…Â¸Ã…Â¸
             };
         
       
         cupAPIRequest.setTransferFee(cart.getAmount());
         cupAPIRequest.setBackEndUrl(QuickPayConf.merFrontEndUrl);
         cupAPIRequest.setCharset(QuickPayConf.charset);
         cupAPIRequest.setCommodityDiscount(commodityDiscount);
         cupAPIRequest.setCommodityName(commodityName);
         cupAPIRequest.setCommodityQuantity(commodityQuantity);
         cupAPIRequest.setCommodityUnitPrice(commodityUnitPrice);
         cupAPIRequest.setCommodityUrl(commodityUrl);
         cupAPIRequest.setCustomerIp(ipAddr);
         cupAPIRequest.setCustomerName(merName);
         cupAPIRequest.setDefaultBankNumber("");
         cupAPIRequest.setTransType(transType);
         cupAPIRequest.setDefaultPayType("");
         cupAPIRequest.setFrontEndUrl(QuickPayConf.merFrontEndUrl);
         cupAPIRequest.setMerAbbr("");
         cupAPIRequest.setMerId(merId);
         cupAPIRequest.setMerReserved(merReserved);
         cupAPIRequest.setOrderAmount(orderAmount);
         cupAPIRequest.setOrderCurrency(orderCurrency);
         cupAPIRequest.setOrderNumber("");
         cupAPIRequest.setOrderTime(new Date().toString());
         cupAPIRequest.setOrigQid(origQid);
         cupAPIRequest.setSignMethod(signType);
         cupAPIRequest.setSignature(QuickPayConf.signature);
         cupAPIRequest.setTransTimeout(transTimeout);
         cupAPIRequest.setVersion(QuickPayConf.version);
		 cupAPIRequest.setParticular(cart.getParticular());
		 cupAPIRequest.setUserid(cart.getUser_id());
		 cupAPIRequest.setMcPartnerTransId(cart.getPgPartnerTransId());
		 cupAPIRequest.setUrl(DynamicPaymentConstant.FLO2CASHURL);	
		 cupAPIRequest.setUrl("");
		
         html = new QuickPayUtils().createPayHtml(QuickPayConf.merReqVo, valueVo, signType, QuickPayConf.gateWay);//ÃƒÂ¨Ã‚Â·Ã‚Â³ÃƒÂ¨Ã‚Â½Ã‚Â¬ÃƒÂ¥Ã‹â€ Ã‚Â°ÃƒÂ©Ã¢â‚¬Å“Ã‚Â¶ÃƒÂ¨Ã¯Â¿Â½Ã¢â‚¬ï¿½ÃƒÂ©Ã‚Â¡Ã‚ÂµÃƒÂ©Ã¯Â¿Â½Ã‚Â¢ÃƒÂ¦Ã¢â‚¬ï¿½Ã‚Â¯ÃƒÂ¤Ã‚Â»Ã‹Å“
       //  transactionBean.setRequest(html);
         
         
     } else {
         valueVo = new String[]{
                             QuickPayConf.version,//ÃƒÂ¥Ã¯Â¿Â½Ã¯Â¿Â½ÃƒÂ¨Ã‚Â®Ã‚Â®ÃƒÂ§Ã¢â‚¬Â°Ã‹â€ ÃƒÂ¦Ã…â€œÃ‚Â¬
                             QuickPayConf.charset,//ÃƒÂ¥Ã‚Â­Ã¢â‚¬â€�ÃƒÂ§Ã‚Â¬Ã‚Â¦ÃƒÂ§Ã‚Â¼Ã¢â‚¬â€œÃƒÂ§Ã‚Â Ã¯Â¿Â½
                 transType,//ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ§Ã‚Â±Ã‚Â»ÃƒÂ¥Ã…Â¾Ã¢â‚¬Â¹
                 origQid,//ÃƒÂ¥Ã…Â½Ã…Â¸ÃƒÂ¥Ã‚Â§Ã¢â‚¬Â¹ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ¦Ã‚ÂµÃ¯Â¿Â½ÃƒÂ¦Ã‚Â°Ã‚Â´ÃƒÂ¥Ã¯Â¿Â½Ã‚Â·
                 merId,//ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ¤Ã‚Â»Ã‚Â£ÃƒÂ§Ã‚Â Ã¯Â¿Â½
                 merName,//ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ§Ã‚Â®Ã¢â€šÂ¬ÃƒÂ§Ã‚Â§Ã‚Â°
                 acqCode,//ÃƒÂ¦Ã¢â‚¬ï¿½Ã‚Â¶ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¦Ã…â€œÃ‚ÂºÃƒÂ¦Ã…Â¾Ã¢â‚¬Å¾ÃƒÂ¤Ã‚Â»Ã‚Â£ÃƒÂ§Ã‚Â Ã¯Â¿Â½ÃƒÂ¯Ã‚Â¼Ã‹â€ ÃƒÂ¤Ã‚Â»Ã¢â‚¬Â¦ÃƒÂ¦Ã¢â‚¬ï¿½Ã‚Â¶ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¦Ã…â€œÃ‚ÂºÃƒÂ¦Ã…Â¾Ã¢â‚¬Å¾ÃƒÂ¦Ã…Â½Ã‚Â¥ÃƒÂ¥Ã¢â‚¬Â¦Ã‚Â¥ÃƒÂ©Ã…â€œÃ¢â€šÂ¬ÃƒÂ¨Ã‚Â¦Ã¯Â¿Â½ÃƒÂ¥Ã‚Â¡Ã‚Â«ÃƒÂ¥Ã¢â‚¬Â Ã¢â€žÂ¢ÃƒÂ¯Ã‚Â¼Ã¢â‚¬Â°
                 merCode,//ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ§Ã‚Â±Ã‚Â»ÃƒÂ¥Ã‹â€ Ã‚Â«ÃƒÂ¯Ã‚Â¼Ã‹â€ ÃƒÂ¦Ã¢â‚¬ï¿½Ã‚Â¶ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¦Ã…â€œÃ‚ÂºÃƒÂ¦Ã…Â¾Ã¢â‚¬Å¾ÃƒÂ¦Ã…Â½Ã‚Â¥ÃƒÂ¥Ã¢â‚¬Â¦Ã‚Â¥ÃƒÂ©Ã…â€œÃ¢â€šÂ¬ÃƒÂ¨Ã‚Â¦Ã¯Â¿Â½ÃƒÂ¥Ã‚Â¡Ã‚Â«ÃƒÂ¥Ã¢â‚¬Â Ã¢â€žÂ¢ÃƒÂ¯Ã‚Â¼Ã¢â‚¬Â°
                 commodityUrl,//ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¥Ã¢â‚¬Å“Ã¯Â¿Â½URL
                 commodityName,//ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¥Ã¢â‚¬Å“Ã¯Â¿Â½ÃƒÂ¥Ã¯Â¿Â½Ã¯Â¿Â½ÃƒÂ§Ã‚Â§Ã‚Â°
                 commodityUnitPrice,//ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¥Ã¢â‚¬Å“Ã¯Â¿Â½ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¤Ã‚Â»Ã‚Â· ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¤Ã‚Â½Ã¯Â¿Â½ÃƒÂ¯Ã‚Â¼Ã…Â¡ÃƒÂ¥Ã‹â€ Ã¢â‚¬Â 
                 commodityQuantity,//ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¥Ã¢â‚¬Å“Ã¯Â¿Â½ÃƒÂ¦Ã¢â‚¬Â¢Ã‚Â°ÃƒÂ©Ã¢â‚¬Â¡Ã¯Â¿Â½
                 commodityDiscount,//ÃƒÂ¦Ã…Â Ã‹Å“ÃƒÂ¦Ã¢â‚¬Â°Ã‚Â£ ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¤Ã‚Â½Ã¯Â¿Â½ÃƒÂ¯Ã‚Â¼Ã…Â¡ÃƒÂ¥Ã‹â€ Ã¢â‚¬Â 
                 transferFee,//ÃƒÂ¨Ã‚Â¿Ã¯Â¿Â½ÃƒÂ¨Ã‚Â´Ã‚Â¹ ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¤Ã‚Â½Ã¯Â¿Â½ÃƒÂ¯Ã‚Â¼Ã…Â¡ÃƒÂ¥Ã‹â€ Ã¢â‚¬Â 
                 new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(++i),//ÃƒÂ¨Ã‚Â®Ã‚Â¢ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¥Ã¯Â¿Â½Ã‚Â·ÃƒÂ¯Ã‚Â¼Ã‹â€ ÃƒÂ©Ã…â€œÃ¢â€šÂ¬ÃƒÂ¨Ã‚Â¦Ã¯Â¿Â½ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ¨Ã¢â‚¬Â¡Ã‚ÂªÃƒÂ¥Ã‚Â·Ã‚Â±ÃƒÂ§Ã¢â‚¬ï¿½Ã…Â¸ÃƒÂ¦Ã‹â€ Ã¯Â¿Â½ÃƒÂ¯Ã‚Â¼Ã¢â‚¬Â°
                 orderAmount,//ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ©Ã¢â‚¬Â¡Ã¢â‚¬ËœÃƒÂ©Ã‚Â¢Ã¯Â¿Â½ ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Â¢ÃƒÂ¤Ã‚Â½Ã¯Â¿Â½ÃƒÂ¯Ã‚Â¼Ã…Â¡ÃƒÂ¥Ã‹â€ Ã¢â‚¬Â 
                 orderCurrency,//ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ¥Ã‚Â¸Ã¯Â¿Â½ÃƒÂ§Ã‚Â§Ã¯Â¿Â½
                 //new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),//ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ¦Ã¢â‚¬â€�Ã‚Â¶ÃƒÂ©Ã¢â‚¬â€�Ã‚Â´
                 //vrajesh
                 //isoFormat.format(new Date()),//the time system using by UnionPay is GMT +8
                 new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),//the time system using by UnionPay is GMT +8
                 ipAddr, //"127.0.0.1",//ÃƒÂ§Ã¢â‚¬ï¿½Ã‚Â¨ÃƒÂ¦Ã‹â€ Ã‚Â·IP
                 "",//ÃƒÂ§Ã¢â‚¬ï¿½Ã‚Â¨ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ§Ã…â€œÃ…Â¸ÃƒÂ¥Ã‚Â®Ã…Â¾ÃƒÂ¥Ã‚Â§Ã¢â‚¬Å“ÃƒÂ¥Ã¯Â¿Â½Ã¯Â¿Â½
                 "",//ÃƒÂ©Ã‚Â»Ã‹Å“ÃƒÂ¨Ã‚Â®Ã‚Â¤ÃƒÂ¦Ã¢â‚¬ï¿½Ã‚Â¯ÃƒÂ¤Ã‚Â»Ã‹Å“ÃƒÂ¦Ã¢â‚¬â€œÃ‚Â¹ÃƒÂ¥Ã‚Â¼Ã¯Â¿Â½
                 "",//ÃƒÂ©Ã‚Â»Ã‹Å“ÃƒÂ¨Ã‚Â®Ã‚Â¤ÃƒÂ©Ã¢â‚¬Å“Ã‚Â¶ÃƒÂ¨Ã‚Â¡Ã…â€™ÃƒÂ§Ã‚Â¼Ã¢â‚¬â€œÃƒÂ¥Ã¯Â¿Â½Ã‚Â·
                 transTimeout,//ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ¨Ã‚Â¶Ã¢â‚¬Â¦ÃƒÂ¦Ã¢â‚¬â€�Ã‚Â¶ÃƒÂ¦Ã¢â‚¬â€�Ã‚Â¶ÃƒÂ©Ã¢â‚¬â€�Ã‚Â´
                 QuickPayConf.merFrontEndUrl,// ÃƒÂ¥Ã¢â‚¬Â°Ã¯Â¿Â½ÃƒÂ¥Ã¯Â¿Â½Ã‚Â°ÃƒÂ¥Ã¢â‚¬ÂºÃ…Â¾ÃƒÂ¨Ã‚Â°Ã†â€™ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·URL
                             QuickPayConf.merBackEndUrl,// ÃƒÂ¥Ã¯Â¿Â½Ã…Â½ÃƒÂ¥Ã¯Â¿Â½Ã‚Â°ÃƒÂ¥Ã¢â‚¬ÂºÃ…Â¾ÃƒÂ¨Ã‚Â°Ã†â€™ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·URL
                 merReserved //ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ¤Ã‚Â¿Ã¯Â¿Â½ÃƒÂ§Ã¢â‚¬Â¢Ã¢â€žÂ¢ÃƒÂ¥Ã…Â¸Ã…Â¸
             };
 
         html = new QuickPayUtils().createPayHtml(QuickPayConf.reqVo, valueVo, signType, QuickPayConf.gateWay);//ÃƒÂ¨Ã‚Â·Ã‚Â³ÃƒÂ¨Ã‚Â½Ã‚Â¬ÃƒÂ¥Ã‹â€ Ã‚Â°ÃƒÂ©Ã¢â‚¬Å“Ã‚Â¶ÃƒÂ¨Ã¯Â¿Â½Ã¢â‚¬ï¿½ÃƒÂ©Ã‚Â¡Ã‚ÂµÃƒÂ©Ã¯Â¿Â½Ã‚Â¢ÃƒÂ¦Ã¢â‚¬ï¿½Ã‚Â¯ÃƒÂ¤Ã‚Â»Ã‹Å“
       //  transactionBean.setRequest(html);
 
     }
     //Added for db operation - vrajesh
     //UnionPayDAO unionPayDAO = new UnionPayDAO(); 
     //int recordsInserted = unionPayDAO.saveRequestData(QuickPayConf.merReqVo, valueVo, signType, QuickPayConf.gateWay);
 
         
         response.setContentType("text/html;charset="+QuickPayConf.charset);   
         response.setCharacterEncoding(QuickPayConf.charset);
         try {
                 response.getWriter().write(html);
         } catch (IOException e) {
                 
         }
         response.setStatus(HttpServletResponse.SC_OK);
 }
	 public void responseService(HttpServletRequest request, HttpServletResponse response, String returnUrl) {
	        try {
	            request.setCharacterEncoding(QuickPayConf.charset);
	        } catch (UnsupportedEncodingException e) {
	        }

	        Map<String, String> map = new TreeMap<String, String>();

	        String[] resArr = new String[QuickPayConf.notifyVo.length];
	        for (int i = 0; i < QuickPayConf.notifyVo.length; i++) {
	            resArr[i] = request.getParameter(QuickPayConf.notifyVo[i]);
	            map.put(QuickPayConf.notifyVo[i], resArr[i]);
	        }
	        String signature = request.getParameter(QuickPayConf.signature);
	        String signMethod = request.getParameter(QuickPayConf.signMethod);
	        
	     
	        response.setContentType("text/html;charset=" + QuickPayConf.charset);
	        response.setCharacterEncoding(QuickPayConf.charset);
	        Boolean signatureCheck = false;
	        try {
	            signatureCheck = new QuickPayUtils().checkSign(QuickPayConf.notifyVo, resArr, signMethod, signature);
	            response.getWriter().append("You can show the result of the transaction based on the response code<br>").append("<br>Signature Correct?:" + signatureCheck).append("<br>Transaction Successful:" + "00".equals(map.get("respCode")));
	            if (!"00".equals(resArr[10])) {
	                response.getWriter().append("<br>Response Code:" + map.get("respCode"));
	                response.getWriter().append("<br>Response Message:" + map.get("respMsg"));
	                response.getWriter().append("<br>qid (used to cancel/refund): " + map.get("qid"));
	                response.getWriter().append("<br><a href="+returnUrl+">Return to website</a>");
	            }
	        } catch (IOException e) {
	            System.out.println(e);
	        }

	        response.setStatus(HttpServletResponse.SC_OK);
	        // added - vrajesh
	        /*StringBuffer queryString = new StringBuffer("?");
	        queryString.append("signatureCheck=");
	        queryString.append(signatureCheck);
	        queryString.append("&");
	        queryString.append("respCode=");
	        queryString.append(map.get("respCode").toString());
	        queryString.append("&");
	        queryString.append("respMsg=");
	        queryString.append(map.get("respMsg").toString());
	        queryString.append("&");
	        queryString.append("qid=");
	        queryString.append(map.get("qid").toString());
	        try {
	            response.sendRedirect("http://" + DynamicPaymentConstant.SERVER_HOST + "/EcommerceClient/dynamicresponseCpu" + queryString.toString());
	        } catch (IOException e) {
	        
	            e.printStackTrace();
	        }*/
	     // ends - vrajesh
	    }
//	 public void responseService(HttpServletRequest request, HttpServletResponse response) {
//			try {
//				request.setCharacterEncoding(QuickPayConf.charset);
//			} catch (UnsupportedEncodingException e) {
//			}
//	                Map<String, String> map = new TreeMap<String, String>();
//
//			String[] resArr = new String[QuickPayConf.notifyVo.length]; 
//			for(int i=0;i<QuickPayConf.notifyVo.length;i++){
//				resArr[i] = request.getParameter(QuickPayConf.notifyVo[i]);
//	                        map.put(QuickPayConf.notifyVo[i], resArr[i]);
//			}
//			String signature = request.getParameter(QuickPayConf.signature);
//			String signMethod = request.getParameter(QuickPayConf.signMethod);
//			
//			response.setContentType("text/html;charset="+QuickPayConf.charset);   
//			response.setCharacterEncoding(QuickPayConf.charset);
//			
//				Boolean signatureCheck = new QuickPayUtils().checkSign(QuickPayConf.notifyVo, resArr, signMethod, signature);
//	                        System.out.println("ÃƒÂ§Ã‚Â­Ã‚Â¾ÃƒÂ¥Ã¯Â¿Â½Ã¯Â¿Â½ÃƒÂ¦Ã‹Å“Ã‚Â¯ÃƒÂ¥Ã¯Â¿Â½Ã‚Â¦ÃƒÂ¦Ã‚Â­Ã‚Â£ÃƒÂ§Ã‚Â¡Ã‚Â®ÃƒÂ¯Ã‚Â¼Ã…Â¡"+ signatureCheck);
//				System.out.println("ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ¦Ã‹Å“Ã‚Â¯ÃƒÂ¥Ã¯Â¿Â½Ã‚Â¦ÃƒÂ¦Ã‹â€ Ã¯Â¿Â½ÃƒÂ¥Ã…Â Ã…Â¸ÃƒÂ¯Ã‚Â¼Ã…Â¡"+"00".equals(map.get("respCode")));
//				if(!"00".equals(resArr[10])){
//	                            System.out.println("ÃƒÂ¥Ã¢â‚¬Å“Ã¯Â¿Â½ÃƒÂ¥Ã‚ÂºÃ¢â‚¬ï¿½ÃƒÂ§Ã‚Â Ã¯Â¿Â½:" + map.get("respCode"));
//	                            System.out.println("ÃƒÂ¥Ã‚Â¤Ã‚Â±ÃƒÂ¨Ã‚Â´Ã‚Â¥ÃƒÂ¥Ã…Â½Ã…Â¸ÃƒÂ¥Ã¢â‚¬ÂºÃ‚Â :" + map.get("respMsg"));
//				}
//		
//			response.setStatus(HttpServletResponse.SC_OK);
//		}
//	 
//	 
}
