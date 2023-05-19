package com.gill.calculation.springcalculationapplication.entities;

public class Expression {
	private Integer numberFirst;
	private Integer numberSecond;
	private String operation;

	public Expression() {
	}

	public Expression(Expression obj) {
		this.numberFirst = obj.numberFirst;
		this.numberSecond = obj.numberSecond;
		this.operation = obj.operation;
	}

	public Expression(Integer numberFirst, Integer numberSecond, String operation) {
		this.numberFirst = numberFirst;
		this.numberSecond = numberSecond;
		this.operation = operation;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj.getClass() != this.getClass()) {
			return false;
		}

		Expression expression = (Expression) obj;

		return (expression.numberFirst.equals(this.numberFirst) && expression.numberSecond.equals(this.numberSecond)
				&& expression.operation.equals(this.operation));
	}

	@Override
	public int hashCode() {
		int result = 0;
		result += numberFirst.hashCode();
		result += numberSecond.hashCode();
		result += operation.hashCode();
		return result;
	}

	public Integer getNumberFirst() {
		return numberFirst;
	}

	public void setNumberFirst(Integer numberFirst) {
		this.numberFirst = numberFirst;
	}

	public Integer getNumberSecond() {
		return numberSecond;
	}

	public void setNumberSecond(Integer numberSecond) {
		this.numberSecond = numberSecond;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public String toString() {
		return numberFirst + " " + operation + " " + numberSecond;
	}
}
