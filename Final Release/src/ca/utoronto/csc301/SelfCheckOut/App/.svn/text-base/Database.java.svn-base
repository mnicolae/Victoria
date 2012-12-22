/*
 * Creator: Alex Rodrigues
 * The ProductDB class is a wrapper around the database. The items are stored in a SQL Database.
 */

package ca.utoronto.csc301.SelfCheckOut.App;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import ca.utoronto.csc301.SelfCheckOut.Exceptions.DatabaseConnectionException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidBICException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.JDCBDriverException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountException;
import ca.utoronto.csc301.SelfCheckOut.Gui.User;

/**
 * The ProductDB class is a wrapper around a database of products which are
 * managed elsewhere.
 */
public class Database {

	/**
	 * An enumeration of the tables that exist in the database.
	 * 
	 * @author Alex Rodrigues
	 * 
	 */
	public static enum Table {
		ProductCategory, Sale, BulkProduct, PackagedProduct
	}

	/**
	 * The location and name of the JDBC driver. This is used for registering
	 * the driver.
	 */
	private final String JDBC_DRIVER_NAME = "org.sqlite.JDBC";
	/**
	 * The name of the database.
	 */
	private String DB_NAME;

	/**
	 * The amount of seconds elapsed before a query will time out.
	 */
	private final int queryTimeOut = 5;

	/**
	 * This maps recently searched product codes to their product info.
	 * This puts less strain on the database and reduces concurrency issues when overloading the database
	 * with requests in the test suite.
	 */
	private static Map<String, ProductInfo> productCache = new HashMap<String,ProductInfo>();
	/**
	 * Create a wrapper around a database.
	 * 
	 * @param The
	 *            location of the database.
	 * @throws JDCBDriverException
	 * @throws DatabaseConnectionException
	 */
	public Database(String dbLocation) throws JDCBDriverException{

		this.DB_NAME = dbLocation;
		// Register the JDBC Driver
		if (!registerDriver()) {
			throw new JDCBDriverException();
		}
	}

	/**
	 * Create a wrapper around the default database.
	 * 
	 * @throws JDCBDriverException
	 * @throws DatabaseConnectionException
	 */
	public Database() throws JDCBDriverException {
		this("Database/SelfCheckOut.db");
	}

