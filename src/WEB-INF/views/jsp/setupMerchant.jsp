<%@ include file="fragments/backendHeader.jsp"%>
<%@ include file="fragments/backendMenu.jsp"%>
<%@ page session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="com.payitnz.config.DynamicPaymentConstant" %>
<style>
table.dataTable thead .sorting_asc:after{
	content: "";
    float:none;
}
table.dataTable thead .sorting:after{
	content: "";
    float:none;
}
table.dataTable thead .sorting_desc:after{
	content: "";
    float:none;
}
table.dataTable.no-footer{
border-bottom: 1px solid #ddd;
}
table.dataTable {
border-collapse: collapse;
}
.div_float {
float:left;
}
.readonly_background{
background-color:#eee;
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
<!-- Page Content -->
<div id="page-wrapper">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
   				 <div class="right_first_box"><a href="#"><img src="resources/core/images/home.png" ></a> / &nbsp;<span class="breadscrum_active">Setup Merchant</span></div>
             </div>
		</div>
		<br class="clear">
		<div class="row">
			<div class="col-md-12">
				<ul class="nav nav-tabs responsive-tabs">
					<li class="active"><a href="#Users">Merchants</a></li>
					<li><a href="#Create" id="create1" onclick="generateID();">Register Merchant</a></li>
				</ul>
				<div class="tab-content">
				<div class="tab-pane active" id="Users">
					<form method='POST' id="search_user_form">
						<div class="row">
							<div class="col-lg-9 col-md-11">
								<h4>Search & Filter</h4>
								<div class="row">
									<div class="col-lg-2 col-md-2 col-sm-3">
										<lable class="control_lable">Search For</lable>
									</div>
									<div class="col-lg-5 col-md-3 col-sm-9">
										<input type="text" class="inp" name="search_string"
											id="search_string" placeholder="Name, Username, Email, Phone"/>
									</div>
									<div class="col-lg-4 col-md-4 col-sm-6">
										<button type="submit"
											class="btn btn-primary save_btn btn_search">Search</button>
										<button type="button" class="btn btn-primary btn_reset">Reset</button>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-2 col-md-2 col-sm-2 col-xs-5">
										<lable>Status</lable>
									</div>
									<div class="col-lg-10 col-md-10 col-sm-4 col-xs-7">
										<div class="row check_box">
											<div class="col-md-3">
												<input type="checkbox" name="status" value="1"><label>Active</label>
											</div>
											<div class="col-md-3">
												<input type="checkbox" name="status" value="0"><label>Inactive</label>
											</div>
											<div class="col-md-3">
												<input type="checkbox" name="status" value="2" ><label>Locked Out</label>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						</form>
						<h6 class="${css} text-center">${message}</h6>
						<div id="userTableListing">
						<div class="row">
							<div class="col-md-12">
								<div class="table-responsive">
									<table class="table table-bordered" id="userTable">
										<thead>
											<tr>
												<th>Name</th>
												<th>Username</th>
												<th>Email</th>
												<th>Phone</th>
												<th>Status</th>
												<th>Action</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
											<c:when test="${Names.size() > 0 }">
											<c:forEach items="${Names}" var="Name">
												<tr>
													<td>${Name.firstName}</td>
													<td>${Name.username}</td>
													<td>${Name.email}</td>
													<td>${Name.phoneNo}</td>
													<td>${Name.status}</td>
													<td><a href="viewMerchant?id=${Name.id}">View</a> / <a
														href="editMerchant?id=${Name.id}">Edit</a> / <a
														onclick="return (confirm(${Name.id}))"href="deleteMerchant?id=${Name.id}">Delete</a></td>
												</tr>
											</c:forEach>
											</c:when>
											<c:otherwise>
											<tr>
											<td></td>
											<td></td>
											<td>No merchant found</td>
											<td></td>
											<td></td>
											<td></td>
											</tr>
											</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						</div>
						<div class="row">
							<div class="col-md-2 col-sm-5 col-xs-6">
							<a href="downloadMerchantCSV"><div class="btn btn-primary save_btn">Download CSV</div></a>
							</div>
							<div class="col-md-2 col-sm-5 col-xs-6">
							<a href="downloadMerchantPDF"><div class="btn btn-primary save_btn">Download PDF</div></a>
							</div>
						</div>
					</div>
					<!--Tab div end-->
					<div class="tab-pane" id="Create">
						<form:form action="registerMerchant" commandName='userForm'
							method='POST' id="register_form" enctype="multipart/form-data">
							<div class="row">
								<div class="col-lg-7 col-md-12">
									<div class="row">
										<div class="col-md-4">
											<form:errors cssClass="error" />
											<lable>Company Name<span class="star_required">*</span></lable>
										</div>
										<div class="col-md-8">
											<c:choose>
											<c:when test="${sessionScope.role_id == 2}">
											<form:input type="text"
												class="inp validate[required,custom[onlyLetterNumberSpaceTempSubj]]"
												placeholder="Company Name" path="companyName" id="company_name" readonly = "true" />
											</c:when>
											<c:otherwise>
											<form:input type="text"
												class="inp validate[required,custom[onlyLetterNumberSpaceTempSubj]]"
												placeholder="Company Name" path="companyName" id="company_name" />
											</c:otherwise>
											</c:choose>						
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<lable>First Name<span class="star_required">*</span></lable>
										</div>
										<div class="col-md-8">
											<form:input type="text"
												class="inp validate[required,custom[onlyLetterSp]]"
												placeholder="First Name" path="firstName" />
										</div>

									</div>
									<div class="row">
										<div class="col-md-4">
											<lable>Last Name<span class="star_required">*</span></lable>
										</div>
										<div class="col-md-8">
											<form:input type="text"
												class="inp validate[required,custom[onlyLetterSp]]"
												placeholder="Last Name" path="lastName" />
										</div>
										<div id="feedback"></div>
									</div>
									
									<div class="row">
										<div class="col-md-4">
											<lable>Username<span class="star_required">*</span></lable>
										</div>
										<div class="col-md-8">
											<form:input type="text"
												class="inp validate[required,custom[onlyLetterNumberSpaceTempSubj]]"
												placeholder="Username" path="username" />
										</div>
									</div>
									
									<div class="row">
										<div class="col-md-4">
											<lable>Phone Number<span class="star_required">*</span></lable>
										</div>
										<div class="col-md-8">
											<form:input type="text"
												class="inp validate[required,custom[phone],maxSize[10]]"
												placeholder="Phone Number" path="phoneNo" />
										</div>


									</div>
									<div class="row">
										<div class="col-md-4">
											<lable>Email<span class="star_required">*</span></lable>
										</div>
										<div class="col-md-8">
											<form:input type="text"
												class="inp validate[required,custom[email]]"
												placeholder="Email" path="email" id="txtemail" />
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<lable>Retype Email<span class="star_required">*</span></lable>
										</div>
										<div class="col-md-8">
											<form:input type="text"
												class="inp validate[required,custom[email],equals[txtemail]]"
												placeholder="Retype Email" path="retypeEmail" onpaste="return false;" />
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<lable>User ID<span class="star_required">*</span></lable>
										</div>
										<div class="col-md-8">
											<form:input type="text"
												class="readonly_background inp validate[required]"
												placeholder="user id" path="infidigiUserId" id="infidigiUserId" readonly="true"/>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<lable>Password<span class="star_required">*</span></lable>
										</div>
										<div class="col-md-8">
											<form:input type="text"
												class="readonly_background inp validate[required]"
												placeholder="password" path="infidigiPassword" id="infidigiPassword" readonly="true" />
										</div>
									</div>
									
									<div class="row">
										<div class="col-md-4">
											<lable>Permissions<span class="star_required">*</span></lable>
										</div>			
				
										<div class="col-lg-4 col-md-3 col-sm-4">
											<form:checkbox  class="check_box validate[funcCall[checkboxValidationFunction]]" id="all" label="All"
												path="all_permission" value="1" onClick="checkAll();"/>
											<form:input type="hidden" path="all_permission"  value="0"/>
										</div>
										<div class="col-lg-4 col-md-3 col-sm-4">
											<form:checkbox class="check_box validate[funcCall[checkboxValidationFunction]]" id="alipay_transactions"
												path="permission_alipay_transactions" value="1" label="Alipay Transactions"/>
											<form:input type="hidden" path="permission_alipay_transactions"  value="0"/>
										</div>	
									</div>
									
									<div class="row">
										<div class="col-md-4">											
										</div>	
										<div class="col-lg-4 col-md-3 col-sm-4">
											<form:checkbox class="check_box validate[funcCall[checkboxValidationFunction]]" id="refund"
												path="permission_refund" value="1" label="Alipay Refund"/>
											<form:input type="hidden" path="permission_refund"  value="0"/>
										</div>
										<div class="col-lg-4 col-md-4 col-sm-4">
											<form:checkbox class="check_box validate[funcCall[checkboxValidationFunction]]" id="access_app"
												path="permission_access_app" value="1" label="Access App" />
											<form:input type="hidden" path="permission_access_app"  value="0"/>
										</div>
										<form:input type="hidden" path="permission_setup_merchant"  value="0"/>
									</div>
									
									<div class="row">
										<div class="col-md-4">
										</div>	
										<div class="col-lg-4 col-md-3 col-sm-4">
											<form:checkbox  class="check_box validate[funcCall[checkboxValidationFunction]]" id="setup_users"
												path="permission_setup_users" value="1" label="Setup Users"/>
											<form:input type="hidden" path="permission_setup_users"  value="0"/>
										</div>	
										<div class="col-lg-4 col-md-4 col-sm-4">
											<form:checkbox class="check_box validate[funcCall[checkboxValidationFunction]]" id="setup_connections"
												path="permission_setup_connections" value="1" label="Setup Connections" />
											<form:input type="hidden" path="permission_setup_connections"  value="0"/>
										</div>									
									</div>
									
									<form:input type="hidden" path="permission_settlement"  value="0"/>
									<form:input type="hidden" path="permission_reconciliation"  value="0"/>
									<form:input type="hidden" class="inp" placeholder="id" path="id" />
									<br class="clear"> <br class="clear">
									<p class="note">
									<strong>Note: </strong> Select files to upload.Press Add button to add more file inputs.
									</p>
									<p class="note">
									Maximum file size allowed for each file is 10MB. Total numbers of files allowed are 10.
									</p>
									<br>
									<div class="btn btn-primary save_btn" id="addFile">Add More</div>
									<br class="clear"> <br class="clear">
								</div>
								
								<div class="row">
									<div class="col-md-12">
										<div class="table-responsive">

											<table class="table table-bordered textarea_marginbottom" id="fileTable">
												<thead>
													<tr>
														<th>File Description</th>
														<th>File</th>
														<th>Action</th>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td><textarea class="table_inp" name="file_description[0]" cols="2"
																rows="2"></textarea></td>
														<td><textarea class="table_inp" name="file_name[0]" cols="2"
																rows="2"></textarea></td>
														<td><input type="file" name="files[0]" id="files[0]" class="div_float" onchange="fileSizeValidation(this,files[0]);"><input type="button"  class="btn btn-primary save_btn" value="Delete" onclick="deleteRow(this)"></td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
								</div>
								<div class="row buttons">
									<div class="col-md-12">

										<form:button type="submit" class="btn btn-primary save_btn"
											id='save'>Save</form:button>
										<div class="btn btn-primary" id="cancelBtn">Cancel</div>
									</div>
								</div>
							</div>
							<!--Tab div end-->
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!--Container-fluid end-->
</div>
<!--Page-wrapper-->
<script type="text/javascript">
var	validationStatus = 1;
	$(document).ready(
			function() {
			
				$("#register_form").validationEngine('attach', {
					scroll : false
				});
				$("#save").click(function() {
					$("#register_form").validationEngine('attach', {
						scroll : false
					});
					if(validationStatus == 0){
						alert('File size must be less than 10 Mb!');
						return false;
					}	
				});
				
				$(".btn_reset").click(function() {
					$('#search_user_form').trigger("reset");
					var search = {}
					var statusArr  = new Array();
					search["search_string"] = $("#search_string").val();
					$('input[name="status"]:checked').each(function() {
						statusArr.push(this.value);
					});
					search['status'] = statusArr;
					blockDiv('userTableListing');
					$.ajax({
						type : "POST",
						url :"<%= "/"+DynamicPaymentConstant.SITE_URL +"/getMerchantsTableAjax" %>",
						dataType : "html",
						contentType: "application/json; charset=utf-8",
						data : JSON.stringify(search),
						timeout : 100000,
						success : function(data) {
							$('#userTableListing').html(data);
							unblockDiv('userTableListing');
						},
						error : function(e) {
							console.log("ERROR: ", e);
							unblockDiv('userTableListing');
						},
						done : function(e) {
							console.log("DONE");
							unblockDiv('userTableListing');
						}
					});
				});
				
				$("#search_user_form").submit(function(event) {
					// Prevent the form from submitting via the browser.
					event.preventDefault();
					//search data
					var search = {}
					var statusArr  = new Array();
					search["search_string"] = $("#search_string").val();
					$('input[name="status"]:checked').each(function() {
						statusArr.push(this.value);
					});
					search['status'] = statusArr;
					blockDiv('userTableListing');
					$.ajax({
						type : "POST",
						url :"<%= "/"+DynamicPaymentConstant.SITE_URL +"/getMerchantsTableAjax" %>",
						dataType : "html",
						contentType: "application/json; charset=utf-8",
						data : JSON.stringify(search),
						timeout : 100000,
						success : function(data) {
							$('#userTableListing').html(data);
							unblockDiv('userTableListing');
						},
						error : function(e) {
							console.log("ERROR: ", e);
							unblockDiv('userTableListing');
						},
						done : function(e) {
							console.log("DONE");
							unblockDiv('userTableListing');
						}
					});
				});
				
				$('#cancelBtn').click(function(){
					window.location.href="setupMechant";
				});
				
				$('#addFile').click(function() {
					var fileIndex = $('#fileTable tr').length - 1;
					if(fileIndex < 10){
						$('#fileTable').append(
								'<tr>'+
								'<td><textarea class="table_inp" name="file_description['+ fileIndex +']" cols="2" rows="2"></textarea></td>'+
								'<td><textarea class="table_inp" name="file_name['+ fileIndex +']" cols="2" rows="2"></textarea></td>'+
								'<td><input type="file" name="files['+ fileIndex +']" id="files['+ fileIndex +']" class="div_float" onchange="fileSizeValidation(this,files['+ fileIndex+']);"><input type="button"  class="btn btn-primary save_btn" value="Delete" onclick="deleteRow(this)"></td>'+
								'</tr>');
					}
				});
				
				$('#userTable').DataTable({"info":false,"searching":false});				
			});
	
	function fileSizeValidation(event,attribute){
		validationStatus = 1;
		var filesize =  event.files[0].size/1048576;
	
		if(filesize > 10)
		{
			alert('File size must be less than 10 Mb!');
			validationStatus = 0;
		}
	}
	
	function deleteRow(row)
	{
	    var i=row.parentNode.parentNode.rowIndex;
	    document.getElementById('fileTable').deleteRow(i);
	}
</script>

<script>
function confirm(id){
	alertify.confirm("Are you sure you want to delete this user?", function (e) { if (e) {
	        // user clicked "ok"
			window.location.href="deleteMerchant?id="+id;
	    } else {
	        // user clicked "cancel"
	  		return false;
	    }
	});
	return false;
}

function generateID(){
	var status = false;
	var search = {}
	
	//genrate userId
	do{
	search["infidigiUserId"] = ""+<c:out value="${sessionScope.merchantId}"/>+""+ Math.floor(Math.random() * 10000);	
	$.ajax({
		type : "POST",
		url :"<%= DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SITE_URL+"/checkMerchantUserId" %>",
		contentType: "application/json; charset=utf-8",
		data : JSON.stringify(search),
		timeout : 100000,
		success : function(data) {
			status = data;
		},
		error : function(e) {
		},
		done : function(e) {
		}
	});
		
	}while(status == true);
	
	//check if exists	
	$("#infidigiUserId").val(search["infidigiUserId"]);
	$("#infidigiPassword").val(Math.floor(Math.random() * 1000000000));
}


function blockDiv(element,height,width) {
	$('#'+element).block({
		message: '<img src="<%= DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SITE_URL+"/resources/core/images/loader.gif" %>" />',
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

function checkAll(){

	if($('#all').is(":checked")){
		$("#refund").prop('checked', true);
		$("#alipay_transactions").prop('checked', true);
		//$("#setup_merchant").prop('checked', true);
		$("#access_app").prop('checked', true);
		//$("#settlement").prop('checked', true);
		//$("#reconciliation").prop('checked', true);
		$("#setup_users").prop('checked', true);
		$("#setup_connections").prop('checked', true);
	}else{
		$("#refund").prop('checked', false);
		$("#alipay_transactions").prop('checked', false);
		$("#access_app").prop('checked', false);
		//$("#setup_merchant").prop('checked', false);
		//$("#settlement").prop('checked', false);
		//$("#reconciliation").prop('checked', false);
		$("#setup_users").prop('checked', false);
		$("#setup_connections").prop('checked', false);
	}
}

function checkboxValidationFunction() {
  	var all = $('#all').is(":checked");
  	var refund = $("#refund").is(":checked");
  	var alipay_transactions = $("#alipay_transactions").is(":checked");
  	//var setup_merchant = $("#setup_merchant").is(":checked");
	var access_app = $("#access_app").is(":checked");
	//var settlement = $("#settlement").is(":checked");
	//var reconciliation = $("#reconciliation").is(":checked");
	var setup_users = $("#setup_users").is(":checked");
	var setup_connections = $("#setup_connections").is(":checked");

  if (!all && !refund && !alipay_transactions  && !access_app && !setup_users && !setup_connections) {
    return "permission is required";
  }

}
</script>
<%@ include file="fragments/backendFooter.jsp"%>
