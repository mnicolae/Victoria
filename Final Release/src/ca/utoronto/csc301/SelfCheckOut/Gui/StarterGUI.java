/**
 * The GUI of the main menu.
 * 
 * @author Mihai Nicolae
 * 
 */
package ca.utoronto.csc301.SelfCheckOut.Gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StarterGUI extends JFrame implements ActionListener {
	/**
	 * Class serial version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Button for customer action
	 */
	protected JButton customerButton;
	/**
	 * Button for employee action
	 */
	protected JButton employeeButton;
	/**
	 * Button for administrator action
	 */
	protected JButton adminButton;
	/**
	 * Path to the home icon
	 */
	protected static final String HomeIconPath = "pic/home.png";
	/**
	 * Path to the shopping cart icon
	 */
	protected static final String ScoIconPath = "pic/shopping_cart.png";
	/**
	 * Text that will be displayed on top of the window.
	 */
	protected static final String windowTitle = "Main menu";
	/**
	 * Button tool-tips
	 */
	protected static final String customerTooltip = "Click this button to start the SelfCheckOut application";
	protected static final String employeeTooltip = "Click this button to start the login application for employees";
	protected static final String adminTooltip = "Click this button to start the login application for administrators";
	
	/**
	 * The constructor of StarterGUI. Instantiates all the elements within.
	 */
	public StarterGUI() {
		super(windowTitle);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(addPanel());
		
		// Display the window
		setLocationRelativeTo(null); // center frame
		setIconImage(Toolkit.getDefaultToolkit().getImage(HomeIconPath)); // add custom icon
		pack();
		setSize(500,200);
		setVisible(true);
	}
	
	/**
	 * Add all the components of the panel.
	 */
	private JPanel addPanel() {
		
		instantiateButtons();		
		JPanel mainPane = new JPanel(new BorderLayout());
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		mainPane.setLayout(gridbag);
		c.anchor = GridBagConstraints.EAST;
		
		c.insets = new Insets(10, 10, 10, 10);		
		c.weightx = GridBagConstraints.NONE;		
		c.gridx = 0;
		c.gridy = 1;
		mainPane.add(customerButton, c);
	
		c.gridx = 1;
		c.gridy = 1;
		mainPane.add(employeeButton, c);
	
		c.gridx = 2;
		c.gridy = 1;
		mainPane.add(adminButton, c);
		
		return mainPane;
	}

	/*
	 * Helper that instantiates the buttons. 
	 */
	private void instantiateButtons() {
		// Customer Button
		customerButton = new JButton("Customer");
		customerButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		customerButton.setHorizontalTextPosition(AbstractButton.CENTER);
		customerButton.setToolTipText(customerTooltip);
		customerButton.setActionCommand("customer");
		customerButton.addActionListener(this);

		// Employee Button
		employeeButton = new JButton("Employee");
		employeeButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		employeeButton.setHorizontalTextPosition(AbstractButton.CENTER);
		employeeButton.setToolTipText(employeeTooltip);
		employeeButton.setActionCommand("employee");
		employeeButton.addActionListener(this);
		
		// Administrator Button
		adminButton = new JButton("Administrator");
		adminButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		adminButton.setHorizontalTextPosition(AbstractButton.CENTER);
		adminButton.setToolTipText(adminTooltip);
		adminButton.setActionCommand("administrator");
		adminButton.addActionListener(this);
		
		Font font = new Font("Helvitica", Font.PLAIN, 18);
		customerButton.setFont(font);
		employeeButton.setFont(font);
		adminButton.setFont(font);
	}
	
	/**
	 * Define the behavior of the buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		try {
			// Customer action
			if ("customer".equals(e.getActionCommand())) {
				this.dispose(); // dispose of this window
				instantiateScoGUI(); // create new Self Check Out window
			} 
			// Employee action
			else if ("employee".equals(e.getActionCommand())) {
				this.dispose(); // dispose of this window
				instantiateEmployeeLogin(); // create new Login window
			}
			
			// Administrator action
			else if ("administrator".equals(e.getActionCommand())) {
				this.dispose(); // dispose of this window
				instantiateAdminLogin(); // create new Login window
			}
		} catch (Exception ex) {
		}
	}

	/**
	 * Create the SelfCheckOut GUI and show it. 
	 */
	private void instantiateScoGUI() {
		// Create and set up the window
		new SelfCheckOutGUI();
	}
	
	/*
	 * Create the login GUI for administrator.
	 */	
	private void instantiateAdminLogin() {
		// Create and set up the window
				JFrame frame = new JFrame("Self Check Out");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// Add content to the window
				frame.add(new Adminlogin());
				
				// Display the window
				frame.pack();
				frame.setVisible(true);
	}
	
	/*
	 * Create the login GUI for Employees.
	 */	
	private void instantiateEmployeeLogin() {
		// Create and set up the window
				JFrame frame = new JFrame("Self Check Out");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// Add content to the window
				frame.add(new EmployeeLogin());

				// Display the window
				frame.pack();
				frame.setVisible(true);
	}
	
	/**
	 * Show the Graphical User Interface for the main menu application
	 */
	public static void main(String[] args) {
		new StarterGUI();
	}
}
