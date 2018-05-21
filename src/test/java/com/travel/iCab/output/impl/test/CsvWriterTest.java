/**
 * 
 */
package com.travel.iCab.output.impl.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


import com.travel.iCab.output.impl.CsvWriter;

/**
 * @author venkatc
 *
 */
public class CsvWriterTest {
	@Test
	public void saveRoutePlanTest(){
		
		assertTrue(CsvWriter.saveRoutePlanTest());
		
	}

}
