package com.github.dkessel.jpimps.api.plugin;

import java.io.*;
import java.util.*;

public interface PluginContainerInterface {

	public void addPlugin(PluginInterface plugin);

	public List<String> getPluginNames();

	public PluginInterface getPlugin(String name);

	public File getWorkDirectoryForPlugin(Class<?> clazz);
	
	public void shutdownAll();

	public void startAll();

	public Object getPluginInterface(Class<?> clazz);
}
