/**
 * 
 */
package com.travel.iCab.manager;

import java.io.File;
import java.util.ArrayList;

import com.travel.iCab.domain.BaseLocation;
import com.travel.iCab.domain.Cab;
import com.travel.iCab.domain.Employee;
import com.travel.iCab.domain.GeoLocation;
import com.travel.iCab.geocode.Geocoder;
import com.travel.iCab.geocode.impl.GoogleMapsGeocoder;
import com.travel.iCab.input.InputReader;
import com.travel.iCab.input.impl.CsvReader;


public class InputManager {

	private InputReader inputCreater;

	public InputManager (InputReader inputCreater) {
		this.inputCreater = inputCreater;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList[] process () {

		ArrayList[] empAndCabListArray = new ArrayList[2];

		try {
			ArrayList<Employee> employeeList = inputCreater.getEmployeeList();
			empAndCabListArray[0] = employeeList;

			ArrayList<Cab> cabList = inputCreater.getCabList();
			empAndCabListArray[1] = cabList;

		} finally {
			inputCreater.close();
		}

		return empAndCabListArray;

	}
	/**
	 * This is Junit Test Method.
	 * @return
	 */
	public static boolean processTest() {
		// TODO Auto-generated method stub
		
		try{
			String baseLocationAddress = "Infosys, Sholinganallur, Chennai";
			File employeeDetailsFile = new File("/Users/raghu/HackathonWorkSpace/iCab/src/main/resources/EmployeeDetails.csv");
			File cabDetailsFile = new File("/Users/raghu/HackathonWorkSpace/iCab/src/main/resources/CabDetails.csv");

				
				
				InputReader inputCreater = new CsvReader(employeeDetailsFile, cabDetailsFile);

				Geocoder geocoder = new GoogleMapsGeocoder("AIzaSyBhq_7ACryDVReeXL_Cml58P7GMsCBT4zg");

				GeoLocation baseGeoLocation = geocoder.geoCode("Office", baseLocationAddress);
				BaseLocation baseLocation = new BaseLocation();
				baseLocation.setGeoLocation(baseGeoLocation);

				
				
				InputManager inputManager = new InputManager(inputCreater);
		
				
				inputManager.process();
				return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
				
		return false;
		


	}
}