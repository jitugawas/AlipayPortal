package com.dpg.dynamicgateway;
import com.payitnz.config.DynamicPaymentConstant;

/**
 * ÃƒÂ¥Ã¯Â¿Â½Ã¯Â¿Â½ÃƒÂ§Ã‚Â§Ã‚Â°ÃƒÂ¯Ã‚Â¼Ã…Â¡ÃƒÂ¦Ã¢â‚¬ï¿½Ã‚Â¯ÃƒÂ¤Ã‚Â»Ã‹Å“ÃƒÂ©Ã¢â‚¬Â¦Ã¯Â¿Â½ÃƒÂ§Ã‚Â½Ã‚Â®ÃƒÂ§Ã‚Â±Ã‚Â»
 * ÃƒÂ¥Ã…Â Ã…Â¸ÃƒÂ¨Ã†â€™Ã‚Â½ÃƒÂ¯Ã‚Â¼Ã…Â¡ÃƒÂ©Ã¢â‚¬Â¦Ã¯Â¿Â½ÃƒÂ§Ã‚Â½Ã‚Â®ÃƒÂ§Ã‚Â±Ã‚Â»
 * ÃƒÂ§Ã‚Â±Ã‚Â»ÃƒÂ¥Ã‚Â±Ã…Â¾ÃƒÂ¦Ã¢â€šÂ¬Ã‚Â§ÃƒÂ¯Ã‚Â¼Ã…Â¡ÃƒÂ¥Ã¢â‚¬Â¦Ã‚Â¬ÃƒÂ¥Ã¢â‚¬Â¦Ã‚Â±ÃƒÂ§Ã‚Â±Ã‚Â»
 * ÃƒÂ§Ã¢â‚¬Â°Ã‹â€ ÃƒÂ¦Ã…â€œÃ‚Â¬ÃƒÂ¯Ã‚Â¼Ã…Â¡1.0
 * ÃƒÂ¦Ã¢â‚¬â€�Ã‚Â¥ÃƒÂ¦Ã…â€œÃ…Â¸ÃƒÂ¯Ã‚Â¼Ã…Â¡2011-03-11
 * ÃƒÂ¤Ã‚Â½Ã…â€œÃƒÂ¨Ã¢â€šÂ¬Ã¢â‚¬Â¦ÃƒÂ¯Ã‚Â¼Ã…Â¡ÃƒÂ¤Ã‚Â¸Ã‚Â­ÃƒÂ¥Ã¢â‚¬ÂºÃ‚Â½ÃƒÂ©Ã¢â‚¬Å“Ã‚Â¶ÃƒÂ¨Ã¯Â¿Â½Ã¢â‚¬ï¿½UPOPÃƒÂ¥Ã¢â‚¬ÂºÃ‚Â¢ÃƒÂ©Ã‹Å“Ã…Â¸
 * ÃƒÂ§Ã¢â‚¬Â°Ã‹â€ ÃƒÂ¦Ã¯Â¿Â½Ã†â€™ÃƒÂ¯Ã‚Â¼Ã…Â¡ÃƒÂ¤Ã‚Â¸Ã‚Â­ÃƒÂ¥Ã¢â‚¬ÂºÃ‚Â½ÃƒÂ©Ã¢â‚¬Å“Ã‚Â¶ÃƒÂ¨Ã¯Â¿Â½Ã¢â‚¬ï¿½
 * ÃƒÂ¨Ã‚Â¯Ã‚Â´ÃƒÂ¦Ã‹Å“Ã…Â½ÃƒÂ¯Ã‚Â¼Ã…Â¡ÃƒÂ¤Ã‚Â»Ã‚Â¥ÃƒÂ¤Ã‚Â¸Ã¢â‚¬Â¹ÃƒÂ¤Ã‚Â»Ã‚Â£ÃƒÂ§Ã‚Â Ã¯Â¿Â½ÃƒÂ¥Ã¯Â¿Â½Ã‚ÂªÃƒÂ¦Ã‹Å“Ã‚Â¯ÃƒÂ¤Ã‚Â¸Ã‚ÂºÃƒÂ¤Ã‚ÂºÃ¢â‚¬Â ÃƒÂ¦Ã¢â‚¬â€œÃ‚Â¹ÃƒÂ¤Ã‚Â¾Ã‚Â¿ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ¦Ã‚ÂµÃ¢â‚¬Â¹ÃƒÂ¨Ã‚Â¯Ã¢â‚¬Â¢ÃƒÂ¨Ã¢â€šÂ¬Ã…â€™ÃƒÂ¦Ã¯Â¿Â½Ã¯Â¿Â½ÃƒÂ¤Ã‚Â¾Ã¢â‚¬ÂºÃƒÂ§Ã…Â¡Ã¢â‚¬Å¾ÃƒÂ¦Ã‚Â Ã‚Â·ÃƒÂ¤Ã‚Â¾Ã¢â‚¬Â¹ÃƒÂ¤Ã‚Â»Ã‚Â£ÃƒÂ§Ã‚Â Ã¯Â¿Â½ÃƒÂ¯Ã‚Â¼Ã…â€™ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ¥Ã¯Â¿Â½Ã‚Â¯ÃƒÂ¤Ã‚Â»Ã‚Â¥ÃƒÂ¦Ã‚Â Ã‚Â¹ÃƒÂ¦Ã¯Â¿Â½Ã‚Â®ÃƒÂ¨Ã¢â‚¬Â¡Ã‚ÂªÃƒÂ¥Ã‚Â·Ã‚Â±ÃƒÂ§Ã‚Â½Ã¢â‚¬ËœÃƒÂ§Ã‚Â«Ã¢â€žÂ¢ÃƒÂ§Ã…Â¡Ã¢â‚¬Å¾ÃƒÂ©Ã…â€œÃ¢â€šÂ¬ÃƒÂ¨Ã‚Â¦Ã¯Â¿Â½ÃƒÂ¯Ã‚Â¼Ã…â€™ÃƒÂ¦Ã…â€™Ã¢â‚¬Â°ÃƒÂ§Ã¢â‚¬Â¦Ã‚Â§ÃƒÂ¦Ã…Â Ã¢â€šÂ¬ÃƒÂ¦Ã…â€œÃ‚Â¯ÃƒÂ¦Ã¢â‚¬â€œÃ¢â‚¬Â¡ÃƒÂ¦Ã‚Â¡Ã‚Â£ÃƒÂ§Ã‚Â¼Ã¢â‚¬â€œÃƒÂ¥Ã¢â‚¬Â Ã¢â€žÂ¢,ÃƒÂ¥Ã‚Â¹Ã‚Â¶ÃƒÂ©Ã¯Â¿Â½Ã…Â¾ÃƒÂ¤Ã‚Â¸Ã¢â€šÂ¬ÃƒÂ¥Ã‚Â®Ã…Â¡ÃƒÂ¨Ã‚Â¦Ã¯Â¿Â½ÃƒÂ¤Ã‚Â½Ã‚Â¿ÃƒÂ§Ã¢â‚¬ï¿½Ã‚Â¨ÃƒÂ¨Ã‚Â¯Ã‚Â¥ÃƒÂ¤Ã‚Â»Ã‚Â£ÃƒÂ§Ã‚Â Ã¯Â¿Â½ÃƒÂ£Ã¢â€šÂ¬Ã¢â‚¬Å¡ÃƒÂ¨Ã‚Â¯Ã‚Â¥ÃƒÂ¤Ã‚Â»Ã‚Â£ÃƒÂ§Ã‚Â Ã¯Â¿Â½ÃƒÂ¤Ã‚Â»Ã¢â‚¬Â¦ÃƒÂ¤Ã‚Â¾Ã¢â‚¬ÂºÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬Å¡ÃƒÂ¨Ã¢â€šÂ¬Ã†â€™ÃƒÂ£Ã¢â€šÂ¬Ã¢â‚¬Å¡
 * */

