package ca.utoronto.csc301.SelfCheckOut.Exceptions;

/**
 * Thrown when given type of payment is unrecognised
 */
public class UnrecognizedPayTypeException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnrecognizedPayTypeException() {
	}

	public UnrecognizedPayTypeException(String message) {
		super(message);
	}

	public UnrecognizedPayTypeException(Throwable cause) {
		super(cause);
	}

	public UnrecognizedPayTypeException(String message, Throwable cause) {
		super(message, cause);
	}

}
