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

		//add in subjectToObjects
		if(subjectToObjects.containsKey(subjectID)){
//			addObjectIfNotAdded(subject, object);
			
			TreeSet<Integer> treeSetObjects = subjectToObjects.get(subjectID);
			if(!treeSetObjects.contains(objectID))//add object to the treeset
				treeSetObjects.add(objectID);

			else
				return; // if the triplet already exists we quit
			
			
		} else {
			TreeSet<Integer> singleObjectTS = new TreeSet<Integer>();
			singleObjectTS.add(objectID);
			subjectToObjects.put(subjectID, singleObjectTS);
		}
		
		
		//add in objectToSubjects
		if(objectToSubjects.containsKey(objectID))
		{
			
			TreeSet<Integer> treeSetObjects = objectToSubjects.get(objectID);
			//Certainly doesn't contain subject(otherwise we would've quit procedure earlier)
			treeSetObjects.add(subjectID);
		} 
		else 
		{
			TreeSet<Integer> singleSubjectTS = new TreeSet<Integer>();
			singleSubjectTS.add(subjectID);
			objectToSubjects.put(objectID, singleSubjectTS);
		}
		
	}



	public String toString()
	{
		return "subjectsToObjects: \n" + subjectToObjects + "\nobjectsToSubjects: " + objectToSubjects;
	}
	
}
