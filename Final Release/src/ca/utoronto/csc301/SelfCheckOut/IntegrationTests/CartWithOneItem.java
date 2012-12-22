package ca.utoronto.csc301.SelfCheckOut.IntegrationTests;

import static org.junit.Assert.*;

import java.util.Enumeration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.utoronto.csc301.SelfCheckOut.App.BIC;
import ca.utoronto.csc301.SelfCheckOut.App.CheckOutCart;
import ca.utoronto.csc301.SelfCheckOut.App.GroceryItem;
import ca.utoronto.csc301.SelfCheckOut.App.Database;
import ca.utoronto.csc301.SelfCheckOut.App.SelfCheckOut;
import ca.utoronto.csc301.SelfCheckOut.App.UPC;
import ca.utoronto.csc301.SelfCheckOut.Devices.BaggingArea;
import ca.utoronto.csc301.SelfCheckOut.Devices.BaggingAreaEvent;
import ca.utoronto.csc301.SelfCheckOut.Devices.PaymentCollector;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.IncorrectStateException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidBICException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidProductException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidUPCException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidWeightException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountException;

public class CartWithOneItem {

	static SelfCheckOut firstSCO;
	static BaggingArea firstBA;
	static Database firstPDB;
	static PaymentCollector firstPC;
	static double EPSILON = 1e-15;
	static BIC firstBIC;
	static UPC firstUPC;
	static double firstUPCWeight;
	static double firstWeight;
	static GroceryItem gi;
	static double firstBulkTaxRate;
	static double firstPackagedTaxRate;

	@BeforeClass
	public static void classSetUp() throws Exception {
		// create a SelfCheckOut
		firstBA = new BaggingArea();
		firstPC = new PaymentCollector();
		firstPDB = new Database("Database/TestSelfCheckOut.db");
		firstSCO = new SelfCheckOut(firstBA, firstPC, firstPDB);
		// create a packaged item
		try {
			firstUPC = new UPC("717951000842");
		} catch (InvalidUPCException e) {
			fail("Invalid UPC");
		}
		// create a bulk item to play with
		try {
			firstBIC = new BIC("11111");
		} catch (InvalidBICException e) {
			fail("Invalid BIC");
		}

		firstBulkTaxRate = 1;
		firstBulkTaxRate += firstPDB.lookUpItem(firstBIC).getTaxRate();
		firstPackagedTaxRate = 1;
		firstPackagedTaxRate += firstPDB.lookUpItem(firstUPC).getTaxRate();
		firstWeight = 2.61;
		firstUPCWeight = 1.35;
	}

	@AfterClass
	public static void classTearDown() {
		firstSCO = null;
		firstBA = null;
		firstPDB = null;
		firstPC = null;
	}

	@Before
	public void setUp() throws Exception {
		gi = firstSCO.addItem(firstBIC, firstWeight);
	}

	@After
	public void tearDown() {
		firstSCO.resetAll();
	}

	@Test
	public void listingItemsInCart() {
		Enumeration<GroceryItem> egi;

		egi = firstSCO.listItemsInCart();
		assertEquals(gi, egi.nextElement());
		assertFalse(egi.hasMoreElements());
	}

	@Test
	public void calculatingTotalCost() {

		double total, unitPrice = 0;

		total = firstSCO.getTotalCost();
		try {
			unitPrice = firstPDB.lookUpItem(firstBIC).getPrice();
		} catch (SaleDiscountException ignore) {
			fail("invalid sale");
		}
		assertEquals(firstWeight * unitPrice * firstBulkTaxRate, total, 0.0);
	}

	@Test
	public void addingSecondBulkItemThenAnotherPackaged() {
		BIC secondBIC = null;
		BaggingAreaEvent event1;
		BaggingAreaEvent event2;
		double secondWeight = 1.44;
		double secondBulkTaxRate = 1;
		try {
			secondBIC = new BIC("22222");
			// make sure the state returns to 'ADDING' before adding another
			// item
			event1 = new BaggingAreaEvent(0, firstWeight);
			firstSCO.notifyBaggingAreaEvent(firstBA, event1);
			firstSCO.addItem(secondBIC, secondWeight);

			secondBulkTaxRate += firstPDB.lookUpItem(secondBIC).getTaxRate();
			assertEquals(firstSCO.getTotalCost(),
					1.8009 + (1.4256 * secondBulkTaxRate), EPSILON);
			event2 = new BaggingAreaEvent(firstWeight, Math.abs(secondWeight
					- firstWeight));
			firstSCO.notifyBaggingAreaEvent(firstBA, event2);
			firstSCO.addItem(firstUPC);
			assertEquals(firstSCO.getTotalCost(), (3.20 * firstPackagedTaxRate)
					+ 1.8009 + (1.4256 * secondBulkTaxRate), EPSILON);
		} catch (SaleDiscountException e) {
			fail("Invalid Sale");
		} catch (InvalidBICException e) {
			fail("Invalid BIC");
		} catch (IncorrectStateException e) {
			fail("Incorrect State");
		} catch (InvalidProductException e) {
			fail("Invalid Product");
		} catch (InvalidWeightException e) {
			fail("Invalid Weight");
		}

	}

