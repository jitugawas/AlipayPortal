<%@ include file="fragments/backendHeader.jsp"%>
<%@ include file="fragments/backendMenu.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.payitnz.config.DynamicPaymentConstant"%>
   <!-- Page Content -->
      <div id="page-wrapper">
          <div class="container-fluid">
              <div class="row">
                 <div class="col-md-12">
  				  <div class="right_first_box"><a href="#"><img src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/images/home.png" ></a> / &nbsp;<span class="breadscrum_active">Settlement</span></div>
                 </div>
               </div>
              
               <h3 class="main_heading">Settlement</h3>
               
             <div class="row">
		            <div class="col-md-12">
		            <div class="row">
		                <div class="col-lg-12 col-md-12">
		                <form name="settlementForm" id="settlementForm" action="settlementPost" method="POST">
		                <div class="row">
		                     <div class="col-lg-3 col-md-6 col-sm-12">
		                       <lable class="calender_lable">Start Date</lable>
		                       <input type="text" class="calender_inp" name="start_date" id="start_date" placeholder="22/1/2016">
		                      <!--  <div class="calender_img"><img src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/images/manage_events.png"></div> -->
		                   </div>
		                    
		                    <div class="col-lg-3 col-md-6 col-sm-12">
		                       <lable class="calender_lable">End Date</lable>
		                       <input type="text" class="calender_inp" name="end_date" id="end_date" placeholder="22/1/2016">
		                      <!--  <div class="calender_img"><img src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/images/manage_events.png"></div> -->
		                   </div>
		                   
		                    <div class="col-lg-4 col-md-4 col-xs-12">
		                      <a href="#" id="searchBtn" onClick="submitForm();"><div class="btn btn-primary settle_btn">Search</div></a>
		                    </div>
		                    
		                      <div class="col-lg-4 col-md-4 col-xs-12">
		                      <a href="uploadSettlement"><div class="btn btn-primary settle_btn">Upload File</div></a>
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
		              
		              <div class="row">
		                <div class="col-md-12">
		               
		                <div class="table-responsive">
		                       <table class="table table-bordered" id="settlementTable">
		                            <thead>
		                            <tr>
		                            <th>Sr No</th>
		                            <th>File Name</th>
		                            <th>Transaction Records</th>
		                            <th>Credit File</th>
		                            <th>Uploaded Date</th>		                           
		                            <th colspan="3">Action</th>
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
									<td>${settlement.fileName}</td>
									<td>${settlement.transactionCount}</td>
									<td>${settlement.creditFile}</td>
									<td>${settlement.uploadedDate}</td>									
									 <td colspan="3"><a href="settledTransactions?id=${settlement.id}">View</a> |
									  <c:if test="${settlement.settlementStatus == 0}">
									   <a href="validateSettlement?id=${settlement.id}">validate</a>
									</c:if>	
									<c:if test="${settlement.settlementStatus == 1}">
									    <a href="validateSettlement?id=${settlement.id}">Re validate</a>
									</c:if>		
									<c:if test="${settlement.settlementStatus == 2}">
									   Errors
									</c:if>	 | <a href="javascript:void(0);" onClick="confirmDelete(${settlement.id});">Delete</a></td>
									
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
		                
		              <!-- <div class="row">
		                <div class="col-md-6 ">
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
		                  </div> -->
		                
		                
		                 </div>
		                 
		                 </div>
		                  
		             </div>
		
		   </div>
                 </div><!-- container-fluid end-->
             </div><!--Page-wrapper-->
             
        <br class="clearfix">
        <!-- /#page-wrapper -->
        
        
<script type="text/javascript">


$(document).ready(function() {
	
	
	$("#start_date" ).datepicker({		
			
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
		
		
		$("#viewDetails").click(function(){
			if($("#selectDate").val() == ""){
				alert("Please select date before you continue!");
				return false;
			}else{
				$("#viewForm").submit();
			}
		});
		
		$("#searchBtn").click(function(){
		
			if($("#start_date").val() == "" && $("#end_date").val() == ""){
				alert("Please select start date and enda date before you continue!!");
				return false;
			
			}
			
			if($("#start_date").val() != "" && $("#end_date").val() != ""){

				var startDateArr = $("#start_date").val().split("-");
				var startDate  = startDateArr[0]+startDateArr[1]+startDateArr[2];
				
				var endDateArr = $("#end_date").val().split("-");
				var endDate  = endDateArr[0]+endDateArr[1]+endDateArr[2];
			
				if(startDate > endDate ){
					alert("Start date should be less then end date!");
					$("#start_date").val("");
					$("#end_date").val("");
					$("#start_date").focus();
					return false;
				}
			
			}
			
			$("#settlementForm").submit();			
			
		});
		
		$('#settlementTable').DataTable({"info":false,"searching":false});
	});

function convertToSqlFormat(dateStr) {
    var parts = dateStr.split("-");
    return new Date(parts[2], parts[1] - 1, parts[0]);
}

function submitForm(){
	//document.settlementForm.submit();
}

function confirmDelete(id){
	var prompt =confirm("Are you sure you want to delete this record frm the system?. Please select okay to continue.");
	if(prompt){
		document.location.href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/deleteSettlement?id="+id;
	}
}
</script>
        
        
<%@ include file="fragments/backendFooter.jsp"%>        