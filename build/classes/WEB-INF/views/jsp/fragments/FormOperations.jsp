<script type="text/javascript">
  $(document).ready(function () {
	 
	  /*	 $(".rd_amount").attr('readonly', 'true');
		 $("#url").hide();
	 $(".rd_amount").attr('readonly', 'true');
	  $(".newtr").hide();
	  $("#btnAddnew").hide();
	  $("#editSave").hide();
	  $("#editClear").hide();
	  $(".btnback").hide();
	  
	  var secretKey = $("#first_secret_key").val();
  		if(secretKey != "")
  		{
  			$(".saveSecret").prop('value', 'Re-Generate');
  		}
  		
	  if($("#elseBlock").val() == "0")
		  {
		  $(".btnback").show();
		  $(".btnNext").hide();
		  $(".btnreset").hide();
		  }
	  
	  $('#clear_dynamic_button').click(function(){
		
		$("#txtUserID").val("");
		$("#txtMcc").val("");
		$("#txtTtype").val("");
		$("#txtMname").val("");
		$("#txtCurl").val("");
		$("#txtTransCurr").val("");
		$("#txtOrderTO").val("");
		$("#txtMarcres").val("");
		$("#supported_method_dynamic").val("");
		//$("#txtOrderTO").val("");
			
	  });
	  */
	 	  
	  $('#clear_alipay_button').click(function(){
		  var service = $("#service").val();
		  	var alipay_partner_ID = $("#payitnz_id").val("");
		  	
		  	var alipay_partner_key = $("#partner_key").val("")	;
		  	var character_set = $("#charSet").val("");
		  	var return_utl = $("#return_url").val("");
		 	var currency = $("#currency").val("");
			
		  });
	  
	  $('#clear_alipay_online_button').click(function(){
	
       	$("#alipay_online_partner_id").val("");
       	$("#alipay_online_partner_key").val("");
       	$("#online_character_set").val();
       	$("#alipay_online_notification_url").val("");
       	$("#alipay_online_return_url").val("");
       	$("#order_valid_time").val(0);
      	$("#online_currency").val("");
       	$("#alipay_supported_method").attr('checked',false);
			
		  });
	  
	  /*
	  $('#clear_poli_button').click(function(){
			
		  var accountID = $("#poli_account_id").val("");
	       	var password = $("#password").val("");
	       	var merchant_reference = $("#poli_merchant_reference").val("");
	  
	       var merchant_data = $("#merchant_data").val("");
	       	var payment_method = $("#payment_method").val("");
	       	var currency_code = $("#currency_code").val("");
	       	var merchant_reference_format = $("#merchant_reference_format").val("");
	    
	       	var timeout = $("#fi_code").val("");
	      	var timeout = $("#company_code").val("");
	      	$("#poli_homepage_url").val("");
	    	$("#poli_success_url").val("");
	    	$("#poli_failure_url").val("");
	    	$("#poli_cancellation_url").val("");
	    	$("#poli_notification_url").val("");
	    	
	    	$("#poli_timeout").val("");
	      	
	      	var supported_method_poli = $("#supported_method_poli").val("");
				
		  });
     	
	  
	  $('#clear_dps_button').click(function(){
		  var PxPayUserId = $("#PxPayUserId").val("");
		  	var PxPayKey = $("#PxPayKey").val("");
		  	
		  	var PxPayUrl = $("#PxPayUrl").val("")	;
		  	var email = $("#email").val("");
		 		
		  	var supported_method = $("#DPSupported_method").val("");
		  	
		   	var success_url = $("#DPSsuccess_url").val("");
		     	var failure_url = $("#DPSfailure_url").val("");
			
	  });
	  
	 
	  $('#clear_f2c_button').click(function(){
			
		 //$("#service").val("");
		  	var accountID = $("#account_id").val("");
		  	
		  	var secret_key = $("#secret_key").val("");
		  	var merchant_reference = $("#merchant_reference").val("");
		  	var notification_url = $("#f2c_notification_url").val("");
		  	var header_image = $("#header_image").val("");
		  	$("#f2c_merchant_reference").val("");
		  	$("#f2c_return_url").val("");
		  	$("#backf").val("");
		  	$("#f2c_custom_data").val("");
		  	
		  	$("#backf").css("background-color", "#FFFFFF"); 
			$("#border_color").css("background-color", "#FFFFFF"); 
		
		  var custom_data = $("#custom_data").val("");
		  	var store_card = $("#store_card").val("");
		  	
		  	var display_customer_email = $("#display_customer_email").val("");
		  	var payment_method = $("#payment_method").val("");
		  	var visa = $("#visa").val("");
		  	var american_express = $("#american_express").val("");
		  	var dinner_club = $("#dinner_club").val("");
		  	var border_color = $("#border_color").val("FFFFFF");
		  	var border_color = $("#border_color").val("FFFFFF");
		  	var background_color = $("#backf").val("FFFFFF");
				
		  });
	 
	  $('.urlBound').attr('checked', false);
	  
            $(".saveClass").click(function(){
            	var txtvalue = $(this).parent().parent().find('input').val();
            	var txtid = $(this).parent().parent().find('input').attr('id');
            	if (confirm('Are you sure you want to save this Url')) {
            		$.ajax({
            			type : "GET",
            			url : "${home}UpdateKeyInfo",
            			data: "secret_key_url="+txtvalue +"&id="+txtid,

            			  success: function(response){
            			        // we have the response
            			      location.reload();
            			        $('#txtvalue').val('');
            			       
            			        },
            			        error: function(e){
            			        alert('Error: ' + e);
            			        }
            			        });
            	} else {
            	    // Do nothing!
            	}
            	
            	
            });
 
           /* $(".re_generateClass").click(function(){
            	var txtvalue = $(this).parent().parent().find('input').val();
            	var txtid = $(this).parent().parent().find('input').attr('id');
            	if (confirm('Are you sure you want to Generate new Key?')) {
            		$.ajax({
            			type : "GET",
            			url : "${home}regenretSkey",
            			data: "id="+txtid+"&secret_key_url="+txtvalue,

            			  success: function(response){
            			        // we have the response
            			      location.reload();
            			        $('#txtvalue').val('');
            			       
            			        },
            			        error: function(e){
            			        alert('Error: ' + e);
            			        }
            			        });
            	} else {
            	    // Do nothing!
            	}
         	
            	
            });
            $(".generateClass").click(function(){
            	var url = $("#first_name").val();
            	if (confirm('Are you sure you want to Generate new Key?')) {
            		if(url != NaN && url != "")
            			{
            		$.ajax({
            			type : "GET",
            			url : "${home}SaveSecretKey",
            			data: "secret_key_url="+url,
            			
                         	  success: function(response){
            			        // we have the response
            			      location.reload();
            			        $('#txtvalue').val('');
            			       
            			        },
            			        error: function(e){
            			        alert('Error: ' + e);
            			        }
            			        });
            			}
            		else
            			{
            			alert("URL should not be empty");
            			}
            	} else {
            	    // Do nothing!
            	}
         	
            	
            });*/
            /*
            $(".saveSecret").click(function(){
            	var secretKey = $("#first_secret_key").val();
            	
            	if (confirm('Are you sure you want to Generate new Key?')) {
            		$.ajax({
            			type : "GET",
            			url : "${home}SaveSecretKey",
            			data: "secret_key="+secretKey,
                         	  success: function(response){
            			        // we have the response
            			  
            			        $('#txtvalue').val('');
            			       
            			        },
            			        error: function(e){
            			       
            			        }
            			        });
            	} else {
            	    // Do nothing!
            	}
            	
            });
            
            $(".deleteClass").click(function(){
            	var txtvalue = $(this).parent().parent().find('input').val();
            	var txtid = $(this).parent().parent().find('input').attr('id');
            	if (confirm('Are you sure you want to Delete Key?')) {
            		$.ajax({
            			type : "GET",
            			url : "${home}deleteKey",
            			data: "id="+txtid,

            			  success: function(response){
            			        // we have the response
            			      location.reload();
            			        $('#txtvalue').val('');
            			       
            			        },
            			        error: function(e){
            			        alert('Error: ' + e);
            			        }
            			        });
            	} else {
            	    // Do nothing!
            	}
         	
            	
            });
            var counter = 1;
            $("#btnAddnew").click(function(event){
            	$(".newtr").show();
            	  $("#editSave").show();
              	  $("#editClear").show();
           });
    
            
           /* $('.urlBound').change(function() {
                if($(this).is(":checked")) {
                    var returnVal = "Are you sure?";
                    $(this).attr("checked", returnVal);
                    $(".secKey").attr("readonly", false); 
                    $("#btnAddnew").show();
                  
                }
                else
                	{
                	var returnVal = "Are you sure?";
                    $(this).attr("unchcked", returnVal);
                    $(".secKey").attr("readonly", true); 
                    $("#btnAddnew").hide();
                    $("#editSave").hide();
               	    $("#editClear").hide();
              	    $(".newtr").hide();
                	}
                   
            });
            
            
            //setupConnection jsp
            */
            
              $('#save_dynamic_button').click(function () {

            	 $('#dynamic_con_form').attr('action',"saveDynamicPayConnection");  
                 var txtUserID = $("#txtUserID").val();
             	var txtMcc = $("#txtMcc").val();
             	var txtTtype = $("#txtTtype").val();
             	var txtMname = $("#txtMname").val();
             	var txtCurl = $("#txtCurl").val();
             	var txtTransCurr = $("#txtTransCurr").val();
             	var txtOrderTO = $("#txtOrderTO").val();
             	var txtMarcres = $("#txtMarcres").val();
             	var cancellationUrl = $("#dynamic_cancellation_url").val();
             	var supported_method = $("#supported_method_dynamic").val();
             	
             	if(txtUserID!="" && txtUserID >= 1)
             	{
             		if(txtTtype!="" && txtTtype >= 1)
             	   	{
             			if(txtOrderTO!="" && txtOrderTO >= 1)
                 	   	{
             	if (confirm('Are you sure you want to save this Gateway Information.')) {
             	if(txtUserID!="" && txtMcc != "" && txtType != "")
             		{
             		
             		$.ajax({
             			type : "POST",
             			url : "${home}saveDynamicPayConnection",
             			data: "merchant_id="+txtUserID+"&mcc="+txtMcc+
             			"&merchant_name="+txtMname+"&commodity_url="+txtCurl+"&currency"+txtTransCurr+
             			"&timeout="+txtOrderTO+"&merchant_reserved_field="+txtMarcres+"&dynamic_cancellation_url"+cancellationUrl,

             			success: function(response){
         			        // we have the response
         			        
								location.reload(true)            	
								alert("Successfully Registered");
         			        $('#txtvalue').val('');
         			       
         			        },
             			        error: function(e){
             			        alert('Error: ' + e);
             			        }
             			        });
             		}
             		}
             	else
             		{
             		
             		}
                 	   	}
             			else
             				{
             				alert("Invalid Order Timeout!");
             				}
             	   	}
             		else
             			{
                 		alert("Invalid Transaction Type!");

             			}
             		}
             	else
             		{
             		alert("Invalid Marchant ID!");
             		}
             });
            
            
            
            $('#save_F2c_button').click(function () {
            	 $('#f2c_con_form').attr('action',"saveF2CPayConnection");  
           	  $("#Dyerror").hide();
           	  $("#F2Cerror").show();
				
               var service = $("#sevice").val();
            	var accountID = $("#account_id").val();
            	
            	var secret_key = $("#secret_key").val();
            	var merchant_reference = $("#merchant_reference").val();
            	var notification_url = $("#notification_url").val();
            	var header_image = $("#header_image").val();
            //	var header_bottom_border_color = $("#txtOrderTO").val();
            //	var header_background_color = $("#header_background_color").val();
            var custom_data = $("#custom_data").val();
            	//var store_card = $("#store_card").val();
            	
            	//var display_customer_email = $("#display_customer_email").val();
            	var payment_method = $("#payment_method").val();
            	var visa = $("#visa").val();
            	var american_express = $("#american_express").val();
            	var dinner_club = $("#dinner_club").val();
            	var border_color = $("#border_color").val();
            	var background_color = $("#background_color").val();
            	
            	var display_customer_email = $('#display_customer_email :selected').text();
            	//$("#display_customer_email").val();
            	var store_card = $('#store_card :selected').text();
            	
            	if (confirm('Are you sure you want to save this Gateway Information.')) {
            
            	if(accountID!="" && txtMcc != "" && txtType != "")
            		{
            		debugger;
            		
            		$.ajax({
            			type : "POST",
            			url : "${home}saveF2CPayConnection",
            			data: "sevice="+sevice+"&account_id="+accountID+"&secret_key "+secret_key+
            			"&merchant_reference ="+merchant_reference +"&notification_url"+notification_url +
            			"&header_image ="+header_image  +"&f2c_custom_data "+custom_data+
            			"&store_card ="+store_card +"&display_customer_email "+display_customer_email+ 
            			"header_bottom_border_color"+border_color+"header_background_color"+background_color,
							
            			  success: function(response){
            			        // we have the response
            			        
								location.reload(true)            	
								alert("Successfully Registered");
            			        $('#txtvalue').val('');
            			       
            			        },
            			        error: function(e){
            			        alert('Error: ' + e);
            			        }
            			        });
            		}
            		}
            	        
            });

            $('#save_Poli_button').click(function () {
           	 $('#poli_con_form').attr('action',"savePoliPayConnection");  
          	  $("#Dyerror").hide();
          	  $("#F2Cerror").show();

           	var accountID = $("#account_id").val();
           	var password = $("#password").val();
           	var merchant_reference = $("#merchant_reference").val();
           	var notification_url = $("#notification_url").val();
           var merchant_data = $("#merchant_data").val();
           	var payment_method = $("#payment_method").val();
           	var currency_code = $("#currency_code").val();
           	var merchant_reference_format = $("#merchant_reference_format").val();
           	var homepage_url = $("#homepage_url").val();
           	var success_url = $("#success_url").val();
           	var failure_url = $("#failure_url").val();
           	var cancellation_url = $("#cancellation_url").val();
           	var timeout = $("#fi_code").val();
          	var timeout = $("#company_code").val();

          	var supported_method_poli = $("#supported_method_poli").val();
          	if (confirm('Are you sure you want to save this Gateway Information.')) {
           	if(accountID!="" && txtMcc != "" && txtType != "")
           		{
           		
           		$.ajax({
           			type : "POST",
           			url : "${home}savePoliPayConnection",
           			data: "password="+password+"&account_id="+accountID+
           			"&merchant_reference ="+merchant_reference +"&notification_url"+notification_url +
           			"&currency_code ="+currency_code  +"&merchant_data "+merchant_data+
           			"&merchant_reference_format ="+merchant_reference_format +"&homepage_url "+homepage_url+ 
           			"&payment_method  ="+payment_method +"&success_url="+success_url + 
           			"&failure_url   ="+failure_url +"&cancellation_url="+cancellation_url+
           			"&timeout="+timeout+"&fi_code="+fi_code+
           			"&company_code="+company_code,
							
           			  success: function(response){
           			        // we have the response
           			        
								location.reload(true)            	
								alert("Successfully Registered");
           			        $('#txtvalue').val('');
           			       
           			        },
           			        error: function(e){
           			        alert('Error: ' + e);
           			        }
           			        });
           		}
           		}
           	        
           });
            
                        
            $('#save_alipay_button').click(function () {
           	 $('#alipay_con_form').attr('action',"saveAlipayPayConnection");  
          	  $("#Dyerror").hide();
          	  $("#F2Cerror").show();
  
              var service = $("#service").val();
           	var alipay_partner_ID = $("#payitnz_id").val();
           	var alipay_partner_key = $("#partner_key").val()	;
           	var character_set = $("#charSet").val();
           	var return_utl = $("#return_url").val();
          		var currency = $("#currency").val();

           	if (confirm('Are you sure you want to save this Gateway Information.')) {
           	if($('#alipay_con_form').validate())
           	{
           		
           		$.ajax({
           			type : "POST",
           			url : "${home}saveAlipayPayConnection",
           			data: "service="+service+"&payitnz_id="+alipay_partner_ID+"&partner_key "+alipay_partner_key+
           			"&charSet ="+character_set + "&return_utl ="+return_utl +
           			"&currency ="+currency,
							
           			  success: function(response){
           			        // we have the response
           			        
								location.reload(true)            	
								alert("Successfully Registered");
           			        $('#txtvalue').val('');
           			       
           			        },
           			        error: function(e){
           			        alert('Error: ' + e);
           			        }
           			        });
           	}
            }
           	        
           });
           
           
           
           $('#save_alipay_online_button').click(function () {
          	$('#alipay_con_form1').attr('action',"saveAlipayPayOnlineConnection");  
           $("#Dyerror").hide();
        	$("#F2Cerror").show();
        	
           var service = $("#online_service").val();
         	var alipay_partner_ID = $("#alipay_online_partner_id").val();
         	var alipay_partner_key = $("#alipay_online_partner_key").val()	;
         	var character_set = $("#online_character_set").val();
         	var notification_url = $("#alipay_online_notification_url").val();
         	var return_utl = $("#alipay_online_return_url").val();
         	var order_valid_time = $("#order_valid_time").val();
        	var currency = $("#online_currency").val();
         	var supported_method = $("#alipay_supported_method").val();
         	
         	if (confirm('Are you sure you want to save this Gateway Information.')) {
         		if($('#alipay_con_form1').validate())
           	{
         		$.ajax({
         			type : "POST",
         			url : "${home}saveAlipayPayOnlineConnection",
         			data: "online_service="+service+"&alipay_online_partner_id="+alipay_partner_ID+"&alipay_online_partner_key "+alipay_partner_key+
         			"&online_character_set ="+character_set +"&alipay_online_notification_url"+notification_url +
         			"&alipay_online_return_url ="+return_utl  +"&order_valid_time "+order_valid_time+
         			"&online_currency ="+currency + 
         			"&alipay_supported_method  ="+supported_method,
							
         			  success: function(response){
         			        // we have the response
         			        
								location.reload(true)            	
								alert("Successfully Registered");
         			        $('#txtvalue').val('');
         			       
         			        },
         			        error: function(e){
         			        alert('Error: ' + e);
         			        }
         			        });
         		}
         		
         	 }
           });
            
           
            $('#save_DPS_button').click(function () {
            	 $('#dps_con_form').attr('action',"saveDPSPayConnection");  
           	  $("#Dyerror").hide();
           	  $("#F2Cerror").show();
         
               var PxPayUserId = $("#PxPayUserId").val();
            	var PxPayKey = $("#PxPayKey").val();
            	
            	var PxPayUrl = $("#PxPayUrl").val()	;
            	var email = $("#email").val();
           		var currency = $("#DPScurrency").val();
            	var supported_method = $("#DPSupported_method").val();
            	
             	var success_url = $("#DPSsuccess_url").val();
               	var failure_url = $("#DPSfailure_url").val();
               	var transaction_type = $("#DPStransaction_type").val();
               	var visa = $("#dpsvisa").val();
            	var american_express = $("#dpsamerican_express").val();
            	var dinner_club = $("#dpsdinner_club").val();
               	if (confirm('Are you sure you want to save this Gateway Information.')) {
               	if(PxPayUserId != "")
               	{
               		if(PxPayKey != "" && PxPayKey >= 1)
                   	{
            	if(accountID!="" && txtMcc != "" && txtType != "")
            		{
            		
            		$.ajax({
            			type : "POST",
            			url : "${home}saveDPSPayConnection",
            			data: "PxPayUserId="+PxPayUserId+"&PxPayKey="+PxPayKey+"&PxPayUrl "+PxPayUrl+
            			"&email ="+email + "&DPScurrency ="+currency +"&DPSsuccess_url="+success_url + 
               			"&DPSsuccess_url   ="+failure_url +
               			"&DPStransaction_type  ="+transaction_type,
							
            			  success: function(response){
            			        // we have the response
            			        
								location.reload(true)            	
								alert("Successfully Registered");
            			        $('#txtvalue').val('');
            			       
            			        },
            			        error: function(e){
            			        alert('Error: ' + e);
            			        }
            			        });
            		}
            		}
                   	}
               		else
               			{
               			alert("Invalid Order PxPayKey!");
               			}
               	}
               
            });
            /*
            $('input:radio').change(function () {
            	            	var rdID =$(this).attr('id'); //$(this).closest('tr').children().find('input:checked').attr('id');//.parent().parent().find('input:radio').attr('id');
			var splitedID = rdID.split("_");
			var splitStr = splitedID[0];
			var splitVal = splitedID[1];
			if(splitStr == "rd"){
				//amount_1
				var per = "amount_1";
				var splitPer = per.split("_");
				var splitPerStr = splitPer[0]+"_"+splitVal;
				
				$('#'+splitPerStr).attr('readonly', 'true');
				 $("#percentage_"+splitVal).removeAttr('readonly');
				 $("#amount_"+splitVal).val('0.0');
				 $("#percentage_"+splitVal).val('');
				$('#rdFees_'+splitVal).prop('checked', false);
				
			}
			else if(splitStr == "rdFees"){
				//percentage_1
				var per = "percentage_1";
				var splitPer = per.split("_");
				var splitPerStr = splitPer[0]+"_"+splitVal;
				$('#'+splitPerStr).attr('readonly', 'true');
				 $("#amount_"+splitVal).removeAttr('readonly');
				$('#rd_'+splitVal).prop('checked', false);
				 $("#percentage_"+splitVal).val('0.0');
				 $("#amount_"+splitVal).val('');
				
			}
        
            });
            
			 $(".save_amt").click(function(){
            
            	var rdID =$(this).attr('id');
            	var splitedID = rdID.split("_");
            	var splitVal = splitedID[1];
            	var amount = $("#amount_"+splitVal).val();
            	var percentage = $("#percentage_"+splitVal).val();
            	var method_type = splitVal;
            
            	if(amount > 0 || percentage > 0)
          		{
          		$.ajax({
          			type : "POST",
          			url : "${home}SaveFeedetails",
          			data: "amount="+amount+"&percentage="+percentage+"&method_type="+method_type,
							
          			  success: function(response){
          			        // we have the response
          			        
								//location.reload(true)            	
								alert("Fees Saved Successfully !");
								location.reload(true)   
          			        $('#txtvalue').val('');
          			       
          			        },
          			        error: function(e){
          			        //alert('Error: ' + e);
          			        }
          			        });
          		}
              	        
            	
            	return false;
            });
         */  
            
});
</script>