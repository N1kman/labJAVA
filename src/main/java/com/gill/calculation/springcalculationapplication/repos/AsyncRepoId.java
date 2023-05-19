package com.gill.calculation.springcalculationapplication.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gill.calculation.springcalculationapplication.databaseEntities.AsyncId;

public interface AsyncRepoId extends JpaRepository<AsyncId, Integer> {

}
