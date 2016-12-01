<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.payitnz.config.DynamicPaymentConstant"%>
<div id="searchResponseAjax">
	<table class="table table-bordered" id="reportTable">
		<thead>
			<tr>
				<th>Channel</th>
				<th>Alipay Transaction ID</th>
				<th>Amount</th>
				<th>Reference</th>
				<th>Particulars</th>
				<th>Date/Time</th>
				<th>Status</th>
				<!-- th>Action</th-->
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${transList != null && transList.size() > 0}">
					<c:forEach items="${transList}" var="each">
						<tr>
							<td>${each.value.channel }</td>
							<td><a href="<%= DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SITE_URL+"/getTransactionDetailsById" %>?id=${each.value.id }" target="_blank">${each.value.pgPartnerTransId }</a></td>
							<td>${each.value.mcTransAmount }</td>
							<td>${each.value.mcReference }</td>
							<td>${each.value.mcParticular }</td>
							<td>${each.value.TransactionDate }</td>
							<td>${each.value.status }</td>
							<!--  
							<c:choose>
							<c:when test="${each.value.status == 'SUCCESS' }">
							<td><a href=""><div class="btn btn-primary btnCancel">cancel</div></a></td>
							</c:when>
							<c:otherwise>
							<td></td>
							</c:otherwise>
							</c:choose>
							-->
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td></td>
						<td></td>
						<td></td>
						<td>No Transaction Details Found</td>
						<td></td>
						<td></td>
						<td></td>
						<!--td></td-->
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>

	<div class="dataTables_wrapper" id="NewPaginationContainer"></div>
	<script>
	jQuery(document).ready(function($) {
			$.fn.dataTable.moment('DD-MM-YYYY HH:mm:ss');
			$('#reportTable').DataTable({"info":false,"searching":false,"order": [[ 5, "desc" ]]});
		
		$("#NewPaginationContainer").append($(".dataTables_paginate"));
	});
</script>
</div>