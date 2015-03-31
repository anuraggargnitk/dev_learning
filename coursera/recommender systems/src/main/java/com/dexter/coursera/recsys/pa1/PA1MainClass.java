package com.dexter.coursera.recsys.pa1;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dexter.coursera.recsys.pa.utils.FileReader;

public class PA1MainClass {

	private static final Set<String> targetMovieList = new HashSet<String>();

	private static final int TOP_MOVIES_SIZE = 5;

	private static String inputFileName = "D:/personal/docs/development/courses/recommender systems/coursera/eclipse/src/main/resources/recsys_data_ratings.csv";

	static {
/*		targetMovieList.add("581");
		targetMovieList.add("9741");
		targetMovieList.add("36658");*/

		targetMovieList.add("11");
		targetMovieList.add("121");
		targetMovieList.add("8587");

	}

	public static void main(String[] args) {
		// inputFileName = "C:/My Documents/Downloads/output.csv";
		FileReader fl = FileReader.getInstance();
		// since src folder is always under classpath, so it ll find the file
		// from src/main/resources
		List<String> inputRatings = fl.read(inputFileName);
		// part1(inputRatings);
		part2(inputRatings);

	}

	private static void part1(List<String> inputRatings) {
		if (inputRatings != null) {
			Set<String> systemUserSet = new HashSet<String>();
			Map<String, Set<String>> moviesToUsersMap = new HashMap<String, Set<String>>();
			for (String userRating : inputRatings) {
				String[] ratingRow = userRating.split(",");
				// movie not rated
				if (ratingRow.length == 3) {
					String userId = ratingRow[0];
					String movieId = ratingRow[1];

					systemUserSet.add(userId);
					Set<String> usersList = moviesToUsersMap.get(movieId);
					if (usersList == null) {
						usersList = new HashSet<String>();
						moviesToUsersMap.put(movieId, usersList);
					}
					usersList.add(userId);
				}
			}

			Map<String, List<Map.Entry<String, Integer>>> simpleFinalCounts = new HashMap<String, List<Map.Entry<String, Integer>>>(
					targetMovieList.size());
			for (String targetMovieId : targetMovieList) {
				Set<String> targetMovieUserSet = moviesToUsersMap
						.get(targetMovieId);

				List<Map.Entry<String, Integer>> top5MovieAndCount = new ArrayList<Map.Entry<String, Integer>>(
						TOP_MOVIES_SIZE);
				for (int j = 0; j < TOP_MOVIES_SIZE; j++) {
					top5MovieAndCount.add(null);
				}
				simpleFinalCounts.put(targetMovieId, top5MovieAndCount);
				for (Map.Entry<String, Set<String>> movieAndUsersEntry : moviesToUsersMap
						.entrySet()) {
					String movieId = movieAndUsersEntry.getKey();
					if (!movieId.equals(targetMovieId)) {
						Set<String> users = movieAndUsersEntry.getValue();
						int matchCount = 0;
						for (String userId : users) {
							if (targetMovieUserSet.contains(userId)) {
								matchCount++;
							}
						}
						int i = 0;
						Map.Entry<String, Integer> movieAndCount = null;
						while (i < TOP_MOVIES_SIZE) {
							movieAndCount = top5MovieAndCount.get(i);
							if (movieAndCount != null
									&& movieAndCount.getValue() > matchCount) {
								i++;
								continue;
							} else {
								top5MovieAndCount.set(i,
										new AbstractMap.SimpleEntry(movieId,
												matchCount));
								Map.Entry<String, Integer> temp = null;
								for (; i < (TOP_MOVIES_SIZE - 1); i++) {
									temp = top5MovieAndCount.get(i + 1);
									top5MovieAndCount.set(i + 1, movieAndCount);
									movieAndCount = temp;
								}
								break;
							}
						}
					}
				}
			}

			for (String targetMovieId : targetMovieList) {
				List<Map.Entry<String, Integer>> top5MoviesList = simpleFinalCounts
						.get(targetMovieId);
				int targetMovieRated = moviesToUsersMap.get(targetMovieId)
						.size();
				StringBuilder sb = new StringBuilder(targetMovieId);
				for (Map.Entry<String, Integer> entry : top5MoviesList) {
					sb.append(",")
							.append(entry.getKey())
							.append(",")
							.append((entry.getValue() * 100.0f)
									/ (targetMovieRated * 100.0f));
				}
				System.out.println(sb.toString());
			}

		}
	}

	private static void part2(List<String> inputRatings) {
		if (inputRatings != null) {
			Set<String> systemUserSet = new HashSet<String>();
			Map<String, Set<String>> moviesToUsersMap = new HashMap<String, Set<String>>();
			for (String userRating : inputRatings) {
				String[] ratingRow = userRating.split(",");
				// movie not rated
				if (ratingRow.length == 3) {
					String userId = ratingRow[0];
					String movieId = ratingRow[1];

					systemUserSet.add(userId);
					Set<String> usersList = moviesToUsersMap.get(movieId);
					if (usersList == null) {
						usersList = new HashSet<String>();
						moviesToUsersMap.put(movieId, usersList);
					}
					usersList.add(userId);
				}
			}

			for (String targetMovieId : targetMovieList) {

				List<Map.Entry<String, Float>> top5MovieAndCount = new ArrayList<Map.Entry<String, Float>>(
						TOP_MOVIES_SIZE);
				for (int j = 0; j < TOP_MOVIES_SIZE; j++) {
					top5MovieAndCount.add(null);
				}

				Set<String> targetMovieUserSet = moviesToUsersMap
						.get(targetMovieId);
				int targetMovieRatingCount = targetMovieUserSet.size();
				int targetMovieNotRatedCount = systemUserSet.size()
						- targetMovieRatingCount;
				for (Map.Entry<String, Set<String>> movieAndUsersEntry : moviesToUsersMap
						.entrySet()) {
					String movieId = movieAndUsersEntry.getKey();
					if (!movieId.equals(targetMovieId)) {
						Set<String> users = movieAndUsersEntry.getValue();
						int simpleMatchCount = 0;
						int advMatchCount = 0;
						for (String userId : users) {
							if (targetMovieUserSet.contains(userId)) {
								simpleMatchCount++;
							} else {
								advMatchCount++;
							}
						}
						Float val = (simpleMatchCount
								* targetMovieNotRatedCount * 1.0f)
								/ (targetMovieRatingCount * advMatchCount * 1.0f);
						int i = 0;
						Map.Entry<String, Float> movieAndCount = null;
						while (i < TOP_MOVIES_SIZE) {
							movieAndCount = top5MovieAndCount.get(i);
							if (movieAndCount != null
									&& movieAndCount.getValue() > val) {
								i++;
								continue;
							} else {
								top5MovieAndCount.set(i,
										new AbstractMap.SimpleEntry(movieId,
												val));
								Map.Entry<String, Float> temp = null;
								for (; i < (TOP_MOVIES_SIZE - 1); i++) {
									temp = top5MovieAndCount.get(i + 1);
									top5MovieAndCount.set(i + 1, movieAndCount);
									movieAndCount = temp;
								}
								break;
							}
						}

					}
				}
				StringBuilder sb = new StringBuilder(targetMovieId);
				for (Map.Entry<String, Float> entry : top5MovieAndCount) {
					sb.append(",").append(entry.getKey()).append(",")
							.append(entry.getValue());
				}
				System.out.println(sb.toString());
			}

		}
	}

}
