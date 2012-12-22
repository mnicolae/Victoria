/*
 * Creator: Susan Elliott Sim
 * 
 * Created on January 29, 2008, September 12, 2012
 * Updated on September 30, 2012
 * 
 * This class contains JUnit test cases for BulkProduct.java.
 * 
 */
package ca.utoronto.csc301.SelfCheckOut.App;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BulkProductTest {
	double EPSILON = 1e-15; // a very small number

	BulkProduct firstBulkProduct, differentBulkProduct, BulkProductOnSale;
	BIC firstBIC, differentBIC, BulkProductOnSaleBIC;
	String firstCode, firstDescription, differentCode, differentDescription,
			BulkProductOnSaleCode, BulkProductOnSaleDescription;
	double firstPrice, differentPrice, firstTax, differentTax,
			BulkProductOnSalePrice, BulkProductOnSaleTax,
			BulkProductOnSaleDiscount;

	@Before
	public void setUp() throws Exception {
		/* Instantiate a BulkProduct. */
		firstCode = "11111";
		firstBIC = new BIC(firstCode);
		firstDescription = "Banana";
		firstPrice = 0.69;
		firstTax = 0.10;
		firstBulkProduct = new BulkProduct(firstDescription, firstBIC,
				firstPrice, firstTax);

		/* Instantiate a different BulkProduct. */
		differentCode = "33333";
		differentBIC = new BIC(differentCode);
		differentDescription = "Spinach";
		differentPrice = 0.99;
		differentTax = 0.0;
		differentBulkProduct = new BulkProduct(differentDescription,
				differentBIC, differentPrice, differentTax);

		/* Instantiate a BulkProduct with discount. */
		BulkProductOnSaleCode = "88888";
		BulkProductOnSaleBIC = new BIC(BulkProductOnSaleCode);
		BulkProductOnSaleDescription = "t-bone steak";
		BulkProductOnSalePrice = 9.99;
		BulkProductOnSaleTax = 0.15;
		BulkProductOnSaleDiscount = 0.20;
		BulkProductOnSale = new BulkProduct(BulkProductOnSaleDescription,
				BulkProductOnSaleBIC, BulkProductOnSalePrice,
				BulkProductOnSaleTax, BulkProductOnSaleDiscount);

	}

	@After
	public void tearDown() throws Exception {
		/* Tear down the BulkProduct. */
		firstBulkProduct = null;
		firstBIC = null;
		firstDescription = null;
		firstPrice = 0.0;
		firstTax = 0.0;
	}

	@Test
	public void testGetBIC() {
		// first a test with a correct BIC
		assertEquals(firstBIC, firstBulkProduct.getBIC());
		// now test with a different BIC
		assertFalse(differentBIC == firstBulkProduct.getBIC());
	}

	/*
	 * The test for getCode is the same as getBIC, because it's a facade for the
	 * method.
	 */
	@Test
	public void testGetCode() {
		// first a test with a correct BIC
		assertEquals(firstBIC, firstBulkProduct.getCode());
		// now test with a different BIC
		assertFalse(differentBIC == firstBulkProduct.getCode());

	}

	@Test
	public void testGetPrice() {
		// first a test with a correct price
		assertEquals(firstPrice, firstBulkProduct.getPrice(), EPSILON);
		// now test with a different price
		assertFalse(differentPrice == firstBulkProduct.getPrice());
	}

	@Test
	public void testGetDescription() {
		// first a test with a correct description
		assertEquals(firstDescription, firstBulkProduct.getDescription());
		// now test with a different description
		assertFalse(differentDescription == firstBulkProduct.getDescription());
	}

	@Test
	public void testIsTaxed() {
		// first a test for a BulkProduct incurring tax
		assertTrue(firstBulkProduct.isTaxed());
		// now a test for a BulkProduct not incurring tax
		assertFalse(differentBulkProduct.isTaxed());
	}

	@Test
	public void testGetTaxRate() {
		// first a test with a correct tax rate
		assertEquals(firstTax, firstBulkProduct.getTaxRate(), EPSILON);
		// now a test with a different tax rate
		assertFalse(differentTax == firstBulkProduct.getTaxRate());
	}

	@Test
	public void testIsOnSale() {
		// first a test with BulkProduct on sale
		assertTrue(BulkProductOnSale.isOnSale());
		// now a test for BulkProduct not on sale
		assertFalse(firstBulkProduct.isOnSale());
	}

	@Test
	public void testGetDiscount() {
		// first a test with BulkProduct on sale
		assertEquals(BulkProductOnSale.getDiscount(),
				BulkProductOnSaleDiscount, EPSILON);
		// now a test for BulkProduct not on sale
		assertFalse(BulkProductOnSaleDiscount == differentBulkProduct
				.getDiscount());
	}
	
	@Test
	/**
	 * Test the getter and setter methods for flat rate discount.
	 */
	public void testSetDiscount(){
		//set the flat rate discount to $20
		BulkProductOnSale.setFlatRateDiscount(20);
		//assert flat rate discount is $20
		assertEquals(20.0, BulkProductOnSale.getFlatRateDiscount(), EPSILON);
	}

}
