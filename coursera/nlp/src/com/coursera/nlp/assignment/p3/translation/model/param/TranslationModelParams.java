package com.coursera.nlp.assignment.p3.translation.model.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslationModelParams {

	private Map<String, Integer> distortionAlignmentMappingCountMap = new HashMap<String, Integer>();

	private Map<String, Integer> distortionAlignmentCountMap = new HashMap<String, Integer>();

	private Map<String, Integer> emissionWordsMappingCountMap = new HashMap<String, Integer>();

	private Map<String, Integer> emissionWordCountMap = new HashMap<String, Integer>();

	public void registerCounts(List<String[]> trainingData) {
		if (trainingData != null) {
			for (String[] mTrain : trainingData) {
				if (mTrain.length != 3) {
					String english = "NULL " + mTrain[0];
					String french = mTrain[1];
					String alignmentDetails = mTrain[2];
					String[] alignmentsArray = alignmentDetails.split("\\s+");
					String[] frenchArray = french.split("\\s+");
					String[] englishArray = english.split("\\s+");
					int m = frenchArray.length;
					int l = englishArray.length;
					for (int i = 0; i < alignmentsArray.length; i++) {
						String alignmentKey = new StringBuilder()
								.append(alignmentsArray[i]).append(" ")
								.append(l).append(" ").append(m).toString();
						String alignmentMappingKey = new StringBuilder()
								.append(i).append(" ").append(alignmentKey)
								.toString();

						Integer alignmentMappingCount = distortionAlignmentMappingCountMap
								.get(alignmentMappingKey);
						if (alignmentMappingCount == null) {
							alignmentMappingCount = 0;
						}
						distortionAlignmentMappingCountMap.put(
								alignmentMappingKey, alignmentMappingCount + 1);

						Integer alignmentCount = distortionAlignmentCountMap
								.get(alignmentKey);
						if (alignmentCount == null) {
							alignmentCount = 0;
						}
						distortionAlignmentCountMap.put(alignmentKey,
								alignmentCount + 1);

						String englishAlignedWord = englishArray[Integer
								.parseInt(alignmentsArray[i])];
						String frenchWord = frenchArray[i];

						String emissionParamMappingKey = new StringBuilder()
								.append(frenchWord).append(" ")
								.append(englishAlignedWord).toString();

						Integer emissionMappingCount = emissionWordsMappingCountMap
								.get(emissionParamMappingKey);
						if (emissionMappingCount == null) {
							emissionMappingCount = 0;
						}
						emissionWordsMappingCountMap.put(
								emissionParamMappingKey,
								emissionMappingCount + 1);

						Integer emissionWordsCount = emissionWordCountMap
								.get(englishAlignedWord);
						if (emissionWordsCount == null) {
							emissionWordsCount = 0;
						}
						emissionWordCountMap.put(englishAlignedWord,
								emissionWordsCount + 1);
					}

				}
			}
		}
	}

}
