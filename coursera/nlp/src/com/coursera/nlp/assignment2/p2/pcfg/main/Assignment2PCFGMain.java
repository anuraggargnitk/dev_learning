package com.coursera.nlp.assignment2.p2.pcfg.main;

import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;

import com.coursera.nlp.assignment.file.mgmt.FileReader;
import com.coursera.nlp.assignment.file.mgmt.FileWriter;
import com.coursera.nlp.assignment2.p2.pcfg.CKYAlgorithm;
import com.coursera.nlp.assignment2.p2.pcfg.PCFGCounts;

public class Assignment2PCFGMain {

	public static void main(String[] args) {
		FileWriter writer = FileWriter.getInstance();
		FileReader reader = FileReader.getInstance();

		//executePart1(reader, writer, 5, "_RARE_");

		executePart2(reader, writer, 5, "_RARE_");

	}

	private static void executePart2(FileReader reader, FileWriter writer,
			int rareWordCountLimit, String rareWordsReplacementString) {

		List<String> treeInputCountRows = reader
				.read("D:/personal/docs/courses/NLP/coursera/exercises/h2.2/assignment/parse_train.counts.out");

		PCFGCounts pcfgCounts = new PCFGCounts();
		pcfgCounts.registerCounts(treeInputCountRows);

		CKYAlgorithm ckyAlgo = new CKYAlgorithm(pcfgCounts,
				rareWordsReplacementString);

		// dev test data
		List<String> devTestDataSentences = reader
				.read("D:/personal/docs/courses/NLP/coursera/exercises/h2.2/assignment/parse_dev.dat");

		List<String> devSentencesParseTreesList = ckyAlgo.generateParseTree(devTestDataSentences, "SBARQ");
		// write the dev test data result - generated parse trees
		writer.write("D:/personal/docs/courses/NLP/coursera/exercises/h2.2/assignment/parse_dev.out", devSentencesParseTreesList);

		//now run it onto test data
		List<String> testDataSentences = reader
				.read("D:/personal/docs/courses/NLP/coursera/exercises/h2.2/assignment/parse_test.dat");
		// test data results
		List<String> testSentencesParseTreesList = ckyAlgo.generateParseTree(testDataSentences, "SBARQ");
		writer.write("D:/personal/docs/courses/NLP/coursera/exercises/h2.2/assignment/parse_test.p2.out", testSentencesParseTreesList);
		
	}

	private static void executePart1(FileReader reader, FileWriter writer,
			int rareWordCountLimit, String rareWordsReplacementString) {

		List<String> treeInputCountRows = reader
				.read("D:/personal/docs/courses/NLP/coursera/exercises/h2.2/assignment/cfg.counts.txt");

		PCFGCounts pcfgCounts = new PCFGCounts();
		pcfgCounts.registerCounts(treeInputCountRows);

		Set<String> rareWords = pcfgCounts.getRareWordsSet(rareWordCountLimit);

		List<String> wordTrees = reader
				.read("D:/personal/docs/courses/NLP/coursera/exercises/h2.2/assignment/parse_train.dat");
		wordTrees = replaceRareWords(wordTrees, rareWords,
				rareWordsReplacementString);
		wordTrees.add("");

		writer.write(
				"D:/personal/docs/courses/NLP/coursera/exercises/h2.2/assignment/parse_train_rare.dat",
				wordTrees);
	}

	private static List<String> replaceRareWords(List<String> wordTrees,
			Set<String> rareWords, String rareWordReplacement) {
		for (int i = 0; i < wordTrees.size(); i++) {
			wordTrees.set(
					i,
					parseJSONArray(wordTrees.get(i), rareWords,
							rareWordReplacement));
		}

		return wordTrees;
	}

	static String parseJSONArray(String jsonStr, Set<String> rareWords,
			String rareWordReplacement) {
		JSONArray jsonArray = JSONArray.fromObject(jsonStr);
		int size = jsonArray.size();
		if (size == 2) {
			if (rareWords.contains(jsonArray.getString(1))) {
				jsonArray.set(1, rareWordReplacement);
			}
		} else if (size == 3) {
			for (int i = 1; i < size; i++) {
				String str = jsonArray.getString(i);
				String modifiedStr = parseJSONArray(str, rareWords,
						rareWordReplacement);
				jsonArray.set(i, modifiedStr);
			}
		}
		return jsonArray.toString();
	}

}
