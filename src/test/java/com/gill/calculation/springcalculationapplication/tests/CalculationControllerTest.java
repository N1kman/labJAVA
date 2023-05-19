package com.gill.calculation.springcalculationapplication.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.gill.calculation.springcalculationapplication.controllers.CalculationController;
import com.gill.calculation.springcalculationapplication.databaseEntities.Result;
import com.gill.calculation.springcalculationapplication.entities.CacheMap;
import com.gill.calculation.springcalculationapplication.entities.CounterOfCalls;
import com.gill.calculation.springcalculationapplication.entities.Expression;
import com.gill.calculation.springcalculationapplication.entities.ExpressionStrings;
import com.gill.calculation.springcalculationapplication.entities.Response;
import com.gill.calculation.springcalculationapplication.entities.ResponseList;
import com.gill.calculation.springcalculationapplication.entities.ResultOfExpression;
import com.gill.calculation.springcalculationapplication.exceptions.ErrorMessage;
import com.gill.calculation.springcalculationapplication.exceptions.InternalServerException;
import com.gill.calculation.springcalculationapplication.exceptions.NotValidParametrsException;
import com.gill.calculation.springcalculationapplication.services.CalculationService;
import com.gill.calculation.springcalculationapplication.services.impl.DatabaseService;
import com.gill.calculation.springcalculationapplication.validators.ValidatorExpression;

public class CalculationControllerTest {

	/* for activation of Mockito */
	private AutoCloseable closeable;
	@Mock
	private CacheMap cacheMap;
	@Mock
	private ValidatorExpression validator;
	@Mock
	private CalculationService service;
	@Mock
	private DatabaseService databaseService;
	@InjectMocks
	private CalculationController controller;

	@BeforeEach
	void initService() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void closeService() throws Exception {
		closeable.close();
	}

	@Test
	public void testController() {
		String numberFirst = "12";
		String numberSecond = "4";
		String operation = "minus";

		Expression expression = new Expression(Integer.parseInt(numberFirst), Integer.parseInt(numberSecond),
				operation);
		ResultOfExpression resultOfExpression = new ResultOfExpression(8, expression);

		when(validator.isCorrectExpression(numberFirst, numberSecond, operation)).thenReturn(new ErrorMessage());
		when(service.calculate(expression)).thenReturn(resultOfExpression);
		when(cacheMap.getCache(any())).thenReturn(resultOfExpression);
		when(cacheMap.haveCacheByValue(any())).thenReturn(Boolean.TRUE);

		Response response = controller.getResultExpression(numberFirst, numberSecond, operation);

		verify(cacheMap, times(1)).getCache(any());
		verify(cacheMap, times(1)).haveCacheByValue(any());
		verify(validator, times(1)).isCorrectExpression(numberFirst, numberSecond, operation);
		verify(service, times(1)).calculate(any());
		assertEquals(8, response.getResultOfIntExpression().getResult());
	}

	@Test
	public void testControllerNull() {
		String numberFirst = "12";
		String numberSecond = "4";
		String operation = "minus";

		Expression expression = new Expression(Integer.parseInt(numberFirst), Integer.parseInt(numberSecond),
				operation);
		ResultOfExpression resultOfExpression = new ResultOfExpression(8, expression);

		when(validator.isCorrectExpression(numberFirst, numberSecond, operation)).thenReturn(new ErrorMessage());
		when(service.calculate(expression)).thenReturn(resultOfExpression);
		when(cacheMap.getCache(any())).thenReturn(null);
		when(cacheMap.haveCacheByValue(any())).thenReturn(Boolean.FALSE);

		List<Result> array = new ArrayList<>();
		array.add(new Result(12, 12, "plus", 24, "12 plus 12"));
		when(databaseService.getAllResults()).thenReturn(array);

		doNothing().when(cacheMap).putCache(any(), any());
		doNothing().when(databaseService).saveResult(any());

		Response response = controller.getResultExpression(numberFirst, numberSecond, operation);

		verify(cacheMap, times(1)).getCache(any());
		verify(cacheMap, times(1)).haveCacheByValue(any());
		verify(cacheMap, times(2)).putCache(any(), any());
		verify(databaseService, times(1)).saveResult(any());
		verify(validator, times(1)).isCorrectExpression(numberFirst, numberSecond, operation);
		verify(service, times(1)).calculate(any());
		assertEquals(8, response.getResultOfIntExpression().getResult());
	}

