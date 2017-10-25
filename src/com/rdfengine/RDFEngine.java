package com.rdfengine;

import com.rdfengine.datasetconstruction.Loader;
import com.rdfengine.models.RDFDictionary;

public class RDFEngine {




	public static void main(String[] args) {

		/**
		 * 
		 * For command line run, specify 2 arguments:
		 * - the interrogated file
		 * - the directory containing a set of queries
		 * 
		 * Example:
		 * java RDFEngine args[0]:<doc.rdf> args[1]:<directory of queries>
		 * 
		 */

		// Add data to Dictionary from specified file
		String filePath = "assets/datasets/500K.rdf";  
		if(args.length > 0){
			filePath = args[0];
		}
		Loader.loadData(filePath);

		// Show all Resources for test
		RDFDictionary.getInstance().showAll(); 
		
		// Execute University Queries


	}

}
