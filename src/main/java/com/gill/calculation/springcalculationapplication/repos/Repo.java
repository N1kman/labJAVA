package com.gill.calculation.springcalculationapplication.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gill.calculation.springcalculationapplication.databaseEntities.Result;

public interface Repo extends JpaRepository<Result, Integer> {
}
