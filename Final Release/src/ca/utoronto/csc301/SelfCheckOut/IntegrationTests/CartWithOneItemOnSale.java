package ca.utoronto.csc301.SelfCheckOut.IntegrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.utoronto.csc301.SelfCheckOut.App.BIC;
import ca.utoronto.csc301.SelfCheckOut.App.Database;
import ca.utoronto.csc301.SelfCheckOut.App.SelfCheckOut;
import ca.utoronto.csc301.SelfCheckOut.App.UPC;
import ca.utoronto.csc301.SelfCheckOut.Devices.BaggingArea;
import ca.utoronto.csc301.SelfCheckOut.Devices.PaymentCollector;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.IncorrectStateException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidProductException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidWeightException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountNegativeException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountTooLargeException;

/**
 * Integration tests for a self check out cart that contains exactly one item
 * and that item is on sale.
 * 
 * @author Alex Rodrigues
 * 
 */
public class CartWithOneItemOnSale {

	static SelfCheckOut selfCheckOut;
	static BaggingArea baggingArea;
	static Database productDB;
	static PaymentCollector paymentCollector;
	static double EPSILON = 1e-13;

	/*
	 * Packaged Product that is on sale with a percent off.
	 */
	static UPC upcCodeOnSaleByPercent;
	static double ppOnSaleByPercentTaxRate;
	static double ppOnSaleByPercentTrueCost = 3.33;
	/*
	 * Bulk Product that is on sale with a percent off.
	 */
	static BIC bicCodeOnSaleByPercent;
	static double bpOnSaleByPercentTaxRate;
	static double bpOnSaleByPercentTrueCost = 9.99;
	/*
	 * Packaged Product that is on sale with a flat rate off.
	 */
	static UPC upcCodeOnSaleByFlatRate;
	static double ppOnSaleByFlatRateTaxRate;
	static double ppOnSaleByFlatRateTrueCost = 6.00;
	/*
	 * Bulk Product that is on sale with a flat rate off.
	 */
	static BIC bicCodeOnSaleByFlatRate;
	static double bpOnSaleByFlatRateTaxRate;
	static double bpOnSaleByFlatRateTrueCost = 10;

	/*
	 * Packaged Product that has both a flat rate and a percent off.
	 */
	static UPC upcCodeOnSaleByFlatAndPercent;
	static double ppOnSaleByFlatAndPercentTaxRate;
	static double ppOnSaleByFlatAndPercentTrueCost = 10;
	/*
	 * Bulk Product that has both a flat rate and a percent off.
	 */
	static BIC bicCodeOnSaleByFlatAndPercent;
	static double bpOnSaleByFlatAndPercentTaxRate;
	static double bpOnSaleByFlatAndPercentTrueCost = 4.50;
	/*
	 * Packaged Product that has 100 percent off.
	 */
	static UPC upcCodeOnSale100PercentOff;
	static double ppOnSale100PercentOffTrueCost = 99.99;
	/*
	 * Bulk Product that has both a flat rate and a percent off.
	 */
	static BIC bicCodeOnSale100PercentOff;
	static double bpOnSale100PercentOffTrueCost = 10;
	/*
	 * Packaged Product that has 100 percent off.
	 */
	static UPC upcCodeOnSaleFullFlatRateOff;
	static double ppOnSaleFullFlateRateOffTrueCost = 2.0;
	/*
	 * Bulk Product that has both a flat rate and a percent off.
	 */
	static BIC bicCodeOnSaleFullFlatRateOff;
	static double bpOnSaleFullFlateRateOffTrueCost = 2.0;
	/*
	 * Packaged Product that has no real discount ie. percent and flat rate off
	 * are 0 the sale exists
	 */
	static UPC upcCodeOnSaleNoDiscount;
	static double ppOnSaleNoDiscountTaxRate;
	static double ppOnSaleNoDiscountTrueCost = 3.52;
	/*
	 * Bulk Product that has no real discount ie. percent and flat rate off are
	 * 0 yet the sale exists
	 */
	static BIC bicCodeOnSaleNoDiscount;
	static double bpOnSaleNoDiscountTaxRate;
	static double bpOnSaleNoDiscountTrueCost = 0.69;

	static BIC saleDiscountTooLarge;
	static BIC saleDiscountNegative;

	double productWeight = 10;
	double percentOff = 0.20;
	double flatRateOff = 2.00;

