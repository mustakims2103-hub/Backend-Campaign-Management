package com.dt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.dt.payloads.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CampaignNotFoundException.class)
	public ResponseEntity<ErrorResponse> campNotFound(CampaignNotFoundException campExce, 
			                                   WebRequest webRequest){
		 
		ErrorResponse errorRes= new ErrorResponse(campExce.getMessage(), 
				                                  webRequest.getDescription(false),
				                                  "CAMPAIGN NOT FOUND"
				                                );
		
		return new ResponseEntity<>(errorRes, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> userNotFound(UserNotFoundException userExce, WebRequest webRequest){
		 
		ErrorResponse errorRes= new ErrorResponse(userExce.getMessage(), 
				                                  webRequest.getDescription(false),
				                                  "USER NOT FOUND"
				                                );
		
		return new ResponseEntity<>(errorRes, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex, WebRequest webRequest) {

        ErrorResponse error = new ErrorResponse(ex.getMessage(),
        	 	webRequest.getDescription(false),
                 "WRONG USERNAME OR PASSWORD"
        		);

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}
