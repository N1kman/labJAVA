package com.gill.calculation.springcalculationapplication.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gill.calculation.springcalculationapplication.entities.CacheMap;
import com.gill.calculation.springcalculationapplication.entities.Expression;
import com.gill.calculation.springcalculationapplication.entities.ResultOfExpression;
import com.gill.calculation.springcalculationapplication.services.CalculationService;

@Service
public class CalculationServiceImpl implements CalculationService {
	/* Cache */
	private CacheMap cacheMap;
	/* Logger */
	private static final Logger logger = LoggerFactory.getLogger(CalculationServiceImpl.class);
	/* Counter */
	private CounterOfCallsService counterOfCallsService;

	@Autowired
	public CalculationServiceImpl(CacheMap cacheMap) {
		this.cacheMap = cacheMap;
	}

	@Override
	public ResultOfExpression calculate(Expression expression) {
		ResultOfExpression responseByCache;
		// add call
		counterOfCallsService.сountNotSynchronizedCalls(1);
		counterOfCallsService.сountCalls(1);

		int result = 0; // result of calculation
		String operation = expression.getOperation(); // get operation

		// try to get cache
		responseByCache = cacheMap.getCache(expression.toString());
		if (responseByCache != null) {
			return responseByCache;
		} 
		
		// checking operation
		switch (operation) {
		case "minus":
			logger.info("Performing subtraction");
			result = expression.getNumberFirst() - expression.getNumberSecond();
			break;

		case "plus":
			logger.info("Performing addition");
			result = expression.getNumberFirst() + expression.getNumberSecond();
			break;

		case "divide":
			logger.info("Performing division");
			result = expression.getNumberFirst() / expression.getNumberSecond();
			break;

		case "multiply":
			logger.info("Performing multiplacation");
			result = expression.getNumberFirst() * expression.getNumberSecond();
			break;
		}

		// get result class
		ResultOfExpression resultOfExpression = new ResultOfExpression(result, expression);
		return resultOfExpression;
	}

	@Autowired
	public void setCounterOfCallsService(CounterOfCallsService counterOfCallsService) {
		this.counterOfCallsService = counterOfCallsService;
	}

	public Integer countMin(List<ResultOfExpression> responseList) {
		return responseList.stream().mapToInt(iter -> iter.getResult()).min().orElse(Integer.MAX_VALUE);
	}

	public Integer countMax(List<ResultOfExpression> responseList) {
		return responseList.stream().mapToInt(iter -> iter.getResult()).max().orElse(Integer.MIN_VALUE);
	}

	public Double countAverage(List<ResultOfExpression> responseList) {
		return responseList.stream().mapToInt(iter -> iter.getResult()).average().orElse(Double.MIN_VALUE);
	}
}
