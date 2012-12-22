/**
 * Integration tests for purchasing impulse buy products

 */
package ca.utoronto.csc301.SelfCheckOut.IntegrationTests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.utoronto.csc301.SelfCheckOut.App.BIC;
import ca.utoronto.csc301.SelfCheckOut.App.CheckOutCart;
import ca.utoronto.csc301.SelfCheckOut.App.Database;
import ca.utoronto.csc301.SelfCheckOut.App.SelfCheckOut;
import ca.utoronto.csc301.SelfCheckOut.App.UPC;
import ca.utoronto.csc301.SelfCheckOut.Devices.BaggingArea;
import ca.utoronto.csc301.SelfCheckOut.Devices.BaggingAreaEvent;

import ca.utoronto.csc301.SelfCheckOut.Devices.PaymentCollector;

import ca.utoronto.csc301.SelfCheckOut.Exceptions.IncorrectStateException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidBICException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidProductException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidWeightException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountException;

import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidUPCException;

public class PurchaseImpulseProducts {

	static SelfCheckOut firstSCO;
	static BaggingArea firstBA;
	static PaymentCollector firstPC;
	static Database PDB;
	static int firstSCOid;
	static CheckOutCart firstCOO;
	static int secondSCOid;
	static BIC firstImpulseBulk;
	static double firstImpulseBulkWeight;
	static UPC firstImpulsePackaged;
	static double firstImpulsePackagedWeight;
	static double EPSILON = 1e-13;
	static BaggingAreaEvent event;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		firstBA = new BaggingArea();
		firstPC = new PaymentCollector();
		PDB = new Database("Database/TestSelfCheckOut.db");
		firstSCOid = 1;
		secondSCOid = 2;
		firstSCO = new SelfCheckOut(firstBA, firstPC, PDB, firstSCOid);
		firstImpulseBulkWeight = 3.4;
		firstImpulsePackagedWeight = 5.3;

		// create a packaged item
		try {
			firstImpulsePackaged = new UPC("780166035718");
		} catch (InvalidUPCException e) {
			fail("Invalid UPC");
		}
		// create a bulk item
		try {
			firstImpulseBulk = new BIC("77777");
		} catch (InvalidBICException e) {
			fail("Invalid BIC");
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		firstBA = null;
		firstPC = null;
		PDB = null;
		firstSCOid = 0;
		secondSCOid = 0;
		firstSCO = null;
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		firstSCO.resetAll();
	}

	@Test
	public void testGetTotalBought() {
		double totalBoughtBulk = 14;
		double totalBoughtPackaged = 2;
		// check the actual data in database
		assertEquals(totalBoughtBulk,
				PDB.getTotalBought(firstSCOid, firstImpulseBulk), EPSILON);
		assertEquals(totalBoughtPackaged,
				PDB.getTotalBought(firstSCOid, firstImpulsePackaged), EPSILON);
	}

	@Test
	public void testGetProfitMargin() {
		double profitBulk = 2.1;
		double profitPackaged = 4.16;

		// check the actual data in database
		assertEquals(profitBulk,
				PDB.getProfitMargin(firstSCOid, firstImpulseBulk), EPSILON);
		assertEquals(profitPackaged,
				PDB.getProfitMargin(firstSCOid, firstImpulsePackaged), EPSILON);
	}

