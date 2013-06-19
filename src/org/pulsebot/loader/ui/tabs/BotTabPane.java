package org.pulsebot.loader.ui.tabs;

import org.pulsebot.loader.Client;
import org.pulsebot.loader.ui.OverviewPanel;
import org.pulsebot.loader.utils.ClientPool;
import org.pulsebot.loader.utils.Utilities;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;

/**
 * Creates and handles the tabs for the client
 *
 * @author JJ
 */
public class BotTabPane extends JTabbedPane {

    private static final long serialVersionUID = 5485690562007244965L;
    private static final HashMap<Integer, Client> clients = ClientPool.getClients();
    private OverviewPanel overviewPanel;
    private int currentTab;

    /**
     * Constructors two Tabs
     */
    public BotTabPane() {
        overviewPanel = new OverviewPanel();
        setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

        // Listens to tab changes
        addChangeListener(new TabListener());

        // Creates and adds the "overview" tab
        insertTab("Overview", null, overviewPanel, "overview of all tabs", 0);

        // Creates and adds the "add button" tab
        addTab("+", null);
        JButton addButton = new BotButton("+");
        addButton.addActionListener(new AddListener());

        setTabComponentAt(1, addButton);
    }

    /**
     * Adds a Tab containing an instance of StartPanel
     */
    private void spawnTab() {
        synchronized (clients) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    System.out.println("Inner spawn tab started");
                    int count = getTabCount() - 1;

                    JPanel panel = new JPanel();

                    try {
                        Icon myPicture = new ImageIcon(new URL("http://puu.sh/36OZ5.gif"));
                        JLabel label = new JLabel(myPicture);
                        panel.add(label);
                    } catch (Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    ;
                    insertTab("Tab #" + count, null, panel, "switch to this tab", count);
                    setTabComponentAt(count, new BotTab(BotTabPane.this, "Tab #" + count));
                    setSelectedIndex(count);

                    Client client = new Client("http://oldschool38.runescape.com/", 765, 503, overviewPanel);
                    while (client.getCanvas() == null) {
                        Utilities.sleep(100);
                    }

                    panel.removeAll();
                    panel.add(client.getLoader());
                    revalidate();

                    clients.put(client.getCanvas().getClass().getClassLoader().hashCode(), client);
                    overviewPanel.updateAll();

                    System.out.println("Inner spawn tab done");
                }
            }).start();
        }
        System.out.println("Spawn tab done");

    }

    /**
     * Handles the addition of new tabs
     *
     * @author JJ
     */
    private class AddListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Clicked add");
            spawnTab();
        }

    }

    /**
     * Listens to tab changes
     *
     * @author JJ
     */
    private class TabListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            System.out.println("Tab changed!");
            int index = getSelectedIndex();
            System.out.println("Selected tab index: " + index);
        }
    }

}
