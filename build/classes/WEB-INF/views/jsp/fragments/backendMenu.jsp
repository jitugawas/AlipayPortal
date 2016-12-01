<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.payitnz.config.DynamicPaymentConstant" %>

<style>
.welcome {
	margin-top: 15px;
	font-size: 14px;
	color: #fff;
}
.well {
    min-height: 20px;
    padding-top: 10px;
    padding-bottom:0px;
    padding-left: 10px;
    padding-right: 10px;
    margin-top:5px;
    margin-bottom: 0px;
    background-color: #f5f5f5;
    border: 1px solid #e3e3e3;
    line-height: initial;
    font-size: 11px;
}
}
</style>

<!-- Navigation -->
<nav class="navbar navbar-default navbar-static-top" role="navigation"
	style="margin-bottom: 0">
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-collapse">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a href="home" class="navbar-brand logo_img text-center"><img
			src="resources/core/images/logo.png" class="img-responsive"></a>
	</div>
	<!-- /.navbar-header -->
	
	 <div class="navbar-right right_side_name">
 	 <div class="well" style="display:inline-block">
  	 <p>You are currently logged in as <b>"${sessionScope.username}"</b></p>
  	 <p>Logged in at ${sessionScope.current_logged_in_at} </p>
  	 <p>Last Logged in at ${sessionScope.last_logged_in_at} </p>
	 </div>
 	 <ul style="display:inline-block" class="nav navbar-top-links navbar-right right_side_name">
		<li class="dropdown">
			<a class="dropdown-toggle"
			data-toggle="dropdown" href="#"> <i class="fa fa-user fa-fw"></i>
					<i class="fa fa-caret-down"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#myModal" id="profileModal" data-toggle="modal" ><i class="fa fa-user fa-fw"></i>User
						Profile</a></li>
				<!-- li class="divider"></li-->
				<li><a href="webLogout"><i class="fa fa-sign-out fa-fw"></i>
						Logout</a></li>
			</ul> <!-- /.dropdown-user --></li>
	</ul>
	</div>
	
	<div class="navbar-default sidebar" role="navigation">
		<div class="sidebar-nav navbar-collapse">
			<ul class="nav" id="side-menu">

				<li id="home"><a href="home"><span
						class="glyphicon glyphicon-dashboard" aria-hidden="true"></span>Dashboard</a></li>
				<c:if 
				test="${sessionScope.all_permission == 1 || sessionScope.permission_alipay_transactions == 1}">
				<li id="alipayTransactions"><a href="alipayTransactions"><span
						class="glyphicon glyphicon-usd" aria-hidden="true"></span>Alipay
						Transactions</a></li>
				</c:if>
				<c:if
					test="${sessionScope.all_permission == 1 || sessionScope.permission_refund == 1}">
					<li id="alipayRefund"><a href="refundListing"><span
							class="glyphicon glyphicon-check" aria-hidden="true"></span>Alipay Refund</a></li>
				</c:if>
				<c:if
					test="${sessionScope.all_permission == 1 || sessionScope.permission_setup_connections == 1}">
					<li id="setupConnection"><a href="setupConnection"><span
							class="glyphicon glyphicon-check" aria-hidden="true"></span>Setup
							Payment Connections</a></li>
				</c:if>
				<c:if test="${(sessionScope.all_permission == 1 || sessionScope.permission_setup_merchant == 1) && sessionScope.role_id == 1}">
					<li id="setupMerchant"><a href="setupMerchant"><span
							class="glyphicon glyphicon-user" aria-hidden="true"></span>Setup
							Merchant</a></li>
				</c:if>
				<c:if test="${(sessionScope.all_permission == 1 || sessionScope.permission_setup_users == 1) && sessionScope.role_id != 3}">
					<li id="setupUser"><a href="setupUser"><span
							class="glyphicon glyphicon-user" aria-hidden="true"></span>Setup
							Users</a></li>
				</c:if>
				<c:if test="${(sessionScope.all_permission == 1 || sessionScope.permission_reconciliation == 1) && sessionScope.role_id == 1}">
					<li id="reconcillation"><a href="reconcil"><span
							class="glyphicon glyphicon-retweet" aria-hidden="true"></span>Reconciliation</a></li>
				</c:if>
				<c:if test="${(sessionScope.all_permission == 1 || sessionScope.permission_settlement == 1) && sessionScope.role_id == 1}">
					<li><a href="settlement"><span
							class="glyphicon glyphicon-check" aria-hidden="true"></span>Settlement</a></li>
				</c:if>
				<c:if test="${sessionScope.role_id == 1}">
					<li id="setupAlerts"><a href="setupAlerts"><span
							class="glyphicon glyphicon-check" aria-hidden="true"></span>Setup Alerts</a></li>
				</c:if>
				<li></li>

			</ul>
		</div>
		<!-- /.sidebar-collapse -->
	</div>
	<!-- /.navbar-static-side -->
</nav>
<script>
	$(document).ready(function(){
		var urlLink = '<%=request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1,
					request.getRequestURI().length() - 4)%>';
						if (urlLink) {

							if (urlLink == "ViewMerchant"
									|| urlLink == "registerMerchant"
									|| urlLink == "editMerchant") {
								$("#setupMerchant").addClass("menu_active");
							} else if (urlLink == "ViewUser"
									|| urlLink == "registerUser"
									|| urlLink == "editUser") {
								$("#setupUser").addClass("menu_active");
							} else {
								$("#" + urlLink).addClass("menu_active");
							}
						}

						$("#side-menu li").click(function() {
							$("#side-menu li").removeClass('menu_active');
							$(this).addClass("menu_active");
						});

					});
</script>
<!-- modals  -->
<!-- Modal -->
<div id="myModal" class="modal fade" role="dialog">	
</div>

<script>
$(document).ready(function(){
	$("#changePassword").click(function(){
		$("#changePasswordContent").toggle();
	})
	
	$("#profileModal").click(function(){
		$.ajax({
			type : "POST",
			url :"<%= "/"+DynamicPaymentConstant.SITE_URL+"/viewProfile" %>",
			timeout : 100000,
			success : function(data) {
				$("#myModal").html(data);
				$('#myModal').modal('show');
			},
			error : function(e) {
			},
			done : function(e) {
			}
		});
	});
	
	
	
});
</script>



