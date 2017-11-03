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
	public void addTripletToPropertyTable(Integer subject, Integer object){

		//add in subjectToObjects
		if(subjectToObjects.containsKey(subject)){
//			addObjectIfNotAdded(subject, object);
			
			TreeSet<Integer> treeSetObjects = subjectToObjects.get(subject);
			if(!treeSetObjects.contains(object))//add object to the treeset
				treeSetObjects.add(object);

			else
				return; // if the triplet already exists we quit
			
			
		} else {
			TreeSet<Integer> singleObjectTS = new TreeSet<Integer>();
			singleObjectTS.add(object);
			subjectToObjects.put(subject, singleObjectTS);
		}
		
		
		//add in objectToSubjects
		if(objectToSubjects.containsKey(object))
		{
			
			TreeSet<Integer> treeSetObjects = objectToSubjects.get(object);
			//Certainly doesn't contain subject(otherwise we would've quit procedure earlier)
			treeSetObjects.add(subject);
		} 
		else 
		{
			TreeSet<Integer> singleSubjectTS = new TreeSet<Integer>();
			singleSubjectTS.add(subject);
			objectToSubjects.put(object, singleSubjectTS);
		}
		
	}



	public String toString()
	{
		return "subjectsToObjects: \n" + subjectToObjects + "\nobjectsToSubjects: " + objectToSubjects;
	}
	
}
