/**
 * 
 */
package com.travel.iCab.domain;

public class Cab implements DropPoint{
	
	private String id;
	private int ratePerKM;
	private int capacity;
	private BaseLocation baseLocation;
	
	//Shadow variable
	private Employee nextDropEmp;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the ratePerKM
	 */
	public int getRatePerKM() {
		return ratePerKM;
	}

	/**
	 * @param ratePerKM the ratePerKM to set
	 */
	public void setRatePerKM(int ratePerKM) {
		this.ratePerKM = ratePerKM;
	}

	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
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
	/* (non-Javadoc)
	 * @see com.travel.iCab.domain.DropPoint#getGeoLocation()
	 */
	public GeoLocation getGeoLocation() {
		return baseLocation.getGeoLocation();
	}

	/* (non-Javadoc)
	 * @see com.travel.iCab.domain.DropPoint#getCab()
	 */
	public Cab getCab() {
		return this;
	}
	
	public long getCost (long distance) {
		return ratePerKM * distance;
	}

	/**
     * @param dropPoint never null
     * @return a positive number, the distance multiplied by 1000 to avoid floating point arithmetic rounding errors
     */
    public long getDistanceTo(DropPoint dropPoint) {
        return baseLocation.getDistanceTo(dropPoint);
    }
	
    public String toString() {
    	if (this.id == null) {
    		return super.toString();
    	}
		return new StringBuffer("Cab : "+id+", Capacity : "+capacity+", Rate : "+ratePerKM).toString();
    }
}
