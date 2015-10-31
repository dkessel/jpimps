package com.github.dkessel.jpimps.impl.ui;

import javax.swing.*;

public class MessageDialogPopUpThread extends Thread {

	private final String title;
	private final String message;
	private final int type;

	public MessageDialogPopUpThread(final String title, final String message,
			final int type) {
		super("MessageDialogPopUpThread");
		this.title = title;
		this.message = message;
		this.type = type;
	}

	@Override
	public void run() {
		JOptionPane.showMessageDialog(null, message, title, type);
	}
}