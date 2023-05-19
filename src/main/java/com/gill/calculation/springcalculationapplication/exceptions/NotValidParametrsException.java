package com.gill.calculation.springcalculationapplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NotValidParametrsException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private ErrorMessage errorMessage;

	public NotValidParametrsException(ErrorMessage errorMessage) {
		super();
		this.setErrorMessage(errorMessage);
	}

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

}
