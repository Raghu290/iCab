/**
 * 
 */
package com.travel.iCab.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

import com.travel.iCab.optaplanner.EmployeeDistanceDifficultyWeightFactory;

@PlanningEntity(difficultyWeightFactoryClass = EmployeeDistanceDifficultyWeightFactory.class)
public class Employee implements DropPoint{

	private String empID;
	private String name;
	private String gender;
	
	private Location location;
	private GeoLocation geoLocation;
	
	// Planning variables which changes during planning, between score calculations.
	private DropPoint previousDropPoint;
	
	// Shadow variables
	private Employee nextDropEmp;
	private Cab cab;
	
	/**
	 * @return the empID
	 */
	public String getEmpID() {
		return empID;
	}
	/**
	 * @param empID the empID to set
	 */
	public void setEmpID(String empID) {
		this.empID = empID;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the sex
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	/**
	 * @return the geoLocation
	 */
	public GeoLocation getGeoLocation() {
		return geoLocation;
	}
	/**
	 * @param geoLocation the geoLocation to set
	 */
	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}
	/**
	 * @return the previousDropEmp
	 */
	@PlanningVariable(valueRangeProviderRefs = {"cabRange", "employeeRange"},
            graphType = PlanningVariableGraphType.CHAINED)
	public DropPoint getPreviousDropPoint() {
		return previousDropPoint;
	}
	/**
	 * @param previousDropEmp the previousDropEmp to set
	 */
	public void setPreviousDropPoint(DropPoint previousDropPoint) {
		this.previousDropPoint = previousDropPoint;
	}
	/**
	 * @return the nextDropEmp
	 */
	public Employee getNextDropEmp() {
		return nextDropEmp;
	}
	/**
	 * @param nextDropEmp the nextDropEmp to set
	 */
	public void setNextDropEmp(Employee nextDropEmp) {
		this.nextDropEmp = nextDropEmp;
	}
	/**
	 * @return the cab
	 */
	@AnchorShadowVariable(sourceVariableName = "previousDropPoint")
	public Cab getCab() {
		return cab;
	}
	/**
	 * @param cab the cab to set
	 */
	public void setCab(Cab cab) {
		this.cab = cab;
	}
	
	public long getDistanceFromPreviousDropPoint() {
		if (previousDropPoint == null) 
			return 0L;
		return getDistanceFrom(previousDropPoint);
	}
	
	public long getDistanceFrom(DropPoint dropPoint) {
		return dropPoint.getGeoLocation().getDistanceTo(geoLocation);
	}
	
	public long getDistanceTo(DropPoint dropPoint) {
		return geoLocation.getDistanceTo(dropPoint.getGeoLocation());
	}
	/**
	 * @return
	 */
	public boolean isFemale() {
		if (gender != null && gender.equalsIgnoreCase("Female")){
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		
		if (empID == null)
			return super.toString();
		
		return new StringBuffer("EmpID : "+empID+", Name : "+name+", Gender : "+gender+", Address : "+location.getAddress()).toString();
	}
}
