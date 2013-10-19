package org.pulsebot.ui;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * An ImageButton is a button that displays an image
 * and text. The image is loaded from the local resources
 * 
 * @author JJ
 */
public class ImageButton extends JButton {

	private static final long serialVersionUID = -2447998029362501959L;

	public ImageButton(String tooltip, String imageName){
		setToolTipText(tooltip);
		setOpaque(false);
		setBorderPainted(false); 
        setContentAreaFilled(false); 
        setFocusPainted(false); 
		ImageIcon icon = new ImageIcon(getClass().getResource("/resources/" + imageName));
		setIcon(icon);
	}
	
}
