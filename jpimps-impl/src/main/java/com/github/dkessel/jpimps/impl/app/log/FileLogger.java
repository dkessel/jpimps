package com.github.dkessel.jpimps.impl.app.log;

import java.io.*;
import java.util.*;

import com.github.dkessel.jpimps.api.app.log.*;
import com.github.dkessel.jpimps.util.*;

public class FileLogger implements LogInterface {

	private final BufferedWriter writer;
	private final LogInterface errorLogger;

	private final ConcurrentSimpleDateFormat sdf;

	private final Object SYNC = new Object();

	public FileLogger(final String fileName, final LogInterface errorLogger)
			throws Exception {
		this.sdf = new ConcurrentSimpleDateFormat("HH:mm:ss,SSS");
		final File f = new File(fileName);
		errorLogger.debug("FileLogger",
				"using log file: " + f.getAbsolutePath());
		this.writer = new BufferedWriter(new FileWriter(f));
		this.errorLogger = errorLogger;
	}

	private void log(final String text) {
		synchronized (SYNC) {
			try {
				writer.write(text);
				writer.newLine();
				writer.flush();
			} catch (final IOException e) {
				errorLogger.error("FileLogger",
						"failed logging :" + Exceptions.toString(e));
			}
		}
	}

	@Override
	public void debug(final String category, final String text) {
		log(LogUtil.createLog(sdf.format(new Date()), Thread.currentThread()
				.getName(), "DEBUG", category, text));
	}

	@Override
	public void error(final String category, final String text) {
		log(LogUtil.createLog(sdf.format(new Date()), Thread.currentThread()
				.getName(), "ERROR", category, text));
	}

	@Override
	public void info(final String category, final String text) {
		log(LogUtil.createLog(sdf.format(new Date()), Thread.currentThread()
				.getName(), "INFO", category, text));
	}

	@Override
	public void warn(final String category, final String text) {
		log(LogUtil.createLog(sdf.format(new Date()), Thread.currentThread()
				.getName(), "WARN", category, text));
	}

}
