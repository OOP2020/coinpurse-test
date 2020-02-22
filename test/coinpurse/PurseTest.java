package coinpurse;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Basic tests for Purse.
 *
 * @author  Resident Evil (good testers are evil)
 */
public class PurseTest {
	// default currency
	private String currency;
    // tolerance for floating point comparisons
	private static final double TOL = 1.0E-8;
	/**
     * Set up a test fixture.
     * Called before every test case method.
     */
	@Before
    public  void setUp() {
        currency = "Clams";
    }

    /**
     * Test constructor and capacity.
     */
	@Test
    public void testPurse() {
        Purse purse1 = new Purse(1);
        assertEquals(1, purse1.getCapacity());
        Purse purse2 = new Purse(1000);
        assertEquals(1000,purse2.getCapacity());
        // make sure capacity isn't static
        assertEquals(1, purse1.getCapacity());
        // new purse is empty
        assertEquals( 0, purse1.count() );
        assertEquals( 0, purse2.count() );
    }
    
	@Test
    public void testInsert() {
        Purse purse = new Purse(1);
        // insert
        assertTrue( purse.insert(new Coin(1, currency)) );
        assertFalse( purse.insert(new Coin(1, currency)) ); // already full
        int capac = 5;
        Purse purse5 = new Purse(capac);
        for(int k=1; k<=capac; k++) {
            Coin coin = new Coin(10*k, currency);
            assertTrue( purse5.insert( coin ) );
        }
        // should be full now
        assertFalse( purse5.insert( new Coin(1, currency) ) );
    }
    
	@Test
    public void testCannotInsertZeroValue() {
        // can't insert zero value coin
        Purse purse = new Purse(2);
        Coin zero = new Coin(0, currency);
        assertFalse( purse.insert(zero ) );
    }
    
    /**
	 * Hard to test insert() without using getBalance,
	 * so I call this a test of both
	 */
	@Test(timeout=1000)
	public void testInsertAndBalance() {
		int capacity = 10;
		Purse purse = new Purse(capacity);
		Purse emptypurse = new Purse(capacity);
		int balance = 0;
		for(int n=1; n<=capacity; n++) {
			assertTrue( purse.insert(new Coin(n, currency)) );
			balance += n;
			assertEquals( n, purse.count() );
			assertEquals( balance, purse.getBalance(currency), TOL );
			assertEquals( 0, emptypurse.getBalance(currency), TOL );
		}
	}
	
	@Test(timeout=1000)
	public void testIsFull() {
	    // test purse with capacity 1, 10, and 100.
		for(int capacity=1; capacity<=100; capacity = 10*capacity) {
    		Purse purse = new Purse(capacity);
    		for(int k=1; k<=capacity; k++) {
    			assertFalse( purse.isFull() );  // new purse is not full yet
    			assertTrue(String.format("insert coin %d into purse with cap %d",k,capacity), 
    					   purse.insert(new Coin(k, currency))
    					   );
    		}
    		// now it should be full
    		assertTrue( purse.isFull() );
		}
	}
	
	/**
	 * Test simple withdraw of 1 coin
	 */
	@Test(timeout=1000)
	public void testSimpleWithdraw() {
		Purse purse = new Purse(3);
		assertEquals("Initial balance is 0", 0, purse.getBalance(currency), TOL);
		int [] amounts = {1, 5, 10};
		for(int amount: amounts) {
		    // insert 1 coin
		    Coin coin = new Coin(amount, currency);
			assertTrue( purse.insert(coin) );
			// should be able to withdraw it!
			Object[] wd = purse.withdraw(amount, currency);
			assertNotNull( wd );
			assertEquals( 1, wd.length );
			assertSame( coin, wd[0] );
			// purse should be empty
			assertEquals( 0, purse.getBalance(currency), TOL );
			assertEquals( 0, purse.count() );
		}
		
		// test some withdraws that should fail
		purse = new Purse(1);
		purse.insert( new Coin(5, currency) );
		assertEmptyOrNull( "Purse has 5, withdraw 6 should fail", purse.withdraw(6, currency) );
		assertEmptyOrNull( "Purse has 5-unit coin, withdraw 4-unit should fail", purse.withdraw(4, currency) );
		purse = new Purse(1);
		purse.insert( new Coin(1, currency) );
		assertEmptyOrNull( "Purse contains 1 baht, but tried to withdraw 2 baht",purse.withdraw(2, currency) );
	}
	

	
//	@Test
//	public void testWithdrawNegativeAmount( ) {
//		Purse purse = new Purse(5);
//		purse.insert(new Coin(5,currency));
//		Object[] wd = purse.withdraw(-1, currency);
//		assertEmptyOrNull("Cannot withdraw negative amount", wd);
//	}
	
	/**
	 * Withdraw entire balance.
	 * Many student codes have problem with this.
	 */
	@Test(timeout=1000)
	public void testWithdrawEverything() {
		// A new purse with a few ones, fives, and tens
		// If you make the number really big,
		// you should also increase the timeout.
		int nOnes = 4;
		int nFives = 2;
		int nTens =  1;
		int size = nOnes+nFives+nTens;
		Purse purse = new Purse(size);
		
		// insert the coins
		for(int k=0; k<nOnes; k++) assertTrue(purse.insert(new Coin(1, currency)));
		for(int k=0; k<nFives; k++) assertTrue(purse.insert(new Coin(5, currency)));
		for(int k=0; k<nTens; k++) assertTrue(purse.insert(new Coin(10, currency)));
		double total = nOnes*1.0 + nFives*5.0 + nTens*10.0;
		
		assertEquals(total, purse.getBalance(currency), TOL);
		Object[] wd = purse.withdraw(total, currency);
		assertNotNull("Couldn't withdraw all money", wd);
		assertEquals("Withdraw array is wrong size. Should withdraw all coins",size,wd.length);
		assertEquals("Withdraw amount incorrect", total, sumMoney(wd), TOL);
		
		// should not be anything in purse
		assertEquals("Purse should be empty now", 0, purse.count());
		assertEquals("Purse should be empty now", 0.0, purse.getBalance(currency), TOL);
	}
	
