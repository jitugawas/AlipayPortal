<%@ include file="fragments/backendHeader.jsp"%>
<%@ include file="fragments/backendMenu.jsp"%>
<%@ page import="com.payitnz.config.DynamicPaymentConstant" %>
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
	<div class="container-fluid" id="homePageDiv">
		<div class="row">
			<div class="col-md-12">
				<div class="right_first_box">
					<a href="#"><img src="resources/core/images/home.png"></a> / &nbsp;<span
						class="breadscrum_active">Dashboard</span>
				</div>
			</div>
		</div>

		<h3 class="main_heading">Dashboard</h3>
	
		<div id="graphSection">
		<div class="row">
			<div class="col-md-6" id="ch1">
				<div id="chart1"></div>
				<div class="text-center">
					<b>Dollar value of Transactions in last <c:out value="${DisplayDays}"/> 
					<c:choose>
					<c:when test="${DisplayDays == 1}">
					day
					</c:when>
					<c:otherwise>
					days
					</c:otherwise>
					</c:choose>
					</b>
				</div>
			</div>
			<div class="col-md-6" id="ch2">
				<div id="chart"></div>
				<div class="text-center">
					<b>Number of Transactions in last <c:out value="${DisplayDays}"/>
					<c:choose>
					<c:when test="${DisplayDays == 1}">
					day
					</c:when>
					<c:otherwise>
					days
					</c:otherwise>
					</c:choose>
					</b>
				</div>
			</div>
		</div>
		</div>
		
		<!--div class="row">
			<div class="col-md-12">
			<div id="chart"></div>
				<img src="resources/core/images/graphs.jpg" width="100%"
					style="border: 1px solid #ccc;">
			</div>
		</div-->
		
		<br class="clear"> <br>
		<form  id="dashboardTransactionForm" method="POST">
		<div class="row">
			<div class="col-lg-4 col-md-5 col-sm-9">
				<lable class="calender_lable">Start Date</lable>
				<input type="text" id="from" name="from" class="calender_inp validate[required]"
					placeholder="dd-mm-yyyy" readOnly>
			</div>

			<div class="col-lg-4 col-md-5 col-sm-9">
				<lable class="calender_lable">End Date</lable>
				<input type="text" id="to" name="to" class="calender_inp  validate[required]"
					placeholder="dd-mm-yyyy" readOnly>
			</div>
			<div class="col-lg-2 col-md-2 col-sm-12 col-xs-12">
			<button type="submit" id="dashboardSearch" class="btn btn-primary save_btn">Search</button>
			</div>
		</div>
		</form>
		<br class="clear">
		<div id="dashboardTable">
		<div class="row">
			<div class="col-md-12">
				<div class="tabl_heading">Transactions in last <c:out value="${DisplayDays}"/>
					<c:choose>
					<c:when test="${DisplayDays == 1}">
					day
					</c:when>
					<c:otherwise>
					days
					</c:otherwise>
					</c:choose>
				</div>
				<div class="table-responsive">
					<table class="table table-bordered" id="transTable">
						<thead>
							<tr>
								<th>Channel</th>
								<th style="width:15%;">Transaction No</th>
								<th>Dollar Value</th>
								<th>Average</th>
								<th>Largest</th>
								<th>Smallest</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${TransactionDetails}" var="tDetails">
								<tr>
									<td>${tDetails.value.payment_method}</td>
									<td>${tDetails.value.trans_num}</td>
									<td>$${tDetails.value.sum_amount}</td>
									<td>$${tDetails.value.avg_amount}</td>
									<td>$${tDetails.value.max_amount}</td>
									<td>$${tDetails.value.min_amount}</td>
								</tr>
								
							</c:forEach>

						</tbody>
						<tfoot>
						<tr style="background-color:#515763;color:#fff;">
								<td><b>Total</b></td>
								<td><b>${TotalTransVolume}</b></td>
								<td><b>$${TotalTransValue}</b></td>
								<td></td>
								<td></td>
								<td></td>
						</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
		</div>
		<div class="row">
			<div class="col-md-2 col-sm-5 col-xs-6">
				<a href="downloadDashboardCSV"><div class="btn btn-primary save_btn">Download CSV</div></a>
			</div>
			<div class="col-md-2 col-sm-5 col-xs-6">
				<a href="downloadDashboardPDF"><div class="btn btn-primary save_btn">Download PDF</div></a>
			</div>
		</div>
		<br class="clear">
		<div class="row">
			<div class="col-lg-9 col-md-12">
				<div class="row" id="totalDiv">
					<div class="col-lg-3 col-md-4 col-sm-5">
						<lable class="control_lable">Total Transaction Volume for <c:out value="${DisplayDays}"/>
						<c:choose>
						<c:when test="${DisplayDays == 1}">
						day
						</c:when>
						<c:otherwise>
						days
						</c:otherwise>
						</c:choose>
						</lable>
					</div>
					<div class="col-lg-3 col-md-2 col-sm-7">
						<input type="text" class="inp" value="${TotalTransVolume}"
							disabled readonly>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-5">
						<lable class="control_lable">Total Transaction $ Value for <c:out value="${DisplayDays}"/><c:choose>
						<c:when test="${DisplayDays == 1}">
						day
						</c:when>
						<c:otherwise>
						days
						</c:otherwise>
						</c:choose></lable>
					</div>
					<div class="col-lg-3 col-md-2 col-sm-7">
						<input type="text" class="inp" value="$${TotalTransValue}" disabled
							readonly>
					</div>
				</div>
			</div>
		</div>
		
	</div>
	<!-- container-fluid end-->
