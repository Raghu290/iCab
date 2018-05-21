/**
 * 
 */
package com.travel.iCab.output.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.travel.iCab.domain.Cab;
import com.travel.iCab.domain.Employee;
import com.travel.iCab.output.OutputWriter;

public class CsvWriter implements OutputWriter {

	private CSVPrinter csvPrinter;
	private String[] header = {"Cab", "EmpID", "Pincode", "EmpName", "Gender", "Address", "Latitude", "Longitude"};

	public CsvWriter(FileWriter fileWriter) throws IOException {
		csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader(header).withRecordSeparator('\n'));
	}

	/* (non-Javadoc)
	 * @see com.travel.iCab.output.OutputCreater#saveRoutePlan(java.util.HashMap)
	 */
	public void saveRoutePlan(HashMap<Cab, ArrayList<Employee>> sortedCabAllocationMap) throws IOException {
		for (Map.Entry<Cab,ArrayList<Employee>> entry: sortedCabAllocationMap.entrySet()) {

			Cab cab = entry.getKey();

			for (Employee employee: entry.getValue()) {
				ArrayList<String> newLine = new ArrayList<String>();
				newLine.add(cab.getId());
				newLine.add(employee.getEmpID());
				newLine.add(employee.getLocation().getPincode());
				newLine.add(employee.getName());
				newLine.add(employee.getGender());
				newLine.add(employee.getLocation().getAddress());
				newLine.add(String.valueOf(employee.getGeoLocation().getLatitude()));
				newLine.add(String.valueOf(employee.getGeoLocation().getLongitude()));
				csvPrinter.printRecord(newLine);
			}
		}

	}

	/* (non-Javadoc)
	 * @see com.travel.iCab.output.OutputCreater#close()
	 */
	public void close() {

		try {
			csvPrinter.flush();
			csvPrinter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**This is a JUnit Test method.
	 * @param args
	 */
	public static boolean saveRoutePlanTest() {

		File file = new File("/Users/raghu/HackathonWorkSpace/iCab/src/main/resources/RoutePlan.csv");

		CsvWriter csvWriter = null;
		try {
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(file);
			csvWriter = new CsvWriter(fileWriter);
			csvWriter.saveRoutePlan(null);
			csvWriter.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
return false;
	}
}
