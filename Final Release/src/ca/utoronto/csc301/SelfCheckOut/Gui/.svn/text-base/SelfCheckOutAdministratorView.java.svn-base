package ca.utoronto.csc301.SelfCheckOut.Gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;

import ca.utoronto.csc301.SelfCheckOut.App.Database;
import ca.utoronto.csc301.SelfCheckOut.App.Database.Table;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.DatabaseConnectionException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.JDCBDriverException;

/**
 * The GUI of the administrator view for the SelfCheckOut machine.
 * 
 * @author Alex Rodrigues
 * 
 */
public class SelfCheckOutAdministratorView extends JFrame {

	/**
	 * Class serial version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The database of the self check out machine.
	 */
	private Database db;
	/**
	 * The panel that contains the tabs of the GUI.
	 */
	private JTabbedPane tabbedPane;
	/**
	 * Text Area for application messages
	 */
	protected JTextArea messagesTextArea;
	/**
	 * A Vector of all the Packaged Products and Bulk Products.
	 */
	private Vector<String> allProducts;
	/**
	 * A vector of all the tax categories in the table product category.
	 */
	private Vector<String> allTaxCategories;
	/**
	 * The user currently using the AdminView
	 */
	private User user;
	
	private final String defaultSearchMessage = "Enter your search here...";

	/**
	 * Constructor for the SelfCheckOutAdministratorView.
	 */
	public SelfCheckOutAdministratorView() {
		super("Administrator View");

		try {
			this.db = new Database();
		} catch (JDCBDriverException ignore) {
		}
		this.resetTabs();
	}

	/**
	 * Constructor for the SelfCheckOutAdministratorView, including privilege.
	 * 
	 * @param privilige
	 *            true if administrator, false if employee
	 */
	public SelfCheckOutAdministratorView(User usr) {
		super("");

		StringBuilder toolbarText = new StringBuilder();
		if (usr.getPrivilege() == 1) { // administrator
			toolbarText.append("Administrator View: ");
		} else { // employee
			toolbarText.append("Employee View: ");
		}
		toolbarText.append(usr.fname);

		setTitle(toolbarText.toString());

		try {
			this.user = usr;
			this.db = new Database();
		} catch (JDCBDriverException ignore) {
		}
		this.resetTabs();
	}

	/**
	 * Reset each tab to synchronize the GUI's contents with the database. This
	 * method basically behaves like a refresh button.
	 * 
	 */
	public void resetTabs() {
		this.resetTabs("", "", "");
	}

	/**
	 * Reset each tab to synchronize the GUI's contents with the database. This
	 * method basically behaves like a refresh button.
	 * 
	 * @param message
	 *            The message to be printed to the text area; uses default text
	 *            if message is empty string or null.
	 * @param searchTable
	 *            The table that the search term is applied to.
	 * @param lastSearchTerm
	 *            The last search term for the table.
	 */
	public void resetTabs(String message, String searchTable,
			String lastSearchTerm) {
		int currentTab = 0;
		allProducts = db.getAllProducts();
		allTaxCategories = db.getAllTaxCategories();
		if (tabbedPane != null) {
			currentTab = tabbedPane.getSelectedIndex();
		}
		JPanel jpane = new JPanel(new GridLayout(1, 1));
		tabbedPane = createTabs(currentTab, searchTable, lastSearchTerm);
		jpane.add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		Container c = getContentPane();
		c.removeAll();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		c.add(new JSeparator(SwingConstants.VERTICAL));
		c.add(jpane);

		// output change to user
		messagesTextArea = new JTextArea();
		messagesTextArea.setFont(new Font("Serif", Font.PLAIN, 14));
		messagesTextArea.setLineWrap(true);
		messagesTextArea.setWrapStyleWord(true);
		messagesTextArea.setEditable(false);

		if (message == null || message == "") {
			messagesTextArea
					.setText("Note: The first column of every spreadsheet is not editable"
							+ " for database consistency reasons; use Add/Remove button"
							+ " instead.\nNote 2: \"Sale ID\" in \"Sales\" spreadsheet is for"
							+ " coding purposes; it is not editable and will increment"
							+ " automatically.\nNote 3: Select a row and press the \"Remove\" button to to remove it");
		} else {
			messagesTextArea.setText(message);
		}
		c.add(messagesTextArea);
		setSize(1000, 600);
		setVisible(true);
	}

