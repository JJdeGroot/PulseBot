package org.pulsebot.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.pulsebot.api.Skills;
import org.pulsebot.api.Skills.SKILLS;

/**
 * The AccountEditor is a frame where users
 * can type in the details for a new account
 * or edit an existing account.
 * 
 * @author JJ
 *
 */
public class AccountEditor extends JFrame {

	private static final long serialVersionUID = 1415892885770208798L;
	private final String location = System.getProperty("user.home") + File.separator + "PulseBot" + File.separator + "Accounts" + File.separator;
	
	private JTable table;
	private String oldUsername;
	private JTextField usernameField, passwordField, pinField;
	private JComboBox<Skills.SKILLS> lampBox;
	
	/** Makes a new AccountEditor with the desired name */
	public AccountEditor(JTable table, String name){
		super(name);
		this.table = table;
		
		setLayout(new GridLayout(9, 1));
		setSize(250, 250);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Username
		JLabel usernameLabel = new JLabel("Username:");
		add(usernameLabel);
		
		usernameField = new JTextField();
		add(usernameField);
		
		// Password
		JLabel passwordLabel = new JLabel("Password:");
		add(passwordLabel);

		passwordField = new JTextField();
		add(passwordField);

		// Pin
		JLabel pinLabel = new JLabel("PIN:");
		add(pinLabel);

		pinField = new JTextField();
		add(pinField);

		// Lamp skill
		JLabel lampLabel = new JLabel("Lamp skill:");
		add(lampLabel);

		lampBox = new JComboBox<Skills.SKILLS>(SKILLS.values());
		add(lampBox);
		
		// Save button
		JButton saveButton = new JButton(name);
		saveButton.addActionListener(new SaveListener());
		add(saveButton);
	}
	
	/** Makes a new AccountEditor and puts values in the fields */
	public AccountEditor(JTable table, String name, Object[] values){
		this(table, name);
		this.oldUsername = values[0].toString();
		
		usernameField.setText(oldUsername);
		passwordField.setText(values[1].toString());
		pinField.setText(values[2].toString());
		
		SKILLS[] skills = SKILLS.values();
		for(SKILLS skill : skills){
			if(skill.toString().equalsIgnoreCase(values[3].toString())){
				lampBox.setSelectedItem(skill);
				break;
			}
		}
	}
	
	/** Listens to clicks on the save button */
	private class SaveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			System.out.println("Save clicked!");
			
			// Username is atleast 1 character
			String username = usernameField.getText();
			if(username.length() == 0){
				usernameField.setForeground(Color.RED);
				return;
			}
			
			// Password is 5-20 characters
			String password = passwordField.getText();
			if(password.length() < 5 || password.length() > 20){
				passwordField.setForeground(Color.RED);
				return;
			}

			// Pin is 4 numbers
			String pin = pinField.getText();
			if(pin.length() != 4 || !pin.matches("\\d+")){
				pinField.setForeground(Color.RED);
				return;
			}
			
			// Skill
			String skill = lampBox.getSelectedItem().toString();
			Object[] data = new Object[] {username, password, pin, skill};
			
			// Saving to file
			File file = new File(location + username + ".txt");
			// If we are editing and the username changed, remove the old file
			if(oldUsername != null && !oldUsername.equals(username)){
				File oldFile = new File(location + oldUsername + ".txt");
				oldFile.delete();
			}
			
			// Writing data to the file
			try {
				PrintWriter pw = new PrintWriter(file);
				for(int i = 0; i < data.length; i++){
					pw.println(data[i]);
				}
				pw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			// Updating JTable in AccountManager
			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			if(oldUsername == null){ // Adding
				tableModel.insertRow(tableModel.getRowCount()-1, data);
			}else{ // Editing
				int row = table.convertRowIndexToModel(table.getSelectedRow());
				for(int i = 1; i < data.length; i++){
					tableModel.setValueAt(data[i], row, i);
				}
			}
			
			tableModel.fireTableDataChanged();
			dispose();
		}
		
		
	}
	
}
