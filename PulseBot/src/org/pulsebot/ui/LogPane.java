package org.pulsebot.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.pulsebot.utils.Utilities;

/**
 * The LogPane is a pane where the user can find the
 * script and bot logs.
 * 
 * @author JJ
 */
public class LogPane extends JPanel {

	private static final long serialVersionUID = -4924845103824625416L;
	private JTextArea botPane, scriptPane;

	public LogPane(){
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(getWidth(), 125));
		
		// Tabs
		JTabbedPane tabPane = new JTabbedPane();
		redirectSystemStreams();

		// Bot text panel
		botPane = new JTextArea();
		botPane.setLineWrap(true);
		botPane.setEditable(false);
		JScrollPane botScrollPane = new JScrollPane(botPane);
		tabPane.addTab("Bot log", null, botScrollPane, "Show bot log");
		
		// Script text panel
		scriptPane = new JTextArea();
		scriptPane.setLineWrap(true);
		scriptPane.setEditable(false);
		JScrollPane scriptScrollPane = new JScrollPane(scriptPane);
		tabPane.addTab("Script log", null, scriptScrollPane, "Show script log");

		add(tabPane, BorderLayout.CENTER);
	}

	private void updateBotPane(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Document doc = botPane.getDocument();
					doc.insertString(doc.getLength(), "[" + Utilities.getFormattedTime() + "]\t" + text, null);
					botPane.setCaretPosition(doc.getLength() - 1);
				} catch (BadLocationException e) {
					throw new RuntimeException(e);
				}
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
