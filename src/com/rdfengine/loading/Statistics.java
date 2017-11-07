package com.rdfengine.loading;

import java.util.ArrayList;
import java.util.HashMap;

import com.rdfengine.triplets.Triplet;

public abstract class Statistics {

	private static HashMap<Integer, Integer> nPredicateInstances;
	private static HashMap<Integer, Integer> nSubjectsByPredicate;
	private static HashMap<Integer, Integer> nObjectsByPredicate;
	private static HashMap<Integer, ArrayList<Integer>> subjectsByPredicate;
	private static HashMap<Integer, ArrayList<Integer>> objectsByPredicate;
	
	public static void computeTriplet(Triplet triplet)
	{
		boolean knownPredicate = false;
		int predicate = triplet.getPredicate();
		int nPredicate = 0;
		if(nPredicateInstances.containsKey(predicate))
			{
			knownPredicate = true;
			nPredicate = nPredicateInstances.get(predicate);
			}
		
		nPredicateInstances.put(triplet.getPredicate(), nPredicate+1);
		
		
	}
}
