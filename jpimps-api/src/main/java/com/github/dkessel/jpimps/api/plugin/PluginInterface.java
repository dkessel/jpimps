package com.github.dkessel.jpimps.api.plugin;

import java.awt.event.*;
import java.util.*;

public interface PluginInterface {

	public Map<String, ActionListener> getAvailableActions();

	public String getUIName();

	public void shutdown();

	void startUp();
}
