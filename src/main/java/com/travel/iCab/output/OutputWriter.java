/**

 * 
 */
package com.travel.iCab.output;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.travel.iCab.domain.Cab;
import com.travel.iCab.domain.Employee;


public interface OutputWriter {
	/*
	 * Writes the sortedCabAllocationMap to an output Stream
	 */
	void saveRoutePlan(HashMap<Cab,ArrayList<Employee>> sortedCabAllocationMap) throws IOException;
	
	/*
	 * Closes the opened connections
	 */
	void close();
}
