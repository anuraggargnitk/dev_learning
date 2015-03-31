package com.dexter.coursera.recsys.wa.utils;

import java.util.ArrayList;
import java.util.List;

import com.dexter.coursera.recsys.pa.utils.FileReader;
import com.dexter.coursera.recsys.pa.utils.FileWriter;

public class WA1MainClass {

	public static void main(String[] args) {
		FileReader fl = FileReader.getInstance();
		List<String> input = fl.read("C:/My Documents/Downloads/recsys_data_WA 1 Rating Matrix.csv");
		String firstRow = input.get(0);
		String[] headers = firstRow.split(",");
		List<String> output = new ArrayList<String>(headers.length - 1);
		
		String[] movieIds = new String[headers.length - 1];

		for (int i = 1; i < headers.length; i++) {
			String movieName = headers[i];
			String movieId = movieName;
			movieIds[i - 1] = movieId;
		}

		for (int i = 1; i < input.size(); i++) {
			String userRatings = input.get(i);

			String[] ratingColmns = userRatings.split(",");
			String userId = ratingColmns[0];

			for (int j = 1; j < ratingColmns.length; j++) {
				String rating = ratingColmns[j];
				if (!(rating == null || rating.trim().isEmpty())) {
					output.add(userId + "," + movieIds[j - 1] + "," + rating);
				}
			}
		}
		
		FileWriter fw =  FileWriter.getInstance();
		fw.write("C:/My Documents/Downloads/output.csv", output);
	}

}
