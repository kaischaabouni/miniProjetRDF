package com.rdfengine.app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.rdfengine.loading.Loader;
import com.rdfengine.query.QueryManager;

public class RDFEngine {

	private static final String EXECUTION_TIME = "executiontime.csv";
	public static String execTimeFile;

	/*
	 * Main Program
	 */
	public static void main(String[] args) {

		/*
		 *  Dataset filePath and queriesDirecoryPath
		 */
		for(int i=1; i<=10; i++)
		{
		String filePath = "assets/datasets/500K.rdf";  
		String queriesDirecoryPath = "assets/queries/";  
		if(args.length == 2){
			filePath = args[0];
			queriesDirecoryPath = args[1];
		}

		/*
		 *  Load data to Dictionary from specified file
		 */
		Loader.loadData(filePath);

		/*
		 *  Execute Queries
		 */
		QueryManager.executeQueriesFromDirectoryPath(queriesDirecoryPath);

		/*
		 * Write Time result
		 */
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(EXECUTION_TIME))) {
			bw.write("Loading + Parsing Time : " + Loader.loadingDataTime + " ms");
			bw.newLine();
			bw.write("Creating Jena Query Instances Time : " + QueryManager.creatingJenaQueries + " ms");
			bw.newLine();
			bw.write("Execution Time(+Pre-processing) : " + QueryManager.executionTime + QueryManager.preProcessingTime + " ms");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		}
	}

}
