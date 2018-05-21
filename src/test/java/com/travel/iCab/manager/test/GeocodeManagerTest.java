/**
 * 
 */
package com.travel.iCab.manager.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.travel.iCab.manager.GeocodeManager;

/**
 * @author venkatc
 *
 */
public class GeocodeManagerTest {
	@Test
	public void processTest(){
		assertTrue(GeocodeManager.processTest());
	}
}
