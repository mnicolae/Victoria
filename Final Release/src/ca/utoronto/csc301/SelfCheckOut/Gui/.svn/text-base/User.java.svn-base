/*
 * This is utility class for login system.
 * @author Nisarg Patel
 */
package ca.utoronto.csc301.SelfCheckOut.Gui;

import ca.utoronto.csc301.SelfCheckOut.App.Database;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.DatabaseConnectionException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.JDCBDriverException;

public class User {

	public String loginName;
	public String password = "";
	public String fname;

	/*
	 * Privilege to access completed administrator features 
	 * 0 - Employee Privilege 
	 * 1 - Administrator Privilege
	 */
	public int privilege;

	/*
	 * This method Encrypts Password
	 * 
	 * @param String password :plain text password Entered by user
	 * @return String encrypted string of password
	 */
	public String encryptPassword(char[] clearPassword) {
		String password = "";
		int[] salt = { 213, 34, 45, 567, 923, 1324, 45, 67, 78, 1234, 56, 1346,
				67, 86, 7, 234, 897, 123, 546, 2 };
		for (int i = 0; i < clearPassword.length; i++) {
			if (salt.length > i) {
				password += (clearPassword[i] + salt[i]);
			}
		}
		return password;
	}

	/*
	 * This method checks entered password to password of user from database.
	 * 
	 * @param String password :plain text password Entered by user
	 * 
	 * @return true if password matched
	 */
	public boolean comparePassword(char[] Password) {
		if (this.password.equals(encryptPassword(Password))) {
			return true;
		}
		return false;
	}

	/*
	 * Sets privilege for user
	 */
	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}
	
	/*
	 * Gets privilege for user
	 */
	public int getPrivilege() {
		return this.privilege;
	}

	/*
	 * 
	 */
	public static User getUser(String username) {
		try {
			Database db = new Database();
			User user = db.getUser(username);
			return user;
		} catch (JDCBDriverException e) {
			return null;
		}
	}
}