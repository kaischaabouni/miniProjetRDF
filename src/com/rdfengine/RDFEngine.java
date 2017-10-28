package com.rdfengine;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
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
		String q = "SELECT ?v0 WHERE { "
			+ "?v0 <http://purl.org/dc/terms/Location> <http://db.uwaterloo.ca/~galuc/wsdbm/City62> . "
			+ "	?v0 <http://schema.org/nationality> <http://db.uwaterloo.ca/~galuc/wsdbm/Country29> . "
			+ "	?v0 <http://db.uwaterloo.ca/~galuc/wsdbm/gender> <http://db.uwaterloo.ca/~galuc/wsdbm/Gender1> . "
			+ "	?v0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://db.uwaterloo.ca/~galuc/wsdbm/Role2> .} ";

		Query query = QueryFactory.create(q);
		QueryManager.executeQuery(query);
		long endQueryTime = System.currentTimeMillis();
		
		System.out.println("Loading Dataset Time : " + ((endLoadTime - startTime) / 1000) + " s");
		System.out.println("Query and Display Time : " + ((endQueryTime - endLoadTime) / 1000) + " s");
	}

}
