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
<style>
.error {
	color: red;
	float: left;
	font-size: 13px;
	padding-top: 10px;
}
.readonly_background{
background-color:#eee;
}
</style>
<title>Login</title>
</head>
<body class="login_box_body">
	<div class="container">
		<div class="row">
			<div class=" col-md-offset-2 col-md-8">
				<form:form action="SaveVerificationInfo" commandName='userForm'
					method='POST' id="verification_form">
					<div class="login_box">
					<c:choose>
					<c:when test="${expired == 1 }">
					<h4 >Your link is already expired.</h4>
					</c:when>
					<c:otherwise>
					<c:choose>
					<c:when test="${verified == 0 }">
					<div class="row">
							<div class="col-lg-8 col-md-8">
								<c:if test="${role_id == 3}">
								<h4 class="heading">User Account Verification</h4>
								</c:if>
								<c:if test="${role_id == 2}">
								<h4 class="heading">Merchant Account Verification</h4>
								</c:if>
								<div class="row">
									<div class="col-md-4">
										<lable>Company Name</lable>
									</div>
									<div class="col-md-8">
										<form:input type="text" class="inp readonly_background" path="company_name"
											readonly="true" value="${companyName}" />
									</div>
								</div>
								<div class="row">
									<div class="col-md-4">
										<c:if test="${role_id == 3}">
										<lable>User ID</lable>
										</c:if>
										<c:if test="${role_id == 2}">
										<lable>Merchant ID</lable>
										</c:if>
									</div>
									<div class="col-md-8">
										<form:input type="text" class="inp readonly_background" path="user_id"
											readonly="true" value="${email}"/>
									</div>
								</div>
								<form:input type="hidden" path="id" value="${id}" />

								<div class="row">
									<div class="col-md-4">
										<lable>Security Question-1</lable>
									</div>
									<div class="col-md-8">
										<form:select class="select validate[required]"
											path="question2">
											<c:forEach items="${questions}" var="question">
												<form:option value="${question}">
     				 							  ${question}
 												 </form:option>
											</c:forEach>

										</form:select>
									</div>
								</div>

								<div class="row">
									<div class="col-md-4">
										<lable>Answer<span class="star_required">*</span></lable>
									</div>
									<div class="col-md-8">
										<form:input type="text"
											class="inp validate[required,custom[onlyLetterSp]]"
											placeholder="Answer" path="Answer" />
									</div>
								</div>

								<div class="row">
									<div class="col-md-4">
										<lable>Security Question-2</lable>
									</div>
									<div class="col-md-8">
										<form:select name="que" class="select validate[required]"
											path="question1">
											<c:forEach items="${questions}" var="question">
												<form:option value="${question}"> ${question}</form:option>

											</c:forEach>
										</form:select>
									</div>
								</div>

								<div class="row">
									<div class="col-md-4">
										<lable>Answer<span class="star_required">*</span></lable>
									</div>
									<div class="col-md-8">
										<form:input type="text"
											class="inp validate[required,custom[onlyLetterSp]]"
											placeholder="Answer" path="Answer1" />
									</div>
								</div>

								<div class="row buttons">
									<div class="col-md-12">
										<form:button type="submit" class="btn btn-primary save_btn"
											id='save'>Save</form:button>
									</div>
								</div>

							</div>

						</div>
					</c:when>
					<c:otherwise>
					<h4 >Your account is already verified.</h4>
					</c:otherwise>
					</c:choose>
					</c:otherwise>
					</c:choose>
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
            $("#verification_form").validationEngine('attach', { scroll: false });
            $("#save").click(function () {
                $("#verification_form").validationEngine('attach', { scroll: false });
            });

        });
</script>
