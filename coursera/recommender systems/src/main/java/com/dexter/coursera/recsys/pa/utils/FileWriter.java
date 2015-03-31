package com.dexter.coursera.recsys.pa.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class FileWriter {

	private static FileWriter fileWrite = new FileWriter();

	private FileWriter() {
	}

	public static FileWriter getInstance() {
		return fileWrite;
	}

	public final void write(final String fileName, final List<String> output) {
		java.io.FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			// Create file
			fstream = new java.io.FileWriter(fileName);
			out = new BufferedWriter(fstream);
			int size = output.size();
			for (int i = 1; i <= size; i++) {
				out.write(output.get(i - 1));
				if (i != size) {
					out.newLine();
				}
			}

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		} finally {
			// Close the output stream

			try {
				if (out != null) {
					out.close();
				}
				if (fstream != null) {
					fstream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
