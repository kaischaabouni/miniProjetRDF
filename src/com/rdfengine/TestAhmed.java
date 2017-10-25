package com.rdfengine;

import com.rdfengine.datasetconstruction.Loader;
import com.rdfengine.datasetconstruction.Triplets;
import com.rdfengine.models.RDFDictionary;

public class TestAhmed {

	public static void main(String[] args) {
		String filePath = "assets/datasets/500K.rdf";
		Loader.loadData(filePath);
		RDFDictionary.getInstance().showAll(); 
		System.out.println(Triplets.getTriplets());
	}

}
