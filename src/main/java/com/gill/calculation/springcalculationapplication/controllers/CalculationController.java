package com.gill.calculation.springcalculationapplication.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.gill.calculation.springcalculationapplication.entities.Response;
import com.gill.calculation.springcalculationapplication.entities.ResponseList;
import com.gill.calculation.springcalculationapplication.entities.ResultOfExpression;
import com.gill.calculation.springcalculationapplication.exceptions.ErrorMessage;
import com.gill.calculation.springcalculationapplication.exceptions.InternalServerException;
import com.gill.calculation.springcalculationapplication.exceptions.NotValidParametrsException;
import com.gill.calculation.springcalculationapplication.annotations.CustomExceptionHandler;
import com.gill.calculation.springcalculationapplication.databaseEntities.Result;
import com.gill.calculation.springcalculationapplication.entities.CacheMap;
import com.gill.calculation.springcalculationapplication.entities.CounterOfCalls;
import com.gill.calculation.springcalculationapplication.entities.Expression;
import com.gill.calculation.springcalculationapplication.entities.ExpressionStrings;
import com.gill.calculation.springcalculationapplication.services.CalculationService;
import com.gill.calculation.springcalculationapplication.services.impl.DatabaseService;
import com.gill.calculation.springcalculationapplication.validators.ValidatorExpression;

@RestController
@CustomExceptionHandler
@RequestMapping("/api/version1")
public class CalculationController {

	/* Service */
	private CalculationService calculationInts;
	/* Cache */
	private CacheMap cacheMap;
	/* Validator */
	private ValidatorExpression validatorExpression;
	/* Logger */
	private static final Logger logger = LoggerFactory.getLogger(CalculationController.class);
	/* Counter */
	private CounterOfCalls counterOfCalls;
	/* Database */
	private DatabaseService databaseService;

	@Autowired
	public void setCounterOfCalls(CounterOfCalls counterOfCalls) {
		this.counterOfCalls = counterOfCalls;
	}

	@Autowired
	public CalculationController(CalculationService calculationInts, ValidatorExpression validatorExpression,
			DatabaseService databaseService, CacheMap cacheMap) {
		this.calculationInts = calculationInts;
		this.validatorExpression = validatorExpression;
		this.databaseService = databaseService;
		this.cacheMap = cacheMap;
	}
	
	public void setCacheMap(CacheMap cacheMap) {
		this.cacheMap = cacheMap;
	}

	/*
	 * Gets result of expression
	 */
	@GetMapping("/getResult")
	@ResponseStatus(HttpStatus.OK)
	public Response getResultExpression(@RequestParam(defaultValue = "0") String numberFirst,
			@RequestParam(defaultValue = "0") String numberSecond,
			@RequestParam(defaultValue = "plus") String operation) {

		ErrorMessage errorMessage = validatorExpression.isCorrectExpression(numberFirst, numberSecond, operation);

		if (errorMessage.getMessages().size() != 0) {
			throw new NotValidParametrsException(errorMessage);
		}

		try {
			Expression expression = new Expression(Integer.parseInt(numberFirst), Integer.parseInt(numberSecond),
					operation);
			// try to get cache
			ResultOfExpression resultOfExpression = cacheMap.getCache(expression.toString());
			if (resultOfExpression == null) {
				logger.info("Take from database");
				Iterable<Result> list = (databaseService.getAllResults());
				for (Result data : list) {
					// safe to cache
					cacheMap.putCache(data.getHash(), new ResultOfExpression(data.getResult(),
							new Expression(data.getNumberFirst(), data.getNumberSecond(), data.getOperation())));
				}
			}
			
			resultOfExpression = calculationInts.calculate(expression);
			
			if (cacheMap.haveCacheByValue(resultOfExpression) == false) {
				logger.info("Put to cache");
				cacheMap.putCache(expression.toString(), resultOfExpression);
				logger.info("Put to database");
				databaseService.saveResult(new Result(resultOfExpression.getNumberFirst(),
						resultOfExpression.getNumberSecond(), resultOfExpression.getOperation(),
						resultOfExpression.getResult(), resultOfExpression.toString()));
			}
			
			return new Response(resultOfExpression);

		} catch (Exception exception) {

			logger.error(exception.getMessage());
			errorMessage.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			errorMessage.addMessage(exception.getMessage());
			throw new InternalServerException(errorMessage);
		}
	}

