package com.rdfengine;

import com.rdfengine.datasetconstruction.Dictionary;
import com.rdfengine.datasetconstruction.Loader;
import com.rdfengine.models.MultivaluedPropertyTable;
import com.rdfengine.triplets.Triplets;

public class Test {

	public static void main(String[] args) {
//		String filePath = "assets/datasets/500K.rdf";
//		Loader.loadData(filePath);
//		Dictionary.getInstance().showAll(); 
		MultivaluedPropertyTable p = new MultivaluedPropertyTable();
		p.addTriplet(1, 2);
		p.addTriplet(1, 3);
		p.addTriplet(2, 4);
		p.addTriplet(2, 4);
		p.addTriplet(5, 2);
		System.out.println(p);
		//System.out.println(Triplets.getTriplets());
	}

}
