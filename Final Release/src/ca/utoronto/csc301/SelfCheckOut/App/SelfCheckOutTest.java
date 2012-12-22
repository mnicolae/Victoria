/* Creator: Mihai Nicolae
 * 
 * Created on September 26, 2012
 * Updated on September 26, 2012
 * 
 * This class contains JUnit test cases for SelfCheckOut.java.
 * 
 */
package ca.utoronto.csc301.SelfCheckOut.App;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.utoronto.csc301.SelfCheckOut.Devices.BaggingArea;
import ca.utoronto.csc301.SelfCheckOut.Devices.PaymentCollector;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.AddWhileBaggingException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.IncorrectStateException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidBICException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidProductException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidUPCException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidWeightException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountException;

public class SelfCheckOutTest {

	static SelfCheckOut sco;
	static BaggingArea bArea;
	static PaymentCollector pCollector;
	static Database pdb;
	static UPC secondUPC, nonDbUPC;
	static BIC secondBIC, nonDbBIC;
	static double secondUPCWeight;
	static double secondBICWeight;
	static double secondBulkTaxRate = 1;
	static double secondPackagedTaxRate = 1;
	static double EPSILON = 1e-13;

	@BeforeClass
	public static void classSetUp() throws Exception {
		bArea = new BaggingArea();
		pCollector = new PaymentCollector();
		pdb = new Database("Database/TestSelfCheckOut.db");
		sco = new SelfCheckOut(bArea, pCollector, pdb);

		// create some packaged items
		try {
			secondUPC = new UPC("786936224306");
			nonDbUPC = new UPC("012398235414");
		} catch (InvalidUPCException e) {
			fail("Invalid UPC");
		}

		// create some bulk items
		try {
			secondBIC = new BIC("22222");
			nonDbBIC = new BIC("12346");
		} catch (InvalidBICException e) {
			fail("Invalid BIC");
		}

		secondBulkTaxRate += pdb.lookUpItem(secondBIC).getTaxRate();
		secondPackagedTaxRate = 1;
		secondPackagedTaxRate += pdb.lookUpItem(secondUPC).getTaxRate();
		secondBICWeight = 2.61;
		secondUPCWeight = 1.35;
	}

	@AfterClass
	public static void classTearDown() throws Exception {
		bArea = null;
		pCollector = null;
		pdb = null;
		sco = null;
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
		sco.resetAll();
	}

//  ===== Test for exceptions =====
//	===============================
	/*
	 * Add a BIC item that doesn't exist in the database.
	 */
	@Test(expected = InvalidProductException.class)
	public void testAddNonExistingBICItem() throws Exception {
		sco.addItem(nonDbBIC, 10);
	}

	/*
	 * Add a UPC item that doesn't exist in the database.
	 */
	@Test(expected = InvalidProductException.class)
	public void testAddNonExistingUPCItem() throws Exception {
		sco.addItem(nonDbUPC);
	}

	/*
	 * Add a BIC item with a negative weight.
	 */
	@Test(expected = InvalidWeightException.class)
	public void testAddBICItemNegativeWeight() throws Exception {
		int negWeight = -10;
		sco.addItem(secondBIC, negWeight);
	}

	/*
	 * Add an item without bagging the previous one.
	 */
	@Test(expected = AddWhileBaggingException.class)
	public void testAddWithoutBaggingFirst() throws Exception {
		sco.addItem(secondUPC);
		sco.addItem(secondBIC, secondBICWeight);
	}

//  ===== End of tests for exceptions =====
//  =======================================

//  ===== Tests on empty SelfCheckOut =====
//  =======================================
	/*
	 * Test getTotalCost method on an empty selfCheckOut
	 */
	@Test
	public void testGetTotalCostEmptySco() throws Exception {
		assertEquals(sco.getTotalCost(), 0, EPSILON);
	}

	/*
	 * Test getTotalTax method on an empty selfCheckOut
	 */
	@Test
	public void testGetTotalTaxEmptySco() throws Exception {
		assertEquals(sco.getTotalTax(), 0, EPSILON);
	}

	/*
	 * Test getSubTotal method on an empty selfCheckOut
	 */
	@Test
	public void testGetSubTotalEmptySco() throws Exception {
		assertEquals(sco.getSubTotal(), 0, EPSILON);
	}

	/*
	 * Test getTotalDiscount method on an empty selfCheckOut
	 */
	@Test
	public void testGetTotalDiscountEmptySco() throws Exception {
		assertEquals(sco.getTotalDiscount(), 0, EPSILON);
	}

//  ===== End of tests on empty SelfCheckOut =====
//  =======================================
	
	/*
	 * Test getSCOid method
	 */
	@Test
	public void testGetSCOid() throws Exception {
		assertEquals(sco.getSCOid(), 0, EPSILON);
	}
	
	/*
	 * Add a BIC item that exists in the database.
	 */
	@Test
	public void testAddValidBICItem() throws Exception {

	}

	/*
	 * Add a UPC item that exists in the database.
	 */
	@Test
	public void testAddValidUPCItem() throws Exception {

	}
}
