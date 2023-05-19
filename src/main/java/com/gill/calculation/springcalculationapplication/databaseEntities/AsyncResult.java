package com.gill.calculation.springcalculationapplication.databaseEntities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "asycresults")
public class AsyncResult {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "program_id")
	private Integer programId;
	@Column(name = "number_first")
	private Integer numberFirst;
	@Column(name = "operation")
	private String operation;
	@Column(name = "number_second")
	private Integer numberSecond;
	@Column(name = "result")
	private Integer result;

	public AsyncResult() {
	}
	
	public AsyncResult(Integer programId, Integer numberFirst, String operation, Integer numberSecond, Integer result) {
		this.programId = programId;
		this.numberFirst = numberFirst;
		this.operation = operation;
		this.numberSecond = numberSecond;
		this.result = result;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Integer getNumberFirst() {
		return numberFirst;
	}

	public void setNumberFirst(Integer numberFirst) {
		this.numberFirst = numberFirst;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Integer getNumberSecond() {
		return numberSecond;
	}

	public void setNumberSecond(Integer numberSecond) {
		this.numberSecond = numberSecond;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
