package ca.utoronto.csc301.SelfCheckOut.Devices;

import static org.junit.Assert.*;

import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.Test;

import ca.utoronto.csc301.SelfCheckOut.App.SelfCheckOut;
//import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidCardException;
//import ca.utoronto.csc301.SelfCheckOut.Exceptions.NegativePayAmountException;
//import ca.utoronto.csc301.SelfCheckOut.Exceptions.UnrecognizedPayTypeException;

public class PaymentCollectorTest {

	SelfCheckOut checkOut;
	PaymentCollector payCol;
	double EPSILON = 1e-15; // a very small number

	@Before
	public void setUp() throws Exception {
		payCol = new PaymentCollector(false);
		payCol.setTotalCost(6.0);
		payCol.setTotalPaid(0.0);

		checkOut = new SelfCheckOut();

	}

	/**
	 * Test for cash payment.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCollectCash() throws Exception {
		checkOut.payForGroceries(false, "", 20.0, 1);
		payCol.collect("", 20.0, 1);
		assertEquals(payCol.getChange(), 14.0, EPSILON);
	}

	/**
	 * Test for debit payment.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCollectDebit() throws Exception {
		checkOut.payForGroceries(false, "1234567890123456", 20.0, 2);
		payCol.collect("1234567890123456", 20.0, 2);
		equals(payCol.isFinishedPaying());
	}

	/**
	 * Test for Credit payment.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCollectCredit() throws Exception {
		checkOut.payForGroceries(false, "1234567890123456", 20, 3);
		payCol.collect("1234567890123456", 20, 3);
		equals(payCol.isFinishedPaying());
	}

	/**
	 * Test for Gift card payment.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCollectGift() throws Exception {
		checkOut.payForGroceries(false, "1234567890123456", 20, 4);
		payCol.collect("1234567890123456", 20, 4);
		equals(payCol.isFinishedPaying());
	}

	/**
	 * Test for when the exact amount on bill is paid.
	 */
	@Test
	public void testMakePaymentExactAmount() {
		assertEquals(payCol.makePayment(6.0, true), 0.0, EPSILON);
	}

	/**
	 * Test for when more than amount on bill is paid.
	 */
	@Test
	public void testMakePaymentOverpay() {
		assertEquals(payCol.makePayment(10.0, true), 4.0, EPSILON);
	}

	/**
	 * Test for when less than amount on bill is paid.
	 */
	@Test
	public void testMakePaymentUnderpay() {
		assertEquals(payCol.makePayment(4.0, true), 0.0, EPSILON);
	}

	/**
	 * Test setter function for totalCost.
	 */
	@Test
	public void testSetTotalCost() {
		payCol.setTotalCost(30);
		assertEquals(payCol.getTotalCost(), 30, EPSILON);
	}

	/**
	 * Test for getter function for totalCost.
	 */
	@Test
	public void testGetTotalCost() {
		assertEquals(payCol.getTotalCost(), 6.0, EPSILON);
	}

	/**
	 * Test for setter function totalPaid.
	 */
	@Test
	public void testSetTotalPaid() {
		payCol.setTotalPaid(30);
		assertEquals(payCol.getTotalPaid(), 30, EPSILON);
	}

	/**
	 * Test for getter function totalPaid.
	 */
	@Test
	public void testGetTotalPaid() {
		assertEquals(payCol.getTotalCost(), 6.0, EPSILON);
	}

	/**
	 * Test for setter function change.
	 */
	@Test
	public void testSetChange() {
		payCol.setChange(5.0);
		assertEquals(payCol.getChange(), 5.0, EPSILON);
	}

	/**
	 * Test for getter function change.
	 */
	@Test
	public void testsGetChange() {
		payCol.setChange(5.0);
		assertEquals(payCol.getChange(), 5.0, EPSILON);

	}
}
