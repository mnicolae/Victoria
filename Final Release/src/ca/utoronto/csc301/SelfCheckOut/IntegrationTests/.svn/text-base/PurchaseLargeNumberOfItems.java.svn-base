package ca.utoronto.csc301.SelfCheckOut.IntegrationTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.utoronto.csc301.SelfCheckOut.App.BIC;

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

public class PurchaseLargeNumberOfItems {
	static SelfCheckOut firstSCO;
	static BaggingArea firstBA;
	static Database firstPDB;
	static PaymentCollector firstPC;
	static double EPSILON = 1e-15;
	static BIC firstBIC, secondBIC, thirdBIC, fourthBIC, fifthBIC;
	static UPC firstUPC, secondUPC, thirdUPC, fourthUPC;
	static double firstUPCWeight, secondUPCWeight, thirdUPCWeight,
			fourthUPCWeight;
	static double firstWeight, secondWeight, thirdWeight, fourthWeight,
			fifthWeight;
	static GroceryItem gi;
	static double firstBulkTaxRate = 1, secondBulkTaxRate = 1,
			thirdBulkTaxRate = 1, fourthBulkTaxRate = 1, fifthBulkTaxRate = 1;
	static double firstPackagedTaxRate = 1, secondPackagedTaxRate = 1,
			thirdPackagedTaxRate = 1, fourthPackagedTaxRate = 1;

	@BeforeClass
	public static void classSetUp() throws Exception {
		// create a SelfCheckOut
		firstBA = new BaggingArea();
		firstPC = new PaymentCollector();
		firstPDB = new Database("Database/TestSelfCheckOut.db");
		firstSCO = new SelfCheckOut(firstBA, firstPC, firstPDB);
		// create a packaged item
		try {
			firstUPC = new UPC("786936224306");
			secondUPC = new UPC("717951000842");
			thirdUPC = new UPC("024543213710");
			fourthUPC = new UPC("085392132225");
		} catch (InvalidUPCException e) {
			fail("Invalid UPC");
		}
		// create a bulk item
		try {
			firstBIC = new BIC("11111");
			secondBIC = new BIC("22222");
			thirdBIC = new BIC("33333");
			fourthBIC = new BIC("44444");
			fifthBIC = new BIC("55555");
		} catch (InvalidBICException e) {
			fail("Invalid BIC");
		}

		firstBulkTaxRate = 1;
		secondBulkTaxRate = 1;
		thirdBulkTaxRate = 1;
		fourthBulkTaxRate = 1;
		fifthBulkTaxRate = 1;
		firstBulkTaxRate += firstPDB.lookUpItem(firstBIC).getTaxRate();
		secondBulkTaxRate += firstPDB.lookUpItem(secondBIC).getTaxRate();
		thirdBulkTaxRate += firstPDB.lookUpItem(thirdBIC).getTaxRate();
		fourthBulkTaxRate += firstPDB.lookUpItem(fourthBIC).getTaxRate();
		fifthBulkTaxRate += firstPDB.lookUpItem(fifthBIC).getTaxRate();

		firstPackagedTaxRate = 1;
		secondPackagedTaxRate = 1;
		thirdPackagedTaxRate = 1;
		fourthPackagedTaxRate = 1;
		firstPackagedTaxRate += firstPDB.lookUpItem(firstUPC).getTaxRate();
		secondPackagedTaxRate += firstPDB.lookUpItem(secondUPC).getTaxRate();
		thirdPackagedTaxRate += firstPDB.lookUpItem(thirdUPC).getTaxRate();
		fourthPackagedTaxRate += firstPDB.lookUpItem(fourthUPC).getTaxRate();

		firstWeight = 2.61;
		secondWeight = 112.39;
		thirdWeight = 225.21;
		fourthWeight = 400.80;
		fifthWeight = 45.50;

		firstUPCWeight = 1.35;
		secondUPCWeight = 4;
		thirdUPCWeight = 2.2;
		fourthUPCWeight = 0.8;
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
		// gi = firstSCO.addItem(firstBIC, firstWeight);
	}

	@After
	public void tearDown() {
		firstSCO.resetAll();
	}

