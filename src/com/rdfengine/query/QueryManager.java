package com.rdfengine.query;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.TreeSet;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.core.TriplePath;
import com.hp.hpl.jena.sparql.syntax.ElementPathBlock;
import com.hp.hpl.jena.sparql.syntax.ElementVisitorBase;
import com.hp.hpl.jena.sparql.syntax.ElementWalker;
import com.rdfengine.loading.AllProperties;
import com.rdfengine.loading.Dictionary;
//import com.rdfengine.loading.Loader;
import com.rdfengine.models.TriplePatternOfStarQuery;

public class QueryManager {

	private static final String RESULT_FILE_NAME = "result.csv";

	/*
	 * Query Status Information
	 */
	private static ArrayList<Integer> queryResult = null;
	public static ArrayList<ArrayList<Integer>> queriesResults;
	private static boolean queryExecutionCompleted = false;
	private static ArrayList<TriplePatternOfStarQuery> triplePatternsList;
	private static ArrayList<ArrayList<TriplePatternOfStarQuery>> QueriesTriplePatternsList;
	private static String subjectVariableName = null;



	/**
	 * Pre-Process Query extract triple patterns
	 */
	public static ArrayList<TriplePatternOfStarQuery> preProcessQuery(Query query){

		// initialize 
		triplePatternsList = new ArrayList<TriplePatternOfStarQuery>();
		
		
		subjectVariableName = null;
		queryExecutionCompleted = false;

		// Parsing the query and executing triple patterns
		ElementWalker.walk(query.getQueryPattern(),new ElementVisitorBase(){
			@Override public void visit(ElementPathBlock elementPathBlock){
				ListIterator<TriplePath> listIterator=elementPathBlock.getPattern().iterator();
				while (!queryExecutionCompleted && listIterator.hasNext()) {
					TriplePath triplePath=listIterator.next();

					// Add <> if resource or "" if literal
					String predicate = "<" + triplePath.getPredicate().toString() + ">";
					String object;
					if(triplePath.getObject().isLiteral()){
						object = "\"" + triplePath.getObject().toString() + "\"";
					} else {
						object = "<" + triplePath.getObject().toString() + ">";
					}

					// create TripplePatternOfStarQuery
					subjectVariableName = triplePath.getSubject().getName();
					Integer predicateID; 
					Integer objectID;
					int numberOccurencesMin = -1;
					if(Dictionary.getInstance().containsResource(predicate) 
							&& Dictionary.getInstance().containsResource(object)){
						predicateID = Dictionary.getInstance().getId(predicate);
						objectID = Dictionary.getInstance().getId(object);
						
						int currentNumberOcurrencesOfProperty = 
								AllProperties.getNumberOcurrencesOfProperty(predicateID);
						if(numberOccurencesMin >= 0 && 
								currentNumberOcurrencesOfProperty < numberOccurencesMin){
							numberOccurencesMin = currentNumberOcurrencesOfProperty;
							
							// add TripplePatternOfStarQuery to the head of triplePatternsList
							triplePatternsList.add(0, new TriplePatternOfStarQuery(predicateID, objectID));
						} else {
							// add TripplePatternOfStarQuery to the end of triplePatternsList
							triplePatternsList.add(new TriplePatternOfStarQuery(predicateID, objectID));
						}
					} else {
						
						// resource doesn't exist in dictionary, no need to read the res of triple-pattern 
						queryExecutionCompleted = true;
						
					}
				}
			}
		});
		return triplePatternsList;
	}


	/**
	 * Execute Query 
	 */
	public static ArrayList<Integer> executeQuery(ArrayList<TriplePatternOfStarQuery> query) {

		// initialize
		queryResult = new ArrayList<Integer>();

		// Iterator for triplePatternsList
		Iterator<TriplePatternOfStarQuery> iterator = query.iterator();
		queryExecutionCompleted = false;
		// Execute First Triple Pattern
		if(!queryExecutionCompleted && iterator.hasNext()){
			executeTriplePattern(iterator.next());
			
		}

		// Execute the rest of triple patterns with join with (temporary result) if the query is not completed
		while (!queryExecutionCompleted && iterator.hasNext()) {
			executeTriplePatternWithJoin(iterator.next(), queryResult);
			
		}
		queryExecutionCompleted = true;
		return queryResult;
	}


	/**
	 * Execute first triple pattern (without join)
	 */
	private static void executeTriplePattern(TriplePatternOfStarQuery triplePatternOfStarQuery) {

		Integer predicateID = triplePatternOfStarQuery.getPredicateID();
		Integer objectID = triplePatternOfStarQuery.getObjectID();

		// Check if property exists
		if(AllProperties.contains(predicateID)){
			queryResult = new ArrayList<Integer>(AllProperties.getListSubjectsByPredicateAndObject(predicateID, objectID));
			if(queryResult.isEmpty()){
				// result empty of the first pattern: no need to continue execution of the query
				queryExecutionCompleted = true;
			}
		} else {
			// property doesn't exist no need to continue the execution of the query
			queryExecutionCompleted = true;
		}
	}


