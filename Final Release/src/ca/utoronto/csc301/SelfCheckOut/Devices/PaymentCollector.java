/*
 * Creator: Susan Elliott Sim
 * Course: Inf111, Winter 2008
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008
 * 
 * Copyright, 2006, 2008 University of California. 
 * 
 * The PaymentCollector class handles the payment process. This is not a concern for this assignment.
 * It is created to make the system more "complete," but it lacks real functionalities.
 */

package ca.utoronto.csc301.SelfCheckOut.Devices;

import ca.utoronto.csc301.SelfCheckOut.Exceptions.*;

/**
 * This class represents the payment-collecting portion of the system: the
 * credit-card pad or cash-input part of the system. We are not concerned with
 * this part of the system, so a simple stub method is provided.
 * 
 */
public class PaymentCollector {
	/**
	 * Whether or not shopper finished paying in full.
	 */
	private boolean finishedPaying;
	/**
	 * Total cost of checkout cart, value is constantly set to total cost once
	 * the shopper entered the payment state; do not use this as a "storage"
	 * variable
	 */
	private double totalCost;
	/**
	 * Amount of total payment so far (must be non-negative).
	 */
	private double totalPaid;
	/**
	 * Amount of change to be returned to customer (must be non-negative).
	 */
	private double change;

	/**
	 * The old constructor; kept for compatibility reasons.
	 */
	public PaymentCollector() {
		// to work with pre-existing test cases, finishedPaying is true
		// for argumentless constructor
		this(true);
	}

	/**
	 * The boolean argument determines whether or not the shopper finished
	 * paying for the grocery items right from the start (useful for testing).
	 * 
	 * @param startingState
	 *            whether or not shopper finished paying in full from the get-go
	 */
	public PaymentCollector(boolean startingState) {
		setFinishedPaying(startingState);
		setChange(0.0);
		setTotalCost(0.0);
		setTotalPaid(0.0);
	}

	/**
	 * This method is called by SelfCheckOut when the customer is finished
	 * scanning and wishes to pay. Checks validity of cardID (if payType is not
	 * cash) before applying payment. Change is set to the appropriate amount if
	 * the shopper finished paying in full.
	 * 
	 * @param cardID
	 *            ID of the card, empty string if payType is cash
	 * @param payAmount
	 *            the amount paid
	 * @param payType
	 *            1=cash, 2=debit, 3=credit, 4=gift card
	 * @throws InvalidCardException
	 * @throws NegativePayAmountException
	 * @throws UnrecognizedPayTypeException
	 */
	public void collect(String cardID, double payAmount, int payType)
			throws InvalidCardException, NegativePayAmountException,
			UnrecognizedPayTypeException {

		if (payType < 1 || payType > 4) {
			throw new UnrecognizedPayTypeException(
					"Unrecognized payment method");
		}

		if (payAmount < 0) {
			throw new NegativePayAmountException("Negative payment amount");
		}

		// Cash payment
		if (payType == 1) {
			// allow change and do not cap the maximum amount that can be paid
			setChange(makePayment(payAmount, false));
		}

		// Debit payment
		else if (payType == 2) {
			debitCardReader(cardID, payAmount); // check validity of debit card
			// do not set change because cash back is not allowed, also cap
			// the maximum amount that can be paid
			makePayment(payAmount, true);
		}

		// Credit payment
		else if (payType == 3) {
			creditCardReader(cardID, payAmount); // check validity of credit
													// card
			// do not set change because cash back is not allowed, also cap
			// the maximum amount that can be paid
			makePayment(payAmount, true);
		}

		// Gift card payment
		else if (payType == 4) {
			giftCardReader(cardID, payAmount); // check validity of gift card
			// do not set change because cash back is not allowed, also cap
			// the maximum amount that can be paid
			makePayment(payAmount, true);
		}

	}

	/**
	 * Handles interactions with the debit card reader device.
	 * 
	 * @param cardID
	 *            ID of credit card
	 * @param payAmount
	 *            Amount to be used from credit card
	 * @throws InvalidCardException
	 */
	private void debitCardReader(String cardID, double payAmount)
			throws InvalidCardException {
		// check validity of card ID and then pass it to imaginary
		// interac (TM) card reader machine (or some other machine)

		// for local validation we only check that cardID is a 16 digits number
		if (!cardID.matches("\\d{16}")) {
			throw new InvalidCardException("Invalid debit card ID");
		}

		// code for passing cardID and payAmount to imaginary interac (TM)
		// machine goes here ...
		// read response from card reader machine, do not do anything if card
		// was valid and throw InvalidCardException like local validation
		// if card was invalid
	}

