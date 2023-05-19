package com.gill.calculation.springcalculationapplication.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.gill.calculation.springcalculationapplication.controllers.AsyncCalculationController;
import com.gill.calculation.springcalculationapplication.entities.Expression;
import com.gill.calculation.springcalculationapplication.entities.ResponseId;
import com.gill.calculation.springcalculationapplication.entities.ResultOfExpression;
import com.gill.calculation.springcalculationapplication.exceptions.ErrorMessage;
import com.gill.calculation.springcalculationapplication.exceptions.NotValidParametrsException;
import com.gill.calculation.springcalculationapplication.services.CalculationService;
import com.gill.calculation.springcalculationapplication.services.impl.AsyncDatabaseIncrementService;
import com.gill.calculation.springcalculationapplication.services.impl.AsyncDatabaseService;
import com.gill.calculation.springcalculationapplication.validators.ValidatorExpression;

public class ColculationControllerAsyncTest {
	/* for activation of Mockito */
	private AutoCloseable closeable;
	/* Database */
	@Mock
	private AsyncDatabaseService asyncDatabaseService;
	/* Database */
	@Mock
	private AsyncDatabaseIncrementService asyncDatabaseIncrementService;
	/* Service */
	@Mock
	private CalculationService service;
	/* Validator */
	@Mock
	private ValidatorExpression validator;

	@InjectMocks
	private AsyncCalculationController controller;

	@BeforeEach
	void initService() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void closeService() throws Exception {
		closeable.close();
	}

	@Test
	public void testAsyncController() {
		String numberFirst = "12";
		String numberSecond = "4";
		String operation = "minus";

		Expression expression = new Expression(Integer.parseInt(numberFirst), Integer.parseInt(numberSecond),
				operation);
		ResultOfExpression resultOfExpression = new ResultOfExpression(8, expression);

		when(validator.isCorrectExpression(numberFirst, numberSecond, operation)).thenReturn(new ErrorMessage());
		when(service.calculate(expression)).thenReturn(resultOfExpression);
		when(asyncDatabaseIncrementService.generateId()).thenReturn(1);
		doNothing().when(asyncDatabaseService).saveResult(any());

		ResponseId response = controller.getResultExpression(numberFirst, numberSecond, operation);

		verify(asyncDatabaseIncrementService, times(1)).generateId();
		verify(asyncDatabaseService, times(1)).saveResult(any());
		verify(validator, times(1)).isCorrectExpression(numberFirst, numberSecond, operation);
		verify(service, times(1)).calculate(any());
		assertEquals(1, response.getId());
	}

	@Test
	public void testAsyncControllerException() {
		String numberFirst = "12";
		String numberSecond = "4ds";
		String operation = "minus";

		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.addMessage("Second parametr is not number");
		errorMessage.setStatusCode(HttpStatus.BAD_REQUEST);

		when(validator.isCorrectExpression(numberFirst, numberSecond, operation)).thenReturn(errorMessage);
		NotValidParametrsException exception = assertThrows(NotValidParametrsException.class,
				() -> controller.getResultExpression(numberFirst, numberSecond, operation));

		verify(validator, times(1)).isCorrectExpression(numberFirst, numberSecond, operation);
		verify(service, times(0)).calculate(any());
		assertEquals(HttpStatus.BAD_REQUEST, exception.getErrorMessage().getStatusCode());
		assertEquals("Second parametr is not number", exception.getErrorMessage().getMessages().get(0));
	}

}
