package coinpurse;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
/**
 * Test of the Coin class.
 *
 * @author Resident Evil
 */
public class CoinTest  {
	private static final double TOL = 1.0E-6; // tolerance for comparisons
	/**
	 * Setup any test fixtures.
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link coinpurse.Coin#getValue()}.
	 */
	@Test(timeout=1000)
	public void testGetValue() {
		// a different currency
		final String currency = "Nuts";
		final double smallValue = 5.0E-8;
		final double largeValue = 1.1E+8;

		Coin coin = new Coin(2.0, currency);
		Coin small = new Coin(smallValue, currency);		
		Coin big = new Coin(largeValue, currency);
		
		assertEquals( 2.0, coin.getValue(), TOL );
		assertEquals( smallValue, small.getValue(), TOL );
		assertEquals( largeValue, big.getValue(), TOL );
		// do it again to verify that the value didn't change.
		assertEquals( 2.0, coin.getValue(), TOL );
		assertEquals( smallValue, small.getValue(), TOL );
		assertEquals( largeValue, big.getValue(), TOL );
	}

	/**
	 * Test the equals method.
	 */
	@Test(timeout=100)
	public void testEquals() {
		String currency = "Baht";
		Coin coin1 = new Coin(10, currency);
		Coin coin2 = new Coin(10, currency);
		Coin coin3 = new Coin(5, currency);
		
		assertFalse( "Coin can't equal null", coin1.equals(null) );
		// coin.equals(non-coin) should be false
		Double ten = new Double(10.0);
		assertFalse( "Coin does not equal Double", coin1.equals(ten) );
		// another common mistake:
		String tostring = coin1.toString();
		assertFalse( "Coin does not equal String", coin1.equals(tostring));
		// now test with coins
		assertTrue( coin1.equals(coin1) );
		// do it again - test the value didn't change.
		assertTrue( coin2.equals(coin1) );
		// coins with same currency but different value
		assertFalse( "values not equal", coin1.equals(coin3) );
		assertFalse( "values not equal", coin3.equals(coin1) );
		// coins with same value but different currency
		Coin bath = new Coin(10.0, "Bath"); // common misspelling
		assertFalse( "same value but currency differs", coin1.equals(bath) );
	}
	

	/**
	 * Test the equals does not use == for strings.
	 */
	@Test(timeout=100)
	public void testEqualsStringValues() {
		Coin coin1 = new Coin(10.0, "satang");
		Coin coin2 = new Coin(10.0, new String("sat"+"ang"));
		assertEquals("Coin uses == to compare strings", coin1, coin2);
		// toLowerCase also creates a new string
		Coin coin3 = new Coin(10.0, "SATANG".toLowerCase());
		assertEquals("Coin uses == to compare strings", coin1, coin3);
	}
	
	/**
	 * test that compareTo orders coins by increasing value
	 */
	@Test(timeout=100)
	public void testCompareTo() {
		String currency = "USD";
		Coin coin1 = new Coin(1, currency);
		Coin coin2 = new Coin(2, currency);
//		assertTrue( coin1.compareTo(null) < 0 );
		assertEquals( 0, coin1.compareTo(coin1) );
		assertTrue( coin1.compareTo(coin2) < 0 );
		assertTrue( coin2.compareTo(coin1) > 0 );
		Coin clone = new Coin(coin1.getValue(), coin1.getCurrency());
		assertEquals( 0, coin1.compareTo(clone) );
		// an extreme case
		Coin max = new Coin(Integer.MAX_VALUE, currency);
		assertTrue( coin1.compareTo(max) < 0 );
		assertTrue( max.compareTo(coin1) > 0 );
	}

	
	/**
	 * test that compareTo orders coins by currency
	 * ignoring case, then by value.
	 */
	@Test(timeout=100)
	public void testCompareCurrency() {
		Coin coin1 = new Coin(1, "Baht");
		Coin coin2 = new Coin(2, "Dollar");
		assertTrue( "Baht before Dollar", coin1.compareTo(coin2) < 0 );
		assertTrue( "Dollar after Baht", coin2.compareTo(coin1) > 0 );

		Coin clone = new Coin(coin1.getValue(), coin1.getCurrency().toLowerCase());
		assertEquals("shuould ignore case of currency", 0, coin1.compareTo(clone) );

		coin1 = new Coin( 0.5, "zeds");
		coin2 = new Coin( 100.0, "zeds");
		Coin coin3 = new Coin( 0.5, "Astra");
		Coin coin4 = new Coin( 0.0, "Astra");
		Coin coin5 = new Coin( 0.0, "Baht");
		assertTrue( coin1.compareTo(coin2) < 0);  // 0.5 < 100.0 Zeds
		assertTrue( coin1.compareTo(coin3) > 0);  // Astra < Zeds
		assertTrue( coin4.compareTo(coin3) < 0);  // 0.0 < 0.5 Astra
		assertTrue( coin4.compareTo(coin5) < 0);  // 0 Astra < 0 Baht

	}
	
	@Test(timeout=100)
	public void testCompareToUsesDouble() {
		String currency = "Baht";
		Coin coin1 = new Coin(0.1, currency);
		Coin coin2 = new Coin(0.2, currency);
		assertTrue( "0.1 Baht < 0.2 Baht", coin1.compareTo(coin2) < 0);
		assertTrue( "0.2 Baht > 0.1 Baht", coin2.compareTo(coin1) > 0);
		coin1 = new Coin(0.99, currency);
		coin2 = new Coin(1.00, currency);
		assertTrue( "0.99 Baht < 1.00 Baht", coin1.compareTo(coin2) < 0);
		assertTrue( "1.00 Baht > 0.99 Baht", coin2.compareTo(coin1) > 0);
	}
		
	/**
	 * Test that toString() contains the value of the coin
	 */
	@Test(timeout=100)
	public void testToString() {
		Coin coin = new Coin(123, "Rupee");
	    String s = coin.toString();
	    assertTrue(s.startsWith("123"));
	    assertTrue(s.contains("Rupee"));
	    
	    coin = new Coin(0.5, "Dragma");
	    s = coin.toString();
	    assertTrue(s.startsWith("0.5"));
	    assertTrue(s.contains("Dragma"));
	}
}

