package com.rdfengine;

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

		// Add data to Dictionary from 
		RDFDictionary dictionary = RDFDictionary.getInstance();
		String filename = "University0_0.nt";
		dictionary.addRDFValuesFromFile("assets/datasets/" + filename);
		
		// Execute University Queries
		
		
	}

}
