package com.github.dkessel.jpimps.plugins.logbrowser;

import java.awt.*;
import java.io.*;

import javax.swing.*;

import com.github.dkessel.jpimps.api.app.*;
import com.github.dkessel.jpimps.api.app.log.*;

public class LogViewerFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private final LogInterface tfLogger;

	private final JTextArea textField;
	@SuppressWarnings("unused")
	private final JPimpsApplicationInterface app;
	private final static String CAT = "LogViewerFrame";
	private final Logger logger;

	public LogViewerFrame(final LogPluginInterface logInterface,
			final JPimpsApplicationInterface app) {
		super("Log Viewer");
		this.app = app;
		this.logger = app.getLogger(CAT);
		setLayout(new BorderLayout(5, 5));

		setBackground(Color.BLACK);
		textField = new JTextArea();
		textField.setEditable(false);
		textField.setBackground(Color.BLACK);
		textField.setForeground(Color.GREEN);
		textField.setLineWrap(true);
		textField.setWrapStyleWord(true);
		tfLogger = new TextBoxLogInterface(textField);
		final JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);
		setDefaultCloseOperation(HIDE_ON_CLOSE);

		final Rectangle bounds = getGraphicsConfiguration().getBounds();
		setSize((int) (bounds.width * 0.9), (int) (bounds.height * 0.9));
		// to center on screen:
		setLocationRelativeTo(null);
	}

	public LogInterface getTextComponentLogger() {
		return tfLogger;
	}

	public void initWithFile(final File file) {
		final StringBuilder sb = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			while (bufferedReader.ready()) {
				sb.append(bufferedReader.readLine() + "\n");
			}

			textField.setText(sb.toString());
		} catch (final Exception e) {
			logger.error("failed reading log file: " + e, e);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					logger.error("failed closing file: " + file, e);
				}
			}
		}

	}
}
