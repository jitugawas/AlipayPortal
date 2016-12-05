package com.dpg.dynamicgateway;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;

import com.dpg.dynamicgateway.QuickPayUtils;

/**
 * Ã¥ï¿½ï¿½Ã§Â§Â°Ã¯Â¼Å¡Ã¦â€�Â¯Ã¤Â»ËœÃ¨Â¯Â·Ã¦Â±â€šÃ§Â±Â»
 * Ã¥Å Å¸Ã¨Æ’Â½Ã¯Â¼Å¡servletÃ¦â€“Â¹Ã¥Â¼ï¿½Ã¦â€�Â¯Ã¤Â»ËœÃ§Â±Â»Ã¨Â¯Â·Ã¦Â±â€š
 * Ã§Â±Â»Ã¥Â±Å¾Ã¦â‚¬Â§Ã¯Â¼Å¡
 * Ã§â€°Ë†Ã¦Å“Â¬Ã¯Â¼Å¡1.0
 * Ã¦â€”Â¥Ã¦Å“Å¸Ã¯Â¼Å¡2011-03-11
 * Ã¤Â½Å“Ã¨â‚¬â€¦Ã¯Â¼Å¡Ã¤Â¸Â­Ã¥â€ºÂ½Ã©â€œÂ¶Ã¨ï¿½â€�UPOPÃ¥â€ºÂ¢Ã©ËœÅ¸
 * Ã§â€°Ë†Ã¦ï¿½Æ’Ã¯Â¼Å¡Ã¤Â¸Â­Ã¥â€ºÂ½Ã©â€œÂ¶Ã¨ï¿½â€�
 * Ã¨Â¯Â´Ã¦ËœÅ½Ã¯Â¼Å¡Ã¤Â»Â¥Ã¤Â¸â€¹Ã¤Â»Â£Ã§Â ï¿½Ã¥ï¿½ÂªÃ¦ËœÂ¯Ã¤Â¸ÂºÃ¤Âºâ€ Ã¦â€“Â¹Ã¤Â¾Â¿Ã¥â€¢â€ Ã¦Ë†Â·Ã¦Âµâ€¹Ã¨Â¯â€¢Ã¨â‚¬Å’Ã¦ï¿½ï¿½Ã¤Â¾â€ºÃ§Å¡â€žÃ¦Â Â·Ã¤Â¾â€¹Ã¤Â»Â£Ã§Â ï¿½Ã¯Â¼Å’Ã¥â€¢â€ Ã¦Ë†Â·Ã¥ï¿½Â¯Ã¤Â»Â¥Ã¦Â Â¹Ã¦ï¿½Â®Ã¨â€¡ÂªÃ¥Â·Â±Ã§Â½â€˜Ã§Â«â„¢Ã§Å¡â€žÃ©Å“â‚¬Ã¨Â¦ï¿½Ã¯Â¼Å’Ã¦Å’â€°Ã§â€¦Â§Ã¦Å â‚¬Ã¦Å“Â¯Ã¦â€“â€¡Ã¦Â¡Â£Ã§Â¼â€“Ã¥â€ â„¢,Ã¥Â¹Â¶Ã©ï¿½Å¾Ã¤Â¸â‚¬Ã¥Â®Å¡Ã¨Â¦ï¿½Ã¤Â½Â¿Ã§â€�Â¨Ã¨Â¯Â¥Ã¤Â»Â£Ã§Â ï¿½Ã£â‚¬â€šÃ¨Â¯Â¥Ã¤Â»Â£Ã§Â ï¿½Ã¤Â»â€¦Ã¤Â¾â€ºÃ¥ï¿½â€šÃ¨â‚¬Æ’Ã£â‚¬â€š
 * */
@RequestMapping("/payServlet")  
public class QuickPaySampleServLet extends HttpServlet {
    /**
     * 
     */
    private static final long serialVersionUID = -6247574514957274823L;
    static int i = 0;

