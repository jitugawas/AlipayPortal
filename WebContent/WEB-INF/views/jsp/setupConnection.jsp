<!DOCTYPE html>
<html lang="en">

<%@ include file="fragments/backendHeader.jsp"%>
<%@ include file="fragments/backendMenu.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@	taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- Page Content -->

<a href = "setupPaymentConnection.jsp"> setupPaymentConnection.jsp</a>
<div id="page-wrapper">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="right_first_box">
					<a href="#"><img src="resources/core/images/home.png"></a> /
					&nbsp;<span class="breadscrum_active">Setup Payment
						Connections</span>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<ul class="nav nav-tabs responsive-tabs" id="tabs">
					<li class="active"><a href="#alipay_f2f">Alipay Offline</a></li>			
					<li><a href="#alipay_online">Alipay Online</a></li>
					<li><a href="#Dynamic">Dynamic</a></li>
					<li><a href = "#DPS">DPS</a></li>
					<li><a href="#Flo2Cash">Flo2Cash</a></li>
					
					<li><a href="#POLi">POLi</a></li>
				</ul>

				<div class="tab-content">
					<div class="tab-pane active" id="alipay_f2f">
						<form:form commandName='registrationForm' method='POST'
							id="alipay_con_form">
							<div class="row">
								<div class="col-lg-12 col-md-11">
									<div class="row">
										<div class="col-md-3 control_label">
											Service Name<span class="star_required">*</span>
										</div>
										<div class="col-md-4">
											<form:input type="text" class="inp validate[required]"
												path="service" id="service"
												value="alipay.acquire.overseas.spot.pay" readonly="true" placeholder="service"/>
										</div>
									</div>
									<div class="row">
										<div class="col-md-3 control_label">
											Alipay Partner ID<span class="star_required">*</span>
										</div>
										<div class="col-md-4">
											<form:input type="text" class="inp validate[required]"
												path="payitnz_id" id="payitnz_id" placeholder="partner id"/>
										</div>
									</div>
									<div class="row">
										<div class="col-md-3 control_label">
											Alipay Partner Key<span class="star_required">*</span>
										</div>
										<div class="col-md-4">
											<form:input type="text" class="inp validate[required]"
												path="partner_key" id="partner_key" placeholder="partner key" />
										</div>
									</div>
									<div class="row">
										<div class="col-md-3 control_label">
											Character set on merchant website<span class="star_required">*</span>
										</div>
										<div class="col-md-4">
											<form:input type="text" class="inp validate[required]"
												path="charSet" id="charSet" placeholder="charset" />
										</div>
									</div>
									<div class="row">
										<div class="col-md-3 control_label">
											Transaction currency<span class="star_required">*</span>
										</div>
										<div class="col-md-4">
											<form:input type="text" class="inp validate[required]"
												path="currency" id="currency" placeholder="currency" />
										</div>
									</div>
									<div class="row">
										<div class="col-md-3 control_label">
											Return URL<span class="star_required">*</span>
										</div>
										<div class="col-md-4">
											<form:input type="text" class="inp validate[required]"
												path="return_url" id="return_url" placeholder="return url" />
										</div>
									</div>
								</div>
							</div>

							<div class="row buttons">
								<div class="col-md-5 col-md-offset-2 col-sm-12 col-xs-12">
									<button class="btn btn-primary save_btn"
										id="save_alipay_button">Save</button>
									<button type="reset" class="btn btn-primary save_btn"
										id="reset_alipay_button">Reset</button>
									<input type="button" class="btn btn-primary save_btn"
										value="Clear" id='clear_alipay_button'>
								</div>
							</div>
						</form:form>
					</div>

					<div class="tab-pane" id="alipay_online">
						<div class="row">
							<form:form commandName='registrationForm' method='POST'
								id="alipay_con_form1">
								<div class="col-lg-8 col-md-12">
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">Service Name<span
												class="star_required">*</span></lable>
										</div>
										<div class="col-lg-6 col-md-7">
											<form:input type="text" class="inp validate[required]"
												path="online_service" value="create_forex_trade"
												readonly="true" placeholder="service" />
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">Alipay Partner ID<span
												class="star_required">*</span></lable>
										</div>
										<div class="col-lg-6 col-md-7">
											<form:input type="text" class="inp validate[required]"
												path="alipay_online_partner_id" id="alipay_online_partner_id" placeholder="partner id"/>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">Alipay Partner Key<span
												class="star_required">*</span></lable>
										</div>
										<div class="col-lg-6 col-md-7">
											<form:input type="text" class="inp validate[required]"
												path="alipay_online_partner_key" id="alipay_online_partner_key" placeholder="partner key" />
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">Character set on
											merchant website<span class="star_required">*</span></lable>
										</div>
										<div class="col-lg-7 col-md-7 col-sm-5 col-xs-12">
											<div class="row check_box">
												<div class="col-md-5">
													<form:select class="select" path="online_character_set" id="online_character_set">

														<form:option value="utf-8">utf-8</form:option>
														<form:option value="gbk">gbk</form:option>
														<form:option value="gb2312">gb2312</form:option>
													</form:select>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">Transaction currency<span
												class="star_required">*</span></lable>
										</div>
										<div class="col-lg-6 col-md-7">
											<form:input type="text"
												class="inp validate[required, maxSize[50]]"
												path="online_currency" id="online_currency" placeholder="currency"/>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">Return URL<span
												class="star_required">*</span></lable>
										</div>
										<div class="col-lg-6 col-md-7">
											<form:input type="text"
												class="inp validate[required, maxSize[2000], custom[url]]"
												path="alipay_online_return_url" id="alipay_online_return_url" placeholder="return url"/>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">Notification URL<span
												class="star_required">*</span></lable>
										</div>
										<div class="col-lg-6 col-md-7">
											<form:input type="text"
												class="inp validate[required, maxSize[1000], custom[url]]"
												path="alipay_online_notification_url" id="alipay_online_notification_url" placeholder="notification url"/>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">Order valid time<span
												class="star_required">*</span></lable>
										</div>
										<div class="col-lg-6 col-md-7">
											<form:input type="text"
												class="inp validate[required, maxSize[1000], custom[integer]]"
												path="order_valid_time" id="order_valid_time" placeholder="time"/>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5 col-sm-7 col-xs-12">
											<lable>Active Payment Method</lable>
										</div>
										<div class="col-lg-7 col-md-7 col-sm-5 col-xs-12">
											<div class="row check_box">
												<div class="col-md-5">
												<form:checkbox path="alipay_supported_method" label="Alipay" value= "6" class="chk_support_dynamic" id= "alipay_supported_method"/> 
												
												</div>
											</div>
										</div>
									</div>
									
									<div class="row buttons">
										<div class="col-md-12">
											<button class="btn btn-primary save_btn"
												id="save_alipay_online_button">Save</button>
											 <button
												type="reset" class="btn btn-primary save_btn" 
												id='reset_alipay_online_button'>Reset</button>
												<input type="button" class="btn btn-primary save_btn"
										value="Clear" id='clear_alipay_online_button'>
										</div>
									</div>
								</div>
								<!--content col end-->
							</form:form>
						</div>

						<!--content row end-->
					</div>
					
										
				
					<div class="tab-pane" id="Dynamic">
						<form:form commandName='registrationForm' id="dynamic_con_form">
							<div class="row">
							
							<form:errors cssClass="error" id="Dyerror"/>
							<br class="clear">
								<div class="col-lg-8 col-md-12">
								
									<div class="row">
									
										<div class="col-lg-4 col-md-5">
										
											<lable class="control_lable">Merchant ID<span class="star_required">*</span></lable>
												
										</div>
									
										<div class="col-lg-6 col-md-7">
										
											<form:input type="text"
												class="inp validate[required,maxSize[15], minSize[15]]"
												placeholder="Merchant Id" path="merchant_id" id="txtUserID" />
												
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">MCC<span class="star_required">*</span></lable>
										</div>
										<div class="col-lg-6 col-md-7">
											<form:input type="text"
												class="inp validate[required,maxSize[4], minSize[4], custom[onlyNumberSp]]"
												placeholder="Mcc" path="mcc" id="txtMcc"/>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">Transaction Type<span class="star_required">*</span></lable>
										</div>
										<div class="col-lg-6 col-md-7">
											<form:input type="text"
												class="inp validate[required,custom[onlyNumberSp], maxSize[2], minSize[2]]"
												placeholder="Transaction Type" path="type" id="txtTtype" value = "01" readonly = "true"/>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">Merchant Name<span class="star_required">*</span></lable>
										</div>
										<div class="col-lg-6 col-md-7">
											<form:input type="text"
												class="inp validate[required, maxSize[25]]"
												placeholder="Merchant Name" path="merchant_name" id="txtMname"/>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">Commodity URL<span class="star_required">*</span></lable>
										</div>
										<div class="col-lg-6 col-md-7">
											<form:input type="text"
												class="inp validate[required, url, maxSize[1024], custom[url]]"
												placeholder="Commodity URL" path="commodity_url" id="txtCurl"/>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">Transaction Currency<span class="star_required">*</span> </lable>
										</div>
										<div class="col-lg-6 col-md-7">
											<form:input type="text"
												class="inp validate[required,custom[onlyNumberSp],maxSize[3], minSize[3]]"
												placeholder="Currency" path="currency" id="txtTransCurr"/>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">Order Timeout<span class="star_required">*</span></lable>
										</div>
										<div class="col-lg-6 col-md-7">
												<form:input type="text"
												class="inp validate[required,custom[onlyNumberSp], maxSize[10]]"
												placeholder="Timeout" path="timeout" id="txtOrderTO"/>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-4 col-md-5">
											<lable class="control_lable">Merchant Reserved Field<span class="star_required">*</span></lable>
										</div>
										<div class="col-lg-6 col-md-7">
												<form:input type="text"
												class="inp validate[required, maxSize[1024]]"
												placeholder="Merchant Reserved Field" path="merchant_reserved_field" id="txtMarcres"/>
										</div>
									</div>

									
									<div class="row buttons">
										<div class="col-md-12">
										<form:button type="submit" class="btn btn-primary save_btn"
											id='save_dynamic_button'>Save</form:button>
											
										<input type="button" class="btn btn-primary save_btn" value = "Clear" id='clear_dynamic_button'>
										
											<a href=<%=DynamicPaymentConstant.SERVER_HOST + DynamicPaymentConstant.SERVER_SITE_URL+"setupPayConnection"%>><input type="button" class="btn btn-primary save_btn" value = "Reset" id='reset_dynamic_button'></a>
										
											
										</div>
									</div>
								</div>
								<!--content col end-->

							</div>
							<!--content row end-->
						</form:form>

					</div>
								
					<div class="tab-pane" id="DPS">
		    		<form:form commandName='registrationForm' method='POST' id="dps_con_form">
					
						<div class="row">
							<div class="col-lg-8 col-md-12">
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">PxPayUserId<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, [maxSize[32]]" path="PxPayUserId"/>
									</div>
								</div>
								<div class="row"> 
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">PxPayKey<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7 ">
										<form:input type="text" class="inp validate[required, [maxSize[64]] " path="PxPayKey"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">PxPayUrl<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, custom[url]]" path="PxPayUrl"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Transaction Type<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, [maxSize[8]]" path="DPStransaction_type" value="Purchase" readonly = "true"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Transaction Currency<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, [maxSize[4]]" path="DPScurrency" value = "NZD" readonly = "true"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Email<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, maxSize[255], custom[email]]" path="email"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Success URL<span class="star_required">*</span> </lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, [maxSize[255], custom[url]]" path="DPSsuccess_url"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Failure URL<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, [maxSize[255], custom[url]]" path="DPSfailure_url"/>
									</div>
								</div>

								<div class="row buttons">
									<div class="col-md-12">
									<button class="btn btn-primary save_btn" id="save_DPS_button">Save</button>
									<input type="button" class="btn btn-primary save_btn" value = "Clear" id='clear_dps_button'>
									<a href=<%=DynamicPaymentConstant.SERVER_HOST + DynamicPaymentConstant.SERVER_SITE_URL+"setupPayConnection"%>><input type="button" class="btn btn-primary save_btn" value = "Reset" id='reset_dynamic_button'></a>
										
									</div>
								</div>
							</div>
							<!--content col end-->

						</div>
						</form:form>
					</div>
					
					<div class="tab-pane" id="Flo2Cash">
					<form:form commandName='registrationForm' method='POST' id="f2c_con_form">
					
						<div class="row">
						
							<div class="col-lg-8 col-md-12">
								<div class="row">
									<div class="col-md-5">
										<lable class="control_lable">Web Payments Integration
										Service<span class="star_required">*</span></lable>
									</div>
									<div class="col-md-7">
										<form:input type="text" class="inp validate[required]" path="F2c_service" id="service" value="_xclick" readonly="true"/>
									</div>
								</div>
								<div class="row">
									<div class="col-md-5">
										<lable class="control_lable">Flo2Cash Account ID<span class="star_required">*</span></lable>
									</div>
									<div class="col-md-7">
										<form:input type="text" class="inp validate[required,custom[integer]]" path="f2c_account_id" id="account_id"/>
									</div>
								</div>
								<div class="row">
									<div class="col-md-5">
										<lable class="control_lable">Flo2Cash Secret Hash Key<span class="star_required">*</span></lable>
									</div>
									<div class="col-md-7">
										<form:input type="text" class="inp validate[required]" path="f2c_secret_key" id="secret_key"/>
									</div>
								</div>
								<div class="row">
									<div class="col-md-5">
										<lable class="control_lable">Merchant Reference</lable>
									</div>
									<div class="col-md-7">
										<form:input type="text" class="inp validate[maxSize[15]]" path="f2c_merchant_reference"/>
									</div>
								</div>
								<div class="row">
									<div class="col-md-5">
										<lable class="control_lable">Return URL<span class="star_required">*</span></lable>
									</div>
									<div class="col-md-7">
										<form:input type="text" class="inp validate[required,maxSize[1024], custom[url]]" path="f2c_return_url" />
									</div>
								</div>
								<div class="row">
									<div class="col-md-5">
										<lable class="control_lable">Notification URL(MNS)</lable>
									</div>
									<div class="col-md-7">
										<form:input type="text" class="inp validate[maxSize[1024], custom[url]]" path="f2c_notification_url" id ="f2c_notification_url" />
									</div>
								</div>
								<div class="row">
									<div class="col-md-5">
										<lable class="control_lable">Header Image</lable>
									</div>
									<div class="col-md-7">
										<form:input type="text" class="inp validate[maxSize[1024]]" path="header_image" />
									</div>
								</div>
								<div class="row">
									<div class="col-md-5 col-sm-7 col-xs-8">
										<lable>Header Bottom Border Color</lable>
									</div>
									<div class="col-md-7">
								<form:input class="jscolor inp" name="Header_Bottom_Border_Color" path="header_bottom_border_color" id="border_color"/>
								</div>
								</div>
								
								<div class="row">
									<div class="col-md-5 col-sm-7 col-xs-8">
										<lable>Header Background Color</lable>
									</div>
									<div class="col-md-7">
								 	<form:input class="jscolor inp" name="Header_Bottom_Border_Color" path="header_background_color" id="backf"/>

									</div>
								</div>
								<div class="row">
									<div class="col-md-5">
										<lable class="control_lable">Merchant defined Custom
										Data</lable>
									</div>
									<div class="col-md-7">
										<form:input type="text" class="inp validate[maxSize[1024]]" path="f2c_custom_data"/>
									</div>
								</div>

								<div class="row">
									<div class="col-md-5">
										<lable class="control_lable">Store Card</lable>
									</div>
									<div class="col-md-7">
										<form:select class="select" path="store_card" id="store_card">
											<option>${isStoreCard}</option>
													<c:choose>
    													<c:when test="${isStoreCard == 'No'}">
    													<option>Yes</option>
    														</c:when>
															 <c:otherwise>
											      				<option>No</option>
											   				 </c:otherwise>
											   		</c:choose>
										</form:select>
									</div>
								</div>

								<div class="row">
									<div class="col-md-5">
										<lable class="control_lable">Display Customer Email</lable>
									</div>
									<div class="col-md-7">
									
										<form:select class="select" path="display_customer_email" id="display_customer_email">
											<option>${isEmail}</option>
														<c:choose>
    													<c:when test="${isEmail == 'No'}">
    													<option>Yes</option>
    														</c:when>
															 <c:otherwise>
											      				<option>No</option>
											   				 </c:otherwise>
											   			</c:choose>
										</form:select>
									</div>
								</div>
							
								<div class="row">
									<div class="col-md-5">
										<lable class="control_lable">Payment Method</lable>
									</div>
									<div class="col-md-7">
										<form:select class="select" path="f2c_payment_method" value = "Standard" readonly = "true">
											<option>Standard</option>
										</form:select>
									</div>
								</div>

								<div class="row buttons">
									<div class="col-md-12">
										
										<button class="btn btn-primary save_btn" id='save_F2c_button'>Save</button>
										<input type="button" class="btn btn-primary save_btn" value = "Clear" id='clear_f2c_button'>
										<a href=<%=DynamicPaymentConstant.SERVER_HOST + DynamicPaymentConstant.SERVER_SITE_URL+"setupPayConnection"%>><input type="button" class="btn btn-primary save_btn" value = "Reset" id='reset_dynamic_button'></a>
										
									</div>
								</div>

							
						</div>
						</div>
						
						</form:form>
					</div>
					
		
					
				
					<div class="tab-pane" id="POLi">
		           <form:form commandName='registrationForm' method='POST' id="poli_con_form">
					
						<div class="row">
							<div class="col-lg-8 col-md-12">
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Poli Account ID<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required]" path = "poli_account_id"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Poli Password<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required]" path = "password"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Currency Code<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required]" path = "currency_code"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Merchant Reference<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, maxSize[100]]" path = "poli_merchant_reference"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Merchant Reference Format<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, maxSize[50]]" path = "merchant_reference_format"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">MerchantData<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, maxSize[2000]]" path = "merchant_data"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Merchant Homepage URL<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, maxSize[1000], custom[url]]" path = "poli_homepage_url"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Success URL<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, maxSize[1000], custom[url]]" path = "poli_success_url"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Failure URL<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, maxSize[1000], custom[url]]" path = "poli_failure_url"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Cancellation URL<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, maxSize[1000], custom[url]]" path = "poli_cancellation_url"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Notification URL<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, maxSize[1000], custom[url]]" path = "poli_notification_url"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Timeout<span class="star_required">*</span></lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp validate[required, custom[onlyNumberSp]]" path = "poli_timeout"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Selected FI Code</lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp" path = "fi_code"/>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-4 col-md-5">
										<lable class="control_lable">Company Code</lable>
									</div>
									<div class="col-lg-6 col-md-7">
										<form:input type="text" class="inp" path = "company_code"/>
									</div>
								</div>
								
								<div class="row buttons">
									<div class="col-md-12">
									<button class="btn btn-primary save_btn" id="save_Poli_button">Save</button>
									<input type="button" class="btn btn-primary save_btn" value = "Clear" id='clear_poli_button'>
									<a href=<%=DynamicPaymentConstant.SERVER_HOST + DynamicPaymentConstant.SERVER_SITE_URL+"setupPayConnection"%>><input type="button" class="btn btn-primary save_btn" value = "Reset" id='reset_dynamic_button'></a>
								
									</div>
								</div>
							</div>
							<!--content col end-->

						</div>
						</form:form>
						<!--content row end-->



					</div>
					
				</div>

			</div>

		</div>

	</div>
</div>
<script type="text/javascript">
    $("#alipay_con_form").validationEngine('attach', { scroll: false });
    $("#alipay_con_form1").validationEngine('attach', { scroll: false });
    $(function(){
    	   var hash = window.location.hash;
    	   hash && $('ul.nav a[href="' + hash + '"]').tab('show');

    	   $('.nav-tabs a').click(function (e) {
    	  $(this).tab('show');
    	  var scrollmem = $('body').scrollTop() || $('html').scrollTop();
    	  window.location.hash = this.hash;
    	  $('html,body').scrollTop(scrollmem);
    	   });
    	 });
</script>
<%@ include file="fragments/FormOperations.jsp"%>
<%@ include file="fragments/backendFooter.jsp"%>


</html>
