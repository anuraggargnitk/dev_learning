package com.dexter.rnd.nlp.bean;

import static com.dexter.rnd.nlp.constant.NLPConstant.SPACE_REGEX;

public class EmissionParamTrainer {

	public EmissionParamBean train(String[] sentences, String sentenceTags[]) {

		// TODO: exception handling and corner cases
		EmissionParamBean emissionParamBean = null;

		if (sentences == null) {

		}
		if (sentenceTags == null) {

		}

		if (sentences.length != sentenceTags.length) {

		}

		emissionParamBean = new EmissionParamBean();

		for (int i = 0; i < sentences.length; i++) {
			String[] words = sentences[i].split(SPACE_REGEX);
			String[] tags = sentenceTags[i].split(SPACE_REGEX);
			if (words != null && tags != null) {
				for (int j = 0; j < words.length; j++) {
					emissionParamBean.putEmissionParams(words[j], tags[j]);
				}
			}
		}

		return emissionParamBean;
	}

}
