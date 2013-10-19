package org.pulsebot.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * The AccountManager gives the users an overview
 * where they can see all the details of their accounts
 * Users have the ability to add, edit and delete accounts
 * 
 * @author JJ
 *
 */
public class AccountManager extends JDialog {

	private static final long serialVersionUID = 6995290853085096278L;
	private final File accountFolder = new File(System.getProperty("user.home") + File.separator + "PulseBot" + File.separator + "Accounts");
	private final String[] columnNames = {"Username", "Password", "Pin", "Lamp skill"};
	private final String location = System.getProperty("user.home") + File.separator + "PulseBot" + File.separator + "Accounts" + File.separator;
	
	private JTable table;
	private JButton addButton, editButton, deleteButton;
	
	/** Creates the account manager frame */
	public AccountManager(){
        /*
		setTitle("Account Manager");
		setLayout(new BorderLayout(0, 0));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600, 200);
		
		// Loading accounts
		constructTable();
		
		// Button panel
		JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
		ClickListener clickListener = new ClickListener();
		
		addButton = new JButton("Add");
		addButton.addActionListener(clickListener);
		buttonPanel.add(addButton);
		
		editButton = new JButton("Edit");
		editButton.addActionListener(clickListener);
		buttonPanel.add(editButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(clickListener);
		buttonPanel.add(deleteButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
		                   */

	}
	
	/**
	 * Loads all accounts from the PulseBot account folder
	 * @return an ArrayList containg account files
	 */
	private ArrayList<File> loadAccounts(){
		System.out.println("Accounts location: " + location);
		ArrayList<File> fileList = new ArrayList<File>();
		
		// Checking if the account folder exists
		if(!accountFolder.exists()){
			accountFolder.mkdirs();
		}
		
		// Loading all files
		File[] files = accountFolder.listFiles();
		for(File file : files){
			// Checking if the file is a .txt file
			String fileName = file.getName();
			System.out.println("File name: " + fileName);
			
			if(fileName.endsWith(".txt")){
				System.out.println("Found account file!");
				fileList.add(file);
			}
		}
		
		return fileList;		
		
	}
	
	/** Constructs a JTable where all account details are displayed */
	private void constructTable(){
	    // Table model
	    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = 8913158579227467670L;
			public boolean isCellEditable(int nRow, int nCol) {
				return false;
	        }
	    };

		ArrayList<File> fileList = loadAccounts();
		for(int i = 0; i < fileList.size(); i++){
			File file = fileList.get(i);
			System.out.println("\nFile: " + file.getName());
			
			// Gathering the data to display in the row
			Object[] data = new Object[4];
			data[0] = file.getName().substring(0, file.getName().length()-4);
			try {
				Scanner scanner = new Scanner(file);
				int index = 1;
				while(scanner.hasNextLine() && index < data.length){
					String line = scanner.nextLine();
					System.out.println("Data: " + line);
					data[index] = line;
					index++;
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			tableModel.addRow(data);
		}
		
		// JTable settings		
		table = new JTable(tableModel);
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
		
		// Putting the table in a ScrollPane
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
	}

	/** Listens to clicks on one of the buttons */
	private class ClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			System.out.println("Button clicked!");
	
			Object source = ae.getSource();
			
			if(source.equals(addButton)){
				System.out.println("Add button clicked!");
				
				// Spawning the AccountEditor
				AccountEditor accountEditor = new AccountEditor(table, "Add account");
				accountEditor.setLocationRelativeTo(AccountManager.this);
				accountEditor.setVisible(true);
				
			}else if(source.equals(editButton)){
				System.out.println("Edit button clicked!");

				// Grabbing data from the table
				int row = table.getSelectedRow();
				Object[] values = new Object[table.getColumnCount()];
				for(int col = 0; col < values.length; col++){
					values[col] = table.getValueAt(row, col);  
		        } 
				
				// Spawning the AccountEditor
				AccountEditor accountEditor = new AccountEditor(table, "Edit account", values);
				accountEditor.setLocationRelativeTo(AccountManager.this);
				accountEditor.setVisible(true);

			}else if(source.equals(deleteButton)){
				System.out.println("Delete button clicked!");

				// Getting username and corresponding file
				int row = table.getSelectedRow();
				Object username = table.getValueAt(row, 0);
				File file = new File(location + username + ".txt");
				
				// Deleting file if it exists
				if(file.exists()){
					file.delete();
					DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
					tableModel.removeRow(row);
					System.out.println("Deleted account " + username);
				}
			}
		
		}
		
		
	}
	
}