	@BeforeClass
	public static void classSetUp() throws Exception {

		productDB = new Database("Database/TestSelfCheckOut.db");
		paymentCollector = new PaymentCollector();
		baggingArea = new BaggingArea();
		selfCheckOut = new SelfCheckOut(baggingArea, paymentCollector,
				productDB);

		upcCodeOnSaleByPercent = new UPC("780166035718");
		ppOnSaleByPercentTaxRate = productDB.lookUpItem(upcCodeOnSaleByPercent)
				.getTaxRate();

		bicCodeOnSaleByPercent = new BIC("88888");
		bpOnSaleByPercentTaxRate = productDB.lookUpItem(bicCodeOnSaleByPercent)
				.getTaxRate();

		upcCodeOnSaleNoDiscount = new UPC("786936224306");
		ppOnSaleNoDiscountTaxRate = productDB.lookUpItem(
				upcCodeOnSaleNoDiscount).getTaxRate();

		bicCodeOnSaleNoDiscount = new BIC("11111");
		bpOnSaleNoDiscountTaxRate = productDB.lookUpItem(
				bicCodeOnSaleNoDiscount).getTaxRate();

		upcCodeOnSaleByFlatRate = new UPC("796030114977");
		ppOnSaleByFlatRateTaxRate = productDB.lookUpItem(
				upcCodeOnSaleByFlatRate).getTaxRate();

		bicCodeOnSaleByFlatRate = new BIC("99999");
		bpOnSaleByFlatRateTaxRate = productDB.lookUpItem(
				bicCodeOnSaleByFlatRate).getTaxRate();

		upcCodeOnSaleByFlatAndPercent = new UPC("712345678904");
		ppOnSaleByFlatAndPercentTaxRate = productDB.lookUpItem(
				upcCodeOnSaleByFlatAndPercent).getTaxRate();

		bicCodeOnSaleByFlatAndPercent = new BIC("12345");
		bpOnSaleByFlatAndPercentTaxRate = productDB.lookUpItem(
				bicCodeOnSaleByFlatAndPercent).getTaxRate();

		upcCodeOnSale100PercentOff = new UPC("086637677174");
		bicCodeOnSale100PercentOff = new BIC("12121");
		upcCodeOnSaleFullFlatRateOff = new UPC("012345678905");
		bicCodeOnSaleFullFlatRateOff = new BIC("21212");

		saleDiscountTooLarge = new BIC("06660");
		saleDiscountNegative = new BIC("06661");

	}

