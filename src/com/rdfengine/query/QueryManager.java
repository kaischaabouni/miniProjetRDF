package com.rdfengine.query;

import java.util.ArrayList;
import java.util.ListIterator;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.sparql.core.TriplePath;
import com.hp.hpl.jena.sparql.syntax.ElementPathBlock;
import com.hp.hpl.jena.sparql.syntax.ElementVisitorBase;
import com.hp.hpl.jena.sparql.syntax.ElementWalker;
import com.rdfengine.datasetconstruction.Triplets;
import com.rdfengine.models.Dictionary;
import com.rdfengine.models.Triplet;

public class QueryManager {

	/*
	 * Temporary or Final Result
	 */
	private static ArrayList<Integer> queryResult = null;

	/*
	 * Execute Queries in files in directory
	 */
	public static void executeQuery (String queriesDirectoryPath){

	}

	/*
	 * Execute Query (star query)
	 */
	public static void executeQuery(Query query) {

		// initiate result to null
		queryResult = null;

		// Parsing the query and executing triple patterns
		ElementWalker.walk(query.getQueryPattern(),new ElementVisitorBase(){
			@Override public void visit(ElementPathBlock elementPathBlock){
				ListIterator<TriplePath> listIterator=elementPathBlock.getPattern().iterator();
				while (listIterator.hasNext()) {
					TriplePath triplePath=listIterator.next();
					executeTriplePatternWithJoin(triplePath, queryResult);
				}
			}
		});
	}

	/*
	 * Execute a triple pattern
	 */
	private static void executeTriplePatternWithJoin(TriplePath tp, ArrayList<Integer> joinTable) {
		Dictionary dictionary = Dictionary.getInstance();
		int predicateID;
		int objectID;

		// Add <> if resource or "" if literal
		String predicate = "<" + tp.getPredicate().toString() + ">";
		String object;
		if(tp.getObject().isLiteral()){
			object = "\"" + tp.getObject().toString() + "\"";
		} else {
			object = "<" + tp.getObject().toString() + ">";
		}

		predicateID = dictionary.getId(predicate);
		objectID = dictionary.getId(object);

		queryResult = new ArrayList<Integer>();

		for(Triplet triple :Triplets.getTriplets()){
			if((joinTable == null || joinTable.contains(triple.getSubject())) 
					&& triple.getPredicate() == predicateID 
					&& triple.getObject() == objectID){
				
				// Add the subject ID if it is not already added
				if(! queryResult.contains(triple.getSubject())){
					queryResult.add(triple.getSubject());
				}
			}
		}
	}
	
	/*
	 * Display Query Result
	 */
	public static void displayQueryResult(){
		Dictionary dictionary = Dictionary.getInstance();
		System.out.println("---------------------------------------------------------------");
		System.out.println("| Result                                                      |");
		System.out.println("===============================================================");
		for(int subjectID : queryResult){
			System.out.println(dictionary.getResource(subjectID));
		}
		System.out.println("---------------------------------------------------------------");
	}
}