	/**
	 * Register the Sqlite JDCBC driver.
	 * 
	 * @return The success of registering the driver.
	 */
	private boolean registerDriver() {
		try {
			Class.forName(JDBC_DRIVER_NAME);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * Return an open connection to the Database.
	 * 
	 * @return
	 * 
	 * @return The connection to the Database.
	 * @throws DatabaseConnectionException
	 */
	private Connection connectDB() throws DatabaseConnectionException {
		try {
			return DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
		} catch (SQLException sqle) {
			throw new DatabaseConnectionException();
		}
	}

	/**
	 * This method looks up a product in the database.
	 * 
	 * @param code
	 *            The UPC or BIC of the product.
	 * @return The ProductInfo of the corresponding product, or null if no such
	 *         product.
	 * @throws SaleDiscountException
	 */
	public ProductInfo lookUpItem(Code code) throws SaleDiscountException {

		if (code instanceof BIC) {
			// query BulkProduct
			return lookUpBulkProduct((BIC) code);
		} else if (code instanceof UPC) {
			// query PackagedProduct
			return lookUpPackagedProduct((UPC) code);
		}
		return null;
	}

	/**
	 * Add the tax to the daily tax log in the database.
	 * 
	 * @param tax
	 *            The amount of tax to be logged.
	 */
	public void logTax(double tax) {
		Connection conn = null;
		try {

			conn = connectDB();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			// Set the current total tax for the day to zero if no record exists
			// for the day
			String query1 = "insert or ignore into TaxLog values(date('now'), 0);";
			// Increment the total tax
			String query2 = "update TaxLog set totalTax = totalTax + " + tax
					+ " where date like date('now');";
			stmt.executeUpdate(query1 + query2);
			stmt.close();
		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException e) {
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignore) {
			}
		}
	}

	/**
	 * Execute an arbitrary query to update the database. Throw SQLException if
	 * not possible (useful for excepion.getMessages()).
	 * 
	 * @param query
	 *            The query.
	 * @throws SQLException
	 *             Uses customized error message.
	 */
	public void updateDatabase(String query) throws SQLException {
		Connection conn = null;
		try {
			conn = connectDB();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			stmt.executeUpdate(query);
			stmt.close();
		} catch (DatabaseConnectionException e) {
		} finally {
			if (conn != null)
				conn.close();
		}
		productCache.clear(); //cache may be inaccurate following the update above
	}

	/**
	 * Look up a BulkProduct in the database.
	 * 
	 * @param BIC
	 *            The BIC of the product.
	 * @return The BulkProduct of the corresponding product, or null if no such
	 *         product.
	 * @throws SaleDiscountException
	 */
	private BulkProduct lookUpBulkProduct(BIC code)
			throws SaleDiscountException {
		
		//if product code is in our cache return the product info
		if (productCache.containsKey(code.getCode())){
			return (BulkProduct) productCache.get(code.getCode());
		}
		BulkProduct bp = null;
		Connection conn = null;
		try {
			conn = connectDB();

			Statement stmt = conn.createStatement();
			Statement stmt2 = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			stmt2.setQueryTimeout(queryTimeOut);
			String q = "select * from BulkProduct natural join ProductCategory where BIC = '"
					+ code.getCode() + "'";

			String q2 = "select s.code, sum(percentReduction) as percentReduction, sum(flatReduction) as flatReduction "
					+ "from BulkProduct b, Sale s "
					+ "where date('now') >= s.startDate and date('now') <= s.endDate and b.BIC=s.code and b.BIC='"
					+ code.getCode() + "'";
			ResultSet rs = stmt.executeQuery(q);
			ResultSet rs2 = stmt2.executeQuery(q2);
			if (rs.next()) {
				double discount = 0;
				String descrip = rs.getString("descrip");
				double unitPrice = Double
						.parseDouble(rs.getString("unitPrice"));
				double taxRate = Double.parseDouble(rs.getString("taxRate"));
				double percentReduction = 0;
				double flatReduction = 0;
				if (rs2.next()) {
					try {
						percentReduction = Double.parseDouble(rs2
								.getString("percentReduction"));
						flatReduction = Double.parseDouble(rs2
								.getString("flatReduction"));
					} catch (Exception ignore) {
					}
					discount = getDiscount(percentReduction, flatReduction,
							unitPrice);

				}

				bp = new BulkProduct(descrip, code, unitPrice, taxRate,
						discount);
				bp.setFlatRateDiscount(flatReduction);
			}
			rs.close();
			rs2.close();
			stmt.close();
			stmt2.close();
		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException ignore) {
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
			}
		}
		if (bp != null){
			//add this product to our cache
			productCache.put(code.getCode(), bp);
		}
		return bp;
	}