	/**
	 * Create and return the tabs for the administrator GUI.
	 * 
	 * @param tabIndex
	 *            The index of the tab that starts with focus.
	 * @return JTabbedPane The tabs for administrator view GUI.
	 */
	private JTabbedPane createTabs(int tabIndex, String searchTable,
			String lastSearchTerm) {
		JTabbedPane tabbedPane = new JTabbedPane();
		ImageIcon icon = null; // no icon for the tabs currently

		// create tab for sqlite table productCategory
		JComponent productCategoryPanel = createPanel(Table.ProductCategory,
				searchTable, lastSearchTerm);
		tabbedPane.addTab("Tax Categories", icon, productCategoryPanel,
				"Tax Categories");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		// create tab for sqlite table BulkProduct
		JComponent bulkProductPanel = createPanel(Table.BulkProduct,
				searchTable, lastSearchTerm);
		tabbedPane.addTab("Bulk Products", icon, bulkProductPanel,
				"Bulk Products");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		// create tab for sqlite table PackagedProduct
		JComponent packagedProductPanel = createPanel(Table.PackagedProduct,
				searchTable, lastSearchTerm);
		tabbedPane.addTab("Packaged Products", icon, packagedProductPanel,
				"Packaged Products");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		// create tab for sqlite table Sale
		JComponent salePanel = createPanel(Table.Sale, searchTable,
				lastSearchTerm);
		salePanel.setPreferredSize(new Dimension(410, 50));
		tabbedPane.addTab("Sales", icon, salePanel, "Sales");
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

		tabbedPane.setSelectedIndex(tabIndex);
		return tabbedPane;
	}

	/**
	 * Return a String array that contains the IDs of the selected rows. To be
	 * used with removing selected rows in a table.
	 * 
	 * @param jtable
	 *            is the JTable
	 * @return String array of IDs
	 */
	private List<String> getSelectedRowValues(JTable jtable) {
		int i;
		int rowCount = jtable.getSelectedRowCount();
		int[] selectedRows = jtable.getSelectedRows();
		List<String> result = new ArrayList<String>();

		for (i = 0; i < rowCount; i++) {
			// get value at each row given from selectedRows, and in column 0
			// we always want column 0 because that is where the ID is
			result.add(jtable.getModel().getValueAt(selectedRows[i], 0)
					.toString());
		}

		return result;
	}