    public void init() {

    }

    
    public void service(HttpServletRequest request, HttpServletResponse response) {
            //Ã¥â€¢â€ Ã¦Ë†Â·Ã©Å“â‚¬Ã¨Â¦ï¿½Ã§Â»â€žÃ¨Â£â€¦Ã¥Â¦â€šÃ¤Â¸â€¹Ã¥Â¯Â¹Ã¨Â±Â¡Ã§Å¡â€žÃ¦â€¢Â°Ã¦ï¿½Â®
    
        String ipAddr = new QuickPayUtils().getAddr(request);
        String transType = request.getParameter("transType");
        String origQid = request.getParameter("origQid");
        String merId = request.getParameter("merId");
        String merCode = request.getParameter("merCode");
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
        merName = new String(request.getParameter("merName").getBytes("ISO-8859-1"), "UTF-8");
        commodityUrl = new String(request.getParameter("commodityUrl").getBytes("ISO-8859-1"),"UTF-8");
        commodityName = new String(request.getParameter("commodityName").getBytes("ISO-8859-1"),"UTF-8");
    } catch (UnsupportedEncodingException ex) {
        Logger.getLogger(QuickPaySampleServLet.class.getName()).log(Level.SEVERE, null, ex);
        return;
    }
        String commodityUnitPrice = request.getParameter("commodityUnitPrice");
        String commodityQuantity = request.getParameter("commodityQuantity");
        String commodityDiscount = request.getParameter("commodityDiscount");
        String transferFee = request.getParameter("transferFee");
        String orderCurrency = request.getParameter("orderCurrency");
        String orderAmount = request.getParameter("orderAmount");
    
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
        merReserved = request.getParameter("merReserved");
        transTimeout = request.getParameter("transTimeout");
    
        String signType = request.getParameter("sign");
        if (!QuickPayConf.signType_SHA1withRSA.equalsIgnoreCase(signType)) {
                signType = QuickPayConf.signType;
        }
    
        String html;
        String[] valueVo;
    
