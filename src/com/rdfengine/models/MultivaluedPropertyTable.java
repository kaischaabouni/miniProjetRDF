package com.rdfengine.models;

import java.util.HashMap;
import java.util.TreeSet;

public class MultivaluedPropertyTable extends PropertyTable {

	private HashMap<Integer, TreeSet<Integer>> subjectToObjects;
	private HashMap<Integer, TreeSet<Integer>> objectToSubjects;
	
	
	public MultivaluedPropertyTable() {
		super();
		subjectToObjects = new HashMap<Integer, TreeSet<Integer>>();
		objectToSubjects = new HashMap<Integer, TreeSet<Integer>>();
	}

	@Override
	public void addSubjectAndObjectToPropertyTable(Integer subjectID, Integer objectID){

		if(! subjectToObjects.containsKey(subjectID)){
			// add the subject and a single Object treeSet to subjectToObjects
			TreeSet<Integer> singleObjectTS = new TreeSet<Integer>();
			singleObjectTS.add(objectID);
			subjectToObjects.put(subjectID, singleObjectTS);
			
			// 
			if(! objectToSubjects.containsKey(objectID)){
				// add object and a single Subject treeSet to objectsToSubjects
				TreeSet<Integer> singleSubjectTS = new TreeSet<Integer>();
				singleSubjectTS.add(subjectID);
				objectToSubjects.put(objectID, singleSubjectTS);
			} else {
				// objectToSubjects contains object; add subject to the treeSetSubjects
				TreeSet<Integer> treeSetSubjects = objectToSubjects.get(objectID);
				treeSetSubjects.add(subjectID);
			}
		} else {
			
			//subjectToObjects contains the subject
			TreeSet<Integer> treeSetObjects = subjectToObjects.get(subjectID);
			if(! treeSetObjects.contains(objectID)){
				
				// subjectToObjects doesn't contain the object
				
				treeSetObjects.add(objectID);
				
				if(! objectToSubjects.containsKey(objectID)){
					// objectToSubjects doesn't contain the object
					// add the object and a single Subject treeSet to objectsToSubjects
					TreeSet<Integer> singleSubjectTS = new TreeSet<Integer>();
					singleSubjectTS.add(subjectID);
					objectToSubjects.put(objectID, singleSubjectTS);
				} else {
					// objectToSubjects contain the object
					// subjectToObjects doesn't contain
					// hence it is impossible that objectToSubjects contains the subject
					TreeSet<Integer> treeSetSubjects = objectToSubjects.get(objectID);
					treeSetSubjects.add(subjectID);
				}
			}
		}
	}



	public String toString()
	{
		return "subjectsToObjects: " + subjectToObjects + "\nobjectsToSubjects: " + objectToSubjects;
	}
	
}
