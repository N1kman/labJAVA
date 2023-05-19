package com.gill.calculation.springcalculationapplication.entities;

public class ExpressionStrings {

	private String numberFirst;
	private String numberSecond;
	private String operation;

	public ExpressionStrings(String numberFirst, String numberSecond, String operation) {
		this.numberFirst = numberFirst;
		this.numberSecond = numberSecond;
		this.operation = operation;
	}

	public String getNumberFirst() {
		return numberFirst;
	}

	public void setNumberFirst(String numberFirst) {
		this.numberFirst = numberFirst;
	}

	public String getNumberSecond() {
		return numberSecond;
	}

	public void setNumberSecond(String numberSecond) {
		this.numberSecond = numberSecond;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

}
