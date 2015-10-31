package com.github.dkessel.jpimps.impl.app.log;

import java.util.*;

import com.github.dkessel.jpimps.api.app.log.*;

public class MultiLogger implements LogInterface, LogPluginInterface {

	private final LogInterface primary;
	private final List<LogInterface> secondary;

	public MultiLogger(final LogInterface primary) {
		this.primary = primary;
		this.secondary = new ArrayList<LogInterface>();
	}

	@Override
	public void debug(final String category, final String text) {
		primary.debug(category, text);

		for (final LogInterface log : secondary) {
			log.debug(category, text);
		}
	}

	@Override
	public void error(final String category, final String text) {
		primary.error(category, text);
		for (final LogInterface log : secondary) {
			log.error(category, text);
		}
	}

	@Override
	public void info(final String category, final String text) {
		primary.info(category, text);
		for (final LogInterface log : secondary) {
			log.info(category, text);
		}
	}

	@Override
	public void warn(final String category, final String text) {
		primary.warn(category, text);
		for (final LogInterface log : secondary) {
			log.warn(category, text);
		}
	}

	@Override
	public void addLogTarget(final LogInterface logInterface) {
		primary.debug("MultiLogger", "add log target: " + logInterface);
		secondary.add(logInterface);
	}

}
