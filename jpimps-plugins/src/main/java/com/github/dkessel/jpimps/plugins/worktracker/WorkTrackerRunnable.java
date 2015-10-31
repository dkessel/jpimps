package com.github.dkessel.jpimps.plugins.worktracker;

import java.awt.*;
import java.io.*;
import java.util.*;

import com.github.dkessel.jpimps.api.app.*;
import com.github.dkessel.jpimps.api.app.log.*;
import com.github.dkessel.jpimps.api.ui.*;

public class WorkTrackerRunnable implements Runnable {

	private static final int CHECK_DELAY = 10000;

	private Point lastPos;

	private static final String CAT = "WorkTrackerRunnable";
	private static final String WORK_LOG = "work_log.tsv";

	private static final String TODAYS_FIRST = "todays_first.txt";
	private static final String TODAYS_LAST = "todays_last.txt";

	private final JPimpsApplicationInterface app;
	private final Logger logger;
	private final WorkingDay wd;
	private final File workDir;

	public WorkTrackerRunnable(final JPimpsApplicationInterface app,
			final WorkingDay wd, File workDir) {
		this.app = app;
		this.logger = app.getLogger(CAT);
		this.wd = wd;
		this.workDir = workDir;
	}

	@Override
	public void run() {

		try {

			final Calendar startTS = Calendar.getInstance();
			final Calendar startDayOnly = clearNonDateFields(startTS);

			if (wd.today == null) {
				handleStartup(startTS, startDayOnly);
			}

			// loop...
			while (!Thread.interrupted()) {

				try {
					Thread.sleep(CHECK_DELAY);
				} catch (final InterruptedException e) {
					logger.debug("interrupted.... shutting down");
					break;
				}

				final PointerInfo info = MouseInfo.getPointerInfo();
				Point newPos = null;
				if (info != null) {
					newPos = info.getLocation();
				}

				if ((newPos == null)
						|| ((lastPos != null) && lastPos.equals(newPos))) {
					continue;
				}

				if ((lastPos == null) || !lastPos.equals(newPos)) {
					lastPos = newPos;
				}

				final Calendar now = Calendar.getInstance();
				final Calendar nowDayOnly = clearNonDateFields(now);

				if (wd.today.before(nowDayOnly)) {
					handleDayChange(now, nowDayOnly);
				} else {
					handleSameDayMovement(now);
				}
			}

		} catch (Exception e) {
			logger.error("error in run():", e);
		}

	}

	private boolean reloadOldDaysLast() {
		boolean fromFile = false;
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader(
					new File(workDir, TODAYS_LAST)));
			final String ts = r.readLine();
			final long millis = Long.valueOf(ts);
			final Calendar temp = Calendar.getInstance();
			temp.setTimeInMillis(millis);

			final Calendar nowDayOnly = clearNonDateFields(Calendar
					.getInstance());

