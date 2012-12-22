/*
 * Creator: Susan Elliott Sim
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008, September 12, 2012
 * 
 * The ProductInfo interface is implemented by BulkProduct and PackagedProduct.
 */

package ca.utoronto.csc301.SelfCheckOut.App;

/**
 * The ProductInfo interface is implemented by any class which represents a
 * saleable product in our store. In out example, these will be the BulkProduct
 * and PackagedProduct. These products differ in the manner in which their
 * prices are calculated, but have in common a descriptor, price, and
 * identifying code. The interface provides common accessor methods for these
 * fields.
 * 
 */
public interface ProductInfo {
	/**
	 * Accessor method for product description
	 */
	public String getDescription();

	/**
	 * Accessor method for unit price
	 */
	public double getPrice();

	/**
	 * Accessor method for identifying code, either BIC or UPC. (Both are of
	 * type Code)
	 */
	public Code getCode();

	/**
	 * Accessor method for determining if the product is taxed.
	 */
	public boolean isTaxed();

	/**
	 * Accessor method for the amount of tax incurred on this product.
	 */
	public double getTaxRate();

	/**
	 * Accessor method for determining if the product is on sale.
	 * 
	 * @return boolean True if the product is on sale.
	 */
	public boolean isOnSale();

	/**
	 * Acessor method for the discount of the product.
	 * 
	 * @return Double The total discount for the product.
	 */
	public double getDiscount();

	/**
	 * Acessor method for flat rate discount of the product.
	 * 
	 * @return Double The total flat rate discount for the product.
	 */
	public double getFlatRateDiscount();

	/**
	 * A setter method for the flat rate discount of the product.
	 * 
	 * @param Double
	 *            The flat rate discount of the product.
	 */
	public void setFlatRateDiscount(double flatRateDiscount);
}
