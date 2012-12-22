/*
 * Creator: Susan Elliott Sim
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008, September 12, 2012, September 30, 2012
 * 
 * The BulkProduct class is for products with a BIC code. It implements the ProductInfo interface.
 */

package ca.utoronto.csc301.SelfCheckOut.App;

import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountNegativeException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountTooLargeException;

/**
 * The BulkProduct class represents a grocery product which is sold by weight,
 * such as produce, deli, meat, etc. The details of the product are stored at
 * the time of construction, and can be retrieved using accessor functions.
 * 
 */
public class BulkProduct implements ProductInfo {
	
	/**
	 * The flat rate discount for the product.
	 */
	private double flatRateDiscount = 0;
	/**
	 * The BIC representing the product's 5-digit bulk item code.
	 */
	private BIC myBIC;

	/**
	 * The price per unit weight of the product
	 */
	private double myUnitPrice;

	/**
	 * The String description of the product.
	 */
	private String myDescription;

	/**
	 * Determine if the product's price incurs tax or not.
	 */
	private double taxRate;

	/**
	 * Indicates discount on item if it is on sale.
	 */
	private double discount;

	/**
	 * @param description
	 *            The text description of the product.
	 * @param BICcode
	 *            A BIC representing the product's 5-digit bulk item code.
	 * @param price
	 *            The unit price of the product.
	 * @throws SaleDiscountException
	 */
	public BulkProduct(String description, BIC BICcode, double price)
			throws SaleDiscountException {
		this(description, BICcode, price, 0, 0);
	}

	/**
	 * @param description
	 *            The text description of the product.
	 * @param BICcode
	 *            A BIC representing the product's 5-digit bulk item code.
	 * @param price
	 *            The unit price of the product.
	 * @param taxRate
	 *            The amount of tax incurred.
	 * @throws SaleDiscountException
	 */
	public BulkProduct(String description, BIC BICcode, double price,
			double taxRate) throws SaleDiscountException {
		this(description, BICcode, price, taxRate, 0);
	}

	/**
	 * @param description
	 *            The text description of the product.
	 * @param BICcode
	 *            A BIC representing the product's 5-digit bulk item code.
	 * @param price
	 *            The unit price of the product.
	 * @param taxRate
	 *            The amount of tax incurred.
	 * @param discount
	 *            The discount on the unit price.
	 * @throws SaleDiscountException
	 */

	public BulkProduct(String description, BIC BICcode, double price,
			double taxRate, double discount) throws SaleDiscountException {
		this.taxRate = taxRate;
		myDescription = description;
		myBIC = BICcode;
		myUnitPrice = price;
		this.discount = discount;
		validateDiscount();

	}

	/**
	 * Validate the discount. Invalid discounts include negative discounts or
	 * discounts that are too large
	 * 
	 * @throws SaleDiscountException
	 *             The type of discount error.
	 */
	private void validateDiscount() throws SaleDiscountException {
		if (this.discount > myUnitPrice) {
			throw new SaleDiscountTooLargeException();
		}
		if (this.discount < 0) {
			throw new SaleDiscountNegativeException();
		}
	}

	/**
	 * Accessor function returning the product's BIC.
	 */
	public BIC getBIC() {
		return myBIC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uci.ics121.SelfCheckOut.App.ProductInfo#getCode()
	 */
	public Code getCode() {
		return getBIC();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uci.ics121.SelfCheckOut.App.ProductInfo#getPrice()
	 */
	public double getPrice() {
		return myUnitPrice;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uci.ics121.SelfCheckOut.App.ProductInfo#getDescription()
	 */
	public String getDescription() {
		return myDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.utoronto.csc301.SelfCheckOut.App.ProductInfo#isTaxed()
	 */
	public boolean isTaxed() {
		return taxRate > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.utoronto.csc301.SelfCheckOut.App.ProductInfo#getTaxRate()
	 */
	public double getTaxRate() {
		return taxRate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.utoronto.csc301.SelfCheckOut.App.ProductInfo#isOnSale()
	 */
	public boolean isOnSale() {

		return discount > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.utoronto.csc301.SelfCheckOut.App.ProductInfo#getDiscount()
	 */
	public double getDiscount() {
		return discount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.utoronto.csc301.SelfCheckOut.App.ProductInfo#getFlatRateDiscount()
	 */
	public double getFlatRateDiscount() {
		return this.flatRateDiscount;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.utoronto.csc301.SelfCheckOut.App.ProductInfo#setFlatRateDiscount()
	 */
	public void setFlatRateDiscount(double flatRateDiscount){
		this.flatRateDiscount = flatRateDiscount;
	}
}
