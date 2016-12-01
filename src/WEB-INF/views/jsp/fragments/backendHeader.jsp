<%@ page import="com.payitnz.config.DynamicPaymentConstant"%>
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title><%= request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1,request.getRequestURI().length()-4) %></title>

<link rel="stylesheet" href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/bootstrap.min.css">
<link rel="stylesheet" href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/style.css" >
<link rel="stylesheet" href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/bootstrap-responsive-tabs.css">
<link rel="stylesheet" href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/jquery-ui.css">
<link rel="stylesheet" href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/jquery.dataTables.min.css">
<link rel="stylesheet" href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/bootstrap-responsive-tabs.css">
<link rel="stylesheet" href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/validationEngine.jquery.css">
<link rel="stylesheet" href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/bootstrap-multiselect.css">
<link rel="stylesheet" href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/alertify.core.css">
<link rel="stylesheet" href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/alertify.bootstrap.css">
<link rel="stylesheet" href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/alertify.default.css">
<link rel="stylesheet" href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/css/tiptip.css">
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<!-- jQuery library -->
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/jquery.min.js"></script>
<!-- Latest compiled JavaScript -->
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/bootstrap.min.js"></script>
<!-- Custom Theme JavaScript -->
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/admin.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/raphael.min.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/elycharts.min.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/jquery-ui.min.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/jquery.dataTables.min.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/js.cookie.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/jquery.validationEngine.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/jquery.validationEngine-en.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/bootstrap-multiselect.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/alertify.min.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/jscolor.min.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/jscolor.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/moment.min.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/datetime-moment.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/jquery.blockUI.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/jquery.browser.min.js"></script>
<script src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/js/tiptip.js"></script>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.ArrayList"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
<style>
	.star_required {margin-left:2px;color:red;}
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

#loading {
    width: 100%;
    height: 100%;
    top: 0px;
    left: 0px;
    position: fixed;
    display: block;
    opacity: 0.7;
    background-color: #fff;
    z-index: 99999;
    text-align: center;
}

#message_loading {
    width: 100%;
    height: 100%;
    top: 0px;
    left: 0px;
    position: fixed;
    display: block;
    opacity: 0.7;
    background-color: #fff;
    z-index: 99999;
    text-align: center;
}
</style>
</head>
<body>
<div id="wrapper">
<div id="loading" class="loading1" style='display:none;'>
<img id="loading-image" src="<%=DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SITE_URL %>/resources/core/images/loader.gif" alt="Loading..." />
</div>
