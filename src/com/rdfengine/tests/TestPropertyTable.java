package com.rdfengine.tests;

import com.rdfengine.models.MultivaluedPropertyTable;

public class TestPropertyTable {

	public static void main(String[] args) {
//		String filePath = "assets/datasets/500K.rdf";
//		Loader.loadData(filePath);
//		Dictionary.getInstance().showAll(); 
		MultivaluedPropertyTable p = new MultivaluedPropertyTable();
		p.addSubjectAndObjectToPropertyTable(1, 2);
		p.addSubjectAndObjectToPropertyTable(1, 3);
		p.addSubjectAndObjectToPropertyTable(2, 4);
		p.addSubjectAndObjectToPropertyTable(2, 4);
		p.addSubjectAndObjectToPropertyTable(5, 2);
		System.out.println(p);
		//System.out.println(Triplets.getTriplets());
	}

}
