package org.pulsebot.loader.ui;

import org.pulsebot.loader.Client;
import org.pulsebot.loader.ClientApplet;
import org.pulsebot.loader.types.Language;
import org.pulsebot.loader.types.Version;
import org.pulsebot.loader.utils.ClientPool;
import org.pulsebot.loader.utils.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Creates an interactable panel
 * @author JJ
 */
public class StartPanel extends JPanel {

	private static final HashMap<Integer, Client> clients = ClientPool.getClients();
	private static final long serialVersionUID = 6769431865853054803L;

	private JComboBox<Version> versionBox;
	private JComboBox<Language> languageBox;
	private ClientApplet clientApplet = null;
	private OverviewPanel overviewPanel;
	
	/** 
	 * Creates a panel with two combo boxes for the user to
	 * choose what they want to load up.
	 */
	public StartPanel(OverviewPanel overviewPanel){
		this.overviewPanel = overviewPanel;
		setSize(300, 300);
		setLayout(new GridBagLayout());

		// Click Listener
		ClickListener clickListener = new ClickListener();
		
		// Constraints
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
	
		// RuneScape version
		JLabel versionLabel = new JLabel("RuneScape version");
		gbc.insets = new Insets(-150, 0, 0, 0);
		add(versionLabel, gbc);
		
		versionBox = new JComboBox<Version>(Version.values());
		gbc.insets = new Insets(-100, 0, 0, 0);
		versionBox.addActionListener(clickListener);
		add(versionBox, gbc);
		
		// RuneScape language
		JLabel languageLabel = new JLabel("RuneScape language");
		gbc.insets = new Insets(0, 0, 0, 0);
		add(languageLabel, gbc);
		
		languageBox = new JComboBox<Language>(Language.values());
		gbc.insets = new Insets(50, 0, 0, 0);
		add(languageBox, gbc);
		
		// Start
		JButton startButton = new JButton("Start");
		gbc.insets = new Insets(150, 0, 0, 0);
		startButton.addActionListener(clickListener);
		add(startButton, gbc);

	}
	
	/**
	 * Adds a client
	 * @param client client to add
	 */
	public void addClient(Client client){
		System.out.println("Adding client! [StartPanel]");
		synchronized (clients) {
			clients.put(client.getCanvas().getClass().getClassLoader().hashCode(), client);
		}
		overviewPanel.updateAll();
	}

	/**
	 * Destroys the applet if we are displaying one
	 */
	public void destructApplet(){
		if(clientApplet != null){
			clientApplet.destruct();
		}
	}

	/**
	 * Listens to the start button
	 * @author JJ
	 */
	private class ClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();
			if(obj instanceof JButton){
				System.out.println("Start button pressed!");
				Version version = Version.values()[versionBox.getSelectedIndex()];
				Language language = Language.values()[languageBox.getSelectedIndex()];
				System.out.println("Chosen version: " + version + ", chosen language: " + language);
				
				removeAll();

				System.out.println("Creating client");
				Client client = new Client("http://oldschool38.runescape.com/", 765, 553, overviewPanel);
				while(client.getCanvas() == null){
					System.out.println("Waiting for canvas @ StartPanel");
					Utilities.sleep(500);
				}
				System.out.println("adding client @ StartPanel");
				addClient(client);
				
				System.out.println("Adding client applet");
				clientApplet = client.getLoader();
				
				add(clientApplet);
				revalidate();
			}else{
				System.out.println("Version changed!");
				
				Version version = Version.values()[versionBox.getSelectedIndex()];
				System.out.println("Selected value: " + version);
				languageBox.setEnabled(version.equals(Version.NORMAL));
			}
			
			
		}
		
		
		
	}
	
	
	
	
}
