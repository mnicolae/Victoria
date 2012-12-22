/*
 * Creator: Susan Elliott Sim
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008, September 12, 2012
 * 
 * The CheckOutCart class maintains the items added as well as the total weight (tracked by the system, NOT 
 * the bagging area) and cost of the items.
 */

package ca.utoronto.csc301.SelfCheckOut.App;

import java.util.Vector;
import java.util.*;

/**
 * The CheckOutCart class stores a Vector of GroceryItems which the customer has
 * scanned. It represents the products which the customer has scanned and bagged
 * so far in the transaction.
 * 
 */
public class CheckOutCart {
	/**
	 * A Vector of GroceryItems.
	 */
	private Vector<GroceryItem> myItems; // stores the items added

	/**
	 * The cost of the items in the cart
	 */
	private double totalCost;

	/**
	 * The tax for all items in the cart that are taxable.
	 */
	private double totalTax;

	/**
	 * The predicted weight of the items in the cart.
	 */
	private double totalWeight; // total weight of the items, tracked by the
								// system. Can be used to compare with the
								// weight in the bagging area.
	/**
	 * The total amount of money saved from one or more products on sale.
	 */
	private double totalDiscount;

	/**
	 * Creates a new CheckOutCart with an empty item list, 0 cost and 0 weight.
	 */
	public CheckOutCart() {
		myItems = new Vector<GroceryItem>();
		totalCost = 0;
		totalWeight = 0;
		totalTax = 0;
		totalDiscount = 0;
	}

	/**
	 * Accessor method which returns the cost of the items in the cart.
	 */
	public double getTotalCost() {
		return totalCost;
	}

	/**
	 * Accessor method which returns the predicted weight of the items in the
	 * cart.
	 */
	public double getTotalWeight() {
		return totalWeight;
	}

	/**
	 * Add a single item to the cart, and add its weight and cost to the running
	 * totals.
	 * 
	 * @param newItem
	 */
	public void addItemToCart(GroceryItem newItem) {
		ProductInfo info = newItem.getInfo();
		myItems.add(newItem);
		double price = newItem.getPrice();

		if (info.isOnSale()) {
			if (info instanceof PackagedProduct) {
				price -= info.getDiscount();
			} else {
				price -= info.getDiscount() * newItem.getWeight();
			}
			totalDiscount += (newItem.getPrice() - price);
		}

		totalCost += price;
		if (info.isTaxed()) {
			totalCost += price * info.getTaxRate();
			totalTax += price * info.getTaxRate();
		}

		totalWeight = totalWeight + newItem.getWeight();
	}

	/**
	 * This method returns an enumeration of the GroceryItems in the cart. We
	 * don't return the Vector since we don't want external code to alter our
	 * cart.
	 */
	public Enumeration<GroceryItem> listItems() {
		Enumeration<GroceryItem> enumItems = myItems.elements();
		return enumItems;
	}

	/**
	 * Return the total tax incurred on all items in the cart.
	 * 
	 * @return The total tax incurred on all items in the cart.
	 */
	public double getTotalTax() {
		return totalTax;
	}

	/**
	 * Return the sub-total: the total cost of all items in the cart without
	 * tax.
	 * 
	 * @return The total cost of all items in the cart without tax.
	 */
	public double getSubTotal() {
		return totalCost - totalTax;
	}

	/**
	 * Return the total amount of money saved from items on sale in the check
	 * out cart.
	 * 
	 * @return The total amount of money saved from items on sale.
	 */
	public double getTotalDiscount() {
		return totalDiscount;
	}
}
