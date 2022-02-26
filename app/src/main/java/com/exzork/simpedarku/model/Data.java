package com.exzork.simpedarku.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("errors")
	private List<String> errors;

	@SerializedName("users")
	private List<User> users;

	@SerializedName("user")
	private User user;

	@SerializedName("report")
	private Report report;

	@SerializedName("reports")
	private List<Report> reports;



	public List<String> getErrors(){
		return errors;
	}

	public List<User> getUsers() {
		return users;
	}

	public User getUser() {
		return user;
	}

	public Report getReport() {
		return report;
	}

	public List<Report> getReports() {
		return reports;
	}

	public String getErrorString(){
		StringBuilder sb = new StringBuilder();
		int n = errors.size();
		int i = 0;
		for (String s : errors) {
			sb.append(s);
			if ( n!= 1 && i < errors.size()-1){
				sb.append("\n");
			}
			i++;
		}
		return sb.toString();
	}
}