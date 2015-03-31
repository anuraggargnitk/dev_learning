package com.coursera.nlp.assignment.file.mgmt;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

	private static FileReader instance = new FileReader();

	private FileReader() {
	}

	public static FileReader getInstance() {
		return instance;
	}

	public List<String> read(final String fileName) {
		FileInputStream fstream = null;
		DataInputStream in = null;
		BufferedReader br = null;
		List<String> input = new ArrayList<String>();
		try {
			// Open the file that is the first
			// command line parameter
			fstream = new FileInputStream(fileName);
			// Get the object of DataInputStream
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in, "UTF8"));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				//System.out.println(count + " : " + strLine);
				if(!(strLine == null || strLine.trim().isEmpty()))	{
					if(strLine.trim().equals("/"))	{
						strLine = ";";
					}
				}
				/*String temp = " return STATE_ID into THIS_STATE_ID;";
				if(strLine != null && strLine.trim().endsWith(temp))	{
					strLine = strLine.substring(0, strLine.length() - temp.length());
					input.add(strLine+ ";");
					strLine = "    select LAST_INSERT_ID() into THIS_STATE_ID;";
				}
				temp = " return OUTCOME_ID into THIS_OUTCOME_ID;";
				if(strLine != null && strLine.trim().endsWith(temp))	{
					strLine = strLine.substring(0, strLine.length() - temp.length());
					input.add(strLine+ ";");
					strLine = "    select LAST_INSERT_ID() into THIS_OUTCOME_ID;";
				}*/				
				input.add(strLine);
			}
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		} finally {
			// Close the input stream
			try {
				if (br != null) {
					br.close();
				}
				if (in != null) {
					in.close();
				}
				if (fstream != null) {
					fstream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return input;
	}
}
