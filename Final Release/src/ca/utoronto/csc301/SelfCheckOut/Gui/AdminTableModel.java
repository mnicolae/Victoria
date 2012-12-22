package ca.utoronto.csc301.SelfCheckOut.Gui;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import ca.utoronto.csc301.SelfCheckOut.App.BIC;
import ca.utoronto.csc301.SelfCheckOut.App.Database;
import ca.utoronto.csc301.SelfCheckOut.App.ProductInfo;
import ca.utoronto.csc301.SelfCheckOut.App.UPC;
import ca.utoronto.csc301.SelfCheckOut.App.Database.Table;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidBICException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidUPCException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountException;

/**
 * A table model for the Administrator View. An instance of AdminTableModel will
 * populate the GUI with the rows and columns from a database SQLite table.
 * 
 */
public class AdminTableModel extends DefaultTableModel {

	/**
	 * Maps abbreviated SQLite table column names to a user friendly name.
	 */
	private Map<String, String> userFriendlyColumnNames;

	/**
	 * The SQLite table that is being modeled.
	 */
	private Table table;

	/**
	 * The GUI for the self check out administrator view.
	 */
	private SelfCheckOutAdministratorView gui;
	/**
	 * The database of the self check out machine.
	 */
	private Database db;

	/**
	 * The names of each column in the SQLite table being represented.
	 */
	private Vector<String> tableColumnNames;

	/**
	 * The location of the description column for the table Sale.
	 */
	private final int SALE_DESCRIP_COLUMN_NUM = 2;

	/**
	 * The expected length of a BIC code.
	 */
	private final int BIC_CODE_LENGTH = 5;
	/**
	 * The expected length of a UPC code.
	 */
	private final int UPC_CODE_LENGTH = 12;
	/**
	 * The location of the key for a given row in the table.
	 */
	private final int KEY_COLUMN_NUM = 0;
	/**
	 * The column number for tax rate in table Product Category.
	 */
	private final int TAXRATE_COLUMM_NUM = 1;

	/**
	 * The column number for percent discount column in table Sale.
	 */
	private final int PERCENT_DISC_COLUMN_NUM = 5;

	/**
	 * The column number for flat rate discount column in table Sale.
	 */
	private final int FLATRATE_DISC_COLUMN_NUM = 6;

	/**
	 * The column number for percent discount column in table Sale.
	 */
	private final int STARTDATE_COLUMN_NUM = 3;

	/**
	 * The column number for flat rate discount column in table Sale.
	 */
	private final int ENDDATE_COLUMN_NUM = 4;
	/**
	 * A boolean variable indicating whether an administrator or an employee 
	 * is accessing this AdminView. 
	 * 
	 * privilege is true if administrator, false if employee
	 */
	private boolean privilege;
	
	/**
	 * The last search term used on this table.
	 */
	private String lastSearchTerm = "";
	/**
	 * A table model for the Administrator GUI.
	 */
	public AdminTableModel(SelfCheckOutAdministratorView gui, Table table,
			Database db) {
		super();
		this.table = table;
		this.db = db;
		this.gui = gui;
		this.tableColumnNames = new Vector<String>();
		mapColumnNames();
		generateTableRows();
	}

	/**
	 * A table model for the Administrator GUI.
	 */
	public AdminTableModel(SelfCheckOutAdministratorView gui, Table table,
			Database db, boolean priv) {
		super();
		this.table = table;
		this.db = db;
		this.gui = gui;
		this.tableColumnNames = new Vector<String>();
		this.privilege = priv;
		mapColumnNames();
		generateTableRows();
	}