public class QuickPayConf {

	// ÃƒÂ§Ã¢â‚¬Â°Ã‹â€ ÃƒÂ¦Ã…â€œÃ‚Â¬ÃƒÂ¥Ã¯Â¿Â½Ã‚Â·
	public final static String version = "1.0.0";

	// ÃƒÂ§Ã‚Â¼Ã¢â‚¬â€œÃƒÂ§Ã‚Â Ã¯Â¿Â½ÃƒÂ¦Ã¢â‚¬â€œÃ‚Â¹ÃƒÂ¥Ã‚Â¼Ã¯Â¿Â½
	public final static String charset = "UTF-8";

	// ÃƒÂ¥Ã…Â¸Ã‚ÂºÃƒÂ§Ã‚Â¡Ã¢â€šÂ¬ÃƒÂ§Ã‚Â½Ã¢â‚¬ËœÃƒÂ¥Ã¯Â¿Â½Ã¢â€šÂ¬ÃƒÂ¯Ã‚Â¼Ã‹â€ ÃƒÂ¨Ã‚Â¯Ã‚Â·ÃƒÂ¦Ã…â€™Ã¢â‚¬Â°ÃƒÂ§Ã¢â‚¬ÂºÃ‚Â¸ÃƒÂ¥Ã‚ÂºÃ¢â‚¬ï¿½ÃƒÂ§Ã…Â½Ã‚Â¯ÃƒÂ¥Ã‚Â¢Ã†â€™ÃƒÂ¥Ã‹â€ Ã¢â‚¬Â¡ÃƒÂ¦Ã¯Â¿Â½Ã‚Â¢ÃƒÂ¯Ã‚Â¼Ã¢â‚¬Â°
//develpment
/*
        private final static String REMOTE_FRONT_END_IP = "http://127.0.0.1:8084";
        private final static String REMOTE_BACK_END_IP = "http://127.0.0.1:8084";
        private final static String LOCAL_FRONT_END_IP = "http://127.0.0.1:8084";
        private final static String LOCAL_BACK_END_IP = "http://127.0.0.1:8084";
*/
//Testing from local to 10.81(testing)