</div>
<!--Page-wrapper-->

<!--Page-wrapper-->
<script type="text/javascript">
	$(document).ready(function() {
		$("#dashboardSearch").click(function() {
			$("#dashboardTransactionForm").validationEngine('attach', {
				scroll : false
			});
		});
	});
</script>
<script>
	jQuery(document).ready(function($) {
		$("#dashboardTransactionForm").submit(function(event){
			// Prevent the form from submitting via the browser.
			var status = $("#dashboardTransactionForm").validationEngine('validate'); 
			 if(!status){
				 return false;
			 }
			
			event.preventDefault();

			//search data
			var search = {}
			if($('#from').val()){
				fDate = convertToSqlFormat($("#from").val());
				search["fromDate"] = [fDate.getFullYear(),fDate.getMonth()+1,fDate.getDate()].join('-');
			}else{
					search["fromDate"] = $('#from').val();
			}
			if($("#to").val()){
				tDate =  convertToSqlFormat($("#to").val());
				// to add 1 day use:
				tDate.setDate(tDate.getDate()+1);
				search["toDate"] =  [tDate.getFullYear(),tDate.getMonth()+1,tDate.getDate()].join('-');	
			}else{
				search["toDate"] = $("#to").val();	
			}
			
			blockDiv('homePageDiv');
			
			$.ajax({
				type : "POST",
				url :"<%= "/"+DynamicPaymentConstant.SITE_URL +"/getDashboardTableAjax" %>",
				dataType : "html",
				contentType: "application/json; charset=utf-8",
				data : JSON.stringify(search),
				timeout : 100000,
				success : function(data) {
					console.log("SUCCESS: ", data);
					$('#graphSection').html($('#graphSection' ,data).html());
					$('#dashboardTable').html($('#dashboardTable' ,data).html());
					$('#totalDiv').html($('#totalDiv' ,data).html());
					unblockDiv('homePageDiv');
				},
				error : function(e) {
					console.log("ERROR: ", e);
					unblockDiv('homePageDiv');
				},
				done : function(e) {
					console.log("DONE");
					unblockDiv('homePageDiv');
				}
			});
		});
	$('#transTable').DataTable({"info":false,"searching":false,"paging":false});
	});
</script>



<script>

$.elycharts.templates['pie_basic_1'] = {
		  type: "pie",
		   style : {
			   "background-color" : "#fff"
			  },
		  defaultSeries: {
		  plotProps : {
			   stroke : "white",
			   "stroke-width" : 2,
			   opacity : 0.8
			  },
		    highlight: {
		      newProps: {
		        opacity: 1
		      }
		    },
		    tooltip: {
		      frameProps: {
		        opacity: 0.8
		      }
		    },
		    label: {
		      active: true,
		      props: {
		        fill: "white"
		      }
		    },
		    startAnimation: {
		      active: true,
		      type: "avg"
		    }
		  }
		};



$("#chart").chart({
	  template: "pie_basic_1",
	  values: {
		  serie1 : <c:out value="${GraphTransNumData}"/>
	  },
	  labels: ["Alipay Offline", "Alipay Online"],
	  tooltips: {
	    serie1: <c:out value="${GraphTransNumTooltipData}"  escapeXml="false"/>
	  },
	  defaultSeries: {
	    r: 0,
	    values: [{
	      plotProps: {
	        fill: "#85f7d5"
	      }
	    }, {
	      plotProps: {
	        fill: "#99ccff"
	      }
	    }]
	  },
	});


