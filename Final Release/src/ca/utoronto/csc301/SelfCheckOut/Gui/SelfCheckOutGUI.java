/*
 * 
 * Created on October 2, 2008
 * Updated on October 6, 2008, September 12, 2012, October 13, 2012
 * 
 * The SelfCheckOutGUI class handles the Graphical User Interface for the Self CheckOut 
 * system. It allows the user to do the following actions in the system: 
 * Start Transaction, Add a Packaged Item, Add a Bulk Item, 
 * Bag Item, Pay for Items, and summon a cashier for Help.
 * Application messages, including exceptions, will be shown in the Messages section of the
 * screen.
 */
package ca.utoronto.csc301.SelfCheckOut.Gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ca.utoronto.csc301.SelfCheckOut.App.GroceryItem;
import ca.utoronto.csc301.SelfCheckOut.App.SelfCheckOut;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.AddWhileBaggingException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.AddWhilePayingException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.DatabaseConnectionException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.IncorrectStateException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidBICException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidCardException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidProductException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidUPCException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.InvalidWeightException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.JDCBDriverException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.NegativePayAmountException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.SaleDiscountException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.UnrecognizedPayTypeException;

/**
 * This class contains the main method that will show the Graphical User
 * Interface of the Self CheckOut System.
 */
public class SelfCheckOutGUI extends JFrame implements ActionListener {
	/**
	 * Class serial version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * String for the UPC label
	 */
	protected static final String upcLabelString = "UPC Number";
	/**
	 * String for the BIC label
	 */
	protected static final String bicLabelString = "BIC Number";
	/**
	 * String for the Weight label
	 */
	protected static final String bicWeightLabelString = "Weight";
	/**
	 * String for the payment card ID
	 */
	protected static final String payCardIDLabelString = "Card ID";
	/**
	 * String for the payment amount
	 */
	protected static final String payAmountLabelString = "Amount ($)";
	/**
	 * String for title of cashier notification dialog
	 */
	protected static final String cashierDialogTitle = "Cashier Notification";
	/**
	 * String for text of cashier notification dialog
	 */
	protected static final String cashierDialogContent = "Incoming help request from SelfCheckOut #";
	/**
	 * Button for Start action
	 */
	protected JButton startButton;
	/**
	 * Button for Add Packaged Item action
	 */
	protected JButton addUPCButton;
	/**
	 * Button for Add Bulk Item action
	 */
	protected JButton addBICButton;
	/**
	 * Button for Bag Item action
	 */
	protected JButton bagItemButton;
	/**
	 * Button for Pay for Items action
	 */
	protected JButton payButton;
	/**
	 * Button for Help action
	 */
	protected JButton helpButton;
	/**
	 * Button for canceling Help action
	 */
	protected JButton helpButtonCancel;
	/**
	 * Buttons for cashier notification dialog
	 */
	protected JButton cashierReportButton;
	protected JButton cashierCancelButton;
	protected JButton cashierDisableButton;
	/**
	 * Radio button group for payment method
	 */
	protected ButtonGroup PayMethodRadioButtonGroup;
	/**
	 * Radio buttons for payment method (they are in PayMethodRadioButtonGroup)
	 */
	protected JRadioButton cashRadioButton;
	protected JRadioButton debitCardRadioButton;
	protected JRadioButton creditCardRadioButton;
	protected JRadioButton giftCardRadioButton;
	/**
	 * Text Field for UPC
	 */
	protected JTextField upcTextField;
	/**
	 * Text Field for BIC
	 */
	protected JTextField bicTextField;
	/**
	 * Text Field for Weight
	 */
	protected JTextField bicWeightTextField;
	/**
	 * Text Field for payment card ID
	 */
	protected JTextField payCardIDTextField;
	/**
	 * Text Field for payment amount
	 */
	protected JTextField payAmountTextField;
	/**
	 * Text Area for application messages
	 */
	protected JTextArea messagesTextArea;
	/**
	 * SelfCheckOut object for the transaction
	 */
	protected SelfCheckOut selfCheckOut = null;
	/**
	 * GroceryItem object used for the transaction
	 */
	protected GroceryItem groceryItem = null;
	/**
	 * Frame for cashier dialog
	 */
	protected JFrame cashierFrame;
	/**
	 * Dialog for cashier
	 */
	protected JDialog cashierDialog;
	/**
	 * Boolean that indicates whether help is enabled. It is true upon
	 * initialization.
	 */
	protected boolean HELP_ENABLE = true;
	/**
	 * Path to the shopping cart icon
	 */
	protected static final String ScoIconPath = "pic/shopping_cart.png";
	