        private final static String REMOTE_FRONT_END_IP = "http://202.82.58.122:5555";
        private final static String REMOTE_BACK_END_IP =  "http://202.82.58.122:5555";
//        private final static String LOCAL_FRONT_END_IP = "http://www.ivaultit.com";
//        private final static String LOCAL_BACK_END_IP = "http://www.ivaultit.com";
        
        //test-server
        private final static String LOCAL_FRONT_END_IP = DynamicPaymentConstant.SERVER_HOST ;
        private final static String LOCAL_BACK_END_IP =  DynamicPaymentConstant.SERVER_HOST ;
        
        //local system
        //private final static String LOCAL_FRONT_END_IP = "http://localhost:8080";
        //private final static String LOCAL_BACK_END_IP = "http://localhost:8080";


        /* ÃƒÂ¥Ã¢â‚¬Â°Ã¯Â¿Â½ÃƒÂ¥Ã¯Â¿Â½Ã‚Â°ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ¦Ã‚ÂµÃ¢â‚¬Â¹ÃƒÂ¨Ã‚Â¯Ã¢â‚¬Â¢ÃƒÂ§Ã…Â½Ã‚Â¯ÃƒÂ¥Ã‚Â¢Ã†â€™ */
	private final static String UPOP_BASE_URL = REMOTE_FRONT_END_IP + "/DPGW/MerchantSrv/";

	/* ÃƒÂ¥Ã¢â‚¬Â°Ã¯Â¿Â½ÃƒÂ¥Ã¯Â¿Â½Ã‚Â°ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“PMÃƒÂ§Ã…Â½Ã‚Â¯ÃƒÂ¥Ã‚Â¢Ã†â€™ÃƒÂ¯Ã‚Â¼Ã‹â€ ÃƒÂ¥Ã¢â‚¬Â¡Ã¢â‚¬Â ÃƒÂ§Ã¢â‚¬ï¿½Ã…Â¸ÃƒÂ¤Ã‚ÂºÃ‚Â§ÃƒÂ§Ã…Â½Ã‚Â¯ÃƒÂ¥Ã‚Â¢Ã†â€™ÃƒÂ¯Ã‚Â¼Ã¢â‚¬Â° */
	//private final static String UPOP_BASE_URL = "https://www.epay.lxdns.com/UpopWeb/api/";

