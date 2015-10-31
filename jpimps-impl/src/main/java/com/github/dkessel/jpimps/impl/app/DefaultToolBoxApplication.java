package com.github.dkessel.jpimps.impl.app;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import com.github.dkessel.jpimps.api.app.*;
import com.github.dkessel.jpimps.api.app.log.*;
import com.github.dkessel.jpimps.api.plugin.*;
import com.github.dkessel.jpimps.api.ui.*;
import com.github.dkessel.jpimps.impl.app.log.*;
import com.github.dkessel.jpimps.impl.plugin.*;
import com.github.dkessel.jpimps.util.*;

public class DefaultToolBoxApplication implements JPimpsApplicationInterface {

	private static final String CAT = "App";

	private final PluginContainerInterface pluginContainer;

	private ToolBoxUserInterface ui;
	private LogInterface log;

	private boolean running;

	public DefaultToolBoxApplication(final LogPluginInterface lpi) {
		this.running = false;
		this.pluginContainer = new DefaultPluginContainer(this, lpi);
	}

	@Override
	public Logger getLogger(final String category) {
		if (log == null) {
			throw new IllegalArgumentException("getlogger before init!");
		}
		return new SimpleLoggerAdapter(category, log);
	}

	@SuppressWarnings("hiding")
	@Override
	public void init(final ToolBoxUserInterface ui, final LogInterface log)
			throws Exception {
		this.ui = ui;
		this.log = log;

		final List<PluginInterface> plugins = loadPlugins();

		for (final PluginInterface currPlugin : plugins) {
			pluginContainer.addPlugin(currPlugin);
		}

	}

	private List<PluginInterface> loadPlugins() throws Exception {

		final List<PluginInterface> plugins = new ArrayList<PluginInterface>(5);

		final File pluginConfig = new File("cfg", "plugins.txt");
		log.debug(CAT,
				"loading plugin config:\n" + pluginConfig.getAbsolutePath());

		final BufferedReader reader = new BufferedReader(new FileReader(
				pluginConfig));
		String pluginName = null;
		while ((pluginName = reader.readLine()) != null) {
			pluginName = pluginName.trim();

			log.info(CAT, "loading plugin: " + pluginName);

			try {
				final Class<?> clazz = Class.forName(pluginName);
				final Constructor<?> constructor = clazz
						.getConstructor(JPimpsApplicationInterface.class);
				final PluginInterface newPlugin = (PluginInterface) (constructor
						.newInstance(this));
				plugins.add(newPlugin);
			} catch (final Exception e) {
				log.error(CAT, "Failed loading plugin: " + pluginName + "\n"
						+ Exceptions.toString(e));
			}

		}
		reader.close();

		log.info(CAT, "plugin count: " + plugins.size());

		return plugins;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void start() {
		log.debug(CAT, "starting application");
		running = true;
		ui.start();
		pluginContainer.startAll();
		log.debug(CAT, "started application");
	}

	@Override
	public void exit() {
		log.debug(CAT, "exiting application");
		running = false;
		pluginContainer.shutdownAll();
		ui.destroy();
		log.debug(CAT, "exit application done");
	}

	@Override
	public PluginContainerInterface getPluginContainer() {
		return pluginContainer;
	}

	@Override
	public ToolBoxUserInterface ui() {
		return ui;
	}
}
