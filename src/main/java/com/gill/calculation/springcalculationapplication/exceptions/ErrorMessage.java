package com.gill.calculation.springcalculationapplication.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ErrorMessage {
	private HttpStatus statusCode;
	private List<String> messages = new ArrayList<>();

	public ErrorMessage() {
	}

	public ErrorMessage(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

	public ErrorMessage(HttpStatus statusCode, String message) {
		this.statusCode = statusCode;
		this.messages.add(message);
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void addMessage(String message) {
		this.messages.add(message);
	}

}
