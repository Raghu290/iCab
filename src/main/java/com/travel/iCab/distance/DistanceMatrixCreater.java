package com.travel.iCab.distance;
import java.util.ArrayList;
import com.travel.iCab.domain.GeoLocation;
/**
 * 
 * @author Raghu
 * normal interface to distance matrix creation
 */
public interface DistanceMatrixCreater {

	ArrayList<GeoLocation> createDistanceMatrix(ArrayList<GeoLocation> dropLocationList);
	
}