	@Test
	public void PurchasingLargeNumberOfproducts() throws InvalidUPCException {
		double temp, currentWeight;
		BaggingAreaEvent event1;
		BaggingAreaEvent event2;
		BaggingAreaEvent event3;
		BaggingAreaEvent event4;
		BaggingAreaEvent event5;
		BaggingAreaEvent event6;
		BaggingAreaEvent event7;
		BaggingAreaEvent event8;
		try {
			firstSCO.addItem(firstBIC, firstWeight);

			event1 = new BaggingAreaEvent(0, firstWeight);
			firstSCO.notifyBaggingAreaEvent(firstBA, event1);
			firstSCO.addItem(secondBIC, secondWeight);
			assertEquals(firstSCO.getTotalCost(),
					1.8009 + (111.2661 * secondBulkTaxRate), 0.1);
			currentWeight = firstWeight + secondWeight;

			event2 = new BaggingAreaEvent(currentWeight, Math.abs(currentWeight
					- secondWeight));
			firstSCO.notifyBaggingAreaEvent(firstBA, event2);
			firstSCO.addItem(thirdBIC, thirdWeight);
			assertEquals(firstSCO.getTotalCost(), (222.9579 * thirdBulkTaxRate)
					+ 1.8009 + (111.2661 * secondBulkTaxRate), 0.1);
			temp = (222.9579 * thirdBulkTaxRate) + 1.8009
					+ (111.2661 * secondBulkTaxRate);
			currentWeight += thirdWeight;

			event3 = new BaggingAreaEvent(currentWeight, Math.abs(currentWeight
					- thirdWeight));
			firstSCO.notifyBaggingAreaEvent(firstBA, event3);
			firstSCO.addItem(fourthBIC, fourthWeight);
			assertEquals(firstSCO.getTotalCost(),
					(1118.232 * fourthBulkTaxRate) + temp, 0.1);
			temp = (1118.232 * fourthBulkTaxRate) + temp;
			currentWeight += fourthWeight;

			event4 = new BaggingAreaEvent(currentWeight, Math.abs(currentWeight
					- fourthWeight));
			firstSCO.notifyBaggingAreaEvent(firstBA, event4);
			firstSCO.addItem(fifthBIC, fifthWeight);
			assertEquals(firstSCO.getTotalCost(), (58.695 * fifthBulkTaxRate)
					+ temp, 0.1);
			temp = (58.695 * fifthBulkTaxRate) + temp;
			currentWeight += fifthWeight;

			// since we do not have very large database we will add same product
			// number of times
			event5 = new BaggingAreaEvent(currentWeight, Math.abs(currentWeight
					- fifthWeight));
			for (int i = 0; i < 100; i++) {
				firstSCO.notifyBaggingAreaEvent(firstBA, event5);
				firstSCO.addItem(firstUPC);
			}
			assertEquals(firstSCO.getTotalCost(), temp
					+ (352 * firstPackagedTaxRate), 0.1);
			temp = temp + (352 * firstPackagedTaxRate);
			currentWeight += (firstUPCWeight * 100);

			event6 = new BaggingAreaEvent(currentWeight, Math.abs(currentWeight
					- (firstUPCWeight * 100)));
			for (int i = 0; i < 200; i++) {
				firstSCO.notifyBaggingAreaEvent(firstBA, event6);
				firstSCO.addItem(secondUPC);
			}
			assertEquals(firstSCO.getTotalCost(), temp
					+ (640 * secondPackagedTaxRate), 0.1);
			temp = temp + (640 * secondPackagedTaxRate);
			currentWeight += (secondUPCWeight * 200);

			event7 = new BaggingAreaEvent(currentWeight, Math.abs(currentWeight
					- (secondUPCWeight * 200)));
			for (int i = 0; i < 500; i++) {
				firstSCO.notifyBaggingAreaEvent(firstBA, event7);
				firstSCO.addItem(thirdUPC);
			}
			assertEquals(firstSCO.getTotalCost(), temp
					+ (2000 * thirdPackagedTaxRate), 0.1);
			temp = temp + (2000 * thirdPackagedTaxRate);
			currentWeight += (thirdUPCWeight * 500);

			event8 = new BaggingAreaEvent(currentWeight, Math.abs(currentWeight
					- (thirdUPCWeight * 500)));
			for (int i = 0; i < 500; i++) {
				firstSCO.notifyBaggingAreaEvent(firstBA, event8);
				firstSCO.addItem(fourthUPC);
			}
			assertEquals(firstSCO.getTotalCost(), temp
					+ (1750 * fourthPackagedTaxRate), 0.1);
			temp = temp + (1750 * fourthPackagedTaxRate);
			currentWeight += (fourthUPCWeight * 500);

			assertEquals(firstSCO.getSubTotal(), 6254.9518, 0.01); // check
			// sub-total
			// [price
			// without
			// tax]
			assertEquals(firstSCO.getTotalTax(), 840.1193, 0.001); // check
			// total tax
			assertEquals(firstSCO.getTotalCost(), temp, 0.01); // check total
			// price

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
}
