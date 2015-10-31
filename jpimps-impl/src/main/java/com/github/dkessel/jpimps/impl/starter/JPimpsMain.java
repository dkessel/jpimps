package com.github.dkessel.jpimps.impl.starter;

import java.util.*;

import javax.swing.*;

import com.github.dkessel.jpimps.api.app.*;
import com.github.dkessel.jpimps.api.app.log.*;
import com.github.dkessel.jpimps.api.ui.*;
import com.github.dkessel.jpimps.impl.app.*;
import com.github.dkessel.jpimps.impl.app.log.*;
import com.github.dkessel.jpimps.impl.ui.*;

public class JPimpsMain {

	public static void main(final String[] args) throws Exception {
		final LogInterface stdOutErr = new SysOutSysErrLogger();
		final MultiLogger multiLog = new MultiLogger(stdOutErr);
		final LogInterface fileLogger = new FileLogger("jpimps.log", stdOutErr);
		multiLog.addLogTarget(fileLogger);

		multiLog.info("BOOT", "== STARTUP: " + new Date());

		final String lnf = UIManager.getSystemLookAndFeelClassName();
		UIManager.setLookAndFeel(lnf);

		// init dependencies

		final JPimpsApplicationInterface app = JPimpsApplicationFactory
				.createDefaultToolBoxApplication(multiLog);
		final ToolBoxUserInterface ui = ToolBoxUIFactory
				.createToolBoxUITray(app);

		app.init(ui, multiLog);

		app.start();

		while (app.isRunning()) {
			Thread.sleep(100);
		}

	}

}
