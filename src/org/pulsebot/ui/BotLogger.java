package org.pulsebot.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class BotLogger extends JPanel {

	private static final long serialVersionUID = -4924845103824625416L;
	private JTabbedPane tabPane;
	private JTextPane botPane, scriptPane;
	
	
	public BotLogger(){
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(getWidth(), 100));
		
		// Tabs
		tabPane = new JTabbedPane();
		
		// Bot text panel
		botPane = new JTextPane();
		botPane.setEditable(false);
		botPane.setToolTipText("Displays the bot log");
		JScrollPane botScrollPane = new JScrollPane(botPane);
		tabPane.addTab("Bot log", botScrollPane);
		
		// Script text panel
		scriptPane = new JTextPane();
		scriptPane.setEditable(false);
		scriptPane.setToolTipText("Displays the script log");
		JScrollPane scriptScrollPane = new JScrollPane(scriptPane);
		tabPane.addTab("Script log", scriptScrollPane);

		add(tabPane, BorderLayout.CENTER);
		
		//redirectSystemStreams();
	}
	
	
	private void updateBotPane(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Document doc = botPane.getDocument();
				try {
					doc.insertString(doc.getLength(), text, null);
				} catch (BadLocationException e) {
					throw new RuntimeException(e);
				}
				botPane.setCaretPosition(doc.getLength() - 1);
			}
		});
	}

	public void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(final int b) throws IOException {
				updateBotPane(String.valueOf((char) b));
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				updateBotPane(new String(b, off, len));
			}

			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}
		};

		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
	}
	
}
