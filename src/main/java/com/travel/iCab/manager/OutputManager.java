/**
 * 
 */
package com.travel.iCab.manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

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
import com.travel.iCab.output.OutputWriter;
import com.travel.iCab.output.impl.CsvWriter;

public class OutputManager {

	private OutputWriter outputWriter;

	public OutputManager(OutputWriter outputWriter) {
		this.outputWriter = outputWriter;
	}

	public void process(HashMap<Cab, ArrayList<Employee>> sortedCabAllocationMap) throws IOException {

		try {
			outputWriter.saveRoutePlan(sortedCabAllocationMap);
		} finally {
			outputWriter.close();
		}

	}

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
public static boolean processTest()  {
		

		// TODO Auto-generated method stub

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

			distanceManager.process(employeeList, baseLocation);
			
			OptaPlannerManager optaPlannerManager = new OptaPlannerManager(employeeList, cabList, baseLocation);
			HashMap<Cab,ArrayList<Employee>> sortedCabAllocationMap = optaPlannerManager.process();
			OutputManager outputManager = new OutputManager(csvWriter);

			outputManager.process(sortedCabAllocationMap);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
				
		return false;
		

	
		

	

		
		
		

	}

}
