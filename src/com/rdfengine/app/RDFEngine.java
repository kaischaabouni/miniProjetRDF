package com.rdfengine.app;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.rdfengine.loading.Loader;
import com.rdfengine.query.QueryManager;

public class RDFEngine {

	/*
	 * Main Program
	 */
	public static void main(String[] args) {

		/*
		 *  Dataset filePath and queriesDirecoryPath
		 */
		String filePath = "assets/datasets/500K.rdf";  
		String queriesDirecoryPath = "assets/queries/";  
		if(args.length == 2){
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
		 *  Execute Queries
		 */
		QueryManager.executeQueriesFromDirectoryPath(queriesDirecoryPath);
//		String q = "SELECT ?v0 WHERE {"
//				+ "?v0 <http://purl.org/dc/terms/Location> <http://db.uwaterloo.ca/~galuc/wsdbm/City0> . "
//				+ "	?v0 <http://schema.org/nationality> <http://db.uwaterloo.ca/~galuc/wsdbm/Country3> . "
//				+ "	?v0 <http://db.uwaterloo.ca/~galuc/wsdbm/gender> <http://db.uwaterloo.ca/~galuc/wsdbm/Gender1> . }";
//
//		Query query = QueryFactory.create(q);
//		QueryManager.preProcessQuery(query);
//		QueryManager.executeQuery();
//		QueryManager.displayResult();

		long endQueryTime = System.currentTimeMillis();
		System.out.println("Loading Dataset Time : " + ((endLoadTime - startTime) / 1000) + " s");
		System.out.println("Query and Display Time : " + ((endQueryTime - endLoadTime) / 1000) + " s");
	}

}
