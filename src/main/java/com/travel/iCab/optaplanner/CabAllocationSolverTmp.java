/**
 * 
 */
package com.travel.iCab.optaplanner;

import java.util.ArrayList;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import com.travel.iCab.distance.impl.GraphHopperDistanceMatrixCreater;
import com.travel.iCab.domain.BaseLocation;
import com.travel.iCab.domain.Cab;
import com.travel.iCab.domain.Employee;
import com.travel.iCab.domain.GeoLocation;
import com.travel.iCab.domain.Location;
import com.travel.iCab.geocode.impl.GoogleMapsGeocoder;


public class CabAllocationSolverTmp {

	public static final String SOLVER_CONFIG
	= "SolverConfig.xml";
	private static GoogleMapsGeocoder googleMapsGeocoder;
	private static GraphHopperDistanceMatrixCreater graphhopperDistanceMatrixCreater;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		googleMapsGeocoder = new GoogleMapsGeocoder("AIzaSyBhq_7ACryDVReeXL_Cml58P7GMsCBT4zg");
		graphhopperDistanceMatrixCreater = new GraphHopperDistanceMatrixCreater();

		SolverFactory solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);

		if (solverFactory != null) {
			System.out.println("Success");
		} else {
			System.out.println("Nope");
		}

		Solver solver = solverFactory.buildSolver();
		CabAllocationSolution unSolvedCabSolution = new CabAllocationSolution();

		//set depot
		BaseLocation baseLocation = new BaseLocation();
		baseLocation.setGeoLocation(googleMapsGeocoder.geoCode("Office", "Infosys, Sholinganallur, Chennai"));

		//set cab list
		ArrayList<Cab> cabList = createCabList(baseLocation);
		//set employee list
		ArrayList<Employee> employeeList = createEmployeeList();

		//Calculate Distance Matrix
		calculateDistanceMatrix(employeeList,baseLocation);

		unSolvedCabSolution.setCabList(cabList);
		unSolvedCabSolution.setEmployeeList(employeeList);
		unSolvedCabSolution.setBaseLocation(baseLocation);

		solver.solve(unSolvedCabSolution);

		CabAllocationSolution solvedCabSolution = (CabAllocationSolution)solver.getBestSolution();

		System.out.println("Score : "+solvedCabSolution.getScore());
		ArrayList<Employee> allocatedEmployeeList = solvedCabSolution.getEmployeeList();
		ArrayList<Cab> allocatedCabList = solvedCabSolution.getCabList();

		for (Employee emp : allocatedEmployeeList) {
			System.out.println("Emp Name : "+emp.getName());
			System.out.println("Cab ID   : "+emp.getCab().getId());
			if (emp.getNextDropEmp() != null)
			System.out.println("Next : "+emp.getNextDropEmp().getName());
			System.out.println();
		}



	}

	/**
	 * @param employeeList
	 * @param baseLocation 
	 */
	private static void calculateDistanceMatrix(ArrayList<Employee> employeeList, BaseLocation baseLocation) {
		ArrayList<GeoLocation> geoLocationList = new ArrayList<GeoLocation>();
		geoLocationList.add(baseLocation.getGeoLocation());

		for (Employee emp: employeeList) {
			geoLocationList.add(emp.getGeoLocation());
		}
		graphhopperDistanceMatrixCreater.createDistanceMatrix(geoLocationList);

	}

	/**
	 * @return
	 */
	private static ArrayList<Employee> createEmployeeList() {
		ArrayList<Employee> employeeList = new ArrayList<Employee>();
		Employee emp1 = new Employee();
		emp1.setEmpID("600484");
		emp1.setName("Siva");
		emp1.setGender("Male");
		Location emp1Loc = new Location();
		emp1Loc.setAddress("90F, NMK Street, Ayanavaram, Chennai, India");
		emp1Loc.setCity("Chennai");
		emp1Loc.setState("TN");
		emp1Loc.setCountry("IN");
		emp1Loc.setPincode("600023");
		emp1.setLocation(emp1Loc);
		GeoLocation emp1GeoLoc = googleMapsGeocoder.geoCode("600484", emp1Loc.getAddress());
		emp1.setGeoLocation(emp1GeoLoc);

		Employee emp2 = new Employee();
		emp2.setEmpID("600483");
		emp2.setName("Prasanna");
		emp2.setGender("Male");
		Location emp2Loc = new Location();
		emp2Loc.setAddress("Ambattur, Chennai, India");
		emp2Loc.setCity("Chennai");
		emp2Loc.setState("TN");
		emp2Loc.setCountry("IN");
		emp2Loc.setPincode("600053");
		emp2.setLocation(emp2Loc);
		GeoLocation emp2GeoLoc = googleMapsGeocoder.geoCode("600483", emp2Loc.getAddress());
		emp2.setGeoLocation(emp2GeoLoc);

		Employee emp3 = new Employee();
		emp3.setEmpID("600482");
		emp3.setName("Ananth");
		emp3.setGender("Male");
		Location emp3Loc = new Location();
		emp3Loc.setAddress("Mogappair");
		emp3Loc.setCity("Chennai");
		emp3Loc.setState("TN");
		emp3Loc.setCountry("IN");
		emp3Loc.setPincode("600037");
		emp3.setLocation(emp3Loc);
		GeoLocation emp3GeoLoc = googleMapsGeocoder.geoCode("600482", emp3Loc.getAddress());
		emp3.setGeoLocation(emp3GeoLoc);

		Employee emp4 = new Employee();
		emp4.setEmpID("600481");
		emp4.setName("Rekha");
		emp4.setGender("Female");
		Location emp4Loc = new Location();
		emp4Loc.setAddress("Vepampattu");
		emp4Loc.setCity("Chennai");
		emp4Loc.setState("TN");
		emp4Loc.setCountry("IN");
		emp4Loc.setPincode("602024");
		emp4.setLocation(emp4Loc);
		GeoLocation emp4GeoLoc = googleMapsGeocoder.geoCode("600481", emp4Loc.getAddress());
		emp4.setGeoLocation(emp4GeoLoc);


		employeeList.add(emp1);
		employeeList.add(emp2);
		employeeList.add(emp3);
		employeeList.add(emp4);

		return employeeList;
	}

	/**
	 * @return
	 */
	private static ArrayList<Cab> createCabList(BaseLocation baseLocation) {
		ArrayList<Cab> cabList = new ArrayList<Cab>();

		Cab cab1 = new Cab();
		cab1.setId("CAB A");
		cab1.setCapacity(4);
		cab1.setRatePerKM(10);
		cab1.setBaseLocation(baseLocation);
		cabList.add(cab1);

		Cab cab2 = new Cab();
		cab2.setId("CAB B");
		cab2.setCapacity(4);
		cab2.setRatePerKM(10);
		cab2.setBaseLocation(baseLocation);
		cabList.add(cab2);

		return cabList;
	}

}
