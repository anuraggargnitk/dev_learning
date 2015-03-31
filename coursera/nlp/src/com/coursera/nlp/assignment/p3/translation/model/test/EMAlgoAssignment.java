package com.coursera.nlp.assignment.p3.translation.model.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class EMAlgoAssignment {

	private Map<JILMKey, Double> distortionAlignmentMappingCountMap = new HashMap<JILMKey, Double>();

	private Map<ILMKey, Double> distortionAlignmentCountMap = new HashMap<ILMKey, Double>();

	private Map<String, Map<String, Double>> translationMappingCountMap = new HashMap<String, Map<String, Double>>();

	private Map<String, Double> translationSrcWordCountMap = new HashMap<String, Double>();

	private Map<JILMKey, Double> qMap = new HashMap<JILMKey, Double>();

	private Map<String, Map<String, Double>> tMap = new HashMap<String, Map<String, Double>>();

	private int numOfIterations;

	private Map<String, Set<String>> englishToForeignWordsMap = new HashMap<String, Set<String>>();

	public EMAlgoAssignment(int numOfIterations) {
		this.numOfIterations = numOfIterations;
	}

	public void estimate(List<String> srcLangSentences,
			List<String> destLangSentences) {
		// preprocessInput(srcLangSentences, destLangSentences);
		for (int k = 0; k < numOfIterations; k++) {
			translationSrcWordCountMap.clear();
			translationMappingCountMap.clear();
			distortionAlignmentCountMap.clear();
			distortionAlignmentMappingCountMap.clear();
			System.out.println("Iteration Num : " + (k + 1));
			for (int index = 0; index < srcLangSentences.size(); index++) {

				String[] srcLangWords = ("NULL " + srcLangSentences.get(index))
						.split("\\s++");
				String[] destLangWords = destLangSentences.get(index).split(
						"\\s+");

				int m = destLangWords.length;
				int l = srcLangWords.length;

				for (int i = 0; i < m; i++) {
					String destLangWord = destLangWords[i];
					double deltaDenominator = 0;
					double[] deltaNumerator = new double[l];

					for (int j = 0; j < l; j++) {
						String srcLangWord = srcLangWords[j];
						deltaNumerator[j] = getTVal(destLangWord, srcLangWord,
								false) * getQVal(j, i, l, m, false);
						deltaDenominator = deltaDenominator + deltaNumerator[j];
					}

					for (int j = 0; j < l; j++) {
						double delta = deltaNumerator[j] / deltaDenominator;
						String srcLangWord = srcLangWords[j];

						Map<String, Double> destLangMap = translationMappingCountMap
								.get(srcLangWord);
						if (destLangMap == null) {
							destLangMap = new HashMap<String, Double>();
							translationMappingCountMap.put(srcLangWord,
									destLangMap);
						}
						double mappedCount = 0d;
						if (destLangMap.containsKey(destLangWord)) {
							mappedCount = destLangMap.get(destLangWord);
						}
						destLangMap.put(destLangWord, mappedCount + delta);

						double srcWordCount = 0d;
						if (translationSrcWordCountMap.containsKey(srcLangWord)) {
							srcWordCount = translationSrcWordCountMap
									.get(srcLangWord);
						}
						translationSrcWordCountMap.put(srcLangWord,
								srcWordCount + delta);

						JILMKey jilmKey = new JILMKey(j, new ILMKey(i, l, m));
						double distMappingCount = 0d;
						if (distortionAlignmentMappingCountMap
								.containsKey(jilmKey)) {
							distMappingCount = distortionAlignmentMappingCountMap
									.get(jilmKey);
						}
						distortionAlignmentMappingCountMap.put(jilmKey,
								distMappingCount + delta);

						ILMKey ilmKey = jilmKey.getILMKey();
						Double distCount = 0d;
						if (distortionAlignmentCountMap.containsKey(ilmKey)) {
							distCount = distortionAlignmentCountMap.get(ilmKey);
						}
						distortionAlignmentCountMap.put(ilmKey, distCount
								+ delta);
					}
				}
			}

			System.out.println("Going to calculate t & q values now");
			for (Entry<String, Map<String, Double>> srcLangEntry : tMap
					.entrySet()) {
				String srcLangKey = srcLangEntry.getKey();
				Map<String, Double> destLangTValMap = srcLangEntry.getValue();
				Map<String, Double> destLangTranslationMap = translationMappingCountMap
						.get(srcLangKey);
				double srcLangTranslationWordCount = translationSrcWordCountMap
						.get(srcLangKey);
				for (Entry<String, Double> destLangEntry : destLangTValMap
						.entrySet()) {
					String destLangKey = destLangEntry.getKey();
					double translationMappingWordCount = destLangTranslationMap
							.get(destLangKey);
					destLangEntry.setValue(translationMappingWordCount
							/ srcLangTranslationWordCount);
				}
			}

			for (Entry<JILMKey, Double> qEntry : qMap.entrySet()) {
				JILMKey jilmKey = qEntry.getKey();
				ILMKey ilmKey = jilmKey.getILMKey();

				double distMappingCount = distortionAlignmentMappingCountMap
						.get(jilmKey);
				double distCount = distortionAlignmentCountMap.get(ilmKey);

				if (Double.compare(distCount, 0d) != 0) {
					qEntry.setValue(distMappingCount / distCount);
				}
			}
		}
	}

	public void estimateT(List<String> srcLangSentences,
			List<String> destLangSentences) {
		preprocessInput(srcLangSentences, destLangSentences);
		for (int k = 0; k < numOfIterations; k++) {
			translationSrcWordCountMap.clear();
			translationMappingCountMap.clear();
			distortionAlignmentCountMap.clear();
			distortionAlignmentMappingCountMap.clear();
			System.out.println("Iteration Num : " + (k + 1));
			for (int index = 0; index < srcLangSentences.size(); index++) {

				String[] srcLangWords = ("NULL " + srcLangSentences.get(index))
						.split("\\s++");
				String[] destLangWords = destLangSentences.get(index).split(
						"\\s+");

				int m = destLangWords.length;
				int l = srcLangWords.length;

				for (int i = 0; i < m; i++) {
					String destLangWord = destLangWords[i];
					double deltaDenominator = 0;
					double[] deltaNumerator = new double[l];

					for (int j = 0; j < l; j++) {
						String srcLangWord = srcLangWords[j];
						deltaNumerator[j] = getTVal(destLangWord, srcLangWord,
								false);
						deltaDenominator = deltaDenominator + deltaNumerator[j];
					}

					for (int j = 0; j < l; j++) {
						double delta = deltaNumerator[j] / deltaDenominator;
						String srcLangWord = srcLangWords[j];

						Map<String, Double> destLangMap = translationMappingCountMap
								.get(srcLangWord);
						if (destLangMap == null) {
							destLangMap = new HashMap<String, Double>();
							translationMappingCountMap.put(srcLangWord,
									destLangMap);
						}
						double mappedCount = 0d;
						if (destLangMap.containsKey(destLangWord)) {
							mappedCount = destLangMap.get(destLangWord);
						}
						destLangMap.put(destLangWord, mappedCount + delta);

						double srcWordCount = 0d;
						if (translationSrcWordCountMap.containsKey(srcLangWord)) {
							srcWordCount = translationSrcWordCountMap
									.get(srcLangWord);
						}
						translationSrcWordCountMap.put(srcLangWord,
								srcWordCount + delta);
					}
				}
			}

			System.out.println("Going to calculate t & q values now");
			for (Entry<String, Map<String, Double>> srcLangEntry : tMap
					.entrySet()) {
				String srcLangKey = srcLangEntry.getKey();
				Map<String, Double> destLangTValMap = srcLangEntry.getValue();
				Map<String, Double> destLangTranslationMap = translationMappingCountMap
						.get(srcLangKey);
				double srcLangTranslationWordCount = translationSrcWordCountMap
						.get(srcLangKey);
				for (Entry<String, Double> destLangEntry : destLangTValMap
						.entrySet()) {
					String destLangKey = destLangEntry.getKey();
					double translationMappingWordCount = destLangTranslationMap
							.get(destLangKey);
					destLangEntry.setValue(translationMappingWordCount
							/ srcLangTranslationWordCount);
				}
			}
		}
	}

	private void preprocessInput(List<String> englishSentences,
			List<String> foreignSentences) {

		if (englishSentences != null && foreignSentences != null) {
			for (int index = 0; index < englishSentences.size(); index++) {
				String engSent = "NULL " + englishSentences.get(index);
				String foreignSent = foreignSentences.get(index);
				String[] engWords = engSent.split("\\s+");
				String[] foreignWords = foreignSent.split("\\s+");
				Set<String> foreignWordsSet = new HashSet<String>(
						foreignWords.length);
				for (String foreignWord : foreignWords) {
					foreignWordsSet.add(foreignWord);
				}
				for (int j = 0; j < engWords.length; j++) {
					// tVal code
					Set<String> mappedForeignWordsSet = englishToForeignWordsMap
							.get(engWords[j]);
					if (mappedForeignWordsSet == null) {
						// System.out.println(engWords[j]);
						mappedForeignWordsSet = new HashSet<String>();
						englishToForeignWordsMap.put(engWords[j],
								mappedForeignWordsSet);
					}
					mappedForeignWordsSet.addAll(foreignWordsSet);
				}
			}
		}
		System.out.println("Total English WOrds : "
				+ englishToForeignWordsMap.size());
	}

	private double getTVal(String destLangWord, String srcLangWord,
			boolean returnZero) {

		Map<String, Double> destMap = tMap.get(srcLangWord);

		if (destMap != null) {
			if (destMap.containsKey(destLangWord)) {
				return destMap.get(destLangWord);
			} else if (returnZero) {
				return 0d;
			}
		} else {
			destMap = new HashMap<String, Double>();
			tMap.put(srcLangWord, destMap);
		}
		double tVal = 0d;
		Set<String> mappedSet = englishToForeignWordsMap.get(srcLangWord);
		if (!(mappedSet == null || mappedSet.isEmpty())) {
			tVal = 1f / mappedSet.size();
		}
		destMap.put(destLangWord, tVal);
		return tVal;
	}

	private double getQVal(int j, int i, int l, int m, boolean returnZero) {
		JILMKey jilmKey = new JILMKey(j, new ILMKey(i, l, m));
		if (qMap.containsKey(jilmKey)) {
			return qMap.get(jilmKey);
		} else if (returnZero) {
			return 0d;
		} else {
			qMap.put(jilmKey, 1d / (l + 1));
			return qMap.get(jilmKey);
		}
	}

	public List<String> findAlignments(List<String> srcLangSentences,
			List<String> destLangSentences, boolean addNull) {
		List<String> output = new ArrayList<String>();
		if (srcLangSentences != null && destLangSentences != null
				&& srcLangSentences.size() == destLangSentences.size()) {
			for (int index = 0; index < destLangSentences.size(); index++) {
				String[] destLangWords = destLangSentences.get(index).split(
						"\\s+");
				String[] srcLangWords = srcLangSentences.get(index).split(
						"\\s++");

				for (int i = 0; i < destLangWords.length; i++) {
					String destWord = destLangWords[i];
					double maxTQVal = 0d;
					int max_j = -1;
					for (int j = 0; j < srcLangWords.length; j++) {
						String srcWord = srcLangWords[j];
						Double tQVal = getTVal(destWord, srcWord, true)
								* getQVal((j + 1), i, srcLangWords.length + 1,
										destLangWords.length, true);
						if (Double.compare(tQVal, maxTQVal) != -1) {
							maxTQVal = tQVal;
							max_j = j;
						}
					}
					if (max_j != -1) {
						output.add((index + 1) + " " + (max_j + 1) + " "
								+ (i + 1));
					}
				}
			}
		}
		return output;
	}

	private static class ILMKey {
		private int i;
		private int l;
		private int m;

		public ILMKey(int i, int l, int m) {
			this.i = i;
			this.l = l;
			this.m = m;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + i;
			result = prime * result + l;
			result = prime * result + m;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ILMKey other = (ILMKey) obj;
			if (i != other.i)
				return false;
			if (l != other.l)
				return false;
			if (m != other.m)
				return false;
			return true;
		}

	}

	private static class JILMKey {
		private int j;
		private ILMKey ilmKey;

		public JILMKey(int j, ILMKey ilmKey) {
			this.j = j;
			this.ilmKey = ilmKey;
		}

		public ILMKey getILMKey() {
			return ilmKey;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((ilmKey == null) ? 0 : ilmKey.hashCode());
			result = prime * result + j;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			JILMKey other = (JILMKey) obj;
			if (ilmKey == null) {
				if (other.ilmKey != null)
					return false;
			} else if (!ilmKey.equals(other.ilmKey))
				return false;
			if (j != other.j)
				return false;
			return true;
		}

	}
}
