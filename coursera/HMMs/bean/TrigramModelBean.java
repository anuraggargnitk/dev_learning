package com.dexter.rnd.nlp.bean;

import java.util.HashMap;
import java.util.Map;

import com.dexter.rnd.nlp.bean.TrigramModelEstimator.BigramKey;
import com.dexter.rnd.nlp.bean.TrigramModelEstimator.TrigramKey;

public class TrigramModelBean {

	private Map<TrigramKey, Integer> trigramCountMap = new HashMap<TrigramKey, Integer>();

	private Map<BigramKey, Integer> bigramCountMap = new HashMap<BigramKey, Integer>();

	public float getTrigramEstimateValue(String v, String w, String u) {
		BigramKey bigramKey = new BigramKey(u, w);
		TrigramKey trigramKey = new TrigramKey(v, bigramKey);
		if (trigramCountMap.containsKey(trigramKey)) {
			float trigramCount = trigramCountMap.get(trigramKey) + 0f;
			float bigramCount = bigramCountMap.get(bigramKey);
			return trigramCount / bigramCount;
		}
		return 0f;
	}

	public void calculateTrigramEstimateCount(TrigramKey trigram) {
		Integer trigramCount = trigramCountMap.get(trigram);
		if (trigramCount == null) {
			trigramCount = 0;
		}
		trigramCountMap.put(trigram, trigramCount + 1);

		BigramKey bigram = trigram.getBigramKey();
		Integer bigramCount = bigramCountMap.get(bigram);
		if (bigramCount == null) {
			bigramCount = 0;
		}
		bigramCountMap.put(bigram, bigramCount + 1);
	}

}
