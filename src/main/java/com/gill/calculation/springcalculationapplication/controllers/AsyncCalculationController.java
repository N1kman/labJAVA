package com.gill.calculation.springcalculationapplication.controllers;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gill.calculation.springcalculationapplication.annotations.CustomExceptionHandler;
import com.gill.calculation.springcalculationapplication.databaseEntities.AsyncResult;
import com.gill.calculation.springcalculationapplication.entities.Expression;
import com.gill.calculation.springcalculationapplication.entities.ResponseId;
import com.gill.calculation.springcalculationapplication.entities.ResultOfExpression;
import com.gill.calculation.springcalculationapplication.exceptions.ErrorMessage;
import com.gill.calculation.springcalculationapplication.exceptions.InternalServerException;
import com.gill.calculation.springcalculationapplication.exceptions.NotValidParametrsException;
import com.gill.calculation.springcalculationapplication.services.CalculationService;
import com.gill.calculation.springcalculationapplication.services.impl.AsyncDatabaseIncrementService;
import com.gill.calculation.springcalculationapplication.services.impl.AsyncDatabaseService;
import com.gill.calculation.springcalculationapplication.validators.ValidatorExpression;

@RestController
@CustomExceptionHandler
@RequestMapping("/api/async")
public class AsyncCalculationController {
	/* Logger */
	private static final Logger logger = LoggerFactory.getLogger(CalculationController.class);
	/* Database */
	private AsyncDatabaseService asyncDatabaseService;
	/* Database */
	private AsyncDatabaseIncrementService asyncDatabaseIncrementService;
	/* Service */
	private CalculationService calculationInts;
	/* Validator */
	private ValidatorExpression validatorExpression;
	
	@Autowired
	public AsyncCalculationController(AsyncDatabaseService asyncDatabaseService,
			AsyncDatabaseIncrementService asyncDatabaseIncrementService, CalculationService calculationInts,
			ValidatorExpression validatorExpression) {
		this.asyncDatabaseService = asyncDatabaseService;
		this.asyncDatabaseIncrementService = asyncDatabaseIncrementService;
		this.calculationInts = calculationInts;
		this.validatorExpression = validatorExpression;
	}

	@GetMapping("/count")
	@ResponseStatus(HttpStatus.OK)
	public ResponseId getResultExpression(@RequestParam(defaultValue = "0") String numberFirst,
			@RequestParam(defaultValue = "0") String numberSecond,
			@RequestParam(defaultValue = "plus") String operation) {
		logger.info("Validation");
		ErrorMessage errorMessage = validatorExpression.isCorrectExpression(numberFirst, numberSecond, operation);

		if (errorMessage.getMessages().size() != 0) {
			throw new NotValidParametrsException(errorMessage);
		}

		logger.info("Generate id");
		ResponseId responseId = new ResponseId(asyncDatabaseIncrementService.generateId(), "Starting colculation...");

		CompletableFuture.runAsync(new Runnable() {
			@Override
			public void run() {
				logger.info("Starting colculation");
				ResultOfExpression resultOfExpression = calculationInts.calculate(
						new Expression(Integer.parseInt(numberFirst), Integer.parseInt(numberSecond), operation));
				asyncDatabaseService.saveResult(new AsyncResult(responseId.getId(), resultOfExpression.getNumberFirst(),
						resultOfExpression.getOperation(), resultOfExpression.getNumberSecond(),
						resultOfExpression.getResult()));
				logger.info("Ending colculation");
			}
		});
		
		logger.info("Return id");
		return responseId;
	}
	
	@GetMapping("/getById")
	@ResponseStatus(HttpStatus.OK)
	public AsyncResult getById(@RequestParam(required=true) Integer id) {
		AsyncResult asyncResult = asyncDatabaseService.getById(id);
		if(asyncResult == null) {
			ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Id is not found");
			throw new InternalServerException(errorMessage);
		} 
		return asyncResult;
	}
}
