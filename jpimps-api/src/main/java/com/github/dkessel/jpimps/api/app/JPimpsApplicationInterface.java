package com.github.dkessel.jpimps.api.app;

import com.github.dkessel.jpimps.api.app.log.LogInterface;
import com.github.dkessel.jpimps.api.app.log.Logger;
import com.github.dkessel.jpimps.api.plugin.PluginContainerInterface;
import com.github.dkessel.jpimps.api.ui.ToolBoxUserInterface;

public interface JPimpsApplicationInterface {

	/**
	 * Returns a {@link Logger} for a given log category name.
	 * 
	 * @param category - the log category name
	 * @return {@link Logger}
	 */
	public Logger getLogger(String category);

	public ToolBoxUserInterface ui();

	public void init(ToolBoxUserInterface ui, LogInterface log)
			throws Exception;

	public void start();

	public boolean isRunning();

	public void exit();

	public PluginContainerInterface getPluginContainer();
}
