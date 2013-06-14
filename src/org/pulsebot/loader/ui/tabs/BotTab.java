package org.pulsebot.loader.ui.tabs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Creates and adds a close button to a tab
 * and handles the actions performed on it
 * @author JJ
 */
public class BotTab extends JPanel {

	private static final long serialVersionUID = -3776747096077166405L;
	
	private JTabbedPane pane;

    /**
     * Creates a new tab with a close button
     * @param pane the jtabbedpane parent
     * @param title title of the label
     */
    public BotTab(JTabbedPane pane, String title) {
        // Remove gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.pane = pane;
        setOpaque(false);

        // Creating and adding the name label
        JLabel label = new JLabel(title);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        add(label);

        // Creating and adding the close button
        JButton closeButton = new BotButton("x");
        closeButton.addActionListener(new CloseListener());
        add(closeButton);
    }

    /** Listens to clicks on the close button */
    private class CloseListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Closing tab");
            int i = pane.indexOfTabComponent(BotTab.this);
            if (i != -1) {
                pane.remove(i);
            }
        }

    }

}