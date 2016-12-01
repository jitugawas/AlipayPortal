<%@ include file="fragments/backendHeader.jsp"%>
<%@ include file="fragments/backendMenu.jsp"%>
<%@ page session="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<form:form action="registerMerchant" commandName='userForm'
	id="userForm" method="POST" enctype="multipart/form-data">
	<!-- Page Content -->
	<div id="page-wrapper">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12">
					<div class="right_first_box">
						<img src="resources/core/images/home.png" width="21" height="19"><a
							href="#">Home</a> > <a href="#">Edit User Details</a>
					</div>
				</div>
			</div>
			<div class="row"
				style="background: #fff; margin-right: 0px; margin-left: 0px;">
				<div class="col-lg-6 col-md-6">
					<form:errors cssClass="error" />

					<h4 class="heading">Edit User Details</h4>

					<div class="row">
						<div class="form-group">
							<div class="col-md-4">
								<lable>Company Name<span class="star_required">*</span></lable>
							</div>
							<div class="col-md-8">
								<form:input type="text"
									class="inp validate[required,custom[onlyLetterNumberSpaceTempSubj]]"
									placeholder="Comapany Name" path="company_name" />
								<form:errors path="company_name" cssClass="error"></form:errors>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="form-group">
							<div class="col-md-4">
								<lable>First Name<span class="star_required">*</span></lable>
							</div>
							<div class="col-md-8">
								<form:input type="text"
									class="inp validate[required,custom[onlyLetterSp]]"
									placeholder="First Name" path="first_name" />
								<form:errors path="first_name" cssClass="error"></form:errors>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-md-4">
							<lable>Last Name<span class="star_required">*</span></lable>
						</div>
						<div class="col-md-8">
						<form:input type="hidden" class="inp" path="verified" />
							<form:input type="text"
								class="inp validate[required,custom[onlyLetterSp]]"
								placeholder="Last Name" path="last_name" />
							<form:errors path="last_name" cssClass="error"></form:errors>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<lable>Phone Number<span class="star_required">*</span></lable>
						</div>
						<div class="col-md-8">
							<form:input type="hidden" class="inp" placeholder="id" path="id" />
							<form:input type="text"
								class="inp validate[required,custom[phone],minSize[10]],maxSize[10]]]"
								placeholder="Phone Number" path="phone_number" />
							<form:errors path="phone_number" cssClass="error"></form:errors>
						</div>

					</div>
					<div class="row">
						<div class="col-md-4">
							<lable>Email<span class="star_required">*</span></lable>
						</div>
						<div class="col-md-8">
						<form:input type="hidden" class="inp" path="password" />
							<form:input type="text"
								class="inp validate[required,custom[email]]" placeholder="Email" path="email" readonly="true"/>
							<form:errors path="email" cssClass="error"></form:errors>
						</div>

					</div>
					<div class="row">
						<div class="col-md-4">
							<lable>Status<span class="star_required">*</span></lable>
						</div>
						<div class="col-md-8">
						<form:select class="select inp validate[required]" path="status" id="status">
			            <form:option value="0">Inactive</form:option>
			            <form:option value="1">Active</form:option>
			            <form:option value="2">LockedOut</form:option>
			            </form:select>
							<form:errors path="status" cssClass="error"></form:errors>
						</div>
					</div>
					
					<div class="row">
					<div class="col-md-12">
					<div class="table-responsive">

						<table class="table table-bordered textarea_marginbottom" id="fileListing">
							<thead>
								<tr>
									<th>File Name</th>
									<th>File Description</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody>
							<c:choose>
							<c:when test="${UserFiles.size() > 0 }">
							<c:forEach items="${UserFiles}" var="file">
							<tr>
							<td>${file.file_name}</td>
							<td>${file.file_description}</td>
							<td><input type="button" class="btn btn-primary save_btn" value="Delete"  onclick="return (deleteFile(${file.id},${file.user_id}))"></td>
							</tr>
							</c:forEach>
							</c:when>
							<c:otherwise>
							<tr>
							<td></td>
							<td>No uploaded file found</td>
							<td></td>
							</tr>
							</c:otherwise>
							</c:choose>
							</tbody>
						</table>
					</div>
					</div>
					</div>
					
					<p class="note">
						<strong>Note: </strong> Select files to upload.Press Add button to
						add more file inputs.
					</p>
					<p class="note">Maximum file size allowed for each file is
						10MB. Total numbers of files allowed are 10.</p>
					<br>
					<div class="btn btn-primary save_btn" id="addFile"
						style="margin-bottom: 10px;">Add More</div>
				</div>

			</div>
			<br class="clear">
			<div class="row">
				<div class="col-md-12">
					<div class="table-responsive">

						<table class="table table-bordered textarea_marginbottom"
							id="fileTable">
							<thead>
								<tr>
									<th>File Description</th>
									<th>File</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody>
							<tr>
							<td><textarea class="table_inp" name="file_description[0]"
									cols="1" rows="2"></textarea></td>
							<td><textarea class="table_inp" name="file_name[0]"
									cols="1" rows="2"></textarea></td>
							<td><input type="file" name="files[0]" id="files[0]" onchange="fileSizeValidation(this,files[0]);"><input type="button"  class="btn btn-primary save_btn" value="Delete" onclick="deleteRow(this)"></td>
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
	</div>
	<!--Page-wrapper-->
</form:form>

<script type="text/javascript">
var	validationStatus = 1;
	$(document)
			.ready(
					function() {
						$("#userForm").validationEngine('attach', {
							scroll : false
						});
						$("#save").click(function() {
							$("#userForm").validationEngine('attach', {
								scroll : false
							});
							
							if(validationStatus == 0){
								alert('File size must be less than 10 Mb!');
								return false;
							}
						});

						$('#cancelBtn').click(function() {
							window.location.href = "setupUser";
						});

						$('#addFile')
								.click(
										function() {
											var uploadedFiles = $('#fileListing tr').length - 1;
											var fileIndex = $('#fileTable tr').length - 1;
											if ((parseInt(fileIndex)+parseInt(uploadedFiles)) < 10) {
												$('#fileTable')
														.append(
																'<tr>'
																		+ '<td><textarea class="table_inp" name="file_description['+ fileIndex +']" cols="1" rows="2"></textarea></td>'
																		+ '<td><textarea class="table_inp" name="file_name['+ fileIndex +']" cols="1" rows="2"></textarea></td>'
																		+ '<td><input type="file" name="files['+ fileIndex +']" id="files['+ fileIndex +']" onchange="fileSizeValidation(this,files['+ fileIndex+']);"><input type="button"  class="btn btn-primary save_btn" value="Delete" onclick="deleteRow(this)"></td>'
																		+ '</tr>');
											}
										});
						
						
						var status = $("#status").val();
						if(status == 0){
							$("#status option[value='1']").remove();
							$("#status option[value='2']").remove();
						}else if(status == 1){
							$("#status option[value='0']").remove();
						}else if(status == 2){
							$("#status option[value='0']").remove();
						}
						
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
	
	
	function deleteFile(id,userId)
	{
		alertify.confirm("Are you sure you want to delete this file?", function (e) { if (e) {
	        // user clicked "ok"
			window.location.href="deleteUserFile?id="+id+"&userId="+userId;
	    } else {
	        // user clicked "cancel"
	  		return false;
	    }
	});
	return false;
	 
	}

</script>
<%@ include file="fragments/backendFooter.jsp"%>
