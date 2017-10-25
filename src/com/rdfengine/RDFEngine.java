package com.rdfengine;

import com.rdfengine.datasetconstruction.Loader;
import com.rdfengine.datasetconstruction.Triplets;
import com.rdfengine.models.RDFDictionary;

public class RDFEngine {




	public static void main(String[] args) {

		/*
		 *  Default filePath and queriesDirecoryPath if RDFEngine exectuted without arguments
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
		System.out.println("Loading Time : " + ((endLoadTime - startTime) / 1000) + " s");

		/*
		 *  Show all Resources for test
		 */
		//RDFDictionary.getInstance().showAll(); 
		//System.out.println(Triplets.getTriplets());

		// Execute University Queries


	}

}
