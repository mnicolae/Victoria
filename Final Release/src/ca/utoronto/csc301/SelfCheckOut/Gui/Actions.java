/*
 * Creator: Rosalva Gallardo-Valencia
 * 
 * Created on Oct 2, 2008
 * Updated on Oct 6, 2008, September 12, 2012, October 13, 2012
 * 
 * The Actions class handles the actions that are called
 * from the Graphical User Interface for the Self CheckOut 
 * system. It includes actions such as: 
 * Start Transaction, Add a Packaged Item, Add a Bulk Item, 
 * Bag Item, Pay for Items, and summon a cashier for help. 
 * Also, it includes an internal action for printing the 
 * contents of the shopping cart.
 */
package ca.utoronto.csc301.SelfCheckOut.Gui;

import java.util.Enumeration;

import ca.utoronto.csc301.SelfCheckOut.App.BIC;
import ca.utoronto.csc301.SelfCheckOut.App.GroceryItem;
import ca.utoronto.csc301.SelfCheckOut.App.Database;
import ca.utoronto.csc301.SelfCheckOut.App.SelfCheckOut;
import ca.utoronto.csc301.SelfCheckOut.App.UPC;
import ca.utoronto.csc301.SelfCheckOut.Devices.BaggingArea;
import ca.utoronto.csc301.SelfCheckOut.Devices.PaymentCollector;

/**
 * This class contains the actions that can be called from the Graphical User
 * Interface of the Self CheckOut System. It also includes an internal action to
 * print the contents of the shopping cart
 * 
 */
public class Actions {
	/**
	 * Method that creates a new SelfCheckOut object that includes baggingArea,
	 * paymentCollector, and productDB objects
	 * 
	 * @return new selfCheckOut object
	 * @throws Exception
	 */
	protected SelfCheckOut start() throws Exception {
		SelfCheckOut selfCheckOut;
		BaggingArea baggingArea;
		Database productDB;
		PaymentCollector paymentCollector;

		baggingArea = new BaggingArea();
		paymentCollector = new PaymentCollector(false);
		productDB = new Database();
		selfCheckOut = new SelfCheckOut(baggingArea, paymentCollector,
				productDB);
		return selfCheckOut;
	}

	/**
	 * Method that creates a new UPC object with the universalProductCode given
	 * as parameter, then it adds the UPC to the selfCheckOut object. It returns
	 * the groceryItem that was added to the shopping cart.
	 * 
	 * @param selfCheckOut
	 *            SelfCheckOut object where the Packaged Item will be added
	 * @param universalProductCode
	 *            String that contains the universal product code for the
	 *            Packaged item
	 * @return groceryItem that was added to the shopping cart
	 * @throws Exception
	 */
	protected GroceryItem addUPC(SelfCheckOut selfCheckOut,
			String universalProductCode) throws Exception {
		UPC upc = new UPC(universalProductCode);
		GroceryItem groceryItem = selfCheckOut.addItem(upc);
		return groceryItem;
	}

	/**
	 * Method that creates a new BIC object with the bulkItemCode given as
	 * parameter, then it adds the BIC to the selfCheckOut object indicating
	 * also the weight of the Bulk Item. It returns the groceryItem that was
	 * added to the shopping cart
	 * 
	 * @param selfCheckOut
	 *            SelfCheckOut object where the Bulk Item will be added
	 * @param bulkItemCode
	 *            String that contains the bulk item code for the Bulk Item
	 * @param weight
	 *            Double number that contains the weight of the Bulk Item
	 * @return groceryItem that was added to the shopping cart
	 * @throws Exception
	 */
	protected GroceryItem addBIC(SelfCheckOut selfCheckOut,
			String bulkItemCode, double weight) throws Exception {
		BIC bic = new BIC(bulkItemCode);
		GroceryItem groceryItem = selfCheckOut.addItem(bic, weight);
		return groceryItem;
	}

	/**
	 * Method that bags an item using the baggingArea object from the
	 * selfCheckOut object
	 * 
	 * @param selfCheckOut
	 *            SelfCheckOut object for the transaction
	 * @param groceryItem
	 *            GroceryItem that will be bagged
	 * @throws Exception
	 */
	protected void bagItem(SelfCheckOut selfCheckOut, GroceryItem groceryItem)
			throws Exception {
		BaggingArea baggingArea = selfCheckOut.getBaggingArea();
		baggingArea.changeWeight(groceryItem.getWeight());
	}

	/**
	 * Method that collects the payment for all the groceries in the cart
	 * 
	 * @param selfCheckOut
	 *            SelfCheckOut object for the transaction
	 * @param cardID
	 *            ID of the card used, empty string if cash payment
	 * @param payAmount
	 *            amount paid in this instance of payment
	 * @param payType
	 *            type of payment, see PaymentCollector.collect()
	 * @throws Exception
	 */
	protected void payItems(SelfCheckOut selfCheckOut, String cardID,
			double payAmount, int payType) throws Exception {

		selfCheckOut.payForGroceries(true, cardID, payAmount, payType);
	}

	/**
	 * Method that prints the contents of the shopping cart This method is
	 * called internally for the GUI class and does not correspond to any
	 * external GUI action
	 * 
	 * @param listItemsInCart
	 *            Enumeration of GroceryItems that are in the shopping cart
	 */
	public String printShoppingCart(Enumeration<GroceryItem> listItemsInCart) {
		String returnString = "\n";
		int count = 0;
		GroceryItem groceryItem;
		String line;
		while (listItemsInCart.hasMoreElements()) {
			count++;
			groceryItem = listItemsInCart.nextElement();
			line = "Item " + count + ": ";
			line += groceryItem.getInfo().getCode().getCode();
			line += "-";
			line += groceryItem.getInfo().getDescription();
			line += " ";
			line += groceryItem.getWeight();
			line += "lb $";
			line += groceryItem.getPrice();
			if (groceryItem.getInfo().getDiscount() > 0) {
				line += ", with sale $ "
						+ (groceryItem.getPrice() - groceryItem.getInfo()
								.getDiscount());
			}
			line += "\n";
			returnString += line;

		}
		return returnString;
	}
}
