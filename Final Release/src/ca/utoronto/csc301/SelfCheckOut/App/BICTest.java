/* Creator: Susan Elliott Sim
 * 
 * Created on January 29, 2008
 * Updated on September 12, 2012
 * 
 * This class contains JUnit test cases for BIC.java.
 * 
 */

package ca.utoronto.csc301.SelfCheckOut.App;

import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidBICException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidUPCException;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BICTest {

	BIC firstBIC;
	String firstCode;

	@Before
	public void setUp() throws Exception {
		firstCode = "55555";
		try {
			firstBIC = new BIC(firstCode);
		} catch (InvalidBICException e) {
			fail("Invalid BIC");
		}
	}

	@After
	public void tearDown() throws Exception {
		firstBIC = null;
		firstCode = null;
	}

	@Test(expected = InvalidBICException.class)
	public void constructWithNullBIC() throws InvalidBICException {
		BIC secondBIC = new BIC(null);
	}

	@Test(expected = InvalidBICException.class)
	public void constructWithCodeLengthGT5() throws InvalidBICException {
		BIC secondBIC = new BIC("666666");
	}

	@Test(expected = InvalidBICException.class)
	public void constructWithCodeLength1() throws InvalidBICException {
		BIC secondBIC = new BIC("0");
	}

	/**
	 * Test the BIC constructor with a 5-character alphabetical string.
	 * 
	 * @throws InvalidUPCException
	 */
	@Test(expected = InvalidBICException.class)
	public void constructWithAlphabeticalChars() throws InvalidBICException {
		BIC secondBIC = new BIC("abcde");
	}

	/**
	 * Test the BIC constructor with an 5-character alpha-numerical string.
	 * 
	 * @throws InvalidUPCException
	 */
	@Test(expected = InvalidBICException.class)
	public void constructWithAlphaNumericalChars() throws InvalidBICException {
		BIC secondBIC = new BIC("a1b2c");
	}

	/**
	 * Test the UPC constructor with a 5-character non-alpha-numerical string.
	 * 
	 * @throws InvalidUPCException
	 */
	@Test(expected = InvalidBICException.class)
	public void constructWithNonAlphaNumericalChars()
			throws InvalidBICException {
		BIC secondBIC = new BIC("&*$@!");
	}

	@Test
	public void testGetCode() {
		Integer i = new Integer(firstCode) - 1;

		assertEquals(firstCode, firstBIC.getCode());
		assertFalse(i.equals(firstBIC.getCode()));
	}

	@Test
	public void sameBICs() throws InvalidBICException {
		BIC secondBIC = new BIC(firstCode);

		assertTrue(firstBIC.equals(firstBIC));
		assertTrue(firstBIC.equals(secondBIC));
	}

	@Test
	public void twoDifferentBICs() throws InvalidBICException {
		BIC secondBIC = new BIC("12345");
		assertFalse(firstBIC.equals(secondBIC));
	}

	@Test
	public void inputBICWrongType() throws InvalidBICException {
		Object obj = new Object();
		assertFalse(firstBIC.equals(obj));
	}

}
