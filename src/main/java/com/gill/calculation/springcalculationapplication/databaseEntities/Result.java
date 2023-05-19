package com.gill.calculation.springcalculationapplication.databaseEntities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "results")
public class Result {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer numberFirst;
	private Integer numberSecond;
	private String operation;
	private Integer result;
	private String hash;

	public Result(Integer numberFirst, Integer numberSecond, String operation, Integer result, String hash) {
		this.numberFirst = numberFirst;
		this.numberSecond = numberSecond;
		this.operation = operation;
		this.result = result;
		this.hash = hash;
	}

	public Result() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
}