	@Test
	public void purchaseOneImpulseProduct() throws Exception {

		try {
			firstSCO.addItem(firstImpulsePackaged);

			// check the original data in database
			assertEquals(2,
					PDB.getTotalBought(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16,
					PDB.getProfitMargin(firstSCOid, firstImpulsePackaged),
					EPSILON);
			// bag item before payment
			event = new BaggingAreaEvent(0, firstImpulsePackagedWeight);
			firstSCO.notifyBaggingAreaEvent(firstBA, event);

			// pay for the product and check if update changed in database
			firstCOO = firstSCO.payForGroceries();
			assertEquals(3,
					PDB.getTotalBought(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16 + (4.16 / 2),
					PDB.getProfitMargin(firstSCOid, firstImpulsePackaged),
					EPSILON);

			// finally undo the update to preserve original database data
			PDB.undoUpdateForTesting(firstCOO, firstSCO.getSCOid());

			// check the original data in database again
			assertEquals(2,
					PDB.getTotalBought(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16,
					PDB.getProfitMargin(firstSCOid, firstImpulsePackaged),
					EPSILON);

		} catch (IncorrectStateException e) {
			fail("Invalid State.");
		} catch (InvalidProductException e) {
			fail("Invalid Prodduct.");
		} catch (InvalidWeightException e) {
			fail("Invalid Weight.");
		}
	}

	@Test
	public void purchaseOneImpulseBulk() throws Exception {

		try {
			firstSCO.addItem(firstImpulseBulk, 7.5);

			// check the original data in database
			assertEquals(14.0,
					PDB.getTotalBought(firstSCOid, firstImpulseBulk), EPSILON);
			assertEquals(2.1,
					PDB.getProfitMargin(firstSCOid, firstImpulseBulk), EPSILON);

			// bag item before payment
			event = new BaggingAreaEvent(0, firstImpulseBulkWeight);
			firstSCO.notifyBaggingAreaEvent(firstBA, event);

			// pay for the product and check if update changed in database
			firstCOO = firstSCO.payForGroceries();
			assertEquals(21.5,
					PDB.getTotalBought(firstSCOid, firstImpulseBulk), EPSILON);
			assertEquals(2.1 + ((2.1 / 14.0) * 7.5),
					PDB.getProfitMargin(firstSCOid, firstImpulseBulk), EPSILON);

			// finally undo the update to preserve original database data
			PDB.undoUpdateForTesting(firstCOO, firstSCO.getSCOid());

			// check the original data in database again
			assertEquals(14.0,
					PDB.getTotalBought(firstSCOid, firstImpulseBulk), EPSILON);
			assertEquals(2.1,
					PDB.getProfitMargin(firstSCOid, firstImpulseBulk), EPSILON);

		} catch (IncorrectStateException e) {
			fail("Invalid State.");
		} catch (InvalidProductException e) {
			fail("Invalid Prodduct.");
		} catch (InvalidWeightException e) {
			fail("Invalid Weight.");
		}
	}

	/**
	 * Test for purchasing 2 packaged and 2 bulk impulse products.
	 * 
	 * @throws Exception
	 */
	@Test
	public void purchaseCombinationImpulse() throws Exception {
		BaggingAreaEvent event1;
		BaggingAreaEvent event2;
		BaggingAreaEvent event3;
		// create two new products
		try {
			BIC secondImpulseBulk = new BIC("66666");
			UPC secondImpulsePackaged = new UPC("737666003167");

			// appropriately add and bag all the items
			firstSCO.addItem(firstImpulsePackaged);
			event1 = new BaggingAreaEvent(0, 1);
			firstSCO.notifyBaggingAreaEvent(firstBA, event1);
			firstSCO.addItem(secondImpulsePackaged);
			event2 = new BaggingAreaEvent(1, 1);
			firstSCO.notifyBaggingAreaEvent(firstBA, event2);
			firstSCO.addItem(firstImpulseBulk, 2.4);
			event3 = new BaggingAreaEvent(2, 2.4);
			firstSCO.notifyBaggingAreaEvent(firstBA, event3);
			firstSCO.addItem(secondImpulseBulk, 5.0);
			event1 = new BaggingAreaEvent(3, firstImpulseBulkWeight);
			firstSCO.notifyBaggingAreaEvent(firstBA, event);

			firstCOO = firstSCO.payForGroceries();

			// check for new correct new values
			assertEquals(3,
					PDB.getTotalBought(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16 + (4.16 / 2),
					PDB.getProfitMargin(firstSCOid, firstImpulsePackaged),
					EPSILON);

			assertEquals(16.4,
					PDB.getTotalBought(firstSCOid, firstImpulseBulk), EPSILON);
			assertEquals(2.1 + ((2.1 / 14.0) * 2.4),
					PDB.getProfitMargin(firstSCOid, firstImpulseBulk), EPSILON);

			assertEquals(2,
					PDB.getTotalBought(firstSCOid, secondImpulsePackaged),
					EPSILON);
			assertEquals(0.5 + (0.5 / 1),
					PDB.getProfitMargin(firstSCOid, secondImpulsePackaged),
					EPSILON);

			assertEquals(10.0,
					PDB.getTotalBought(firstSCOid, secondImpulseBulk), EPSILON);
			assertEquals(0.15 + ((0.15 / 5.0) * 5.0),
					PDB.getProfitMargin(firstSCOid, secondImpulseBulk), EPSILON);

			// finally undo the update to preserve original database data
			PDB.undoUpdateForTesting(firstCOO, firstSCO.getSCOid());

			// check if original database data is restored

			assertEquals(2,
					PDB.getTotalBought(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16,
					PDB.getProfitMargin(firstSCOid, firstImpulsePackaged),
					EPSILON);

			assertEquals(14, PDB.getTotalBought(firstSCOid, firstImpulseBulk),
					EPSILON);
			assertEquals(2.1,
					PDB.getProfitMargin(firstSCOid, firstImpulseBulk), EPSILON);

			assertEquals(1,
					PDB.getTotalBought(firstSCOid, secondImpulsePackaged),
					EPSILON);
			assertEquals(0.5,
					PDB.getProfitMargin(firstSCOid, secondImpulsePackaged),
					EPSILON);

			assertEquals(5, PDB.getTotalBought(firstSCOid, secondImpulseBulk),
					EPSILON);
			assertEquals(0.15,
					PDB.getProfitMargin(firstSCOid, secondImpulseBulk), EPSILON);

		} catch (InvalidBICException e) {
			fail("Invalid BIC.");
		} catch (InvalidUPCException e) {
			fail("Invalid UPC.");
		} catch (IncorrectStateException e) {
			fail("Invalid State.");
		} catch (InvalidProductException e) {
			fail("Invalid Product.");
		} catch (InvalidWeightException e) {
			fail("Invalid Weight.");
		}
	}

	/**
	 * Test for purchasing different impulse items in two different self
	 * checkout stands.
	 * 
	 * @throws Exception
	 */
	@Test
	public void purchaseDifferentWithDifferentSCOid() throws Exception {

		// create components for a second, separate SCO
		BaggingArea secondBA = new BaggingArea();
		PaymentCollector secondPC = new PaymentCollector();
		SelfCheckOut secondSCO = new SelfCheckOut(secondBA, secondPC, PDB,
				secondSCOid);
		CheckOutCart secondCOO;
		BaggingAreaEvent event2 = new BaggingAreaEvent(0,
				firstImpulseBulkWeight);

		try {
			firstSCO.addItem(firstImpulsePackaged);
			secondSCO.addItem(firstImpulseBulk, 3.5);

			// bag items before payment
			event = new BaggingAreaEvent(0, firstImpulsePackagedWeight);
			firstSCO.notifyBaggingAreaEvent(firstBA, event);
			event2 = new BaggingAreaEvent(0, firstImpulseBulkWeight);
			secondSCO.notifyBaggingAreaEvent(secondBA, event2);

			firstCOO = firstSCO.payForGroceries();
			secondCOO = secondSCO.payForGroceries();

			// check if database updated correctly
			assertEquals(3,
					PDB.getTotalBought(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16 + (4.16 / 2),
					PDB.getProfitMargin(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(60.5,
					PDB.getTotalBought(secondSCOid, firstImpulseBulk), EPSILON);
			assertEquals(8.55 + ((8.55 / 57) * 3.5),
					PDB.getProfitMargin(secondSCOid, firstImpulseBulk), EPSILON);

			// undo changes
			PDB.undoUpdateForTesting(firstCOO, firstSCOid);
			PDB.undoUpdateForTesting(secondCOO, secondSCOid);

			// check for original data
			assertEquals(2,
					PDB.getTotalBought(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16,
					PDB.getProfitMargin(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(57, PDB.getTotalBought(secondSCOid, firstImpulseBulk),
					EPSILON);
			assertEquals(8.55,
					PDB.getProfitMargin(secondSCOid, firstImpulseBulk), EPSILON);

		} catch (IncorrectStateException e) {
			fail("Invalid State.");
		} catch (InvalidProductException e) {
			fail("Invalid Product.");
		} catch (InvalidWeightException e) {
			fail("Invalid Weight.");
		}
	}

	/**
	 * Test for purchasing the same impulse items in two different self checkout
	 * stands.
	 * 
	 * @throws Exception
	 */
	@Test
	public void purchaseSameWithDifferentSCOid() throws Exception {

		// create components for a second, separate SCO
		BaggingArea secondBA = new BaggingArea();
		PaymentCollector secondPC = new PaymentCollector();
		SelfCheckOut secondSCO = new SelfCheckOut(secondBA, secondPC, PDB,
				secondSCOid);
		CheckOutCart secondCOO;
		BaggingAreaEvent event2 = new BaggingAreaEvent(0,
				firstImpulseBulkWeight);

		try {
			firstSCO.addItem(firstImpulsePackaged);
			secondSCO.addItem(firstImpulsePackaged);

			// bag items before payment
			event = new BaggingAreaEvent(0, firstImpulsePackagedWeight);
			firstSCO.notifyBaggingAreaEvent(firstBA, event);
			event2 = new BaggingAreaEvent(0, firstImpulsePackagedWeight);
			secondSCO.notifyBaggingAreaEvent(secondBA, event2);

			firstCOO = firstSCO.payForGroceries();
			secondCOO = secondSCO.payForGroceries();

			// check if database updated correctly
			assertEquals(3,
					PDB.getTotalBought(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16 + (4.16 / 2),
					PDB.getProfitMargin(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(3,
					PDB.getTotalBought(secondSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16 + (4.16 / 2),
					PDB.getProfitMargin(secondSCOid, firstImpulsePackaged),
					EPSILON);

			// undo changes
			PDB.undoUpdateForTesting(firstCOO, firstSCOid);
			PDB.undoUpdateForTesting(secondCOO, secondSCOid);

			// check for original data
			assertEquals(2,
					PDB.getTotalBought(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16,
					PDB.getProfitMargin(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(2,
					PDB.getTotalBought(secondSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16,
					PDB.getProfitMargin(secondSCOid, firstImpulsePackaged),
					EPSILON);

		} catch (IncorrectStateException e) {
			fail("Invalid State.");
		} catch (InvalidProductException e) {
			fail("Invalid Product.");
		} catch (InvalidWeightException e) {
			fail("Invalid Weight.");
		}
	}

	/**
	 * Test for purchasing multiple impulse items in 3 different self checkouts.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void purchaseMultipleDifferentSCOid() throws Exception {
		BaggingAreaEvent event1;
		BaggingAreaEvent event2;
		BaggingAreaEvent event3;
		int thirdSCOid = 3;
		// create components for a second, separate SCO
		BaggingArea secondBA = new BaggingArea();
		PaymentCollector secondPC = new PaymentCollector();
		SelfCheckOut secondSCO = new SelfCheckOut(secondBA, secondPC, PDB,
				secondSCOid);
		CheckOutCart secondCOO;
		BaggingAreaEvent event4 = new BaggingAreaEvent(0,
				firstImpulsePackagedWeight);

		// create components for a third, separate SCO
		BaggingArea thirdBA = new BaggingArea();
		PaymentCollector thirdPC = new PaymentCollector();

		SelfCheckOut thirdSCO = new SelfCheckOut(thirdBA, thirdPC, PDB,
				thirdSCOid);
		CheckOutCart thirdCOO;
		BaggingAreaEvent event5 = new BaggingAreaEvent(0,
				firstImpulseBulkWeight);

		try {
			// add and bag 2 items at each self checkout
			firstSCO.addItem(firstImpulsePackaged);
			event = new BaggingAreaEvent(0, firstImpulsePackagedWeight);
			firstSCO.notifyBaggingAreaEvent(firstBA, event);

			firstSCO.addItem(firstImpulseBulk, firstImpulseBulkWeight);
			event1 = new BaggingAreaEvent(firstImpulsePackagedWeight,
					firstImpulseBulkWeight);
			firstSCO.notifyBaggingAreaEvent(firstBA, event1);

			secondSCO.addItem(firstImpulseBulk, firstImpulseBulkWeight);
			event4 = new BaggingAreaEvent(0, firstImpulseBulkWeight);
			secondSCO.notifyBaggingAreaEvent(secondBA, event4);

			secondSCO.addItem(firstImpulsePackaged);
			event2 = new BaggingAreaEvent(firstImpulseBulkWeight,
					firstImpulsePackagedWeight);
			secondSCO.notifyBaggingAreaEvent(secondBA, event2);

			thirdSCO.addItem(firstImpulsePackaged);
			event5 = new BaggingAreaEvent(0, firstImpulsePackagedWeight);
			thirdSCO.notifyBaggingAreaEvent(thirdBA, event5);

			thirdSCO.addItem(firstImpulseBulk, firstImpulseBulkWeight);
			event3 = new BaggingAreaEvent(firstImpulsePackagedWeight,
					firstImpulseBulkWeight);
			thirdSCO.notifyBaggingAreaEvent(thirdBA, event3);

			// pay for everything
			firstCOO = firstSCO.payForGroceries();
			secondCOO = secondSCO.payForGroceries();
			thirdCOO = thirdSCO.payForGroceries();

			// check if database updated correctly
			assertEquals(3,
					PDB.getTotalBought(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16 + (4.16 / 2),
					PDB.getProfitMargin(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(17.4,
					PDB.getTotalBought(firstSCOid, firstImpulseBulk), EPSILON);
			assertEquals(2.1 + ((2.1 / 14.0) * 3.4),
					PDB.getProfitMargin(firstSCOid, firstImpulseBulk), EPSILON);

			assertEquals(3,
					PDB.getTotalBought(secondSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16 + (4.16 / 2),
					PDB.getProfitMargin(secondSCOid, firstImpulsePackaged),
					EPSILON);

			assertEquals(60.4,
					PDB.getTotalBought(secondSCOid, firstImpulseBulk), EPSILON);
			assertEquals(8.55 + ((8.55 / 57.0) * 3.4),
					PDB.getProfitMargin(secondSCOid, firstImpulseBulk), EPSILON);

			assertEquals(54,
					PDB.getTotalBought(thirdSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(110.24 + (110.24 / 53),
					PDB.getProfitMargin(thirdSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(36.4,
					PDB.getTotalBought(thirdSCOid, firstImpulseBulk), EPSILON);
			assertEquals(4.95 + ((4.95 / 33.0) * 3.4),
					PDB.getProfitMargin(thirdSCOid, firstImpulseBulk), EPSILON);

			// undo changes
			PDB.undoUpdateForTesting(firstCOO, firstSCOid);
			PDB.undoUpdateForTesting(secondCOO, secondSCOid);
			PDB.undoUpdateForTesting(thirdCOO, thirdSCOid);

			// check for original data
			assertEquals(2,
					PDB.getTotalBought(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16,
					PDB.getProfitMargin(firstSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(14, PDB.getTotalBought(firstSCOid, firstImpulseBulk),
					EPSILON);
			assertEquals(2.1,
					PDB.getProfitMargin(firstSCOid, firstImpulseBulk), EPSILON);

			assertEquals(2,
					PDB.getTotalBought(secondSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(4.16,
					PDB.getProfitMargin(secondSCOid, firstImpulsePackaged),
					EPSILON);

			assertEquals(57, PDB.getTotalBought(secondSCOid, firstImpulseBulk),
					EPSILON);
			assertEquals(8.55,
					PDB.getProfitMargin(secondSCOid, firstImpulseBulk), EPSILON);

			assertEquals(53,
					PDB.getTotalBought(thirdSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(110.24,
					PDB.getProfitMargin(thirdSCOid, firstImpulsePackaged),
					EPSILON);
			assertEquals(33, PDB.getTotalBought(thirdSCOid, firstImpulseBulk),
					EPSILON);
			assertEquals(4.95,
					PDB.getProfitMargin(thirdSCOid, firstImpulseBulk), EPSILON);
		} catch (IncorrectStateException e) {
			fail("Invalid State.");
		} catch (InvalidProductException e) {
			fail("Invalid Product.");
		} catch (InvalidWeightException e) {
			fail("Invalid Weight.");
		} catch (SaleDiscountException e) {
			fail("Invalid Discount.");
		} catch (SQLException e) {
			fail("Invalid Database Query.");
		}
	}
}
