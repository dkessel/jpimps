/**
 * 
 */
package com.github.dkessel.jpimps.impl.app.log;

import com.github.dkessel.jpimps.api.app.log.*;
import com.github.dkessel.jpimps.util.*;

public class SysOutSysErrLogger implements LogInterface {

	@Override
	public void info(final String category, final String text) {
		System.out.println(LogUtil.createLog("INFO", category, text));
	}

	@Override
	public void debug(final String category, final String text) {
		System.out.println(LogUtil.createLog("DEBUG", category, text));
	}

	@Override
	public void warn(final String category, final String text) {
		System.err.println(LogUtil.createLog("WARN", category, text));
	}

	@Override
	public void error(final String category, final String text) {
		System.err.println(LogUtil.createLog("ERROR", category, text));

	}

}
