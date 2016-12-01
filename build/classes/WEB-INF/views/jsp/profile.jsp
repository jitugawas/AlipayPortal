<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="com.payitnz.config.DynamicPaymentConstant"%>
<style>
.inp {
margin-bottom:0px;
}
</style>
<div class="modal-dialog" id="dialog">
	<!-- Modal content-->
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 class="modal-title text-center">User Profile</h4>
		</div>
		<div class="modal-body">
			<div class="row">
				<div class="col-md-12 ">
					<form class="form-horizontal" id="updateProfile"
						action="updateProfile" method="POST" enctype="multipart/form-data">
						<fieldset>
							<hr>
							<h4>Basic Details</h4>
							<hr>
							<!-- Form Name -->
							<!-- Text input-->
							<div class="form-group">
								<label class="col-md-4 control-label" for="Name (Full name)">First
									Name</label>
								<div class="col-md-6">
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-user"> </i>
										</div>
										<input id="FirstName" name="FirstName" type="text"
											placeholder="first name" value="${FirstName}"
											class="inp validate[required,custom[onlyLetterSp]]"
											>
									</div>
								</div>
							</div>

							<div class="form-group">
								<label class="col-md-4 control-label" for="Name (Full name)">Last
									Name</label>
								<div class="col-md-6">
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-user"> </i>
										</div>
										<input id="LastName" name="LastName" type="text"
											placeholder="last name" value="${LastName}"
											class="inp validate[required,custom[onlyLetterSp]]"
											>
									</div>
								</div>
							</div>

							<div class="form-group">
								<label class="col-md-4 control-label" for="Name (Full name)">Username</label>
								<div class="col-md-6">
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-user"> </i>
										</div>
										<input id="UserName" name="UserName" type="text"
											placeholder="username" value="${UserName}" readonly="true"
											class="form-control input-md inp validate[required,custom[onlyLetterNumberSpaceTempSubj]]"
											>
									</div>
								</div>
							</div>

							<!-- Text input-->
							<div class="form-group">
								<label class="col-md-4 control-label" for="Primary Occupation">Company
									Name</label>
								<div class="col-md-6">
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-briefcase"></i>
										</div>
										<input id="CompanyName" name="CompanyName" type="text"
											placeholder="Company" value="${CompanyName}"
											class="form-control input-md inp validate[required,custom[onlyLetterNumberSpaceTempSubj]]">
									</div>
								</div>
							</div>


							<!-- Text input-->
							<div class="form-group">
								<label class="col-md-4 control-label" for="Phone number ">Phone
									Number </label>
								<div class="col-md-6">
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-mobile fa-1x" style="font-size: 20px;"></i>
										</div>
										<input id="MobileNumber" name="MobileNumber" type="text"
											class="form-control input-md inp validate[required,custom[phone],maxSize[10]]"
											placeholder="Phone number " value="${MobileNumber}"
											>
									</div>
								</div>
							</div>


							<!-- Text input-->
							<div class="form-group">
								<label class="col-md-4 control-label" for="Email Address">Email
									Address</label>
								<div class="col-md-6">
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-envelope-o"></i>
										</div>
										<input id="Email" name="Email" type="text"
											value="${Email}" placeholder="Email Address"
											class="form-control input-md inp" readonly="true">
									</div>
								</div>
							</div>

							<hr>
							<h4>Change Password</h4>
							<hr>

							<div class="form-group">
								<label class="col-md-4 control-label" for="Name (Full name)">Current
									password</label>
								<div class="col-md-6">
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-key"> </i>
										</div>
										<input id="current_psswd" name="current_psswd" type="password"
											placeholder="Current password"
											class="form-control input-md validate[funcCall[checkCurrentPassword[current_psswd]],condRequired[new_psswd]] text-input" >
									</div>
								</div>
							</div>

							<div class="form-group">
								<label class="col-md-4 control-label" for="Name (Full name)">New
									Password</label>
								<div class="col-md-6">
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-key"> </i>
										</div>
										<input id="new_psswd" name="new_psswd" type="password"
											placeholder="New Password"
											class="form-control input-md validate[condRequired[current_psswd],minSize[6],maxSize[12]]">
									</div>
								</div>
							</div>

							<div class="form-group">
								<label class="col-md-4 control-label" for="Name (Full name)">Retype
									Password</label>
								<div class="col-md-6">
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-keyboard-o"> </i>
										</div>
										<input id="cfm_new_psswd" name="cfm_new_psswd"
											type="password" placeholder="Retype Password"
											class="form-control input-md validate[condRequired[current_psswd],equals[new_psswd],minSize[6],maxSize[12]]">
									</div>
								</div>
							</div>


							<div class="form-group">
								<label class="col-md-4 control-label"></label>
								<div class="col-md-6">
									<button type="submit" id="save" href="" class="submit btn btn-success">
										<span class="glyphicon glyphicon-thumbs-up"></span> Save
									</button>
									<a href="#" class="btn btn-danger" value=""
										data-dismiss="modal"><span
										class="glyphicon glyphicon-remove-sign"></span> Cancel</a>
								</div>
							</div>
						</fieldset>
					</form>
				</div>
				<!--div class="col-md-2 hidden-xs">
