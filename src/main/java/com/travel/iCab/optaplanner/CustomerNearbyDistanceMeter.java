/**
 * 
 */
package com.travel.iCab.optaplanner;

import org.optaplanner.core.impl.heuristic.selector.common.nearby.NearbyDistanceMeter;

import com.travel.iCab.domain.DropPoint;
import com.travel.iCab.domain.Employee;


public class CustomerNearbyDistanceMeter implements NearbyDistanceMeter<Employee, DropPoint> {

	
	public double getNearbyDistance(Employee origin, DropPoint destination) {
		System.out.println("Origin "+origin.getEmpID()+"  cab "+origin.getCab());
		System.out.println("Destination "+destination);
		try {
			
		} catch (Exception e) {
			
		}
		System.out.println(origin.getDistanceTo(destination));
		return origin.getDistanceTo(destination);
	}

}
