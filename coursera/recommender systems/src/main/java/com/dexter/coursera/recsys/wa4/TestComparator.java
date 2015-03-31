package com.dexter.coursera.recsys.wa4;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;

public class TestComparator implements Comparator<AbstractMap.SimpleEntry<String, Integer>>	{

	@Override
	public int compare(SimpleEntry<String, Integer> o1,
			SimpleEntry<String, Integer> o2) {
		// TODO Auto-generated method stub
		return o1.getValue().compareTo(o2.getValue());
		//return 0;
	}

}