	protected JButton backToMain;
	
	protected JPanel appPanel;
	
	/**
	 * This constructor creates the text fields, labels, and buttons. It
	 * organizes all these objects in a Grid Bag that has 5 lines, one per each
	 * action. It also creates a text area to show the application messages
	 * including exceptions. Finally, it includes all the created controls in a
	 * panel
	 */
	public SelfCheckOutGUI() {
		super("Self Check Out");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(720, 650);
		setVisible(true);
		setLayout(new BorderLayout());

		// Text field for UPC
		upcTextField = new JTextField(10);
		upcTextField.setActionCommand(upcLabelString);
		upcTextField.addActionListener(this);

		// Text field for BIC
		bicTextField = new JTextField(10);
		bicTextField.setActionCommand(bicLabelString);
		bicTextField.addActionListener(this);

		// Text field for BIC Weight
		bicWeightTextField = new JTextField(10);
		bicWeightTextField.setActionCommand(bicWeightLabelString);
		bicWeightTextField.addActionListener(this);

		// Text field for payment card ID
		payCardIDTextField = new JTextField(10);
		payCardIDTextField.setActionCommand(payCardIDLabelString);
		payCardIDTextField.addActionListener(this);

		// Text field for payment amount
		payAmountTextField = new JTextField(10);
		payAmountTextField.setActionCommand(payAmountLabelString);
		payAmountTextField.addActionListener(this);

		// Label for UPC
		JLabel upcTextFieldLabel = new JLabel(upcLabelString + ": ");
		upcTextFieldLabel.setLabelFor(upcTextField);

		// Label for BIC
		JLabel bicTextFieldLabel = new JLabel(bicLabelString + ": ");
		bicTextFieldLabel.setLabelFor(bicTextField);

		// Label for BIC Weight
		JLabel bicWeightTextFieldLabel = new JLabel(bicWeightLabelString + ": ");
		bicWeightTextFieldLabel.setLabelFor(bicWeightTextField);

		// Label for payment card ID
		JLabel payCardIDTextFieldLabel = new JLabel(payCardIDLabelString + ": ");
		payCardIDTextFieldLabel.setLabelFor(payCardIDTextField);

		// Label for payment amount
		JLabel payAmountTextFieldLabel = new JLabel(payAmountLabelString + ": ");
		payAmountTextFieldLabel.setLabelFor(payAmountTextField);

		// Start Button
		startButton = new JButton("Start");
		startButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		startButton.setHorizontalTextPosition(AbstractButton.CENTER);
		startButton.setActionCommand("start");
		startButton.addActionListener(this);

		// Add Packaged Item Button
		addUPCButton = new JButton("Add UPC");
		addUPCButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		addUPCButton.setHorizontalTextPosition(AbstractButton.CENTER);
		addUPCButton.setActionCommand("addUPC");
		addUPCButton.addActionListener(this);

		// Add Bulk Item Button
		addBICButton = new JButton("Add BIC");
		addBICButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		addBICButton.setHorizontalTextPosition(AbstractButton.CENTER);
		addBICButton.setActionCommand("addBIC");
		addBICButton.addActionListener(this);

		// Bag Item Button
		bagItemButton = new JButton("Bag Item");
		bagItemButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		bagItemButton.setHorizontalTextPosition(AbstractButton.CENTER);
		bagItemButton.setActionCommand("bagItem");
		bagItemButton.addActionListener(this);

		// Pay Button
		payButton = new JButton("Pay");
		payButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		payButton.setHorizontalTextPosition(AbstractButton.CENTER);
		payButton.setActionCommand("payItems");
		payButton.addActionListener(this);

		// Pay Radio Buttons
		cashRadioButton = new JRadioButton("Cash", true);
		debitCardRadioButton = new JRadioButton("Debit Card");
		creditCardRadioButton = new JRadioButton("Credit Card");
		giftCardRadioButton = new JRadioButton("Gift Card");

		// Pay Radio Button Group
		PayMethodRadioButtonGroup = new ButtonGroup();
		PayMethodRadioButtonGroup.add(cashRadioButton);
		PayMethodRadioButtonGroup.add(debitCardRadioButton);
		PayMethodRadioButtonGroup.add(creditCardRadioButton);
		PayMethodRadioButtonGroup.add(giftCardRadioButton);

		// Help Button
		helpButton = new JButton("Help");
		helpButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		helpButton.setHorizontalTextPosition(AbstractButton.CENTER);
		helpButton.setActionCommand("summonHelp");
		helpButton.addActionListener(this);

		// Cancel Help Button
		helpButtonCancel = new JButton("Cancel");
		helpButtonCancel.setVerticalTextPosition(AbstractButton.BOTTOM);
		helpButtonCancel.setHorizontalTextPosition(AbstractButton.CENTER);
		helpButtonCancel.setActionCommand("cancelHelp");
		helpButtonCancel.addActionListener(this);
		// button is not available upon initialization, becomes available
		// whenever help button is pressed
		helpButtonCancel.setEnabled(false);
		
		// Back to main button
		backToMain = new JButton("Back to main menu");
		backToMain.setActionCommand("backToMain");
		backToMain.addActionListener(this);

		// Cashier Buttons
		cashierReportButton = new JButton("Report to SelfCheckOut");
		cashierReportButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		cashierReportButton.setHorizontalTextPosition(AbstractButton.CENTER);
		cashierReportButton.setActionCommand("cashierReport");
		cashierReportButton.addActionListener(this);

		cashierCancelButton = new JButton("Cancel help request");
		cashierCancelButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		cashierCancelButton.setHorizontalTextPosition(AbstractButton.CENTER);
		cashierCancelButton.setActionCommand("cashierCancel");
		cashierCancelButton.addActionListener(this);

		cashierDisableButton = new JButton("Disable future help requests");
		cashierDisableButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		cashierDisableButton.setHorizontalTextPosition(AbstractButton.CENTER);
		cashierDisableButton.setActionCommand("cashierDisable");
		cashierDisableButton.addActionListener(this);

		// Lay out the text controls and the labels
		JPanel textControlsPane = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		textControlsPane.setLayout(gridbag);
		c.anchor = GridBagConstraints.EAST;

		// Line 1: Start Action
		c.gridwidth = GridBagConstraints.REMAINDER; // next-to-last
		c.fill = GridBagConstraints.NONE; // reset to default
		c.weightx = 0.0; // reset to default
		textControlsPane.add(startButton, c);

		// Line 2: Add Packaged Item
		c.gridwidth = GridBagConstraints.RELATIVE; // next-to-last
		c.fill = GridBagConstraints.NONE; // reset to default
		c.weightx = 0.0; // reset to default
		textControlsPane.add(upcTextFieldLabel, c);

		c.gridwidth = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		textControlsPane.add(upcTextField, c);

		c.gridwidth = GridBagConstraints.REMAINDER; // end row
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		textControlsPane.add(addUPCButton, c);

		// Line 3: Add Bulk Item
		c.gridwidth = GridBagConstraints.RELATIVE; // next-to-last
		c.fill = GridBagConstraints.NONE; // reset to default
		c.weightx = 0.0; // reset to default
		textControlsPane.add(bicTextFieldLabel, c);

		c.gridwidth = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		textControlsPane.add(bicTextField, c);

		c.gridwidth = GridBagConstraints.RELATIVE; // next-to-last
		c.fill = GridBagConstraints.NONE; // reset to default
		c.weightx = 0.0; // reset to default
		textControlsPane.add(bicWeightTextFieldLabel, c);

		c.gridwidth = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		textControlsPane.add(bicWeightTextField, c);

		c.gridwidth = GridBagConstraints.REMAINDER; // end row
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		textControlsPane.add(addBICButton, c);

		// Line 4: Bag Item
		c.gridwidth = GridBagConstraints.REMAINDER; // next-to-last
		c.fill = GridBagConstraints.NONE; // reset to default
		c.weightx = 0.0; // reset to default
		textControlsPane.add(bagItemButton, c);

		// Line 5: Pay for Items
		c.gridwidth = GridBagConstraints.RELATIVE; // next-to-last
		c.fill = GridBagConstraints.NONE; // reset to default
		c.weightx = 0.0; // reset to default
		textControlsPane.add(payCardIDTextFieldLabel, c);

		c.gridwidth = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		textControlsPane.add(payCardIDTextField, c);

		c.gridwidth = GridBagConstraints.RELATIVE; // next-to-last
		c.fill = GridBagConstraints.NONE; // reset to default
		c.weightx = 0.0; // reset to default
		textControlsPane.add(payAmountTextFieldLabel, c);

		c.gridwidth = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		textControlsPane.add(payAmountTextField, c);

		c.gridwidth = GridBagConstraints.REMAINDER; // next-to-last
		c.fill = GridBagConstraints.NONE; // reset to default
		c.weightx = 0.0; // reset to default
		textControlsPane.add(payButton, c);

		// Line 6: Payment Method
		c.gridwidth = GridBagConstraints.RELATIVE; // next-to-last
		c.fill = GridBagConstraints.NONE; // reset to default
		c.weightx = 0.0; // reset to default
		textControlsPane.add(cashRadioButton, c);

		c.gridwidth = GridBagConstraints.RELATIVE; // next-to-last
		c.fill = GridBagConstraints.NONE; // reset to default
		c.weightx = 0.0; // reset to default
		textControlsPane.add(debitCardRadioButton, c);

		c.gridwidth = GridBagConstraints.RELATIVE; // next-to-last
		c.fill = GridBagConstraints.NONE; // reset to default
		c.weightx = 0.0; // reset to default
		textControlsPane.add(creditCardRadioButton, c);

		c.gridwidth = GridBagConstraints.REMAINDER; // end row
		c.fill = GridBagConstraints.NONE; // reset to default
		c.weightx = 0.0; // reset to default
		textControlsPane.add(giftCardRadioButton, c);

		// Line 7: Help
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		textControlsPane.add(helpButton, c);

		// Line 7: Cancel Help
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		textControlsPane.add(helpButtonCancel, c);

		// Line 7: Back to main
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0,20,0,0);  //top padding
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		textControlsPane.add(backToMain, c);