	/**
	 * Handles interactions with the credit card reader device.
	 * 
	 * @param cardID
	 *            ID of credit card
	 * @param payAmount
	 *            Amount to be used from credit card
	 * @throws InvalidCardException
	 */
	private void creditCardReader(String cardID, double payAmount)
			throws InvalidCardException {
		// check validity of card ID and then pass it to imaginary
		// interac (TM) card reader machine (or some other machine)

		// for local validation we only check that cardID is a 16 digits number
		if (!cardID.matches("\\d{16}")) {
			throw new InvalidCardException("Invalid credit card ID");
		}

		// code for passing cardID and payAmount to imaginary interac (TM)
		// machine goes here ...
		// read response from card reader machine, do not do anything if card
		// was valid and throw InvalidCardException like local validation
		// if card was invalid
	}

	/**
	 * Handles interactions with the gift card reader device.
	 * 
	 * @param cardID
	 *            ID of gift card
	 * @param payAmount
	 *            Amount to be used from gift card
	 * @throws InvalidCardException
	 */
	private void giftCardReader(String cardID, double payAmount)
			throws InvalidCardException {
		// we are not responsible for implementing the gift card user story
		// so we only perform a simple check on validity of card ID without
		// regards for other important data such as the amount left in the gift
		// card or the expiry date of the gift card

		// for local validation we only check that cardID is a 16 digits number
		if (!cardID.matches("\\d{16}")) {
			throw new InvalidCardException("Invalid gift card ID");
		}

		// if we were responsible for implementing user story #77 (which is
		// the gift card user story) then our code for querying the database
		// to get information about the gift card would go here ...
		// read response and:
		// if all constraints (expiry date, etc.) are valid, we would subtract
		// payAmount from amount left in gift card
		// else throw InvalidCardException
	}

	/**
	 * Pay the cost of the items in the shopping cart by payAmount. If the
	 * payAmount is greater than the amount that needs to be paid, then reduce
	 * the amount that needs to be paid to 0 and return the excess payAmount
	 * (i.e. return = payAmount - "amount that needs to be paid"). Also set
	 * finishedPaying to true if the shopping cart is paid in full.
	 * 
	 * @param payAmount
	 *            the amount to be paid (non-negative)
	 * @param capTotalPaid
	 *            guarantees getTotalPaid() <= getTotalCost() if true
	 * @return the change (or excess payAmount as defined above)
	 */
	public double makePayment(double payAmount, boolean capTotalPaid) {
		double change = 0.0;
		double totalCost = getTotalCost();
		double totalPaid = getTotalPaid();
		double remainderPay = totalCost - totalPaid;

		// payAmount equals or exceeds the amount that needs to be paid
		if (remainderPay - payAmount <= 0) {

			change = payAmount - remainderPay;

			// do not set change here or else shopper will get cash back
			// from using credit/debit/gift card payment method
			// change is set in caller which is collect() in this case
			// setChange(change);

			if (capTotalPaid) {
				setTotalPaid(totalCost);
			} else {
				setTotalPaid(totalPaid + payAmount);
			}

			setFinishedPaying(true);

			// payAmount is not enough to pay for the shopping cart in full
		} else {
			setTotalPaid(totalPaid + payAmount);
		}

		return change;
	}

	/**
	 * Getter method.
	 * 
	 * @return change the change for the shopper
	 */
	public double getChange() {
		return change;
	}

	/**
	 * Setter method. Set the change to be given to the customer.
	 * 
	 * @param c
	 *            the amount of change (non-negative)
	 */
	public void setChange(double c) {
		this.change = c;
	}

	/**
	 * Getter method. True if shopper finished paying, false otherwise.
	 * 
	 * @return True if shopper finished paying, false otherwise.
	 */
	public boolean isFinishedPaying() {
		return finishedPaying;
	}

	/**
	 * Setter method. True if the shopper finished paying.
	 * 
	 * @param finishedPaying
	 *            whether or not the shopper finished paying.
	 */
	public void setFinishedPaying(boolean finishedPaying) {
		this.finishedPaying = finishedPaying;
	}

	/**
	 * Getter method. Amount paid by shopper so far.
	 * 
	 * @return Amount paid by shopper so far (non-negative).
	 */
	public double getTotalPaid() {
		return totalPaid;
	}

	/**
	 * Setter method. Set the total amount paid so far
	 * 
	 * @param paid
	 *            the total amount paid so far (non-negative)
	 */
	public void setTotalPaid(double paid) {
		this.totalPaid = paid;
	}

	/**
	 * Get the total cost of all items in the cart. This represents the final
	 * cost i.e including any tax and deductions from sales.
	 * 
	 * @return totalCost
	 */
	public double getTotalCost() {
		return totalCost;
	}

	/**
	 * Set the total cost of checkout cart. Note: value is constantly set to
	 * total cost once the shopper entered the payment state; do not use this as
	 * a "storage" variable
	 * 
	 * @param totalCost
	 *            total cost of the checkout cart (non-negative)
	 */
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

}
