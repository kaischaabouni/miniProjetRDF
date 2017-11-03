package com.rdfengine.loading;

import java.util.ArrayList;


public abstract class AllSubjects {
	
	/*
	 * All subjects
	 */
	private static ArrayList<Integer> allSubjects= new ArrayList<Integer>() ;

	/*
	 * Add new subject
	 */
	public static void addSubject(Integer subjectID) {
		allSubjects.add(subjectID);
	}	
}
