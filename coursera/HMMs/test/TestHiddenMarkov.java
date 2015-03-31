package com.dexter.rnd.nlp.test;

import com.dexter.rnd.nlp.bean.EmissionParamBean;
import com.dexter.rnd.nlp.bean.EmissionParamTrainer;
import com.dexter.rnd.nlp.bean.TrigramModelBean;
import com.dexter.rnd.nlp.bean.TrigramModelEstimator;
import com.dexter.rnd.nlp.hmm.HiddenMarkovModel;

public class TestHiddenMarkov {

	public static void main(String[] args) {

		String[] trainingSentences = { "the dog saw the cat",
				"the cat saw the saw" };
		String[] trainingSentenceTags = { "D N V D N STOP", "D N V D N STOP" };
		EmissionParamBean paramBean = new EmissionParamTrainer().train(
				trainingSentences, trainingSentenceTags);
		TrigramModelBean trigramModelBean = new TrigramModelEstimator()
				.estimate(trainingSentenceTags);
		String testSentence = "the cat saw the saw";
		String[] tagSet = { "D", "N", "V" };
		System.out.println("Final Answer : "
				+ new HiddenMarkovModel(paramBean, trigramModelBean, tagSet)
						.generateTags(testSentence));
	}

}
