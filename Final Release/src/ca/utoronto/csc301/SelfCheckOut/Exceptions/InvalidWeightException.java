package ca.utoronto.csc301.SelfCheckOut.Exceptions;

/**
 * An exception that is thrown when an impossible weight is recorded by the
 * sensor. For example a weight can not be negative or 0 for an item.
 * 
 * @author Nisarg
 * 
 */
public class InvalidWeightException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidWeightException() {
		super();
	}

	public InvalidWeightException(String message) {
		super(message);
	}

	public InvalidWeightException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidWeightException(Throwable cause) {
		super(cause);
	}

}
