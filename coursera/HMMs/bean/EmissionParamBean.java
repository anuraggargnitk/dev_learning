package com.dexter.rnd.nlp.bean;

import java.util.HashMap;
import java.util.Map;

public class EmissionParamBean {

	private Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();

	private Map<String, Integer> map2 = new HashMap<String, Integer>();

	public float getEmissionParamValue(String word, String tag) {
		Map<String, Integer> tagMap = map.get(word);
		if (tagMap != null && tagMap.containsKey(tag)) {
			return (0f + tagMap.get(tag)) / map2.get(tag);
		}
		return 0F;
	}

	public void putEmissionParams(String word, String tag) {
		Map<String, Integer> tagMap = map.get(word);

		if (tagMap == null) {
			tagMap = new HashMap<String, Integer>();
			map.put(word, tagMap);
		}
		Integer combinationCount = tagMap.get(tag);
		if (combinationCount == null) {
			combinationCount = 0;
		}
		//count(x|y)) - word given tag count
		tagMap.put(tag, combinationCount + 1);

		//count(y) - tag count
		Integer tagCount = map2.get(tag);
		if (tagCount == null) {
			tagCount = 0;
		}
		map2.put(tag, tagCount + 1);

	}

}
