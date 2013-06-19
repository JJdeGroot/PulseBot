package org.pulsebot.loader.ui.tabs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created with IntelliJ IDEA.
 * User: JJ
 * Date: 14-6-13
 * Time: 21:59
 * To change this template use File | Settings | File Templates.
 */
public class BotButton extends JButton {

    /**
     * Sets up the layout of a bot button
     *
     * @param text text to display
     */
    public BotButton(String text) {
        setFont(new Font("Tahoma", Font.BOLD, 10));
        setText(text);

        setFocusable(false);
        setMargin(new Insets(0, 0, 0, 0));
        setBorderPainted(false);
        setContentAreaFilled(false);

        addMouseListener(new HoverListener());
    }

    private class HoverListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setForeground(Color.RED);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setForeground(Color.BLACK);
        }
    }

}
