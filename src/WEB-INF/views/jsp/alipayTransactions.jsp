

<%@ include file="fragments/backendHeader.jsp"%>
<%@ include file="fragments/backendMenu.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@	taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<style>
.ui-datepicker-trigger {
	margin-bottom: 20px;
	cursor: pointer;
}
table.dataTable thead .sorting_asc:after {
	content: "";
	float: none;
}

table.dataTable thead .sorting:after {
	content: "";
	float: none;
}

table.dataTable thead .sorting_desc:after {
	content: "";
	float: none;
}

table.dataTable.no-footer {
	border-bottom: 1px solid #ddd;
}

table.dataTable {
	border-collapse: collapse;
}

.div_float {
	float: left;
}

.readonly_background {
	background-color: #eee;
}

.col-lg-2{
	width:14%;
}

.col-md-2{

}

.col-sm-4{

}

#divLoading
{
	display : none;
}
#divLoading.show
{
	display : block;
	position : fixed;
	z-index: 100;
	//background-image : url('http://loadinggif.com/images/image-selection/3.gif');
	background-image:url('<%=DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SITE_URL+"/resources/core/images/loader.gif"%>');
	background-color:#000;
	opacity : 0.6;
	background-repeat : no-repeat;
	background-position : center;
	left : 0;
	bottom : 0;
	right : 0;
	top : 0;
}
#loadinggif.show
{
	left : 50%;
	top : 50%;
	position : absolute;
	z-index : 101;
	width : 32px;
	height : 32px;
	margin-left : -16px;
	margin-top : -16px;
}
div.content {
	width : 1000px;
	height : 1000px;
}

.hide{
	display:none;
}

</style>

<div id="divLoading"> 
Processing......
</div>