<img src="http://websamplenow.com/30/userprofile/images/avatar.jpg" class="img-responsive img-thumbnail ">
  </div-->
			</div>
		</div>
	</div>
</div>

<script>
$(document).ready(function(){
	$('#updateProfile .submit').click(function(event){
		 event.preventDefault();	 
		 $("#updateProfile").validationEngine('attach', {
				scroll : false
			});
		 var validation = true;
		 
		 //check if current password , new password and confirm password
		 if($("#new_psswd").validationEngine('validate') || $("#cfm_new_psswd").validationEngine('validate') || $("#current_psswd").validationEngine('validate')){
			 validation = false;
		 }
		 
		 //check other fields
		 if($("#FirstName").validationEngine('validate') || $("#LastName").validationEngine('validate') || $("#CompanyName").validationEngine('validate') || $("#MobileNumber").validationEngine('validate')){
			 validation = false;
		 }
		 
		 if(validation){
			 if($("#updateProfile").validationEngine('validate')){
				 ajax_operation();
			 }
		 }else{
			 return false;
		 }
		
		});
	
	$("#updateProfile").validationEngine('attach', {
		scroll : false
	});
	
});



function checkCurrentPassword(field, rules, i, options){
	 var a=rules[i+2];
	 var curntPswdStatus = function () {
		 var currentPasswordStatus = null;
		 $.ajax({
				type : "POST",
				'async': false,
				url :"<%= "/"+DynamicPaymentConstant.SITE_URL+"/checkUserPassword/" %>",
				data : {'fieldValue':field.val()},
				dataType : 'json',
				timeout : 100000,		
				success : function(data) {	
					currentPasswordStatus = data[1];
				},
				error : function(data) {
				},
			});	
		    return currentPasswordStatus;
		}();
		
		if(!curntPswdStatus || curntPswdStatus == null){
			return "* Incorrect current password" ;
		}else {
			return true;
		}
}

function ajax_operation(){
	blockDiv('dialog');
	var formElement = document.getElementById("updateProfile");
	$.ajax({
		type : "POST",
		url :"<%= "/"+DynamicPaymentConstant.SITE_URL+"/updateProfile/" %>",
		timeout : 100000,
		data : new FormData(formElement),
		 processData: false,
		 contentType: false,				
		 success : function(data) {	
			 unblockDiv('dialog');
			 var current_psswd = $("#current_psswd").val("");		
			 var password = $('#new_psswd').val("");
			 var confirmPassword = $('#cfm_new_psswd').val("");
			 alertify.alert("Profile updated successfully");
		},
		error : function(data) {
		},
	});
}
</script>



<script>
function blockDiv(element,height,width) {
	$('#'+element).block({
		message: '<img src="<%=DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SITE_URL+"/resources/core/images/loader.gif" %>" />',
		css: { 
			top:  ($(window).height() - 24) / 2 + 'px', 
			left: ($(window).width() - 144) / 2 + 'px', 
			height: '24px',
			width: '144px',
			border: '0'
	   } 
	});
	if(height>0)
	$('.blockUI.blockMsg').center(height, width);
}

function unblockDiv(element) {
	$('#'+element).unblock();
}
</script>