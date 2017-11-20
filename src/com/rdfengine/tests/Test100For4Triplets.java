package com.rdfengine.tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.hp.hpl.jena.query.Query;
import com.rdfengine.loading.Loader;
import com.rdfengine.query.QueryManager;

public class Test100For4Triplets {

	public static final String EXECTIMEFILEDEFAULTPATH = "eval100For4Triplets";
	public static int totalExecutionTime = 0;
	public static int totalLoadingTime = 0;
	public static int totalCreatingJenaQueriesTime = 0;
	public static int totalPreprocessingTime = 0;
	/*
	 * Main Program
	 */
	public static void main(String[] args) {

		/*
		 *  Dataset filePath and queriesDirecoryPath
		 */
		for(int i=1; i<=10; i++)
		{
		String filePath = "assets/datasets/100K.rdf";  
		String queriesDirecoryPath = "assets/queries by triplets/1/";  
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
		totalExecutionTime+= QueryManager.executionTime;
		totalLoadingTime+= Loader.loadingDataTime;
		totalCreatingJenaQueriesTime+= QueryManager.creatingJenaQueries;
		totalPreprocessingTime+= QueryManager.preProcessingTime;
if(i==10)
{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(EXECTIMEFILEDEFAULTPATH))) {
			
			
		
			bw.write("Number of queries: " + QueryManager.numberOfQueries);
			bw.newLine();
			bw.write("Loading + Parsing Time : " + totalLoadingTime/10 + " ms");
			bw.newLine();
			bw.write("Creating Jena Query Instances Time : " + totalCreatingJenaQueriesTime/10 + " ms");
			bw.newLine();
			bw.write("Pre-processing Time : " + totalPreprocessingTime/10 + " ms");
			bw.newLine();
			bw.write("Execution Time : " + totalExecutionTime/10 + " ms");
			System.out.println(EXECTIMEFILEDEFAULTPATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
}
		
		}
	}

}
