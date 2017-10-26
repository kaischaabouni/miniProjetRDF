package com.rdfengine.query;

import java.util.ArrayList;

import com.rdfengine.datasetconstruction.Triplets;
import com.rdfengine.models.RDFDictionary;
import com.rdfengine.models.Triplet;

public class QueryManager {

	public static void executeQuery(String string) {
		RDFDictionary dictionary = RDFDictionary.getInstance(); 
		int idPredicate = dictionary.getId("<http://schema.org/nationality>");
		int idObject = dictionary.getId("<http://db.uwaterloo.ca/~galuc/wsdbm/Country3>");
		ArrayList<Triplet> resultQuery = new ArrayList<Triplet>();
		for(Triplet triplet :Triplets.getTriplets()){
			if(triplet.getPredicate() == idPredicate && triplet.getObject() == idObject){
				resultQuery.add(triplet);
			}
		}
		
		// Print result
		System.out.println("Query Result: ");	
		for(Triplet triplet : resultQuery){
			System.out.println(dictionary.getResource(triplet.getSubject()));	
		}
	}

}
