package ca.utoronto.csc301.SelfCheckOut.Exceptions;

/**
 * An exception that indicates an invalid sale has occurred. This happens if the
 * discount is negative.
 * 
 * @author Alex Rodrigues
 * 
 */
public class SaleDiscountNegativeException extends SaleDiscountException {
	private static final long serialVersionUID = 1L;

	public SaleDiscountNegativeException() {
		super();
	}

	public SaleDiscountNegativeException(String message) {
		super(message);
	}

	public SaleDiscountNegativeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SaleDiscountNegativeException(Throwable cause) {
		super(cause);
	}

}
