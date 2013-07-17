package org.pulsebot;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;

import org.pulsebot.injection.generic.RSClient;
import org.pulsebot.ui.BotLogger;
import org.pulsebot.ui.BotMenuBar;

public class PulseBot extends JFrame {

	private static final long serialVersionUID = 5964969367854549246L;

	/**
	 * d.y = player name
	 * client.ga = player array
	 * Updater logs: http://javahacking.org/forum/index.php?/forum/49-updater-logs/
	 */
	
	public PulseBot(){
		setTitle("PulseBot");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Fixing JMenu appearing under Applet
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

		// Showing a custom screen
		ImageIcon icon = new ImageIcon(getClass().getResource("/resources/boot_pulse.gif"));
		JLabel label = new JLabel(icon, JLabel.CENTER);
		add(label, BorderLayout.CENTER);
		
		// ToolBar
		BotMenuBar botMenuBar = new BotMenuBar();
		add(botMenuBar, BorderLayout.NORTH);
		
		// Debug log
		BotLogger botLogger = new BotLogger();
		add(botLogger, BorderLayout.SOUTH);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		System.out.println("PulseBot has been started!\n");
		
		// Loading and adding the client
		RSClient client = new RSClient();
		remove(label);
		add(client, BorderLayout.CENTER);
		revalidate();
	}
	
	
	public static void main(String[] args){
		// Start bot
		PulseBot bot = new PulseBot();
	}
	
	
}
