package com.payitnz.model;

public class AlipayPaymentGatewayBean {
	
	int id;
	String payment_method;
	int type;
	int payment_method_type_id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPayment_method_type_id() {
		return payment_method_type_id;
	}
	public void setPayment_method_type_id(int payment_method_type_id) {
		this.payment_method_type_id = payment_method_type_id;
	}
	
}
