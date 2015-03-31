package com.coursera.nlp.assignment2.p2.pcfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PCFGCounts {

	private Map<String, Integer> wordCountMap = new HashMap<String, Integer>();

	private Map<String, Integer> unaryCountMap = new HashMap<String, Integer>();

	private Map<String, Integer> binaryCountMap = new HashMap<String, Integer>();

	private Map<String, Integer> nonTerminalCountMap = new HashMap<String, Integer>();

	private Map<String, List<String[]>> ruleMap = new HashMap<String, List<String[]>>();

	public void registerCounts(List<String> treeInputCountRows) {
		for (String treeInputCountRow : treeInputCountRows) {
			String[] rowElements = treeInputCountRow.split("\\s+");
			int size = rowElements.length;
			switch (size) {
			case 3:
				if ("NONTERMINAL".equals(rowElements[1])) {
					Integer nonTerminalCount = Integer.parseInt(rowElements[0]);
					String nonTerminal = rowElements[2];
					nonTerminalCountMap.put(nonTerminal, nonTerminalCount);
				}
				break;
			case 4:
				if ("UNARYRULE".equals(rowElements[1])) {
					String word = rowElements[3];
					Integer wordCount = Integer.parseInt(rowElements[0]);
					Integer initialWordCount = wordCountMap.get(word);
					if (initialWordCount == null) {
						initialWordCount = 0;
					}
					wordCountMap.put(word, initialWordCount + wordCount);

					unaryCountMap.put(new StringBuilder()
							.append(rowElements[2]).append(" ").append(word)
							.toString(), wordCount);
				}
				break;
			case 5:
				if ("BINARYRULE".equals(rowElements[1])) {
					Integer wordCount = Integer.parseInt(rowElements[0]);
					String x = rowElements[2];
					binaryCountMap.put(new StringBuilder().append(x)
							.append(" ").append(rowElements[3]).append(" ")
							.append(rowElements[4]).toString(), wordCount);
					List<String[]> derivations = ruleMap.get(x);

					if (derivations == null) {
						derivations = new ArrayList<String[]>();
						ruleMap.put(x, derivations);
					}
					derivations.add(new String[] { rowElements[3],
							rowElements[4] });
				}
				break;
			}
		}
	}

	public Set<String> getRareWordsSet(int rareWordCount) {
		Set<String> rareWordsList = new HashSet<String>();
		for (String word : wordCountMap.keySet()) {
			if (wordCountMap.get(word) < rareWordCount) {
				rareWordsList.add(word);
			} else {
				// System.out.println("Not rare : " + word);
			}
		}
		return rareWordsList;
	}

	public float getBinaryRuleProb(String x, String y1, String y2) {
		String binaryKey = new StringBuffer().append(x).append(" ").append(y1)
				.append(" ").append(y2).toString();
		return (binaryCountMap.get(binaryKey) + 0f)
				/ nonTerminalCountMap.get(x);
	}

	public float getUnaryRuleProb(String x, String w,
			String rareWordsReplacementString) {
		if (!wordCountMap.containsKey(w)) {
			//System.out.println("word not present : " + w);
			w = rareWordsReplacementString;
		}
		String unaryKey = new StringBuilder(x).append(" ").append(w).toString();
		if (unaryCountMap.containsKey(unaryKey)) {
			return unaryCountMap.get(unaryKey)
					/ (nonTerminalCountMap.get(x) + 0f);
		} else {
			return 0f;
		}
	}

	public List<String[]> getBinaryRuleVal(String tag) {
		return ruleMap.get(tag);
	}

}
