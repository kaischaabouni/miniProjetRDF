package com.rdfengine.app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.rdfengine.loading.Loader;
import com.rdfengine.query.QueryManager;

public class RDFEngine {

	private static final String EXECUTION_TIME = "executiontime.csv";

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

		/*
		 * Write Time result
		 */
		long endQueryTime = System.currentTimeMillis();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(EXECUTION_TIME))) {
			bw.write("Loading Time (ms), Reading and Executing Queries Time (ms)");
			bw.newLine();
			bw.write((endLoadTime - startTime) + ", " + (endQueryTime - endLoadTime));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
