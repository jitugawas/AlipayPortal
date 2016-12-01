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

		<h3 class="main_heading">Alipay Transactions Refund </h3>

		<div class="row">
			<div class="col-md-12">
				<ul class="nav nav-tabs responsive-tabs">
					<li class="active"><a href="#all" onClick="hideRefund();">Listing</a></li>
					<li><a href="#alipay_online" id="refundDiv" style="display:none;">Refund</a></li>

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
												<option value="F2F">Alipay Offline</option>
												<option value="Online">Alipay Online</option>
											</select>
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
												<table class="table table-bordered" id="refundTable">
													<thead>
														<tr>
															<th>Channel</th>
															<th>Alipay Transaction ID</th>
															<th>Amount</th>
															<th>Reference</th>
															<th>Particulars</th>
															<th>Date/Time</th>
															<th>Status</th>
															<th>Action</th>
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
																		<c:choose>
																		<c:when test="${each.value.status == 'SUCCESS' }">
																		<td><a href="javascript:void(0);" onClick="loadTransaction('${each.value.pgPartnerTransId}');"><div class="btn btn-primary btnRefundFirst">Refund</div></a></td>
																		</c:when>
																		<c:otherwise>
																		<td></td>
																		</c:otherwise>
																		</c:choose>
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
																	<td></td>
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
               <div class="col-lg-2  col-md-4 control_label">Transaction ID</div>
               <div class="col-lg-4 col-md-6">
               <input type="email" class="inp" placeholder="Transaction ID" id="transaction_id" name="transaction_id" disabled="true">
               </div>
               </div>
               
               <div class="row">
               <div class="col-lg-2 col-md-4 control_label">Amount</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Amount" id="amt" name="amt" disabled="true">
               </div>
               <div class="col-lg-2 col-md-4 control_label">Available for Refund</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Amount" id="amt_available" name="amt_available">
               </div>
               </div>
               
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Date/Time</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Date/Time" id="dateAndtime" disabled>
               </div>
               </div>
               
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Status</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Status" id="status" disabled>
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Channel</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Channel" id="chan" disabled>
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Reference</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Reference" id="ref" disabled>
               </div>
               </div>
               
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Coordinates</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Coordinates" id="coordinates" disabled>
               </div>
               </div>
                           
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Comments</div>
               <div class="col-lg-4 col-md-6">
               <textarea class="inp" placeholder="Comments" id="comments" rows="5" cols="5"></textarea>
               <input type="hidden" name="total_fee" id="total_fee" value=""/>
               </div>
               </div>
               
               <!-- <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Email</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Email" id="email" readonly>
               </div>
               </div>
               <div class="row">
               <div class="col-lg-2  col-md-4 control_label">Mobile</div>
               <div class="col-lg-4 col-md-6">
               <input type="text" class="inp" placeholder="Mobile" id="mobile">
               </div>
               </div> -->
               
               </div>
             
               <div class="row">
              &nbsp;
               </div>  
                                    
               <div class="row">
               <div class="col-lg-4  col-md-4 control_label"><strong>Transaction History</strong></div>
               </div>
               
                <div class="row">
	               <div class="table-responsive" style="width:95%;margin:0 auto;">
						<table class="table table-bordered listTable">
							<thead>
								<tr>
									<th>Channel</th>
									<th>Alipay Transaction ID</th>
									<th>Amount</th>
									<th>Reference</th>
									<th>Particulars</th>
									<th>Date/Time</th>
									<th>Status</th>															
								</tr>
							</thead>
							<tbody id="tbodyid">
							
							</tbody>
						</table>
						
						<div id="refMsg"><h5>This transaction is not eligible for refund.</h5></div>
					</div>
				</div>
              
               </div>
                </div>
               
                <div class="row buttons" id="refundBtnRow" style="display:none;">
                     <div class="col-md-9 col-md-offset-2 col-sm-12 col-xs-12">
                       <!-- <div class="btn btn-primary">Download PDF</div> -->
                        <button type="button" id="btnrefund"
												class="btn btn-primary">Refund</button>
                    <button type="button" id="btnCancel"
												class="btn btn-primary" onClick="defaultListing();">Cancel</button>
                       <div id="loader" style="display:none;padding:15px;float:left;margin-left:225px;margin-top:-55px;">Processing refund ..... <img width="65" height="65" src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/images/spinner.gif"/></div>	
                      </div>
                      </div>
                
                
                
                  
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
	     	
     	 $('#TransactionForm').attr('action',"refundSearch");  
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
     	    checked.push(parseInt($(this).val()));
     	});
   
     	blockDiv('all');
     		$.ajax({
     			type : "POST",
     			url : "${home}refundSearch",
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
	 $('.btnRefundFirst').click(function () {
		 
			var check = confirm("Are you sure you want to cancel this transaction ? Please select okay to continue!");
			if(check){
				var transaction_id = $(this).closest("tr").find("td:eq(1)").text()
				var transaction_amt = $(this).closest("tr").find("td:eq(2)").text();
			
				$("div#divLoading").addClass('show');
				//blockDiv("all","1000","1000"); 
				$.ajax({
		     			type : "GET",
		     			'async': false,
		     			
		     			url :"<%= "/"+DynamicPaymentConstant.SITE_URL+"/" %>",
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
	 $('#btnrefund').click(function () {
	     	
		 
		 var conf = confirm("Are you sure you want to proceed with refund ? Please select yes to continue!");

		 if(conf){
			 
				 $("div#divLoading").addClass('show');
				 $('#TransactionForm').attr('action',"webRefund");  
		        var amt = $("#amt_available").val();
		     	var dateAndtime = $("#dateAndtime").val();
		     	var channel = $( '#chan').val();
		     	var trans_id = $("#transaction_id").val();
		    	var reference = $("#ref").val();
		     	var status = $("#status").val();
		     	var comments = $("#comments").val();
		     	var email = $("#email").val();
		     	var mobile = $("#mobile").val();
		     	var coordinates = $("#coordinates").val();		     	
		     	$('#btnrefund').attr("disabled","true");
		     	document.getElementById("btnrefund").disabled = true;
		     	
	     		$.ajax({
	     			 type : "POST",
	   	          'async': false,
	   	          url :"<%= "/"+DynamicPaymentConstant.SITE_URL+"/webRefund" %>",
	   	          timeout : 100000,
	   	          dataType: 'json',
	   	          data: {'pgPartnerTransId':trans_id, "amount":amt, "date_and_time":dateAndtime, "channel":channel, "reference":reference, "status":status, "refundReason":comments, "email":email, "mobile":mobile, "latitude":coordinates,"total_fee":$("#total_fee").val()},
	   	       		success : function(data) { 
	           		 alert(data['msg']);
	           		$("#loader").css("display","none");
	           		$('#btnrefund').attr("disabled","false");
	           		document.getElementById("btnrefund").disabled = false;
		           	 $("div#divLoading").removeClass('show');  
		        	 $("div#divLoading").addClass('hide');
	           	},
	         	error : function(data) {
	        	 	 alert("Something went wrong during the refund process!");
	        		 $("#loader").css("display","none");
	           		$('#btnrefund').attr("disabled","false");
	           		document.getElementById("btnrefund").disabled = false;
	           		 unblockDiv("all");
	           		 $("div#divLoading").removeClass('show');  
    	        	 $("div#divLoading").addClass('hide');
	            },
	         });
		 }
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
        	            	alert("Invalid transaction id. Please check!!");        	            	
       	               },
       	       });
     		});
	 
	 
	 
           $('#amt_available').change(function () {
        	               		
        	
        	   if($("#amt_available").val() > 0)
        		   {
        		   if($("#amt_available").val() > $("#amt").val())
        			   {
        			   alert("Please enter valid amount for refund!");
               			alert(data['mcTransAmount']);
               			$("#amt_available").val($("#amt").val());
               			return false;
        			   }
        		   
        		   else
        			   {
        			   
        			   }
        		   }
        	   else
        		   {
        		   alert("Please enter valid amount for refund!");
          			alert(data['mcTransAmount']);
          			$("#amt_available").val($("#amt").val());
          			return false;
        		   }
      	 	 });

	  
	  function loadTransaction(transactionId){
		    //$('#TransactionForm').attr('action',"Transaction");  
		    var trans_id = transactionId;//$("#transaction_id").val();
		      
		         $("div#divLoading").addClass('show');
		         $.ajax({
		                    type : "POST",
		                    'async': false,
		                    url :"<%= "/"+DynamicPaymentConstant.SITE_URL+"/Transaction" %>",
		                    timeout : 100000,
		                    dataType: 'json',
		                    data: {'pgPartnerTransId':trans_id},
		                     success : function(data) { 
		                 
		                    		  $("#transaction_id").val(transactionId);         
		                              $("#amt").val(data['0']['mcTransAmount']);
		                              $("#dateAndtime").val(data['0']['TransactionDate']);
		                              $("#chan").val(data['0']['channel']);
		                              $("#status").val(data['0']['status']);
		                              $("#coordinates").val(data['0']['latlong']);
		                              $("#total_fee").val(data['0']['mcTransAmount']);
		                              $("#amt_available").val(data['0']['remainingAmt']);
		                             
		                              $("div#divLoading").removeClass('show');  
		                   			  $("div#divLoading").addClass('hide');
		                              $( "#refundDiv" ).css( "display","block");
		                              $( "#refundDiv" ).trigger( "click" ); 
		                              $( "#refundBtnRow" ).css( "display","block"); 
		                              if(data['0']['remainingAmt'] == 0)
		                            	 {
		                            	  $("#amt_available").attr("disabled", "disabled"); 
		                            	 $("#btnrefund").addClass('hide');
		                            	 $("#refMsg").addClass('show');
		                            	 
		                            	 }
		                              buildTable(data,transactionId);       
		                        },
		                      error : function(data) {
		                        unblockDiv("all");
		                        $("div#divLoading").removeClass('show');  
		                     $("div#divLoading").addClass('hide');
		                      },
		                });
		         
		   }
	  
	  
	  function buildTable(data,transactionId){
		  var tableData = "";
          if(Object.keys(data).length > 0)
          {
          	for(i=1;i<Object.keys(data).length;i++){
        		 tableData += '<tr>'+
 
	           '<td>'+data[i]['channel']+'</td>'+
	           
				'<td><a target="_blank" href="<%= DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SITE_URL+"/getTransactionDetailsById" %>?id='+data[i]['id']+'" >'+transactionId+' </a> \</td>'+

				
				
	         //  '<td>'+transactionId+'</td>'+
	           '<td>'+data[i]['mcTransAmount']+'</td>'+
	           '<td>'+data[i]['mcReference']+'</td>'+
	           '<td>'+data[i]['mcParticular']+'</td>'+
	           
	           '<td>'+data[i]['TransactionDate']+'</td>'+
	           '<td>'+data[i]['status']+'</td>'+
	           '</tr>';
        	  }
         }
          else
         {
        	tableData +='<tbody><tr><td colspan="7">No history available</td></tr></tbody>';
         }
	
         $("#tbodyid").empty(); 
         $('.listTable').append(tableData);
	  } 


</script>
<script>
	jQuery(document).ready(
			function($) {
				 $.fn.dataTable.moment('DD-MM-YYYY HH:mm:ss');
				 
				 $("#refMsg").addClass('hide');
				$('#refundTable').DataTable({
					"info" : false,
					"searching" : false,
					"order": [[ 5, "desc" ]]
				});
				$("#NewPaginationContainer").append($(".dataTables_paginate"));

				var d = new Date(), date = (d.getUTCFullYear()) + '/'
						+ (d.getUTCMonth() + 1) + '/' + (d.getUTCDate());

				 $('.btnCancel').click(function () {
					var transaction_id = $(this).closest("tr").find("td:eq(1)").text()
					var transaction_amt = $(this).closest("tr").find("td:eq(2)").text();
					$.ajax({
		     			type : "GET",
		     			'async': false,
		     			
		     			url :"<%= "/"+DynamicPaymentConstant.SITE_URL+"/webCancellation" %>",
		     			timeout : 100000,
	        	          dataType: 'json',
		     			  data: {'pgPartnerTransId':transaction_id, 'amount':transaction_amt},
								
		     			 success : function(data) { 
	        	        	   alert(data['msg']);
	        	                 
	        	              },
	        	            error : function(data) {
	        	               },
	        	              });
					
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

function defaultListing(){
	document.location.href="<%=DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SITE_URL+"/refundListing" %>";
}

function hideRefund(){
	 $( "#refundDiv" ).css( "display","none");    
     $( "#refundBtnRow" ).css( "display","none"); 
}
</script>

</body>
<%@ include file="fragments/FormOperations.jsp"%>
<%@ include file="fragments/backendFooter.jsp"%>
</html>
