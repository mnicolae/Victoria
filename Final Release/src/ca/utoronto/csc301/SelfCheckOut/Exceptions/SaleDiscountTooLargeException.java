package ca.utoronto.csc301.SelfCheckOut.Exceptions;

/**
 * An exception that indicates an invalid sale has occurred. This happens if the
 * discount is larger than the price of the item.
 * 
 * @author Alex Rodrigues
 * 
 */
public class SaleDiscountTooLargeException extends SaleDiscountException {
	private static final long serialVersionUID = 1L;

	public SaleDiscountTooLargeException() {
		super();
	}

	public SaleDiscountTooLargeException(String message) {
		super(message);
	}

	public SaleDiscountTooLargeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SaleDiscountTooLargeException(Throwable cause) {
		super(cause);
	}

}
