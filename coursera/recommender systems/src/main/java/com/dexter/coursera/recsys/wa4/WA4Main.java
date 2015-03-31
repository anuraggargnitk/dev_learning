package com.dexter.coursera.recsys.wa4;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.dexter.coursera.recsys.pa.utils.FileReader;
import com.dexter.coursera.recsys.pa.utils.FileWriter;

public class WA4Main {

	private static String[] userArr = null;
	private static String[] movieArr = null;

	private static final int RUN_TEST_CASE = 2;

	public static void main(String[] args) {
		WA4Main main = new WA4Main();
		FileReader fl = FileReader.getInstance();
		List<String> userMovieRatings = fl
				.read("D:/personal/docs/development/courses/recommender systems/coursera/eclipse/data/wa4/input.csv");
		List<String> userCorrelation = fl
				.read("D:/personal/docs/development/courses/recommender systems/coursera/eclipse/data/wa4/correlation.csv");
		Map<String, Map<String, Double>> userMovieRatingsMap = main
				.processUserMovieInputs(userMovieRatings);
		Map<String, Map<String, Double>> correlationMap = main
				.processCorrelation(userCorrelation);

		String[] inputUserIds = new String[] { "3712", "3867", "860" };
		List<String> output = new ArrayList<String>();
		for (String inputUserId : inputUserIds) {
			List<Map.Entry<String, Double>> topNUserList = main
					.findTopNCorrelationsForaUser(5, correlationMap,
							inputUserId);

			List<Map.Entry<String, Double>> topNMoviesList = main
					.computeRatingsForAUser(topNUserList, userMovieRatingsMap,
							3, inputUserId);
			for (Entry<String, Double> movieRating : topNMoviesList) {
				String movieName = movieRating.getKey();
				String movieId = movieName.substring(0, movieName.indexOf(":"));
				String result = movieId + " " + movieRating.getValue();
				System.out.println(result);
				output.add(result);
			}
			System.out.println();
		}

		FileWriter fw = FileWriter.getInstance();
		String outputFileName = "D:/personal/docs/development/courses/recommender systems/coursera/eclipse/data/wa4/non-normalized.txt";
		if (RUN_TEST_CASE == 2) {
			outputFileName = "D:/personal/docs/development/courses/recommender systems/coursera/eclipse/data/wa4/normalized.txt";
		}
		fw.write(outputFileName, output);
	}

	private List<Map.Entry<String, Double>> computeRatingsForAUser(
			List<Map.Entry<String, Double>> topNUserList,
			Map<String, Map<String, Double>> userMovieRatingsMap, int n,
			String targetUserId) {
		List<Map.Entry<String, Double>> topNMoviesList = new ArrayList<Map.Entry<String, Double>>(
				n);
		for (int i = 0; i < n; i++) {
			topNMoviesList.add(null);
		}
		for (String movieName : movieArr) {
			Double numerator = 0d;
			Double denominator = 0d;
			for (Entry<String, Double> user : topNUserList) {
				String userId = user.getKey();
				Double avgUserRating = 0d;
				if (RUN_TEST_CASE == 2) {
					avgUserRating = getAvgUserRating(userId,
							userMovieRatingsMap);
				}
				Double movieRating = userMovieRatingsMap.get(userId).get(
						movieName);
				Double correlationVal = user.getValue();

				if (movieRating.doubleValue() == 0d) {
					correlationVal = 0d;
				}
				numerator = numerator + (movieRating - avgUserRating)
						* correlationVal;
				denominator = denominator + correlationVal;
			}
			if (denominator != 0d) {
				Double avgUserRating = 0d;
				if (RUN_TEST_CASE == 2) {
					avgUserRating = getAvgUserRating(targetUserId,
							userMovieRatingsMap);
				}
				double rating = avgUserRating + numerator / denominator;
				for (int i = 0; i < topNMoviesList.size(); i++) {
					Map.Entry<String, Double> current = new AbstractMap.SimpleEntry<String, Double>(
							movieName, rating);
					if (topNMoviesList.get(i) == null) {
						topNMoviesList.set(i, current);
					} else if (topNMoviesList.get(i).getValue() < rating) {
						Map.Entry<String, Double> onNextLoc = null;
						for (int j = i; j < topNMoviesList.size(); j++) {
							onNextLoc = topNMoviesList.get(j);
							topNMoviesList.set(j, current);
							current = onNextLoc;
						}
						break;
					}
				}
			}
		}
		return topNMoviesList;
	}

