package com.github.dkessel.jpimps.plugins.memory;

import java.awt.event.*;
import java.util.*;

import com.github.dkessel.jpimps.api.app.*;
import com.github.dkessel.jpimps.api.app.log.*;
import com.github.dkessel.jpimps.api.plugin.*;
import com.github.dkessel.jpimps.api.ui.*;

public class MemoryPlugin implements PluginInterface {

	private static final String CAT = "MemoryPlugin";

	@SuppressWarnings("unused")
	private final JPimpsApplicationInterface app;
	private final Logger logger;
	private final Map<String, ActionListener> actions;

	public MemoryPlugin(final JPimpsApplicationInterface app) {
		this.app = app;
		this.logger = app.getLogger(CAT);
		actions = new HashMap<String, ActionListener>(2);

		actions.put("Garbage Collection", new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				Runtime.getRuntime().gc();
				Runtime.getRuntime().gc();
				Runtime.getRuntime().gc();
			}
		});

		actions.put("Show Memory Info", new ActionListener() {

			private static final int MB = 1024 * 1024;

			@Override
			public void actionPerformed(final ActionEvent e) {
				final long freeMem = Runtime.getRuntime().freeMemory() / MB;
				final long totalMem = Runtime.getRuntime().totalMemory() / MB;
				final long usedMem = totalMem - freeMem;
				final double usedPercentage = (usedMem / (double) totalMem) * 100d;

				final StringBuilder sb = new StringBuilder();
				sb.append("Total memory: " + totalMem + " MB\n");
				sb.append("Used memory: " + usedMem + " MB\n");
				sb.append("Free memory: " + freeMem + " MB\n");
				sb.append("Used: " + usedPercentage + " %\n");
				sb.append("Available processors: "
						+ Runtime.getRuntime().availableProcessors());

				logger.info("Memory Information:\n\n" + sb.toString());
				app.ui().displayMessage("Memory Information", sb.toString(),
						ToolBoxUserInterface.INFO);
			}
		});
	}

	@Override
	public Map<String, ActionListener> getAvailableActions() {
		return Collections.unmodifiableMap(actions);
	}

	@Override
	public String getUIName() {
		return "Memory Plugin";
	}

	@Override
	public void shutdown() {
		/**/
	}

	@Override
	public void startUp() {
		logger.info("starting...");
	}

}
