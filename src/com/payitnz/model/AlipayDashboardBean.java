package com.payitnz.model;

import java.sql.Date;

public class AlipayDashboardBean {

	//search
	Date toDate;
	Date fromDate;
	int user_id;
	int role_id;
	int display_transactions_per_day;
	
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public int getDisplay_transactions_per_day() {
		return display_transactions_per_day;
	}
	public void setDisplay_transactions_per_day(int display_transactions_per_day) {
		this.display_transactions_per_day = display_transactions_per_day;
	}
	
	//display fields	
	int trans_num; 
	int payment_method_id; 
	double sum_amount; 
	double avg_amount; 
	double max_amount;
	double min_amount;
	double total_amount;
	String email;
	String company_name;
	int total_transaction;
	
	public String getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	String payment_method;
	
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public double getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}
	public int getTotal_transaction() {
		return total_transaction;
	}
	public void setTotal_transaction(int total_transaction) {
		this.total_transaction = total_transaction;
	}
	public int getTrans_num() {
		return trans_num;
	}
	public void setTrans_num(int trans_num) {
		this.trans_num = trans_num;
	}
	public int getPayment_method_id() {
		return payment_method_id;
	}
	public void setPayment_method_id(int payment_method_id) {
		this.payment_method_id = payment_method_id;
	}
	public double getSum_amount() {
		return sum_amount;
	}
	public void setSum_amount(double sum_amount) {
		this.sum_amount = sum_amount;
	}
	public double getAvg_amount() {
		return avg_amount;
	}
	public void setAvg_amount(double avg_amount) {
		this.avg_amount = avg_amount;
	}
	public double getMax_amount() {
		return max_amount;
	}
	public void setMax_amount(double max_amount) {
		this.max_amount = max_amount;
	}
	public double getMin_amount() {
		return min_amount;
	}
	public void setMin_amount(double min_amount) {
		this.min_amount = min_amount;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
