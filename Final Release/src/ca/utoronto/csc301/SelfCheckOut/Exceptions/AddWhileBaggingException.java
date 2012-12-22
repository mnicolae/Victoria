/*
 * 
 * Creator: Susan Elliott Sim
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008, September 12, 2012
 * 
 */

package ca.utoronto.csc301.SelfCheckOut.Exceptions;

/**
 * A simple exception which is thrown when a customer tries to scan a new item
 * before bagging the previous one.
 * 
 */
public class AddWhileBaggingException extends IncorrectStateException {
	private static final long serialVersionUID = 1L;

	public AddWhileBaggingException() {
		super();
	}

	public AddWhileBaggingException(String message) {
		super(message);
	}

	public AddWhileBaggingException(String message, Throwable cause) {
		super(message, cause);
	}

	public AddWhileBaggingException(Throwable cause) {
		super(cause);
	}

}
