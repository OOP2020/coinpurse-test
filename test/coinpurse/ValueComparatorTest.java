package coinpurse;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ValueComparatorTest {
        private ValueComparator comparator;
        
        @Before
        public void setUp() {
            comparator = new ValueComparator();
        }
    
	/**
	 * test that compareTo orders by increasing value
	 */
	@Test(timeout=100)
	public void testCompareByValue() {
		String currency = "USD";
		BankNote note1 = new BankNote(10, currency);
		BankNote note2 = new BankNote(20, currency);
        Coin coin = new Coin(10, currency);
//		assertTrue( bank1.compareTo(null) < 0 );
		assertEquals( 0, comparator.compare(note1, coin) );
                
		assertTrue( comparator.compare(note1, note2) < 0 );
		assertTrue( comparator.compare(note2, coin) > 0 );
		BankNote clone = new BankNote(note1.getValue(), note1.getCurrency());
		assertEquals( 0, comparator.compare(note1, clone) );
		// an extreme case
		BankNote max = new BankNote(Integer.MAX_VALUE, currency);
		assertTrue( comparator.compare(note1, max) < 0 );
		assertTrue( comparator.compare(max, note1) > 0 );
	}
	
	/** Comparator should order by currency ignoring case before value. */
	@Test(timeout=100)
	public void testCompareByCurrency() {
		Coin euro = new Coin(1, "Euro");
		Coin lowereuro = new Coin(1, "euro");
		assertEquals(0, comparator.compare(euro, lowereuro));
		
		BankNote astra = new BankNote(10, "Astra");
		assertTrue(comparator.compare(astra, euro) < 0);
		assertTrue(comparator.compare(lowereuro,astra) > 0);
		
		BankNote dollars = new BankNote(100, "Dollars");
		assertTrue(comparator.compare(astra, dollars) < 0);
		assertTrue(comparator.compare(euro, dollars) > 0);

		BankNote dollar = new BankNote(100, "dollar");
		assertTrue(comparator.compare(dollar, dollars) < 0);
	}
	
	@Test(timeout=100)
	public void testCompareToUsesDouble() {
		String currency = "Baht";
		BankNote bank1 = new BankNote(0.1, currency);
		BankNote bank2 = new BankNote(0.2, currency);
		assertTrue( "0.1Baht < 0.2Baht", comparator.compare(bank1,bank2) < 0);
		assertTrue( "0.2Baht > 0.1Baht", comparator.compare(bank2,bank1) > 0);
		bank1 = new BankNote(0.99, currency);
		bank2 = new BankNote(1.00, currency);
		assertTrue( "0.99Baht < 1.0Baht", comparator.compare(bank1,bank2) < 0);
		assertTrue( "1.0Baht > 0.99Baht", comparator.compare(bank2,bank1) > 0);
		Coin coin1 = new Coin(0.001, currency);
		Coin coin2 = new Coin(0.001, currency);
		assertEquals(0, comparator.compare(coin1,coin2));
		coin2 = new Coin(0.01, currency);
		assertTrue(comparator.compare(coin1,coin2) < 0);
	}
}
