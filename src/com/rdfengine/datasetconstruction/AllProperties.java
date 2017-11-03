package com.rdfengine.datasetconstruction;

import java.util.HashMap;

import com.rdfengine.models.MultivaluedPropertyTable;
import com.rdfengine.models.PropertyTable;

public abstract class AllProperties {
	
	/*
	 * All properties
	 */
	private static HashMap<Integer, PropertyTable> allProperties= new HashMap<Integer, PropertyTable>();
	
	/*
	 * Add triple
	 */
	public static void addTriple(Integer subject, Integer predicate, Integer object){
		PropertyTable propertyTable;
		
		// create new propertyTable or search the propertyTable if already added
		if (!allProperties.containsKey(predicate)){ ///######if doesn"t exist in dictionary then it's new
			propertyTable= new MultivaluedPropertyTable();
			allProperties.put(predicate, propertyTable);
		} else {
			propertyTable = allProperties.get(predicate);
		}
		
		// add Subject and Object to propertyTable
		propertyTable.addTriplet(subject, object);
	}	
}
