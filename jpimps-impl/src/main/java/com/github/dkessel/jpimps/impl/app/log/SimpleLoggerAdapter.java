package com.github.dkessel.jpimps.impl.app.log;

import com.github.dkessel.jpimps.api.app.log.*;
import com.github.dkessel.jpimps.util.*;

public class SimpleLoggerAdapter implements Logger {

	private final String category;
	private final LogInterface target;

	public SimpleLoggerAdapter(final String category, final LogInterface target) {
		this.category = category;
		this.target = target;
	}

	@Override
	public void debug(final String text) {
		target.debug(category, text);
	}

	@Override
	public void error(final String text) {
		target.error(category, text);
	}

	@Override
	public void info(final String text) {
		target.info(category, text);
	}

	@Override
	public void warn(final String text) {
		target.warn(category, text);

	}

	@Override
	public void debug(final String text, final Throwable t) {
		debug(text + "\n" + Exceptions.toString(t));
	}

	@Override
	public void error(final String text, final Throwable t) {
		error(text + "\n" + Exceptions.toString(t));
	}

	@Override
	public void info(final String text, final Throwable t) {
		info(text + "\n" + Exceptions.toString(t));
	}

	@Override
	public void warn(final String text, final Throwable t) {
		warn(text + "\n" + Exceptions.toString(t));
	}

}
