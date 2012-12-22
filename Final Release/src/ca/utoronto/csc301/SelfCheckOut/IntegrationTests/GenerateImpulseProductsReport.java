package ca.utoronto.csc301.SelfCheckOut.IntegrationTests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.utoronto.csc301.SelfCheckOut.App.Database;

public class GenerateImpulseProductsReport {

	static Database PDB;
	static String highestProfit;
	static String mostSold;
	static String fastestMoneyMaking;
	static String fastestSelling;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PDB = new Database("Database/TestSelfCheckOut.db");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Generates a report to the terminal. The report has information on: Rank
	 * items by which has the largest profit margin total regardless of date,
	 * Rank items by which sold the most regardless of date, Rank items by which
	 * makes the most money given the date that item started selling, i.e.
	 * (profit margin of item) / (todayDate - startDate of item) Rank items by
	 * which sold the most given the date that item started selling, i.e (total
	 * sold / (todayDate - startDate of item)
	 */
	@Test
	public void GenerateReportToScreen() {
		System.out.println(PDB.generateReport());
	}

	/**
	 * Generates a report to a file in the src directory. The report has
	 * information on: Rank items by which has the largest profit margin total
	 * regardless of date, Rank items by which sold the most regardless of date,
	 * Rank items by which makes the most money given the date that item started
	 * selling, i.e. (profit margin of item) / (todayDate - startDate of item)
	 * Rank items by which sold the most given the date that item started
	 * selling, i.e (total sold / (todayDate - startDate of item)
	 */
	@Test
	public void GenerateReportToFile() {
		PDB.generateReportToFile("report.txt");
	}
}
