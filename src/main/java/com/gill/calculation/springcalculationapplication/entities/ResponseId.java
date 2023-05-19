package com.gill.calculation.springcalculationapplication.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseId {
	private Integer id;
	private String process;

	public ResponseId(Integer id, String process) {
		this.id = id;
		this.process = process;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

}
