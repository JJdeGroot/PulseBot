package org.pulsebot.loader.ui;

import org.pulsebot.loader.ui.tabs.BotTabPane;

import javax.swing.*;
import java.awt.*;

/**
 * Creates the frame containing everything our client needs
 * @author JJ
 */
public class BotFrame extends JFrame {

	private static final long serialVersionUID = 3376926333205547092L;
	
	private BotTabPane botTabs;
	
	/**
	 * Creates a frame containing everything our client needs
	 */
	public BotFrame(){
		super("PulseBot");

        // Size
        setSize(790, 580);
        setLayout(new BorderLayout(0, 0));

        // Tabs
        botTabs = new BotTabPane();
        add(botTabs, BorderLayout.NORTH);

        // Location
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
}
