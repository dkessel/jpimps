package com.github.dkessel.jpimps.api.ui;

public interface ToolBoxUserInterface {

	public static final String NONE = "NONE";
	public static final String INFO = "INFO";
	public static final String ERROR = "ERROR";
	public static final String WARNING = "WARNING";

	public void destroy();

	public void start();

	public void displayMessage(String caption, String text, String type);

	public void setStatusText(String status);
}
