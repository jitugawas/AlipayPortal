<%@ include file="fragments/backendHeader.jsp"%>
<%@ include file="fragments/backendMenu.jsp"%>
<%@ page session="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style>
.div_float {
float:left;
}
.check_box {
margin-top : 0px!important;
margin-right : 0px!important;
}
label {
    display: inline-block;
    max-width: 100%;
    margin-bottom: 5px;
    font-weight: lighter;
    padding-left: 5px;
}
</style>
<form:form action="saveAdminEmailAlertsConfig" id="alertSettingForm" commandName='alertForm' method="POST" enctype="multipart/form-data">
	<!-- Page Content -->
	<div id="page-wrapper">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12">
					<div class="right_first_box">
						<a href="#"><img src="resources/core/images/home.png"></a><span class="breadscrum_active">Setup Alerts </span>
					</div>
				</div>
			</div>
			<div class="row"
				style="background: #fff; margin-right: 0px; margin-left: 0px;">
				<div class="col-lg-7 col-md-12">
					<h4 class="heading">Email Alert Notifications</h4>
					<h6 class="${css} text-center">${message}</h6>
					<div class="row">
						<div class="col-md-12 col-sm-12">
							<form:checkbox path="is_reconciliation_success_emails" value="1" id="is_reconciliation_success_emails"/>
							<label>Send Reconciliation Successful Alerts</label>
							<form:input type="hidden" path="is_reconciliation_success_emails"  value="0"/>
						</div>
					</div>
					<div class="row">
						<div class="col-md-8">
							<form:textarea class="inp validate[funcCall[validateEmails[reconciliation_success_emails]],condRequired[]] text-input" path="reconciliation_success_emails" id="reconciliation_success_emails" placeholder="email1,email2,..." /> 
						</div>
					</div>
					
					<div class="row">
						<div class="col-md-12 col-sm-12">
							<form:checkbox path="is_reconciliation_failure_emails" value="1" id="is_reconciliation_failure_emails"/>
							<label>Send Reconciliation Failure Alerts</label>
							<form:input type="hidden" path="is_reconciliation_failure_emails"  value="0"/>
						</div>
					</div>
					<div class="row">
						<div class="col-md-8">
							<form:textarea class="inp validate[funcCall[validateEmails[reconciliation_failure_emails]],condRequired[]] text-input" path="reconciliation_failure_emails" id="reconciliation_failure_emails" placeholder="email1,email2,..."/> 
						</div>
					</div>
					
					<div class="row">
						<div class="col-md-12 col-sm-12">
							<form:checkbox path="is_settlement_success_emails" value="1" id="is_settlement_success_emails"/>
							<label>Send Settlement Success Alerts</label>
							<form:input type="hidden" path="is_settlement_success_emails"  value="0"/>
						</div>
					</div>
					<div class="row">
						<div class="col-md-8">
							<form:textarea class="inp validate[funcCall[validateEmails[settlement_success_emails]],condRequired[]] text-input" path="settlement_success_emails" id="settlement_success_emails" placeholder="email1,email2,..." /> 
						</div>
					</div>
					
					<div class="row">
						<div class="col-md-12 col-sm-12">
							<form:checkbox path="is_settlement_failure_emails" value="1" id="is_settlement_failure_emails"/>
							<label>Send Settlement Failure Alerts</label>
							<form:input type="hidden" path="is_settlement_failure_emails"  value="0"/>
						</div>
					</div>
					<div class="row">
						<div class="col-md-8">
							<form:textarea class="inp validate[funcCall[validateEmails[settlement_failure_emails]],condRequired[]] text-input" path="settlement_failure_emails" id="" placeholder="email1,email2,..." />  
						</div>
					</div>
					
					<div class="row">
						<div class="col-md-12 col-sm-12">
							<form:checkbox path="is_settle_reconcile_phone_number" value="1" id="is_settle_reconcile_phone_number"/>
							<label>Send Settlement / Reconciliation SMS</label>
							<form:input type="hidden" path="is_settle_reconcile_phone_number" value="0"/>
						</div>
					</div>
					<div class="row">
						<div class="col-md-8">
							<form:textarea class="inp validate[custom[phone],maxSize[10]]" path="settle_reconcile_phone_number" placeholder="Phone number" /> 
						</div>
					</div>
				</div>
			</div>
			<br class="clear">
			<div class="row buttons">
				<div class="col-md-12">
					<button class="btn btn-primary save_btn"
						id='save'>Save</button>
					<div class="btn btn-primary" id="cancelBtn">Cancel</div>
				</div>
			</div>
		</div>
	</div>
	<!--Page-wrapper-->
</form:form>

<script>
function validateEmail(email) 
{
	var regExp = /(^[a-z]([a-z_\.]*)@([a-z_\.]*)([.][a-z]{3})$)|(^[a-z]([a-z_\.]*)@([a-z_\.]*)(\.[a-z]{3})(\.[a-z]{2})*$)/i;
	return regExp.test(email);
}

function validateEmails(field, rules, i, options)
{
	var emails = field.val();
    var res = emails.split(",");
    for(i = 0; i < res.length; i++)
    {
    	if(!validateEmail(res[i])){
    		return "Invalid email ids";
    	}
    } 
}

$(document).ready(function(){
	$("#alertSettingForm").validationEngine('attach', {
		scroll : false
	});
	$("#save").click(function(){
		$("#alertSettingForm").validationEngine('attach', {
			scroll : false
		});
	});
})
</script>

<%@ include file="fragments/backendFooter.jsp"%>
