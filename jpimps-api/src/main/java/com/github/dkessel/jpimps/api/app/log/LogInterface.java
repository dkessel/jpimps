package com.github.dkessel.jpimps.api.app.log;

public interface LogInterface {

	public void debug(String category, String text);

	public void error(String category, String text);

	public void info(String category, String text);

	public void warn(String category, String text);
}
