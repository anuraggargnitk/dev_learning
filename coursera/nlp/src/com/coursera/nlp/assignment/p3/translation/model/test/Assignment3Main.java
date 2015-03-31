package com.coursera.nlp.assignment.p3.translation.model.test;

import java.util.List;

import com.coursera.nlp.assignment.file.mgmt.FileReader;
import com.coursera.nlp.assignment.file.mgmt.FileWriter;

public class Assignment3Main {

	public static void main(String[] args) {
		FileReader reader = FileReader.getInstance();
		FileWriter writer = FileWriter.getInstance();

		List<String> englishCorpus = reader
				.read("D:/personal/docs/courses/NLP/coursera/exercises/h3/corpus.en");
		List<String> spanishCorpus = reader
				.read("D:/personal/docs/courses/NLP/coursera/exercises/h3/corpus.es");

		EMAlgoAssignment emAlgo = new EMAlgoAssignment(5);
		emAlgo.estimateT(englishCorpus, spanishCorpus);
		emAlgo.estimate(englishCorpus, spanishCorpus);

		List<String> englishTrainingCorpus = reader
				.read("D:/personal/docs/courses/NLP/coursera/exercises/h3/dev.en");
		List<String> spanishTrainingCorpus = reader
				.read("D:/personal/docs/courses/NLP/coursera/exercises/h3/dev.es");
		List<String> trainingOutput = emAlgo.findAlignments(
				englishTrainingCorpus, spanishTrainingCorpus, true);
		writer.write(
				"D:/personal/docs/courses/NLP/coursera/exercises/h3/dev2.out",
				trainingOutput);

		List<String> englishTestCorpus = reader
				.read("D:/personal/docs/courses/NLP/coursera/exercises/h3/test.en");
		List<String> spanishTestCorpus = reader
				.read("D:/personal/docs/courses/NLP/coursera/exercises/h3/test.es");
		List<String> testOutput = emAlgo.findAlignments(englishTestCorpus,
				spanishTestCorpus, true);
		writer.write(
				"D:/personal/docs/courses/NLP/coursera/exercises/h3/alignment_test.p2.out",
				testOutput);
	}

}
