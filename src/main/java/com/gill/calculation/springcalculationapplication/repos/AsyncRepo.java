package com.gill.calculation.springcalculationapplication.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gill.calculation.springcalculationapplication.databaseEntities.AsyncResult;

public interface AsyncRepo extends JpaRepository<AsyncResult, Integer> {
}
