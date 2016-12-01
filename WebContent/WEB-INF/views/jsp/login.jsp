<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page import="com.payitnz.config.DynamicPaymentConstant"%>

<!DOCTYPE>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<link href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/style.css" rel="stylesheet">
<link rel="stylesheet"
	href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/validationEngine.jquery.css">
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/jquery.min.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/jquery.validationEngine.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/jquery.validationEngine-en.js"></script>

<!-- jQuery library -->

<!-- Latest compiled JavaScript -->
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/bootstrap.min.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/admin.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/js.cookie.js"></script>
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
label{
	cursor:pointer;
}
</style>
<title>Login</title>
</head>

<body class="login_box_body">
	<div class="container">
		<div class="row">
			<div class=" col-md-offset-3 col-md-6">
				<form:form action="${loginAction}" commandName='user' id="loginForm">

					<div class="login_box">
						<div class="row">
							<div class="col-md-12">
								<div class="header_part">Login</div>
							</div>
						</div>
						<div class="text-center logo_img"><a href="webLogin"><img src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/images/logo.png" class="text-center"></a></div>
						<h6 class="${css} text-center">${message}</h6>
						<div class="form-group">
							<form:errors cssClass="error" />
							<lable>Username  <b>OR</b>  Email<span class="star_required">*</span></lable>
							<form:input type="text" class="form-control validate[required]"
								path="username" />
						</div>

						<div class="form-group">
							<lable>Password<span class="star_required">*</span></lable>
							<form:input type="password"
								class="form-control validate[required]" path="infidigiPassword" />
						</div>
						<div class="form-group text-center">
							<button class="btn btn-lg btn-primary" id="btn_login">Login</button>
						</div>
						<div class="row">
							<div class="col-md-12 text-center">
								<a href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/forgotPassword"><label>Forgot Password?</label></a>
							</div>
						</div>
						<br class="clear">
						<div class="row footer">						
							<div class="col-md-12 text-center">Copyright @infiDigi Payments 2016. All rights reserved</div>
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
	$(document).ready(function() {
		$("#loginForm").validationEngine('attach', {
			scroll : false
		});
		$("#btn_login").click(function() {
			$("#loginForm").validationEngine('attach', {
				scroll : false
			});
		});

	});
</script>