		// Create border for Actions
		textControlsPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Actions"),
				BorderFactory.createEmptyBorder(20, 100, 20, 400)));

		// Create a text area for the application messages
		messagesTextArea = new JTextArea();
		messagesTextArea.setFont(new Font("Serif", Font.ITALIC, 16));
		messagesTextArea.setLineWrap(true);
		messagesTextArea.setWrapStyleWord(true);
		messagesTextArea.setEditable(false);

		// Add scroll to the text area
		JScrollPane areaScrollPane = new JScrollPane(messagesTextArea);
		areaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(250, 250));

		// Create border for Messages
		areaScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Messages"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)),
				areaScrollPane.getBorder()));

		// Include all the controls in the application panel
		appPanel = new JPanel(new BorderLayout());
		appPanel.add(textControlsPane, BorderLayout.PAGE_START);
		appPanel.add(areaScrollPane, BorderLayout.CENTER);

		add(appPanel, BorderLayout.LINE_START);
	}

	/**
	 * Method that receives the ActionEvent when a button is pressed in the GUI.
	 * It calls to the appropriate action in the system and shows the result of
	 * the action in the message text area. If an exception is raised, this is
	 * showed in the message text area starting with the word EXCEPTION.
	 * 
	 * @param e
	 *            ActionEvent captured when user presses a button in the GUI
	 */
	public void actionPerformed(ActionEvent e) {
		// Instantiate actions class
		Actions actions = new Actions();

		try {
			// Start Action
			if ("start".equals(e.getActionCommand())) {
				// reset to true whenever a new self-checkout is started
				HELP_ENABLE = true;
				helpButton.setEnabled(true);
				helpButtonCancel.setEnabled(false);
				selfCheckOut = actions.start();
				messagesTextArea.setText("SelfCheckOut has been started");

				// Add Packaged Item
			} else if ("addUPC".equals(e.getActionCommand())) {

				groceryItem = actions.addUPC(selfCheckOut,
						upcTextField.getText());
				messagesTextArea.setText("Shopping cart "
						+ actions.printShoppingCart(selfCheckOut
								.listItemsInCart()));
				messagesTextArea.append("\n\nUPC Product "
						+ upcTextField.getText() + " added.");
				upcTextField.setText("");

				// Add Bulk Item
			} else if ("addBIC".equals(e.getActionCommand())) {

				groceryItem = actions.addBIC(selfCheckOut,
						bicTextField.getText(),
						Double.parseDouble(bicWeightTextField.getText()));
				messagesTextArea.setText("Shopping cart "
						+ actions.printShoppingCart(selfCheckOut
								.listItemsInCart()));
				messagesTextArea.append("\n\nBIC Product "
						+ bicTextField.getText() + " added. ");
				bicTextField.setText("");
				bicWeightTextField.setText("");

				// Bag Item
			} else if ("bagItem".equals(e.getActionCommand())) {

				actions.bagItem(selfCheckOut, groceryItem);
				messagesTextArea.setText("Shopping cart "
						+ actions.printShoppingCart(selfCheckOut
								.listItemsInCart()));
				messagesTextArea.append("\n\nTotal Cost $"
						+ selfCheckOut.getTotalCost());
				messagesTextArea.append("\nProduct bagged. ");

				// Pay for Items
			} else if ("payItems".equals(e.getActionCommand())) {

				if (cashRadioButton.isSelected()) {
					actions.payItems(selfCheckOut, "",
							Double.parseDouble(payAmountTextField.getText()), 1);

				} else if (debitCardRadioButton.isSelected()) {
					actions.payItems(selfCheckOut,
							payCardIDTextField.getText(),
							Double.parseDouble(payAmountTextField.getText()), 2);

				} else if (creditCardRadioButton.isSelected()) {
					actions.payItems(selfCheckOut,
							payCardIDTextField.getText(),
							Double.parseDouble(payAmountTextField.getText()), 3);

				} else if (giftCardRadioButton.isSelected()) {
					actions.payItems(selfCheckOut,
							payCardIDTextField.getText(),
							Double.parseDouble(payAmountTextField.getText()), 4);
				}

				messagesTextArea.setText("Shopping cart "
						+ actions.printShoppingCart(selfCheckOut
								.listItemsInCart()));

				double totalCost = selfCheckOut.getPaymentCollector()
						.getTotalCost();
				double totalPaid = selfCheckOut.getPaymentCollector()
						.getTotalPaid();

				messagesTextArea.append("\n\n $" + totalPaid
						+ " paid so far, $" + (totalCost - totalPaid)
						+ " left to pay.");

				payCardIDTextField.setText("");
				payAmountTextField.setText("");

				// if total cost not fully paid, wait for shopper
				// to press Pay button again; else print receipt
				if (selfCheckOut.getPaymentCollector().isFinishedPaying()) {
					messagesTextArea.setText(selfCheckOut.getReceipt());
				}
				// Summon a cashier for help

			} else if ("summonHelp".equals(e.getActionCommand())) {
				helpButton.setEnabled(false); // disable help button
				helpButtonCancel.setEnabled(true); // enable cancel button
				messagesTextArea.setText("Help request sent. Please wait.");

				/*
				 * Specify action that will notify the cashier here
				 */

				cashierFrame = new JFrame("Cashier Notifications");
				cashierFrame.setVisible(false);
				cashierFrame.pack();

				// Label for content of cashier dialog
				JLabel cashierDialogContentLabel = new JLabel(
						cashierDialogContent + selfCheckOut.getSCOid());

				// Create the dialog box
				// Using the JFrame tokenFrame as the parent component
				// allows the dialog to be centered in front of the frame
				cashierDialog = new JDialog(cashierFrame, cashierDialogTitle);
				cashierDialog.setLayout(new FlowLayout());
				cashierDialog
						.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				cashierDialog.add(cashierDialogContentLabel);
				cashierDialog.add(cashierReportButton);
				cashierDialog.add(cashierCancelButton);
				cashierDialog.add(cashierDisableButton);
				cashierDialog.setVisible(true);
				cashierDialog.pack();

				// Cancel help request
			} else if ("cancelHelp".equals(e.getActionCommand())) {
				helpButton.setEnabled(true); // enable help button
				helpButtonCancel.setEnabled(false); // disable cancel button
				messagesTextArea.setText("Help request canceled.");

				/*
				 * Specify action that will notify the cashier here
				 */

				// destroy the existing cashier dialog
				cashierFrame.dispose();

				// Cashier cancel help request remotely
			} else if ("cashierCancel".equals(e.getActionCommand())) {
				if (HELP_ENABLE) {
					helpButton.setEnabled(true); // enable help button
					helpButtonCancel.setEnabled(false); // disable cancel button
				} else {
					helpButton.setEnabled(false); // disable help button
					helpButtonCancel.setEnabled(false); // disable cancel button
				}
				cashierFrame.dispose();
				messagesTextArea
						.setText("No cashier available at the moment. We apologize for the incovenience.");

				// Cashier report to help request
			} else if ("cashierReport".equals(e.getActionCommand())) {
				if (!HELP_ENABLE) {
					helpButton.setEnabled(false); // disable help button
					helpButtonCancel.setEnabled(false); // disable cancel button
				}
				cashierFrame.dispose();
				messagesTextArea
						.setText("A cashier is on his way, please wait.");

				// Cashier disable future help requests
			} else if ("cashierDisable".equals(e.getActionCommand())) {
				HELP_ENABLE = false;
				helpButton.setEnabled(HELP_ENABLE);
				helpButtonCancel.setEnabled(HELP_ENABLE);
				
			} else if ("backToMain".equals(e.getActionCommand())) {
				this.dispose();
				new StarterGUI();
			}

			// Show exception in the text area for messages
		} catch (SaleDiscountException sdne) {
			messagesTextArea.setText("EXCEPTION: Invalid Sale.");
		} catch (JDCBDriverException jdcbde) {
			messagesTextArea.setText("EXCEPTION: JDBC driver missing.");
		} catch (DatabaseConnectionException dbce) {
			messagesTextArea
					.setText("EXCEPTION: Failed to connect to the Database.");
		} catch (AddWhileBaggingException awbe) {
			messagesTextArea
					.setText("EXCEPTION: An action was performed before previous item was bagged.");
		} catch (AddWhilePayingException awpe) {
			messagesTextArea
					.setText("EXCEPTION: Item scanned while payment is being processed");
		} catch (InvalidProductException ipe) {
			messagesTextArea.setText("EXCEPTION: Item not recognized.");
		} catch (IncorrectStateException ise) {
			messagesTextArea
					.setText("EXCEPTION: Invalid action for current state of Self Check Out.");
		} catch (NumberFormatException nfe) {
			messagesTextArea
					.setText("EXCEPTION: Invalid format for the Weight or Pay Amount.");
		} catch (InvalidUPCException iupce) {
			messagesTextArea.setText("EXCEPTION: Invalid UPC: "
					+ iupce.getMessage());
		} catch (InvalidBICException ibice) {
			messagesTextArea.setText("EXCEPTION: Invalid BIC: "
					+ ibice.getMessage());
		} catch (InvalidWeightException iw) {
			messagesTextArea.setText("EXCEPTION: " + iw.getMessage());
		} catch (InvalidCardException ic) {
			messagesTextArea.setText("Exception: " + ic.getMessage());
		} catch (NegativePayAmountException npa) {
			messagesTextArea.setText("Exception: payment cannot be negative");
		} catch (UnrecognizedPayTypeException upt) {
			messagesTextArea.setText("Exception: unrecognized payment type");
		} catch (NullPointerException npe) {
			// Checks if the selfCheckOut object is null
			if (selfCheckOut == null) {
				messagesTextArea
						.setText("EXCEPTION: Self Check Out has not been started.");
			} else {
				messagesTextArea.setText("EXCEPTION: Null Pointer Exception.");
			}
		} catch (Exception exception) {
			messagesTextArea.setText("EXCEPTION: An exception has occurred: "
					+ exception.getMessage()
					+ ". Check console for more details.");
			exception.printStackTrace();
		}
	}


}