	@AfterClass
	public static void classTearDown() {
		productDB = null;
		paymentCollector = null;
		baggingArea = null;
		selfCheckOut = null;
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() {
		selfCheckOut.resetAll();
	}

	/**
	 * Test payment for one item.
	 * 
	 * @throws Exception
	 */
	@Test
	public void payForOneItem() throws Exception {
		selfCheckOut.payForGroceries(false, "", 250, 1);
		equals(paymentCollector.isFinishedPaying());
	}

	/**
	 * Test an empty self check out cart with a single bulk product item that is
	 * on sale. The type of sale is percent off.
	 **/
	@Test
	public void addingBulkProductOnSaleByPercent() {

		try {
			// Add the bulk product that is on sale by percent
			selfCheckOut.addItem(bicCodeOnSaleByPercent, productWeight);
			// test the total discount
			assertEquals(
					bpOnSaleByPercentTrueCost * productWeight * percentOff,
					selfCheckOut.getTotalDiscount(), EPSILON);
			// test sub total
			assertEquals(bpOnSaleByPercentTrueCost * productWeight
					* (1 - percentOff), selfCheckOut.getSubTotal(), EPSILON);
			// test the total cost
			assertEquals(bpOnSaleByPercentTrueCost * productWeight
					* (1 - percentOff) * (bpOnSaleByPercentTaxRate + 1),
					selfCheckOut.getTotalCost(), EPSILON);

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

	/**
	 * Test an empty self check out cart with a single packaged product item
	 * that is on sale. The type of sale is percent off.
	 */

	@Test
	public void addingPackagedProductOnSaleByPercent() {

		try {
			// Add the packaged product that is on sale by percent
			selfCheckOut.addItem(upcCodeOnSaleByPercent);
			// test the total discount
			assertEquals(ppOnSaleByPercentTrueCost * percentOff,
					selfCheckOut.getTotalDiscount(), EPSILON);
			// test the sub total
			assertEquals(ppOnSaleByPercentTrueCost * (1 - percentOff),
					selfCheckOut.getSubTotal(), EPSILON);
			// test the total cost
			assertEquals(ppOnSaleByPercentTrueCost * (1 - percentOff)
					* (ppOnSaleByPercentTaxRate + 1),
					selfCheckOut.getTotalCost(), EPSILON);
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

	/**
	 * Test an empty self check out cart with a single bulk product item that is
	 * on sale. The type of sale is flat rate off.
	 **/
	@Test
	public void addingBulkProductOnSaleByFlatRate() {

		try {
			// Add the bulk product that is on sale by flat rate
			selfCheckOut.addItem(bicCodeOnSaleByFlatRate, productWeight);
			// test the total discount
			assertEquals(flatRateOff * productWeight,
					selfCheckOut.getTotalDiscount(), EPSILON);
			// test sub total
			assertEquals((bpOnSaleByFlatRateTrueCost - flatRateOff)
					* productWeight, selfCheckOut.getSubTotal(), EPSILON);
			// test the total cost
			assertEquals((bpOnSaleByFlatRateTrueCost - flatRateOff)
					* productWeight * (bpOnSaleByFlatRateTaxRate + 1),
					selfCheckOut.getTotalCost(), EPSILON);

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

	/**
	 * Test an empty self check out cart with a single packaged product item
	 * that is on sale. The type of sale is flate rate off.
	 */

	@Test
	public void addingPackagedProductOnSaleByFlatRate() {

		try {
			// Add the packaged product that is on sale by flat rate
			selfCheckOut.addItem(upcCodeOnSaleByFlatRate);
			// test the total discount
			assertEquals(flatRateOff, selfCheckOut.getTotalDiscount(), EPSILON);
			// test the sub total
			assertEquals(ppOnSaleByFlatRateTrueCost - flatRateOff,
					selfCheckOut.getSubTotal(), EPSILON);
			// test the total cost
			assertEquals((ppOnSaleByFlatRateTrueCost - flatRateOff)
					* (ppOnSaleByFlatRateTaxRate + 1),
					selfCheckOut.getTotalCost(), EPSILON);
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

	/**
	 * Test an empty self check out cart with a single packaged product item
	 * that is on sale. The type of sale is both flat rate and percent rate.
	 */

	@Test
	public void addingPackagedProductOnSaleByFlatAndPercent() {

		try {
			// Add the packaged product that is on sale by flat rate and percent
			selfCheckOut.addItem(upcCodeOnSaleByFlatAndPercent);
			// test the total discount
			assertEquals((ppOnSaleByFlatAndPercentTrueCost - flatRateOff)
					* percentOff + flatRateOff,
					selfCheckOut.getTotalDiscount(), EPSILON);
			// test the sub total
			assertEquals((ppOnSaleByFlatAndPercentTrueCost - flatRateOff)
					* (1 - percentOff), selfCheckOut.getSubTotal(), EPSILON);
			// test the total cost
			assertEquals((ppOnSaleByFlatAndPercentTrueCost - flatRateOff)
					* (1 - percentOff) * (ppOnSaleByFlatAndPercentTaxRate + 1),
					selfCheckOut.getTotalCost(), EPSILON);
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

	/**
	 * Test an empty self check out cart with a single bulk product item that is
	 * on sale. The type of sale is both flat rate and percent rate.
	 */

	@Test
	public void addingBulkProductOnSaleByFlatAndPercent() {

		try {
			// Add the bulk product that is on sale by flat rate and percent
			selfCheckOut.addItem(bicCodeOnSaleByFlatAndPercent, productWeight);
			// test the total discount
			assertEquals((bpOnSaleByFlatAndPercentTrueCost - flatRateOff)
					* percentOff * productWeight + flatRateOff * productWeight,
					selfCheckOut.getTotalDiscount(), EPSILON);
			// test the sub total
			assertEquals((bpOnSaleByFlatAndPercentTrueCost - flatRateOff)
					* productWeight * (1 - percentOff),
					selfCheckOut.getSubTotal(), EPSILON);
			// test the total cost
			assertEquals((bpOnSaleByFlatAndPercentTrueCost - flatRateOff)
					* productWeight * (1 - percentOff)
					* (bpOnSaleByFlatAndPercentTaxRate + 1),
					selfCheckOut.getTotalCost(), EPSILON);
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

	/**
	 * Test an empty self check out cart with a single bulk product item with
	 * one hundred percent off. This is an edge case.
	 */
	@Test
	public void addingBulkProductOneHundredPercentOff() {

		try {
			// Add the bulk product that is on sale by 100 percent off
			selfCheckOut.addItem(bicCodeOnSale100PercentOff, productWeight);
			// test the total discount
			assertEquals(bpOnSale100PercentOffTrueCost * productWeight,
					selfCheckOut.getTotalDiscount(), EPSILON);
			// test the sub total
			assertEquals(0, selfCheckOut.getSubTotal(), EPSILON);
			// test the total cost
			assertEquals(0, selfCheckOut.getTotalCost(), EPSILON);
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

	/**
	 * Test an empty self check out cart with a single packaged product item
	 * with one hundred percent off. This is an edge case.
	 */
	@Test
	public void addingPackagedProductOneHundredPercentOff() {

		try {
			// Add the packaged product that is on sale by 100 percent off
			selfCheckOut.addItem(upcCodeOnSale100PercentOff);
			// test the total discount
			assertEquals(ppOnSale100PercentOffTrueCost,
					selfCheckOut.getTotalDiscount(), EPSILON);
			// test the sub total
			assertEquals(0, selfCheckOut.getSubTotal(), EPSILON);
			// test the total cost
			assertEquals(0, selfCheckOut.getTotalCost(), EPSILON);
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

	/**
	 * Test an empty self check out cart with a single packaged product item
	 * with a flat rate off equal to the price of the item. This is an edge
	 * case.
	 */
	@Test
	public void addingPackagedProductFullFlatRateOff() {

		try {
			// Add the packaged product that is on sale with a full flat rate
			// off
			selfCheckOut.addItem(upcCodeOnSaleFullFlatRateOff);
			// test the total discount
			assertEquals(ppOnSaleFullFlateRateOffTrueCost,
					selfCheckOut.getTotalDiscount(), EPSILON);
			// test the sub total
			assertEquals(0, selfCheckOut.getSubTotal(), EPSILON);
			// test the total cost
			assertEquals(0, selfCheckOut.getTotalCost(), EPSILON);
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

	/**
	 * Test an empty self check out cart with a single packaged product item
	 * with with a flat rate off equal to the price of the item . This is an
	 * edge case.
	 */
	@Test
	public void addingBulkProductFullFlatRateOff() {

		try {
			// Add the bulk product that is on sale by flat rate and percent
			selfCheckOut.addItem(bicCodeOnSaleFullFlatRateOff, productWeight);
			// test the total discount
			assertEquals(bpOnSaleFullFlateRateOffTrueCost * productWeight,
					selfCheckOut.getTotalDiscount(), EPSILON);
			// test the sub total
			assertEquals(0, selfCheckOut.getSubTotal(), EPSILON);
			// test the total cost
			assertEquals(0, selfCheckOut.getTotalCost(), EPSILON);
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

	/**
	 * Test an empty self check out cart with a single packaged product item
	 * that is on sale but with no real discount.
	 */
	@Test
	public void addingPackagedProductNoRealDiscount() {

		try {
			// Add the packaged product that is on sale with no real discount
			selfCheckOut.addItem(upcCodeOnSaleNoDiscount);
			// test the total discount
			assertEquals(0, selfCheckOut.getTotalDiscount(), EPSILON);
			// test the sub total
			assertEquals(ppOnSaleNoDiscountTrueCost,
					selfCheckOut.getSubTotal(), EPSILON);
			// test the total cost
			assertEquals(ppOnSaleNoDiscountTrueCost
					* (1 + ppOnSaleNoDiscountTaxRate),
					selfCheckOut.getTotalCost(), EPSILON);
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

	/**
	 * Test an empty self check out cart with a single bulk product item that is
	 * on sale but with no real discount.
	 */
	@Test
	public void addingBulkProductNoRealDiscount() {

		try {
			// Add the bulk product that has no real discount
			selfCheckOut.addItem(bicCodeOnSaleNoDiscount, productWeight);
			// test the total
			assertEquals(0, selfCheckOut.getTotalDiscount(), EPSILON);
			// test the sub total
			assertEquals(bpOnSaleNoDiscountTrueCost * productWeight,
					selfCheckOut.getSubTotal(), EPSILON);
			// test the total cost
			assertEquals(bpOnSaleNoDiscountTrueCost * productWeight
					* (1 + bpOnSaleNoDiscountTaxRate),
					selfCheckOut.getTotalCost(), EPSILON);
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

	/**
	 * Test an empty self check out cart with an item whose discount is too
	 * large. This is a negative case.
	 */
	@Test(expected = SaleDiscountTooLargeException.class)
	public void addingItemDiscountToLarge() throws SaleDiscountException {
		try {
			// add the item on sale which results in a discount too large
			selfCheckOut.addItem(saleDiscountTooLarge, productWeight);
		} catch (IncorrectStateException e) {
			fail("Invalid state");
		} catch (InvalidProductException e) {
			fail("invalid product");
		} catch (InvalidWeightException e) {
			fail("invalid weight");
		}
	}

	/**
	 * Test an empty self check out cart with an item that whose discount is
	 * negative.
	 */
	@Test(expected = SaleDiscountNegativeException.class)
	public void addingItemDiscountNegative() throws SaleDiscountException {
		try {
			// add the item on sale that results in a negative discount
			selfCheckOut.addItem(saleDiscountNegative, productWeight);
		} catch (SaleDiscountTooLargeException e) {
			fail("Sale to large");
		} catch (IncorrectStateException e) {
			fail("Invalid state");
		} catch (InvalidProductException e) {
			fail("invalid product");
		} catch (InvalidWeightException e) {
			fail("invalid weight");
		}
	}

}
