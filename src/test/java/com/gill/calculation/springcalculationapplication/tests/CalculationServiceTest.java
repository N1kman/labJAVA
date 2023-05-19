package com.gill.calculation.springcalculationapplication.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gill.calculation.springcalculationapplication.entities.CacheMap;
import com.gill.calculation.springcalculationapplication.entities.CounterOfCalls;
import com.gill.calculation.springcalculationapplication.entities.Expression;
import com.gill.calculation.springcalculationapplication.entities.ResultOfExpression;
import com.gill.calculation.springcalculationapplication.services.impl.CalculationServiceImpl;
import com.gill.calculation.springcalculationapplication.services.impl.CounterOfCallsService;

public class CalculationServiceTest {

	private CalculationServiceImpl service;
	static private Expression expression;

	@BeforeAll
	static void setUp() throws Exception {
		expression = new Expression();
	}

	@BeforeEach
	public void setParametrs() throws Exception {
		service = new CalculationServiceImpl(new CacheMap());
		service.setCounterOfCallsService(new CounterOfCallsService(new CounterOfCalls()));
		expression.setNumberFirst(12);
		expression.setNumberSecond(4);
	}

	@Test
	public void testCalculationPlus() {
		expression.setOperation("plus");
		ResultOfExpression resultOfExpression = service.calculate(expression);
		assertEquals(16, resultOfExpression.getResult());
	}

	@Test
	public void testCalculationMinus() {
		expression.setOperation("minus");
		ResultOfExpression resultOfExpression = service.calculate(expression);
		assertEquals(8, resultOfExpression.getResult());
	}

	@Test
	public void testCalculationMultiply() {
		expression.setOperation("multiply");
		ResultOfExpression resultOfExpression = service.calculate(expression);
		assertEquals(48, resultOfExpression.getResult());
	}

	@Test
	public void testCalculationDivide() {
		expression.setOperation("divide");
		ResultOfExpression resultOfExpression = service.calculate(expression);
		assertEquals(3, resultOfExpression.getResult());
		CacheMap cache  = new CacheMap();
		cache.putCache("12 divide 4", resultOfExpression);
		service = new CalculationServiceImpl(cache);
		service.setCounterOfCallsService(new CounterOfCallsService(new CounterOfCalls()));
		resultOfExpression = service.calculate(expression);
		assertEquals(3, resultOfExpression.getResult());
	}

	@Test
	public void testCalculationDivideWithException() {
		expression.setOperation("divide");
		expression.setNumberSecond(0);
		Throwable exception = assertThrows(ArithmeticException.class, () -> service.calculate(expression));
		assertEquals("/ by zero", exception.getMessage());
	}

	@Test
	public void testIndicators() {
		List<ResultOfExpression> list = new ArrayList<>();
		list.add(new ResultOfExpression(30, new Expression(10, 20, "plus")));
		list.add(new ResultOfExpression(20, new Expression(10, 2, "multiply")));
		list.add(new ResultOfExpression(10, new Expression(20, 2, "divide")));

		assertEquals(10, service.countMin(list));
		assertEquals(30, service.countMax(list));
		assertEquals(20, service.countAverage(list));
	}
}
