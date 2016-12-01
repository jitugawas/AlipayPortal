<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
							<td><a href="viewUser?id=${Name.id}">View</a> / <a
								href="editUser?id=${Name.id}">Edit</a> / <a
								onclick="return (confirm(${Name.id}))"href="deleteUser?id=${Name.id}">Delete</a></td>
						</tr>
					</c:forEach>
					</c:when>
					<c:otherwise>
					<tr>
					<td></td>
					<td></td>
					<td>No user found</td>
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
<script type="text/javascript">
	$(document).ready(
			function() {
				$('#userTable').DataTable({"info":false,"searching":false});
			});
</script>