package org.pulsebot.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.pulsebot.script.Script;
import org.pulsebot.script.ScriptManifest;
import org.pulsebot.script.ScriptThread;
import org.pulsebot.utils.RSClassLoader;

/**
 * The ScriptSelector gives the users an overview
 * where they can see all the scripts they have added
 * to PulseBot. 
 * 
 * If the Scripter has added the ScriptManifest
 * the user can immediately see the category, author, name,
 * description and version of the script.
 * 
 * If the ScriptManifest is not present the script will still
 * show up with only it's name.
 * 
 * @author JJ
 *
 */
public class ScriptSelector extends JDialog {

	private static final long serialVersionUID = -7791653382057604102L;
	private final File location = new File(System.getProperty("user.home") + File.separator + "PulseBot" + File.separator + "Scripts");
	private final String[] columnNames = {"Category", "Author", "Name", "Description", "Version"};
	
	private Script script;

	private ArrayList<Class<?>> scripts;
	private JTable table;
	private JButton startButton;
	private JTextField searchField;
	
	// Action buttons
	private ImageButton botStartButton, botPauseButton, botStopButton, botRepeatButton;
	
	public ScriptSelector(ImageButton botStartButton, ImageButton botPauseButton, ImageButton botStopButton, ImageButton botRepeatButton){
		setTitle("Script Selector");
		setLayout(new BorderLayout(0, 0));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600, 300);
		
		this.botStartButton = botStartButton;
		this.botPauseButton = botPauseButton;
		this.botStopButton = botStopButton;
		this.botRepeatButton = botRepeatButton;
		
		// Loading scripts
		scripts = loadScripts();
		System.out.println("Loaded scripts: " + scripts);

		// Constructing JTable to display all scripts
		constructTable();
		
		// Start Button
		startButton = new JButton("Start script");
		startButton.setToolTipText("Starts the selected script");
		startButton.addActionListener(new StartListener());
		add(startButton, BorderLayout.SOUTH);
		
		// Search field
		searchField = new JTextField();
		searchField.setToolTipText("Search through the table");
		searchField.getDocument().addDocumentListener(new SearchListener());
		add(searchField, BorderLayout.NORTH);
	}
	
	/**
	 * Returns the last ran script
	 * @return last ran script
	 */
	public Script getScript(){
		return script;
	}
	
	/**
	 * Loads all class files from the PulseBot script folder that
	 * are PulseBot scripts. A class is considered as PulseBot script
	 * when it extends org.pulsebot.script.Script
	 * @return an ArrayList containg Scripts
	 */
	private ArrayList<Class<?>> loadScripts(){
		System.out.println("Script location: " + location);
		
		// Checking if the script folder exists
		if(!location.exists()){
			location.mkdirs();
		}
		
		// Loading all files in the script location
		ArrayList<Class<?>> scripts = new ArrayList<Class<?>>();
		File[] files = location.listFiles();
		for(File file : files){
			// Checking if the file is a .class file
			String fileName = file.getAbsolutePath();
			System.out.println("\nFile name: " + fileName);
			
			if(fileName.endsWith(".class")){
				try {
					// Retrieving the full name of the class (includes package)
					RSClassLoader rsClassLoader =  new RSClassLoader();
					String fullName = rsClassLoader.getFullClassName(fileName);
					System.out.println("Full name: " + fullName);

					// Loading the class
					Class<?> clazz = rsClassLoader.loadClass(fullName);
					System.out.println("Loaded class: " + clazz);

					// Checking if the class extends Script
					String superName = clazz.getSuperclass().getName();
					System.out.println("Super name: " + superName);
					if(superName.equals("org.pulsebot.script.Script")){
						System.out.println("Found script: " + file.getName());
						scripts.add(clazz);
					}
				} catch (NoClassDefFoundError nc) {
					String message = nc.getMessage();
					System.out.println("Did not find class: " + message);
					String name = message.substring(message.indexOf("name: ") + 6, message.length()-1);
					System.out.println("Full class name: " + name);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        
			}
		}

		return scripts;
	}

	
	/** Constructs a JTable */
	private void constructTable(){
		Object[][] data = new Object[scripts.size()][columnNames.length];
		for(int i = 0; i < data.length; i++){
			Class<?> clazz = scripts.get(i);
	
			// Checking if the ScriptManifest is present
			Annotation[] annotations = clazz.getAnnotations();
			for(Annotation annotation : annotations){
				String type = annotation.annotationType().getName();
				System.out.println("Annotation type: " + type);
				if(type.equals("org.pulsebot.script.ScriptManifest")){
					ScriptManifest manifest = (ScriptManifest) annotation;
					System.out.println("Found ScriptManifest: " + manifest);
					
					data[i][0] = manifest.category();
					data[i][1] = manifest.author();
					data[i][2] = manifest.name();
					data[i][3] = manifest.description();
					data[i][4] = manifest.version();

					break;
				}
			}
			
			// Adding class name if no script manifest present 
			if(data[i][2] == null){
				data[i][2] = clazz.getSimpleName();
			}
		}
		
		// Creates a new JTable that can't be edited
		table = new JTable(data, columnNames) {
			private static final long serialVersionUID = 6296047786870589412L;
			public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
	    };

		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);

		// Putting the table in a ScrollPane
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void startScript(){
		try {
			script = (Script) script.getClass().newInstance();
			
			botStartButton.setEnabled(false);
			botPauseButton.setEnabled(true);
			botStopButton.setEnabled(true);
			botRepeatButton.setEnabled(false);
	
	    	ScriptThread scriptThread = new ScriptThread(script);
			scriptThread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Listens to typing in the search field */
	private class SearchListener implements DocumentListener {
		
		/** Handles the updating of the JTable */
		private void handleUpdate(){
			table.clearSelection();

			String text = searchField.getText().toLowerCase();
			System.out.println("\n\nTyped text: " + text);
			
			// Selecting rows that contain the search query text
			if(text.length()  > 0){
				for (int row = 0; row < table.getRowCount(); row++) {
	                for (int col = 0; col < table.getColumnCount(); col++) {
	                	Object obj = table.getValueAt(row, col);
	                	if(obj instanceof String){
	                		String value = (String) obj;
	                		if(value.toLowerCase().contains(text)){
	                			System.out.println("Found text " + text + " in " + value);
	                			table.addRowSelectionInterval(row, row);
	                		}
	                	}
	                }
	            }
			}
		}
		
		@Override
		public void changedUpdate(DocumentEvent e) {
			handleUpdate();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			handleUpdate();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			handleUpdate();
		}
	}
	
	/** Listens to clicks on the start button */
	private class StartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("The start button has been clicked!");
			
			int row = table.getSelectedRow() != -1 ? table.convertRowIndexToModel(table.getSelectedRow()) : -1;
			if(row != -1){
				Class<?> clazz = scripts.get(row);
			    System.out.println("Selected script: " + clazz);
		
			    try {
			    	script = (Script) clazz.newInstance();
			    	startScript();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			    
			    dispose();
			}
		}
	}
	
	
}