	/**
	 * Looks up a PackagedProduct in the database.
	 * 
	 * @param UPC
	 *            The UPC of the product.
	 * @return The PackagedProduct of the corresponding product, or null if no
	 *         such product.
	 * @throws SaleDiscountException
	 */
	private PackagedProduct lookUpPackagedProduct(UPC code)
			throws SaleDiscountException {
		
		//if product code is in our cache return the product info
		if (productCache.containsKey(code.getCode())){
			return (PackagedProduct) productCache.get(code.getCode());
		}
		double discount = 0;
		Connection conn = null;
		PackagedProduct pp = null;
		try {
			conn = connectDB();

			Statement stmt = conn.createStatement();
			Statement stmt2 = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			stmt2.setQueryTimeout(queryTimeOut);
			String q = "select * from PackagedProduct natural join ProductCategory where UPC = '"
					+ code.getCode() + "'";
			String q2 = "select s.code, sum(percentReduction) as percentReduction, sum(flatReduction) as flatReduction "
					+ "from PackagedProduct p, Sale s "
					+ "where date('now') >= s.startDate and date('now') <= s.endDate and p.UPC=s.code and p.UPC='"
					+ code.getCode() + "'";
			ResultSet rs = stmt.executeQuery(q);
			ResultSet rs2 = stmt2.executeQuery(q2);
			if (rs.next()) {
				String descrip = rs.getString("descrip");
				double price = Double.parseDouble(rs.getString("price"));
				double weight = Double.parseDouble(rs.getString("weight"));
				double taxRate = Double.parseDouble(rs.getString("taxRate"));
				double percentReduction = 0;
				double flatReduction = 0;
				if (rs2.next()) {
					try {
						percentReduction = Double.parseDouble(rs2
								.getString("percentReduction"));
						flatReduction = Double.parseDouble(rs2
								.getString("flatReduction"));
					} catch (Exception ignore) {
					}
					discount = getDiscount(percentReduction, flatReduction,
							price);

				}
				pp = new PackagedProduct(descrip, code, price, weight, taxRate,
						discount);
				pp.setFlatRateDiscount(flatReduction);
			}
			rs.close();
			rs2.close();
			stmt.close();
			stmt2.close();
		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException ignore) {
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
			}
		}
		if (pp != null){
			//add this product to our cache
			productCache.put(code.getCode(), pp);
		}
		return pp;
	}

	/**
	 * Return the total discount from a price using a percent off and a flat
	 * reduction.
	 * 
	 * @param percentReduction
	 *            The percent off from the total price.
	 * @param flatReduction
	 *            The flat discount to take off the total price.
	 * @param price
	 *            The starting price.
	 * @return The total discount obtained from the percent off and the flat
	 *         reduction.
	 */
	private double getDiscount(double percentReduction, double flatReduction,
			double price) {
		double discount, PReduction;

		// apply straight reduction first and then %reduction on
		// the price.
		discount = flatReduction;
		PReduction = (price - flatReduction) * percentReduction;
		discount += PReduction;
		return discount;
	}

	/**
	 * Update ImpulseProducts table for every product in checkout cart. update
	 * the total bought and profit margin gained for that product
	 * 
	 * @param COO
	 *            is the checkout cart to look through SCOid is the self
	 *            checkout ID to update
	 * @throws SQLException
	 * 
	 */
	public void updateImpulseProducts(CheckOutCart COO, int SCOid)
			throws SQLException {

		PreparedStatement updateTotalBought = null;
		Connection conn = null;
		try {
			conn = connectDB();
			conn.setAutoCommit(false);

			Enumeration<GroceryItem> itemsInCart = COO.listItems();
			while (itemsInCart.hasMoreElements()) {
				GroceryItem info = itemsInCart.nextElement();

				String query1 = "update ImpulseProducts set totalBought = totalBought + 1, profitMargin = profitMargin + "
						+ "(profitMargin / totalBought) where code = ? AND SelfCheckoutID = ?";

				// updates Bulk products a bit different because of weight
				if (info.getInfo().getCode() instanceof BIC) {
					query1 = "update ImpulseProducts set totalBought = totalBought + "
							+ info.getWeight()
							+ ", profitMargin = profitMargin + ((profitMargin / totalBought)*"
							+ info.getWeight()
							+ ") where code = ? AND SelfCheckoutID = ?";
				}

				updateTotalBought = conn.prepareStatement(query1);
				// update table for every item in cart
				updateTotalBought.setString(1, info.getInfo().getCode()
						.getCode());
				updateTotalBought.setInt(2, SCOid);
				updateTotalBought.executeUpdate();
				conn.commit();
			}
		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException e) {
		} finally {
			if (updateTotalBought != null) {
				updateTotalBought.close();
			}
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
			}
		}

	}

	/**
	 * Undo the update to database by decreasing totalBought by 1, and
	 * decreasing profitMargin. This is used for testing purposes only as I want
	 * to reset database back to original state after testing
	 * updateImpulseProducts.
	 * 
	 * @throws SQLException
	 * 
	 */
	public void undoUpdateForTesting(CheckOutCart COO, int SCOid)
			throws SQLException {
		PreparedStatement updateTotalBought = null;
		Connection conn = null;
		try {
			conn = connectDB();
			conn.setAutoCommit(false);
			Enumeration<GroceryItem> itemsInCart = COO.listItems();
			while (itemsInCart.hasMoreElements()) {
				GroceryItem info = itemsInCart.nextElement();
				String query1 = "update ImpulseProducts set totalBought = totalBought - 1, profitMargin = profitMargin - "
						+ "(profitMargin / totalBought) where code = ? AND SelfCheckoutID = ?";

				// updates Bulk products a bit different because of weight
				if (info.getInfo().getCode() instanceof BIC) {
					query1 = "update ImpulseProducts set totalBought = totalBought - "
							+ info.getWeight()
							+ ", profitMargin = profitMargin - ((profitMargin / totalBought)*"
							+ info.getWeight()
							+ ") where code = ? AND SelfCheckoutID = ?";
				}
				updateTotalBought = conn.prepareStatement(query1);
				updateTotalBought.setString(1, info.getInfo().getCode()
						.getCode());
				updateTotalBought.setInt(2, SCOid);
				updateTotalBought.executeUpdate();
				conn.commit();
			}
		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException e) {
		} finally {
			if (updateTotalBought != null) {
				updateTotalBought.close();
			}
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
			}

		}
	}

	/**
	 * Return the total amount bought for an impulse buy product at a
	 * SelfCheckout stand. Used for testing purposes.
	 * 
	 * @param SCOid
	 *            the SelfCheckout stand ID that product is bought at
	 * @param productCode
	 *            the product code
	 * @return total amount that product was bought at SCOid
	 */
	public double getTotalBought(int SCOid, Code productCode) {
		double totalBought = 0;
		Connection conn = null;
		try {
			conn = connectDB();

			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			String query1 = "SELECT totalBought FROM ImpulseProducts where code = '"
					+ productCode.getCode()
					+ "' AND "
					+ "SelfCheckoutID = "
					+ SCOid;
			ResultSet rs = stmt.executeQuery(query1);
			if (rs.next()) {
				totalBought = Double.parseDouble(rs.getString("totalBought"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException e) {

		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}
		}
		return totalBought;
	}

	/**
	 * Return the total profit margin for an impulse buy product at a
	 * SelfCheckout stand. Used for testing purposes.
	 * 
	 * @param SCOid
	 *            the SelfCheckout stand ID that product is bought at
	 * @param productCode
	 *            the product code
	 * @return profit margin for the product at SCOid
	 */
	public double getProfitMargin(int SCOid, Code productCode) {
		double profitMargin = 0;
		Connection conn = null;
		try {
			conn = connectDB();

			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			String query1 = "SELECT profitMargin FROM ImpulseProducts where code = '"
					+ productCode.getCode()
					+ "' AND "
					+ "SelfCheckoutID = "
					+ SCOid;
			ResultSet rs = stmt.executeQuery(query1);
			if (rs.next()) {
				profitMargin = Double.parseDouble(rs.getString("profitMargin"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException ignore) {
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignore) {
			}
		}
		return profitMargin;
	}

	/*
	 * This is method queries username provided by user in database. If username
	 * is registered it returns User object.
	 * 
	 * @param String username : username to be queried.
	 * 
	 * @return object of User
	 */
	public User getUser(String username) {
		User user = new User();
		Connection conn = null;
		try {
			conn = connectDB();
			Statement stmt = conn.createStatement();
			ResultSet rs;
			stmt.setQueryTimeout(queryTimeOut);
			String query = "select * from UserAccounts where username='"
					+ username + "'";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				user.loginName = rs.getString("username");
				user.password = rs.getString("password");
				user.fname = rs.getString("name");
				user.privilege = rs.getInt("privilege");
			}
			rs.close();
			stmt.close();
		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException ignore) {
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}
		}
		return user;
	}

	/**
	 * Generate header for report that includes today's date and time.
	 * 
	 * @param filename
	 *            of report
	 */
	private void generaterHeaderToReport(String filename) {
		BufferedWriter output;
		String date = new Date().toString();

		try {
			output = new BufferedWriter(new FileWriter(filename, true));
			output.append("Report on Profit of Impulse Buy Products as of "
					+ date);
			output.newLine();
			output.newLine();
			output.append("============================================");
			output.newLine();
			output.append("============================================");
			output.newLine();
			output.newLine();
			output.close();
		} catch (IOException e) {

		}

	}

	/**
	 * Query for a ranking of the highest profitable products overall.
	 * 
	 * @return the sub-report, ranking items by highest profits.
	 */
	private String getHighestProfitImpulse() {
		String result = "Most Profitable Product Overall: ";
		int ranking = 2;
		Connection conn = null;
		try {
			conn = connectDB();

			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			String query1 = "SELECT descrip, profitMargin, SelfCheckoutID FROM ImpulseProducts ORDER BY profitMargin DESC";
			ResultSet rs = stmt.executeQuery(query1);
			if (rs.next()) {
				result += rs.getString("descrip")
						+ " sold with a profit margin of $" + rs.getDouble(2)
						+ " at Self-Checkout counter #" + rs.getInt(3)
						+ ".\n\n";
			}
			result += "Ranking for Highest Profit Overall: \n";
			while (ranking <= 20 && rs.next()) {
				result += ranking + ") " + rs.getString("descrip")
						+ " sold with a profit margin of $" + rs.getDouble(2)
						+ " at Self-Checkout counter #" + rs.getInt(3) + ".\n";
				ranking += 1;
			}
			rs.close();
			stmt.close();

		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException ignore) {
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignore) {
			}
		}
		return result;
	}

	/**
	 * Query for a ranking of the highest profitable products overall and append
	 * it to a report titled filename.
	 * 
	 * @param filename
	 *            is the name of the file to write the report to.
	 * 
	 */
	private void getHighestProfitToReport(String filename) {
		BufferedWriter output;
		int ranking = 2;
		Connection conn = null;
		try {
			conn = connectDB();

			output = new BufferedWriter(new FileWriter(filename, true));

			output.append("Most Profitable Product Overall: ");
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			String query1 = "SELECT descrip, profitMargin, SelfCheckoutID FROM ImpulseProducts ORDER BY profitMargin DESC";
			ResultSet rs = stmt.executeQuery(query1);
			if (rs.next()) {
				output.append(rs.getString("descrip")
						+ " sold with a profit margin of $" + rs.getDouble(2)
						+ " at Self-Checkout counter #" + rs.getInt(3) + ".");
			}
			output.newLine();
			output.newLine();
			output.append("Ranking for Highest Profit Overall: ");
			output.newLine();
			while (ranking <= 20 && rs.next()) {
				output.append(ranking + ") " + rs.getString("descrip")
						+ " sold with a profit margin of $" + rs.getDouble(2)
						+ " at Self-Checkout counter #" + rs.getInt(3) + ".");
				output.newLine();
				ranking += 1;
			}

			output.newLine();
			output.append("============================================");
			rs.close();
			stmt.close();
			output.close();
		} catch (IOException ignore) {
		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException ignore) {
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {

			}
		}
	}

	/**
	 * Query for a ranking of the most sold products overall.
	 * 
	 * @return the sub-report, ranking items by with most sold.
	 */
	private String getMostSoldImpulse() {
		String result = "Most Sold Overall: ";
		int ranking = 2;
		Connection conn = null;
		try {

			conn = connectDB();

			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			String query1 = "SELECT descrip, totalBought, SelfCheckoutID FROM ImpulseProducts ORDER BY totalBought DESC";
			ResultSet rs = stmt.executeQuery(query1);
			if (rs.next()) {
				result += rs.getString("descrip") + " sold " + rs.getDouble(2)
						+ " units at Self-Checkout counter #" + rs.getInt(3)
						+ ".\n\n";
			}
			result += "Ranking for Most Sold Overall: \n";
			while (ranking <= 20 && rs.next()) {
				result += ranking + ") " + rs.getString("descrip") + " sold "
						+ rs.getDouble(2) + " units at Self-Checkout counter #"
						+ rs.getInt(3) + ".\n";
				ranking += 1;
			}
			rs.close();
			stmt.close();

		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException ignore) {
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {

			}
		}

		return result;
	}

	/**
	 * Query for a ranking of the most sold products overall and append it to a
	 * report titled filename.
	 * 
	 * @param filename
	 *            is the name of the file to write the report to.
	 * 
	 */
	private void getMostSoldToReport(String filename) {
		BufferedWriter output;
		int ranking = 2;
		Connection conn = null;
		try {
			conn = connectDB();
			output = new BufferedWriter(new FileWriter(filename, true));
			output.newLine();
			output.newLine();
			output.append("Most Sold Overall: ");
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			String query1 = "SELECT descrip, totalBought, SelfCheckoutID FROM ImpulseProducts ORDER BY totalBought DESC";
			ResultSet rs = stmt.executeQuery(query1);
			if (rs.next()) {
				output.append(rs.getString("descrip") + " sold "
						+ rs.getDouble(2) + " units at Self-Checkout counter #"
						+ rs.getInt(3) + ".");
			}
			output.newLine();
			output.newLine();
			output.append("Ranking for Most Sold Overall: ");
			output.newLine();
			while (ranking <= 20 && rs.next()) {
				output.append(ranking + ") " + rs.getString("descrip")
						+ " sold " + rs.getDouble(2)
						+ " units at Self-Checkout counter #" + rs.getInt(3)
						+ ".");
				output.newLine();
				ranking += 1;
			}

			output.newLine();
			output.append("============================================");
			rs.close();
			stmt.close();
			output.close();
		} catch (IOException ignore) {
		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException ignore) {
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignore) {
			}
		}
	}

	/**
	 * Query for a ranking of the "fastest money making" products.
	 * "fastest money making" is determined by: (profit margin of item) / (# of
	 * days item has be sold)
	 * 
	 * @return the sub-report, ranking items by fastest money making.
	 */
	private String getFastestMoneyMakingImpulse() {
		String result = "Fastest Money Making Product Overall: ";
		int ranking = 2;
		Connection conn = null;
		try {
			conn = connectDB();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			String query1 = "SELECT (julianday(date('now')) - julianday(started)), descrip, "
					+ "(profitMargin / (julianday(date('now')) - julianday(started))), "
					+ "SelfCheckoutID FROM ImpulseProducts ORDER BY  "
					+ "(profitMargin / (julianday(date('now')) - julianday(started))) DESC";
			ResultSet rs = stmt.executeQuery(query1);
			if (rs.next()) {
				result += rs.getString("descrip")
						+ " has an average profit margin of $"
						+ rs.getDouble(3)
						+ " per day since it started selling "
						+ rs.getDouble(1)
						+ " days ago at Self-Checkout counter #" + rs.getInt(4)
						+ ".\n\n";
			}
			result += "Ranking for Fastest Money Making: \n";
			while (ranking <= 20 && rs.next()) {
				result += ranking + ") " + rs.getString("descrip")
						+ " has an average profit margin of $"
						+ rs.getDouble(3)
						+ " per day since it started selling "
						+ rs.getDouble(1)
						+ " days ago at Self-Checkout counter #" + rs.getInt(4)
						+ ".\n";
				ranking += 1;
			}
			rs.close();
			stmt.close();

		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException ignore) {
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignore) {
			}
		}
		return result;
	}

	/**
	 * Query for a ranking of the fastest money making product overall and
	 * append it to a report titled filename.
	 * 
	 * "fastest money making" is determined by: (profit margin of item) / (# of
	 * days item has be sold)
	 * 
	 * @param filename
	 *            is the name of the file to write the report to.
	 * 
	 */
	private void getFastestMoneyMakingToReport(String filename) {
		BufferedWriter output;
		int ranking = 2;
		Connection conn = null;
		try {
			conn = connectDB();
			output = new BufferedWriter(new FileWriter(filename, true));
			output.newLine();
			output.newLine();
			output.append("Fastest Money Making Product Overall: ");
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			String query1 = "SELECT (julianday(date('now')) - julianday(started)), descrip, "
					+ "(profitMargin / (julianday(date('now')) - julianday(started))), "
					+ "SelfCheckoutID FROM ImpulseProducts ORDER BY  "
					+ "(profitMargin / (julianday(date('now')) - julianday(started))) DESC";

			ResultSet rs = stmt.executeQuery(query1);
			if (rs.next()) {
				output.append(rs.getString("descrip")
						+ " has an average profit margin of $"
						+ rs.getDouble(3)
						+ " per day since it started selling "
						+ rs.getDouble(1)
						+ " days ago at Self-Checkout counter #" + rs.getInt(4)
						+ ".");
			}
			output.newLine();
			output.newLine();
			output.append("Ranking for Fastest Money Making Products: ");
			output.newLine();
			while (ranking <= 20 && rs.next()) {
				output.append(ranking + ") " + rs.getString("descrip")
						+ " has an average profit margin of $"
						+ rs.getDouble(3)
						+ " per day since it started selling "
						+ rs.getDouble(1)
						+ " days ago at Self-Checkout counter #" + rs.getInt(4)
						+ ".");
				output.newLine();
				ranking += 1;
			}

			output.newLine();
			output.append("============================================");
			rs.close();
			stmt.close();
			output.close();
		} catch (IOException e) {
		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException ignore) {
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignore) {
			}
		}
	}

	/**
	 * Query for a ranking of the fastest selling products. fastest selling is
	 * determined by: (total sold) / (# of days item has be sold)
	 * 
	 * @return the sub-report, ranking items by fastest money making.
	 */
	private String getFastestSellingImpulse() {
		String result = "Fastest Selling Product Overall: ";
		int ranking = 2;
		Connection conn = null;
		try {
			conn = connectDB();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			String query1 = "SELECT (julianday(date('now')) - julianday(started)), descrip, "
					+ "(totalBought / (julianday(date('now')) - julianday(started))), SelfCheckoutID FROM ImpulseProducts "
					+ "ORDER BY  (totalBought / (julianday(date('now')) - julianday(started))) DESC";
			ResultSet rs = stmt.executeQuery(query1);
			if (rs.next()) {
				result += rs.getString("descrip") + " has sold on average "
						+ rs.getDouble(3)
						+ " units per day since it started selling "
						+ rs.getDouble(1)
						+ " days ago at Self-Checkout counter #" + rs.getInt(4)
						+ ".\n\n";
			}
			result += "Ranking for Fastest Selling Products: \n";
			while (ranking <= 20 && rs.next()) {
				result += ranking + ") " + rs.getString("descrip")
						+ " has sold on average " + rs.getDouble(3)
						+ " units per day since it started selling "
						+ rs.getDouble(1)
						+ " days ago at Self-Checkout counter #" + rs.getInt(4)
						+ ".\n";
				ranking += 1;
			}
			rs.close();
			stmt.close();

		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException ignore) {
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignore) {
			}
		}
		return result;
	}

	/**
	 * Query for a ranking of the selling product overall and append it to a
	 * report titled filename.
	 * 
	 * fastest selling is determined by: (profit margin of item) / (# of days
	 * item has be sold)
	 * 
	 * @param filename
	 *            is the name of the file to write the report to.
	 * 
	 */
	private void getFastestSellingToReport(String filename) {
		BufferedWriter output;
		int ranking = 2;
		Connection conn = null;
		try {
			conn = connectDB();
			output = new BufferedWriter(new FileWriter(filename, true));
			output.newLine();
			output.newLine();
			output.append("Fastest Selling Product Overal: ");
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			String query1 = "SELECT (julianday(date('now')) - julianday(started)), descrip, "
					+ "(totalBought / (julianday(date('now')) - julianday(started))), SelfCheckoutID FROM ImpulseProducts "
					+ "ORDER BY  (totalBought / (julianday(date('now')) - julianday(started))) DESC";

			ResultSet rs = stmt.executeQuery(query1);
			if (rs.next()) {
				output.append(rs.getString("descrip") + " has sold on average "
						+ rs.getDouble(3)
						+ " units per day since it started selling "
						+ rs.getDouble(1)
						+ " days ago at Self-Checkout counter #" + rs.getInt(4)
						+ ".");
			}
			output.newLine();
			output.newLine();
			output.append("Ranking for Fastest Selling Products: ");
			output.newLine();
			while (ranking <= 20 && rs.next()) {
				output.append(ranking + ") " + rs.getString("descrip")
						+ " has sold on average " + rs.getDouble(3)
						+ " units per day since it started selling "
						+ rs.getDouble(1)
						+ " days ago at Self-Checkout counter #" + rs.getInt(4)
						+ ".");
				output.newLine();
				ranking += 1;
			}

			output.newLine();
			output.append("============================================");
			rs.close();
			stmt.close();
			output.close();
		} catch (IOException e) {
		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException ignore) {
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignore) {
			}
		}
	}

	/**
	 * Generates a report. The report has information on: Rank items by which
	 * has the largest profit margin total regardless of date, Rank items by
	 * which sold the most regardless of date, Rank items by which makes the
	 * most money given the date that item started selling, i.e. (profit margin
	 * of item) / (todayDate - startDate of item) Rank items by which sold the
	 * most given the date that item started selling, i.e (total sold /
	 * (todayDate - startDate of item)
	 */
	public String generateReport() {
		String highestProfit = this.getHighestProfitImpulse();
		String mostSold = this.getMostSoldImpulse();
		String fastestMoneyMaking = this.getFastestMoneyMakingImpulse();
		String fastestSelling = this.getFastestSellingImpulse();
		String report = "";
		String date = new Date().toString();

		report += "Report on Profit of Impulse Buy Products as of " + date
				+ "\n" + "============================================\n"
				+ "============================================\n\n"
				+ highestProfit + "\n"
				+ "============================================\n\n" + mostSold
				+ "\n" + "============================================\n\n"
				+ fastestMoneyMaking + "\n"
				+ "============================================\n\n"
				+ fastestSelling + "\n";

		return report;
	}

	public boolean generateReportToFile(String filename) {

		try {

			PrintWriter out = new PrintWriter(filename);
			this.generaterHeaderToReport(filename);
			this.getHighestProfitToReport(filename);
			this.getMostSoldToReport(filename);
			this.getFastestMoneyMakingToReport(filename);
			this.getFastestSellingToReport(filename);
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	/**
	 * Return every row in a SQLite table.
	 * 
	 * @return The ResultSet that contains every row in the SQLite table.
	 */
	public ResultSet listTable(String tableName) {
		Connection conn = null;
		try {
			conn = connectDB();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			String q = "select * from " + tableName;
			ResultSet rs = stmt.executeQuery(q);
			return rs;
		} catch (SQLException ignore) {
		} catch (DatabaseConnectionException ignore) {
		} 
		return null;
	}

	/**
	 * Check to see if the id in product category is safe to remove. id is safe
	 * to remove if it is not referred to in another table.
	 * 
	 * @param category
	 *            id being checked
	 * @return boolean True iff the category is not used by any products.
	 */
	public boolean isSafeToRemoveFromProductCategory(String category) {
		Connection conn = null;
		try {
			conn = connectDB();
			// create statements to hold queries
			Statement stmt = conn.createStatement();
			Statement stmt2 = conn.createStatement();
			stmt.setQueryTimeout(queryTimeOut);
			stmt2.setQueryTimeout(queryTimeOut);
			// query BulkProduct for products that have the same category as id
			String bulkQuery = "select * from BulkProduct where category = '"
					+ category + "';";
			// query PackagedProduct for products that have same category as id
			String packagedQuery = "select * from PackagedProduct where category = '"
					+ category + "';";
			ResultSet rs = stmt.executeQuery(bulkQuery);

			// if both result sets are empty
			if (!rs.next()) {
				ResultSet rs2 = stmt.executeQuery(packagedQuery);
				if (!rs2.next()) {
					// if both are empty, then id is not used by any products
					return true;
				}
				rs2.close();
			}
			rs.close();
			stmt.close();
			stmt2.close();
		} catch (SQLException e) {
		} catch (DatabaseConnectionException ignore) {
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignore) {
			}
		}
		// id is not safe to remove if either result set is nonempty
		return false;
	}

	/**
	 * Look up an item by category and return true if it exists in
	 * ProductCategory table. Throws SQLException if query failed to execute.
	 * 
	 * @param category
	 *            category being looked up
	 * @return boolean True iff category exists in ProductCategory table
	 * @throws Exception
	 *             Uses customised error message.
	 */
	public boolean lookUpCategory(String category) throws SQLException {
		boolean retVal = false;
		Connection conn = null;
		try {
			conn = connectDB();
			String sqlText = "select * from ProductCategory where category = ?";
			PreparedStatement ps = conn.prepareStatement(sqlText);
			ps.setString(1, category);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) { // query returned a non-empty result
				retVal = true;
			}
			ps.close(); // close prepared statement and result set
			rs.close();
		} catch (SQLException e) {
			throw new SQLException("Failed to update database, contact"
					+ " nearest database administrator.");
		} catch (DatabaseConnectionException ignore) {
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignore) {
			}
		}
		return retVal;
	}

	/**
	 * Return all products as a string in the format "Code:Description"
	 * 
	 * @return Vector<String> A Vector of products descriptions.
	 */
	public Vector<String> getAllProducts() {
		Vector<String> products = new Vector<String>();

		ResultSet bulkProductRS = this.listTable(Table.BulkProduct.toString());

		try {
			while (bulkProductRS.next()) {
				products.add(bulkProductRS.getString(1) + ": "
						+ bulkProductRS.getString(2));
			}
			bulkProductRS.close();
		} catch (SQLException ignore) {
		}

		ResultSet packagedProductRS = this.listTable(Table.PackagedProduct
				.toString());

		try {
			while (packagedProductRS.next()) {
				products.add(packagedProductRS.getString(1) + ": "
						+ packagedProductRS.getString(2));
			}
			packagedProductRS.close();
		} catch (SQLException ignore) {
		}

		return products;
	}

	/**
	 * Return all Tax Categories as a string.
	 * 
	 * @return Vector<String> A Vector of Tax Categories.
	 */
	public Vector<String> getAllTaxCategories() {
		Vector<String> categories = new Vector<String>();

		ResultSet productCategoryRS = this.listTable(Table.ProductCategory
				.toString());

		try {
			while (productCategoryRS.next()) {
				categories.add(productCategoryRS.getString(1));
			}
			productCategoryRS.close();
		} catch (SQLException ignore) {
		}
		return categories;
	}
}
