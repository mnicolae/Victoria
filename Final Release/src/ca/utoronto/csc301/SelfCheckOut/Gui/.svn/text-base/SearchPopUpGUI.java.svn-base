package ca.utoronto.csc301.SelfCheckOut.Gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;

/**
 * GUI for search box
 */
public class SearchPopUpGUI extends JFrame implements ActionListener, ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Toggle between simple search view and advanced search view
	 */
	protected JCheckBox toggleAdvancedButton;
	
	/**
	 * JPanels for underlying panel, view settings, simple search,
	 * advanced search
	 */
	protected JPanel basePanel, viewSettingPanel, simpleSearchPanel,
			advancedSearchPanel;

	/**
	 * TODO change this for final release
	 * The default text that will be displayed when search box is opened.
	 */
	protected String outputTextBoxDefaultMsg =
			"Search result goes here or maybe select matching cells" +
			" in AdminView spreadsheet";
	
	/**
	 * Label for the general search box in both simple and advanced search view
	 */
	protected String generalSearchLabel = "Search For";
	
	/**
	 * Search button that's used in both simple and advanced search view
	 */
	protected JButton searchButton;

	/**
	 * The custom JComboBox search bar in simple search view
	 */
	protected JComboBox simpleSearchBar;
	
	/**
	 * A list of Strings to be displayed in search suggestion
	 */
	protected Vector<String> searchSuggest = new Vector<String>();

	/**
	 * Search text field in advanced search view
	 */
	protected JTextField advSearchTextField;
	
	/**
	 * Selection menu in advanced search view
	 */
	protected JComboBox advSearchList;

	/**
	 * The types of search that can be performed in advanced view; listed in
	 * the selection menu "advSearchList"
	 */
	// TODO change to real table names in SCO.db and display user friendly
	// names from AdminTableModel.mapColumnNames()
	protected String[] searchType =
		{ 	"Product Description", "BIC", "UPC",
			"Sale Percent Discount (%)", "etc..." };

	/**
	 * Scrollable text box for displaying search results
	 */
	protected JScrollPane textBoxScrollPane;
	
	/**
	 * Text box used for displaying search result
	 */
	protected JTextArea outputTextBox;
	
	/**
	 * Create and display the pop-up search window
	 */
	public SearchPopUpGUI() {
		super("Search Box");

		// Don't use DISPOSE_ON_CLOSE so that previous view setting is retained
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		int sizeX = 700;
		int sizeY = 500;
		setPreferredSize(new Dimension(sizeX, sizeY));
		
		
		// Set up panels
		basePanel = new JPanel();
		this.add(basePanel);
		basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));
		basePanel.setVisible(true);
		
		viewSettingPanel = createviewSettingPanel();
		viewSettingPanel.setVisible(true);
		// setMaximumSize for formatting relative size of each panel
		viewSettingPanel.setMaximumSize(new Dimension(sizeX, 1));
		basePanel.add(viewSettingPanel);
		
		simpleSearchPanel = createSimpleSearchPanel();
		simpleSearchPanel.setVisible(true);
		// setMaximumSize for formatting relative size of each panel
		simpleSearchPanel.setMaximumSize(new Dimension(sizeX, 1));
		basePanel.add(simpleSearchPanel);
		
		advancedSearchPanel = createAdvancedSearchPanel();
		advancedSearchPanel.setVisible(false);
		// setMaximumSize for formatting relative size of each panel
		advancedSearchPanel.setMaximumSize(new Dimension(sizeX, 1));
		basePanel.add(advancedSearchPanel);
		
		textBoxScrollPane = createOutputTextBoxScrollPane();
		textBoxScrollPane.setVisible(true);
		// doesn't need setMaximumSize; take up rest of space
		basePanel.add(textBoxScrollPane);

		
		pack();
		setVisible(true);
	}
	
	/**
	 * Redisplay the search box if the user previously closed it
	 */
	public void openSearchBox() {
		outputTextBox = new JTextArea(outputTextBoxDefaultMsg);
		this.setVisible(true);
	}

	/**
	 * Create the JPanel for objects associated with search settings.
	 * Currently there is only 1 setting.
	 * @return the JPanel object
	 */
	private JPanel createviewSettingPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		// Set up check box
		toggleAdvancedButton = new JCheckBox("Advanced View", false);
		toggleAdvancedButton.addItemListener(this);
		panel.add(toggleAdvancedButton);
		
		return panel;
	}
	
	/**
	 * Create the JPanel for objects associated with simple search view
	 * @return the JPanel object
	 */
	private JPanel createSimpleSearchPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Simple Search"));
		panel.setLayout(new GridBagLayout());
		
		// Custom JComboBox search bar; allows for search suggestion
		simpleSearchBar = new JComboBox(searchSuggest);
		simpleSearchBar.setEditable(true);
		simpleSearchBar.setActionCommand("simpleSearch");
		simpleSearchBar.addActionListener(this);
		
		// code by Markus Jevring from http://stackoverflow.com/questions/
		// 822432/how-to-make-jcombobox-look-like-a-jtextfield
		
		simpleSearchBar.setUI(new BasicComboBoxUI() {
		    @Override
		    protected JButton createArrowButton() {
		    	return new JButton() {
		    		@Override
		    		public int getWidth() {
		    			return 0;
		    		}
		    	};
		    }
		});
		

		// code by Scott Faria from http://stackoverflow.com/questions/
		// 8949466/detecting-jcombobox-editing
		final Component editor = simpleSearchBar
				.getEditor().getEditorComponent();
		
	    if (editor instanceof JTextField) {
	        Document doc = ((JTextField) editor).getDocument();
	        doc.addDocumentListener(new DocumentListener() {
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					// User changed font style of text; irrelevant in this case
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					// User typed a new character in search bar; handle this
					// in updateSearchSuggest()
					updateSearchSuggest(((JTextField) editor).getText());
				}

				@Override
				public void removeUpdate(DocumentEvent arg0) {
					// User deleted text from search bar
					updateSearchSuggest(((JTextField) editor).getText());
				}
	        });                                      
	    } else {
	    	throw new RuntimeException("Java Swing implementation changed");
	    }
		
	    
		// Search bar label
		JLabel simpleSearchFieldLabel = new JLabel(generalSearchLabel + ": ");
		simpleSearchFieldLabel.setLabelFor(simpleSearchBar);
		
		// Search button
		searchButton = new JButton("Search");
		searchButton.setVerticalTextPosition(AbstractButton.CENTER);
		searchButton.setHorizontalTextPosition(AbstractButton.CENTER);
		searchButton.setActionCommand("simpleSearch");
		searchButton.addActionListener(this);
		
		// Search panel layout
		GridBagConstraints gc = new GridBagConstraints();

		// Row 1
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0.0;
		panel.add(simpleSearchFieldLabel, gc);
		
		gc.gridx = 1;
		gc.weightx = 0.5;
		panel.add(simpleSearchBar, gc);
		
		gc.gridx = 2;
		gc.weightx = 0.25;
		panel.add(searchButton, gc);
		
		
		return panel;
	}

	/**
	 * Create the JPanel for objects associated with advanced search view
	 * @return the JPanel object
	 */
	private JPanel createAdvancedSearchPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Advanced Search"));
		panel.setLayout(new GridBagLayout());
		
		// Text field
		advSearchTextField = new JTextField(20);
		advSearchTextField.setActionCommand("advSearch");
		advSearchTextField.addActionListener(this);

		// Search text field label
		JLabel advsSearchFieldLabel = new JLabel(generalSearchLabel + ": ");
		advsSearchFieldLabel.setLabelFor(advSearchTextField);
		
		// Selection box
		advSearchList = new JComboBox(searchType);
		
		// Search button
		searchButton = new JButton("Search");
		searchButton.setVerticalTextPosition(AbstractButton.CENTER);
		searchButton.setHorizontalTextPosition(AbstractButton.CENTER);
		searchButton.setActionCommand("advSearch");
		searchButton.addActionListener(this);
		
		// Search panel layout
		GridBagConstraints gc = new GridBagConstraints();

		// Row 1
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0.0;
		panel.add(advsSearchFieldLabel, gc);
		
		gc.gridx = 1;
		gc.weightx = 0.5;
		panel.add(advSearchTextField, gc);

		gc.gridx = 2;
		gc.weightx = 0.25;
		panel.add(advSearchList, gc);

		gc.gridx = 3;
		panel.add(searchButton, gc);
		
		return panel;
		
	}

	/**
	 * Create the JScrollPane for the search result output text box
	 * @return the JScrollPane object
	 */
	private JScrollPane createOutputTextBoxScrollPane() {
		outputTextBox = new JTextArea(outputTextBoxDefaultMsg);
		outputTextBox.setFont(new Font("Serif", Font.PLAIN, 15));
		outputTextBox.setLineWrap(true);
		outputTextBox.setWrapStyleWord(true);
		outputTextBox.setEditable(false);
		
		// Add scroll to the text area
		JScrollPane textBoxScrollPane = new JScrollPane(outputTextBox);
		textBoxScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		textBoxScrollPane.setBorder(
				BorderFactory.createTitledBorder("Search Result"));
		
		return textBoxScrollPane;
	}
	
	/**
	 * Updates the list of search suggestions in custom JComboBox search bar
	 * based on the text that are currently in the search bar.
	 */
	private void updateSearchSuggest(String input) {
		input = input.toLowerCase();
		System.out.println("\"" + input + "\"");
		
		// prevents UP/DOWN arrow key from "selecting" the item in the menu and
		// cause it to "move" around the menu instead
		// code by Rob Camick from
		// http://tips4java.wordpress.com/2009/05/17/combo-box-no-action
		simpleSearchBar.putClientProperty("JComboBox.isTableCellEditor", true);
		
		// TODO potential GUI bug where user mouse-overs something and then erase text field
		// To duplicate bug:
		// type "bb" -> mouse-over one search suggestion -> hold Backspace until empty -> type "a"
		// source of problem: interaction between selection through keyboard and JComboBox
		// also problems with interaction between mouse-over and JComboBox
		
		// this is only a simple example, we will use SQL queries
		// instead of IF-ELSE statements in final release
		if (input.isEmpty()) {
			searchSuggest.clear();
			simpleSearchBar.hidePopup();
			
		} else if (input.startsWith("a")) {
			searchSuggest.clear();
			searchSuggest.add("apple");
			searchSuggest.add("avocado");
			searchSuggest.add("awesome");


			simpleSearchBar.hidePopup();
			simpleSearchBar.showPopup();
			
		} else if (input.startsWith("b")) {
			searchSuggest.clear();
			
			searchSuggest.add("banana");
			searchSuggest.add("Broccoli");

			simpleSearchBar.hidePopup();
			simpleSearchBar.showPopup();
			
		} else if (input.startsWith("what")) {
			searchSuggest.clear();
			
			searchSuggest.add("What is the answer to life, the universe, and everything");

			simpleSearchBar.hidePopup();
			simpleSearchBar.showPopup();
			
		} else {
			// unrecognised input, show empty search suggestion
			searchSuggest.clear();
			
			searchSuggest.add("Unrecognized input...");

			simpleSearchBar.hidePopup();
			simpleSearchBar.showPopup();
		}
		
	}
	
	/**
	 * 
	 * @param input - the text that user searched for
	 * @param type - column name (or empty String if not applicable)
	 * @return String containing data of what the user searched for
	 */
	private String queryDatabase(String input, String type) {
		// TODO change to real SQL queries, do something about type
		return "\nYou searched for: \"" + input + "\" under the category \"" +
				type + "\"";
	}
	
	/**
	 * Output text box will automatically scroll to bottom; creating a
	 * method for this will save a few lines of code
	 * @param msg - the message to be displayed in the text box
	 */
	private void writeToTextBox(String msg) {
		outputTextBox.append("\n" + msg);
		outputTextBox.setCaretPosition(outputTextBox.getDocument().getLength());
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();
		
		if (action == "advSearch") {
			String output = queryDatabase(advSearchTextField.getText(), 
					(String) advSearchList.getSelectedItem());
			
			writeToTextBox(output);
			advSearchTextField.setText("");
			
		} else if (action == "simpleSearch") {
			// user pressed the Enter key, selected one of the search
			// suggestions, or pressed the Search button
			String input = (String) simpleSearchBar.getSelectedItem();
			
			if (input == null || input.isEmpty()) {
				return;
			}
			
			// TODO maybe write guessColumnName(input) for "String type" arg
			// of queryDatabase such as "type = BIC if input.size() == 5"
			writeToTextBox(queryDatabase(input, "UNDETERMINED"));
			
			if (input == "What is the answer to life, the universe, and everything") {
				writeToTextBox("\n42");
			}
			
			Component editor = simpleSearchBar.getEditor().getEditorComponent();
			((JTextField) editor).setText("");

			simpleSearchBar.setSelectedIndex(-1);
			searchSuggest.clear();
			simpleSearchBar.hidePopup();
			
		}
	}

	@Override
	public void itemStateChanged(ItemEvent ie) {
		Object obj = ie.getItemSelectable();
		
		if (obj == toggleAdvancedButton) {
			// Change to simple/advanced search view depending on whether
			// or not toggleAdvancedB button is selected.
			
			advSearchTextField.setText("");
			Component editor = simpleSearchBar.getEditor().getEditorComponent();
			((JTextField) editor).setText("");
			searchSuggest.clear();
			
			if (toggleAdvancedButton.isSelected()) {
				simpleSearchPanel.setVisible(false);
				advancedSearchPanel.setVisible(true);
			} else {
				simpleSearchPanel.setVisible(true);
				advancedSearchPanel.setVisible(false);
			}
		}
	}

	/**
	 * Show the pop-up search box.
	 * TODO remove main() after integration with SelfCheckOutAdministratorView
	 */
	public static void main(String[] args) {
		// note that the previous simple/advanced view is remembered even if
		// the search pop-up window is closed
		new SearchPopUpGUI();
	}


}
