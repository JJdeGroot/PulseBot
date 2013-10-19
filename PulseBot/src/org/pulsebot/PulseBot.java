package org.pulsebot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.pulsebot.ui.LogPane;
import org.pulsebot.ui.ActionBar;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;

public class PulseBot extends JFrame {

	private static final long serialVersionUID = 5964969367854549246L;

	public PulseBot() {
		super("PulseBot");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Logo
		ImageIcon logo = new ImageIcon(getClass().getResource("/resources/small_logo.png"));
		setIconImage(logo.getImage());
	
		// Action bar
		ActionBar actionBar = new ActionBar();
		add(actionBar, BorderLayout.NORTH);

		// Center panel
		JPanel panel = new JPanel(new BorderLayout(0, 0));
		
		// Log pane
		LogPane logPane = new LogPane();
		panel.add(logPane, BorderLayout.CENTER);

		// Boot image label
		ImageIcon icon = new ImageIcon(getClass().getResource("/resources/boot_pulse.gif"));
		JLabel label = new JLabel(icon);
		panel.add(label, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		
		// Showing
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		// Loading RuneScape
		Client client = new Client();
		panel.remove(label);
		panel.add(client, BorderLayout.NORTH);
		revalidate();
	}
	
	
	public static void main(String[] args){
		// Fixes the layout
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

		try {
			// Theme gray/blue
			/*
			NimRODTheme theme = new NimRODTheme();
			theme.setPrimary1(new Color(79, 150, 235));
			theme.setPrimary2(new Color(89, 160, 245));
			theme.setPrimary3(new Color(99, 170, 255));
			theme.setSecondary1(new Color(184, 184, 184));
			theme.setSecondary2(new Color(194, 194, 194));
			theme.setSecondary3(new Color(204, 204, 204));
			theme.setBlack(new Color(20, 20, 20));
			theme.setWhite(new Color(200, 200, 200));
			theme.setMenuOpacity(200);
			theme.setFrameOpacity(200);
			theme.setFont(new Font("Verdana", Font.PLAIN, 10));
			*/
			// Theme gray/blue
			NimRODTheme theme = new NimRODTheme();
			theme.setPrimary1(new Color(74, 148, 217));
			theme.setPrimary2(new Color(79, 153, 222));
			theme.setPrimary3(new Color(84, 158, 227));
			theme.setSecondary1(new Color(194, 194, 194));
			theme.setSecondary2(new Color(204, 204, 204));
			theme.setSecondary3(new Color(214, 214, 214));
			theme.setBlack(new Color(30, 30, 30));
			theme.setWhite(new Color(200, 200, 200));
			theme.setMenuOpacity(200);
			theme.setFrameOpacity(200);
			theme.setFont(new Font("Verdana", Font.PLAIN, 10));

			// Look and feel
			NimRODLookAndFeel feel = new NimRODLookAndFeel();
			NimRODLookAndFeel.setCurrentTheme(theme);
			UIManager.setLookAndFeel(feel);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Running
		PulseBot bot = new PulseBot();
	}
	
	
}
