package com.gill.calculation.springcalculationapplication.services;

import java.util.List;

import com.gill.calculation.springcalculationapplication.entities.Expression;
import com.gill.calculation.springcalculationapplication.entities.ResultOfExpression;

public interface CalculationService {
	ResultOfExpression calculate(Expression expression);
	Integer countMin(List<ResultOfExpression> responseList);
	Integer countMax(List<ResultOfExpression> responseList);
	Double countAverage(List<ResultOfExpression> responseList);
}
