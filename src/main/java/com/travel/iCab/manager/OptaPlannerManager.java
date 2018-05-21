/**
 * 
 */
package com.travel.iCab.manager;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

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
import com.travel.iCab.optaplanner.CabAllocationSolution;
import com.travel.iCab.output.impl.CsvWriter;


public class OptaPlannerManager {
	
	private String SOLVER_CONFIG = "SolverConfig.xml";
	private ArrayList<Employee> employeeList;
	private ArrayList<Cab> cabList;
	private BaseLocation baseLocation;
	private SolverFactory solverFactory;
	
	public OptaPlannerManager (ArrayList<Employee> employeeList, ArrayList<Cab> cabList, BaseLocation baseLocation) {
		this.employeeList = employeeList;
		this.cabList = cabList;
		this.baseLocation = baseLocation;
		this.solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);
	}

	public HashMap<Cab,ArrayList<Employee>> process() {
		Solver solver = solverFactory.buildSolver();
		CabAllocationSolution unSolvedCabSolution = new CabAllocationSolution();
		
		unSolvedCabSolution.setEmployeeList(employeeList);
		unSolvedCabSolution.setCabList(cabList);
		unSolvedCabSolution.setBaseLocation(baseLocation);
		
		solver.solve(unSolvedCabSolution);
		
		CabAllocationSolution solvedCabSolution = (CabAllocationSolution)solver.getBestSolution();
		
		System.out.println("Final score");
		System.out.println(solvedCabSolution.getScore().getHardScore());
		System.out.println(solvedCabSolution.getScore().getSoftScore());
		
		HashMap<Cab,ArrayList<Employee>> sortedCabAllocationMap = formatOutput(solvedCabSolution);
		
		return sortedCabAllocationMap;
	}
	
	private HashMap<Cab,ArrayList<Employee>> formatOutput (CabAllocationSolution cabAllocationSolution) {
		ArrayList<Cab> cabList = cabAllocationSolution.getCabList();
		HashMap<Cab,ArrayList<Employee>> cabAllocationMap = new HashMap<Cab, ArrayList<Employee>>(); 

		for (Cab cab: cabList) {
			cabAllocationMap.put(cab, new ArrayList<Employee>());
		}

		ArrayList<Employee> employeeList = cabAllocationSolution.getEmployeeList();
		HashMap<Employee,Employee> previousEmpDropMap = new HashMap<Employee, Employee>();

		for (Employee employee: employeeList) {
			if (employee.getCab() != null) {
				cabAllocationMap.get(employee.getCab()).add(employee);
				if (employee.getNextDropEmp() != null) {
					previousEmpDropMap.put(employee.getNextDropEmp(), employee);
				}
			}
		}

		sort(cabAllocationMap,cabList,previousEmpDropMap);

		return cabAllocationMap;
	}

	/**
	 * @param cabAllocationMap
	 * @param cabList
	 * @param previousEmpDropMap
	 */
	private void sort(HashMap<Cab, ArrayList<Employee>> cabAllocationMap, ArrayList<Cab> cabList,
			HashMap<Employee, Employee> previousEmpDropMap) {

		for (Cab cab: cabList) {
			ArrayList<Employee> unsortedEmployeeList = cabAllocationMap.get(cab);
			Employee lastDropEmployee = null;

			for (Employee employee: unsortedEmployeeList) {
				if (employee.getNextDropEmp() == null) {
					lastDropEmployee = employee;
					break;
				}
			}

			for (int index = unsortedEmployeeList.size()-1; index >= 0; index--) {
				if (unsortedEmployeeList.indexOf(lastDropEmployee) >= 0 
						&&index != unsortedEmployeeList.indexOf(lastDropEmployee)) {
					Collections.swap(unsortedEmployeeList, unsortedEmployeeList.indexOf(lastDropEmployee), index);
				}
				lastDropEmployee = previousEmpDropMap.get(lastDropEmployee);
			}
			cabAllocationMap.put(cab, unsortedEmployeeList);
		}

	}
	/**
	 * This is the JUnit Test method.
	 * @return
	 */
	
	public static boolean processTest() {
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

			
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
				
		return false;
	
	}
}
