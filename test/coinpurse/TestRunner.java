package coinpurse;

import java.util.List;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
/**
 * Run JUnit test suites and print results on console.
 * You can run JUnit tests in an IDE without using this class.
 * For running on console, this TestRunner prints messages
 * about failed tests that are easier to read than JUnit output.
 * 
 * Run this class as an ordinary Java application (using main).
 * 
 * @author James Brucker
 *
 */
public class TestRunner {

	/**
	 * Run the JUnit tests and display results.
	 * @param args
	 */
	public static void main(String[] args) {
		int failures = runTestSuite( ValuableTest.class );
		failures += runTestSuite( CoinTest.class );
		failures += runTestSuite( BankNoteTest.class );
		failures += runTestSuite( PurseTest.class );
		failures += runTestSuite( ValueComparatorTest.class );
		
		// return code indicates success or failure
		System.exit(failures);
	}
	
	/**
	 * Run a JUnit test suite and print summary of results.
	 * @param clazz class object for the JUnit test suite.
	 * @return number of failed tests
	 */
	public static int runTestSuite( Class<?> clazz ) {
		// Print class name first in case it crashes
		String name = clazz.getSimpleName();
		System.out.println("Running "+name);
		Result result = org.junit.runner.JUnitCore.runClasses( clazz );
		int count = result.getRunCount();
		int failed = result.getFailureCount();
		int success = count - failed;
		System.out.printf("%-12.12s   Success: %d  Failures: %d\n",
				clazz.getSimpleName(), success, failed);
		
		// this sometimes doesn't seem to return all the failures
		List<Failure> failures = result.getFailures();
		for(Failure f: failures) {
			Description d = f.getDescription();			
			System.out.println( f.getTestHeader() +": "+ f.getMessage() );
			System.out.println( d.getDisplayName() );
		}
		// return the number of failures
		return failed;
	}
}
