package com.github.dkessel.jpimps.plugins.worktracker;

import java.util.*;

public class WorkingDay {

	public Calendar today;
	public Calendar todaysFirst;
	public Calendar todaysLast;
	public long todaysHours;
	public long breakTimeStart = 0;
	public long breakTimeEnd = 45 * TimeConstants.ONE_MINUTE; // 45 Minuten

	private static final String TAB = "\t";

	@Override
	public String toString() {
		final long breakDurationInSeconds = secondsBetweenTimeStamps(
				breakTimeEnd, breakTimeStart);
		final Calendar eod = ((Calendar) todaysFirst.clone());
		eod.add(Calendar.HOUR_OF_DAY, 8);
		eod.add(Calendar.SECOND, (int) breakDurationInSeconds);

		final long netWorkingTimeInSeconds = calculateNetWorkingTimeInSeconds();

		return "Working Day:\n" + "Date: " + calToDateString(today) + "\n"
				+ "From: " + calToString(todaysFirst) + "\n" + "To: "
				+ calToString(todaysLast) + "\n" + "Break duration: "
				+ secondsToString(breakDurationInSeconds) + "\n"
				+ "Net Work duration: "
				+ secondsToString(netWorkingTimeInSeconds) + "\n"
				+ "End of work day: " + calToString(eod);
	}

	public String toTSV() {
		return calToDateString(today) + TAB + calToString(todaysFirst) + TAB
				+ calToString(todaysLast) + TAB
				+ durationBetweenAsString(breakTimeEnd, breakTimeStart) + TAB
				+ secondsToString(calculateNetWorkingTimeInSeconds());
	}

	private String calToDateString(final Calendar c) {
		final StringBuilder sb = new StringBuilder();
		sb.append(String.format("%02d", c.get(Calendar.DAY_OF_MONTH)) + ".");
		sb.append(String.format("%02d", c.get(Calendar.MONTH) + 1) + ".");
		sb.append(String.format("%04d", c.get(Calendar.YEAR)));
		return sb.toString();
	}

	/**
	 * @return the amount of hours
	 */
	public long getNetWorkingHours() {
		return getTimeInHours(calculateNetWorkingTimeInSeconds());
	}

	private long calculateNetWorkingTimeInSeconds() {
		return secondsBetween(todaysLast, todaysFirst)
				- secondsBetweenTimeStamps(breakTimeEnd, breakTimeStart);
	}

	public long secondsBetween(final Calendar last, final Calendar first) {
		return secondsBetweenTimeStamps(last.getTimeInMillis(),
				first.getTimeInMillis());
	}

	private long secondsBetweenTimeStamps(final long last, final long first) {
		if (first > last) {
			throw new IllegalArgumentException("first > last!" + first + " > "
					+ last);
		}
		return (last - first) / 1000L;
	}

	private static long getTimeInHours(final long timeInSeconds) {
		return (timeInSeconds) / 3600L;
	}

	private String durationBetweenAsString(final long lastTime,
			final long firstTime) {
		final long durationBetweenInSeconds = secondsBetweenTimeStamps(
				lastTime, firstTime);
		return secondsToString(durationBetweenInSeconds);
	}

	private String secondsToString(long durationInSeconds) {
		final StringBuilder sb = new StringBuilder();
		if (durationInSeconds < 0) {
			durationInSeconds = durationInSeconds * -1;
			sb.append("-");
		}
		final long durationInHours = getTimeInHours(durationInSeconds);
		final long remainingSeconds = (durationInSeconds) % 3600L;
		final long durationMinutes = remainingSeconds / 60L;
		final long durationSeconds = remainingSeconds % 60L;

		sb.append(String.format("%02d", durationInHours) + ":");
		sb.append(String.format("%02d", durationMinutes) + ":");
		sb.append(String.format("%02d", durationSeconds));
		return sb.toString();
	}

	private String calToString(final Calendar c) {
		final StringBuilder sb = new StringBuilder();
		sb.append(String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":");
		sb.append(String.format("%02d", c.get(Calendar.MINUTE)) + ":");
		sb.append(String.format("%02d", c.get(Calendar.SECOND)));
		return sb.toString();
	}

}
