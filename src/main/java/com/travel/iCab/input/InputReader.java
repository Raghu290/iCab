/**
 * 
 */
package com.travel.iCab.input;

import java.util.ArrayList;

import com.travel.iCab.domain.Cab;
import com.travel.iCab.domain.Employee;


public interface InputReader {
	
	/*
	 * Gets the list of Employees
	 */
	ArrayList<Employee> getEmployeeList();
	
	/*
	 * Gets the list of cabs
	 */
	ArrayList<Cab> getCabList();
	
	/*
	 * Closes all connection related things
	 */
	public void close();
	
}
