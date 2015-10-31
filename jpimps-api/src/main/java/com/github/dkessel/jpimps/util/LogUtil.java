package com.github.dkessel.jpimps.util;

public class LogUtil {

	public static String createLog(final String level, final String category,
			final String text) {
		return createLog("", Thread.currentThread().getName(), level, category,
				text);
	}

	public static String createLog(final String time, final String threadName,
			final String level, final String category, final String text) {
		return time + " [" + threadName + "] " + level + " " + category + " - "
				+ text;
	}
}
