package com.gill.calculation.springcalculationapplication.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gill.calculation.springcalculationapplication.databaseEntities.AsyncId;

import com.gill.calculation.springcalculationapplication.repos.AsyncRepoId;

@Service
public class AsyncDatabaseIncrementService {
	@Autowired
	AsyncRepoId asyncRepoId;
	
	public Integer generateId() {
		AsyncId asyncId = new AsyncId();
		asyncRepoId.save(asyncId);
		asyncRepoId.deleteById(asyncId.getId() - 1);
		return asyncId.getId();
	}
}
