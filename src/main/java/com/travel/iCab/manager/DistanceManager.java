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
public class DistanceManager {

private DistanceMatrixCreater distanceMatrixCreater;
	
	public DistanceManager (DistanceMatrixCreater distanceMatrixCreater) {
		this.distanceMatrixCreater = distanceMatrixCreater;
	}
	
	public void process (ArrayList<Employee> employeeList, BaseLocation baseLocation) {
		
		ArrayList<GeoLocation> geoLocationList = new ArrayList<GeoLocation>();
		geoLocationList.add(baseLocation.getGeoLocation());
		
		for (Employee employee: employeeList) {
			geoLocationList.add(employee.getGeoLocation());
		}
		
		distanceMatrixCreater.createDistanceMatrix(geoLocationList);
	}
/**
 * This method is for JUnit Testing.
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
			
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
				
		return false;
		

	}
}
