package ca.utoronto.csc301.SelfCheckOut.IntegrationTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Enumeration;

import ca.utoronto.csc301.SelfCheckOut.App.BIC;
import ca.utoronto.csc301.SelfCheckOut.App.CheckOutCart;
import ca.utoronto.csc301.SelfCheckOut.App.GroceryItem;
import ca.utoronto.csc301.SelfCheckOut.App.Database;
import ca.utoronto.csc301.SelfCheckOut.App.SelfCheckOut;
import ca.utoronto.csc301.SelfCheckOut.App.UPC;
import ca.utoronto.csc301.SelfCheckOut.Devices.BaggingArea;
import ca.utoronto.csc301.SelfCheckOut.Devices.BaggingAreaEvent;
import ca.utoronto.csc301.SelfCheckOut.Devices.PaymentCollector;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.AddWhileBaggingException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.AddWhilePayingException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.IncorrectStateException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidBICException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidProductException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidUPCException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidWeightException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountException;

public class CartWithNoItems {

	static SelfCheckOut firstSCO;
	static BaggingArea firstBA;
	static Database firstPDB;
	static PaymentCollector firstPC;
	static double EPSILON = 1e-15;

	@BeforeClass
	public static void classSetUp() throws Exception {

		// create a SelfCheckOut
		firstBA = new BaggingArea();
		firstPC = new PaymentCollector();
		firstPDB = new Database("Database/TestSelfCheckOut.db");
		firstSCO = new SelfCheckOut(firstBA, firstPC, firstPDB);
	}

	@AfterClass
	public static void classTearDown() {
		firstSCO = null;
		firstBA = null;
		firstPDB = null;
		firstPC = null;
	}

	@After
	public void tearDown() {
		firstSCO.resetAll();
	}

	@Test
	public void listItemsInEmptyCart() {
		Enumeration<GroceryItem> egi;

		// there's nothing in the cart, so this should be null
		egi = firstSCO.listItemsInCart();
		assertFalse(egi.hasMoreElements());

	}

	@Test
	public void calculatingTotalCostOfEmptyCart() {
		assertEquals(0.0, firstSCO.getTotalCost(), 0.0);
	}

	@Test
	public void payingForNoGroceries() throws Exception {

		CheckOutCart coc;
		// test paying for empty grocery cart
		coc = firstSCO.payForGroceries();
		assertFalse(coc.listItems().hasMoreElements());
	}

	@Test
	public void addOneBulkProduct() throws InvalidWeightException {
		GroceryItem gi;
		BIC firstBIC;
		double firstWeight;
		double taxRate = 1;

		try {
			firstBIC = new BIC("11111");
			taxRate += firstPDB.lookUpItem(firstBIC).getTaxRate();
			assertEquals(taxRate, 1.0, EPSILON);
			firstWeight = 2.61;
			gi = firstSCO.addItem(firstBIC, firstWeight);
			assertNotNull(gi);
			assertEquals("Banana", gi.getInfo().getDescription());
			assertEquals(0.69, gi.getInfo().getPrice(), 0.0);
			assertEquals(firstWeight * 0.69 * taxRate, firstSCO.getTotalCost(),
					EPSILON);

		} catch (AddWhileBaggingException awbe) {
			fail("Item scanned before previous item is bagged.");
		} catch (AddWhilePayingException awpe) {
			fail("Item scanned while payment is being processed");
		} catch (InvalidProductException ipe) {
			fail("Item not recognized.");
		} catch (IncorrectStateException ise) {
			fail("Invalid action for current state of Self Check Out.");
		} catch (InvalidBICException e) {
			fail("Invalid code for BIC.");
		} catch (SaleDiscountException e) {
			fail("Invalid Sale");
		}
	}

	@Test
	public void addOnePackagedProduct() {
		GroceryItem pp;
		UPC firstUPC;
		double taxRate = 1;

		try {
			firstUPC = new UPC("786936224306");
			taxRate += firstPDB.lookUpItem(firstUPC).getTaxRate();
			pp = firstSCO.addItem(firstUPC);
			assertNotNull(pp);
			assertEquals("Kellogg Cereal", pp.getInfo().getDescription());
			assertEquals(3.52, pp.getInfo().getPrice(), 0.0);
			assertEquals(taxRate * 3.52, firstSCO.getTotalCost(), EPSILON);
		} catch (AddWhileBaggingException awbe) {
			fail("Item scanned before previous item is bagged.");
		} catch (AddWhilePayingException awpe) {
			fail("Item scanned while payment is being processed");
		} catch (InvalidProductException ipe) {
			fail("Item not recognized.");
		} catch (IncorrectStateException ise) {
			fail("Invalid action for current state of Self Check Out.");
		} catch (InvalidUPCException e) {
			fail("Invalid code for BIC.");
		} catch (InvalidWeightException e) {
			fail("Invalid Weight");
		} catch (SaleDiscountException e) {
			fail("Invalid Sale");
		}
	}

	@Test
	public void addCombinationBulkThenPackaged() {
		GroceryItem gi;
		BIC firstBIC;
		double firstWeight;
		BaggingAreaEvent event1;
		UPC firstUPC;
		double bulkTaxRate = 1;
		double packagedTaxRate = 1;

		try {
			firstBIC = new BIC("11111");
			firstWeight = 2.61;
			gi = firstSCO.addItem(firstBIC, firstWeight);
			bulkTaxRate += firstPDB.lookUpItem(firstBIC).getTaxRate();
			assertEquals(firstWeight * 0.69 * bulkTaxRate,
					firstSCO.getTotalCost(), EPSILON);
			event1 = new BaggingAreaEvent(0, firstWeight);
			// need to bag item before being able to add
			firstSCO.notifyBaggingAreaEvent(firstBA, event1);
			firstUPC = new UPC("786936224306");
			gi = firstSCO.addItem(firstUPC);
			packagedTaxRate += firstPDB.lookUpItem(firstUPC).getTaxRate();
			assertNotNull(gi);
			assertEquals("Kellogg Cereal", gi.getInfo().getDescription());
			assertEquals(3.52, gi.getInfo().getPrice(), 0.0);
			assertEquals(firstSCO.getTotalCost(), (packagedTaxRate * 3.52)
					+ firstWeight * 0.69, EPSILON);

		} catch (AddWhileBaggingException awbe) {
			fail("Item scanned before previous item is bagged.");
		} catch (AddWhilePayingException awpe) {
			fail("Item scanned while payment is being processed");
		} catch (InvalidProductException ipe) {
			fail("Item not recognized.");
		} catch (IncorrectStateException ise) {
			fail("Invalid action for current state of Self Check Out.");
		} catch (InvalidBICException e) {
			fail("Invalid code for BIC.");
		} catch (InvalidUPCException e) {
			fail("Invalid UPC");
		} catch (InvalidWeightException e) {
			fail("Invalid Weight");
		} catch (SaleDiscountException e) {
			fail("Invalid Sale");
		}
	}

}
