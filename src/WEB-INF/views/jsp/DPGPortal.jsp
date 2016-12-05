<%@page import="com.payitnz.config.DynamicPaymentConstant"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<link href="resources/core/css/style.css" rel="stylesheet">
<spring:url value="/payments/callapiUnionPay"
	var="paymentActionUnionPayUrl" />

<script type="text/javascript">

function addProcessingFees(obj, amount) {
	debugger;
	 var methodType = obj.value.replace(/\s/g, "");
	 var method = methodType.toLowerCase();
	  alert("In addprocessingfees");
	 if(method == 'Flo2Cash' || method == 'flo2cash') {
	  alert("flo2cash");
	  var total = document.getElementById('F2CExtraAmount').value;
	  //var total = tot.toFixed(2);
	  var n= total - amount;
	  var ExtraCharge = n.toFixed(2);
	  document.getElementById('totalAmount').innerHTML = '<div class="message" id="totalAmount">'+
	   ' <p>The selected payment method will be charged a convenience fee of $' + ExtraCharge +'</p>'+
	   ' <p>The total Payment amount is $' + total +'</p></div>';
	  document.getElementById('amount').value = total;
	  document.getElementById('confee').value = ExtraCharge;
	  
	 } 
	 
	 if(method == 'alipay' || method == "Alipay") {
	  
	  var total = document.getElementById('AlipayExtraAmount').value;
	  //var ExtraCharge = total - amount;
	  var n= total - amount;
	  var ExtraCharge = n.toFixed(2);
	  document.getElementById('totalAmount').innerHTML = '<div class="message" id="totalAmount">'+
	   ' <p>The selected payment method will be charged a convenience fee of $' + ExtraCharge+'</p>'+
	   ' <p>The total Payment amount is $' + total +'</p></div>';
	  document.getElementById('amount').value = total;
	  document.getElementById('confee').value = ExtraCharge;
	 }
	 
	 if(method == 'Payment Express(DPS)' || method == 'paymentexpress(dps)') {
	  
	  var total = document.getElementById('DPSExtraAmount').value;
	  //var ExtraCharge = total - amount;
	  var n= total - amount;
	  var ExtraCharge = n.toFixed(2);
	  document.getElementById('totalAmount').innerHTML = '<div class="message" id="totalAmount">'+
	   ' <p>The selected payment method will be charged a convenience fee of $' + ExtraCharge +'</p>'+
	   ' <p>The total Payment amount is $' + total +'</p></div>';
	  document.getElementById('amount').value = total;
	  document.getElementById('confee').value = ExtraCharge;
	 }
	 
	 if(method == 'Poli' || method == 'poli') {
	  
	  var total = document.getElementById('PoliExtraAmount').value;
	  //var ExtraCharge = total - amount;
	  var n= total - amount;
	  var ExtraCharge = n.toFixed(2);
	  document.getElementById('totalAmount').innerHTML = '<div class="message" id="totalAmount">'+
	   ' <p>The selected payment method will be charged a convenience fee of $' + ExtraCharge+'</p>'+
	   ' <p>The total Payment amount is $' + total +'</p></div>';
	  document.getElementById('amount').value = total;
	  document.getElementById('confee').value = ExtraCharge;
	 }
	  
	 if(method == 'CUP' || method =='cup') {
	  
	  var total = document.getElementById('DynamicExtraAmount').value;
	  //var ExtraCharge = total - amount;
	  var n= total - amount;
	  var ExtraCharge = n.toFixed(2);
	  document.getElementById('totalAmount').innerHTML = '<div class="message" id="totalAmount">'+
	   ' <p>The selected payment method will be charged a convenience fee of $' + ExtraCharge+'</p>'+
	   ' <p>The total Payment amount is $' + total +'</p></div>';
	  document.getElementById('amount').value = total;
	  document.getElementById('confee').value = ExtraCharge;
	  
	 }

	}
	function checkPaymentMethod(){
		var payValue = document.getElementsByName('gateway');
		
		debugger;
		var methodChecked = false;
		
		for(var i=0;i< payValue.length;i++){
			if(payValue[i].checked){
				methodChecked =  true;
				
			}
		}
		
		if(!methodChecked){
			alert("Please select a payment method.");
		}
		
		return methodChecked;
	}
	
</script>