	/* ÃƒÂ¥Ã¢â‚¬Â°Ã¯Â¿Â½ÃƒÂ¥Ã¯Â¿Â½Ã‚Â°ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ§Ã¢â‚¬ï¿½Ã…Â¸ÃƒÂ¤Ã‚ÂºÃ‚Â§ÃƒÂ§Ã…Â½Ã‚Â¯ÃƒÂ¥Ã‚Â¢Ã†â€™ */
	//private final static String UPOP_BASE_URL = "https://unionpaysecure.com/api/";
	
	/* ÃƒÂ¥Ã¯Â¿Â½Ã…Â½ÃƒÂ¥Ã¯Â¿Â½Ã‚Â°ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ¦Ã‚ÂµÃ¢â‚¬Â¹ÃƒÂ¨Ã‚Â¯Ã¢â‚¬Â¢ÃƒÂ§Ã…Â½Ã‚Â¯ÃƒÂ¥Ã‚Â¢Ã†â€™ */
	private final static String UPOP_BSPAY_BASE_URL = REMOTE_BACK_END_IP + "/DPGW/MerchantSrv/";
	
	/* ÃƒÂ¥Ã¯Â¿Â½Ã…Â½ÃƒÂ¥Ã¯Â¿Â½Ã‚Â°ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“PMÃƒÂ§Ã…Â½Ã‚Â¯ÃƒÂ¥Ã‚Â¢Ã†â€™ÃƒÂ¯Ã‚Â¼Ã‹â€ ÃƒÂ¥Ã¢â‚¬Â¡Ã¢â‚¬Â ÃƒÂ§Ã¢â‚¬ï¿½Ã…Â¸ÃƒÂ¤Ã‚ÂºÃ‚Â§ÃƒÂ§Ã…Â½Ã‚Â¯ÃƒÂ¥Ã‚Â¢Ã†â€™ÃƒÂ¯Ã‚Â¼Ã¢â‚¬Â° */
//	private final static String UPOP_BSPAY_BASE_URL = "https://www.epay.lxdns.com/UpopWeb/api/";
	
	/* ÃƒÂ¥Ã¯Â¿Â½Ã…Â½ÃƒÂ¥Ã¯Â¿Â½Ã‚Â°ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ§Ã¢â‚¬ï¿½Ã…Â¸ÃƒÂ¤Ã‚ÂºÃ‚Â§ÃƒÂ§Ã…Â½Ã‚Â¯ÃƒÂ¥Ã‚Â¢Ã†â€™ */
	//private final static String UPOP_BSPAY_BASE_URL = "https://besvr.unionpaysecure.com/api/";
	
	/* ÃƒÂ¦Ã…Â¸Ã‚Â¥ÃƒÂ¨Ã‚Â¯Ã‚Â¢ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ¦Ã‚ÂµÃ¢â‚¬Â¹ÃƒÂ¨Ã‚Â¯Ã¢â‚¬Â¢ÃƒÂ§Ã…Â½Ã‚Â¯ÃƒÂ¥Ã‚Â¢Ã†â€™ */
	private final static String UPOP_QUERY_BASE_URL = REMOTE_BACK_END_IP + "/DPGW/MerchantSrv/";
	
	/* ÃƒÂ¦Ã…Â¸Ã‚Â¥ÃƒÂ¨Ã‚Â¯Ã‚Â¢ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“PMÃƒÂ§Ã…Â½Ã‚Â¯ÃƒÂ¥Ã‚Â¢Ã†â€™ÃƒÂ¯Ã‚Â¼Ã‹â€ ÃƒÂ¥Ã¢â‚¬Â¡Ã¢â‚¬Â ÃƒÂ§Ã¢â‚¬ï¿½Ã…Â¸ÃƒÂ¤Ã‚ÂºÃ‚Â§ÃƒÂ§Ã…Â½Ã‚Â¯ÃƒÂ¥Ã‚Â¢Ã†â€™ÃƒÂ¯Ã‚Â¼Ã¢â‚¬Â° */
//	private final static String UPOP_QUERY_BASE_URL = "https://www.epay.lxdns.com/UpopWeb/api/";
	
