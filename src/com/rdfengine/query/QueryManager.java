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
	private static ArrayList<Integer> queryResult = null;
	private static boolean queryExecutionCompleted = false;
	private static ArrayList<TriplePatternOfStarQuery> triplePatternsList = null;
	private static String subjectVariableName = null;

	
	
	/**
	 * Pre-Process Query extract triple patterns
	 */
	public static void preProcessQuery(Query query){
		// initialize 
		triplePatternsList = new ArrayList<TriplePatternOfStarQuery>();
		subjectVariableName = null;
		queryExecutionCompleted = false;

		// Parsing the query and executing triple patterns
		ElementWalker.walk(query.getQueryPattern(),new ElementVisitorBase(){
			@Override public void visit(ElementPathBlock elementPathBlock){
				ListIterator<TriplePath> listIterator=elementPathBlock.getPattern().iterator();
				while (listIterator.hasNext()) {
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
					Integer predicateID = Dictionary.getInstance().getId(predicate);
					Integer objectID = Dictionary.getInstance().getId(object);
					TriplePatternOfStarQuery triplePatternOfStarQuery = 
							new TriplePatternOfStarQuery(predicateID, objectID);

					// add TripplePatternOfStarQuery to triplePatternsList
					triplePatternsList.add(triplePatternOfStarQuery);
				}
			}
		});
	}


	/**
	 * Execute Query 
	 */
	public static void executeQuery() {

		// initialize
		queryResult = new ArrayList<Integer>();

		// Iterator for triplePatternsList
		Iterator<TriplePatternOfStarQuery> iterator = triplePatternsList.iterator();

		// Execute First Triple Pattern
		if(iterator.hasNext()){
			executeTriplePattern(iterator.next());
		}

		// Execute the rest of triple patterns with join with (temporary result) if the query is not completed
		while (!queryExecutionCompleted && iterator.hasNext()) {
			executeTriplePatternWithJoin(iterator.next(), queryResult);
		}
		queryExecutionCompleted = true;
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

		// Execute Queries from each file in the directory
		File directory = new File(queriesDirectoryPath); 
		for (File file : directory.listFiles()) {
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(RESULT_FILE_NAME));
				String line = null;
				String sparqlQuery = "";
				Query query;
				while ((line = bufferedReader.readLine()) != null) {
					if(! line.isEmpty()){
						sparqlQuery = sparqlQuery + line;
					} else {
						if(! sparqlQuery.isEmpty()){
							query = QueryFactory.create(sparqlQuery);
							sparqlQuery = "";

							// Pre-Process Query
							preProcessQuery(query);

							// Execute Query
							executeQuery();

							// Write Result in RESULT_FILE_NAME
							writeResult(bufferedWriter);

							// Display result
							//displayResult();
						}
					}
				}
				bufferedReader.close();
				bufferedWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * Write Query Result
	 */
	private static void writeResult(BufferedWriter bufferedWriter) throws IOException {
		bufferedWriter.write("---------------------------------------------------------------");
		bufferedWriter.newLine();
		bufferedWriter.write("| " + subjectVariableName + " |");
		bufferedWriter.newLine();
		bufferedWriter.write("===============================================================");
		bufferedWriter.newLine();
		for(int subjectID : queryResult){
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
