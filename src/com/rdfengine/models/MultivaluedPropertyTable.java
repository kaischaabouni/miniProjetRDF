package com.rdfengine.models;

import java.util.HashMap;
import java.util.TreeSet;

public class MultivaluedPropertyTable extends PropertyTable {

	private HashMap<Integer, TreeSet<Integer>> subjectToObjects;
	private HashMap<Integer, TreeSet<Integer>> objectsToSubjects;
	
	
	public MultivaluedPropertyTable() {
		super();
		subjectToObjects = new HashMap<Integer, TreeSet<Integer>>();
		objectsToSubjects = new HashMap<Integer, TreeSet<Integer>>();
	}

	public void addSubjectAndObject(Integer subject, Integer object){

		if(subjectToObjects.containsKey(subject)){
			addObjectIfNotAdded(subject, object);
			/*
			TreeSet<Integer> treeSetObjects = subjectToObjects.get(subject);
			if(!treeSetObjects.contains(object)){
				// add object to subjectToObjects
				treeSetObjects.add(object);
				
				// add object to objectsToSubjects
				//objectsToSubjects.put(object, treeSetSubjects);

			}
			*/
		} else {
			//subjectToObjects.put(key, value)
		}
				
	}

	private void addObjectIfNotAdded(Integer subject, Integer object) {
		TreeSet<Integer> treeSetObjects = subjectToObjects.get(subject);
		if(!treeSetObjects.contains(object)){
			// add object to subjectToObjects
			treeSetObjects.add(object);
			
			// add object to objectsToSubjects
			//objectsToSubjects.put(object, treeSetSubjects);

		}
	}




	
}
