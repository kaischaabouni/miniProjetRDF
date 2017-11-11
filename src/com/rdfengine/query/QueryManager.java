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
	//	private static final String EXECUTION_TIME = "executiontime.csv";

	/*
	 * Query Status Information
	 */
	private static ArrayList<Integer> queryResult = null;
	private static boolean queryExecutionCompleted = false;
	private static ArrayList<TriplePatternOfStarQuery> triplePatternsList = null;
	private static String subjectVariableName = null;
	//	private static long startReadingQueryTime = 0;
	//	private static long readingQueryTime = 0;
	//	private static long startExecutionTime = 0;
	//	private static long executionTime = 0;


	/**
	 * Pre-Process Query extract triple patterns
	 */
	public static void preProcessQuery(Query query){
		// initialize 
		triplePatternsList = new ArrayList<TriplePatternOfStarQuery>();
		subjectVariableName = null;
		queryExecutionCompleted = false;
		//		startReadingQueryTime = System.currentTimeMillis();

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

		// update Reading Query Time
		//		startExecutionTime = System.currentTimeMillis();
		//		readingQueryTime = readingQueryTime + startExecutionTime - startReadingQueryTime;
		//System.out.println("readingQueryTime : " + readingQueryTime);
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

		// update Execution Time
		//		executionTime = executionTime + System.currentTimeMillis() - startExecutionTime;
		//System.out.println("executionTime : " + executionTime);
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

		// initialize current and next sparql query
		String currentSparqlQuery = "";
		String line = "";
		int posSelect = -1;
		Query query;

		// Execute Queries from each file in the directory
		BufferedWriter bufferedWriterResult;
		try {
			bufferedWriterResult = new BufferedWriter(new FileWriter(RESULT_FILE_NAME));

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
								executeQuery();
								writeResult(bufferedWriterResult);
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
				executeQuery();
				writeResult(bufferedWriterResult);	
			}

			// close buffered Writer
			bufferedWriterResult.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		// Write Execution Time
		//		try (BufferedWriter bufferedWriterExecutionTime = new BufferedWriter(new FileWriter(EXECUTION_TIME))) {
		//			writeExecutionTime(bufferedWriterExecutionTime);
		//			bufferedWriterExecutionTime.close();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
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
	 * Write Execution Time
	 * @param bufferedWriterExecutionTime
	 * @throws IOException 
	 */
	//	private static void writeExecutionTime(BufferedWriter bufferedWriterExecutionTime) throws IOException {
	//		
	//		// Wite Execution Time
	//		bufferedWriterExecutionTime.write("Loading Time (ms), Reading Queries Time (ms), Execution Queries Time (ms)");
	//		bufferedWriterExecutionTime.newLine();
	//		bufferedWriterExecutionTime.write(Loader.getLoadingTime() + ", " + readingQueryTime + ", " + executionTime);
	//		bufferedWriterExecutionTime.newLine();
	//
	//		// Reinitialize to 0
	//		readingQueryTime = 0;
	//		executionTime = 0;
	//	}


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
