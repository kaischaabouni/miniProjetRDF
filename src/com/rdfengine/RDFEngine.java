package com.rdfengine;

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
		long endQueryTime = System.currentTimeMillis();
		System.out.println("Loading Dataset Time : " + ((endLoadTime - startTime) / 1000) + " s");
		System.out.println("Query and Display Time : " + ((endQueryTime - endLoadTime) / 1000) + " s");
	}

}
