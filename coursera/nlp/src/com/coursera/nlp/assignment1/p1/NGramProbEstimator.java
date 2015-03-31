package com.coursera.nlp.assignment1.p1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NGramProbEstimator {

	private Map<TrigramKey, Integer> trigramCountMap = new HashMap<TrigramKey, Integer>();
	private Map<BigramKey, Integer> bigramCountMap = new HashMap<BigramKey, Integer>();
	private Map<String, Integer> unigramCountMap = new HashMap<String, Integer>();
	private int totalWordCount = 0;	
	
	public void createModel(List<String[]> input) {
		for (String[] words : input) {
			int nGram = words[1].codePointAt(0) - 48;
			int count = Integer.parseInt(words[0]);
			switch (nGram) {
			case 1:
				unigramCountMap.put(words[2], count);
				totalWordCount = totalWordCount + count;
				break;
			case 2:
				BigramKey bigramKey = new BigramKey(words[2], words[3]);
				bigramCountMap.put(bigramKey, count);
				break;
			case 3:
				TrigramKey trigramKey = new TrigramKey(words[4], new BigramKey(
						words[2], words[3]));
				trigramCountMap.put(trigramKey, count);
				break;
			}
		}
	}

	public float getTrigramProbability(String w, String u, String v) {
		BigramKey bigram = new BigramKey(w, u);
		return (trigramCountMap.get(new TrigramKey(v, bigram)) + 0f)
				/ bigramCountMap.get(bigram);
	}

	private static class BigramKey {
		private String u;

		private String v;

		private BigramKey(String u, String v) {
			super();
			this.u = u;
			this.v = v;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((u == null) ? 0 : u.hashCode());
			result = prime * result + ((v == null) ? 0 : v.hashCode());
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
			BigramKey other = (BigramKey) obj;
			if (u == null) {
				if (other.u != null)
					return false;
			} else if (!u.equals(other.u))
				return false;
			if (v == null) {
				if (other.v != null)
					return false;
			} else if (!v.equals(other.v))
				return false;
			return true;
		}

	}

	private static class TrigramKey {
		private String v;

		private BigramKey bigramKey;

		private TrigramKey(String v, BigramKey bigramKey) {
			super();
			this.v = v;
			this.bigramKey = bigramKey;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((bigramKey == null) ? 0 : bigramKey.hashCode());
			result = prime * result + ((v == null) ? 0 : v.hashCode());
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
			TrigramKey other = (TrigramKey) obj;
			if (bigramKey == null) {
				if (other.bigramKey != null)
					return false;
			} else if (!bigramKey.equals(other.bigramKey))
				return false;
			if (v == null) {
				if (other.v != null)
					return false;
			} else if (!v.equals(other.v))
				return false;
			return true;
		}

	}
}
