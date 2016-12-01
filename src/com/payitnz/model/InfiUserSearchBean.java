package com.payitnz.model;

import java.util.ArrayList;

public class InfiUserSearchBean {
	String search_string;
	ArrayList status;
	int user_id;
	int role_id;
	int display_user;
	
	public int getDisplay_user() {
		return display_user;
	}
	public void setDisplay_user(int display_user) {
		this.display_user = display_user;
	}
	public String getSearch_string() {
		return search_string;
	}
	public void setSearch_string(String search_string) {
		this.search_string = search_string;
	}
	public ArrayList getStatus() {
		return status;
	}
	public void setStatus(ArrayList status) {
		this.status = status;
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
}
