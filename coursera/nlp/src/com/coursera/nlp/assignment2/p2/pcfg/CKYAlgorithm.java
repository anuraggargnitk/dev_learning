package com.coursera.nlp.assignment2.p2.pcfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

public class CKYAlgorithm {

	private PCFGCounts pcfgCounts;

	private String rareWordsReplacementString;

	private Map<String, Float> probMap = new HashMap<String, Float>();

	private Map<String, String> backPointerMap = new HashMap<String, String>();

	public CKYAlgorithm(PCFGCounts pcfgCounts, String rareWordsReplacementString) {
		this.pcfgCounts = pcfgCounts;
		this.rareWordsReplacementString = rareWordsReplacementString;
	}

	public List<String> generateParseTree(List<String> sentences,
			String startSymbol) {
		List<String> generatedParseTreeList = new ArrayList<String>();
		for (String sentence : sentences) {

			probMap.clear();
			backPointerMap.clear();
			// sentence = "Who was President Cleveland <s wife ?";
			String[] words = sentence.split("\\s+");
			float maxProb = findMaxProbParseTree(0, words.length - 1,
					startSymbol, words);
			System.out.println("Prob for sentence : " + sentence + " is : "
					+ maxProb);
			String parseTree = tracepath(0, words.length - 1, startSymbol,
					words);
			System.out.println("Parse Tree is : " + parseTree);
			generatedParseTreeList.add(parseTree);
		}
		return generatedParseTreeList;
	}

	private float findMaxProbParseTree(int start, int end, String startSymbol,
			String[] words) {

		String key = getKey(start, end, startSymbol);
		if (probMap.containsKey(key)) {
			// System.out.println("key : " + key);
			return probMap.get(key);
		}

		if (start == end) {
			// call to pcfgcounts for unary prob - symbol and words
			float terminalProb = pcfgCounts.getUnaryRuleProb(startSymbol,
					words[start], rareWordsReplacementString);
			probMap.put(key, terminalProb);
			return terminalProb;
		}

		float maxProb = 0f;
		String maxProbSplitKey = null;
		for (int i = start; i < end; i++) {
			List<String[]> allDerivations = pcfgCounts
					.getBinaryRuleVal(startSymbol);
			if (allDerivations == null) {
				break;
			}
			for (String[] symbols : allDerivations) {
				float binaryRuleProb = pcfgCounts.getBinaryRuleProb(
						startSymbol, symbols[0], symbols[1]);
				float firstSymbolProb = findMaxProbParseTree(start, i,
						symbols[0], words);
				float prob = 0f;
				if (Float.compare(firstSymbolProb, 0) != 0) {
					float secondSymbolProb = findMaxProbParseTree(i + 1, end,
							symbols[1], words);
					prob = binaryRuleProb * firstSymbolProb * secondSymbolProb;
				}
				if (Float.compare(prob, maxProb) != -1) {
					maxProb = prob;
					maxProbSplitKey = new StringBuilder().append(i).append(" ")
							.append(symbols[0]).append(" ").append(symbols[1])
							.toString();
				}
			}

		}

		if (maxProbSplitKey != null) {
			probMap.put(key, maxProb);
			backPointerMap.put(key, maxProbSplitKey);
		}

		return maxProb;
	}

	private String getKey(int i, int j, String tag) {
		return new StringBuilder().append(i).append(" ").append(j).append(" ")
				.append(tag).toString();
	}

	private String tracepath(int i, int j, String startSymbol, String[] words) {
		JSONArray tree = new JSONArray();
		tree.add(startSymbol);
		String key = getKey(i, j, startSymbol);
		String maxProbSplitKey = backPointerMap.get(key);
		if (maxProbSplitKey == null) {
			// System.out.println("i : " + i + " j: " + j);
			tree.add(words[i]);
			return tree.toString();
		}
		String[] splitKeys = maxProbSplitKey.split("\\s+");
		tree.add(tracepath(i, Integer.parseInt(splitKeys[0]), splitKeys[1],
				words));
		tree.add(tracepath(Integer.parseInt(splitKeys[0]) + 1, j, splitKeys[2],
				words));
		return tree.toString();
	}

}
