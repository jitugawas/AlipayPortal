<%@ include file="fragments/backendHeader.jsp"%>
<%@ include file="fragments/backendMenu.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.payitnz.config.DynamicPaymentConstant"%>
<style>
.ui-datepicker-trigger {
	margin-bottom: 20px;
	cursor: pointer;
}
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
margin: initial;
margin-bottom:20px;
}

table.dataTable tfoot th, table.dataTable tfoot td {
    padding: 10px 18px 6px 10px;
}
.table-responsive {
    overflow-x: visible;
}
</style>
<!-- Page Content -->

<div id="page-wrapper">
     <div class="container-fluid">
         <div class="row">
            <div class="col-md-12">
			<div class="right_first_box"><a href="#"><img src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/images/home.png" ></a> / &nbsp;<a href="<%=DynamicPaymentConstant.SITE_BASE_URL %>/settlement">Settlement</a> &nbsp;/ &nbsp;<span  class="breadscrum_active">Upload New File</span></div>
            </div>
          </div>
         
          <h3 class="main_heading">Settlement</h3>
          <form method="POST" name="uploadSettlementForm" id="uploadSettlementForm" action="saveSettlementFile" enctype="multipart/form-data">
           <div class="row">
		       <div class="col-md-12">
		         <h4>Please select file to upload</h4><br>
		         <input type="file" name="settlementFile" id="settlementFile"/><br><br>
		          <!--<c:if test="${!empty message}"/>-->
		         <div style="padding-left:50px;padding-top:5px;padding-bottom:5px;">  ${message} </div>
		         <form:errors path="reconcillationFile" cssClass="error"/>
		         <input type="hidden" name="upload_type" id="upload_type" value= "file"/>
		          <div class="btn btn-primary" id="uploadfile">Upload</div>&nbsp;  
		           <div class="btn btn-primary" id="uploadFtpfile">FTP Upload</div> 
		           <div id="loader" style="display:none;padding:15px;">Processing please wait ..... <img width="65" height="65" src="<%=DynamicPaymentConstant.SITE_BASE_URL %>/resources/core/images/spinner.gif"/></div>			              
		         </div>
			</div>
		</form>
    </div><!-- container-fluid end-->
</div><!--Page-wrapper-->

<style>
        .error {
            color: red;
        }
</style>
    
<!--Page-wrapper-->
<script type="text/javascript">
	$(document).ready(function() {
		var btnClicked = 0;
		var btnFclicked = 0;
		$("#uploadfile").click(function() {
			
			
			var selected = false;
			
			if( document.getElementById("settlementFile").files.length > 0){
				selected = true;
			}
			
			var fileName = $("#settlementFile").val();
			if(fileName == ""){
				
				alert("Please select file to uplaod!");
				$("#settlementFile").focus();
				return false;
				
			}		
			
			var flag = ValidateSingleInput(document.getElementById("settlementFile"));
			
			if(flag && btnClicked == 0 && btnFclicked == 0){
				btnClicked++;
				$("#loader").css("display","block");
				$("#uploadSettlementForm").submit();
			}else{
				alert("Please wait till the current processing is complete.");
			}
		});
		
		$("#uploadFtpfile").click(function() {
						
			if(btnFclicked == 0 && btnClicked==0){
				$("#loader").css("display","block");
				btnFclicked++;
				$("#upload_type").val("ftp");
				$("#uploadSettlementForm").submit();
			}else{
				alert("Please wait till the current processing is complete.");
			}
		});
	});
	
	var _validFileExtensions = ["csv,txt"];    
	function ValidateSingleInput(oInput) {
		
	    if (oInput.type == "file") {
	        var sFileName = oInput.value;
	        
	        var extension = sFileName.split('.').pop();
	         if (sFileName.length > 0) {
	            var blnValid = false;
	           /* for (var j = 0; j < _validFileExtensions.length; j++) {
	                var sCurExtension = _validFileExtensions[j];
	               
	                alert(extension.toLowerCase()+" >> "+ sCurExtension.toLowerCase());
	                if (extension.toLowerCase() == sCurExtension.toLowerCase()){ //sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
	                    blnValid = true;
	                    break;
	                }
	            }*/
	            
	             //alert(extension.toLowerCase()+" >> "+ sCurExtension.toLowerCase());
	                if (extension.toLowerCase() == "txt" || extension.toLowerCase() == "csv" ){ //sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
	                    blnValid = true;
	                    return true;
	                }
	             
	            if (!blnValid) {
	                alert("Sorry, " + sFileName + " is invalid, allowed extensions are: " + _validFileExtensions.join(", "));
	                oInput.value = "";
	                return false;
	            }
	        }
	    }
	    return true;
	}
</script>

<script>

function convertToSqlFormat(dateStr) {
    var parts = dateStr.split("-");
    return new Date(parts[2], parts[1] - 1, parts[0]);
}


</script>


<%@ include file="fragments/backendFooter.jsp"%>