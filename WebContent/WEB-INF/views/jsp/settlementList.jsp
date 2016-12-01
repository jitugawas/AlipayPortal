<%@ include file="fragments/backendHeader.jsp"%>
<%@ include file="fragments/backendMenu.jsp"%>
<%@ page import="com.payitnz.config.DynamicPaymentConstant"%>
<style>
.ui-datepicker-trigger {
	margin-bottom: 20px;
	cursor: pointer;
}
</style>
<!-- Page Content -->
 <div id="page-wrapper">
          <div class="container-fluid">
              <div class="row">
                 <div class="col-md-12">
    <div class="right_first_box"><a href="settlement"><img src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/images/home.png" ></a> / &nbsp;<span class="breadscrum_active">Settlement</span></div>
                 </div>
               </div>
              
               <h3 class="main_heading">Settlement Transaction List</h3>
               
               <div class="row">
         <div class="col-md-12">
            <div class="row">
            	<form name="viewForm" id="viewForm" action="settledTransactions" method="POST">
	                <div class="col-lg-12 col-md-12">
	              	  <!-- <div class="row">
	                 <div class="col-lg-12 col-md-12 col-sm-12">
	                 <lable style="margin-bottom:15px;float:left;">Select Date to View File Transactions for that Date</lable><br>
	                </div>
	                </div>-->
	                <div class="row">
	                <div class="col-lg-3 col-md-6 col-sm-12">
		                       <lable class="calender_lable">Start Date</lable>
		                       <input type="text" class="calender_inp" name="start_date" id="start_date" placeholder="22/1/2016">
		                        <!-- <div class="calender_img"><img src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/images/manage_events.png"></div> -->
		                 </div>
		                    
		                    <div class="col-lg-3 col-md-6 col-sm-12">
		                       <lable class="calender_lable">End Date</lable>
		                       <input type="text" class="calender_inp" name="end_date" id="end_date" placeholder="22/1/2016">
		                       <!-- <div class="calender_img"><img src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/images/manage_events.png"></div> -->
		                   </div>
		                   
		                    <div class="col-lg-4 col-md-4 col-xs-12">
		                      <a href="javascript:void(0);" id="searchBtn" onClick="submitForm();"><div class="btn btn-primary settle_btn">View</div></a>
		                    </div>
		                    
	                </div>
	                     <div class="col-lg-4 col-md-4 col-xs-12">
		                      <a href="settlement"><div class="btn btn-primary settle_btn">Back</div></a>
		                    </div>
	                </div>
                </form>
                
                <br class="clear">
                
                <div class="row">
                <div class="col-lg-12 col-md-12 col-sm-12">
                	
                	<div class="row">         
                    <div style="padding-left:50px;padding-top:5px;padding-bottom:5px;"> ${message} </div>        
              		 </div>
                     
					
							
                </div>
                </div>
                
                <div class="row">
                	<div style="padding-left:20px;padding-top:5px;padding-bottom:5px;"> ${selectedDate}</div>
                </div>
              
                <div class="row">         
                	<div style="padding-left:20px;padding-top:5px;padding-bottom:5px;"> ${totalResults} </div>        
              	 </div>
                     
               <br class="clear">
                
 				<div class="row tably_view collapse" style="display:block;">
                <div class="col-md-12">
               
                <div class="table-responsive ">
                       <table class="table table-bordered" id="settlementTable">
                            <thead>
                            <tr>
	                            <th>Sr. No</th>
	                            <th>Partner transaction id</th>
	                            <th>Transaction id</th>	                           
	                            <th>Amount</th>
	                            <th>Rmb Amount</th>
	                            <th>Fee</th>
	                            <th>Settlement</th>
	                            <th>Rmb Settlement</th>
	                            <th>Currency</th>
	                            <th>Payment time</th>
	                            <th>Settlement time</th>
	                            <th>Rate</th>
	                            <th>Type</th>
	                            <th>Transaction Status</th>
	                            <th>Remark</th>	  
	                            <th>Settlement Status </th>                          
                            </tr>
                            </thead>
                            <tbody>
                            <c:set var="count" value="0" scope="page" />
                            <c:choose>
							<c:when test="${settlementList.size() > 0 }">
							<c:forEach items="${settlementList}" var="settlement">
							<c:set var="count" value="${count + 1}" scope="page"/>
								<tr>
									<td>${count}</td>
									<td><!-- <a href="getTransactionDetailsById?id=${settlement.partnerTransactionId}"> -->${settlement.partnerTransactionId}</td>
									<td>${settlement.transactionId}</td>
									<td>${settlement.amount}</td>
									<td>${settlement.rmbAmount}</td>
									<td>${settlement.fee}</td>
									<td>${settlement.settlement}</td>
									<td>${settlement.rmbSettlement}</td>
									<td>${settlement.currency}</td>
									<td>${settlement.paymentTime}</td>
									<td>${settlement.settlementTime}</td>
									<td>${settlement.rate}</td>									
									<td>${settlement.type}</td>
									<td>${settlement.status}</td>
									<td>${settlement.remark}</td>
									<td>									
									<c:if test="${settlement.settlementStatus == 0}">
									   Not validated
									</c:if>	
									<c:if test="${settlement.settlementStatus == 1}">
									   Validated
									</c:if>		
									<c:if test="${settlement.settlementStatus == 2}">
									   Errors
									</c:if>									
									</td>
									
								</tr>
							</c:forEach>
							</c:when>
							<c:otherwise>
							<tr>					
							<td colspan="10" style="text-align:center;">No results found</td>							
							</tr>
							</c:otherwise>
							</c:choose>                            
                            
                            </tbody>
                         </table>
                        </div>
                </div>
                
                </div>
                
                <div class="row tably_view collapse">
                <div class="col-md-6">
                     <nav>
                      <ul class="pagination">
                        <li><a aria-label="Previous" href="#"><span aria-hidden="true">«</span></a></li>
                        <li class="active"><a href="#">1</a></li>
                        <li><a href="#">2</a></li>
                        <li><a href="#">3</a></li>
                        <li><a href="#">4</a></li>
                        <li><a aria-label="Next" href="#"><span aria-hidden="true">»</span></a></li>
                      </ul>
                    </nav>
                </div>
                  </div>
                
                
                
              </div>
                 </div>
                  
             </div>

   </div>
                 </div>
                 <!-- container-fluid end-->
                 
