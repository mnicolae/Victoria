/*
 * Creator: Kevin Leung
 * 
 * Updated on October 3, 2012
 * This class contains JUnit test cases for PackagedProduct.java.
 * 
 */

package ca.utoronto.csc301.SelfCheckOut.App;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PackagedProductTest {
	double EPSILON = 1e-15; // a very small number

	String firstCode, firstDescription, differentCode, differentDescription,
			PackagedProductOnSaleCode, PackagedProductOnSaleDescription;
	double firstPrice, differentPrice, firstWeight, differentWeight, firstTax,
			differentTax, PackagedProductOnSalePrice, PackagedProductOnSaleTax,
			PackagedProductOnSaleDiscount, PackagedProductOnSaleWeight;
	UPC differentUPC, firstUPC, PackagedProductOnSaleUPC;
	PackagedProduct differentInfo, firstInfo, PackagedProductOnSale;

	@Before
	public void setUp() throws Exception {
		/* Instantiate a PackagedProduct. */
		firstCode = "024543213710";
		firstUPC = new UPC(firstCode);
		firstDescription = "Ice Cream";
		firstPrice = 4.00;
		firstWeight = 2.2;
		firstTax = 0.13;
		firstInfo = new PackagedProduct(firstDescription, firstUPC, firstPrice,
				firstWeight, firstTax);

		/* Instantiate a different PackagedProduct. */
		differentCode = "786936224306";
		differentUPC = new UPC(differentCode);
		differentDescription = "Kellogg Cereal";
		differentPrice = 3.52;
		differentWeight = 1.35;
		differentTax = 0.00;
		differentInfo = new PackagedProduct(differentDescription, differentUPC,
				differentPrice, differentWeight);

		/* Instantiate a PackagedProduct with discount. */
		PackagedProductOnSaleCode = "780166035718";
		PackagedProductOnSaleUPC = new UPC(PackagedProductOnSaleCode);
		PackagedProductOnSaleDescription = "Wired";
		PackagedProductOnSalePrice = 3.33;
		PackagedProductOnSaleWeight = 0.6;
		PackagedProductOnSaleTax = 0.13;
		PackagedProductOnSaleDiscount = 0.20;
		PackagedProductOnSale = new PackagedProduct(
				PackagedProductOnSaleDescription, PackagedProductOnSaleUPC,
				PackagedProductOnSalePrice, PackagedProductOnSaleWeight,
				PackagedProductOnSaleTax, PackagedProductOnSaleDiscount);
	}

	@After
	public void tearDown() throws Exception {
		/* Tear down the PackagedProduct. */
		differentCode = null;
		differentUPC = null;
		differentDescription = null;
		differentPrice = 0.0;
		differentWeight = 0.0;
		differentInfo = null;
	}

	@Test
	public void testGetUPC() {
		// test with a correct UPC
		assertEquals(firstUPC, firstInfo.getUPC());
		// now test with a different UPC
		assertFalse(differentUPC == firstInfo.getUPC());
	}

	/*
	 * The test for getCode is the same as getUPC, because it's a facade for the
	 * method.
	 */
	@Test
	public void testGetCode() {
		// test with a correct UPC
		assertEquals(firstUPC, firstInfo.getCode());
		// test with a different UPC
		assertFalse(differentUPC == firstInfo.getCode());
	}

	@Test
	public void testGetPrice() {
		// first a test with a correct Price
		assertEquals(firstPrice, firstInfo.getPrice(), EPSILON);
		// now test with a different Price
		assertFalse(differentPrice == firstInfo.getPrice());
	}

	@Test
	public void testGetWeight() {
		// first a test with a correct weight
		assertEquals(firstWeight, firstInfo.getWeight(), EPSILON);
		// now test with a different weight
		assertFalse(firstWeight == differentInfo.getWeight());
	}

	@Test
	public void testGetDescription() {
		// first a test with a correct Description
		assertEquals(firstDescription, firstInfo.getDescription());
		// now test with a different Price
		assertFalse(differentDescription == firstInfo.getDescription());
	}

	@Test
	public void testIsTaxed() {
		// first a PackagedProduct incurring Tax.
		assertTrue(firstInfo.isTaxed());
		// now a test for a PackagedProduct not incurring Tax.
		assertFalse(differentInfo.isTaxed());
	}

	@Test
	public void testGetTaxrate() {
		// first a test with a correct Tax rate.
		assertEquals(firstTax, firstInfo.getTaxRate(), EPSILON);
		// now a test with a different Tax rate.
		assertFalse(differentTax == firstInfo.getTaxRate());
	}

	@Test
	public void testIsOnSale() {
		// first a test with BulkProduct on sale
		assertTrue(PackagedProductOnSale.isOnSale());
		// now a test for PackagedProduct not on sale
		assertFalse(firstInfo.isOnSale());
	}

	@Test
	public void testGetDiscount() {
		// first a test with BulkProduct on sale
		assertEquals(PackagedProductOnSale.getDiscount(),
				PackagedProductOnSaleDiscount, EPSILON);
		// now a test for PackagedProduct not on sale
		assertFalse(PackagedProductOnSaleDiscount == firstInfo.getDiscount());
	}
	
	/**
	 * Test the getter and setter methods for flat rate discount.
	 */
	public void testSetDiscount(){
		//set the flat rate discount to $20
		PackagedProductOnSale.setFlatRateDiscount(20);
		//assert flat rate discount is $20
		assertEquals(20.0, PackagedProductOnSale.getFlatRateDiscount(), EPSILON);
	}

}