	private List<Map.Entry<String, Double>> findTopNCorrelationsForaUser(int n,
			Map<String, Map<String, Double>> correlationMap, String userId) {
		List<Map.Entry<String, Double>> topNUsersList = new ArrayList<Map.Entry<String, Double>>(
				n);
		Map<String, Double> userCorrelationMap = correlationMap.get(userId);
		Set<String> userNames = userCorrelationMap.keySet();
		userNames.remove(userId);
		String[] userIdKeys = userNames.toArray(new String[0]);
		for (int i = 0; i < n; i++) {
			for (int j = i; j < userIdKeys.length; j++) {
				if (userCorrelationMap.get(userIdKeys[i]) < userCorrelationMap
						.get(userIdKeys[j])) {
					String temp = userIdKeys[i];
					userIdKeys[i] = userIdKeys[j];
					userIdKeys[j] = temp;
				}
			}
			topNUsersList.add(new AbstractMap.SimpleEntry(userIdKeys[i],
					userCorrelationMap.get(userIdKeys[i])));
		}
		return topNUsersList;
	}

	private Map<String, Map<String, Double>> processCorrelation(
			List<String> correlationInput) {
		Map<String, Map<String, Double>> correlationMap = new HashMap<String, Map<String, Double>>();

		String[] userArr = null;
		for (String input : correlationInput) {
			String[] inputArr = input.split(",");
			if (userArr == null) {
				userArr = new String[inputArr.length - 1];
				for (int i = 1; i < inputArr.length; i++) {
					userArr[i - 1] = inputArr[i];
					correlationMap.put(userArr[i - 1],
							new HashMap<String, Double>());
				}
			} else {
				String userName = inputArr[0];
				Map<String, Double> map = correlationMap.get(userName);
				for (int j = 1; j < inputArr.length; j++) {
					String correlationVal = inputArr[j];
					map.put(userArr[j - 1], Double.parseDouble(correlationVal));
				}

			}
		}
		return correlationMap;
	}

	private Map<String, Map<String, Double>> processUserMovieInputs(
			List<String> userMovieRatings) {
		Map<String, Map<String, Double>> userMovieRatingMap = new HashMap<String, Map<String, Double>>();
		movieArr = new String[userMovieRatings.size() - 1];
		int k = 0;
		for (String input : userMovieRatings) {
			String[] inputArr = null;
			if (userArr == null) {
				inputArr = input.split(",");
				userArr = new String[inputArr.length - 1];

				for (int i = 1; i < inputArr.length; i++) {
					userArr[i - 1] = inputArr[i];
					userMovieRatingMap.put(userArr[i - 1],
							new HashMap<String, Double>());
				}
			} else {
				inputArr = input.split(",", userArr.length + 1);
				movieArr[k++] = inputArr[0];
				for (int j = 1; j < inputArr.length; j++) {
					String movieRating = inputArr[j];
					if (movieRating == null || movieRating.trim().isEmpty()) {
						movieRating = "0.00";
					}
					userMovieRatingMap.get(userArr[j - 1]).put(inputArr[0],
							Double.parseDouble(movieRating));
				}

			}
		}
		return userMovieRatingMap;
	}

	private Double getAvgUserRating(String targetUserId,
			Map<String, Map<String, Double>> userMovieRatingsMap) {
		Collection<Double> ratingsList = userMovieRatingsMap.get(targetUserId)
				.values();
		Double meanRating = 0d;
		int count = 0;
		if (ratingsList != null) {
			for (Double rating : ratingsList) {
				if (rating != 0d) {
					meanRating = meanRating + rating;
					count++;
				}
			}
			meanRating = meanRating / count;
		}
		return meanRating;
	}

}
