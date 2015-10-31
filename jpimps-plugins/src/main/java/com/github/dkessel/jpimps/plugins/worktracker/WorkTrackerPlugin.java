package com.github.dkessel.jpimps.plugins.worktracker;

import java.awt.event.*;
import java.io.*;
import java.util.*;

import com.github.dkessel.jpimps.api.app.*;
import com.github.dkessel.jpimps.api.app.log.*;
import com.github.dkessel.jpimps.api.plugin.*;
import com.github.dkessel.jpimps.api.ui.*;

public class WorkTrackerPlugin implements PluginInterface {

	private static final String CAT = "WorkTrackerPlugin";
	private final JPimpsApplicationInterface app;
	private final Logger logger;

	private WorkingDay wd;
	private Thread t;

	private final File pluginDir;

	public WorkTrackerPlugin(final JPimpsApplicationInterface app) {
		this.app = app;
		this.logger = app.getLogger(CAT);
		this.pluginDir = this.app.getPluginContainer()
				.getWorkDirectoryForPlugin(this.getClass());
	}

	@Override
	public Map<String, ActionListener> getAvailableActions() {
		final HashMap<String, ActionListener> hashMap = new HashMap<String, ActionListener>();
		final ActionListener showTimeAction = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				app.ui().displayMessage(
						"WorkTracker",
						wd.toString() + "\nTSV: "
								+ wd.toTSV().replace("\t", "; "),
						ToolBoxUserInterface.INFO);
			}
		};
		hashMap.put("Show Work Time", showTimeAction);
		return hashMap;
	}

	@Override
	public String getUIName() {
		return "WorkTracker";
	}

	@Override
	public void shutdown() {
		if ((t != null) && t.isAlive()) {
			t.interrupt();
		}
	}

	@Override
	public void startUp() {
		logger.info("starting...");
		wd = new WorkingDay();
		t = new Thread(new WorkTrackerRunnable(app, wd, pluginDir),
				"WorkTracker");
		t.start();
	}

}
