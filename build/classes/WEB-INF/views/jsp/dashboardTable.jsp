<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div>
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
	  }
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
		c1.attr('width', jQuery("#ch2").width());
		c1.attr('height', jQuery("#ch2").height());
		var c2 = $('#chart1');
		c2.attr('width', jQuery("#ch1").width());
		c2.attr('height', jQuery("#ch1").height());
		

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
	</div>

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
		<script>
		jQuery(document).ready(function($) {
			$('#transTable').DataTable({"info":false,"searching":false,"paging":false});
		});
		</script>
	</div>

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
			<input type="text" class="inp" value="${TotalTransVolume}" disabled
				readonly>
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
