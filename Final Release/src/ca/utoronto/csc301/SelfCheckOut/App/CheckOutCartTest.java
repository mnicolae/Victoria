/*
 * Creator: Nisargkumar Patel
 * 
 * Created on Monday 24, September 2012
 * 
 * This class contains JUnit test cases for CheckoutCart.java.
 * 
 */
package ca.utoronto.csc301.SelfCheckOut.App;

import java.util.Enumeration;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CheckOutCartTest {

	double EPSILON = 1e-15;

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
		firstInfo = new BulkProduct(firstDescription, firstBIC, unitPrice, 0.0);

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
				differentPrice, differentWeight, 0.15);

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

	@Test
	public void CheckOutWithNoItem() {
		CheckOutCart instance = new CheckOutCart();
		double ExtotalWeight = 0.0;
		double ExtotalPrice = 0.0;
		// test with correct TotalWeight
		assertEquals(ExtotalWeight, instance.getTotalWeight(), EPSILON);
		// test with correct TotalPrice
		assertEquals(ExtotalPrice, instance.getTotalCost(), EPSILON);
	}

	@Test
	public void CheckOutOneItem() {
		CheckOutCart instance = new CheckOutCart();
		instance.addItemToCart(firstGroceryItem);
		double ExtotalWeight = 10;
		double ExtotalPrice = 6.9;
		double ETotalTax = 0.0;
		double ESubTotal = 6.9;
		String product;
		Enumeration<GroceryItem> listItemsInCart;
		GroceryItem groceryItem;
		// test with correct TotalWeight
		assertEquals(ExtotalWeight, instance.getTotalWeight(), EPSILON);
		// test with different TotalWeight
		assertFalse(differentWeight == instance.getTotalWeight());

		// test with correct TotalPrice
		assertEquals(ExtotalPrice, instance.getTotalCost(), EPSILON);
		// test with different TotalPrice
		assertFalse(differentPrice == instance.getTotalCost());

		// test TotalTax
		assertEquals(ETotalTax, instance.getTotalTax(), EPSILON);
		// test SubTotal
		assertEquals(ESubTotal, instance.getSubTotal(), EPSILON);

		// check items added to cart
		listItemsInCart = instance.listItems();
		groceryItem = listItemsInCart.nextElement();
		product = groceryItem.getInfo().getDescription();
		assertEquals(firstDescription, product);
	}

	@Test
	public void CheckOutMoreThanOneItem() {
		CheckOutCart instance = new CheckOutCart();
		instance.addItemToCart(firstGroceryItem); // non-taxed item
		instance.addItemToCart(differentGroceryItem); // taxed item
		double ExtotalWeight = 11.35;
		double ExtotalPrice = 10.948; // considering tax at 15%
		double ETotalTax = 0.528;
		double garbage = 12.25;
		double ESubTotal = 10.420;
		String product;
		Enumeration<GroceryItem> listItemsInCart;
		GroceryItem groceryItem;
		// test with correct TotalWeight
		assertEquals(ExtotalWeight, instance.getTotalWeight(), EPSILON);
		// test with different TotalWeight
		assertFalse(garbage == instance.getTotalWeight());

		// test with correct TotalPrice
		assertEquals(ExtotalPrice, instance.getTotalCost(), EPSILON);
		// test with Different TotalPrice
		assertFalse(garbage == instance.getTotalCost());

		// checks SubTotal(TotalPrice - TotalTax)
		assertEquals(ESubTotal, instance.getSubTotal(), EPSILON);
		// test TotalTax
		assertEquals(ETotalTax, instance.getTotalTax(), EPSILON);

		// check items added to cart
		listItemsInCart = instance.listItems();
		groceryItem = listItemsInCart.nextElement();
		// first product
		product = groceryItem.getInfo().getDescription();
		assertEquals(firstDescription, product);

		groceryItem = listItemsInCart.nextElement();
		// second product
		product = groceryItem.getInfo().getDescription();
		assertEquals(differentDescription, product);
	}

}
