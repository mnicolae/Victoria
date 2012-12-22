package ca.utoronto.csc301.SelfCheckOut.Exceptions;

/**
 * An exception that indicates an invalid sale has occurred. This can happen if
 * the discount is more then the total cost of an item or the discount is
 * negative.
 * 
 * @author Alex Rodrigues
 * 
 */
public class SaleDiscountException extends Exception {
	private static final long serialVersionUID = 1L;

	public SaleDiscountException() {
		super();
	}

	public SaleDiscountException(String message) {
		super(message);
	}

	public SaleDiscountException(String message, Throwable cause) {
		super(message, cause);
	}

	public SaleDiscountException(Throwable cause) {
		super(cause);
	}

}
