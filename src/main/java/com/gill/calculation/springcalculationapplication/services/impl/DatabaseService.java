package com.gill.calculation.springcalculationapplication.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gill.calculation.springcalculationapplication.databaseEntities.Result;
import com.gill.calculation.springcalculationapplication.repos.Repo;

@Service
public class DatabaseService {
	@Autowired 
	private Repo repo;
	
	public Iterable<Result> getAllResults(){
		return repo.findAll();
	}
	
	public void saveResult(Result result){
		repo.save(result);
	}
}
