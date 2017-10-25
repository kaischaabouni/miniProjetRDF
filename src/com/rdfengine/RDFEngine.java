package com.rdfengine;

import com.rdfengine.datasetconstruction.Loader;
import com.rdfengine.models.RDFDictionary;

public class RDFEngine {




	public static void main(String[] args) {

		// Default filePath and queriesDirecoryPath if RDFEngine exectuted without arguments
		String filePath = "assets/datasets/500K.rdf";  
		String queriesDirecoryPath = "assets/queries/";  
		
		// Executing with Command Line Arguments 
		if(args.length > 0){
			filePath = args[0];
			queriesDirecoryPath = args[1];
		}
		
		// Load data to Dictionary from specified file
		Loader.loadData(filePath);

		// Show all Resources for test
		RDFDictionary.getInstance().showAll(); 
		
		// Execute University Queries
		

	}

}
