<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ include file="fragments/backendHeader.jsp"%>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<link href="resources/core/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/core/css/style.css" rel="stylesheet">
<link rel="stylesheet"
	href="resources/core/css/validationEngine.jquery.css">
<script src="resources/core/js/jquery.min.js"></script>
<script src="resources/core/js/jquery.validationEngine.js"></script>
<script src="resources/core/js/jquery.validationEngine-en.js"></script>

<!-- jQuery library -->

<!-- Latest compiled JavaScript -->
<script src="resources/core/js/bootstrap.min.js"></script>
<script src="resources/core/js/admin.js"></script>
<script src="resources/core/js/js.cookie.js"></script>

<script type="text/javascript">

</script>
<style>
.error {
	color: red;
	float: left;
	font-size: 13px;
	padding-top: 10px;
}
.login_box {
    margin-top: 6%;  
}

</style>
<title>Login</title>
</head>
<body class="login_box_body">
	<div class="container">
		<div class="row">
			<div class=" col-md-offset-1 col-md-10">
					<div class="login_box">
					<c:choose>
					<c:when test="${transData.size()  > 0}">
						<div class="row">
							<div class="col-lg-12 col-md-12">
						
								<h4 class="heading text-center">Transaction Details</h4>
								<div class="table-responsive">
								<table class="table table-bordered textarea_marginbottom">
								<tbody>
								<c:forEach items="${transData}" var="each">
								<tr>
								<td><b>${each.key}</b></td>
								<td>${each.value}</td>
								</tr>
								</c:forEach>
								</tbody>
								</table>
								</div>
							
							</div>
						</div>
					<c:if test="${not empty otherTransData}">
   
							<div class="row">
							<div class="col-lg-12 col-md-12" id="other">
						
								<h4 class="heading text-center">Original Transaction Details</h4>
								<div class="table-responsive">
								<table class="table table-bordered textarea_marginbottom">
								<tbody>
								<c:forEach items="${otherTransData}" var="each">
								<tr>
								<td><b>${each.key}</b></td>
								<td>${each.value}</td>
								</tr>
								</c:forEach>
								</tbody>
								</table>
								</div>
							
							</div>
						</div>
					
					</c:if>
						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<a href="downloadTransactionDetailsCSV?id=<%= request.getParameter("id") %>"><div class="btn btn-primary save_btn">Download CSV</div></a>
								<a href="downloadTransactionDetailsReportPDF?id=<%= request.getParameter("id") %>"><div class="btn btn-primary save_btn">Download PDF</div></a>
							</div>
						</div>
						</c:when>
							<c:otherwise>
							<div class="row">
							<div class="col-lg-12 col-md-12">
							<h4 class="heading text-center">No transaction details found</h4>
							</div>
							</div>
							</c:otherwise>
							</c:choose>
					</div>
					<br class="clear">
					<br class="clear">
			</div>
		</div>
		<!--row end-->
	</div>
	<!--container end-->
</body>

</html>