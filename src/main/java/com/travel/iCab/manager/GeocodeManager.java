/**
 * 
 */
package com.travel.iCab.manager;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import com.travel.iCab.distance.DistanceMatrixCreater;
import com.travel.iCab.distance.impl.GraphHopperDistanceMatrixCreater;
import com.travel.iCab.domain.BaseLocation;
import com.travel.iCab.domain.Cab;
import com.travel.iCab.domain.Employee;
import com.travel.iCab.domain.GeoLocation;
import com.travel.iCab.geocode.Geocoder;
import com.travel.iCab.geocode.impl.GoogleMapsGeocoder;
import com.travel.iCab.input.InputReader;
import com.travel.iCab.input.impl.CsvReader;
import com.travel.iCab.output.impl.CsvWriter;

public class GeocodeManager {

	private Geocoder geocoder;

	public GeocodeManager (Geocoder geocoder) {
		this.geocoder = geocoder;
	}

	public void process (ArrayList<Employee> employeeList, ArrayList<Cab> cabList, BaseLocation baseLocation) {

		for (Employee employee: employeeList) {

			//If already geocoded via input file, then skip
			if (employee.getGeoLocation() != null) {
				System.out.println("EmpID "+employee.getEmpID()+" is already geocoded");
				continue;
			}
			
			GeoLocation geoLocation = null;
			String fullAddress = employee.getLocation().getAddress()+","
					+ employee.getLocation().getCity()+","
					+ employee.getLocation().getState()+","
					+ employee.getLocation().getCountry();
			System.out.println("EmpID "+employee.getEmpID()+" Address : "+fullAddress);
			
			geoLocation = geocoder.geoCode(employee.getEmpID(), fullAddress);
			
			if (geoLocation != null && !employee.getLocation().getPincode().equals(geoLocation.getPostalCode())) {
				System.out.println("Postal code mistmatch.....................Org "+employee.getLocation().getPincode()+" --- Calculated one "+geoLocation.getPostalCode());
				geoLocation = null;
			}
			
			if (geoLocation == null) {
				System.out.println("Unable to find/mismatch using address, trying with pincode "+employee.getLocation().getPincode()+", "+employee.getLocation().getCity());
				geoLocation = geocoder.geoCode(employee.getEmpID(), employee.getLocation().getPincode()+", "+employee.getLocation().getCity());
			}
			
			if (geoLocation != null) {
				System.out.println(employee.getEmpID()+" "+geoLocation.getLongitude()+"  "+geoLocation.getLatitude());
				employee.setGeoLocation(geoLocation);
			} else {
				System.out.println("Unable to find.");
			}
			
		}
		
		for (Cab cab: cabList) {
			cab.setBaseLocation(baseLocation);
		}
	}
/*
 * This is JUnit Test method.
 */
	public static boolean processTest() {
	
		
		// TODO Auto-generated method stub
		try{
		String baseLocationAddress = "Infosys, Sholinganallur, Chennai";
		File employeeDetailsFile = new File("/Users/raghu/HackathonWorkSpace/iCab/src/main/resources/EmployeeDetails.csv");
		File cabDetailsFile = new File("/Users/raghu/HackathonWorkSpace/iCab/src/main/resources/CabDetails.csv");

			
			
			InputReader inputCreater = new CsvReader(employeeDetailsFile, cabDetailsFile);

			Geocoder geocoder = new GoogleMapsGeocoder("AIzaSyBhq_7ACryDVReeXL_Cml58P7GMsCBT4zg");

			DistanceMatrixCreater distanceMatrixCreater = new GraphHopperDistanceMatrixCreater();

			GeoLocation baseGeoLocation = geocoder.geoCode("Office", baseLocationAddress);
			BaseLocation baseLocation = new BaseLocation();
			baseLocation.setGeoLocation(baseGeoLocation);

			FileWriter fileWriter = new FileWriter("/Users/raghu/HackathonWorkSpace/iCab/src/main/resources/RoutePlan.csv");
			
			CsvWriter csvWriter = new CsvWriter(fileWriter);
			
			DistanceManager distanceManager = new DistanceManager(distanceMatrixCreater);
			InputManager inputManager = new InputManager(inputCreater);
			GeocodeManager geocodeManager = new GeocodeManager(geocoder);
			
			ArrayList[] empAndCabList = inputManager.process();

			ArrayList<Employee> employeeList = empAndCabList[0];
			ArrayList<Cab> cabList = empAndCabList[1];

			geocodeManager.process(employeeList, cabList, baseLocation);
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;

	}

}
