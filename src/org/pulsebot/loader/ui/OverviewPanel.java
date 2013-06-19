package org.pulsebot.loader.ui;

import org.pulsebot.loader.Client;
import org.pulsebot.loader.utils.ClientPool;
import org.pulsebot.loader.utils.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Set;

public class OverviewPanel extends JPanel {

    private static final long serialVersionUID = 5060519844144818836L;

    private HashMap<Integer, Client> clients;
    private HashMap<Integer, JLabel> labels;
    private int width = 1, height = 1;


    public OverviewPanel() {
        setPreferredSize(new Dimension(765, 503));
        setBackground(new Color(0, 102, 255));
        setDoubleBuffered(true);
        updateAll();
    }

    private ImageIcon resize(BufferedImage image, int number) {
        Graphics2D g2d = image.createGraphics();
        // text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Verdana", Font.BOLD, 40));
        g2d.drawString("Tab #" + number, 5, 40);
        // border
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(0, 0, image.getWidth(), image.getHeight());

        Image resizedImage = Utilities.scaleImage(getWidth() / height, getHeight() / width, image);
        return new ImageIcon(resizedImage);
    }

    public void updateAll() {
        removeAll();

        clients = ClientPool.getClients();
        labels = new HashMap<Integer, JLabel>();

        int size = clients.size();
        while (width * height < size) {
            if (width < height) {
                width++;
            } else {
                height++;
            }
        }

        setLayout(new GridLayout(width, height));

        Set<Integer> keys = clients.keySet();
        int tabNumber = 1;
        for (int key : keys) {
            Client client = clients.get(key);
            ImageIcon icon = resize(client.getGameBuffer(), tabNumber++);
            JLabel label = new JLabel(icon);
            labels.put(key, label);
            add(label);
        }

        revalidate();
    }

    public void update(int hashCode) {
        Set<Integer> keys = clients.keySet();
        int tabNumber = 1;

        for (int key : keys) {
            if (key == hashCode) {
                JLabel label = labels.get(hashCode);
                Client client = clients.get(hashCode);
                ImageIcon icon = resize(client.getGameBuffer(), tabNumber++);
                if (icon != null && label != null) {
                    label.setIcon(icon);
                    label.revalidate();
                }
            }
            tabNumber++;
        }
    }

}