	/* ÃƒÂ¦Ã…Â¸Ã‚Â¥ÃƒÂ¨Ã‚Â¯Ã‚Â¢ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ§Ã¢â‚¬ï¿½Ã…Â¸ÃƒÂ¤Ã‚ÂºÃ‚Â§ÃƒÂ§Ã…Â½Ã‚Â¯ÃƒÂ¥Ã‚Â¢Ã†â€™ */
	//private final static String UPOP_QUERY_BASE_URL = "https://query.unionpaysecure.com/api/";

	// ÃƒÂ¦Ã¢â‚¬ï¿½Ã‚Â¯ÃƒÂ¤Ã‚Â»Ã‹Å“ÃƒÂ§Ã‚Â½Ã¢â‚¬ËœÃƒÂ¥Ã¯Â¿Â½Ã¢â€šÂ¬
	public final static String gateWay = UPOP_BASE_URL + "Payment.action";

	// ÃƒÂ¥Ã¯Â¿Â½Ã…Â½ÃƒÂ§Ã‚Â»Ã‚Â­ÃƒÂ¤Ã‚ÂºÃ‚Â¤ÃƒÂ¦Ã‹Å“Ã¢â‚¬Å“ÃƒÂ§Ã‚Â½Ã¢â‚¬ËœÃƒÂ¥Ã¯Â¿Â½Ã¢â€šÂ¬
	public final static String backStagegateWay = UPOP_BSPAY_BASE_URL + "AutoPayment.action";

	// ÃƒÂ¦Ã…Â¸Ã‚Â¥ÃƒÂ¨Ã‚Â¯Ã‚Â¢ÃƒÂ§Ã‚Â½Ã¢â‚¬ËœÃƒÂ¥Ã¯Â¿Â½Ã¢â€šÂ¬
	public final static String queryUrl = UPOP_QUERY_BASE_URL + "Query.action";
	
	// ÃƒÂ¨Ã‚Â®Ã‚Â¤ÃƒÂ¨Ã‚Â¯Ã¯Â¿Â½ÃƒÂ¦Ã¢â‚¬ï¿½Ã‚Â¯ÃƒÂ¤Ã‚Â»Ã‹Å“2.0ÃƒÂ§Ã‚Â½Ã¢â‚¬ËœÃƒÂ¥Ã¯Â¿Â½Ã¢â€šÂ¬
	public final static String authPayUrl = UPOP_BASE_URL + "AuthPay.action";
	
	// ÃƒÂ¥Ã¯Â¿Â½Ã¢â‚¬ËœÃƒÂ§Ã…Â¸Ã‚Â­ÃƒÂ¤Ã‚Â¿Ã‚Â¡ÃƒÂ§Ã‚Â½Ã¢â‚¬ËœÃƒÂ¥Ã¯Â¿Â½Ã¢â€šÂ¬
	public final static String smsUrl = UPOP_BASE_URL + "Sms.action";
        //information below to verify the signature
	// ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ¤Ã‚Â»Ã‚Â£ÃƒÂ§Ã‚Â Ã¯Â¿Â½
	public final static String merCode = "620055473925001"; // "105550149170027";

	// ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ¥Ã¯Â¿Â½Ã¯Â¿Â½ÃƒÂ§Ã‚Â§Ã‚Â°
	public final static String merName = "Ahura Consulting Ltd";
	
	//public final static String merFrontEndUrl = LOCAL_FRONT_END_IP + "/UpopSDK/MDKFrontEndUrl";
	public final static String merFrontEndUrl = LOCAL_FRONT_END_IP + "PayitnzNew/unionPayResponse";

	//public final static String merBackEndUrl = LOCAL_BACK_END_IP + "/UpopSDK/MDKBackEndUrl";
	public final static String merBackEndUrl = LOCAL_BACK_END_IP + "PayitnzNew/unionPayResponse";