	@Test
	public void addingPackagedProduct() {
		BaggingAreaEvent event1;
		try {
			event1 = new BaggingAreaEvent(0, firstWeight);
			firstSCO.notifyBaggingAreaEvent(firstBA, event1);
			firstSCO.addItem(firstUPC);
			assertEquals(firstSCO.getTotalCost(),
					((firstPackagedTaxRate * 3.2) + 1.8009), 0.0);
		} catch (IncorrectStateException e) {
			fail("Incorrect State");

		} catch (InvalidProductException e) {
			fail("Invalid Product");
		} catch (InvalidWeightException e) {
			fail("Invalid Weight");
		} catch (SaleDiscountException e) {
			fail("Invalid Sale");
		}
	}

	@Test
	public void addingTwoPackagedProductsThenOneBulk() {
		BaggingAreaEvent event1;
		BaggingAreaEvent event2;
		BaggingAreaEvent event3;
		BIC secondBIC;
		double secondWeight = 5.23;
		UPC secondUPC;
		double currWeight;
		double currTotal;
		double secondUPCWeight;
		double secondBulkTaxRate = 1;
		double secondPackagedTaxRate = 1;
		try {
			secondBIC = new BIC("33333");
			secondBulkTaxRate += firstPDB.lookUpItem(secondBIC).getTaxRate();
			secondUPC = new UPC("717951000842");
			secondPackagedTaxRate += firstPDB.lookUpItem(secondUPC)
					.getTaxRate();
			secondUPCWeight = 4;
			event1 = new BaggingAreaEvent(0, firstWeight);
			firstSCO.notifyBaggingAreaEvent(firstBA, event1);
			// need to bag item before being able to add
			assertEquals(firstSCO.getTotalCost(), 1.8009, EPSILON);
			firstSCO.addItem(firstUPC);
			currTotal = 1.8009 + (firstPackagedTaxRate * 3.2);
			assertEquals(firstSCO.getTotalCost(), currTotal, EPSILON);
			currWeight = firstWeight + firstUPCWeight;
			event2 = new BaggingAreaEvent(currWeight, Math.abs(currWeight
					- secondUPCWeight));
			firstSCO.notifyBaggingAreaEvent(firstBA, event2);
			// test if change in weight of bagging area is correct
			assertEquals(event2.getChange(), Math.abs(3.96 - 4), EPSILON);
			assertEquals(firstPDB.lookUpItem(secondUPC).getPrice(), 3.20,
					EPSILON);
			firstSCO.addItem(secondUPC);
			currTotal += secondPackagedTaxRate * 3.2;
			currWeight += secondUPCWeight;
			// test for correct total cost with all 3 items
			assertEquals(firstSCO.getTotalCost(), currTotal, 0.1);
			event3 = new BaggingAreaEvent(currWeight, Math.abs(currWeight
					- secondWeight));
			firstSCO.notifyBaggingAreaEvent(firstBA, event3);
			firstSCO.addItem(secondBIC, secondWeight);
			currTotal += 5.23 * 0.99 * secondBulkTaxRate;
			assertEquals(firstSCO.getTotalCost(), currTotal, 0.1);
		} catch (IncorrectStateException e) {
			fail("Incorrect State");
		} catch (InvalidUPCException e) {
			fail("Invalid UPC");
		} catch (InvalidBICException e) {
			fail("Invalid BIC");
		} catch (InvalidProductException e) {
			fail("Invalid Product");
		} catch (InvalidWeightException e) {
			fail("Invalid Weight");
		} catch (SaleDiscountException e) {
			fail("Invalid Sale");
		}
	}

	@Test
	public void payingForGroceries() throws Exception {
		CheckOutCart coc;
		Enumeration<GroceryItem> egi;
		double unitPrice;
		BaggingAreaEvent event1;

		// first check that coc contains some groceries
		event1 = new BaggingAreaEvent(0, firstWeight);
		firstSCO.notifyBaggingAreaEvent(firstBA, event1);
		coc = firstSCO.payForGroceries();
		assertTrue(coc.listItems().hasMoreElements());

		// now check that the one GroceryItem in coc is the one that we put
		// in by checking the code, weight, and price
		gi = coc.listItems().nextElement();
		assertEquals(firstBIC.toString(), gi.getInfo().getCode().toString());

		double EPSILON = 1e-15; // a really small number
		assertEquals(firstWeight, gi.getWeight(), EPSILON);

		unitPrice = firstPDB.lookUpItem(firstBIC).getPrice();

		assertEquals(firstWeight * unitPrice, gi.getPrice(), 0.0);

		// we would check the actual payment here
		// but since it isn't implemented, we won't

		// current cart should be empty again

		egi = firstSCO.listItemsInCart();
		assertFalse(egi.hasMoreElements());

	}

}
