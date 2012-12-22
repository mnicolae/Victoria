/*
 * 
 * Creator: Susan Elliott Sim
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008, September 12, 2012
 * 
 */
package ca.utoronto.csc301.SelfCheckOut.App;

/**
 * The Code interface represents the methods common to Universal Product Codes
 * (UPCs) and Bulk Item Codes (BICs). This allows the two types of codes to be
 * used interchangeably where appropriate.
 * 
 * @see BIC
 * @see UPC
 * 
 */
public interface Code {
	/**
	 * Get the code (a String of digits) which this object represents.
	 * 
	 * @return The String of digits.
	 */
	public String getCode();

	/**
	 * Compare another Code object to this Code object to determine if they are
	 * effectively equal. This occurs if the Strings they represent are
	 * identical.
	 * 
	 * @param comparedCode
	 *            The Code object we are comparing to.
	 * @return <code>true</code> if the String representations are identical,
	 *         <code>false</code> otherwise.
	 */
	public boolean equals(Code comparedCode);

	/**
	 * The hashCode is generally the final character of a Code, and is used to
	 * determine whether the Code is a legal example of its type.
	 * 
	 * @return An int containing the hash digit.
	 * @see UPC#hashCode()
	 */
	public int hashCode();

	/**
	 * Checks if the provided string contains non-digit characters.
	 * 
	 * @param code
	 * @return <code>true</code> if the string string has non-digit characters;
	 *         <code>false</code> otherwise.
	 */
	public boolean hasNonDigits(String code);
}
