/**
 * 
 */
package com.github.dkessel.jpimps.util;

import java.io.*;

public class Exceptions {

	public static String throwableToString(final Throwable e) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintWriter pw = new PrintWriter(baos);
		e.printStackTrace(pw);
		pw.flush();
		pw.close();
		final String stackTrace = new String(baos.toByteArray());
		return stackTrace;
	}

}