<!-- Page Content -->
<div id="page-wrapper">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="right_first_box">
					<a href="#"><img src="resources/core/images/home.png"></a> /
					&nbsp;<span class="breadscrum_active">Alipay Transactions</span>
				</div>
			</div>
		</div>

		<h3 class="main_heading">Alipay Transactions</h3>

		<div class="row">
			<div class="col-md-12">
				<ul class="nav nav-tabs responsive-tabs">
					<li class="active"><a href="#all">Report</a></li>
					<!-- <li><a href="#alipay_online">Refund</a></li>-->

				</ul>

				<form:form id="TransactionForm" method="POST" action="search"
					commandName='search'>
					<div class="tab-content">
						<div class="tab-pane active" id="all">

							<div class="row">
								<div class="col-lg-12 col-md-12">
									<div class="row">
										<div class="col-lg-4 col-md-6 col-sm-12">
											<lable class="calender_lable">Start Date</lable>
											<input type="text" id="fromSearchDate1"
												name="fromSearchDate1" class="calender_inp date"
												placeholder="dd/mm/yyyy" readOnly>
										</div>

										<div class="col-lg-4 col-md-6 col-sm-12">
											<lable class="calender_lable">End Date</lable>
											<input type="text" id="toSearchDate1" name="toSearchDate1"
												class="calender_inp date" placeholder="dd/mm/yyyy" readOnly>
										</div>
										<div class="col-lg-4 col-md-8 col-sm-12">
											<lable class="calender_lable">Transaction ID</lable>
											<input type="text" class="calender_select select_width"
												id="trans_id" placeholder="Transaction ID">
										</div>
										
									</div>

									<div class="row">
										<div class="col-lg-4 col-md-8 col-sm-12">
											<lable class="calender_lable"> Particulars</lable>
											<input type="text" class="calender_select select_width"
												id="particular" placeholder="Particular">
										</div>
										<div class="col-lg-4 col-md-8 col-sm-12">
											<lable class="calender_lable"> Reference</lable>
											<input type="text" class="calender_select select_width"
												id="reference" placeholder="Reference">
										</div>

										<div class="col-lg-3 col-md-8 col-sm-12">
											<lable class="calender_lable2">Channel</lable>
											<select class="calender_select select_width" id="channel">
												<option value="">All</option>
												<option value="Alipay Offline">Alipay Offline</option>
												<option value="Alipay Online">Alipay Online</option>
											</select>
										</div>
									</div>


									<div class="row">
										<c:if test="${sessionScope.role_id == 1}">
										<div class="col-lg-3 col-md-8 col-sm-12">
											<lable class="calender_lable">Select Merchant</lable>
											<select class="calender_select select_width" id="user_id">
												<option value="">All</option>
												<c:choose>
													<c:when test="${Users.size() > 0 }">
														<c:forEach items="${Users}" var="each">
															<option value="${each.value.user_id}">${each.value.Name}</option>
														</c:forEach>
													</c:when>
												</c:choose>

											</select>
										</div>
										</c:if>
									</div>									

									<div class="row">
							          <div class="col-lg-2 col-md-3 col-sm-4">
							           <input type="checkbox" class="check_box" id="success"
							            name="status[]" value="SUCCESS">Successful
							          </div>
							          <div class="col-lg-2 col-md-3 col-sm-4">
							           <input type="checkbox" class="check_box" id="declined"
							            name="status[]" value="FAILED">Failed
							          </div>
							          <div class="col-lg-2 col-md-3 col-sm-4">
							           <input type="checkbox" class="check_box" id="refund"
							            name="status[]" value="REFUND">Refund
							          </div>
							          <div class="col-lg-2 col-md-3 col-sm-4">
							           <input type="checkbox" class="check_box" id="settled"
							            name="status[]" value="IS_SETTLED">Settled
							          </div>
							          <div class="col-lg-2 col-md-3 col-sm-4">
							           <input type="checkbox" class="check_box" id="nSettled"
							            name="status[]" value="NOT_SETTLED">Not Settled
							          </div>
							          <div class="col-lg-2 col-md-3 col-sm-4">
							           <input type="checkbox" class="check_box" id="other"
							            name="status[]" value="CANCELLED">Cancelled
							          </div>        
									</div>
									
									<div class="row">
										<div class="col-md-12">
											<button type="submit" id="searchTransactions"
												class="btn btn-primary">Search</button>
										</div>
									</div>
									<br class="clear"> 
									<div class="row">
										<div class="col-md-12">

											<div class="table-responsive" id="searchResponseAjax">
												<table class="table table-bordered" id="reportTable">
													<thead>
														<tr>
															<th>Channel</th>
															<th>Alipay ID</th>
															<th>Amount</th>
															<th>Reference</th>
															<th>Particulars</th>
															<th>Date/Time</th>
															<th>Status</th>
															<!-- <th>Action</th> -->
														</tr>
													</thead>
													<tbody>
														<c:choose>
															<c:when test="${transList.size() > 0 }">
																<c:forEach items="${transList }" var="each">
																	<tr>
																		<td>${each.value.channel }</td>
																		<td><a href="<%= DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SITE_URL+"/getTransactionDetailsById" %>?id=${each.value.id }" target="_blank">${each.value.pgPartnerTransId }</a></td>
																		<td>${each.value.mcTransAmount }</td>
																		<td>${each.value.mcReference }</td>
																		<td>${each.value.mcParticular }</td>
																		<td>${each.value.TransactionDate }</td>
																		<td>${each.value.status }</td>
																		<!--<c:choose>
																		<c:when test="${each.value.status == 'SUCCESS' }">
																		<td><a href="javascript:void(0);"><div class="btn btn-primary btnCancel">cancel</div></a></td>
																		</c:when>
																		<c:otherwise>
																		<td></td>
																		</c:otherwise>
																		</c:choose>-->
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
																	<!-->td></td-->
																</tr>
															</c:otherwise>
														</c:choose>
													</tbody>
												</table>
											</div>
										</div>
									</div>

									<div class="row buttons">
										<div class="col-md-12 col-sm-12 col-xs-12">
											<a href="downloadReportPDF"><div class="btn btn-primary">Download PDF</div></a>
											 <a href="downloadReportCSV"><div class="btn btn-primary">Download CSV</div></a>
										</div>
									</div>

								</div>
							</div>

							<br class="clear">
						</div>
						
						 <div class="tab-pane" id="alipay_online">
          
                
                 
                 <div class="row">
                <div class="col-lg-12 col-md-11">
               <div class="row">
               <div class="col-lg-2 col-md-4 control_label">Amount</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Amount" id="amt">
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Date/Time</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Date/Time" id="dateAndtime">
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Transaction ID</div>
               <div class="col-lg-4 col-md-6">
               <input type="email" class="inp" placeholder="Transaction ID" id="transaction_id">
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Status</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Status" id="status">
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Channel</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Channel" id="chan">
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Reference</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Reference" id="ref">
               </div>
               </div>
              
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Comments</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Comments" id="comments">
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Email</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Email" id="email">
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Mobile</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Mobile" id="mobile">
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Coordinates</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Coordinates" id="coordinates">
               </div>
               </div>
              <!-- <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Settlement ID</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Settlement ID" id="settlement_id">
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Settlement Date/Time</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Settlement Date/Time" id="settlement_date">
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Settlement Amount</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Settlement Amount" id="settlement_amt">
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Settlement </div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Settlement" id="settlement">
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2 col-md-4 control_label">Confirmation ID</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Confirmation ID" id="confirmation_id">
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2 col-md-4 control_label">Convenience Fee</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Convenience Fee" id="confee">
               </div>
               </div>
                <div class="row">
               <div class="col-lg-2 col-md-4 control_label">Alipay Charge</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Alipay Charge" id="alipay_charge">
               </div>
               </div>
                <div class="row">
               <div class="col-lg-2 col-md-4 control_label">Transaction Fee</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Transaction Fee" id="trans_fee">
               </div>-->
               </div>
               
              
               </div>
                </div>
               
                <!-- <div class="row buttons">
                     <div class="col-md-5 col-md-offset-2 col-sm-12 col-xs-12">
                        <div class="btn btn-primary">Download PDF</div> 
                        <button type="submit" id="btnrefund"
												class="btn btn-primary">Refund</button>
                    
                       
                      </div>
                      </div>
                -->
                
                
                  
         </form:form>
               </div><!--Tab div end-->
						<!--Tab div end-->
					</div>
				
			</div>

		</div>
		<!--white wrapper-->
	</div>
	<!-- container-fluid end-->