	/**
	 * Fill the table model with rows from a SQLite table.
	 */
	private void generateTableRows() {
		this.columnIdentifiers.clear();
		this.dataVector.clear();
		this.tableColumnNames.clear();
		try {
			ResultSet rs = db.listTable(table.toString());
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int col = 1; col <= rsmd.getColumnCount(); col++) {

				String colName = rsmd.getColumnName(col);
				tableColumnNames.add(colName);
				if (userFriendlyColumnNames.containsKey(colName)) {
					colName = userFriendlyColumnNames.get(colName);
				}
				this.columnIdentifiers.add(colName);
			}
			// iterate through each row in the database
			while (rs.next()) {
				Vector<Object> row = new Vector<Object>();
				// parse each column in a row
				for (int col = 1; col <= rsmd.getColumnCount(); col++) {
					
					if ((table == Table.ProductCategory && col == TAXRATE_COLUMM_NUM + 1)
							|| (table == Table.Sale && col == 5)) {
						//certain columns should be converted to percent dd%
						row.add(Double.parseDouble(rs.getString(col)) * 100);
						
					} else if (this.columnIdentifiers.get(col - 1).equals(
							"Taxable Category") &&  table != Table.ProductCategory) {
						//columns that allow taxable category to be editted should use drop down list
						//JTable t = new JTable(this);
						//new DefaultCellEditor(getTaxCategoryDropDownList(rs.getString(col)))); 
						row.add(rs.getString(col));
					} else {
						row.add(rs.getString(col));
					}
				}
				this.addRow(row);
			}
		rs.close();
		} catch (SQLException ignore) {
		}
		if (table == Table.Sale) {
			addDescriptionColumnToSale();
		}
	}
	/**
	 * Insert a column to the Sale table for description of products.
	 */
	private void addDescriptionColumnToSale() {

		this.columnIdentifiers.insertElementAt("Description",
				SALE_DESCRIP_COLUMN_NUM);
		this.tableColumnNames.insertElementAt("descrip",
				SALE_DESCRIP_COLUMN_NUM);
		for (int rowNum = 0; rowNum < dataVector.size(); rowNum++) {
			Vector<Object> row = (Vector<Object>) dataVector.get(rowNum);
			String code = row.get(1).toString();
			String descrip = getDescription(code);
			row.insertElementAt(descrip, SALE_DESCRIP_COLUMN_NUM);
		}
	}

	/**
	 * Return the description of a Product.
	 * 
	 * @return String The description of the product.
	 */
	private String getDescription(String code) {
		try {
			if (code.length() == BIC_CODE_LENGTH) {
				BIC bic = new BIC(code);
				return db.lookUpItem(bic).getDescription();
			} else if (code.length() == UPC_CODE_LENGTH) {
				UPC upc = new UPC(code);
				return db.lookUpItem(upc).getDescription();
			}
		} catch (Exception ignore) {
		}
		return "Product Missing";
	}

	/**
	 * Map abbreviated SQLite table column names to a user friendly name.
	 */
	private void mapColumnNames() {
		userFriendlyColumnNames = new HashMap<String, String>();
		userFriendlyColumnNames.put("descrip", "Description");
		userFriendlyColumnNames.put("category", "Taxable Category");
		userFriendlyColumnNames.put("taxRate", "Tax Rate (%)");
		userFriendlyColumnNames.put("startDate", "Start Of Sale (YYYY-MM-DD)");
		userFriendlyColumnNames.put("endDate", "End of Sale (YYYY-MM-DD)");
		userFriendlyColumnNames.put("percentReduction", "Percent Discount (%)");
		userFriendlyColumnNames.put("flatReduction", "Flat Rate Discount ($)");
		userFriendlyColumnNames.put("code", "Product Code");
		userFriendlyColumnNames.put("wholesalePrice", "Whole Sale Price ($)");
		userFriendlyColumnNames.put("unitPrice", "Unit Price ($)");
		userFriendlyColumnNames.put("saleID", "Sale ID");
		userFriendlyColumnNames.put("price", "Price ($)");
		userFriendlyColumnNames.put("weight", "Weight");
	}

	/**
	 * Return true if a cell is editable.
	 * 
	 * @param row
	 *            The row number of the cell.
	 * @param col
	 *            The column number of the cell.
	 */
	public boolean isCellEditable(int row, int col) {
		// no cell can be edited if the user is an employee
		if (privilege == false) {
			return false;
		}
		
		// Note that the data/cell address is constant,
		// no matter where the cell appears on screen.
		if (table == Table.Sale) {
			return col > SALE_DESCRIP_COLUMN_NUM;
		}
		return col > KEY_COLUMN_NUM;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#setValueAt(java.lang.Object,
	 * int, int)
	 */
	public void setValueAt(Object newVal, int row, int column) {

		if (newVal == null)
			return;

		Vector<Object> rowContents = ((Vector<Object>) this.dataVector
				.elementAt(row));
		Object cell = rowContents.elementAt(column);
		// only update if a change has occurred in the cell
		if (cell == null || !cell.toString().trim().equals(newVal.toString().trim())) {
			try {
				// throws Exception detailing if the update failed
				updateDatabase(newVal, rowContents, column);

				// reset the view to match the model
				String columnAttribute = userFriendlyColumnNames
						.get(tableColumnNames.get(column));
				String key = rowContents.get(0).toString();

				this.gui.resetTabs("Successfully updated \"" + columnAttribute
						+ "\" of \"" + key + "\" from "
						+ (cell != null ? cell.toString() : "null") + " to "
						+ newVal, table.toString(), lastSearchTerm);
			} catch (Exception e) {
				// output the customized error messages
				this.gui.resetTabs(e.getMessage(), table.toString(), lastSearchTerm);
			}
		}
	}

	/**
	 * If a change is made to a cell in the GUI table, update the corresponding
	 * cell in the database. Throw Exception if change breaks database
	 * constraints (useful for exception.getMessage()).
	 * 
	 * @param newVal
	 *            The changed value.
	 * @param row
	 *            The row to be updated in the database.
	 * @param column
	 *            The column of the row to be updated in the database.
	 * @throws Exception
	 *             Throw customized error message that details the error in
	 *             updating the database.
	 */
	private void updateDatabase(Object newVal, Vector row, int column)
			throws Exception {

		validateRow(newVal, row, column);

		String tableName = table.toString();
		String columnAttribute = tableColumnNames.get(column);
		String keyName = tableColumnNames.get(KEY_COLUMN_NUM);
		String key = row.get(KEY_COLUMN_NUM).toString();
		String updatedVal = newVal.toString();
		if ((table == Table.ProductCategory && column == TAXRATE_COLUMM_NUM)
				|| (table == Table.Sale && column == 5)) {
			updatedVal = (Double.parseDouble(updatedVal) / 100) + "";
		}
		// throws SQLException
		db.updateDatabase("update " + tableName + " set " + columnAttribute
				+ " = '" + updatedVal + "' where " + keyName + " = \"" + key
				+ "\";");
	}

	/**
	 * Validate a column attribute for a row. Throw Exception if change breaks
	 * database constraints (useful for exception.getMessage()).
	 * 
	 * @param newVal
	 *            The changed value.
	 * @param row
	 *            The row to be validated.
	 * @param column
	 *            The column of the row to be validated.
	 * @throws Exception
	 *             Throw customized error message detailing why validation
	 *             failed.
	 */
	private void validateRow(Object newVal, Vector row, int column)
			throws Exception {

		switch (table) {
		case ProductCategory:
			validateProductCategory(newVal, row, column);
			break;
		case Sale:
			validateSale(newVal, row, column);
			break;
		case BulkProduct:
			validateBulkProduct(newVal, row, column);
			break;
		case PackagedProduct:
			validatePackagedProduct(newVal, row, column);
			break;
		default:
			// sqlite table not recognized
			throw new Exception(
					"SQLite Table not recognised, contact the vendor.");
		}
	}

	/**
	 * Validate a Packaged Product row, with a new value for a column attribute.
	 * Throw Exception if change breaks PackagedProduct table constraints
	 * (useful for exception.getMessage())
	 * 
	 * @param newVal
	 *            The changed value.
	 * @param row
	 *            The row to be validated.
	 * @param column
	 *            The column of the row to be validated.
	 * @throws Exception
	 *             Throw customized error message detailing why validation
	 *             failed.
	 */
	private void validatePackagedProduct(Object newVal, Vector row, int column)
			throws Exception {

		if (table != Table.PackagedProduct) {
			throw new Exception("Incorrect table (validatePackagedProduct), "
					+ " yell at programmer.");
		} else if (newVal == null) {
			throw new Exception("New value should not be empty"
					+ "(validatePackagedProduct)");
		}
		// add breaks
		switch (column) {
		case 1: // Description column
			break;
		case 2: // Price column
			validatePositiveDouble(newVal.toString());
			break;
		case 3: // Weight column
			validatePositiveDouble(newVal.toString());
			break;
		case 4: // Taxable category column
			if (!db.lookUpCategory(newVal.toString())) {
				throw new Exception("Category does not exist in Tax Rate table");
			}
			break;
		case 5: // Whole Sale Price column
			validatePositiveDouble(newVal.toString());
			break;
		default:
			// UPC column or invalid column value (when new columns are added)
			throw new Exception("General exception (validatePackagedProduct)");
		}
	}

	/**
	 * Validate a Bulk Product row, with a new value for a column attribute.
	 * Throw Exception if change breaks BulkProduct table constraints (useful
	 * for exception.getMessage()).
	 * 
	 * @param newVal
	 *            The changed value.
	 * @param row
	 *            The row to be validated.
	 * @param column
	 *            The column of the row to be validated.
	 * @throws Exception
	 *             Throw customized error message detailing why validation
	 *             failed.
	 */
	private void validateBulkProduct(Object newVal, Vector row, int column)
			throws Exception {

		if (table != Table.BulkProduct) {
			throw new Exception("Incorrect table (validateBulkProduct), yell"
					+ " at programmer.");
		} else if (newVal == null) {
			throw new Exception("New value should not be empty"
					+ "(validateBulkProduct)");
		}

		switch (column) {
		case 1: // Description column
			break;
		case 2: // Unit price column
			validatePositiveDouble(newVal.toString());
			break;
		case 3: // Taxable category column
			if (!db.lookUpCategory(newVal.toString())) {
				throw new Exception("Category does not exist in Tax Rate table");
			}
			break;
		case 4: // Whole Sale Price column
			validatePositiveDouble(newVal.toString());
			break;
		default:
			// BIC column or invalid column value (when new columns are added)
			throw new Exception("General exception (validateBulkProduct)");
		}
	}

	/**
	 * Validate a Sale row, with a new value for a column attribute. Throw
	 * Exception if change breaks Sale table constraints (useful for
	 * exception.getMessage()).
	 * 
	 * @param newVal
	 *            The changed value.
	 * @param row
	 *            The row to be validated.
	 * @param column
	 *            The column of the row to be validated.
	 * @throws Exception
	 *             Throw customized error message detailing why validation
	 *             failed.
	 */
	private void validateSale(Object newVal, Vector row, int column)
			throws Exception {

		if (table != Table.Sale) {
			throw new Exception("Incorrect table (validateSale), yell at"
					+ " programmer.");
		} else if (newVal == null) {
			throw new Exception("New value should not be empty (validateSale)");
		} else if (column <= SALE_DESCRIP_COLUMN_NUM) {
			// The attributes Sale ID, Code and Description (columns 0, 1, and 2
			// respectively) should
			// not be edited.
			throw new Exception(
					"Sale ID, Code and Description values should not be edited replace the row by removing it and adding a new row");
		} else if (column == STARTDATE_COLUMN_NUM
				|| column == ENDDATE_COLUMN_NUM) {
			String s = newVal.toString();
			// Date format expected YYYY-MM-DD for columns start date and end
			// date
			if (!s.matches("([0-9]{4})-(0[1-9]|1[0-2])-"
					+ "(0[1-9]|[1-2][0-9]|3[0-1])")) {
				throw new Exception("Date should be in YYYY-MM-DD format");
			}
			// TODO check if changing date of discount will cause
			// total discount to exceed base price
			// return;
		} else if (column == PERCENT_DISC_COLUMN_NUM) {
			// Column percent off should be a percentage.
			validateRate(newVal.toString());
			// return;
		} else if (column == FLATRATE_DISC_COLUMN_NUM) {
			// Column flat rate discount should be positive and should not
			// exceed the products base price.
			try {
				double newFlatRateDisc = Double.parseDouble(newVal.toString());

				// discount rate is negative
				if (newFlatRateDisc < 0) {
					throw new Exception("Flat rate discount should be a"
							+ " positive number");
				}

				// code and oldFlateRateDisc are valid by database constraint
				String code = row.get(1).toString();
				double oldFlatRateDisc = Double.parseDouble(row.get(column)
						.toString());
				ProductInfo info = null;

				if (code.length() == BIC_CODE_LENGTH) {
					BIC bic = new BIC(code);
					info = db.lookUpItem(bic);

				} else if (code.length() == UPC_CODE_LENGTH) {
					UPC upc = new UPC(code);
					info = db.lookUpItem(upc);
				}

				// info shouldn't be null, but catch exception just in case
				double newDiscount = info.getFlatRateDiscount()
						- oldFlatRateDisc + newFlatRateDisc;
				if (newDiscount > info.getPrice()) {
					throw new Exception("Flat rate discount should not"
							+ " exceed the price of the product");
				}

			} catch (NumberFormatException nfe) {
				throw new NumberFormatException("Flat rate discount should be"
						+ " a positive number");
			} catch (NullPointerException npe) {
				throw new NullPointerException(
						"Applying sale on a product"
								+ " that could not be find; Does this product exist any more? If not please remove "
								+ " sale entry or add the product back to the system.");
			} catch (SaleDiscountException sde) {
				throw new SaleDiscountException(
						"Selected entry's total "
								+ " discount has already exceeded price of the product.");
			}
		}

		// this exception will be thrown when new columns are added
		// to Sale and user edits cell under that column
		// NOTE: uncomment "return;" if this is uncommented
		// throw new Exception ("General exception (validateSale)");
	}

	/**
	 * Validate a ProductCategory row, with a new value for a column attribute.
	 * Throw Exception if change breaks ProductCategory table constraints
	 * (useful for exception.getMessage()).
	 * 
	 * @param newVal
	 *            The changed value.
	 * @param row
	 *            The row to be validated.
	 * @param column
	 *            The column of the row to be validated.
	 * @throws Exception
	 *             Throw customized error message detailing why validation
	 *             failed.
	 */
	private void validateProductCategory(Object newVal, Vector row, int column)
			throws Exception {

		if (table != Table.ProductCategory) {
			throw new Exception("Incorrect table (validateProductCategory),"
					+ " contact the vendor.");
		} else if (newVal == null) {
			throw new Exception("New value should not be empty"
					+ " (validateProductCategory)");
		} else if (column == TAXRATE_COLUMM_NUM) {
			validateRate(newVal.toString());
		}
	}

	/**
	 * Add a new row with key as textID if textID is a valid key.
	 * 
	 * @param textID
	 *            The key for the new row.
	 */
	public void addRow(String textID) {

		if (textID.isEmpty()) {
			return;
		}

		String outputMsg = "";

		try {
			switch (table) {
			case ProductCategory:
				db.updateDatabase("insert into ProductCategory values ('"
						+ textID + "', 0.00);");
				break;

			case BulkProduct:
				BIC bic;
				bic = new BIC(textID);
				db.updateDatabase("insert into BulkProduct values ('"
						+ bic.getCode()
						+ "', 'No description', 0.00, 'veggie', 0.00);");
				break;

			case PackagedProduct:
				UPC upc;
				upc = new UPC(textID);
				db.updateDatabase("insert into PackagedProduct values ('"
						+ upc.getCode() + "', 'No description', 0.00,"
						+ " 0.00, 'veggie', 0.00);");
				break;

			case Sale:
				// sales for a packagedProduct
				if (textID.length() == 12) {
					UPC upcSales;
					upcSales = new UPC(textID);
					if (db.lookUpItem(upcSales) == null) {
						outputMsg = "Packaged product does not exist"
								+ " in database";
						break;
					}
					// TODO check if adding the new discount will cause
					// total discount to exceed base price
					db.updateDatabase("insert into Sale values(null, '"
							+ upcSales.getCode()
							+ "' ,'0000-01-01','0000-01-01',0.0 ,0)");

					// sales for a bulk product
				} else if (textID.length() == 5) {
					BIC bicSales;
					bicSales = new BIC(textID);
					if (db.lookUpItem(bicSales) == null) {
						outputMsg = "Bulk product does not exist"
								+ " in database";
						break;
					}
					// TODO check if adding the new discount will cause
					// total discount to exceed base price
					db.updateDatabase("insert into Sale values(null, '"
							+ bicSales.getCode()
							+ "' ,'0000-01-01','0000-01-01',0.0 ,0)");
				} else {
					outputMsg = "Invalid product code, should be in either BIC format or UPC format";
				}
				break;
			}

		} catch (InvalidBICException e) {
			outputMsg = e.getMessage();
		} catch (InvalidUPCException e) {
			outputMsg = e.getMessage();
		} catch (SQLException e) {
			outputMsg = e.getMessage();
		} catch (SaleDiscountException sde) {
			outputMsg = "The total discount of the inputted product ID"
					+ " has already exceeded base price";
		}
		gui.resetTabs(outputMsg, table.toString(), lastSearchTerm);
	}

	/**
	 * Remove rows determined by toRemove.
	 * 
	 * @param toRemove
	 *            a List of strings that contain an ID to specify which rows to
	 *            remove
	 * 
	 *            NOTE: CAREFUL WHEN REMOVING, DATABASE CONSTRAINT NOT
	 *            GUARANTEED
	 */
	public void removeRows(List<String> toRemove) {

		if (toRemove.size() <= 0) { // nothing to remove
			return;
		}

		String outputMsg = "";
		List<String> SQLqueries = new ArrayList<String>();

		switch (table) {
		case ProductCategory:
			// TO-DO Jackson?
			for (String id : toRemove) {
				// need to first check if the ID being removed is being
				// referenced to
				// by a product in another table
				if (!db.isSafeToRemoveFromProductCategory(id)) {
					outputMsg += id + " is being referenced to by a product"
							+ " in another table\n";
				} else {
					// if not being referenced, then id is safe to remove
					SQLqueries.add("DELETE FROM ProductCategory WHERE "
							+ " category = '" + id + "';");
				}
			}
			break;
		case BulkProduct:
			// removing any bulkproduct will also remove it from sales
			// in order to maintain consistency
			for (String id : toRemove) {
				SQLqueries.add("DELETE FROM Sale WHERE code = '" + id + "'");
				SQLqueries.add("DELETE FROM BulkProduct WHERE BIC = '" + id
						+ "'");
			}
			break;
		case PackagedProduct:
			// removing any packagedProduct will also remove it from sale
			// in order to maintain consistency
			for (String id : toRemove) {
				SQLqueries.add("DELETE FROM Sale WHERE code = '" + id + "'");
				SQLqueries.add("DELETE FROM PackagedProduct WHERE UPC = '" + id
						+ "'");
			}
			break;
		case Sale:
			// Remove item on sale from Sale relation.
			for (String id : toRemove) {
				SQLqueries.add("DELETE FROM Sale WHERE saleID = '" + id + "'");
			}
			break;
		}

		// execute SQLqueries
		for (String query : SQLqueries) {
			try {
				db.updateDatabase(query);
			} catch (SQLException e) {
				outputMsg += "Failed to execute SQL query: " + query + "\n";
			}
		}

		gui.resetTabs(outputMsg, table.toString(), lastSearchTerm);

	}
	
