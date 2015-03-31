package com.coursera.nlp.assignment1.p1.main;

import java.util.ArrayList;
import java.util.List;

import com.coursera.nlp.assignment.file.mgmt.FileReader;
import com.coursera.nlp.assignment.file.mgmt.FileWriter;
import com.coursera.nlp.assignment1.p1.EmissionProbEstimator;
import com.coursera.nlp.assignment1.p1.NGramProbEstimator;
import com.coursera.nlp.assignment1.p1.POSTagger;

public class Assignment1Main {
	public static void main(String[] args) {
		FileReader fileReader = FileReader.getInstance();
		List<String> trainingDataCountList = fileReader
				.read("D:/personal/docs/courses/NLP/coursera/h1-p/count.txt");

		List<String[]> wordAndTagData = new ArrayList<String[]>();
		List<String[]> nGramData = new ArrayList<String[]>();

		for (String trainingStr : trainingDataCountList) {
			String[] words = trainingStr.split("\\s+");
			if ("WORDTAG".equalsIgnoreCase(words[1])) {
				wordAndTagData.add(words);
			} else if (words[1].endsWith("-GRAM")) {
				nGramData.add(words);
			}
		}

		NGramProbEstimator nGramProbEstimator = new NGramProbEstimator();
		nGramProbEstimator.createModel(nGramData);

		EmissionProbEstimator emissionProbEstimator = new EmissionProbEstimator();
		emissionProbEstimator.createModel(wordAndTagData);
		int infrequentCount = 5;
		FileWriter fileWriter = FileWriter.getInstance();

		String __REPLACEMENT_WORD = "_RARE_";

		emissionProbEstimator.accomodateInfrequentWords(infrequentCount,
				__REPLACEMENT_WORD);

		POSTagger posTagger = new POSTagger(emissionProbEstimator,
				nGramProbEstimator, emissionProbEstimator.getTagList());

		List<String> actualDevTestData = fileReader
				.read("D:/personal/docs/courses/NLP/coursera/h1-p/gene.dev");

		List<String> devTestDataWithoutBlankLines = removeBlankLines(actualDevTestData);

		List<String> devOutput = posTagger.tagUsingViterbi(
				devTestDataWithoutBlankLines, __REPLACEMENT_WORD);

		addBlankLinesToOutput(actualDevTestData, devOutput);

		fileWriter.write(
				"D:/personal/docs/courses/NLP/coursera/h1-p/gene_dev.p1.out",
				devOutput);

		List<String> actualFinalTestData = fileReader
				.read("D:/personal/docs/courses/NLP/coursera/h1-p/gene.test");

		List<String> actualFinalTestDataWithoutBlankLines = removeBlankLines(actualFinalTestData);
		
		List<String> finalTestOutput = posTagger.tagUsingViterbi(actualFinalTestDataWithoutBlankLines,
				__REPLACEMENT_WORD);

		addBlankLinesToOutput(actualFinalTestData, finalTestOutput);
		
		fileWriter.write(
				"D:/personal/docs/courses/NLP/coursera/h1-p/gene_test.p1.out",
				finalTestOutput);

	}

	private static List<String> removeBlankLines(List<String> input) {
		List<String> listWithoutBlankLines = new ArrayList<String>();

		for (String str : input) {
			if (!str.trim().isEmpty()) {
				listWithoutBlankLines.add(str);
			}
		}

		return listWithoutBlankLines;
	}

	private static List<String> addBlankLinesToOutput(List<String> actualInput,
			List<String> devOutput) {
		for (int i = 0; i < actualInput.size(); i++) {
			if (actualInput.get(i).trim().isEmpty()) {
				devOutput.add(i, actualInput.get(i));
			}
		}
		return devOutput;
	}

}
