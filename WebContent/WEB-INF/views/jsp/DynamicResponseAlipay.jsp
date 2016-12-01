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
		sign:${sign}<br><br>
		Transaction No:${trade_no}<br><br>
		Amount:$${ total_fee}<br><br>
		sign_type:${ sign_type}<br><br>
		Date:${ out_trade_no}<br><br>
		Status:${ trade_status}<br><br>
		currency:${ currency}<br><br>
	Unique Reference:${merchant_unique_reference}
	
	</div>
	
	<form id="aliPayForm" name="aliPayForm" method="POST" action="${return_url}">
  	
  	<input type="hidden" name="sign" value="${sign}" />
  	<input type="hidden" name="transaction_id" value="${trade_no}" />
  	<input type="hidden" name="total_fee" value="${total_fee}" />
  	<input type="hidden" name="txn_status" value="${transaction_status}" />
  	<input type="hidden" name="merchant_reference" value="${merchant_reference}" />
  	<input type="hidden" name="merchant_unique_reference" value="${merchant_unique_reference}" />
	</form>
	
	<a href="javascript:void();" onclick="document.getElementById('aliPayForm').submit();" class="btn btn-default">Return to website</a>
	<!-- <a href="javascript:void(0);" onClick="parent.closeIFrame();" > Close Iframe </a> -->
</div>
</body>
</html>