/**
	 * Search an item from Product Category, Packaged Product, BulkProduct or
	 * Sale relations.
	 * 
	 * @param searchTerm
	 *            The input in the search text field.
	 */
	public void searchItem(String searchTerm) {
		this.lastSearchTerm = searchTerm;
		generateTableRows();
		if (searchTerm.isEmpty()) {
			return;
		}
		searchTerm = searchTerm.toLowerCase();
		
		String output = "";
		boolean foundMatch = false;
		for (int rowNum = 0; rowNum < this.dataVector.size(); ++rowNum) {
			boolean rowHasMatch = false;
			Vector<Object> row = (Vector<Object>) this.dataVector.get(rowNum);
			for (int colNum = 0; colNum < row.size(); ++colNum) {
				String column = row.get(colNum).toString().toLowerCase();
				if (column.contains(searchTerm)) {
					rowHasMatch = true;
					foundMatch = true;
					break;
				}
			}
			if (!rowHasMatch) {
				this.dataVector.remove(rowNum);
				this.fireTableRowsDeleted(rowNum, rowNum);
				rowNum--;
			}
			

		}
	}

	
	/**
	 * Check if the string represents a valid percent (a double, 0.0 to 1.0).
	 * Throw NumberFormatException if string does not represent a valid percent.
	 * 
	 * @param s
	 *            The representation of a percent.
	 * @throws NumberFormatException
	 *             Throw error message detailing why validation failed.
	 */
	private void validateRate(String s) throws NumberFormatException {
		try {
			Double d = Double.parseDouble(s);
			if (d < 0 || d > 100) {
				// caught in catch block, share same message
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException("Invalid percent, must be a number"
					+ " between 0 and 100");
		}
	}

	/**
	 * Throw NumberFormatException if string is not a positive double (useful
	 * for exception.getMessages()).
	 * 
	 * @param s
	 *            The representation of a price or weight.
	 * @throws NumberFormatException
	 *             Throw error message detailing why validation failed.
	 */
	private void validatePositiveDouble(String s) throws NumberFormatException {
		try {
			Double d = Double.parseDouble(s);
			if (d < 0) {
				// caught in catch block, share same error message
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException("Invalid positive number");
		}
	}
}