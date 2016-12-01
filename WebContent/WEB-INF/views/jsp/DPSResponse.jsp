<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.dynamicpayment.paymentexpress.*"%>
<jsp:useBean id="responseBean" class="com.dynamicpayment.paymentexpress.Response" />
<jsp:setProperty name="responseBean" property="*" />
<!DOCTYPE html>
<html lang="en">


Response got from Payment Express:

<div class="container">
<div>
<table>
	<!--<tr>
		<td colspan="2" align="center"><b>Response</b></td>
	</tr>-->
	<tr>
		<td>Amount</td>
		<td>$${Amount}</td>
	</tr>
	<tr>
		<td>Auth Code</td>
		<td>${auth_code}</td>
	</tr>
	
	<tr>
		<td>Cardholder Name</td>
		<td>${cardHolderName}</td>
	</tr>
	<tr>
		<td>Card Name</td>
		<td>${card_name}</td>
	</tr>
	<tr>
		<td>Card Number</td>
		<td>${card_number}</td>
	</tr>
	<tr>
		<td>Cardnumber2</td>
		<td>${Cardnumber2}</td>
	</tr>
	<tr>
		<td>Client Info</td>
		<td>${client_info}</td>
	</tr>
	<tr>
		<td>Currency Input</td>
		<td>${currency}</td>
	</tr>
	<tr>
		<td>Amount Settlement</td>
		<td>$${amountset}</td>
	</tr>
	<tr>
		<td>Expiry Date</td>
		<td>${expiry_date}</td>
	</tr>
	<tr>
		<td>DPS Billing ID</td>
		<td>${billingID}</td>
	</tr>
	<tr>
		<td>DPS Txn Ref</td>
		<td>${taxRef}</td>
	</tr>
	<tr>
		<td>Email Address</td>
		<td>${email}</td>
	</tr>
	<tr>
		<td>Merchant Reference</td>
		<td>${merRef}</td>
	</tr>
	<tr>
		<td>Response Text</td>
		<td>${resp_text}</td>
	</tr>
	<tr>
		<td>Success</td>
		<td>${success}</td>
	</tr>
	<tr>
		<td>TxnData1</td>
		<td>${txn1}</td>
	</tr>
	<tr>
		<td>TxnData2</td>
		<td>${txn2}</td>
	</tr>
	<tr>
		<td>TxnData3</td>
		<td>${txn3}</td>
	</tr>
	<tr>
		<td>TxnId</td>
		<td>${txnID}</td>
	</tr>
	<tr>
		<td>TxnMac</td>
		<td>${txnMac}</td>
	</tr>
	<tr>
		<td>Transaction Type</td>
		<td>${trans_type}</td>
	</tr>
	<tr>
		<td>Unique Reference</td>
		<td>${merchant_unique_reference}</td>
	</tr>

</table>
<form id="dpsForm" name="dpsForm" method="POST" action="${return_url}">
  	
  	<input type="hidden" name="type" value="${trans_type}" />
  	<input type="hidden" name="transanction_id" value="${taxRef}" />
  	<input type="hidden" name="total_fee" value="${Amount}" />
  <input type="hidden" name="txn_status" value="${transaction_status}" />
  	<input type="hidden" name="merchant_reference" value="${merRef}" />
  	<input type="hidden" name="merchant_unique_reference" value="${merchant_unique_reference}" />
	</form>
	
	<a href="javascript:void();" onclick="document.getElementById('dpsForm').submit();" class="btn btn-default">Return to website</a>
	<!-- <a href="javascript:void(0);" onClick="parent.closeIFrame();" > Close Iframe </a>	-->
</div>
</div>
</body>
</html>