</div>
<!--Page-wrapper-->

<script>
	$(function() {
		$("#fromSearchDate1").datepicker(
				{
					defaultDate : "+1w",
					changeMonth : true,
					changeYear : true,
					maxDate : new Date(),
					numberOfMonths : 1,
					dateFormat : "dd/mm/yy",
					showOn : "button",
					buttonImage : "resources/core/images/manage_events.png",
					buttonImageOnly : true,
					buttonText : "Select date",
					onClose : function(selectedDate) {
						$("#toSearchDate1").datepicker("option", "minDate",
								selectedDate);
					},
					onSelect : function(selectedDate) {
						var fromDate = $('#fromSearchDate1').val();
						var toDate = $('#toSearchDate1').val();
					}
				});
		$("#toSearchDate1").datepicker(
				{
					defaultDate : "+1w",
					changeMonth : true,
					changeYear : true,
					maxDate : new Date(),
					numberOfMonths : 1,
					dateFormat : "dd/mm/yy",
					showOn : "button",
					buttonImage : "resources/core/images/manage_events.png",
					buttonImageOnly : true,
					buttonText : "Select date",
					onClose : function(selectedDate) {
						$("#fromSearchDate1").datepicker("option", "maxDate",
								selectedDate);
					},
					onSelect : function(selectedDate) {
						var fromDate = $('#fromSearchDate1').val();
						var toDate = $('#toSearchDate1').val();
					}
				});
	});

	function convertToSqlFormat(dateStr) {
		var parts = dateStr.split("-");
		return new Date(parts[2], parts[1] - 1, parts[0]);
	}

	 $('#searchTransactions').click(function () {
	     	
     	 $('#TransactionForm').attr('action',"webSearch");  
        var fromSearchDate1 = $("#fromSearchDate1").val();
     	var toSearchDate1 = $("#toSearchDate1").val();
     	var user_id = $( '#user_id :selected').val();
     	var channel = $( '#channel :selected').val();
     	var trans_id = $("#trans_id").val();
    	var reference = $("#reference").val();
     	var particular = $("#particular").val();
     	var checked = []
     	$("input[name='status[]']:checked").each(function ()
     	{
     	    checked.push("'"+$(this).val()+"'");
     	});
   
     	blockDiv('all');
     		$.ajax({
     			type : "POST",
     			url : "${home}webSearch",
     			dataType: 'html',
     			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
     			data: "startDate="+fromSearchDate1+"&endDate="+toSearchDate1+"&channel="+channel+
     			"&userID="+user_id + "&pgPartnerTransId="+trans_id +"&reference=" +reference+"&particular=" +particular+
        			"&status="+checked,
						
        			success : function(data) {
        				console.log("SUCCESS:", data);
        				$("#searchResponseAjax").html(data);
        				unblockDiv('all');
        			},
        			error : function(e) {
        				console.log("ERROR: ", e);
        				unblockDiv('all');
        			}
     			       
     		 });
     		
     });
	 $('#btnrefund').click(function () {
	     	
     	 $('#TransactionForm').attr('action',"webRefund");  
        var amt = $("#amt").val();
     	var dateAndtime = $("#dateAndtime").val();
     	var channel = $( '#chan').val();
     	var trans_id = $("#transaction_id").val();
    	var reference = $("#ref").val();
     	var status = $("#status").val();
     	var comments = $("#comments").val();
     	var email = $("#email").val();
     	var mobile = $("#mobile").val();
     	var coordinates = $("#coordinates").val();
     	
   
     	
     		$.ajax({
     			 type : "POST",
   	          'async': false,
   	          url :"<%= "/"+DynamicPaymentConstant.SITE_URL+"/webRefund" %>",
   	          timeout : 100000,
   	          dataType: 'json',
   	          data: {'pgPartnerTransId':trans_id, "amount":amt, "date_and_time":dateAndtime, "channel":channel, "reference":reference, "status":status, "refundReason":comments, "email":email, "mobile":mobile, "latitude":coordinates},
   	       success : function(data) { 
            alert(data['msg']);
          
           },
         error : function(data) {
            },
           });
     		
     });

   
	 $('#transaction_id').change(function () {
	 
     	 $('#TransactionForm').attr('action',"Transaction");  
      	var trans_id = $("#transaction_id").val();
    	
     	//blockDiv('all');
   	 $.ajax({
        	          type : "POST",
        	          'async': false,
        	          url :"<%= "/"+DynamicPaymentConstant.SITE_URL+"/Transaction" %>",
        	          timeout : 100000,
        	          dataType: 'json',
        	          data: {'pgPartnerTransId':trans_id},
        	           success : function(data) { 
        	        	      	                  
        	                  $("#amt").val(data['mcTransAmount'])
        	                   $("#dateAndtime").val(data['TransactionDate'])
        	                   $("#chan").val(data['channel'])
        	                   $("#status").val(data['status'])
        	                    $("#coordinates").val(data['latlong'])
        	                   
        	                  
        	              },
        	            error : function(data) {
        	               },
        	              });
     		
     });
        	