	// ÃƒÂ¥Ã…Â Ã‚Â ÃƒÂ¥Ã‚Â¯Ã¢â‚¬Â ÃƒÂ¦Ã¢â‚¬â€œÃ‚Â¹ÃƒÂ¥Ã‚Â¼Ã¯Â¿Â½
	public final static String signType = "MD5";
	public final static String signType_SHA1withRSA = "SHA1withRSA";

	// ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¥Ã…Â¸Ã…Â½ÃƒÂ¥Ã‚Â¯Ã¢â‚¬Â ÃƒÂ¥Ã…â€™Ã¢â€žÂ¢ÃƒÂ¯Ã‚Â¼Ã…â€™ÃƒÂ©Ã…â€œÃ¢â€šÂ¬ÃƒÂ¨Ã‚Â¦Ã¯Â¿Â½ÃƒÂ¥Ã¢â‚¬â„¢Ã…â€™ÃƒÂ©Ã¢â‚¬Å“Ã‚Â¶ÃƒÂ¨Ã¯Â¿Â½Ã¢â‚¬ï¿½ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ§Ã‚Â½Ã¢â‚¬ËœÃƒÂ§Ã‚Â«Ã¢â€žÂ¢ÃƒÂ¤Ã‚Â¸Ã…Â ÃƒÂ©Ã¢â‚¬Â¦Ã¯Â¿Â½ÃƒÂ§Ã‚Â½Ã‚Â®ÃƒÂ§Ã…Â¡Ã¢â‚¬Å¾ÃƒÂ¤Ã‚Â¸Ã¢â€šÂ¬ÃƒÂ¦Ã‚Â Ã‚Â·
	public final static String securityKey = "88888888";
//        public final static String securityKey = "2UPxPIkgRzGPuS3gTLZiw9Rx9AkYK3Rg";

	// ÃƒÂ§Ã‚Â­Ã‚Â¾ÃƒÂ¥Ã¯Â¿Â½Ã¯Â¿Â½
	public final static String signature = "signature";
	public final static String signMethod = "signMethod";

        public final static boolean isMerInterface = true;

//ÃƒÂ¦Ã…â€œÃ‚ÂºÃƒÂ¦Ã…Â¾Ã¢â‚¬Å¾ÃƒÂ¦Ã…Â½Ã‚Â¥ÃƒÂ¥Ã¯Â¿Â½Ã‚Â£
        //ÃƒÂ§Ã‚Â»Ã¢â‚¬Å¾ÃƒÂ¨Ã‚Â£Ã¢â‚¬Â¦ÃƒÂ¦Ã‚Â¶Ã‹â€ ÃƒÂ¨Ã‚Â´Ã‚Â¹ÃƒÂ¨Ã‚Â¯Ã‚Â·ÃƒÂ¦Ã‚Â±Ã¢â‚¬Å¡ÃƒÂ¥Ã…â€™Ã¢â‚¬Â¦
        
        public final static String[] backReqVo = new String[]{
			"version",
            "charset",
            "transType",
            "origQid",
            "merId",
            "merAbbr",
            "acqCode",
            "merCode",
            "orderNumber",
            "orderAmount",
            "orderCurrency",
            "orderTime",
            "customerIp",
            "transTimeout",
            "frontEndUrl",
            "backEndUrl",
            "merReserved"
	};
        
	public final static String[] reqVo = new String[]{
			"version",
            "charset",
            "transType",
            "origQid",
            "merId",
            "merAbbr",
            "acqCode",
            "merCode",
            "commodityUrl",
            "commodityName",
            "commodityUnitPrice",
            "commodityQuantity",
            "commodityDiscount",
            "transferFee",
            "orderNumber",
            "orderAmount",
            "orderCurrency",
            "orderTime",
            "customerIp",
            "customerName",
            "defaultPayType",
            "defaultBankNumber",
            "transTimeout",
            "frontEndUrl",
            "backEndUrl",
            "merReserved"
	};
        
