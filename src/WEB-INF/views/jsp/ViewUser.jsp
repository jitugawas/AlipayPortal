<%@ include file="fragments/backendHeader.jsp"%>
<%@ include file="fragments/backendMenu.jsp"%>
<%@ page session="true"%>
<!-- Page Content -->
<div id="page-wrapper">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="right_first_box">
					<a href="#"><img src="resources/core/images/home.png"></a><a
						href="setupUser">Setup Users</a> / &nbsp;<span class="breadscrum_active">View User Details</span>
				</div>
			</div>
		</div>
		<br class="clear">
		<div class="row" style="background:#fff;margin-right:0px;margin-left:0px;">
			<div class="col-lg-6 col-md-6">
			<a href="setupUser"><div class="btn btn-primary save_btn" style="margin-top:10px;">Return to User Listing</div></a>
			<h4 class="heading">User Details</h4>
			<div class="table-responsive">
				<table class="table table-bordered textarea_marginbottom">
				<tbody>
				<tr>
				<td>Company Name</td>
				<td>${companyName}</td>
				</tr>
				<tr>
				<td>Name</td>
				<td>${FirstName} ${Lastname}</td>
				</tr>
				<tr>
				<td>Account ID</td>
				<td>${AccountID}</td>
				</tr>
				<tr>
				<td>User ID</td>
				<td>${UserID}</td>
				</tr>
				<tr>
				<td>Username</td>
				<td>${UserName}</td>
				</tr>
				<tr>
				<td>Phone Number</td>
				<td>${MobileNumber}</td>
				</tr>
				<tr>
				<td>Email</td>
				<td>${Email}</td>
				</tr>
				<tr>
				<td>Created Date</td>
				<td>${CreatedDate}</td>
				</tr>
				</tbody>
			</table>
			</div>
			</div>
		</div>
		
		<div class="row" style="background:#fff;margin-right:0px;margin-left:0px;">
			<div class="col-lg-6 col-md-6">
			<h4 class="heading">User Files Details</h4>
			<div class="table-responsive">
				<table class="table table-bordered textarea_marginbottom">
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
				<td><input type="button" class="btn btn-primary save_btn" value="Download"  onclick="downloadFile(${file.id})"></td>
				</tr>
				</c:forEach>
				</c:when>
				<c:otherwise>
				<tr>
				<td></td>
				<td>No file found</td>
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
</div>
<script>
function downloadFile(id)
{
window.location.href="downloadFile?id="+id;
}
</script>
<!--Page-wrapper-->
<%@ include file="fragments/backendFooter.jsp"%>