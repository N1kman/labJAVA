package com.gill.calculation.springcalculationapplication.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseList {
	private Integer maxResult;
	private Integer minResult;
	private Double averageResult;
	private List<Response> list = new ArrayList<>();
	
	public List<Response> getList(){
		return this.list;
	}

	public Integer getMax() {
		return maxResult;
	}

	public void setMax(Integer max) {
		this.maxResult = max;
	}

	public Integer getMin() {
		return minResult;
	}

	public void setMin(Integer min) {
		this.minResult = min;
	}

	public Double getAverage() {
		return averageResult;
	}

	public void setAverage(Double average) {
		this.averageResult = average;
	}
	
}
