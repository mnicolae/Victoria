package ca.utoronto.csc301.SelfCheckOut.Exceptions;

/**
 * An exception that is thrown when the JDBC driver is missing or invalid.
 * 
 * @author Alex Rodrigues
 * 
 */
public class JDCBDriverException extends IncorrectStateException {
	private static final long serialVersionUID = 1L;

	public JDCBDriverException() {
		super();
	}

	public JDCBDriverException(String message) {
		super(message);
	}

	public JDCBDriverException(String message, Throwable cause) {
		super(message, cause);
	}

	public JDCBDriverException(Throwable cause) {
		super(cause);
	}

}
