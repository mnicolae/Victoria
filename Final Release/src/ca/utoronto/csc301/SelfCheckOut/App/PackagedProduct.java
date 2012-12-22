/*
 * Creator: Susan Elliott Sim
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008, September 12, 2012
 * 
 * The PackagedProduct class is for products with a UPC code. It implements the ProductInfo interface.
 */

package ca.utoronto.csc301.SelfCheckOut.App;

import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountNegativeException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountTooLargeException;

/**
 * A PackedProduct represents a single UPC-code-bearing product in the store.
 * Packaged products are sold as discrete single units, and never by weight.
 * Note the difference between 'items' and 'products': A 'product' is a type of
 * good sold at the store, whereas an 'item' is a particular box of that
 * product.
 * 
 */
public class PackagedProduct implements ProductInfo {
	
	/**
	 * The flat rate discount for the product.
	 */
	private double flatRateDiscount = 0;
	/**
	 * The UPC for this product.
	 */
	private UPC myUPC;

	/**
	 * The price for a box of the product.
	 */
	private double myPrice;

	/**
	 * The estimated weight for a box of the product.
	 */
	private double myWeight;

	/**
	 * A text description of the product.
	 */
	private String myDescription;

	/**
	 * The amount of tax incurred on this product.
	 */
	private double taxRate;

	/**
	 * The discount taken off the product's total price.
	 */
	private double discount;

	/**
	 * This constructor stores all relevant details of the product, which can be
	 * retrieved using accessor methods.
	 * 
	 * @param descrip
	 *            A text description of the product.
	 * @param UPCcode
	 *            A unique 12-digit UPC code for the product.
	 * @param productCost
	 *            The cost of the product.
	 * @param productWeight
	 *            The estimated weight of the product.
	 * @throws SaleDiscountException
	 */
	public PackagedProduct(String descrip, UPC UPCcode, double productCost,
			double productWeight) throws SaleDiscountException {
		this(descrip, UPCcode, productCost, productWeight, 0, 0);
	}

	/**
	 * This constructor stores all relevant details of the product, which can be
	 * retrieved using accessor methods.
	 * 
	 * @param descrip
	 *            A text description of the product.
	 * @param UPCcode
	 *            A unique 12-digit UPC code for the product.
	 * @param productCost
	 *            The cost of the product.
	 * @param productWeight
	 *            The estimated weight of the product.
	 * @param taxRate
	 *            The amount of tax incurred on this product.
	 * @throws SaleDiscountException
	 */
	public PackagedProduct(String descrip, UPC UPCcode, double productCost,
			double productWeight, double taxRate) throws SaleDiscountException {
		this(descrip, UPCcode, productCost, productWeight, taxRate, 0);
	}

	/**
	 * This constructor stores all relevant details of the product, which can be
	 * retrieved using accessor methods.
	 * 
	 * @param descrip
	 *            A text description of the product.
	 * @param UPCcode
	 *            A unique 12-digit UPC code for the product.
	 * @param productCost
	 *            The cost of the product.
	 * @param productWeight
	 *            The estimated weight of the product.
	 * @param taxRate
	 *            The amount of tax incurred on this product.
	 * @param discount
	 *            The total discount/sale on the product
	 * @throws SaleDiscountException
	 */
	public PackagedProduct(String descrip, UPC UPCcode, double productCost,
			double productWeight, double taxRate, double discount)
			throws SaleDiscountException {
		this.taxRate = taxRate;
		myDescription = descrip;
		myUPC = UPCcode;
		myPrice = productCost;
		myWeight = productWeight;
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
		if (this.discount > myPrice) {
			throw new SaleDiscountTooLargeException();
		}
		if (this.discount < 0) {
			throw new SaleDiscountNegativeException();
		}
	}

	/**
	 * An accessor method which returns the unique UPC of the product.
	 */
	public UPC getUPC() {
		return myUPC;
	}

	/**
	 * An accessor method which returns the unique Code (UPC) of the product.
	 */
	public Code getCode() {
		return getUPC();
	}

	/**
	 * An accessor method which returns the price of the product.
	 */
	public double getPrice() {
		return myPrice;
	}

	/**
	 * An accessor method which returns the weight of the product.
	 */
	public double getWeight() {
		return myWeight;
	}

	/**
	 * An accessor method which returns the text description of the product.
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
	 * @see ca.utoronto.csc301.SelfCheckOut.App.ProductInfo#getTaxedRate()
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
