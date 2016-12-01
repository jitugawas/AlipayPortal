<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ include file="fragments/backendHeader.jsp"%>
<%@page import="com.payitnz.config.DynamicPaymentConstant"%>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<link href="resources/core/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/core/css/style.css" rel="stylesheet">
<link rel="stylesheet" href="resources/core/css/validationEngine.jquery.css">
<script src="resources/core/js/jquery.min.js"></script>
<script src="resources/core/js/jquery.validationEngine.js"></script>
<script src="resources/core/js/jquery.validationEngine-en.js"></script>

<!-- jQuery library -->

<!-- Latest compiled JavaScript -->
<script src="resources/core/js/bootstrap.min.js"></script>
<script src="resources/core/js/admin.js"></script>
<script src="resources/core/js/js.cookie.js"></script>
<style>
.error {
	color: red;
	float: left;
	font-size: 13px;
	padding-top: 10px;
}
.success {
	color:green;
	float: left;
	font-size: 13px;
	padding-top: 10px;
}
</style>
<title>forgot password</title>
</head>
<body class="login_box_body">
	<div class="container">
		<div class="row">
			<div class=" col-md-offset-3 col-md-6">
				<form:form action="verifyEmail" commandName='userForm' id="emailForm" method="GET">

					<div class="login_box">
					<div class="row">
						<div class="col-md-12">
						       <div class="header_part">Forgot Password</div>
						</div>
						</div>	
						<div class="text-center logo_img"><a href="webLogin"><img src="resources/core/images/logo.png" class="text-center"></a></div>			
						<h6 class="${css}">${message}</h6>
						<div class="form-group">
							<form:errors cssClass="error" />
							<lable>Please enter your email id</lable>
							<form:input type="text" class="form-control validate[required,custom[email]]" path="email" value="${email}"/>
						</div>
						<div class="form-group">
							<button class="btn btn-lg btn-primary save_btn" id="btn_submit">Submit</button>
							<input type="button" class="btn btn-lg btn-primary save_btn" id="btn_submit"  onClick="document.location.href='<%= DynamicPaymentConstant.SITE_BASE_URL %>/webLogin'" value="Back"/> 
						</div>
					</div>
					<br class="clear">
					<br class="clear">
				</form:form>
			</div>
		</div>
		<!--row end-->
	</div>
	<!--container end-->
</body>
</html>

<script type="text/javascript">
  $(document).ready(function () {
            $("#emailForm").validationEngine('attach', { scroll: false });
            $("#btn_submit").click(function () {
                $("#emailForm").validationEngine('attach', { scroll: false });
            });

        });
</script>