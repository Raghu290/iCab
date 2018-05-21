/**
 * 
 */
package com.travel.iCab.distance.impl.test;

import static org.junit.Assert.*;



import org.junit.Test;

import com.travel.iCab.distance.impl.GraphHopperDistanceMatrixCreater;



/**
 * @author venkatc
 *
 */
public class GraphHopperDistanceMatrixCreaterTest {
	
	@Test	
	public void createDistanceMatrix(){
		
		assertTrue(new GraphHopperDistanceMatrixCreater().createDistanceMatrix());
		System.out.println("Test case create distance matrix completed");	
		
	}
	
	
	
	
	
}
