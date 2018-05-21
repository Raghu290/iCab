/**
 * 
 */
package com.travel.iCab.input.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.travel.iCab.domain.Cab;
import com.travel.iCab.domain.Employee;
import com.travel.iCab.domain.GeoLocation;
import com.travel.iCab.domain.Location;
import com.travel.iCab.input.InputReader;


public class CsvReader implements InputReader {

	private CSVParser employeeFileParser;
	private CSVParser cabFileParser;

	public CsvReader(File employeeDetailsFile, File cabDetailsFile) throws IOException {

		employeeFileParser = CSVParser.parse(employeeDetailsFile, Charset.defaultCharset(), CSVFormat.DEFAULT.withHeader());
		cabFileParser = CSVParser.parse(cabDetailsFile, Charset.defaultCharset(), CSVFormat.DEFAULT.withHeader());
	}

	
	public ArrayList<Employee> getEmployeeList() {

		ArrayList<Employee> employeeList = new ArrayList<Employee>();

		for (CSVRecord csvRecord : employeeFileParser) {

			Employee employee = new Employee();
			employee.setEmpID(csvRecord.get("EmpID"));
			employee.setName(csvRecord.get("Name"));
			employee.setGender(csvRecord.get("Gender"));

			Location location = new Location();
			location.setAddress(csvRecord.get("Address1") + csvRecord.get("Address2"));
			location.setCity(csvRecord.get("City"));
			location.setState(csvRecord.get("State"));
			location.setCountry(csvRecord.get("Country"));
			location.setPincode(csvRecord.get("Pincode"));

			employee.setLocation(location);
			
			if (csvRecord.get("Latitude") != null && csvRecord.get("Longitude") != null
					&& !"".equals(csvRecord.get("Latitude").trim()) && !"".equals(csvRecord.get("Longitude").trim())) {
				GeoLocation geoLocation = new GeoLocation(employee.getEmpID(), Double.parseDouble(csvRecord.get("Latitude")),
						Double.parseDouble(csvRecord.get("Longitude")), csvRecord.get("Pincode"));
				employee.setGeoLocation(geoLocation);
			}

			employeeList.add(employee);
		}

		return employeeList;
	}

	
	public ArrayList<Cab> getCabList() {

		ArrayList<Cab> cabList = new ArrayList<Cab>();

		for (CSVRecord csvRecord : cabFileParser) {
			Cab cab = new Cab();
			cab.setId(csvRecord.get("CabID"));
			try {
				cab.setCapacity(Integer.parseInt(csvRecord.get("Capacity")));
				cab.setRatePerKM(Integer.parseInt(csvRecord.get("Rate")));
			}
			catch (NumberFormatException numberFormatException) {
				//log the exception here
				continue;
			}
			cabList.add(cab);
		}

		return cabList;
	}

	
	public void close() {
		try {
		if (employeeFileParser != null && !employeeFileParser.isClosed()) {
			employeeFileParser.close();
		}
		if (cabFileParser != null && !cabFileParser.isClosed()) {
			cabFileParser.close();
		}
		} catch (Exception exception) {
			//log the exception
			//no need to do anything
		}

	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			File employeeDetailsFile = new File("/Users/raghu/HackathonWorkSpace/iCab/src/main/resources/EmployeeDetails.csv");
			File cabDetailsFile = new File("/Users/raghu/HackathonWorkSpace/iCab/src/main/resources/CabDetails.csv");

			CsvReader fileReader = new CsvReader(employeeDetailsFile, cabDetailsFile);
			ArrayList<Employee> employeeList = fileReader.getEmployeeList();
			ArrayList<Cab> cabList = fileReader.getCabList();

			System.out.println(employeeList.size());
			System.out.println(cabList.size());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
