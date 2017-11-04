package com.rdfengine.loading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

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

	/*
	 * Check if AllProperties conatins property
	 */
	public static boolean contains(Integer predicateID) {
		return allProperties.containsKey(predicateID);
	}


	/**
	 * 
	 * @param predicateID
	 * @param subjectID
	 * @return list of objects corresponding to subjectID in property table of predicateID
	 */
	public static TreeSet<Integer> getListObjectsByPredicateAndSubject(Integer predicateID, Integer subjectID) {
		return allProperties.get(predicateID).getListObjectsBySubject(subjectID);
	}

	
	/**
	 * 
	 * @param predicateID
	 * @param objectID
	 * @return list of subjects corresponding to objectID in property table of predicateID
	 */
	public static TreeSet<Integer> getListSubjectsByPredicateAndObject(Integer predicateID, Integer objectID) {
		return allProperties.get(predicateID).getListSubjectsByObject(objectID);
	}
}
