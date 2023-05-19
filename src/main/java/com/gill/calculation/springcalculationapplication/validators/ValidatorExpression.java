package com.gill.calculation.springcalculationapplication.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.gill.calculation.springcalculationapplication.exceptions.ErrorMessage;

@Component
public class ValidatorExpression {
	/* Logger */
	private static final Logger logger = LoggerFactory.getLogger(ValidatorExpression.class);
	
	public ErrorMessage isCorrectExpression(String numberFirst, String numberSecond, String operation) {
		ErrorMessage errorMessage = new ErrorMessage();
		if (!isNumber(numberFirst)) {
			logger.error("First parametr is not number");
			errorMessage.setStatusCode(HttpStatus.BAD_REQUEST);
			errorMessage.addMessage("First parametr is not number");
		}

		if (!isNumber(numberSecond)) {
			logger.error("Second parametr is not number");
			errorMessage.setStatusCode(HttpStatus.BAD_REQUEST);
			errorMessage.addMessage("Second parametr is not number");
		}

		if (!isOperation(operation)) {
			logger.error("Third parametr is not operation");
			errorMessage.setStatusCode(HttpStatus.BAD_REQUEST);
			errorMessage.addMessage("Third parametr is not operation");
		}
		return errorMessage;
	}

	public boolean isNumber(String num) {
		for (int i = 0; i < num.length(); ++i) {
			if (!Character.isDigit(num.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public boolean isOperation(String operation) {
		if (!operation.equals("minus") && !operation.equals("plus") && !operation.equals("divide")
				&& !operation.equals("multiply")) {
			return false;
		}
		return true;
	}
}
