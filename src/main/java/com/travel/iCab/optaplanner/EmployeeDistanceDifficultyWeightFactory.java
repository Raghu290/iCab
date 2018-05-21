/**
 * 
 */
package com.travel.iCab.optaplanner;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

import com.travel.iCab.domain.BaseLocation;
import com.travel.iCab.domain.Employee;


public class EmployeeDistanceDifficultyWeightFactory
		implements SelectionSorterWeightFactory<CabAllocationSolution, Employee> {

	/* (non-Javadoc)
	 * @see org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory#createSorterWeight(org.optaplanner.core.api.domain.solution.Solution, java.lang.Object)
	 */
	public Comparable createSorterWeight(CabAllocationSolution cabAllocationSolution, Employee employee) {
		BaseLocation baseLocation = cabAllocationSolution.getBaseLocation();
		return new EmployeeDistanceDifficultyWeight(employee, baseLocation.getGeoLocation().getDistanceTo(employee.getGeoLocation())
				+ employee.getGeoLocation().getDistanceTo(baseLocation.getGeoLocation()));
		
	}
	
	 public static class EmployeeDistanceDifficultyWeight
     implements Comparable<EmployeeDistanceDifficultyWeight> {

		 private final Employee employee;
		 private final long roundTripDistance;
		 
		 public EmployeeDistanceDifficultyWeight (Employee employee, long roundTripDistance) {
			 this.employee = employee;
			 this.roundTripDistance = roundTripDistance;
		 }
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(EmployeeDistanceDifficultyWeight other) {
			return new CompareToBuilder()
                    .append(roundTripDistance, other.roundTripDistance) // Ascending (further from the depot are more difficult)
//					  .append(other.roundTripDistance, roundTripDistance ) // Ascending (nearer from the depot are more difficult)
                    .append(employee.getEmpID(), other.employee.getEmpID())
                    .toComparison();
		}
		 
	 }

}
