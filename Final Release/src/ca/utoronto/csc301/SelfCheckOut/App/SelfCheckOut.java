/*
 * Creator: Susan Elliott Sim
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008, September 12, 2012
 * 
 * The SelfCheckOut class contains functions that can be called by the real user interface
 * of the self checkout systems.
 */

package ca.utoronto.csc301.SelfCheckOut.App;

import java.sql.SQLException;
import java.util.*;

import ca.utoronto.csc301.SelfCheckOut.Devices.BaggingArea;
import ca.utoronto.csc301.SelfCheckOut.Devices.BaggingAreaEvent;
import ca.utoronto.csc301.SelfCheckOut.Devices.BaggingAreaListener;
import ca.utoronto.csc301.SelfCheckOut.Devices.PaymentCollector;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.AddWhileBaggingException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.AddWhilePayingException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.IncorrectStateException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidProductException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidWeightException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountException;
import ca.utoronto.csc301.SelfCheckOut.Gui.Actions;

/**
 * The SelfCheckOut class contains the business logic of the sales point, and
 * keeps track of the state of the current customer's checkout. The class
 * contains methods to handle adding items to the customer's cart, accepting
 * payment, and receiving events from the BaggingArea.
 * 
 */
public class SelfCheckOut implements BaggingAreaListener {
	/**
	 * This enumeration represents the four states of the SelfCheckOut system:<br>
	 * <code>READY</code> means the system is awaiting a new customer<br>
	 * <code>ADDING</code> means the system is prepared for another item to be
	 * added<br>
	 * <code>BAGGING</code> means the system is awaiting notification that the
	 * item has been placed in the bagging area<br>
	 * <code>DONEADDING</code> means the system is waiting for the customer to
	 * pay for their items.<br>
	 * Attempts to add items while <code>BAGGING</code> or
	 * <code>DONEADDING</code> result in errors.
	 * 
	 * @author Susan Elliott Sim
	 * 
	 */
	public enum checkOutState {
		READY, ADDING, BAGGING, DONEADDING
	}; // different states of the system

	/**
	 * The cart containing items the customer has scanned.
	 */
	private CheckOutCart checkOutCart;
	/**
	 * The associated BaggingArea, which will notify SelfCheckOut when it
	 * detects a weight change.
	 */
	public BaggingArea baggingArea;

	/**
	 * An object representing the credit card or cash accepting device.
	 */
	private PaymentCollector paymentCollector;

	/**
	 * The database of products in the store.
	 */
	private Database DB;

	/**
	 * The current state of the system.
	 */
	private checkOutState transactionState;

	/**
	 * unique ID used to differentiate self checkout stands
	 */
	private int selfCheckOutID;

	/**
	 * The receipt of the check out cart.
	 */
	private String receipt;

	/**
	 * The argument-less constructor makes the necessary utility classes and
	 * passes them to the argumented constructor.
	 * 
	 * @throws Exception
	 */
	public SelfCheckOut() throws Exception {
		this(new BaggingArea(), new PaymentCollector(), new Database());
	}

	/**
	 * This is the chief constructor. It records the provided BaggingArea,
	 * PaymentCollector and ProductDB, and attaches itself to the BaggingArea so
	 * that it receives notifications of BaggingAreaEvents.
	 * 
	 * @param bagging
	 * @param payment
	 * @param db
	 * @throws Exception
	 */
	public SelfCheckOut(BaggingArea bagging, PaymentCollector payment,
			Database db) throws Exception {
		checkOutCart = new CheckOutCart();
		baggingArea = bagging;
		baggingArea.attach(this);
		paymentCollector = payment;
		DB = db;
		transactionState = checkOutState.READY;
	}

	/**
	 * Secondary constructor. When ID is given, it is assigned to selfcheckout.
	 * When ID is not given, defaults to 0. ID represents which selfcheckout a
	 * customer is currently using.
	 * 
	 * @param bagging
	 * @param payment
	 * @param db
	 * @param id
	 * @throws Exception
	 */
	public SelfCheckOut(BaggingArea bagging, PaymentCollector payment,
			Database db, int ID) throws Exception {
		checkOutCart = new CheckOutCart();
		baggingArea = bagging;
		baggingArea.attach(this);
		paymentCollector = payment;
		DB = db;
		transactionState = checkOutState.READY;
		selfCheckOutID = ID;
	}

