/**
 * 
 */
package com.travel.iCab.manager.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.travel.iCab.manager.ProcessManager;

/**
 * @author venkatc
 *
 */
public class ProcessManagerTest {
	
	
	@Test
	public void processManagerTest(){
		
		assertTrue(ProcessManager.processTest());
		
	}

}
