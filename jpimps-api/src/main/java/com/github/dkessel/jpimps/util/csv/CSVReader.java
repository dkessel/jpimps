package com.github.dkessel.jpimps.util.csv;

import java.io.*;
import java.util.*;

public class CSVReader {

	final List<String[]> entries;

	public CSVReader(final File csvFile, final char delimiter,
			final boolean skipFirstLine) throws IOException {
		if (!csvFile.isFile()) {
			throw new FileNotFoundException("file not found: "
					+ csvFile.getAbsolutePath());
		}
		entries = new ArrayList<String[]>();
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader(csvFile));
			String line = null;
			int count = 0;
			while ((line = r.readLine()) != null) {
				count++;
				if ((count == 1) && skipFirstLine) {
					continue;
				}
				final String[] entry = line.split(delimiter + "");
				entries.add(entry);
			}
		} finally {
			if (r != null) {
				r.close();
			}
		}
	}

	public int getRowCount() {
		return entries.size();
	}

	public String[] getRow(final int index) {
		return entries.get(index);
	}
}
