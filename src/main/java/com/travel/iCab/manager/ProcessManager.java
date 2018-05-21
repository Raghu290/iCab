/**
 * 
 */
package com.travel.iCab.manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

/**
 * @author Sivakumar Manoharan
 *
 */
public class ProcessManager {

	private InputManager inputManager;
	private GeocodeManager geocodeManager;
	private DistanceManager distanceManager;
	private OutputManager outputManager;
	
	private BaseLocation baseLocation;

	public ProcessManager (BaseLocation baseLocation, InputReader inputCreater, Geocoder geocoder, DistanceMatrixCreater distanceMatrixCreater, OutputWriter outputWriter) {
		this.baseLocation = baseLocation;
		this.inputManager = new InputManager(inputCreater);
		this.geocodeManager = new GeocodeManager(geocoder);
		this.distanceManager = new DistanceManager(distanceMatrixCreater);
		this.outputManager = new OutputManager(outputWriter);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap<Cab,ArrayList<Employee>> process() throws IOException {

		ArrayList[] empAndCabList = inputManager.process();

		ArrayList<Employee> employeeList = empAndCabList[0];
		ArrayList<Cab> cabList = empAndCabList[1];

		geocodeManager.process(employeeList, cabList, baseLocation);

		distanceManager.process(employeeList, baseLocation);

		OptaPlannerManager optaPlannerManager = new OptaPlannerManager(employeeList, cabList, baseLocation);
		HashMap<Cab,ArrayList<Employee>> sortedCabAllocationMap =  optaPlannerManager.process();;

		
		
		outputManager.process(sortedCabAllocationMap);
	
	
	
		
		return sortedCabAllocationMap;
//		return null;

	}

	

	/**This method is JUnit Test method.
	 * @param args
	 * @throws IOException 
	 */
	public static boolean processTest() {
		try{
			String baseLocationAddress = "Infosy, Sholinganallur, Chennai";
			File employeeDetailsFile = new File("/Users/raghu/HackathonWorkSpace/iCab/src/main/resources/EmployeeDetails.csv");
			File cabDetailsFile = new File("/Users/raghu/HackathonWorkSpace/iCab/src/main/resources/CabDetails.csv");

			InputReader inputCreater = new CsvReader(employeeDetailsFile, cabDetailsFile);

			Geocoder geocoder = new GoogleMapsGeocoder("AIzaSyBhq_7ACryDVReeXL_Cml58P7GMsCBT4zg");

			DistanceMatrixCreater distanceMatrixCreater = new GraphHopperDistanceMatrixCreater();

			GeoLocation baseGeoLocation = geocoder.geoCode("Office", baseLocationAddress);
			BaseLocation baseLocation = new BaseLocation();
			baseLocation.setGeoLocation(baseGeoLocation);

			FileWriter fileWriter = new FileWriter("E:/HackathonWorkSpace/iCab/src/main/resources/RoutePlan.csv");
			CsvWriter csvWriter = new CsvWriter(fileWriter);

			ProcessManager processManager = new ProcessManager(baseLocation, inputCreater, geocoder, distanceMatrixCreater,csvWriter);
			HashMap<Cab,ArrayList<Employee>> sortedCabAllocationMap = processManager.process();

			for (Map.Entry<Cab,ArrayList<Employee>> entry: sortedCabAllocationMap.entrySet()) {
				Cab cab = entry.getKey();
				System.out.println(cab);
				ArrayList<Employee> sortedEmployeeList = sortedCabAllocationMap.get(cab);
				for (Employee employee: sortedEmployeeList) {
					System.out.println(employee);
				}
				System.out.println();
				System.out.println("----------------------------------------------");
			}
			processManager.calculateTolerance(sortedCabAllocationMap);
			return true;

		}catch(Exception e){
			e.printStackTrace();
		}
		return false;

	}

	public long calculateTolerance(HashMap<Cab, ArrayList<Employee>> sortedCabAllocationMap) {
		double toleranceFactor = 1.5;
		for (Map.Entry<Cab,ArrayList<Employee>> entry: sortedCabAllocationMap.entrySet()) {
			Cab cab = entry.getKey();
			long distanceFromBase = 0;
			long distanceTraveled = 0;
			ArrayList<Employee> sortedEmployeeList = sortedCabAllocationMap.get(cab);
			
			for (int index = 0; index < sortedEmployeeList.size(); index++) {
				distanceFromBase = baseLocation.getGeoLocation().getDistanceTo(sortedEmployeeList.get(index).getGeoLocation());
				if (index == 0) {
					distanceTraveled = distanceFromBase;
					System.out.println(sortedEmployeeList.get(index).getEmpID()+"  tolerateable 7777777777777777777777");
				} else {
					distanceTraveled = distanceTraveled + sortedEmployeeList.get(index-1).getGeoLocation().getDistanceTo(sortedEmployeeList.get(index).getGeoLocation());
					if (distanceTraveled > (distanceFromBase * toleranceFactor)) {
						System.out.println(sortedEmployeeList.get(index).getEmpID()+"  Untolerable-----------------000000000000");
						
					} else {
						System.out.println(sortedEmployeeList.get(index).getEmpID()+"  tolerateable 7777777777777777777777");
					}
				}
				
			}
		}
		return 0;
	}
	
}
