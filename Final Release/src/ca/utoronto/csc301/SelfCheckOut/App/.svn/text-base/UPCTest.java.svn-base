/* Creator: Mihai Nicolae
 * 
 * Created on September 26, 2012
 * Updated on September 26, 2012
 * 
 * This class contains JUnit test cases for UPC.java.
 * 
 */
package ca.utoronto.csc301.SelfCheckOut.App;

import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidUPCException;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UPCTest {

	UPC firstUPC;
	String firstCode;

	@Before
	public void setUp() throws Exception {
		firstCode = "786936224306";
		try {
			firstUPC = new UPC(firstCode);
		} catch (InvalidUPCException iUPCe) {
			fail("Invalid UPC");
		}
	}

	@After
	public void tearDown() throws Exception {
		firstUPC = null;
		firstCode = null;
	}

	@Test(expected = InvalidUPCException.class)
	public void constructWithNullUPC() throws InvalidUPCException {
		UPC secondUPC = new UPC(null);
	}

	@Test(expected = InvalidUPCException.class)
	public void constructWithCodeLengthGT12() throws InvalidUPCException {
		UPC secondUPC = new UPC("0123456789123");
	}

	@Test(expected = InvalidUPCException.class)
	public void constructWithCodeLengthLT12() throws InvalidUPCException {
		UPC secondUPC = new UPC("01234567890");
	}

	@Test(expected = InvalidUPCException.class)
	public void constructWithCodeLength1() throws InvalidUPCException {
		UPC secondUPC = new UPC("0");
	}

	@Test(expected = InvalidUPCException.class)
	public void constructWithFailingChecksum() throws InvalidUPCException {
		UPC secondUPC = new UPC("01234567890");
	}

	/**
	 * Test the UPC constructor with a 12-character alphabetical string. The
	 * string was chosen specifically to pass the checksum.
	 * 
	 * @throws InvalidUPCException
	 */
	@Test(expected = InvalidUPCException.class)
	public void constructWithAlphabeticalChars() throws InvalidUPCException {
		UPC secondUPC = new UPC("abcdefghijkt");
	}

	/**
	 * Test the UPC constructor with an 12-character alpha-numerical string. The
	 * string was chosen specifically to pass the checksum.
	 * 
	 * @throws InvalidUPCException
	 */
	@Test(expected = InvalidUPCException.class)
	public void constructWithAlphaNumericalChars() throws InvalidUPCException {
		UPC secondUPC = new UPC("a0c2e4g6i8k8");
	}

	/**
	 * Test the UPC constructor with a 12-character non-alpha-numerical string.
	 * The string was chosen specifically to pass the checksum.
	 * 
	 * @throws InvalidUPCException
	 */
	@Test(expected = InvalidUPCException.class)
	public void constructWithNonAlphaNumericalChars()
			throws InvalidUPCException {
		UPC secondUPC = new UPC(";<=>?@#$%&'(");
	}

	@Test
	public void testGetCode() {
		Long i = new Long(firstCode) - 1;

		assertEquals(firstCode, firstUPC.getCode());
		assertFalse(i.equals(firstUPC.getCode()));
	}

	@Test
	public void sameUPCs() throws InvalidUPCException {
		UPC secondUPC = new UPC(firstCode);

		assertTrue(firstUPC.equals(firstUPC));
		assertTrue(firstUPC.equals(secondUPC));
	}

	@Test
	public void differentUPCs() throws InvalidUPCException {
		UPC secondUPC = new UPC("012398235414");
		assertFalse(firstUPC.equals(secondUPC));
	}

	@Test
	public void inputUPCWrongType() throws InvalidUPCException {
		Object obj = new Object();
		assertFalse(firstUPC.equals(obj));
	}

}