	/**
	 * This version of addItem() accepts a UPC code and adds the corresponding
	 * PackagedProduct to the customer's cart. It looks the code up in the DB.
	 * 
	 * @param upcCode
	 *            The UPC of the scanned item.
	 * @return The GroceryItem which is also added to the CheckOutCart.
	 * @throws IncorrectStateException
	 *             Thrown when addItem() is called during Bagging or once
	 *             payment is initiated.
	 * @throws InvalidProductException
	 *             Thrown when a product corresponding to the UPC is not found.
	 * @throws InvalidWeightException
	 */
	public GroceryItem addItem(UPC upcCode) throws IncorrectStateException,
			InvalidProductException, InvalidWeightException,
			SaleDiscountException {
		/*
		 * if weight change is not ok, or transactionState is BAGGING or
		 * DONEADDING, don't allow customer to add!
		 */
		if (transactionState == checkOutState.BAGGING) {
			// user should place the previous item in the bagging area first.
			throw new AddWhileBaggingException();
		} else if (transactionState == checkOutState.DONEADDING) {
			// user has chosen to pay, and cannot add more items
			throw new AddWhilePayingException();
		} else {
			// returns a ProductInfo object
			ProductInfo info = DB.lookUpItem(upcCode);
			if (info == null) {
				throw new InvalidProductException();
			} else {
				// create a new GroceryItem object
				GroceryItem newItem = new GroceryItem(info,
						((PackagedProduct) info).getPrice(),
						((PackagedProduct) info).getWeight());
				// add the new GroceryItem object to vector
				checkOutCart.addItemToCart(newItem);
				transactionState = checkOutState.BAGGING;
				return newItem;
			}
		}
	}

	// This function will be called to add a BulkProduct object to checkOutCart.
	// It accepts an BIC code
	// object since this is what the user interface will pass. This function
	// will use it to find the product
	// information, create a GroceryItem object to add to the cart.
	/**
	 * This version of addItem() accepts a BIC and weight, and adds the
	 * corresponding BulkProduct to the customer's cart. It looks the code up in
	 * the DB.
	 * 
	 * @param bicCode
	 *            The BIC of the scanned item.
	 * @param weight
	 *            The amount of the BulkProduct being purchased.
	 * @return The GroceryItem which is also added to the CheckOutCart.
	 * @throws IncorrectStateException
	 *             Thrown when addItem() is called during Bagging or once
	 *             payment is initiated.
	 * @throws InvalidProductException
	 *             Thrown when a product corresponding to the BIC is not found.
	 * @throws InvalidWeightException
	 */
	public GroceryItem addItem(BIC bicCode, double weight)
			throws IncorrectStateException, InvalidProductException,
			InvalidWeightException, SaleDiscountException {

		/*
		 * if weight change is not ok, or transactionState is BAGGING or
		 * DONEADDING, don't allow customer to add!
		 */
		if (transactionState == checkOutState.BAGGING) {
			// user should place the previous item in the bagging area first.
			throw new AddWhileBaggingException();
		} else if (transactionState == checkOutState.DONEADDING) {
			// user has chosen to pay, and cannot add more items
			throw new AddWhilePayingException();
		} else {
			// returns a ProductInfo object
			ProductInfo info = DB.lookUpItem(bicCode);
			if (info == null) {
				throw new InvalidProductException();
			} else {
				// create a new GroceryItem object
				GroceryItem newItem = new GroceryItem(info,
						((BulkProduct) info).getPrice() * weight, weight);
				// add the new GroceryItem object to the cart
				checkOutCart.addItemToCart(newItem);
				transactionState = checkOutState.BAGGING;
				return newItem;
			}
		}
	}

	// Calls relevant function in CheckOutCart to return the Enumeration
	// containing items in the CheckOutcart
	/**
	 * This method retrieves an enumeration of all the items currently in the
	 * cart and returns it.
	 */
	public Enumeration<GroceryItem> listItemsInCart() {
		return checkOutCart.listItems();
	}

	/**
	 * This method returns the current cost total of all items in the cart.
	 */
	public double getTotalCost() {
		return checkOutCart.getTotalCost();
	}

	/**
	 * This method returns the total amount of tax for all items in the cart.
	 */
	public double getTotalTax() {
		return checkOutCart.getTotalTax();
	}

	/**
	 * Return the total amount of money saved from items on sale in the current
	 * check out cart.
	 * 
	 * @return The total amount of money saved from items on sale in the current
	 *         check out cart.
	 */
	public double getTotalDiscount() {
		return checkOutCart.getTotalDiscount();
	}

	/**
	 * Return the self checkout ID.
	 * 
	 * @return the self checkout ID.
	 */
	public int getSCOid() {
		return selfCheckOutID;
	}

	/**
	 * Return the total cost of all items in the cart with out tax.
	 * 
	 * @return The total cost of all items in the cart with out tax.
	 */
	public double getSubTotal() {
		return checkOutCart.getSubTotal();
	}

