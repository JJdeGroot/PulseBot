package org.pulsebot.loader.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Creates the frame containing everything our client needs
 * @author JJ
 */
public class BotFrame extends JFrame {

	private static final long serialVersionUID = 3376926333205547092L;
	
	private BotTabs botTabs;
	
	/**
	 * Creates a frame containing everything our client needs
	 */
	public BotFrame(){
		super("PulseBot");
		setSize(790, 580);
		setLayout(new BorderLayout(0, 0));
        setResizable(false);
		botTabs = new BotTabs();
		add(botTabs, BorderLayout.NORTH);
	
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
}