	@Test
	public void testControllerThrowNotValidParametrsException() {
		String numberFirst = "1d2";
		String numberSecond = "4s";
		String operation = "minssus";
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.addMessage("First parametr is not number");
		errorMessage.addMessage("Second parametr is not number");
		errorMessage.addMessage("Third parametr is not operation");
		errorMessage.setStatusCode(HttpStatus.BAD_REQUEST);

		when(validator.isCorrectExpression(numberFirst, numberSecond, operation)).thenReturn(errorMessage);
		NotValidParametrsException exception = assertThrows(NotValidParametrsException.class,
				() -> controller.getResultExpression(numberFirst, numberSecond, operation));

		verify(validator, times(1)).isCorrectExpression(numberFirst, numberSecond, operation);
		verify(service, times(0)).calculate(any());
		assertEquals(HttpStatus.BAD_REQUEST, exception.getErrorMessage().getStatusCode());
		assertEquals("First parametr is not number", exception.getErrorMessage().getMessages().get(0));
		assertEquals("Second parametr is not number", exception.getErrorMessage().getMessages().get(1));
		assertEquals("Third parametr is not operation", exception.getErrorMessage().getMessages().get(2));
	}

	@Test
	public void testGetAllResults() {
		List<Result> array = new ArrayList<>();
		array.add(new Result(12, 12, "plus", 24, "12 plus 12"));
		when(databaseService.getAllResults()).thenReturn(array);
		controller.getAllResults();
		verify(databaseService, times(1)).getAllResults();
	}

