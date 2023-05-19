package com.gill.calculation.springcalculationapplication.entities;

public class ResultOfExpression extends Expression {
	private Integer result;

	public ResultOfExpression() {
	}

	public ResultOfExpression(int result, Expression expression) {
		super(expression);
		this.result = result;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

}
