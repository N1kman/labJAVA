package com.gill.calculation.springcalculationapplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gill.calculation.springcalculationapplication.annotations.CustomExceptionHandler;
import com.gill.calculation.springcalculationapplication.entities.Response;

@RestControllerAdvice(annotations = CustomExceptionHandler.class)
public class ApplicationExceptionHandler {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(InternalServerException.class)
	public Response internalServerException(InternalServerException exception) {
		return new Response(exception.getErrorMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NotValidParametrsException.class)
	public Response notValidParametrsException(NotValidParametrsException exception) {
		return new Response(exception.getErrorMessage());
	}

}
