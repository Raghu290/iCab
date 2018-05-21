package com.travel.iCab.geocode.impl;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.travel.iCab.domain.GeoLocation;
import com.travel.iCab.geocode.Geocoder;

public class GoogleMapsGeocoder implements Geocoder{
private GeoApiContext context;
	
	public GoogleMapsGeocoder(String apiKey){
		
		this.context = new GeoApiContext();
		this.context.setApiKey(apiKey);
			
	}

	public GeoLocation geoCode(String id, String address) {
		GeoLocation geoLocation = null;
		GeocodingResult[] results;
		try {
			
			results = GeocodingApi.geocode(context,address).await();
			
			if (results.length > 0) {
				geoLocation = new GeoLocation(id, results[0].geometry.location.lat, results[0].geometry.location.lng,getPostalCode(results[0]));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return geoLocation;
		
	}
	
	private String getPostalCode (GeocodingResult googleResult) {
		String postalCode = null;
		for (AddressComponent addressComponent: googleResult.addressComponents){
			for (AddressComponentType addressComponentType: addressComponent.types) {
				if (AddressComponentType.POSTAL_CODE.compareTo(addressComponentType) == 0) {
					postalCode = addressComponent.longName;
				}
			}
		}
		return postalCode;
	}
	 
	/**
	 * This is JUnit Test Method.
	 * @return
	 */
	
public boolean geoCodeTest() {
		
		GoogleMapsGeocoder googleMapsGeocoder = new GoogleMapsGeocoder("AIzaSyBhq_7ACryDVReeXL_Cml58P7GMsCBT4zg");
		String address = "90F, NMK Street, Ayanavaram, Chennai";
//		String address = "Nungambakkam, Chennai";
//		String address = "Infosys, Sholinganallur, Chennai";
		String id = "600484";
		GeoLocation geoLocation = googleMapsGeocoder.geoCode(id, address);
		
		System.out.println(geoLocation.getLatitude());
		System.out.println(geoLocation.getLongitude());
		
		if(geoLocation.getPostalCode().equalsIgnoreCase(id)){
			return true;
		}else{
			return false;
		}
		
		
		
	}

	
	
	
}
