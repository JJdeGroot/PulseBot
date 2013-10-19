package org.pulsebot.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.pulsebot.script.Script;

/**
 * The ActionBar is a bar where the user can perform 
 * multiple actions such as starting scripts, debugging
 * information and changing settings.
 * 
 * @author JJ
 */
public class ActionBar extends JMenuBar {

	private static final long serialVersionUID = -3436445134620686202L;

	// Debug menu
	private JCheckBoxMenuItem debugPlayerItem, debugNPCItem;
	// Tools menu
	private JMenuItem toolsManagerItem;
	// Action buttons
	private ImageButton startButton, pauseButton, stopButton, repeatButton;

	private ScriptSelector scriptSelector;

	public ActionBar(){
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(getWidth(), 20));

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
		
		add(debugMenu, BorderLayout.WEST);
		
		// TOOLS MENU
		JMenu toolsMenu = new JMenu("Tools");
		ToolsListener toolsListener = new ToolsListener();
		
		// Account Manager
		toolsManagerItem = new JMenuItem("Account Manager");
		toolsManagerItem.addActionListener(toolsListener);
		toolsMenu.add(toolsManagerItem);
		
		add(toolsMenu, BorderLayout.CENTER);

		// ACTION BUTTONS
		JPanel actionPanel = new JPanel(new GridLayout(1, 4));
		ButtonListener buttonListener = new ButtonListener();
		
		// Start
		startButton = new ImageButton("Start script", "start_icon.png");
		startButton.addActionListener(buttonListener);
		actionPanel.add(startButton);
		
		// Pause
		pauseButton = new ImageButton("Pause script", "pause_icon.png");
		pauseButton.setEnabled(false);
		pauseButton.addActionListener(buttonListener);
		actionPanel.add(pauseButton);
		
		// Stop
		stopButton = new ImageButton("Stop script", "stop_icon.png");
		stopButton.setEnabled(false);
		stopButton.addActionListener(buttonListener);
		actionPanel.add(stopButton);
		
		// Repeat
		repeatButton = new ImageButton("Repeat script", "repeat_icon.png");
		repeatButton.setEnabled(false);
		repeatButton.addActionListener(buttonListener);
		actionPanel.add(repeatButton);

		add(actionPanel, BorderLayout.EAST);
	}
	
	/** Listens to the debug menu */
	private class DebugListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent ae) {
			System.out.println("Debug item clicked!");
			
			Object source = ae.getSource();
			if(source.equals(debugPlayerItem)){
				System.out.println("Clicked on debug player");
			}else if(source.equals(debugNPCItem)){
				System.out.println("Clicked on debug NPC");
			}
			
		}
		
	}
	
	/** Listens to the tools menu */
	private class ToolsListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent ae) {
			System.out.println("Tools item clicked!");
			
			Object source = ae.getSource();
			if(source.equals(toolsManagerItem)){
				System.out.println("Clicked on account manager");
                System.out.println("Account Manager disabled at this time.");
				// Showing the account manager
				//AccountManager am = new AccountManager();
				//am.setLocation(getLocationOnScreen());
				//am.setVisible(true);
				
			}
			
		}
		
	}
	
	/** Listens to the action buttons */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			System.out.println("Action button clicked!");

			Object source = ae.getSource();
			if(source.equals(startButton)){
				System.out.println("Clicked on start button");

				scriptSelector = new ScriptSelector(startButton, pauseButton, stopButton, repeatButton);
				scriptSelector.setLocation(getLocationOnScreen());
				scriptSelector.setVisible(true);
				
			}else if(source.equals(pauseButton)){
				System.out.println("Clicked on pause button");
				
				if(scriptSelector != null){
					Script script = scriptSelector.getScript();
					if(script.isPaused()){
						pauseButton.setText("Pause script");
					}else{
						pauseButton.setText("Resume script");
					}
					script.pause();
				}
				
			}else if(source.equals(stopButton)){
				System.out.println("Clicked on stop button");

				if(scriptSelector != null){
					startButton.setEnabled(true);
					pauseButton.setEnabled(false);
					stopButton.setEnabled(false);
					repeatButton.setEnabled(true);
					
					
					Script script = scriptSelector.getScript();
					script.onFinish();
					
					Set<Thread> threads = Thread.getAllStackTraces().keySet();
					for(Thread thread : threads){
						System.out.println("Thread name: " + thread.getName());
						if(thread.getName().equals("Script")){
							System.out.println("Interrupting script thread!");
							thread.interrupt();
						}
					}
					script.stop();
					
					
				}
				
			}else if(source.equals(repeatButton)){
				System.out.println("Clicked on repeat button");
				if(scriptSelector != null){
					scriptSelector.startScript();
				}
				
			}
			
			
		}
		
	}
	
	
	
}
