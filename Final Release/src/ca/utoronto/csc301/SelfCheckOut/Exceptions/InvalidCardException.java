package ca.utoronto.csc301.SelfCheckOut.Exceptions;

/**
 * Superclass exception for various invalid card exceptions
 */
public class InvalidCardException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidCardException() {
		super();
	}

	public InvalidCardException(String message) {
		super(message);
	}

	public InvalidCardException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCardException(Throwable cause) {
		super(cause);
	}

}
