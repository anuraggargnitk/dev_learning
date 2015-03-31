package com.coursera.nlp.assignment.p3.translation.model.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class EMAlgo {

	private Map<String, Float> distortionAlignmentMappingCountMap = new HashMap<String, Float>();

	private Map<String, Float> distortionAlignmentCountMap = new HashMap<String, Float>();

	private Map<String, Float> emissionWordsMappingCountMap = new HashMap<String, Float>();

	private Map<String, Float> emissionWordCountMap = new HashMap<String, Float>();

	private Map<String, Float> qMap = new HashMap<String, Float>();

	private Map<String, Float> tMap = new HashMap<String, Float>();

	public void findAlignments(List<String[]> trainingData) {
		if (trainingData != null) {
			for (int k = 0; k < 20; k++) {
				emissionWordCountMap.clear();
				emissionWordsMappingCountMap.clear();
				distortionAlignmentCountMap.clear();
				distortionAlignmentMappingCountMap.clear();

				for (String[] languageSentences : trainingData) {
					String srcLang = "NULL" + languageSentences[0];
					String destLang = languageSentences[1];

					String[] srcLangWords = srcLang.split("\\s+");
					String[] destLangWords = destLang.split("\\s+");

					int l = destLangWords.length;
					int m = srcLangWords.length;

					for (int i = 0; i < l; i++) {
						String destLangWord = destLangWords[i];
						float deltaDenominator = 0;
						float[] deltaNumerator = new float[m];
						for (int j = 0; j < m; j++) {
							String srcLangWord = srcLangWords[j];
							String countILMKey = new StringBuilder().append(i)
									.append(" ").append(l).append(" ")
									.append(m).toString();
							String countJGivenILMKey = new StringBuilder()
									.append(j).append(" ").append(countILMKey)
									.toString();

							String srcLangDestlangWordMappingKey = new StringBuilder()
									.append(srcLangWord).append(" ")
									.append(destLangWord).toString();

							float q = getQVal(countJGivenILMKey);
							float t = getTVal(srcLangDestlangWordMappingKey);
							deltaNumerator[j] = q * t;
							deltaDenominator = deltaDenominator
									+ deltaNumerator[j];
						}

						for (int j = 0; j < m; j++) {
							float delta = deltaNumerator[j] / deltaDenominator;

							String srcLangWord = srcLangWords[j];
							String countILMKey = new StringBuilder().append(i)
									.append(" ").append(l).append(" ")
									.append(m).toString();
							String countJGivenILMKey = new StringBuilder()
									.append(j).append(" ").append(countILMKey)
									.toString();

							String srcLangDestlangWordMappingKey = new StringBuilder()
									.append(srcLangWord).append(" ")
									.append(destLangWord).toString();

							Float countJGivenILM = distortionAlignmentMappingCountMap
									.get(countJGivenILMKey);
							if (countJGivenILM == null) {
								countJGivenILM = 0f;
							}
							distortionAlignmentMappingCountMap.put(
									countJGivenILMKey, countJGivenILM + delta);

							Float countILM = distortionAlignmentCountMap
									.get(countILMKey);
							if (countILM == null) {
								countILM = 0f;
							}
							distortionAlignmentCountMap.put(countILMKey,
									countILM + delta);

							Float srcDestLangMappingCount = emissionWordsMappingCountMap
									.get(srcLangDestlangWordMappingKey);
							if (srcDestLangMappingCount == null) {
								srcDestLangMappingCount = 0f;
							}
							emissionWordsMappingCountMap.put(
									srcLangDestlangWordMappingKey,
									srcDestLangMappingCount + delta);

							Float srcLangWordCount = emissionWordCountMap
									.get(srcLangWord);
							if (srcLangWordCount == null) {
								srcLangWordCount = 0f;
							}
							emissionWordCountMap.put(srcLangWord,
									srcLangWordCount + delta);
						}
					}
				}

				for (Entry<String, Float> entry : qMap.entrySet()) {
					String jGivenILMKey = entry.getKey();
					String ilmKey = jGivenILMKey.substring(jGivenILMKey
							.indexOf(" ") + 1);
					float newQVal = distortionAlignmentMappingCountMap
							.get(jGivenILMKey)
							/ distortionAlignmentCountMap.get(ilmKey);
					entry.setValue(newQVal);
				}

				for (Entry<String, Float> entry : tMap.entrySet()) {
					String srcDestLangKey = entry.getKey();
					String srcLangKey = srcDestLangKey.substring(0,
							srcDestLangKey.indexOf(" "));
					float newTVal = emissionWordsMappingCountMap
							.get(srcDestLangKey)
							/ emissionWordCountMap.get(srcLangKey);
					entry.setValue(newTVal);
				}
			}
		}
	}

	private float getQVal(String key) {

		if (qMap.containsKey(key)) {
			return qMap.get(key);
		}
		float randomVal = (new Random()).nextFloat();
		qMap.put(key, randomVal);
		return randomVal;
	}

	private float getTVal(String key) {

		if (tMap.containsKey(key)) {
			return tMap.get(key);
		}
		float randomVal = (new Random()).nextFloat();
		tMap.put(key, randomVal);
		return randomVal;
	}

}
