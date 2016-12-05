package com.payitnz.util;

import com.payitnz.config.DynamicPaymentConstant;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URISyntaxException;

public class TwilioSMSApi {

    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID = DynamicPaymentConstant.TWILIO_ACCOUNT_SID;
    public static final String AUTH_TOKEN = DynamicPaymentConstant.TWILIO_ACCOUNT_TOKEN;

    public static void main(String[] args) throws URISyntaxException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message
                .creator(new PhoneNumber("+917744834998"),  // to
                         new PhoneNumber(DynamicPaymentConstant.TWILIO_FROM_NO),  // from
                         "Where's Wallace?")
                .create();
    }
    
    public boolean sendSMSAlert(String messageAlert,String toNumber){
    
    	   Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

           Message message = Message
                   .creator(new PhoneNumber(toNumber),  // to
                            new PhoneNumber(DynamicPaymentConstant.TWILIO_FROM_NO),  // from
                            messageAlert)
                   .create();
           if(message.getStatus().equals("1")){
        	   return true;
           }else{
        	   return false;
           }
           
    }
}