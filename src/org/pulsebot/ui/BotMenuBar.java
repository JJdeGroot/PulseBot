package org.pulsebot.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class BotMenuBar extends JMenuBar {

	private static final long serialVersionUID = -3436445134620686202L;

	// Debug menu
	private JCheckBoxMenuItem debugPlayerItem, debugNPCItem;
	
	public BotMenuBar(){
		// DEBUG MENU
		JMenu debugMenu = new JMenu("Debug");
		DebugListener debugListener = new DebugListener();
		
		// Player
		debugPlayerItem = new JCheckBoxMenuItem("Players");
		debugPlayerItem.addActionListener(debugListener);
		debugMenu.add(debugPlayerItem);
		
		// Npc
		debugNPCItem = new JCheckBoxMenuItem("NPCs");
		debugNPCItem.addActionListener(debugListener);
		debugMenu.add(debugNPCItem);
		
		add(debugMenu);
	}
	
	private class DebugListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent ae) {
			System.out.println("Item clicked!");
			
			Object source = ae.getSource();
			if(source.equals(debugPlayerItem)){
				System.out.println("Clicked on debug player");
			}else if(source.equals(debugNPCItem)){
				System.out.println("Clicked on debug NPC");
			}
			
		}
		
	}
	
	
	
}
