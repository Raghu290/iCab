package com.travel.iCab;

import java.io.File;
import java.io.FileWriter;
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
import com.travel.iCab.manager.ProcessManager;
import com.travel.iCab.output.impl.CsvWriter;


public class App 
{
	public static void main( String[] args )
	{
		try {
			
			
			String baseLocationAddress = "Infosys, Sholinganallur, Chennai";
			//File employeeDetailsFile = new File("E:/HackathonWorkSpace/iCab/src/main/resources/EmployeeDetails.csv");
			//File cabDetailsFile = new File("E:/HackathonWorkSpace/iCab/src/main/resources/CabDetails.csv");
			File employeeDetailsFile = new File("/Users/raghu/HackathonWorkSpace/iCab/src/main/resources/EmployeeDetails.csv");
			File cabDetailsFile = new File("/Users/raghu/HackathonWorkSpace/iCab/src/main/resources/CabDetails.csv");

		//	String baseLocationAddress = args[0];
			//File employeeDetailsFile = new File(args[1]);
			//File cabDetailsFile = new File(args[2]);
			
			InputReader inputCreater = new CsvReader(employeeDetailsFile, cabDetailsFile);

			Geocoder geocoder = new GoogleMapsGeocoder("AIzaSyBhq_7ACryDVReeXL_Cml58P7GMsCBT4zg");
	//		Geocoder geocoder = new GoogleMapsGeocoder(args[3]);

			DistanceMatrixCreater distanceMatrixCreater = new GraphHopperDistanceMatrixCreater();

			GeoLocation baseGeoLocation = geocoder.geoCode("Office", baseLocationAddress);
			BaseLocation baseLocation = new BaseLocation();
			baseLocation.setGeoLocation(baseGeoLocation);
			
			//FileWriter fileWriter = new FileWriter("/Users/raghu/HackathonWorkSpace/iCab/src/main/resources/RoutePlan.csv");
			FileWriter fileWriter = new FileWriter("/Users/raghu/HackathonWorkSpace/iCab/src/main/resources/RoutePlan.csv");
//			FileWriter fileWriter = new FileWriter(args[4]);
			
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
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
