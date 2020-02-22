package coinpurse;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test of the BankNote class.
 *
 * @author Resident Evil
 */
public class BankNoteTest  {
	private static final double TOL = 1.0E-6; // tolerance for comparisons
	private String currency = "Baht";
	/**
	 * Setup any test fixtures.
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link coinpurse.BankNote#getValue()}.
	 */
	@Test(timeout=1000)
	public void testGetValue() {
		
		final double smallValue = 5.0E-8;
		final double largeValue = 1.1E+8;
		BankNote small = new BankNote(smallValue, currency);
		BankNote bank = new BankNote(2.0, currency);
		
		BankNote big = new BankNote(largeValue, currency);
		
		assertEquals( 2.0, bank.getValue(), TOL );
		assertEquals( smallValue, small.getValue(), TOL );
		assertEquals( largeValue, big.getValue(), TOL );
		// do it again to test the value didn't change.
		assertEquals( 2.0, bank.getValue(), TOL );
		assertEquals( smallValue, small.getValue(), TOL );
		assertEquals( largeValue, big.getValue(), TOL );
	}

	/**
	 * Test the equals method.
	 */
	@Test(timeout=100)
	public void testEquals() {
		BankNote bank1 = new BankNote(10, currency);
		BankNote bank2 = new BankNote(10, currency);
		BankNote bank3 = new BankNote(5, currency);
		
		assertFalse( "BankNote does not equal null", bank1.equals(null) );
		// bank.equals(non-bank) should be false
		Double ten = new Double(10);
		assertFalse( "BankNote does not equal Double", bank1.equals(ten) );
		// another common mistake:
		String tostring = bank1.toString();
		assertFalse( "BankNote does not equal String", bank1.equals(tostring));
		// now test with banks
		assertTrue( "banknote should equal itself", bank1.equals(bank1) );
		// do it again - test the value didn't change.
		assertEquals( bank1, bank2 );
		assertEquals( bank2, bank1 );
		// banks with same currency but different value
		assertFalse( "values not equal", bank1.equals(bank3) );
		assertFalse( "values not equal", bank3.equals(bank1) );
		// banks with same value but different currency
		BankNote bath = new BankNote(bank1.getValue(), "Bath"); // common misspelling
		assertFalse( "same value but currency differs", bank1.equals(bath) );
	}
	

	/**
	 * Test the equals does not use == for strings.
	 */
	@Test(timeout=100)
	public void testEqualsStringValues() {
		BankNote bank1 = new BankNote(10.0, "satang");
		BankNote bank2 = new BankNote(10.0, new String("sat"+"ang"));
		assertEquals("BankNote uses == to compare strings", bank1, bank2);
		// toLowerCase also creates a new string
		BankNote bank3 = new BankNote(10.0, "SATANG".toLowerCase());
		assertEquals("BankNote uses == to compare strings", bank1, bank3);
	}
	/**
	 * Test that toString() contains the value of the banknote
	 */
	@Test(timeout=100)
	public void testToString() {
		BankNote bank = new BankNote(500, "Rupee");
	    String s = bank.toString();
	    assertTrue(s.startsWith("500"));
	    assertTrue(s.contains("Rupee"));
	    
	    bank = new BankNote(1, "Dragma");
	    s = bank.toString();
	    assertTrue(s.startsWith("1"));
	    assertTrue(s.contains("Dragma"));
	}
	
	@Test(timeout=100)
	public void testBanknoteSerialNumber() {
		BankNote bank1 = new BankNote(20,"USD");
		long serial1 = bank1.getSerial();
		BankNote bank2 = new BankNote(20,"USD");
		long serial2 = bank2.getSerial();
		assertTrue("serial should be at least 1,000,000", serial1 >= 1000000);
		assertTrue("serial should be at least 1,000,000", serial2 >= 1000000);
		assertEquals("Serial should not change", serial1, bank1.getSerial());
		assertEquals("Serial should not change", serial2, bank2.getSerial());
		assertEquals("Serial should not change", serial1, bank1.getSerial()); // make sure getSerial doesn't change it
		assertNotSame("Serial should be unique", serial1, serial2);
		assertTrue("Serial number should increase", serial2 > serial1);
		BankNote bank3 = new BankNote(100,"USD");
		long serial3 = bank3.getSerial();
		assertNotSame("Serial should be unique", serial1, serial3);
		assertNotSame("Serial should be unique", serial2, serial3);
	}

}

