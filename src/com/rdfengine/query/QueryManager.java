package com.rdfengine.query;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.sparql.core.TriplePath;
import com.hp.hpl.jena.sparql.syntax.ElementPathBlock;
import com.hp.hpl.jena.sparql.syntax.ElementVisitorBase;
import com.hp.hpl.jena.sparql.syntax.ElementWalker;
import com.rdfengine.datasetconstruction.Triplets;
import com.rdfengine.models.RDFDictionary;
import com.rdfengine.models.Triplet;

public class QueryManager {

	/*
	 * Execute Queries in files in directory
	 */
	public static void executeQuery (String queriesDirectoryPath){
		
	}
	
	/*
	 * Execute Query (star query)
	 */
	public static void executeQuery(Query query) {
		ElementWalker.walk(query.getQueryPattern(),new ElementVisitorBase(){
			@Override public void visit(ElementPathBlock elementPathBlock){
				ListIterator<TriplePath> listIterator=elementPathBlock.getPattern().iterator();
				while (listIterator.hasNext()) {
					TriplePath triplePath=listIterator.next();
					executeTriple(triplePath);
				}
			}
		});
		
		
		/*
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
		*/
	}

	private static void executeTriple(TriplePath tp) {
		System.out.println("S: " + tp.getSubject().getName() + " | P: " + tp.getPredicate().toString() + " | O: " + tp.getObject().toString());
	}

}