        public final static String[] reqVo2 = new String[]{
			"version",
            "charset",
            "transType",
            "origQid",
            "merId",
            "merAbbr",
            "acqCode",
            "merCode",
            "orderNumber",
            "orderAmount",
            "orderCurrency",
            "orderTime",
            "customerIp",
            "customerName",
            "transTimeout",
            "frontEndUrl",
            "backEndUrl",
            "merReserved"
	};

	public final static String[] notifyVo = new String[]{
            "charset",
            "cupReserved",
            "exchangeDate",
            "exchangeRate",
            "merAbbr",
            "acqCode",
            "merId",
            "orderAmount",
            "orderCurrency",
            "orderNumber",
            "qid",
            "respCode",
            "respMsg",
            "respTime",
            "settleAmount",
            "settleCurrency",
            "settleDate",
            "traceNumber",
            "traceTime",
            "transType",
            "version"
	};

	public final static String[] queryVo = new String[]{
		"version",
		"charset",
		"transType",
                "acqCode",
		"merId",
		"orderNumber",
		"orderTime",
		"merReserved"
	};
        
        

    public final static String[] queryRes = new String[]{
        "version",
        "charset",
        "signMethod",
        "signature",
        "transType",
        "respCode",
        "respTime",
        "queryResult",
        "qid",
        "traceNumber",
        "traceTime",
        "settleAmount",
        "settleCurrency",
        "settleDate",
        "exchangeRate",
        "exchangeDate",
        "cupReserved"
    };

//ÃƒÂ¥Ã¢â‚¬Â¢Ã¢â‚¬Â ÃƒÂ¦Ã‹â€ Ã‚Â·ÃƒÂ¦Ã…Â½Ã‚Â¥ÃƒÂ¥Ã¯Â¿Â½Ã‚Â£
        //ÃƒÂ§Ã‚Â»Ã¢â‚¬Å¾ÃƒÂ¨Ã‚Â£Ã¢â‚¬Â¦ÃƒÂ¦Ã‚Â¶Ã‹â€ ÃƒÂ¨Ã‚Â´Ã‚Â¹ÃƒÂ¨Ã‚Â¯Ã‚Â·ÃƒÂ¦Ã‚Â±Ã¢â‚¬Å¡ÃƒÂ¥Ã…â€™Ã¢â‚¬Â¦
	public final static String[] merReqVo = new String[]{
			"version",
            "charset",
            "transType",
            "origQid",
            "merId",
            "merAbbr",
            "commodityUrl",
            "commodityName",
            "commodityUnitPrice",
            "commodityQuantity",
            "commodityDiscount",
            "transferFee",
            "orderNumber",
            "orderAmount",
            "orderCurrency",
            "orderTime",
            "customerIp",
            "customerName",
            "defaultPayType",
            "defaultBankNumber",
            "transTimeout",
            "frontEndUrl",
            "backEndUrl",
            "merReserved"
	};

	public final static String[] merNotifyVo = new String[]{
            "charset",
            "cupReserved",
            "exchangeDate",
            "exchangeRate",
            "merAbbr",
            "merId",
            "orderAmount",
            "orderCurrency",
            "orderNumber",
            "qid",
            "respCode",
            "respMsg",
            "respTime",
            "settleAmount",
            "settleCurrency",
            "settleDate",
            "traceNumber",
            "traceTime",
            "transType",
            "version"
	};

	public final static String[] merQueryVo = new String[]{
		"version",
		"charset",
		"transType",
		"merId",
		"orderNumber",
		"orderTime",
		"merReserved"
	};

    public final static String[] merQueryRes = new String[]{
        "version",
        "charset",
        "signMethod",
        "signature",
        "transType",
        "respCode",
        "respTime",
        "queryResult",
        "qid",
        "traceNumber",
        "traceTime",
        "settleAmount",
        "settleCurrency",
        "settleDate",
        "exchangeRate",
        "exchangeDate",
        "cupReserved"
    };

    public final static String[] smsVo = new String[]{
		"version",
		"charset",
		"acqCode",
		"merId",
		"merAbbr",
		"orderNumber",
		"orderAmount",
        "orderCurrency",
		"merReserved"
	};


}
