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
	
	<%
	    String txn_id = request.getParameter("txn_id");
		String Status = request.getParameter("txn_status");
		String status_flag = Status;
		String card_type= request.getParameter("card_type");
		String reference = request.getParameter("reference");
		   
		if(txn_id != null)
		{
			if(Status.equals("2"))
			{
				Status =" Transaction Successful";
			}
			if(Status.equals("1"))
			{
				Status =" Transaction Processing";
			}
			if(Status.equals("3"))
			{
				Status =" Transaction Failed";
			}
			if(Status.equals("4"))
			{
				Status =" Transaction Blocked";
			}
			if(Status.equals("11"))
			{
				Status =" Transaction Declined";
			}
		    
			
		   
		   
		%>
		<div class="col-sm-offset-2 col-sm-12">
			<H3>Response got from Payment Gateway:</H3><br><br>
			Transaction ID:<%=txn_id%><br><br>
			Status:<%=Status%><br><br>
			receipt_no:<%=request.getParameter("txn_status")%><br><br>
			account_id:<%=request.getParameter("account_id")%><br><br>
			reference:<%=request.getParameter("reference")%><br><br>
			card_type:<%=request.getParameter("card_type")%><br><br>
			response_text:<%=request.getParameter("response_text")%><br><br>
			authorisation_code:<%=request.getParameter("authorisation_code")%><br><br>
			custom_data:<%=request.getParameter("custom_data")%><br><br>
			date:<%=request.getParameter("date")%><br><br>
			Unique Reference:${merchant_unique_reference}
			
			
		</div>
		<%
		}
		else
		{
			request.setAttribute("txn_status", 11);
	%>
	<div class="col-sm-offset-2 col-sm-12">
		<H3>Response got from Payment Gateway:</H3><br><br>
		
		Status: Transaction Cancelled <br><br>
				
	</div>
	<%} %>
	
	<form name="f2cForm" id="f2cForm" method="POST" action="${return_url}">
  	
  	<input type="hidden" name="transaction_id" value="<%=txn_id%>" />
  	<input type="hidden" name="card_type" value="<%=card_type%>" />
  	<input type="hidden" name="reference" value="<%=reference%>" />
  	<input type="hidden" name="status" value="<%=status_flag%>" />
  	<input type="hidden" name="status_message" value="<%=Status%>" />
  	<input type="hidden" name="merchant_reference" value="${merchant_reference}" />
  	<input type="hidden" name="merchant_unique_reference" value="${merchant_unique_reference}" />
	</form>
	
	<a href="javascript:void();" onclick="document.getElementById('f2cForm').submit();" class="btn btn-default">Return to website</a>

</div>
</body>
</html>