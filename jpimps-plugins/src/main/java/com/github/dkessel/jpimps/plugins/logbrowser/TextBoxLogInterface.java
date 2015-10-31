package com.github.dkessel.jpimps.plugins.logbrowser;

import java.awt.*;
import java.util.*;

import javax.swing.text.*;

import com.github.dkessel.jpimps.api.app.log.*;
import com.github.dkessel.jpimps.util.*;

public class TextBoxLogInterface implements LogInterface {

	private static final String NL = "\n";
	private final JTextComponent textBox;

	private final ConcurrentSimpleDateFormat sdf;

	public TextBoxLogInterface(final JTextComponent textBox) {
		this.textBox = textBox;
		this.sdf = new ConcurrentSimpleDateFormat("HH:mm:ss,SSS");
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(textBox.getText());
		stringBuilder.append(NL);
		stringBuilder.append("=== logViewerFrame attached...");
		stringBuilder.append(NL);
		final String text = stringBuilder.toString();
		textBox.setText(text);
		textBox.setCaretPosition(text.length() - 1);
	}

	private String buildText(final String threadName, final String category,
			final String level, final String text) {
		final StringBuilder stringBuilder = new StringBuilder();
		final String time = sdf.format(new Date());
		stringBuilder.append(LogUtil.createLog(time, threadName, level,
				category, text));
		stringBuilder.append(NL);
		final String result = stringBuilder.toString();
		return result;
	}

	@Override
	public void debug(final String category, final String text) {
		EventQueue.invokeLater(new InsertLogRunner(buildText(Thread
				.currentThread().getName(), category, "DEBUG", text)));
	}

	@Override
	public void error(final String category, final String text) {
		EventQueue.invokeLater(new InsertLogRunner(buildText(Thread
				.currentThread().getName(), category, "ERROR", text)));
	}

	@Override
	public void info(final String category, final String text) {
		EventQueue.invokeLater(new InsertLogRunner(buildText(Thread
				.currentThread().getName(), category, "INFO", text)));
	}

	@Override
	public void warn(final String category, final String text) {
		EventQueue.invokeLater(new InsertLogRunner(buildText(Thread
				.currentThread().getName(), category, "WARN", text)));
	}

	private final class InsertLogRunner implements Runnable {

		private final String text;

		public InsertLogRunner(final String text) {
			this.text = text;

		}

		@Override
		public void run() {
			final String newText = textBox.getText() + text;
			textBox.setText(newText);
			textBox.setCaretPosition(newText.length() - 1);
		}

	}
}
