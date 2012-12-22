/*
 * GUI for creating new User 
 * @author Nisarg Patel
 */
package ca.utoronto.csc301.SelfCheckOut.Gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ca.utoronto.csc301.SelfCheckOut.App.Database;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.DatabaseConnectionException;
import ca.utoronto.csc301.SelfCheckOut.Exceptions.JDCBDriverException;

public class CreateNewUserGui extends JFrame {
	// container for login window
	private Container pane;

	// layout manager for window
	private GridBagConstraints gridConstraints;

	// Label for username
	private JLabel unameLabel;
	// Label for password
	private JLabel passLabel;
	// Label for actual name of user
	private JLabel nameLabel;
	// Label for privilege
	private JLabel privilegeLabel;

	// Text field for username
	private JTextField unameField;
	// Text field for actual name of user
	private JTextField nameField;
	// Text field for password
	private JPasswordField passwordField;

	// set of Radio option for privilege
	private JRadioButton employeeRadio;
	private JRadioButton adminRadio;

	// error message
	private JLabel errorMsgLabel;

	// Button to create user
	private JButton addUserButton;

	// width of login screen
	private int WIDTH = 500;

	// height of login screen
	private int HEIGHT = 400;

	public CreateNewUserGui() {
		super("Create new User");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		// instantiate components
		nameLabel = new JLabel(" Name ");
		nameField = new JTextField(10);
		unameField = new JTextField(10);
		unameLabel = new JLabel(" Username ");
		passLabel = new JLabel(" Password ");
		passwordField = new JPasswordField(10);
		privilegeLabel = new JLabel(" Privilege ");
		employeeRadio = new JRadioButton(" Employee ", true);
		adminRadio = new JRadioButton(" Administrator ");
		addUserButton = new JButton(" Create ");
		errorMsgLabel = new JLabel("*username already exists!");
		// setting radio buttons
		ButtonGroup privilegegroup = new ButtonGroup();
		privilegegroup.add(employeeRadio);
		privilegegroup.add(adminRadio);

		// setting font
		Font font = new Font("Helvitica", Font.PLAIN, 18);
		nameLabel.setFont(font);
		unameLabel.setFont(font);
		passLabel.setFont(font);
		privilegeLabel.setFont(font);
		employeeRadio.setFont(font);
		adminRadio.setFont(font);
		nameField.setFont(font);
		passwordField.setFont(font);
		unameField.setFont(font);
		addUserButton.setFont(font);
		// setting layout
		pane = getContentPane();
		pane.setLayout(new GridBagLayout());
		gridConstraints = new GridBagConstraints();
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 1;
		gridConstraints.gridwidth = 1;
		gridConstraints.gridheight = 1;
		gridConstraints.insets = new Insets(2, 3, 2, 2);
		gridConstraints.fill = GridBagConstraints.BOTH;
		// adding components to panel

		pane.add(nameLabel, gridConstraints);
		gridConstraints.gridwidth = 10;
		gridConstraints.gridx = 2;
		pane.add(nameField, gridConstraints);
		gridConstraints.gridwidth = 1;
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 2;
		pane.add(unameLabel, gridConstraints);
		gridConstraints.gridwidth = 10;
		gridConstraints.gridx = 2;
		pane.add(unameField, gridConstraints);
		gridConstraints.gridwidth = 1;
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 3;
		pane.add(passLabel, gridConstraints);
		gridConstraints.gridwidth = 10;
		gridConstraints.gridx = 2;
		pane.add(passwordField, gridConstraints);
		gridConstraints.gridwidth = 1;
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 4;
		pane.add(privilegeLabel, gridConstraints);
		gridConstraints.gridx = 2;
		pane.add(employeeRadio, gridConstraints);
		gridConstraints.gridx = 3;
		pane.add(adminRadio, gridConstraints);
		gridConstraints.gridx = 3;
		gridConstraints.gridy = 5;
		pane.add(addUserButton, gridConstraints);
		gridConstraints.gridwidth = 10;
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 6;
		setVisible(true);

		Handler handler = new Handler();
		addUserButton.addActionListener(handler);

	}

	/*
	 * This class is Event Handler for createNewUserGui.
	 */

	private class Handler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String username, name;
			char[] clearPassword;
			String password;
			int privilege;
			pane.remove(errorMsgLabel);
			try {
				Database db = new Database();
				username = unameField.getText();
				clearPassword = passwordField.getPassword();
				password = (new User()).encryptPassword(clearPassword);
				name = nameField.getText();
				if (adminRadio.isSelected()) {
					privilege = 1;
				} else {
					privilege = 0;
				}
				String query = "INSERT INTO UserAccounts VALUES ('" + username
						+ "','" + password + "','" + name + "'," + privilege
						+ ")";
				db.updateDatabase(query);
				dispose();
				new StarterGUI();

			} catch (JDCBDriverException e) {

			} catch (SQLException e) {
				pane.add(errorMsgLabel, gridConstraints);
				setVisible(true);
			}

		}

	}

}