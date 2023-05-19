package com.gill.calculation.springcalculationapplication.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gill.calculation.springcalculationapplication.exceptions.ErrorMessage;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
	private ResultOfExpression resultOfIntExpression;
	private ErrorMessage errorMessage;

	public Response(ResultOfExpression resultOfIntExpression, ErrorMessage errorMessage) {
		this.resultOfIntExpression = resultOfIntExpression;
		this.errorMessage = errorMessage;
	}
	
	public Response(ResultOfExpression resultOfIntExpression) {
		this.resultOfIntExpression = resultOfIntExpression;
	}
	
	public Response(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ResultOfExpression getResultOfIntExpression() {
		return resultOfIntExpression;
	}

	public void setResultOfExpression(ResultOfExpression resultOfIntExpression) {
		this.resultOfIntExpression = resultOfIntExpression;
	}

	public ErrorMessage getErrorEntity() {
		return errorMessage;
	}

	public void setErrorEntity(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

}
