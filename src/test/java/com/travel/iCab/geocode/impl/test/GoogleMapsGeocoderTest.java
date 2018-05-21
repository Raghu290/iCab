/**
 * 
 */
package com.travel.iCab.geocode.impl.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.travel.iCab.geocode.impl.GoogleMapsGeocoder;

/**
 * @author venkatc
 *
 */
public class GoogleMapsGeocoderTest {
	@Test
	public void geocodetest(){
		assertTrue(new GoogleMapsGeocoder("AIzaSyBhq_7ACryDVReeXL_Cml58P7GMsCBT4zg").geoCodeTest());
		System.out.println("Test case geocode from locationn is executed successfully");
	}

}