	/*
	 * Post request
	 */
	@PostMapping("/post")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseList postRequest(@RequestBody List<ExpressionStrings> list) {
		ResponseList responseList = new ResponseList();
		List<ResultOfExpression> dataForIndicators = new ArrayList<>();

		list.forEach((iter) -> {
			ErrorMessage errorMessage = validatorExpression.isCorrectExpression(iter.getNumberFirst(),
					iter.getNumberSecond(), iter.getOperation());

			if (errorMessage.getMessages().size() != 0) {
				responseList.getList().add(new Response(errorMessage));
			} else {
				try {
					Expression expression = new Expression(Integer.parseInt(iter.getNumberFirst()),
							Integer.parseInt(iter.getNumberSecond()), iter.getOperation());
					ResultOfExpression resultOfExpression = cacheMap.getCache(expression.toString());
					if (resultOfExpression == null) {
						logger.info("Take from database");
						Iterable<Result> listData = (databaseService.getAllResults());
						for (Result data : listData) {
							// safe to cache
							cacheMap.putCache(data.getHash(),
									new ResultOfExpression(data.getResult(), new Expression(data.getNumberFirst(),
											data.getNumberSecond(), data.getOperation())));
						}
					}
					
					resultOfExpression = calculationInts.calculate(expression);

					if (cacheMap.haveCacheByValue(resultOfExpression) == false) {
						logger.info("Put to cache");
						cacheMap.putCache(expression.toString(), resultOfExpression);
						logger.info("Put to database");
						databaseService.saveResult(new Result(resultOfExpression.getNumberFirst(),
								resultOfExpression.getNumberSecond(), resultOfExpression.getOperation(),
								resultOfExpression.getResult(), resultOfExpression.toString()));
					}

					responseList.getList().add(new Response(resultOfExpression));
					dataForIndicators.add(resultOfExpression);

				} catch (Exception exception) {
					logger.error(exception.getMessage());
					errorMessage.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
					errorMessage.addMessage(exception.getMessage());
					responseList.getList().add(new Response(errorMessage));
				}
			}
		});

		if (dataForIndicators.size() != 0) {
			responseList.setMin(calculationInts.countMin(dataForIndicators));
			responseList.setMax(calculationInts.countMax(dataForIndicators));
			responseList.setAverage(calculationInts.countAverage(dataForIndicators));
		}

		return responseList;
	}

	@GetMapping("/getAll")
	public Iterable<Result> getAllResults() {
		return databaseService.getAllResults();
	}

	/*
	 * Gets counter
	 */
	@GetMapping("/getCounterOfCalls")
	@ResponseStatus(HttpStatus.OK)
	public CounterOfCalls getCounterOfCalls() {
		return this.counterOfCalls;
	}

	/*
	 * Gets cache
	 */
	@GetMapping("/getCacheMap")
	@ResponseStatus(HttpStatus.OK)
	public CacheMap getCache() {
		return cacheMap;
	}

	/*
	 * Gets keys of cache
	 */
	@GetMapping("/getCacheMap/keys")
	@ResponseStatus(HttpStatus.OK)
	public Set<String> getCacheKeys() {
		return cacheMap.getMap().keySet();
	}

	/*
	 * Gets values of cache
	 */
	@GetMapping("/getCacheMap/values")
	@ResponseStatus(HttpStatus.OK)
	public Collection<ResultOfExpression> getCacheValues() {
		return cacheMap.getMap().values();
	}

}
