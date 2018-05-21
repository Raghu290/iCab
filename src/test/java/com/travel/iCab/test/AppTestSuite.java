/**
 * 
 */
package com.travel.iCab.test;

import org.junit.runner.RunWith;

import org.junit.runners.Suite;

import com.travel.iCab.distance.impl.test.GraphHopperDistanceMatrixCreaterTest;
import com.travel.iCab.geocode.impl.test.GoogleMapsGeocoderTest;

import com.travel.iCab.manager.test.DistanceManagerTest;
import com.travel.iCab.manager.test.GeocodeManagerTest;
import com.travel.iCab.manager.test.InputManagerTest;
import com.travel.iCab.manager.test.OptaplannerManagerTest;
import com.travel.iCab.manager.test.OutputManagerTest;
import com.travel.iCab.manager.test.ProcessManagerTest;
import com.travel.iCab.output.impl.test.CsvWriterTest;

/**
 * @author venkatc
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	GraphHopperDistanceMatrixCreaterTest.class,
	GoogleMapsGeocoderTest.class,
	ProcessManagerTest.class,
	DistanceManagerTest.class,
	OptaplannerManagerTest.class,
	OutputManagerTest.class,
	InputManagerTest.class,
	GeocodeManagerTest.class,
	CsvWriterTest.class	
})
public class AppTestSuite {
	
	

}
