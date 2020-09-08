package com.javaeasy.security.model;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HttpResponse {

	@JsonFormat(shape = Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss", timezone = "Asia/Kolkata")
	private Date timeStamp;
	private int httpStatusCode;
	private HttpStatus status;
	private String reason;
	private String message;
}