<title>Payment Method</title>
</head>
<body style="background: #e8e8e8;">



	<spring:url value="/callPayemntApi" var="paymentActionUrl" />
	<form:form class="form-horizontal" method="post"
		commandName="shoppingCart" action="${paymentActionUrl}"
		id="paymentForm" name="paymentForm" onsubmit="return checkPaymentMethod();">

		<form:hidden path="particular" id="particular"/>
		<form:hidden path="amount" id="amount" />
		<form:hidden path="infidigiAccountId" id="infidigiAccountId" />
		<form:hidden path="gateway" id="gateway" />
		<form:hidden path="paymentMethod" id="paymentMethod" />
	

		<div class="container">
			<div class="row">
				<div class="col-lg-offset-3 col-lg-6 col-md-offset-2 col-md-8">
					<div class="login_box secure_payment">
		
				
		
					
				   <!-- ----------top section ends --------------- -->
				    <!-- ----------middle section--------------- -->
						<h4>Please select a payment method</h4>
						
						<div class="col-sm-12">
							<div class="message"></div>
							<c:choose>
								<c:when test="${paymentMethods.size() > 0}">
									<!--<spring:bind path="paymentMethod">
										<form:radiobuttons id="payment" path="paymentMethod"
											items="${paymentMethods}" element="label class='radio'"
											onchange="addProcessingFees(this,${amount})" />
									</spring:bind>-->
									
									<c:forEach items ="${paymentMethods}" var="each">
									<label class="radio">
									<form:radiobutton path="gateway" id="gateway" value="${each}"/>
									
									<c:choose>
									
									<c:when test="${display_content ==1 }">
									<c:forEach items="${logoUrls}" var="logo">
									<c:if test="${logo.value[each] != null}">
									<img class="" src="${logo.value[each]}" />
									</c:if>
									</c:forEach>
									</c:when>
									<c:otherwise>
									<label>${each}</label>
									</c:otherwise>
									</c:choose><br>
									</label>
									
									</c:forEach>
									
									<input type="hidden" name="elseBlock" id="elseBlock" value="1" />
									<br class="clear">
								</c:when>

								<c:otherwise>
									<h4 style="color: #FF0000;" class="tryagain">Payment
										Method Not Found. Please try again.</h4>
									<input type="hidden" name="elseBlock" id="elseBlock" value="0" />
								</c:otherwise>
							</c:choose>
						</div>
						<br class="clear"> <br class="clear"> <br class="clear">
						<div class="message" id="totalAmount">
							
							<p>The total Payment amount is $${amount}</p>
						</div>
					
						<div class="">
				       <a href="" onclick = "goBack()"><button type="button"
				         class="btn btn-lg btn-primary btnreset" style="font-size: 15px;  margin-right: 15px; padding: 5px 15px;">Cancel</button></a> <a
				        href=""><button class="btn btn-lg btn-primary btnNext" style="font-size: 15px;  margin-right: 15px; padding: 5px 15px;">Next</button></a>
				       <a href="<%=DynamicPaymentConstant.SERVER_HOST + DynamicPaymentConstant.SERVER_SITE_URL%>"><button
				         type="button" class="btn btn-lg btn-primary btnback" style="font-size: 15px;  margin-right: 15px; padding: 5px 15px;">Back</button></a>
      					</div>
						<!-- ----------middle section ends --------------- -->
						
						<!-- ----------bottom  section--------------- -->
						<br class="clear">
		
						
						
						<c:if test="${is_display_text_bottom == 1 }">
						<div class="text-<c:if test="${justified_text_bottom == 1 }">left</c:if><c:if test="${justified_text_bottom == 2 }">center</c:if><c:if test="${justified_text_bottom == 3 }">right</c:if>">
						<c:choose>
						<c:when test="${hyperlink_bottom == '' || hyperlink_bottom == null}">
						<p style="<c:if test="${font_size_bottom != 0}">font-size:${font_size_bottom}px;</c:if><c:if test="${font_family_bottom != null ||  font_family_bottom != ''}">font-family:${font_family_bottom}</c:if>">${text_bottom}</p>
						</c:when>
						<c:otherwise>
						<a href="${hyperlink_bottom}" target="_blank"><p style="<c:if test="${font_size_bottom != 0}">font-size:${font_size_bottom}px;</c:if><c:if test="${font_family_bottom != null ||  font_family_bottom != ''}">font-family:${font_family_bottom}</c:if>">${text_bottom}</p></a>
						</c:otherwise>
						</c:choose>
						</div>
				        </c:if>
						<!-- ----------bottom  section ends --------------- -->
					</div>
					<br class="clear"> <br class="clear">
				</div>
			</div>
			<!--row end-->
		</div>
		<!--container end-->
	</form:form>
	<!-- jQuery library -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
	<%@ include file="fragments/FormOperations.jsp"%>
	<!-- Latest compiled JavaScript -->
	<script
		src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	<script>
  function goBack() {
     window.history.back();
  }
 </script>
</body>
</html>
