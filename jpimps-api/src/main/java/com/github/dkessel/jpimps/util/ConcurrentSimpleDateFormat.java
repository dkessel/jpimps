package com.github.dkessel.jpimps.util;

import java.text.*;
import java.util.*;

public class ConcurrentSimpleDateFormat extends SimpleDateFormat {

	private static final long serialVersionUID = 1L;
	private final SimpleDateFormat sdf;

	public ConcurrentSimpleDateFormat(final String pattern,
			final DateFormatSymbols formatSymbols) {
		sdf = new SimpleDateFormat(pattern, formatSymbols);
	}

	public ConcurrentSimpleDateFormat(final String pattern, final Locale locale) {
		sdf = new SimpleDateFormat(pattern, locale);
	}

	public ConcurrentSimpleDateFormat(final String pattern) {
		sdf = new SimpleDateFormat(pattern);
	}

	public ConcurrentSimpleDateFormat() {
		sdf = new SimpleDateFormat();
	}

	@Override
	public synchronized void applyLocalizedPattern(final String pattern) {
		sdf.applyLocalizedPattern(pattern);
	}

	@Override
	public synchronized void applyPattern(final String pattern) {
		sdf.applyPattern(pattern);
	}

	@Override
	public synchronized boolean equals(final Object obj) {
		return sdf.equals(obj);
	}

	@Override
	public synchronized AttributedCharacterIterator formatToCharacterIterator(
			final Object obj) {
		return sdf.formatToCharacterIterator(obj);
	}

	@Override
	public synchronized Date get2DigitYearStart() {
		return sdf.get2DigitYearStart();
	}

	@Override
	public synchronized DateFormatSymbols getDateFormatSymbols() {
		return super.getDateFormatSymbols();
	}

	@Override
	public synchronized int hashCode() {
		return sdf.hashCode();
	}

	@Override
	public synchronized void set2DigitYearStart(final Date startDate) {
		sdf.set2DigitYearStart(startDate);
	}

	@Override
	public synchronized void setDateFormatSymbols(
			final DateFormatSymbols newFormatSymbols) {
		sdf.setDateFormatSymbols(newFormatSymbols);
	}

	@Override
	public synchronized String toLocalizedPattern() {
		return sdf.toLocalizedPattern();
	}

	@Override
	public synchronized String toPattern() {
		return sdf.toPattern();
	}

	@Override
	public synchronized Calendar getCalendar() {
		return sdf.getCalendar();
	}

	@Override
	public synchronized NumberFormat getNumberFormat() {
		return sdf.getNumberFormat();
	}

	@Override
	public synchronized TimeZone getTimeZone() {
		return sdf.getTimeZone();
	}

	@Override
	public synchronized boolean isLenient() {
		return sdf.isLenient();
	}

	@Override
	public synchronized Date parse(final String source) throws ParseException {
		return sdf.parse(source);
	}

	@Override
	public synchronized Object parseObject(final String source,
			final ParsePosition pos) {
		return sdf.parseObject(source, pos);
	}

	@Override
	public synchronized void setCalendar(final Calendar newCalendar) {
		sdf.setCalendar(newCalendar);
	}

	@Override
	public synchronized void setLenient(final boolean lenient) {
		sdf.setLenient(lenient);
	}

	@Override
	public synchronized void setNumberFormat(final NumberFormat newNumberFormat) {
		sdf.setNumberFormat(newNumberFormat);
	}

	@Override
	public synchronized void setTimeZone(final TimeZone zone) {
		sdf.setTimeZone(zone);
	}

	@Override
	public synchronized Object parseObject(final String source)
			throws ParseException {
		return sdf.parseObject(source);
	}

	@Override
	public synchronized StringBuffer format(final Date date,
			final StringBuffer toAppendTo, final FieldPosition fieldPosition) {
		return sdf.format(date, toAppendTo, fieldPosition);
	}

	@Override
	public synchronized Date parse(final String source, final ParsePosition pos) {
		return sdf.parse(source, pos);
	}

}