	@Test
	public void testControllerThrowInternalServerException() {
		String numberFirst = "12";
		String numberSecond = "0";
		String operation = "divide";

		when(validator.isCorrectExpression(numberFirst, numberSecond, operation)).thenReturn(new ErrorMessage());

		Expression expression = new Expression(Integer.parseInt(numberFirst), Integer.parseInt(numberSecond),
				operation);
		when(service.calculate(expression)).thenThrow(new ArithmeticException("/ by zero"));

		InternalServerException exception = assertThrows(InternalServerException.class,
				() -> controller.getResultExpression(numberFirst, numberSecond, operation));

		verify(validator, times(1)).isCorrectExpression(numberFirst, numberSecond, operation);
		verify(service, times(1)).calculate(any());

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getErrorMessage().getStatusCode());
		assertEquals("/ by zero", exception.getErrorMessage().getMessages().get(0));
	}

	@Test
	public void testGetCache() {
		Expression expression = new Expression(12, 4, "divide");

		CacheMap cacheMap = new CacheMap();
		ResultOfExpression result = new ResultOfExpression(3, expression);
		cacheMap.putCache(expression.toString(), result);

		controller.setCacheMap(cacheMap);
		CacheMap resultCache = controller.getCache();
		Set<String> resultCacheKeys = controller.getCacheKeys();
		Collection<ResultOfExpression> resultCacheValues = controller.getCacheValues();

		assertEquals(result, resultCache.getCache("12 divide 4"));
		assertEquals(Boolean.TRUE, resultCacheKeys.contains(expression.toString()));
		assertEquals(Boolean.TRUE, resultCacheValues.contains(result));
	}

	@Test
	public void testControllerCounter() {
		CounterOfCalls counterOfCalls = new CounterOfCalls();
		counterOfCalls.addToCounter(1);
		controller.setCounterOfCalls(counterOfCalls);

		CounterOfCalls resultCounterOfCalls = controller.getCounterOfCalls();

		assertEquals(counterOfCalls.getCounter(), resultCounterOfCalls.getCounter());
	}

	@Test
	public void testPostRequest() {
		List<ExpressionStrings> list = new ArrayList<>();
		list.add(new ExpressionStrings("12", "12", "plus"));
		list.add(new ExpressionStrings("1f2", "f12", "pflus"));
		list.add(new ExpressionStrings("12", "12", "multiply"));
		list.add(new ExpressionStrings("12", "0", "divide"));

		when(cacheMap.getCache(any())).thenReturn(null);

		List<Result> array = new ArrayList<>();
		array.add(new Result(12, 12, "plus", 24, "12 plus 12"));
		when(databaseService.getAllResults()).thenReturn(array);

		doNothing().when(cacheMap).putCache(any(), any());
		doNothing().when(databaseService).saveResult(any());

		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.addMessage("First parametr is not number");
		errorMessage.addMessage("Second parametr is not number");
		errorMessage.addMessage("Third parametr is not operation");
		errorMessage.setStatusCode(HttpStatus.BAD_REQUEST);

		when(validator.isCorrectExpression("12", "12", "plus")).thenReturn(new ErrorMessage());
		when(validator.isCorrectExpression("1f2", "f12", "pflus")).thenReturn(errorMessage);
		when(validator.isCorrectExpression("12", "12", "multiply")).thenReturn(new ErrorMessage());
		when(validator.isCorrectExpression("12", "0", "divide")).thenReturn(new ErrorMessage());

		Expression expression = new Expression(12, 12, "plus");
		ResultOfExpression resultOfExpression = new ResultOfExpression(24, expression);
		when(cacheMap.getCache(
				expression.getNumberFirst() + " " + expression.getOperation() + " " + expression.getNumberSecond()))
				.thenReturn(resultOfExpression);
		when(service.calculate(expression)).thenReturn(resultOfExpression);
		expression.setOperation("multiply");
		resultOfExpression.setResult(144);
		when(cacheMap.getCache(
				expression.getNumberFirst() + " " + expression.getOperation() + " " + expression.getNumberSecond()))
				.thenReturn(resultOfExpression);
		when(service.calculate(any())).thenReturn(resultOfExpression);
		expression.setOperation("divide");
		expression.setNumberSecond(0);
		when(service.calculate(expression)).thenThrow(new ArithmeticException("/ by zero"));

		when(service.countMin(any())).thenReturn(24);
		when(service.countMax(any())).thenReturn(144);
		when(service.countAverage(any())).thenReturn(84.0);

		when(cacheMap.haveCacheByValue(any())).thenReturn(Boolean.FALSE);
		doNothing().when(cacheMap).putCache(any(), any());
		doNothing().when(databaseService).saveResult(any());

		ResponseList responseList = controller.postRequest(list);

		verify(validator, times(4)).isCorrectExpression(any(), any(), any());
		verify(service, times(3)).calculate(any());
		verify(service, times(1)).calculate(expression);

		assertEquals(24, responseList.getMin());
		assertEquals(144, responseList.getMax());
		assertEquals(84.0, responseList.getAverage());
		assertEquals(4, responseList.getList().size());
	}

	@Test
	public void testPostRequestNothing() {
		List<ExpressionStrings> list = new ArrayList<>();
		list.add(new ExpressionStrings("1f2", "f12", "pflus"));
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.addMessage("First parametr is not number");
		errorMessage.addMessage("Second parametr is not number");
		errorMessage.addMessage("Third parametr is not operation");
		errorMessage.setStatusCode(HttpStatus.BAD_REQUEST);

		when(validator.isCorrectExpression("1f2", "f12", "pflus")).thenReturn(errorMessage);

		ResponseList responseList = controller.postRequest(list);

		verify(validator, times(1)).isCorrectExpression("1f2", "f12", "pflus");
		verify(service, times(0)).calculate(any());
		assertEquals(1, responseList.getList().size());
	}
}
