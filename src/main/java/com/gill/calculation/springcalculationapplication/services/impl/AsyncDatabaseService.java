package com.gill.calculation.springcalculationapplication.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gill.calculation.springcalculationapplication.databaseEntities.AsyncResult;
import com.gill.calculation.springcalculationapplication.repos.AsyncRepo;

@Service
public class AsyncDatabaseService {
	@Autowired 
	private AsyncRepo repo;
	
	public AsyncResult getById(Integer id){
		return repo.findById(id).orElse(null);
	}
	
	public void saveResult(AsyncResult result){
		repo.save(result);
	}
}
