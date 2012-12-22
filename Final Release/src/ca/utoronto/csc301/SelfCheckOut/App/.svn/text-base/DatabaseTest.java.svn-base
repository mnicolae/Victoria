package ca.utoronto.csc301.SelfCheckOut.App;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.utoronto.csc301.SelfCheckOut.Exceptions.DatabaseConnectionException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.JDCBDriverException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountNegativeException;

public class DatabaseTest {

	static Database pdb;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		pdb = new Database("Database/TestSelfCheckOut.db");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		pdb = null;
	}

	/**
	 * Test isSafeToRemoveFromProductCategory for removing an ID that is not
	 * being referred to by any product in the DB.
	 */
	@Test
	public void safeToRemove() {
		assertTrue(pdb.isSafeToRemoveFromProductCategory("electronics"));
	}

	/**
	 * Test isSafeToRemoveFromProductCategory for removing an ID that is being
	 * referred to by a product in Bulk products.
	 */
	@Test
	public void notSafeToRemoveBulk() {
		assertFalse(pdb.isSafeToRemoveFromProductCategory("fruit"));
	}

	/**
	 * Test isSafeToRemoveFromProductCategory for removing an ID that is being
	 * referred to by a product in Packaged products.
	 */
	@Test
	public void notSafeToRemovePackaged() {
		assertFalse(pdb.isSafeToRemoveFromProductCategory("magazine"));
	}

	/**
	 * Test isSafeToRemoveFromProductCategory for removing an ID that is being
	 * referred to by products in both Bulk and Packaged products
	 */
	@Test
	public void notSafeToRemoveBoth() {
		assertFalse(pdb.isSafeToRemoveFromProductCategory("prepared food"));
	}

	/**
	 * Test lookUpItemByCategory with a category that exists in the database.
	 */
	@Test
	public void lookUpExistingCategory() throws Exception {
		assertTrue(pdb.lookUpCategory("veggie"));
	}

	/**
	 * Test lookUpItemByCategory with a category that doesn't exist in the
	 * database.
	 */
	@Test
	public void lookUpNonExistingCategory() throws Exception {
		assertFalse(pdb.lookUpCategory("doesNotExist"));
	}

	/**
	 * Test listTable with a table that does not exist.
	 */
	@Test
	public void listMissingTable() {
		// query the database using a table that doesn't exist
		assertEquals(pdb.listTable("fakeTable"), null);
	}

	/**
	 * Test listTable with a table that does exist.
	 */
	@Test
	public void listTable() {
		// query the database
		ResultSet rs = pdb.listTable("ProductCategory");
		// check if a result was returned
		assertTrue(rs != null);
		try {
			// check if there is a row in the result
			assertTrue(rs.next());
		} catch (SQLException e) {
			fail("Could not find table");
		}finally{
			try {
				if (rs != null)rs.close();
			} catch (SQLException ignore) {
			}
		}
	}

	/**
	 * Test update database with an ill formed query.
	 */
	@Test(expected=SQLException.class)
	public void updateDatabaseIllFormedQuery() throws SQLException {
		// check if updating the database with an ill formed query will
		// result in a SQLException
		pdb.updateDatabase("select *from234");
		
	}

	/**
	 * Test update database with a well formed query.
	 */
	@Test
	public void updateDatabaseWellFormedQuery() throws SQLException {
		// check if updating the database with an well formed query will succeed

		pdb.updateDatabase("update ProductCategory set taxRate = 0.00" +
				" where category='veggie'");
	}

	/**
	 * Test Generating a report to a file in the src directory.
	 */
	@Test
	public void GenerateReportToFile() {
		// check the success of generating the report
		assertTrue(pdb.generateReportToFile("report.txt"));
	}

	/**
	 * Test getting a report.
	 */
	@Test
	public void GenerateReport() {
		// check if the report has at least one character
		assertTrue(pdb.generateReport().length() > 0);
	}
	
	/**
	 * Test getting all products in the database.
	 */
	@Test
	public void getAllProducts() {
		Vector<String> products = pdb.getAllProducts();
		//check if any products were obtained
		assertTrue(products.size() > 0);
	}
	
	/**
	 * Test getting all tax categories in the database.
	 */
	@Test
	public void getAllCategories() {
		Vector<String> categories = pdb.getAllTaxCategories();
		//check if any products were obtained
		assertTrue(categories.size() > 0);
	}
}
