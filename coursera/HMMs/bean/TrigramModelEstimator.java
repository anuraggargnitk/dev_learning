package com.dexter.rnd.nlp.bean;

import static com.dexter.rnd.nlp.constant.NLPConstant.SPACE_REGEX;

public class TrigramModelEstimator {

	private static final String __STARTER_SYMBOLS = "* * ";

	public TrigramModelBean estimate(String[] data) {
		TrigramModelBean trigramModelBean = null;
		if (data != null) {
			trigramModelBean = new TrigramModelBean();
			for (int i = 0; i < data.length; i++) {
				String sentence = __STARTER_SYMBOLS + data[i];
				String[] words = sentence.split(SPACE_REGEX);
				for (int j = 2; j < words.length; j++) {
					String v = words[j];
					String u = words[j - 1];
					String w = words[j - 2];
					// count(w,u,v)
					TrigramKey trigram = new TrigramKey(v, new BigramKey(u, w));
					trigramModelBean.calculateTrigramEstimateCount(trigram);
				}
			}
		}
		return trigramModelBean;
	}

	static class BigramKey {
		private String w;

		private String u;

		public BigramKey(String u, String w) {
			super();
			this.w = w;
			this.u = u;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
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
			BigramKey other = (BigramKey) obj;
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

	static class TrigramKey {

		private String v;

		private BigramKey bigramKey;

		public TrigramKey(String v, BigramKey bigramKey) {
			super();
			this.v = v;
			this.bigramKey = bigramKey;
		}

		public BigramKey getBigramKey() {
			return bigramKey;
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