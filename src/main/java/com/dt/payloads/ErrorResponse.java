package com.dt.payloads;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorResponse {

	LocalDateTime errorTime;
	
	String errorMessage;
	String errorDescription;
	String errorCode;
	
	public ErrorResponse(String errorMessage, String errorDescription, String errorCode) {
		this.errorTime = LocalDateTime.now();
		this.errorMessage = errorMessage;
		this.errorDescription = errorDescription;
		this.errorCode = errorCode;
	}
	
	
}
