<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">

<div class="container">
	
	<div class="col-sm-offset-2 col-sm-12">
		<H3>Response got from Payment Gateway:</H3><br><br>
	<%
	Object st = request.getAttribute("TransactionStatusCode");
	String tran_status = st.toString();
	if(st.equals("Cancelled"))
			{
				%>
				Status: Transaction Cancelled due to wrong method data.<br><br>
				<%
			}
	else
	{
	%>
		PayerAcctNumber:${PayerAcctNumber}<br><br>
		MerchantAcctNumber:${MerchantAcctNumber}<br><br>
		MerchantAcctName:${ MerchantAcctName}<br><br>
		TransactionRefNo:${ TransactionRefNo}<br><br>
		CurrencyCode:${ CurrencyCode}<br><br>
		PaymentAmount:${ PaymentAmount}<br><br>
		AmountPaid:${ AmountPaid}<br><br>
		BankReceipt:${ BankReceipt}<br><br>
		BankReceiptDateTime:${BankReceiptDateTime }<br><br>
		TransactionStatusCode:${TransactionStatusCode }<br><br>
		ErrorCode:${ErrorCode }<br><br>
		ErrorMessage:${ ErrorMessage}<br><br>
		FinancialInstitutionCode:${ FinancialInstitutionCode}<br><br>
		FinancialInstitutionName:${ FinancialInstitutionName}<br><br>
		MerchantReference:${ MerchantReference}<br><br>
		PayerFirstName:${ PayerFirstName}<br><br>
		PayerFamilyName:${PayerFamilyName}<br><br>
		Unique Reference:${merchant_unique_reference}
	<%} %>
	</div>
	
	<form id="poliForm" name="poliForm" method="POST" action="${return_url}">
  	
  	<input type="hidden" name="sign" value="${sign}" />
  	<input type="hidden" name="transanction_id" value="${TransactionRefNo}" />
  	<input type="hidden" name="total_fee" value="${AmountPaid}" />
  	<input type="hidden" name="status" value="${tran_status}" />
  	<input type="hidden" name="merchant_reference" value="${merchant_reference}" />
  	<input type="hidden" name="merchant_unique_reference" value="${merchant_unique_reference}" />
	</form>
	
	<a href="javascript:void();" onclick="document.getElementById('poliForm').submit();" class="btn btn-default">Return to website</a>
		
</div>
</body>
</html>