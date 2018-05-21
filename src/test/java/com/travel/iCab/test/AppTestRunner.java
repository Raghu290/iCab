/**
 * 
 */
package com.travel.iCab.test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;



/**
 * @author venkatc
 *
 */
public class AppTestRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Result result = JUnitCore.runClasses(AppTestSuite.class);
		
		for(Failure failure :result.getFailures()){
			System.out.println(failure.toString());
			
		}
		System.out.println("Testing completed  :"+result.wasSuccessful());
	}

}
