package com.github.dkessel.jpimps.plugins.logbrowser;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import com.github.dkessel.jpimps.api.app.*;
import com.github.dkessel.jpimps.api.app.log.*;
import com.github.dkessel.jpimps.api.plugin.*;

public class LogBrowserPlugin implements PluginInterface {

	private final LogViewerFrame viewer;

	private final static String CAT = "LogBrowserPlugin";
	private final Map<String, ActionListener> actions;
	@SuppressWarnings("unused")
	private final JPimpsApplicationInterface app;
	private final Logger logger;

	private final class LogBrowserAction implements ActionListener {

		@SuppressWarnings("unused")
		private final JPimpsApplicationInterface app;
		private final LogPluginInterface lpi;

		private boolean initialized = false;

		public LogBrowserAction(final LogBrowserPlugin plugin,
				final LogPluginInterface lpi,
				final JPimpsApplicationInterface app) {
			this.app = app;
			this.lpi = lpi;
		}

		@Override
		public void actionPerformed(final ActionEvent e) {

			if (viewer == null) {
				return;
			}

			if (!initialized) {
				viewer.initWithFile(new File("jpimps.log"));
				initialized = true;
				final LogInterface logInterface = viewer
						.getTextComponentLogger();
				lpi.addLogTarget(logInterface);
				viewer.setVisible(true);
			} else {
				if (!viewer.isVisible()) {
					viewer.setVisible(true);
				}
				// see:
				// http://stackoverflow.com/questions/309023/howto-bring-a-java-window-to-the-front
				viewer.setExtendedState(viewer.getExtendedState()
						& ~Frame.ICONIFIED);
				viewer.setAlwaysOnTop(true);
				viewer.toFront();
				viewer.requestFocus();
				viewer.setAlwaysOnTop(false);
			}
		}
	}

	public LogBrowserPlugin(final JPimpsApplicationInterface app) {
		actions = new HashMap<String, ActionListener>();
		this.app = app;
		this.logger = app.getLogger(CAT);
		final Object pluginInterface = app.getPluginContainer()
				.getPluginInterface(LogPluginInterface.class);

		if (!(pluginInterface instanceof LogPluginInterface)) {
			logger.error("incompatible log interface: " + pluginInterface);
			viewer = null;
			return;
		}

		final LogPluginInterface lpi = (LogPluginInterface) pluginInterface;
		viewer = new LogViewerFrame(lpi, app);

		actions.put("Log Viewer", new LogBrowserAction(this, lpi, app));
	}

	@Override
	public Map<String, ActionListener> getAvailableActions() {
		return actions;
	}

	@Override
	public String getUIName() {
		return "LogBrowser";
	}

	@Override
	public void shutdown() {
		if (viewer != null) {
			viewer.dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.danielkessel.toolbox.plugin.PluginInterface#startUp()
	 */
	@Override
	public void startUp() {
		logger.info("starting...");

	}

}
