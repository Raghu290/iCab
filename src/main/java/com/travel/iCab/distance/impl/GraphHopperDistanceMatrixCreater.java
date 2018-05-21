package com.travel.iCab.distance.impl;
import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.Map;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.util.CmdArgs;
import com.graphhopper.util.shapes.GHPoint;
import com.travel.iCab.distance.DistanceMatrixCreater;
import com.travel.iCab.domain.GeoLocation;
/*
 * this class is used to create distance matrix and set to same geoLocation object
 */
public class GraphHopperDistanceMatrixCreater implements DistanceMatrixCreater{
	
	private GraphHopper graphHopper ;
	public GraphHopperDistanceMatrixCreater(){
		/*Giving graphHooper configurations. and importOrLoad will load all the map data of city from properties file location.*/
		String[] args = new String[1];
        args[0] = "config=src/main/resources/graphhopper-config.properties";
		graphHopper = new GraphHopper().init(CmdArgs.read(args)).setEnableInstructions(false).
        importOrLoad();
		
	}
	/*Calculating distance from one GeoLocation to others. And set it to the same object which in turn used from Employee Planning entity.*/
	public ArrayList<GeoLocation> createDistanceMatrix(ArrayList<GeoLocation> dropLocationList) {

		int dropLocationListSize = dropLocationList.size();

		for (int fromIndex = 0; fromIndex < dropLocationListSize; fromIndex++) {

			Map<GeoLocation, Double> travelDistanceMap = new LinkedHashMap<GeoLocation, Double>(dropLocationListSize);

			for (int toIndex = 0; toIndex < dropLocationListSize; toIndex++) {
				/*here distance is 0. */
				if (fromIndex == toIndex)
					continue;
				double distance = getDistance(dropLocationList.get(fromIndex), dropLocationList.get(toIndex));
				System.out.println("From : "+dropLocationList.get(fromIndex).getId()
						+", To : "+dropLocationList.get(toIndex).getId()
						+" , Distance : "+distance);
				travelDistanceMap.put(dropLocationList.get(toIndex), distance);
			}
                    //setting to GeoLocation object the Map
			dropLocationList.get(fromIndex).setTravelDistanceMap(travelDistanceMap);
			System.out.println(dropLocationList.get(fromIndex).getId()+"    "+travelDistanceMap);
		}
		return dropLocationList;
	}
	
	/*
	 * Actual implementation of GraphHooper
	 */
	
	private double getDistance(GeoLocation origin, GeoLocation destination) {
		
		GHPoint startPoint 	= new GHPoint(origin.getLatitude(), origin.getLongitude());
		GHPoint endPoint 	= new GHPoint(destination.getLatitude(), destination.getLongitude());
		GHRequest ghRequest = new GHRequest(startPoint, endPoint);
		ghRequest.setVehicle("car");
		ghRequest.setWeighting("fastest");
		
		GHResponse ghResponse = graphHopper.route(ghRequest);
		
		if (ghResponse.hasErrors()) {
			// need to throw error from here, needs to be handled
			System.out.println(ghResponse.getErrors());
			return 0;
		}
		
		return ghResponse.getDistance();
	}
	
	public  boolean createDistanceMatrix() {
		
		GeoLocation geoLocation01 =  new GeoLocation("DC", 12.89033315,80.2279785383965,"0000");
		GeoLocation geoLocation02 = new GeoLocation("Emp_01", 13.097408,80.2291942,"000");
		GeoLocation geoLocation03 = new GeoLocation("Emp_02", 13.0595365,80.24247919999999,"000");
		
		ArrayList<GeoLocation> dropLocationList = new ArrayList<GeoLocation>();
		dropLocationList.add(geoLocation01);
		dropLocationList.add(geoLocation02);
		dropLocationList.add(geoLocation03);
		
		GraphHopperDistanceMatrixCreater graphHopperDistanceMatrixCreater = new GraphHopperDistanceMatrixCreater();
		ArrayList<GeoLocation> dropLocationListResult = graphHopperDistanceMatrixCreater.createDistanceMatrix(dropLocationList);
		for(int i=0;i<dropLocationList.size();i++){
			GeoLocation g =(GeoLocation)dropLocationList.get(i);
			g.getTravelDistanceMap().size();
		}

		if(dropLocationList.size()==dropLocationListResult.size()){
			return true;
		}else{
			return false;
		}
	}
	
	

}
