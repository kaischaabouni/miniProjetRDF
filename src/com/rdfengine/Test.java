package com.rdfengine;

import com.rdfengine.datasetconstruction.Loader;
import com.rdfengine.datasetconstruction.Triplets;
import com.rdfengine.models.Dictionary;

public class Test {

	public static void main(String[] args) {
		String filePath = "assets/datasets/500K.rdf";
		Loader.loadData(filePath);
		Dictionary.getInstance().showAll(); 
		//System.out.println(Triplets.getTriplets());
	}

}
