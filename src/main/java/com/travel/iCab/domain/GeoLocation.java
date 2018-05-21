
package com.travel.iCab.domain;

import java.util.Map;


public class GeoLocation {
	
	private String id;
	private double longitude;
	private double latitude;
	private String postalCode;
	private Map<GeoLocation, Double> travelDistanceMap;
	
	public GeoLocation(String id, double latitude, double longitude, String postalCode) {
		this.setId(id);
		this.latitude = latitude;
		this.longitude = longitude;
		this.postalCode = postalCode;
	}
	
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
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the travelDistanceMap
	 */
	public Map<GeoLocation, Double> getTravelDistanceMap() {
		return travelDistanceMap;
	}

	/**
	 * @param travelDistanceMap the travelDistanceMap to set
	 */
	public void setTravelDistanceMap(Map<GeoLocation, Double> travelDistanceMap) {
		this.travelDistanceMap = travelDistanceMap;
	}
	
	public long getDistanceTo(GeoLocation geoLocation) {
		if (this == geoLocation) {
			return 0L;
		}
		
		double distance = travelDistanceMap.get(geoLocation);
		// Multiplied by 1000 to avoid floating point arithmetic rounding errors
        return (long) (distance * 1000.0);
	}

}