<!--Page-wrapper-->

<script type="text/javascript">


$(document).ready(function() {
	
	
	$("#start_date" ).datepicker({
		/*defaultDate : "+1w",
		changeMonth : true,
		changeYear : true,
		maxDate: new Date(),
		numberOfMonths : 1,
		dateFormat : "dd-mm-yy"
			*/
			
		defaultDate : "+1w",
		changeMonth : true,
		changeYear : true,
		maxDate : new Date(),
		numberOfMonths : 1,
		dateFormat : "dd-mm-yy",
		showOn : "button",
		buttonImage : "<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/images/manage_events.png",
		buttonImageOnly : true,
		buttonText : "Select date"
	});
	
	$("#end_date" ).datepicker({
		/*defaultDate : "+1w",
		changeMonth : true,
		changeYear : true,
		maxDate: new Date(),
		numberOfMonths : 1,
		dateFormat : "dd-mm-yy"
			*/
			
		defaultDate : "+1w",
		changeMonth : true,
		changeYear : true,
		maxDate : new Date(),
		numberOfMonths : 1,
		dateFormat : "dd-mm-yy",
		showOn : "button",
		buttonImage : "<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/images/manage_events.png",
		buttonImageOnly : true,
		buttonText : "Select date"
	});
	
});

	$(document).ready(function() {
		$("#dashboardSearch").click(function() {
			$("#dashboardTransactionForm").validationEngine('attach', {
				scroll : false
			});
		});
		
		$("#viewDetails").click(function(){
			if($("#selectDate").val() == ""){
				alert("Please select date before you continue!");
				return false;
			}else{
				$("#viewForm").submit();
			}
		});
		
		$('#settlementList').DataTable({"info":false,"searching":true});
	});

function convertToSqlFormat(dateStr) {
    var parts = dateStr.split("-");
    return new Date(parts[2], parts[1] - 1, parts[0]);
}

	
function submitForm(){
	
	if($("#start_date").val() == "" || $("#end_date").val() == ""){
		alert("Please enter start date and end date before you contionue!")
		$("#start_date").focus();
		return false;
	}
	document.viewForm.submit();
}	
	
	


</script>


<%@ include file="fragments/backendFooter.jsp"%>