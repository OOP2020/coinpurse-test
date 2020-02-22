package coinpurse;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

/**
 * Simple tests of objects implementing Valuable.
 * @author jim
 *
 */
public class ValuableTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test(timeout=100)
	public void testImplementsValuable() {
		Coin c = new Coin(0.5,"Baht");
		BankNote b = new BankNote(20.0, "USD");
		assertTrue("Coin should implement Valuable", c instanceof Valuable);
		assertTrue("BankNote should implement Valuable", b instanceof Valuable);
	}
	
	@Test(timeout=100)
	public void testDeclaredMethods() {
		try {
			Method m = Valuable.class.getMethod("getValue");
			assertEquals("getValue returns double", double.class, m.getReturnType());
		} catch(NoSuchMethodException mex) {
			// this doesn't work. Throws exception if method not defined.
			fail("Valuable didn't define getValue()");
		}
		try {
			Method m = Valuable.class.getMethod("getCurrency");
			assertEquals("getCurrency returns String", String.class, m.getReturnType());
		} catch(NoSuchMethodException mex) {
			// this doesn't work. Throws exception if method not defined.
			fail("Valuable didn't define getCurrency()");
		}
	}
}
