package com.coursera.nlp.assignment1.p1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmissionProbEstimator2 {

	private Map<String, Map<String, Integer>> wordtagCountMap = new HashMap<String, Map<String, Integer>>();
	private Map<String, Integer> tagCountMap = new HashMap<String, Integer>();
	private Map<String, Integer> wordCountMap = new HashMap<String, Integer>();

	public void createModel(List<String[]> inputs) {
		for (String[] words : inputs) {
			String tag = words[2];
			String word = words[3];
			Integer wordTagCount = Integer.parseInt(words[0]);

			Map<String, Integer> map = wordtagCountMap.get(word);
			if (map == null) {
				map = new HashMap<String, Integer>();
				wordtagCountMap.put(word, map);
			}
			Integer mapWordTagCount = map.get(tag);
			if (mapWordTagCount == null) {
				mapWordTagCount = 0;
			}
			map.put(tag, mapWordTagCount + wordTagCount);
			// calculate tag counts
			Integer tagCount = tagCountMap.get(tag);
			if (tagCount == null) {
				tagCount = 0;
			}
			tagCountMap.put(tag, tagCount + wordTagCount);
			// calculate word counts
			Integer wordCount = wordCountMap.get(word);
			if (wordCount == null) {
				wordCount = 0;
			}
			wordCountMap.put(word, wordCount + wordTagCount);
		}
	}

	public void accomodateInfrequentWords(int count, String replacementWord) {
		int replacementWordCount = 0;
		Map<String, Integer> replacementWordTagMap = new HashMap<String, Integer>();
		Iterator<String> wordIterator = wordCountMap.keySet().iterator();
		while (wordIterator.hasNext()) {
			String word = wordIterator.next();
			Integer wordCount = wordCountMap.get(word);
			if (wordCount < count) {
				wordIterator.remove();
				replacementWordCount = replacementWordCount + wordCount;

				Map<String, Integer> map = wordtagCountMap.remove(word);
				for (String tag : map.keySet()) {
					Integer replacementWordTagCount = replacementWordTagMap
							.get(tag);
					if (replacementWordTagCount == null) {
						replacementWordTagCount = 0;
					}
					replacementWordTagMap.put(tag, replacementWordTagCount
							+ map.get(tag));
				}
			}
		}
		wordCountMap.put(replacementWord, replacementWordCount);
		wordtagCountMap.put(replacementWord, replacementWordTagMap);
	}

	public float calculateEmissionProbabilty(String word, String tag,
			String replacementWord) {
		Map<String, Integer> map = wordtagCountMap.get(word);
		if (map == null) {
			map = wordtagCountMap.get(replacementWord);
		}
		Integer wordTagCount = map.get(tag);
		if (wordTagCount == null) {
			return 0f;
		}
		Integer tagCount = tagCountMap.get(tag);

		return (wordTagCount + 0f) / tagCount;
	}

	public List<String> argMaxTag(List<String> inputList, String replacementWord) {
		List<String> list = new ArrayList<String>();

		Set<String> tags = tagCountMap.keySet();
		for (String inputWord : inputList) {
			if (inputWord != null && inputWord.length() > 0) {
				float maxProb = -1f;
				String maxArgTag = null;
				for (String tag : tags) {
					float emissionProb = calculateEmissionProbabilty(inputWord,
							tag, replacementWord);
					if (Float.compare(maxProb, emissionProb) != 1) {
						maxArgTag = tag;
						maxProb = emissionProb;
					}
				}
				list.add(new StringBuffer().append(inputWord).append(" ")
						.append(maxArgTag).toString());
			} else {
				list.add(inputWord);
			}
		}

		return list;
	}

	public String[] getTagList() {
		Set<String> tagSet = tagCountMap.keySet();
		return tagSet.toArray(new String[0]);
	}
}