	/**
	 * Create and return the panel for a SQLite table.
	 * 
	 * @param Table
	 *            The SQLite table.
	 * @return JComponent The panel for the SQLite table.
	 */
	protected JComponent createPanel(final Table table, String searchTable,
			String lastSearchTerm) {
		boolean priv = false;
		if (user.getPrivilege() == 1) {
			priv = true;
		}
		final AdminTableModel atm = new AdminTableModel(this, table, db, priv);
		final JTable jtable = new JTable(atm);

		if (table == table.BulkProduct) {
			// add a drop down menu to bulk product's tax categories
			TableColumn statusColumn = jtable.getColumnModel().getColumn(3);
			JComboBox cbStatus = new JComboBox(allTaxCategories);
			statusColumn.setCellEditor(new DefaultCellEditor(cbStatus));
		} else if (table == table.PackagedProduct) {
			// add a drop down menu to pacakaged product's tax categories
			TableColumn statusColumn = jtable.getColumnModel().getColumn(4);
			JComboBox cbStatus = new JComboBox(allTaxCategories);
			statusColumn.setCellEditor(new DefaultCellEditor(cbStatus));
		}
		
		// make the grid lines appear
		jtable.setShowGrid(true);
		jtable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scrollTable = new JScrollPane(jtable);
		scrollTable.setColumnHeader(null);
		scrollTable.setMinimumSize(new Dimension(100, 80));
		// Specify that components should be laid out top to bottom
		Box tableBox = new Box(BoxLayout.Y_AXIS);
		tableBox.add(scrollTable);

		// Create add button, text field, and remove button
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gridConstraints = new GridBagConstraints();
		JButton addItemButton = new JButton("Add");
		final JComponent addNewEntryInput;
		if (table == table.Sale) {
			addNewEntryInput = new JComboBox(allProducts);
		} else {
			addNewEntryInput = new JTextField(25);
		}

		final JTextField searchEntryInput;
		searchEntryInput = new JTextField(defaultSearchMessage, 25);

		if (table.toString().equals(searchTable)) {
			searchEntryInput.setText(lastSearchTerm);
			atm.searchItem(lastSearchTerm);
		}
		JButton removeItemButton = new JButton("Remove");
		JLabel newRowTextLabel = new JLabel(getLabel(table));
		newRowTextLabel.setLabelFor(addNewEntryInput);
		
		if (user.getPrivilege() == 0) {
			removeItemButton.setEnabled(false);
			addItemButton.setEnabled(false);
		}
		
		// logout button
		JButton logOut = new JButton("Log Out");
		//SCO screen button
		JButton toSCO = new JButton("Click to go to Self Checkout");
		
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 0;
		gridConstraints.gridwidth = 3;
		gridConstraints.insets = new Insets(2, 3, 2, 2);
		gridConstraints.anchor = GridBagConstraints.EAST;
		gridConstraints.fill = GridBagConstraints.HORIZONTAL;
		
		p.add(searchEntryInput, gridConstraints);
		
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 1;
		gridConstraints.gridwidth = 1;
		p.add(newRowTextLabel, gridConstraints);
		
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 1;
		p.add(addNewEntryInput, gridConstraints);
		
		gridConstraints.gridx = 2;
		gridConstraints.gridy = 1;
		p.add(addItemButton, gridConstraints);
		
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 2;
		gridConstraints.gridwidth = 3;
		p.add(removeItemButton, gridConstraints);
		
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 3;
		gridConstraints.gridwidth = 1;
		p.add(toSCO, gridConstraints);
		
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 3;
		gridConstraints.gridwidth = 2;
		p.add(logOut, gridConstraints);
		
		tableBox.add(p);

		// action listener for add button
		addItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// an input box is either a text box or a drop down list
				if (addNewEntryInput instanceof JTextField) {
					atm.addRow(((JTextField) (addNewEntryInput)).getText());
				} else {
					String selectedText = ((JComboBox) (addNewEntryInput))
							.getSelectedItem().toString();
					if (table != Table.Sale) {
						atm.addRow(selectedText);
					} else {
						// Selected text is in format Code:Description
						// we slice the first part to get the code
						int endOfCodeSlice = selectedText.indexOf(':');
						String code = selectedText.substring(0, endOfCodeSlice);
						atm.addRow(code);
					}
				}
			}
		});
		// action listener for remove button
		removeItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// toRemove contains all the selected rows to remove
				List<String> toRemove = getSelectedRowValues(jtable);
				atm.removeRows(toRemove);
			}
		});
		// key listener for search input box; used for searching in real time
		searchEntryInput.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				JTextField textField = (JTextField) e.getSource();
				String searchTerm = textField.getText();
				atm.searchItem(searchTerm);
			}

			public void keyTyped(KeyEvent e) {

			}

			public void keyPressed(KeyEvent e) {
			}
		});
		
		//focus listener for search input box
		searchEntryInput.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				//erase the default text if focus is gained
				JTextField textField = (JTextField) e.getSource();
				if (textField.getText().equals(defaultSearchMessage)){
					textField.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				//set default message if search box not being used
				JTextField textField = (JTextField) e.getSource();
				if (textField.getText().isEmpty()) {
					textField.setText(defaultSearchMessage);
				}

			}
		});
		
		// function for logout
		logOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// exit back to the starterGUI
				dispose();
				new StarterGUI();
			}
		});
		
		// function for to SCO button
		toSCO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// exit to SCO
				dispose();
				new SelfCheckOutGUI();
			}
		});

		return tableBox;
	}
	
	/**
	 * Return a string that is used for the label of the text field of table.
	 * 
	 * @param table
	 * @return a string that is the label.
	 */
	/**
	 * @param table
	 * @return
	 */
	private String getLabel(Table table) {
		String label = "Input: ";
		switch (table) {
		case ProductCategory:
			label = "Input a new Taxable Category and press the \"Add\" button \nto add a new entry:";
			break;

		case BulkProduct:
			label = "Input a new BIC code and press the \"Add\" button \nto add a new Bulk Product: ";
			break;

		case PackagedProduct:
			label = "Input a new UPC code and press the \"Add\" button \nto add a new Packaged Product: ";
			break;

		case Sale:
			label = "Select a product and press the \"Add\" button \nto create a new sale: ";
			break;
		}
		return label;
	}
}
