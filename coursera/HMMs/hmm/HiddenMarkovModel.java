package com.dexter.rnd.nlp.hmm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.dexter.rnd.nlp.bean.EmissionParamBean;
import com.dexter.rnd.nlp.bean.TrigramModelBean;
import static com.dexter.rnd.nlp.constant.NLPConstant.SPACE_REGEX;

public class HiddenMarkovModel {

	/**
	 * Using Viterbi Algorithm
	 */

	private static final String[] __STAR_SET = { "*" };

	private Map<ViterbiBean, Float> maxValuesMap = new HashMap<ViterbiBean, Float>();

	private EmissionParamBean emissionParamBean = null;

	private TrigramModelBean trigramModelBean = null;

	private String[] tagSet;

	public HiddenMarkovModel(EmissionParamBean emissionParamBean,
			TrigramModelBean trigramModelBean, String[] tagSet) {
		for (String u : __STAR_SET) {
			for (String w : __STAR_SET) {
				maxValuesMap.put(new ViterbiBean(w, u, 0), 1f);
			}
		}
		this.emissionParamBean = emissionParamBean;
		this.trigramModelBean = trigramModelBean;
		this.tagSet = tagSet;
	}

	public float generateTags(String testSentence) {
		String[] testWords = testSentence.split(SPACE_REGEX);

		String max_u = null, max_v = null;

		for (int k = 1; k <= testWords.length; k++) {
			for (String u : getTagSet(k - 1)) {
				float maxPieVal = 0f;
				for (String v : getTagSet(k)) {
					float maxPieValForW = 0f;
					for (String w : getTagSet(k - 2)) {
						float previousPieVal = maxValuesMap
								.get(new ViterbiBean(w, u, k - 1));
						float emissionParamVal = emissionParamBean
								.getEmissionParamValue(testWords[k - 1], v);
						float probVal = trigramModelBean
								.getTrigramEstimateValue(v, w, u);
						float currentPieVal = previousPieVal * emissionParamVal
								* probVal;
						if (Float.compare(currentPieVal, maxPieValForW) != -1) {
							maxPieValForW = currentPieVal;
						}
					}
					if (k == testWords.length && maxPieValForW > maxPieVal) {
						maxPieVal = maxPieValForW;
						max_u = u;
						max_v = v;
					}
					maxValuesMap.put(new ViterbiBean(u, v, k), maxPieValForW);
					System.out.println("pie( " + k + ", " + u + ", " + v
							+ " ) = " + maxPieValForW);

				}
			}
		}
		if (max_u != null & max_v != null) {
			return maxValuesMap.get(new ViterbiBean(max_u, max_v,
					testWords.length))
					* trigramModelBean.getTrigramEstimateValue("STOP", max_u,
							max_v);
		}
		return 0;
		// TODO: generate the sequence and multiply with q(STOP|u,v)
	}

	private String[] getTagSet(int k) {
		return (k == 0) ? __STAR_SET : ((k == -1) ? __STAR_SET : tagSet);
	}

	private static class ViterbiBean {

		private String u;

		private String v;

		private Integer k;

		ViterbiBean(String u, String v, Integer k) {
			super();
			this.u = u;
			this.v = v;
			this.k = k;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((k == null) ? 0 : k.hashCode());
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
			ViterbiBean other = (ViterbiBean) obj;
			if (k == null) {
				if (other.k != null)
					return false;
			} else if (!k.equals(other.k))
				return false;
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

}