			if (temp.before(nowDayOnly)) {
				wd.todaysLast = temp;
				logger.debug("read old TODAYS_LAST TS from file... : "
						+ temp.getTimeInMillis() + "; "
						+ new Date(temp.getTimeInMillis()).toString());
				fromFile = true;
			} else {
				logger.info("CANNOT READ TODAYS_LAST TS for DATE - Skipping too young TS : "
						+ new Date(temp.getTimeInMillis()).toString());
			}

		} catch (final Exception e) {
			logger.error("error reading old TODAYS_LAST TS: " + e, e);
		} finally {
			try {
				if (r != null) {
					r.close();
				}
			} catch (final IOException e) {
				logger.error("error closing TS file: " + e, e);
			}

		}
		return fromFile;
	}

	private void handleDayChange(final Calendar now, final Calendar nowDayOnly) {
		logger.debug("handleDayChange: new day!");

		writeWDToTSVFile();

		logger.info(wd.toString());

		app.ui().displayMessage("WorkTracker", wd.toString(),
				ToolBoxUserInterface.INFO);

		wd.today = nowDayOnly;
		wd.todaysFirst = now;
		wd.todaysLast = now;
		wd.todaysHours = 0;
		saveTSToFile(wd.todaysFirst.getTimeInMillis(), TODAYS_FIRST);
	}

	private void writeWDToTSVFile() {
		BufferedWriter w = null;
		try {
			w = new BufferedWriter(new FileWriter(new File(workDir, WORK_LOG),
					true));
			w.write(wd.toTSV() + "\n");
			w.flush();
			logger.info("saved data:\n" + wd.toTSV());
		} catch (final Exception e) {
			logger.error("error saving work time in TSV file: " + e, e);
		} finally {
			try {
				if (w != null) {
					w.close();
				}
			} catch (final IOException e) {
				logger.error("error closing work time TSV file: " + e, e);
			}
		}
	}

	private void handleStartup(final Calendar now, final Calendar nowDayOnly) {
		wd.today = nowDayOnly;
		wd.todaysFirst = now;
		wd.todaysLast = now;

		// fix "first", if present
		boolean fromFile = false;
		if (new File(workDir, TODAYS_FIRST).isFile()) {
			fromFile = initFromTSFile();
		}

		if (!fromFile) {
			saveTSToFile(wd.todaysFirst.getTimeInMillis(), TODAYS_FIRST);
		}

		logger.debug("starting on day: " + nowDayOnly.getTime());
	}

	private void handleSameDayMovement(final Calendar now) {
		wd.todaysLast = now;
		saveTSToFile(wd.todaysLast.getTimeInMillis(), TODAYS_LAST);
		final long newHours = wd.getNetWorkingHours();
		if (newHours != wd.todaysHours) {
			wd.todaysHours = newHours;
			if (newHours >= 8) {
				app.ui().displayMessage("WorkTracker",
						"today's net work hours: " + wd.getNetWorkingHours(),
						ToolBoxUserInterface.INFO);
			} else {
				app.ui().displayMessage("WorkTracker",
						"today's net work hours: " + wd.getNetWorkingHours(),
						ToolBoxUserInterface.NONE);
			}
		}
	}

	private boolean initFromTSFile() {
		boolean fromFile = false;
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader(new File(workDir,
					TODAYS_FIRST)));
			final String ts = r.readLine();
			final long millis = Long.valueOf(ts);
			final Calendar todaysFirstFromFile = Calendar.getInstance();
			todaysFirstFromFile.setTimeInMillis(millis);

			Calendar now = Calendar.getInstance();
			final Calendar nowDayOnly = clearNonDateFields(now);

			// set todaysFirst/today from file....
			wd.todaysFirst = todaysFirstFromFile;
			wd.today = clearNonDateFields(todaysFirstFromFile);

			if (!todaysFirstFromFile.before(nowDayOnly)) {
				logger.debug("using OLD start TS from file... : "
						+ todaysFirstFromFile.getTimeInMillis()
						+ "; "
						+ new Date(todaysFirstFromFile.getTimeInMillis())
								.toString());
				fromFile = true;
			} else {
				// handle day change...
				logger.info("DETECTED DAY CHANGE ON STARTUP...");
				reloadOldDaysLast(); // sets wd.todaysLast from file...
				handleDayChange(now, nowDayOnly);
				logger.debug("using NEW start TS : " + now.getTimeInMillis()
						+ "; " + new Date(now.getTimeInMillis()).toString());
			}

		} catch (final Exception e) {
			logger.error("error reading TS: " + e, e);
		} finally {
			try {
				if (r != null) {
					r.close();
				}
			} catch (final IOException e) {
				logger.error("error closing TS file: " + e, e);
			}

		}
		return fromFile;
	}

	private void saveTSToFile(long tsMillis, String fileName) {
		BufferedWriter tw = null;
		try {
			tw = new BufferedWriter(new FileWriter(new File(workDir, fileName)));
			tw.write(tsMillis + "\n");
			tw.flush();
		} catch (final Exception e) {
			logger.error("error writing TS: " + e, e);
		} finally {
			try {
				if (tw != null) {
					tw.close();
				}
			} catch (final IOException e) {
				logger.error("error closing TS file: " + e, e);
			}

		}
	}

	private Calendar clearNonDateFields(final Calendar now) {
		final Calendar c = (Calendar) now.clone();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.SECOND);
		c.clear(Calendar.MILLISECOND);
		return c;
	}

}
