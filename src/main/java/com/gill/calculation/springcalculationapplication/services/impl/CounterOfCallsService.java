package com.gill.calculation.springcalculationapplication.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gill.calculation.springcalculationapplication.entities.CounterOfCalls;

@Service
public class CounterOfCallsService {

	/* Counter */
	private CounterOfCalls counterOfCalls;
	/* Logger */
	private static final Logger logger = LoggerFactory.getLogger(CalculationServiceImpl.class);
	
	@Autowired
	public CounterOfCallsService(CounterOfCalls counterOfCalls){
		this.counterOfCalls = counterOfCalls;
	}
	
	public void сountCalls(long number) {
		counterOfCalls.addToCounter(number);
		logger.info("Number of call is " + counterOfCalls.getCounter());
	}
	
	public void сountNotSynchronizedCalls(long number) {
		counterOfCalls.addToNotSynchronizedCounter(number);
	}
	
}
