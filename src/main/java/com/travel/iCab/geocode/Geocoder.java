package com.travel.iCab.geocode;

import com.travel.iCab.domain.GeoLocation;

public interface Geocoder {
	

	public GeoLocation geoCode(String id, String address);


}
