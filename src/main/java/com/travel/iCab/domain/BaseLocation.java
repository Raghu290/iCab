package com.travel.iCab.domain;

public class BaseLocation {
	
	private GeoLocation geoLocation;

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

	
	  public long getDistanceTo(DropPoint dropPoint) {
	        return geoLocation.getDistanceTo(dropPoint.getGeoLocation());
	    }
	

}
