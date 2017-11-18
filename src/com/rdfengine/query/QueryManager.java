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
import com.rdfengine.models.TriplePatternOfStarQuery;

public class QueryManager {

	private static final String RESULT_FILE_NAME = "result.csv";

	/*
	 * Query Status Information
	 */
	//private static ArrayList<Integer> queryResult = null;
	//## private static ArrayList<ArrayList<Integer>> queriesResults;

	//private static ArrayList<Boolean> queriesExecutionCompleted ;

	//private static ArrayList<TriplePatternOfStarQuery> triplePatternsList;
	//private static ArrayList<ArrayList<TriplePatternOfStarQuery>> queriesTriplePatternsList;

	//private static ArrayList<String> queriesSubjectVariableName;

	private static ArrayList<QueryStatus> queriesStatus;



	/**
	 * Execute Queries in files in directory
	 */
	public static void executeQueriesFromDirectoryPath (String queriesDirectoryPath){

		// initialize queries status
		queriesStatus = new ArrayList<QueryStatus>();

		//## initialize queriesResults
		//## queriesResults = new ArrayList<ArrayList<Integer>>();

		// initialize current and next sparql query
		//## queriesTriplePatternsList = new ArrayList<ArrayList<TriplePatternOfStarQuery>>();
		String currentSparqlQuery = "";
		String line = "";
		int posSelect = -1;
		Query query;

		// Pre process Queries from the files in the directory

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
								//## queriesTriplePatternsList.add(triplePatternsList);
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
				//## queriesTriplePatternsList.add(triplePatternsList);

			}

