package com.dexter.coursera.recsys.wa4;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Test {
	public static void main(String[] args) {
		PriorityQueue<AbstractMap.SimpleEntry<String, Integer>> a = new PriorityQueue<AbstractMap.SimpleEntry<String, Integer>>(
				5, new Comparator<AbstractMap.SimpleEntry<String, Integer>>(){

					@Override
					public int compare(SimpleEntry<String, Integer> o1,
							SimpleEntry<String, Integer> o2) {
						// TODO Auto-generated method stub
						return o1.getValue().compareTo(o2.getValue());
					}});

		for (int i = 0; i < 10; i++) {
			Integer val = Integer.parseInt("" + Math.round(Math.random() * 1000));
			System.out.println("Adding integer : " + val);
			a.add(getEntry("an" + i,val));
		}

		System.out.println("Size of Priority Queue : " + a.size());
		for(int i = 0;i<10;i++)	{
			System.out.println("Removing : " + a.remove().getValue());
		}
	}

	static AbstractMap.SimpleEntry<String, Integer> getEntry(String key,
			Integer val) {
		AbstractMap.SimpleEntry<String, Integer> simUsrVal = new AbstractMap.SimpleEntry(
				key, val);
		return simUsrVal;
	}
}
