package com.github.dkessel.jpimps.impl.ui;

import com.github.dkessel.jpimps.api.app.*;
import com.github.dkessel.jpimps.api.ui.*;

public class ToolBoxUIFactory {

	/**
	 * Creates a new tray icon-based {@link ToolBoxUserInterface}.
	 * 
	 * @param app
	 * 
	 * @return {@link ToolBoxUserInterface}
	 */
	public static ToolBoxUserInterface createToolBoxUITray(
			final JPimpsApplicationInterface app) {
		return new ToolBoxUITray(app);
	}
}
