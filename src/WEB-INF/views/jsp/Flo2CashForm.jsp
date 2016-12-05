<%@ page import="com.payitnz.config.DynamicPaymentConstant" %>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">

<%--<jsp:include page="../fragments/header.jsp" />--%>
<%
	String web2PayURL = "https://demo.flo2cash.co.nz/web2pay/default.aspx";
	String merchant_verifier = "NjIxNTAzMTAuMDBJVjIwMDYxMDMxMDAxSXRlbSBObyAxMjNodHRwczovL3d3dy5haHVyYWNsb3VkLmNvbS9SZXN1bHQuYXNweHN0YW5kYXJkZTc5Y2JhNzktZTkwYi00MGQ3LTliYzgtYTUxNTA4OTI1NjVi";

	  HttpSession session = request.getSession();
	  //  String username = (String)request.getAttribute("un");
	    session.setAttribute("testPay", "0");
	    
	    System.out.println("TestPay:"+session.getAttribute("testPay").toString());
	    
%>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Payment Method</title>
<style>
.error {
	color: red;
	float: left;
	font-size: 13px;
	padding-top: 10px;
}

.success {
	color: green;
	float: left;
	font-size: 13px;
	padding-top: 10px;
}
.star_required {margin-left:2px;color:red;}
</style>
</head>

<body>
	<spring:url value="/listGateways" var="paymentActionUrl" />
	<div class="login">
		<form:form class="login-container" method="post"
			commandName="ShoppingCart" action="${paymentActionUrl}">
			<form:errors cssClass="error" />
			<br>
			<br>
			<label>Amount:<span class="star_required">*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
			<input type="text" name="amount" id="amount" />
			<br>
			<br>
			<label>Item Name:<span class="star_required">*</span></label>
			<input type="text" name="particular" id="particular"/>
			<br><br>
			<label>Account Id:<span class="star_required">*</span></label>
			<input type="text" name="infidigiAccountId" id="infidigiAccountId" />
			<br>
			<br>
			<label>Unique Reference:</label>
			<input type="text" name="reference" id="reference"/>
			<input type="hidden" name="gateway" id="gateway"/>
			<br>
			<br>
		
			<br>
			<label class="col-sm-2 control-label">&nbsp;</label>
			<input type="button" value="Pay Now" id="pay">
			<label class="col-sm-2 control-label">&nbsp;</label>
			<input type="reset" value="Cancel">
		</form:form>

	</div>
	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
	<script type="text/javascript">
		$(document).ready(function() {
			
			
			$('#pay').prop('type', 'button');
			$('#dir').prop('checked', true);
			$("#selectedLogin").val("0");
			$('input:radio').change(function() {
					var rdbuttonvalue = $(this).val();
				$("#selectedLogin").val(rdbuttonvalue);
	
				if (rdbuttonvalue == "Don't have Direct Communication With DPS") {
					$("#url").show();
					$("#url").val("");
					$("#selectedLogin").val(rdbuttonvalue);
				} else {
					$("#url").hide();
					$("#selectedLogin").val("0");
				}
		});
							
		$("#pay").click(function() {
		//	alert("acc"+$("#infidigiAccountId").val());
			var amt = $("#amount").val();
			var particular = $("#particular").val()
			var accountId = $("#infidigiAccountId").val();
			var gateway = $('#merList :selected').text();;
			var reference = $("#reference").val();
		//	alert("accountid"+accountId);
			
			if (amt == "") {
				alert("Amount should not be blank.");
				return false;
			}else if(isNaN(amt)){
				alert("Amount should be numeric.");
				return false;
			} 
			
			if (particular == "") {
				alert("Item should not be Blank.");
				return false;
			}
			
			if (accountId == "") {
				alert("Accont Id should not be Blank.");
				return false;
			}
			
			if(gateway == 'Alipay Online')
			{
				//alert("Alipay");
				$("#gateway").val('Alipay Online');
				
			}
			if(gateway == 'DPS')
			{
				alert("DPS");
				$("#gateway").val('DPS');
			}
			if(gateway == 'Flo2Cash')
			{
				//alert("Flo2Cash");
				$("#gateway").val('Flo2Cash');
			}
			if(gateway == 'POLi')
			{
				alert("POLi");
				$("#gateway").val('POLi');
			}
			if(gateway == 'CUP')
			{
				alert("cup");
				$("#gateway").val('CUP');
			}
		     
			$('#pay').prop('type', 'submit');
			
		});
		
		$("#infidigiAccountId1").change(function() {
			var acc_id = $("#infidigiAccountId").val();
		    
	         $.ajax({
	                    type : "POST",
	                    'async': false,
	                    url :"<%= "/"+DynamicPaymentConstant.SITE_URL+"/listGateways" %>",
	                    timeout : 100000,
	                    dataType: 'json',
	                    data: {'infidigiAccountId':acc_id},
	                     success : function(data) { 
                          // alert("Done");
	                             buildTable(data);       
	                        },
	                      error : function(data) {
	                    	     alert("not");
	                        },
	                       
	                      });
			});
		
		
		
                           function buildTable(data){
                     		  var tableData = "";
                     		  debugger;
                               if(Object.keys(data).length > 0)
                               {
                            	   //alert(Object.keys(data).length);
                               	for(i=1;i<=Object.keys(data).length;i++){
                             		
                             		 $('#merList').append($('<option>',
                             			     {
                             			        value: i,
                             			        text : data[i]['channel'] 
                             			    }));
                     	           
                             	  }
                              }
                               else
                              {
                             	tableData +='<tbody><tr><td colspan="7">No history available</td></tr></tbody>';
                              }
                     	                            
                     	  } 

	});
		
	</script>
	<%--<jsp:include page="../fragments/footer.jsp" />--%>

</body>
<%@ include file="fragments/FormOperations.jsp"%>
</html>