$.elycharts.templates['line_basic_6'] = {
		  type: "line",
		  margins: [10, 10, 20, 50],
		  style : {
					   "background-color" : "#fff"
					  },
		  defaultSeries: {
		    highlight: {
		      newProps: {
		        r: 8,
		        opacity: 1
		      },
		      overlayProps: {
		        fill: "white",
		        opacity: 0.2
		      }
		    }
		  },
		  series: {
		    serie1: {
		      color: "#85f7d5",
		      tooltip: {
		        frameProps: {
		          stroke: "#85f7d5"
		        }
		      }
		    },
		    serie2: {
		      color: "#99ccff",
		      tooltip: {
		        frameProps: {
		          stroke: "#99ccff"
		        }
		      }
		    },
		    
		  },
		  defaultAxis: {
		    labels: true
		  },
		  axis: {
		    x: {
		      labelsRotate: 360,
		      labelsProps: {
		        font: "12px Verdana"
		      },
		      l: {
				      titleProps: {
				        fill: "black"
				      },
				      titleDistance:45,
				      labels: true
				    }
		    },
		    
		  },
		  features: {
		    grid: {
		      draw: false,
		      forceBorder: true,
		      ny: 5
		    }
		  },
		  barMargins: 10
		};
	

	$("#chart1").chart({
		template : "line_basic_6",
		tooltips : {
			serie1: ["<c:out value="${GraphTransTooltipValue1}"  escapeXml="false"/>", ""],
		    serie2: ["", "<c:out value="${GraphTransTooltipValue2}"  escapeXml="false"/>"]
		},
		values : {
			serie1: [<c:out value="${GraphTransValue1}" />,0],
		    serie2: [0, <c:out value="${GraphTransValue2}" />]
		},
		labels : ["Alipay Offline", "Alipay Online"],
		defaultSeries : {
			type : "bar",
			stacked: true
		},
		axis: {
		    l: {
		      title: "Dollar Value",
		      titleDistance:40,
		    }
		  },
		barMargins : 10
	});
	
	$(window).on("resize", function () {
		var c1 = $('#chart');
		c1.attr('width', jQuery("#ch1").width());
		c1.attr('height', jQuery("#ch1").height());
		var c2 = $('#chart1');
		c2.attr('width', jQuery("#ch2").width());
		c2.attr('height', jQuery("#ch2").height());
		

		$("#chart").chart({
			  template: "pie_basic_1",
			  values: {
				  serie1 : <c:out value="${GraphTransNumData}"/>
			  },
			  labels: ["Alipay Offline", "Alipay Online"],
			  tooltips: {
			    serie1: <c:out value="${GraphTransNumTooltipData}"  escapeXml="false"/>
			  },
			  defaultSeries: {
			    r: 0,
			    values: [{
			      plotProps: {
			        fill: "#85f7d5"
			      }
			    }, {
			      plotProps: {
			        fill: "#99ccff"
			      }
			    }]
			  }
			});
		
		
		$("#chart1").chart({
			template : "line_basic_6",
			tooltips : {
				serie1: ["<c:out value="${GraphTransTooltipValue1}"  escapeXml="false"/>", ""],
			    serie2: ["", "<c:out value="${GraphTransTooltipValue2}"  escapeXml="false"/>"]
			},
			values : {
				serie1: [<c:out value="${GraphTransValue1}" />,0],
			    serie2: [0, <c:out value="${GraphTransValue2}" />]
			},
			labels : ["Alipay Offline", "Alipay Online"],
			defaultSeries : {
				type : "bar",
				stacked: true
			},
			axis: {
			    l: {
			    	title: "Dollar Value",
				    titleDistance:40,
			    }
			  },
			barMargins : 10
		});
	});
</script>

<script>

function convertToSqlFormat(dateStr) {
    var parts = dateStr.split("-");
    return new Date(parts[2], parts[1] - 1, parts[0]);
}
</script>

<script>
	$(function() {
		$("#from").datepicker({
			defaultDate : "+1w",
			changeMonth : true,
			changeYear : true,
			maxDate: new Date(),
			numberOfMonths : 1,
			dateFormat : "dd-mm-yy",
			showOn: "button",
	      	buttonImage: "resources/core/images/manage_events.png",
     	 	buttonImageOnly: true,
	      	buttonText: "Select date",
			onClose : function(selectedDate) {
				$("#to").datepicker("option", "minDate", selectedDate);
			},
			onSelect : function(selectedDate) {
				var fromDate = $('#from').val();
				var toDate = $('#to').val();
			}
		});
		$("#to").datepicker({
			defaultDate : "+1w",
			changeMonth : true,
			changeYear : true,
			maxDate: new Date(),
			numberOfMonths : 1,
			dateFormat : "dd-mm-yy",
			showOn: "button",
	      	buttonImage: "resources/core/images/manage_events.png",
     	 	buttonImageOnly: true,
     	 	buttonText: "Select date",
			onClose : function(selectedDate) {
				$("#from").datepicker("option", "maxDate", selectedDate);
			},
			onSelect : function(selectedDate) {
				var fromDate = $('#from').val();
				var toDate = $('#to').val();
			}
		});
	});
	
	function blockDiv(element,height,width) {
		$('#'+element).block({
			message: '<img src="<%= DynamicPaymentConstant.SERVER_HOST+DynamicPaymentConstant.SITE_URL+"/resources/core/images/loader.gif" %>" />',
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
</script>

<%@ include file="fragments/backendFooter.jsp"%>