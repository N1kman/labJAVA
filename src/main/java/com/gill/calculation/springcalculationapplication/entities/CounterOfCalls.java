package com.gill.calculation.springcalculationapplication.entities;

import org.springframework.stereotype.Component;

@Component
public class CounterOfCalls {

	private volatile long counter = 0;
	private long notSynchronizedCounter = 0;

	public synchronized long getCounter() {
		return this.counter;
	}

	public long getNotSynchronizedCounter() {
		return this.notSynchronizedCounter;
	}

	public void addToNotSynchronizedCounter(long number) {
		this.notSynchronizedCounter += number;
	}

	public synchronized void addToCounter(long number) {
		this.counter += number;
	}
}