	/**
	 * Execute a triple pattern with join
	 */
	private static void executeTriplePatternWithJoin(TriplePatternOfStarQuery triplePatternOfStarQuery, ArrayList<Integer> joinTable) {

		Integer predicateID = triplePatternOfStarQuery.getPredicateID();
		Integer objectID = triplePatternOfStarQuery.getObjectID();
		queryResult = new ArrayList<Integer>();

		// Check if property exists
		if(AllProperties.contains(predicateID)){
			for(Integer subjectID : joinTable){
				TreeSet<Integer> listObjects = 
						AllProperties.getListObjectsByPredicateAndSubject(predicateID, subjectID);
				if(listObjects.contains(objectID)){
					queryResult.add(subjectID);
				} 
			}
			if(queryResult.isEmpty()){
				// result empty of the first pattern: no need to continue execution of the query
				queryExecutionCompleted = true;
			}
		} else {
			queryExecutionCompleted = true;
		}
	}


	/**
	 * Execute Queries in files in directory
	 */
	public static void executeQueriesFromDirectoryPath (String queriesDirectoryPath){
		
		queriesResults = new ArrayList<ArrayList<Integer>>();
		// initialize current and next sparql query
		QueriesTriplePatternsList = new ArrayList<ArrayList<TriplePatternOfStarQuery>>();
		String currentSparqlQuery = "";
		String line = "";
		int posSelect = -1;
		Query query;

		// Execute Queries from each file in the directory
		
		try {
			

			File directory = new File(queriesDirectoryPath); 
			for (File file : directory.listFiles()) {
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

					// Read line by line the current file
					line = bufferedReader.readLine();
					while (line != null) {
						// Search for an occurrence of "SELECT"
						posSelect = line.indexOf("SELECT");
						if(posSelect >= 0){
							currentSparqlQuery = currentSparqlQuery + " " + line.substring(0, posSelect);

							// Execute Query. (PS: always true except for the first time)
							if(currentSparqlQuery.contains("SELECT")){
								query = QueryFactory.create(currentSparqlQuery);							
								preProcessQuery(query);
								QueriesTriplePatternsList.add(triplePatternsList);
							}

							//
							currentSparqlQuery = line.substring(posSelect);
						} else {
							currentSparqlQuery = currentSparqlQuery + " " + line;
						}
						line = bufferedReader.readLine();
					}
					bufferedReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Execute the last query
			if(currentSparqlQuery.contains("SELECT")){
				query = QueryFactory.create(currentSparqlQuery);							
				preProcessQuery(query);
				QueriesTriplePatternsList.add(triplePatternsList);
				
			}

			// close buffered Writer
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		long start = System.currentTimeMillis();
		for(ArrayList<TriplePatternOfStarQuery> queryAsTriplePattern: QueriesTriplePatternsList)
			queriesResults.add(executeQuery(queryAsTriplePattern));
	
		long end = System.currentTimeMillis();
		System.out.println(end-start);
		
		BufferedWriter bufferedWriterResult;
		try {
		bufferedWriterResult = new BufferedWriter(new FileWriter(RESULT_FILE_NAME));
		for(ArrayList<Integer> result: queriesResults)
			writeResult(bufferedWriterResult, result);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}


	/**
	 * Write Query Result
	 */
	private static void writeResult(BufferedWriter bufferedWriter, ArrayList<Integer> result) throws IOException {
		bufferedWriter.write("---------------------------------------------------------------");
		bufferedWriter.newLine();
		bufferedWriter.write("| " + subjectVariableName + " |");
		bufferedWriter.newLine();
		bufferedWriter.write("===============================================================");
		bufferedWriter.newLine();
		for(int subjectID : result){
			bufferedWriter.write(Dictionary.getInstance().getResource(subjectID));
			bufferedWriter.newLine();
		}
		bufferedWriter.write("---------------------------------------------------------------");
		bufferedWriter.newLine();
	}


	/**
	 * Display Query Result
	 */
	

	public static void displayResult(){
		Dictionary dictionary = Dictionary.getInstance();
		System.out.println("---------------------------------------------------------------");
		System.out.println("| " + subjectVariableName + " |");
		System.out.println("===============================================================");
		for(int subjectID : queryResult){
			System.out.println(dictionary.getResource(subjectID));
		}
		System.out.println("---------------------------------------------------------------");
	}
}