</script>
<script>
	jQuery(document).ready(
			function($) {
				 $.fn.dataTable.moment('DD-MM-YYYY HH:mm:ss');
				
				$('#reportTable').DataTable({
					"info" : false,
					"searching" : false,
					"order": [[ 5, "desc" ]]
				});
				$("#NewPaginationContainer").append($(".dataTables_paginate"));

				var d = new Date(), date = (d.getUTCFullYear()) + '/'
						+ (d.getUTCMonth() + 1) + '/' + (d.getUTCDate());

				 $('.btnCancel').click(function () {
					 
					var check = confirm("Are you sure you want to cancel this transaction ? Please select okay to continue!");
					if(check){
						var transaction_id = $(this).closest("tr").find("td:eq(1)").text()
						var transaction_amt = $(this).closest("tr").find("td:eq(2)").text();
						$("div#divLoading").addClass('show');
						//blockDiv("all","1000","1000"); 
						$.ajax({
				     			type : "GET",
				     			'async': false,
				     			
				     			url :"<%= "/"+DynamicPaymentConstant.SITE_URL+"/webCancellation" %>",
				     			timeout : 100000,
			        	          dataType: 'json',
				     			  data: {'pgPartnerTransId':transaction_id, 'amount':transaction_amt},
										
				     			 success : function(data) { 
			        	        	     alert(data['msg']);
			        	        		 //unblockDiv("all");
				        	        	 $("div#divLoading").removeClass('show');  
				        	        	 $("div#divLoading").addClass('hide');
			        	              },
			        	           	  error : function(data) {
			        	            	//unblockDiv("all");
			        	            	 $("div#divLoading").removeClass('show');  
				        	        	 $("div#divLoading").addClass('hide');
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
	if($('.blockUI.blockMsg')!=null){
		$('.blockUI.blockMsg').css("display","block");
	}
}

function unblockDiv(element) {
	$('#'+element).unblock();
}
</script>

</body>
<%@ include file="fragments/FormOperations.jsp"%>
<%@ include file="fragments/backendFooter.jsp"%>
</html>
