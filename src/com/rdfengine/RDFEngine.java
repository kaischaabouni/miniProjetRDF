package com.rdfengine;

import com.rdfengine.datasetconstruction.Loader;
import com.rdfengine.query.QueryManager;

public class RDFEngine {




	public static void main(String[] args) {

		/*
		 *  Default filePath and queriesDirecoryPath if RDFEngine executed without arguments
		 */
		String filePath = "assets/datasets/500K.rdf";  
		String queriesDirecoryPath = "assets/queries/";  

		/*
		 *  Executing with Command Line Arguments 
		 */
		if(args.length > 0){
			filePath = args[0];
			queriesDirecoryPath = args[1];
		}

		/*
		 *  Load data to Dictionary from specified file
		 */
		long startTime = System.currentTimeMillis();
		Loader.loadData(filePath);
		long endLoadTime = System.currentTimeMillis();

		/*
		 *  Show all Resources for test
		 */
		//RDFDictionary.getInstance().showAll(); 
		//System.out.println(Triplets.getTriplets());

		/*
		 *  Parse SPARQL query
		 */
		
		/*
		 *  Execute Queries: search for the result in columns
		 */
		// Suppose we have the query: SELECT ?v0 WHERE { ?v0 <http://schema.org/nationality> <http://db.uwaterloo.ca/~galuc/wsdbm/Country3> . } 
		// subject: variable
		// predicate: <http://schema.org/nationality>
		// object: <http://db.uwaterloo.ca/~galuc/wsdbm/Country3>
		QueryManager.executeQuery("Mock Query just for test");
		long endQueryTime = System.currentTimeMillis();
		
		System.out.println("Loading Dataset Time : " + ((endLoadTime - startTime) / 1000) + " s");
		System.out.println("Query and Display Time : " + ((endQueryTime - endLoadTime) / 1000) + " s");
	}

}
