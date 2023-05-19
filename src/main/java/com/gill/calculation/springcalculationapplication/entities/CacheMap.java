package com.gill.calculation.springcalculationapplication.entities;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gill.calculation.springcalculationapplication.controllers.CalculationController;

@Component
public class CacheMap {
	/*Logger*/
	private static final Logger logger = LoggerFactory.getLogger(CalculationController.class);
	/*Map for cache*/
	HashMap<String, ResultOfExpression> map = new HashMap<>();

	public void putCache(String string, ResultOfExpression resultOfExpression) {
		logger.info("Put cache " + string);
		map.put(string, resultOfExpression);
	}
	
	public ResultOfExpression getCache(String string) {
		logger.info("Get cache by " + string);
		return map.get(string);
	}
	
	public boolean haveCacheByValue(ResultOfExpression resultOfExpression) {
		logger.info("Check cache by value");
		return map.containsValue(resultOfExpression);
	}
	
	public HashMap<String, ResultOfExpression> getMap(){
		return map;
	}

}
