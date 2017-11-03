package com.rdfengine.loading;

import java.util.HashMap;

import com.rdfengine.models.MultivaluedPropertyTable;
import com.rdfengine.models.PropertyTable;

public abstract class AllProperties {
	
	/*
	 * All properties
	 */
	private static HashMap<Integer, PropertyTable> allProperties= new HashMap<Integer, PropertyTable>();

	/*
	 * Add new propertyTable
	 */
	public static void addProperty(Integer predicateID) {
		PropertyTable propertyTable = new MultivaluedPropertyTable();
		allProperties.put(predicateID, propertyTable);
	}	
	
	/*
	 * Add Subject And Object To Existing Property
	 */
	public static void addSubjectAndObjectToExistingProperty(Integer subjectID, Integer predicateID, Integer objectID){
		PropertyTable propertyTable = allProperties.get(predicateID);
		propertyTable.addSubjectAndObjectToPropertyTable(subjectID, objectID);
	}

	public static void display() {
		System.out.println(allProperties.toString());
	}
}
