package com.rdfengine.models;

import java.util.TreeSet;

public abstract class PropertyTable {

	/*
	 * Add Subject and Object
	 */
	public abstract void addSubjectAndObjectToPropertyTable(Integer subjectID, Integer objectID);
	

	/**
	 * Get list Objects by subject
	 * @param subjectID
	 * @return list of objects by subject. The list can be empty if no such object exists
	 */
	public abstract TreeSet<Integer> getListObjectsBySubject(Integer subjectID);


	/**
	 * Get list Subjects by object
	 * @param objectID
	 * @return list of subjects by object. The list can be empty if no such subbject exists
	 */
	public abstract TreeSet<Integer> getListSubjectsByObject(Integer objectID);

	
	/**
	 * 
	 * @return
	 */
	public abstract int getNumberOfOcurrences();
}
