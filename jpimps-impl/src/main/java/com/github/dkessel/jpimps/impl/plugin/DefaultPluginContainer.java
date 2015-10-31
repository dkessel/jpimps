package com.github.dkessel.jpimps.impl.plugin;

import java.io.*;
import java.util.*;

import com.github.dkessel.jpimps.api.app.*;
import com.github.dkessel.jpimps.api.app.log.*;
import com.github.dkessel.jpimps.api.plugin.*;

public class DefaultPluginContainer implements PluginContainerInterface {

	private static final String CAT = "PluginContainer";
	private final JPimpsApplicationInterface app;
	private final Map<String, PluginInterface> plugins;
	private final LogPluginInterface lpi;
	private Logger logger;

	private final File baseDir;
	private final File pluginDir;

	public DefaultPluginContainer(final JPimpsApplicationInterface app,
			final LogPluginInterface lpi) {
		this.app = app;
		this.lpi = lpi;
		plugins = new HashMap<String, PluginInterface>();
		this.baseDir = new File(".").getParentFile();
		this.pluginDir = new File(baseDir, "plugins");
		pluginDir.mkdirs();
	}

	@Override
	public void addPlugin(final PluginInterface plugin) {
		plugins.put(plugin.getUIName(), plugin);
	}

	@Override
	public PluginInterface getPlugin(final String name) {
		return plugins.get(name);
	}

	@Override
	public List<String> getPluginNames() {
		return new ArrayList<String>(plugins.keySet());
	}

	@Override
	public void shutdownAll() {
		for (final PluginInterface plugin : plugins.values()) {
			plugin.shutdown();
		}
	}

	@Override
	public void startAll() {
		this.logger = app.getLogger(CAT);
		for (final PluginInterface plugin : plugins.values()) {
			plugin.startUp();
		}

	}

	@Override
	public Object getPluginInterface(final Class<?> clazz) {
		if (clazz == LogPluginInterface.class) {
			return lpi;
		}

		logger.error("did not find plugin interface for class: " + clazz);
		return null;
	}

	@Override
	public File getWorkDirectoryForPlugin(Class<?> clazz) {
		final File workDir = new File(pluginDir, clazz.getPackage().getName());
		workDir.mkdirs();
		return workDir;
	}

}
