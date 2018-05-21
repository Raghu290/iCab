/**
 * 
 */
package com.travel.iCab.optaplanner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import com.travel.iCab.domain.BaseLocation;
import com.travel.iCab.domain.Cab;
import com.travel.iCab.domain.Employee;
/**
 * this class methos is called after every move with solution class for calculating score.
 * return the hard and soft score for planner to pick the best.
 * @author Raghu
 *
 */
public class CabAllocationEScoreCalculator implements EasyScoreCalculator<CabAllocationSolution> {

	/**   Done for hackathon..
	 * This class is needed for calculating score for differernt constraints. We need to implement calculate score method of EasyScoreCalculator interface.
	 * We need to define this class in config xml.
	 * And also score type needs to entitled in config xml.
	 */
	public HardSoftLongScore calculateScore(CabAllocationSolution cabAllocationSolution) {
		long hardScore = 0L;//default score. Ideally it should be 0 for best move.
		long softScore = 0L;//can be below this.
		 BaseLocation baseLocation = cabAllocationSolution.getBaseLocation();
//		boolean checkTolerance = true;
		ArrayList<Cab> cabList = cabAllocationSolution.getCabList();
		HashMap<Cab,ArrayList<Employee>> cabAllocationMap = new HashMap<Cab, ArrayList<Employee>>(); 

		/*putting all cabs into map*/
		for (Cab cab: cabList) {
			cabAllocationMap.put(cab, new ArrayList<Employee>());
		}
		
		ArrayList<Employee> employeeList = cabAllocationSolution.getEmployeeList();
		HashMap<Employee,Employee> previousEmpDropMap = new HashMap<Employee, Employee>();

//		for (Employee employee: employeeList) {
//			if (employee.getCab() == null) {
//				//dont check for tolerance
//				checkTolerance = false;
//			}
//		}
		/*Making the map with nextDropEmployee and presentEmployee.*/
		for (Employee employee: employeeList) {
			if (employee.getCab() != null) {
				if (cabAllocationMap.get(employee.getCab()) != null) {
					cabAllocationMap.get(employee.getCab()).add(employee);
					if (employee.getNextDropEmp() != null) {
						previousEmpDropMap.put(employee.getNextDropEmp(), employee);
					}
				}
			}
		}
		/*end of the loop* */
		/*This check is for is the last drop employee is female.In that case this will leave cab unfull.
		 * At least one place will be left behind as per below logic for security person.*/
		for (Employee employee: employeeList) {
			
			Cab allocatedCab = employee.getCab();
			//System.out.println("Cab "+allocatedCab);
			if (allocatedCab == null) {
				continue;
			}
			
			if (employee.getNextDropEmp() == null) {
				if (cabAllocationMap.get(allocatedCab).size() >= allocatedCab.getCapacity()) {
					if (employee.isFemale()) {
						//High negative hardscore to avoid this solution
						hardScore -= 1000;
//						return HardSoftLongScore.valueOf(hardScore, softScore);
					} 
				}
			}
		}
		/* This check is for checking that the cab is not overloaded.*/
		for (Entry<Cab, ArrayList<Employee>> entry: cabAllocationMap.entrySet()) {
			int capacity = entry.getKey().getCapacity();
			ArrayList<Employee> employeesOfACab = entry.getValue();
			int demand =  employeesOfACab.size();
			
			if (demand > capacity) {
                // Score constraint vehicleCapacity
                hardScore -= (demand - capacity);
//                return HardSoftLongScore.valueOf(hardScore, softScore);
            }
		}
		/*This is sorting logic to put the employee drop point from base location of a cab.This is important logic for checking the tolerance factor*/
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
		/*end of employee sorting logic.*/
//		if (checkTolerance) {
		
		double toleranceFactor = 1.5;
		for (Map.Entry<Cab,ArrayList<Employee>> entry: cabAllocationMap.entrySet()) {
			Cab cab = entry.getKey();
			long distanceFromBase = 0;
			long distanceTraveled = 0;
			//now the employee list is sorted.
			ArrayList<Employee> sortedEmployeeList = cabAllocationMap.get(cab);
			
			for (int index = 0; index < sortedEmployeeList.size(); index++) {
				//calculating distance of each employee from the base.
				distanceFromBase = baseLocation.getGeoLocation().getDistanceTo(sortedEmployeeList.get(index).getGeoLocation());

				
				//index =0 means its first employee
				if (index == 0) {
					distanceTraveled = distanceFromBase;
				} else {// now the distance travelled by employee is total distnce till now + diffrence between the homes of previous employee
					distanceTraveled = distanceTraveled + sortedEmployeeList.get(index-1).getGeoLocation().getDistanceTo(sortedEmployeeList.get(index).getGeoLocation());
					if (distanceTraveled > (distanceFromBase * toleranceFactor)) {
//						System.out.println("Untolerable-----------------000000000000");
						hardScore --;
//						return HardSoftLongScore.valueOf(hardScore, softScore);
					} else {
//						System.out.println("tolerateable 7777777777777777777777");
					}
				}
				
			}
			// this is simple soft score whihch is total distance travelled by cab * rate/km. Minimum score means best cost effective.
			softScore -= cab.getCost(distanceTraveled);
//		}
		}

		return HardSoftLongScore.valueOf(hardScore, softScore);
	}

}
