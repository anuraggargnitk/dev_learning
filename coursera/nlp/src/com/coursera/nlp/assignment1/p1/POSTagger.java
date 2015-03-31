package com.coursera.nlp.assignment1.p1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POSTagger {

	private EmissionProbEstimator emissionProbEstimator;

	private NGramProbEstimator nGramProbEstimator;

	private Map<ViterbiKey, Float> pieVals = new HashMap<POSTagger.ViterbiKey, Float>();

	private Map<ViterbiKey, String> pieTags = new HashMap<POSTagger.ViterbiKey, String>();

	private static final String __STAR = "*";

	private static final String[] __DEFAULT_TAG_SET = { __STAR };

	private String[] tagSet = null;

	private static final float __MIN_VALUE = Float.NEGATIVE_INFINITY;

	public POSTagger(EmissionProbEstimator emissionProbEstimator,
			NGramProbEstimator nGramProbEstimator, String[] tagSet) {
		this.emissionProbEstimator = emissionProbEstimator;
		this.nGramProbEstimator = nGramProbEstimator;
		this.tagSet = tagSet;
		pieVals.put(new ViterbiKey(__STAR, __STAR, 0), 0f);
	}

	public List<String> tagUsingViterbi(List<String> inputs,
			String replacementWord) {
		float maxProb = __MIN_VALUE;
		String uTag = null;
		String vTag = null;
		for (int k = 1; k <= inputs.size(); k++) {
			for (String v : getTagSet(k)) {
				float emissionProb = emissionProbEstimator
						.calculateEmissionProbabilty(inputs.get(k - 1), v,
								replacementWord);
				for (String u : getTagSet(k - 1)) {
					float maxPieVal = __MIN_VALUE;
					String wTag = null;
					for (String w : getTagSet(k - 2)) {
						float nGramProb = nGramProbEstimator
								.getTrigramProbability(w, u, v);
						Float previousPieVal = pieVals.get(new ViterbiKey(w, u,
								k - 1));

						if (previousPieVal == null
								|| Float.compare(0f, nGramProb) == 0
								|| Float.compare(0f, emissionProb) == 0) {
							continue;
						}
						float currentPieVal = previousPieVal
								+ (float) Math.log(nGramProb)
								+ (float) Math.log(emissionProb);
						if (Float.compare(currentPieVal, maxPieVal) != -1) {
							maxPieVal = currentPieVal;
							wTag = w;
						}
					}
					if (Float.compare(__MIN_VALUE, maxPieVal) != 0) {
						ViterbiKey key = new ViterbiKey(u, v, k);
						pieVals.put(key, maxPieVal);
						System.out.println("{ u=" + u + " , v=" + v + " , k="
								+ k + "} = " + maxPieVal);
						pieTags.put(key, wTag);
					}
					if (k == inputs.size()) {
						if (Float.compare(maxPieVal, maxProb) != -1) {
							maxProb = maxPieVal;
							uTag = u;
							vTag = v;
						}
					}
				}
			}
			System.out.println("\n");
		}

		if (uTag != null & vTag != null) {
			maxProb = maxProb
					* nGramProbEstimator.getTrigramProbability(uTag, vTag,
							"STOP");
			String v = vTag, u = uTag;
			for (int i = inputs.size(); i > 0; i--) {
				String wordAndTag = inputs.get(i - 1) + " " + v;
				inputs.set(i - 1, wordAndTag);
				String w = pieTags.get(new ViterbiKey(u, v, i));
				v = u;
				u = w;
			}
		}
		return inputs;
	}

	private String[] getTagSet(int i) {
		return (i == -1 || i == 0) ? __DEFAULT_TAG_SET : tagSet;
	}

	private static class ViterbiKey {
		private String w;

		private String u;

		private int k;

		public ViterbiKey(String w, String u, int k) {
			super();
			this.w = w;
			this.u = u;
			this.k = k;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + k;
			result = prime * result + ((u == null) ? 0 : u.hashCode());
			result = prime * result + ((w == null) ? 0 : w.hashCode());
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
			ViterbiKey other = (ViterbiKey) obj;
			if (k != other.k)
				return false;
			if (u == null) {
				if (other.u != null)
					return false;
			} else if (!u.equals(other.u))
				return false;
			if (w == null) {
				if (other.w != null)
					return false;
			} else if (!w.equals(other.w))
				return false;
			return true;
		}
	}

}
