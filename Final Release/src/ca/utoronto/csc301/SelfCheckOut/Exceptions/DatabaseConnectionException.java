package ca.utoronto.csc301.SelfCheckOut.Exceptions;

/**
 * An exception that is thrown when the check out machine fails to connect to
 * the database.
 * 
 * @author Alex Rodrigues
 * 
 */
public class DatabaseConnectionException extends IncorrectStateException {
	private static final long serialVersionUID = 1L;

	public DatabaseConnectionException() {
		super();
	}

	public DatabaseConnectionException(String message) {
		super(message);
	}

	public DatabaseConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseConnectionException(Throwable cause) {
		super(cause);
	}

}
