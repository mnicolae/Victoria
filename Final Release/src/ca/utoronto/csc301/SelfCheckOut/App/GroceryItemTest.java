/* Creator: Mihai Nicolae
 * 
 * Created on September 23, 2012
 * Updated on September 23, 2012
 * 
 * This class contains JUnit test cases for GroceryItem.java.
 * 
 */
package ca.utoronto.csc301.SelfCheckOut.App;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidBICException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidWeightException;

public class GroceryItemTest {

	double EPSILON = 1e-15; // a really small number

	double unitPrice, firstPrice, firstWeight;
	double differentPrice, differentWeight;
	String firstDescription, firstCode, differentDescription, differentCode;
	BIC firstBIC;
	UPC differentUPC;

	ProductInfo firstInfo, differentInfo;
	GroceryItem firstGroceryItem, differentGroceryItem;

	@Before
	public void setUp() throws Exception {
		/* Instantiate a BulkProduct. */
		firstCode = "11111";
		firstBIC = new BIC(firstCode);
		firstDescription = "Banana";
		unitPrice = 0.69;
		firstInfo = new BulkProduct(firstDescription, firstBIC, unitPrice);

		/* Instantiate the related GroceryItem. */
		firstWeight = 10;
		firstPrice = unitPrice * firstWeight;
		firstGroceryItem = new GroceryItem(firstInfo, firstPrice, firstWeight);

		/* Instantiate a PackagedProduct. */
		differentCode = "786936224306";
		differentUPC = new UPC(differentCode);
		differentDescription = "Kellogg Cereal";
		differentPrice = 3.52;
		differentWeight = 1.35;
		differentInfo = new PackagedProduct(differentDescription, differentUPC,
				differentPrice, differentWeight);

		/* Instantiate the related GroceryItem. */
		differentGroceryItem = new GroceryItem(differentInfo, differentPrice,
				differentWeight);
	}

	@After
	public void tearDown() throws Exception {
		/* Tear down the BulkProduct. */
		firstCode = null;
		firstBIC = null;
		firstDescription = null;
		unitPrice = 0.0;
		firstInfo = null;

		/* Tear down the related GroceryItem. */
		firstWeight = 0.0;
		firstPrice = 0.0;
		firstGroceryItem = null;

		/* Tear down the PackagedProduct. */
		differentCode = null;
		differentUPC = null;
		differentDescription = null;
		differentPrice = 0.0;
		differentWeight = 0.0;
		differentInfo = null;

		/* Tear down the related GroceryItem. */
		differentGroceryItem = null;
	}

	/**
	 * Test the constructor with a negative weight.
	 * 
	 * @throws InvalidWeightException
	 * @throws InvalidBICException
	 */
	@Test(expected = InvalidWeightException.class)
	public void constructWithNegativeWeight() throws InvalidWeightException,
			InvalidBICException {
		double NewWeight = -10; // negative weight!
		GroceryItem NewGroceryItem = new GroceryItem(firstInfo, firstPrice,
				NewWeight);
	}

	@Test
	public void testGetPrice() {
		// first a test with a correct price
		assertEquals(firstPrice, firstGroceryItem.getPrice(), EPSILON);
		// now test with a different price
		assertFalse(differentPrice == firstGroceryItem.getPrice());
	}

	@Test
	public void testGetWeight() {
		// first a test with a correct weight
		assertEquals(firstWeight, firstGroceryItem.getWeight(), EPSILON);
		// now test with a different weight
		assertFalse(differentWeight == firstGroceryItem.getWeight());
	}

	@Test
	public void testGetInfo() {
		// first a test with the correct ProductInfo
		assertEquals(firstInfo, firstGroceryItem.getInfo());
		// now test with a different ProductInfo
		assertNotSame(firstInfo, differentGroceryItem.getInfo());
	}
}