			// close buffered Writer

		} catch (Exception e1) {
			e1.printStackTrace();
		}


		// Executing queries
		long start = System.currentTimeMillis();
		for(QueryStatus queryStatus: queriesStatus){
			executeQuery(queryStatus);
			//## queriesResults.add(executeQuery(queryAsTriplePattern));
		}

		long end = System.currentTimeMillis();
		System.out.println(end-start);

		BufferedWriter bufferedWriterResult;
		try {
			bufferedWriterResult = new BufferedWriter(new FileWriter(RESULT_FILE_NAME));
			for(QueryStatus queryStatus: queriesStatus){
				writeResult(bufferedWriterResult, queryStatus);
			}
			bufferedWriterResult.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Pre-Process Query extract triple patterns
	 */
	private static void preProcessQuery(Query query){

		//System.out.println(query.serialize());
		
		// initialize Query Status
		QueryStatus queryStatus = new QueryStatus();
		ArrayList<TriplePatternOfStarQuery> triplePatternsList = new ArrayList<TriplePatternOfStarQuery>();


		//## queriesSubjectVariableName = null;
		//## queriesExecutionCompleted = false;

		// Parsing the query and executing triple patterns
		ElementWalker.walk(query.getQueryPattern(),new ElementVisitorBase(){
			@Override public void visit(ElementPathBlock elementPathBlock){
				ListIterator<TriplePath> listIterator=elementPathBlock.getPattern().iterator();
				while (!queryStatus.isQueryExecutionCompleted() && listIterator.hasNext()) {
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
					//## queriesSubjectVariableName = triplePath.getSubject().getName();
					queryStatus.setQueriesSubjectVariableName(triplePath.getSubject().getName());
					
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
						queryStatus.setQueryExecutionCompleted(true);
					}
				}
			}
		});
		
		// set triplePatternsList
		queryStatus.setQueriesTriplePatternsList(triplePatternsList);
		
		// add queryStatus to the list queries Status
		queriesStatus.add(queryStatus);
	}


	/**
	 * Execute Query 
	 */
	private static void executeQuery(QueryStatus queryStatus) {

		// initialize queryResult
		ArrayList<Integer> queryResult = new ArrayList<Integer>();

		// Iterator for triplePatternsList
		Iterator<TriplePatternOfStarQuery> iterator = queryStatus.getQueriesTriplePatternsList().iterator();
		//## queriesExecutionCompleted = false;

		// Execute First Triple Pattern
		if(!queryStatus.isQueryExecutionCompleted() && iterator.hasNext()){
			executeTriplePattern(iterator.next(), queryResult, queryStatus);
			//System.out.println("ap trace1 : ");
//			for(int id: queryResult)
//				System.out.println(Dictionary.getInstance().getResource(id));
		}

		// Execute the rest of triple patterns with join with (temporary result) if the query is not completed
		while (!queryStatus.isQueryExecutionCompleted() && iterator.hasNext()) {
			queryResult = executeTriplePatternWithJoin(iterator.next(), queryResult, queryStatus);
			//System.out.println("ap trace2 : ");
//			for(int id: queryResult)
//				System.out.println(Dictionary.getInstance().getResource(id));
		}
		queryStatus.setQueryExecutionCompleted(true);
		queryStatus.setQueryResult(queryResult);
	}


	/**
	 * Execute first triple pattern (without join)
	 * @param triplePatternOfStarQuery
	 * @param queryResult
	 * @param queryStatus 
	 */
	private static void executeTriplePattern(TriplePatternOfStarQuery triplePatternOfStarQuery, ArrayList<Integer> queryResult, QueryStatus queryStatus) {

		Integer predicateID = triplePatternOfStarQuery.getPredicateID();
		Integer objectID = triplePatternOfStarQuery.getObjectID();
		
		// Check if property exists
		if(AllProperties.contains(predicateID)){
			queryResult.addAll(AllProperties.getListSubjectsByPredicateAndObject(predicateID, objectID));
			if(queryResult.isEmpty()){
				// result empty of the first pattern: no need to continue execution of the query
				queryStatus.setQueryExecutionCompleted(true);
			}
		} else {
			// property doesn't exist no need to continue the execution of the query
			queryStatus.setQueryExecutionCompleted(true);
		}
	}


	/**
	 * Execute a triple pattern with join
	 * @param queryStatus 
	 * @return 
	 */
	private static ArrayList<Integer> executeTriplePatternWithJoin(TriplePatternOfStarQuery triplePatternOfStarQuery, ArrayList<Integer> queryResult, QueryStatus queryStatus) {

		Integer predicateID = triplePatternOfStarQuery.getPredicateID();
		Integer objectID = triplePatternOfStarQuery.getObjectID();
				
		ArrayList<Integer> newQueryResult = new ArrayList<Integer>();

		// Check if property exists
		if(AllProperties.contains(predicateID)){
			for(Integer subjectID : queryResult){
				TreeSet<Integer> listObjects = 
						AllProperties.getListObjectsByPredicateAndSubject(predicateID, subjectID);
				if(listObjects.contains(objectID)){
					newQueryResult.add(subjectID);
				} 
			}
			if(newQueryResult.isEmpty()){
				// result empty of the first pattern: no need to continue execution of the query
				queryStatus.setQueryExecutionCompleted(true);
			}
		} else {
			queryStatus.setQueryExecutionCompleted(true);
		}
		
		// update to the new list
		return newQueryResult;
	}



	/**
	 * Write Query Result
	 */
	private static void writeResult(BufferedWriter bufferedWriter, QueryStatus queryStatus) throws IOException {
		bufferedWriter.write("---------------------------------------------------------------");
		bufferedWriter.newLine();
		bufferedWriter.write("| " + queryStatus.getQueriesSubjectVariableName() + " |");
		bufferedWriter.newLine();
		bufferedWriter.write("===============================================================");
		bufferedWriter.newLine();
		for(int subjectID : queryStatus.getQueryResult()){
			bufferedWriter.write(Dictionary.getInstance().getResource(subjectID));
			bufferedWriter.newLine();
		}
		bufferedWriter.write("---------------------------------------------------------------");
		bufferedWriter.newLine();
	}
}
