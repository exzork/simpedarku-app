package com.exzork.simpedarku.model;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse{

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public Data getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}

	@Override
	public String toString() {
		return "ErrorResponse{" +
				"data=" + data +
				", message='" + message + '\'' +
				", status='" + status + '\'' +
				'}';
	}
}