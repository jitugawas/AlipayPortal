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
    <div class="right_first_box"><a href="#"><img src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/images/home.png" ></a> / &nbsp;<span class="breadscrum_active">Reconcilation</span></div>
                 </div>
               </div>
              
               <h3 class="main_heading">Reconcilation</h3>
               
               <div class="row">
         <div class="col-md-12">
            <div class="row">
            	<form name="viewForm" id="viewForm" action="reconcile" method="POST">
	                <div class="col-lg-12 col-md-12">
	                <div class="row">
	                <div class="col-lg-12 col-md-12 col-sm-12">
	                 <lable style="margin-bottom:15px;float:left;">Select Date to View File Transactions for that Date</lable><br>
	                </div>
	                </div>
	                <div class="row">
	                <div class="col-lg-5 col-md-5 col-sm-12 col-xs-12">
	                   <input type="text" class="calender_inp space_calendar" placeholder="22-1-2016" id="selectDate" name="selectDate" readonly>
	                  <!-- <div class="calender_img"><img src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/images/manage_events.png"></div>-->
	                  <div class="row" style="float:right;"> <div class="btn btn-primary view_btn" data-toggle="collapse" id="viewDetails" >View</div> 
	                   <a href="uploadFile"><div class="btn btn-primary view_btn">Upload</div></a></div>
	               	</div>
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
                       <table class="table table-bordered" id="reconcileTable">
                            <thead>
                            <tr>
	                            <th>Sr. No</th>
	                            <th>Partner transaction id</th>
	                            <th>Alipay Transaction id</th>
	                            <th>Transaction amount</th>
	                            <th>Charge amount</th>
	                            <th>Currency</th>
	                            <th>Payment time</th>
	                            <th>Transaction type</th>
	                            <th>Remark</th>
	                            <th>Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:set var="count" value="0" scope="page" />
                            <c:choose>
							<c:when test="${reconcileList.size() > 0 }">
							<c:forEach items="${reconcileList}" var="reconcile">
							<c:set var="count" value="${count + 1}" scope="page"/>
								<tr>
									<td>${count}</td>
									<td>${reconcile.partnerTransactionId}</td>
									<td>${reconcile.transactionId}</td>
									<td>${reconcile.transactionAmount}</td>
									<td>${reconcile.chargeAmount}</td>
									<td>${reconcile.currency}</td>
									<td>${reconcile.paymentTime}</td>
									<td>${reconcile.transactionType}</td>
									<td>${reconcile.remark}</td>
									<td>									
									<c:if test="${reconcile.reconcillationStatus == 0}">
									   Not validated
									</c:if>	
									<c:if test="${reconcile.reconcillationStatus == 1}">
									   Validated
									</c:if>		
									<c:if test="${reconcile.reconcillationStatus == 2}">
									   Errors
									</c:if>									
									</td>
									
								</tr>
							</c:forEach>
							</c:when>
							<c:otherwise>
							<tr>					
							<td colspan="10" style="text-align:center;">No records found</td>							
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
	
	
	$("#selectDate" ).datepicker({
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
		
		$('#reconcileTable').DataTable({"info":false,"searching":false});
	});

function convertToSqlFormat(dateStr) {
    var parts = dateStr.split("-");
    return new Date(parts[2], parts[1] - 1, parts[0]);
}

	
	
	


</script>


<%@ include file="fragments/backendFooter.jsp"%>