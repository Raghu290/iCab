/**
 * 
 */
package com.travel.iCab.optaplanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

import com.travel.iCab.domain.BaseLocation;
import com.travel.iCab.domain.Cab;
import com.travel.iCab.domain.Employee;


@PlanningSolution
public class CabAllocationSolution implements Solution<HardSoftLongScore> {

	private ArrayList<Employee> employeeList;
	private ArrayList<Cab> cabList;
	private BaseLocation baseLocation;
	
	private HardSoftLongScore score;
	
	/**
	 * @return the employeeList
	 */
	@PlanningEntityCollectionProperty
    @ValueRangeProvider(id = "employeeRange")
	public ArrayList<Employee> getEmployeeList() {
		return employeeList;
	}

	/**
	 * @param employeeList the employeeList to set
	 */
	public void setEmployeeList(ArrayList<Employee> employeeList) {
		this.employeeList = employeeList;
	}

	/**
	 * @return the cabList
	 */
	@PlanningEntityCollectionProperty
    @ValueRangeProvider(id = "cabRange")
	public ArrayList<Cab> getCabList() {
		return cabList;
	}

	/**
	 * @param cabList the cabList to set
	 */
	public void setCabList(ArrayList<Cab> cabList) {
		this.cabList = cabList;
	}

	/**
	 * @return the baseLocation
	 */
	public BaseLocation getBaseLocation() {
		return baseLocation;
	}

	/**
	 * @param baseLocation the baseLocation to set
	 */
	public void setBaseLocation(BaseLocation baseLocation) {
		this.baseLocation = baseLocation;
	}

	/* (non-Javadoc)
	 * @see org.optaplanner.core.api.domain.solution.Solution#getProblemFacts()
	 */
	public Collection<? extends Object> getProblemFacts() {
		List<Object> facts = new ArrayList<Object>();
		facts.addAll(cabList);
		facts.addAll(employeeList);
		return facts;
	}

	/* (non-Javadoc)
	 * @see org.optaplanner.core.api.domain.solution.Solution#getScore()
	 */
	public HardSoftLongScore getScore() {
		return score;
	}

	/* (non-Javadoc)
	 * @see org.optaplanner.core.api.domain.solution.Solution#setScore(org.optaplanner.core.api.score.Score)
	 */
	public void setScore(HardSoftLongScore score) {
		this.score = score;
		
	}


}