        if(QuickPayConf.isMerInterface){
            valueVo = new String[]{
                                QuickPayConf.version,//Ã¥ï¿½ï¿½Ã¨Â®Â®Ã§â€°Ë†Ã¦Å“Â¬
                                QuickPayConf.charset,//Ã¥Â­â€”Ã§Â¬Â¦Ã§Â¼â€“Ã§Â ï¿½
                    transType,//Ã¤ÂºÂ¤Ã¦Ëœâ€œÃ§Â±Â»Ã¥Å¾â€¹
                    origQid,//Ã¥Å½Å¸Ã¥Â§â€¹Ã¤ÂºÂ¤Ã¦Ëœâ€œÃ¦Âµï¿½Ã¦Â°Â´Ã¥ï¿½Â·
                    merId,//Ã¥â€¢â€ Ã¦Ë†Â·Ã¤Â»Â£Ã§Â ï¿½
                    merName,//Ã¥â€¢â€ Ã¦Ë†Â·Ã§Â®â‚¬Ã§Â§Â°
                    commodityUrl,//Ã¥â€¢â€ Ã¥â€œï¿½URL
                    commodityName,//Ã¥â€¢â€ Ã¥â€œï¿½Ã¥ï¿½ï¿½Ã§Â§Â°
                    commodityUnitPrice,//Ã¥â€¢â€ Ã¥â€œï¿½Ã¥ï¿½â€¢Ã¤Â»Â· Ã¥ï¿½â€¢Ã¤Â½ï¿½Ã¯Â¼Å¡Ã¥Ë†â€ 
                    commodityQuantity,//Ã¥â€¢â€ Ã¥â€œï¿½Ã¦â€¢Â°Ã©â€¡ï¿½
                    commodityDiscount,//Ã¦Å ËœÃ¦â€°Â£ Ã¥ï¿½â€¢Ã¤Â½ï¿½Ã¯Â¼Å¡Ã¥Ë†â€ 
                    transferFee,//Ã¨Â¿ï¿½Ã¨Â´Â¹ Ã¥ï¿½â€¢Ã¤Â½ï¿½Ã¯Â¼Å¡Ã¥Ë†â€ 
                    new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(++i),//Ã¨Â®Â¢Ã¥ï¿½â€¢Ã¥ï¿½Â·Ã¯Â¼Ë†Ã©Å“â‚¬Ã¨Â¦ï¿½Ã¥â€¢â€ Ã¦Ë†Â·Ã¨â€¡ÂªÃ¥Â·Â±Ã§â€�Å¸Ã¦Ë†ï¿½Ã¯Â¼â€°
                    orderAmount,//Ã¤ÂºÂ¤Ã¦Ëœâ€œÃ©â€¡â€˜Ã©Â¢ï¿½ Ã¥ï¿½â€¢Ã¤Â½ï¿½Ã¯Â¼Å¡Ã¥Ë†â€ 
                    orderCurrency,//Ã¤ÂºÂ¤Ã¦Ëœâ€œÃ¥Â¸ï¿½Ã§Â§ï¿½
                    new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),//Ã¤ÂºÂ¤Ã¦Ëœâ€œÃ¦â€”Â¶Ã©â€”Â´
                    ipAddr, //Ã§â€�Â¨Ã¦Ë†Â·IP
                    "",//Ã§â€�Â¨Ã¦Ë†Â·Ã§Å“Å¸Ã¥Â®Å¾Ã¥Â§â€œÃ¥ï¿½ï¿½
                    "",//Ã©Â»ËœÃ¨Â®Â¤Ã¦â€�Â¯Ã¤Â»ËœÃ¦â€“Â¹Ã¥Â¼ï¿½
                    "",//Ã©Â»ËœÃ¨Â®Â¤Ã©â€œÂ¶Ã¨Â¡Å’Ã§Â¼â€“Ã¥ï¿½Â·
                    transTimeout,//Ã¤ÂºÂ¤Ã¦Ëœâ€œÃ¨Â¶â€¦Ã¦â€”Â¶Ã¦â€”Â¶Ã©â€”Â´
                    QuickPayConf.merFrontEndUrl,// Ã¥â€°ï¿½Ã¥ï¿½Â°Ã¥â€ºÅ¾Ã¨Â°Æ’Ã¥â€¢â€ Ã¦Ë†Â·URL
                                QuickPayConf.merBackEndUrl,// Ã¥ï¿½Å½Ã¥ï¿½Â°Ã¥â€ºÅ¾Ã¨Â°Æ’Ã¥â€¢â€ Ã¦Ë†Â·URL
                    merReserved //Ã¥â€¢â€ Ã¦Ë†Â·Ã¤Â¿ï¿½Ã§â€¢â„¢Ã¥Å¸Å¸
                };
            html = new QuickPayUtils().createPayHtml(QuickPayConf.merReqVo, valueVo, signType, QuickPayConf.gateWay);//Ã¨Â·Â³Ã¨Â½Â¬Ã¥Ë†Â°Ã©â€œÂ¶Ã¨ï¿½â€�Ã©Â¡ÂµÃ©ï¿½Â¢Ã¦â€�Â¯Ã¤Â»Ëœ
    
        } else {
            valueVo = new String[]{
                                QuickPayConf.version,//Ã¥ï¿½ï¿½Ã¨Â®Â®Ã§â€°Ë†Ã¦Å“Â¬
                                QuickPayConf.charset,//Ã¥Â­â€”Ã§Â¬Â¦Ã§Â¼â€“Ã§Â ï¿½
                    transType,//Ã¤ÂºÂ¤Ã¦Ëœâ€œÃ§Â±Â»Ã¥Å¾â€¹
                    origQid,//Ã¥Å½Å¸Ã¥Â§â€¹Ã¤ÂºÂ¤Ã¦Ëœâ€œÃ¦Âµï¿½Ã¦Â°Â´Ã¥ï¿½Â·
                    merId,//Ã¥â€¢â€ Ã¦Ë†Â·Ã¤Â»Â£Ã§Â ï¿½
                    merName,//Ã¥â€¢â€ Ã¦Ë†Â·Ã§Â®â‚¬Ã§Â§Â°
                    acqCode,//Ã¦â€�Â¶Ã¥ï¿½â€¢Ã¦Å“ÂºÃ¦Å¾â€žÃ¤Â»Â£Ã§Â ï¿½Ã¯Â¼Ë†Ã¤Â»â€¦Ã¦â€�Â¶Ã¥ï¿½â€¢Ã¦Å“ÂºÃ¦Å¾â€žÃ¦Å½Â¥Ã¥â€¦Â¥Ã©Å“â‚¬Ã¨Â¦ï¿½Ã¥Â¡Â«Ã¥â€ â„¢Ã¯Â¼â€°
                    merCode,//Ã¥â€¢â€ Ã¦Ë†Â·Ã§Â±Â»Ã¥Ë†Â«Ã¯Â¼Ë†Ã¦â€�Â¶Ã¥ï¿½â€¢Ã¦Å“ÂºÃ¦Å¾â€žÃ¦Å½Â¥Ã¥â€¦Â¥Ã©Å“â‚¬Ã¨Â¦ï¿½Ã¥Â¡Â«Ã¥â€ â„¢Ã¯Â¼â€°
                    commodityUrl,//Ã¥â€¢â€ Ã¥â€œï¿½URL
                    commodityName,//Ã¥â€¢â€ Ã¥â€œï¿½Ã¥ï¿½ï¿½Ã§Â§Â°
                    commodityUnitPrice,//Ã¥â€¢â€ Ã¥â€œï¿½Ã¥ï¿½â€¢Ã¤Â»Â· Ã¥ï¿½â€¢Ã¤Â½ï¿½Ã¯Â¼Å¡Ã¥Ë†â€ 
                    commodityQuantity,//Ã¥â€¢â€ Ã¥â€œï¿½Ã¦â€¢Â°Ã©â€¡ï¿½
                    commodityDiscount,//Ã¦Å ËœÃ¦â€°Â£ Ã¥ï¿½â€¢Ã¤Â½ï¿½Ã¯Â¼Å¡Ã¥Ë†â€ 
                    transferFee,//Ã¨Â¿ï¿½Ã¨Â´Â¹ Ã¥ï¿½â€¢Ã¤Â½ï¿½Ã¯Â¼Å¡Ã¥Ë†â€ 
                    new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(++i),//Ã¨Â®Â¢Ã¥ï¿½â€¢Ã¥ï¿½Â·Ã¯Â¼Ë†Ã©Å“â‚¬Ã¨Â¦ï¿½Ã¥â€¢â€ Ã¦Ë†Â·Ã¨â€¡ÂªÃ¥Â·Â±Ã§â€�Å¸Ã¦Ë†ï¿½Ã¯Â¼â€°
                    orderAmount,//Ã¤ÂºÂ¤Ã¦Ëœâ€œÃ©â€¡â€˜Ã©Â¢ï¿½ Ã¥ï¿½â€¢Ã¤Â½ï¿½Ã¯Â¼Å¡Ã¥Ë†â€ 
                    orderCurrency,//Ã¤ÂºÂ¤Ã¦Ëœâ€œÃ¥Â¸ï¿½Ã§Â§ï¿½
                    //new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),//Ã¤ÂºÂ¤Ã¦Ëœâ€œÃ¦â€”Â¶Ã©â€”Â´
                    //vrajesh
                    //isoFormat.format(new Date()),//the time system using by UnionPay is GMT +8
                    new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),//the time system using by UnionPay is GMT +8
                    ipAddr, //"127.0.0.1",//Ã§â€�Â¨Ã¦Ë†Â·IP
                    "",//Ã§â€�Â¨Ã¦Ë†Â·Ã§Å“Å¸Ã¥Â®Å¾Ã¥Â§â€œÃ¥ï¿½ï¿½
                    "",//Ã©Â»ËœÃ¨Â®Â¤Ã¦â€�Â¯Ã¤Â»ËœÃ¦â€“Â¹Ã¥Â¼ï¿½
                    "",//Ã©Â»ËœÃ¨Â®Â¤Ã©â€œÂ¶Ã¨Â¡Å’Ã§Â¼â€“Ã¥ï¿½Â·
                    transTimeout,//Ã¤ÂºÂ¤Ã¦Ëœâ€œÃ¨Â¶â€¦Ã¦â€”Â¶Ã¦â€”Â¶Ã©â€”Â´
                    QuickPayConf.merFrontEndUrl,// Ã¥â€°ï¿½Ã¥ï¿½Â°Ã¥â€ºÅ¾Ã¨Â°Æ’Ã¥â€¢â€ Ã¦Ë†Â·URL
                                QuickPayConf.merBackEndUrl,// Ã¥ï¿½Å½Ã¥ï¿½Â°Ã¥â€ºÅ¾Ã¨Â°Æ’Ã¥â€¢â€ Ã¦Ë†Â·URL
                    merReserved //Ã¥â€¢â€ Ã¦Ë†Â·Ã¤Â¿ï¿½Ã§â€¢â„¢Ã¥Å¸Å¸
                };
    
            html = new QuickPayUtils().createPayHtml(QuickPayConf.reqVo, valueVo, signType, QuickPayConf.gateWay);//Ã¨Â·Â³Ã¨Â½Â¬Ã¥Ë†Â°Ã©â€œÂ¶Ã¨ï¿½â€�Ã©Â¡ÂµÃ©ï¿½Â¢Ã¦â€�Â¯Ã¤Â»Ëœ
    
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
    

}