	/**
	 * When the bagging area detects a change in total weight, this function is
	 * called to change the state of the system (transactionState). Normally we
	 * would do a weight check here to ascertain if the predicted and actual
	 * bagging area weights match. Since that functionality is not implemented
	 * in this example, we simply change state to ADDING
	 * 
	 * @param be
	 *            The attached BaggingArea which is sending the event.
	 * @param event
	 *            The BaggingAreaEvent, which includes the total weight and most
	 *            recent change in the bagging area.
	 */
	public void notifyBaggingAreaEvent(BaggingArea be, BaggingAreaEvent event) {
		transactionState = checkOutState.ADDING;
	}

	/**
	 * This function handles the payment aspect of the self checkout machine. It
	 * will not reset the transactionState of the machine even after customer
	 * finished paying so that the customer will have time to look at the
	 * receipt.
	 * 
	 * @param logTax
	 *            Log the tax or not.
	 * @return the cart corresponding to the just-completed transaction or null
	 *         if customer didn't pay in full yet.
	 */
	public CheckOutCart payForGroceries(boolean logTax, String cardID,
			double payAmount, int payType) throws Exception {

		// make a copy of the old cart
		CheckOutCart oldCheckOutCart = checkOutCart;

		if (transactionState == checkOutState.BAGGING) {
			// user should place the previous item in the bagging area first.
			throw new AddWhileBaggingException();
		}

		transactionState = checkOutState.DONEADDING;
		paymentCollector.setTotalCost(getTotalCost());
		paymentCollector.collect(cardID, payAmount, payType);

		// check if customer has fully paid for grocery items
		if (paymentCollector.isFinishedPaying()) {
			// here is where we would record the transaction into our store's
			// inventory
			// and financial records, if we were simulating that part of the
			// system.
			if (logTax) {
				DB.logTax(checkOutCart.getTotalTax());
			}

			// check all items being bought to see if they are impulse buy
			// products
			try {
				DB.updateImpulseProducts(oldCheckOutCart, getSCOid());
			} catch (SQLException e) {
			}

			setReceipt();
			checkOutCart = new CheckOutCart();
			transactionState = checkOutState.READY;

		}
		return oldCheckOutCart;
	}

	/**
	 * Accessor method for the cart's receipt.
	 * 
	 * @return The Receipt.
	 */
	public String getReceipt() {
		return receipt;
	}

	/**
	 * Set the receipt for the check out cart.
	 */
	private void setReceipt() {
		String receipt = "========== Receipt ==========";
		receipt += "\nShopping cart "
				+ (new Actions()).printShoppingCart(listItemsInCart());
		receipt += "\n\nSub-Total $" + getSubTotal();
		receipt += "\nTax $" + getTotalTax();
		receipt += "\nDiscount $" + getTotalDiscount();
		receipt += "\nTotal Cost $" + getTotalCost();
		receipt += "\nTotal Paid $" + this.getPaymentCollector().getTotalPaid();
		receipt += "\nChange $" + getPaymentCollector().getChange();
		this.receipt = receipt;
	}

	/**
	 * Handling payment is not part of this assignment. This function just
	 * returns the cart indicating the payment was received. It also clears the
	 * shopping cart and resets the system state. If we implemented this part of
	 * the system, we would throw an exception to indicate a failed transaction
	 * rather than returning null.
	 * 
	 * @return the cart corresponding to the just-completed transaction.
	 * @throws Exception
	 */
	public CheckOutCart payForGroceries() throws Exception {
		// Default do not log tax (so payForGroceries can be used in testing)
		return payForGroceries(false, "", 0.0, 1);
	}

	/**
	 * An accessor method which returns the BaggingArea associated with this
	 * SelfCheckout. Useful if the application wants to also receive bagging
	 * events, for example.
	 */
	public BaggingArea getBaggingArea() {
		return baggingArea;
	}

	/**
	 * An accessor method which returns the ProductDB associated with this
	 * SelfCheckOut. Useful if the application wants to add items to the
	 * database or to look up items.
	 */
	public Database getProductDB() {
		return DB;
	}

	/**
	 * An accessor method which returns the PaymentCollector associated with
	 * this SelfCheckOut. Useful if... useful if.... All right, not particularly
	 * useful.
	 */
	public PaymentCollector getPaymentCollector() {
		return paymentCollector;
	}

	/**
	 * A utility method for resetting the SelfCheckOut to the initial state with
	 * an empty cart. Useful when testing.
	 */
	public void resetAll() {

		// replace the old cart with a new one
		checkOutCart = new CheckOutCart();

		// reset our state to waiting for a customer.
		transactionState = checkOutState.READY;

	}
}
