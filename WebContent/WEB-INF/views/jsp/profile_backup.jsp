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
										<input id="Name (Full name)" name="FirstName" type="text"
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
										<input id="Name (Full name)" name="LastName" type="text"
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
										<input id="Name (Full name)" name="UserName" type="text"
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
										<input id="Phone number " name="MobileNumber" type="text"
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
										<input id="Email Address" name="Email" type="text"
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
											class="form-control input-md validate[ajax[ajaxCheckUserPasswordCall]]" >
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
									<button type="submit" id="save" href="" class="btn btn-success">
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

<script type="text/javascript">
	$(document).ready(
			function() {
				$("#updateProfile").validationEngine('attach', {
					scroll : false
				});
				
				$("#updateProfile").submit(function(e) {					
				
					
					$("#updateProfile").validationEngine('attach', {
						scroll : false
					})
					
					e.preventDefault();
					
					var current_psswd = $("#current_psswd").val();		
					var password = $('#new_psswd').val();
 					var confirmPassword = $('#cfm_new_psswd').val();
					var validation = true;
						
					if (current_psswd){
						//check if current password exists
						var success = 
							function () {
							var success = false;
							var search = {}
							search['infidigiPassword'] = current_psswd;
							$.ajax({
								type : "POST",
								async:false,
								url :"<%= "/"+DynamicPaymentConstant.SITE_URL+"/checkUserPassword" %>",
								timeout : 100000,
								dataType: 'json',
								contentType: "application/json; charset=utf-8",	
								data : JSON.stringify(search),	
								success : function(data) {
									success = data[1];
								},
								error : function(data) {
								},
							});
							return success;
						}();

						if(success){
							
							//check if password  and confirm password
							if(password == ""){
								return false;
								validation = false;
							}						
							if(confirmPassword == ""){
								return false;
								validation = false;
							}						
							// match both fields
							if(password != confirmPassword){
								return false;
								validation = false;
							}					
							//continue update profile		
						}else{
							return false;
							validation = false;
						}	
					}
 						
 						
 					if(validation){
 						blockDiv('dialog');
 						var formElement = document.getElementById("updateProfile");
						$.ajax({
							type : "POST",
							async:false,
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
 							
			});		
			});	

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