	/**
	 * Test withdraw requiring many coins.
	 * We only use 1, 5, and 10 unit coins,
	 * so a greedy withdraw strategy should work.
	 */
	@Test(timeout=1000)
	public void testMultipleWithdraw() {
		Purse purse = new Purse(10);
		purse.insert(new Coin(1, currency) );
		purse.insert( new Coin(5, currency) );
		purse.insert( new Coin(1, currency) );
		Object[] wd = purse.withdraw(5,currency);
		assertNotNull( wd );
		assertEquals( 2, purse.getBalance(currency), TOL );
		assertEquals( 5, sumMoney(wd), TOL );
		
		// A new purse with a few ones, fives, and tens
		// If you make the number really big,
		// you should also increase the timeout.
		int nOnes = 4; // use 2-4, but not a multiple of 5
		int nFives = 3;
		int nTens =  3;
		purse = new Purse(nOnes+nFives+nTens);
		
		// insert the coins
		for(int k=0; k<nOnes; k++) assertTrue(purse.insert(new Coin(1, currency)));
		for(int k=0; k<nFives; k++) assertTrue(purse.insert(new Coin(5, currency)));
		for(int k=0; k<nTens; k++) assertTrue(purse.insert(new Coin(10, currency)));
		
		int balance = nOnes*1 + nFives*5 + nTens*10;
		assertEquals(balance, purse.getBalance(currency), TOL );
		
		// withdraw half the money
		int amount = nOnes/2 + 5*(nFives/2) + 10*(nTens/2);
		wd = purse.withdraw(amount, currency);
		assertNotNull( "Couldn't withdraw "+amount, wd );
		double sum = sumMoney( wd );
		assertEquals( "Didn't withdraw "+amount, amount, sum, TOL);
		assertEquals( balance - amount, purse.getBalance(currency), TOL );
		
		// withdraw the rest, except 1 Baht
		balance = balance - amount;
		amount = balance - 1;
		wd = purse.withdraw( amount, currency );
		
		assertNotNull( "Couldn't withdraw "+amount, wd );
		sum = sumMoney( wd );
		assertEquals( "Didn't withdraw "+amount, amount, sum, TOL );
		assertEquals( balance - amount, purse.getBalance(currency), TOL );
		
		balance = balance - amount;
		// withdraw the rest
		amount = balance;
		wd = purse.withdraw( amount, currency );
		
		assertNotNull( "Couldn't withdraw "+amount, wd );
		sum = sumMoney( wd );
		assertEquals( "Didn't withdraw "+amount, amount, sum, TOL );
		assertEquals( balance - amount, purse.getBalance(currency), TOL );
		
		// should be empty now
		assertEquals( 0, purse.getBalance(currency), TOL );
		assertEquals( 0, purse.count() );
	}
	
	/** Test withdraw for multiple currencies. */
	@Test
	public void testWithdrawUsesCurrency() {
		Purse purse = new Purse(20);
		String currency1 = "THB";
		String currency2 = "usd";
		Coin coin1 = new Coin(1, currency1);
		Coin coin2 = new Coin(10, currency2);
		purse.insert(coin1);
		purse.insert(coin2);
		assertNull( purse.withdraw(10, currency1));
		assertNull( purse.withdraw(1, currency2));
		Object[] wd = purse.withdraw(1, currency1);
		assertSame(coin1, wd[0]);
		wd = purse.withdraw(10, currency2);
		assertSame(coin2, wd[0]);
		// purse should be empty now
		assertEquals(0, purse.count());
		
		// intermix currencies
		purse.insert(new Coin(1, currency1));
		purse.insert(new Coin(5, currency2));
		purse.insert(new Coin(10, currency1));
		purse.insert(new Coin(5, "Fake"));
		purse.insert(new Coin(5, currency2));
		
		assertNull( purse.withdraw(15, currency2) );
		wd = purse.withdraw(10, currency2);
		assertEquals(10.0, sumMoney(wd), TOL);
		
		wd = purse.withdraw(10, currency1);
		assertEquals(10.0, sumMoney(wd), TOL);
		
		wd = purse.withdraw(1.0, currency1);
		assertEquals(1.0, sumMoney(wd), TOL);

		// Get the last one
		wd = purse.withdraw(5, "Fake");
		assertNotNull(wd);
		Coin fake = (Coin)wd[0];
		assertEquals(5.0, fake.getValue(), TOL);
		assertEquals("Fake", fake.getCurrency());
	}
	
	/** Parameter defined as Object so it works whether or not the student code
	 *  has a Valuable interface.
	 * @param money
	 * @return sum of all values, ignoring currency
	 */
	private static double sumMoney(Object[] money) {
	    if (money == null) return 0;
	    int sum = 0;
	    for(Object obj : money) {
	    	if (obj instanceof Coin) sum+= ((Coin)obj).getValue();
	    	else if (obj instanceof Valuable) sum += ((Valuable)obj).getValue();
	    }
	    return sum;
	}
	
	private void assertEmptyOrNull(String msg, Object[] array) {
		assertTrue(msg, array==null || array.length==0);
	}
}
