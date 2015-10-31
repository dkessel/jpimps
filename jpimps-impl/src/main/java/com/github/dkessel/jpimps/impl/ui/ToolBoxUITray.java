package com.github.dkessel.jpimps.impl.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import com.github.dkessel.jpimps.api.app.*;
import com.github.dkessel.jpimps.api.app.log.*;
import com.github.dkessel.jpimps.api.plugin.*;
import com.github.dkessel.jpimps.api.ui.*;

public class ToolBoxUITray implements ToolBoxUserInterface {

	private static final String CAT = "TrayUI";

	private final JPimpsApplicationInterface app;
	private Logger logger;

	private TrayIcon trayIcon;

	private Image defaultIcon;
	private Image infoIcon;
	private Image warnIcon;
	private Image errorIcon;

	public ToolBoxUITray(final JPimpsApplicationInterface app) {
		this.app = app;
	}

	@Override
	public void destroy() {
		if (trayIcon != null) {
			SystemTray.getSystemTray().remove(trayIcon);
		}
		logger.debug("destroyed");
	}

	@Override
	public void start() {
		this.logger = app.getLogger(CAT);
		// defaultIcon = loadImage("tray-question.png");
		defaultIcon = loadImage("toolbox.gif");
		infoIcon = loadImage("info.png");
		warnIcon = loadImage("warning.png");
		errorIcon = loadImage("error.png");

		if (defaultIcon == null) {
			JOptionPane.showMessageDialog(null,
					"error loading the default image!", "error!",
					JOptionPane.ERROR_MESSAGE);
		}

		if ((defaultIcon == infoIcon) && (warnIcon == errorIcon)
				&& (defaultIcon == null) && (warnIcon == null)) {
			JOptionPane.showMessageDialog(null, "error loading the images!",
					"error!", JOptionPane.ERROR_MESSAGE);
		}

		final PopupMenu menu = new PopupMenu();
		trayIcon = new TrayIcon(defaultIcon, "ToolBox", menu);
		trayIcon.setImageAutoSize(true);
		final ActionListener doubleClickListener = new DoubleClickListener(app);
		trayIcon.addActionListener(doubleClickListener);

		final ActionListener exitListener = new ExitListener(app);

		final ArrayList<String> pluginNames = new ArrayList<String>(app
				.getPluginContainer().getPluginNames());
		Collections.sort(pluginNames);

		for (final String pluginName : pluginNames) {

			final PluginInterface plugin = app.getPluginContainer().getPlugin(
					pluginName);

			final Map<String, ActionListener> actions = plugin
					.getAvailableActions();

			if (actions.isEmpty()) {
				logger.debug("installing dummy UI section for plugin: "
						+ pluginName);
				final MenuItem menuItem = new MenuItem(pluginName);
				menuItem.setEnabled(false);
				menu.add(menuItem);
			}

			for (final String actionName : actions.keySet()) {
				final ActionListener actionListener = actions.get(actionName);

				final MenuItem pluginAction = new MenuItem(actionName);
				pluginAction.addActionListener(actionListener);
				menu.add(pluginAction);
			}
			menu.add("-");

		}

		final MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(exitListener);
		menu.add(exitItem);

		if (SystemTray.isSupported()) {
			final SystemTray tray = SystemTray.getSystemTray();

			try {
				tray.add(trayIcon);

			} catch (final AWTException e) {
				System.err.println("TrayIcon could not be added.");
				trayIcon.displayMessage("error",
						"TrayIcon could not be added.",
						TrayIcon.MessageType.ERROR);
			}

			trayIcon.displayMessage(null, "ToolBox started!",
					TrayIcon.MessageType.INFO);
		}

		logger.debug("started");
	}

	private Image loadImage(final String file) {
		Image result = null;

		final File fallback = new File("res", file);

		result = Toolkit.getDefaultToolkit().getImage(
				fallback.getAbsolutePath());
		if (result == null) {
			logger.error("error loading fallback image:\n" + fallback);
		}

		if (result == null) {
			logger.error("returning null image: " + file);
		}
		return result;
	}

	private static final class ExitListener implements ActionListener {

		private final JPimpsApplicationInterface app;

		public ExitListener(final JPimpsApplicationInterface app) {
			this.app = app;
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			app.exit();
		}
	}

	private class DoubleClickListener implements ActionListener {
		@SuppressWarnings("unused")
		private final JPimpsApplicationInterface app;

		private DoubleClickListener(final JPimpsApplicationInterface app) {
			this.app = app;
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			trayIcon.setImage(defaultIcon);
			setStatusText(null);
		}
	}

	@Override
	public void displayMessage(final String caption, final String text,
			final String type) {

		Image newImage = defaultIcon;

		if (ToolBoxUserInterface.NONE.equals(type)) {
			trayIcon.displayMessage(caption, text,
					TrayIcon.MessageType.valueOf(type));
		} else {
			int t = JOptionPane.PLAIN_MESSAGE;

			if (ToolBoxUserInterface.INFO.equals(type)) {
				t = JOptionPane.INFORMATION_MESSAGE;
			} else if (ToolBoxUserInterface.WARNING.equals(type)) {
				t = JOptionPane.WARNING_MESSAGE;
				newImage = warnIcon;
			} else {
				t = JOptionPane.ERROR_MESSAGE;
				newImage = errorIcon;
			}

			final MessageDialogPopUpThread m = new MessageDialogPopUpThread(
					caption, text, t);
			m.start();
		}
		trayIcon.setImage(newImage);
	}

	@Override
	public void setStatusText(final String status) {
		trayIcon.setToolTip(status);
	}
}
