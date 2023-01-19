package com.prueba.nextia.domain;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HttpResponse {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss", timezone = "America/Mexico_City")
	private Date timeStamp;
	private int HttpStatusCode; // 201, 205, 500 internal server
	private HttpStatus httpStatus;
	private String reason;
	private String message;
	
	public HttpResponse () {}
	
	public int getHttpStatusCode() {
		return HttpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		HttpStatusCode = httpStatusCode;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message) {
		super();
		this.timeStamp = new Date();
		HttpStatusCode = httpStatusCode;
		this.httpStatus = httpStatus;
		this.reason = reason;
		this.message = message;
	}

}
