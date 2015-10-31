package com.github.dkessel.jpimps.api.app.log;

public interface Logger {

	public void debug(String text);

	public void debug(String text, Throwable t);

	public void error(String text);

	public void error(String text, Throwable t);

	public void info(String text);

	public void info(String text, Throwable t);

	public void warn(String text);

	public void warn(String text, Throwable t);

}
