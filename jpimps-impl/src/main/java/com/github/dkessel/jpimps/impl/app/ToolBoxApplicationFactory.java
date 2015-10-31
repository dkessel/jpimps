package com.github.dkessel.jpimps.impl.app;

import com.github.dkessel.jpimps.api.app.*;
import com.github.dkessel.jpimps.api.app.log.*;

public class ToolBoxApplicationFactory {

	/**
	 * Creates a new default {@link JPimpsApplicationInterface} implementation.
	 * 
	 * @param lpi
	 *            - {@link LogPluginInterface}
	 * 
	 * @return {@link JPimpsApplicationInterface}
	 */
	public static JPimpsApplicationInterface createDefaultToolBoxApplication(
			final LogPluginInterface lpi) {
		return new DefaultToolBoxApplication(lpi);
	}
}
